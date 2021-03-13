package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
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
public class MrApproveSearchDTO extends BaseDto {

  private String madtId;
  private String mrId;
  private String unitId;
  private String madtLevel;
  private String status;
  private String userId;
  private String userName;
  private String unitName;
  private String unitCode;
  private String incommingDate;
  private String approvedDate;
  private String notes;
  private String returnCode;
}
