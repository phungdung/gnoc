/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ITSOL
 */

@Getter
@Setter
@AllArgsConstructor
public class CrAlarmDTO {

  private Long id;
  private Long crId;
  private String faultSrc;
  private Long faultId;
  private String createDate;
  private String faultName;
  private Long faultGroupId;
  private String faultGroupName;
  private Long faultLevelId;
  private String faultLevelCode;
  private Long deviceTypeId;
  private String deviceTypeCode;
  private String vendorId;
  private String vendorCode;
  private String vendorName;
  private String moduleId;
  private String serviceCode;
  private String moduleCode;
  private String moduleName;
  private String nationCode;
  private String checkbox;
  private String autoLoad;
  private Long crImpactSegmentId;
  private Long crDeviceTypeId;
  private String keyword;
  private String defaultSortField;

  public CrAlarmDTO() {
    setDefaultSortField("name");
  }

  public CrAlarmInsiteDTO toInsiteDTO() {
    CrAlarmInsiteDTO dto = new CrAlarmInsiteDTO(
        this.id,
        this.crId,
        this.faultSrc,
        this.faultId,
        this.createDate,
        this.faultName,
        this.faultGroupId,
        this.faultGroupName,
        this.faultLevelId,
        this.faultLevelCode,
        this.deviceTypeId,
        this.deviceTypeCode,
        this.vendorId,
        this.vendorCode,
        this.vendorName,
        this.moduleId,
        this.serviceCode,
        this.moduleCode,
        this.moduleName,
        this.nationCode,
        this.checkbox,
        this.defaultSortField,
        this.autoLoad,
        this.crImpactSegmentId,
        this.crDeviceTypeId,
        this.keyword,
        null,
        null,
        null,
        null,
        null,
        null);
    return dto;
  }
}
