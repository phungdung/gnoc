package com.viettel.gnoc.cr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrForNocProDTO {

  private Long crId;
  private String crNumber;
  private String title;
  private String activeUser;
  private boolean nodeFromImpactList = false;
  private boolean nodeFromAffectList = false;
  private String startActiveDate;
  private String endActiveDate;
}
