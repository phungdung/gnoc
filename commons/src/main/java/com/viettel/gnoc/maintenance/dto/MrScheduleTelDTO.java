package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrScheduleTelEntity;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MrScheduleTelDTO extends BaseDto {

  private Long scheduleId;
  private String marketCode;
  private String arrayCode;
  private String deviceType;
  private Long deviceId;
  private String deviceCode;
  private String deviceName;
  private Long procedureId;
  private Date lastDate;
  private Date nextDate;
  private Date nextDateModify;
  private String modifyUser;
  private Date modifyDate;
  private Date updatedDate;
  private Long mrId;
  private String mrSoft;
  private String mrHard;
  private String mr12m;
  private String station;
  private String region;
  private Long mrHardCycle;
  private String mrConfirm;
  private String mrComment;
  private String networkType;
  private Long crId;
  private Long woId;
  private String groupCode;
  //add
  private String marketName;
  private String arrayName;
  private String deviceTypeName;
  private String nodeIp;
  private String procedureName;
  private String mrType;
  private String mrCode;
  private String crNumber;
  private String mrContentId;
  private String mrMode;
  private String cycle;
  private String cycleType;
  private String cycleTypeName;
  private String implementUnitId;
  private String implementUnitName;
  private Date dateModifyFromSearch;
  private Date dateModifyToSearch;
  private String userMrHard;
  private String woCode;
  //BEGIN RIKKEI
  private String scheduleType;
  private String nationName;
  private String checkingUnitId;
  private String checkingUnitName;
  private String mrConfirmName;
  private String vendor;
  private String impactNode;
  private String woContent;
  private String coordinatingUnitHard;
  private String coordinatingUnitHardName;
  private String crContent;
  //END RIKKEI
  private String typeCr;
  private String arrayActionName;

  private String resultImport;
  private String scheduleIdStr;
  private String arrayCodeStr;
  private String mrIdStr;
  private String lastDateStr;
  private String nextDateStr;
  private String nextDateModifyStr;
  private String updatedDateStr;
  private String stationStr;
  private List<String> scheduleIdList;
  //tiennv them crIdStr for import
  private String crIdStr;

  public MrScheduleTelDTO(Long scheduleId, String marketCode, String arrayCode,
      String deviceType, Long deviceId, String deviceCode, String deviceName,
      Long procedureId, Date lastDate, Date nextDate, Date nextDateModify,
      String modifyUser, Date modifyDate, Date updatedDate, Long mrId, String mrSoft,
      String mrHard, String mr12m, String station, String region, Long mrHardCycle,
      String mrConfirm, String mrComment, String networkType, Long crId, Long woId,
      String groupCode) {
    this.scheduleId = scheduleId;
    this.marketCode = marketCode;
    this.arrayCode = arrayCode;
    this.deviceType = deviceType;
    this.deviceId = deviceId;
    this.deviceCode = deviceCode;
    this.deviceName = deviceName;
    this.procedureId = procedureId;
    this.lastDate = lastDate;
    this.nextDate = nextDate;
    this.nextDateModify = nextDateModify;
    this.modifyUser = modifyUser;
    this.modifyDate = modifyDate;
    this.updatedDate = updatedDate;
    this.mrId = mrId;
    this.mrSoft = mrSoft;
    this.mrHard = mrHard;
    this.mr12m = mr12m;
    this.station = station;
    this.region = region;
    this.mrHardCycle = mrHardCycle;
    this.mrConfirm = mrConfirm;
    this.mrComment = mrComment;
    this.networkType = networkType;
    this.crId = crId;
    this.woId = woId;
    this.groupCode = groupCode;
  }

  public MrScheduleTelDTO(Long scheduleId) {
    this.scheduleId = scheduleId;
  }

  public MrScheduleTelEntity toEntity() {
    return new MrScheduleTelEntity(scheduleId, marketCode, arrayCode, deviceType, deviceId,
        deviceCode,
        deviceName, procedureId, lastDate, nextDate, nextDateModify, modifyUser, modifyDate,
        updatedDate, mrId, mrSoft, mrHard, mr12m, station, region, mrHardCycle, mrConfirm,
        mrComment, networkType, crId, woId, groupCode);
  }
}
