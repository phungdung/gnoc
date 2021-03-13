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
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.repository.CrImpactSegmentRepository;
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
public class CrImpactSegmentBusinessImpl implements CrImpactSegmentBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  protected CrImpactSegmentRepository crImpactSegmentRepository;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  private static final String CR_ImpactSegment_EXPORT = "CR_ImpactSegment_EXPORT";

  @Override
  public Datatable getListCrImpactSegment(ImpactSegmentDTO impactSegmentDTO) {
    log.info("Request to getListImpactSegment : {}", impactSegmentDTO);
    return crImpactSegmentRepository.getListImpactSegment(impactSegmentDTO);
  }

  @Override
  public ResultInSideDto addCrImpactSegment(ImpactSegmentDTO impactSegmentDTO) {
    log.info("Request to addImpactSegment : {}", impactSegmentDTO);
    impactSegmentDTO.setIsActive(1L);
    ResultInSideDto resultInSideDto = crImpactSegmentRepository
        .addOrEditImpactSegment(impactSegmentDTO);
    languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.IMPACT_SEGMENT", resultInSideDto.getId(),
            impactSegmentDTO.getListImpactSegmentName());
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add CrImpactSegment", "Add CrImpactSegment ID: " + resultInSideDto.getId(),
          impactSegmentDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCrImpactSegment(ImpactSegmentDTO impactSegmentDTO) {
    log.info("Request to updateImpactSegment : {}", impactSegmentDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (impactSegmentDTO.getImpactSegmentId() != null
        && impactSegmentDTO.getImpactSegmentId() > 0) {
      impactSegmentDTO.setIsActive(1L);
      resultInSideDto = crImpactSegmentRepository.addOrEditImpactSegment(impactSegmentDTO);
      languageExchangeRepository
          .saveListLanguageExchange("OPEN_PM", "OPEN_PM.IMPACT_SEGMENT", resultInSideDto.getId(),
              impactSegmentDTO.getListImpactSegmentName());
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update CrImpactSegment", "Update CrImpactSegment ID: " + resultInSideDto.getId(),
          impactSegmentDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteCrImpactSegment(Long impactSegmentId) {
    log.info("Request to updateImpactSegment : {}", impactSegmentId);
    ResultInSideDto resultInSideDto = crImpactSegmentRepository
        .deleteImpactSegment(impactSegmentId);
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Delete CrImpactSegment", "Delete CrImpactSegment ID: " + impactSegmentId, null, null));
    languageExchangeRepository
        .deleteListLanguageExchange("OPEN_PM", "OPEN_PM.IMPACT_SEGMENT", impactSegmentId);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListCrImpactSegment(ImpactSegmentDTO impactSegmentDTO) {
    log.info("Request to deleteListCrImpactSegment : {}", impactSegmentDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (impactSegmentDTO.getListId().size() > 0 && !impactSegmentDTO.getListId().isEmpty()) {
      for (Long id : impactSegmentDTO.getListId()) {
        resultInSideDto = crImpactSegmentRepository.deleteImpactSegment(id);
      }
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public ImpactSegmentDTO getDetail(Long impactSegmentId) {
    log.info("Request to getListImpactSegment : {}", impactSegmentId);
    if (impactSegmentId != null && impactSegmentId > 0) {
      ImpactSegmentDTO impactSegmentDTO = crImpactSegmentRepository.getDetail(impactSegmentId);
      impactSegmentDTO.setListImpactSegmentName(languageExchangeRepository
          .getListLanguageExchangeById("OPEN_PM", "OPEN_PM.IMPACT_SEGMENT", impactSegmentId, null));
      return impactSegmentDTO;
    }
    return null;
  }

  @Override
  public File exportData(ImpactSegmentDTO impactSegmentDTO) throws Exception {
    List<ImpactSegmentDTO> lstEx = crImpactSegmentRepository.getListDataExport(impactSegmentDTO);
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Export Data CrImpactSegment", "Export Data CrImpactSegment ",
        impactSegmentDTO, null));
    return exportFileEx(lstEx, "");
  }

  private File exportFileEx(List<ImpactSegmentDTO> lstEx, String key) throws Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("ImpactSegment.export.title");
    String title = I18n.getLanguage("ImpactSegment.export.title");
    List<ConfigFileExport> fileExports = new ArrayList<>();

    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();

    columnSheet = new ConfigHeaderExport("impactSegmentCode", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("impactSegmentName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("appliedSystemName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    if (Constants.RESULT_IMPORT.equals(key)) {

    } else {
      fileNameOut = CR_ImpactSegment_EXPORT;
      subTitle = I18n
          .getLanguage("ImpactSegment.export.exportDate", DateTimeUtils.convertDateOffset());
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstEx,
        sheetName,
        title,
        subTitle,
        7
        , 3
        , 9
        , true
        , "language.ImpactSegment"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
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
