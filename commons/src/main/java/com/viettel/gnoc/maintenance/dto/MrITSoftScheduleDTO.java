package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrITSoftScheduleEntity;
import java.util.List;
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
public class MrITSoftScheduleDTO extends BaseDto {

  //Fields
  private String scheduleId;
  private String marketId;
  private String marketCode;
  private String marketName;
  private String nationName;
  private String arrayCode;
  private String arrayName;
  private String region;
  private String deviceType;
  private String deviceTypeName;
  private String deviceId;
  private String deviceCode;
  private String deviceName;
  private String procedureId;
  private String procedureName;
  private String lastDate;
  private String uptimeDate;
  private String nextDate;
  private String nextDateModify;
  private String modifyUser;
  private String modifyDate;
  private String updatedDate;
  private String levelImportant;
  private String mrId;
  private String mrType;
  private String mrCode;
  private String crId;
  private String crNumber;
  private String mrContentId;
  private String mrMode;
  private String cycle;
  private String cycleType;
  private String cycleTypeName;
  private String dateModifyFromSearch;
  private String dateModifyToSearch;
  private String providerId;
  private String groupCode;
  private String mrConfirm;
  private String mrConfirmName;
  private String timeUp;
  private String logMop;

  private String implementUnitId;
  private String implementUnitName;
  private String checkingUnitId;
  private String checkingUnitName;
  private String ip;
  private String ipNode;
  private String note;
  private String vendor;
  private String mrContent;
  private String networkType;
  private String cdId;
  private String woCode;
  private String userbd;
  private String reasonBd;
  private String typeCr;
  private String arrayActionName;
  private String woId;
  private String id;
  private String ipServer;

  private String boUnit;
  private String boUnitName;
  private String impactNode;
  private String bdType;
  private String stationCode;
  private String wlgText;
  private String ud;
  private String db;
  private String implementUnit;
  private String checkingUnit;
  private String descriptionCr;
  private String nextDateFrom;
  private String nextDateTo;
  private String checkMr;
  private String nodeAffected;
  private String deviceIp;
  private String status;
  private String approveStatus;
  private String approveStatusName;
  private String approveReason;
  private String userLogin;
  private String unitLogin;
  private String isApproveAble;
  private String scheduleQDDate;
  private String objectId;
  private String mrSoft;
  private String resultImport;
  private List<String> scheduleIdList;
  private String mrDate;
  public MrITSoftScheduleDTO(
      String scheduleId,
      String marketCode,
      String arrayCode,
      String deviceType,
      String deviceId,
      String deviceCode,
      String deviceName,
      String procedureId,
      String lastDate,
      String uptimeDate,
      String nextDate,
      String nextDateModify,
      String modifyUser,
      String modifyDate,
      String updatedDate,
      String levelImportant,
      String mrId,
      String providerId,
      String groupCode,
      String ipServer,
      String vendor, String region, String networkType,
      String ipNode, String note, String cdId, String woCode,
      String userbd, String reasonBd, String crId, String logMop) {
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
    this.networkType = networkType;
    this.ipNode = ipNode;
    this.note = note;
    this.cdId = cdId;
    this.woCode = woCode;
    this.userbd = userbd;
    this.reasonBd = reasonBd;
    this.crId = crId;
    this.logMop = logMop;
  }

  public MrITSoftScheduleEntity toEntity() {
    try {
      MrITSoftScheduleEntity model = new MrITSoftScheduleEntity(
          StringUtils.validString(scheduleId) ? Long.valueOf(scheduleId) : null,
          marketCode,
          arrayCode,
          region,
          deviceType,
          deviceId,
          deviceCode,
          deviceName,
          StringUtils.validString(procedureId) ? Long.valueOf(procedureId) : null,
          StringUtils.validString(lastDate) ? DateTimeUtils.convertStringToDate(lastDate) : null,
          StringUtils.validString(uptimeDate) ? DateTimeUtils.convertStringToDate(uptimeDate)
              : null,
          StringUtils.validString(nextDate) ? DateTimeUtils.convertStringToDate(nextDate) : null,
          StringUtils.validString(nextDateModify) ? DateTimeUtils
              .convertStringToDate(nextDateModify) : null,
          modifyUser,
          StringUtils.validString(modifyDate) ? DateTimeUtils.convertStringToDate(modifyDate)
              : null,
          StringUtils.validString(updatedDate) ? DateTimeUtils.convertStringToDate(updatedDate)
              : null,
          levelImportant,
          StringUtils.validString(mrId) ? Long.valueOf(mrId) : null,
          StringUtils.validString(providerId) ? Long.valueOf(providerId) : null,
          groupCode,
          ipServer,
          vendor, networkType, ipNode, note,
          StringUtils.validString(cdId) ? Long.valueOf(cdId) : null,
          StringUtils.validString(woId) ? Long.valueOf(woId) : null, userbd, reasonBd,
          StringUtils.validString(crId) ? Long.valueOf(crId) : null,
          logMop
      );
      return model;
    } catch (Exception e) {

    }
    return null;
  }


}
