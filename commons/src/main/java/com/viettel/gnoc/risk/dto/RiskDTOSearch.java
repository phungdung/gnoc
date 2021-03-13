
package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class RiskDTOSearch extends BaseDto implements Cloneable {

  //Fields
  private String riskId;
  private String riskCode;
  private String riskName;
  private String createTime;
  private String description;
  private String lastUpdateTime;
  private String riskTypeId;
  private String riskTypeName;
  private String arrTypeId;
  private String priorityId;
  private String startTime;
  private String endTime;
  private String receiveUserId;
  private String receiveUnitId;
  private String otherSystemCode;
  private String planCode;
  private String locationCode;
  private String insertSource;
  private String status;
  private String statusName;
  private String defaultSortField;
  private String userId; // nhan vien thuc hien tim kiem
  private Boolean isCreated; // nhan vien tao
  private Boolean isReceiveUser;// nhan vien xu ly
  private Boolean isReceiveUnit;// nhan vien xu ly
  private String createTimeFrom;
  private String createTimeTo;
  private String startTimeFrom;
  private String startTimeTo;
  private String endTimeFrom;
  private String endTimeTo;
  private String receiveUserName; // ten nhan vien xu ly
  private String receiveUnitName; // ten don vi xu ly
  private String receiveUnitCode; // ten don vi xu ly
  private String priorityName; // ten don vi xu ly
  private String remainTime; // ten don vi xu ly
  private List<String> lstFileName;
  private String fileName;
  private String createUserId;
  private String createUserName;
  private String createUnitName;
  private String clearCodeId;
  private String closeCodeId;
  private String closeTime;
  private String comment;
  private String oldStatus;
  private String createUnitId;
  private String childCreateUnit;
  private String childReceiveUnit;
  private String riskGroupTypeId;
  private String endPendingTime;

  private List<byte[]> lstFileArr;
  private String riskTypeCode;
  private Boolean isUnitOfGnoc;
  private String priorityCode;
  private String userName;
  private String password;
  private String signUser;
  private String docTitle;
  private String vofficeTransCode;
  private String subjectId;
  private String subjectName;
  private String effect;
  private String effectName;
  private String effectDetail;
  private String solution;
  private String frequency;
  private String frequencyName;
  private String systemId;
  private String systemName;
  private String groupUnitId;
  private String result;
  private String redundancy;
  private String evedence;

  private String frequencyDetail;
  private String suggestSolution;
  private String reasonAccept;
  private String reasonReject;
  private String reasonCancel;
  private String closedDate;
  private String acceptedDate;
  private String canceledDate;
  private String receivedDate;
  private String openedDate;
  private String rejectedDate;
  private String logTime;
  private String oldLogTime;
  private String isExternalVtnet;
  private String redundacyName;
  private String resultProcessing;


  public RiskDTOSearch(String odId, String odCode, String odName, String createTime,
      String description//
      , String lastUpdateTime, String odTypeId, String arrTypeId, String priorityId//
      , String startTime, String endTime, String receiveUserId, String receiveUnitId//
      , String otherSystemCode, String planCode, String locationCode, String insertSource,
      String status
  ) {
    this.riskId = odId;
    this.riskCode = odCode;
    this.riskName = odName;
    this.createTime = createTime;
    this.description = description;
    this.lastUpdateTime = lastUpdateTime;
    this.riskTypeId = odTypeId;
    this.arrTypeId = arrTypeId;
    this.priorityId = priorityId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.receiveUserId = receiveUserId;
    this.receiveUnitId = receiveUnitId;
    this.otherSystemCode = otherSystemCode;
    this.planCode = planCode;
    this.locationCode = locationCode;
    this.insertSource = insertSource;
    this.status = status;
  }

  // <editor-fold defaultstate="collapsed" desc="GET & SET">
  public String getResultProcessing() {
    return resultProcessing;
  }

  public void setResultProcessing(String resultProcessing) {
    this.resultProcessing = resultProcessing;
  }

  public String getRedundacyName() {
    return redundacyName;
  }

  public void setRedundacyName(String redundacyName) {
    this.redundacyName = redundacyName;
  }

  public String getClosedDate() {
    return closedDate;
  }

  public void setClosedDate(String closedDate) {
    this.closedDate = closedDate;
  }

  public String getIsExternalVtnet() {
    return isExternalVtnet;
  }

  public void setIsExternalVtnet(String isExternalVtnet) {
    this.isExternalVtnet = isExternalVtnet;
  }

  public String getOldLogTime() {
    return oldLogTime;
  }

  public void setOldLogTime(String oldLogTime) {
    this.oldLogTime = oldLogTime;
  }

  public String getAcceptedDate() {
    return acceptedDate;
  }

  public void setAcceptedDate(String acceptedDate) {
    this.acceptedDate = acceptedDate;
  }

  public String getCanceledDate() {
    return canceledDate;
  }

  public void setCanceledDate(String canceledDate) {
    this.canceledDate = canceledDate;
  }

  public String getReceivedDate() {
    return receivedDate;
  }

  public void setReceivedDate(String receivedDate) {
    this.receivedDate = receivedDate;
  }

  public String getOpenedDate() {
    return openedDate;
  }

  public void setOpenedDate(String openedDate) {
    this.openedDate = openedDate;
  }

  public String getRejectedDate() {
    return rejectedDate;
  }

  public void setRejectedDate(String rejectedDate) {
    this.rejectedDate = rejectedDate;
  }

  public String getLogTime() {
    return logTime;
  }

  public void setLogTime(String logTime) {
    this.logTime = logTime;
  }

  public String getReasonAccept() {
    return reasonAccept;
  }

  public void setReasonAccept(String reasonAccept) {
    this.reasonAccept = reasonAccept;
  }

  public String getReasonReject() {
    return reasonReject;
  }

  public void setReasonReject(String reasonReject) {
    this.reasonReject = reasonReject;
  }

  public String getReasonCancel() {
    return reasonCancel;
  }

  public void setReasonCancel(String reasonCancel) {
    this.reasonCancel = reasonCancel;
  }

  public String getSuggestSolution() {
    return suggestSolution;
  }

  public void setSuggestSolution(String suggestSolution) {
    this.suggestSolution = suggestSolution;
  }

  public String getFrequencyDetail() {
    return frequencyDetail;
  }

  public void setFrequencyDetail(String frequencyDetail) {
    this.frequencyDetail = frequencyDetail;
  }

  public void setDefaultSortField(String defaultSortField) {
    this.defaultSortField = defaultSortField;
  }

  public String getDefaultSortField() {
    return defaultSortField;
  }

  public String getSystemName() {
    return systemName;
  }

  public void setSystemName(String systemName) {
    this.systemName = systemName;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getRedundancy() {
    return redundancy;
  }

  public void setRedundancy(String redundancy) {
    this.redundancy = redundancy;
  }

  public String getEvedence() {
    return evedence;
  }

  public void setEvedence(String evedence) {
    this.evedence = evedence;
  }

  public String getCreateUnitName() {
    return createUnitName;
  }

  public void setCreateUnitName(String createUnitName) {
    this.createUnitName = createUnitName;
  }

  public String getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }

  public String getVofficeTransCode() {
    return vofficeTransCode;
  }

  public void setVofficeTransCode(String vofficeTransCode) {
    this.vofficeTransCode = vofficeTransCode;
  }

  public String getGroupUnitId() {
    return groupUnitId;
  }

  public void setGroupUnitId(String groupUnitId) {
    this.groupUnitId = groupUnitId;
  }

  public String getDocTitle() {
    return docTitle;
  }

  public void setDocTitle(String docTitle) {
    this.docTitle = docTitle;
  }

  public String getEffect() {
    return effect;
  }

  public void setEffect(String effect) {
    this.effect = effect;
  }

  public String getEffectName() {
    return effectName;
  }

  public void setEffectName(String effectName) {
    this.effectName = effectName;
  }

  public String getFrequencyName() {
    return frequencyName;
  }

  public void setFrequencyName(String frequencyName) {
    this.frequencyName = frequencyName;
  }

  public String getEffectDetail() {
    return effectDetail;
  }

  public void setEffectDetail(String effectDetail) {
    this.effectDetail = effectDetail;
  }

  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }

  public String getFrequency() {
    return frequency;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  public String getSystemId() {
    return systemId;
  }

  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSignUser() {
    return signUser;
  }

  public void setSignUser(String signUser) {
    this.signUser = signUser;
  }

  public String getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }

  public String getPriorityCode() {
    return priorityCode;
  }

  public void setPriorityCode(String priorityCode) {
    this.priorityCode = priorityCode;
  }

  public Boolean getIsUnitOfGnoc() {
    return isUnitOfGnoc;
  }

  public void setIsUnitOfGnoc(Boolean isUnitOfGnoc) {
    this.isUnitOfGnoc = isUnitOfGnoc;
  }

  public String getRiskTypeCode() {
    return riskTypeCode;
  }

  public void setRiskTypeCode(String riskTypeCode) {
    this.riskTypeCode = riskTypeCode;
  }

  public List<byte[]> getLstFileArr() {
    return lstFileArr;
  }

  public void setLstFileArr(List<byte[]> lstFileArr) {
    this.lstFileArr = lstFileArr;
  }

  public String getEndPendingTime() {
    return endPendingTime;
  }

  public void setEndPendingTime(String endPendingTime) {
    this.endPendingTime = endPendingTime;
  }

  public Boolean getIsReceiveUnit() {
    return isReceiveUnit;
  }

  public void setIsReceiveUnit(Boolean isReceiveUnit) {
    this.isReceiveUnit = isReceiveUnit;
  }

  public String getRiskGroupTypeId() {
    return riskGroupTypeId;
  }

  public void setRiskGroupTypeId(String riskGroupTypeId) {
    this.riskGroupTypeId = riskGroupTypeId;
  }

  public String getChildCreateUnit() {
    return childCreateUnit;
  }

  public void setChildCreateUnit(String childCreateUnit) {
    this.childCreateUnit = childCreateUnit;
  }

  public String getChildReceiveUnit() {
    return childReceiveUnit;
  }

  public void setChildReceiveUnit(String childReceiveUnit) {
    this.childReceiveUnit = childReceiveUnit;
  }

  public String getCreateUnitId() {
    return createUnitId;
  }

  public void setCreateUnitId(String createUnitId) {
    this.createUnitId = createUnitId;
  }

  public String getCreateTimeFrom() {
    return createTimeFrom;
  }

  public void setCreateTimeFrom(String createTimeFrom) {
    this.createTimeFrom = createTimeFrom;
  }

  public String getCreateTimeTo() {
    return createTimeTo;
  }

  public void setCreateTimeTo(String createTimeTo) {
    this.createTimeTo = createTimeTo;
  }

  public String getEndTimeFrom() {
    return endTimeFrom;
  }

  public void setEndTimeFrom(String endTimeFrom) {
    this.endTimeFrom = endTimeFrom;
  }

  public String getEndTimeTo() {
    return endTimeTo;
  }

  public void setEndTimeTo(String endTimeTo) {
    this.endTimeTo = endTimeTo;
  }

  public String getReceiveUnitCode() {
    return receiveUnitCode;
  }

  public void setReceiveUnitCode(String receiveUnitCode) {
    this.receiveUnitCode = receiveUnitCode;
  }

  public String getOldStatus() {
    return oldStatus;
  }

  public void setOldStatus(String oldStatus) {
    this.oldStatus = oldStatus;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getCloseTime() {
    return closeTime;
  }

  public void setCloseTime(String closeTime) {
    this.closeTime = closeTime;
  }

  public String getClearCodeId() {
    return clearCodeId;
  }

  public void setClearCodeId(String clearCodeId) {
    this.clearCodeId = clearCodeId;
  }

  public String getCloseCodeId() {
    return closeCodeId;
  }

  public void setCloseCodeId(String closeCodeId) {
    this.closeCodeId = closeCodeId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public List<String> getLstFileName() {
    return lstFileName;
  }

  public void setLstFileName(List<String> lstFileName) {
    this.lstFileName = lstFileName;
  }

  public String getRemainTime() {
    return remainTime;
  }

  public void setRemainTime(String remainTime) {
    this.remainTime = remainTime;
  }

  public String getStatusName() {
    return statusName;
  }

  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }

  public String getPriorityName() {
    return priorityName;
  }

  public void setPriorityName(String priorityName) {
    this.priorityName = priorityName;
  }

  public String getRiskTypeName() {
    return riskTypeName;
  }

  public void setRiskTypeName(String riskTypeName) {
    this.riskTypeName = riskTypeName;
  }

  public String getReceiveUnitName() {
    return receiveUnitName;
  }

  public void setReceiveUnitName(String receiveUnitName) {
    this.receiveUnitName = receiveUnitName;
  }

  public String getReceiveUserName() {
    return receiveUserName;
  }

  public void setReceiveUserName(String receiveUserName) {
    this.receiveUserName = receiveUserName;
  }

  public String getStartTimeFrom() {
    return startTimeFrom;
  }

  public void setStartTimeFrom(String startTimeFrom) {
    this.startTimeFrom = startTimeFrom;
  }

  public String getStartTimeTo() {
    return startTimeTo;
  }

  public void setStartTimeTo(String startTimeTo) {
    this.startTimeTo = startTimeTo;
  }

  public String getUserId() {
    return userId;
  }

  public Boolean getIsCreated() {
    return isCreated;
  }

  public String getCreateUserId() {
    return createUserId;
  }

  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }

  public String getCreateUserName() {
    return createUserName;
  }

  public void setCreateUserName(String createUserName) {
    this.createUserName = createUserName;
  }

  public void setIsCreated(Boolean isCreated) {
    this.isCreated = isCreated;
  }

  public Boolean getIsReceiveUser() {
    return isReceiveUser;
  }

  public void setIsReceiveUser(Boolean isReceiveUser) {
    this.isReceiveUser = isReceiveUser;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getRiskId() {
    return riskId;
  }

  public void setRiskId(String riskId) {
    this.riskId = riskId;
  }

  public String getRiskCode() {
    return riskCode;
  }

  public void setRiskCode(String riskCode) {
    this.riskCode = riskCode;
  }

  public String getRiskName() {
    return riskName;
  }

  public void setRiskName(String riskName) {
    this.riskName = riskName;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(String lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public String getRiskTypeId() {
    return riskTypeId;
  }

  public void setRiskTypeId(String riskTypeId) {
    this.riskTypeId = riskTypeId;
  }

  public String getArrTypeId() {
    return arrTypeId;
  }

  public void setArrTypeId(String arrTypeId) {
    this.arrTypeId = arrTypeId;
  }

  public String getPriorityId() {
    return priorityId;
  }

  public void setPriorityId(String priorityId) {
    this.priorityId = priorityId;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getReceiveUserId() {
    return receiveUserId;
  }

  public void setReceiveUserId(String receiveUserId) {
    this.receiveUserId = receiveUserId;
  }

  public String getReceiveUnitId() {
    return receiveUnitId;
  }

  public void setReceiveUnitId(String receiveUnitId) {
    this.receiveUnitId = receiveUnitId;
  }

  public String getOtherSystemCode() {
    return otherSystemCode;
  }

  public void setOtherSystemCode(String otherSystemCode) {
    this.otherSystemCode = otherSystemCode;
  }

  public String getPlanCode() {
    return planCode;
  }

  public void setPlanCode(String planCode) {
    this.planCode = planCode;
  }

  public String getLocationCode() {
    return locationCode;
  }

  public void setLocationCode(String locationCode) {
    this.locationCode = locationCode;
  }

  public String getInsertSource() {
    return insertSource;
  }

  public void setInsertSource(String insertSource) {
    this.insertSource = insertSource;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  // </editor-fold>
  @Override
  public RiskDTOSearch clone() {
    try {
      return (RiskDTOSearch) super.clone();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }
}
