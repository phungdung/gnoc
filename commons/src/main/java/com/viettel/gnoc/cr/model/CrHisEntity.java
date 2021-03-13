/**
 * @(#)CrHisBO.java 8/26/2015 4:09 PM, Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CrHisDTO;
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
@Table(schema = "OPEN_PM", name = "CR_HIS")
public class CrHisEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_HIS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CHS_ID", unique = true, nullable = false)
  private Long chsId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "STATUS")
  private Long status;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CHANGE_DATE")
  private Date changeDate;

  @Column(name = "COMMENTS")
  private String comments;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "NOTES")
  private String notes;

  @Column(name = "ACTION_CODE")
  private Long actionCode;

  @Column(name = "EARLIEST_START_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date earliestStartTime;

  @Column(name = "LATEST_END_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date latestStartTime;

  public CrHisEntity(
      Long chsId, Long userId, Long unitId, Long status, Date changeDate, String comments,
      Long crId, String notes, Long actionCode,
      Date earliestStartTime,
      Date latestStartTime) {
    this.chsId = chsId;
    this.userId = userId;
    this.unitId = unitId;
    this.status = status;
    this.changeDate = changeDate;
    this.comments = comments;
    this.crId = crId;
    this.notes = notes;
    this.actionCode = actionCode;
    this.earliestStartTime = earliestStartTime;
    this.latestStartTime = latestStartTime;
  }


  public CrHisDTO toDTO() {
    CrHisDTO dto = new CrHisDTO(
        chsId == null ? null : chsId.toString(),
        userId == null ? null : userId.toString(),
        unitId == null ? null : unitId.toString(),
        status == null ? null : status.toString(),
        changeDate == null ? null
            : DateTimeUtils.convertDateToString(changeDate, DateTimeUtils.patternDateTimeMs),
        comments,
        crId == null ? null : crId.toString(),
        notes,
        actionCode == null ? null : actionCode.toString()
    );
    return dto;
  }
}

