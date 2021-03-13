package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.model.GnocFileEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CR_STATE;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDetailsDTO;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDTO;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFileAttachOutput;
import com.viettel.gnoc.cr.dto.CrFileAttachOutputWithContent;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrForNocProMonitorOnlineDTO;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrOutputForOCSDTO;
import com.viettel.gnoc.cr.dto.CrOutputForQLTNDTO;
import com.viettel.gnoc.cr.dto.CrOutputForSOCDTO;
import com.viettel.gnoc.cr.dto.CrOutputForSOCForm;
import com.viettel.gnoc.cr.dto.CrWoDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.Node;
import com.viettel.gnoc.cr.dto.SelectionResultDTO;
import com.viettel.gnoc.cr.model.CrFilesAttachEntity;
import com.viettel.gnoc.cr.model.CrHisEntity;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.gnoc.wo.model.WorkLogEntity;
import com.viettel.security.PassTranformer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Repository
@Slf4j
public class CrForOtherSystemRepositoryImpl extends BaseRepository implements
    CrForOtherSystemRepository {

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

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CrDBRepository crDBRepository;

  @Autowired
  SmsDBRepository smsDBRepository;

  @Autowired
  CrFilesAttachRepository crFilesAttachRepository;

  @Autowired
  CrDtRepositoryImpl crDtRepository;

  @Autowired
  CrImpactedNodesRepository crImpactedNodesRepository;

  @Autowired
  CrAffectedNodeRepository crAffectedNodeRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  CrGeneralRepository crGeneralRepository;

  @Override
  public List<CrCreatedFromOtherSysDTO> getListDataByObjectId(Long objectId) {

    List<CrCreatedFromOtherSysDTO> lst = null;
    try {
      StringBuilder sql = new StringBuilder();
      sql.append(
          "select a.CCFOSM_ID ccfosmId , a.CR_ID  crId , a.OBJECT_ID  objectId , a.STEP_ID stepId , a.SYSTEM_ID systemId , a.IS_ACTIVE isActive ");
      sql.append(
          " from open_pm.cr_created_from_other_sys a where a.OBJECT_ID =:objectId and a.IS_ACTIVE = 1 ");
      Map<String, Object> params = new HashMap<>();
      params.put("objectId", objectId);
      lst = getNamedParameterJdbcTemplate().query(sql.toString(), params,
          BeanPropertyRowMapper.newInstance(CrCreatedFromOtherSysDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;

  }

  @Override
  public List<CrCreatedFromOtherSysDTO> getListData(Long crId, Long systemId, Long objectId) {

    List<CrCreatedFromOtherSysDTO> lst = null;
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> params = new HashMap<>();
      sql.append(
          "select a.CCFOSM_ID ccfosmId , a.CR_ID  crId , a.OBJECT_ID  objectId , a.STEP_ID stepId , "
              + " a.SYSTEM_ID systemId , a.IS_ACTIVE isActive ");
      sql.append(" from open_pm.cr_created_from_other_sys a where 1 = 1 and a.IS_ACTIVE = 1 ");
      if (!StringUtils.isLongNullOrEmpty(crId)) {
        sql.append(" AND a.CR_ID =:crId ");
        params.put("crId", crId);
      }
      if (!StringUtils.isLongNullOrEmpty(systemId)) {
        sql.append(" AND a.SYSTEM_ID =:systemId ");
        params.put("systemId", systemId);
      }
      if (!StringUtils.isLongNullOrEmpty(objectId)) {
        sql.append(" AND a.OBJECT_ID =:objectId ");
        params.put("objectId", objectId);
      }

      lst = getNamedParameterJdbcTemplate().query(sql.toString(), params,
          BeanPropertyRowMapper.newInstance(CrCreatedFromOtherSysDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;

  }

  @Override
  public CrCreatedFromOtherSysDTO getCrCreatedFromOtherSysDTO(Long crId) {

    CrCreatedFromOtherSysDTO obj = null;
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-cr-create-from-other-sys-dto");
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      List<CrCreatedFromOtherSysDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrCreatedFromOtherSysDTO.class));
      if (lst != null && !lst.isEmpty()) {
        obj = lst.get(0);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return obj;

  }

  @Override
  public boolean checkWoCloseAutoSetting(Long unitId, Long woTypeId) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "check-wo-close-auto-setting");
      Map<String, Object> params = new HashMap<>();
      params.put("unitId", unitId);
      params.put("woTypeId", woTypeId);
      List<Integer> list = getNamedParameterJdbcTemplate()
          .queryForList(sql, params, Integer.class);
//      List<Integer> list = getNamedParameterJdbcTemplate()
//          .query(sql, params, BeanPropertyRowMapper.newInstance(Integer.class));
      if (list != null && !list.isEmpty()) {
        int count = list.get(0);
        if (count > 0) {
          return true;
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }

  @Override
  public List<CrOutputForSOCDTO> getListDeviceAffectForSOC(String lastUpdateTime) {
    List<CrOutputForSOCDTO> result = new ArrayList<>();
    try {

      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-list-device-affect-forSoc");
      Map<String, Object> params = new HashMap<>();
      params.put("state", CR_STATE.ACCEPT);
      params.put("lastUpdateTime", lastUpdateTime);
      log.info("CrForOtherSystem: " + sql);
      List<CrOutputForSOCForm> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrOutputForSOCForm.class));
      log.info("CrForOtherSystem: Finish query");
      Map<String, CrOutputForSOCDTO> mapData = new HashMap<>();
      if (!lst.isEmpty()) {
        for (CrOutputForSOCForm form : lst) {
          if (!StringUtils.isStringNullOrEmpty(form.getNodeName())) {

            CrOutputForSOCDTO temp = mapData.get(form.getCrNumber());
            if (temp == null) {
              temp = new CrOutputForSOCDTO();
              temp.setCrNumber(form.getCrNumber());
              temp.setTitle(form.getTitle());
              temp.setCreateUserName(form.getCreateUserName());
              temp.setDescription(form.getDescription());
              temp.setEndAffectedTime(form.getEndAffectedTime());
              temp.setEndTime(form.getEndTime());
              temp.setExecuteUserName(form.getExecuteUserName());
              temp.setImpactService(form.getImpactService());
              temp.setLastUpdateTime(form.getLastUpdateTime());
              temp.setNotes(form.getNotes());
              temp.setStartAffectedTime(form.getStartAffectedTime());
              temp.setStartTime(form.getStartTime());
              temp.setUnitExecuteName(form.getUnitExecuteName());
              List<String> lstAffectedService = new ArrayList<>();
              if (form.getAffectedService() != null && !""
                  .equals(form.getAffectedService().trim())) {
                lstAffectedService.add(form.getAffectedService().trim());
              }
              temp.setLstAffectedService(lstAffectedService);

              Node n = new Node(form.getNodeName(), form.getNetworkClass());
              List<Node> lstNode = new ArrayList<>();
              lstNode.add(n);
              temp.setLstNodeName(lstNode);
            } else {
              List<String> lstAffectedService = temp.getLstAffectedService();
              if (form.getAffectedService() != null && !"".equals(form.getAffectedService().trim())
                  && !lstAffectedService.contains(form.getAffectedService())) {
                lstAffectedService.add(form.getAffectedService().trim());
              }
              temp.setLstAffectedService(lstAffectedService);
              Node n = new Node(form.getNodeName(), form.getNetworkClass());
              List<Node> lstNode = temp.getLstNodeName();
              if (!lstNode.contains(n)) {
                lstNode.add(n);
              }
              temp.setLstNodeName(lstNode);
            }
            mapData.put(form.getCrNumber(), temp);
          }
        }
      }
      for (CrOutputForSOCDTO dto : mapData.values()) {
        result.add(dto);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public List<CrFileAttachOutputWithContent> getCrFileDTAttachWithContent(String crNumber,
      String attachTime, String fileType) {
    List<CrFileAttachOutputWithContent> result = new ArrayList<>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-cr-fileDT-attach-with-content");
      Map<String, Object> params = new HashMap<>();
      params.put("fileType", fileType.trim());
      params.put("crNumber", crNumber.trim());
      params.put("attachTime", attachTime.trim());
      result = getNamedParameterJdbcTemplate()
          .query(sql, params,
              BeanPropertyRowMapper.newInstance(CrFileAttachOutputWithContent.class));
      if (result != null) {
        for (int i = result.size() - 1; i >= 0; i--) {
          try {
            CrFileAttachOutputWithContent dto = result.get(i);
            byte[] bytes = FileUtils
                .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), dto.getFilePath());
            String encodedString = Base64.encode(bytes);
            dto.setFileContent(encodedString);
          } catch (Exception e) {
            result.remove(i);
            log.error(e.getMessage(), e);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public UsersDTO getUserInfo(String userName) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-user-info");
    Map<String, Object> params = new HashMap<>();
    params.put("userName", userName);
    List<UsersDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(UsersDTO.class));
    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }
    return null;
  }

  public CrInsiteDTO getCrById(Long crId) {
    try {
      if (crId != null) {
        String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SMS,
            "get-cr-by-id");
        Map<String, Object> params = new HashMap<>();
        params.put("cr_id", crId);
        List<CrInsiteDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
            BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
        if (list != null && !list.isEmpty()) {
          return list.get(0);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public String actionResolveCr(CrInsiteDTO crDTO, String locale) {
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      Long userId = Long.valueOf(crDTO.getUserLogin());
      Long deptId = Long.valueOf(crDTO.getUserLoginUnit());
      if (userId == null || deptId == null || crDTO.getActionType() == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionType = crDTO.getActionType();
      String returnCode = crDTO.getActionReturnCodeId();
      String crIdStr = crDTO.getCrId();
      Long crId = Long.parseLong(crIdStr.trim());
      CrInsiteDTO crDTOtoSendSMS = smsDBRepository.getCrById(crId);
      if (actionType.trim().equals(Constants.CR_ACTION_CODE.RESOLVE.toString())) {
        if (returnCode == null
            || "".equals(returnCode.trim())
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (crIdStr == null
            || "".equals(crIdStr.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (!crDBRepository.validateCloseWoWhenResolveCR(crDTO)) {
          return Constants.CR_RETURN_MESSAGE.MUSTCLOSEALLWO;
        }
        crDBRepository
            .updateCrStatusInCaseOfResolve(Long.parseLong(actionType), crId, crDTO, locale);
      }
      smsDBRepository.actionSendSms(Long.parseLong(actionType), crDTO, crDTOtoSendSMS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "EXCEPTION";
    }
    //    LogDBHandler.getMapLogWS().put("dbTime", "" + (end.getTime() - start.getTime()));
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  public Long generateStateForResolve(Long actionType) {
    Long status = Constants.CR_STATE.ACCEPT;
    if (actionType.equals(Constants.CR_ACTION_CODE.RESOLVE)) {
      status = Constants.CR_STATE.RESOLVE;
    }
    return status;
  }

  @Override
  public String createWorklogResolveCR(String userName, String wlgText, Long crId, Long userId) {
    try {
      WorkLogInsiteDTO workLogInsiteDTO = new WorkLogInsiteDTO();
      ResultInSideDto resultInSideDTO = new ResultInSideDto();
      resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
      Long wlgObjectType = 2L;
      Long userGroupAction = 11L;
      Long wlgEffortHours = 0L;
      Long wlgEffortMinutes = 0L;
//      Long wlgAccessType = null;
      Date createDate = DateTimeUtils.convertStringToDate(DateTimeUtils.getSysdate());
      Long wlayId = 79L;
      Long wlgObjectState = 6L;
      workLogInsiteDTO.setWlgText(wlgText);
      workLogInsiteDTO.setWlgObjectId(crId);
      workLogInsiteDTO.setUserId(userId);
      workLogInsiteDTO.setWlgObjectType(wlgObjectType);
      workLogInsiteDTO.setUserGroupAction(userGroupAction);
      workLogInsiteDTO.setWlgEffortHours(wlgEffortHours);
      workLogInsiteDTO.setWlgEffortMinutes(wlgEffortMinutes);
//      workLogInsiteDTO.setWlgAccessType(wlgAccessType);
      workLogInsiteDTO.setCreatedDate(createDate);
      workLogInsiteDTO.setWlayId(wlayId);
      workLogInsiteDTO.setWlgObjectState(wlgObjectState);
      WorkLogEntity workLogEntity = getEntityManager().merge(workLogInsiteDTO.toEntity());
      resultInSideDTO.setId(workLogEntity.getWorkLogId());
//      StringBuilder sql = new StringBuilder(
//          "INSERT INTO OPEN_PM.WORK_LOG (WORK_LOG_ID, WLG_OBJECT_TYPE, WLG_OBJECT_ID, ");
//      sql.append("USER_ID, USER_GROUP_ACTION, WLG_TEXT, WLG_EFFORT_HOURS, WLG_EFFORT_MINUTES, ");
//      sql.append("WLG_ACCESS_TYPE, CREATED_DATE, WLAY_ID, WLG_OBJECT_STATE) ");
//      sql.append(" VALUES (open_pm.work_log__seq.nextval, 2, ");
//      sql.append(":wlgObjectId, :userId, 11, :wlgText, ");
//      sql.append("0, 0, null, ");
//      sql.append("sysdate, 79, 6)");
//      Map<String, Object> parameters = new HashMap<>();
//      parameters.put("wlgObjectId", crId);
//      parameters.put("userId", userId);
//      parameters.put("wlgText", wlgText);
//      getEntityManager().flush();
      return "SUCCESS";
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return "FAIL";
  }

  @Override
  public SelectionResultDTO getCrlistFromTimeInterval(double minute) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-cr-list-form-time-interval");
    Map<String, Object> params = new HashMap<>();
    params.put("state", CR_STATE.ACCEPT);
    params.put("param", minute);
    List<CrForNocProMonitorOnlineDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, params,
            BeanPropertyRowMapper.newInstance(CrForNocProMonitorOnlineDTO.class));
    Map<String, CrForNocProMonitorOnlineDTO> crMap;
    crMap = new HashMap<>();
    String stateName = I18n.getChangeManagement("cr.state.accept");
    List<CrForNocProMonitorOnlineDTO> lstCrUnique = new ArrayList<>();
    boolean newCRFound = false;
    for (CrForNocProMonitorOnlineDTO cr : lst) {
      //get node and service set
      Set<CrAffectedNodesDetailsDTO> setNode;
      Set<CrAffectedServiceDTO> setService;
      Map<String, CrAffectedNodesDetailsDTO> mapDeviceId2Node;
      if (!crMap.containsKey(cr.getCrId())) { //if new CR found
        newCRFound = true;
        cr.setStateName(stateName);
        cr.setState(String.valueOf(Constants.CR_STATE.ACCEPT));
        crMap.put(cr.getCrId(), cr);
        lstCrUnique.add(cr);
        setNode = new HashSet<>();
        cr.setSetAffectedNode(setNode);
        setService = new HashSet<>();
        cr.setSetAffectedService(setService);
        mapDeviceId2Node = new HashMap<>();
        cr.setMapDeviceIdToAffectedNode(mapDeviceId2Node);
      } else {
        setNode = crMap.get(cr.getCrId()).getSetAffectedNode();
        setService = crMap.get(cr.getCrId()).getSetAffectedService();
        mapDeviceId2Node = crMap.get(cr.getCrId()).getMapDeviceIdToAffectedNode();
      }
      //create new node and service from selected CR record
      Set<String> setIp = new HashSet<>();
      CrAffectedNodesDetailsDTO node = new CrAffectedNodesDetailsDTO(
          cr.getInsertTime(), cr.getDeviceId(),
          cr.getDeviceCode(), cr.getDeviceName(), setIp);
      CrAffectedServiceDTO service = new CrAffectedServiceDTO(
          cr.getAffectedServiceId(), cr.getAffectedServiceCode(),
          cr.getAffectedServiceName(), cr.getAffectedServiceDescription());
      //add node and service to set of node or service of CR
      if (node.getDeviceId() != null) {
        boolean newNodeAdded = setNode.add(node);
        if (newNodeAdded) {
          mapDeviceId2Node.put(cr.getDeviceId(), node);
        }
      }
      if (service.getAffectedServiceId() != null) {
        setService.add(service);
      }
      //add IP to set of IP of each node
      Map<String, CrAffectedNodesDetailsDTO> mapDeviceIdToNode = crMap.get(cr.getCrId())
          .getMapDeviceIdToAffectedNode();
      CrAffectedNodesDetailsDTO gotNode = mapDeviceIdToNode.get(cr.getDeviceId());
      if (gotNode != null) {
        gotNode.getSetDeviceIp().add(cr.getDeviceIp());
      }
      //all affected services and devices are already added to setNode and setService
      if (newCRFound) {
        cr.setAffectedServiceCode(null);
        cr.setAffectedServiceId(null);
        cr.setAffectedServiceName(null);
        cr.setDeviceId(null);
        cr.setDeviceCode(null);
        cr.setDeviceName(null);
        cr.setDeviceIp(null);
        newCRFound = false; //reset flag
      }
    }
    for (CrForNocProMonitorOnlineDTO cr : lstCrUnique) {
      cr.setMapDeviceIdToAffectedNode(null);
      cr.setCrnodes(cr.getSetAffectedNode() != null ? cr.getSetAffectedNode().size() : 0);
    }
    return new SelectionResultDTO(null, null, lstCrUnique);
  }

  @Override
  public List<CrFileAttachOutput> getCrFileDTAttach(String crNumber, String attachTime) {
    List<CrFileAttachOutput> result = new ArrayList<>();
    try {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-cr-fileDT-attach");
      Map<String, Object> params = new HashMap<>();
      params.put("crNumber", crNumber.trim());
      params.put("attachTime", attachTime.trim());
      result = getNamedParameterJdbcTemplate()
          .query(sql, params,
              BeanPropertyRowMapper.newInstance(CrFileAttachOutput.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public List<CrOutputForOCSDTO> getCrForOCS(String userName, String startTime) {
    List<CrOutputForOCSDTO> result = new ArrayList<>();
    try {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-cr-for-ocs");
      Map<String, Object> params = new HashMap<>();
      params.put("userName", userName.trim());
      params.put("startTime", startTime.trim());
      params.put("state", CR_STATE.ACCEPT);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrOutputForOCSDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public List<CrWoDTO> getListWOFromCR(String crId, String crProcess) {
    List<CrWoDTO> lst = new ArrayList<>();
    try {
      if (crId == null) {
        return lst;
      }
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-list-wo-from-cr");
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      params.put("crId", crId);
      params.put("crProcessId", crProcess);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrWoDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }


  @Override
  public CrOutputForQLTNDTO getCrForQLTN(String crNumber, String type) {
    CrOutputForQLTNDTO result = new CrOutputForQLTNDTO();
    try {
      String sql = "";
      if ("GET_IMPACT_TIME".equals(type)) {
        sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-cr-for-qltn2");
      } else {
        sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-cr-for-qltn");
      }
      Map<String, Object> params = new HashMap<>();
      params.put("crNumber", crNumber);
      List<CrOutputForQLTNDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrOutputForQLTNDTO.class));
      if (!lst.isEmpty()) {
        result.setCrNumber(lst.get(0).getCrNumber());
        result.setState(lst.get(0).getState());
        result.setCreateUser(lst.get(0).getCreateUser());
        result.setCreateUserCode(lst.get(0).getCreateUserCode());
        result.setUserConsider(lst.get(0).getUserConsider());
        result.setUserConsiderCode(lst.get(0).getUserConsiderCode());
        result.setImpactStartTime(lst.get(0).getImpactStartTime());
        result.setImpactEndTime(lst.get(0).getImpactEndTime());
        List<String> lstWoCode = new ArrayList<>();
        List<String> lstftName = new ArrayList<>();
        String woInfo = getWoInfor(
            crNumber.substring(crNumber.lastIndexOf("_") + 1, crNumber.length()));
        if (woInfo != null) {
          lstWoCode = Arrays.asList(woInfo.split("#####")[0].split(","));
          lstftName = Arrays.asList(woInfo.split("#####")[1].split(","));
        }
        result.setLstWoCode(lstWoCode);
        result.setLstFtName(lstftName);
        result.setUserExecuteCode(lst.get(0).getUserExecuteCode());
        result.setUserExecute(lst.get(0).getUserExecute());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    result.setResultCode("OK");
    return result;
  }

  private String getWoInfor(String crId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-wo-infor");
    Map<String, Object> params = new HashMap<>();
    params.put("crId", crId);

    List<WoDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(WoDTO.class));

    String woCode = "";
    String ftName = "";
    if (lst == null || lst.isEmpty()) {
      return null;
    } else {
      for (WoDTO woDTO : lst) {
        if (!"".equals(woCode)) {
          woCode = woCode + "," + woDTO.getWoCode();
        } else {
          woCode = woDTO.getWoCode();
        }

        if (!"".equals(ftName)) {
          ftName =
              ftName + "," + (StringUtils.isStringNullOrEmpty(woDTO.getStationId()) ? "N/A"
                  : woDTO.getStationId());
        } else {
          ftName = StringUtils.isStringNullOrEmpty(woDTO.getStationId()) ? "N/A"
              : woDTO.getStationId();
        }

      }
      return woCode + "#####" + ftName;
    }
  }

  @Override
  public String createMapFile(String username, String crId, String fileType, String fileName,
      String fileContent) {
    UsersDTO userDTO = getUserInfo(username);
    if (userDTO == null) {
      return I18n.getChangeManagement("qltn.userNotExist");
    }
    try {
      //luu file len server
      if (StringUtils.isStringNullOrEmpty(fileContent)) {
        return I18n.getChangeManagement("cr.msg.error");
      }
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      UnitDTO unitToken = unitRepository.findUnitById(Long.valueOf(userDTO.getUnitId()));
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, fileName, Base64.decode(fileContent),
              null);
      //luu file vao DB
      List<CrFilesAttachInsiteDTO> lstUpload = new ArrayList<>();
      CrFilesAttachInsiteDTO attachDTO = new CrFilesAttachInsiteDTO();
      attachDTO.setCrId(Long.valueOf(crId));
      attachDTO.setFileName(FileUtils.getFileName(fullPath));
      attachDTO.setFileType(fileType);
      attachDTO.setTimeAttack(DateUtil.sysDate());
      attachDTO.setFilePath(fullPath);
      attachDTO.setUserId(
          !DataUtil.isNullOrEmpty(userDTO.getUserId()) ? Long.valueOf(userDTO.getUserId()) : null);
      attachDTO.setDtFileHistory(I18n.getChangeManagement("othersystem.insertFile"));
      lstUpload.add(attachDTO);
      List<CrFilesAttachInsiteDTO> lstUpdateInsert = crFilesAttachRepository
          .getListCrFilesToUpdateOrInsert(lstUpload, false);
      ResultInSideDto resultFileDataOld = crFilesAttachRepository.add(lstUpdateInsert.get(0));
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(fileName);
      gnocFileDto.setFileType(fileType);
      gnocFileDto.setCreateUnitId(Long.valueOf(userDTO.getUnitId()));
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(Long.valueOf(userDTO.getUserId()));
      gnocFileDto.setCreateUserName(userDTO.getUsername());
      gnocFileDto.setCreateTime(new Date());
      gnocFileDto.setMappingId(resultFileDataOld.getId());
      gnocFileDtos.add(gnocFileDto);
      gnocFileRepository.saveListGnocFileNotDeleteAll(
          GNOC_FILE_BUSSINESS.CR, lstUpdateInsert.get(0).getCrId(), gnocFileDtos);
      return "";
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return "";
  }

  @Override
  public ResultDTO insertFile(String userName, String crNumber, String fileType, String fileName,
      String fileContent) {
    ResultDTO result = new ResultDTO();
    try {

      UsersDTO userDTO = getUserInfo(userName);
      if (userDTO == null) {
        result.setKey("NOK");
        result.setMessage(I18n.getChangeManagement("qltn.userNotExist"));
        return result;
      }
      CrInsiteDTO crInfo = getCrInfo(crNumber);

      if (crInfo == null) {
        result.setKey("NOK");
        result.setMessage(I18n.getChangeManagement("qltn.crNumberNotExist"));
        return result;
      }
      //luu file len server
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      UnitDTO unitToken = unitRepository.findUnitById(Long.valueOf(userDTO.getUnitId()));
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, fileName, Base64.decode(fileContent),
              null);
      //luu file vao DB
      List<CrFilesAttachInsiteDTO> lstUpload = new ArrayList<>();
      CrFilesAttachInsiteDTO attachDTO = new CrFilesAttachInsiteDTO();
      attachDTO.setCrId(Long.valueOf(crInfo.getCrId()));
      attachDTO.setFileName(FileUtils.getFileName(fullPath));
      attachDTO.setFileType(fileType);
      attachDTO.setTimeAttack(DateUtil.sysDate());
      attachDTO.setFilePath(fullPath);
      attachDTO.setUserId(
          !DataUtil.isNullOrEmpty(userDTO.getUserId()) ? Long.valueOf(userDTO.getUserId()) : null);
      attachDTO.setDtFileHistory(I18n.getChangeManagement("qltn.insertFile"));
      lstUpload.add(attachDTO);
      List<CrFilesAttachInsiteDTO> lstUpdateInsert = crFilesAttachRepository
          .getListCrFilesToUpdateOrInsert(lstUpload, false);
      //Kiem tra neu la file log thi xoa file cu
      if (Constants.CR_FILE_TYPE.LOG_IMPACT.equals(fileType)) {
        deleteFileByType(crInfo.getCrId(), fileType);
      }
      ResultInSideDto resultFileDataOld = crFilesAttachRepository.add(lstUpdateInsert.get(0));
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(fileName);
      gnocFileDto.setFileType(fileType);
      gnocFileDto.setCreateUnitId(Long.valueOf(userDTO.getUnitId()));
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(Long.valueOf(userDTO.getUserId()));
      gnocFileDto.setCreateUserName(userDTO.getUsername());
      gnocFileDto.setCreateTime(new Date());
      gnocFileDto.setMappingId(resultFileDataOld.getId());
      gnocFileDtos.add(gnocFileDto);
      gnocFileRepository.saveListGnocFileNotDeleteAll(
          GNOC_FILE_BUSSINESS.CR, lstUpdateInsert.get(0).getCrId(), gnocFileDtos);
      result.setKey("OK");
      return result;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public CrInsiteDTO getCrInfo(String crNumber) {
    try {
      if (!StringUtils.isStringNullOrEmpty(crNumber)) {
        String sql = "select a.cr_id crId, a.cr_number crNumber from open_pm.cr a where a.cr_number= :crNumber ";
        Map<String, Object> params = new HashMap<>();
        params.put("crNumber", crNumber);
        List<CrInsiteDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
            BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
        if (list != null && !list.isEmpty()) {
          return list.get(0);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public void deleteFileByType(String crId, String fileType) throws Exception {
    if (StringUtils.isStringNullOrEmpty(crId)) {
      return;
    }
    if (StringUtils.isStringNullOrEmpty(fileType)) {
      return;
    }
    List<CrFilesAttachEntity> crFilesAttachEntity = findByMultilParam(CrFilesAttachEntity.class,
        "crId", crId, "fileType", fileType);
    CrFilesAttachInsiteDTO crFilesAttachDTO = null;
    if (!crFilesAttachEntity.isEmpty()) {
      crFilesAttachDTO = crFilesAttachEntity.get(0).toDTO();
    }
    if (crFilesAttachDTO != null) {
      deleteByMultilParam(GnocFileEntity.class,
          "businessCode", GNOC_FILE_BUSSINESS.CR,
          "businessId", crId,
          "mappingId", crFilesAttachDTO.getFileId());
      getEntityManager().remove(crFilesAttachDTO);
    }
    return;
  }

  @Override
  public ResultDTO updateDtInfo(String userName, String crNumber, String dtCode,
      List<String> lstIpImpact, List<String> lstIpAffect, String mopFile, String mopFileContent,
      String mopRollbackFile, String mopRollbackFileContent, List<String> lstAffectService,
      String nationCode) {
    System.out.println("B1");

    ResultDTO result = new ResultDTO();
    HashSet<String> checkSet = new HashSet<>();
    List<List<String>> ips = new ArrayList<>();
    List<String> iplst = new ArrayList<>();
    HashMap<String, List<InfraDeviceDTO>> deviceMap = new HashMap<>();
    try {
      //Kiem tra user co ton tai tren gnoc khong va lay thong tin userId
      Long crId = null;
      String crState = "";
      String crCreateTime = null;
      String earlierStartTime = null;

      String fileId = "";
      UsersDTO userDto = getUserInfo(userName);
      if (userDto == null || "".equals(userDto)) {
        result.setKey("NOK");
        result.setMessage(I18n.getChangeManagement("qltn.userNotExist"));
        return result;
      }
      if (dtCode == null || dtCode.trim().isEmpty()) {
        result.setKey("NOK");
        result.setMessage("DT code is not null");
        return result;
      }
      dtCode = dtCode.trim();
      if (nationCode == null || nationCode.trim().isEmpty()) {
        nationCode = "VNM";
      }
      nationCode = nationCode.trim().toUpperCase();
      //Lay thong tin cr va file dt, kiem tra dau vao
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-list-cr-by-crNumber");
      Map<String, Object> params = new HashMap<>();
      params.put("crNumber", crNumber);
      List<CrInsiteDTO> lstCr = getNamedParameterJdbcTemplate().query(sql, params,
          BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));

      if (lstCr == null || lstCr.isEmpty()) {
        result.setKey("NOK");
        result.setMessage(I18n.getChangeManagement("qltn.crNotExistOrNotAttached"));
        return result;
      } else {
        CrInsiteDTO us = lstCr.get(0);
        crId = Long.parseLong(us.getCrId());
        crState = us.getState();
        crCreateTime = DateUtil.date2ddMMyyyyHHMMss(us.getCreatedDate());
//        DateTimeUtils.convertStringToDateTime(us.getCreatedDate().toString());
//        earlierStartTime = this.parseToDate(us[5].toString(), "dd/MM/yyyy HH:mm:ss");
        earlierStartTime = DateUtil.date2ddMMyyyyHHMMss(us.getEarliestStartTime());
//        DateTimeUtils.convertStringToDateTime(us.getEarliestStartTime().toString());
      }
      if (!Constants.CR_STATE.DRAFT.toString().equals(crState)
          && !Constants.CR_STATE.OPEN.toString().equals(crState)
          && !Constants.CR_STATE.QUEUE.toString().equals(crState)
          && !Constants.CR_STATE.COORDINATE.toString().equals(crState)
          && !Constants.CR_STATE.INCOMPLETE.toString().equals(crState)
          && !Constants.CR_STATE.APPROVE.toString().equals(crState)) {
        result.setKey("NOK");
        result.setMessage(I18n.getChangeManagement("qltn.crStateInvalid"));
        return result;
      }
      System.out.println("B2");
      //Cap nhat lai dt
      for (CrInsiteDTO cr : lstCr) {
        fileId = cr.getFileId();
        String fileType = cr.getFileType();
        if (!Constants.CR_FILE_TYPE.DT_EXECUTE.equals(fileType)
            && !Constants.CR_FILE_TYPE.DT_SCRIPT.equals(fileType)
            && !Constants.CR_FILE_TYPE.DT_ROLLBACK.equals(fileType)) {
          continue;
        }
        //luu file len server
        StringBuilder sqlDt = new StringBuilder();
        sqlDt.append(" UPDATE "
            + "  OPEN_PM.CR_FILES_ATTACH "
            + " SET "
            + "  user_id        = :userId, "
            + "  dt_code        = :dtCode, "
            + "  dt_file_history=dt_file_history "
            + "  || :dtFileHistory, "
            + "  file_name=:fileName, "
            + "  file_path= :filePath "
            + " WHERE "
            + "  file_id  =:fileId "
            + " AND dt_code= :dtCode ");
        Map<String, Object> paramDt = new HashMap<>();
        paramDt.put("userId", userDto.getUserId());
        paramDt.put("dtCode", dtCode);
        paramDt.put("dtFileHistory",
            I18n.getChangeManagement("qltn.editDtFile") + ", userName: " + userName + ", dtCode: "
                + dtCode);
        List<GnocFileEntity> gnocFileEntities = findByMultilParam(GnocFileEntity.class,
            "businessCode", GNOC_FILE_BUSSINESS.CR,
            "businessId", crId,
            "mappingId", Long.valueOf(fileId));
        if (Constants.CR_FILE_TYPE.DT_EXECUTE.equals(fileType) || Constants.CR_FILE_TYPE.DT_SCRIPT
            .equals(fileType)) {
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, mopFile,
                  Base64.decode(mopFileContent),
                  null);
          paramDt.put("fileName", FileUtils.getFileName(fullPath));
          paramDt.put("filePath", fullPath);
          if (!gnocFileEntities.isEmpty()) {
            GnocFileEntity gnocFileEntity = gnocFileEntities.get(0);
            gnocFileEntity.setPath(fullPath);
            gnocFileEntity.setFileName(mopFile);
            getEntityManager().merge(gnocFileEntity);
          }
        } else if (Constants.CR_FILE_TYPE.DT_ROLLBACK.equals(fileType)) {
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, mopRollbackFile,
                  Base64.decode(mopRollbackFileContent), null);
          paramDt.put("fileName", FileUtils.getFileName(fullPath));
          paramDt.put("filePath", fullPath);
          if (!gnocFileEntities.isEmpty()) {
            GnocFileEntity gnocFileEntity = gnocFileEntities.get(0);
            gnocFileEntity.setPath(fullPath);
            gnocFileEntity.setFileName(mopRollbackFile);
            getEntityManager().merge(gnocFileEntity);
          }
        }
        paramDt.put("fileId", fileId);
        paramDt.put("dtCode", dtCode);
        getNamedParameterJdbcTemplate().update(sqlDt.toString(), paramDt);
        getEntityManager().flush();
      }
      System.out.println("B3");

      //Lay danh sach node mang
      if (lstIpImpact != null) {
        for (String ip : lstIpImpact) {
          if (ip == null || ip.trim().isEmpty() || checkSet.contains(ip.trim())) {
            continue;
          }
          checkSet.add(ip);
          if (!iplst.isEmpty() && iplst.size() % 100 == 0) {
            ips.add(iplst);
            iplst.clear();
          }
          iplst.add(ip.trim());
        }
      }
      if (lstIpAffect != null) {
        for (String ip : lstIpAffect) {
          if (ip == null || ip.trim().isEmpty() || checkSet.contains(ip.trim())) {
            continue;
          }
          checkSet.add(ip);
          if (!iplst.isEmpty() && iplst.size() % 200 == 0) {
            ips.add(iplst);
            iplst.clear();
          }
          iplst.add(ip.trim());
        }
      }
      if (!iplst.isEmpty()) {
        ips.add(iplst);
      }

      for (List<String> findingIps : ips) {
        List<InfraDeviceDTO> infraDeviceLst = crDtRepository
            .geInfraDeviceByIps(findingIps, nationCode);
        for (InfraDeviceDTO device : infraDeviceLst) {
          if (device.getIp() == null || device.getIp().trim().isEmpty()) {
            continue;
          }

          if (deviceMap.containsKey(device.getIp().trim())) {
            deviceMap.get(device.getIp().trim()).add(device);
          } else {
            List<InfraDeviceDTO> list = new ArrayList<>();
            list.add(device);
            deviceMap.put(device.getIp().trim(), list);
          }
        }

      }
      System.out.println("B4");
      //cap nhat node mang tac dong
      this.deleteImpactNodeCodeByCrIdAndDtCode(crId, dtCode, crCreateTime, earlierStartTime);
      List<CrImpactedNodesDTO> lstImpactInsert = new ArrayList<>();
      for (String ip : lstIpImpact) {
        if (deviceMap.containsKey(ip.trim())) {
          List<InfraDeviceDTO> list = deviceMap.get(ip.trim());
          for (InfraDeviceDTO deviceDTO : list) {
            CrImpactedNodesDTO temp = new CrImpactedNodesDTO();
            temp.setCrId(crId.toString());
            temp.setDeviceId(deviceDTO.getDeviceId());
            temp.setIpId(deviceDTO.getIpId());
//            temp.setInsertTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            temp.setInsertTime(DateTimeUtils.getSysDateTime());
            temp.setDtCode(dtCode);

            temp.setIp(deviceDTO.getIp());
            temp.setDeviceCode(deviceDTO.getDeviceCode());
            temp.setDeviceName(deviceDTO.getDeviceName());
            temp.setNationCode(deviceDTO.getNationCode());

            lstImpactInsert.add(temp);
          }
        }
      }
      System.out.println("B5");
      crImpactedNodesRepository.saveListDTONoIdSession(lstImpactInsert, null);
      //cap nhat node mang anh huong
      this.deleteAffectNodeCodeByCrIdAndDtCode(crId, dtCode, crCreateTime, earlierStartTime);
      List<CrAffectedNodesDTO> lstAffectedInsert = new ArrayList<>();
      if (lstIpAffect != null && !lstIpAffect.isEmpty()) {
        for (String ip : lstIpAffect) {
          if (deviceMap.containsKey(ip.trim())) {
            List<InfraDeviceDTO> list = deviceMap.get(ip.trim());
            for (InfraDeviceDTO deviceDTO : list) {
              CrAffectedNodesDTO temp = new CrAffectedNodesDTO();
              temp.setCrId(crId.toString());
              temp.setDeviceId(deviceDTO.getDeviceId());
              temp.setIpId(deviceDTO.getIpId());
              temp.setInsertTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
              temp.setDtCode(dtCode);

              temp.setIp(deviceDTO.getIp());
              temp.setDeviceCode(deviceDTO.getDeviceCode());
              temp.setDeviceName(deviceDTO.getDeviceName());
              temp.setNationCode(deviceDTO.getNationCode());

              lstAffectedInsert.add(temp);
            }
          }
        }
        crAffectedNodeRepository.saveListDTONoIdSession(lstAffectedInsert, null);
      }

      System.out.println("B6");
      //Cap nhat lai dich vu anh huong
      if (lstAffectService != null && !lstAffectService.isEmpty()) {
        deleteSubAffectedService(crId.toString(), Constants.CR_AFFECTED_SERVICE.UDCNTT_CODE);
        for (String serviceCode : lstAffectService) {
          insertSubAffectedService(crId.toString(), serviceCode);
        }
      }
      result.setKey("OK");
      System.out.println("Finish");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
      result.setKey("NOK");
      result.setMessage(e.getMessage());
      return result;
    } finally {
      checkSet.clear();
      ips.clear();
      deviceMap.clear();
    }
    return result;
  }

  public void deleteImpactNodeCodeByCrIdAndDtCode(
      Long crId, String dtCode, String crCreateDate, String earlierStartDate) {
    StringBuilder sql = new StringBuilder();
    if (crId == null) {
      return;
    }
    try {
      Date createDate = null;
      if (crCreateDate != null) {
        createDate = DateUtil.string2DateTime(crCreateDate);
      }

      sql.append(" delete from cr_impacted_nodes ");
      sql.append(" where cr_id = :crId ");
      if (crCreateDate == null) {
        sql.append(" and insert_time >= trunc(sysdate) - 15");
      } else {
        sql.append(" and insert_time >=  to_date(:crCreateDate, 'dd/MM/yyyy HH24:mi:ss')");
      }

      if (dtCode != null && !dtCode.trim().isEmpty()) {
        sql.append(" and dt_code = :dtCode ");
      }

      if (earlierStartDate != null) {
        sql.append(" and insert_time <= to_date(:earlierStartDate,'dd/MM/yyyy HH24:mi:ss' )");
      }
      Map<String, Object> param = new HashMap<>();
      param.put("crId", crId);
      if (crCreateDate != null) {
        param.put("crCreateDate", crCreateDate);
      }
      if (dtCode != null && !dtCode.trim().isEmpty()) {
        param.put("dtCode", dtCode);
      }
      if (earlierStartDate != null) {
        if (earlierStartDate.compareTo(crCreateDate) <= 0) {

          // do ECR ccos the xay ra  earlierStartDate < crCreateDate
          earlierStartDate = this.addString(createDate, 30);
        }
        param.put("earlierStartDate", earlierStartDate);
      }

      getNamedParameterJdbcTemplate().update(sql.toString(), param);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private Date addDate(Date date, int additionalVal) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, additionalVal);
    return cal.getTime();
  }

  private String addString(Date date, int additionalVal) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, additionalVal);
    return cal.getTime().toString();
  }

  public void deleteAffectNodeCodeByCrIdAndDtCode(Long crId, String dtCode, String crCreateDate,
      String earlierStartDate) {
    StringBuilder sql = new StringBuilder();
    try {
      if (crId == null) {
        return;
      }
      Date createDate = null;

      if (crCreateDate != null) {
        createDate = DateUtil.string2DateTime(crCreateDate);
      }

      sql.append(" delete from CR_AFFECTED_NODES ");
      sql.append(" where cr_id = :crId");
      if (crCreateDate == null) {
        sql.append(" and insert_time >= trunc(sysdate) - 15");
      } else {
        sql.append(" and insert_time >= to_date(:crCreateDate, 'dd/MM/yyyy HH24:mi:ss') ");
      }
      if (dtCode != null && !dtCode.trim().isEmpty()) {
        sql.append(" and dt_code = :dtCode ");
      }
      if (earlierStartDate != null) {
        sql.append(" and insert_time <= to_date(:earlierStartDate, 'dd/MM/yyyy HH24:mi:ss')");
      }

      Map<String, Object> paramAffect = new HashMap<>();
      paramAffect.put("crId", crId);
      if (crCreateDate != null) {
        paramAffect.put("crCreateDate", crCreateDate);
      }
      if (dtCode != null && !dtCode.trim().isEmpty()) {
        paramAffect.put("dtCode", dtCode);
      }
      if (earlierStartDate != null) {
        if (earlierStartDate.compareTo(crCreateDate) <= 0) {
          // do ECR ccos the xay ra  earlierStartDate < crCreateDate
          earlierStartDate = this.addString(createDate, 30);
        }
        paramAffect.put("earlierStartDate", earlierStartDate);
      }
      getNamedParameterJdbcTemplate().update(sql.toString(), paramAffect);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public void deleteSubAffectedService(String crId, String parentCode) throws Exception {
    StringBuilder sql = new StringBuilder();
    sql.append(" DELETE cr_affected_service_details "
        + " WHERE cr_id = :crId AND affected_service_id IN( "
        + "    SELECT affected_service_id "
        + "    FROM affected_services "
        + "    WHERE parent_id=(SELECT affected_service_id FROM affected_services WHERE service_code=:serviceCode))");
    Map<String, Object> paramdel = new HashMap<>();
    paramdel.put("crId", crId);
    paramdel.put("serviceCode", parentCode);
    getNamedParameterJdbcTemplate().update(sql.toString(), paramdel);

  }

  public void insertSubAffectedService(String crId, String serviceCode) throws Exception {
    StringBuilder sql = new StringBuilder();
    sql.append(
        "insert into cr_affected_service_details a (a.casds_id, a.cr_id, a.affected_service_id, a.insert_time) "
            + " values (CASDS_SEQ.nextval, :crId,(select affected_service_id from affected_services"
            + " where service_code=:serviceCode),sysdate )");
    Map<String, Object> paramIn = new HashMap<>();
    paramIn.put("crId", crId);
    paramIn.put("serviceCode", serviceCode);
    getNamedParameterJdbcTemplate().update(sql.toString(), paramIn);
    getEntityManager().flush();
  }

  public String setCrInfor(CrDTO crDTO) {
    String rs = "";
    Long crId = getSequenId("cr_seq");
    crDTO.setCrId(crId.toString());
    crDTO.setCrNumber("CR_EMERGENCY_CNTT_" + crId);
    crDTO.setState(Constants.CR_STATE.CLOSE.toString());
    if (crDTO.getTitle() == null || "".equals(crDTO.getTitle())) {
      rs = "cr.trace.crNameRequire";
      return rs;
    }
    if (crDTO.getDescription() == null || "".equals(crDTO.getDescription())) {
      rs = "cr.trace.crDescriptionRequire";
      return rs;
    }

    crDTO.setCrType(Constants.CR_TYPE.EMERGENCY.toString());
    crDTO.setRisk("2");//muc rui ro 2
    crDTO.setImpactSegment("94");//mang tac dong cntt
    crDTO.setDeviceType("103");//tat ca cac UDCNTT
    crDTO.setSubcategory("11");//xu ly su co
    crDTO.setPriority(Constants.CR_PRIORITY.IMEDIATELY.toString());
    crDTO.setProcessTypeId("0");//quy trinh dac biet
    String createDate = DateUtil.date2ddMMyyyyHHMMss(new Date());
    crDTO.setCreatedDate(createDate);
    crDTO.setUpdateTime(createDate);

    if (crDTO.getEarliestStartTime() == null || "".equals(crDTO.getEarliestStartTime())) {
      rs = "cr.trace.earliestStartTimeRequire";
      return rs;
    }
    if (crDTO.getLatestStartTime() == null || "".equals(crDTO.getLatestStartTime())) {
      rs = "cr.trace.latestStartTimeRequire";
      return rs;
    }
    Date startTime = DateUtil
        .string2DateByPattern(crDTO.getEarliestStartTime(), "dd/MM/yyyy HH:mm:ss");
    if (startTime == null) {
      rs = "cr.trace.earliestStartTimeInvalid";
      return rs;
    }
    Date endTime = DateUtil
        .string2DateByPattern(crDTO.getLatestStartTime(), "dd/MM/yyyy HH:mm:ss");
    if (endTime == null) {
      rs = "cr.trace.latestStartTimeInvalid";
      return rs;
    }
    //validate cr_impact_frame: 1-Nigth(23h00-4h59) 2-Day(0h00-23h59)
    String dutyType = getDutyType(startTime, endTime);
    if (!"1".equals(dutyType) && !"2".equals(dutyType)) {
      rs = "cr.trace.dutyTypeInvalid&DUTYTYPE&" + dutyType;
      return rs;
    }
    crDTO.setDutyType(dutyType);
    //don vi, user
    if (crDTO.getChangeOrginator() == null || "".equals(crDTO.getChangeOrginator())) {
      rs = "cr.trace.changeOrginatorRequire";
      return rs;
    }
    UsersDTO userCreate = getUserInfo(crDTO.getChangeOrginator());//user tao
    if (StringUtils.isStringNullOrEmpty(userCreate)) {
      rs = "cr.trace.changeOrginatorInvalid";
      return rs;
    }
    crDTO.setChangeOrginator(userCreate.getUserId());
    crDTO.setChangeOrginatorUnit(userCreate.getUnitId());

    if (crDTO.getChangeResponsible() == null || "".equals(crDTO.getChangeResponsible())) {
      rs = "cr.trace.changeResponsibleRequire";
      return rs;
    }
    UsersDTO userExecute = getUserInfo(
        crDTO.getChangeResponsible());//don vi thuc hien
    if (StringUtils.isStringNullOrEmpty(userExecute)) {
      rs = "cr.trace.changeResponsibleInvalid";
      return rs;
    }
    crDTO.setChangeResponsible(userExecute.getUserId());
    crDTO.setChangeResponsibleUnit(userExecute.getUnitId());

    crDTO.setImpactAffect("142");//muc do anh huong trung binh
//281   	VN 	Việt Nam
//289721	VTC	Cambodia
//289722	STL	Laos
//289725	VCR	Cameroon
//289728	VTP	Peru
//289723	VTL	Timor Leste
//289726	VTB	Burundi
//289727	VTZ	Tanzania
//289724	MVT	Mozambique
//289729	NAT	Haiti
//282	KV1	Khu vực 1
//283	KV2	Khu vực 2
//284	KV3	Khu vực 3
    Map<String, String> mapLocation = new HashMap<String, String>();
    mapLocation.put("VN", "281");
    mapLocation.put("VTC", "289721");//Cambodia
    mapLocation.put("STL", "289722");//Laos
    mapLocation.put("VCR", "289725");//Cameroon
    mapLocation.put("VTP", "289728");//Peru
    mapLocation.put("VTL", "289723");//Timor Leste
    mapLocation.put("VTB", "289726");//Burundi
    mapLocation.put("VTZ", "289727");//Tanzania
    mapLocation.put("VMVT", "289724");//Mozambique
    mapLocation.put("NAT", "289729");//Haiti
    mapLocation.put("KV1", "282");
    mapLocation.put("KV2", "283");
    mapLocation.put("KV3", "284");
    if (crDTO.getCountry() == null) {
      rs = "cr.trace.countryRequire";
      return rs;
    }
    String country = mapLocation.get(crDTO.getCountry());
    if (country == null) {
      rs = "cr.trace.countryInvalid";
      return rs;
    }
    crDTO.setCountry(country);
    if ("281".equals(country)) {
      if (crDTO.getRegion() == null) {
        rs = "cr.trace.regionRequire";
        return rs;
      }
      String region = mapLocation.get(crDTO.getRegion());
      if (region == null) {
        rs = "cr.trace.regionInvalid";
        return rs;
      }
      crDTO.setRegion(region);
    }
    crDTO.setRelateCr(Constants.CR_RELATED.NON_RELATED);//cr lien quan

    //dich vu anh huong
    crDTO.setServiceAffecting("1");
    List<CrAffectedServiceDetailsDTO> lstAff = new ArrayList<>();
    CrAffectedServiceDetailsDTO affSer = new CrAffectedServiceDetailsDTO();
    affSer.setAffectedServiceId("30");//UDCNTT
    affSer.setCrId(crDTO.getCrId());
    affSer.setInsertTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    lstAff.add(affSer);
    crDTO.setLstAffectedService(lstAff);
    crDTO.setDisturbanceStartTime(crDTO.getEarliestStartTime());
    crDTO.setDisturbanceEndTime(crDTO.getLatestStartTime());

    //Node mang tac dong
    if (crDTO.getLstNetworkNodeId() != null) {
      getIpImpact(crDTO.getLstNetworkNodeId(), crDTO);

    }

    return rs;
  }

  public Long getSequenId(String sequense) {
    String sql = "select " + sequense + ".nextval from dual";
    Query query = getEntityManager().createNativeQuery(sql);
    List<BigDecimal> lst = query.getResultList();
    if (lst != null && !lst.isEmpty()) {
      return lst.get(0).longValue();
    } else {
      return null;
    }
  }

  private String getDutyType(Date startTime, Date endTime) {
    if (endTime.getTime() < startTime.getTime()) {
      return "0";
    }
    //cr_impact_frame: 1-Nigth(23h00-4h59) 2-Day(0h00-23h59)
    //20201207 dungpv edit khung thoi gian tac dong
    if (startTime.getDate() != endTime.getDate()) {//khac ngay la tac dong dem
      String validateTimeCr = validateTimeCr("1", startTime, endTime);
      if (!StringUtils.isStringNullOrEmpty(validateTimeCr)) {
        return validateTimeCr;
      }
      return "1";
    } else {//tac dong ngay
      String validateTimeCr = validateTimeCr("2", startTime, endTime);
      if (!StringUtils.isStringNullOrEmpty(validateTimeCr)) {
        return validateTimeCr;
      }
      return "2";
    }
    //end
  }

  //20201207 dungpv add vaildate khung thoi gian tac dong
  private String validateTimeCr(String dutyType, Date startDate, Date endDate) {
    CrImpactFrameInsiteDTO form = new CrImpactFrameInsiteDTO();
    form.setImpactFrameId(Long.valueOf(dutyType));
    List<ItemDataCRInside> lstFrame = crGeneralRepository.getListDutyTypeCBB(form, "");
    try {
      ItemDataCRInside dataCR = lstFrame.get(0);
      String[] startendarray = dataCR.getSecondValue().split(",");
      if (startendarray.length > 1) {
        String[] startDuty = startendarray[0].split(":");
        String[] endDuty = startendarray[1].split(":");
        if (startDuty.length > 2 && endDuty.length > 2) {
          Calendar startDutyCal = Calendar.getInstance();
          startDutyCal.clear();
          startDutyCal.setTime(startDate);
          if (startDate != null) {
            startDutyCal.set(startDate.getYear() + 1900, startDate.getMonth(),
                startDate.getDate(),
                Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                Integer.valueOf(startDuty[2]));
          }
          Date startDutyDate = startDutyCal.getTime();
          Calendar endDutyCal = Calendar.getInstance();
          endDutyCal.clear();
          if (startDate != null) {
            endDutyCal.set(startDate.getYear() + 1900, startDate.getMonth(),
                startDate.getDate(),
                Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                Integer.valueOf(endDuty[2]));
          }
          Date endDutyDate;
          if (Integer.valueOf(startDuty[0]) > Integer.valueOf(endDuty[0])) {//tac dong dem
            Calendar startDutyCalCheck = Calendar.getInstance();
            startDutyCalCheck.clear();
            if (startDate != null) {
              startDutyCalCheck
                  .set(startDate.getYear() + 1900, startDate.getMonth(),
                      startDate.getDate(),
                      0, 0, 0);
            }
            Date checkstartDutyDate = startDutyCalCheck.getTime();
            Calendar endDutyCalCheck = startDutyCalCheck;
            endDutyCalCheck.clear();
            if (endDate != null) {
              endDutyCalCheck.set(endDate.getYear() + 1900, endDate.getMonth(),
                  endDate.getDate(),
                  0, 0, 0);
            }
            Date checkendDutyDate = endDutyCalCheck
                .getTime();//1445014800000 | 1445014800000
            if (startDate != null) {
              if (endDate != null && checkstartDutyDate
                  .equals(checkendDutyDate)) {//Cung ngay
                if (endDate.getHours() <= (Integer.valueOf(endDuty[0])
                    + 1)) {//sang hom sau (00h-5h)
                  startDutyCal
                      .set(endDate.getYear() + 1900, endDate.getMonth(),
                          endDate.getDate() - 1,
                          Integer.valueOf(startDuty[0]),
                          Integer.valueOf(startDuty[1]),
                          Integer.valueOf(startDuty[2]));
                  startDutyDate = startDutyCal.getTime();
                  endDutyCal.set(endDate.getYear() + 1900, endDate.getMonth(),
                      endDate.getDate(),
                      Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                      Integer.valueOf(endDuty[2]));
//                            endDutyDate = endDutyCal.getTime();
                } else {//dem hom truoc (23h-24h)
                  startDutyCal
                      .set(startDate.getYear() + 1900, startDate.getMonth(),
                          startDate.getDate(),
                          Integer.valueOf(startDuty[0]),
                          Integer.valueOf(startDuty[1]),
                          Integer.valueOf(startDuty[2]));
                  startDutyDate = startDutyCal.getTime();
                  endDutyCal
                      .set(startDate.getYear() + 1900, startDate.getMonth(),
                          startDate.getDate() + 1,
                          Integer.valueOf(endDuty[0]),
                          Integer.valueOf(endDuty[1]),
                          Integer.valueOf(endDuty[2]));
//                            endDutyDate = endDutyCal.getTime();
                }
              } else {
                endDutyCal
                    .set(startDate.getYear() + 1900, startDate.getMonth(),
                        startDate.getDate() + 1,
                        Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                        Integer.valueOf(endDuty[2]));
//                        endDutyDate = endDutyCal.getTime();
              }
            }
          }
//                    endDutyCal.add(Calendar.MINUTE, 1);
          endDutyDate = endDutyCal.getTime();
          if (startDate != null && endDate != null) {
            if (startDate.compareTo(startDutyDate) < 0
                || endDate.compareTo(endDutyDate) > 0) {
//                        return CMResourceBundleUtils.getChangeManagementString("cr.msg.timecr.not.in.duty.date", locale);
              return dataCR.getDisplayStr();
            }
          }
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return ex.getMessage();
    }
    return "";
  }
  //end

  private void getIpImpact(List<CrImpactedNodesDTO> lstImpact, CrDTO crDTO) {
    //Lay danh sach node mang
    Map<String, String> mapIp = new HashMap<>();
    StringBuilder sqlIp = new StringBuilder();
    sqlIp.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-ip-impact"));
    if (lstImpact != null && !lstImpact.isEmpty()) {
      for (int i = 0; i < lstImpact.size(); i++) {
        sqlIp.append("?,");
      }
    }
    if (",".equals(String.valueOf(sqlIp.charAt(sqlIp.length() - 1)))) {
      sqlIp.replace(sqlIp.length() - 1, sqlIp.length(), "");
      sqlIp.append(")");
    }

    Query queryIp = getEntityManager().createNativeQuery(sqlIp.toString());
    int j = 1;
    for (int i = 0; i < lstImpact.size(); i++) {
      queryIp.setParameter(j, lstImpact.get(i).getIp());
      j++;
    }

    List<Object[]> lstIp = queryIp.getResultList();
    for (Object[] ipArr : lstIp) {
      mapIp.put(ipArr[2] + "", ipArr[0] + "@" + ipArr[1]);
    }
    for (CrImpactedNodesDTO dto : lstImpact) {
      String val = mapIp.get(dto.getIp());
      if (val == null) {
        continue;
      }
      String deviceId = val.split("@")[0];
      String ipId = val.split("@")[1];
      dto.setCrId(crDTO.getCrId());
      dto.setDeviceId(deviceId);
      dto.setIpId(ipId);
      dto.setInsertTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

    }
  }

  @Override
  public ResultInSideDto saveObjectSession(CrHisDTO crHisDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CrHisEntity crHisEntity = getEntityManager()
        .merge(crHisDTO.toEntity());
    resultInSideDTO.setId(crHisEntity.getChsId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto saveObject(CrDTO crDTO) {
    try {
      ResultInSideDto resultInSideDto = insertByModel(crDTO.toModelInsiteDTO().toEntity(),
          "crId");
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
