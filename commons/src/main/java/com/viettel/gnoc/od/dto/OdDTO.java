/**
 * @(#)OdForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.model.OdEntity;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OdDTO extends BaseDto {

  //Fields
  private Long odId;
  private String odCode;
  private String odName;
  private Date createTime;
  private String description;
  private Date lastUpdateTime;
  private Long odTypeId;
  private Long groupTypeId;
  private Long priorityId;
  private Date startTime;
  private Date endTime;
  private Long receiveUserId;
  private Long receiveUnitId;
  private String otherSystemCode;
  private String planCode;
  private String insertSource;
  private Long status;
  private Long oldStatus;
  private Long createPersonId;
  private Date closeTime;
  private Long closeCodeId;
  private Long clearCodeId;
  private Date endPendingTime;
  private Long numPending;
  private String vofficeTransCode;
  private Long woId;
  private String createUserName;
  private String fileName;
  private List<OdRelationDTO> lstOdRelation;
  private List<String> lstReceiveUnitId;
  private List<String> lstFileName;
  private String createPersonName;
  private String receiveUnitName;
  private String receiveUserName;
  private List<OdHistoryDTO> odHistoryDTO;
  private List<GnocFileDto> gnocFileDtos;
  private Long otherSystemId;
  private String otherSystemType;
  Long reasonGroup;
  String reasonDetail;
  Long solutionGroup;
  String solutionDetail;
  Date solutionCompleteTime;
  private Long createUnitId;
  private Long approverId;
  private String finishedTime;
  private String oldStatusName;
  private String reasonPause;
  private String resultApproval;

  public OdEntity toEntity() {
    return new OdEntity(odId, odCode, odName, createTime, description, lastUpdateTime, odTypeId,
        groupTypeId,
        priorityId, startTime, endTime, receiveUserId, receiveUnitId, otherSystemCode, planCode,
        insertSource,
        status, createPersonId, closeTime, closeCodeId, clearCodeId, endPendingTime, numPending,
        vofficeTransCode, woId, reasonGroup, reasonDetail, solutionGroup, solutionDetail,
        solutionCompleteTime, createUnitId, approverId,
        StringUtils.isNotNullOrEmpty(finishedTime) ? DateTimeUtils.convertStringToDate(finishedTime) : null, reasonPause);
  }

  public OdDTO(Long odId, String odCode, String odName, Date createTime, String description,
      Date lastUpdateTime, Long odTypeId, Long groupTypeId, Long priorityId, Date startTime,
      Date endTime, Long receiveUserId, Long receiveUnitId, String otherSystemCode,
      String planCode, String insertSource, Long status,
      Long createPersonId, Date closeTime, Long closeCodeId, Long clearCodeId,
      Date endPendingTime, Long numPending, String vofficeTransCode, Long woId,
      String createUserName, String fileName,
      List<OdRelationDTO> lstOdRelation, List<String> lstReceiveUnitId,
      List<GnocFileDto> gnocFileDtos, Long otherSystemId, String otherSystemType,
      List<String> lstFileName, String createPersonName, Long oldStatus, Long reasonGroup,
      String reasonDetail, Long solutionGroup, String solutionDetail, Date solutionCompleteTime,
      Long createUnitId, Long approverId, String finishedTime, String reasonPause, String resultApproval) {
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
    this.planCode = planCode;
    this.insertSource = insertSource;
    this.status = status;
    this.oldStatus = oldStatus;
    this.createPersonId = createPersonId;
    this.closeTime = closeTime;
    this.closeCodeId = closeCodeId;
    this.clearCodeId = clearCodeId;
    this.endPendingTime = endPendingTime;
    this.numPending = numPending;
    this.vofficeTransCode = vofficeTransCode;
    this.woId = woId;
    this.createUserName = createUserName;
    this.fileName = fileName;
    this.lstOdRelation = lstOdRelation;
    this.lstReceiveUnitId = lstReceiveUnitId;
    this.gnocFileDtos = gnocFileDtos;
    this.otherSystemId = otherSystemId;
    this.otherSystemType = otherSystemType;
    this.lstFileName = lstFileName;
    this.createPersonName = createPersonName;
    this.reasonGroup = reasonGroup;
    this.reasonDetail = reasonDetail;
    this.solutionGroup = solutionGroup;
    this.solutionDetail = solutionDetail;
    this.solutionCompleteTime = solutionCompleteTime;
    this.createUnitId = createUnitId;
    this.approverId = approverId;
    this.finishedTime = finishedTime;
    this.reasonPause = reasonPause;
    this.resultApproval = resultApproval;
  }

  public OdDTO(Long odId, String odCode, String odName, Date createTime, String description,
      Date lastUpdateTime, Long odTypeId, Long groupTypeId, Long priorityId, Date startTime,
      Date endTime, Long receiveUserId, Long receiveUnitId, String otherSystemCode,
      String planCode, String insertSource, Long status, Long createPersonId,  Date closeTime, Long closeCodeId
      , Long clearCodeId, Date endPendingTime, Long numPending, String vofficeTransCode, Long woId, Long reasonGroup
      , String reasonDetail, Long solutionGroup, String solutionDetail, Date solutionCompleteTime, Long createUnitId
      , Long approverId, String finishedTime, String reasonPause) {
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
}
