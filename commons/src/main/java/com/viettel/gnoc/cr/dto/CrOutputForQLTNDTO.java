package com.viettel.gnoc.cr.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrOutputForQLTNDTO {

  String crNumber;
  String state;
  String createUserCode;//user tao
  String createUser;//user tao
  String impactStartTime;
  String impactEndTime;
  String userConsiderCode;//user tham dinh
  String userConsider;//user tham dinh
  String userExecuteCode;//user thuc hien
  String userExecute;//user thuc hien
  String woCode;
  String ftName;
  List<String> lstWoCode;
  List<String> lstFtName;
  String resultCode;
  String description;
}
