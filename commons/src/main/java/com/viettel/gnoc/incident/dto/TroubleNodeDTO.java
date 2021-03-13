/**
 * @(#)TroubleNodeForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.model.TroubleNodeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TroubleNodeDTO extends BaseDto {

  //Fields
  private Long id;
  private Long troubleId;
  private Long deviceId;
  private String deviceCode;
  private String deviceName;
  private String ip;

  private String vendor;

  public TroubleNodeDTO(Long id, Long troubleId, Long deviceId) {
    this.id = id;
    this.troubleId = troubleId;
    this.deviceId = deviceId;
  }

  public TroubleNodeDTO(
      Long id, Long troubleId, Long deviceId, String deviceCode, String deviceName, String ip,
      String vendor) {
    this.id = id;
    this.troubleId = troubleId;
    this.deviceId = deviceId;
    this.deviceCode = deviceCode;
    this.deviceName = deviceName;
    this.ip = ip;
    this.vendor = vendor;
  }

  public TroubleNodeEntity toEntity() {
    TroubleNodeEntity model = new TroubleNodeEntity(
        StringUtils.validString(id) ? Long.valueOf(id) : null,
        StringUtils.validString(troubleId) ? Long.valueOf(troubleId) : null,
        StringUtils.validString(deviceId) ? Long.valueOf(deviceId) : null,
        deviceCode,
        deviceName,
        ip,
        vendor
    );
    return model;
  }

}
