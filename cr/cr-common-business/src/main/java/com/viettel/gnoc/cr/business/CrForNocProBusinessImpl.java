package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.CrAffectedServiceInfo;
import com.viettel.gnoc.cr.dto.CrAlarmDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrDetailInfoDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrForNocProDTO;
import com.viettel.gnoc.cr.dto.CrForNocProObj;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.MessageForm;
import com.viettel.gnoc.cr.dto.MiniCrDTO;
import com.viettel.gnoc.cr.dto.MiniImpactedNode;
import com.viettel.gnoc.cr.repository.CrAlarmRepository;
import com.viettel.gnoc.cr.repository.CrForNocProRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.gnoc.cr.repository.SmsDBRepository;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class CrForNocProBusinessImpl implements CrForNocProBusiness {

  @Value("${application.ws.user_service:null}")
  private String userConf;
  @Value("${application.ws.pass_service:null}")
  private String passConf;
  @Value("${application.ws.salt_service:null}")
  private String saltConf;

  @Autowired
  private CrForNocProRepository crForNocProRepository;

  @Autowired
  private CrRepository crRepository;

  @Autowired
  private SmsDBRepository smsDBRepository;

  @Autowired
  private CrBusiness crBusiness;

  @Autowired
  private CrAlarmRepository crAlarmRepository;

  private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  private boolean validateString(String txt) {
    if (txt == null || txt.trim().isEmpty()) {
      return false;
    }
    return true;
  }

  private Long objectToLong(Object txt) {
    if (txt == null || txt.toString().trim().isEmpty()) {
      return null;
    }
    try {
      return Long.parseLong(txt.toString());
    } catch (NumberFormatException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  @Override
  public CrForNocProObj getCrObjectForNocPro(String userService, String passService,
      String deviceCode, String deviceName, String ip, String activeTimeStr, String countryCode,
      String impactSegmentCode) {
    log.debug("Request to getCrObjectForNocPro : {}", userService, passService, deviceCode,
        deviceName, ip, activeTimeStr, countryCode, impactSegmentCode);
    CrForNocProObj crObject = new CrForNocProObj();
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription(
            I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        return crObject;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (!validateString(activeTimeStr)) {
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription("ActiveTime s'not allow null");
      return crObject;
    }

    if (!validateString(deviceCode) && !validateString(deviceName)
        && !validateString(ip) && !validateString(impactSegmentCode)) {
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription("Please Insert at least one Data to find CR");
      return crObject;
    }

    try {
      Date activeTime = this.dateFormat.parse(activeTimeStr);
      if (activeTime == null) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription("Can not Parse activeTime to Date ");
        return crObject;
      }
      List<Long> listNodeIp = crForNocProRepository
          .getNodeIpId(deviceCode, deviceName, ip, countryCode);

      List<List<Long>> listIpIds = new ArrayList<>();
      if (listNodeIp != null && listNodeIp.size() > 500) {

        int count = listNodeIp.size() / 500;
        if (listNodeIp.size() % 500 != 0) {
          count++;
        }

        List<Long> ips;
        for (int p = 1; p <= count; p++) {

          if (p < count) {
            ips = listNodeIp.subList((p - 1) * 500, p * 500);
          } else {
            ips = listNodeIp.subList((p - 1) * 500, listNodeIp.size());
          }

          if (ips != null && !ips.isEmpty()) {
            listIpIds.add(ips);
          }

        }
      } else {

        if (listNodeIp != null && !listNodeIp.isEmpty()) {
          listIpIds.add(listNodeIp);
        }
      }

      List<MiniCrDTO> crDtoList = crForNocProRepository.getCrByStateAndActiveTime(6L, activeTime);
      Date minDate = null;
      Date maxDate = null;
      List<List<Long>> arrayCrId = new ArrayList<>();

      List<Long> ids = new ArrayList<>();
      if (crDtoList != null) {
        for (MiniCrDTO dto : crDtoList) {

          if (minDate == null) {
            minDate = dto.getCreatedDate();
          }
          if (maxDate == null) {
            maxDate = dto.getEarliestStartTime();
          }

          if (dto.getCreatedDate() != null && minDate.compareTo(dto.getCreatedDate()) > 0) {
            minDate = dto.getCreatedDate();
          }
          if (dto.getEarliestStartTime() != null
              && maxDate.compareTo(dto.getEarliestStartTime()) < 0) {
            maxDate = dto.getEarliestStartTime();
          }
          ids.add(dto.getCrId());

          if (ids.size() >= 500) {
            arrayCrId.add(ids);
            ids = new ArrayList<>();
          }
        }
      }
      if (minDate == null) {
        minDate = activeTime;
      }
      if (maxDate == null) {
        maxDate = activeTime;
      }
      arrayCrId.add(ids);
      List<CrForNocProDTO> searchCrList = new ArrayList<>();
      if (!arrayCrId.isEmpty() && !listIpIds.isEmpty()) {
        for (List<Long> crIds : arrayCrId) {
          List<CrForNocProDTO> objs = crForNocProRepository
              .getCrFromImpactNode(crIds, listIpIds, maxDate, minDate);
          if (objs != null && !objs.isEmpty()) {
            searchCrList.addAll(objs);
          }
        }

        if (!searchCrList.isEmpty()) {
          crObject.setDataList(searchCrList);
          return crObject;
        }
      }
      if (!arrayCrId.isEmpty() && !listIpIds.isEmpty()) {
        for (List<Long> crIds : arrayCrId) {
          List<CrForNocProDTO> objs = crForNocProRepository
              .getCrFromAffectNode(crIds, listIpIds, maxDate, minDate);
          if (objs != null && !objs.isEmpty()) {
            searchCrList.addAll(objs);
          }
        }
        if (!searchCrList.isEmpty()) {
          crObject.setDataList(searchCrList);
          return crObject;
        }
      }
      searchCrList = crForNocProRepository.getCrByImpactSegment(6L, impactSegmentCode, activeTime);
      if (!searchCrList.isEmpty()) {
        crObject.setDataList(searchCrList);
        return crObject;
      }
    } catch (ParseException e) {
      log.error(e.getMessage(), e);
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription(e.getMessage());
    }

    return crObject;
  }

  @Override
  public CrForNocProObj getCrObjectV2(String userService, String passService, Integer delayMinutes,
      String nationCode) {
    log.debug("Request to getCrObjectV2 : {}", userService, passService, delayMinutes, nationCode);
    CrForNocProObj crObject = new CrForNocProObj();
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription(
            I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        return crObject;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    if (delayMinutes < 0 || delayMinutes > 24 * 60) {
      crObject.setStatus(1);
      crObject.setDescription("Delay Minutes is not valid");
      crObject.setKey(Constants.RESULT.FAIL);
      return crObject;
    }
    HashMap<Long, CrDetailInfoDTO> detailMap = new HashMap<>();
    List<List<Long>> listIpIds = new ArrayList<>();
    List<Long> ips = new ArrayList<>();
    List<MiniImpactedNode> impactedNodeList = new ArrayList<>();
    List<MiniImpactedNode> affectedNodeList = new ArrayList<>();
    List<CrAlarmDTO> alarmlist = new ArrayList<>();
    List<CrFilesAttachDTO> fileAttachList = new ArrayList<>();
    List<CrAffectedServiceInfo> affectedServiceList = new ArrayList<>();
    HashMap<Long, HashSet<Long>> crAffectedSeriviceIdSet = new HashMap<>();

    try {
      if (delayMinutes < 0) {
        delayMinutes = delayMinutes * (-1);
      }

      List<Long> stateList = new ArrayList<>();
      stateList.add(Constants.CR_STATE.ACCEPT);

      Date now = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(now);
      cal.set(Calendar.SECOND, 0);

      Date latestTime = cal.getTime();

      cal.setTime(now);
      cal.add(Calendar.MINUTE, delayMinutes);
      cal.set(Calendar.SECOND, 0);
      Date earliestTime = cal.getTime();

      List<CrDetailInfoDTO> detaillist = crForNocProRepository
          .getListCrDetailInfoDTO(stateList, earliestTime, latestTime, nationCode);
      if (detaillist == null || detaillist.isEmpty()) {
        crObject.setStatus(0);
        crObject.setKey(Constants.RESULT.SUCCESS);
        return crObject;
      }

      Date startTime = new Date();
      Date endTime = new Date();

      for (CrDetailInfoDTO dto : detaillist) {
        if (dto.getCrId() == null) {
          continue;
        }
        detailMap.put(dto.getCrId(), dto);
        ips.add(dto.getCrId());
        if (ips.size() % 500 == 0) {
          listIpIds.add(ips);
          ips.clear();
        }

        if (dto.getCreatedDate() != null && startTime.compareTo(dto.getCreatedDate()) > 0) {
          startTime = dto.getCreatedDate();
        }

        if (dto.getLatestStartTime() != null && endTime.compareTo(dto.getLatestStartTime()) < 0) {
          endTime = dto.getLatestStartTime();
        }
      }

      if (!ips.isEmpty()) {
        listIpIds.add(ips);
      }

      impactedNodeList = crForNocProRepository
          .getImpactedNodeByCrIdsV2(listIpIds, startTime, endTime);
      if (impactedNodeList != null) {
        for (MiniImpactedNode obj : impactedNodeList) {
          if (obj.getCrId() == null) {
            continue;
          }
          if (detailMap.containsKey(obj.getCrId())) {
            detailMap.get(obj.getCrId()).getImpactNodes().add(obj);
          }
        }
      }

      affectedNodeList = crForNocProRepository
          .getAffectedNodeByCrIdsV2(listIpIds, startTime, endTime);
      if (affectedNodeList != null) {
        for (MiniImpactedNode obj : affectedNodeList) {
          if (obj.getCrId() == null) {
            continue;
          }
          if (detailMap.containsKey(obj.getCrId())) {
            detailMap.get(obj.getCrId()).getAffectNodes().add(obj);
          }
        }
      }
      fileAttachList = crForNocProRepository.getFileAttachCrIdsV2(listIpIds, startTime, endTime);

      if (fileAttachList != null) {
        for (CrFilesAttachDTO obj : fileAttachList) {
          if (obj.getCrId() == null) {
            continue;
          }
          if (detailMap.containsKey(Long.valueOf(obj.getCrId()))) {
            detailMap.get(Long.valueOf(obj.getCrId())).getFilesAttachList().add(obj);
          }
        }
      }
      alarmlist = crForNocProRepository
          .getListAlarm(listIpIds, startTime, endTime);/////////////////
      if (alarmlist != null) {
        for (CrAlarmDTO obj : alarmlist) {

          Long crId = this.objectToLong(obj.getCrId());
          if (crId == null) {
            continue;
          }
          if (detailMap.containsKey(crId)) {
            detailMap.get(crId).getAlarmList().add(obj);
          }
        }
      }
      affectedServiceList = crForNocProRepository
          .getListAffectService(listIpIds, startTime, endTime);
      if (affectedServiceList != null) {

        for (CrAffectedServiceInfo obj : affectedServiceList) {

          Long crId = this.objectToLong(obj.getCrId());
          if (crId == null) {
            continue;
          }
          if (!crAffectedSeriviceIdSet.containsKey(crId) || !crAffectedSeriviceIdSet.get(crId)
              .contains(obj.getAffectedServiceId())) {
            HashSet<Long> set = (crAffectedSeriviceIdSet.containsKey(crId) ? crAffectedSeriviceIdSet
                .get(crId) : new HashSet<>());
            set.add(obj.getAffectedServiceId());

            if (!crAffectedSeriviceIdSet.containsKey(crId)) {
              crAffectedSeriviceIdSet.put(crId, set);
            }
          } else {
            continue;
          }

          if (detailMap.containsKey(crId)) {
            detailMap.get(crId).getAffectedServiceList().add(obj);
          }
        }
      }

      crObject.setStatus(0);
      crObject.setKey(Constants.RESULT.SUCCESS);
      crObject.getCrDetailList().addAll(detailMap.values());

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription(e.getMessage());
    } finally {
      crAffectedSeriviceIdSet.clear();
      detailMap.clear();
      ips.clear();
      listIpIds.clear();
      impactedNodeList.clear();
      affectedNodeList.clear();
      alarmlist.clear();
      affectedServiceList.clear();
    }
    return crObject;
  }

  @Override
  public CrForNocProObj insertResolveWoLog(String userService, String passService, String userName,
      String crNumber, String worklogContent, Long userGroupActionId, Long wlayId) {
    log.debug("Request to insertResolveWoLog : {}", userService, passService, userName, crNumber,
        worklogContent, userGroupActionId, wlayId);
    CrForNocProObj crObject = new CrForNocProObj();
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    if (worklogContent == null || worklogContent.trim().isEmpty()
        || worklogContent.length() > 1000) {
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription("The length of worklogContent is not valid");
      return crObject;
    }
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        return crObject;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    List<Long> listUserId = new ArrayList<>();
    try {
      CrDTO crDto = crRepository.getCrByNumber(crNumber, null, null);
      if (crDto == null) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription("Not found CR by CrNumber : " + crNumber);
        return crObject;
      }
      UsersDTO createdUserDTO = this.crForNocProRepository.getUserByUserName(userName);
      if (createdUserDTO == null) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription("Not found User : " + userName);
        return crObject;
      }
      Long changeResponsible = objectToLong(crDto.getChangeResponsible());
      WorkLogInsiteDTO workLogDTO = new WorkLogInsiteDTO();
      workLogDTO.setWlgObjectType(2L);
      workLogDTO.setWlgObjectId(
          DataUtil.isNullOrEmpty(crDto.getCrId()) ? null : Long.valueOf(crDto.getCrId()));
      workLogDTO.setUserId(DataUtil.isNullOrEmpty(createdUserDTO.getUserId()) ? null
          : Long.valueOf(createdUserDTO.getUserId()));
      workLogDTO
          .setUserGroupAction(userGroupActionId != null ? userGroupActionId : 11L);
      workLogDTO.setWlgText(worklogContent);
      workLogDTO.setWlgEffortHours(0L);
      workLogDTO.setWlgEffortMinutes(0L);
      workLogDTO.setCreatedDate(new Date());
      workLogDTO.setWlayId(wlayId != null ? Long.valueOf(wlayId) : 79L);
      workLogDTO.setWlgObjectState(6L);

      ResultInSideDto res = crBusiness.insertWorkLogProxy(workLogDTO);
      if (res == null || res.getMessage() == null || !"SUCCESS"
          .equalsIgnoreCase(res.getMessage().trim())) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription("Can bot create WorkLog");
        return crObject;
      }
      if (changeResponsible == null) {
        crObject.setStatus(0);
        crObject.setDescription(Constants.RESULT.SUCCESS);
        return crObject;
      }
      listUserId.add(changeResponsible);
      List<MessageForm> lst = smsDBRepository.getListUsersByUserId(listUserId, false);
      for (MessageForm msg : lst) {
        msg.setMessageContent(worklogContent);
        smsDBRepository.actionInsertIntoMess(msg);
      }

      crObject.setStatus(0);
      crObject.setDescription(Constants.RESULT.SUCCESS);
    } catch (Exception e) {
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription(e.getMessage());
      log.error(e.getMessage(), e);
    }
    return crObject;
  }

  @Override
  public CrForNocProObj sendSMS(String userService, String passService, String crNumber,
      String smsContent) {
    log.debug("Request to sendSMS : {}", userService, passService, crNumber, smsContent);
    CrForNocProObj crObject = new CrForNocProObj();
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        return crObject;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    List<Long> listUserId = new ArrayList<>();
    try {
      CrDTO crDto = crRepository.getCrByNumber(crNumber, null, null);
      if (crDto == null) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription("Not found CR by CrNumber : " + crNumber);
        return crObject;
      }
      if (crDto.getChangeResponsible() != null) {
        listUserId.add(objectToLong(crDto.getChangeResponsible()));
      }
      List<MessageForm> lst = smsDBRepository.getListUsersByUserId(listUserId, false);
      for (MessageForm msg : lst) {
        msg.setMessageContent(smsContent);
        smsDBRepository.actionInsertIntoMess(msg);
      }
    } catch (Exception e) {
      crObject.setStatus(1);
      crObject.setDescription(Constants.RESULT.FAIL);
      crObject.setDescription(e.getMessage());
      log.error(e.getMessage(), e);
    } finally {
      listUserId.clear();
    }
    return crObject;
  }

  @Override
  public CrForNocProObj insertAlarmDetail(String userService, String passService, String crNumber,
      String faultIdStr, String faultName, String faultSrc, String vendorCode, String vendorName,
      String moduleCode, String moduleName, String nationCode) {
    log.debug("Request to insertAlarmDetail : {}", userService, passService, crNumber, faultIdStr,
        faultName,
        faultSrc, vendorCode, vendorName, moduleCode, moduleName, nationCode);
    CrForNocProObj crObject = new CrForNocProObj();
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        return crObject;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
      return crObject;
    }
    if (!validateString(crNumber)) {
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription("Not found CR by CrNumber : " + crNumber);
      return crObject;
    }
    if (!validateString(faultIdStr) || !validateString(faultName) || !validateString(faultSrc)
        || this.objectToLong(faultIdStr) == null) {
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription("The information about Fault is not valid ");
      return crObject;
    }
    if (!validateString(vendorCode) && !validateString(moduleCode)) {
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription("The information about vendorCode or moduleCode is not valid ");
      return crObject;
    }

    if (validateString(moduleCode) && !validateString(nationCode)) {
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription("The information about nationcode is not valid ");
      return crObject;
    }
    Long faultId = this.objectToLong(faultIdStr);
    List<CrAlarmSettingDTO> insertList = new ArrayList<>();
    try {
      CrDTO crDto = crRepository.getCrByNumber(crNumber, null, null);
      if (crDto == null || this.objectToLong(crDto.getProcessTypeId()) == null) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription("Not found CR by CrNumber : " + crNumber);
        return crObject;
      }
      Long processTypeId = this.objectToLong(crDto.getProcessTypeId());
      if (validateString(vendorCode)) {
        List<CrAlarmSettingDTO> exitedSetting = crAlarmRepository
            .getAlarmSetting(vendorCode, null, null, processTypeId, null, null, null, null, null,
                faultId);
        if (exitedSetting == null || exitedSetting.isEmpty()) {

          CrAlarmSettingDTO dto = new CrAlarmSettingDTO();
          dto.setApprovalStatus(0L);
          dto.setCrProcessId(processTypeId);
          dto.setFaultId(faultId);
          dto.setFaultName(faultName);
          dto.setFaultSrc(faultSrc);
          dto.setVendorCode(vendorCode);
          dto.setVendorName(vendorName);
          dto.setModuleCode(null);
          dto.setModuleName(null);
          dto.setNationCode(null);
          dto.setType(1L);

          insertList.add(dto);

        }

      }
      if (validateString(moduleCode)) {
        List<CrAlarmSettingDTO> exitedSetting = crAlarmRepository
            .getAlarmSetting(null, moduleCode, nationCode, processTypeId, null, null, null, null,
                null, faultId);
        if (exitedSetting == null || exitedSetting.isEmpty()) {

          CrAlarmSettingDTO dto = new CrAlarmSettingDTO();
          dto.setApprovalStatus(0L);
          dto.setCrProcessId(processTypeId);
          dto.setFaultId(faultId);
          dto.setFaultName(faultName);
          dto.setFaultSrc(faultSrc);
          dto.setVendorCode(null);
          dto.setVendorName(null);
          dto.setModuleCode(moduleCode);
          dto.setModuleName(moduleName);
          dto.setNationCode(nationCode);
          dto.setType(2L);
          insertList.add(dto);
        }

      }
      boolean doInsert = crAlarmRepository.insertListAlarmSetting(insertList);
      if (!doInsert) {
        crObject.setStatus(1);
        crObject.setKey(Constants.RESULT.FAIL);
        crObject.setDescription("Inert Fail");
        return crObject;
      }
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.SUCCESS);
      return crObject;
    } catch (Exception e) {
      crObject.setStatus(1);
      crObject.setKey(Constants.RESULT.FAIL);
      crObject.setDescription(e.getMessage());
      log.error(e.getMessage(), e);
    } finally {
      insertList.clear();
    }

    return crObject;
  }

  @Override
  public WorkLogDTO getWorkLog(String crId) {
    log.debug("Request to getWorkLog : {}", crId);
    return crForNocProRepository.getWorkLog(crId);
  }

  @Override
  public List<CrDTO> getCRbyImpactIP(CrInsiteDTO crInsiteDTO) {
    log.debug("Request to getCRbyImpactIP : {}", crInsiteDTO);
    return crRepository.getCRbyImpactIP(crInsiteDTO);
  }
}
