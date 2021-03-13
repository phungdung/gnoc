package com.viettel.gnoc.incident.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfraDeviceExportDTO {

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
  //    private static long changedTime = 0;
  //Constructor
  private String nationCode;
  private List<String> lstDeviceCode;
  private String resultImport;
}
