package com.viettel.gnoc.cr.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrDetailInfoDTO {

  private Long crId;
  private String crNumber;
  private String title;
  private Date createdDate;
  private Date earliestStartTime;
  private Date latestStartTime;
  private String description;
  private Date disturbanceStartTime;
  private Date disturbanceEndTime;
  private Long changeOrginatorUnit;
  private String changeOrginatorUnitName;
  private Long changeOrginator;
  private String changeOrginatorName;
  private Long changeResponsibleUnit;
  private String changeResponsibleUnitName;
  private Long changeResponsible;
  private String changeResponsibleName;
  private String crType;
  private Long impactSegmentId;
  private String impactSegment;
  private Long risk;
  private Long priority;

  //tiennv them nang cap CR
  private Long autoExecute;
  private Long isConfirmAction;
  private Long relateToPrimaryCr;
  private Long relateToPreApprovedCr;

  private List<MiniImpactedNode> impactNodes = new ArrayList<>();
  private List<MiniImpactedNode> affectNodes = new ArrayList<>();
  private List<CrAlarmDTO> alarmList = new ArrayList<>();
  private List<CrAffectedServiceInfo> affectedServiceList = new ArrayList<>();
  private List<CrFilesAttachDTO> filesAttachList = new ArrayList<>();
}
