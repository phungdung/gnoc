package com.viettel.gnoc.wo.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportCaseForm {

  private String cfgSupportCaseID;
  private String caseName;
  private String serviceID;
  private String serviceName;
  private String infraTypeID;
  private String infraTypeName;
  private String status;
  private List<SupportCaseTestForm> lstSuportCaseTest;

}
