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
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import com.viettel.gnoc.cr.repository.CrManagerScopeRepository;
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

@Slf4j
@Transactional
@Service
public class CrManagerScopeBusinessImpl implements CrManagerScopeBusiness {

  @Autowired
  CrManagerScopeRepository crManagerScopeRepository;

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public ResultInSideDto insertCrManagerScope(CrManagerScopeDTO crManagerScopeDTO) {
    ResultInSideDto resultInSideDto = crManagerScopeRepository
        .insertCrManagerScope(crManagerScopeDTO);
    if (crManagerScopeDTO.getListCmseName() != null) {
      languageExchangeRepository
          .saveListLanguageExchange("OPEN_PM", "OPEN_PM.CR_MANAGER_SCOPE", resultInSideDto.getId(),
              crManagerScopeDTO.getListCmseName());
    }
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add crManagerScope", "Add crManagerScope ID: " + resultInSideDto.getId(),
          crManagerScopeDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCrManagerScope(CrManagerScopeDTO crManagerScopeDTO) {
    ResultInSideDto resultInSideDto = crManagerScopeRepository
        .updateCrManagerScope(crManagerScopeDTO);
    if (crManagerScopeDTO.getListCmseName() != null) {
      languageExchangeRepository
          .saveListLanguageExchange("OPEN_PM", "OPEN_PM.CR_MANAGER_SCOPE", resultInSideDto.getId(),
              crManagerScopeDTO.getListCmseName());
    }
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update crManagerScope", "Update crManagerScope ID: " + resultInSideDto.getId(),
          crManagerScopeDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteCrManagerScope(Long id) {
    ResultInSideDto resultInSideDto = crManagerScopeRepository.deleteCrManagerScope(id);
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Delete Cr Manager Scope", "Delete Cr Manager Scope ID: " + id,
        null, null));
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListCrManagerScope(List<CrManagerScopeDTO> crManagerScopeDTOS) {
    return crManagerScopeRepository.deleteListCrManagerScope(crManagerScopeDTOS);
  }

  @Override
  public CrManagerScopeDTO findCrManagerScopeDTOById(Long id) {
    CrManagerScopeDTO crManagerScopeDTO = crManagerScopeRepository.findCrManagerScopeDTOById(id);
    crManagerScopeDTO.setListCmseName(languageExchangeRepository
        .getListLanguageExchangeById("OPEN_PM", "OPEN_PM.CR_MANAGER_SCOPE", id, null));
    return crManagerScopeDTO;
  }

  @Override
  public ResultInSideDto updateLisCrManagerScope(List<CrManagerScopeDTO> crManagerScopeDTOS) {
    List<Long> ids = new ArrayList<>();
    for (CrManagerScopeDTO item : crManagerScopeDTOS) {
      ids.add(item.getCmseId());
    }
    return crManagerScopeRepository.updateLisCrManagerScope(ids);
  }

  @Override
  public Datatable getListCrManagerScopeSearch(CrManagerScopeDTO crManagerScopeDTO) {
    return crManagerScopeRepository.getListCrManagerScopeSearch(crManagerScopeDTO);
  }

  @Override
  public File exportData(CrManagerScopeDTO crManagerScopeDTO) throws Exception {
    crManagerScopeDTO.setPage(1);
    crManagerScopeDTO.setPageSize(Integer.MAX_VALUE);
    Datatable datatable = getListCrManagerScopeSearch(crManagerScopeDTO);
    List<CrManagerScopeDTO> lstReturn = (List<CrManagerScopeDTO>) datatable.getData();
    File exportData = exportFileEx(lstReturn);
    if (exportData != null) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Export Data crManagerScope", "Export Data crManagerScope  ",
          null, null));
    }
    return exportData;
  }

  private File exportFileEx(List<CrManagerScopeDTO> lstData) throws Exception {
    String title = I18n.getLanguage("crManagerScope.title_export");
    String fileNameOut = "CrManagerScope";
    String subTitle = I18n
        .getLanguage("crManagerScope.export.exportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = renderHeaderSheet("cmseCode", "cmseName",
        "description");
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configfileExport = new ConfigFileExport(
        lstData
        , I18n.getLanguage("crManagerScope.title_export")
        , title
        , subTitle
        , 7
        , 3
        , 7
        , true
        , "language.crManagerScope"
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
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("crManagerScope.stt"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    configfileExport
        .setCustomColumnWidth(new String[]{"2000", "6000", "6000", "6000", "6000", "6000", "6000"});
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

  private List<ConfigHeaderExport> renderHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", true, 0, 1, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }
}
