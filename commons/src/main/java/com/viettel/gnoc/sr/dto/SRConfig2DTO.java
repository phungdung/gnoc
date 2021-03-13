package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.sr.model.SRConfig2Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SRConfig2DTO {

  private Long configId;
  private String configCode;
  private String status;
  private String nextStatus;
  private String serviceArray;
  private String serviceGroup;
  private String currentStatus;
  private String configGroup;
  private String configDes;
  private String serviceCode;

  private SrWsToolCrDTO srWsToolCrDTO;

  public SRConfig2DTO(Long configId, String configCode, String status, String nextStatus,
      String serviceArray, String serviceGroup, String currentStatus, String configGroup,
      String configDes, String serviceCode) {
    this.configId = configId;
    this.configCode = configCode;
    this.status = status;
    this.nextStatus = nextStatus;
    this.serviceArray = serviceArray;
    this.serviceGroup = serviceGroup;
    this.currentStatus = currentStatus;
    this.configGroup = configGroup;
    this.configDes = configDes;
    this.serviceCode = serviceCode;
  }

  public SRConfig2Entity toEntity() {
    return new SRConfig2Entity(configId, configCode, status, nextStatus, serviceArray, serviceGroup,
        currentStatus, configGroup, configDes, serviceCode);
  }
}
