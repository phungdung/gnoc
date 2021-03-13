package com.viettel.gnoc.wo.model;

import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.Wo;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
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
@Table(schema = "WFM", name = "WO")
public class WoEntity {

//  static final long serialVersionUID = 1L;

  @Id
//  @SequenceGenerator(name = "generator", sequenceName = "WO_SEQ", allocationSize = 1)
//  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_ID", unique = true, nullable = false)
  private Long woId;

  @Column(name = "PARENT_ID")
  private Long parentId;

  @Column(name = "WO_CODE", nullable = false)
  private String woCode;

  @Column(name = "WO_CONTENT", nullable = false)
  private String woContent;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_DATE", nullable = false)
  private Date createDate;

  @Column(name = "WO_SYSTEM", nullable = false)
  private String woSystem;

  @Column(name = "WO_SYSTEM_ID")
  private String woSystemId;

  @Column(name = "WO_TYPE_ID")
  private Long woTypeId;

  @Column(name = "CREATE_PERSON_ID")
  private Long createPersonId;

  @Column(name = "CD_ID")
  private Long cdId;

  @Column(name = "FT_ID")
  private Long ftId;

  @Column(name = "STATUS", nullable = false)
  private Long status;

  @Column(name = "PRIORITY_ID")
  private Long priorityId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "START_TIME", nullable = false)
  private Date startTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "END_TIME", nullable = false)
  private Date endTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "FINISH_TIME")
  private Date finishTime;

  @Column(name = "RESULT")
  private Long result;

  @Column(name = "STATION_ID")
  private Long stationId;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "WO_DESCRIPTION")
  private String woDescription;

  @Column(name = "SUMMARY_STATUS")
  private Long summaryStatus;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CD_RECEIVED_ALARM_TIME")
  private Date cdReceivedAlarmTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CD_RECEIVED_THRESHOLD_TIME")
  private Date cdReceivedThresholdTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "FT_RECEIVED_ALARM_TIME")
  private Date ftReceivedAlarmTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "FT_RECEIVED_THRESHOLD_TIME")
  private Date ftReceivedThresholdTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "FT_PROCESS_ALARM_TIME")
  private Date ftProcessAlarmTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "FT_PROCESS_THRESHOLD_TIME")
  private Date ftProcessThresholdTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "SUMMARY_TIME")
  private Date summaryTime;

  @Column(name = "WO_SYSTEM_OUT_ID")
  private String woSystemOutId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "WO_WORKLOG_CONTENT")
  private String woWorklogContent;

  @Column(name = "NUM_PENDING")
  private Long numPending;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "END_PENDING_TIME")
  private Date endPendingTime;

  @Column(name = "REASON_OVERDUE_LV1_ID")
  private String reasonOverdueLV1Id;

  @Column(name = "REASON_OVERDUE_LV1_NAME")
  private String reasonOverdueLV1Name;

  @Column(name = "REASON_OVERDUE_LV2_ID")
  private String reasonOverdueLV2Id;

  @Column(name = "REASON_OVERDUE_LV2_NAME")
  private String reasonOverdueLV2Name;

  @Column(name = "PROCESSING_GUIDELINES")
  private String processingGuidelines;

  @Column(name = "COMMENT_COMPLETE")
  private String commentComplete;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "COMPLETED_TIME")
  private Date completedTime;

  @Column(name = "IS_COMPLETED_ON_VSMART")
  private Long isCompletedOnVsmart;

  @Column(name = "IS_CALL")
  private Long isCall;

  @Column(name = "LINE_CODE")
  private String lineCode;

  @Column(name = "SOLUTION_GROUP_ID")
  private Long solutionGroupId;

  @Column(name = "SOLUTION_GROUP_NAME")
  private String solutionGroupName;

  @Column(name = "SOLUTION_DETAIL")
  private String solutionDetail;

  @Column(name = "CELL_SERVICE")
  private String cellService;

  @Column(name = "CONCAVE_AREA_CODE")
  private String concaveAreaCode;

  @Column(name = "LONGITUDE")
  private String longitude;

  @Column(name = "LATITUDE")
  private String latitude;

  @Column(name = "LOCATION_CODE")
  private String locationCode;

  @Column(name = "REASON_DETAIL")
  private String reasonDetail;

  @Column(name = "WAREHOUSE_CODE")
  private String warehouseCode;

  @Column(name = "NEED_SUPPORT")
  private Long needSupport;

  @Column(name = "CONSTRUCTION_CODE")
  private String constructionCode;

  @Column(name = "CD_ASSIGN_ID")
  private Long cdAssignId;

  @Column(name = "DELTA_CLOSE_WO")
  private Double deltaCloseWo;

  @Column(name = "CONFIRM_NOT_CREATE_ALARM")
  private Long confirmNotCreateAlarm;

  @Column(name = "ALIAS")
  private String alias;

  @Column(name = "DEVICE_CODE")
  private String deviceCode;

  @Column(name = "NATION_CODE")
  private String nationCode;

  @Column(name = "SYNC_STATE")
  private Long syncState;

  @Column(name = "STATION_CODE_NIMS")
  private String stationCodeNims;

  @Column(name = "REASON_APPROVE_NOK")
  private String reasonApproveNok;

  @Column(name = "PLAN_CODE")
  private String planCode;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_TIME_SMS_OVERDUE")
  private Date lastTimeSmsOverdue;

  @Column(name = "AMI_ONE_ID")
  private String amiOneId;

  @Column(name = "NATION")
  private String nation;

  @Column(name = "NUM_SUPPORT")
  private Long numSupport;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "ESTIMATE_TIME")
  private Date estimateTime;

  @Column(name = "DISTANCE")
  private String distance;

  @Column(name = "CABLE_CODE")
  private String cableCode;

  @Column(name = "CABLE_TYPE_CODE")
  private String cableTypeCode;

  @Column(name = "VIBA_LINE_CODE")
  private String vibaLineCode;

  @Column(name = "NUM_AUDIT_FAIL")
  private Long numAuditFail;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CUSTOMER_TIME_DESIRE_FROM")
  private Date customerTimeDesireFrom;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CUSTOMER_TIME_DESIRE_TO")
  private Date customerTimeDesireTo;

  @Column(name = "NUM_RECHECK")
  private Long numRecheck;

  @Column(name = "POINT_OK")
  private String pointOk;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "FT_ACCEPTED_TIME")
  private Date ftAcceptedTime;

  @Column(name = "PARENT_CODE")
  private String parentCode;

  @Column(name = "IS_SEND_SMS_OVERDUE")
  private Long isSendSmsOverdue;

  @Column(name = "REASON_INTERFERENCE")
  private Long reasonInterference;

  @Column(name = "IS_SEND_CALL_BUSINESS")
  private String isSendCallBusiness;

  @Column(name = "ALLOW_SUPPORT")
  private Long allowSupport;

  @Column(name = "IS_SEND_SMS_CUSTOMER_DESIRE")
  private Long isSendSmsCustomerDesire;

  @Column(name = "DISTRICT_RECHECK")
  private Long districtRecheck;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "FAILURE_REASON")
  private String failureReason;

  @Column(name = "NEW_FAILURE_REASON")
  private String newFailureReason;

  @Column(name = "DEVICE_TYPE_NAME")
  private String deviceTypeName;

  @Column(name = "CALL_TO_CD")
  private Long callToCd;

  @Column(name = "HAS_COST")
  private Long hasCost;

  @Column(name = "NUM_AUTO_CHECK")
  private Long numAutoCheck;

  @Column(name = "SYNC_STATUS")
  private Long syncStatus;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "START_WORKING_TIME")
  private Date startWorkingTime;

  @Column(name = "START_LONGITUDE")
  private String startLongitude;

  @Column(name = "START_LATITUDE")
  private String startLatitude;

  @Column(name = "WORK_STATUS")
  private Long workStatus;

  @Column(name = "REASON_EXTENTION")
  private String reasonExtention;

  @Column(name = "UNIT_APPROVE_EXTEND")
  private String unitApproveExtend;

  @Column(name = "REASON_REJECT_ID")
  private Long reasonRejectId;

  //Constructors
  public WoEntity(Long woId, String woCode, String woSystem, Long status) {
    this.woId = woId;
    this.woCode = woCode;
    this.woSystem = woSystem;
    this.status = status;
  }

  public WoInsideDTO toDTO() {
    return new WoInsideDTO(woId, parentId, woCode, woContent, createDate, woSystem, woSystemId,
        woTypeId, createPersonId, cdId, ftId, status, priorityId, startTime, endTime, finishTime,
        result, stationId, stationCode, lastUpdateTime, fileName, woDescription, summaryStatus,
        cdReceivedAlarmTime, cdReceivedThresholdTime, ftReceivedAlarmTime, ftReceivedThresholdTime,
        ftProcessAlarmTime, ftProcessThresholdTime, summaryTime, woSystemOutId, userId,
        woWorklogContent, numPending, endPendingTime, reasonOverdueLV1Id, reasonOverdueLV1Name,
        reasonOverdueLV2Id, reasonOverdueLV2Name, processingGuidelines, commentComplete,
        completedTime, isCompletedOnVsmart, isCall, lineCode, solutionGroupId, solutionGroupName,
        solutionDetail, cellService, concaveAreaCode, longitude, latitude, locationCode,
        reasonDetail, warehouseCode, needSupport, constructionCode, cdAssignId, deltaCloseWo,
        confirmNotCreateAlarm, alias, deviceCode, nationCode, syncState, stationCodeNims,
        reasonApproveNok, planCode, lastTimeSmsOverdue, amiOneId, nation, numSupport, estimateTime,
        distance, cableCode, cableTypeCode, vibaLineCode, numAuditFail, customerTimeDesireFrom,
        customerTimeDesireTo, numRecheck, pointOk, ftAcceptedTime, parentCode, isSendSmsOverdue,
        reasonInterference, isSendCallBusiness, allowSupport, isSendSmsCustomerDesire,
        districtRecheck, deviceType, failureReason, newFailureReason, deviceTypeName, callToCd,
        hasCost, numAutoCheck, reasonExtention, unitApproveExtend,
        StringUtils.isStringNullOrEmpty(reasonRejectId) ? null : reasonRejectId.toString());
  }

  public Wo toWo() {
    return new Wo(woId, parentId, woCode, woContent, createDate, woSystem, woSystemId,
        woTypeId, createPersonId, cdId, ftId, status, priorityId, startTime, endTime, finishTime,
        result, stationId, stationCode, lastUpdateTime, fileName, woDescription, summaryStatus,
        cdReceivedAlarmTime, cdReceivedThresholdTime, ftReceivedAlarmTime, ftReceivedThresholdTime,
        ftProcessAlarmTime, ftProcessThresholdTime, summaryTime, woSystemOutId, userId,
        woWorklogContent, numPending, endPendingTime, reasonOverdueLV1Id, reasonOverdueLV1Name,
        reasonOverdueLV2Id, reasonOverdueLV2Name, processingGuidelines, commentComplete,
        completedTime, isCompletedOnVsmart, isCall, lineCode, solutionGroupId, solutionGroupName,
        solutionDetail, cellService, concaveAreaCode, longitude, latitude, locationCode,
        reasonDetail, warehouseCode, needSupport, constructionCode, cdAssignId, deltaCloseWo,
        confirmNotCreateAlarm, alias, deviceCode, nationCode, syncState, stationCodeNims,
        reasonApproveNok, planCode, lastTimeSmsOverdue, amiOneId, nation, numSupport, estimateTime,
        distance, cableCode, cableTypeCode, vibaLineCode, numAuditFail, customerTimeDesireFrom,
        customerTimeDesireTo, numRecheck, pointOk, ftAcceptedTime, parentCode, isSendSmsOverdue,
        reasonInterference, isSendCallBusiness, allowSupport, isSendSmsCustomerDesire,
        districtRecheck, deviceType, failureReason, newFailureReason, deviceTypeName, callToCd,
        hasCost, numAutoCheck, reasonExtention, unitApproveExtend,
        StringUtils.isStringNullOrEmpty(reasonRejectId) ? null : reasonRejectId.toString());
  }
}
