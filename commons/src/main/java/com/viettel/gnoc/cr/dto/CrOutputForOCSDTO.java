package com.viettel.gnoc.cr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrOutputForOCSDTO {

  String crNumber;
  String state;
  String impactStartTime;
  String impactEndTime;
  String userExecuteCode;//user thuc hien
  String userExecute;//user thuc hien
  String description;
  String resultCode;

}
