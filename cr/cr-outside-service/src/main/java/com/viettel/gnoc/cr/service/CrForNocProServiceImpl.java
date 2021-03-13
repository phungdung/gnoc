package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.business.CrForNocProBusiness;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrForNocProObj;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrForNocProServiceImpl implements CrForNocProService {

  @Autowired
  private CrForNocProBusiness crForNocProBusiness;
  @Resource
  private WebServiceContext wsContext;

  @Override
  public CrForNocProObj getCrObjectForNocPro(String userService, String passService,
      String deviceCode, String deviceName, String ip, String activeTimeString, String countryCode,
      String impactSegmentCode) {
    log.debug("Request to getCrObjectForNocPro : {}", userService, passService, deviceCode,
        deviceName, ip, activeTimeString, countryCode, impactSegmentCode);
    setLocale();
    return crForNocProBusiness
        .getCrObjectForNocPro(userService, passService, deviceCode, deviceName, ip,
            activeTimeString, countryCode, impactSegmentCode);
  }

  @Override
  public CrForNocProObj getCrObjectV2(String userService, String passService,
      Integer delayMinutes) {
    log.debug("Request to getCrObjectV2 : {}", userService, passService, delayMinutes);
    setLocale();
    return crForNocProBusiness.getCrObjectV2(userService, passService, delayMinutes, null);
  }

  @Override
  public CrForNocProObj getCrObjectForNocV3(String userService, String passService,
      Integer delayMinutes, String nationCode) {
    log.debug("Request to getCrObjectForNocV3 : {}", userService, passService, delayMinutes,
        nationCode);
    setLocale();
    return crForNocProBusiness.getCrObjectV2(userService, passService, delayMinutes, nationCode);
  }

  @Override
  public CrForNocProObj insertResolveWoLog(String userService, String passService, String userName,
      String crNumber, String wologContent, Long userGroupActionId, Long wlayId) {
    log.debug("Request to insertResolveWoLog : {}", userService, passService, userName, crNumber,
        wologContent, userGroupActionId, wlayId);
    setLocale();
    return crForNocProBusiness
        .insertResolveWoLog(userService, passService, userName, crNumber, wologContent,
            userGroupActionId, wlayId);
  }

  @Override
  public CrForNocProObj sendSMS(String userService, String passService, String crNumber,
      String smsContent) {
    log.debug("Request to sendSMS : {}", userService, passService, crNumber, smsContent);
    setLocale();
    return crForNocProBusiness.sendSMS(userService, passService, crNumber, smsContent);
  }

  @Override
  public CrForNocProObj insertAlarmDetail(String userService, String passService, String crNumber,
      String faultId, String faultName, String faultSrc, String vendorCode, String vendorName,
      String moduleCode, String moduleName, String nationcode) {
    log.debug("Request to insertAlarmDetail : {}", userService, passService, crNumber, faultId,
        faultName, faultSrc, vendorCode, vendorName, moduleCode, moduleName, nationcode);
    setLocale();
    return crForNocProBusiness
        .insertAlarmDetail(userService, passService, crNumber, faultId, faultName, faultSrc,
            vendorCode, vendorName, moduleCode, moduleName, nationcode);
  }

  @Override
  public WorkLogDTO getWorkLog(String crNumber) {
    log.debug("Request to getWorkLog : {}", crNumber);
    setLocale();
    if (crNumber == null || !crNumber.contains("_")) {
      return new WorkLogDTO();
    }
    String crId = crNumber.substring(crNumber.lastIndexOf("_") + 1, crNumber.length());
    return crForNocProBusiness.getWorkLog(crId);
  }

  @Override
  public ResultDTO getCRbyImpactIP(List<String> listImpactedNode, String impactedTimeFrom,
      String impactedTimeTo) {
    log.debug("Request to getCRbyImpactIP : {}", listImpactedNode, impactedTimeFrom, impactedTimeTo);
    ResultDTO resultDTO = new ResultDTO(null, RESULT.SUCCESS, RESULT.SUCCESS);
    setLocale();
    CrInsiteDTO crInsiteDTO = new CrInsiteDTO();
    if (listImpactedNode == null || listImpactedNode.isEmpty()) {
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(I18n.getLanguage("cr.err.listImpactedNode.isNull"));
      return resultDTO;
    }
    if (StringUtils.isStringNullOrEmpty(impactedTimeFrom)) {
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(I18n.getLanguage("cr.err.impactedTimeFrom.isNull"));
      return resultDTO;
    } else {
      try {
        crInsiteDTO.setImpactedTimeFrom(DateUtil.string2Date(impactedTimeFrom));
      } catch (Exception e) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(I18n.getLanguage("cr.err.impactedTimeFrom.validateForm"));
        return resultDTO;
      }
    }
    if (StringUtils.isStringNullOrEmpty(impactedTimeTo)) {
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(I18n.getLanguage("cr.err.impactedTimeTo.isNull"));
      return resultDTO;
    } else {
      try {
        crInsiteDTO.setImpactedTimeTo(DateUtil.string2Date(impactedTimeTo));
      } catch (Exception e) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(I18n.getLanguage("cr.err.impactedTimeTo.validateForm"));
        return resultDTO;
      }
    }
    crInsiteDTO.setListImpactedNode(listImpactedNode);
    List<CrDTO> lstResult = crForNocProBusiness.getCRbyImpactIP(crInsiteDTO);
    resultDTO.setLstResult(lstResult);
    return resultDTO;
  }

  private void setLocale() {
    I18n.setLocaleForService(wsContext);
  }

}
