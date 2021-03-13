package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.model.WorkTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class WorkTimeDTO {

  //Fields
  private String id;
  private String woId;
  private String userId;
  private String content;
  private String insertTime;
  private String longtitude;
  private String latitude;
  private String isActive;
  private String updateTime;

  public WorkTimeDTO(String id, String woId, String userId,
      String content, String insertTime, String longtitude,
      String latitude, String isActive, String updateTime
  ) {
    this.id = id;
    this.woId = woId;
    this.userId = userId;
    this.content = content;
    this.insertTime = insertTime;
    this.longtitude = longtitude;
    this.latitude = latitude;
    this.isActive = isActive;
    this.updateTime = updateTime;
  }

  public WorkTimeEntity toEntity() {
    try {
      WorkTimeEntity model = new WorkTimeEntity(
          !StringUtils.validString(id) ? null : Long.valueOf(id),
          !StringUtils.validString(woId) ? null : Long.valueOf(woId),
          !StringUtils.validString(userId) ? null : Long.valueOf(userId),
          content,
          !StringUtils.validString(insertTime) ? null
              : DateTimeUtils.convertStringToDate(insertTime),
          longtitude,
          latitude,
          !StringUtils.validString(isActive) ? null : Long.valueOf(isActive),
          !StringUtils.validString(updateTime) ? null
              : DateTimeUtils.convertStringToDate(updateTime)
      );
      return model;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }

}
