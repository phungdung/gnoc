package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.DataHistoryChange;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.*;
import com.viettel.gnoc.wo.dto.WoConfigPropertyDTO;
import com.viettel.gnoc.wo.repository.WoConfigPropertyRepository;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class WoConfigPropertyBusinessImpl implements WoConfigPropertyBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  private final static String Wo_Config_Property_EXPORT = "Wo_Config_Property_EXPORT";
  @Autowired
  protected WoConfigPropertyRepository woConfigPropertyRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;

  @Override
  public Datatable getListConfigPropertyDTO(WoConfigPropertyDTO configPropertyDTO) {
    log.debug("Request to getListConfigPropertyDTO : {}", configPropertyDTO);
    return woConfigPropertyRepository.getListConfigPropertyDTO(configPropertyDTO);
  }

  @Override
  public ResultInSideDto addConfigProperty(WoConfigPropertyDTO configPropertyDTO) {
    log.debug("Request to addConfigProperty : {}", configPropertyDTO);
    ResultInSideDto resultInSideDto;
    resultInSideDto = woConfigPropertyRepository.addConfigProperty(configPropertyDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        //Old Object History
        dataHistoryChange.setOldObject(new WoConfigPropertyDTO());
        //New Object History
        dataHistoryChange.setNewObject(configPropertyDTO);
        dataHistoryChange.setType("WO_CONFIG_PROPERTY");
        dataHistoryChange.setActionType("add");
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateConfigProperty(WoConfigPropertyDTO configPropertyDTO) {
    log.debug("Request to updateConfigProperty : {}", configPropertyDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
   try {
     WoConfigPropertyDTO oldHis = getDetail(configPropertyDTO.getKey());
     resultInSideDto = woConfigPropertyRepository.updateConfigProperty(configPropertyDTO);
     if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
       //Add history
       try {
         UserToken userToken = ticketProvider.getUserToken();
         List<String> keys = getAllKeysDTO();
         DataHistoryChange dataHistoryChange = new DataHistoryChange();
         //Old Object History
         dataHistoryChange.setOldObject(oldHis);
         //New Object History
         dataHistoryChange.setNewObject(configPropertyDTO);
         dataHistoryChange.setType("WO_CONFIG_PROPERTY");
         dataHistoryChange.setActionType("update");
         dataHistoryChange.setUserId(userToken.getUserID().toString());
         dataHistoryChange.setKeys(keys);
         commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
       } catch (Exception err) {
         log.error(err.getMessage());
       }
     }
   } catch (Exception error) {
     log.error(error.getMessage());
     resultInSideDto.setKey(Constants.RESULT.FAIL);
   }
    return  resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(String key) {
    log.debug("Request to delete : {}", key);
    ResultInSideDto resultInSideDto;
    WoConfigPropertyDTO oldHis = getDetail(key);
    resultInSideDto =  woConfigPropertyRepository.delete(key);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      //Add history
      try {
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        List<String> keys = getAllKeysDTO();
        UserToken userToken = ticketProvider.getUserToken();
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new WoConfigPropertyDTO());
        dataHistoryChange.setType("WO_CONFIG_PROPERTY");
        dataHistoryChange.setActionType("delete");
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public File exportData(WoConfigPropertyDTO configPropertyDTO) throws Exception {
    log.debug("Request to getListDataExport : {}", configPropertyDTO);
    return exportFileEx(woConfigPropertyRepository.getListDataExport(configPropertyDTO));
  }

  @Override
  public WoConfigPropertyDTO getDetail(String key) {
    log.debug("Request to getDetail : {}", key);
    return woConfigPropertyRepository.getDetail(key);
  }

  private File exportFileEx(List<WoConfigPropertyDTO> lstData) throws Exception {
    String title = I18n.getLanguage("WoConfigProperty.export.title");
    String fileNameOut = Wo_Config_Property_EXPORT;
    String subTitle = I18n
        .getLanguage("WoConfigProperty.export.exportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = readerHeaderSheet(
        "key", "value", "description");
    Map<String, String> filedSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstData,
        I18n.getLanguage("WoConfigProperty.export.title"),
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.WoConfigProperty",
        lstHeaderSheet,
        filedSplit,
        "",
        I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configFileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  private List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = WoConfigPropertyDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        return keys;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
