package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrITHardScheduleEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j

public class MrITHardScheduleDTO extends BaseDto {

  private Long scheduleId;
  private String marketId;
  private String marketCode;
  private String arrayCode;
  private String deviceType;
  private String deviceId;
  private String deviceCode;
  private String deviceName;
  private Long procedureId;
  private Date lastDate;
  private Date uptimeDate;
  private Date nextDate;
  private Date nextDateModify;
  private String modifyUser;
  private Date modifyDate;
  private Date updatedDate;
  private String levelImportant;
  private Long mrId;
  private Long providerId;
  private String groupCode;
  private String ipServer;
  private String vendor;
  private String region;
  private String stationCode;
  private String bdType;
  private String networkType;
  private String note;
  private String ipNode;
  private Long cdId;
  private Long woId;
  private String userbd;
  private String reasonBd;
  private Long crId;

  private String dateModifyFromSearch;
  private String dateModifyToSearch;
  private Long cycle;
  private String checkMr;
  private String nationName;
  private String marketName;
  private String arrayCodeStr;
  private String nodeAffected;
  private Long objectId;
  private String nextDateStr;
  private String lastDateStr;
  private String wlgText;
  private String descriptionCr;
  private Long implementUnitId;
  private String implementUnitName;
  private Long checkingUnitId;
  private String checkingUnitName;
  private String cycleType;
  private String ud;
  private String db;
  private Long boUnit;
  private Long approveStatus;
  private String procedureName;
  private String nextDateModifyStr;
  private String mrConfirmHard;
  private String mrConfirmHardName;
  private String resultImport;
  private String boUnitName;

  private String mrContentId;
  private String mrType;
  private String mrCode;
  private String mrHard;
  private String arrayName;
  private String crNumber;
  private String typeCr;
  private String arrayActionName;
  private Long cdIdHard;
  private String cdIdHardStr;
  private String woContent;
  private String woCode;

  public MrITHardScheduleDTO(Long scheduleId, String marketCode, String arrayCode,
      String deviceType, String deviceId, String deviceCode, String deviceName,
      Long procedureId, Date lastDate, Date uptimeDate, Date nextDate, Date nextDateModify,
      String modifyUser, Date modifyDate, Date updatedDate, String levelImportant,
      Long mrId, Long providerId, String groupCode, String ipServer, String vendor,
      String region, String stationCode, String bdType, String networkType, String note,
      String ipNode, Long cdId, Long woId, String userbd, String reasonBd, Long crId) {
    this.scheduleId = scheduleId;
    this.marketCode = marketCode;
    this.arrayCode = arrayCode;
    this.deviceType = deviceType;
    this.deviceId = deviceId;
    this.deviceCode = deviceCode;
    this.deviceName = deviceName;
    this.procedureId = procedureId;
    this.lastDate = lastDate;
    this.uptimeDate = uptimeDate;
    this.nextDate = nextDate;
    this.nextDateModify = nextDateModify;
    this.modifyUser = modifyUser;
    this.modifyDate = modifyDate;
    this.updatedDate = updatedDate;
    this.levelImportant = levelImportant;
    this.mrId = mrId;
    this.providerId = providerId;
    this.groupCode = groupCode;
    this.ipServer = ipServer;
    this.vendor = vendor;
    this.region = region;
    this.stationCode = stationCode;
    this.bdType = bdType;
    this.networkType = networkType;
    this.note = note;
    this.ipNode = ipNode;
    this.cdId = cdId;
    this.woId = woId;
    this.userbd = userbd;
    this.reasonBd = reasonBd;
    this.crId = crId;
  }

  public MrITHardScheduleDTO(Long scheduleId) {
    this.scheduleId = scheduleId;
  }

  public MrITHardScheduleEntity toEntity() {
    return new MrITHardScheduleEntity(scheduleId, marketCode, arrayCode, deviceType, deviceId,
        deviceCode, deviceName, procedureId, lastDate, uptimeDate, nextDate, nextDateModify,
        modifyUser, modifyDate, updatedDate, levelImportant, mrId, providerId, groupCode, ipServer,
        vendor, region, stationCode, bdType, networkType, note, ipNode, cdId, woId, userbd,
        reasonBd, crId);
  }
}
