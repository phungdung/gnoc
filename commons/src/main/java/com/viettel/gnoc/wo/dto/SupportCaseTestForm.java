package com.viettel.gnoc.wo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportCaseTestForm {

  private String id;
  private String cfgSuppportCaseId;
  private String testCaseName;
  private String fileRequired;
  private String description;
  private String result;
  private byte[] fileArr;
  private String fileName;

}
