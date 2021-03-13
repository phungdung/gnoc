/**
 * @(#)TroublesBO.java 8/17/2015 8:35 PM, Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.model;

import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(schema = "ONE_TM", name = "TROUBLES")
@Getter
@Setter
@NoArgsConstructor
public class TroublesEntity {

  //Fields
  @Id
  @Column(name = "TROUBLE_ID", unique = true, nullable = false)
  private Long troubleId;

  @Column(name = "TROUBLE_CODE", nullable = false)
  private String troubleCode;

  @Column(name = "TROUBLE_NAME", nullable = false)
  private String troubleName;

  @Column(name = "DESCRIPTION", nullable = false)
  private String description;

  @Column(name = "TYPE_ID", nullable = false)
  private Long typeId;

  @Column(name = "SUB_CATEGORY_ID", nullable = false)
  private Long subCategoryId;

  @Column(name = "PRIORITY_ID", nullable = false)
  private Long priorityId;

  @Column(name = "IMPACT_ID", nullable = false)
  private Long impactId;

  @Column(name = "STATE", nullable = false)
  private Long state;

  @Column(name = "AFFECTED_NODE")
  private String affectedNode;

  @Column(name = "AFFECTED_SERVICE")
  private String affectedService;

  @Column(name = "LOCATION")
  private String location;

  @Column(name = "LOCATION_ID")
  private Long locationId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME", nullable = false)
  private Date createdTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "ASSIGN_TIME")
  private Date assignTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "BEGIN_TROUBLE_TIME")
  private Date beginTroubleTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "END_TROUBLE_TIME")
  private Date endTroubleTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "DEFERRED_TIME")
  private Date deferredTime;

  @Column(name = "CREATE_USER_ID")
  private Long createUserId;

  @Column(name = "CREATE_UNIT_ID", nullable = false)
  private Long createUnitId;

  @Column(name = "CREATE_USER_NAME")
  private String createUserName;

  @Column(name = "CREATE_UNIT_NAME", nullable = false)
  private String createUnitName;

  @Column(name = "INSERT_SOURCE", nullable = false)
  private String insertSource;

  @Column(name = "RELATED_PT")
  private String relatedPt;

  @Column(name = "VENDOR_ID")
  private Long vendorId;

  @Column(name = "RELATED_KEDB")
  private String relatedKedb;

  @Column(name = "RECEIVE_UNIT_ID", nullable = false)
  private Long receiveUnitId;

  @Column(name = "RECEIVE_USER_ID")
  private Long receiveUserId;

  @Column(name = "RECEIVE_UNIT_NAME", nullable = false)
  private String receiveUnitName;

  @Column(name = "RECEIVE_USER_NAME")
  private String receiveUserName;

  @Column(name = "ROOT_CAUSE")
  private String rootCause;

  @Column(name = "WORK_ARROUND")
  private String workArround;

  @Column(name = "SOLUTION_TYPE")
  private Long solutionType;

  @Column(name = "WORK_LOG")
  private String workLog;

  @Column(name = "REJECTED_CODE")
  private Long rejectedCode;

  @Column(name = "REJECT_REASON")
  private String rejectReason;

  @Column(name = "RISK")
  private Long risk;

  @Column(name = "SUPPORT_UNIT_ID")
  private Long supportUnitId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "QUEUE_TIME")
  private Date queueTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CLEAR_TIME")
  private Date clearTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CLOSED_TIME")
  private Date closedTime;

  @Column(name = "REASON_OVERDUE")
  private String reasonOverdue;

  @Column(name = "TIME_ZONE_CREATE")
  private Long timeZoneCreate;

  @Column(name = "DEFERRED_REASON")
  private String deferredReason;

  @Column(name = "CLOSE_CODE")
  private Long closeCode;

  @Column(name = "REASON_ID")
  private Long reasonId;

  @Column(name = "TIME_USED")
  private Double timeUsed;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "ASSIGN_TIME_TEMP")
  private Date assignTimeTemp;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "REJECT_TIME")
  private Date rejectTime;

  @Column(name = "TIME_PROCESS")
  private Double timeProcess;

  @Column(name = "TIME_CREATE_CFG")
  private Double timeCreateCfg;

  @Column(name = "SUPPORT_UNIT_NAME")
  private String supportUnitName;

  @Column(name = "REASON_NAME")
  private String reasonName;

  @Column(name = "TBL_CURR")
  private String tblCurr;

  @Column(name = "TBL_HIS")
  private String tblHis;

  @Column(name = "TBL_CLEAR")
  private String tblClear;

  @Column(name = "ALARM_ID")
  private Long alarmId;

  @Column(name = "NETWORK_LEVEL")
  private String networkLevel;

  @Column(name = "AUTO_CREATE_WO")
  private Long autoCreateWO;

  @Column(name = "LINE_CUT_CODE")
  private String lineCutCode; // mã tuyến đứt

  @Column(name = "CODE_SNIPPET_OFF")
  private String codeSnippetOff; // mã đoạn đứt

  @Column(name = "CABLE_TYPE")
  private Long cableType; // loại cáp

  @Column(name = "CLOSURES_REPLACE")
  private String closuresReplace; // măng xông thay thế

  @Column(name = "where_Wrong")
  private String whereWrong;

  @Column(name = "asessment_Data")
  private Long asessmentData;

  @Column(name = "alarm_group")
  private String alarmGroup; // nhóm cảnh báo

  @Column(name = "service_type")
  private Long serviceType;  // Loại dịch vụ

  @Column(name = "complaint_group_id")
  private String complaintGroupId;  // nhóm khiếu nại

  @Column(name = "SPM_CODE")
  private String spmCode;

  @Column(name = "REASON_LV1_ID")
  private String reasonLv1Id;

  @Column(name = "REASON_LV1_NAME")
  private String reasonLv1Name;

  @Column(name = "REASON_LV2_ID")
  private String reasonLv2Id;

  @Column(name = "REASON_LV2_NAME")
  private String reasonLv2Name;

  @Column(name = "REASON_LV3_ID")
  private String reasonLv3Id;

  @Column(name = "REASON_LV3_NAME")
  private String reasonLv3Name;

  @Column(name = "REASON_OVERDUE_ID")
  private String reasonOverdueId;

  @Column(name = "REASON_OVERDUE_NAME")
  private String reasonOverdueName;

  @Column(name = "REASON_OVERDUE_ID_2")
  private String reasonOverdueId2;

  @Column(name = "REASON_OVERDUE_NAME_2")
  private String reasonOverdueName2;

  @Column(name = "CLOSE_TT_TIME", unique = false, nullable = true)
  private Double closeTtTime; //Thoi gian dong canh bao, chuyen Long thành Double

  @Column(name = "TRANS_NETWORK_TYPE_ID")
  private Long transNetworkTypeId; //ma loai mang truyen dan

  @Column(name = "TRANS_REASON_EFFECTIVE_ID")
  private Long transReasonEffectiveId; //Ma nguyen nhan anh huong dich vu

  @Column(name = "TRANS_REASON_EFFECTIVE_CONTENT")
  private String transReasonEffectiveContent; //Noi dung nguyen nhan anh huong dich vu.

  @Column(name = "AUTO_CLOSE")
  private Long autoClose; //Tu dong close canh bao.

  @Column(name = "WO_CODE")
  private String woCode; //Mã workorder

  @Column(name = "CLEAR_USER_ID")
  private Long clearUserId;

  @Column(name = "CLEAR_USER_NAME")
  private String clearUserName;

  @Column(name = "RELATED_TROUBLE_CODES")
  private String relatedTroubleCodes; //HaiNV20, thong tin su co lien quan RELATED_TROUBLE_CODE

  @Column(name = "RE_OCCUR")
  private Long reOccur; //so lan xuat lai

  @Column(name = "RELATED_TT")
  private String relatedTt;

  @Column(name = "is_move")
  private Long isMove;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "DATE_MOVE")
  private Date dateMove;

  @Column(name = "unit_move")
  private Long unitMove;

  @Column(name = "unit_move_name")
  private String unitMoveName;

  @Column(name = "is_update_after_closed")
  private String isUpdateAfterClosed;

  @Column(name = "COUNT_REOPEN")
  private Long countReopen;

  @Column(name = "IS_STATION_VIP")
  private Long isStationVip;

  @Column(name = "is_tick_help")
  private Long isTickHelp;

  @Column(name = "num_help")
  private Long numHelp;

  @Column(name = "num_pending")
  private Long numPending;

  @Column(name = "NUM_AON")
  private Long numAon;

  @Column(name = "NUM_GPON")
  private Long numGpon;

  @Column(name = "NUM_NEXTTV")
  private Long numNexttv;

  @Column(name = "NUM_THC")
  private Long numThc;

  @Column(name = "TECHNOLOGY")
  private String technology;

  @Column(name = "info_ticket")
  private String infoTicket;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "Catching_time")
  private Date catchingTime;

  @Column(name = "AMI_ID")
  private Long amiId;

  @Column(name = "RELATED_CR")
  private String relatedCr;

  @Column(name = "NATION_CODE")
  private String nationCode;

  @Column(name = "IS_CHAT")
  private Long isChat;

  @Column(name = "COMPLAINT_ID")
  private Long complaintId;

  @Column(name = "num_reassign")
  private Long numReassign;

  @Column(name = "defer_type")
  private Long deferType;//loai tam dong

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "estimate_Time")
  private Date estimateTime;//thoi gian du kien

  @Column(name = "longitude")
  private String longitude;//kinh do

  @Column(name = "latitude")
  private String latitude;//vi do

  @Column(name = "group_Solution")
  private Long groupSolution; //nhom giai phap

  @Column(name = "cell_Service")
  private String cellService;//cell phuc vu

  @Column(name = "concave")
  private String concave; //vong lom

  @Column(name = "error_code")
  private Long errorCode;// loai loi cua CC

  @Column(name = "type_unit")
  private Long typeUnit;//loai don vi tao ticket

  @Column(name = "is_send_tktu")
  private Long isSendTktu;//gui ket qua sang tktu

  @Column(name = "trouble_assign_id")
  private Long troubleAssignId;

  //luu cho cc
  @Column(name = "CREATE_UNIT_ID_BY_CC")
  private Long createUnitIdByCC;

  @Column(name = "COMPLAINT_TYPE_ID")
  private Long complaintTypeId;

  @Column(name = "COMPLAINT_PARENT_ID")
  private Long complaintParentId;

  @Column(name = "ISDN")
  private String isdn;

  @Column(name = "CREATE_USER_BY_CC")
  private String createUserByCC;

  @Column(name = "LST_COMPLAINT")
  private String lstComplaint;

  @Column(name = "downtime")
  private String downtime;

  @Column(name = "num_affect")
  private String numAffect;

  @Column(name = "sub_min")
  private String subMin;

  @Column(name = "Warn_Level")
  private Long warnLevel;

  @Column(name = "is_fail_device")
  private Long isFailDevice;

  @Column(name = "country")
  private String country;

  @Column(name = "is_overdue")
  private Long isOverdue;

  @Column(name = "UNIT_APPROVAL")
  private String unitApproval;
  public TroublesEntity(Long troubleId, String troubleCode, String troubleName,
      String description, Long typeId, Long subCategoryId, Long priorityId,
      Long impactId, Long state, String affectedNode, String affectedService,
      String location, Long locationId, Date createdTime, Date lastUpdateTime,
      Date assignTime, Date beginTroubleTime, Date endTroubleTime, Date deferredTime,
      Long createUserId, Long createUnitId, String createUserName, String createUnitName,
      String insertSource, String relatedPt, Long vendorId, String relatedKedb,
      Long receiveUnitId, Long receiveUserId, String receiveUnitName, String receiveUserName,
      String rootCause, String workArround, Long solutionType, String workLog,
      Long rejectedCode, String rejectReason, Long risk, Long supportUnitId, Date queueTime,
      Date clearTime, Date closedTime, String reasonOverdue, Long timeZoneCreate,
      String deferredReason, Long closeCode, Long reasonId, Double timeUsed, Date assignTimeTemp,
      Double timeProcess, String supportUnitName, String reasonName, Double timeCreateCfg,
      String tblCurr, String tblHis, String tblClear,
      Long alarmId, String networkLevle, Long autoCreateWO, String lineCutCode,
      String codeSnippetOff,
      Long cableType, String closuresReplace, String whereWrong, Long asessmentData,
      String alarmGroup, Long serviceType, String complaintGroupId, String reasonLv1Id,
      String reasonLv1Name, String reasonLv2Id, String reasonLv2Name, String reasonLv3Id,
      String reasonLv3Name, String reasonOverdueId, String reasonOverdueName,
      String reasonOverdueId2, String reasonOverdueName2, String spmCode, String relatedTt,
      Long isMove, Date dateMove, Long unitMove, String unitMoveName, String isUpdateAfterClosed,
      Long isStationVip,
      Long isTickHelp, Long numPending, Long numAon, Long numGpon, Long numNexttv, Long numThc,
      String technology, String infoTicket, Date catchingTime, Long numHelp, Long amiId,
      String relatedCr, String nationCode, Long isChat, Long complaintId, Long numReassign,
      Long deferType, Date estimateTime, String longitude, String latitude, Long groupSolution,
      String cellService,
      String concave, Long errorCode, Long typeUnit, Long isSendTktu, Long troubleAssignId,
      Long createUnitIdByCC,
      Long complaintTypeId, Long complaintParentId, String isdn, String createUserByCC,
      String lstComplaint,
      String downtime, Long countReopen, String numAffect, String subMin, Long warnLevel,
      Long isFailDevice, String country,
      Long isOverdue, String unitApproval) {
    this.troubleId = troubleId;
    this.troubleCode = troubleCode;
    this.troubleName = troubleName;
    this.description = description;
    this.typeId = typeId;
    this.subCategoryId = subCategoryId;
    this.priorityId = priorityId;
    this.impactId = impactId;
    this.state = state;
    this.affectedNode = affectedNode;
    this.affectedService = affectedService;
    this.location = location;
    this.locationId = locationId;
    this.createdTime = createdTime;
    this.lastUpdateTime = lastUpdateTime;
    this.assignTime = assignTime;
    this.beginTroubleTime = beginTroubleTime;
    this.endTroubleTime = endTroubleTime;
    this.deferredTime = deferredTime;
    this.createUserId = createUserId;
    this.createUnitId = createUnitId;
    this.createUserName = createUserName;
    this.createUnitName = createUnitName;
    this.insertSource = insertSource;
    this.relatedPt = relatedPt;
    this.vendorId = vendorId;
    this.relatedKedb = relatedKedb;
    this.receiveUnitId = receiveUnitId;
    this.receiveUserId = receiveUserId;
    this.receiveUnitName = receiveUnitName;
    this.receiveUserName = receiveUserName;
    this.rootCause = rootCause;
    this.workArround = workArround;
    this.solutionType = solutionType;
    this.workLog = workLog;
    this.rejectedCode = rejectedCode;
    this.rejectReason = rejectReason;
    this.risk = risk;
    this.supportUnitId = supportUnitId;
    this.queueTime = queueTime;
    this.clearTime = clearTime;
    this.closedTime = closedTime;
    this.reasonOverdue = reasonOverdue;
    this.timeZoneCreate = timeZoneCreate;
    this.deferredReason = deferredReason;
    this.closeCode = closeCode;
    this.reasonId = reasonId;
    this.timeUsed = timeUsed;
    this.assignTimeTemp = assignTimeTemp;
    this.timeProcess = timeProcess;
    this.supportUnitName = supportUnitName;
    this.reasonName = reasonName;
    this.timeCreateCfg = timeCreateCfg;
    this.tblCurr = tblCurr;
    this.tblHis = tblHis;
    this.tblClear = tblClear;
    this.alarmId = alarmId;
    this.networkLevel = networkLevle;
    this.autoCreateWO = autoCreateWO;
    this.lineCutCode = lineCutCode;
    this.codeSnippetOff = codeSnippetOff;
    this.cableType = cableType;
    this.closuresReplace = closuresReplace;
    this.whereWrong = whereWrong;
    this.asessmentData = asessmentData;
    this.alarmGroup = alarmGroup;
    this.serviceType = serviceType;
    this.complaintGroupId = complaintGroupId;
    this.reasonLv1Id = reasonLv1Id;
    this.reasonLv1Name = reasonLv1Name;
    this.reasonLv2Id = reasonLv2Id;
    this.reasonLv2Name = reasonLv2Name;
    this.reasonLv3Id = reasonLv3Id;
    this.reasonLv3Name = reasonLv3Name;
    this.reasonOverdueId = reasonOverdueId;
    this.reasonOverdueName = reasonOverdueName;
    this.reasonOverdueId2 = reasonOverdueId2;
    this.reasonOverdueName2 = reasonOverdueName2;
    this.spmCode = spmCode;
    this.relatedTt = relatedTt;
    this.isMove = isMove;
    this.dateMove = dateMove;
    this.unitMove = unitMove;
    this.unitMoveName = unitMoveName;
    this.isUpdateAfterClosed = isUpdateAfterClosed;
    this.isStationVip = isStationVip;
    this.isTickHelp = isTickHelp;
    this.numPending = numPending;
    this.numAon = numAon;
    this.numGpon = numGpon;
    this.numNexttv = numNexttv;
    this.numThc = numThc;
    this.technology = technology;
    this.infoTicket = infoTicket;
    this.catchingTime = catchingTime;
    this.numHelp = numHelp;
    this.amiId = amiId;
    this.relatedCr = relatedCr;
    this.nationCode = nationCode;
    this.isChat = isChat;
    this.complaintId = complaintId;
    this.numReassign = numReassign;
    this.deferType = deferType;
    this.estimateTime = estimateTime;
    this.longitude = longitude;
    this.latitude = latitude;
    this.groupSolution = groupSolution;
    this.cellService = cellService;
    this.concave = concave;
    this.errorCode = errorCode;
    this.typeUnit = typeUnit;
    this.isSendTktu = isSendTktu;
    this.troubleAssignId = troubleAssignId;
    this.createUnitIdByCC = createUnitIdByCC;
    this.complaintTypeId = complaintTypeId;
    this.complaintParentId = complaintParentId;
    this.isdn = isdn;
    this.createUserByCC = createUserByCC;
    this.lstComplaint = lstComplaint;
    this.downtime = downtime;
    this.countReopen = countReopen;
    this.numAffect = numAffect;
    this.subMin = subMin;
    this.warnLevel = warnLevel;
    this.isFailDevice = isFailDevice;
    this.country = country;
    this.isOverdue = isOverdue;
    this.unitApproval = unitApproval;
  }

  public TroublesInSideDTO toDTO() {
    TroublesInSideDTO dto = new TroublesInSideDTO(
        troubleId, troubleCode, troubleName,
        description,
        typeId,
        subCategoryId,
        priorityId,
        impactId,
        state, affectedNode, affectedService, location,
        locationId,
        createdTime,
        lastUpdateTime,
        assignTime,
        beginTroubleTime,
        endTroubleTime,
        deferredTime,
        createUserId, createUnitId,
        createUserName, createUnitName, insertSource, relatedPt,
        vendorId,
        relatedKedb,
        receiveUnitId,
        receiveUserId, receiveUnitName, receiveUserName,
        rootCause,
        workArround,
        solutionType, workLog,
        rejectedCode, rejectReason,
        risk, supportUnitId,
        queueTime,
        clearTime,
        closedTime,
        reasonOverdue,
        timeZoneCreate,
        deferredReason,
        closeCode,
        reasonId,
        supportUnitName,
        reasonName,
        tblCurr,
        tblHis,
        tblClear,
        alarmId,
        networkLevel,
        autoCreateWO,
        lineCutCode,
        codeSnippetOff,
        cableType,
        closuresReplace,
        whereWrong,
        asessmentData,
        alarmGroup,
        serviceType,
        null, spmCode, complaintGroupId,
        reasonLv1Id,
        reasonLv1Name,
        reasonLv2Id,
        reasonLv2Name,
        reasonLv3Id,
        reasonLv3Name,
        reasonOverdueId,
        reasonOverdueName,
        reasonOverdueId2,
        reasonOverdueName2,
        relatedTt,
        isMove,
        dateMove,
        unitMove,
        unitMoveName, isUpdateAfterClosed, isStationVip,
        isTickHelp,
        numPending,
        numAon,
        numGpon,
        numNexttv,
        numThc,
        technology,
        infoTicket,
        catchingTime,
        numHelp,
        amiId,
        relatedCr,
        nationCode,
        isChat,
        complaintId,
        numReassign,
        deferType,
        estimateTime,
        longitude, latitude,
        groupSolution, cellService, concave,
        errorCode, typeUnit,
        isSendTktu, troubleAssignId,
        createUnitIdByCC, complaintTypeId,
        complaintParentId, isdn, createUserByCC, lstComplaint,
        downtime, countReopen, numAffect, subMin,
        warnLevel, isFailDevice, unitApproval,
        country, isOverdue, assignTimeTemp, timeProcess, timeUsed
    );
    //HaiNV20 update
    dto.setAutoClose(autoClose); //HaiNV20 add
    dto.setCloseTtTime(closeTtTime);
    dto.setTransNetworkTypeId(transNetworkTypeId);
    dto.setTransReasonEffectiveId(
        transReasonEffectiveId);
    dto.setTransReasonEffectiveContent(transReasonEffectiveContent);
    dto.setWoCode(woCode);
//        dto.setProcessingGuidelines(processingGuidelines); //HaiNV20 bo sung thong tin huong dan xu ly.
    dto.setRelatedTroubleCodes(relatedTroubleCodes); //HaiNV20 bo sung thong tin troubles lien quan.
    dto.setReOccur(reOccur);
    return dto;
  }

  public String toString() {
    return "Troubles{" + "troubleId=" + troubleId + ", \n troubleCode=" + troubleCode
        + ", \n troubleName=" + troubleName + ", \n description=" + description + ", \n typeId="
        + typeId + ", \n subCategoryId=" + subCategoryId + ", \n priorityId=" + priorityId
        + ", \n impactId=" + impactId + ", \n state=" + state + ", \n affectedNode=" + affectedNode
        + ", \n affectedService=" + affectedService + ", \n location=" + location
        + ", \n locationId=" + locationId + ", \n createdTime=" + createdTime
        + ", \n lastUpdateTime=" + lastUpdateTime + ", \n assignTime=" + assignTime
        + ", \n beginTroubleTime=" + beginTroubleTime + ", \n endTroubleTime=" + endTroubleTime
        + ", \n deferredTime=" + deferredTime + ", \n createUserId=" + createUserId
        + ", \n createUnitId=" + createUnitId + ", \n createUserName=" + createUserName
        + ", \n createUnitName=" + createUnitName + ", \n insertSource=" + insertSource
        + ", \n relatedPt=" + relatedPt + ", \n vendorId=" + vendorId + ", \n relatedKedb="
        + relatedKedb + ", \n receiveUnitId=" + receiveUnitId + ", \n receiveUserId="
        + receiveUserId + ", \n receiveUnitName=" + receiveUnitName + ", \n receiveUserName="
        + receiveUserName + ", \n rootCause=" + rootCause + ", \n workArround=" + workArround
        + ", \n solutionType=" + solutionType + ", \n workLog=" + workLog + ", \n rejectedCode="
        + rejectedCode + ", \n rejectReason=" + rejectReason + ", \n risk=" + risk
        + ", \n supportUnitId=" + supportUnitId + ", \n queueTime=" + queueTime + ", \n clearTime="
        + clearTime + ", \n closedTime=" + closedTime + ", \n reasonOverdue=" + reasonOverdue
        + ", \n timeZoneCreate=" + timeZoneCreate + ", \n deferredReason=" + deferredReason
        + ", \n closeCode=" + closeCode + ", \n reasonId=" + reasonId + ", \n timeUsed=" + timeUsed
        + ", \n assignTimeTemp=" + assignTimeTemp + ", \n timeProcess=" + timeProcess
        + ", \n timeCreateCfg=" + timeCreateCfg + ", \n supportUnitName=" + supportUnitName
        + ", \n reasonName=" + reasonName + '}';
  }

}
