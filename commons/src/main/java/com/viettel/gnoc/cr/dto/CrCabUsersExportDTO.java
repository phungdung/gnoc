package com.viettel.gnoc.cr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrCabUsersExportDTO {

  private Long crCabUsersId;
  private Long impactSegmentId;
  private Long executeUnitId;
  private Long cabUnitId;
  private Long userID;
  private Long creationUnitId;
  private String segmentName;
  private String cabUnitName;
  private String executeUnitName;
  private String userFullName;
  private String assignMonth;
  private String assignYear;
  private String countCR;
  private String creationUnitName;
  private String resultImport;
  private String action;

}
