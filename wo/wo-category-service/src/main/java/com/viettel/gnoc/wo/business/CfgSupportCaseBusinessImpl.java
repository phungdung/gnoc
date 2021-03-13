package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.wo.dto.CfgSupportCaseDTO;
import com.viettel.gnoc.wo.dto.CfgSupportCaseTestDTO;
import com.viettel.gnoc.wo.repository.CfgSupportCaseRepository;
import com.viettel.gnoc.wo.repository.CfgSupportCaseTestRepository;
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

@Service
@Transactional
@Slf4j
public class CfgSupportCaseBusinessImpl implements CfgSupportCaseBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;
  private final static String CFG_SUPPORT_CASE_EXPORT = "CFG_SUPPORT_CASE_EXPORT";

  @Autowired
  protected CfgSupportCaseRepository cfgSupportCaseRepository;

  @Autowired
  protected CfgSupportCaseTestRepository cfgSupportCaseTestRepository;

  @Override
  public ResultInSideDto deleteCaseAndCaseTest(Long id) {
    log.debug("Request to deleteCaseAndCaseTest : {}", id);
    return cfgSupportCaseRepository.deleteCaseAndCaseTest(id);
  }

  @Override
  public ResultInSideDto add(CfgSupportCaseDTO cfgSupportCaseDTO) {
    log.debug("Request to add : {}", cfgSupportCaseDTO);
    return cfgSupportCaseRepository.add(cfgSupportCaseDTO);
  }

  @Override
  public ResultInSideDto edit(CfgSupportCaseDTO cfgSupportCaseDTO) {
    log.debug("Request to edit : {}", cfgSupportCaseDTO);
    return cfgSupportCaseRepository.edit(cfgSupportCaseDTO);
  }

  @Override
  public CfgSupportCaseDTO getDetail(Long id) {
    log.debug("Request to getDetail : {}", id);
    return cfgSupportCaseRepository.getDetail(id);
  }

  @Override
  public Datatable getListCfgSupportCaseDTONew(CfgSupportCaseDTO cfgSupportCaseDTO) {
    log.debug("Request to getListCfgSupportCaseDTONew : {}", cfgSupportCaseDTO);
    return cfgSupportCaseRepository.getListCfgSupportCaseDTONew(cfgSupportCaseDTO);
  }

  @Override
  public File exportData(CfgSupportCaseDTO cfgSupportCaseDTO) throws Exception {
    log.debug("Request to exportData : {}", cfgSupportCaseDTO);
    List<CfgSupportCaseDTO> list = cfgSupportCaseRepository
        .getListCfgSupportCaseExport(cfgSupportCaseDTO);
    for (CfgSupportCaseDTO dto : list) {
      if ("0".equals(dto.getFileRequiredTxt())) {
        dto.setFileRequiredTxt(I18n.getLanguage("wo.common.option.0"));
      } else {
        dto.setFileRequiredTxt(I18n.getLanguage("wo.common.option.1"));
      }
    }
    return exportFileEx(list);
  }

  private File exportFileEx(List<CfgSupportCaseDTO> lstImport) throws Exception {
    String sheetName = "CfgTime";
    String title = I18n.getLanguage("cfgSupportCase.export.title");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet1;
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    columnSheet1 = new ConfigHeaderExport("caseName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("serviceName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("infraTypeName", "LEFT", false, 0, 0, new String[]{},
        null, "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("testCaseName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("fileRequiredTxt", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");

    String fileNameOut = CFG_SUPPORT_CASE_EXPORT;
    String subTitle = I18n
        .getLanguage("cfgSupportCase.export.exportDate", DateTimeUtils.convertDateOffset());

    lstHeaderSheet1.add(columnSheet1);
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configfileExport = new ConfigFileExport(
        lstImport
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.cfgSupportCase"
        , lstHeaderSheet1
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configfileExport);
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

  @Override
  public List<CfgSupportCaseTestDTO> getListCfgSupportCaseTestId(Long cfgSuppportCaseId) {
    log.debug("Request to getListCfgSupportCaseTestId : {}", cfgSuppportCaseId);
    return cfgSupportCaseTestRepository.getListCfgSupportCaseTestId(cfgSuppportCaseId);
  }
}
