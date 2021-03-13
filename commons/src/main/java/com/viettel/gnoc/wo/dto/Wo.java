package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.model.WoEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wo {

  private Long woId;
  private Long parentId;
  private String woCode;
  private String woContent;
  private Date createDate;
  private String woSystem;
  private String woSystemId;
  private Long woTypeId;
  private Long createPersonId;
  private Long cdId;
  private Long ftId;
  private Long status;
  private Long priorityId;
  private Date startTime;
  private Date endTime;
  private Date finishTime;
  private Long result;
  private Long stationId;
  private String stationCode;
  private Date lastUpdateTime;
  private String fileName;
  private String woDescription;
  private Long summaryStatus;
  private Date cdReceivedAlarmTime;
  private Date cdReceivedThresholdTime;
  private Date ftReceivedAlarmTime;
  private Date ftReceivedThresholdTime;
  private Date ftProcessAlarmTime;
  private Date ftProcessThresholdTime;
  private Date summaryTime;
  private String woSystemOutId;
  private Long userId;
  private String woWorklogContent;
  private Long numPending;
  private Date endPendingTime;
  private String reasonOverdueLV1Id;
  private String reasonOverdueLV1Name;
  private String reasonOverdueLV2Id;
  private String reasonOverdueLV2Name;
  private String processingGuidelines;
  private String commentComplete;
  private Date completedTime;
  private Long isCompletedOnVsmart;
  private Long isCall;
  private String lineCode;
  private Long solutionGroupId;
  private String solutionGroupName;
  private String solutionDetail;
  private String cellService;
  private String concaveAreaCode;
  private String longitude;
  private String latitude;
  private String locationCode;
  private String reasonDetail;
  private String warehouseCode;
  private Long needSupport;
  private String constructionCode;
  private Long cdAssignId;
  private Double deltaCloseWo;
  private Long confirmNotCreateAlarm;
  private String alias;
  private String deviceCode;
  private String nationCode;
  private Long syncState;
  private String stationCodeNims;
  private String reasonApproveNok;
  private String planCode;
  private Date lastTimeSmsOverdue;
  private String amiOneId;
  private String nation;
  private Long numSupport;
  private Date estimateTime;
  private String distance;
  private String cableCode;
  private String cableTypeCode;
  private String vibaLineCode;
  private Long numAuditFail;
  private Date customerTimeDesireFrom;
  private Date customerTimeDesireTo;
  private Long numRecheck;
  private String pointOk;
  private Date ftAcceptedTime;
  private String parentCode;
  private Long isSendSmsOverdue;
  private Long reasonInterference;
  private String isSendCallBusiness;
  private Long allowSupport;
  private Long isSendSmsCustomerDesire;
  private Long districtRecheck;
  private String deviceType;
  private String failureReason;
  private String newFailureReason;
  private String deviceTypeName;
  private Long callToCd;
  private Long hasCost;
  private Long numAutoCheck;
  private String reasonExtention;
  private String unitApproveExtend;
  private String reasonRejectId;

  public WoEntity toEntity() {
    return new WoEntity(woId, parentId, woCode, woContent, createDate, woSystem, woSystemId,
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
        hasCost, numAutoCheck, null,null, null, null, null,
        reasonExtention, unitApproveExtend, StringUtils.validString(reasonRejectId) ? Long.valueOf(reasonRejectId) : null);
  }

}
