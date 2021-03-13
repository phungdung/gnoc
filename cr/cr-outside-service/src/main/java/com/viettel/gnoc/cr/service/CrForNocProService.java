package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrForNocProObj;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(serviceName = "CrForNocProService")
@XmlSeeAlso({CrDTO.class})
public interface CrForNocProService {

  @WebMethod(operationName = "getCrObjectForNocPro")
  public CrForNocProObj getCrObjectForNocPro(
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "deviceCode") String deviceCode,
      @WebParam(name = "deviceName") String deviceName,
      @WebParam(name = "ip") String ip,
      @WebParam(name = "activeTime") String activeTimeString,
      @WebParam(name = "countryCode") String countryCode,
      @WebParam(name = "impactSegmentCode") String impactSegmentCode
  );

  @WebMethod(operationName = "getCrObjectV2")
  public CrForNocProObj getCrObjectV2(
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "delayMinutes") Integer delayMinutes
  );

  @WebMethod(operationName = "getCrObjectForNocV3")
  public CrForNocProObj getCrObjectForNocV3(
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "delayMinutes") Integer delayMinutes,
      @WebParam(name = "nationCode") String nationCode
  );

  @WebMethod(operationName = "insertResolveWoLog")
  public CrForNocProObj insertResolveWoLog(
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "userName") String userName,
      @WebParam(name = "crNumber") String crNumber,
      @WebParam(name = "wologContent") String wologContent,
      @WebParam(name = "userGroupActionId") Long userGroupActionId,
      @WebParam(name = "wlayId") Long wlayId
  );

  @WebMethod(operationName = "sendSMS")
  public CrForNocProObj sendSMS(
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "crNumber") String crNumber,
      @WebParam(name = "smsContent") String smsContent
  );

  @WebMethod(operationName = "insertAlarmDetail")
  public CrForNocProObj insertAlarmDetail(
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "crNumber") String crNumber,
      @WebParam(name = "faultId") String faultId,
      @WebParam(name = "faultName") String faultName,
      @WebParam(name = "faultSrc") String faultSrc,
      @WebParam(name = "vendorCode") String vendorCode,
      @WebParam(name = "vendorName") String vendorName,
      @WebParam(name = "moduleCode") String moduleCode,
      @WebParam(name = "moduleName") String moduleName,
      @WebParam(name = "nationcode") String nationcode
  );

  @WebMethod(operationName = "getWorkLog")
  public WorkLogDTO getWorkLog(@WebParam(name = "crNumber") String crNumber);

  @WebMethod(operationName = "getCRbyImpactIP")
  public ResultDTO getCRbyImpactIP(
      @WebParam(name = "listImpactedNode") List<String> listImpactedNode,
      @WebParam(name = "impactedTimeFrom") String impactedTimeFrom,
      @WebParam(name = "impactedTimeTo") String impactedTimeTo);

}
