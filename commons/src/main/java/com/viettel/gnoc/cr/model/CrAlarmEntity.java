/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CR_ALARM")
public class CrAlarmEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_ALARM_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "FAULT_SRC")
  private String faultSrc;

  @Column(name = "FAULT_ID")
  private Long faultId;

  @Column(name = "CREATE_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date createDate;

  @Column(name = "FAULT_NAME")
  private String faultName;

  @Column(name = "FAULT_GROUP_ID")
  private Long faultGroupId;

  @Column(name = "FAULT_GROUP_NAME")
  private String faultGroupName;

  @Column(name = "FAULT_LEVEL_ID")
  private Long faultLevelId;

  @Column(name = "FAULT_LEVEL_CODE")
  private String faultLevelCode;

  @Column(name = "DEVICE_TYPE_ID")
  private Long deviceTypeId;

  @Column(name = "DEVICE_TYPE_CODE")
  private String deviceTypeCode;

  @Column(name = "VENDOR_CODE")
  private String vendorCode;

  @Column(name = "VENDOR_NAME")
  private String vendorName;

  @Column(name = "MODULE_CODE")
  private String moduleCode;

  @Column(name = "MODULE_NAME")
  private String moduleName;

  @Column(name = "NATION_CODE")
  private String nationCode;

  @Column(name = "keyword")
  private String keyword;

  @Column(name = "NUMBER_OCCURENCES")
  private Long numberOccurences;

  public CrAlarmEntity(Long id,
      Long crId,
      String faultSrc,
      Long faultId,
      String faultName,
      Long faultGroupId,
      String faultGroupName,
      Long faultLevelId,
      String faultLevelCode,
      Long deviceTypeId,
      String deviceTypeCode,
      String vendorCode,
      Date createDate,
      Long numberOccurences
      ) {

    this.id = id;
    this.crId = crId;
    this.faultSrc = faultSrc;
    this.faultId = faultId;
    this.faultName = faultName;
    this.faultGroupId = faultGroupId;
    this.faultGroupName = faultGroupName;
    this.faultLevelId = faultLevelId;
    this.faultLevelCode = faultLevelCode;
    this.deviceTypeId = deviceTypeId;
    this.deviceTypeCode = deviceTypeCode;
    this.vendorCode = vendorCode;
    this.createDate = createDate;
    this.numberOccurences = numberOccurences;
  }

  public CrAlarmInsiteDTO toDTO() {
    CrAlarmInsiteDTO alarmDTO = new CrAlarmInsiteDTO();

    alarmDTO.setId(this.id);
    alarmDTO.setCrId(this.crId);
    alarmDTO.setFaultSrc(this.faultSrc);
    alarmDTO.setFaultId(this.faultId);
    alarmDTO.setFaultName(faultName);
    alarmDTO.setFaultGroupId(this.faultGroupId);
    alarmDTO.setFaultGroupName(this.faultGroupName);
    alarmDTO.setFaultLevelId(faultLevelId);
    alarmDTO.setFaultLevelCode(faultLevelCode);
    alarmDTO.setDeviceTypeId(deviceTypeId);
    alarmDTO.setDeviceTypeCode(deviceTypeCode);
    alarmDTO.setVendorCode(vendorCode);

    alarmDTO.setVendorCode(vendorCode);
    alarmDTO.setVendorName(vendorName);
    alarmDTO.setModuleCode(moduleCode);
    alarmDTO.setModuleName(moduleName);
    alarmDTO.setNationCode(nationCode);
    alarmDTO.setKeyword(keyword);
    alarmDTO.setNumberOccurences(numberOccurences);
    if (createDate != null) {
      alarmDTO.setCreateDate(DateTimeUtils.date2ddMMyyyyHHMMss(createDate));
    }

    return alarmDTO;
  }

}
