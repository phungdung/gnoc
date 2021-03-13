/**
 * @(#)CrApprovalDepartmentBO.java 8/24/2015 2:28 PM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CR_APPROVAL_DEPARTMENT")
public class CrApprovalDepartmentEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_APPROVAL_DEPARTMENT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CADT_ID", nullable = false)
  private Long cadtId;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "CADT_LEVEL")
  private Long cadtLevel;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "USER_ID")
  private Long userId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "INCOMMING_DATE")
  private Date incommingDate;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "APPROVED_DATE")
  private Date approvedDate;

  @Column(name = "NOTES")
  private String notes;

  @Column(name = "RETURN_CODE")
  private Long returnCode;

  public CrApprovalDepartmentEntity(
      Long cadtId, Long crId, Long unitId, Long cadtLevel, Long status, Long userId,
      Date incommingDate, Date approvedDate, String notes, Long returnCode) {
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
  }

  public CrApprovalDepartmentInsiteDTO toDTO() {
    CrApprovalDepartmentInsiteDTO dto = new CrApprovalDepartmentInsiteDTO(
        cadtId == null ? null : cadtId.toString(),
        crId == null ? null : crId.toString(),
        unitId == null ? null : unitId.toString(),
        cadtLevel == null ? null : cadtLevel.toString(),
        status == null ? null : status.toString(),
        userId == null ? null : userId.toString(),
        incommingDate == null ? null : DateTimeUtils
            .convertDateToString(incommingDate, DateTimeUtils.patternDateTimeMs),
        approvedDate == null ? null : DateTimeUtils.
            convertDateToString(approvedDate, DateTimeUtils.patternDateTimeMs),
        notes,
        returnCode == null ? null : returnCode.toString()
    );
    return dto;
  }

  public CrApprovalDepartmentDTO toOutSideDTO() {
    CrApprovalDepartmentDTO dto = new CrApprovalDepartmentDTO(
        cadtId == null ? null : cadtId.toString(),
        crId == null ? null : crId.toString(),
        unitId == null ? null : unitId.toString(),
        cadtLevel == null ? null : cadtLevel.toString(),
        status == null ? null : status.toString(),
        userId == null ? null : userId.toString(),
        incommingDate == null ? null
            : DateTimeUtils.convertDateToString(incommingDate, DateTimeUtils.patternDateTimeMs),
        approvedDate == null ? null
            : DateTimeUtils.convertDateToString(approvedDate, DateTimeUtils.patternDateTimeMs),
        notes,
        returnCode == null ? null : returnCode.toString()
    );
    return dto;
  }
}
