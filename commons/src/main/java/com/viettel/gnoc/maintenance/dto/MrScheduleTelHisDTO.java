package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrScheduleTelHisEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author trungduong
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class MrScheduleTelHisDTO extends BaseDto {

  private Long mrDeviceHisId;
  private String marketCode;
  private String arrayCode;
  private String deviceType;
  private String deviceId;
  private String deviceCode;
  private String deviceName;
  private String mrDate;
  private String mrContent;
  private String mrMode;
  private String mrType;
  private String mrId;
  private String mrCode;
  private String crId;
  private String crNumber;
  private String importantLevel;
  private String procedureId;
  private String procedureName;
  private String networkType;
  private String cycle;
  private String region;
  private String title;
  private String note;

  private String nodeStatus;
  private String woId;
  private String marketName;
  private String arrayName;
  private String mrDateFrom;
  private String mrDateTo;
  private String mrArrayType;
  private String mrTypeName;
  private String earliestTime;
  private String lastestTime;
  private String responsibleUnitCR;
  private String considerUnitCR;
  private String state;
  private String unitName;
  private String considerName;
  private String reponsibleUnitName;
  private String mrComment;
  private String mrModeName;
  private String nodeStatusStr;
  private String mrWO;
  private String unitCreateMr;
  private Long scheduleId;
  private String mrState;

  private Boolean checkMrCr;

  public MrScheduleTelHisDTO(Long mrDeviceHisId, String marketCode, String arrayCode,
      String deviceType, String deviceId, String deviceCode, String deviceName,
      String mrDate, String mrContent, String mrMode, String mrType, String mrId,
      String mrCode, String crId, String crNumber, String importantLevel,
      String procedureId, String procedureName, String networkType, String cycle,
      String region, String title, String note) {
    this.mrDeviceHisId = mrDeviceHisId;
    this.marketCode = marketCode;
    this.arrayCode = arrayCode;
    this.deviceType = deviceType;
    this.deviceId = deviceId;
    this.deviceCode = deviceCode;
    this.deviceName = deviceName;
    this.mrDate = mrDate;
    this.mrContent = mrContent;
    this.mrMode = mrMode;
    this.mrType = mrType;
    this.mrId = mrId;
    this.mrCode = mrCode;
    this.crId = crId;
    this.crNumber = crNumber;
    this.importantLevel = importantLevel;
    this.procedureId = procedureId;
    this.procedureName = procedureName;
    this.networkType = networkType;
    this.cycle = cycle;
    this.region = region;
    this.title = title;
    this.note = note;
  }

  public MrScheduleTelHisEntity toEntity() {
    MrScheduleTelHisEntity model = new MrScheduleTelHisEntity(
        mrDeviceHisId
        , marketCode
        , arrayCode
        , deviceType
        , deviceId
        , deviceCode
        , deviceName
        , StringUtils.isStringNullOrEmpty(mrDate) ? null : DateTimeUtils.convertStringToDate(mrDate)
        , mrContent
        , mrMode
        , mrType
        , StringUtils.isStringNullOrEmpty(mrId) ? null : Long.valueOf(mrId)
        , mrCode
        , StringUtils.isStringNullOrEmpty(crId) ? null : Long.valueOf(crId)
        , crNumber
        , importantLevel
        , procedureId
        , procedureName
        , networkType
        , StringUtils.isStringNullOrEmpty(cycle) ? null : Long.valueOf(cycle)
        , region
        , title
        , note
    );
    return model;
  }
}
