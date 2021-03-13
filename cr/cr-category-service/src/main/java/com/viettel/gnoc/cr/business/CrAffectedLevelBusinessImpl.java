package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CrAffectedLevelDTO;
import com.viettel.gnoc.cr.repository.CrAffectedLevelRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

/**
 * @author DungPV
 */
@Service
@Slf4j
@Transactional
public class CrAffectedLevelBusinessImpl implements CrAffectedLevelBusiness {

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  CrAffectedLevelRepository crAffectedLevelRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;


  @Value("${application.temp.folder}")
  private String tempFolder;

  private final static String Cr_Affected_Level_EXPORT = "Cr_Affected_Level";

  @Override
  public Datatable getListCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO) {
    log.info("Request to getListCrAffectedLevel : {}", crAffectedLevelDTO);
    return crAffectedLevelRepository.getListCrAffectedLevel(crAffectedLevelDTO);
  }

  @Override
  public ResultInSideDto addCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO) {
    log.info("Request to addCrAffectedLevel : {}", crAffectedLevelDTO);
    crAffectedLevelDTO.setIsActive(1L);
    ResultInSideDto resultInSideDto = crAffectedLevelRepository
        .addOrUpdateCrAffectedLevel(crAffectedLevelDTO);
    languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.AFFECTED_LEVEL", resultInSideDto.getId(),
            crAffectedLevelDTO.getListAffectedLevelName());
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add crAffectedLevel", "Add crAffectedLevel ID: " + resultInSideDto.getId(),
          crAffectedLevelDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO) {
    log.info("Request to updateCrAffectedLevel : {}", crAffectedLevelDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    crAffectedLevelDTO.setIsActive(1L);
    if (crAffectedLevelDTO.getAffectedLevelId() != null
        && crAffectedLevelDTO.getAffectedLevelId() > 0) {
      resultInSideDto = crAffectedLevelRepository.addOrUpdateCrAffectedLevel(crAffectedLevelDTO);
      languageExchangeRepository
          .saveListLanguageExchange("OPEN_PM", "OPEN_PM.AFFECTED_LEVEL", resultInSideDto.getId(),
              crAffectedLevelDTO.getListAffectedLevelName());
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update crAffectedLevel", "Update crAffectedLevel ID: " + resultInSideDto.getId(),
          crAffectedLevelDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteCrAffectedLevel(Long affectedLevelId) {
    log.info("Request to deleteCrAffectedLevel : {}", affectedLevelId);
    ResultInSideDto resultInSideDto = crAffectedLevelRepository
        .deleteCrAffectedLevel(affectedLevelId);
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Delete crAffectedLevel", "Delete crAffectedLevel ID: " + affectedLevelId, null, null));
    languageExchangeRepository
        .deleteListLanguageExchange("OPEN_PM", "OPEN_PM.AFFECTED_LEVEL", affectedLevelId);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO) {
    log.info("Request to deleteListCrAffectedLevel : {}", crAffectedLevelDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (crAffectedLevelDTO.getListId().size() > 0 && !crAffectedLevelDTO.getListId().isEmpty()) {
      for (int i = 0; i < crAffectedLevelDTO.getListId().size(); i++) {
        resultInSideDto = crAffectedLevelRepository
            .deleteCrAffectedLevel(crAffectedLevelDTO.getListId().get(i));
      }
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public CrAffectedLevelDTO getDetail(Long affectedLevelId) {
    CrAffectedLevelDTO crAffectedLevelDTO = crAffectedLevelRepository.getDetail(affectedLevelId);
    crAffectedLevelDTO.setListAffectedLevelName(languageExchangeRepository
        .getListLanguageExchangeById("OPEN_PM", "OPEN_PM.AFFECTED_LEVEL", affectedLevelId, null));
    return crAffectedLevelDTO;
  }

  @Override
  public File exportData(CrAffectedLevelDTO crAffectedLevelDTO) throws Exception {
    List<CrAffectedLevelDTO> lstEx = crAffectedLevelRepository
        .getListDataExport(crAffectedLevelDTO);
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Export data crAffectedLevel", "Export data crAffectedLevel ", crAffectedLevelDTO, null));
    return exportEx(lstEx, "");
  }

  private File exportEx(List<CrAffectedLevelDTO> lstEx, String key) throws Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("CrAffectedLevel.export.title");
    String title = I18n.getLanguage("CrAffectedLevel.export.title");
    List<ConfigFileExport> fileExports = new ArrayList<>();

    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("affectedLevelCode", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("affectedLevelName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("twoApproveLevelName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("appliedSystemName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    if (Constants.RESULT_IMPORT.equals(key)) {

    } else {
      fileNameOut = Cr_Affected_Level_EXPORT;
      subTitle = I18n
          .getLanguage("CrAffectedLevel.export.eportDate", DateTimeUtils.convertDateOffset());
    }
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstEx,
        sheetName,
        title,
        subTitle,
        7,
        3,
        9,
        true,
        "language.CrAffectedLevel",
        lstHeaderSheet,
        fieldSplit,
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
}
