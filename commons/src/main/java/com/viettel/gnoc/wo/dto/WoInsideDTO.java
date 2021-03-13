package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.KeyValueDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.wo.model.WoEntity;
import com.viettel.vmsa.MopGnoc;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Slf4j
public class WoInsideDTO extends BaseDto {

  private Long woId;
  private Long parentId;
  private String woCode;
  @NotNull(message = "validation.woDTO.woContent.notNull")
  @Size(max = 2000, message = "validation.woDTO.woContent.tooLong")
  private String woContent;
  private Date createDate;
  @NotNull(message = "validation.woDTO.woSystem.notNull")
  @SizeByte(max = 25, message = "validation.woDTO.woSystem.tooLong")
  private String woSystem;
  @SizeByte(max = 50, message = "validation.woDTO.woSystemId.tooLong")
  private String woSystemId;
  private Long woTypeId;
  private Long createPersonId;
  private Long cdId;
  private Long ftId;
  @NotNull(message = "validation.woDTO.status.notNull")
  private Long status;
  private Long priorityId;
  @NotNull(message = "validation.woDTO.startTime.notNull")
  private Date startTime;
  @NotNull(message = "validation.woDTO.endTime.notNull")
  private Date endTime;
  private Date finishTime;
  private Long result;
  private Long stationId;
  @SizeByte(max = 50, message = "validation.woDTO.stationCode.tooLong")
  private String stationCode;
  private Date lastUpdateTime;
  @Size(max = 2000, message = "validation.woDTO.fileName.tooLong")
  private String fileName;
  @Size(max = 2000, message = "validation.woDTO.woDescription.tooLong")
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
  @Size(max = 4000, message = "validation.woDTO.woWorklogContent.tooLong")
  private String woWorklogContent;
  private Long numPending;
  private Date endPendingTime;
  @SizeByte(max = 2000, message = "validation.woDTO.reasonOverdueLV1Id.tooLong")
  private String reasonOverdueLV1Id;
  @SizeByte(max = 2000, message = "validation.woDTO.reasonOverdueLV1Name.tooLong")
  private String reasonOverdueLV1Name;
  @SizeByte(max = 2000, message = "validation.woDTO.reasonOverdueLV2Id.tooLong")
  private String reasonOverdueLV2Id;
  @SizeByte(max = 2000, message = "validation.woDTO.reasonOverdueLV2Name.tooLong")
  private String reasonOverdueLV2Name;
  private String processingGuidelines;
  private String commentComplete;
  private Date completedTime;
  private Long isCompletedOnVsmart;
  private Long isCall;
  @SizeByte(max = 2000, message = "validation.woDTO.lineCode.tooLong")
  private String lineCode;
  private Long solutionGroupId;
  @SizeByte(max = 2000, message = "validation.woDTO.solutionGroupName.tooLong")
  private String solutionGroupName;
  @Size(max = 4000, message = "validation.woDTO.solutionDetail.tooLong")
  private String solutionDetail;
  private String cellService;
  private String concaveAreaCode;
  private String longitude;
  private String latitude;
  @SizeByte(max = 200, message = "validation.woDTO.locationCode.tooLong")
  private String locationCode;
  @Size(max = 4000, message = "validation.woDTO.reasonDetail.tooLong")
  private String reasonDetail;
  @SizeByte(max = 255, message = "validation.woDTO.warehouseCode.tooLong")
  private String warehouseCode;
  private Long needSupport;
  @SizeByte(max = 500, message = "validation.woDTO.constructionCode.tooLong")
  private String constructionCode;
  private Long cdAssignId;
  private Double deltaCloseWo;
  private Long confirmNotCreateAlarm;
  private String alias;
  @SizeByte(max = 255, message = "validation.woDTO.deviceCode.tooLong")
  private String deviceCode;
  private String nationCode;
  private Long syncState;
  private String stationCodeNims;
  private String reasonApproveNok;
  @Size(max = 2000, message = "validation.woDTO.planCode.tooLong")
  private String planCode;
  private Date lastTimeSmsOverdue;
  private String amiOneId;
  private String nation;
  private Long numSupport;
  private Date estimateTime;
  @SizeByte(max = 500, message = "validation.woDTO.distance.tooLong")
  private String distance;
  @SizeByte(max = 500, message = "validation.woDTO.cableCode.tooLong")
  private String cableCode;
  @SizeByte(max = 500, message = "validation.woDTO.cableTypeCode.tooLong")
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
  @Size(max = 2000, message = "validation.woDTO.deviceType.tooLong")
  private String deviceType;
  @Size(max = 2000, message = "validation.woDTO.failureReason.tooLong")
  private String failureReason;
  @Size(max = 2000, message = "validation.woDTO.newFailureReason.tooLong")
  private String newFailureReason;
  @Size(max = 2000, message = "validation.woDTO.deviceTypeName.tooLong")
  private String deviceTypeName;
  private Long callToCd;
  private Long hasCost;
  private Long numAutoCheck;

  private Double offSetFromUser;
  private String resultName;
  private String woTypeCode;
  private String priorityCode;
  private Long timeOver;

  private WoDetailDTO woDetailDTO;
  private List<WoMerchandiseInsideDTO> listWoMerchandiseInsideDTO;
  private WoKTTSInfoDTO woKTTSInfoDTO;
  private List<WoTestServiceMapDTO> listWoTestServiceMap;
  private List<GnocFileDto> gnocFileCreateWoDtos;
  private List<GnocFileDto> gnocFileDtos;
  private Long otherSystemId;
  private String otherSystemType;
  private List<String> fileNamesDelete;
  private String comment;
  private String role;
  private String auditResult;
  private List<UsersInsideDto> listCd;
  private String system;
  private String reasonName;
  private String reasonId;
  private String customer;
  private String phone;
  private List<WoMaterialDeducteInsideDTO> lstDeducte;
  private String reasonLv1Id;
  private String reasonLv1Name;
  private String reasonLv2Id;
  private String reasonLv2Name;
  private String reasonLv3Id;
  private String reasonLv3Name;
  private Boolean checkRefresh;
  private WoHistoryInsideDTO woHistoryInsideDTO;
  private WoHistoryDTO woHistoryDTO;
  private Boolean showMaterialInfo;
  private Boolean isShowReasonInfo;
  private Boolean isShowReasonOverdueInfo;
  private String action;
  private String spmCode;
  private String unitCode;
  private String isSendFT;
  private String notGetFileCfgWhenCreate;
  private Long ccGroupId;
  private Long telServiceId;
  private String customerPhone;
  private String customerName;
  private String portCorrectId;
  private String errTypeNims;
  private String subscriptionId;
  private String ccPriorityCode;
  private Long serviceId;
  private String effectType;
  private String service;
  private String subscriberName;
  private String technicalClues;
  private List<KeyValueDTO> lstKeyValue;
  private String isValidWoTrouble;
  private Date clearTime;
  private Long reasonTroubleId;
  private String reasonTroubleName;
  private String solution;
  private Double polesDistance;
  private Long scriptId;
  private String scriptName;
  private Long isCcResult;
  private String productCode;
  private List<String> listFileName;
  private List<byte[]> fileArr;
  private String ftAudioName;//ten file am thanh ft
  private String cdAudioName;//ten file am thanh cd
  private Long requiredTtReason;
  private String linkCode; //ma tuyen
  private Long linkId;   //id tuyen
  private Long alarmId;//id khieu nai
  //  private List<WoMerchandiseInsideDTO> lstMerchandise;
  private List<WoMerchandiseDTO> lstMerchandise;
  private WoKTTSInfoDTO woKttsInfo;
  private WoInsideDTO parentWo;
  private List<String> lstUsername;
  private List<String> lstDescription;
  private List<Boolean> getFileParent;
  private List<WoTestServiceMapDTO> lstWoTestService;
  private String userCall;
  private String crNumber;
  private String crTitle;
  private String impactNode;
  private List<WoInsideDTO> lstWo;
  private String isHot;
  private String numComplaint;
  private String messageCreate;
  private String createWoType;
  private String createPeronEmail;
  private String createPersonMobile;
  private String woGroupName;
  private String woGroupEmail;
  private String woGroupMobile;
  private String ccResult;
  private String checkQosInternetResult;
  private String checkQosTHResult;
  private String checkQrCode;
  private String infraTypeName;
  private String priorityColorCode;

  //for WoDTOSearchWeb
  private Date startTimeFrom;
  private Date startTimeTo;
  private Date endTimeFrom;
  private Date endTimeTo;
  private Date startDateFrom;
  private Date startDateTo;
  private Date completeTimeFrom;
  private Date completeTimeTo;
  private Long woTypeGroupId;
  private String createPersonName;
  private String ftName;
  private String ftFullName;
  private String ftMobile;
  private List<String> lstStationCode;
  private List<String> cdIdList;
  private String parentName;
  private String accountIsdn;
  private Boolean isContainChildUnit;
  private Long createUnitId;
  private Long processUnitId;
  private Boolean isCreated;
  private Boolean isCd;
  private Boolean isFt;
  private Boolean isNeedSupport;
  private String statusSearchWeb;
  private String woTypeName;
  private String statusName;
  private String priorityName;
  private String comments;
  private String cdName;
  private Double remainTime;
  private String kedbCode;
  private Long kedbId;
  private Long contractId;
  private Long processActionId;
  private Long ableMop;
  private String createUnitName;// ten don vi tao
  private Long ccServiceId;
  private Long infraType;
  private Long hasWorklog;
  private String contractCode;
  private String processActionName;
  private String contractPartner;

  private String lineCutCode;// ma tuyen dut
  private String codeSnippetOff;// ma doan dut
  private String closuresReplace; // mang xong thay the
  private String user;
  private String woSourceCode;
  private String cdGroupCode;
  private String isSendCd;
  private String accountGline;
  private String woWorklog;
  private String acountUplink;

  private String cdGroupType;
  private String connectorCode;
  private String deptCode;
  private Long syncStatus;
  private Long offset;

  //tiennv bo sung cho chuc nang loadMopTest
  private Long typeOperation;
  private CrFilesAttachInsiteDTO crFilesAttachInsiteDTO;
  private MopGnoc mopGnoc;
  private List<Long> idDeleteList;

  //update WO
  private String customerGroupType;
  private String ktrKeyPoint;
  private String contentUpgradeAndDownGrade;

  //thangdt nang cap ws insertWoKTTS
  private Long activeEnvironmentId;

  //tiennv bo sung ham cho vsmart
  private String startWorkingTime;
  private String startLongitude;
  private String startLatitude;
  private String workStatus;

  //hungtv nang cap phe duyet gia han
  private String isApproveExtend;
  private String reasonExtention;
  private String unitApproveExtend;
  private String commentApprove;
  private String resultApprove;

  //tiennv bo sung cho vsmart cap nhat
  private String reasonRejectId;

  public Boolean isIsCreated() {
    return isCreated;
  }

  public Boolean isIsCd() {
    return isCd;
  }

  public Boolean isIsFt() {
    return isFt;
  }

  public WoInsideDTO(Long woId, Long parentId, String woCode, String woContent, Date createDate,
      String woSystem, String woSystemId, Long woTypeId, Long createPersonId, Long cdId,
      Long ftId, Long status, Long priorityId, Date startTime, Date endTime,
      Date finishTime, Long result, Long stationId, String stationCode, Date lastUpdateTime,
      String fileName, String woDescription, Long summaryStatus, Date cdReceivedAlarmTime,
      Date cdReceivedThresholdTime, Date ftReceivedAlarmTime, Date ftReceivedThresholdTime,
      Date ftProcessAlarmTime, Date ftProcessThresholdTime, Date summaryTime,
      String woSystemOutId, Long userId, String woWorklogContent, Long numPending,
      Date endPendingTime, String reasonOverdueLV1Id, String reasonOverdueLV1Name,
      String reasonOverdueLV2Id, String reasonOverdueLV2Name, String processingGuidelines,
      String commentComplete, Date completedTime, Long isCompletedOnVsmart, Long isCall,
      String lineCode, Long solutionGroupId, String solutionGroupName,
      String solutionDetail, String cellService, String concaveAreaCode, String longitude,
      String latitude, String locationCode, String reasonDetail, String warehouseCode,
      Long needSupport, String constructionCode, Long cdAssignId, Double deltaCloseWo,
      Long confirmNotCreateAlarm, String alias, String deviceCode, String nationCode,
      Long syncState, String stationCodeNims, String reasonApproveNok, String planCode,
      Date lastTimeSmsOverdue, String amiOneId, String nation, Long numSupport,
      Date estimateTime, String distance, String cableCode, String cableTypeCode,
      String vibaLineCode, Long numAuditFail, Date customerTimeDesireFrom,
      Date customerTimeDesireTo, Long numRecheck, String pointOk, Date ftAcceptedTime,
      String parentCode, Long isSendSmsOverdue, Long reasonInterference,
      String isSendCallBusiness, Long allowSupport, Long isSendSmsCustomerDesire,
      Long districtRecheck, String deviceType, String failureReason,
      String newFailureReason, String deviceTypeName, Long callToCd, Long hasCost,
      Long numAutoCheck, String reasonExtention, String unitApproveExtend, String reasonRejectId) {
    this.woId = woId;
    this.parentId = parentId;
    this.woCode = woCode;
    this.woContent = woContent;
    this.createDate = createDate;
    this.woSystem = woSystem;
    this.woSystemId = woSystemId;
    this.woTypeId = woTypeId;
    this.createPersonId = createPersonId;
    this.cdId = cdId;
    this.ftId = ftId;
    this.status = status;
    this.priorityId = priorityId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.finishTime = finishTime;
    this.result = result;
    this.stationId = stationId;
    this.stationCode = stationCode;
    this.lastUpdateTime = lastUpdateTime;
    this.fileName = fileName;
    this.woDescription = woDescription;
    this.summaryStatus = summaryStatus;
    this.cdReceivedAlarmTime = cdReceivedAlarmTime;
    this.cdReceivedThresholdTime = cdReceivedThresholdTime;
    this.ftReceivedAlarmTime = ftReceivedAlarmTime;
    this.ftReceivedThresholdTime = ftReceivedThresholdTime;
    this.ftProcessAlarmTime = ftProcessAlarmTime;
    this.ftProcessThresholdTime = ftProcessThresholdTime;
    this.summaryTime = summaryTime;
    this.woSystemOutId = woSystemOutId;
    this.userId = userId;
    this.woWorklogContent = woWorklogContent;
    this.numPending = numPending;
    this.endPendingTime = endPendingTime;
    this.reasonOverdueLV1Id = reasonOverdueLV1Id;
    this.reasonOverdueLV1Name = reasonOverdueLV1Name;
    this.reasonOverdueLV2Id = reasonOverdueLV2Id;
    this.reasonOverdueLV2Name = reasonOverdueLV2Name;
    this.processingGuidelines = processingGuidelines;
    this.commentComplete = commentComplete;
    this.completedTime = completedTime;
    this.isCompletedOnVsmart = isCompletedOnVsmart;
    this.isCall = isCall;
    this.lineCode = lineCode;
    this.solutionGroupId = solutionGroupId;
    this.solutionGroupName = solutionGroupName;
    this.solutionDetail = solutionDetail;
    this.cellService = cellService;
    this.concaveAreaCode = concaveAreaCode;
    this.longitude = longitude;
    this.latitude = latitude;
    this.locationCode = locationCode;
    this.reasonDetail = reasonDetail;
    this.warehouseCode = warehouseCode;
    this.needSupport = needSupport;
    this.constructionCode = constructionCode;
    this.cdAssignId = cdAssignId;
    this.deltaCloseWo = deltaCloseWo;
    this.confirmNotCreateAlarm = confirmNotCreateAlarm;
    this.alias = alias;
    this.deviceCode = deviceCode;
    this.nationCode = nationCode;
    this.syncState = syncState;
    this.stationCodeNims = stationCodeNims;
    this.reasonApproveNok = reasonApproveNok;
    this.planCode = planCode;
    this.lastTimeSmsOverdue = lastTimeSmsOverdue;
    this.amiOneId = amiOneId;
    this.nation = nation;
    this.numSupport = numSupport;
    this.estimateTime = estimateTime;
    this.distance = distance;
    this.cableCode = cableCode;
    this.cableTypeCode = cableTypeCode;
    this.vibaLineCode = vibaLineCode;
    this.numAuditFail = numAuditFail;
    this.customerTimeDesireFrom = customerTimeDesireFrom;
    this.customerTimeDesireTo = customerTimeDesireTo;
    this.numRecheck = numRecheck;
    this.pointOk = pointOk;
    this.ftAcceptedTime = ftAcceptedTime;
    this.parentCode = parentCode;
    this.isSendSmsOverdue = isSendSmsOverdue;
    this.reasonInterference = reasonInterference;
    this.isSendCallBusiness = isSendCallBusiness;
    this.allowSupport = allowSupport;
    this.isSendSmsCustomerDesire = isSendSmsCustomerDesire;
    this.districtRecheck = districtRecheck;
    this.deviceType = deviceType;
    this.failureReason = failureReason;
    this.newFailureReason = newFailureReason;
    this.deviceTypeName = deviceTypeName;
    this.callToCd = callToCd;
    this.hasCost = hasCost;
    this.numAutoCheck = numAutoCheck;
    this.reasonExtention = reasonExtention;
    this.unitApproveExtend = unitApproveExtend;
    this.reasonRejectId = reasonRejectId;
  }

  public WoInsideDTO(Long woId, Long parentId, String woCode, String woContent, Date createDate,
      String woSystem, String woSystemId, Long woTypeId, Long createPersonId, Long cdId, Long ftId,
      Long status, Long priorityId, Date startTime, Date endTime, Date finishTime, Long result,
      Long stationId, String stationCode, Date lastUpdateTime, String fileName,
      String woDescription, Long summaryStatus, Date cdReceivedAlarmTime,
      Date cdReceivedThresholdTime, Date ftReceivedAlarmTime, Date ftReceivedThresholdTime,
      Date ftProcessAlarmTime, Date ftProcessThresholdTime, Date summaryTime, String woSystemOutId,
      Long userId, String woWorklogContent, Long numPending, Date endPendingTime,
      String reasonOverdueLV1Id, String reasonOverdueLV1Name, String reasonOverdueLV2Id,
      String reasonOverdueLV2Name, String processingGuidelines, String commentComplete,
      Date completedTime, Long isCompletedOnVsmart, Long isCall, String lineCode,
      Long solutionGroupId, String solutionGroupName, String solutionDetail, String cellService,
      String concaveAreaCode, String longitude, String latitude, String locationCode,
      String reasonDetail, String warehouseCode, Long needSupport, String constructionCode,
      Long cdAssignId, Double deltaCloseWo, Long confirmNotCreateAlarm, String alias,
      String deviceCode, String nationCode, Long syncState, String stationCodeNims,
      String reasonApproveNok, String planCode, Date lastTimeSmsOverdue, String amiOneId,
      String nation, Long numSupport, Date estimateTime, String distance, String cableCode,
      String cableTypeCode, String vibaLineCode, Long numAuditFail, Date customerTimeDesireFrom,
      Date customerTimeDesireTo, Long numRecheck, String pointOk, Date ftAcceptedTime,
      String parentCode, Long isSendSmsOverdue, Long reasonInterference, String isSendCallBusiness,
      Long allowSupport, Long isSendSmsCustomerDesire, Long districtRecheck, String deviceType,
      String failureReason, String newFailureReason, String deviceTypeName, Long callToCd,
      Long hasCost, Long numAutoCheck, String resultName, String woTypeCode,
      String priorityCode, Long timeOver, WoDetailDTO woDetailDTO,
      List<WoMerchandiseInsideDTO> listWoMerchandiseInsideDTO,
      WoKTTSInfoDTO woKTTSInfoDTO,
      List<WoTestServiceMapDTO> listWoTestServiceMap,
      List<GnocFileDto> gnocFileCreateWoDtos,
      List<GnocFileDto> gnocFileDtos, Long otherSystemId, String otherSystemType,
      List<String> fileNamesDelete, String comment, String role, String auditResult,
      List<UsersInsideDto> listCd, String system, String reasonName, String reasonId,
      String customer, String phone,
      List<WoMaterialDeducteInsideDTO> lstDeducte, String reasonLv1Id, String reasonLv1Name,
      String reasonLv2Id, String reasonLv2Name, String reasonLv3Id, String reasonLv3Name,
      Boolean checkRefresh, WoHistoryInsideDTO woHistoryInsideDTO,
      WoHistoryDTO woHistoryDTO, Boolean showMaterialInfo, Boolean isShowReasonInfo,
      Boolean isShowReasonOverdueInfo, String action, String spmCode, String unitCode,
      String isSendFT, String notGetFileCfgWhenCreate, Long ccGroupId, Long telServiceId,
      String customerPhone, String customerName, String portCorrectId, String errTypeNims,
      String subscriptionId, String ccPriorityCode, Long serviceId, String effectType,
      String service, String subscriberName, String technicalClues,
      List<KeyValueDTO> lstKeyValue, String isValidWoTrouble, Date clearTime,
      Long reasonTroubleId, String reasonTroubleName, String solution, Double polesDistance,
      Long scriptId, String scriptName, Long isCcResult, String productCode,
      List<String> listFileName, List<byte[]> fileArr, String ftAudioName,
      String cdAudioName, Long requiredTtReason, String linkCode, Long linkId, Long alarmId,
      List<WoMerchandiseDTO> lstMerchandise, WoKTTSInfoDTO woKttsInfo,
      WoInsideDTO parentWo, List<String> lstUsername, List<String> lstDescription,
      List<Boolean> getFileParent,
      List<WoTestServiceMapDTO> lstWoTestService, String userCall, String crNumber,
      String crTitle, String impactNode, List<WoInsideDTO> lstWo, String isHot,
      String numComplaint, String messageCreate, String createWoType,
      String createPeronEmail, String createPersonMobile, String woGroupName,
      String woGroupEmail, String woGroupMobile, String ccResult,
      String checkQosInternetResult, String checkQosTHResult, String checkQrCode,
      String infraTypeName, String priorityColorCode, Date startTimeFrom, Date startTimeTo,
      Date endTimeFrom, Date endTimeTo, Date startDateFrom, Date startDateTo,
      Date completeTimeFrom, Date completeTimeTo, Long woTypeGroupId,
      String createPersonName, String ftName, String ftFullName, String ftMobile,
      List<String> lstStationCode, List<String> cdIdList, String parentName,
      String accountIsdn, Boolean isContainChildUnit, Long createUnitId, Long processUnitId,
      Boolean isCreated, Boolean isCd, Boolean isFt, Boolean isNeedSupport,
      String statusSearchWeb, String woTypeName, String statusName, String priorityName,
      String comments, String cdName, Double remainTime, String kedbCode, Long kedbId,
      Long contractId, Long processActionId, Long ableMop, String createUnitName,
      Long ccServiceId, Long infraType, Long hasWorklog, String contractCode,
      String processActionName, String contractPartner, String lineCutCode,
      String codeSnippetOff, String closuresReplace, String user, String woSourceCode,
      String cdGroupCode, String isSendCd, String accountGline, String woWorklog,
      String acountUplink, String cdGroupType, String connectorCode, String deptCode,
      Long typeOperation, CrFilesAttachInsiteDTO crFilesAttachInsiteDTO,
      MopGnoc mopGnoc) {
    this.woId = woId;
    this.parentId = parentId;
    this.woCode = woCode;
    this.woContent = woContent;
    this.createDate = createDate;
    this.woSystem = woSystem;
    this.woSystemId = woSystemId;
    this.woTypeId = woTypeId;
    this.createPersonId = createPersonId;
    this.cdId = cdId;
    this.ftId = ftId;
    this.status = status;
    this.priorityId = priorityId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.finishTime = finishTime;
    this.result = result;
    this.stationId = stationId;
    this.stationCode = stationCode;
    this.lastUpdateTime = lastUpdateTime;
    this.fileName = fileName;
    this.woDescription = woDescription;
    this.summaryStatus = summaryStatus;
    this.cdReceivedAlarmTime = cdReceivedAlarmTime;
    this.cdReceivedThresholdTime = cdReceivedThresholdTime;
    this.ftReceivedAlarmTime = ftReceivedAlarmTime;
    this.ftReceivedThresholdTime = ftReceivedThresholdTime;
    this.ftProcessAlarmTime = ftProcessAlarmTime;
    this.ftProcessThresholdTime = ftProcessThresholdTime;
    this.summaryTime = summaryTime;
    this.woSystemOutId = woSystemOutId;
    this.userId = userId;
    this.woWorklogContent = woWorklogContent;
    this.numPending = numPending;
    this.endPendingTime = endPendingTime;
    this.reasonOverdueLV1Id = reasonOverdueLV1Id;
    this.reasonOverdueLV1Name = reasonOverdueLV1Name;
    this.reasonOverdueLV2Id = reasonOverdueLV2Id;
    this.reasonOverdueLV2Name = reasonOverdueLV2Name;
    this.processingGuidelines = processingGuidelines;
    this.commentComplete = commentComplete;
    this.completedTime = completedTime;
    this.isCompletedOnVsmart = isCompletedOnVsmart;
    this.isCall = isCall;
    this.lineCode = lineCode;
    this.solutionGroupId = solutionGroupId;
    this.solutionGroupName = solutionGroupName;
    this.solutionDetail = solutionDetail;
    this.cellService = cellService;
    this.concaveAreaCode = concaveAreaCode;
    this.longitude = longitude;
    this.latitude = latitude;
    this.locationCode = locationCode;
    this.reasonDetail = reasonDetail;
    this.warehouseCode = warehouseCode;
    this.needSupport = needSupport;
    this.constructionCode = constructionCode;
    this.cdAssignId = cdAssignId;
    this.deltaCloseWo = deltaCloseWo;
    this.confirmNotCreateAlarm = confirmNotCreateAlarm;
    this.alias = alias;
    this.deviceCode = deviceCode;
    this.nationCode = nationCode;
    this.syncState = syncState;
    this.stationCodeNims = stationCodeNims;
    this.reasonApproveNok = reasonApproveNok;
    this.planCode = planCode;
    this.lastTimeSmsOverdue = lastTimeSmsOverdue;
    this.amiOneId = amiOneId;
    this.nation = nation;
    this.numSupport = numSupport;
    this.estimateTime = estimateTime;
    this.distance = distance;
    this.cableCode = cableCode;
    this.cableTypeCode = cableTypeCode;
    this.vibaLineCode = vibaLineCode;
    this.numAuditFail = numAuditFail;
    this.customerTimeDesireFrom = customerTimeDesireFrom;
    this.customerTimeDesireTo = customerTimeDesireTo;
    this.numRecheck = numRecheck;
    this.pointOk = pointOk;
    this.ftAcceptedTime = ftAcceptedTime;
    this.parentCode = parentCode;
    this.isSendSmsOverdue = isSendSmsOverdue;
    this.reasonInterference = reasonInterference;
    this.isSendCallBusiness = isSendCallBusiness;
    this.allowSupport = allowSupport;
    this.isSendSmsCustomerDesire = isSendSmsCustomerDesire;
    this.districtRecheck = districtRecheck;
    this.deviceType = deviceType;
    this.failureReason = failureReason;
    this.newFailureReason = newFailureReason;
    this.deviceTypeName = deviceTypeName;
    this.callToCd = callToCd;
    this.hasCost = hasCost;
    this.numAutoCheck = numAutoCheck;
    this.offSetFromUser = offSetFromUser;
    this.resultName = resultName;
    this.woTypeCode = woTypeCode;
    this.priorityCode = priorityCode;
    this.timeOver = timeOver;
    this.woDetailDTO = woDetailDTO;
    this.listWoMerchandiseInsideDTO = listWoMerchandiseInsideDTO;
    this.woKTTSInfoDTO = woKTTSInfoDTO;
    this.listWoTestServiceMap = listWoTestServiceMap;
    this.gnocFileCreateWoDtos = gnocFileCreateWoDtos;
    this.gnocFileDtos = gnocFileDtos;
    this.otherSystemId = otherSystemId;
    this.otherSystemType = otherSystemType;
    this.fileNamesDelete = fileNamesDelete;
    this.comment = comment;
    this.role = role;
    this.auditResult = auditResult;
    this.listCd = listCd;
    this.system = system;
    this.reasonName = reasonName;
    this.reasonId = reasonId;
    this.customer = customer;
    this.phone = phone;
    this.lstDeducte = lstDeducte;
    this.reasonLv1Id = reasonLv1Id;
    this.reasonLv1Name = reasonLv1Name;
    this.reasonLv2Id = reasonLv2Id;
    this.reasonLv2Name = reasonLv2Name;
    this.reasonLv3Id = reasonLv3Id;
    this.reasonLv3Name = reasonLv3Name;
    this.checkRefresh = checkRefresh;
    this.woHistoryInsideDTO = woHistoryInsideDTO;
    this.woHistoryDTO = woHistoryDTO;
    this.showMaterialInfo = showMaterialInfo;
    this.isShowReasonInfo = isShowReasonInfo;
    this.isShowReasonOverdueInfo = isShowReasonOverdueInfo;
    this.action = action;
    this.spmCode = spmCode;
    this.unitCode = unitCode;
    this.isSendFT = isSendFT;
    this.notGetFileCfgWhenCreate = notGetFileCfgWhenCreate;
    this.ccGroupId = ccGroupId;
    this.telServiceId = telServiceId;
    this.customerPhone = customerPhone;
    this.customerName = customerName;
    this.portCorrectId = portCorrectId;
    this.errTypeNims = errTypeNims;
    this.subscriptionId = subscriptionId;
    this.ccPriorityCode = ccPriorityCode;
    this.serviceId = serviceId;
    this.effectType = effectType;
    this.service = service;
    this.subscriberName = subscriberName;
    this.technicalClues = technicalClues;
    this.lstKeyValue = lstKeyValue;
    this.isValidWoTrouble = isValidWoTrouble;
    this.clearTime = clearTime;
    this.reasonTroubleId = reasonTroubleId;
    this.reasonTroubleName = reasonTroubleName;
    this.solution = solution;
    this.polesDistance = polesDistance;
    this.scriptId = scriptId;
    this.scriptName = scriptName;
    this.isCcResult = isCcResult;
    this.productCode = productCode;
    this.listFileName = listFileName;
    this.fileArr = fileArr;
    this.ftAudioName = ftAudioName;
    this.cdAudioName = cdAudioName;
    this.requiredTtReason = requiredTtReason;
    this.linkCode = linkCode;
    this.linkId = linkId;
    this.alarmId = alarmId;
    this.lstMerchandise = lstMerchandise;
    this.woKttsInfo = woKttsInfo;
    this.parentWo = parentWo;
    this.lstUsername = lstUsername;
    this.lstDescription = lstDescription;
    this.getFileParent = getFileParent;
    this.lstWoTestService = lstWoTestService;
    this.userCall = userCall;
    this.crNumber = crNumber;
    this.crTitle = crTitle;
    this.impactNode = impactNode;
    this.lstWo = lstWo;
    this.isHot = isHot;
    this.numComplaint = numComplaint;
    this.messageCreate = messageCreate;
    this.createWoType = createWoType;
    this.createPeronEmail = createPeronEmail;
    this.createPersonMobile = createPersonMobile;
    this.woGroupName = woGroupName;
    this.woGroupEmail = woGroupEmail;
    this.woGroupMobile = woGroupMobile;
    this.ccResult = ccResult;
    this.checkQosInternetResult = checkQosInternetResult;
    this.checkQosTHResult = checkQosTHResult;
    this.checkQrCode = checkQrCode;
    this.infraTypeName = infraTypeName;
    this.priorityColorCode = priorityColorCode;
    this.startTimeFrom = startTimeFrom;
    this.startTimeTo = startTimeTo;
    this.endTimeFrom = endTimeFrom;
    this.endTimeTo = endTimeTo;
    this.startDateFrom = startDateFrom;
    this.startDateTo = startDateTo;
    this.completeTimeFrom = completeTimeFrom;
    this.completeTimeTo = completeTimeTo;
    this.woTypeGroupId = woTypeGroupId;
    this.createPersonName = createPersonName;
    this.ftName = ftName;
    this.ftFullName = ftFullName;
    this.ftMobile = ftMobile;
    this.lstStationCode = lstStationCode;
    this.cdIdList = cdIdList;
    this.parentName = parentName;
    this.accountIsdn = accountIsdn;
    this.isContainChildUnit = isContainChildUnit;
    this.createUnitId = createUnitId;
    this.processUnitId = processUnitId;
    this.isCreated = isCreated;
    this.isCd = isCd;
    this.isFt = isFt;
    this.isNeedSupport = isNeedSupport;
    this.statusSearchWeb = statusSearchWeb;
    this.woTypeName = woTypeName;
    this.statusName = statusName;
    this.priorityName = priorityName;
    this.comments = comments;
    this.cdName = cdName;
    this.remainTime = remainTime;
    this.kedbCode = kedbCode;
    this.kedbId = kedbId;
    this.contractId = contractId;
    this.processActionId = processActionId;
    this.ableMop = ableMop;
    this.createUnitName = createUnitName;
    this.ccServiceId = ccServiceId;
    this.infraType = infraType;
    this.hasWorklog = hasWorklog;
    this.contractCode = contractCode;
    this.processActionName = processActionName;
    this.contractPartner = contractPartner;
    this.lineCutCode = lineCutCode;
    this.codeSnippetOff = codeSnippetOff;
    this.closuresReplace = closuresReplace;
    this.user = user;
    this.woSourceCode = woSourceCode;
    this.cdGroupCode = cdGroupCode;
    this.isSendCd = isSendCd;
    this.accountGline = accountGline;
    this.woWorklog = woWorklog;
    this.acountUplink = acountUplink;
    this.cdGroupType = cdGroupType;
    this.connectorCode = connectorCode;
    this.deptCode = deptCode;
    this.typeOperation = typeOperation;
    this.crFilesAttachInsiteDTO = crFilesAttachInsiteDTO;
    this.mopGnoc = mopGnoc;
  }

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
        hasCost, numAutoCheck, syncStatus
        , StringUtils.validString(startWorkingTime) ? DateTimeUtils
        .convertStringToDate(startWorkingTime) : null
        , startLongitude, startLatitude,
        StringUtils.validString(workStatus) ? Long.valueOf(workStatus) : null,
        reasonExtention, unitApproveExtend,
        StringUtils.validString(reasonRejectId) ? Long.valueOf(reasonRejectId) : null
    );
  }

  public WoDTOSearch toWoDTOSearch() {
    try {
      WoDTOSearch woDTOSearch = new WoDTOSearch(
          StringUtils.validString(woId) ? String.valueOf(woId) : null,
          StringUtils.validString(parentId) ? String.valueOf(woId) : null,
          parentName,
          woCode,
          woContent,
          woSystem,
          woSystemId,
          woSystemOutId,
          StringUtils.validString(createPersonId) ? String.valueOf(createPersonId) : null,
          createPersonName,
          StringUtils.validString(createDate) ? createDate.toString()
              : null,
          StringUtils.validString(woTypeId) ? String.valueOf(woTypeId) : null,
          woTypeName,
          StringUtils.validString(cdId) ? String.valueOf(cdId) : null,
          cdName,
          StringUtils.validString(ftId) ? String.valueOf(ftId) : null,
          ftName,
          StringUtils.validString(status) ? String.valueOf(status) : null,
          statusName,
          StringUtils.validString(priorityId) ? String.valueOf(priorityId) : null,
          priorityName,
          StringUtils.validString(startTime) ? startTime.toString() : null,
          StringUtils.validString(startTimeFrom) ? startTimeFrom.toString()
              : null,
          StringUtils.validString(startTimeTo) ? startTimeTo.toString()
              : null,
          StringUtils.validString(endTime) ? endTime.toString() : null,
          StringUtils.validString(endTimeFrom) ? endTimeFrom.toString()
              : null,
          StringUtils.validString(endTimeTo) ? endTimeTo.toString() : null,
          StringUtils.validString(finishTime) ? finishTime.toString()
              : null,
          StringUtils.validString(result) ? String.valueOf(result) : null,
          StringUtils.validString(stationId) ? String.valueOf(stationId) : null,
          stationCode,
          StringUtils.validString(lastUpdateTime) ? lastUpdateTime.toString() : null,
          fileName,
          comments,
          StringUtils.validString(userId) ? String.valueOf(userId) : null,
          createPeronEmail,
          createPersonMobile,
          woGroupName,
          woGroupEmail,
          woGroupMobile,
          woDescription,
          StringUtils.validString(createUnitId) ? String.valueOf(createUnitId) : null,
          StringUtils.validString(processUnitId) ? String.valueOf(processUnitId) : null,
          accountIsdn,
          StringUtils.validString(serviceId) ? String.valueOf(serviceId) : null,
          StringUtils.validString(infraType) ? String.valueOf(infraType) : null,
          StringUtils.validString(ccServiceId) ? String.valueOf(ccServiceId) : null,
          StringUtils.validString(ccGroupId) ? String.valueOf(ccGroupId) : null,
          StringUtils.validString(isCcResult) ? String.valueOf(isCcResult) : null,
          ccResult,
          checkQosInternetResult,
          checkQosTHResult,
          checkQrCode,
          woTypeCode,
          StringUtils.validString(endPendingTime) ? endPendingTime.toString() : null,
          reasonName,
          reasonId,
          customer,
          phone,
          reasonOverdueLV1Id,
          reasonOverdueLV1Name,
          reasonOverdueLV2Id,
          reasonOverdueLV2Name,
          StringUtils.validString(completedTime) ? completedTime.toString()
              : null,
          commentComplete,
          longitude,
          latitude,
          kedbCode,
          StringUtils.validString(kedbId) ? String.valueOf(kedbId) : null,
          StringUtils.validString(requiredTtReason) ? String.valueOf(requiredTtReason) : null,
          contractCode,
          scriptName,
          warehouseCode,
          constructionCode,
          numComplaint,
          customerPhone,
          infraTypeName,
          StringUtils.validString(ableMop) ? String.valueOf(ableMop) : null,
          deviceCode,
          planCode,
          linkCode,
          StringUtils.validString(linkId) ? String.valueOf(linkId) : null,
          StringUtils.validString(alarmId) ? String.valueOf(alarmId) : null,
          cdAudioName,
          ftAudioName,
          amiOneId,
          distance,
          cableCode,
          cableTypeCode,
          StringUtils.validString(customerTimeDesireFrom) ? customerTimeDesireFrom.toString()
              : null,
          StringUtils.validString(customerTimeDesireTo) ? customerTimeDesireTo.toString() : null,
          portCorrectId,
          errTypeNims,
          subscriptionId,
          StringUtils.validString(numRecheck) ? String.valueOf(numRecheck) : null,
          StringUtils.validString(districtRecheck) ? String.valueOf(districtRecheck) : null,
          locationCode,
          pointOk,
          priorityColorCode,
          deviceType,
          deviceTypeName,
          failureReason,
          customerName
      );
      return woDTOSearch;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public WoDTO toModelOutSide() {
    WoDTO model = new WoDTO(
        StringUtils.validString(woId) ? String.valueOf(woId) : null,
        StringUtils.validString(parentId) ? String.valueOf(parentId) : null,
        woCode,
        woContent,
        StringUtils.validString(createDate) ? DateTimeUtils.date2ddMMyyyyHHMMss(createDate) : null,
        woSystem,
        woSystemId,
        StringUtils.validString(woTypeId) ? String.valueOf(woTypeId) : null,
        StringUtils.validString(createPersonId) ? String.valueOf(createPersonId) : null,
        StringUtils.validString(cdId) ? String.valueOf(cdId) : null,
        StringUtils.validString(ftId) ? String.valueOf(ftId) : null,
        StringUtils.validString(status) ? String.valueOf(status) : null,
        StringUtils.validString(priorityId) ? String.valueOf(priorityId) : null,
        StringUtils.validString(startTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(startTime) : null,
        StringUtils.validString(endTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(endTime) : null,
        StringUtils.validString(finishTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(finishTime) : null,
        StringUtils.validString(result) ? String.valueOf(result) : null,
        StringUtils.validString(stationId) ? String.valueOf(stationId) : null,
        stationCode,
        StringUtils.validString(lastUpdateTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(lastUpdateTime)
            : null,
        fileName,
        woDescription,
        woSystemOutId,
        null,
        StringUtils.validString(numPending) ? String.valueOf(numPending) : null,
        StringUtils.validString(endPendingTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(endPendingTime)
            : null,
        reasonOverdueLV1Id,
        reasonOverdueLV1Name,
        reasonOverdueLV2Id,
        reasonOverdueLV2Name,
        null,
        commentComplete,
        StringUtils.validString(completedTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(completedTime)
            : null,
        StringUtils.validString(isCompletedOnVsmart) ? String.valueOf(isCompletedOnVsmart) : null,
        isCall,
        lineCode,
        StringUtils.validString(solutionGroupId) ? String.valueOf(solutionGroupId) : null,
        solutionGroupName,
        solutionDetail,
        cellService,
        concaveAreaCode,
        longitude,
        latitude,
        locationCode,
        reasonDetail,
        warehouseCode,
        StringUtils.validString(needSupport) ? String.valueOf(needSupport) : null,
        constructionCode,
        StringUtils.validString(cdAssignId) ? String.valueOf(cdAssignId) : null,
        StringUtils.validString(deltaCloseWo) ? String.valueOf(deltaCloseWo) : null,
        StringUtils.validString(confirmNotCreateAlarm) ? String.valueOf(confirmNotCreateAlarm)
            : null,
        null,
        deviceCode,
        nationCode,
        null,
        null,
        null,
        planCode,
        amiOneId,
        StringUtils.validString(numSupport) ? String.valueOf(numSupport) : null,
        StringUtils.validString(estimateTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(estimateTime)
            : null,
        distance,
        cableCode,
        cableTypeCode,
        vibaLineCode,
        StringUtils.validString(numAuditFail) ? String.valueOf(numAuditFail) : null,
        StringUtils.validString(customerTimeDesireFrom) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(customerTimeDesireFrom) : null,
        StringUtils.validString(customerTimeDesireTo) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(customerTimeDesireTo) : null,
        StringUtils.validString(numRecheck) ? String.valueOf(numRecheck) : null,
        pointOk,
        StringUtils.validString(ftAcceptedTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(ftAcceptedTime)
            : null,
        parentCode,
        StringUtils.validString(isSendSmsOverdue) ? String.valueOf(isSendSmsOverdue) : null,
        StringUtils.validString(reasonInterference) ? String.valueOf(reasonInterference) : null,
        null,
        StringUtils.validString(allowSupport) ? String.valueOf(allowSupport) : null,
        deviceType,
        failureReason,
        newFailureReason,
        deviceTypeName,
        StringUtils.validString(callToCd) ? String.valueOf(callToCd) : null,
        StringUtils.validString(hasCost) ? String.valueOf(hasCost) : null,
        StringUtils.validString(numAutoCheck) ? String.valueOf(numAutoCheck) : null,
        woTypeCode,
        priorityCode,
        woHistoryDTO,
        spmCode,
        unitCode,
        isSendFT,
        notGetFileCfgWhenCreate,
        StringUtils.validString(ccGroupId) ? String.valueOf(ccGroupId) : null,
        StringUtils.validString(telServiceId) ? String.valueOf(telServiceId) : null,
        customerPhone,
        customerName,
        portCorrectId,
        errTypeNims,
        subscriptionId,
        ccPriorityCode,
        StringUtils.validString(serviceId) ? String.valueOf(serviceId) : null,
        effectType,
        service,
        subscriberName,
        technicalClues,
        lstKeyValue,
        isValidWoTrouble,
        StringUtils.validString(clearTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(clearTime) : null,
        StringUtils.validString(reasonTroubleId) ? String.valueOf(reasonTroubleId) : null,
        reasonTroubleName,
        solution,
        StringUtils.validString(polesDistance) ? String.valueOf(polesDistance) : null,
        StringUtils.validString(scriptId) ? String.valueOf(scriptId) : null,
        scriptName,
        StringUtils.validString(isCcResult) ? String.valueOf(isCcResult) : null,
        productCode,
        listFileName,
        fileArr,
        ftAudioName,
        cdAudioName,
        StringUtils.validString(requiredTtReason) ? String.valueOf(requiredTtReason) : null,
        linkCode,
        StringUtils.validString(linkId) ? String.valueOf(linkId) : null,
        StringUtils.validString(alarmId) ? String.valueOf(alarmId) : null,
        lstMerchandise,
        isHot,
        numComplaint,
        messageCreate,
        createWoType,
        ccResult,
        checkQosInternetResult,
        checkQosTHResult,
        checkQrCode,
        createPersonName,
        ftName,
        lstStationCode,
        cdIdList,
        accountIsdn,
        kedbCode,
        StringUtils.validString(kedbId) ? String.valueOf(kedbId) : null,
        StringUtils.validString(ableMop) ? String.valueOf(ableMop) : null,
        StringUtils.validString(ccServiceId) ? String.valueOf(ccServiceId) : null,
        StringUtils.validString(infraType) ? String.valueOf(infraType) : null,
        lineCutCode,
        codeSnippetOff,
        closuresReplace,
        woSourceCode,
        cdGroupCode,
        isSendCd,
        accountGline,
        woWorklog,
        acountUplink,
        woKttsInfo,
        null,
        cdGroupType,
        connectorCode,
        deptCode,
        null,
        null,
        null,
        null,
        null,
        customerGroupType,
        ktrKeyPoint,
        contentUpgradeAndDownGrade,
        activeEnvironmentId,
        null,
        null,
        null,
        null,
        null,
        reasonRejectId
    );
    return model;
  }
}
