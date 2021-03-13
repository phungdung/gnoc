package com.viettel.gnoc.cr.business;

import static com.viettel.gnoc.commons.repository.BaseRepository.getSqlLanguageExchange;
import static com.viettel.gnoc.commons.repository.BaseRepository.setLanguage;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
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
import com.viettel.gnoc.cr.dto.CrManagerRolesDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopesOfRolesDTO;
import com.viettel.gnoc.cr.repository.CrManagerScopesOfRolesRepository;
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
public class CrManagerScopesOfRolesBusinessImpl implements CrManagerScopesOfRolesBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected CrManagerScopesOfRolesRepository crManagerScopesOfRolesRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  private final static String Cr_ManagerScopesOfRoles_EXPORT = "Cr_Manager_Scopes_Of_Roles_EXPORT";

  @Override
  public Datatable getListManagerScopesOfRoles(
      CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
    log.info("Request to getManagerScopesOfRoles : {}", crManagerScopesOfRolesDTO);
    return crManagerScopesOfRolesRepository.getListManagerScopesOfRoles(crManagerScopesOfRolesDTO);
  }

  @Override
  public ResultInSideDto addManagerScopesOfRoles(
      CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
    log.info("Request to addManagerScopesOfRoles : {}", crManagerScopesOfRolesDTO);
    ResultInSideDto resultInSideDto = crManagerScopesOfRolesRepository
        .addOrEdit(crManagerScopesOfRolesDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add CrManagerScopesOfRoles",
          "Add CrManagerScopesOfRoles ID: " + resultInSideDto.getId(), crManagerScopesOfRolesDTO,
          null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateManagerScopesOfRoles(
      CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
    log.info("Request to updateManagerScopesOfRoles : {}", crManagerScopesOfRolesDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (crManagerScopesOfRolesDTO.getCmsorsId() != null
        && crManagerScopesOfRolesDTO.getCmsorsId() > 0) {
      resultInSideDto = crManagerScopesOfRolesRepository.addOrEdit(crManagerScopesOfRolesDTO);
      if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
        UserToken userToken = ticketProvider.getUserToken();
        commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Update CrManagerScopesOfRoles",
            "Update CrManagerScopesOfRoles Id: " + resultInSideDto.getId(),
            crManagerScopesOfRolesDTO, null));
      }
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteManagerScopesOfRoles(Long cmsorsId) {
    log.info("Request to deleteManagerScopesOfRoles : {}", cmsorsId);
    ResultInSideDto resultInSideDto = crManagerScopesOfRolesRepository
        .deleteManagerScopesOfRoles(cmsorsId);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete CrManagerScopesOfRoles", "Delete CrManagerScopesOfRoles Id: " + cmsorsId, null,
          null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListManagerScopesOfRoles(
      CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (!crManagerScopesOfRolesDTO.getListId().isEmpty()
        && crManagerScopesOfRolesDTO.getListId().size() > 0) {
      for (Long id : crManagerScopesOfRolesDTO.getListId()) {
        resultInSideDto = crManagerScopesOfRolesRepository.deleteManagerScopesOfRoles(id);
      }
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public File exportData(CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) throws Exception {
    List<CrManagerScopesOfRolesDTO> lstCrEx = crManagerScopesOfRolesRepository
        .getListDataExport(crManagerScopesOfRolesDTO);
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Export Data CrManagerScopesOfRoles",
        "Export Data CrManagerScopesOfRoles", crManagerScopesOfRolesDTO, null));
    return exportFileEx(lstCrEx, "");
  }

  @Override
  public CrManagerScopesOfRolesDTO getDetail(Long cmsorsId) {
    log.info("Request to getDetail : {}", cmsorsId);
    if (cmsorsId > 0 && cmsorsId != null) {
      return crManagerScopesOfRolesRepository.getDetail(cmsorsId);
    }
    return null;
  }

  @Override
  public List<CrManagerScopeDTO> getListManagerScopeCBB() {
    log.info("Request to getListManagerScopeCBB : {}");
    List<CrManagerScopeDTO> lstReturn = crManagerScopesOfRolesRepository.getListManagerScopeCBB();
    getListLanguageExchange(lstReturn, "cmseId", "cmseName",
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR, Constants.APPLIED_BUSSINESS.CR_MANAGER_SCOPE);
    return lstReturn;
  }

  @Override
  public List<CrManagerRolesDTO> getListCrManagerRolesCBB() {
    log.info("Request to getListCrManagerRolesCBB : {}");
    List<CrManagerRolesDTO> lstReturn = crManagerScopesOfRolesRepository.getListCrManagerRolesCBB();
    getListLanguageExchange(lstReturn, "cmreId", "cmreName",
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR, Constants.APPLIED_BUSSINESS.CR_MANAGER_ROLE);
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

  private File exportFileEx(List<CrManagerScopesOfRolesDTO> lstCrEx, String key) throws
      Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("CrManagerScopesOfRoles.export.title");
    String title = I18n.getLanguage("CrManagerScopesOfRoles.export.title");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("cmseCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("cmseName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("cmreCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("cmreName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    if (Constants.RESULT_IMPORT.equals(key)) {

    } else {
      fileNameOut = Cr_ManagerScopesOfRoles_EXPORT;
      subTitle = I18n
          .getLanguage("CrManagerScopesOfRoles.export.eportDate",
              DateTimeUtils.convertDateOffset());
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstCrEx,
        sheetName,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.CrManagerScopesOfRoles",
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
