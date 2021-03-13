package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CrModuleDraftDTO;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.model.CrAlarmEntity;
import com.viettel.nims.infra.webservice.CatVendorBO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class CrAlarmInsiteDTO extends BaseDto {

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
  private String defaultSortField;
  private String autoLoad;
  private Long crImpactSegmentId;
  private Long crDeviceTypeId;
  private String keyword;

  private String createdUser;
  private List<CatVendorBO> lstVendor;
  private List<CrModuleDraftDTO> lstModule;
  private Long crAlarmSettingId;
  //CR_Upgrade_R3333308: them nang cap goi sang NOC
  private String processId;
  private Long numberOccurences;//so lan xuat hien lon hon toi thieu

  public CrAlarmEntity toEntity() {
    CrAlarmEntity alarm = new CrAlarmEntity(
        this.id == null ? null : objectToLong(this.id),
        this.crId == null ? null : objectToLong(this.crId),
        this.faultSrc,
        this.faultId == null ? null : objectToLong(this.faultId),
        this.faultName,
        this.faultGroupId == null ? null : objectToLong(this.faultGroupId),
        this.faultGroupName,
        this.faultLevelId == null ? null : objectToLong(this.faultLevelId),
        this.faultLevelCode,
        this.deviceTypeId == null ? null : objectToLong(this.deviceTypeId),
        this.deviceTypeCode,
        this.vendorCode,
        this.createDate == null ? null : DateTimeUtils.convertStringToDate(createDate),
        this.numberOccurences == null ? null : objectToLong(this.numberOccurences)
    );
    alarm.setVendorCode(vendorCode);
    alarm.setVendorName(vendorName);
    alarm.setModuleCode(moduleCode);
    alarm.setModuleName(moduleName);
    alarm.setNationCode(nationCode);
    alarm.setKeyword(keyword);
    return alarm;
  }

  private Long objectToLong(Object o) {
    try {
      return Long.parseLong(String.valueOf(o));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public CrAlarmDTO toOutSideDTO() {
    CrAlarmDTO dto = new CrAlarmDTO(
        id,
        crId,
        faultSrc,
        faultId,
        createDate,
        faultName,
        faultGroupId,
        faultGroupName,
        faultLevelId,
        faultLevelCode,
        deviceTypeId,
        deviceTypeCode,
        vendorId,
        vendorCode,
        vendorName,
        moduleId,
        serviceCode,
        moduleCode,
        moduleName,
        nationCode,
        checkbox,
        autoLoad,
        crImpactSegmentId,
        crDeviceTypeId,
        keyword,
        defaultSortField);
    return dto;
  }

}
