/**
 * @(#)InfraDeviceForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfraDeviceDTO extends BaseDto {

  private String checkbox;
  private String deviceId;
  private String stationId;
  private String deviceCode;
  private String deviceTypeId;
  private String departmentId;
  private String serial;
  private String createDate;
  private String supplierId;
  private String description;
  private String ownerId;
  private String deviceName;
  private String status;
  private String networkClass;
  private String networkType;
  private String longMigrateStationCode;
  private String deviceCodeOld;
  private String dcCabinet;
  private String hostName;
  private String buidingId;
  private String floorId;
  private String cpu;
  private String ram;
  private String hdd;
  private String subnetmask;
  private String errorTelnetReason;
  private String lastUpdateTime;
  private String insertTime;
  private String acceptance;
  private String ip;
  private String ipId;
  private String nationCode;
  private List<String> lstDeviceCode;
  private String resultImport;
  private List<String> lstIps;
  private String dtCode;

  public InfraDeviceDTO(String deviceId, String stationId, String deviceCode, String deviceTypeId,
      String departmentId, String serial,
      String createDate, String supplierId, String description, String ownerId, String deviceName,
      String status, String networkClass,
      String networkType, String longMigrateStationCode, String deviceCodeOld, String dcCabinet,
      String hostName, String buidingId,
      String floorId, String cpu, String ram, String hdd, String subnetmask,
      String errorTelnetReason, String lastUpdateTime, String insertTime,
      String acceptance, String nationCode) {
    this.deviceId = deviceId;
    this.stationId = stationId;
    this.deviceCode = deviceCode;
    this.deviceTypeId = deviceTypeId;
    this.departmentId = departmentId;
    this.serial = serial;
    this.createDate = createDate;
    this.supplierId = supplierId;
    this.description = description;
    this.ownerId = ownerId;
    this.deviceName = deviceName;
    this.status = status;
    this.networkClass = networkClass;
    this.networkType = networkType;
    this.longMigrateStationCode = longMigrateStationCode;
    this.deviceCodeOld = deviceCodeOld;
    this.dcCabinet = dcCabinet;
    this.hostName = hostName;
    this.buidingId = buidingId;
    this.floorId = floorId;
    this.cpu = cpu;
    this.ram = ram;
    this.hdd = hdd;
    this.subnetmask = subnetmask;
    this.errorTelnetReason = errorTelnetReason;
    this.lastUpdateTime = lastUpdateTime;
    this.insertTime = insertTime;
    this.acceptance = acceptance;
    this.nationCode = nationCode;
  }
}
