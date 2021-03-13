package com.viettel.gnoc.risk.model;

import com.viettel.gnoc.risk.dto.RiskDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "RISK")
public class RiskEntity {

  @Id
//  @SequenceGenerator(name = "generator", sequenceName = "RISK_SEQ", allocationSize = 1)
//  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "RISK_ID", unique = true, nullable = false)
  private Long riskId;

  @Column(name = "RISK_CODE", nullable = false)
  private String riskCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_TIME", nullable = false)
  private Date createTime;

  @Column(name = "CREATE_USER_ID")
  private Long createUserId;

  @Column(name = "CREATE_UNIT_ID")
  private Long createUnitId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  @Column(name = "SYSTEM_ID")
  private Long systemId;

  @Column(name = "ARR_ID")
  private Long arrId;

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

  @Column(name = "RISK_TYPE_ID")
  private Long riskTypeId;

  @Column(name = "GROUP_UNIT_ID")
  private Long groupUnitId;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "SUBJECT_ID")
  private Long subjectId;

  @Column(name = "EFFECT")
  private Long effect;

  @Column(name = "EFFECT_DETAIL")
  private String effectDetail;

  @Column(name = "FREQUENCY")
  private Long frequency;

  @Column(name = "SOLUTION")
  private String solution;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "FINISH_TIME")
  private Date finishTime;

  @Column(name = "RESULT")
  private Long result;

  @Column(name = "REDUNDANCY")
  private Long redundancy;

  @Column(name = "EVEDENCE")
  private String evedence;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CLOSE_TIME")
  private Date closedTime;

  @Column(name = "INSERT_SOURCE")
  private String insertSource;

  @Column(name = "OTHER_SYSTEM_CODE")
  private String otherSystemCode;

  @Column(name = "RISK_NAME")
  private String riskName;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_SEND_SMS_GOING_OVERDUE")
  private Date lastSendSmsGoingOverdue;

  @Column(name = "IS_EXTERNAL_VTNET")
  private Long isExternalVtnet;

  @Column(name = "FREQUENCY_DETAIL")
  private String frequencyDetail;

  @Column(name = "REASON_ACCEPT")
  private String reasonAccept;

  @Column(name = "REASON_REJECT")
  private String reasonReject;

  @Column(name = "REASON_CANCEL")
  private String reasonCancel;

  @Column(name = "SUGGEST_SOLUTION")
  private String suggestSolution;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CLOSED_DATE")
  private Date closedDate;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "ACCEPTED_DATE")
  private Date acceptedDate;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CANCELED_DATE")
  private Date canceledDate;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "RECEIVED_DATE")
  private Date receivedDate;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "OPENED_DATE")
  private Date openedDate;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "REJECTED_DATE")
  private Date rejectedDate;

  @Column(name = "LOG_TIME")
  private String logTime;

  @Column(name = "RESULT_PROCESSING")
  private String resultProcessing;

  @Column(name = "RISK_COMMENT")
  private String riskComment;

  @Column(name = "CHANGE_REASON")
  private String changeReason;

  @Column(name = "IS_SEND_SMS_OVERDUE")
  private Long isSendSmsOverdue;

  public RiskDTO toDTO() {
    return new RiskDTO(riskId, riskCode, description, createTime, createUserId, createUnitId,
        lastUpdateTime, systemId, arrId, priorityId, startTime, endTime, receiveUserId,
        receiveUnitId, riskTypeId, groupUnitId, status, subjectId, effect, effectDetail, frequency,
        solution, finishTime, result, redundancy, evedence, closedTime, insertSource,
        otherSystemCode, riskName, lastSendSmsGoingOverdue, isExternalVtnet, frequencyDetail,
        reasonAccept, reasonReject, reasonCancel, suggestSolution, closedDate, acceptedDate,
        canceledDate, receivedDate, openedDate, rejectedDate, logTime, resultProcessing,
        riskComment, changeReason, isSendSmsOverdue);
  }

}
