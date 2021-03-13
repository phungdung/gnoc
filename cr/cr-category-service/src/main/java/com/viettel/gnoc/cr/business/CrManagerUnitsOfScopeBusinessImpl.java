package com.viettel.gnoc.cr.business;

import static com.viettel.gnoc.commons.repository.BaseRepository.getSqlLanguageExchange;
import static com.viettel.gnoc.commons.repository.BaseRepository.setLanguage;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CrManagerUnitsOfScopeDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.repository.CrManagerUnitsOfScopeRepository;
import com.viettel.gnoc.cr.repository.DeviceTypesRepository;
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
public class CrManagerUnitsOfScopeBusinessImpl implements CrManagerUnitsOfScopeBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  private final static String Cr_Manager_Units_Of_Scope_EXPORT = "Cr_Manager_Units_Of_Scope_EXPORT";

  @Autowired
  protected CrManagerUnitsOfScopeRepository crManagerUnitsOfScopeRepository;

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected UnitRepository unitRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected DeviceTypesRepository deviceTypesRepository;

  @Autowired
  TicketProvider ticketProvider;

  //private UserToken userToken = ticketProvider.getUserToken();

  @Override
  public Datatable getListUnitOfScope(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    log.info("Request to getManagerScopesOfRoles : {}", crManagerUnitsOfScopeDTO);
    return crManagerUnitsOfScopeRepository.getListCrManagerUnitsOfScope(crManagerUnitsOfScopeDTO);
  }

  @Override
  public CrManagerUnitsOfScopeDTO getDetail(Long cmnoseId) {
    log.info("Request to getDetail : {}", cmnoseId);
    if (cmnoseId > 0 && cmnoseId != null) {
      return crManagerUnitsOfScopeRepository.getDetail(cmnoseId);
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteCrManagerUnitsOfScope(Long cmnoseId) {
    log.info("Request to deleteCrManagerUnitsOfScope : {}", cmnoseId);
    ResultInSideDto resultInSideDto = crManagerUnitsOfScopeRepository
        .deleteCrManagerUnitsOfScope(cmnoseId);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete CrManagerUnitsOfScope", "Delete CrManagerUnitsOfScope ID: " + cmnoseId, null,
          null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListCrManagerUnitsOfScope(
      CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (!crManagerUnitsOfScopeDTO.getListId().isEmpty()
        && crManagerUnitsOfScopeDTO.getListId().size() > 0) {
      for (Long id : crManagerUnitsOfScopeDTO.getListId()) {
        resultInSideDto = crManagerUnitsOfScopeRepository.deleteCrManagerUnitsOfScope(id);
      }
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public File exportData(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) throws Exception {
    List<CrManagerUnitsOfScopeDTO> lstEx = (List<CrManagerUnitsOfScopeDTO>)
        crManagerUnitsOfScopeRepository.getListDataExport(crManagerUnitsOfScopeDTO).getData();
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Export Data CrManagerUnitsOfScope", "Export Data CrManagerUnitsOfScope ",
        crManagerUnitsOfScopeDTO,
        null));
    return exportFileEx(lstEx, "");
  }

  @Override
  public ResultInSideDto addCrManagerUnitsOfScope(
      CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    log.info("Request to addCrManagerUnitsOfScope : {}", crManagerUnitsOfScopeDTO);
    ResultInSideDto resultInSideDto = crManagerUnitsOfScopeRepository.add(crManagerUnitsOfScopeDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add CrManagerUnitsOfScope", "Add CrManagerUnitsOfScope ID: " + resultInSideDto.getId(),
          crManagerUnitsOfScopeDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCrManagerUnitsOfScope(
      CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    log.info("Request to updateCrManagerUnitsOfScope : {}", crManagerUnitsOfScopeDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (crManagerUnitsOfScopeDTO.getCmnoseId() != null
        && crManagerUnitsOfScopeDTO.getCmnoseId() > 0) {
      resultInSideDto = crManagerUnitsOfScopeRepository.edit(crManagerUnitsOfScopeDTO);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update CrManagerUnitsOfScope",
          "Update CrManagerUnitsOfScope ID: " + resultInSideDto.getId(), crManagerUnitsOfScopeDTO,
          null));
    }
    return resultInSideDto;
  }

  @Override
  public List<ItemDataCRInside> getListDeviceTypeByImpactSegmentCBB(Long impactSegmentId) {
    log.info("Request to getListDeviceTypeByImpactSegmentCBB : {}", impactSegmentId);
    List<ItemDataCRInside> lstReturn = crManagerUnitsOfScopeRepository
        .getListDeviceTypeByImpactSegmentCBB(impactSegmentId);
    getListLanguageExchange(lstReturn, "valueStr", "displayStr",
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR, Constants.APPLIED_BUSSINESS.DEVICE_TYPES);
    return lstReturn;
  }

  @Override
  public List<UnitDTO> findListUnitByCodeOrName(UnitDTO unitDTO) {
    log.info("Request to findListUnitByCodeOrName : {}", unitDTO);
    return unitRepository.getListUnitByCodeOrName(unitDTO);
  }

  @Override
  public List<ImpactSegmentDTO> getListImpactSegmentCBB() {
    log.info("Request to getListImpactSegmentCBB : {}");
    List<ImpactSegmentDTO> lstReturn = crManagerUnitsOfScopeRepository.getListImpactSegmentCBB();
    getListLanguageExchange(lstReturn, "impactSegmentId", "impactSegmentName",
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR, Constants.APPLIED_BUSSINESS.IMPACT_SEGMENT);
    return lstReturn;
  }

  private List<?> getListLanguageExchange(List<?> lstReturn, String colId, String colName,
      String system, String bussiness) {
    try {
      String locale = I18n.getLocale();
      Map<String, Object> map = getSqlLanguageExchange(system, bussiness, locale);
      String sqlLanguage = (String) map.get("sql");
      Map mapParam = (Map) map.get("mapParam");
      Map mapType = (Map) map.get("mapType");
      List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
          .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
      lstReturn = setLanguage(lstReturn, lstLanguage, colId, colName);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstReturn;
  }

  private File exportFileEx(List<CrManagerUnitsOfScopeDTO> lstCrEx, String key)
      throws Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("CrManagerUnitsOfScope.export.title");
    String title = I18n.getLanguage("CrManagerUnitsOfScope.export.title");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("cmseCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("cmseName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("unitCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("unitName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("crTypeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("deviceTypeName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    if (Constants.RESULT_IMPORT.equals(key)) {

    } else {
      fileNameOut = Cr_Manager_Units_Of_Scope_EXPORT;
      subTitle = I18n
          .getLanguage("CrManagerUnitsOfScope.export.eportDate", DateTimeUtils.convertDateOffset());
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstCrEx,
        sheetName,
        title,
        subTitle,
        7,
        3,
        9,
        true,
        "language.CrManagerUnitsOfScope",
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
