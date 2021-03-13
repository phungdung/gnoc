/**
 * @(#)InfraIpBO.java 8/27/2015 3:03 PM, Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(schema = "COMMON_GNOC", name = "INFRA_DEVICE")
@Getter
@Setter
@NoArgsConstructor
public class InfraDeviceEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "INFRA_DEVICE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "DEVICE_ID", nullable = false)
  //Fields
  private Long deviceId;

  @Column(name = "STATION_ID")
  private Long stationId;

  @Column(name = "DEVICE_CODE", nullable = false)
  private String deviceCode;

  @Column(name = "DEVICE_TYPE_ID")
  private Long deviceTypeId;

  @Column(name = "DEPARTMENT_ID")
  private Long departmentId;

  @Column(name = "SERIAL")
  private String serial;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_DATE")
  private Date createDate;

  @Column(name = "SUPPLIER_ID")
  private Long supplierId;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "OWNER_ID")
  private Long ownerId;

  @Column(name = "DEVICE_NAME")
  private String deviceName;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "NETWORK_CLASS")
  private String networkClass;

  @Column(name = "NETWORK_TYPE")
  private String networkType;

  @Column(name = "LONG_MIGRATE_STATION_CODE")
  private String longMigrateStationCode;

  @Column(name = "DEVICE_CODE_OLD")
  private String deviceCodeOld;

  @Column(name = "DC_CABINET")
  private Double dcCabinet;

  @Column(name = "HOST_NAME")
  private String hostName;

  @Column(name = "BUIDING_ID")
  private Double buidingId;

  @Column(name = "FLOOR_ID")
  private Double floorId;

  @Column(name = "CPU")
  private String cpu;

  @Column(name = "RAM")
  private String ram;

  @Column(name = "HDD")
  private String hdd;

  @Column(name = "SUBNETMASK")
  private String subnetmask;

  @Column(name = "ERROR_TELNET_REASON")
  private String errorTelnetReason;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "INSERT_TIME")
  private Date insertTime;

  @Column(name = "ACCEPTANCE")
  private Long acceptance;

  @Column(name = "NATION_CODE")
  private String nationCode;

  public InfraDeviceDTO toDTO() {
    InfraDeviceDTO dto = new InfraDeviceDTO(
        deviceId == null ? null : deviceId.toString(),
        stationId == null ? null : stationId.toString(), deviceCode,
        deviceTypeId == null ? null : deviceTypeId.toString(),
        departmentId == null ? null : departmentId.toString(),
        serial, createDate == null ? null
        : DateTimeUtils.convertDateToString(createDate, Constants.ddMMyyyy),
        supplierId == null ? null : supplierId.toString(), description,
        ownerId == null ? null : ownerId.toString(),
        deviceName, status == null ? null : status.toString(), networkClass, networkType,
        longMigrateStationCode, deviceCodeOld,
        dcCabinet == null ? null : dcCabinet.toString(), hostName,
        buidingId == null ? null : buidingId.toString(),
        floorId == null ? null : floorId.toString(), cpu, ram, hdd, subnetmask, errorTelnetReason,
        lastUpdateTime == null ? null
            : DateTimeUtils.convertDateToString(lastUpdateTime, Constants.ddMMyyyy),
        insertTime == null ? null
            : DateTimeUtils.convertDateToString(insertTime, Constants.ddMMyyyy),
        acceptance == null ? null : acceptance.toString(), nationCode
    );
    return dto;
  }
}

