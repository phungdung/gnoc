package com.viettel.gnoc.cr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrOutputForSOCForm {

  private String affectedService;
  private String crNumber;
  private String title;
  private String description;
  private String nodeName;
  private String networkClass;
  private String notes;
  private String impactService;
  private String startTime;
  private String endTime;
  private String startAffectedTime;
  private String endAffectedTime;
  private String unitExecuteName;
  private String createUserName;
  private String executeUserName;
  private String lastUpdateTime;
}
