package com.viettel.gnoc.cr.util;

import com.viettel.aam.LinkCrResult;
import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.RoleUserBusiness;
import com.viettel.gnoc.commons.business.RolesBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RoleUserDTO;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.incident.provider.WSSPMProvidePort;
import com.viettel.gnoc.commons.incident.provider.WSSecurityPort;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.CR_ACTION_CODE;
import com.viettel.gnoc.commons.utils.Constants.CR_FILE_TYPE;
import com.viettel.gnoc.commons.utils.Constants.CR_STATE;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.business.CrForOtherSystemBusiness;
import com.viettel.gnoc.cr.business.CrImpactedNodesBusiness;
import com.viettel.gnoc.cr.dto.AttachDtDTO;
import com.viettel.gnoc.cr.dto.CoordinationSettingDTO;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFileObject;
import com.viettel.gnoc.cr.dto.CrFileObjectInsite;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.TempImportDataDTO;
import com.viettel.gnoc.cr.dto.UserCabCrForm;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.cr.repository.CoordinationSettingRepository;
import com.viettel.gnoc.cr.repository.CrDtRepository;
import com.viettel.gnoc.cr.repository.CrFilesAttachRepository;
import com.viettel.gnoc.cr.repository.CrGeneralRepository;
import com.viettel.gnoc.cr.repository.CrImpactedNodesRepository;
import com.viettel.gnoc.cr.repository.CrProcessRepository;
import com.viettel.gnoc.cr.repository.CrProcessWoRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.gnoc.cr.repository.ImportDataRepository;
import com.viettel.gnoc.cr.thread.SendFailedWoThread;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.gnoc.ws.provider.WSGatePort;
import com.viettel.gnoc.ws.provider.WSTDTTPort;
import com.viettel.gnoc.ws.provider.WSVipaDdPort;
import com.viettel.gnoc.ws.provider.WSVipaIpPort;
import com.viettel.security.PassTranformer;
import com.viettel.security.service.CrsImpactForm;
import com.viettel.service.SpmCrsSubsciberForm;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import viettel.passport.client.UserToken;

@Service
@Slf4j
public class CrProcessFromClient {

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

  @Value("${application.ftp.folder}")
  private String ftpFolder;

  @Value("${application.conf.user_service:null}")
  private String userService;

  @Value("${application.conf.pass_service:null}")
  private String passService;

  @Value("${application.conf.salt_service:null}")
  private String saltService;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.conf.WO_FAIL_CODE:null}")
  private String woFailtCode;

  @Value("${application.cmc_config.system_user_id:null}")
  private String systemUserId;

  @Autowired
  CrProcessWoRepository crProcessWoRepository;

  @Autowired
  CrDtRepository crDtRepository;

  @Autowired
  LoadVMSAMopThread loadVMSAMopThread;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  RoleUserBusiness roleUserBusiness;

  @Autowired
  RolesBusiness rolesBusiness;

  @Autowired
  CrForOtherSystemBusiness crForOtherSystemBusiness;

  @Autowired
  CrRepository crRepository;

  @Autowired
  CrImpactedNodesBusiness crImpactedNodesBusiness;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  CrFilesAttachRepository crFilesAttachRepository;

  @Autowired
  CrProcessRepository crProcessRepository;

  @Autowired
  WSVipaDdPort wsVipaDdPort;

  @Autowired
  CrGeneralRepository crGeneralRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CrImpactedNodesRepository crImpactedNodesRepository;

  @Autowired
  WSSecurityPort wsSecurityPort;

  @Autowired
  ImportDataRepository importDataRepository;

  @Autowired
  WSSPMProvidePort wsspmProvidePort;

  @Autowired
  WSVipaIpPort wsVipaIpPort;

  @Autowired
  WSTDTTPort wstdttPort;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  CoordinationSettingRepository coordinationSettingRepository;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  MrCategoryProxy mrCategoryProxy;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  WSGatePort wsGatePort;

  @Autowired
  MrCategoryUtil mrCategoryUtil;

  public String updateWoWhenChangeCr(List<WoDTO> lstSubWo, String woNewStatus, String comment) {
    if (lstSubWo != null && !lstSubWo.isEmpty()) {
      for (WoDTO dto : lstSubWo) {
        try {
          log.info("Start Update WO When change CR:  " + dto.getWoId());
          dto = woServiceProxy.findWoByIdWSProxy(Long.valueOf(dto.getWoId()));
          String oldStatus = dto.getStatus();
          //kiem tra trang thai wo la du thao thi update giao CD
          if (Constants.WO_STATUS_NEW.DRAFT.equals(oldStatus)
              && (!Constants.WO_STATUS_NEW.UNASSIGNED.equals(oldStatus)
              || Constants.WO_STATUS_NEW.CLOSED_CD.equals(woNewStatus))) {
            log.info("Comein Update WO When change CR: " + oldStatus + " -> " + woNewStatus);
            dto.setStatus(woNewStatus);
            dto.setResult(null);
            dto.setFinishTime(null);
            woServiceProxy.updateWoProxy(dto);
            insertWoHis(oldStatus, woNewStatus, dto, comment);
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new RuntimeException(
              I18n.getMessages("common.errCode011") + " " + I18n.getMessages("common.errDes011"));
//          return I18n.getMessages("common.errCode011") + " " + I18n.getMessages("common.errDes011");
        }
      }
    }
    return "";
  }

  public void insertWoHis(String oldStatus, String woNewStatus, WoDTO dto, String comment) {
    //them moi bang his
    WoHistoryInsideDTO newHis = new WoHistoryInsideDTO();
    newHis.setOldStatus(StringUtils.validString(oldStatus) ? Long.valueOf(oldStatus) : null);
    newHis.setNewStatus(StringUtils.validString(woNewStatus) ? Long.valueOf(woNewStatus) : null);
    newHis.setWoId(StringUtils.validString(dto.getWoId()) ? Long.valueOf(dto.getWoId()) : null);
    newHis.setWoCode(dto.getWoCode());
    newHis.setWoContent(dto.getWoContent());
    newHis.setFileName(dto.getFileName());
    newHis.setComments(comment);
    newHis.setCdId(Long.valueOf(dto.getCdId()));
    newHis.setUpdateTime(new Date());
    newHis.setCreatePersonId(
        StringUtils.validString(dto.getCreatePersonId()) ? Long.valueOf(dto.getCreatePersonId())
            : null);
    woServiceProxy.insertWoHistory(newHis);
  }

  //longlt6 add funtion Close Cr End
  //tuanpv14_start add function start
  public List<WoDTO> getListSubWo(String crId, String type, Date crCreateDate,
      List<String> listWoId) {
    UserToken userToken = ticketProvider.getUserToken();
    List<WoDTO> lstSubWo = new ArrayList<WoDTO>();
    try {
      Date crDate = crCreateDate;
      if (crDate == null) {
        crDate = new Date();
      }
      Calendar cal = Calendar.getInstance();
      cal.setTime(crDate);
      cal.add(Calendar.DATE, -3);

      WoDTOSearch condition = new WoDTOSearch();
      condition.setWoSystemId(crId);
      condition.setUserId(userToken.getUserID().toString());
      condition.setWoSystem("CR");
      condition.setPage(1);
      condition.setPageSize(Integer.MAX_VALUE);
      condition.setSortName("woCode");
      condition.setSortType("");
      List<WoDTOSearch> lstWo = woServiceProxy
          .getListDataSearchProxy(condition);
      if (lstWo != null && !lstWo.isEmpty()) {
        for (WoDTOSearch o : lstWo) {
          WoDTO dto = new WoDTO();
          dto.setWoId(o.getWoId());
          dto.setWoCode(o.getWoCode());
          dto.setWoTypeId(o.getWoTypeId());
          dto.setWoTypeCode(o.getWoTypeCode());
          dto.setCreateDate(o.getCreateDate());
          dto.setStatus(o.getStatus());
          dto.setStartTime(o.getStartTime());
          dto.setEndTime(o.getEndTime());
          dto.setWoContent(o.getWoContent());
          lstSubWo.add(dto);
        }
      }

      if (lstSubWo != null && !lstSubWo.isEmpty() && type != null) {
        lstSubWo.removeIf(c -> (!c.getWoCode().contains(type)));
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstSubWo;
  }

  public Map<String, Double> getMapDurationByProcessId(String crProcessId) {
    Map<String, Double> rs = new HashMap<>();
    try {
      List<CrProcessWoDTO> lstProcessWo = crProcessWoRepository.getLstWoFromProcessId(crProcessId);
      if (lstProcessWo != null) {
        for (CrProcessWoDTO dto : lstProcessWo) {
          rs.put(dto.getWoName(), dto.getDurationWo());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  public void updateWoByCrTime(List<WoDTO> lstSubWo, String crStartTime, String crEndTime,
      Map<String, Double> duration, String processId, String dutyType) {
    if (lstSubWo != null && !lstSubWo.isEmpty()) {
      WoTypeInsideDTO dtoType = new WoTypeInsideDTO();
      dtoType.setWoTypeCode(Constants.CONFIG_PROPERTY.TS_DIEU_CHUYEN_NANG_CAP_CODE);
      List<WoTypeInsideDTO> listWoTypeDTO = woCategoryServiceProxy
          .getListWoTypeDTO(dtoType);
      String idNangCap = "";
      if (listWoTypeDTO != null && !listWoTypeDTO.isEmpty()) {
        idNangCap = String.valueOf(listWoTypeDTO.get(0).getWoTypeId());
      }

      dtoType.setWoTypeCode(Constants.CONFIG_PROPERTY.TS_DIEU_CHUYEN_HA_CAP_CODE);
      List<WoTypeInsideDTO> listWoTypeDTOHC = woCategoryServiceProxy
          .getListWoTypeDTO(dtoType);
      String idHaCap = "";
      if (listWoTypeDTOHC != null && !listWoTypeDTOHC.isEmpty()) {
        idHaCap = String.valueOf(listWoTypeDTOHC.get(0).getWoTypeId());
      }
      List<CrProcessWoDTO> lstCrWo = crProcessWoRepository.getLstWoFromProcessId(processId);
      if (lstCrWo != null && !lstCrWo.isEmpty()) {
        processId = "";
        for (CrProcessWoDTO crProcessWoDTO : lstCrWo) {
          processId = processId + "," + crProcessWoDTO.getWoTypeId();
        }
        processId = processId + ",";
      }

      for (WoDTO dto : lstSubWo) {
        try {
          dto = woServiceProxy.findWoByIdWSProxy(Long.parseLong(dto.getWoId()));
          String oldStatus = dto.getStatus();
          String newStatus = dto.getStatus();
          //wo nang cap thi day luon
          //start time wo = start time cr, tru wo dieu chuyen nang cap
          SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
          SimpleDateFormat sp2 = new SimpleDateFormat("dd/MM/yyyy");
          Date woStartTime = sp.parse(dto.getStartTime());
          if (idNangCap.equals(dto.getWoTypeId()) || idHaCap.equals(dto.getWoTypeId())) {
            if (Constants.WO_STATUS.DRAFT.equals(dto.getStatus())) {
              dto.setStatus(Constants.WO_STATUS.UNASSIGNED);
              dto.setResult(null);
              dto.setFinishTime(null);
              newStatus = Constants.WO_STATUS.UNASSIGNED;
            }
          } else {
            dto.setStartTime(crStartTime);
          }
          //end time wo =end time cr+3, tru wo dieu chuyen nang cap va ha cap
          if (!idNangCap.equals(dto.getWoTypeId()) && !idHaCap.equals(dto.getWoTypeId())
              && !processId.contains("," + dto.getWoTypeId() + ",")) {
            Date d1 = sp.parse(crEndTime);
            if ("1".equals(dutyType) || "5".equals(dutyType) || "6".equals(dutyType)) {
              Date d2 = new Date(d1.getTime() + 10 * 60l * 60l * 1000l);
              String dateTemp = sp2.format(d2);
              d2 = sp.parse(dateTemp + " 07:00:00");
              dto.setEndTime(sp.format(d2));
            } else {
              Date d2 = new Date(d1.getTime() + 2 * 60l * 60l * 1000l);
              dto.setEndTime(sp.format(d2));
            }
          } else {
            Date woD2 = sp.parse(dto.getEndTime());
            Long du = woD2.getTime() - woStartTime.getTime();
            Date d1 = sp.parse(dto.getStartTime());
            Date d2 = new Date(d1.getTime() + du);
            dto.setEndTime(sp.format(d2));
          }

          woServiceProxy.updateWoProxy(dto);
          insertWoHis(oldStatus, newStatus, dto, "system update wo by cr");

        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }

  public boolean checkCreateUserRoll(String changeOrginator) {
    try {
      RoleUserDTO roleUserDTO = new RoleUserDTO();
      roleUserDTO.setUserId(changeOrginator);
      roleUserDTO.setIsActive("1");
      List<RoleUserDTO> lstRoleUserDTOs = roleUserBusiness
          .getListRolesDTO(roleUserDTO, 0, Integer.MAX_VALUE, "", "");
      if (lstRoleUserDTOs != null && !lstRoleUserDTOs.isEmpty()) {
        String lstRoleId = "";
        for (RoleUserDTO dto : lstRoleUserDTOs) {
          lstRoleId += dto.getRoleId() + ",";
        }
        if (lstRoleId.endsWith(",")) {
          lstRoleId = lstRoleId.substring(0, lstRoleId.length() - 1);
        }
        ConditionBean conditionBean = new ConditionBean("roleId", lstRoleId, Constants.NAME_IN,
            Constants.NUMBER);
        List<ConditionBean> lstConditionBeans = new ArrayList<>();
        lstConditionBeans.add(conditionBean);
        List<RolesDTO> lstRolesDTOs = rolesBusiness
            .getListRolesByCondition(lstConditionBeans, 0, Integer.MAX_VALUE, "", "");
        if (lstRolesDTOs != null && !lstRolesDTOs.isEmpty()) {
          for (RolesDTO dto : lstRolesDTOs) {
            if ("1".equals(dto.getStatus()) && dto.getRoleCode().contains("TP")) {
              return true;
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;

  }

  public void loadVMSAMop(CrInsiteDTO crDto) {
    try {
      //LoadVMSAMopThread loadMopThread = new LoadVMSAMopThread(crDto, crDtRepository);
      loadVMSAMopThread.LoadVMSAMop(crDto);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public List<CrInsiteDTO> validateCheckImpact(UserToken userToken, CrInsiteDTO searchDTO) {

    List<CrInsiteDTO> lstCR;
    String[] lstCrNumber = searchDTO.getCrNumber().split(",");
    searchDTO.setUserLogin(userToken.getUserID().toString());
    searchDTO.setUserLoginUnit(userToken.getDeptId().toString());
    searchDTO.setSearchType(Constants.CR_SEARCH_TYPE.LOOKUP.toString());
    searchDTO.setLocale(I18n.getLocale());
    //Tim kiem role
    lstCR = getListCrDTO(searchDTO, true);
    List<String> lstCrFind = new ArrayList<>();
    if (lstCR != null && !lstCR.isEmpty()) {
      for (CrInsiteDTO crDTO : lstCR) {
        lstCrFind.add(crDTO.getCrNumber());
      }
    }
    String crNotFound = "";
    for (String cr : lstCrNumber) {
      if (!lstCrFind.contains(cr)) {
        crNotFound += cr + ", ";
      }
    }
    if (!"".equals(crNotFound)) {
      lstCR = null;
    }
    return lstCR;
  }

  public List<CrInsiteDTO> getListCrDTO(CrInsiteDTO crDTO, boolean isReport) {
    List<CrInsiteDTO> listCrDTO = new ArrayList<>();
    try {
      if (isReport) {
        List<CrInsiteDTO> listCrId = crRepository.getListCRIdForExport(crDTO);
        if (listCrId != null && !listCrId.isEmpty()) {
          int size = listCrId.size();
          String idStr = "";

          Date earliestCrCreatedTime = null;
          Date earliestCrStartTime = null;
          Date latestCrStartTime = null;
          Date latestCrUpdateTime = null;

          for (int i = 0; i < size; i++) {
            CrInsiteDTO dtoId = listCrId.get(i);
            if (dtoId.getCreatedDate() != null) {
              if (earliestCrCreatedTime == null
                  || dtoId.getCreatedDate().compareTo(earliestCrCreatedTime) < 0) {
                earliestCrCreatedTime = dtoId.getCreatedDate();
              }
            }

            if (dtoId.getEarliestStartTime() != null) {
              if (earliestCrStartTime == null
                  || dtoId.getEarliestStartTime().compareTo(earliestCrStartTime) < 0) {
                earliestCrStartTime = dtoId.getEarliestStartTime();
              }
            }

            if (dtoId.getLatestStartTime() != null) {
              if (latestCrStartTime == null
                  || dtoId.getLatestStartTime().compareTo(latestCrStartTime) > 0) {
                latestCrStartTime = dtoId.getLatestStartTime();
              }
            }

            if (dtoId.getUpdateTime() != null) {
              if (latestCrUpdateTime == null
                  || dtoId.getUpdateTime().compareTo(latestCrUpdateTime) > 0) {
                latestCrUpdateTime = dtoId.getUpdateTime();
              }
            }

            idStr += dtoId.getCrId() + ",";
            if ((i != 0 && i % 500 == 0) || i == size - 1) {
              List<CrInsiteDTO> lsttemp = crRepository.getListCRForExport(crDTO, idStr,
                  earliestCrCreatedTime,
                  earliestCrStartTime,
                  latestCrStartTime,
                  latestCrUpdateTime, null);
              if (lsttemp != null) {
                //sua loi trung ban ghi khi cr duoc link tu nhieu nguon
                List<CrInsiteDTO> lstAdd = new ArrayList<CrInsiteDTO>();
                for (int j = 0; j < lsttemp.size(); j++) {
                  CrInsiteDTO dto = lsttemp.get(j);
                  int k = j + 1;
                  while (k < lsttemp.size()
                      && dto.getCrNumber().equals(lsttemp.get(k).getCrNumber())) {
                    dto.setRelateCr("" + dto.getRelateCr() + ";" + lsttemp.get(k).getRelateCr());
//                    j = k;
                    k++;
                  }
                  lstAdd.add(dto);
                }
                listCrDTO.addAll(lstAdd);
              } else {
                System.err.println(idStr);
              }
              idStr = "";
              earliestCrCreatedTime = null;
              earliestCrStartTime = null;
              latestCrStartTime = null;
              latestCrUpdateTime = null;
            }
          }
        }

        if (listCrDTO != null && !listCrDTO.isEmpty()) {
          //Lay thong tin CR lien quan
          String idRelate = "";
          for (CrInsiteDTO dto : listCrDTO) {
            if (dto.getRelateToPreApprovedCr() != null) {
              idRelate += dto.getRelateToPreApprovedCr() + ",";
            }
            if (dto.getRelateToPrimaryCrNumber() != null) {
              idRelate += dto.getRelateToPrimaryCrNumber() + ",";
            }
          }
          if (!"".equals(idRelate)) {
            idRelate = idRelate.substring(0, idRelate.length() - 1);
            List<CrInsiteDTO> listRelate = crRepository.getListCRByLstId(idRelate);
            if (listRelate != null && !listRelate.isEmpty()) {
              Map<String, String> mapCR = new HashMap<String, String>();
              for (CrInsiteDTO dto : listRelate) {
                mapCR.put(dto.getCrId(), dto.getCrNumber());
              }
              for (CrInsiteDTO dto : listCrDTO) {
                if (dto.getRelateToPreApprovedCr() != null) {
                  dto.setRelateToPreApprovedCr(
                      mapCR.get(dto.getRelateToPreApprovedCr()) == null ? ""
                          : mapCR.get(dto.getRelateToPreApprovedCr()));
                }
                if (dto.getRelateToPrimaryCrNumber() != null) {
                  dto.setRelateToPrimaryCrNumber(
                      mapCR.get(dto.getRelateToPrimaryCrNumber()) != null ? mapCR
                          .get(dto.getRelateToPrimaryCrNumber()) : "");
                }
              }
            }
          }
        }
        if (listCrDTO.size() >= 65525) {
          listCrDTO = listCrDTO.subList(0, 65525);
        }
      } else {
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return listCrDTO;
  }

  public List<InfraDeviceDTO> getDataExport(List<CrInsiteDTO> lstCR) {
    List<InfraDeviceDTO> lstExport = new ArrayList<>();
    //Lay danh sach node mang theo tung cr
    Map<String, ArrayList<String>> mapIpDuplicate = new HashMap<>();
    Map<String, ArrayList<String>> mapNodeCodeDuplicate = new HashMap<>();
    Map<String, InfraDeviceDTO> mapIpDevice = new HashMap<>();
    Map<String, InfraDeviceDTO> mapNodeCodeDevice = new HashMap<>();
    for (CrInsiteDTO crCheck : lstCR) {
      CrImpactedNodesDTO crImpactedNodesDTO0 = new CrImpactedNodesDTO();
      crImpactedNodesDTO0.setCrId(crCheck.getCrId());
      crImpactedNodesDTO0
          .setInsertTime(DateTimeUtils.date2ddMMyyyyHHMMss(crCheck.getCreatedDate()));
      crImpactedNodesDTO0.setType(Constants.CR_NODE_TYPE.EFFECT);
      List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs = crImpactedNodesBusiness
          .search(crImpactedNodesDTO0, 0, 100, "", "");//crDTOMain.getLstNetworkNodeId();
      if (lstCrImpactedNodesDTOs != null && !lstCrImpactedNodesDTOs.isEmpty()) {
        for (CrImpactedNodesDTO crImpactedNodesDTO : lstCrImpactedNodesDTOs) {
          InfraDeviceDTO infraDeviceDTO = new InfraDeviceDTO();
          infraDeviceDTO.setIp(crImpactedNodesDTO.getIp());
          infraDeviceDTO.setIpId(crImpactedNodesDTO.getIpId());
          infraDeviceDTO.setDeviceCode(crImpactedNodesDTO.getDeviceCode());
          infraDeviceDTO.setDeviceCodeOld(crImpactedNodesDTO.getDeviceCodeOld());
          infraDeviceDTO.setDeviceName(crImpactedNodesDTO.getDeviceName());
          infraDeviceDTO.setDeviceId(crImpactedNodesDTO.getDeviceId());
          infraDeviceDTO.setCheckbox("0");
          mapIpDevice.put(crImpactedNodesDTO.getIp(), infraDeviceDTO);
          mapNodeCodeDevice.put(crImpactedNodesDTO.getDeviceCode(), infraDeviceDTO);

          ArrayList<String> lstIpCrNumber = mapIpDuplicate.get(crImpactedNodesDTO.getIp());
          if (lstIpCrNumber == null) {
            lstIpCrNumber = new ArrayList<>();
          }
          lstIpCrNumber.add(crCheck.getCrNumber());
          if (crImpactedNodesDTO.getIp() != null) {
            mapIpDuplicate.put(crImpactedNodesDTO.getIp(), lstIpCrNumber);
          }

          ArrayList<String> lstNodeCodeCrNumber = mapNodeCodeDuplicate
              .get(crImpactedNodesDTO.getDeviceCode());
          if (lstNodeCodeCrNumber == null) {
            lstNodeCodeCrNumber = new ArrayList<>();
          }
          lstNodeCodeCrNumber.add(crCheck.getCrNumber());
          if (crImpactedNodesDTO.getDeviceCode() != null) {
            mapNodeCodeDuplicate.put(crImpactedNodesDTO.getDeviceCode(), lstNodeCodeCrNumber);
          }

        }

      }
    }
    for (Map.Entry<String, ArrayList<String>> entry : mapIpDuplicate.entrySet()) {
      String ip = entry.getKey();
      ArrayList<String> lstCrNumber = entry.getValue();
      if (lstCrNumber.size() > 1) {
        InfraDeviceDTO dtoExport = mapIpDevice.get(ip);
        dtoExport.setDescription(lstCrNumber.toString());
        lstExport.add(dtoExport);
      }
    }
    for (Map.Entry<String, ArrayList<String>> entry : mapNodeCodeDuplicate.entrySet()) {
      String nodeCode = entry.getKey();
      ArrayList<String> lstCrNumber = entry.getValue();
      if (lstCrNumber.size() > 1) {
        InfraDeviceDTO dtoExport = mapNodeCodeDevice.get(nodeCode);
        dtoExport.setDescription(lstCrNumber.toString());
        lstExport.add(dtoExport);
      }
    }
    Map<String, InfraDeviceDTO> mapIpNodeCodeDevice = new HashMap<String, InfraDeviceDTO>();
    for (InfraDeviceDTO dto1 : lstExport) {
      String ip = dto1.getIp();
      String nodeCode = dto1.getDeviceCode();
      if (mapIpNodeCodeDevice.get(ip + "@" + nodeCode) == null) {
        mapIpNodeCodeDevice.put(ip + "@" + nodeCode, dto1);
      } else {
        InfraDeviceDTO dto2 = mapIpNodeCodeDevice.get(ip + "@" + nodeCode);
        List<String> crNumber1 = Arrays.asList(
            dto1.getDescription().substring(1, dto1.getDescription().length() - 1).split(","));
        dto2.setDescription(crNumber1.toString());
        mapIpNodeCodeDevice.put(ip + "@" + nodeCode, dto2);
      }
    }
    lstExport = new ArrayList<>();
    lstExport.addAll(mapIpNodeCodeDevice.values());
    for (int i = lstExport.size() - 1; i >= 0; i--) {
      InfraDeviceDTO dto = lstExport.get(i);
      String strCrNumber = dto.getDescription();
      List<String> lstCrNumber = Arrays
          .asList(strCrNumber.substring(1, strCrNumber.length() - 1).split(","));
      List<String> lstCrNumberNew = new ArrayList<String>();
      for (String str : lstCrNumber) {
        if (!lstCrNumberNew.contains(str.trim())) {
          lstCrNumberNew.add(str.trim());
        }
      }
      if (lstCrNumberNew.size() > 1) {
        dto.setDescription(lstCrNumberNew.toString());
        lstExport.set(i, dto);
      } else {
        lstExport.remove(i);
      }
    }
    return lstExport;
  }

  public File exportFileEx(List lstData, List<ConfigHeaderExport> lstHeaderSheet, String title,
      String sheetName, String fileNameOut, String headerPrefix, String sttKey) throws Exception {
    ConfigFileExport configfileExport;
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet;
    String subTitle = I18n.getLanguage("export.time") + ": " + DateTimeUtils
        .date2ddMMyyyyHHMMss(DateTimeUtils.convertDateOffset());
    configfileExport = new ConfigFileExport(
        lstData
        , sheetName
        , title
        , subTitle
        , 7
        , 1
        , lstHeaderSheet.size()
        , true
        , headerPrefix
        , lstHeaderSheet
        , fieldSplit
        , ""
        , I18n.getLanguage("data.export.firstLeftHeader")
        , I18n.getLanguage("data.export.secondLeftHeader")
        , I18n.getLanguage("data.export.firstRightHeader")
        , I18n.getLanguage("data.export.secondRightHeader")
    );
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage(sttKey),
        "HEAD", "STRING");

    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    String[] customTitle = new String[]{"0", "60", "0,128,0",
        "1"}; //[1:0] co aligncenter ko, String alignLeft, String background, String numberRowMerge
    configfileExport.setCustomTitle(customTitle);
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

  public Long stringToLong(String txt) {
    try {
      if (txt == null || txt.trim().isEmpty()) {
        return null;
      }
      return Long.parseLong(txt.trim());
    } catch (NumberFormatException e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public List<CrInsiteDTO> convertKey2StringListCR(List<CrInsiteDTO> lst) {
    if (lst == null || lst.isEmpty()) {
      return new ArrayList<>();
    }
    for (CrInsiteDTO crDTO1 : lst) {
      String priority = StringUtils.convertKeyToValueByMap(
          Constants.CR_PRIORITY.getGetPriorityName(), crDTO1.getPriority());
      String state = StringUtils.convertKeyToValueByMap(
          Constants.CR_STATE.getGetStateName(), crDTO1.getState());
      crDTO1.setPriority(priority);
      crDTO1.setState(state);
    }
    return lst;
  }

  public ResultInSideDto initSelectDT(Map<String, CrFilesAttachInsiteDTO> mapAttach, String crId,
      List<com.viettel.vmsa.MopDetailDTO> lstMop, CrInsiteDTO crLevel1, String actionType) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, Constants.RESULT.ERROR,
        Constants.RESULT.ERROR);
    try {
      UserToken userToken = ticketProvider.getUserToken();
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      //mop tu vmsa
      Map<String, Object> dataResponse = new HashMap<>();
      List<AttachDtDTO> lstAtt = new ArrayList<>();
      List<CrFilesAttachInsiteDTO> lstFile = new ArrayList<>();
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      Map<String, com.viettel.vmsa.MopDetailDTO> map = new HashMap<>();
      List<String> lstDtcode = new ArrayList<>();
      for (com.viettel.vmsa.MopDetailDTO detailDTO : lstMop) {
        if (StringUtils.isStringNullOrEmpty(detailDTO.getMopFileContent())) {
          resultInSideDto.setMessage(
              I18n.getLanguage("mop.is.not.Exist").replaceAll("@mopCode", detailDTO.getMopId()));
          return resultInSideDto;
        }
        AttachDtDTO addto = new AttachDtDTO();
        addto.setDtCode(detailDTO.getMopId());
        addto.setFileName(detailDTO.getMopFileName());
        addto.setNationCode(detailDTO.getNationCode());
        map.put(detailDTO.getMopId(), detailDTO);
        lstAtt.add(addto);
        lstDtcode.add(detailDTO.getMopId());
      }
      List<InfraDeviceDTO> lstNodeImpactAll = new ArrayList<>();
      int count = 0;
      List<String> lstMopToVMSA = new ArrayList<>();
      List<CrFilesAttachInsiteDTO> existMops = crFilesAttachRepository.checkExistMop(crId);
      Map<String, String> mapMopInDB = new HashMap<>();
      if (existMops != null) {
        for (CrFilesAttachInsiteDTO item : existMops) {
          if (item.getCrId() != null && StringUtils.isNotNullOrEmpty(item.getDtCode())) {
            mapMopInDB.put(item.getCrId().toString() + "_" + item.getDtCode(), item.getDtCode());
          }
        }
      }
      for (AttachDtDTO attachDtSelected : lstAtt) {
        lstMopToVMSA.add(attachDtSelected.getDtCode());
        CrFilesAttachInsiteDTO attachDTO = mapAttach.get(attachDtSelected.getDtCode());
        mapAttach.remove(attachDtSelected.getDtCode());
        com.viettel.vmsa.MopDetailDTO detailDTO = map.get(attachDtSelected.getDtCode());
        List<com.viettel.vmsa.NodeDTO> lstNode = detailDTO.getNodes();
        String nationCode = attachDtSelected.getNationCode();
        if (lstNode != null && !lstNode.isEmpty()) {
          List<String> lstIp = new ArrayList<>();
          for (com.viettel.vmsa.NodeDTO dto : lstNode) {
            if (dto.getNodeIp() != null && !lstIp.contains(dto.getNodeIp())) {
              lstIp.add(dto.getNodeIp());
            }
          }
          attachDtSelected.setLstIpImpact(lstIp);
        }
        if (nationCode == null || nationCode.trim().isEmpty()) {
          nationCode = "VNM";
        }
        String ipAffectInvalid = "";
        List<String> lstIpImpact = attachDtSelected.getLstIpImpact();
        if (lstIpImpact == null || lstIpImpact.isEmpty()) {
          resultInSideDto.setMessage(I18n.getChangeManagement("cr.tdtt.ipImpactErr"));
          return resultInSideDto;
        }
        List<InfraDeviceDTO> lstNodeImpact = getNetworkNodeTDTTV2(lstIpImpact, nationCode);
        String ipImpactInvalid = getLstIpInvalid(lstIpImpact, lstNodeImpact);
        if ("".equals(ipAffectInvalid) && "".equals(ipImpactInvalid)) {
          for (InfraDeviceDTO node : lstNodeImpact) {
            boolean ch = true;
            for (InfraDeviceDTO node2 : lstNodeImpactAll) {
              if (node2.getIp().equals(node.getIp())) {
                ch = false;
              }
            }
            if (ch) {
              node.setDtCode(attachDtSelected.getDtCode());
              lstNodeImpactAll.add(node);
            }
          }

        } else {
          String ipInvalid = "".equals(ipImpactInvalid) ? ipAffectInvalid
              : ("".equals(ipAffectInvalid) ? ipImpactInvalid
                  : ipImpactInvalid + ", " + ipAffectInvalid);
          resultInSideDto
              .setMessage(I18n.getChangeManagement("cr.tdtt.ipNotExist") + " " + ipInvalid);
          return resultInSideDto;

        }
        //save file
        if (!StringUtils.isStringNullOrEmpty(detailDTO.getMopFileContent())) {
          Date date = new Date();
          boolean existMop = false;
          if (attachDtSelected.getDtCode() != null && mapMopInDB
              .containsKey(crId + "_" + attachDtSelected.getDtCode())) {
            existMop = true;
          }
          attachDTO.setFileId(null);
          if (!existMop) {
//            attachDTO.setFileId(existMop.getFileId());
//            attachDTO.setCrId(Long.valueOf(crId));
//            attachDTO.setIsRun(null);
//            lstFile.add(attachDTO);
//          } else {
            byte[] fileContentKpi = Base64.decode(detailDTO.getMopFileContent());
            String fileName =
                count + "_" + DateUtil.date2HHMMssNoSlash(new Date()) + "_" + detailDTO
                    .getMopFileName();
            String fullPath = FileUtils
                .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), ftpFolder, fileName, fileContentKpi, date);
            //start save file old
            String fullPathOld =
                uploadFolder + File.separator + FileUtils.createPathByDate(date) + File.separator
                    + fileName;
            attachDTO.setFilePath(fullPathOld);
            attachDTO.setFileName(fileName);
            attachDTO.setCrId(Long.valueOf(crId));
            lstFile.add(attachDTO);
            //end save file old

            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(fullPath);
            gnocFileDto.setFileName(fileName);
            gnocFileDto.setFileType(attachDTO.getFileType());
            gnocFileDto.setTemplateId(attachDTO.getTempImportId());
            gnocFileDto.setCreateUnitId(userToken.getDeptId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(new Date());
            gnocFileDto.setMappingId(null);
            gnocFileDtos.add(gnocFileDto);
          }

        } else {
          resultInSideDto.setMessage(I18n.getLanguage("mop.is.not.Exist")
              .replaceAll("@mopCode", detailDTO.getMopFileName()));
          return resultInSideDto;
        }
      }
      dataResponse.put("lstNetworkNode", lstNodeImpactAll);
      //luu file
      List<GnocFileDto> gnocFileDtosAdd = new ArrayList<>();
      for (CrFilesAttachInsiteDTO crFilesAttachInsiteDTOAdd : lstFile) {
//        crFilesAttachInsiteDTOAdd.setFileId(null);
        ResultInSideDto resultFileDataOld = crFilesAttachRepository
            .add(crFilesAttachInsiteDTOAdd);
        for (GnocFileDto gnocFileDto : gnocFileDtos) {
          if (gnocFileDto.getFileName() != null && gnocFileDto.getFileName()
              .equalsIgnoreCase(String.valueOf(crFilesAttachInsiteDTOAdd.getFileName()))) {
            log.info("matching File Gnoc vs File AttachOld");
            gnocFileDto.setMappingId(resultFileDataOld.getId());
            gnocFileDtosAdd.add(gnocFileDto);
            break;
          }
        }
      }
      gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CR,
          Long.valueOf(crId), gnocFileDtosAdd);
      //update lai file
      if (lstDtcode != null && !lstDtcode.isEmpty()) {
        crFilesAttachRepository.onUpdateIsRunMop(crLevel1.getCrId(), lstDtcode, crId);
      }
      //load file
      List<CrFilesAttachInsiteDTO> lstFileAttach = loadAttachment(crId);
      dataResponse.put("lstAttachment", lstFileAttach);
      //luu cr
      crRepository.actionLoadMopFromCRParent(crLevel1.getCrId(), "1");
      if (!"3".equals(actionType) && !"4".equals(actionType)) {
        CrInsiteDTO crUpdate = crRepository
            .findCrById(Long.parseLong(crId), ticketProvider.getUserToken());
        crRepository.actionLoadMopFromCRParent(crId, "2");
        //day mop sang VMSA
        wsVipaDdPort
            .updateCrStatusWithMops(crUpdate.getCrNumber(), crUpdate.getState(), lstMopToVMSA);
      }
      resultInSideDto = new ResultInSideDto(null, Constants.RESULT.SUCCESS,
          I18n.getChangeManagement("cr.file.loadDt.from.cr.origin"));
      resultInSideDto.setObject(dataResponse);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return resultInSideDto;
  }

  private List<InfraDeviceDTO> getNetworkNodeTDTTV2(List<String> lstIp, String nationCode) {
    List<InfraDeviceDTO> lstData = new ArrayList();
    try {
      InfraDeviceDTO infraDeviceDTO = new InfraDeviceDTO();
      infraDeviceDTO.setLstIps(lstIp);
      infraDeviceDTO.setNationCode(nationCode);
      lstData = commonStreamServiceProxy.geInfraDeviceByIps(infraDeviceDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstData;
  }

  public String getLstIpInvalid(List<String> lstIpAffect, List<InfraDeviceDTO> lstNodeAffect) {
    String ipInvalid = "";
    if (lstIpAffect != null && !lstIpAffect.isEmpty()) {
      if (lstNodeAffect != null && !lstNodeAffect.isEmpty()) {
        for (String ipAff : lstIpAffect) {
          if (ipAff == null || ipAff.isEmpty()) {
            continue;
          }
          boolean check = false;
          ipAff = ipAff.trim();
          for (InfraDeviceDTO dto : lstNodeAffect) {
            if (ipAff.equals(dto.getIp() != null ? dto.getIp().trim() : dto.getIp())) {
              check = true;
              break;
            }
          }
          if (!check) {
            ipInvalid += ipAff + ", ";
          }
        }
        if (ipInvalid != null && !"".equals(ipInvalid)) {
          ipInvalid = ipInvalid.substring(0, ipInvalid.length() - 2);
        }
      } else {
        String ipInput = lstIpAffect.toString();
        ipInput = ipInput.substring(1, ipInput.length() - 1);
        ipInvalid += ipInput;
      }
    }
    return ipInvalid;
  }

  public List<CrFilesAttachInsiteDTO> loadAttachment(String crId) {
    List<CrFilesAttachInsiteDTO> lstAttachment = new ArrayList<>();
    try {
      CrFilesAttachInsiteDTO crFile = new CrFilesAttachInsiteDTO();
      crFile.setCrId(Long.valueOf(crId));
      lstAttachment = crFilesAttachRepository.getListCrFilesSearch(crFile);
      if (lstAttachment != null) {
        Double offSet = TimezoneContextHolder.getOffsetDouble();
        for (CrFilesAttachInsiteDTO f : lstAttachment) {
          f.setTimeAttack(DateTimeUtils.convertDateToOffset(f.getTimeAttack(), offSet, false));
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    return lstAttachment;
  }

  //xu ly cho cac su kien action --> nua them vao controller tuong ung sau
  public CrInsiteDTO getFormForProcess(CrInsiteDTO crInsiteDTO) {
    if (Constants.CR_ACTION_CODE.APPRAISE.toString().equals(crInsiteDTO.getActionType())) {
      List<UserCabCrForm> lstForm = crGeneralRepository
          .getListUserCab(crInsiteDTO.getImpactSegment(), crInsiteDTO.getChangeResponsibleUnit());
      if (lstForm != null && !lstForm.isEmpty()) {
        crInsiteDTO.setUserCab(lstForm.get(0).getUserCab());
      }
    } else if (Constants.CR_ACTION_CODE.CHANGE_TO_CAB.toString()
        .equals(crInsiteDTO.getActionType())) {
      //kiem tra dau vao, chuyen CAB
      List<UserCabCrForm> lstForm = crGeneralRepository
          .getListUserCab(crInsiteDTO.getImpactSegment(), crInsiteDTO.getChangeResponsibleUnit());
      if (lstForm != null && !lstForm.isEmpty()) {
        crInsiteDTO.setUserCab(lstForm.get(0).getUserCab());
      }
    }

    crInsiteDTO.setUserLogin(ticketProvider.getUserToken().getUserID().toString());
    crInsiteDTO.setUserLoginUnit(ticketProvider.getUserToken().getDeptId().toString());
    converTimeFromClientToServer(crInsiteDTO, ticketProvider.getUserToken().getUserName());
    return crInsiteDTO;
  }

  //actionApproveCR
  public void actionApproveCrAfter(ResultInSideDto ret, CrInsiteDTO crDTOSave) {
    List<String> addedWoIdList = null;
    if (StringUtils.isNotNullOrEmpty(crDTOSave.getListWoId())) {
      addedWoIdList = Arrays.asList(crDTOSave.getListWoId().split(","));
    }
    if (Constants.CR_RETURN_MESSAGE.SUCCESS.equals(ret.getKey()) && Constants.CR_TYPE.STANDARD
        .toString().equals(crDTOSave.getCrType())) {//cr chuan
      if (!checkCreateUserRoll(crDTOSave.getChangeOrginator())) {
        List<WoDTO> lstSubWo = getListSubWo(crDTOSave.getCrId(), null, crDTOSave.getCreatedDate(),
            addedWoIdList);
        updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
            I18n.getLanguage("woTest.woCrApprove"));
        if (lstSubWo != null && !lstSubWo.isEmpty()) {
          Map<String, Double> mapWoDuration = getMapDurationByProcessId(
              crDTOSave.getProcessTypeId());
          updateWoByCrTime(lstSubWo,
              DateTimeUtils.date2ddMMyyyyHHMMss(crDTOSave.getEarliestStartTime()),
              DateTimeUtils.date2ddMMyyyyHHMMss(crDTOSave.getLatestStartTime()), mapWoDuration,
              crDTOSave.getCrProcessId(), crDTOSave.getDutyType());
        }
      }
    }
  }

  // //tham dinh CR(dinh kem file DT) actionAppraiseCr
  public void actionAppraiseCrAfter(ResultInSideDto ret, CrInsiteDTO crDTOSave) {
    List<String> addedWoIdList = null;
    if (StringUtils.isNotNullOrEmpty(crDTOSave.getListWoId())) {
      addedWoIdList = Arrays.asList(crDTOSave.getListWoId().split(","));
    }
    if (Constants.CR_RETURN_MESSAGE.SUCCESS.equals(ret)) {
      if (Constants.CR_ACTION_CODE.APPRAISE.toString().equals(crDTOSave.getActionType())) {
        try {
          List<WoDTO> lstSubWo = getListSubWo(crDTOSave.getCrId(),
              "WO_CR_TEST_SERVICE_AUTO_GENERATE", crDTOSave.getCreatedDate(), addedWoIdList);
          if (lstSubWo != null && !lstSubWo.isEmpty()) {
            //lay danh sach file cr dinh kem
            UserToken userToken = ticketProvider.getUserToken();
            UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
            CrFilesAttachInsiteDTO dtoCondi = new CrFilesAttachInsiteDTO();
            dtoCondi.setCrId(Long.valueOf(crDTOSave.getCrId()));
            dtoCondi.setUserId(userToken.getUserID());
            List<CrFilesAttachInsiteDTO> lstAttachDTOs = crFilesAttachRepository
                .getListCrFilesAttachDTO(dtoCondi, 0, Integer.MAX_VALUE, "", "");
            if (lstAttachDTOs != null && !lstAttachDTOs.isEmpty()) {
              //Lay wo_id cha
              Map<String, WoTestServiceMapDTO> mapServiceMap = new HashMap<>();
              CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = new CrCreatedFromOtherSysDTO();
              crCreatedFromOtherSysDTO.setCrId(crDTOSave.getCrId());
              crCreatedFromOtherSysDTO.setSystemId(CrCreatedFromOtherSysDTO.SYSTEM.WO);
              List<CrCreatedFromOtherSysDTO> lstCrCreatedFromOtherSys = crRepository
                  .getCrCreatedFromOtherSys(crCreatedFromOtherSysDTO);
              if (lstCrCreatedFromOtherSys != null && !lstCrCreatedFromOtherSys.isEmpty()) {
                String woParentId = lstCrCreatedFromOtherSys.get(0).getObjectId();
                WoTestServiceMapDTO con = new WoTestServiceMapDTO();
                con.setWoId(woParentId);
                List<WoTestServiceMapDTO> lstMap = commonStreamServiceProxy
                    .getListWoTestServiceMapDTO(con, 0, Integer.MAX_VALUE, "", "");
                for (WoTestServiceMapDTO obj : lstMap) {
                  String woSubId = obj.getWoSubId();
                  if (woSubId != null && !"".equals(woSubId)) {
                    String[] woSubArr = woSubId.split(",");
                    for (String arr : woSubArr) {
                      mapServiceMap.put(arr, obj);
                    }
                  }
                }
              }
              for (WoDTO dto : lstSubWo) {
                try {
                  dto = woServiceProxy.findWoByIdWSProxy(Long.parseLong(dto.getWoId()));
                  //Kiem tra xem wo co ghi de dinh kem file CR khong
                  WoTestServiceMapDTO mapDTO = mapServiceMap.get(dto.getWoId());
                  if (mapDTO != null && Constants.FILE_CR_ID.equals(mapDTO.getFileId())) {
                    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
                    List<String> fileNames = new ArrayList<>();
                    for (CrFilesAttachInsiteDTO crFilesAttachInsiteDTOAdd : lstAttachDTOs) {
                      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                          "dd/MM/yyyy HH:mm:ss");
                      Date woCreateDate = simpleDateFormat.parse(dto.getCreateDate());
                      byte[] bytes = FileUtils
                          .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                              PassTranformer.decrypt(ftpPass),
                              crFilesAttachInsiteDTOAdd.getFilePath());
                      String fullPath = FileUtils
                          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                              PassTranformer.decrypt(ftpPass), ftpFolder,
                              crFilesAttachInsiteDTOAdd.getFileName(), bytes,
                              FileUtils.createDateOfFileName(woCreateDate));
                      fileNames.add(FileUtils.getFileName(fullPath));
                      GnocFileDto gnocFileDto = new GnocFileDto();
                      gnocFileDto.setPath(fullPath);
                      gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
                      gnocFileDto.setCreateUnitId(userToken.getDeptId());
                      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
                      gnocFileDto.setCreateUserId(userToken.getUserID());
                      gnocFileDto.setCreateUserName(userToken.getUserName());
                      gnocFileDto.setCreateTime(woCreateDate);
                      gnocFileDto.setMappingId(Long.valueOf(crDTOSave.getCrId()));
                      gnocFileDtos.add(gnocFileDto);
                    }
                    gnocFileRepository
                        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO,
                            Long.valueOf(crDTOSave.getCrId()),
                            gnocFileDtos);
                    dto.setFileName(String.join(",", fileNames));
                    dto.setSyncStatus(null);
                    woServiceProxy.updateWoProxy(dto);
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
    }
  }

  //actionReceiveCr
  public void actionReceiveCrThreadBefore(CrInsiteDTO crDTO) {
    try {
      if (Constants.CR_ACTION_CODE.ACCEPT.toString().equals(crDTO.getActionType())) {
        UnitDTO u = unitRepository.findUnitById(Long.valueOf(crDTO.getChangeOrginatorUnit()));
        UsersEntity userCreate = userRepository
            .getUserByUserId(Long.valueOf(crDTO.getChangeOrginator()));
        UsersEntity userExecute = userRepository
            .getUserByUserId(ticketProvider.getUserToken().getUserID());
        CrsImpactForm crBO = new CrsImpactForm();
        crBO.setCrId(crDTO.getCrId());
        crBO.setCrNumber(crDTO.getCrNumber());
        crBO.setCreateDate(DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getCreatedDate()));
        crBO.setCreatorEmail(userCreate.getEmail());
        crBO.setCreatorUsername(userCreate.getUsername());
        crBO.setEndDate(DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()));
        crBO.setExecDepartmentCode(String.valueOf(ticketProvider.getUserToken().getDeptId()));
        crBO.setExecEmployeeEmail(userExecute.getEmail());
        crBO.setExecEmployeePhone(userExecute.getMobile());
        crBO.setExecEmployeeUsername(ticketProvider.getUserToken().getUserName());
        crBO.setOwnerDepartmentCode(u.getUnitCode());
        crBO.setProcessTypeCode(crDTO.getProcessTypeId());
        crBO.setStartDate(DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()));
        crBO.setStatus(Constants.CR_STATE.ACCEPT.toString());
        crBO.setUpdateTime(DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getUpdateTime()));

        CrImpactedNodesDTO crImpactedNodesDTO0 = new CrImpactedNodesDTO();
        crImpactedNodesDTO0.setCrId(crDTO.getCrId());
        crImpactedNodesDTO0
            .setInsertTime(DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getCreatedDate()));
        crImpactedNodesDTO0.setType(Constants.CR_NODE_TYPE.EFFECT);
//        List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs = crImpactedNodesRepository
//            .getImpactedNodesByCrId(Long.valueOf(crDTO.getCrId()), crDTO.getCreatedDate(), null,
//                Constants.CR_NODE_TYPE.EFFECT, null, null, null);
//        log.info("bat dau Goi WSSecurityPort khi nhan CR " + DateTimeUtils
//            .date2ddMMyyyyHHMMss(new Date()));

//        ReceiveCrImpactedNodeThread thread = new ReceiveCrImpactedNodeThread(wsSecurityPort, crBO,
//            lstCrImpactedNodesDTOs);
//        thread.start();
        senTraceInfo(crDTO, Constants.CR_STATE.ACCEPT);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public void actionResolveCrAfter(CrInsiteDTO crDTO) {
    try {
//      String crNumber = crDTO.getCrNumber();
//      UsersEntity userExecute = userRepository
//          .getUserByUserId(Long.valueOf(crDTO.getChangeResponsible()));
//      List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs = crImpactedNodesRepository
//          .getImpactedNodesByCrId(Long.valueOf(crDTO.getCrId()), crDTO.getCreatedDate(), null,
//              Constants.CR_NODE_TYPE.EFFECT, null, null, null);
//      ResetPasswordAfterCompleteCRImpactThread thread = new ResetPasswordAfterCompleteCRImpactThread(
//          wsSecurityPort, lstCrImpactedNodesDTOs, crNumber, userExecute.getEmail());
//      thread.start();
      senTraceInfo(crDTO, Constants.CR_STATE.RESOLVE);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    try {
      String crReturnResolve = crDTO.getCrReturnResolve();
      List<ItemDataCRInside> lstReturnCodeAll = crDTO.getLstReturnCodeAll();
      ItemDataCRInside itemData = null;
      if (lstReturnCodeAll != null && lstReturnCodeAll.size() > 0) {
        for (int i = 0; i < lstReturnCodeAll.size(); i++) {
          if (lstReturnCodeAll.get(i).getValueStr() != null && lstReturnCodeAll.get(i).getValueStr()
              .equals(crReturnResolve)) {
            itemData = lstReturnCodeAll.get(i);
          }
        }
      }
      if (itemData != null && itemData.getSecondValue() != null && itemData.getSecondValue()
          .contains(woFailtCode)) {
        UserToken userToken = ticketProvider.getUserToken();
        String username = userToken.getUserName();
        String reason = username;
        try {
          reason = username + " " + I18n.getChangeManagement("fail.due.to.ft");
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }

        SendFailedWoThread failedWoThread
            = new SendFailedWoThread(username, reason, crDTO.getFailedWoList(), woServiceProxy);
        failedWoThread.start();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public void actionAssignCabAfter(CrInsiteDTO crDTO) {
    try {
      List<String> addedWoIdList = null;
      if (StringUtils.isNotNullOrEmpty(crDTO.getListWoId())) {
        addedWoIdList = Arrays.asList(crDTO.getListWoId().split(","));
      }
      List<WoDTO> lstSubWo = getListSubWo(crDTO.getCrId(), null, crDTO.getCreatedDate(),
          addedWoIdList);
      if (lstSubWo != null && !lstSubWo.isEmpty()) {
        updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
            I18n.getLanguage("woTest.woCrCab"));
        //cap nhat thoi gian cua Wo theo CR
        Map<String, Double> mapWoDuration = getMapDurationByProcessId(crDTO.getProcessTypeId());
        updateWoByCrTime(lstSubWo, DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()),
            DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()), mapWoDuration,
            crDTO.getCrProcessId(), crDTO.getDutyType());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (e.getMessage().contains(
          I18n.getMessages("common.errCode011") + " " + I18n.getMessages("common.errDes011"))) {
        throw new RuntimeException(e.getMessage());
      }
    }
  }

  public void actionEditCrAfter(CrInsiteDTO crDTO) {
    try {
      List<String> addedWoIdList = null;
      if (StringUtils.isNotNullOrEmpty(crDTO.getListWoId())) {
        addedWoIdList = Arrays.asList(crDTO.getListWoId().split(","));
      }
      List<WoDTO> lstSubWo = getListSubWo(crDTO.getCrId(), null, crDTO.getCreatedDate(),
          addedWoIdList);
      if (lstSubWo != null && !lstSubWo.isEmpty()) {
        //cap nhat thoi gian cua Wo theo CR
        Map<String, Double> mapWoDuration = getMapDurationByProcessId(crDTO.getProcessTypeId());
        updateWoByCrTime(lstSubWo, DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()),
            DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()),
            mapWoDuration, crDTO.getCrProcessId(), crDTO.getDutyType());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public void senTraceInfo(CrInsiteDTO crDTOMain, Long crStatus) throws Exception {
    //Neu la cr trace so thi gui danh sach sdt sang tool trace so
    List<CrFileObject> lstFileObject = importDataRepository
        .getListTemplateFileByProcess(crDTOMain.getProcessTypeId(),
            Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_IN, I18n.getLocale());
    Map<String, CrFileObject> map = new HashMap<String, CrFileObject>();
    if (lstFileObject != null && !lstFileObject.isEmpty()) {
      for (CrFileObject lstFileObject1 : lstFileObject) {
        lstFileObject1.setCrId(crDTOMain.getCrId());
        map.put(lstFileObject1.getIdTemplate(), lstFileObject1);
      }
    }
    for (Map.Entry<String, CrFileObject> entrySet : map.entrySet()) {
      CrFileObject crFileObject = entrySet.getValue();
      if ("1500".equals(crFileObject.getIdTemplate())) {
        TempImportDataDTO con = new TempImportDataDTO();
        con.setCrId(crDTOMain.getCrId());
        List<TempImportDataDTO> lst = crFilesAttachRepository.getListTempImportDataDTO(con);
        if (lst != null && !lst.isEmpty()) {
          List<String> lstPhone = new ArrayList<String>();
          for (TempImportDataDTO dto : lst) {
            lstPhone.add(dto.getTempImportValue());
          }
          SpmCrsSubsciberForm form = new SpmCrsSubsciberForm();
          form.getLstSubscriber().addAll(lstPhone);
          form.setCrId(Long.valueOf(crDTOMain.getCrId()));
          form.setCrNumber(crDTOMain.getCrNumber());
          form.setStartTime(DateTimeUtils.date2ddMMyyyyHHMMss(crDTOMain.getEarliestStartTime()));
          form.setEndTime(DateTimeUtils.date2ddMMyyyyHHMMss(crDTOMain.getLatestStartTime()));
          form.setStatus(crStatus);
          String usName = crDTOMain.getChangeResponsibleName();
          if (usName != null && usName.indexOf("(") > 0) {
            usName = usName.substring(0, usName.indexOf("(")).trim();
          } else {
            usName = ticketProvider.getUserToken().getUserName();
          }
          form.setExecEmployeeUsername(usName);
          wsspmProvidePort.receviceOrCompleteCRs(form);
        }
      }
    }
  }

  public String checkCrAuto(CrInsiteDTO crDTOSave, String subDTCode, boolean checkCrAuto) {
    try {
      if (StringUtils.isNotNullOrEmpty(subDTCode)) {
        CrFilesAttachInsiteDTO crFile = new CrFilesAttachInsiteDTO();
        crFile.setCrId(Long.valueOf(crDTOSave.getCrId()));
        List<CrFilesAttachInsiteDTO> lstAttachment = crFilesAttachRepository
            .getListCrFilesAttachDTO(crFile, 0, 1000, "", "");
        String dtCode = "";
        String dtHis = "";
        List<String> lstMop = new ArrayList<String>();
        if (lstAttachment != null) {
          for (CrFilesAttachInsiteDTO dto : lstAttachment) {
            if (dto.getDtCode() != null) {
              dtCode = dto.getDtCode();
              dtHis = dto.getDtFileHistory();
              if (!lstMop.contains(dtCode)) {
                lstMop.add(dtCode);
              }
            }
          }
        }
        boolean checked = checkCrAuto;
        CrDTO crDTO = crDTOSave.toCrDTO();
        System.out.println("--checked-- " + crDTOSave.getCrNumber());

        Long typeConfirmGNOC;
        Long typeRunGNOC;
        String crLinkGNOC = crDTOSave.getCrNumberRelatedCbb();
        if (crDTOSave.getIsConfirmAction() != null) {//co can check tac dong
          typeConfirmGNOC = crDTOSave.getIsConfirmAction();
        } else {
          typeConfirmGNOC = 0L;
        }
        if (crDTOSave.getIsRunType() != null && crDTOSave.getIsRunType() == 1L) {//chay tuan tu
          typeRunGNOC = 1L;
        } else {
          typeRunGNOC = 0L;//chay song song
        }
        if (dtHis != null && dtHis.contains("VIPA_IP")) {
          com.viettel.vipa.ResultDTO result = wsVipaIpPort
              .updateRunAutoStatus(crDTO, subDTCode, checked, typeConfirmGNOC, typeRunGNOC,
                  crLinkGNOC);
          if (result == null) {
            return I18n.getLanguage("cr.cannot.connect.to.AAM") + " VIPA";
          }
          Integer status = result.getResultCode();
          if (status != null && (status == 1 || status == 0)) {
            return "";
          }
          return result.getResultMessage();
        } else if (dtHis != null && dtHis.contains("VIPA_DD")) {
          com.viettel.vmsa.ResultDTO result = wsVipaDdPort
              .updateRunAutoStatus(crDTO, subDTCode, checked, typeConfirmGNOC, typeRunGNOC,
                  crLinkGNOC);
          if (result == null) {
            return I18n.getLanguage("cr.cannot.connect.to.AAM") + " VMSA";
          }
          Integer status = result.getResultCode();
          if (status != null && (status == 1 || status == 0)) {
            return "";
          }
          return result.getResultMessage();

        } else if (StringUtils.isNotNullOrEmpty(dtHis)) {
          LinkCrResult result = wstdttPort
              .updateRunAutoStatus(crDTO, subDTCode, checked, typeConfirmGNOC, typeRunGNOC,
                  crLinkGNOC);
          if (result == null) {
            return I18n.getLanguage("cr.cannot.connect.to.AAM") + " AAM";
          }
          Integer status = result.getStatus();
          if (status != null && status == 1) {
            return "";
          }
          return result.getMessage();
        }
      }
      return "";
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return I18n.getMessages("error");
    }
  }

  public String callMrAfterScheduler(CrInsiteDTO crDTO) {
    //Goi sang MR khi schedule
    try {
      String actionType = crDTO.getActionType();
      if (actionType != null) {
        actionType = actionType.trim();
      }

      if (Constants.CR_ACTION_CODE.SCHEDULE.toString().equalsIgnoreCase(actionType)) {
        if (crDTO.getCrId() != null) {
          Long crId = this.stringToLong(crDTO.getCrId());
          CrCreatedFromOtherSysDTO dto = crForOtherSystemBusiness.getCrCreatedFromOtherSysDTO(crId);

          if (dto != null && dto.getStatus() != null
              && "OK".equals(dto.getStatus())
              && "1".equals(dto.getSystemId())) {
            ResultDTO mrResult = mrCategoryProxy.updateMrStatus(crDTO.getCrId(), "");
            if (!Constants.CR_RETURN_MESSAGE.SUCCESS.equals(mrResult.getKey())) {
              return "Scheduleed Cr Sucesssfully but can not Update MR status it ";
            }
          }
        }
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return Constants.RESULT.ERROR;
    }
    return Constants.RESULT.SUCCESS;
  }

  public void actionScheduleCRAfter(CrInsiteDTO crDTO) {
    try {
      List<String> addedWoIdList = null;
      if (StringUtils.isNotNullOrEmpty(crDTO.getListWoId())) {
        addedWoIdList = Arrays.asList(crDTO.getListWoId().split(","));
      }
      List<WoDTO> lstSubWo = getListSubWo(crDTO.getCrId(), null, crDTO.getCreatedDate(),
          addedWoIdList);
      if (lstSubWo != null && !lstSubWo.isEmpty()) {
        updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
            I18n.getLanguage("woTest.woCrSchedule"));
        //cap nhat thoi gian cua Wo theo CR
        Map<String, Double> mapWoDuration = getMapDurationByProcessId(crDTO.getProcessTypeId());
        updateWoByCrTime(lstSubWo, DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()),
            DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()), mapWoDuration,
            crDTO.getCrProcessId(), crDTO.getDutyType());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (e.getMessage().contains(
          I18n.getMessages("common.errCode011") + " " + I18n.getMessages("common.errDes011"))) {
        throw new RuntimeException(e.getMessage());
      }
    }
  }

  public void actionSaveEnd(CrInsiteDTO crDTOSave, CrInsiteDTO formRoot, String ret, Long crState) {
    try {
      List<String> addedWoIdList = null;
      if (StringUtils.isNotNullOrEmpty(crDTOSave.getListWoId())) {
        addedWoIdList = Arrays.asList(crDTOSave.getListWoId().split(","));
      }
      //chuyen tran thai WO khi cr bi tra ve
      if (Constants.CR_RETURN_MESSAGE.SUCCESS.equals(ret)
          && (Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_IMPL.toString()
          .equals(crDTOSave.getActionType())//nhan vien thuc thi tra ve QLTD 17
          || Constants.CR_ACTION_CODE.RETURN_TO_APPRAISER_BY_IMPL.toString()
          .equals(crDTOSave.getActionType())//nhan vien thuc thi tra ve CHTH
          || Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_SCH.toString()
          .equals(crDTOSave.getActionType())//tra ve don vi tao khi tiep nhan CR
          || Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_SCH.toString()
          .equals(crDTOSave.getActionType())
          || Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_WHEN_CAB.toString()
          .equals(crDTOSave.getActionType())
          || Constants.CR_ACTION_CODE.RETURN_TO_CONSIDER_WHEN_CAB.toString()
          .equals(crDTOSave.getActionType())
          || Constants.CR_ACTION_CODE.RETURN_TO_MANAGE_WHEN_CAB.toString()
          .equals(crDTOSave.getActionType())
          || Constants.CR_ACTION_CODE.RETURN_TO_CAB_WHEN_SCHEDULE.toString()
          .equals(crDTOSave.getActionType())
          || Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_CAB.toString()
          .equals(crDTOSave.getActionType())
          || Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_CAB.toString()
          .equals(crDTOSave.getActionType()))) {
        List<WoDTO> lstSubWo = getListSubWo(crDTOSave.getCrId(), null, crDTOSave.getCreatedDate(),
            addedWoIdList);
        updateWoWhenChangeCrByAction(lstSubWo, Constants.WO_STATUS_NEW.DRAFT,
            I18n.getLanguage("woCR.woCrUnAssign"));
      }

      //tuannpv14_R517456_start link cr khi chuyen trang thai
      if (Constants.CR_RETURN_MESSAGE.SUCCESS.equals(ret)) {
        try {
          //cap nhat sang WO
          String related = crDTOSave.getRelateCr();
          if (Constants.CR_RETURN_MESSAGE.SUCCESS.equals(ret)
              && Constants.CR_STATE.EVALUATE.equals(crState)
              && !Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_IMPL.toString()
              .equals(crDTOSave.getActionType())
              && ("4".equals(crDTOSave.getRisk()) || Constants.CR_RELATED.PRE_APPROVE
              .equals(related))) {
            //chuyen trang thai wo
            List<WoDTO> lstSubWo = getListSubWo(crDTOSave.getCrId(), null,
                crDTOSave.getCreatedDate(), addedWoIdList);
            if (lstSubWo != null && !lstSubWo.isEmpty()) {
              updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
                  I18n.getLanguage("woTest.woEvaluate"));
              //cap nhat thoi gian cua Wo theo CR
              Map<String, Double> mapWoDuration = getMapDurationByProcessId(
                  crDTOSave.getProcessTypeId());
              updateWoByCrTime(lstSubWo,
                  DateTimeUtils.date2ddMMyyyyHHMMss(crDTOSave.getEarliestStartTime()),
                  DateTimeUtils.date2ddMMyyyyHHMMss(crDTOSave.getLatestStartTime()), mapWoDuration,
                  crDTOSave.getCrProcessId(), crDTOSave.getDutyType());
            }
          }

          //lien ket sang Gate
          if (!Constants.CR_STATE.DRAFT.toString().equals(formRoot.getState())
              && Constants.CR_TYPE.EMERGENCY.toString().equals(formRoot.getCrType())
              && formRoot.getRankGate() != null) {
            String type = "1";
            if (Constants.CR_STATE.CANCEL.toString().equals(formRoot.getState()) ||
                Constants.CR_STATE.INCOMPLETE.toString().equals(formRoot.getState())) {
              type = "";
            }
            String userName = formRoot.getChangeOrginatorName()
                .substring(0, formRoot.getChangeOrginatorName().indexOf("("));
            wsGatePort.updateECR(userName.trim(), formRoot.getCrNumber(), formRoot.getTitle(),
                Long.parseLong(formRoot.getState()),
                StringUtils.convertKeyToValueByMap(Constants.CR_STATE.getGetStateName(),
                    formRoot.getState()), formRoot.getRankGate(), type);
          }

          CrFilesAttachInsiteDTO crFile = new CrFilesAttachInsiteDTO();
          crFile.setCrId(Long.valueOf(crDTOSave.getCrId()));
          List<CrFilesAttachInsiteDTO> lstAttachment = crFilesAttachRepository
              .getListCrFilesAttachDTO(crFile, 0, 1000, "", "");
          String dtCode = "";
          String dtHis = "";
          List<String> lstMop = new ArrayList<String>();
          if (lstAttachment != null) {
            for (CrFilesAttachInsiteDTO dto : lstAttachment) {
              if (dto.getDtCode() != null) {
                dtCode = dto.getDtCode();
                dtHis = dto.getDtFileHistory();
                if (!lstMop.contains(dtCode)) {
                  lstMop.add(dtCode);
                }
              }
            }
          }

          if (dtCode != null && !"".equals(dtCode) && crState != null) {
            if (dtHis != null && dtHis.contains("VIPA_IP")) {
//              WSVipaIpPort wSVipaPort = new WSVipaIpPort();
              wsVipaIpPort
                  .updateCrStatusWithMops(crDTOSave.getCrNumber(), crState.toString(), lstMop);
            } else if (dtHis != null && dtHis.contains("VIPA_DD")) {
              CrInsiteDTO crTemp = crRepository
                  .findCrById(Long.parseLong(crDTOSave.getCrId()), ticketProvider.getUserToken());
              if ("1".equals(crTemp.getIsLoadMop())) {
                lstMop = new ArrayList<>();
                if (lstAttachment != null) {
                  for (CrFilesAttachInsiteDTO dto : lstAttachment) {
                    if (dto.getDtCode() != null) {
                      dtCode = dto.getDtCode();
                      if (!lstMop.contains(dtCode) && StringUtils
                          .isStringNullOrEmpty(dto.getIsRun())) {
                        lstMop.add(dtCode);
                      }
                    }
                  }
                }
              } else if ("2".equals(crTemp.getIsLoadMop())) {
                crFile = new CrFilesAttachInsiteDTO();
                crFile.setCrId(Long.valueOf(crTemp.getRelateToPreApprovedCr()));
                crFile.setIsRun(Long.valueOf(crTemp.getCrId()));
                lstAttachment = crFilesAttachRepository
                    .getListCrFilesAttachDTO(crFile, 0, 1000, "", "");
                lstMop = new ArrayList<>();
                if (lstAttachment != null && !lstAttachment.isEmpty()) {
                  for (CrFilesAttachInsiteDTO dto : lstAttachment) {
                    if (dto.getDtCode() != null) {
                      dtCode = dto.getDtCode();
                      if (!lstMop.contains(dtCode)) {
                        lstMop.add(dtCode);
                      }
                    }
                  }
                }
              }
              if (lstMop != null && !lstMop.isEmpty()) {
                wsVipaDdPort
                    .updateCrStatusWithMops(crDTOSave.getCrNumber(), crState.toString(), lstMop);
              }
            } else {
              //link CR
              if (formRoot.getChangeOrginatorName() != null
                  && formRoot.getChangeOrginatorName().indexOf("(") > 0) {
                wstdttPort.linkCr(crDTOSave.getCrNumber(), dtCode,
                    formRoot.getChangeOrginatorName()
                        .substring(0, formRoot.getChangeOrginatorName().indexOf("(")).trim(),
                    crDTOSave.getTitle(),
                    DateTimeUtils.date2ddMMyyyyHHMMss(crDTOSave.getEarliestStartTime()),
                    DateTimeUtils.date2ddMMyyyyHHMMss(crDTOSave.getLatestStartTime()), crState);
              }
            }
          }

        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void updateWoWhenChangeCrByAction(List<WoDTO> lstSubWo, String woNewStatus,
      String comment) {
    if (lstSubWo != null && !lstSubWo.isEmpty()) {
      for (WoDTO dto : lstSubWo) {
        try {
          dto = woServiceProxy.findWoByIdWSProxy(Long.parseLong(dto.getWoId()));
          String oldStatus = dto.getStatus();
          if (!woNewStatus.equals(oldStatus)) {
            dto.setStatus(woNewStatus);
            dto.setResult(null);
            dto.setFinishTime(null);
            woServiceProxy.updateWoProxy(dto);
            insertWoHis(oldStatus, woNewStatus, dto, comment);
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }

  public String validateForm(CrInsiteDTO crInsiteDTO) {

    //validate rankGate
    if (Constants.CR_TYPE.EMERGENCY.toString().equals(crInsiteDTO.getCrType()) && "1"
        .equals(crInsiteDTO.getIsTracingCr())) {//them
      if (crInsiteDTO.getRankGate() == null) {
        return I18n.getLanguage("cr.list.gateRankTime") + " " + I18n
            .getValidation("cr.msg.must.be.not.null");
      }
    }
    List<String> lstFileType = genListFileTypeRequire(crInsiteDTO, crInsiteDTO.getActionRight(),
        crInsiteDTO.getActionType(), false, crInsiteDTO.getCrType(),
        "1".equals(crInsiteDTO.getIsTracingCr()));
    //validate cr lien ket den phe duyet truoc + phai load DT tu MOP
    if (Constants.CR_TYPE.NORMAL.toString().equals(crInsiteDTO.getCrType())) {
      String related = crInsiteDTO.getCrRelatedCbb();
      if (Constants.CR_RELATED.PRE_APPROVE.equals(related)) {
        lstFileType.add(Constants.CR_FILE_TYPE.DT_SCRIPT);
      }
    }
    CrFilesAttachInsiteDTO crFilesAttachDTO = new CrFilesAttachInsiteDTO();
    crFilesAttachDTO.setCrId(Long.valueOf(crInsiteDTO.getCrId()));
    List<CrFilesAttachInsiteDTO> lstAttachment = crFilesAttachRepository
        .getListCrFilesSearch(crFilesAttachDTO);
    List<CrFilesAttachDTO> lstAttachFileImport = crFilesAttachRepository
        .getListFileImportByProcess(crFilesAttachDTO);
    String msg = validateFileAttach(crInsiteDTO, lstAttachment, lstFileType);
    if (msg.length() > 0) {
      return msg;
    }
    msg = validateFileImportByProcess(crInsiteDTO, lstAttachFileImport,
        crInsiteDTO.getActionRight());

    if (msg.length() > 0) {
      return msg;
    }
    //validate create Wo config
    if (!CR_STATE.DRAFT.toString().equals(crInsiteDTO.getState())) {
      msg = validateCreateWoConf(crInsiteDTO);
      if (msg.length() > 0) {
        return msg;
      }
    }

    //validate nhap tuyen cap cho CR truyen dan
    if ("281".equals(crInsiteDTO.getCountry()) && StringUtils
        .isNotNullOrEmpty(crInsiteDTO.getProcessTypeId())) {
      CrProcessInsideDTO process = crProcessRepository
          .findCrProcessById(Long.valueOf(crInsiteDTO.getProcessTypeId()));
      if (process != null && "1".equals(String.valueOf(process.getIsLaneImpact()))) {
        List<CrCableDTO> lst = crInsiteDTO.getLstLaneCable();
        if (lst == null || lst.isEmpty()) {
          return I18n.getLanguage("lane.list") + " " + I18n.getLanguage("common.isRequire");
        }
      }
    }

    return "";
  }

  public List<String> genListFileTypeRequire(CrInsiteDTO crInsiteDTO, String sActionRight,
      String sActionType, boolean isView, String crType, Boolean isTracingCr) {
    List<String> lstFileType = new ArrayList<String>();
    if (Constants.CR_ACTION_RIGHT.CAN_EDIT.equalsIgnoreCase(sActionRight)) {

      if (String.valueOf(Constants.CR_TYPE.EMERGENCY).equals(crType)) {
        if (isTracingCr) {
          lstFileType.add(Constants.CR_FILE_TYPE.LOG_IMPACT);
        } else {
          lstFileType.add(CR_FILE_TYPE.DT_SCRIPT);
        }

      }

    } else if ((Constants.CR_ACTION_RIGHT.CAN_RECEIVE_STANDARD.equalsIgnoreCase(sActionRight)
        || Constants.CR_ACTION_RIGHT.CAN_RECEIVE_STANDARD_NO_ACCEPT.equalsIgnoreCase(sActionRight))
        && (Constants.CR_ACTION_CODE.ACCEPT.toString().equals(sActionType)
        || Constants.CR_ACTION_CODE.ASSIGN_EXC_TO_EMPLOYEE.toString().equals(sActionType)
        || isView)) {
      lstFileType.add(Constants.CR_FILE_TYPE.DT_SCRIPT);
    } else if ((Constants.CR_ACTION_RIGHT.CAN_CONSIDER.equalsIgnoreCase(sActionRight)
        || Constants.CR_ACTION_RIGHT.CAN_CONSIDER_NO_ASSIGNEE.equalsIgnoreCase(sActionRight)) && (
        Constants.CR_ACTION_CODE.APPRAISE.toString().equals(sActionType)
            || isView)) {
      if (!"121".equals(crInsiteDTO.getImpactSegment())) {
        lstFileType.add(Constants.CR_FILE_TYPE.DT_SCRIPT);
      }
    } else if (Constants.CR_ACTION_RIGHT.CAN_RESOLVE.equalsIgnoreCase(sActionRight)) {
      String processType = crInsiteDTO.getProcessTypeId();
      if ("0".equals(processType)) {
        lstFileType.add(Constants.CR_FILE_TYPE.LOG_IMPACT);
      } else if (processType != null) {
        CrProcessInsideDTO process = crProcessRepository
            .findCrProcessById(Long.valueOf(processType));
        if ("1".equals(String.valueOf(process.getRequireFileLog()))) {
          lstFileType.add(Constants.CR_FILE_TYPE.LOG_IMPACT);
        }
      }

    } else if (Constants.CR_ACTION_CODE.CHANGE_TO_CAB.toString().equals(sActionType)
        || Constants.CR_ACTION_CODE.CHANGE_TO_SCHEDULE.toString().equals(sActionType)
        || (Constants.CR_ACTION_CODE.ASSIGN_TO_CAB.toString().equals(sActionType)
        && Constants.CR_ACTION_RIGHT.CAN_VERIFY.equalsIgnoreCase(sActionRight))) {
      if (!"121".equals(crInsiteDTO.getImpactSegment())) {
        lstFileType.add(Constants.CR_FILE_TYPE.DT_SCRIPT);
      }
    }
    List<CatItemDTO> lst = catItemRepository.getListItemByCategory(CATEGORY.CR_FILE_REQUIRED, null);
    if (lst != null && !lst.isEmpty()) {
      String process = crInsiteDTO.getProcessTypeId();
      for (CatItemDTO itemDTO : lst) {
        if (Constants.CAT_ITEM.FILE_KPI_CR_REQUIRED.equals(itemDTO.getItemCode())
            && itemDTO.getItemValue() != null
            && itemDTO.getItemValue().contains("," + process + ",")) {
          lstFileType.add(Constants.CR_FILE_TYPE.CHECK_KPI);
        }
      }
    }
    return lstFileType;
  }

  public String validateFileAttach(CrInsiteDTO crDTO, List<CrFilesAttachInsiteDTO> lstAttachment,
      List<String> lstFileType) {
    for (String fileType : lstFileType) {
      if (Constants.CR_FILE_TYPE.OTHER.equalsIgnoreCase(fileType)) {
        continue;
      }
      boolean check = false;
      if (lstAttachment != null && !lstAttachment.isEmpty()) {
        for (CrFilesAttachInsiteDTO cfardto : lstAttachment) {
          if (fileType.equalsIgnoreCase(cfardto.getFileType())) {
            check = true;
          }
        }
      }
      if (!check) {
        Map<String, String> mapFile = Constants.CR_FILE_TYPE.getGetText();
        String strReturn = "";
        if (mapFile.containsKey(fileType)) {
          strReturn = mapFile.get(fileType);
        }
        return I18n.getChangeManagement("cr.msg.must.be.attach") +
            " " + I18n.getChangeManagement(strReturn);
      }
      if (fileType.equalsIgnoreCase(Constants.CR_FILE_TYPE.DT_SCRIPT)) {
        if (lstAttachment != null && !lstAttachment.isEmpty()) {
          for (CrFilesAttachInsiteDTO cfardto : lstAttachment) {
            if (fileType.equalsIgnoreCase(cfardto.getFileType())) {
              String processGeneral = crDTO.getProcessTypeId();
              if (StringUtils.isNotNullOrEmpty(crDTO.getProcessTypeLv3Id())) {
                processGeneral = crDTO.getProcessTypeLv3Id();
              }
              return validateCRMOP(processGeneral, cfardto);
            }
          }
        }

      }
    }
    return "";
  }

  public String validateCRMOP(String processType, CrFilesAttachInsiteDTO cfardto) {
    if (StringUtils.isNotNullOrEmpty(processType)) {
      List<String> lstProcess = Arrays.asList(processType.split(","));
      for (String item : lstProcess) {
        CrProcessInsideDTO process = crProcessRepository
            .findCrProcessById(Long.valueOf(item.trim()));
        if (process != null && "1".equals(process.getRequireMop())) {
          CrFilesAttachInsiteDTO dtoFile = crFilesAttachRepository
              .findFileAttachById(cfardto.getFileId());
          if (StringUtils.isStringNullOrEmpty(dtoFile.getDtCode())) {
            return I18n.getChangeManagement("cr.msg.must.be.attach.mop");
          }
        }
      }
    }
    return "";
  }

  public String validateFileImportByProcess(CrInsiteDTO crInsiteDTO,
      List<CrFilesAttachDTO> lstFileImport, String actionRight) {
    String crProcess = crInsiteDTO.getProcessTypeId();
    if (StringUtils.isNotNullOrEmpty(crInsiteDTO.getProcessTypeLv3Id())) {
      crProcess = crInsiteDTO.getProcessTypeLv3Id().trim();
    }
    Map<String, CrFileObjectInsite> mapCheck = getFileImportByProcess(crProcess, actionRight);
    if (mapCheck != null && !mapCheck.isEmpty()) {//Co file import theo quy trinh
      if (lstFileImport == null) {
        return I18n.getChangeManagement("cr.msg.must.be.attach.all");
      } else {
        boolean check = false;
        for (Map.Entry<String, CrFileObjectInsite> entrySet : mapCheck.entrySet()) {
          CrFileObjectInsite value = entrySet.getValue();
          if (value != null) {
            for (CrFilesAttachDTO lstFileImport1 : lstFileImport) {
              if (value.getFileType() != null
                  && value.getFileType().equals(lstFileImport1.getFileType())) {
                check = true;
                break;
              }
            }
          }
        }
        if (!check) {
          return I18n.getChangeManagement("cr.msg.must.be.attach.all");
        }
      }
    }
    return "";
  }

  public Map<String, CrFileObjectInsite> getFileImportByProcess(String crProcessId,
      String actionRight) {
    Map<String, CrFileObjectInsite> map = new HashMap<>();
    String type;
    if (Constants.CR_ACTION_RIGHT.CAN_EDIT.equalsIgnoreCase(actionRight)) {
      type = Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_IN;
    } else if (Constants.CR_ACTION_RIGHT.CAN_RESOLVE.equalsIgnoreCase(actionRight)) {
      type = Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_OUT;
    } else {
      return map;
    }
    List<CrFileObjectInsite> lstFileObject = crFilesAttachRepository
        .getListTemplateFileByProcess(crProcessId, type);
    if (lstFileObject != null && !lstFileObject.isEmpty()) {
      for (CrFileObjectInsite lstFileObject1 : lstFileObject) {
        map.put(lstFileObject1.getIdTemplate(), lstFileObject1);
      }
    }
    return map;
  }

  private String validateCreateWoConf(CrInsiteDTO crDTO) {
    String processGeneral = crDTO.getProcessTypeId();
    if (StringUtils.isNotNullOrEmpty(crDTO.getProcessTypeLv3Id())) {
      processGeneral = crDTO.getProcessTypeLv3Id().trim();
    }
    List<CrProcessWoDTO> lstCrWo = crProcessWoRepository
        .getLstWoFromProcessId(processGeneral);
    if (lstCrWo != null && !lstCrWo.isEmpty()) {
      List<String> addedWoIdList = null;
      if (StringUtils.isNotNullOrEmpty(crDTO.getListWoId())) {
        addedWoIdList = Arrays.asList(crDTO.getListWoId().split(","));
      }
      List<WoDTO> lstSubWo = getListSubWo(crDTO.getCrId(), null, crDTO.getCreatedDate(),
          addedWoIdList);
      List<String> lstCreated = new ArrayList<>();
      HashMap<String, Integer> addedMap = new HashMap<>();
      HashMap<String, HashSet<String>> addedName = new HashMap<>();
      HashMap<String, Integer> neededMap = new HashMap<>();

      if (lstSubWo != null && !lstSubWo.isEmpty()) {
        for (WoDTO dto : lstSubWo) {
          lstCreated.add(dto.getWoContent());
          if (dto.getWoTypeId() != null) {
            int count = 1;
            if (addedMap.containsKey(dto.getWoTypeId())) {
              count = addedMap.get(dto.getWoTypeId()) + 1;
              addedName.get(dto.getWoTypeId()).add(dto.getWoContent());
            } else {
              HashSet<String> set = new HashSet();
              set.add(dto.getWoContent());
              addedName.put(dto.getWoTypeId(), set);
            }
            addedMap.put(dto.getWoTypeId(), count);

          }
        }
      }
      //kiem tra WO bat buoc tao
      for (CrProcessWoDTO dto : lstCrWo) {
        String name = dto.getWoName();
        Long isRequire = dto.getIsRequire();
        Long createWoWhenCloseCr = dto.getCreateWoWhenCloseCR();
        if (isRequire != null && isRequire.intValue() == 1) {
          if (createWoWhenCloseCr == null || createWoWhenCloseCr.intValue() != 1) {

            if (dto.getWoTypeId() != null) {
              String woTypeStr = String.valueOf(dto.getWoTypeId());
              int count = 1;
              if (addedMap.containsKey(woTypeStr)) {
                count = addedMap.get(woTypeStr) + 1;
              }
              neededMap.put(woTypeStr, count);

              if (!addedMap.containsKey(woTypeStr)
                  || (addedMap.containsKey(woTypeStr) && addedMap.get(woTypeStr) < count)) {
                // || !(addedName.get(woTypeStr).contains(dto.getWoName()) || addedName.get(woTypeStr).contains(dto.getWoName() + "_" + crDTO.getCrNumber()))
                if (!addedName.containsKey(woTypeStr)) {
                  return I18n.getChangeManagement("cr.wo.createRequire") + ": " + name;
                }

              }

            }
          }
        }

      }
      //kiem tra WO ko bat buoc tao
      try {
        WorkLogCategoryInsideDTO wlCatCon = new WorkLogCategoryInsideDTO();
        wlCatCon.setPage(1);
        wlCatCon.setPageSize(2);
        wlCatCon.setWlayCode(Constants.WORKLOG_CAT.CR_NO_WO_CODE);
        List<WorkLogCategoryInsideDTO> lstWlCat = mrCategoryUtil
            .getListWorkLogCategoryDTO(wlCatCon);
        if (lstWlCat != null && !lstWlCat.isEmpty()) {
          WorkLogCategoryInsideDTO wlDto = lstWlCat.get(0);
          WorkLogInsiteDTO wlCon = new WorkLogInsiteDTO();
          wlCon.setWlgObjectId(
              DataUtil.isNullOrEmpty(crDTO.getCrId()) ? null : Long.valueOf(crDTO.getCrId()));
          wlCon.setWlayId(wlDto.getWlayId());
          wlCon.setUserGroupAction(wlDto.getWlayType());
          wlCon.setWlgObjectType(Long.valueOf(Constants.WORK_LOG_SYSTEM.CR));
          wlCon.setOffset(TimezoneContextHolder.getOffsetDouble());
          wlCon.setPage(0);
          wlCon.setPageSize(100);
          List<WorkLogInsiteDTO> lstWl = mrCategoryUtil.getListWorkLogDTO(wlCon);

          for (CrProcessWoDTO dto : lstCrWo) {
            String name = dto.getWoName();
            Long isRequire = dto.getIsRequire();
            Long createWoWhenCloseCr = dto.getCreateWoWhenCloseCR();
            if (!lstCreated.contains(name) && !isRequire.equals(1L) && (lstWl == null || lstWl
                .isEmpty()) && !"1".equals(String.valueOf(createWoWhenCloseCr))) {
              return I18n.getChangeManagement("common.confirmWo");
            }
          }

        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }

    }
    return "";
  }

  public String validateActionClick(CrInsiteDTO crInsiteDTO) {
    List<String> lstFileType = genListFileTypeRequire(crInsiteDTO, crInsiteDTO.getActionRight(),
        crInsiteDTO.getActionType(), false, crInsiteDTO.getCrType(),
        "1".equals(crInsiteDTO.getIsTracingCr()));
    CrFilesAttachInsiteDTO crFilesAttachDTO = new CrFilesAttachInsiteDTO();
    crFilesAttachDTO.setCrId(Long.valueOf(crInsiteDTO.getCrId()));
    List<CrFilesAttachInsiteDTO> lstAttachment = crFilesAttachRepository
        .getListCrFilesSearch(crFilesAttachDTO);
    List<CrFilesAttachDTO> lstFileImport = crFilesAttachRepository
        .getListFileImportByProcess(crFilesAttachDTO);
    return validateFormProcess(crInsiteDTO, lstAttachment, lstFileType, lstFileImport);
  }

  public String validateFormProcess(CrInsiteDTO crDTO, List<CrFilesAttachInsiteDTO> lstAttachment,
      List<String> lstFileType, List<CrFilesAttachDTO> lstFileImport) {
    //tuanpv14_validate bat buoc nhap Worklog cab start
    if (Constants.CR_ACTION_CODE.CAB.toString().equals(crDTO.getActionType().trim())) {
      try {
        WorkLogCategoryInsideDTO wlCatCon = new WorkLogCategoryInsideDTO();
        wlCatCon.setPage(1);
        wlCatCon.setPageSize(2);
        wlCatCon.setWlayCode(Constants.WORKLOG_CAT.CR_CAB_CODE);
        List<WorkLogCategoryInsideDTO> lstWlCat;
        lstWlCat = mrCategoryUtil.getListWorkLogCategoryDTO(wlCatCon);
        if (lstWlCat != null && !lstWlCat.isEmpty()) {
          WorkLogCategoryInsideDTO wlDto = lstWlCat.get(0);
          WorkLogInsiteDTO wlCon = new WorkLogInsiteDTO();
          wlCon.setWlgObjectId(DataUtil.isNullOrEmpty(crDTO.getCrId()) ? null : Long
              .valueOf(crDTO.getCrId()));
          wlCon.setUserGroupAction(wlDto.getWlayType());
          wlCon.setWlgObjectType(Long.valueOf(Constants.WORK_LOG_SYSTEM.CR));
          wlCon.setOffset(TimezoneContextHolder.getOffsetDouble());
          wlCon.setPage(0);
          wlCon.setPageSize(100);
          List<WorkLogInsiteDTO> lstWl = mrCategoryUtil.getListWorkLogDTO(wlCon);
          if (lstWl == null || lstWl.isEmpty()) {
            return I18n.getChangeManagement("cr.wlCab.require");
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }

    //tuanpv14_validate bat buoc nhap Worklog cab end
    if (Constants.CR_ACTION_CODE.RESOLVE.toString().equals(crDTO.getActionType().trim())
        || Constants.CR_ACTION_CODE.RESOLVE_APPROVE_STD.toString()
        .equals(crDTO.getActionType().trim())) {
      try {
        List<WorkLogCategoryInsideDTO> lstWlCat;
        if (Constants.CR_ACTION_CODE.RESOLVE.toString().equals(crDTO.getActionType().trim())) {
          WorkLogCategoryInsideDTO wlCatCon = new WorkLogCategoryInsideDTO();
          wlCatCon.setPage(1);
          wlCatCon.setPageSize(2);
          wlCatCon.setWlayCode(Constants.WORKLOG_CAT.CR_RESOLVE_CODE);
          lstWlCat = mrCategoryUtil.getListWorkLogCategoryDTO(wlCatCon);
          if (lstWlCat != null && !lstWlCat.isEmpty()) {
            WorkLogCategoryInsideDTO wlDto = lstWlCat.get(0);
            WorkLogInsiteDTO wlCon = new WorkLogInsiteDTO();
            wlCon.setWlgObjectId(DataUtil.isNullOrEmpty(crDTO.getCrId()) ? null : Long
                .valueOf(crDTO.getCrId()));
            wlCon.setUserGroupAction(wlDto.getWlayType());
            wlCon.setWlgObjectType(Long.valueOf(Constants.WORK_LOG_SYSTEM.CR));
            wlCon.setOffset(TimezoneContextHolder.getOffsetDouble());
            wlCon.setPage(0);
            wlCon.setPageSize(100);
            List<WorkLogInsiteDTO> lstWl = mrCategoryUtil.getListWorkLogDTO(wlCon);
            if (lstWl == null || lstWl.isEmpty()) {
              return I18n.getChangeManagement("cr.wlResolve.require");
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      //tuanpv14_validate bat buoc nhap Worklog cab / hoan thanh tac dong end
    }

    String msg = validateFileAttach(crDTO, lstAttachment, lstFileType);
    if (msg.length() > 0) {
      return msg;
    }
    msg = validateFileImportByProcess(crDTO, lstFileImport, crDTO.getActionRight());
    if (msg.length() > 0) {
      return msg;
    }

    if (Constants.CR_ACTION_CODE.RETURN_TO_MANAGE_WHEN_CAB.toString()
        .equals(crDTO.getActionType().trim())) {
      if (!validateAllowReturnToManageWhenCab(crDTO)) {
        return I18n.getChangeManagement("cr.cab.return.to.manage.notAllow");
      }
    }
    return "";
  }

  public boolean validateAllowReturnToManageWhenCab(CrInsiteDTO crDTO) {

    List<UserCabCrForm> lstUserCab = crGeneralRepository
        .getListUserCab(crDTO.getImpactSegment(), crDTO.getChangeResponsibleUnit());
    if (lstUserCab != null && !lstUserCab.isEmpty()) {
      UserCabCrForm form1 = lstUserCab.get(0);
      if (form1.getUsername().equals(ticketProvider.getUserToken().getUserName())) {
        if (lstUserCab.size() == 1) {
          return false;
        } else {
          UserCabCrForm form2 = lstUserCab.get(1);
          crDTO.setUserCab(form2.getUserCab());
        }
      } else {
        crDTO.setUserCab(form1.getUserCab());
      }
    } else {
      return false;
    }
    return true;
  }

  //kiem tra danh sach WO con
  public CrProcessWoDTO processWOTabAdd(CrInsiteDTO crInsiteDTO) {
    String processId = crInsiteDTO.getProcessTypeLv3Id();
    List<CrProcessWoDTO> lst = crProcessWoRepository.getLstWoFromProcessId(processId);
    CrProcessWoDTO pDTODef = null;
    if (lst != null && !lst.isEmpty()) {
      List<WoDTO> lstSub = getListSubWoFromWoTab(crInsiteDTO.getCrId(), null,
          crInsiteDTO.getCreatedDate());
      List<String> lstWoCreated = new ArrayList<>();
      HashMap<String, Integer> addedMap = new HashMap<>();
      HashMap<String, HashSet<String>> addedName = new HashMap<>();
      HashMap<String, Integer> neededMap = new HashMap<>();

      if (lstSub != null) {
        for (WoDTO wo : lstSub) {
          lstWoCreated.add(wo.getWoContent());
          if (wo.getWoTypeId() != null) {
            int count = 1;
            if (addedMap.containsKey(wo.getWoTypeId())) {
              count = addedMap.get(wo.getWoTypeId()) + 1;
              addedName.get(wo.getWoTypeId()).add(wo.getWoContent());
            } else {
              HashSet<String> set = new HashSet();
              set.add(wo.getWoContent());
              addedName.put(wo.getWoTypeId(), set);
            }
            addedMap.put(wo.getWoTypeId(), count);

          }
        }
      }
      for (CrProcessWoDTO d : lst) {
        if (d.getIsRequire() != null && d.getIsRequire().intValue() == 1) {

          if (d.getCreateWoWhenCloseCR() == null || d.getCreateWoWhenCloseCR().intValue() != 1) {

            if (d.getWoTypeId() != null) {
              String woTypeStr = String.valueOf(d.getWoTypeId());
              int count = 1;
              if (addedMap.containsKey(woTypeStr)) {
                count = addedMap.get(woTypeStr) + 1;
              }
              neededMap.put(woTypeStr, count);

              if (!addedMap.containsKey(woTypeStr)
                  || (addedMap.containsKey(woTypeStr) && addedMap.get(woTypeStr) < count)) {

                if (!addedName.containsKey(woTypeStr)) {
                  // || !(addedName.get(woTypeStr).contains(d.getWoName())
                  //                    || addedName.get(woTypeStr)
                  //                    .contains(d.getWoName() + "_" + crInsiteDTO.getCrNumber()))
                  pDTODef = d;
                  List<CatItemDTO> data = catItemBusiness.getListItemByCategory("WO_CR_SPEC", "");
                  if (data != null && data.size() > 0) {
                    for (CatItemDTO item : data) {
                      List<String> itemList =
                          StringUtils.isStringNullOrEmpty(item.getItemValue()) ? new ArrayList<>()
                              : Arrays.asList(item.getItemValue().trim().split(";"));
                      if (itemList.contains(String.valueOf(pDTODef.getWoTypeId()))) {
                        pDTODef.setDurationWo(2D);
                      }
                    }
                  }
                  break;
                }

              }

            }

          }

        }

      }
      if (pDTODef == null) {
        try {
          WorkLogCategoryInsideDTO wlCatCon = new WorkLogCategoryInsideDTO();
          wlCatCon.setWlayCode(Constants.WORKLOG_CAT.CR_NO_WO_CODE);
          wlCatCon.setPage(1);
          wlCatCon.setPageSize(2);
          List<WorkLogCategoryInsideDTO> lstWlCat = mrCategoryUtil
              .getListWorkLogCategoryDTO(wlCatCon);
          if (lstWlCat != null && !lstWlCat.isEmpty()) {
            WorkLogCategoryInsideDTO wlDto = lstWlCat.get(0);
            WorkLogInsiteDTO wlCon = new WorkLogInsiteDTO();
            wlCon.setWlgObjectId(DataUtil.isNullOrEmpty(crInsiteDTO.getCrId()) ? null : Long
                .valueOf(crInsiteDTO.getCrId()));
            wlCon.setUserGroupAction(wlDto.getWlayType());
            wlCon.setWlgObjectType(Long.valueOf(Constants.WORK_LOG_SYSTEM.CR));
            wlCon.setOffset(TimezoneContextHolder.getOffsetDouble());
            wlCon.setPage(0);
            wlCon.setPageSize(100);
            List<WorkLogInsiteDTO> lstWl = mrCategoryUtil.getListWorkLogDTO(wlCon);
            for (CrProcessWoDTO d : lst) {
              if (!d.getCreateWoWhenCloseCR().equals(1L) && d.getIsRequire().equals(0L)
                  && !(lstWoCreated.contains(d.getWoName()) || lstWoCreated
                  .contains(d.getWoName() + "_" + crInsiteDTO.getCrNumber())) && (lstWl == null
                  || lstWl.isEmpty())) {
                pDTODef = d;
                List<CatItemDTO> data = catItemBusiness.getListItemByCategory("WO_CR_SPEC", "");
                if (data != null && data.size() > 0) {
                  for (CatItemDTO item : data) {
                    List<String> itemList =
                        StringUtils.isStringNullOrEmpty(item.getItemValue()) ? new ArrayList<>()
                            : Arrays.asList(item.getItemValue().trim().split(";"));
                    if (itemList.contains(String.valueOf(pDTODef.getWoTypeId()))) {
                      pDTODef.setDurationWo(2D);
                    }
                  }
                }
                break;
              }
            }
          }

        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }

      }
    }
    return pDTODef;
  }

  private List<WoDTO> getListSubWoFromWoTab(String crId, String type, Date crCreateDate)
      throws NumberFormatException {
    List<WoDTO> lstSubWo = new ArrayList<>();
    try {
      ConditionBean conditionBean1 = new ConditionBean("woSystem", "CR", Constants.NAME_EQUAL,
          Constants.STRING);
      ConditionBean conditionBean2 = new ConditionBean("woSystemId", crId, Constants.NAME_EQUAL,
          Constants.STRING);
      if (crCreateDate == null) {
        crCreateDate = new Date();
      }
      Calendar cal = Calendar.getInstance();
      cal.setTime(crCreateDate);
      cal.add(Calendar.DATE, -3);
      String fromDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
      ConditionBean conditionBean3 = new ConditionBean("createDate", fromDate,
          Constants.NAME_GREATER_EQUAL, CrConfig.TYPE_DATE);

      List<ConditionBean> lstConditionBeans = new ArrayList<>();
      lstConditionBeans.add(conditionBean1);
      lstConditionBeans.add(conditionBean2);
      lstConditionBeans.add(conditionBean3);

      lstSubWo = woServiceProxy
          .getListWoByCondition(new BaseDto(lstConditionBeans, 0, Integer.MAX_VALUE, "", "woId"));
      if (lstSubWo != null && !lstSubWo.isEmpty() && type != null) {
        lstSubWo.removeIf(c -> (!c.getWoCode().contains(type)));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstSubWo;
  }

  public String actionCloseCrAfter(CrInsiteDTO crDTO) {
    try {
      if (crDTO.getCrId() != null) {

        Long crId = this.stringToLong(crDTO.getCrId());
        CrCreatedFromOtherSysDTO dto = crForOtherSystemBusiness.getCrCreatedFromOtherSysDTO(crId);

        if (dto != null && dto.getStatus() != null
            && "OK".equals(dto.getStatus())
            && "1".equals(dto.getSystemId())) {
          if ("42".equalsIgnoreCase(crDTO.getActionReturnCodeId())) {
            mrCategoryProxy.reCreatedOrCloseCr(crDTO.getCrId(), "1");
          } else {
            mrCategoryProxy.reCreatedOrCloseCr(crDTO.getCrId(), "0");
          }
        }
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    //hanh dong tao WO
    StringBuilder wrongBuiDer = new StringBuilder();
    try {
      String processType = crDTO.getProcessTypeLv3Id();
      if (StringUtils.isStringNullOrEmpty(processType)) {
        processType = crDTO.getProcessTypeId();
      }
      if (crDTO.getCreateWoAfterClose() && StringUtils.isNotNullOrEmpty(processType)) {
        List<CrProcessWoDTO> lstCrWo = crProcessWoRepository.getLstWoFromProcessId(processType);
        if (lstCrWo != null && !lstCrWo.isEmpty()) {

          //System.out.println("START 1");
          for (CrProcessWoDTO woDTO : lstCrWo) {
            if (woDTO.getCreateWoWhenCloseCR() != null
                && woDTO.getCreateWoWhenCloseCR().intValue() == 1) {

              Long unitId = crDTO.getChangeResponsibleUnit() == null ? null
                  : Long.valueOf(crDTO.getChangeResponsibleUnit());
              CoordinationSettingDTO setting = coordinationSettingRepository
                  .getCoordinationSettingInfor(unitId, null);
              if (setting == null) {
                UnitDTO childUnit = unitRepository.findUnitById(unitId);
                if (childUnit == null) {
                  continue;
                }
                setting = coordinationSettingRepository.getCoordinationSettingInfor(
                    childUnit.getParentUnitId() == null ? null
                        : Long.valueOf(childUnit.getParentUnitId()), null);
              }

              if (setting == null) {
                continue;
              }

              //System.out.println("START 2");
              WoDTO wdto = createWoCMDB(crDTO, woDTO, setting);

              //System.out.println("START 3");
              if (wdto != null) {
                ResultDTO resultDTO = woServiceProxy.insertWoForSPMProxy(wdto);
                if (!"SUCCESS".equalsIgnoreCase(resultDTO.getMessage())) {
                  log.error(resultDTO.getMessage());
                }
              }
            }
          }
        }
      }
      return wrongBuiDer.toString();
    } catch (Exception e) {
      //
      log.error(e.getMessage(), e);
    }
    return "";
  }

  public WoDTO createWoCMDB(CrInsiteDTO crDto, CrProcessWoDTO woProcessDTO,
      CoordinationSettingDTO setting) {

    try {
      if (woProcessDTO == null || crDto == null || woProcessDTO.getWoTypeId() == null) {
        return null;
      }

      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      CrImpactedNodesDTO crImpactedNodesDTO0 = new CrImpactedNodesDTO();
      crImpactedNodesDTO0.setCrId(crDto.getCrId());
      crImpactedNodesDTO0.setInsertTime(DateTimeUtils.date2ddMMyyyyHHMMss(crDto.getCreatedDate()));
      crImpactedNodesDTO0.setType(Constants.CR_NODE_TYPE.EFFECT);
      List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs = crImpactedNodesBusiness
          .search(crImpactedNodesDTO0, 0, 1000, "", "");//crDTOMain.getLstNetworkNodeId();
      StringBuilder builder = new StringBuilder();
      if (lstCrImpactedNodesDTOs != null && !lstCrImpactedNodesDTOs.isEmpty()) {
        lstCrImpactedNodesDTOs.forEach((node) -> {
          builder.append(node.getDeviceCode()).append(" - ").append(node.getIp()).append(";");
        });
      }

      WoPriorityDTO woPriorityDTO = new WoPriorityDTO();
      woPriorityDTO.setWoTypeId(woProcessDTO.getWoTypeId());
      woPriorityDTO.setPage(1);
      woPriorityDTO.setPageSize(10);
      woPriorityDTO.setSortName("priorityName");
      List<WoPriorityDTO> listPriority = woCategoryServiceProxy.getListWoPriorityDTO(woPriorityDTO);

      String priorityId = null;
      if (listPriority != null && !listPriority.isEmpty()) {
        priorityId = String.valueOf(listPriority.get(0).getPriorityId());
      }

      WoDTO woDTO = new WoDTO();
      woDTO.setWoSystem("CR");
      woDTO.setCreatePersonId(systemUserId != null ? systemUserId : "256066");
      woDTO.setWoTypeId(String.valueOf(woProcessDTO.getWoTypeId()));

      woDTO.setWoContent(woProcessDTO.getWoName());
      woDTO.setWoDescription(builder.toString());
      Date sysDate = new Date();
      woDTO.setStartTime(dateFormat.format(sysDate));

      Calendar cal = Calendar.getInstance();
      cal.setTime(sysDate);
      cal.add(Calendar.DAY_OF_YEAR, woProcessDTO.getDurationWo().intValue());
      woDTO.setEndTime(dateFormat.format(cal.getTime()));

      woDTO.setCreateDate(dateFormat.format(sysDate));
      woDTO.setPriorityId(priorityId != null ? priorityId : null);//muc uu tien
      woDTO.setCdId(String.valueOf(setting.getGroupId()));
      woDTO.setWoSystemId(crDto.getCrId());

      return woDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public boolean validateTime(CrInsiteDTO crDTO, Boolean isAddNew, String locale) {
    try {
      if (crDTO.getEarliestStartTime() == null) {
        return false;
      }
      if (crDTO.getLatestStartTime() == null) {
        return false;
      }

      if (crDTO.getEarliestStartTime().compareTo(crDTO.getLatestStartTime()) > -1) {
        return false;
      }

      Date startDate = null;
      Date endDate = null;
      Date startDateImpact = null;
      Date endDateImpact = null;
      startDate = crDTO.getEarliestStartTime();
      endDate = crDTO.getLatestStartTime();
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType()) && "1"
          .equals(crDTO.getIsTracingCr())) {
      } else {
        if (!validateCRDateByDutyDate(startDate, endDate, crDTO, locale)) {
          return false;
        }
      }

      startDate = crDTO.getEarliestStartTime();
      endDate = crDTO.getLatestStartTime();

      if (crDTO.getDisturbanceStartTime() != null) {
        startDateImpact = crDTO.getDisturbanceStartTime();
      }
      if (crDTO.getDisturbanceEndTime() != null) {
        endDateImpact = crDTO.getDisturbanceEndTime();
      }

      if (isAddNew
          || (crDTO.getActionType() != null
          && (Constants.CR_ACTION_CODE.UPDATE.toString().equals(crDTO.getActionType().trim())
          || Constants.CR_ACTION_CODE.APPROVE.toString().equals(crDTO.getActionType().trim())
          || Constants.CR_ACTION_CODE.ASSIGN_TO_CONSIDER.toString()
          .equals(crDTO.getActionType().trim())
          || Constants.CR_ACTION_CODE.CHANGE_TO_CAB.toString().equals(crDTO.getActionType().trim())
          || Constants.CR_ACTION_CODE.ASSIGN_TO_CAB.toString().equals(crDTO.getActionType().trim())
          || Constants.CR_ACTION_CODE.CAB.toString().equals(crDTO.getActionType().trim())
          || Constants.CR_ACTION_CODE.SCHEDULE.toString().equals(crDTO.getActionType().trim())
          || Constants.CR_ACTION_CODE.EDIT_CR_BY_QLTD.toString()
          .equals(crDTO.getActionType().trim())
      ))) {

        if (startDate != null && startDate.compareTo(new Date()) < 0
            && !Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
          return false;
        }

        if (endDate != null && startDate != null && endDate.compareTo(startDate) < 0) {
          return false;
        }

        if (startDateImpact != null && endDateImpact == null) {
          return false;
        }

        if (startDateImpact == null && endDateImpact != null) {
          return false;
        }

        if (endDateImpact != null && startDateImpact != null
            && endDateImpact.compareTo(startDateImpact) < 0) {
          return false;
        }
        if (startDate != null && startDateImpact != null && endDate != null && endDateImpact != null
            && (startDateImpact.compareTo(startDate) < 0 || endDateImpact.compareTo(endDate) > 0)) {
          return false;
        }
      }

      return true;
    } catch (NumberFormatException e) {
      log.error(e.getMessage(), e);
      return false;
    }
  }

  public boolean validateCRDateByDutyDate(Date startDate, Date endDate, CrInsiteDTO crInsiteDTO,
      String locale) {
    CrImpactFrameInsiteDTO form = new CrImpactFrameInsiteDTO();
    form.setImpactFrameId(Long.valueOf(crInsiteDTO.getDutyType()));
    List<ItemDataCRInside> lstFrame = crGeneralRepository.getListDutyTypeCBB(form, locale);
    ItemDataCRInside dataCR = lstFrame.get(0);
    String startend = dataCR.getSecondValue();
    if (startend != null) {
      String[] startendarray = startend.split(",");
      if (startendarray.length > 1) {
        String[] startDuty = startendarray[0].split(":");
        String[] endDuty = startendarray[1].split(":");
        if (startDuty.length > 2 && endDuty.length > 2) {
          Calendar startDutyCal = Calendar.getInstance();
          startDutyCal.clear();
          startDutyCal.setTime(startDate);
          if (startDate != null) {
            startDutyCal.set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                Integer.valueOf(startDuty[2]));
          }
          Date startDutyDate = startDutyCal.getTime();
          Calendar endDutyCal = Calendar.getInstance();
          endDutyCal.clear();
          if (startDate != null) {
            endDutyCal.set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                Integer.valueOf(endDuty[2]));
          }
          Date endDutyDate;
          if (Integer.valueOf(startDuty[0]) > Integer.valueOf(endDuty[0])) {//tac dong dem
            Calendar startDutyCalCheck = Calendar.getInstance();
            startDutyCalCheck.clear();
            if (startDate != null) {
              startDutyCalCheck
                  .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                      0, 0, 0);
            }
            Date checkstartDutyDate = startDutyCalCheck.getTime();
            Calendar endDutyCalCheck = startDutyCalCheck;
            endDutyCalCheck.clear();
            if (endDate != null) {
              endDutyCalCheck.set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate(),
                  0, 0, 0);
            }
            Date checkendDutyDate = endDutyCalCheck.getTime();//1445014800000 | 1445014800000
            if (startDate != null) {
              if (endDate != null && checkstartDutyDate.equals(checkendDutyDate)) {//Cung ngay
                if (endDate.getHours() <= (Integer.valueOf(endDuty[0])
                    + 1)) {//sang hom sau (00h-5h)
                  startDutyCal
                      .set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate() - 1,
                          Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                          Integer.valueOf(startDuty[2]));
                  startDutyDate = startDutyCal.getTime();
                  endDutyCal.set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate(),
                      Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                      Integer.valueOf(endDuty[2]));
//                            endDutyDate = endDutyCal.getTime();
                } else {//dem hom truoc (23h-24h)
                  startDutyCal
                      .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                          Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                          Integer.valueOf(startDuty[2]));
                  startDutyDate = startDutyCal.getTime();
                  endDutyCal
                      .set(startDate.getYear() + 1900, startDate.getMonth(),
                          startDate.getDate() + 1,
                          Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                          Integer.valueOf(endDuty[2]));
//                            endDutyDate = endDutyCal.getTime();
                }
              } else {
                endDutyCal
                    .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate() + 1,
                        Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                        Integer.valueOf(endDuty[2]));
//                        endDutyDate = endDutyCal.getTime();
              }
            }
          }
//          endDutyCal.add(Calendar.MINUTE, 1);
          endDutyDate = endDutyCal.getTime();
          if (startDate != null && endDate != null) {
            if (startDate.compareTo(startDutyDate) < 0
                || endDate.compareTo(endDutyDate) > 0) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  public String validateForService(CrInsiteDTO crInsiteDTO) {
    if (CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_CAB.toString()
        .equals(crInsiteDTO.getActionType())) {
      if (StringUtils.isStringNullOrEmpty(crInsiteDTO.getConsiderUserId())) {
        return RESULT.ERROR;
      }
    }
    List<String> lstFileType = genListFileTypeRequire(crInsiteDTO, crInsiteDTO.getActionRight(),
        crInsiteDTO.getActionType(), false, crInsiteDTO.getCrType(),
        "1".equals(crInsiteDTO.getIsTracingCr()));
    CrFilesAttachInsiteDTO crFilesAttachDTO = new CrFilesAttachInsiteDTO();
    crFilesAttachDTO.setCrId(Long.valueOf(crInsiteDTO.getCrId()));
    List<CrFilesAttachInsiteDTO> lstAttachment = crFilesAttachRepository
        .getListCrFilesSearch(crFilesAttachDTO);
    List<CrFilesAttachDTO> lstFileImport = crFilesAttachRepository
        .getListFileImportByProcess(crFilesAttachDTO);
    return validateFormProcessForService(crInsiteDTO, lstAttachment, lstFileType, lstFileImport);
  }

  public String validateFormProcessForService(CrInsiteDTO crDTO,
      List<CrFilesAttachInsiteDTO> lstAttachment,
      List<String> lstFileType, List<CrFilesAttachDTO> lstFileImport) {
    //tuanpv14_validate bat buoc nhap Worklog cab star
    CrInsiteDTO formRoot = crRepository
        .findCrById(Long.valueOf(crDTO.getCrId()), ticketProvider.getUserToken());

    if (Constants.CR_ACTION_CODE.RETURN_TO_CONSIDER_WHEN_CAB.toString()
        .equals(crDTO.getActionType().trim())
        || Constants.CR_ACTION_CODE.RETURN_TO_APPRAISER_BY_IMPL.toString()
        .equals(crDTO.getActionType().trim())
        || Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_SCH.toString()
        .equals(crDTO.getActionType().trim())) {
      if (StringUtils.isStringNullOrEmpty(formRoot.getConsiderUnitId())
          && StringUtils.isStringNullOrEmpty(formRoot.getConsiderUserId())) {
        return I18n.getChangeManagement("cr.cab.return.to.consider.notAllow");
      }
    }

    if (Constants.CR_ACTION_CODE.RETURN_TO_MANAGE_WHEN_CAB.toString()
        .equals(crDTO.getActionType().trim())) {
      if (!validateAllowReturnToManageWhenCabService(crDTO)) {
        return I18n.getChangeManagement("cr.cab.return.to.manage.notAllow");
      }
    }
    if (Constants.CR_ACTION_CODE.RETURN_TO_CAB_WHEN_SCHEDULE.toString()
        .equals(crDTO.getActionType().trim())) {
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(formRoot.getCrType())) {
        return RESULT.ERROR;
      } else {
        if ("4".equals(formRoot.getRisk()) || "121".equals(formRoot.getImpactSegment())) {
          return RESULT.ERROR;
        }
      }
    }

    return "";
  }

  public boolean validateAllowReturnToManageWhenCabService(CrInsiteDTO crDTO) {

    List<UserCabCrForm> lstUserCab = crGeneralRepository
        .getListUserCab(crDTO.getImpactSegment(), crDTO.getChangeResponsibleUnit());
    if (lstUserCab != null && !lstUserCab.isEmpty()) {
      UserCabCrForm form1 = lstUserCab.get(0);
      if (form1.getUsername().equals(crDTO.getUserLogin())) {
        if (lstUserCab.size() == 1) {
          return false;
        } else {
          UserCabCrForm form2 = lstUserCab.get(1);
          crDTO.setUserCab(form2.getUserCab());
        }
      } else {
        crDTO.setUserCab(form1.getUserCab());
      }
    } else {
      return false;
    }
    return true;
  }

  public String updateCrToMop(CrInsiteDTO crDTO) {
    try {
      CrFilesAttachDTO crFile = new CrFilesAttachDTO();
      crFile.setCrId(crDTO.getCrId());
      List<CrFilesAttachDTO> lstAttachment = crFilesAttachRepository
          .search(crFile, 0, 1000, "", "");
      String dtCode = "";
      String dtHis = "";
      List<String> lstMop = new ArrayList<>();
      if (lstAttachment != null) {
        for (CrFilesAttachDTO dto : lstAttachment) {
          if (dto.getDtCode() != null) {
            dtCode = dto.getDtCode();
            dtHis = dto.getDtFileHistory();
            if (!lstMop.contains(dtCode)) {
              lstMop.add(dtCode);
            }
          }
        }
      }

      if (StringUtils.isNotNullOrEmpty(dtCode) && crDTO.getState() != null) {
        if (dtHis != null && dtHis.contains("VIPA_IP")) {
          wsVipaIpPort
              .updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);
        } else if (dtHis != null && dtHis.contains("VIPA_DD")) {
          if ("1".equals(crDTO.getIsLoadMop())) {
            lstMop = new ArrayList<>();
            if (lstAttachment != null) {
              for (CrFilesAttachDTO dto : lstAttachment) {
                if (dto.getDtCode() != null) {
                  dtCode = dto.getDtCode();
                  if (!lstMop.contains(dtCode) && StringUtils.isStringNullOrEmpty(dto.getIsRun())) {
                    lstMop.add(dtCode);
                  }
                }
              }
            }
          } else if ("2".equals(crDTO.getIsLoadMop())) {
            crFile = new CrFilesAttachDTO();
            crFile.setCrId(crDTO.getRelateToPreApprovedCr());
            crFile.setIsRun(crDTO.getCrId());
            lstAttachment = crFilesAttachRepository.search(crFile, 0, 1000, "", "");
            lstMop = new ArrayList<>();
            if (lstAttachment != null && !lstAttachment.isEmpty()) {
              for (CrFilesAttachDTO dto : lstAttachment) {
                if (dto.getDtCode() != null) {
                  dtCode = dto.getDtCode();
                  if (!lstMop.contains(dtCode)) {
                    lstMop.add(dtCode);
                  }
                }
              }
            }
          }
          if (lstMop != null && !lstMop.isEmpty()) {
            wsVipaDdPort.updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);
          }
        } else {
          if (StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginatorName())) {
            wstdttPort.linkCr(crDTO.getCrNumber(), dtCode, crDTO.getChangeOrginatorName(),
                crDTO.getTitle(), DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()),
                DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()),
                Long.parseLong(crDTO.getState()));
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  public String updateCrToMopInsite(CrInsiteDTO crDTO) {
    try {
      log.info("UPDATE CR TO MOP FO");
      CrFilesAttachDTO crFile = new CrFilesAttachDTO();
      crFile.setCrId(crDTO.getCrId());
      List<CrFilesAttachDTO> lstAttachment = crFilesAttachRepository
          .search(crFile, 0, 1000, "", "");
      String dtCode = "";
      String dtHis = "";
      List<String> lstMop = new ArrayList<>();
      if (lstAttachment != null) {
        for (CrFilesAttachDTO dto : lstAttachment) {
          if (dto.getDtCode() != null) {
            dtCode = dto.getDtCode();
            dtHis = dto.getDtFileHistory();
            if (!lstMop.contains(dtCode)) {
              lstMop.add(dtCode);
            }
          }
        }
      }

      if (StringUtils.isNotNullOrEmpty(dtCode) && crDTO.getState() != null) {
        if (dtHis != null && dtHis.contains("VIPA_IP")) {
          wsVipaIpPort
              .updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);
        } else if (dtHis != null && dtHis.contains("VIPA_DD")) {
          if ("1".equals(crDTO.getIsLoadMop())) {
            lstMop = new ArrayList<>();
            if (lstAttachment != null) {
              for (CrFilesAttachDTO dto : lstAttachment) {
                if (dto.getDtCode() != null) {
                  dtCode = dto.getDtCode();
                  if (!lstMop.contains(dtCode) && StringUtils.isStringNullOrEmpty(dto.getIsRun())) {
                    lstMop.add(dtCode);
                  }
                }
              }
            }
          } else if ("2".equals(crDTO.getIsLoadMop())) {
            crFile = new CrFilesAttachDTO();
            crFile.setCrId(crDTO.getRelateToPreApprovedCr());
            crFile.setIsRun(crDTO.getCrId());
            lstAttachment = crFilesAttachRepository.search(crFile, 0, 1000, "", "");
            lstMop = new ArrayList<>();
            if (lstAttachment != null && !lstAttachment.isEmpty()) {
              for (CrFilesAttachDTO dto : lstAttachment) {
                if (dto.getDtCode() != null) {
                  dtCode = dto.getDtCode();
                  if (!lstMop.contains(dtCode)) {
                    lstMop.add(dtCode);
                  }
                }
              }
            }
          }
          if (lstMop != null && !lstMop.isEmpty()) {
            wsVipaDdPort.updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);
          }
        } else {
          //link CR sang CNTT
          if (crDTO.getChangeOrginatorName() != null
              && crDTO.getChangeOrginatorName().indexOf("(") > 0) {
            LinkCrResult linkCrResult = wstdttPort.linkCr(crDTO.getCrNumber(), dtCode,
                crDTO.getChangeOrginatorName()
                    .substring(0, crDTO.getChangeOrginatorName().indexOf("(")).trim(),
                crDTO.getTitle(), DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()),
                DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()),
                Long.valueOf(crDTO.getState()));
            if (linkCrResult == null) {
              return RESULT.ERROR;
            }
            Integer status = linkCrResult.getStatus();
            if (status != null)
              if (status == 1) {
                return RESULT.SUCCESS;
              } else {
                if (!RESULT.SUCCESS.equals(linkCrResult.getMessage())) {
                  return linkCrResult.getMessage();
                }
                return RESULT.ERROR;
              }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  public CrInsiteDTO converTimeFromClientToServer(CrInsiteDTO crDTO, String userName) {
    try {
      Double offset = userRepository.getOffsetFromUser(userName);
      crDTO.setEarliestStartTime(
          DateTimeUtils.convertDateToOffset(crDTO.getEarliestStartTime(), offset, true));
      crDTO.setLatestStartTime(
          DateTimeUtils.convertDateToOffset(crDTO.getLatestStartTime(), offset, true));
      crDTO.setDisturbanceEndTime(
          DateTimeUtils.convertDateToOffset(crDTO.getDisturbanceEndTime(), offset, true));
      crDTO.setDisturbanceStartTime(
          DateTimeUtils.convertDateToOffset(crDTO.getDisturbanceStartTime(), offset, true));
      crDTO.setCreatedDate(DateTimeUtils.convertDateToOffset(crDTO.getCreatedDate(), offset, true));
      crDTO.setUpdateTime(DateTimeUtils.convertDateToOffset(crDTO.getUpdateTime(), offset, true));
      crDTO.setSentDate(DateTimeUtils.convertDateToOffset(crDTO.getSentDate(), offset, true));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return crDTO;
  }

}
