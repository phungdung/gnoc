package com.viettel.gnoc.maintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MrHisSearchDTO {

  private String mhsId;
  private String userId;
  private String unitId;
  private String unitCode;
  private String status;
  private String changeDate;
  private String comments;
  private String unitName;
  private String userName;
  private String mrId;
  private String notes;
  private String actionCode;
}
