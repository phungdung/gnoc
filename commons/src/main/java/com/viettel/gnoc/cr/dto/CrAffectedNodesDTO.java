/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.model.CrAffectedNodesEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class CrAffectedNodesDTO {

  private String cansId;
  private String crId;
  private String ipId;
  private String deviceId;
  private String insertTime;
  private String ipIdStr;
  private String ip;
  private String deviceName;
  private String deviceCode;
  private String nationCode;
  private String dtCode;

  public CrAffectedNodesDTO(String cansId, String crId, String ipId, String deviceId,
      String insertTime, String dtCode) {
    this.cansId = cansId;
    this.crId = crId;
    this.ipId = ipId;
    this.deviceId = deviceId;
    this.insertTime = insertTime;
    this.dtCode = dtCode;
  }

  public CrAffectedNodesEntity toEntity() {
    CrAffectedNodesEntity model = new CrAffectedNodesEntity(
        !StringUtils.validString(cansId) ? null : Long.valueOf(cansId),
        !StringUtils.validString(crId) ? null : Long.valueOf(crId),
        !StringUtils.validString(ipId) ? null : Long.valueOf(ipId),
        !StringUtils.validString(deviceId) ? null : Long.valueOf(deviceId),
        StringUtils.validString(insertTime)
            ? DateTimeUtils.convertStringToDate(insertTime) : null,
        dtCode
    );
    model.setIp(ip);
    model.setDeviceCode(deviceCode);
    model.setDeviceName(deviceName);
    model.setNationCode(nationCode);
    return model;
  }

}
