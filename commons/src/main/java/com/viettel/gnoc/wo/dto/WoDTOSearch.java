package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoDTOSearch extends BaseDto {

  private String woId;
  private String parentId;
  private String parentName;
  private String woCode;
  private String woContent;
  private String woSystem;
  private String woSystemId;
  private String woSystemOutId;
  private String createPersonId;
  private String createPersonName;
  private String createDate;
  private String woTypeId;
  private String woTypeName;
  private String cdId;
  private String cdName;
  private String ftId;
  private String ftName;
  private String status;
  private String statusName;
  private String priorityId;
  private String priorityName;
  private String startTime;
  private String startTimeFrom;
  private String startTimeTo;
  private String endTime;
  private String endTimeFrom;
  private String endTimeTo;
  private String finishTime;
  private String result;
  private String stationId;
  private String stationCode;
  private String lastUpdateTime;
  private String fileName;
  private String comments;
  private static long changedTime = 0;

  private String userId;
  private String createPeronEmail;
  private String createPersonMobile;
  private String woGroupName;
  private String woGroupEmail;
  private String woGroupMobile;
  private String woDescription;
  private List<ResultDTO> listFileName;

  private Boolean isCreated;
  private Boolean isCd;
  private Boolean isFt;
  private String createUnitId;
  private String processUnitId;
  private Boolean isContainChildUnit;

  private String accountIsdn;
  private String serviceId;
  private String infraType;
  private String ccServiceId;
  private String ccGroupId;
  private String isCcResult;
  private String ccResult;
  private String checkQosInternetResult;
  private String checkQosTHResult;
  private String checkQrCode;
  private String woTypeCode;

  private String endPendingTime;
  private String reasonName;
  private String reasonId;
  private String customer;
  private String phone;

  private String reasonOverdueLV1Id;
  private String reasonOverdueLV1Name;
  private String reasonOverdueLV2Id;
  private String reasonOverdueLV2Name;

  private String completedTime;
  private String commentComplete;
  private String isNeedSupport;

  private String longitude;
  private String latitude;

  private String kedbCode;
  private String kedbId;
  private String requiredTtReason;
  private String contractCode;
  private String scriptName;

  private String warehouseCode;
  private String constructionCode;

  private String numComplaint;
  private String customerPhone;
  private String infraTypeName;
  private String ableMop;
  private String deviceCode;
  private String planCode;

  private String linkCode; //ma tuyen
  private String linkId;   //id tuyen
  private String alarmId;//id khieu nai
  private String cdAudioName;//ten file am thanh cd
  private String ftAudioName;//ten file am thanh ft

  private String amiOneId;
  private String distance;
  private String cableCode;
  private String cableTypeCode;

  private String customerTimeDesireFrom;
  private String customerTimeDesireTo;
  private String portCorrectId;
  private String errTypeNims;
  private String subscriptionId;
  private String numRecheck;
  private String districtRecheck;
  private String locationCode;
  private String pointOk;

  private String priorityColorCode;
  private String deviceType;
  private String deviceTypeName;
  private String failureReason;

  private String customerName;

  private String summaryStatus;
  private Integer typeSearch;
  private String username;
  private Integer isDetail;
  private Double offSetFromUser;

  private String completeTimeFrom;
  private String completeTimeTo;
  private String startWorkingTime;

  private List<String> lstStationCode;
  private String customerGroupType;
  private List<String> lstAccount;
  private String complaintTypeId;

  public WoDTOSearch(String woId, String parentId, String parentName, String woCode,
      String woContent, String woSystem, String woSystemId, String woSystemOutId,
      String createPersonId, String createPersonName, String createDate, String woTypeId,
      String woTypeName, String cdId, String cdName, String ftId, String ftName,
      String status, String statusName, String priorityId, String priorityName,
      String startTime, String startTimeFrom, String startTimeTo, String endTime,
      String endTimeFrom, String endTimeTo, String finishTime, String result,
      String stationId, String stationCode, String lastUpdateTime, String fileName,
      String comments, String userId, String createPeronEmail,
      String createPersonMobile, String woGroupName, String woGroupEmail,
      String woGroupMobile, String woDescription, String createUnitId, String processUnitId,
      String accountIsdn, String serviceId, String infraType, String ccServiceId,
      String ccGroupId, String isCcResult, String ccResult, String checkQosInternetResult,
      String checkQosTHResult, String checkQrCode, String woTypeCode, String endPendingTime,
      String reasonName, String reasonId, String customer, String phone,
      String reasonOverdueLV1Id, String reasonOverdueLV1Name, String reasonOverdueLV2Id,
      String reasonOverdueLV2Name, String completedTime, String commentComplete, String longitude,
      String latitude, String kedbCode,
      String kedbId, String requiredTtReason, String contractCode, String scriptName,
      String warehouseCode, String constructionCode, String numComplaint,
      String customerPhone, String infraTypeName, String ableMop, String deviceCode,
      String planCode, String linkCode, String linkId, String alarmId, String cdAudioName,
      String ftAudioName, String amiOneId, String distance, String cableCode,
      String cableTypeCode, String customerTimeDesireFrom, String customerTimeDesireTo,
      String portCorrectId, String errTypeNims, String subscriptionId, String numRecheck,
      String districtRecheck, String locationCode, String pointOk, String priorityColorCode,
      String deviceType, String deviceTypeName, String failureReason, String customerName) {
    this.woId = woId;
    this.parentId = parentId;
    this.parentName = parentName;
    this.woCode = woCode;
    this.woContent = woContent;
    this.woSystem = woSystem;
    this.woSystemId = woSystemId;
    this.woSystemOutId = woSystemOutId;
    this.createPersonId = createPersonId;
    this.createPersonName = createPersonName;
    this.createDate = createDate;
    this.woTypeId = woTypeId;
    this.woTypeName = woTypeName;
    this.cdId = cdId;
    this.cdName = cdName;
    this.ftId = ftId;
    this.ftName = ftName;
    this.status = status;
    this.statusName = statusName;
    this.priorityId = priorityId;
    this.priorityName = priorityName;
    this.startTime = startTime;
    this.startTimeFrom = startTimeFrom;
    this.startTimeTo = startTimeTo;
    this.endTime = endTime;
    this.endTimeFrom = endTimeFrom;
    this.endTimeTo = endTimeTo;
    this.finishTime = finishTime;
    this.result = result;
    this.stationId = stationId;
    this.stationCode = stationCode;
    this.lastUpdateTime = lastUpdateTime;
    this.fileName = fileName;
    this.comments = comments;
    this.userId = userId;
    this.createPeronEmail = createPeronEmail;
    this.createPersonMobile = createPersonMobile;
    this.woGroupName = woGroupName;
    this.woGroupEmail = woGroupEmail;
    this.woGroupMobile = woGroupMobile;
    this.woDescription = woDescription;
    this.createUnitId = createUnitId;
    this.processUnitId = processUnitId;
    this.accountIsdn = accountIsdn;
    this.serviceId = serviceId;
    this.infraType = infraType;
    this.ccServiceId = ccServiceId;
    this.ccGroupId = ccGroupId;
    this.isCcResult = isCcResult;
    this.ccResult = ccResult;
    this.checkQosInternetResult = checkQosInternetResult;
    this.checkQosTHResult = checkQosTHResult;
    this.checkQrCode = checkQrCode;
    this.woTypeCode = woTypeCode;
    this.endPendingTime = endPendingTime;
    this.reasonName = reasonName;
    this.reasonId = reasonId;
    this.customer = customer;
    this.phone = phone;
    this.reasonOverdueLV1Id = reasonOverdueLV1Id;
    this.reasonOverdueLV1Name = reasonOverdueLV1Name;
    this.reasonOverdueLV2Id = reasonOverdueLV2Id;
    this.reasonOverdueLV2Name = reasonOverdueLV2Name;
    this.completedTime = completedTime;
    this.commentComplete = commentComplete;
    this.longitude = longitude;
    this.latitude = latitude;
    this.kedbCode = kedbCode;
    this.kedbId = kedbId;
    this.requiredTtReason = requiredTtReason;
    this.contractCode = contractCode;
    this.scriptName = scriptName;
    this.warehouseCode = warehouseCode;
    this.constructionCode = constructionCode;
    this.numComplaint = numComplaint;
    this.customerPhone = customerPhone;
    this.infraTypeName = infraTypeName;
    this.ableMop = ableMop;
    this.deviceCode = deviceCode;
    this.planCode = planCode;
    this.linkCode = linkCode;
    this.linkId = linkId;
    this.alarmId = alarmId;
    this.cdAudioName = cdAudioName;
    this.ftAudioName = ftAudioName;
    this.amiOneId = amiOneId;
    this.distance = distance;
    this.cableCode = cableCode;
    this.cableTypeCode = cableTypeCode;
    this.customerTimeDesireFrom = customerTimeDesireFrom;
    this.customerTimeDesireTo = customerTimeDesireTo;
    this.portCorrectId = portCorrectId;
    this.errTypeNims = errTypeNims;
    this.subscriptionId = subscriptionId;
    this.numRecheck = numRecheck;
    this.districtRecheck = districtRecheck;
    this.locationCode = locationCode;
    this.pointOk = pointOk;
    this.priorityColorCode = priorityColorCode;
    this.deviceType = deviceType;
    this.deviceTypeName = deviceTypeName;
    this.failureReason = failureReason;
    this.customerName = customerName;
  }

  public Boolean isIsCreated() {
    return isCreated;
  }

  public Boolean isIsCd() {
    return isCd;
  }

  public Boolean isIsFt() {
    return isFt;
  }
}
