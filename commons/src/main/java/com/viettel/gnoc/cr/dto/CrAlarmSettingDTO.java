package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CrModuleDraftDTO;
import com.viettel.gnoc.cr.model.CrAlarmSettingEntity;
import com.viettel.nims.infra.webservice.CatVendorBO;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DungPV
 */
@Setter
@Getter
@NoArgsConstructor
public class CrAlarmSettingDTO extends BaseDto {

  private Long casId;
  private Long type;
  private String nationCode;
  private String vendorCode;
  private String vendorName;
  private String moduleCode;
  private String moduleName;
  private Long approvalStatus;
  private Long approvalUserId;
  private String faultSrc;
  private Long faultId;
  private String faultName;
  private String faultGroupName;
  private String faultLevelCode;
  private String deviceTypeCode;
  private Long crProcessId;
  private String crProcessName;
  private Long autoLoad;
  private String createdUser;
  private String autoLoadName;
  private String status;
  private Long crDomain;
  private Long deviceType;
  private String keyword;

  private List<Long> lstDeleteId;
  private List<CrAlarmInsiteDTO> lstAlarm;
  private List<CatVendorBO> lstVendor;
  private List<CrModuleDraftDTO> lstModule;
  private String serviceCode;
  private List<String> lstvendorCode;
  private String crId;
  private Long array;
  private String alarmName;
  private List<String> lstModuleCode;
  private Long numberOccurences;//so lan xuat hien lon hon toi thieu

  public CrAlarmSettingDTO(Long casId, Long type, String nationCode, String vendorCode,
      String vendorName, String moduleCode, String moduleName, Long approvalStatus,
      Long approvalUserId, String faultSrc, Long faultId, String faultName,
      String faultGroupName, String faultLevelCode, String deviceTypeCode, Long crProcessId,
      Long autoLoad, String createdUser, String keyword, Long numberOccurences) {
    this.casId = casId;
    this.type = type;
    this.nationCode = nationCode;
    this.vendorCode = vendorCode;
    this.vendorName = vendorName;
    this.moduleCode = moduleCode;
    this.moduleName = moduleName;
    this.approvalStatus = approvalStatus;
    this.approvalUserId = approvalUserId;
    this.faultSrc = faultSrc;
    this.faultId = faultId;
    this.faultName = faultName;
    this.faultGroupName = faultGroupName;
    this.faultLevelCode = faultLevelCode;
    this.deviceTypeCode = deviceTypeCode;
    this.crProcessId = crProcessId;
    this.autoLoad = autoLoad;
    this.createdUser = createdUser;
    this.keyword = keyword;
    this.numberOccurences = numberOccurences;
  }

  public CrAlarmSettingEntity toModel() {
    return new CrAlarmSettingEntity(casId, type, nationCode, vendorCode, vendorName, moduleCode,
        moduleName, approvalStatus, approvalUserId, faultSrc, faultId, faultName, faultGroupName,
        faultLevelCode, deviceTypeCode, crProcessId, autoLoad, createdUser, keyword, numberOccurences);
  }
}
