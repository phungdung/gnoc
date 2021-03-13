package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrScheduleCdHisEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class MrScheduleCdHisDTO extends BaseDto {

  private String scheduleCdHisId;
  private String deviceType;
  private String deviceCdId;
  private String deviceName;
  private String mrDate;
  private String mrContent;
  private String mrMode;
  private String mrType;
  private String mrId;
  private String mrCode;
  private String importantLevel;
  private String procedureId;
  private String procedureName;
  private String stationCode;
  private String cycle;
  private String woCode;
  private String marketCode;
  private String userMrHard;
  private String mrDateFrom;
  private String mrDateTo;
  private String marketName;

  public MrScheduleCdHisDTO(
      String scheduleCdHisId,
      String deviceType,
      String deviceCdId,
      String deviceName,
      String mrDate,
      String mrContent,
      String mrMode,
      String mrType,
      String mrId,
      String mrCode,
      String importantLevel,
      String procedureId,
      String procedureName,
      String marketCode,
      String cycle
  ) {
    this.scheduleCdHisId = scheduleCdHisId;
    this.deviceType = deviceType;
    this.deviceCdId = deviceCdId;
    this.deviceName = deviceName;
    this.mrDate = mrDate;
    this.mrContent = mrContent;
    this.mrMode = mrMode;
    this.mrType = mrType;
    this.mrId = mrId;
    this.mrCode = mrCode;
    this.importantLevel = importantLevel;
    this.procedureId = procedureId;
    this.procedureName = procedureName;
    this.marketCode = marketCode;
    this.cycle = cycle;
  }

  public MrScheduleCdHisEntity toEntity() {
    try {
      MrScheduleCdHisEntity model = new MrScheduleCdHisEntity(
          StringUtils.validString(scheduleCdHisId) ? Long.valueOf(scheduleCdHisId) : null,
          deviceType,
          StringUtils.validString(deviceCdId) ? Long.valueOf(deviceCdId) : null,
          deviceName,
          StringUtils.validString(mrDate) ? DateTimeUtils.convertStringToDate(mrDate) : null,
          mrContent,
          mrMode,
          mrType,
          StringUtils.validString(mrId) ? Long.valueOf(mrId) : null,
          mrCode,
          importantLevel,
          procedureId,
          procedureName,
          marketCode,
          StringUtils.validString(cycle) ? Long.valueOf(cycle) : null
      );
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
