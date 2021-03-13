/**
 * @(#)InfraIpForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InfraIpDTO {

  //Fields
  private String defaultSortField;
  private String deviceId;
  private String deviceIdName;
  private String ipType;
  private String ipVersion;
  private String ip;
  private String ipId;
  private String lastUpdateTime;

  public InfraIpDTO(String deviceId, String ipType, String ipVersion, String ip, String ipId,
      String lastUpdateTime) {
    this.deviceId = deviceId;
    this.ipType = ipType;
    this.ipVersion = ipVersion;
    this.ip = ip;
    this.ipId = ipId;
    this.lastUpdateTime = lastUpdateTime;
  }

}
