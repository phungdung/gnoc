package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrHisEntity;
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
public class MrHisDTO extends BaseDto {

  private String mhsId;
  private String userId;
  private String unitId;
  private String status;
  private String changeDate;
  private String comments;
  private String unitName;
  private String userName;
  private String mrId;
  private String notes;
  private String actionCode;

  public MrHisDTO(String mhsId, String userId, String unitId, String status, String changeDate,
      String comments, String mrId, String notes, String actionCode) {
    this.mhsId = mhsId;
    this.userId = userId;
    this.unitId = unitId;
    this.status = status;
    this.changeDate = changeDate;
    this.comments = comments;
    this.mrId = mrId;
    this.notes = notes;
    this.actionCode = actionCode;
  }

  public MrHisEntity toEntity() {
    try {
      MrHisEntity model = new MrHisEntity(
          StringUtils.validString(mhsId)
              ? Long.valueOf(mhsId) : null,
          StringUtils.validString(userId)
              ? Long.valueOf(userId) : null,
          StringUtils.validString(unitId)
              ? Long.valueOf(unitId) : null,
          StringUtils.validString(status)
              ? Long.valueOf(status) : null,
          StringUtils.validString(changeDate)
              ? DateTimeUtils.convertStringToDate(changeDate) : null,
          comments,
          StringUtils.validString(mrId)
              ? Long.valueOf(mrId) : null,
          notes,
          StringUtils.validString(actionCode)
              ? Long.valueOf(actionCode) : null);
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
