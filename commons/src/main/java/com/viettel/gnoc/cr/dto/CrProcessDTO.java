/**
 * 1
 *
 * @(#)CrProcessForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@AllArgsConstructor
public class CrProcessDTO {

  //Fields
  private String crProcessId;
  private String crProcessCode;
  private String crProcessName;
  private String description;
  private String impactSegmentId;
  private String deviceTypeId;
  private String subcategoryId;
  private String riskLevel;
  private String impactType;
  private String crTypeId;
  private String isActive;
  private String parentId;
  private String impactCharacteristic;

  private String otherDept;

  private String requireMop;
  private String requireFileLog;

  private String approvalLever;
  private String closeCrWhenResolveSuccess;
  private String isVMSAActiveCellProcess;
  private String vMSAActiveCellProcessKey;
  private String isLaneImpact;
  private String defaultSortField;

  // private static long changedTime = 0;
  //Constructor
  public CrProcessDTO() {
    setDefaultSortField("crProcessCode");
  }

  public CrProcessDTO(String crProcessId, String crProcessCode, String crProcessName,
      String description,
      String impactSegmentId, String deviceTypeId, String subcategoryId, String riskLevel,
      String impactType, String crTypeId, String isActive, String parentId,
      String impactCharacteristic,
      String otherDept, String requireMop, String requireFileLog, String approvalLever,
      String closeCrWhenResolveSuccess,
      String isVMSAActiveCellProcess, String vMSAActiveCellProcessKey, String isLaneImpact) {
    this.crProcessId = crProcessId;
    this.crProcessCode = crProcessCode;
    this.crProcessName = crProcessName;
    this.description = description;
    this.impactSegmentId = impactSegmentId;
    this.deviceTypeId = deviceTypeId;
    this.subcategoryId = subcategoryId;
    this.riskLevel = riskLevel;
    this.impactType = impactType;
    this.crTypeId = crTypeId;
    this.isActive = isActive;
    this.parentId = parentId;
    this.impactCharacteristic = impactCharacteristic;
    this.otherDept = otherDept;
    this.requireMop = requireMop;
    this.requireFileLog = requireFileLog;
    this.approvalLever = approvalLever;
    this.closeCrWhenResolveSuccess = closeCrWhenResolveSuccess;
    this.isVMSAActiveCellProcess = isVMSAActiveCellProcess;
    this.vMSAActiveCellProcessKey = vMSAActiveCellProcessKey;
    this.isLaneImpact = isLaneImpact;

  }
}
