package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrApprovalDepartmentEntity;
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
public class MrApprovalDepartmentDTO extends BaseDto {

  //Fields
  private String madtId;
  private String mrId;
  private String unitId;
  private String madtLevel;
  private String status;
  private String userId;
  private String incommingDate;
  private String approvedDate;
  private String notes;
  private String returnCode;

  public MrApprovalDepartmentEntity toEntiy() {
    try {
      MrApprovalDepartmentEntity model = new MrApprovalDepartmentEntity(
          StringUtils.validString(madtId) ?
              Long.valueOf(madtId) : null,
          StringUtils.validString(mrId) ?
              Long.valueOf(mrId) : null,
          StringUtils.validString(unitId) ?
              Long.valueOf(unitId) : null,
          StringUtils.validString(madtLevel) ?
              Long.valueOf(madtLevel) : null,
          StringUtils.validString(status) ?
              Long.valueOf(status) : null,
          StringUtils.validString(userId) ?
              Long.valueOf(userId) : null,
          StringUtils.validString(incommingDate) ?
              DateTimeUtils.convertStringToDate(incommingDate) : null,
          StringUtils.validString(approvedDate) ?
              DateTimeUtils.convertStringToDate(approvedDate) : null,
          notes,
          StringUtils.validString(returnCode) ?
              Long.valueOf(returnCode) : null);
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
