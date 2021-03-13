/**
 * @(#)OdBO.java 8/26/2015 10:42 , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.model;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.od.dto.OdDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "OD")
public class OdEntity {

  static final long serialVersionUID = 1L;

  @Id
  @Column(name = "OD_ID", unique = true)
  private Long odId;
  @Column(name = "OD_CODE")
  private String odCode;
  @Column(name = "OD_NAME")
  private String odName;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_TIME")
  private Date createTime;
  @Column(name = "DESCRIPTION")
  private String description;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;
  @Column(name = "OD_TYPE_ID")
  private Long odTypeId;
  @Column(name = "GROUP_TYPE_ID")
  private Long groupTypeId;
  @Column(name = "PRIORITY_ID")
  private Long priorityId;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "START_TIME")
  private Date startTime;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "END_TIME")
  private Date endTime;
  @Column(name = "RECEIVE_USER_ID")
  private Long receiveUserId;
  @Column(name = "RECEIVE_UNIT_ID")
  private Long receiveUnitId;
  @Column(name = "OTHER_SYSTEM_CODE")
  private String otherSystemCode;
  @Column(name = "PLAN_CODE")
  private String planCode;
  @Column(name = "INSERT_SOURCE")
  private String insertSource;
  @Column(name = "STATUS")
  private Long status;
  @Column(name = "CREATE_PERSON_ID")
  private Long createPersonId;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CLOSE_TIME")
  private Date closeTime;
  @Column(name = "CLOSE_CODE_ID")
  private Long closeCodeId;
  @Column(name = "CLEAR_CODE_ID")
  private Long clearCodeId;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "END_PENDING_TIME")
  private Date endPendingTime;
  @Column(name = "NUM_PENDING")
  private Long numPending;
  @Column(name = "VOFFICE_TRANS_CODE")
  private String vofficeTransCode;
  @Column(name = "WO_ID")
  private Long woId;
  @Column(name = "REASON_GROUP")
  private Long reasonGroup;
  @Column(name = "REASON_DETAIL")
  private String reasonDetail;
  @Column(name = "SOLUTION_GROUP")
  private Long solutionGroup;
  @Column(name = "SOLUTION_DETAIL")
  private String solutionDetail;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "SOLUTION_COMPLETE_TIME")
  private Date solutionCompleteTime;
  @Column(name = "CREATE_UNIT_ID")
  private Long createUnitId;
  @Column(name = "APPROVER_ID")
  private Long approverId;
  @Column(name= "FINISHED_TIME")
  private Date finishedTime;
  @Column(name = "REASON_PAUSE")
  private String reasonPause;

  public OdEntity(Long odId, String odCode, String odName, Date createTime, String description,
      Date lastUpdateTime,
      Long odTypeId, Long groupTypeId,
      Long priorityId, Date startTime, Date endTime, Long receiveUserId, Long receiveUnitId,
      String otherSystemCode, String planCode,
      String insertSource,
      Long status, Long createPersonId, Date closeTime, Long closeCodeId, Long clearCodeId,
      Date endPendingTime, Long numPending,
      String vofficeTransCode, Long woId, Long reasonGroup, String reasonDetail, Long solutionGroup, String solutionDetail
      , Date solutionCompleteTime, Long createUnitId, Long approverId, Date finishedTime, String reasonPause) {
    this.odId = odId;
    this.odCode = odCode;
    this.odName = odName;
    this.createTime = createTime;
    this.description = description;
    this.lastUpdateTime = lastUpdateTime;
    this.odTypeId = odTypeId;
    this.groupTypeId = groupTypeId;
    this.priorityId = priorityId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.receiveUserId = receiveUserId;
    this.receiveUnitId = receiveUnitId;
    this.otherSystemCode = otherSystemCode;
    this.otherSystemCode = otherSystemCode;
    this.planCode = planCode;
    this.insertSource = insertSource;
    this.status = status;
    this.createPersonId = createPersonId;
    this.closeTime = closeTime;
    this.closeCodeId = closeCodeId;
    this.clearCodeId = clearCodeId;
    this.endPendingTime = endPendingTime;
    this.numPending = numPending;
    this.vofficeTransCode = vofficeTransCode;
    this.woId = woId;
    this.reasonGroup = reasonGroup;
    this.reasonDetail = reasonDetail;
    this.solutionGroup = solutionGroup;
    this.solutionDetail = solutionDetail;
    this.solutionCompleteTime = solutionCompleteTime;
    this.createUnitId = createUnitId;
    this.approverId = approverId;
    this.finishedTime = finishedTime;
    this.reasonPause = reasonPause;
  }

  public OdDTO toDTO() {
    return new OdDTO(odId, odCode, odName, createTime, description, lastUpdateTime, odTypeId,
        groupTypeId,
        priorityId, startTime, endTime, receiveUserId, receiveUnitId, otherSystemCode, planCode,
        insertSource,
        status, createPersonId, closeTime, closeCodeId, clearCodeId, endPendingTime, numPending,
        vofficeTransCode, woId, this.reasonGroup, this.reasonDetail, this.solutionGroup,
        this.solutionDetail, this.solutionCompleteTime, this.createUnitId, this.approverId,
        finishedTime != null ? DateTimeUtils.convertDateToString(finishedTime) : null, reasonPause);
  }


}
