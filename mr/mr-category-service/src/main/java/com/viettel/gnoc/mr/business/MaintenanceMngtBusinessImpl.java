package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.CrCommonProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CR_STATE;
import com.viettel.gnoc.commons.utils.Constants.MR_STATE_CODE;
import com.viettel.gnoc.commons.utils.Constants.MR_STATE_NAME_OLD;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO.SYSTEM;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrApprovalDepartmentDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveRolesDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import com.viettel.gnoc.maintenance.dto.MrClientDetail;
import com.viettel.gnoc.maintenance.dto.MrCrRecreatedDTO;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrForNocSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftProcedureDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrImpactedNodesDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrMobileDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistFilesDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.dto.MrSchedulePeriodicDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
import com.viettel.gnoc.mr.repository.MaintenanceMngtRepository;
import com.viettel.gnoc.mr.repository.MrCfgProcedureTelRepository;
import com.viettel.gnoc.mr.repository.MrCrRecreatedRepository;
import com.viettel.gnoc.mr.repository.MrITSoftProcedureRepository;
import com.viettel.gnoc.mr.repository.MrITSoftScheduleRepository;
import com.viettel.gnoc.mr.repository.MrNodesRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.mr.repository.MrServiceRepository;
import com.viettel.gnoc.mr.repository.UserGroupCategoryRepository;
import com.viettel.gnoc.mr.repository.WorkLogCategoryRepository;
import com.viettel.gnoc.mr.repository.WorkLogRepository;
import com.viettel.gnoc.sr.dto.InsertFileDTO;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoKTTSInfoDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseDTO;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.gnoc.ws.provider.NocProWS;
import com.viettel.nms.nocpro.service.ResponseBO;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MaintenanceMngtBusinessImpl implements MaintenanceMngtBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;
  @Value("${application.resoleveSuccess.resolveSuccessUserGroupCategoryId}")
  private String resolveSuccessUserGroupCategoryId;
  @Value("${application.resoleveSuccess.resolveSuccessWorklogCategoryId}")
  private String resolveSuccessWorklogCategoryId;

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

  @Autowired
  MaintenanceMngtRepository maintenanceMngtRepository;

  @Autowired
  MrServiceBusiness mrServiceBusiness;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  UnitBusiness unitBusiness;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  MrApprovalDepartmentBusiness mrApprovalDepartmentBusiness;

  @Autowired
  MrWoTempBusiness mrWoTempBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  MrApprovalDepartmentBusinessImpl mrApprovalDepartmentServiceImpl;

  @Autowired
  MessagesBusiness messagesBusiness;

  @Autowired
  CrCommonProxy crCommonProxy;

  @Autowired
  CrCategoryServiceProxy crCategoryServiceProxy;

  @Autowired
  MrImpactedNodesBusiness mrImpactedNodesBusiness;

  @Autowired
  MrHisServiceBusiness mrHisServiceBusiness;

  @Autowired
  MrSchedulePeriodicBusiness mrSchedulePeriodicBusiness;

  @Autowired
  UserGroupCategoryRepository userGroupCategoryRepository;

  @Autowired
  WorkLogCategoryRepository workLogCategoryRepository;

  @Autowired
  WorkLogRepository workLogRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  MrRepository mrRepository;

  @Autowired
  MrNodesRepository mrNodesRepository;

  @Autowired
  MrScheduleTelRepository mrScheduleTelRepository;

  @Autowired
  MrCfgProcedureTelRepository mrCfgProcedureTelRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  MrITSoftScheduleRepository mrITSoftScheduleRepository;

  @Autowired
  MrITSoftProcedureRepository mrITSoftProcedureRepository;

  @Autowired
  MrCrRecreatedRepository mrCrRecreatedRepository;

  @Autowired
  MrServiceRepository mrServiceRepository;

  public static final String title = "[GNOC_MR] ";

  Map<String, String> mapGetName = new HashMap<>();
  Map<String, String> mapGetNameNew = Constants.MR_STATE_NAME_OLD.getText();

  //  List<UsersInsideDto> lstUser = new ArrayList<>();
  Map<String, UsersInsideDto> mapUser = new HashMap<>();
  Map<String, UnitDTO> mapUnit = new HashMap<>();
  Map<String, CatLocationDTO> mapCountry = new HashMap<>();
  Map<String, WoCdGroupInsideDTO> mapCdIdHard = new HashMap<>();

  @Override
  public File exportSearchData(MrSearchDTO mrSearchDTO) throws Exception {
    List<MrDTO> lstData = getListMrDTOSearch(mrSearchDTO);
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet("mrId", "countryName", "regionName",
        "subcategory", "mrCode", "mrTitle",
        "state", "mrTypeName", "assignToPersonName", "cycle", "earliestTime", "lastestTime",
        "mrCloseDate", "crNumber", "responsibleUnitCRName", "considerUnitCRName", "woCode",
        "cdGroupWoName", "unitCreateMrName", "note");
    return exportFileEx(lstData, lstHeaderSheet, "", mrSearchDTO);
  }

  private File exportFileEx(List<MrDTO> lst, List<ConfigHeaderExport> lstHeaderSheet,
      String code, MrSearchDTO mrSearchDTO) throws Exception {
    String title = I18n.getLanguage("mrMngt.export.title");
    String fileNameOut = "MaintenanceReport";
    String language = I18n.getLocale();
    String sheetName = I18n.getLanguage("mrMngt.export.title");

    String subTitle = "";
    if (mrSearchDTO.getStartDateMr() != null
        && mrSearchDTO.getEndDateMr() != null) {
      subTitle = I18n.getLanguage("export.From") + " "
          + mrSearchDTO.getStartDateMr() + " - " + I18n.getLanguage("export.To")
          + " " + mrSearchDTO.getEndDateMr();
    }
    getMapUnit();
    getMapUser();
    for (MrDTO m : lst) {
      String unitCode = m.getUnitExecute();

      UnitDTO unitDto = mapUnit.get(unitCode);
      m.setUnitName(unitDto.getUnitName());
      UnitDTO unitDtoParent = mapUnit.get(unitDto.getParentUnitId());
      if (unitDtoParent != null) {
        m.setUnitParentName(unitDtoParent.getUnitName());
        UnitDTO unitDtoParent2 = mapUnit.get(unitDtoParent.getParentUnitId());
        if (unitDtoParent2 != null) {
          m.setUnitParentName2(unitDtoParent2.getUnitName());
        }
      }

      if (I18n.getLanguage("mrMngt.list.mrSoft").equals(m.getMrTypeName())
          && mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE).equalsIgnoreCase(m.getState())) {
        if (m.getChangeResponsible() != null && mapUser.containsKey(m.getChangeResponsible())) {
          m.setAssignToPersonName(mapUser.get(m.getChangeResponsible()).getFullname());
          m.setState(I18n.getLanguage("mrMngt.state.alreadyImpl"));
        } else {
          m.setState(I18n.getLanguage("mrMngt.state.notImpl"));
          m.setAssignToPersonName("");
        }
      } else if (I18n.getLanguage("mrMngt.list.mrHard").equals(m.getMrTypeName())
          && mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE).equalsIgnoreCase(m.getState())) {
        if (m.getFtId() != null && mapUser.containsKey(m.getFtId())) {
          m.setAssignToPersonName(mapUser.get(m.getFtId()).getFullname());
          m.setState(I18n.getLanguage("mrMngt.state.alreadyImpl"));
        } else {
          m.setState(I18n.getLanguage("mrMngt.state.notImpl"));
          m.setAssignToPersonName("");
        }
      }
    }

//    ConfigFileExport configfileExport = null;
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet = null;

    if (Constants.RESULT_IMPORT.equals(code)) {
    } else {
      ConfigFileExport configfileExport = new ConfigFileExport(
          lst
          , sheetName
          , title
          , subTitle
          , 7
          , 3
          , lstHeaderSheet.size()
          , true
          , "language.mrMngt.list"
          , lstHeaderSheet
          , fieldSplit
          , ""
          , I18n.getLanguage("cfgProcedureView.export.firstLeftHeader")
          , I18n.getLanguage("cfgProcedureView.export.secondLeftHeader")
          , I18n.getLanguage("cfgProcedureView.export.firstRightHeader")
          , I18n.getLanguage("cfgProcedureView.export.secondRightHeader")
      );
      cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("mrMngt.list.stt"),
          "HEAD", "STRING");

      List<CellConfigExport> lstCellSheet = new ArrayList<>();
      lstCellSheet.add(cellSheet);
      configfileExport.setLstCreatCell(lstCellSheet);
      configfileExport.setLangKey(I18n.getLocale());
      fileExports.add(configfileExport);
    }
    String fileTemplate = "";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    } else {
      fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT_EN.xlsx";
    }
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

  private void getMapUser() {
    if (mapUser.size() == 0) {
      UsersInsideDto usersInsideDtos = new UsersInsideDto();
      usersInsideDtos.setPage(1);
      usersInsideDtos.setPageSize(Integer.MAX_VALUE);
      List<UsersInsideDto> lstUserDTO = commonStreamServiceProxy
          .getListUserDTOByProxy(usersInsideDtos);
      if (lstUserDTO != null && !lstUserDTO.isEmpty()) {
        for (UsersInsideDto item : lstUserDTO) {
          if (item.getUserId() != null) {
            mapUser.put(String.valueOf(item.getUserId()), item);
          }
        }
      }
    }
  }

  private void getMapUnit() {
    if (mapUnit.size() == 0) {
      UnitDTO unitDTO = new UnitDTO();
      List<UnitDTO> lsUnit = new ArrayList<>();
      unitDTO.setStatus(1L);
      lsUnit = commonStreamServiceProxy.getListUnit(unitDTO);
      for (UnitDTO dto : lsUnit) {
        mapUnit.put(dto.getUnitId().toString(), dto);
        mapUnit.put(dto.getUnitCode(), dto);
      }
    }
  }

  private List<MrDTO> getListMrDTOSearch(MrSearchDTO mrSearchDTO) {
    setMapStateName();
    getMapUnit();
    getMapUser();
    if (mapCountry.size() == 0) {
      for (Integer i = 1; i <= 3; i++) {
        List<CatLocationDTO> lstCountry = commonStreamServiceProxy
            .getCatLocationByLevel(i.toString());
        if (lstCountry != null && !lstCountry.isEmpty()) {
          for (CatLocationDTO catLocationDTO : lstCountry) {
            mapCountry.put(catLocationDTO.getLocationId(), catLocationDTO);
          }
        }
      }
    }
    if (mapCdIdHard.size() == 0) {
      WoCdGroupTypeUserDTO woCdGroupTypeUserDTO = new WoCdGroupTypeUserDTO();
      List<WoCdGroupInsideDTO> lstCd = woCategoryServiceProxy
          .getListCdGroupByUser(woCdGroupTypeUserDTO);
      for (WoCdGroupInsideDTO cd : lstCd) {
        mapCdIdHard.put(cd.getWoGroupId().toString(), cd);
      }
    }
    List<MrDTO> lst = mrServiceRepository.getListMrCrWoNewForExport(mrSearchDTO);

    try {

      if (lst != null && !lst.isEmpty()) {
        List<MrDTO> lstNoteHard = new ArrayList<>();
        if ("H".equals(mrSearchDTO.getMrTypeName())) {
          List<MrDTO> lstTmp = mrServiceBusiness.getWorklogFromWo(mrSearchDTO);
          if (lstTmp != null && !lstTmp.isEmpty()) {
            lstNoteHard.addAll(lstTmp);
          }
        }

        for (MrDTO m : lst) {
          try {
            m.setState(mapGetName.get(m.getState()) == null ? Constants.MR_STATE_NAME_OLD.getText()
                .get(m.getState()) : mapGetName.get(m.getState()));
            if (!I18n.getLanguage("mrMngt.mrType.hardWO").equals(m.getMrType())) {
              m.setMrTypeName(I18n.getLanguage("mrMngt.list.mrSoft"));
            } else if ("".equals(m.getMrType())) {
              m.setMrTypeName("");
            } else {
              m.setMrTypeName(I18n.getLanguage("mrMngt.list.mrHard"));
            }
            String assignToPerson = m.getAssignToPerson();
            if (mapUser != null && !mapUser.isEmpty()) {
              if (!StringUtils.isStringNullOrEmpty(assignToPerson)) {
                UsersInsideDto user = mapUser.get(assignToPerson);
                String assignToPersonName = user != null ? user.getFullname() : "";
                m.setAssignToPersonName(assignToPersonName);
              }
            }
            if (I18n.getLanguage("mrMngt.list.mrSoft").equals(m.getMrTypeName())
                && (
                mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE).equalsIgnoreCase(m.getState())
                    || mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE7)
                    .equalsIgnoreCase(m.getState())
                    || mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE8)
                    .equalsIgnoreCase(m.getState())
                    || mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE9)
                    .equalsIgnoreCase(m.getState()))) {
              if (m.getChangeResponsible() != null && mapUser
                  .containsKey(m.getChangeResponsible())) {
                m.setAssignToPersonName(mapUser.get(m.getChangeResponsible()).getFullname());
                if (!mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE7)
                    .equalsIgnoreCase(m.getState())
                    && !mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE8)
                    .equalsIgnoreCase(m.getState())
                    && !mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE9)
                    .equalsIgnoreCase(m.getState())) {
                  m.setState(I18n.getLanguage("mrMngt.state.alreadyImpl"));
                }
              } else {
                if (!mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE7)
                    .equalsIgnoreCase(m.getState())
                    && !mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE8)
                    .equalsIgnoreCase(m.getState())
                    && !mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE9)
                    .equalsIgnoreCase(m.getState())) {
                  m.setState(I18n.getLanguage("mrMngt.state.notImpl"));
                }
                m.setAssignToPersonName("");
              }
            } else if (I18n.getLanguage("mrMngt.list.mrHard").equals(m.getMrTypeName())
                && mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE)
                .equalsIgnoreCase(m.getState())) {
              if (m.getFtId() != null && mapUser.containsKey(m.getFtId())) {
                m.setAssignToPersonName(mapUser.get(m.getFtId()).getFullname());
                m.setState(I18n.getLanguage("mrMngt.state.alreadyImpl"));
              } else {
                m.setState(I18n.getLanguage("mrMngt.state.notImpl"));
                m.setAssignToPersonName("");
              }
            }

            //namtn fix note from wo
            if ("H".equals(mrSearchDTO.getMrTypeName())) {
              if (!lstNoteHard.isEmpty()) {
                if (lstNoteHard.stream().filter(w -> w.getWoId().equals(m.getWoId())).findAny()
                    .isPresent()) {
                  m.setNote(
                      lstNoteHard.stream().filter(w -> w.getWoId().equals(m.getWoId())).findAny()
                          .get().getNote());
                }
              }
            }
            String responsibleUnit = m.getResponsibleUnitCR();
            if (responsibleUnit != null && mapUnit.containsKey(responsibleUnit)) {
              m.setResponsibleUnitCRName(mapUnit.get(responsibleUnit).getUnitName());
            } else {
              m.setResponsibleUnitCRName("");
            }

            String considerUnit = m.getConsiderUnitCR();
            if (considerUnit != null && mapUnit.containsKey(considerUnit)) {
              m.setConsiderUnitCRName(mapUnit.get(considerUnit).getUnitName());
            } else {
              m.setConsiderUnitCRName("");
            }

            String untiCreateMrName = m.getUnitCreateMr();
            if (untiCreateMrName != null && mapUnit.containsKey(untiCreateMrName)) {
              m.setUnitCreateMrName(mapUnit.get(untiCreateMrName).getUnitName());
            } else {
              m.setUnitCreateMrName("");
            }

            if (mapCountry != null && !mapCountry.isEmpty()) {
              if (!StringUtils.isStringNullOrEmpty(m.getCountry())) {
                CatLocationDTO country = mapCountry.get(m.getCountry());
                String countryName = country != null ? country.getLocationName() : "";
                m.setCountryName(countryName);
              }
              if (!StringUtils.isStringNullOrEmpty(m.getRegion())) {
                CatLocationDTO region = mapCountry.get(m.getRegion());
                String regionName = region != null ? region.getLocationName() : "";
                m.setRegionName(regionName);
              }
            }
            if (mapCdIdHard != null && !mapCdIdHard.isEmpty()) {
              if (!StringUtils.isStringNullOrEmpty(m.getCdGroupWo())) {
                WoCdGroupInsideDTO cd = mapCdIdHard.get(m.getCdGroupWo());
                String cdName = cd != null ? cd.getWoGroupName() : "";
                m.setCdGroupWoName(cdName);
              }
            }

          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public Datatable getListMrDTOSearchDatatable(MrSearchDTO mrSearchDTO) {
    setMapStateName();
//    getMapUnit();
    getMapUser();
//    if (mapCountry.size() == 0) {
//      for (Integer i = 1; i <= 3; i++) {
//        List<CatLocationDTO> lstCountry = commonStreamServiceProxy
//            .getCatLocationByLevel(i.toString());
//        if (lstCountry != null && !lstCountry.isEmpty()) {
//          for (CatLocationDTO catLocationDTO : lstCountry) {
//            mapCountry.put(catLocationDTO.getLocationId(), catLocationDTO);
//          }
//        }
//      }
//    }
    /*if (mapCdIdHard.size() == 0) {
      WoCdGroupTypeUserDTO woCdGroupTypeUserDTO = new WoCdGroupTypeUserDTO();
      List<WoCdGroupInsideDTO> lstCd = woCategoryServiceProxy
          .getListCdGroupByUser(woCdGroupTypeUserDTO);
      for (WoCdGroupInsideDTO cd : lstCd) {
        mapCdIdHard.put(cd.getWoGroupId().toString(), cd);
      }
    }*/
    Datatable datatable = mrServiceBusiness.getListMrCrWoNew(mrSearchDTO);
    List<MrDTO> lst = new ArrayList<>();
    if (datatable != null) {
      lst = (List<MrDTO>) datatable.getData();
    }

    try {

      if (lst != null && !lst.isEmpty()) {
//        List<MrDTO> lstNoteHard = new ArrayList<>();
//        if ("H".equals(mrSearchDTO.getMrTypeName())) {
//            List<MrDTO> lstTmp = mrServiceBusiness.getWorklogFromWo(mrSearchDTO);
//            if (lstTmp != null && !lstTmp.isEmpty()) {
//              lstNoteHard.addAll(lstTmp);
//          }
//        }
        List<String> tempCrIds = new ArrayList<>();
        Map<String, String> mapCrStatus = new HashMap<>();
        for (int i = 0; i < lst.size(); i++) {
          if (lst.get(i).getCrId() != null) {
            tempCrIds.add(lst.get(i).getCrId());
          }
          if ((i != 0 && i % 500 == 0) || i == lst.size() - 1) {
            List<CrHisDTO> lstTemp = maintenanceMngtRepository.findCrHisByListCrId(tempCrIds);
            if (lstTemp != null && lstTemp.size() > 0) {
              lstTemp.forEach(item -> {
                mapCrStatus.put(item.getCrId(), item.getChangeDate());
              });
            }
            tempCrIds.clear();
          }
        }
        for (MrDTO m : lst) {
          try {
            m.setState(mapGetName.get(m.getState()) == null ? Constants.MR_STATE_NAME_OLD.getText()
                .get(m.getState()) : mapGetName.get(m.getState()));
            if (!I18n.getLanguage("mrMngt.mrType.hardWO").equals(m.getMrType())) {
              m.setMrTypeName(I18n.getLanguage("mrMngt.list.mrSoft"));
            } else if ("".equals(m.getMrType())) {
              m.setMrTypeName("");
            } else {
              m.setMrTypeName(I18n.getLanguage("mrMngt.list.mrHard"));
            }
            String assignToPerson = m.getAssignToPerson();
            if (mapUser != null && !mapUser.isEmpty()) {
              if (!StringUtils.isStringNullOrEmpty(assignToPerson)) {
                UsersInsideDto user = mapUser.get(assignToPerson);
                String assignToPersonName = user != null ? user.getFullname() : "";
                m.setAssignToPersonName(assignToPersonName);
              }
            }
            if (I18n.getLanguage("mrMngt.list.mrSoft").equals(m.getMrTypeName())
                && (
                mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE).equalsIgnoreCase(m.getState())
                    || mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE7)
                    .equalsIgnoreCase(m.getState())
                    || mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE8)
                    .equalsIgnoreCase(m.getState())
                    || mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE9)
                    .equalsIgnoreCase(m.getState()))) {
              if (m.getChangeResponsible() != null && mapUser
                  .containsKey(m.getChangeResponsible())) {
                m.setAssignToPersonName(mapUser.get(m.getChangeResponsible()).getFullname());
                if (!mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE7)
                    .equalsIgnoreCase(m.getState())
                    && !mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE8)
                    .equalsIgnoreCase(m.getState())
                    && !mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE9)
                    .equalsIgnoreCase(m.getState())) {
                  m.setState(I18n.getLanguage("mrMngt.state.alreadyImpl"));
                }
              } else {
                if (!mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE7)
                    .equalsIgnoreCase(m.getState())
                    && !mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE8)
                    .equalsIgnoreCase(m.getState())
                    && !mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE9)
                    .equalsIgnoreCase(m.getState())) {
                  m.setState(I18n.getLanguage("mrMngt.state.notImpl"));
                }
                m.setAssignToPersonName("");
              }
            } else if (I18n.getLanguage("mrMngt.list.mrHard").equals(m.getMrTypeName())
                && mapGetNameNew.get(Constants.MR_STATE_NAME_OLD.CLOSE)
                .equalsIgnoreCase(m.getState())) {
              if (m.getFtId() != null && mapUser.containsKey(m.getFtId())) {
                m.setAssignToPersonName(mapUser.get(m.getFtId()).getFullname());
                m.setState(I18n.getLanguage("mrMngt.state.alreadyImpl"));
              } else {
                m.setState(I18n.getLanguage("mrMngt.state.notImpl"));
                m.setAssignToPersonName("");
              }
            }
            if (mapCrStatus.containsKey(m.getCrId())) {
              m.setMrCloseDate(mapCrStatus.get(m.getCrId()));
            }
            //namtn fix note from wo
//            if ("H".equals(mrSearchDTO.getMrTypeName())) {
//              if (!lstNoteHard.isEmpty()) {
//                if (lstNoteHard.stream().filter(w -> w.getWoId().equals(m.getWoId())).findAny()
//                    .isPresent()) {
//                  m.setNote(
//                      lstNoteHard.stream().filter(w -> w.getWoId().equals(m.getWoId())).findAny()
//                          .get().getNote());
//                }
//              }
//            }
            /*String responsibleUnit = m.getResponsibleUnitCR();
            if (responsibleUnit != null && mapUnit.containsKey(responsibleUnit)) {
              m.setResponsibleUnitCRName(mapUnit.get(responsibleUnit).getUnitName());
            } else {
              m.setResponsibleUnitCRName("");
            }

            String considerUnit = m.getConsiderUnitCR();
            if (considerUnit != null && mapUnit.containsKey(considerUnit)) {
              m.setConsiderUnitCRName(mapUnit.get(considerUnit).getUnitName());
            } else {
              m.setConsiderUnitCRName("");
            }

            String untiCreateMrName = m.getUnitCreateMr();
            if (untiCreateMrName != null && mapUnit.containsKey(untiCreateMrName)) {
              m.setUnitCreateMrName(mapUnit.get(untiCreateMrName).getUnitName());
            } else {
              m.setUnitCreateMrName("");
            }
*/
//            if (mapCountry != null && !mapCountry.isEmpty()) {
//              if (!StringUtils.isStringNullOrEmpty(m.getCountry())) {
//                CatLocationDTO country = mapCountry.get(m.getCountry());
//                String countryName = country != null ? country.getLocationName() : "";
//                m.setCountryName(countryName);
//              }
//              if (!StringUtils.isStringNullOrEmpty(m.getRegion())) {
//                CatLocationDTO region = mapCountry.get(m.getRegion());
//                String regionName = region != null ? region.getLocationName() : "";
//                m.setRegionName(regionName);
//              }
//            }
           /* if (mapCdIdHard != null && !mapCdIdHard.isEmpty()) {
              if (!StringUtils.isStringNullOrEmpty(m.getCdGroupWo())) {
                WoCdGroupInsideDTO cd = mapCdIdHard.get(m.getCdGroupWo());
                String cdName = cd != null ? cd.getWoGroupName() : "";
                m.setCdGroupWoName(cdName);
              }
            }*/

          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }
        datatable.setData(lst);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return datatable;
  }

  @Override
  public ResultInSideDto onInsert(MrInsideDTO mrInsideDTO) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    ResultInSideDto vadidateOk = validateAddForm(true, mrInsideDTO);
    if (RESULT.SUCCESS.equals(vadidateOk.getKey())) {
      //insert approve department
      UserToken userToken = TicketProvider.getUserToken();
//      userToken.setDeptId(userToken.get);
      MrApprovalDepartmentDTO approve1 = new MrApprovalDepartmentDTO();
      MrApprovalDepartmentDTO approve2 = new MrApprovalDepartmentDTO();
      setApproveDept(approve1, approve2, mrInsideDTO);
      String resApp1 = mrApprovalDepartmentServiceImpl.insertMrApprovalDepartment(approve1)
          .getMessage();
      String resApp2 = mrApprovalDepartmentServiceImpl.insertMrApprovalDepartment(approve2)
          .getMessage();
      String res = mrServiceBusiness.insertMr(mrInsideDTO).getKey();

      //save history add
      MrHisDTO hisDto = new MrHisDTO();
      hisDto.setMrId(mrInsideDTO.getMrId() == null ? null : String.valueOf(mrInsideDTO.getMrId()));
//        hisDto.setStatus(Constants.MR_STATE_CODE.OPEN);
      hisDto.setStatus(Constants.MR_STATE_CODE.INACTIVE);
      hisDto.setActionCode(Constants.MR_HIS_ACTION_CODE.CREATE);
      hisDto
          .setUserId(userToken.getUserID() == null ? null : String.valueOf(userToken.getUserID()));
      hisDto
          .setUnitId(userToken.getDeptId() == null ? null : String.valueOf(userToken.getDeptId()));

      String resStr =
          res != null && "SUCCESS".equals(res) && true ? I18n.getLanguage("common.success")
              : I18n.getLanguage("common.fail");
      if (approve1.getReturnCode() != null) {
        resStr +=
            "," + I18n.getLanguage("mrMngt.list.approve") + " " + I18n
                .getLanguage("common.success");
      }
      hisDto.setComments(
          I18n.getLanguage("common.button.add") + " MR: " + mrInsideDTO.getMrCode() + " " + resStr);
      //hisDto.setStatus(mrDTO.getState());
      hisDto.setChangeDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
      String resH = mrHisServiceBusiness.insertMrHis(hisDto).getKey();
      String valueCmbMrType = I18n.getLanguage("mrMngt.list.mrHard");
      List<InfraDeviceDTO> lstNode = mrInsideDTO.getLstNode();
      if (valueCmbMrType.equals(mrInsideDTO.getMrType())) {
        String resWoTemp = saveWoTemp(mrInsideDTO);
        String resNode = saveNodeNetwork(true, lstNode, mrInsideDTO);
        if ("SUCCESS".equals(res)
            && "SUCCESS".equals(resH)
            && "SUCCESS".equals(resApp1)
            && "SUCCESS".equals(resApp2)
            && "SUCCESS".equals(resWoTemp)) {
//           && "SUCCESS".equals(resNode)
//                MaintenanceSendMessageController.sendSMS(true, userTokenGNOC, mrDTO);
          String message =
              I18n.getLanguage("common.button.add") + " " + I18n.getLanguage("common.success");
          resultInSideDto.setMessage(message);
          resultInSideDto.setKey(RESULT.SUCCESS);
        } else {
          String message =
              I18n.getLanguage("common.button.add") + " " + I18n.getLanguage("common.fail");
          resultInSideDto.setMessage(message);
          resultInSideDto.setKey(RESULT.ERROR);
        }
      } else {
        String resNode = saveNodeNetwork(true, lstNode, mrInsideDTO);
        if ("SUCCESS".equals(res)
            && "SUCCESS".equals(resH)
            && "SUCCESS".equals(resApp1)
            && "SUCCESS".equals(resApp2)) {
//           && "SUCCESS".equals(resNode)
//                MaintenanceSendMessageController.sendSMS(true, userTokenGNOC, mrDTO);
          String message =
              I18n.getLanguage("common.button.add") + " " + I18n.getLanguage("common.success");
          resultInSideDto.setMessage(message);
          resultInSideDto.setKey(RESULT.SUCCESS);
        } else {
          String message =
              I18n.getLanguage("common.button.add") + " " + I18n.getLanguage("common.fail");
          resultInSideDto.setMessage(message);
          resultInSideDto.setKey(RESULT.ERROR);
        }
      }
    }
    return resultInSideDto;
  }

  public ResultInSideDto validateAddForm(boolean isAdd, MrInsideDTO ui) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    String stateCR = null;
    if (ui.getState().equals(Constants.MR_STATE_NAME.CLOSE)) {

      //0: UNASSIGNED, 1: ASSIGNED, 2:REJECT, 3:ACCEPT, 4: DISPATCH, 5: INPROCESS, 6: CLOSED
      try {
        MrWoTempDTO temp = new MrWoTempDTO();
        temp.setWoSystemId(ui.getMrId() == null ? null : String.valueOf(ui.getMrId()));
        List<MrWoTempDTO> lst = mrWoTempBusiness
            .getListMrWoTempDTO(temp, 0, 20, "asc", "woSystemId");
        if (ui.getMrTechnichcal() != null && ui.getMrTechnichcal()
            .equals("TBM")) {
          MrSchedulePeriodicDTO mspd = new MrSchedulePeriodicDTO();
          mspd.setMrId(ui.getMrId().toString());
          List<MrSchedulePeriodicDTO> lstSche = mrSchedulePeriodicBusiness
              .getListMrSchedulePeriodicDTO(mspd, 0, 1000, "asc", "position");
          if (lstSche != null && !lstSche.isEmpty()) {
            for (MrSchedulePeriodicDTO mspdo : lstSche) {
              //boolean isTrue = true;
              if (mspdo.getWoId() != null) {
                if (lst == null || lst.isEmpty()) {
                  resultInSideDto.setKey(RESULT.SUCCESS);
                  return resultInSideDto;
                }
                WoDTO wo = woServiceProxy
                    .findWoById(Long.valueOf(lst.get(0).getWoWfmId()));
                if (wo.getStatus() != null
                    && (wo.getStatus().equals("2") || wo.getStatus().equals("8"))) {
                  //return true;
                } else {
                  resultInSideDto.setKey(RESULT.ERROR);
                  resultInSideDto.setMessage(I18n.getLanguage("mr.wo.notfinish"));
                  return resultInSideDto;
                }
              }
            }
          }
        } else {
          if (lst == null || lst.isEmpty()) {
            resultInSideDto.setKey(RESULT.SUCCESS);
            return resultInSideDto;
          }
          WoDTO wo = woServiceProxy.findWoById(Long.valueOf(lst.get(0).getWoWfmId()));
          if (wo.getStatus() != null
              && (wo.getStatus().equals("2") || wo.getStatus().equals("8"))) {
            try {
              CrInsiteDTO crInsiteDTO = new CrInsiteDTO();
              crInsiteDTO.setSystemId(Long.valueOf(CrCreatedFromOtherSysDTO.SYSTEM.MR));
              crInsiteDTO.setObjectId(ui.getMrId());
              crInsiteDTO.setStepId(null);
              List<CrDTO> lstCr = crServiceProxy.getListCRFromOtherSystem(crInsiteDTO);
              if (lstCr != null && !lstCr.isEmpty()) {
                stateCR = lstCr.get(0).getState();
                System.out.println("CR State : " + stateCR);
                if (stateCR != null
                    && stateCR.equals(String.valueOf(Constants.CR_STATE.CLOSE))) {
                  resultInSideDto.setKey(RESULT.SUCCESS);
                  return resultInSideDto;
                } else if (stateCR != null
                    && !stateCR.equals(String.valueOf(Constants.CR_STATE.CLOSE)) && true) {
                  resultInSideDto.setKey(RESULT.ERROR);
                  resultInSideDto.setMessage(I18n.getLanguage("mrMngt.validate.cr.close"));
                  return resultInSideDto;
                } else {
                  resultInSideDto.setKey(RESULT.SUCCESS);
                  return resultInSideDto;
                }
              }
            } catch (Exception e) {
            }
            resultInSideDto.setKey(RESULT.SUCCESS);
            return resultInSideDto;
          } else {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getLanguage("mr.wo.notfinish"));
            return resultInSideDto;
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("mr.wo.status.invalid"));
        return resultInSideDto;
      }
    }
    return resultInSideDto;
  }

  public boolean validateApprove(MrInsideDTO ui) {
    if (!ui.getReason().equals("1") && (ui.getReasonDetail() == null || ui.getReasonDetail()
        .equals(""))) {
      return false;
    }
    return true;
  }

  public String saveWoTemp(MrInsideDTO mrDTO) throws Exception {
    UserToken userToken = TicketProvider.getUserToken();
    // Generate ma cong viec
    List<String> lstSeq = woServiceProxy.getSequenseWoProxy("WO_SEQ", 1);
    //ducdm1_fix lay sequence_04022016
    int nextVal = Integer.parseInt(lstSeq.get(0));

    String system = mrDTO.getWorkOrderField();
    String woCodeAdd = system + "_" + DateUtil.date2StringNoSlash(new Date()) + "_" + nextVal;
    String woContentAdd = mrDTO.getWorkContent();
    String createPerson = userToken.getUserName();

    // Set gia tri vao WO DTO
    WoDTO woDtoAdd = new WoDTO();
    if ("".equals(mrDTO.getWorkParent())) {
      woDtoAdd.setParentId(null);
    } else {
      woDtoAdd.setParentId(mrDTO.getWorkParent());
    }
    woDtoAdd.setWoTypeId(mrDTO.getWoType());
    woDtoAdd.setWoCode("WO_MR");
    woDtoAdd.setWoContent(woContentAdd);
    woDtoAdd.setWoSystem("MR");
    woDtoAdd.setWoId("1");

    if (mrDTO.getCdGroupWoName() != null) {
      String groupStateValue = mrDTO.getCdGroupWoName();
      String[] stateValue = groupStateValue.split(",");
      if (stateValue != null && stateValue.length > 0) {
        List<String> listState = new ArrayList<>();
        for (String s : stateValue) {
          if (s != null && !s.equals("")) {
            listState.add(s.trim());
          }
        }
        woDtoAdd.setCdIdList(listState);
      }
    }

    woDtoAdd.setWoSystemId(mrDTO.getSystemCode());
    woDtoAdd.setCreatePersonId(String.valueOf(userToken.getUserID()));
    woDtoAdd
        .setStartTime(DateUtil.date2ddMMyyyyHHMMss(mrDTO.getStartDateMr()));
    woDtoAdd.setEndTime(DateUtil.date2ddMMyyyyHHMMss(mrDTO.getEndDateMr()));
    woDtoAdd.setCreateDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    woDtoAdd.setPriorityId(mrDTO.getPriorityCode());
    woDtoAdd.setStatus(Constants.WO_STATUS.UNASSIGNED);
    ArrayList<String> lstFile = mrDTO.getUrl();
    String lstFileUpload = lstFile.toString();
    lstFileUpload = lstFileUpload.substring(1, lstFileUpload.length() - 1);
    woDtoAdd.setFileName(lstFileUpload == null ? "" : lstFileUpload.trim());
    woDtoAdd.setWoDescription(mrDTO.getDescription().trim() == null ? ""
        : mrDTO.getDescription().trim());

    try {
      WoDetailDTO woDetail = new WoDetailDTO();
      Date date = new Date();
      woDetail.setLastUpdateTime(date);
      woDetail.setCreateDate(date);
      UserToken user = TicketProvider.getUserToken();
      ResultInSideDto resultWO = insertWoFromWeb(woDtoAdd, woDetail, null, null, user.getUserName(),
          null);
      System.out.println(
          "--resultWO-- " + resultWO.getId() + " " + resultWO.getMessage() + " " + resultWO
              .getKey());
      String re = resultWO.getMessage();
      if ("SUCCESS".equals(resultWO.getMessage())) {
        return re;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public ResultInSideDto insertWoFromWeb(WoDTO woDTO, WoDetailDTO woDetail,
      List<WoMerchandiseDTO> lstMerchandise, WoKTTSInfoDTO woKttsInfo, String user,
      List<WoTestServiceMapDTO> lstWoTestService) {
    ResultInSideDto dto = new ResultInSideDto();
    try {
      //Date start = new Date();
      WoInsideDTO woInsideDTO = woDTO.toModelInSide();
      woInsideDTO.setWoDetailDTO(woDetail);
      woInsideDTO.setListWoMerchandiseInsideDTO(null);
      woInsideDTO.setWoKTTSInfoDTO(null);
      woInsideDTO.setLstWoTestService(null);
      dto = woServiceProxy
          .insertWoFromWebInMrMNGT(woInsideDTO);
      return dto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      dto.setMessage("NOK");
      return dto;
    }
  }


  public String saveNodeNetwork(boolean isAdd, List<InfraDeviceDTO> lstNetworkNode,
      MrInsideDTO mr) {
    String res = "";
    if (!isAdd) {
      System.out.println("Mr_ID : " + mr.getMrId());
      mrImpactedNodesBusiness
          .deleteImpactNodeByMrId(mr.getMrId() == null ? null : String.valueOf(mr.getMrId()));
    }
    String mrId = String.valueOf(mr.getMrId());
    List<MrImpactedNodesDTO> lst = getLstInsert(lstNetworkNode, mrId);
    res = mrImpactedNodesBusiness.insertOrUpdateListMrImpactedNodes(lst);
    return res;
  }

  public static List<MrImpactedNodesDTO> getLstInsert(List<InfraDeviceDTO> lstNetworkNode,
      String mrId) {
    List<MrImpactedNodesDTO> lst = new ArrayList<>();
    if (lstNetworkNode != null && !lstNetworkNode.isEmpty()) {
      for (InfraDeviceDTO node : lstNetworkNode) {
        MrImpactedNodesDTO mrNode = new MrImpactedNodesDTO();
        mrNode.setMrId(mrId);
        mrNode.setIpId(node.getIpId());
        mrNode.setDeviceId(node.getDeviceId());
        mrNode.setInsertTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        lst.add(mrNode);
      }
    }
    return lst;
  }

  public void setApproveDept(MrApprovalDepartmentDTO mrApp1, MrApprovalDepartmentDTO mrApp2,
      MrInsideDTO dto) {
    UserToken userToken = TicketProvider.getUserToken();
    mrApp1.setMadtLevel("1");
    mrApp1.setIncommingDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    mrApp1.setStatus("0");
    mrApp1.setMrId(dto.getMrId() == null ? null : String.valueOf(dto.getMrId()));

    MrApproveRolesDTO mrRole = new MrApproveRolesDTO();
    mrRole.setRoleCode("TP");
    mrRole
        .setUnitCode(userToken.getDeptId() == null ? null : String.valueOf(userToken.getDeptId()));
    mrRole.setUserId(String.valueOf(userToken.getUserID()));
    try {
      CrInsiteDTO crDTO = new CrInsiteDTO();
      crDTO.setSystemId(Long.valueOf(CrCreatedFromOtherSysDTO.SYSTEM.MR));
      crDTO.setObjectId(dto.getMrId() == null ? null : Long.valueOf(dto.getMrId()));
      List<CrDTO> lstCr = crServiceProxy
          .getListCRFromOtherSystem(crDTO);
      if (lstCr == null || lstCr.isEmpty()) {
        List<MrApproveRolesDTO> lstRole = mrApprovalDepartmentServiceImpl
            .getLstMrApproveUserByRole(mrRole);
        if (lstRole != null && !lstRole.isEmpty()) {
          mrApp1.setUserId(String.valueOf(userToken.getUserID()));
          mrApp1.setStatus("1");
          mrApp1.setApprovedDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
          mrApp1.setReturnCode(Constants.MR_STATE_CODE.QUEUE);
          mrApp1.setNotes(I18n.getLanguage("mrMngt.list.reason") + ": "
              + I18n.getLanguage("mr.approve.res1") + ", "
              + I18n.getLanguage("mrMngt.list.reasonDetail"));
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    mrApp2.setMadtLevel("2");
    mrApp2.setIncommingDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    mrApp2.setStatus("0");
    mrApp2.setMrId(dto.getMrId() == null ? null : String.valueOf(dto.getMrId()));

    List<MrApproveSearchDTO> lstAppDept = getApproveDept2level(userToken.getUserID());
    if (lstAppDept != null && !lstAppDept.isEmpty()) {
      try {
        mrApp1.setUnitId(lstAppDept.get(0).getUnitId());
        mrApp2.setUnitId(lstAppDept.get(1).getUnitId());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }

  }

  public List<MrApproveSearchDTO> getApproveDept2level(Long userId) {
    return
        maintenanceMngtRepository.getLstMrApproveDeptByUser(String.valueOf(userId));
  }

  @Override
  public ResultInSideDto onEdit(MrInsideDTO mrDTO) {
    UserToken userToken = TicketProvider.getUserToken();
    UsersInsideDto dto = userBusiness.getUserDetailById(userToken.getUserID());
    userToken.setDeptId(dto.getUnitId());
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    ResultInSideDto vadidateOk = validateAddForm(false, mrDTO);
    if (RESULT.SUCCESS.equals(vadidateOk.getKey())) {
      String mes = "";
      boolean isTPNoti = false;
      try {
        if (mrDTO.getIsTP()) {
          CrInsiteDTO crDTO = new CrInsiteDTO();
          crDTO.setSystemId(Long.valueOf(SYSTEM.MR));
          crDTO.setObjectId(mrDTO.getMrId() == null ? null : Long.valueOf(mrDTO.getMrId()));
          List<CrDTO> lstCr = crServiceProxy.getListCRFromOtherSystem(crDTO);
          if (lstCr == null
              || (lstCr != null && !lstCr.isEmpty()
              //                        && lstCr.get(0).getState().equals(String.valueOf(Constants.CR_STATE.QUEUE))
              && !lstCr.get(0).getState().equals(String.valueOf(CR_STATE.OPEN))
              && !lstCr.get(0).getState().equals(String.valueOf(CR_STATE.DRAFT)))) {
            MrApprovalDepartmentDTO approveDto = mrDTO.getMrApprovalDepartmentDTO();
            String res2 = mrApprovalDepartmentBusiness.updateMrApprovalDepartment(approveDto);
//                                                        String res2 = WSMrApprovalDepartment.updateMrApprovalDepartment(approveDto);
            if ("SUCCESS".equals(res2)) {
              approveAction(mrDTO, approveDto);
              isTPNoti = true;
            }
          }

        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      MrInsideDTO mrDto1 = mrDTO;
      boolean isFinish = isFinishApprove(mrDto1, userToken);
      if (isFinish) {
        if (checkState(mrDTO.getState(), MR_STATE_CODE.OPEN)) {
          mrDTO.setState(MR_STATE_CODE.QUEUE);
        }
        if (mrDTO.getState() != null && checkState(mrDTO.getState(), MR_STATE_CODE.ACTIVE)) {
          createWOAuto(mrDTO);
        }
      }

      ResultInSideDto res1 = mrServiceBusiness.updateMr(mrDTO);
      if ("SUCCESS".equals(res1.getKey())) {
        resultInSideDto.setKey(RESULT.SUCCESS);
        sendSMS(false, userToken, mrDTO);
        if (isTPNoti) {
          mes += I18n.getLanguage("mrMngt.list.approve") + " " + I18n.getLanguage("common.success");
          resultInSideDto.setMessage(mes);
        } else if (mrDTO.getState() != null
            && mrDTO.getState().equals(MR_STATE_CODE.CLOSE)) {
          mes +=
              I18n.getLanguage("common.button.wfm.close") + " " + I18n
                  .getLanguage("common.success");
          resultInSideDto.setMessage(mes);
        } else if (mrDTO.getState() != null
            && (
            checkState(mrDTO.getState(), MR_STATE_CODE.ACTIVE)
                || checkState(mrDTO.getState(), MR_STATE_CODE.INACTIVE))) {
          mes += I18n.getLanguage("common.switch.state") + " " + I18n.getLanguage("common.success");
          resultInSideDto.setMessage(mes);

        } else {
          mes +=
              I18n.getLanguage("common.editWindow.title") + " " + I18n
                  .getLanguage("common.success");
          resultInSideDto.setMessage(mes);
        }

      } else if (isTPNoti) {
        mes += I18n.getLanguage("mrMngt.list.approve") + " " + I18n.getLanguage("common.fail");
        resultInSideDto.setMessage(mes);
        resultInSideDto.setKey(RESULT.ERROR);
      } else if (mrDTO.getState() != null
          && !mrDTO.getState().equals(MR_STATE_CODE.CLOSE) && (true)) {
        mes += I18n.getLanguage("common.button.wfm.close") + " " + I18n.getLanguage("common.fail");
        resultInSideDto.setMessage(mes);
        resultInSideDto.setKey(RESULT.ERROR);
      } else if (mrDTO.getState() != null
          && (
          checkState(mrDTO.getState(), MR_STATE_CODE.ACTIVE)
              || checkState(mrDTO.getState(), MR_STATE_CODE.INACTIVE))
          && true) {
        mes += I18n.getLanguage("common.switch.state") + " " + I18n.getLanguage("common.fail");
        resultInSideDto.setMessage(mes);
        resultInSideDto.setKey(RESULT.ERROR);
      } else {
        mes += I18n.getLanguage("common.editWindow.title") + " " + I18n.getLanguage("common.fail");
        resultInSideDto.setMessage(mes);
        resultInSideDto.setKey(RESULT.ERROR);
      }
      saveMrHistory(mrDto1, mrDTO, isTPNoti);
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(vadidateOk.getMessage());
    }
    return resultInSideDto;
  }

  public void saveMrHistory(MrInsideDTO previous, MrInsideDTO mr, boolean isTP) {
    try {
      UserToken userToken = TicketProvider.getUserToken();
      UsersInsideDto dto = userBusiness.getUserDetailById(userToken.getUserID());
      userToken.setDeptId(dto.getUnitId());
      UnitDTO unitDTO = unitBusiness.findUnitById(userToken.getDeptId());
      MrHisDTO hisDto = new MrHisDTO();
      hisDto.setMrId(mr.getMrId() == null ? null : String.valueOf(mr.getMrId()));
      if (mr.getState() != null) {
        if (checkState(mr.getState(), Constants.MR_STATE_CODE.ACTIVE)) {
          hisDto.setStatus(Constants.MR_STATE_CODE.ACTIVE);
        } else if (checkState(mr.getState(), Constants.MR_STATE_CODE.CLOSE)) {
          hisDto.setStatus(Constants.MR_STATE_CODE.CLOSE);
        } else if (checkState(mr.getState(), Constants.MR_STATE_CODE.INACTIVE)) {
          hisDto.setStatus(Constants.MR_STATE_CODE.INACTIVE);
        } else if (checkState(mr.getState(), Constants.MR_STATE_CODE.QUEUE)) {
          hisDto.setStatus(Constants.MR_STATE_CODE.QUEUE);
        } else {
          hisDto.setStatus("1");
        }
      }
      if (
          checkState(mr.getState(), Constants.MR_STATE_CODE.OPEN)
              || checkState(mr.getState(), Constants.MR_STATE_CODE.INACTIVE_WAITTING)) {
        hisDto.setActionCode(Constants.MR_HIS_ACTION_CODE.EDIT);
        hisDto.setComments(I18n.getLanguage("mr.history.edit.content1") + " "
            + (Constants.MR_STATE_CODE.getGetText().get(mr.getState()) == null
            ? mr.getState() : Constants.MR_STATE_CODE.getGetText().get(mr.getState())));

        if (isTP) {
          hisDto.setActionCode(Constants.MR_HIS_ACTION_CODE.APPROVE);
          hisDto.setComments(
              I18n.getLanguage("mr.history.approve.content1") + " " + userToken
                  .getUserName() + "-- "
                  + unitDTO.getUnitName() + "--" + unitDTO.getUnitName()
                  + I18n.getLanguage("mr.history.approve.content2") + " "
                  + (Constants.MR_STATE_CODE.getGetText().get(mr.getState()) == null
                  ? mr.getState() : Constants.MR_STATE_CODE.getGetText().get(mr.getState()))
          );
        }
      } else if (checkState(mr.getState(), Constants.MR_STATE_CODE.QUEUE)) {
        if (isTP) {
          hisDto.setActionCode(Constants.MR_HIS_ACTION_CODE.APPROVE);

          hisDto.setComments(
              I18n.getLanguage("mr.history.approve.content1") + " " + userToken
                  .getUserName() + "-- "
                  + unitDTO.getUnitName() + "--" + unitDTO.getUnitName()
                  + I18n.getLanguage("mr.history.approve.content2") + " "
                  + (Constants.MR_STATE_CODE.getGetText().get(mr.getState()) == null
                  ? mr.getState() : Constants.MR_STATE_CODE.getGetText().get(mr.getState())));
        } else {
          hisDto.setStatus(Constants.MR_STATE_CODE.ACTIVE);
          hisDto.setActionCode(Constants.MR_HIS_ACTION_CODE.EXECUTE);

          hisDto.setComments(
              "MR " + I18n.getLanguage("mr.history.execute.content1") + " " + userToken
                  .getUserName() + "-- "
                  + userToken.getUserID()
                  + I18n.getLanguage("mr.history.execute.content2") + " "
                  + (Constants.MR_STATE_CODE.getGetText().get(mr.getState()) == null
                  ? mr.getState() : Constants.MR_STATE_CODE.getGetText().get(mr.getState()))
          );
        }
      } else if (mr.getState().toUpperCase().equals(Constants.MR_STATE_CODE.CLOSE)) {
        if (checkState(previous.getState(), Constants.MR_STATE_CODE.OPEN)) {
          hisDto.setActionCode(Constants.MR_HIS_ACTION_CODE.APPROVE);

          hisDto.setComments(
              I18n.getLanguage("mr.history.approve.content1") + " " + userToken
                  .getUserName() + "-- "
                  + unitDTO.getUnitName() + "--" + unitDTO.getUnitName()
                  + (Constants.MR_STATE_CODE.getGetText().get(mr.getState()) == null
                  ? mr.getState() : Constants.MR_STATE_CODE.getGetText().get(mr.getState())));
        } else {
          hisDto.setActionCode(Constants.MR_HIS_ACTION_CODE.CLOSE);
          hisDto.setComments(
              I18n.getLanguage("mr.history.close.content1") + " " + userToken.getUserName()
                  + "-- "
                  + userToken.getUserID()
                  + I18n.getLanguage("mr.history.execute.content2") + " "
                  + (Constants.MR_STATE_CODE.getGetText().get(mr.getState()) == null
                  ? mr.getState() : Constants.MR_STATE_CODE.getGetText().get(mr.getState())));
        }
      } else {
        hisDto.setActionCode(Constants.MR_HIS_ACTION_CODE.EXECUTE);

        hisDto.setComments(
            "MR " + I18n.getLanguage("mr.history.execute.content1") + " " + userToken
                .getUserName() + "-- "
                + userToken.getUserID()
                + I18n.getLanguage("mr.history.execute.content2") + " "
                + (Constants.MR_STATE_CODE.getGetText().get(mr.getState()) == null
                ? mr.getState() : Constants.MR_STATE_CODE.getGetText().get(mr.getState())));
      }
      hisDto.setChangeDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
      hisDto.setUserId(userToken.getUserID().toString());
      hisDto.setUnitId(userToken.getDeptId().toString());
      mrHisServiceBusiness.insertMrHis(hisDto).getMessage();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public void sendSMS(boolean isAdd, UserToken actor, MrInsideDTO mrDTO) {
    List<MessagesDTO> lstMsg = new ArrayList<>();
    boolean isTP = isTP(mrDTO, actor);
    String time = DateUtil.date2ddMMyyyyHHMMss(new Date());
    if (isAdd) {
      UnitDTO unitExecute = unitBusiness.findUnitById(actor.getDeptId());

      String contentAdd = title + time + " " + I18n.getLanguage("mrMngt.message.add") + ". "
          + I18n.getLanguage("mrMngt.list.mrCode") + " : " + mrDTO.getMrCode();
      MessagesDTO messagesDTO = setMessagesExecuteDTO(contentAdd, actor.getCellphone(),
          String.valueOf(actor.getUserID()), actor.getUserName(), unitExecute);
      lstMsg.add(messagesDTO);

      String contentAssig =
          title + time + " " + I18n.getLanguage("mrMngt.message.assign") + ". "
              + I18n.getLanguage("mrMngt.list.mrCode") + " : " + mrDTO.getMrCode();
      try {
        UsersInsideDto assigToPerson = userBusiness
            .getUserByUserId(Long.valueOf(mrDTO.getAssignToPerson())).toDTO();
        MessagesDTO messagesDTO1 = setMessagesExecuteDTO(contentAssig, assigToPerson.getMobile(),
            mrDTO.getAssignToPerson(), assigToPerson.getUsername(), unitExecute);
        lstMsg.add(messagesDTO1);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }

      List<MrApproveSearchDTO> lstApproveUnit = getApproveDept2level(actor.getUserID());
      if (lstApproveUnit != null && !lstApproveUnit.isEmpty()) {
        String contentApprove =
            title + time + " " + I18n.getLanguage("mrMngt.message.need.approve") + ". "
                + I18n.getLanguage("mrMngt.list.mrCode") + " : " + mrDTO.getMrCode();
        UnitDTO unitAprroveLv1 = unitBusiness
            .findUnitById(Long.valueOf(lstApproveUnit.get(0).getUnitId()));
        setMessageApprove(lstMsg, contentApprove, unitAprroveLv1);
      }

    } else {
      UnitDTO unitExecute = unitBusiness.findUnitById(actor.getDeptId());
      String content;
      if (mrDTO.getState() != null) {
        if (
            checkState(mrDTO.getState(), Constants.MR_STATE_CODE.OPEN)
                || checkState(mrDTO.getState(), Constants.MR_STATE_CODE.INACTIVE_WAITTING)
                || checkState(mrDTO.getState(), Constants.MR_STATE_CODE.QUEUE)) {
          boolean isFinishApprove = false;
          if (isTP) {
            content = title + time + " " + I18n.getLanguage("mrMngt.message.approved") + ". "
                + I18n.getLanguage("mrMngt.list.mrCode") + " : " + mrDTO.getMrCode();
            MessagesDTO messagesDTO = setMessagesExecuteDTO(content, actor.getCellphone(),
                String.valueOf(actor.getUserID()), actor.getUserName(), unitExecute);
            lstMsg.add(messagesDTO);

            MrApproveSearchDTO dto = new MrApproveSearchDTO();
            dto.setMrId(mrDTO.getMrId() == null ? null : String.valueOf(mrDTO.getMrId()));
            List<MrApproveSearchDTO> lst = mrApprovalDepartmentServiceImpl
                .getLstMrApproveSearch(dto);

            if (lst != null && !lst.isEmpty()) {
              for (MrApproveSearchDTO a : lst) {
                if (a.getMadtLevel() != null && a.getMadtLevel().equals("2")
                    && a.getStatus().equals("0")) {
                  String contentApprove =
                      title + time + " " + I18n.getLanguage("mrMngt.message.need.approve")
                          + ". "
                          + I18n.getLanguage("mrMngt.list.mrCode") + " : " + mrDTO.getMrCode();
                  setMessageApprove(lstMsg, contentApprove,
                      unitBusiness.findUnitById(Long.valueOf(a.getUnitId())));
                }
                isFinishApprove = a.getMadtLevel().equals("2") && a.getStatus().equals("1");
              }

            }
            //
            UsersInsideDto mrCreator = userBusiness
                .getUserByUserId(Long.valueOf(mrDTO.getCreatePersonId())).toDTO();
            if (mrCreator != null) {
              UnitDTO mrCreatorUnit = unitBusiness
                  .findUnitById(Long.valueOf(mrCreator.getUnitId()));
              String noticeApproveForCreator = "";
              if (isFinishApprove) {
                noticeApproveForCreator += title + time + " "
                    + I18n.getLanguage("mrMngt.list.mrCode") + " : " + mrDTO.getMrCode() + " "
                    + I18n.getLanguage("mrMngt.message.finish.approved") + " ";
              } else {
                noticeApproveForCreator += title + time + " "
                    + I18n.getLanguage("mrMngt.list.mrCode") + " : " + mrDTO.getMrCode() + " "
                    + I18n.getLanguage("mrMngt.message.approveUnit.approved") + " "
                    + actor.getUserName();
              }

              MessagesDTO messagesForCreator = setMessagesExecuteDTO(noticeApproveForCreator,
                  mrCreator.getMobile(),
                  String.valueOf(mrCreator.getUserId()), mrCreator.getUsername(), mrCreatorUnit);
              lstMsg.add(messagesForCreator);
            }
          } else {
            //edit
            content = title + time + " " + I18n.getLanguage("mrMngt.message.mr") + " " + mrDTO
                .getMrCode() + " "
                + I18n.getLanguage("mrMngt.message.edit") + " " + actor.getUserName();
            setMessageExecute(lstMsg, content, mrDTO, actor, unitExecute);
          }
        } else if (checkState(mrDTO.getState(), Constants.MR_STATE_CODE.CLOSE)) {
          content = title + time + " " + I18n.getLanguage("mrMngt.message.mr") + " " + mrDTO
              .getMrCode() + " "
              + I18n.getLanguage("mrMngt.message.close") + " " + actor.getUserName();
          setMessageExecute(lstMsg, content, mrDTO, actor, unitExecute);
        } else {
          content = title + time + " " + I18n.getLanguage("mrMngt.message.mr") + " " + mrDTO
              .getMrCode() + " "
              + I18n.getLanguage("mrMngt.message.execute") + " "
              + Constants.MR_STATE_CODE.getGetText().get(mrDTO.getState()) + " (" + actor
              .getUserName()
              + ")";
          setMessageExecute(lstMsg, content, mrDTO, actor, unitExecute);
        }
      }

    }

    if (!lstMsg.isEmpty()) {
      messagesBusiness.insertOrUpdateListMessages(lstMsg);
    }
  }

  //send Message cho nguoi tao vao nguoi nhan bao duong
  public void setMessageExecute(List<MessagesDTO> lstMsg, String content,
      MrInsideDTO mrDTO, UserToken actor, UnitDTO unitExecute) {
    MessagesDTO messagesDTO = setMessagesExecuteDTO(content, actor.getCellphone(),
        String.valueOf(actor.getUserID()), actor.getUserName(), unitExecute);
    lstMsg.add(messagesDTO);
    try {
      UsersInsideDto assigToPerson = userBusiness
          .getUserByUserId(Long.valueOf(mrDTO.getAssignToPerson())).toDTO();
      MessagesDTO messagesDTO1 = setMessagesExecuteDTO(content, actor.getCellphone(),
          mrDTO.getAssignToPerson(), assigToPerson.getUsername(), unitExecute);
      lstMsg.add(messagesDTO1);
    } catch (Exception e) {
    }
  }


  //lay so dien thoai truong phong
  public void setMessageApprove(List<MessagesDTO> lstMsg,
      String content, UnitDTO unitExecute) {
    MrApproveRolesDTO mrRole = new MrApproveRolesDTO();
    mrRole.setRoleCode("TP");
    mrRole.setUnitId(
        unitExecute.getUnitId() == null ? null : String.valueOf(unitExecute.getUnitId()));
    try {
      List<MrApproveRolesDTO> lstTP = mrApprovalDepartmentServiceImpl
          .getLstMrApproveUserByRole(mrRole);
      if (lstTP != null && !lstTP.isEmpty()) {
        for (MrApproveRolesDTO mard : lstTP) {
          UsersInsideDto u = userBusiness.getUserByUserId(Long.valueOf(mard.getUserId())).toDTO();
          MessagesDTO messagesDTO = new MessagesDTO();
          messagesDTO.setContent(content);
          messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
          messagesDTO.setReceiverId(String.valueOf(u.getUserId()));
          messagesDTO.setReceiverUsername(u.getUsername());
          messagesDTO.setReceiverPhone(u.getMobile());
          messagesDTO.setSmsGatewayId(
              unitExecute.getSmsGatewayId() == null ? null
                  : String.valueOf(unitExecute.getSmsGatewayId()));
          messagesDTO.setStatus("0");
          lstMsg.add(messagesDTO);
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public boolean isTP(MrInsideDTO mr, UserToken user) {
    boolean result = false;
    MrApproveRolesDTO mrRole = new MrApproveRolesDTO();
    mrRole.setRoleCode("TP");
    mrRole.setUnitCode(user.getDeptId() == null ? null : String.valueOf(user.getDeptId()));
    mrRole.setUserId(user.getUserID() == null ? null : String.valueOf(user.getUserID()));
    try {
      List<MrApproveRolesDTO> lstRole = mrApprovalDepartmentBusiness
          .getLstMrApproveUserByRole(mrRole);
      if (lstRole != null && !lstRole.isEmpty()) {
        return true;
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public MessagesDTO setMessagesExecuteDTO(String content, String phone,
      String userId, String userName, UnitDTO unitExecute) {
    MessagesDTO messagesDTO = new MessagesDTO();
    messagesDTO.setContent(content);
    messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    messagesDTO.setReceiverId(userId);
    messagesDTO.setReceiverUsername(userName);
    messagesDTO.setReceiverPhone(phone);
    messagesDTO.setSmsGatewayId(unitExecute.getSmsGatewayId() == null ? null
        : String.valueOf(unitExecute.getSmsGatewayId()));
    messagesDTO.setStatus("0");
    return messagesDTO;
  }

  public static boolean checkState(String mrState, String state) {
    switch (state) {
      case Constants.MR_STATE_CODE.OPEN:
        return mrState.equals(Constants.MR_STATE_CODE.OPEN)
            || mrState.equals(Constants.MR_STATE_NAME_OLD.OPEN)
            || mrState.equals(Constants.MR_STATE_NAME.OPEN);
      case Constants.MR_STATE_CODE.INACTIVE_WAITTING:
        return mrState.equals(Constants.MR_STATE_CODE.INACTIVE_WAITTING)
            || mrState.equals(Constants.MR_STATE_NAME_OLD.INACTIVE_WAITTING)
            || mrState.equals(Constants.MR_STATE_NAME.INACTIVE_WAITTING);
      case Constants.MR_STATE_CODE.QUEUE:
        return mrState.equals(Constants.MR_STATE_CODE.QUEUE)
            || mrState.equals(Constants.MR_STATE_NAME_OLD.QUEUE)
            || mrState.equals(Constants.MR_STATE_NAME.QUEUE);
      case Constants.MR_STATE_CODE.ACTIVE:
        return mrState.equals(Constants.MR_STATE_CODE.ACTIVE)
            || mrState.equals(Constants.MR_STATE_NAME_OLD.ACTIVE)
            || mrState.equals(Constants.MR_STATE_NAME.ACTIVE);
      case Constants.MR_STATE_CODE.INACTIVE:
        return mrState.equals(Constants.MR_STATE_CODE.INACTIVE)
            || mrState.equals(Constants.MR_STATE_NAME_OLD.INACTIVE)
            || mrState.equals(Constants.MR_STATE_NAME.INACTIVE);
      case Constants.MR_STATE_CODE.CLOSE:
        return mrState.equals(Constants.MR_STATE_CODE.CLOSE)
            || mrState.equals(Constants.MR_STATE_NAME_OLD.CLOSE)
            || mrState.equals(Constants.MR_STATE_NAME.CLOSE);
      default:
        break;
    }
    return false;
  }

  public void createWOAuto(MrInsideDTO mrDto) {
    try {
      UserToken userToken = TicketProvider.getUserToken();
      MrWoTempDTO temp = new MrWoTempDTO();
      temp.setWoSystemId(mrDto.getMrCode());
      List<MrWoTempDTO> lst = mrWoTempBusiness
          .getListMrWoTempDTO(temp, 0, 20, "asc", "woSystemId");
      if (lst != null && !lst.isEmpty()) {
        MrWoTempDTO woTemp = new MrWoTempDTO();
        woTemp = lst.get(0);
        if (woTemp.getWoWfmId() == null) {

          // Generate ma cong viec
          List<String> lstSeq = woServiceProxy.getSequenseWoProxy("WO_SEQ", 1);
          int nextVal = Integer.parseInt(lstSeq.get(0));
          // Set gia tri vao WO DTO
          WoDTO woDtoAdd = new WoDTO();
          woDtoAdd.setWoId(String.valueOf(nextVal));
          woDtoAdd.setWoTypeId(woTemp.getWoTypeId());
          woDtoAdd.setWoCode(woTemp.getWoCode());
          woDtoAdd.setWoContent(woTemp.getWoContent());
          woDtoAdd.setWoSystem(woTemp.getWoSystem());
          woDtoAdd.setStationCode(woTemp.getStationCode());
          woDtoAdd.setCdId(woTemp.getCdId());
          woDtoAdd.setWoSystemId(woTemp.getWoSystemId());
          woDtoAdd.setCreatePersonId(woTemp.getCreatePersonId());
          woDtoAdd.setStartTime(woTemp.getStartTime());
          woDtoAdd.setEndTime(woTemp.getEndTime());
          woDtoAdd.setCreateDate(DateTimeUtils.convertDateTimeStampToString(new java.util.Date()));
          woDtoAdd.setPriorityId(woTemp.getPriorityId());
          woDtoAdd.setStatus(woTemp.getStatus());
          woDtoAdd.setFileName(woTemp.getFileName() == null ? "" : woTemp.getFileName());
          woDtoAdd.setWoDescription(woTemp.getWoDescription());

          //  Set gia tri vao HISTORY DTO
          WoHistoryDTO woDtoHis = new WoHistoryDTO();
          woDtoHis.setNewStatus(Constants.WO_STATUS.UNASSIGNED);
          woDtoHis.setWoId("" + nextVal);
          woDtoHis.setWoCode(woTemp.getWoCode());
          woDtoHis.setWoContent(woTemp.getWoContent());
          woDtoHis.setUserName(userToken.getUserName());
          woDtoHis.setComments("");

          ResultDTO resultWO = woServiceProxy.insertWoProxy(woDtoAdd);

          // Check ket qua insert
          if (resultWO.getMessage() != null
              && "SUCCESS".equals(resultWO.getMessage())
              && true) {
            woTemp.setWoWfmId(resultWO.getId());
            mrDto.setWoId(resultWO.getId());
            mrWoTempBusiness.updateMrWoTemp(woTemp);
          }
          //}
          if (mrDto.getMrTechnichcal() != null && mrDto.getMrTechnichcal().equals("TBM")) {
            List<MrSchedulePeriodicDTO> lstSche = new ArrayList<>();
            MrSchedulePeriodicDTO schedule = new MrSchedulePeriodicDTO();
            schedule.setMrId(mrDto.getMrId() == null ? null : String.valueOf(mrDto.getMrId()));
            schedule.setUserId(mrDto.getCreatePersonId() == null ? null
                : String.valueOf(mrDto.getCreatePersonId()));
            schedule.setTimeInsert(DateUtil.date2ddMMyyyyHHMMss(new Date()));
            schedule.setIsSend("1");
            schedule.setTimeWoStart(woTemp.getStartTime());
            schedule.setTimeWoEnd(woTemp.getEndTime());
            schedule.setPosition("1");
            schedule.setWoId(String.valueOf(nextVal));
            lstSche.add(schedule);

            Date woStartTimeAuto = new Date();
            Date firstDate = new Date();
            if (firstDate.before(mrDto.getEarliestTime())) {
              firstDate = mrDto.getEarliestTime();
            }

            int count = 1;
            int i = 2;
            while (woStartTimeAuto.before(mrDto.getLastestTime())) {
              Calendar c = Calendar.getInstance();
              c.setTime(firstDate);
              //c.add(Calendar.DATE, count);  // number of days to add

              if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.day"))) {
                c.add(Calendar.DATE, count);
              } else if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.week"))) {
                c.add(Calendar.WEEK_OF_MONTH, count);
              } else if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.month"))) {
                c.add(Calendar.MONTH, count);
              } else if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.quarter"))) {
                c.add(Calendar.MONTH, 3 * count);
              } else if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.halfYear"))) {
                c.add(Calendar.MONTH, 6 * count);
              } else if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.year"))) {
                c.add(Calendar.YEAR, count);
              } else {
                break;
              }
              woStartTimeAuto = c.getTime();
//                            System.out.println("woStartTimeAuto : " + DateUtil.date2ddMMyyyyHHMMss(woStartTimeAuto));
              if (woStartTimeAuto.after(mrDto.getLastestTime())) {
                break;
              }

              MrSchedulePeriodicDTO sch = new MrSchedulePeriodicDTO();
              sch.setUserId(mrDto.getCreatePersonId() == null ? null
                  : String.valueOf(mrDto.getCreatePersonId()));
              sch.setMrId(mrDto.getMrId() == null ? null : String.valueOf(mrDto.getMrId()));
              sch.setUserId(mrDto.getCreatePersonId() == null ? null
                  : String.valueOf(mrDto.getCreatePersonId()));
              sch.setTimeInsert(DateUtil.date2ddMMyyyyHHMMss(new Date()));
              sch.setTimeWoStart(
                  getNewDate(DateUtil.string2DateTime(woTemp.getStartTime()), count, mrDto));
              sch.setTimeWoEnd(
                  getNewDate(DateUtil.string2DateTime(woTemp.getEndTime()), count, mrDto));
              sch.setPosition(String.valueOf(i));
              sch.setIsSend("0");
              lstSche.add(sch);
              count++;
              i++;
            }
            try {
              for (MrSchedulePeriodicDTO ms : lstSche) {
                try {
                  String insert = mrSchedulePeriodicBusiness.insertMrSchedulePeriodic(ms)
                      .getMessage();
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                }
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public String getNewDate(Date time, int addTime, MrInsideDTO mrDto) {
    Calendar c1 = Calendar.getInstance();
    c1.setTime(time);
    //c1.add(Calendar.DATE, addTime);  // number of days to add
    if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.day"))) {
      c1.add(Calendar.DATE, addTime);
    } else if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.week"))) {
      c1.add(Calendar.WEEK_OF_MONTH, addTime);
    } else if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.month"))) {
      c1.add(Calendar.MONTH, addTime);
    } else if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.year"))) {
      c1.add(Calendar.YEAR, addTime);
    } else if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.quarter"))) {
      c1.add(Calendar.MONTH, 3 * addTime);
    } else if (mrDto.getInterval().equals(I18n.getLanguage("mrMngt.halfYear"))) {
      c1.add(Calendar.MONTH, 6 * addTime);
    }
    return DateUtil.date2ddMMyyyyHHMMss(c1.getTime());
  }

  private boolean isFinishApprove(MrInsideDTO dto, UserToken user) {
    try {
      MrApproveSearchDTO s = new MrApproveSearchDTO();
      s.setMrId(dto.getMrId() == null ? null : String.valueOf(dto.getMrId()));
      List<MrApproveSearchDTO> lst = mrApprovalDepartmentBusiness.getLstMrApproveSearch(s);
      if (lst != null && !lst.isEmpty()) {
        for (MrApproveSearchDTO m : lst) {
          if (m.getMadtLevel() != null && m.getMadtLevel().equals("2")
              && m.getStatus() != null && m.getStatus().equals("1")) {
            return true;
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }

  private void approveAction(MrInsideDTO mrDTO, MrApprovalDepartmentDTO mrApp) {
    try {
      if (mrApp.getReturnCode().equals(Constants.MR_STATE_CODE.REJECT)) {
//                mrDto.setState(Constants.MR_STATE_NAME.CLOSE);
        mrDTO.setState(Constants.MR_STATE_CODE.CLOSE);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  // initTabApprove
  @Override
  public List<MrApproveSearchDTO> initTabApprove(MrDTO mrDTO) {
    UserToken user = TicketProvider.getUserToken();
    Map<String, String> map = new HashMap<>();
    map.put("3", I18n.getLanguage("mr.approve.yes"));
    map.put("6", I18n.getLanguage("mr.approve.no"));
    List<MrApproveSearchDTO> lst = new ArrayList<>();
    if (mrDTO != null && true) {
      MrApproveSearchDTO approveDept = new MrApproveSearchDTO();
      approveDept.setMrId(mrDTO.getMrId());
      //reload data
      try {
        List<MrApproveSearchDTO> lstTem = mrApprovalDepartmentServiceImpl
            .getLstMrApproveSearch(approveDept);
        if (lstTem != null && !lstTem.isEmpty()) {
          for (MrApproveSearchDTO s : lstTem) {
            s.setReturnCode(
                map.get(s.getReturnCode()) == null ? s.getReturnCode() : map.get(s.getReturnCode())
            );
            s.setStatus(s.getStatus() != null && s.getStatus().equals("1") && true
                ? I18n.getLanguage("mr.isApprove.yes")
                : I18n.getLanguage("mr.isApprove.no"));
            lst.add(s);
          }
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
    } else {
      MrApproveSearchDTO app1 = new MrApproveSearchDTO();
      MrApproveSearchDTO app2 = new MrApproveSearchDTO();

      List<MrApproveSearchDTO> lstApp2level = getApproveDept2level(user.getUserID());
      if (lstApp2level != null && !lstApp2level.isEmpty()) {
        try {
          app1.setUnitName(lstApp2level.get(0).getUnitName());
          app2.setUnitName(lstApp2level.get(1).getUnitName());
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
      app1.setMadtLevel("1");
      app1.setStatus(I18n.getLanguage("mr.isApprove.no"));
      app2.setMadtLevel("2");
      app2.setStatus(I18n.getLanguage("mr.isApprove.no"));
      lst.add(app1);
      lst.add(app2);
    }
    return lst;
  }

  @Override
  public List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO lstCondition) {
    return userGroupCategoryRepository.getListUserGroupBySystem(lstCondition);
  }

  @Override
  public List<WorkLogCategoryInsideDTO> getListWorkLogCategoryDTO(
      WorkLogCategoryInsideDTO workLogCategoryDTO) {
    List<WorkLogCategoryInsideDTO> lst = new ArrayList<>();
    try {
      int rowStart = 0;
      int maxRow = 200;
      String sortType = "";
      String sortFieldList = "";
      if (workLogCategoryDTO != null) {
        lst = workLogCategoryRepository
            .getListWorkLogCategoryDTO(workLogCategoryDTO, rowStart, maxRow, sortType,
                sortFieldList);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public MrInsideDTO findById(Long id) {
    MrInsideDTO mrInsideDTO = new MrInsideDTO();
    UsersEntity usersEntity = null;
    mrInsideDTO = maintenanceMngtRepository.findById(id);
    try {
      if (!StringUtils.isStringNullOrEmpty(mrInsideDTO.getCreatePersonId())) {
        usersEntity = userRepository.getUserByUserId(Long.valueOf(mrInsideDTO.getCreatePersonId()));
        mrInsideDTO.setCreatePersonName(
            usersEntity.getFullname() + " (" + usersEntity.getUsername() + ") ");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return mrInsideDTO;
  }

  @Override
  public boolean isCheckEdit(MrInsideDTO mrInsideDTO, String userId) {
    if (((mrInsideDTO.getCreatePersonId() != null && String.valueOf(mrInsideDTO.getCreatePersonId())
        .equals(userId))
        || mrInsideDTO.getAssignToPerson() != null && mrInsideDTO.getAssignToPerson()
        .equals(userId)) && true) {
      return true;
    } else {
      try {
        MrApproveSearchDTO appSearch = new MrApproveSearchDTO();
        appSearch.setMrId(String.valueOf(mrInsideDTO.getMrId()));
        List<MrApproveSearchDTO> lstApp = mrApprovalDepartmentBusiness
            .getLstMrApproveSearch(appSearch);
        if (lstApp != null && !lstApp.isEmpty()) {
          for (MrApproveSearchDTO mrApp : lstApp) {
            try {
              MrApproveRolesDTO role = new MrApproveRolesDTO();
              role.setUnitCode(mrApp.getUnitCode());
              role.setRoleCode("TP");
              List<MrApproveRolesDTO> lstRole = mrApprovalDepartmentBusiness
                  .getLstMrApproveUserByRole(role);
              if (lstRole != null && !lstRole.isEmpty()) {
                return true;
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
          }
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return false;
  }

  @Override
  public ResultInSideDto insertWorkLogProxy(WorkLogInsiteDTO workLogInsiteDTO) {
    return workLogRepository.insertWorkLog(workLogInsiteDTO.toEntity());
  }

  @Override
  public ResultInSideDto insertWorkLog(WorkLogInsiteDTO workLogInsiteDTO) {
    ResultInSideDto result = new ResultInSideDto();
    UserToken user = TicketProvider.getUserToken();
    boolean validate = validate(workLogInsiteDTO);
    Long state = null;
    if (validate) {
      try {
        if ("2".equals(String.valueOf(workLogInsiteDTO.getWlgObjectType()))) {
          CrInsiteDTO crDTO = crServiceProxy.findCrByIdProxy(workLogInsiteDTO.getWlgObjectId());
          if (crDTO != null) {
            state = Long.valueOf(crDTO.getState());
          }
          workLogInsiteDTO.setWlgObjectState(state);
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
      result = workLogRepository.insertWorkLog(workLogInsiteDTO.toEntity());
      if (result.getKey().equals(RESULT.SUCCESS)) {
        try {
          if (Constants.WORK_LOG_CAT.WLAY_ID_HTTD
              .equals(String.valueOf(workLogInsiteDTO.getWlayId()))
              && String.valueOf(workLogInsiteDTO.getWlgObjectType()).equals("2")) {
            crServiceProxy
                .sendSMSToLstUserConfig(workLogInsiteDTO.getWlgObjectId().toString(), "1");
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        String resolveSuccessUserGroupCategoryId = "11";
        String resolveSuccessWorklogCategoryId = "79";

//        try {
//          resolveSuccessUserGroupCategoryId = resolveSuccessUserGroupCategoryId;
//          resolveSuccessWorklogCategoryId =resolveSuccessUserGroupCategoryId;
//        } catch (Exception e) {
//          log.error(e.getMessage(), e);
//        }

        if (resolveSuccessUserGroupCategoryId
            .equalsIgnoreCase(workLogInsiteDTO.getUserGroupAction().toString())
            && resolveSuccessWorklogCategoryId
            .equalsIgnoreCase(workLogInsiteDTO.getWlayId().toString())) {
          //Goi sang NOCpro để cập nhập trạng thái CR
          try {
            System.out.println("Call Nocpro WS step 0");
            NocProWS nocProWS = new NocProWS();
            ResponseBO responseBO = nocProWS
                .updateCrToFinishImpact(String.valueOf(workLogInsiteDTO.getWlgObjectId()),
                    user.getUserName());
            if (responseBO != null) {
              System.out.println(
                  "Nocpto Response : " + responseBO.getKey() + " : " + responseBO.getDescription());
            } else {
              System.out.println("Can not get Nocpto Response");
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }

        }
      }
      result.setKey(RESULT.SUCCESS);
      result.setMessage(I18n.getLanguage("mr.worklog.createS"));
    } else {
      result.setKey(RESULT.ERROR);
      result.setMessage(I18n.getLanguage("mr.worklog.createF"));
    }
    return result;
  }

  @Override
  public List<WorkLogInsiteDTO> getListWorkLogDTO(WorkLogInsiteDTO dto) {
    List<WorkLogInsiteDTO> list = workLogRepository.getListWorkLogDTO(dto);
    try {
      Double offset = dto.getOffset();
      SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      if (list != null) {
        for (WorkLogInsiteDTO work : list) {
          if (work.getCreatedDate() != null) {
            Date d = work.getCreatedDate();
            String timeTmp = spd.format(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
            work.setCreatedDate(DateTimeUtils.convertStringToDate(timeTmp));
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO) {
    return woCategoryServiceProxy.getListCdGroupByUser(woCdGroupTypeUserDTO);
  }

  public boolean validate(WorkLogInsiteDTO workLogInsiteDTO) {
    return true;
  }

  public void setMapStateName() {
    mapGetName.put(MR_STATE_NAME_OLD.OPEN, I18n.getLanguage("mrMngt.state.open"));
    mapGetName.put(MR_STATE_NAME_OLD.INACTIVE_WAITTING,
        I18n.getLanguage("mrMngt.state.inactive_waitting"));
    mapGetName.put(MR_STATE_NAME_OLD.QUEUE, I18n.getLanguage("mrMngt.state.queue"));
    mapGetName.put(MR_STATE_NAME_OLD.ACTIVE, I18n.getLanguage("mrMngt.state.active"));
    mapGetName.put(MR_STATE_NAME_OLD.INACTIVE, I18n.getLanguage("mrMngt.state.inactive"));
    mapGetName.put(MR_STATE_NAME_OLD.CLOSE, I18n.getLanguage("mrMngt.state.close"));
    mapGetName.put(MR_STATE_NAME_OLD.CLOSE7, I18n.getLanguage("mrMngt.state.close7"));
    mapGetName.put(MR_STATE_NAME_OLD.CLOSE8, I18n.getLanguage("mrMngt.state.close8"));
    mapGetName.put(MR_STATE_NAME_OLD.CLOSE9, I18n.getLanguage("mrMngt.state.close9"));
    mapGetName.put(MR_STATE_NAME_OLD.CLOSE10, I18n.getLanguage("mrMngt.state.close10"));
    mapGetName.put(MR_STATE_NAME_OLD.CLOSE11, I18n.getLanguage("mrMngt.state.close11"));
    mapGetName.put(MR_STATE_NAME_OLD.CLOSE12, I18n.getLanguage("mrMngt.state.close12"));
    mapGetName.put(MR_STATE_NAME_OLD.CLOSE13, I18n.getLanguage("mrMngt.state.close13"));
    mapGetName.put(MR_STATE_NAME_OLD.CLOSE14, I18n.getLanguage("mrMngt.state.close14"));
    mapGetName.put(MR_STATE_NAME_OLD.CLOSE15, I18n.getLanguage("mrMngt.state.close15"));
  }

  @Override
  public ResultDTO updateMrStatus(String crId, String woId) {
    ResultDTO res = new ResultDTO();
    try {
      if (StringUtils.isNotNullOrEmpty(woId) && StringUtils.isNotNullOrEmpty(crId)) {
        res = new ResultDTO("0", RESULT.FAIL, "Input either : CR Id or WO Id");
        return res;
      }

      if (StringUtils.isStringNullOrEmpty(woId) && StringUtils.isStringNullOrEmpty(crId)) {
        res = new ResultDTO("0", RESULT.FAIL, "CR Id or WO Id is not null");
        return res;
      }

      if (!StringUtils.isStringNullOrEmpty(crId) && StringUtils.isStringNullOrEmpty(woId)) {
        if (Long.valueOf(crId) > 0L) {
          List<MrDTO> lstCrId = mrRepository.checkExistCrId(crId);
          if (lstCrId != null && lstCrId.size() > 0) {
            MrDTO dtoUpdateCr = new MrDTO();
            dtoUpdateCr.setMrId(lstCrId.get(0).getMrId());
            if (!StringUtils.isStringNullOrEmpty(dtoUpdateCr.getMrId())) {
              res = mrRepository.updateMrStatus(dtoUpdateCr);
            }
          } else {
            res = new ResultDTO("0", RESULT.FAIL, "CR Id is incorrect");
          }
        }
      }

      if (!StringUtils.isStringNullOrEmpty(woId) && StringUtils.isStringNullOrEmpty(crId)) {
        if (Long.valueOf(woId) > 0L) {
          List<MrDTO> lstWoId = mrRepository.checkExistWoId(woId);
          if (lstWoId.size() > 0) {
            MrDTO dtoUpdateWo = new MrDTO();
            dtoUpdateWo.setMrId(lstWoId.get(0).getMrId());
            if (!StringUtils.isStringNullOrEmpty(dtoUpdateWo.getMrId())) {
              res = mrRepository.updateMrStatus(dtoUpdateWo);
            }
          } else {
            res = new ResultDTO("0", RESULT.FAIL, "WO Id is incorrect");
          }
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res = new ResultDTO("0", RESULT.FAIL, e.getMessage());
    }
    return res;
  }

  @Override
  public ResultDTO reCreatedOrCloseCr(String crId, String status) {
    ResultDTO res = new ResultDTO();
    try {
      if (mrRepository.checkCrIdInMrNode(crId)) {
        return reCreatedOrCloseCrSoft(crId, status);
      }
      if (!StringUtils.isStringNullOrEmpty(crId)) {
        MrDTO mrDTO = mrRepository.getMrForClose(crId);
        if (mrDTO != null) {
          // Muon dong MR
          if ("1".equals(status)) {
            if (mrRepository.getCountCrNotClose(mrDTO.getMrId()) == 0) {

            } else {
              res = new ResultDTO("0", "FAIL", "Cr : Not close");
            }
          } // Tao lai CR
          else if ("0".equals(status)) {
            // Lay danh sach CR can tao lai, CR chinh se nam o dau danh sach
            List<CrDTO> lstCr = mrRepository.getListCrOfMr(mrDTO.getMrId());

            if (lstCr != null) {

              MrITSoftScheduleDTO mrScheduleDTO = mrITSoftScheduleRepository
                  .getByMrId(mrDTO.getMrId());
              MrITSoftProcedureDTO mrCfgProcedureDTO =
                  mrScheduleDTO != null ? mrITSoftProcedureRepository
                      .getDetail(Long.valueOf(mrScheduleDTO.getProcedureId())) : null;

              String primaryCrId = null;
              for (CrDTO cr : lstCr) {
                CrInsiteDTO objCR = crServiceProxy
                    .getCrByIdOutSide(ticketProvider.getUserToken(), Long.valueOf(cr.getCrId()));

                if (objCR != null) {
                  CrInsiteDTO objRecreated = objCR;
                  objRecreated.setState("2"); // tao lai cr
                  List<String> lstSequence = crServiceProxy.getSequenseCrProxy("cr_seq", 1);
                  if (lstSequence != null && !lstSequence.isEmpty()) {
                    objRecreated.setCrId(lstSequence.get(0));

                    if ("1".equals(objCR.getIsPrimaryCr())) {
                      // Neu la CR chinh thi luu lai ID de gan lai cho cac CR phu
                      primaryCrId = lstSequence.get(0);
                    } else if (!StringUtils.isStringNullOrEmpty(objCR.getRelateToPrimaryCr())) {
                      objRecreated.setRelateToPrimaryCr(primaryCrId);
                    }
                  }
                  int endIndex = objCR.getCrNumber().lastIndexOf("_");
                  String crNumber =
                      objCR.getCrNumber().substring(0, endIndex) + "_" + objRecreated.getCrId();
                  objRecreated.setCrNumber(crNumber);
                  objRecreated.setRelateCr(objCR.getRelateCr());

                  String earliestStartTime = DateTimeUtils.convertDateToString(DateUtil
                      .addDay(new Date(), (mrCfgProcedureDTO != null) ?
                          (mrCfgProcedureDTO.getReGenMrAfter() != null) ? mrCfgProcedureDTO
                              .getReGenMrAfter().intValue() : 1 : 1), "dd/MM/yyyy");
                  String latestStartTime = DateTimeUtils.convertDateToString(DateUtil
                      .addDay(DateTimeUtils.convertStringToTime(earliestStartTime, "dd/MM/yyyy"),
                          1), "dd/MM/yyyy");
                  objRecreated.setEarliestStartTime(
                      DateTimeUtils.convertStringToDate(earliestStartTime + " 23:00:00"));
                  objRecreated.setLatestStartTime(
                      DateTimeUtils.convertStringToDate(latestStartTime + " 04:59:00"));

                  if ("1".equals(objCR.getServiceAffecting())) {
                    objRecreated.setDisturbanceStartTime(
                        DateTimeUtils.convertStringToDate(latestStartTime + " 00:00:00"));
                    objRecreated.setDisturbanceEndTime(
                        DateTimeUtils.convertStringToDate(latestStartTime + " 00:04:00"));
                  } else {
                    objRecreated.setDisturbanceStartTime(null);
                    objRecreated.setDisturbanceEndTime(null);
                  }
                  objRecreated.setActionRight(objCR.getActionRight());

                  CrCreatedFromOtherSysDTO ccfosdto = new CrCreatedFromOtherSysDTO();
                  ccfosdto.setCrId(objRecreated.getCrId());
                  ccfosdto.setIsActive("1");
                  ccfosdto.setObjectId(mrDTO.getMrId());
                  ccfosdto.setSystemId("1");
                  objRecreated.setCrCreatedFromOtherSysDTO(ccfosdto);

                  objRecreated.setCreatedDate(new Date());
                  objRecreated.setUpdateTime(new Date());

                  UsersDTO objUser = mrRepository
                      .getUsersOfUnit(Long.parseLong(objCR.getChangeResponsibleUnit()));
                  if (objUser != null) {
                    objRecreated.setUserLogin(objUser.getUserId());
                  } else {
                    objRecreated.setUserLogin(objCR.getUserLogin());
                  }
                  objRecreated.setUserLoginUnit(objCR.getChangeResponsibleUnit());
                  ResultInSideDto resultCr = crServiceProxy.insertCrOutSide(objRecreated);
                  if (resultCr != null && Constants.CR_RETURN_MESSAGE.SUCCESS
                      .equalsIgnoreCase(resultCr.getKey())) {
                    //HÃ nh Ä‘á»™ng giao tháº©m Ä‘inh
                    CrInsiteDTO objVerify = objRecreated;
                    objVerify.setActionType("8");
                    objVerify.setUserLogin(objVerify.getChangeOrginator());
                    objVerify.setUserLoginUnit(objVerify.getChangeOrginatorUnit());
                    crServiceProxy.actionVerifyMrITOutSide(objVerify);

                    if (mrScheduleDTO != null) {
                      InsertFileDTO insertFile = mrRepository.getCrFileInsert(mrScheduleDTO);
                      if (insertFile != null) {
                        String filePath = insertFile
                            .getFileContent();//file path luu trong file content tu ben kia
                        byte[] source = FileUtils
                            .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                                PassTranformer.decrypt(ftpPass), filePath);
                        insertFile.setFileContent(Base64.encode(source));
                        crServiceProxy.insertFile(insertFile);
                      }
                    }

                    try {
                      boolean isExists = mrRepository.checkCrExists(mrDTO.getMrId(), cr.getCrId());
                      if (!isExists) {
                        // Insert vao bangr MR_CR_RECREATED
                        MrCrRecreatedDTO mrCrReCreatedDTO = new MrCrRecreatedDTO();
                        mrCrReCreatedDTO.setMrId(mrDTO.getMrId());
                        mrCrReCreatedDTO.setCrId(cr.getCrId());
                        mrCrRecreatedRepository.insert(mrCrReCreatedDTO);
                      }
                    } catch (Exception e) {
                      log.error(e.getMessage(), e);
                    }

                    try {
                      List<WoDTO> lstWo = mrRepository.getListWoOfCr(cr.getCrId());
                      if (lstWo.size() > 0) {
                        for (WoDTO wo : lstWo) {
                          WoDTO objRecreate = wo;
                          List<String> lstSeq = woServiceProxy.getSequenseWoProxy("WO_SEQ", 1);
                          int nextVal = Integer.parseInt(lstSeq.get(0));
                          objRecreate.setWoId("" + nextVal);
                          String woCodeAdd =
                              "WO_CR_" + DateTimeUtils.convertDateToString(new Date(), "yyyyMMdd")
                                  + "_" + nextVal;
                          objRecreate.setWoCode(woCodeAdd);

                          objRecreate.setWoSystemId(objRecreated.getCrId());

                          objRecreate.setStartTime(DateTimeUtils
                              .date2ddMMyyyyHHMMss(objRecreated.getEarliestStartTime()));
                          objRecreate.setEndTime(
                              DateTimeUtils.date2ddMMyyyyHHMMss(objRecreated.getLatestStartTime()));
                          objRecreate.setStatus(Constants.WO_STATUS.DRAFT);

                          //manhpd1 thoi gian tao Wo = New Date + 1 second
                          Calendar calNow = Calendar.getInstance();
                          calNow.setTime(new Date());
                          calNow.add(Calendar.SECOND, 1);
                          objRecreate
                              .setCreateDate(DateTimeUtils.date2ddMMyyyyHHMMss(calNow.getTime()));

                          //woServicesImpl.insertWoForSPM(objRecreate);
                          ResultDTO resultWo = woServiceProxy.insertWoProxy(objRecreate);
                          if (resultWo != null && Constants.CR_RETURN_MESSAGE.SUCCESS
                              .equalsIgnoreCase(resultWo.getMessage())) {
                            String[] temps = resultWo.getId().split("_");
                            String id = temps[temps.length - 1];

                            //  Set gia tri vao HISTORY DTO
                            WoHistoryInsideDTO woDtoHis = new WoHistoryInsideDTO();
                            woDtoHis.setNewStatus(Long.valueOf(Constants.WO_STATUS.DRAFT));
                            woDtoHis.setWoId(Long.valueOf(id));
                            woDtoHis.setWoCode(objRecreate.getWoCode());
                            woDtoHis.setWoContent(objRecreate.getWoContent());

                            UsersEntity obj = userRepository
                                .getUserByUserId(Long.valueOf(objRecreate.getCreatePersonId()));
                            if (obj != null) {
                              woDtoHis.setUserName(obj.getUsername());
                            } else {
                              woDtoHis.setUserName("gnoc_admin");
                            }
                            woDtoHis.setUpdateTime(new Date());
                            woServiceProxy.insertWoHistory(woDtoHis);
                          }
                        }
                      }
                    } catch (Exception e) {
                      log.error(e.getMessage(), e);
                    }
                    res.setMessage(Constants.CR_RETURN_MESSAGE.SUCCESS);
                  } else {
                    if (resultCr != null) {
                      res = new ResultDTO("0", "FAIL", resultCr.getMessage());
                    }
                    break;
                  }
                }
              }
            } else {
              res = new ResultDTO("0", "FAIL", "lstCr : Not found cr list");
            }
          } else {
            res = new ResultDTO("0", "FAIL", "Status : Invalid");
          }
        } else {
          res = new ResultDTO("0", "FAIL", "Mr : Not found");
        }
      } else {
        res = new ResultDTO("0", "FAIL", "CrId : NULL");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res = new ResultDTO("0", "FAIL", e.getMessage());
    }
    return res;
  }


  public ResultDTO reCreatedOrCloseCrSoft(String crId, String status) {
    ResultDTO res = new ResultDTO();
    try {
      MrDTO mrDTO = mrRepository.getMrForClose(crId);
      if ("1".equalsIgnoreCase(status)) {
        if (mrDTO != null) {
          if (mrRepository.getCountCrNotClose(mrDTO.getMrId()) == 0) {
            // Dong Mr

//                        boolean isOK = gettDAO().updateMrState(gettDAO().getSession(), mrDTO.getMrId(), "6", res);
//                        if (isOK) {
//                            MrHisBusiness mrHisBusiness = new MrHisBusiness();
//                            MrHisDTO mrHisDTO = new MrHisDTO();
//                            mrHisDTO.setStatus("6");
//                            mrHisDTO.setChangeDate(DateTimeUtils.convertDateToString(new Date()));
//                            mrHisDTO.setComments("Đóng MR");
//                            mrHisDTO.setMrId(mrDTO.getMrId());
//                            mrHisDTO.setNotes("Đóng MR sau khi CR cuối hoàn thành");
//                            mrHisDTO.setActionCode("4");
            // Cap nhat du lieu vao bang MR_HIS
//                            mrHisBusiness.createNewObject(new MrHisDAO(gettDAO().getSession()), mrHisDTO);
            mrNodesRepository.updateWoStatus(null, crId, null, status, null, true);

//                            MrScheduleTelDAO mrScheduleTelDAO = new MrScheduleTelDAO(gettDAO().getSession());
//                            List<MrScheduleTelDTO> lstSchedule = mrScheduleTelDAO.getByMrId(gettDAO().getSession(), mrDTO.getMrId());
//                            if (lstSchedule != null) {
//                                for (MrScheduleTelDTO mrScheduleTelDTO : lstSchedule) {
//                                    MrScheduleTelHisBusiness mrScheduleTelHisBusiness = new MrScheduleTelHisBusiness();
//
//                                    MrScheduleTelHisDTO mrScheduleTelHisDTO = new MrScheduleTelHisDTO();
//                                    mrScheduleTelHisDTO.setMarketCode(mrScheduleTelDTO.getMarketCode());
//                                    mrScheduleTelHisDTO.setArrayCode(mrScheduleTelDTO.getArrayCode());
//                                    mrScheduleTelHisDTO.setDeviceType(mrScheduleTelDTO.getDeviceType());
//                                    mrScheduleTelHisDTO.setDeviceId(mrScheduleTelDTO.getDeviceId());
//                                    mrScheduleTelHisDTO.setDeviceCode(mrScheduleTelDTO.getDeviceCode());
//                                    mrScheduleTelHisDTO.setNetworkType(mrScheduleTelDTO.getNetworkType());
//                                    mrScheduleTelHisDTO.setDeviceName(mrScheduleTelDTO.getDeviceName());
//                                    mrScheduleTelHisDTO.setMrDate(DateTimeUtils.date2ddMMyyyyString(new Date()));
//                                    mrScheduleTelHisDTO.setMrContent(mrScheduleTelDTO.getMrContentId());
//                                    mrScheduleTelHisDTO.setMrMode(mrScheduleTelDTO.getMrMode());
//                                    mrScheduleTelHisDTO.setMrType(mrScheduleTelDTO.getMrType());
//                                    mrScheduleTelHisDTO.setMrId(mrScheduleTelDTO.getMrId());
//                                    mrScheduleTelHisDTO.setMrCode(mrScheduleTelDTO.getMrCode());
//                                    mrScheduleTelHisDTO.setCrId(mrScheduleTelDTO.getCrId());
//                                    mrScheduleTelHisDTO.setCrNumber(mrScheduleTelDTO.getCrNumber());
//                                    // mrScheduleTelHisDTO.setImportantLevel(mrScheduleTelDTO.getLevelImportant());
//                                    mrScheduleTelHisDTO.setProcedureId(mrScheduleTelDTO.getProcedureId());
//                                    mrScheduleTelHisDTO.setProcedureName(mrScheduleTelDTO.getProcedureName());
//
//                                    // Cap nhat vao bang HIS
//                                    ResultDTO objResultHis = mrScheduleTelHisBusiness.createNewObject(new MrScheduleTelHisDAO(gettDAO().getSession()), mrScheduleTelHisDTO);
//                                    if (ParamUtils.SUCCESS.equals(objResultHis.getMessage())) {
//                                        // Xoa du lieu o bang MR_SCHEDULE_TEL
//                                        mrScheduleTelDAO.deleteMrScheduleTel(gettDAO().getSession(), mrScheduleTelDTO.getScheduleId());
//                                    }
//
//                                    // Cap nhat bang trang thai cho thiet bi
//                                    MrDeviceBusiness deviceBusiness = new MrDeviceBusiness();
//                                    deviceBusiness.updateStatus(gettDAO().getSession(), mrScheduleTelDTO, "1");
//
//                                    res = new ResultDTO("0", ParamUtils.SUCCESS, "Mr : close success");
//                                }
//                            }
//                        } else {
//                            res = new ResultDTO("0", "FAIL", "Cr : Not close");
//                        }
          }
        }
      } else {
        // Tao lai CR
        MrScheduleTelDTO mrScheduleTelDTO = mrScheduleTelRepository.getByMrId(mrDTO.getMrId())
            .get(0);
        MrCfgProcedureTelDTO mrCfgProcedureTelDTO = null;
        try {
          mrCfgProcedureTelDTO =
              mrScheduleTelDTO != null ? mrCfgProcedureTelRepository
                  .getDetail(Long.valueOf(mrScheduleTelDTO.getProcedureId())) : null;
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }

        CrInsiteDTO objCR = crServiceProxy
            .getCrByIdOutSide(ticketProvider.getUserToken(), Long.valueOf(crId));
        if (objCR != null) {
          CrInsiteDTO objRecreated = objCR;
          objRecreated.setLstNetworkNodeId(new ArrayList<>());
          objRecreated.setState("2"); // táº¡o láº¡i cr
          List<String> lstSequence = crServiceProxy.getSequenseCrProxy("cr_seq", 1);
          if (lstSequence != null && !lstSequence.isEmpty()) {
            objRecreated.setCrId(lstSequence.get(0));
          }
          int endIndex = objCR.getCrNumber().lastIndexOf("_");
          String crNumber = objCR.getCrNumber().substring(0, endIndex) + "_" + objCR.getCrId();
          objRecreated.setCrNumber(crNumber);
          objRecreated.setRelateCr(objCR.getRelateCr());
          List<MrNodesDTO> listNode = mrNodesRepository.getListNodeNOK(crId);
          List<MrNodesDTO> listNodeNew = new ArrayList<>();

          String title = objCR.getTitle();
          int ind = title.length() - 1;
          for (int index = title.length() - 1; index >= 0; index--) {
            if (title.charAt(index) == ' ') {
              ind = index;
              break;
            }
          }
          String titlePrefix = title.substring(0, ind);
          title = titlePrefix + " ";
          if (listNode != null) {
            List<CrImpactedNodesDTO> listCrImpactNodes = new ArrayList<CrImpactedNodesDTO>();
            for (MrNodesDTO item : listNode) {
              CrImpactedNodesDTO crImpactNodes = new CrImpactedNodesDTO();
              crImpactNodes.setCrId(objRecreated.getCrId());
              crImpactNodes.setDeviceId(item.getNodeCode());
              crImpactNodes.setIpId(item.getNodeIp());
              crImpactNodes.setInsertTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
              listCrImpactNodes.add(crImpactNodes);

              MrNodesDTO newNode = item;
              newNode.setCrId(objRecreated.getCrId());
              //newNode.setMrId(null);
              listNodeNew.add(newNode);
              title = title + item.getNodeName() + ";";
            }
            mrNodesRepository.insertMrNodeByDTO(listNodeNew);
            objRecreated.getLstNetworkNodeId().addAll(listCrImpactNodes);
          }
          String earliestStartTime = DateTimeUtils.convertDateToString(DateUtil.addDay(new Date(),
              mrCfgProcedureTelDTO != null ? Integer.parseInt(
                  mrCfgProcedureTelDTO.getReGenMrAfter() != null ? String
                      .valueOf(mrCfgProcedureTelDTO.getReGenMrAfter()) : "1") : 1), "dd/MM/yyyy");
          String latestStartTime = DateTimeUtils.convertDateToString(DateUtil
                  .addDay(DateTimeUtils.convertStringToTime(earliestStartTime, "dd/MM/yyyy"), 1),
              "dd/MM/yyyy");
          objRecreated.setEarliestStartTime(
              DateTimeUtils.convertStringToDate(earliestStartTime + " 23:00:00"));
          objRecreated
              .setLatestStartTime(DateTimeUtils.convertStringToDate(latestStartTime + " 04:59:00"));

          if (title.length() > 500) {
            title = title.substring(0, 500);
          }
          objRecreated.setTitle(title);

          if ("1".equals(objCR.getServiceAffecting())) {
            objRecreated.setDisturbanceStartTime(
                DateTimeUtils.convertStringToDate(latestStartTime + " 00:00:00"));
            objRecreated.setDisturbanceEndTime(
                DateTimeUtils.convertStringToDate(latestStartTime + " 00:04:00"));

            objRecreated.getLstAffectedService().addAll(getAffSer(objCR, objRecreated.getCrId()));
          } else {
            objRecreated.setDisturbanceStartTime(null);
            objRecreated.setDisturbanceEndTime(null);
          }
          objRecreated.setActionRight(objCR.getActionRight());

          CrCreatedFromOtherSysDTO ccfosdto = new CrCreatedFromOtherSysDTO();
          ccfosdto.setCrId(objRecreated.getCrId());
          ccfosdto.setIsActive("1");
          ccfosdto.setObjectId(mrDTO.getMrId());
          ccfosdto.setSystemId("1");
          objRecreated.setCrCreatedFromOtherSysDTO(ccfosdto);

          objRecreated.setCreatedDate(new Date());
          objRecreated.setUpdateTime(new Date());

          UsersDTO objUser = mrRepository
              .getUsersOfUnit(Long.parseLong(objCR.getChangeResponsibleUnit()));
          if (objUser != null) {
            objRecreated.setUserLogin(objUser.getUserId());
          } else {
            objRecreated.setUserLogin(objCR.getUserLogin());
          }
          objRecreated.setUserLoginUnit(objCR.getChangeResponsibleUnit());

          objRecreated.setRelateCr(Constants.CR_RELATED.SECONDARY);
          objRecreated.setRelateToPrimaryCr(crId);
          objRecreated.setIsPrimaryCr(null);
          objRecreated.setUserCab(null);
          ResultInSideDto resultCr = crServiceProxy.insertCrOutSide(objRecreated);
          if (resultCr != null && Constants.CR_RETURN_MESSAGE.SUCCESS
              .equalsIgnoreCase(resultCr.getKey())) {
            //HÃ nh Ä‘á»™ng giao tháº©m Ä‘inh
//                        CrDTO objVerify = objRecreated;
            objRecreated.setActionType("8");
            objRecreated.setUserLogin(objRecreated.getChangeOrginator());
            objRecreated.setUserLoginUnit(objRecreated.getChangeOrginatorUnit());
            objRecreated.setProxyLocale(I18n.getLocale());//them locale sang ben CR
            crServiceProxy.actionVerifyMrITOutSide(objRecreated);

            if (mrScheduleTelDTO != null) {
              InsertFileDTO insertFile = mrRepository.getCrFileTelInsert(mrScheduleTelDTO);

              if (insertFile != null) {
                String filePath = insertFile
                    .getFileContent();//file path luu trong file content tu ben kia
                byte[] source = FileUtils
                    .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                        PassTranformer.decrypt(ftpPass), filePath);
                insertFile.setFileContent(Base64.encode(source));
                crServiceProxy.insertFile(insertFile);
              }
            }

            try {
              List<WoDTO> lstWo = mrRepository.getListWoOfCr(crId);
              if (lstWo.size() > 0) {

                for (WoDTO wo : lstWo) {
                  WoDTO objRecreate = wo;
                  List<String> lstSeq = woServiceProxy.getSequenseWoProxy("WO_SEQ", 1);
                  int nextVal = Integer.parseInt(lstSeq.get(0));
                  objRecreate.setWoId("" + nextVal);
                  String woCodeAdd =
                      "WO_CR_" + DateTimeUtils.convertDateToString(new Date(), "yyyyMMdd") + "_"
                          + nextVal;
                  objRecreate.setWoCode(woCodeAdd);

                  objRecreate.setWoSystemId(objRecreated.getCrId());
                  objRecreate.setWoContent(title);
                  objRecreate.setStartTime(
                      DateTimeUtils.date2ddMMyyyyHHMMss(objRecreated.getEarliestStartTime()));
                  objRecreate.setEndTime(
                      DateTimeUtils.date2ddMMyyyyHHMMss(objRecreated.getLatestStartTime()));
                  objRecreate.setStatus(Constants.WO_STATUS.DRAFT);

                  Calendar calNow = Calendar.getInstance();
                  calNow.setTime(new Date());
                  calNow.add(Calendar.SECOND, 1);
                  objRecreate.setCreateDate(DateTimeUtils.date2ddMMyyyyHHMMss(calNow.getTime()));

                  //woServicesImpl.insertWoForSPM(objRecreate);
                  ResultDTO resultWo = woServiceProxy.insertWoProxy(objRecreate);
                  if (resultWo != null && Constants.CR_RETURN_MESSAGE.SUCCESS
                      .equalsIgnoreCase(resultWo.getMessage())) {
                    String[] temps = resultWo.getId().split("_");
                    String id = temps[temps.length - 1];

                    //  Set gia tri vao HISTORY DTO
                    WoHistoryInsideDTO woDtoHis = new WoHistoryInsideDTO();
                    woDtoHis.setNewStatus(Long.valueOf(Constants.WO_STATUS.DRAFT));
                    woDtoHis.setWoId(Long.valueOf(id));
                    woDtoHis.setWoCode(objRecreate.getWoCode());
                    woDtoHis.setWoContent(objRecreate.getWoContent());
                    UsersEntity obj = userRepository
                        .getUserByUserId(Long.valueOf(objRecreate.getCreatePersonId()));
                    if (obj != null) {
                      woDtoHis.setUserName(obj.getUsername());
                    } else {
                      woDtoHis.setUserName("gnoc_admin");
                    }

                    woDtoHis.setUpdateTime(new Date());
                    woServiceProxy.insertWoHistory(woDtoHis);
                  }
                }
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }

            res.setMessage(Constants.CR_RETURN_MESSAGE.SUCCESS);
          } else if (resultCr != null) {
            res = new ResultDTO("0", "FAIL", resultCr.getMessage());
          }
        }
//                    }
//                } else {
//                    res = new ResultDTO("0", "FAIL", "lstCr : Not found cr list");
//                }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res = new ResultDTO("0", RESULT.FAIL, e.getMessage());
    }
    return res;
  }

  // Láº¥y danh sÃ¡ch dá»‹ch vá»¥ áº£nh hÆ°á»¡ng cá»§a CR cÅ©, gÃ¡n cho CR má»›i
  public List<CrAffectedServiceDetailsDTO> getAffSer(CrInsiteDTO objOld, String newCrId) {
    List<CrAffectedServiceDetailsDTO> lstOld = objOld.getLstAffectedService();
    List<CrAffectedServiceDetailsDTO> lstNew = new ArrayList<>();
    if (lstOld != null) {
      for (CrAffectedServiceDetailsDTO objASOld : lstOld) {
        CrAffectedServiceDetailsDTO casddto = new CrAffectedServiceDetailsDTO();
        casddto.setCrId(newCrId);
        casddto.setInsertTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
        casddto.setAffectedServiceId(objASOld.getAffectedServiceId());
        lstNew.add(casddto);
      }
      return lstNew;
    }

    return null;
  }

  @Override
  public MrClientDetail getMrChartInfoForNOC(MrForNocSearchDTO mrSearchDTO) {
    return mrRepository.getMrChartInfoForNOC(mrSearchDTO);
  }

  @Override
  public List<MrDTO> getListMrForMobile(MrMobileDTO dto) {
    return mrRepository.getListMrForMobile(dto);
  }

  @Override
  public List<MrNodesDTO> getWoCrNodeList(String woId, String crId) {
    return mrNodesRepository.getWoCrNodeList(woId, crId);
  }

  @Override
  public List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp(String woId, String mrNodeId) {
    return mrNodesRepository.getListMrNodeChecklistForPopUp(woId, mrNodeId);
  }

  @Override
  public ResultInSideDto updateMrNodeChecklistForPopUp(
      List<MrNodeChecklistDTO> lstMrNodeChecklistDTO) {
    return mrNodesRepository.updateMrNodeChecklistForPopUp(lstMrNodeChecklistDTO);
  }

  @Override
  public ResultInSideDto updateWoCrNodeStatus(List<MrNodesDTO> lstNodes) {
    ResultInSideDto resultInSideDto;
    try {
      if (lstNodes != null && lstNodes.size() > 0) {
        for (MrNodesDTO nodes : lstNodes) {
          mrNodesRepository.updateWoStatus(nodes.getWoId(), nodes.getCrId(), nodes.getNodeCode(), nodes.getStatus(), nodes.getComments(), false);
        }
        resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, "");
      } else {
        resultInSideDto = new ResultInSideDto(null, RESULT.ERROR, I18n.getLanguage("mrMngt.lstNode.empty"));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto = new ResultInSideDto(null, RESULT.ERROR, e.getMessage());
    }
    return resultInSideDto;
  }

  @Override
  public List<MrNodeChecklistFilesDTO> getListFileMrNodeChecklist_VS(String nodeChecklistId) {
    return mrNodesRepository.getListFileMrNodeChecklist_VS(nodeChecklistId);
  }
}
