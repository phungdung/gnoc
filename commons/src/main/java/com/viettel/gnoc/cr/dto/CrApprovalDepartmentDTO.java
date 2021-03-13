/**
 * @(#)CrApprovalDepartmentForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */

package com.viettel.gnoc.cr.dto;


import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.model.CrApprovalDepartmentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ITSOL
 */

@Getter
@Setter
@AllArgsConstructor
public class CrApprovalDepartmentDTO {

  //Fields
  private String defaultSortField;
  private String cadtId;
  private String crId;
  private String unitId;
  private String cadtLevel;
  private String status;
  private String userId;
  private String incommingDate;
  private String approvedDate;
  private String notes;
  private String returnCode;
  private String creatorId;
  private String unitName;
  private String userName;


  public CrApprovalDepartmentDTO() {
    setDefaultSortField("approvedDate");
  }

  public CrApprovalDepartmentDTO(String cadtId, String crId, String unitId, String cadtLevel,
      String status, String userId, String incommingDate, String approvedDate, String notes,
      String returnCode) {
    this.cadtId = cadtId;
    this.crId = crId;
    this.unitId = unitId;
    this.cadtLevel = cadtLevel;
    this.status = status;
    this.userId = userId;
    this.incommingDate = incommingDate;
    this.approvedDate = approvedDate;
    this.notes = notes;
    this.returnCode = returnCode;
    setDefaultSortField("approvedDate");
  }

  public CrApprovalDepartmentInsiteDTO toInsiteDTO() {
    CrApprovalDepartmentInsiteDTO crApprovalDepartmentInsiteDTO = new CrApprovalDepartmentInsiteDTO(
        this.defaultSortField,
        this.cadtId,
        this.crId,
        this.unitId,
        this.cadtLevel,
        this.status,
        this.userId,
        this.incommingDate,
        this.approvedDate,
        this.notes,
        this.returnCode,
        this.creatorId,
        this.unitName,
        this.userName,
        null,
        null
    );
    return crApprovalDepartmentInsiteDTO;
  }

  public CrApprovalDepartmentEntity toEntity() {
    try {
      CrApprovalDepartmentEntity model = new CrApprovalDepartmentEntity(
          StringUtils.validString(cadtId) ?
              Long.valueOf(cadtId) : null,
          StringUtils.validString(crId) ?
              Long.valueOf(crId) : null,
          StringUtils.validString(unitId) ?
              Long.valueOf(unitId) : null,
          StringUtils.validString(cadtLevel) ?
              Long.valueOf(cadtLevel) : null,
          StringUtils.validString(status) ?
              Long.valueOf(status) : null,
          StringUtils.validString(userId) ?
              Long.valueOf(userId) : null,
          StringUtils.validString(incommingDate) ?
              DateTimeUtils.convertStringToDate(incommingDate) : null,
          StringUtils.validString(approvedDate) ?
              DateTimeUtils.convertStringToDate(approvedDate) : null,
          notes,
          StringUtils.validString(returnCode) ?
              Long.valueOf(returnCode) : null);
      return model;
    } catch (Exception e) {
      return null;
    }
  }
}

