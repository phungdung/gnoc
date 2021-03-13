/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiennv
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class MrScheduleCdExportDTO extends BaseDto {

  private String scheduleId;
  private String marketCode;
  private String marketName;
  private String deviceType;
  private String deviceCdId;
  private String deviceName;
  private String procedureId;
  private String lastDate;
  private String nextDate;
  private String nextDateModify;
  private String station;
  private String stationCode;
  private String updatedDate;
  private String mrId;
  private String modifyUser;
  private String modifyDate;

  private String procedureName;
  private String cycle;
  private String deviceTypeName;

  private String dateModifyFromSearch;
  private String dateModifyToSearch;
  private String userMrHard;
  private String woCode;

  public MrScheduleCdDTO toMrScheduleCdDTO() {
    try {
      MrScheduleCdDTO model = new MrScheduleCdDTO(
          marketCode,
          StringUtils.validString(scheduleId) ? Long.valueOf(scheduleId) : null,
          deviceType,
          StringUtils.validString(deviceCdId) ? Long.valueOf(deviceCdId) : null,
          deviceName,
          StringUtils.validString(procedureId) ? Long.valueOf(procedureId) : null,
          StringUtils.validString(lastDate) ? DateTimeUtils.convertStringToDate(lastDate) : null,
          StringUtils.validString(nextDate) ? DateTimeUtils.convertStringToDate(nextDate) : null,
          StringUtils.validString(nextDateModify) ? DateTimeUtils
              .convertStringToDate(nextDateModify) : null,
          station,
          StringUtils.validString(updatedDate) ? DateTimeUtils.convertStringToDate(updatedDate)
              : null,
          StringUtils.validString(mrId) ? Long.valueOf(mrId) : null,
          modifyUser,
          StringUtils.validString(modifyDate) ? DateTimeUtils.convertStringToDate(modifyDate)
              : null,
          cycle
      );
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
