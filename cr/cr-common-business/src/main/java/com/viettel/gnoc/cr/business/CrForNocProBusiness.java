package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrForNocProObj;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import java.util.List;

public interface CrForNocProBusiness {

  CrForNocProObj getCrObjectForNocPro(String userService, String passService, String deviceCode,
      String deviceName, String ip, String activeTime, String countryCode,
      String impactSegmentCode);

  CrForNocProObj getCrObjectV2(String userService, String passService, Integer delayMinutes,
      String nationCode);

  CrForNocProObj insertResolveWoLog(String userService, String passService, String userName,
      String crNumber, String worklogContent, Long userGroupActionId, Long wlayId);

  CrForNocProObj sendSMS(String userService, String passService, String crNumber,
      String smsContent);

  CrForNocProObj insertAlarmDetail(String userService, String passService, String crNumber,
      String faultId, String faultName, String faultSrc, String vendorCode, String vendorName,
      String moduleCode, String moduleName, String nationcode);

  WorkLogDTO getWorkLog(String crId);

  List<CrDTO> getCRbyImpactIP(CrInsiteDTO crInsiteDTO);
}
