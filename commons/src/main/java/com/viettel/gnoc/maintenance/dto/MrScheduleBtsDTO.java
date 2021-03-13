package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrScheduleBtsEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiennv
 * @version 2.0
 * @since 07/01/2020 04:59:00
 */

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class MrScheduleBtsDTO extends BaseDto {

  //Fields
  private String scheduleId;
  private String marketCode;
  private String areaCode;
  private String stationCode;
  private String deviceType;
  private String serial;
  private String cycle;
  private String mrCode;
  private String woCode;
  private String modifyDate;
  private String nextDateModify;
  private String userManager;
  private String provinceCode;
  private String dateModifyFromSearch;
  private String dateModifyToSearch;
  private String marketName;
  private String provinceName;
  private String deviceTypeName;
  private String woStatus;
  private String woStatusName;

  private String isWoOriginal;
  private String woCodeOriginal;

  public MrScheduleBtsDTO(String scheduleId, String marketCode, String areaCode, String stationCode,
      String deviceType, String serial, String cycle, String mrCode, String woCode,
      String modifyDate, String nextDateModify, String userManager, String provinceCode) {
    this.scheduleId = scheduleId;
    this.marketCode = marketCode;
    this.areaCode = areaCode;
    this.stationCode = stationCode;
    this.deviceType = deviceType;
    this.serial = serial;
    this.cycle = cycle;
    this.mrCode = mrCode;
    this.woCode = woCode;
    this.modifyDate = modifyDate;
    this.nextDateModify = nextDateModify;
    this.userManager = userManager;
    this.provinceCode = provinceCode;
  }

  public MrScheduleBtsEntity toEntity() {
    try {
      MrScheduleBtsEntity model = new MrScheduleBtsEntity(
          !StringUtils.validString(scheduleId) ? null : Long.valueOf(scheduleId),
          marketCode,
          areaCode,
          stationCode,
          deviceType,
          serial,
          cycle,
          mrCode,
          woCode,
          !StringUtils.validString(modifyDate) ? null
              : DateTimeUtils.convertStringToDate(modifyDate),
          !StringUtils.validString(nextDateModify) ? null
              : DateTimeUtils.convertStringToDate(nextDateModify),
          userManager,
          provinceCode
      );
      return model;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

}
