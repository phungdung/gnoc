package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrSchedulePeriodicEntity;
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
public class MrSchedulePeriodicDTO extends BaseDto {

  private String mrspId;
  private String mrId;
  private String woTempId;
  private String userId;
  private String timeInsert;
  private String timeWoStart;
  private String position;
  private String isSend;
  private String timeSend;
  private String timeWoEnd;
  private String woId;

  public MrSchedulePeriodicEntity toEntity() {
    try {
      MrSchedulePeriodicEntity model = new MrSchedulePeriodicEntity(
          StringUtils.validString(mrspId)
              ? Long.valueOf(mrspId) : null,
          StringUtils.validString(mrId)
              ? Long.valueOf(mrId) : null,
          StringUtils.validString(woTempId)
              ? Long.valueOf(woTempId) : null,
          StringUtils.validString(userId)
              ? Long.valueOf(userId) : null,
          StringUtils.validString(timeInsert)
              ? DateTimeUtils.convertStringToDate(timeInsert) : null,
          StringUtils.validString(timeWoStart)
              ? DateTimeUtils.convertStringToDate(timeWoStart) : null,
          position,
          isSend,
          StringUtils.validString(timeSend)
              ? DateTimeUtils.convertStringToDate(timeSend) : null,
          StringUtils.validString(timeWoEnd)
              ? DateTimeUtils.convertStringToDate(timeWoEnd) : null,
          StringUtils.validString(woId)
              ? Long.valueOf(woId) : null);
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
