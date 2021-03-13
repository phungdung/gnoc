/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.cr.dto;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.model.CrImpactedNodesEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class CrImpactedNodesDTO extends BaseDto {

  private String defaultSortField;
  //Fields
  private String cinsId;
  private String crId;
  private String ipId;
  private String deviceId;
  private String ip;
  private String deviceName;
  private String deviceCode;
  private String deviceCodeOld;
  private String insertTime;
  private String type;
  private String nationCode;
  private String dtCode;

  private String crCreatedDateStr;
  private String earlierStartTimeStr;
  private String saveType;
  private String nodeType;
  private String userId;

  public CrImpactedNodesDTO() {
    setDefaultSortField("crId");
  }

  public CrImpactedNodesDTO(String cinsId, String crId, String ipId, String deviceId,
      String insertTime, String dtCode) {
    this.cinsId = cinsId;
    this.crId = crId;
    this.ipId = ipId;
    this.deviceId = deviceId;
    this.insertTime = insertTime;
    this.dtCode = dtCode;
    setDefaultSortField("crId");
  }

  public CrImpactedNodesEntity toEntity() {
    CrImpactedNodesEntity model = new CrImpactedNodesEntity(
        StringUtils.validString(cinsId)
            ? Long.valueOf(cinsId) : null,
        StringUtils.validString(crId)
            ? Long.valueOf(crId) : null,
        StringUtils.validString(ipId)
            ? Long.valueOf(ipId) : null,
        StringUtils.validString(deviceId)
            ? Long.valueOf(deviceId) : null,
        StringUtils.validString(insertTime)
            ? DateTimeUtils.convertStringToDate(insertTime) : null,
        dtCode);

    model.setIp(ip);
    model.setDeviceCode(deviceCode);
    model.setDeviceName(deviceName);
    model.setNationCode(nationCode);
    return model;
  }
}

