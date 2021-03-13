package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.TransitionStateConfigRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateUtil;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

/**
 * @author TienNV
 */

@Service
@Transactional
@Slf4j
public class TransitionStateConfigBusinessImpl extends BaseRepository implements
    TransitionStateConfigBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  TransitionStateConfigRepository transitionStateConfigRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;


  Map<String, String> mapProcess = new HashMap<>();

  @Override
  public List<TransitionStateConfigDTO> onSearch(TransitionStateConfigDTO dto) {
    List<TransitionStateConfigDTO> transitionStateConfigDTOList = transitionStateConfigRepository.onSearch(dto);
    return transitionStateConfigDTOList;
  }

  @Override
  public Datatable getListTransitionStateConfigDTO(
      TransitionStateConfigDTO transitionStateConfigDTO) {
    log.debug("Request to getListTransitionStateConfigDTO: {}", transitionStateConfigDTO);
    setMapProcess();
    Datatable datatable = transitionStateConfigRepository
        .getListTransitionStateConfigDTO(transitionStateConfigDTO);
    List<TransitionStateConfigDTO> list = (List<TransitionStateConfigDTO>) datatable.getData();
    convertProcessName(list);
    datatable.setData(list);
    return datatable;
  }

  @Override
  public ResultInSideDto insertTransitionStateConfig(
      TransitionStateConfigDTO transitionStateConfigDTO) {
    log.debug("Request to insertTransitionStateConfig: {}", transitionStateConfigDTO);
    ResultInSideDto resultInSideDto;
    resultInSideDto = transitionStateConfigRepository.insertTransitionStateConfig(transitionStateConfigDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        //Old Object History
        dataHistoryChange.setOldObject(new TransitionStateConfigDTO());
        //New Object History
        dataHistoryChange.setNewObject(transitionStateConfigDTO);
        dataHistoryChange.setType("UTILITY_STATUS_CONFIG");
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
  public ResultInSideDto updateTransitionStateConfig(
      TransitionStateConfigDTO transitionStateConfigDTO) {
    log.debug("Request to updateTransitionStateConfig: {}", transitionStateConfigDTO);
    ResultInSideDto resultInSideDto;
    TransitionStateConfigDTO oldHis = findTransitionStateConfigById(transitionStateConfigDTO.getId());
    resultInSideDto = transitionStateConfigRepository.updateTransitionStateConfig(transitionStateConfigDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(transitionStateConfigDTO.getId().toString());
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(transitionStateConfigDTO);
        dataHistoryChange.setType("UTILITY_STATUS_CONFIG");
        dataHistoryChange.setActionType("update");
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        List<String> keys = getAllKeysDTO();
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteTransitionStateConfig(Long id) {
    log.debug("Request to deleteTransitionStateConfig: {}", id);
    ResultInSideDto resultInSideDto;
    TransitionStateConfigDTO oldHis = findTransitionStateConfigById(id);
    resultInSideDto = transitionStateConfigRepository.deleteTransitionStateConfig(id);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(id.toString());
        List<String> keys = getAllKeysDTO();
        dataHistoryChange.setType("UTILITY_STATUS_CONFIG");
        dataHistoryChange.setActionType("delete");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new TransitionStateConfigDTO());
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
  public TransitionStateConfigDTO findTransitionStateConfigById(Long id) {
    log.debug("Request to findTransitionStateConfigById: {}", id);
    return transitionStateConfigRepository.findTransitionStateConfigById(id);
  }

  @Override
  public File exportData(TransitionStateConfigDTO transitionStateConfigDTO) throws Exception {
    log.debug("Request to exportData: {}", transitionStateConfigDTO);
    setMapProcess();
    List<TransitionStateConfigDTO> list = transitionStateConfigRepository
        .getListTransitionStateConfigExport(transitionStateConfigDTO);
    convertProcessName(list);
    String[] header = new String[]{"processName", "beginStateName", "endStateName", "description"};
    return handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(new Date()));
  }

  public void convertProcessName(List<TransitionStateConfigDTO> list) {
    for (TransitionStateConfigDTO dto : list) {
      for (Map.Entry<String, String> item : mapProcess.entrySet()) {
        if (String.valueOf(dto.getProcess()).equals(item.getKey())) {
          dto.setProcessName(item.getValue());
          break;
        }
      }
    }
  }

  private File handleFileExport(List<TransitionStateConfigDTO> listExport, String[] columnExport,
                                String date) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("transitionStateConfig.export.sheetname");
    String title = I18n.getLanguage("transitionStateConfig.export.title");
    String fileNameOut = I18n.getLanguage("transitionStateConfig.export.fileNameOut");
    int startRow = 7;
    int mergeTitleEndIndex = columnExport.length;
    String subTitle = I18n
        .getLanguage("transitionStateConfig.export.eportDate", date);
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExport,
        sheetName,
        title,
        subTitle,
        startRow,
        3,
        mergeTitleEndIndex,
        true,
        "language.transitionStateConfig",
        lstHeaderSheet1,
        fieldSplit,
        "",
        I18n.getLanguage("transitionStateConfig.export.firstLeftHeader"),
        I18n.getLanguage("transitionStateConfig.export.secondLeftHeader"),
        I18n.getLanguage("transitionStateConfig.export.firstRightHeader"),
        I18n.getLanguage("transitionStateConfig.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("transitionStateConfig.stt"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport
        .exportExcel(fileTemplate, fileNameOut, fileExports, rootPath, null);
    return fileExport;
  }

  private List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false,
          0, 0, new String[]{}, null, "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
  }

  @Override
  public List<CatItemDTO> getListProcess() {
    log.debug("Request to getListProcess: {}");
    return transitionStateConfigRepository.getListProcess();
  }

  @Override
  public List<CatItemDTO> getListState(Long process) {
    log.debug("Request to getListState: {}", process);
    return transitionStateConfigRepository.getListState(process);
  }

  public void setMapProcess() {
    mapProcess.put(Constants.PROCESS.CR_STATE, I18n.getLanguage("combo.deault.cr_process"));
    mapProcess.put(Constants.PROCESS.MR_STATE, I18n.getLanguage("combo.deault.mr_process"));
    mapProcess.put(Constants.PROCESS.PT_STATE, I18n.getLanguage("combo.deault.pt_process"));
    mapProcess.put(Constants.PROCESS.TT_STATE, I18n.getLanguage("combo.deault.tt_process"));
    mapProcess.put(Constants.PROCESS.WO_STATE, I18n.getLanguage("combo.deault.wo_process"));
    mapProcess.put(Constants.PROCESS.ORDER_STATE, I18n.getLanguage("combo.deault.order_process"));
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = TransitionStateConfigDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        List<String> rmKeys = Arrays.asList("beginStateCode", "endStateCode", "processName", "processCode",
            "endStateName", "beginStateName");
        for (String key : rmKeys) {
          keys.remove(key);
        }
        return keys;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
