package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.KeyValueDTO;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class WoDTO {

  private String woId;
  private String parentId;
  private String woCode;
  private String woContent;
  private String createDate;
  private String woSystem;
  private String woSystemId;
  private String woTypeId;
  private String createPersonId;
  private String cdId;
  private String ftId;
  private String status;
  private String priorityId;
  private String startTime;
  private String endTime;
  private String finishTime;
  private String result;
  private String stationId;
  private String stationCode;
  private String lastUpdateTime;
  private String fileName;
  private String woDescription;
  private String woSystemOutId;
  private String woWorklogContent;
  private String numPending;
  private String endPendingTime;
  private String reasonOverdueLV1Id;
  private String reasonOverdueLV1Name;
  private String reasonOverdueLV2Id;
  private String reasonOverdueLV2Name;
  private String processingGuidelines;
  private String commentComplete;
  private String completedTime;
  private String isCompletedOnVsmart;
  private Long isCall;
  private String lineCode;
  private String solutionGroupId;
  private String solutionGroupName;
  private String solutionDetail;
  private String cellService;
  private String concaveAreaCode;
  private String longitude;
  private String latitude;
  private String locationCode;
  private String reasonDetail;
  private String warehouseCode;
  private String needSupport;//Id he thong
  private String constructionCode;
  private String cdAssignId;
  private String deltaCloseWo;
  private String confirmNotCreateAlarm;
  private String alias;
  private String deviceCode;
  private String nationCode;
  private String syncState;
  private String stationCodeNims;
  private String reasonApproveNok;
  private String planCode;
  private String amiOneId;
  private String numSupport;//Id he thong
  private String estimateTime;
  private String distance;
  private String cableCode;
  private String cableTypeCode;
  private String vibaLineCode;
  private String numAuditFail;
  private String customerTimeDesireFrom;
  private String customerTimeDesireTo;
  private String numRecheck;
  private String pointOk;
  private String ftAcceptedTime;
  private String parentCode;
  private String isSendSmsOverdue;
  private String reasonInterference;
  private String isSendCallBusiness;
  private String allowSupport;
  private String deviceType;
  private String failureReason;
  private String newFailureReason;
  private String deviceTypeName;
  private String callToCd;
  private String hasCost;
  private String numAutoCheck;
  private String woTypeCode;
  private String priorityCode;
  private WoHistoryDTO woHistoryDTO;
  private String spmCode;
  private String unitCode;
  private String isSendFT;
  private String notGetFileCfgWhenCreate;
  private String ccGroupId;
  private String telServiceId;
  private String customerPhone;
  private String customerName;
  private String portCorrectId;
  private String errTypeNims;
  private String subscriptionId;
  private String ccPriorityCode;
  private String serviceId;
  private String effectType;
  private String service;
  private String subscriberName;
  private String technicalClues;
  private List<KeyValueDTO> lstKeyValue;
  private String isValidWoTrouble;
  private String clearTime;
  private String reasonTroubleId;
  private String reasonTroubleName;
  private String solution;
  private String polesDistance; // khoang cot
  private String scriptId;  // id kich ban
  private String scriptName;  // id kich ban
  private String isCcResult;
  private String productCode;
  private List<String> listFileName;
  private List<byte[]> fileArr;
  private String ftAudioName;//ten file am thanh ft
  private String cdAudioName;//ten file am thanh cd
  private String requiredTtReason;
  private String linkCode; //ma tuyen
  private String linkId;   //id tuyen
  private String alarmId;//id khieu nai
  //  private List<WoMerchandiseInsideDTO> lstMerchandise;
  private List<WoMerchandiseDTO> lstMerchandise;
  private String isHot;
  private String numComplaint;
  private String messageCreate;
  private String createWoType;
  private String ccResult;
  private String checkQosInternetResult;
  private String checkQosTHResult;
  private String checkQrCode;
  private String createPersonName;
  private String ftName;
  private List<String> lstStationCode;
  private List<String> cdIdList;
  private String accountIsdn;
  private String kedbCode;
  private String kedbId;
  private String ableMop;
  private String ccServiceId;
  private String infraType;
  private String lineCutCode;// ma tuyen dut
  private String codeSnippetOff;// ma doan dut
  private String closuresReplace; // mang xong thay the
  private String woSourceCode;
  private String cdGroupCode;
  private String isSendCd;
  private String accountGline;
  private String woWorklog;
  private String acountUplink;
  private WoKTTSInfoDTO woKttsInfo;
  private String defaultSortField = "woCode";

  private String cdGroupType;
  private String connectorCode;
  private String deptCode;
  private Long syncStatus;

  //tiennv them cho nang cap import
  private String assign;
  private String importBusinessType;
  private String importBusinessCode;
  private String resultImport;
  private String customerGroupType;
  private String ktrKeyPoint;

  //tiennv them cho nang cap import tai san
  private String contentUpgradeAndDownGrade;

  //thangdt nang cap ws insertWoKTTS
  private Long activeEnvironmentId;
  private String proxyLocale;

  //tiennv bo sung ham cho vsmart
  private String startWorkingTime;
  private String startLongitude;
  private String startLatitude;
  private String workStatus;
  private String reasonRejectId;

  public WoInsideDTO toModelInSide() {
    WoInsideDTO model = new WoInsideDTO(
        StringUtils.validString(woId) ? Long.valueOf(woId) : null,
        StringUtils.validString(parentId) ? Long.valueOf(parentId) : null,
        woCode,
        woContent,
        StringUtils.validString(createDate) ? DateTimeUtils.convertStringToDate(createDate)
            : null,
        woSystem,
        woSystemId,
        StringUtils.validString(woTypeId) ? Long.valueOf(woTypeId) : null,
        StringUtils.validString(createPersonId) ? Long.valueOf(createPersonId) : null,
        StringUtils.validString(cdId) ? Long.valueOf(cdId) : null,
        StringUtils.validString(ftId) ? Long.valueOf(ftId) : null,
        StringUtils.validString(status) ? Long.valueOf(status) : null,
        StringUtils.validString(priorityId) ? Long.valueOf(priorityId) : null,
        StringUtils.validString(startTime) ? DateTimeUtils.convertStringToDate(startTime)
            : null,
        StringUtils.validString(endTime) ? DateTimeUtils.convertStringToDate(endTime)
            : null,
        StringUtils.validString(finishTime) ? DateTimeUtils.convertStringToDate(finishTime)
            : null,
        StringUtils.validString(result) ? Long.valueOf(result) : null,
        StringUtils.validString(stationId) ? Long.valueOf(stationId) : null,
        stationCode,
        StringUtils.validString(lastUpdateTime) ? DateTimeUtils
            .convertStringToDate(lastUpdateTime)
            : null,
        fileName,
        woDescription,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        woSystemOutId,
        null,
        woWorklogContent,
        StringUtils.validString(numPending) ? Long.valueOf(numPending) : null,
        StringUtils.validString(endPendingTime) ? DateTimeUtils
            .convertStringToDate(endPendingTime) : null,
        reasonOverdueLV1Id,
        reasonOverdueLV1Name,
        reasonOverdueLV2Id,
        reasonOverdueLV2Name,
        processingGuidelines,
        commentComplete,
        StringUtils.validString(completedTime) ? DateTimeUtils.convertStringToDate(completedTime)
            : null,
        StringUtils.validString(isCompletedOnVsmart) ? Long.valueOf(isCompletedOnVsmart) : null,
        isCall,
        lineCode,
        StringUtils.validString(solutionGroupId) ? Long.valueOf(solutionGroupId) : null,
        solutionGroupName,
        solutionDetail,
        cellService,
        concaveAreaCode,
        longitude,
        latitude,
        locationCode,
        reasonDetail,
        warehouseCode,
        StringUtils.validString(needSupport) ? Long.valueOf(needSupport) : null,
        constructionCode,
        StringUtils.validString(cdAssignId) ? Long.valueOf(cdAssignId) : null,
        StringUtils.validString(deltaCloseWo) ? Double.valueOf(deltaCloseWo) : null,
        StringUtils.validString(confirmNotCreateAlarm) ? Long.valueOf(confirmNotCreateAlarm)
            : null,
        alias,
        deviceCode,
        nationCode,
        StringUtils.validString(syncState) ? Long.valueOf(syncState) : null,
        stationCodeNims,
        reasonApproveNok,
        planCode,
        null,
        amiOneId,
        null,
        StringUtils.validString(numSupport) ? Long.valueOf(numSupport) : null,
        StringUtils.validString(estimateTime) ? DateTimeUtils.convertStringToDate(estimateTime)
            : null,
        distance,
        cableCode,
        cableTypeCode,
        vibaLineCode,
        StringUtils.validString(numAuditFail) ? Long.valueOf(numAuditFail) : null,
        StringUtils.validString(customerTimeDesireFrom) ? DateTimeUtils
            .convertStringToDate(customerTimeDesireFrom) : null,
        StringUtils.validString(customerTimeDesireTo) ? DateTimeUtils
            .convertStringToDate(customerTimeDesireTo) : null,
        StringUtils.validString(numRecheck) ? Long.valueOf(numRecheck) : null,
        pointOk,
        StringUtils.validString(ftAcceptedTime) ? DateTimeUtils
            .convertStringToDate(ftAcceptedTime) : null,
        parentCode,
        StringUtils.validString(isSendSmsOverdue) ? Long.valueOf(isSendSmsOverdue) : null,
        StringUtils.validString(reasonInterference) ? Long.valueOf(reasonInterference) : null,
        isSendCallBusiness,
        StringUtils.validString(allowSupport) ? Long.valueOf(allowSupport) : null,
        null,
        null,
        deviceType,
        failureReason,
        newFailureReason,
        deviceTypeName,
        StringUtils.validString(callToCd) ? Long.valueOf(callToCd) : null,
        StringUtils.validString(hasCost) ? Long.valueOf(hasCost) : null,
        StringUtils.validString(numAutoCheck) ? Long.valueOf(numAutoCheck) : null,
//        null,
        null,
        woTypeCode,
        priorityCode,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        woHistoryDTO,
        null,
        null,
        null,
        null,
        spmCode,
        unitCode,
        isSendFT,
        notGetFileCfgWhenCreate,
        StringUtils.validString(ccGroupId) ? Long.valueOf(ccGroupId) : null,
        StringUtils.validString(telServiceId) ? Long.valueOf(telServiceId) : null,
        customerPhone,
        customerName,
        portCorrectId,
        errTypeNims,
        subscriptionId,
        ccPriorityCode,
        StringUtils.validString(serviceId) ? Long.valueOf(serviceId) : null,
        effectType,
        service,
        subscriberName,
        technicalClues,
        lstKeyValue,
        isValidWoTrouble,
        StringUtils.validString(clearTime) ? DateTimeUtils.convertStringToDate(clearTime) : null,
        StringUtils.validString(reasonTroubleId) ? Long.valueOf(reasonTroubleId) : null,
        reasonTroubleName,
        solution,
        StringUtils.validString(polesDistance) ? Double.valueOf(polesDistance) : null,
        StringUtils.validString(scriptId) ? Long.valueOf(scriptId) : null,
        scriptName,
        StringUtils.validString(isCcResult) ? Long.valueOf(isCcResult) : null,
        productCode,
        listFileName,
        fileArr,
        ftAudioName,
        cdAudioName,
        StringUtils.validString(requiredTtReason) ? Long.valueOf(requiredTtReason) : null,
        linkCode,
        StringUtils.validString(linkId) ? Long.valueOf(linkId) : null,
        StringUtils.validString(alarmId) ? Long.valueOf(alarmId) : null,
        lstMerchandise,
        woKttsInfo,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        isHot,
        numComplaint,
        messageCreate,
        createWoType,
        null,
        null,
        null,
        null,
        null,
        ccResult,
        checkQosInternetResult,
        checkQosTHResult,
        checkQrCode,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        createPersonName,
        ftName,
        null,
        null,
        lstStationCode,
        cdIdList,
        null,
        accountIsdn,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        kedbCode,
        StringUtils.validString(kedbId) ? Long.valueOf(kedbId) : null,
        null,
        null,
        StringUtils.validString(ableMop) ? Long.valueOf(ableMop) : null,
        null,
        StringUtils.validString(ccServiceId) ? Long.valueOf(ccServiceId) : null,
        StringUtils.validString(infraType) ? Long.valueOf(infraType) : null,
        null,
        null,
        null,
        null,
        lineCutCode,
        codeSnippetOff,
        closuresReplace,
        null,
        woSourceCode,
        cdGroupCode,
        isSendCd,
        accountGline,
        woWorklog,
        acountUplink,
        cdGroupType,
        connectorCode,
        deptCode,
        null,
        null,
        null
    );
    model.setCustomerGroupType(this.customerGroupType);
    model.setKtrKeyPoint(ktrKeyPoint);
    model.setActiveEnvironmentId(this.activeEnvironmentId);
    model.setReasonRejectId(this.reasonRejectId);
    return model;
  }
}
