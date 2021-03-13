package com.viettel.gnoc.od.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OdCfgScheduleCreateExportDTO {

  private Long id;
  private String odName;
  private String odDescription;
  private String odTypeId;
  private String odTypeName;
  private String odGroupTypeName;
  private String odPriority;
  private String odPriorityName;
  private String schedule;
  private String scheduleName;
  private String receiveUnit;
  private String receiveUnitName;
  private String odFileId;
  private String resultImport;
  private String action;
}
