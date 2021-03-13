package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrScheduleITHisEntity;
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
public class MrScheduleITHisDTO extends BaseDto {

  //Fields
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
  private String mrState;
  private String crId;
  private String crNumber;
  private String importantLevel;
  private String procedureId;
  private String procedureName;
  private String nodeStatus;
  private String woId;

  private String marketName;
  private String arrayName;
  private String mrDateFrom;
  private String mrDateTo;

  //Bigin Rikkei
  private String networkType;
  private String mrArrayType;
  private String mrTypeName;
  //duongnt
  private String region;
  private String cycle;
  private String title;
  private String earliestTime;
  private String lastestTime;
  private String responsibleUnitCR;
  private String considerUnitCR;
  private String state;
  private String unitName;
  private String considerName;
  private String reponsibleUnitName;
  private String mrComment;
  private String note;
  private Long scheduleId;
  private String nationName;
  private Boolean checkMrCr;
  private String arrayCodeStr;

  public MrScheduleITHisDTO(Long mrDeviceHisId, String marketCode, String arrayCode,
      String deviceType, String deviceId,
      String deviceCode, String deviceName, String mrDate, String mrContent, String mrMode,
      String mrType, String mrId, String mrCode,
      String crId, String crNumber, String importantLevel, String procedureId, String procedureName,
      String networkType, String note, String cycle, String title, String region) {
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
    this.note = note;
    this.cycle = cycle;
    this.title = title;
    this.region = region;
  }

  public MrScheduleITHisEntity toEntity() {
    MrScheduleITHisEntity model = new MrScheduleITHisEntity(
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
        , note
        , StringUtils.isStringNullOrEmpty(cycle) ? null : Long.valueOf(cycle)
        , title
        , region

    );
    return model;
  }
}
