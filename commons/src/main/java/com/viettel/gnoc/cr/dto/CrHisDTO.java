/**
 * @(#)CrHisForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.model.CrHisEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class CrHisDTO extends BaseDto {

  //Fields
  private String chsId;
  private String userId;
  private String unitId;
  private String status;
  private String changeDate;
  private String comments;
  private String unitName;
  private String userName;
  private String crId;
  private String notes;
  private String actionCode;
  private String returnCodeId;
  private String returnTitle;
  private String earliestStartTime;
  private String latestStartTime;

  // nguyen nhan khi resolve CR
  private String reasonTitle;
  private String returnCode;
  private String actionName;
  private String statusName;
  //tiennv them cho MR
  private String latestEndTime;
  // private static long changedTime = 0;
  //Constructor
  String defaultSortField = "name";

  public CrHisDTO(String chsId, String userId, String unitId, String status, String changeDate,
      String comments, String crId, String notes, String actionCode) {
    this.chsId = chsId;
    this.userId = userId;
    this.unitId = unitId;
    this.status = status;
    this.changeDate = changeDate;
    this.comments = comments;
    this.crId = crId;
    this.notes = notes;
    this.actionCode = actionCode;
    setDefaultSortField("userId");
  }

  public CrHisEntity toEntity() {
    CrHisEntity model = new CrHisEntity(
        StringUtils.validString(chsId)
            ? Long.parseLong(chsId) : null,
        StringUtils.validString(userId)
            ? Long.parseLong(userId) : null,
        StringUtils.validString(unitId)
            ? Long.parseLong(unitId) : null,
        StringUtils.validString(status)
            ? Long.parseLong(status) : null,
        StringUtils.validString(changeDate)
            ? DateTimeUtils.convertStringToDate(changeDate) : null,
        comments,
        StringUtils.validString(crId)
            ? Long.parseLong(crId) : null,
        notes,
        StringUtils.validString(actionCode)
            ? Long.parseLong(actionCode) : null,
        StringUtils.validString(earliestStartTime)
            ? DateTimeUtils.convertStringToDate(earliestStartTime) : null,
        StringUtils.validString(latestStartTime)
            ? DateTimeUtils.convertStringToDate(latestStartTime) : null);
    return model;
  }

}
