package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.risk.model.RiskEntity;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskDTO extends BaseDto {

  private Long riskId;
  private String riskCode;
  @NotNull(message = "validation.riskDTO.description.notNull")
  private String description;
  private Date createTime;
  private Long createUserId;
  private Long createUnitId;
  private Date lastUpdateTime;
  @NotNull(message = "validation.riskDTO.systemId.notNull")
  private Long systemId;
  private Long arrId;
  private Long priorityId;
  private Date startTime;
  private Date endTime;
  private Long receiveUserId;
  @NotNull(message = "validation.riskDTO.receiveUnitId.notNull")
  private Long receiveUnitId;
  @NotNull(message = "validation.riskDTO.riskTypeId.notNull")
  private Long riskTypeId;
  private Long groupUnitId;
  private Long status;
  @NotNull(message = "validation.riskDTO.subjectId.notNull")
  private Long subjectId;
  @NotNull(message = "validation.riskDTO.effect.notNull")
  private Long effect;
  @Size(max = 2000, message = "validation.riskDTO.effectDetail.tooLong")
  private String effectDetail;
  @NotNull(message = "validation.riskDTO.frequency.notNull")
  private Long frequency;
  private String solution;
  private Date finishTime;
  private Long result;
  private Long redundancy;
  private String evedence;
  private Date closedTime;
  private String insertSource;
  private String otherSystemCode;
  @NotNull(message = "validation.riskDTO.riskName.notNull")
  @Size(max = 500, message = "validation.riskDTO.riskName.tooLong")
  private String riskName;
  private Date lastSendSmsGoingOverdue;
  private Long isExternalVtnet;
  @Size(max = 2000, message = "validation.riskDTO.frequencyDetail.tooLong")
  private String frequencyDetail;
  private String reasonAccept;
  private String reasonReject;
  private String reasonCancel;
  @NotNull(message = "validation.riskDTO.suggestSolution.notNull")
  private String suggestSolution;
  private Date closedDate;
  private Date acceptedDate;
  private Date canceledDate;
  private Date receivedDate;
  private Date openedDate;
  private Date rejectedDate;
  private String logTime;
  private String resultProcessing;
  private String riskComment;

  private String fileName;
  private Date startTimeFrom;
  private Date startTimeTo;
  private Date endTimeFrom;
  private Date endTimeTo;
  private Double offSetFromUser;
  private String systemName;
  private String createUnitName;
  private String createUnitCode;
  private String riskTypeName;
  private String receiveUserName;
  private String receiveUnitName;
  private String receiveUnitCode;
  private Double remainTime;
  private String createUserName;
  private Date closeTime;
  private Date createTimeFrom;
  private Date createTimeTo;
  private Long riskGroupTypeId;
  private Long countrySystem;
  private String childCreateUnit;
  private String childReceiveUnit;
  private Long userId; // nhan vien thuc hien tim kiem
  private Boolean isCreated; // nhan vien tao
  private Boolean isReceiveUser; // nhan vien xu ly
  private Boolean isReceiveUnit; // don vi xu ly
  private String statusSearchWeb;
  private List<RiskRelationDTO> lstRiskRelation;
  private String effectName;
  private String frequencyName;
  private Long oldStatus;
  private String comment;
  private List<GnocFileDto> gnocFileDtos;
  private String statusName;
  private String subjectName;
  private String priorityName;
  private String redundacyName;
  private String changeReason;
  private Long userIdLogin;
  private Long unitIdLogin;
  private List<GnocFileDto> gnocFileTemplate;
  private Long riskChangeStatusId;
  private List<Long> idDeleteList;
  private List<Long> idDownloadList;
  private String itemCode;
  private Long isSendSmsOverdue;

  public RiskDTO(Long riskId, String riskCode, String description, Date createTime,
      Long createUserId, Long createUnitId, Date lastUpdateTime, Long systemId, Long arrId,
      Long priorityId, Date startTime, Date endTime, Long receiveUserId, Long receiveUnitId,
      Long riskTypeId, Long groupUnitId, Long status, Long subjectId, Long effect,
      String effectDetail, Long frequency, String solution, Date finishTime, Long result,
      Long redundancy, String evedence, Date closedTime, String insertSource,
      String otherSystemCode, String riskName, Date lastSendSmsGoingOverdue,
      Long isExternalVtnet, String frequencyDetail, String reasonAccept,
      String reasonReject, String reasonCancel, String suggestSolution, Date closedDate,
      Date acceptedDate, Date canceledDate, Date receivedDate, Date openedDate,
      Date rejectedDate, String logTime, String resultProcessing, String riskComment,
      String changeReason, Long isSendSmsOverdue) {
    this.riskId = riskId;
    this.riskCode = riskCode;
    this.description = description;
    this.createTime = createTime;
    this.createUserId = createUserId;
    this.createUnitId = createUnitId;
    this.lastUpdateTime = lastUpdateTime;
    this.systemId = systemId;
    this.arrId = arrId;
    this.priorityId = priorityId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.receiveUserId = receiveUserId;
    this.receiveUnitId = receiveUnitId;
    this.riskTypeId = riskTypeId;
    this.groupUnitId = groupUnitId;
    this.status = status;
    this.subjectId = subjectId;
    this.effect = effect;
    this.effectDetail = effectDetail;
    this.frequency = frequency;
    this.solution = solution;
    this.finishTime = finishTime;
    this.result = result;
    this.redundancy = redundancy;
    this.evedence = evedence;
    this.closedTime = closedTime;
    this.insertSource = insertSource;
    this.otherSystemCode = otherSystemCode;
    this.riskName = riskName;
    this.lastSendSmsGoingOverdue = lastSendSmsGoingOverdue;
    this.isExternalVtnet = isExternalVtnet;
    this.frequencyDetail = frequencyDetail;
    this.reasonAccept = reasonAccept;
    this.reasonReject = reasonReject;
    this.reasonCancel = reasonCancel;
    this.suggestSolution = suggestSolution;
    this.closedDate = closedDate;
    this.acceptedDate = acceptedDate;
    this.canceledDate = canceledDate;
    this.receivedDate = receivedDate;
    this.openedDate = openedDate;
    this.rejectedDate = rejectedDate;
    this.logTime = logTime;
    this.resultProcessing = resultProcessing;
    this.riskComment = riskComment;
    this.changeReason = changeReason;
    this.isSendSmsOverdue = isSendSmsOverdue;
  }

  public RiskEntity toEntity() {
    return new RiskEntity(riskId, riskCode, description, createTime, createUserId, createUnitId,
        lastUpdateTime, systemId, arrId, priorityId, startTime, endTime, receiveUserId,
        receiveUnitId, riskTypeId, groupUnitId, status, subjectId, effect, effectDetail, frequency,
        solution, finishTime, result, redundancy, evedence, closedTime, insertSource,
        otherSystemCode, riskName, lastSendSmsGoingOverdue, isExternalVtnet, frequencyDetail,
        reasonAccept, reasonReject, reasonCancel, suggestSolution, closedDate, acceptedDate,
        canceledDate, receivedDate, openedDate, rejectedDate, logTime, resultProcessing,
        riskComment, changeReason, isSendSmsOverdue);
  }

}
