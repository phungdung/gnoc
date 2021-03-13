package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrSynItDevicesEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MrSynItDevicesDTO extends BaseDto {

  private String id;
  private String marketCode;
  private String objectCode;
  private String mrConfirmHard;
  private Date updateDate;
  private String levelImportant;
  private String mrSoft;
  private String isCompleteSoft;
  private String mrHard;
  private String mrConfirmSoft;
  private String ud;
  private String regionSoft;
  private String notes;
  private String status;
  private String arrayCode;
  private String ipNode;
  private String objectId;
  private String regionHard;
  private String isComplete1m;
  private String groupCode;
  private String createUserSoft;
  private String isComplete3m;
  private String createUserHard;
  private String isComplete12m;
  private String deviceType;
  private String userMrHard;
  private String station;
  private Date synDate;
  private Long upTime;
  private String updateUser;
  private String cdId;
  private Date lastDate6m;
  private Date lastDate1m;
  private Date lastDateSoft;
  private Date lastDate3m;
  private String objectName;
  private String vendor;
  private Date lastDate12m;
  private String isComplete6m;
  private String db;
  private String nodeAffected;
  private String boUnit;
  private String approveStatus;
  private String approveReason;

  //dungpv add
  private String boUnitHard;
  private String approveStatusHard;
  private String approveReasonHard;
  private String boUnitHardName;
  private String approveStatusHardName;
  private Long isRunMop;
  private String isRunMopName;
  //end

  private String userLogin;
  private String unitLogin;
  private String isNotSchedule;//chua sinh lich
  private String implementUnit;
  private String implementUnitName;
  private String checkingUnit;
  private String checkingUnitName;
  private String marketName;
  private String mrSoftDisplay;
  private String cdIdName;
  private String mrConfirmSoftDisplay;
  private String mrConfirmHardDisplay;
  private String levelImportantName;
  private String boUnitName;

  private String idStr;
  private String arrayCodeStr;
  private String deviceTypeStr;
  private String statusStr;
  private String mrConfirmSoftStr;
  private String mrConfirmHardStr;
  private String mrSoftStr;
  private String mrHardStr;
  private String stationStr;
  private String checkNode;
  private String checkApprove;
  private String approveStatusName;
  private String resultImport;
  private MrSynItDevicesDTO mrSynItDevicesOld;
  private String countryCode;
  private String countryName;
  private String statusIIM;
  private String marketCodeIIM;

  public MrSynItDevicesDTO(String id, String marketCode, String objectCode, String mrConfirmHard,
      Date updateDate, String levelImportant, String mrSoft, String isCompleteSoft, String mrHard,
      String mrConfirmSoft, String ud, String regionSoft, String notes, String status,
      String arrayCode, String ipNode, String objectId, String regionHard, String isComplete1m,
      String groupCode, String createUserSoft, String isComplete3m, String createUserHard,
      String isComplete12m, String deviceType, String userMrHard, String station, Date synDate,
      Long upTime, String updateUser, String cdId, Date lastDate6m, Date lastDate1m,
      Date lastDateSoft, Date lastDate3m, String objectName, String vendor, Date lastDate12m,
      String isComplete6m, String db, String nodeAffected, String boUnit, String approveStatus,
      String approveReason, String statusIIM, String boUnitHard, String approveStatusHard,
      String approveReasonHard, Long isRunMop, String marketCodeIIM) {
    this.marketCode = marketCode;
    this.objectCode = objectCode;
    this.mrConfirmHard = mrConfirmHard;
    this.updateDate = updateDate;
    this.levelImportant = levelImportant;
    this.mrSoft = mrSoft;
    this.isCompleteSoft = isCompleteSoft;
    this.mrHard = mrHard;
    this.mrConfirmSoft = mrConfirmSoft;
    this.ud = ud;
    this.regionSoft = regionSoft;
    this.notes = notes;
    this.status = status;
    this.arrayCode = arrayCode;
    this.ipNode = ipNode;
    this.objectId = objectId;
    this.regionHard = regionHard;
    this.id = id;
    this.isComplete1m = isComplete1m;
    this.groupCode = groupCode;
    this.createUserSoft = createUserSoft;
    this.isComplete3m = isComplete3m;
    this.createUserHard = createUserHard;
    this.isComplete12m = isComplete12m;
    this.deviceType = deviceType;
    this.userMrHard = userMrHard;
    this.station = station;
    this.synDate = synDate;
    this.upTime = upTime;
    this.updateUser = updateUser;
    this.cdId = cdId;
    this.lastDate6m = lastDate6m;
    this.lastDate1m = lastDate1m;
    this.lastDateSoft = lastDateSoft;
    this.lastDate3m = lastDate3m;
    this.objectName = objectName;
    this.vendor = vendor;
    this.lastDate12m = lastDate12m;
    this.isComplete6m = isComplete6m;
    this.db = db;
    this.nodeAffected = nodeAffected;
    this.boUnit = boUnit;
    this.approveStatus = approveStatus;
    this.approveReason = approveReason;
    this.statusIIM = statusIIM;
    this.boUnitHard = boUnitHard;
    this.approveStatusHard = approveStatusHard;
    this.approveReasonHard = approveReasonHard;
    this.isRunMop = isRunMop;
    this.marketCodeIIM = marketCodeIIM;
  }


  public MrSynItDevicesEntity toEntity() {
    return new MrSynItDevicesEntity(
        StringUtils.validString(id) ? Long.valueOf(id) : null,
        marketCode,
        objectCode,
        StringUtils.validString(mrConfirmHard) ? Long.valueOf(mrConfirmHard) : null,
        updateDate,
        StringUtils.validString(levelImportant) ? Long.valueOf(levelImportant) : null,
        StringUtils.validString(mrSoft) ? Long.valueOf(mrSoft) : null,
        StringUtils.validString(isCompleteSoft) ? Long.valueOf(isCompleteSoft) : null,
        StringUtils.validString(mrHard) ? Long.valueOf(mrHard) : null,
        StringUtils.validString(mrConfirmSoft) ? Long.valueOf(mrConfirmSoft) : null,
        ud,
        regionSoft,
        notes,
        StringUtils.validString(status) ? Long.valueOf(status) : null,
        arrayCode,
        ipNode,
        StringUtils.validString(objectId) ? Long.valueOf(objectId) : null,
        regionHard,
        StringUtils.validString(isComplete1m) ? Long.valueOf(isComplete1m) : null,
        groupCode,
        createUserSoft,
        StringUtils.validString(isComplete3m) ? Long.valueOf(isComplete3m) : null,
        createUserHard,
        StringUtils.validString(isComplete12m) ? Long.valueOf(isComplete12m) : null,
        deviceType,
        userMrHard,
        station,
        synDate,
        upTime,
        updateUser,
        StringUtils.validString(cdId) ? Long.valueOf(cdId) : null,
        lastDate6m,
        lastDate1m,
        lastDateSoft,
        lastDate3m,
        objectName,
        vendor,
        lastDate12m,
        StringUtils.validString(isComplete6m) ? Long.valueOf(isComplete6m) : null,
        db,
        nodeAffected,
        StringUtils.validString(boUnit) ? Long.valueOf(boUnit) : null,
        StringUtils.validString(approveStatus) ? Long.valueOf(approveStatus) : null,
        approveReason, statusIIM,
        StringUtils.validString(boUnitHard) ? Long.valueOf(boUnitHard) : null,
        StringUtils.validString(approveStatusHard) ? Long.valueOf(approveStatusHard) : null,
        approveReasonHard, isRunMop, marketCodeIIM
    );
  }
}
