package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import com.viettel.gnoc.sr.repository.SRFlowExecuteRepository;
import com.viettel.gnoc.sr.repository.SRRoleActionRepository;
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
public class SRFlowExecuteBusinessImpl implements SRFlowExecuteBusiness {

  @Autowired
  protected SRFlowExecuteRepository srFlowExecuteRepository;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  protected SRRoleActionRepository srRoleActionRepository;


  @Value("${application.temp.folder}")
  private String tempFolder;
  private List<ItemDataCRInside> lstCountry;
  private final static String SR_FLOW_EXECUTE_EXPORT = "SR_FLOW_EXECUTE_EXPORT";

  @Override
  public Datatable getListSRFlowExecute(SRFlowExecuteDTO srFlowExecuteDTO) {
    log.debug("Request to getListSRFlowExecute : {}", srFlowExecuteDTO);
    Datatable datatable = srFlowExecuteRepository.getListSRFlowExecute(srFlowExecuteDTO);
    List<SRFlowExecuteDTO> list = (List<SRFlowExecuteDTO>) datatable.getData();

    //Tinh tong so buoc
    for (SRFlowExecuteDTO srFLDTO : list) {
      String[] arrIDFlow = srFLDTO.getListflowId().split(",");
      Long id = 0L;
      if (arrIDFlow != null) {
        for (String flwID : arrIDFlow) {
          id = Long.parseLong(flwID);
          break;
        }
      }
      List<SRRoleActionDTO> lstRoleAction = srRoleActionRepository
          .getListRoleActionsByFlowExecuteId(id);
      if (lstRoleAction != null && !lstRoleAction.isEmpty()) {
        srFLDTO.setCountStep(lstRoleAction.size());
      }
    }

    //Set button Delete
    List<SRFlowExecuteDTO> lst;
    for (SRFlowExecuteDTO dto : list) {
      dto.setBtnDelete(true);
      lst = srFlowExecuteRepository.isFlowUsingByCatalogTable(dto.getListflowId());
      if (lst != null && !lst.isEmpty()) {
        dto.setBtnDelete(false);
      }
    }
    datatable.setData(list);
    return datatable;
  }

  @Override
  public List<SRFlowExecuteDTO> getListSRFlowExecuteCBB(SRFlowExecuteDTO srFlowExecuteDTO) {
    log.debug("Request to getListSRFlowExecuteCBB : {}", srFlowExecuteDTO);
    return srFlowExecuteRepository.getListSRFlowExecuteCBB(srFlowExecuteDTO);
  }

  @Override
  public ResultInSideDto delete(String listflowId) {
    log.debug("Request to delete : {}", listflowId);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String[] arrIDFlow = listflowId.split(",");
    long flowId = 0L;
    for (String fl_id : arrIDFlow) {
      flowId = Long.parseLong(fl_id);
      srFlowExecuteRepository.delete(flowId);
    }
    return resultInSideDto;
  }

  //MapcountryName
  Map<String, String> mapNameCountry = new HashMap<>();

  public void MapCountryName() {
    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstCountryName) {
        mapNameCountry.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }

  @Override
  public SRFlowExecuteDTO getDetail(String listflowId) {
    log.debug("Request to getDetail : {}", listflowId);
    SRFlowExecuteDTO srFlowExecuteDTO = new SRFlowExecuteDTO();
    String[] arrIDFlow = listflowId.split(",");
    List<SRFlowExecuteDTO> srFlowExecuteDTOS = new ArrayList<>();
    for (String id_flow : arrIDFlow) {
      Long idFLow = Long.parseLong(id_flow);
      List<SRRoleActionDTO> srRoleActionDTOS = srRoleActionRepository
          .getListRoleActionsByFlowExecuteId(idFLow);
      SRFlowExecuteDTO srflow = srFlowExecuteRepository.getDetail(idFLow);
      List<SRFlowExecuteDTO> check = srFlowExecuteRepository.isFlowUsingByCatalog(idFLow);
      if (check != null && check.size() > 0) {
        srflow.setBtnDelete(false);
      } else {
        srflow.setBtnDelete(true);
      }
      srflow.setLstRoleActionDTO(srRoleActionDTOS);
      srFlowExecuteDTOS.add(srflow);
    }
    List<SRRoleActionDTO> srRoleActionDTOS = srFlowExecuteDTOS.get(0).getLstRoleActionDTO();
    srFlowExecuteDTO.setFlowExecuteDTOMainList(srFlowExecuteDTOS);
    srFlowExecuteDTO.setFlowExecuteDTODetailList(srFlowExecuteDTOS);
    srFlowExecuteDTO.setFlowName(srFlowExecuteDTOS.get(0).getFlowName());
    srFlowExecuteDTO.setFlowDescription(srFlowExecuteDTOS.get(0).getFlowDescription());
    srFlowExecuteDTO.setLstRoleActionDTO(srRoleActionDTOS);
    srFlowExecuteDTO.setCountStep(srRoleActionDTOS.size());
    //CurrentStatus
    Map<String, String> mapCurrentStatus = new HashMap<>();
    List<SRRoleActionDTO> lstCurrentStatusName = srRoleActionRepository.getComboBoxStatus();
    if (lstCurrentStatusName != null && !lstCurrentStatusName.isEmpty()) {
      for (SRRoleActionDTO currentStatusName : lstCurrentStatusName) {
        mapCurrentStatus.put(String.valueOf(currentStatusName.getCurrentStatus()),
            currentStatusName.getCurrentStatusName());
      }
    }
    //GroupRole
    Map<String, String> mapGroupRole = new HashMap<>();
    List<SRRoleActionDTO> lstGroupRoleName = srRoleActionRepository.getComboBoxGroupRole();
    if (lstGroupRoleName != null && !lstGroupRoleName.isEmpty()) {
      for (SRRoleActionDTO groupRoleName : lstGroupRoleName) {
        mapGroupRole.put(String.valueOf(groupRoleName.getGroupRole()),
            groupRoleName.getGroupRoleName());
      }
    }
    for (SRRoleActionDTO dto : srRoleActionDTOS) {
      //NextStatusName
      if (dto.getNextStatus() != null && !dto.getNextStatus().isEmpty()) {
        List<String> listNextStatus = new ArrayList<>();
        String[] strNextStatus = dto.getNextStatus().split(",");
        for (String arr : strNextStatus) {
          if (mapCurrentStatus.get(arr.trim()) != null) {
//            nextStatus += mapCurrentStatus.get(arr.trim()) + ",";
            listNextStatus.add(mapCurrentStatus.get(arr.trim()));
          }
        }
        dto.setNextStatusName(String.join(",", listNextStatus));
      }
      //GroupRoleName
      if (dto.getGroupRole() != null && !dto.getGroupRole().isEmpty()) {
        List<String> listRoleName = new ArrayList<>();
        String[] strGroupRole = dto.getGroupRole().split(",");
        for (String arrGroupRoleName : strGroupRole) {
          if (mapGroupRole.get(arrGroupRoleName.trim()) != null) {
            listRoleName.add(mapGroupRole.get(arrGroupRoleName.trim()));
          }
        }
        dto.setGroupRoleName(String.join(",", listRoleName));
      }
      //ActionsName
      Map<String, String> mapAction = new HashMap<>();
      List<SRRoleActionDTO> lstActions = srRoleActionRepository
          .getComboBoxActions(dto.getRoleType());
      if (lstActions != null && !lstActions.isEmpty()) {
        for (SRRoleActionDTO actsName : lstActions) {
          mapAction.put(String.valueOf(actsName.getActions()),
              actsName.getActionsName());
        }
      }
      if (dto.getActions() != null && !dto.getActions().isEmpty()) {
        List<String> listActionsName = new ArrayList<>();
        String[] strActionsName = dto.getActions().split(",");
        for (String arrAction : strActionsName) {
          if (mapAction.get(arrAction.trim()) != null) {
            listActionsName.add(mapAction.get(arrAction.trim()));
          }
        }
        dto.setActionsName(String.join(",", listActionsName));
      }
    }
    return srFlowExecuteDTO;
  }

  @Override
  public File exportSearchData(SRFlowExecuteDTO srFlowExecuteDTO) throws Exception {
    setMapCountryName();
    List<SRFlowExecuteDTO> srFlowExecuteDTOList = srFlowExecuteRepository
        .onSearchExport(srFlowExecuteDTO);
    List<SRRoleActionDTO> listRoleExport = new ArrayList<>();
    if (srFlowExecuteDTOList != null && srFlowExecuteDTOList.size() > 0) {
      for (SRFlowExecuteDTO dto : srFlowExecuteDTOList) {
        //set ten Country
        String lstCountryName = "";
        if (dto.getListCountry() != null) {
          String[] arrCountry = dto.getListCountry().split(",");
          for (String tmp : arrCountry) {
            if (mapCountryName.get(tmp.trim()) != null) {
              lstCountryName += mapCountryName.get(tmp.trim()) + ",";
            }
          }
          dto.setListCountryName(lstCountryName);
        }

        //Lay list RoleAction cua tung Flow
        //Va tinh tong so buoc
        SRFlowExecuteDTO dto2 = getDetail(dto.getListflowId());
        List<SRRoleActionDTO> lstRoleAction = dto2.getLstRoleActionDTO();
        if (lstRoleAction != null && !lstRoleAction.isEmpty()) {
          dto.setCountStep(lstRoleAction.size());
        }
        if (dto2 != null) {
          for (SRRoleActionDTO RoleAcTmp : lstRoleAction) {
            if (RoleAcTmp != null) {
              RoleAcTmp.setFlowName(dto2.getFlowName());
            }
          }
        }
        listRoleExport.addAll(lstRoleAction);
      }
    }
    return exportFileEx(srFlowExecuteDTOList, listRoleExport, "");
  }

  private File exportFileEx(List<SRFlowExecuteDTO> srFlowExecuteDTOList,
      List<SRRoleActionDTO> listRoleExport, String key)
      throws Exception {
    String sheetName = I18n.getLanguage("srFlowExecute.export.nameSheetOne");
    String title = I18n.getLanguage("srFlowExecute.export.title");
    String fileNameOut;
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();

    columnSheet = new ConfigHeaderExport("listCountryName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("flowName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("flowDescription", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("countStep", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    //kiểm tra đầu vào
    fileNameOut = SR_FLOW_EXECUTE_EXPORT;
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        srFlowExecuteDTOList
        , sheetName
        , title
        , null
        , 7
        , 3
        , 7
        , true
        , "language.srFlowExecute.list"
        , headerExportList
        , fieldSplit
        , ""
        , I18n.getLanguage("srFlowExecute.export.firstLeftHeader")
        , I18n.getLanguage("srFlowExecute.export.secondLeftHeader")
        , I18n.getLanguage("srFlowExecute.export.firstRightHeader")
        , I18n.getLanguage("srFlowExecute.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("srFlowExecute.list.stt"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configFileExport.setLstCreatCell(lstCellSheet);
    fileExportList.add(configFileExport);

    //add thêm dữ liệu SRRoleAction vào sheet thứ 2
    ConfigFileExport configFileExportTwo = getConfigFileExport(listRoleExport);
    fileExportList.add(configFileExportTwo);

    //Cấu hình đường dẫn
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExportList
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  public ConfigFileExport getConfigFileExport(List<SRRoleActionDTO> listRoleExport) {
    String sheetName = I18n.getLanguage("srRoleAction.export.nameSheetTwo");
    String title = I18n.getLanguage("srRoleAction.export.title");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("flowName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("groupRole", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("currentStatus", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("nextStatus", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("roleTypeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("actionsName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        listRoleExport,
        sheetName,
        title,
        null,
        7,
        3,
        7,
        true,
        "language.srRoleAction.list",
        lstHeaderSheet,
        fieldSplit,
        "",
        I18n.getLanguage("srFlowExecute.export.firstLeftHeader")
        , I18n.getLanguage("srFlowExecute.export.secondLeftHeader")
        , I18n.getLanguage("srFlowExecute.export.firstRightHeader")
        , I18n.getLanguage("srFlowExecute.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("srFlowExecute.list.stt"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configFileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configFileExport);

    return configFileExport;
  }

  Map<String, String> mapCountryName = new HashMap<>();

  public void setMapCountryName() {
    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstCountryName) {
        mapCountryName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }

}
