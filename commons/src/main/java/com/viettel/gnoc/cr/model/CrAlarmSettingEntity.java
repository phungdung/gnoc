package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DungPV
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CR_ALARM_SETTTING")
public class CrAlarmSettingEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.CR_ALARM_SETTING_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CAS_ID", unique = true, nullable = false)
  private Long casId;
  @Column(name = "TYPE")
  private Long type;
  @Column(name = "NATION_CODE")
  private String nationCode;
  @Column(name = "VENDOR_CODE")
  private String vendorCode;
  @Column(name = "VENDOR_NAME")
  private String vendorName;
  @Column(name = "MODULE_CODE")
  private String moduleCode;
  @Column(name = "MODULE_NAME")
  private String moduleName;
  @Column(name = "APPROVAL_STATUS")
  private Long approvalStatus;
  @Column(name = "APPROVAL_USER_ID")
  private Long approvalUserId;
  @Column(name = "FAULT_SRC")
  private String faultSrc;
  @Column(name = "FAULT_ID")
  private Long faultId;
  @Column(name = "FAULT_NAME")
  private String faultName;
  @Column(name = "FAULT_GROUP_NAME")
  private String faultGroupName;
  @Column(name = "FAULT_LEVEL_CODE")
  private String faultLevelCode;
  @Column(name = "DEVICE_TYPE_CODE")
  private String deviceTypeCode;
  @Column(name = "CR_PROCESS_ID")
  private Long crProcessId;
  @Column(name = "AUTO_LOAD")
  private Long autoLoad;
  @Column(name = "CREATED_USER")
  private String createdUser;
  @Column(name = "keyword")
  private String keyword;
  @Column(name = "NUMBER_OCCURENCES")
  private Long numberOccurences;

  public CrAlarmSettingDTO toDTO() {
    return new CrAlarmSettingDTO(casId, type, nationCode, vendorCode, vendorName, moduleCode,
        moduleName, approvalStatus, approvalUserId, faultSrc, faultId, faultName, faultGroupName,
        faultLevelCode, deviceTypeCode, crProcessId, autoLoad, createdUser, keyword, numberOccurences);
  }
}
