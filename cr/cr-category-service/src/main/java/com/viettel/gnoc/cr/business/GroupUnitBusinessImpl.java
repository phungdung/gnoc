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
import com.viettel.gnoc.cr.dto.GroupUnitDTO;
import com.viettel.gnoc.cr.repository.GroupUnitRepository;
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
public class GroupUnitBusinessImpl implements GroupUnitBusiness {

  @Autowired
  GroupUnitRepository groupUnitRepository;

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public Datatable getListGroupUnitDTO(GroupUnitDTO groupUnitDTO) {
    return groupUnitRepository.getListGroupUnitDTO(groupUnitDTO);
  }

  @Override
  public ResultInSideDto insertGroupUnit(GroupUnitDTO groupUnitDTO) {
    ResultInSideDto resultInSideDto = groupUnitRepository.insertGroupUnit(groupUnitDTO);
    languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.GROUP_UNIT", resultInSideDto.getId(),
            groupUnitDTO.getListGroupUnitName());
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add groupUnit", "Add groupUnit ID: " + resultInSideDto.getId(),
          groupUnitDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateGroupUnit(GroupUnitDTO groupUnitDTO) {
    ResultInSideDto resultInSideDto = groupUnitRepository.updateGroupUnit(groupUnitDTO);
    languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.GROUP_UNIT", resultInSideDto.getId(),
            groupUnitDTO.getListGroupUnitName());
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update groupUnit", "Update groupUnit ID: " + resultInSideDto.getId(),
          groupUnitDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public GroupUnitDTO findGroupUnitById(Long id) {
    GroupUnitDTO groupUnitDTO = groupUnitRepository.findGroupUnitById(id);
    groupUnitDTO.setListGroupUnitName(languageExchangeRepository
        .getListLanguageExchangeById("OPEN_PM", "OPEN_PM.GROUP_UNIT", id, null));
    return groupUnitDTO;
  }

  @Override
  public ResultInSideDto deleteGroupUnit(Long id) {
    ResultInSideDto resultInSideDto = groupUnitRepository.deleteGroupUnit(id);
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Delete groupUnit", "Delete groupUnit ID: " + id,
        null, null));
    languageExchangeRepository.deleteListLanguageExchange("OPEN_PM", "OPEN_PM.GROUP_UNIT", id);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListGroupUnit(List<GroupUnitDTO> groupUnitDTOS) {
    return groupUnitRepository.deleteListGroupUnit(groupUnitDTOS);
  }

  @Override
  public ResultInSideDto updateListGroupUnit(List<GroupUnitDTO> groupUnitDTOS) {
    List<Long> ids = new ArrayList<>();
    for (GroupUnitDTO item : groupUnitDTOS) {
      ids.add(item.getGroupUnitId());
    }
    return groupUnitRepository.updateListGroupUnit(ids);
  }

  @Override
  public File exportDataListGroupUnit(GroupUnitDTO groupUnitDTO) throws Exception {
    groupUnitDTO.setPage(1);
    groupUnitDTO.setPageSize(Integer.MAX_VALUE);
    Datatable datatable = getListGroupUnitDTO(groupUnitDTO);
    List<GroupUnitDTO> lstReturn = (List<GroupUnitDTO>) datatable.getData();

    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Export groupUnit", "Export groupUnit",
        null, null));
    return exportFileEx(lstReturn);
  }

  private File exportFileEx(List<GroupUnitDTO> lstData) throws Exception {
    String title = I18n.getLanguage("groupUnit.title_export");
    String fileNameOut = "GroupUnit";
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = renderHeaderSheet("groupUnitCode", "groupUnitName");
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configfileExport = new ConfigFileExport(
        lstData
        , I18n.getLanguage("groupUnit.title_export")
        , title
        , null
        , 7
        , 3
        , 7
        , true
        , "language.groupUnit"
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
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("groupUnit.stt"),
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

  private List<ConfigHeaderExport> renderHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

}
