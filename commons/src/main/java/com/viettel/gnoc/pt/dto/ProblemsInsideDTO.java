/**
 * @(#)ProblemsForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.pt.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.pt.model.ProblemsEntity;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 * @version 2.0
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ProblemsInsideDTO extends BaseDto {

  private Long problemId;
  private String problemCode;
  @NotNull(message = "validation.problemsDTO.problemName.NotNull")
  @SizeByte(max = 1000, message = "validation.problemsDTO.problemName.tooLong")
  private String problemName;
  @SizeByte(max = 4000, message = "validation.problemsDTO.description.tooLong")
  private String description;
  @NotNull(message = "validation.problemsDTO.typeId.NotNull")
  private Long typeId;
  @NotNull(message = "validation.problemsDTO.subCategoryId.NotNull")
  private Long subCategoryId;
  @SizeByte(max = 4000, message = "validation.problemsDTO.notes.tooLong")
  private String notes;
  private Long priorityId;
  @NotNull(message = "validation.problemsDTO.impactId.NotNull")
  private Long impactId;
  private Long urgencyId;
  @NotNull(message = "validation.problemsDTO.problemState.NotNull")
  private String problemState;
  private Long accessId;
  @SizeByte(max = 1000, message = "validation.problemsDTO.affectedNode.tooLong")
  private String affectedNode;
  @SizeByte(max = 1000, message = "validation.problemsDTO.vendor.tooLong")
  private String vendor;
  @SizeByte(max = 1000, message = "validation.problemsDTO.affectedService.tooLong")
  private String affectedService;
  @SizeByte(max = 1000, message = "validation.problemsDTO.location.tooLong")
  private String location;
  @NotNull(message = "validation.problemsDTO.locationId.NotNull")
  private Long locationId;
  private Date createdTime;
  private Date lastUpdateTime;
  private Date assignTimeTemp;
  private Date esRcaTime;
  private Date esWaTime;
  private Date esSlTime;
  private Date startedTime;
  private Date endedTime;
  private Long createUserId;
  private Long createUnitId;
  private String createUserName;
  private String createUnitName;
  private String createUserPhone;
  private Date deferredTime;
  @SizeByte(max = 200, message = "validation.problemsDTO.insertSource.tooLong")
  private String insertSource;
  private Long isSendMessage;
  @SizeByte(max = 100, message = "validation.problemsDTO.relatedTt.tooLong")
  private String relatedTt;
  @SizeByte(max = 100, message = "validation.problemsDTO.relatedPt.tooLong")
  private String relatedPt;
  @SizeByte(max = 100, message = "validation.problemsDTO.relatedKedb.tooLong")
  private String relatedKedb;
  private Long ptType;
  private Date rcaFoundTime;
  private Date waFoundTime;
  private Date slFoundTime;
  private Date closedTime;
  @SizeByte(max = 4000, message = "validation.problemsDTO.rca.tooLong")
  private String rca;
  @SizeByte(max = 4000, message = "validation.problemsDTO.wa.tooLong")
  private String wa;
  @SizeByte(max = 4000, message = "validation.problemsDTO.solution.tooLong")
  private String solution;
  private Long solutionType;
  private Long receiveUnitId;
  private Long receiveUserId;
  private Double timeUsed;
  @SizeByte(max = 4000, message = "validation.problemsDTO.worklog.tooLong")
  private String worklog;
  @SizeByte(max = 4000, message = "validation.problemsDTO.influenceScope.tooLong")
  private String influenceScope;
  private Date delayTime;
  private Long rcaType;
  @NotNull(message = "validation.problemsDTO.categorization.NotNull")
  private Long categorization;
  @SizeByte(max = 255, message = "validation.problemsDTO.stateCode.tooLong")
  private String stateCode;
  private Long ptRelatedType;
  @SizeByte(max = 500, message = "validation.problemsDTO.contactInfo.tooLong")
  private String contactInfo;
  private Long pmGroup;
  private Long closeCode;
  //HaiNV20 add
  private Date assignedTime;
  private Long pmId;
  @SizeByte(max = 200, message = "validation.problemsDTO.pmUserName.tooLong")
  private String pmUserName;
  @SizeByte(max = 200, message = "validation.problemsDTO.softwareVersion.tooLong")
  private String softwareVersion;
  @SizeByte(max = 200, message = "validation.problemsDTO.hardwareVersion.tooLong")
  private String hardwareVersion;
  private String ptDuplicate;
  @SizeByte(max = 4000, message = "validation.problemsDTO.reasonOverdue.tooLong")
  private String reasonOverdue;
  private Long isChat;
  private String itemTypeCode;
  private String stateName;
  @SizeByte(max = 4000, message = "validation.problemsDTO.content.tooLong")
  private String content;
  private String userUpdateName;
  private String userUpdateId;
  private String unitUpdateId;
  private String unitUpdateName;
  private String worklogNew;
  private String nodeName;
  private String nodeIp;
  private String flag;
  private Long radicalSolutionType;
  //string
  private String typeIdStr;
  private String subCategoryIdStr;
  private String impactIdStr;
  private String urgencyIdStr;
  private String accessIdStr;
  private String receiveUserIdStr;
  private String statusStr;
  private String priorityStr;
  private String rcaTypeStr;
  private String ptRelatedTypeStr;
  private String categorizationStr;
  private String isOutOfDate;
  private String receiveUnitIdStr;
  private String solutionTypeName;
  private String attachFileList;

  private String skipStatus;
  private String colorCheck;
  private Date durationSolutionTime;
  private String reasonReject;

  /*ITSOL add new*/
  private List<String> lstPmGroup;
  private Boolean isCreateUnitId;
  private Boolean isReceiveUnitId;
  private String color;
  private String typeCode;
  private String keyword;
  private String fromDate;
  private String toDate;
  private String unitId;
  private String findInSubUnit;
  private String unitType;
  private UserTokenGNOCSimple userTokenGNOC;
  private List<UsersInsideDto> usersInsideDtos;
  private String pmGroupName;
  private String troubleCode;
  private String priorityName;

  //updateProblems
  private ProblemsInsideDTO problemsInsideDTOOld;
  //updateProblemsNew
  private ProblemsInsideDTO problemsNewDTO;

  private String insertKedb;
  private List<GnocFileDto> gnocFileDtos;
  private Long roleCode;
  public ProblemsEntity toEntity() {
    return new ProblemsEntity(problemId, problemCode, problemName, description, typeId,
        subCategoryId,
        notes, priorityId, impactId, urgencyId, Long.parseLong(problemState), accessId,
        affectedNode, vendor,
        affectedService, location, locationId, createdTime, lastUpdateTime, assignTimeTemp,
        esRcaTime, esWaTime, esSlTime, startedTime, endedTime, createUserId, createUnitId,
        createUserName, createUnitName, createUserPhone, deferredTime, insertSource, isSendMessage,
        relatedTt, relatedPt, relatedKedb, ptType, rcaFoundTime, waFoundTime, slFoundTime,
        closedTime, rca, wa, solution, solutionType, receiveUnitId, receiveUserId, timeUsed,
        worklog, influenceScope, delayTime, rcaType, categorization, stateCode, ptRelatedType,
        contactInfo, pmGroup, closeCode, assignedTime, pmId, pmUserName,
        softwareVersion,
        hardwareVersion, ptDuplicate, reasonOverdue, isChat, skipStatus, durationSolutionTime,
        reasonReject, radicalSolutionType);
  }

  public ProblemsInsideDTO(Long problemId, String problemCode, String problemName,
      String description, Long typeId, Long subCategoryId, String notes, Long priorityId,
      Long impactId, Long urgencyId, String problemState, Long accessId, String affectedNode,
      String vendor, String affectedService, String location, Long locationId,
      Date createdTime, Date lastUpdateTime, Date assignTimeTemp, Date esRcaTime,
      Date esWaTime, Date esSlTime, Date startedTime, Date endedTime, Long createUserId,
      Long createUnitId, String createUserName, String createUnitName,
      String createUserPhone, Date deferredTime, String insertSource, Long isSendMessage,
      String relatedTt, String relatedPt, String relatedKedb, Long ptType,
      Date rcaFoundTime, Date waFoundTime, Date slFoundTime, Date closedTime, String rca,
      String wa, String solution, Long solutionType, Long receiveUnitId, Long receiveUserId,
      Double timeUsed, String worklog, String influenceScope, Date delayTime, Long rcaType,
      Long categorization, String stateCode, Long ptRelatedType, String contactInfo,
      Long pmGroup, Long closeCode, Date assignedTime, Long pmId, String pmUserName,
      String softwareVersion, String hardwareVersion, String ptDuplicate,
      String reasonOverdue, Long isChat, String skipStatus, Date durationSolutionTime, String reasonReject, Long radicalSolutionType) {
    this.problemId = problemId;
    this.problemCode = problemCode;
    this.problemName = problemName;
    this.description = description;
    this.typeId = typeId;
    this.subCategoryId = subCategoryId;
    this.notes = notes;
    this.priorityId = priorityId;
    this.impactId = impactId;
    this.urgencyId = urgencyId;
    this.problemState = problemState;
    this.accessId = accessId;
    this.affectedNode = affectedNode;
    this.vendor = vendor;
    this.affectedService = affectedService;
    this.location = location;
    this.locationId = locationId;
    this.createdTime = createdTime;
    this.lastUpdateTime = lastUpdateTime;
    this.assignTimeTemp = assignTimeTemp;
    this.esRcaTime = esRcaTime;
    this.esWaTime = esWaTime;
    this.esSlTime = esSlTime;
    this.startedTime = startedTime;
    this.endedTime = endedTime;
    this.createUserId = createUserId;
    this.createUnitId = createUnitId;
    this.createUserName = createUserName;
    this.createUnitName = createUnitName;
    this.createUserPhone = createUserPhone;
    this.deferredTime = deferredTime;
    this.insertSource = insertSource;
    this.isSendMessage = isSendMessage;
    this.relatedTt = relatedTt;
    this.relatedPt = relatedPt;
    this.relatedKedb = relatedKedb;
    this.ptType = ptType;
    this.rcaFoundTime = rcaFoundTime;
    this.waFoundTime = waFoundTime;
    this.slFoundTime = slFoundTime;
    this.closedTime = closedTime;
    this.rca = rca;
    this.wa = wa;
    this.solution = solution;
    this.solutionType = solutionType;
    this.receiveUnitId = receiveUnitId;
    this.receiveUserId = receiveUserId;
    this.timeUsed = timeUsed;
    this.worklog = worklog;
    this.influenceScope = influenceScope;
    this.delayTime = delayTime;
    this.rcaType = rcaType;
    this.categorization = categorization;
    this.stateCode = stateCode;
    this.ptRelatedType = ptRelatedType;
    this.contactInfo = contactInfo;
    this.pmGroup = pmGroup;
    this.closeCode = closeCode;
    this.assignedTime = assignedTime;
    this.pmId = pmId;
    this.pmUserName = pmUserName;
    this.softwareVersion = softwareVersion;
    this.hardwareVersion = hardwareVersion;
    this.ptDuplicate = ptDuplicate;
    this.reasonOverdue = reasonOverdue;
    this.isChat = isChat;
    this.skipStatus = skipStatus;
    this.durationSolutionTime = durationSolutionTime;
    this.reasonReject = reasonReject;
    this.radicalSolutionType = radicalSolutionType;
  }

  public String compareContent(Object obj) {
    String ret = "";
    String before = "Before:\n";
    String after = "After:\n";
    if (obj == null) {
      return "";
    }
    final ProblemsInsideDTO other = (ProblemsInsideDTO) obj;
    if (!Objects.equals(this.problemName, other.getProblemName())) {
      before += "problemName:" + this.problemName + "\n";
      after += "problemName:" + other.getProblemName() + "\n";
    }
    if (!Objects.equals(this.description, other.getDescription())) {
      before += "description:" + this.description + "\n";
      after += "description:" + other.getDescription() + "\n";
    }
    if (!Objects.equals(this.typeId, other.getTypeId())) {
      before += "typeId:" + this.typeId + "\n";
      after += "typeId:" + other.getTypeId() + "\n";
    }
    if (!Objects.equals(this.subCategoryId, other.getSubCategoryId())) {
      before += "subCategoryId:" + this.subCategoryId + "\n";
      after += "subCategoryId:" + other.getSubCategoryId() + "\n";
    }
    if (!Objects.equals(this.priorityId, other.getPriorityId())) {
      before += "priorityId:" + this.priorityId + "\n";
      after += "priorityId:" + other.getPriorityId() + "\n";
    }
    if (!Objects.equals(this.impactId, other.getImpactId())) {
      before += "impactId:" + this.impactId + "\n";
      after += "impactId:" + other.getImpactId() + "\n";
    }
    if (!Objects.equals(this.problemState, other.getProblemState())) {
      before += "problemState:" + this.problemState + "\n";
      after += "problemState:" + other.getProblemState() + "\n";
    }
    if (!Objects.equals(this.affectedNode, other.getAffectedNode())) {
      before += "affectedNode:" + this.affectedNode + "\n";
      after += "affectedNode:" + other.getAffectedNode() + "\n";
    }
    if (!Objects.equals(this.vendor, other.getVendor())) {
      before += "vendor:" + this.vendor + "\n";
      after += "vendor:" + other.getVendor() + "\n";
    }
    if (!Objects.equals(this.affectedService, other.getAffectedService())) {
      before += "affectedService:" + this.affectedService + "\n";
      after += "affectedService:" + other.getAffectedService() + "\n";
    }
    if (!Objects.equals(this.locationId, other.getLocationId())) {
      before += "locationId:" + this.locationId + "\n";
      after += "locationId:" + other.getLocationId() + "\n";
    }
    if (!Objects.equals(DateTimeUtils.date2ddMMyyyyHHMMss(this.lastUpdateTime),
        DateTimeUtils.date2ddMMyyyyHHMMss(other.getLastUpdateTime()))) {
      before += "lastUpdateTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(this.lastUpdateTime) + "\n";
      after +=
          "lastUpdateTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(other.getLastUpdateTime()) + "\n";
    }
    if (!Objects.equals(DateTimeUtils.date2ddMMyyyyHHMMss(this.assignTimeTemp),
        DateTimeUtils.date2ddMMyyyyHHMMss(other.getAssignTimeTemp()))) {
      before += "assignTimeTemp:" + DateTimeUtils.date2ddMMyyyyHHMMss(this.assignTimeTemp) + "\n";
      after +=
          "assignTimeTemp:" + DateTimeUtils.date2ddMMyyyyHHMMss(other.getAssignTimeTemp()) + "\n";
    }
    if (!Objects.equals(DateTimeUtils.date2ddMMyyyyHHMMss(this.esRcaTime),
        DateTimeUtils.date2ddMMyyyyHHMMss(other.getEsRcaTime()))) {
      before += "esRcaTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(this.esRcaTime) + "\n";
      after += "esRcaTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(other.getEsRcaTime()) + "\n";
    }
    if (!Objects.equals(DateTimeUtils.date2ddMMyyyyHHMMss(this.esWaTime),
        DateTimeUtils.date2ddMMyyyyHHMMss(other.getEsWaTime()))) {
      before += "esWaTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(this.esWaTime) + "\n";
      after += "esWaTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(other.getEsWaTime()) + "\n";
    }
    if (!Objects.equals(DateTimeUtils.date2ddMMyyyyHHMMss(this.esSlTime),
        DateTimeUtils.date2ddMMyyyyHHMMss(other.getEsSlTime()))) {
      before += "esSlTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(this.esSlTime) + "\n";
      after += "esSlTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(other.getEsSlTime()) + "\n";
    }
    if (!Objects.equals(DateTimeUtils.date2ddMMyyyyHHMMss(this.deferredTime),
        DateTimeUtils.date2ddMMyyyyHHMMss(other.getDeferredTime()))) {
      before += "deferredTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(this.deferredTime) + "\n";
      after += "deferredTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(other.getDeferredTime()) + "\n";
    }
    if (!Objects.equals(this.relatedPt, other.getRelatedPt())) {
      before += "relatedPt:" + this.relatedPt + "\n";
      after += "relatedPt:" + other.getRelatedPt() + "\n";
    }
    if (!Objects.equals(this.relatedKedb, other.getRelatedKedb())) {
      before += "relatedKedb:" + this.relatedKedb + "\n";
      after += "relatedKedb:" + other.getRelatedKedb() + "\n";
    }
    if (!Objects.equals(this.ptType, other.getPtType())) {
      before += "ptType:" + this.ptType + "\n";
      after += "ptType:" + other.getPtType() + "\n";
    }
    if (!Objects.equals(DateTimeUtils.date2ddMMyyyyHHMMss(this.rcaFoundTime),
        DateTimeUtils.date2ddMMyyyyHHMMss(other.getRcaFoundTime()))) {
      before += "rcaFoundTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(this.rcaFoundTime) + "\n";
      after += "rcaFoundTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(other.getRcaFoundTime()) + "\n";
    }
    if (!Objects.equals(DateTimeUtils.date2ddMMyyyyHHMMss(this.waFoundTime),
        DateTimeUtils.date2ddMMyyyyHHMMss(other.getWaFoundTime()))) {
      before += "waFoundTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(this.waFoundTime) + "\n";
      after += "waFoundTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(other.getWaFoundTime()) + "\n";
    }
    if (!Objects.equals(DateTimeUtils.date2ddMMyyyyHHMMss(this.slFoundTime),
        DateTimeUtils.date2ddMMyyyyHHMMss(other.getSlFoundTime()))) {
      before += "slFoundTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(this.slFoundTime) + "\n";
      after += "slFoundTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(other.getSlFoundTime()) + "\n";
    }
    if (!Objects.equals(DateTimeUtils.date2ddMMyyyyHHMMss(this.closedTime),
        DateTimeUtils.date2ddMMyyyyHHMMss(other.getClosedTime()))) {
      before += "closedTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(this.closedTime) + "\n";
      after += "closedTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(other.getClosedTime()) + "\n";
    }
    if (!Objects.equals(this.rca, other.getRca())) {
      before += "rca:" + this.rca + "\n";
      after += "rca:" + other.getRca() + "\n";
    }
    if (!Objects.equals(this.wa, other.getWa())) {
      before += "wa:" + this.wa + "\n";
      after += "wa:" + other.getWa() + "\n";
    }
    if (!Objects.equals(this.solution, other.getSolution())) {
      before += "solution:" + this.solution + "\n";
      after += "solution:" + other.getSolution() + "\n";
    }
    if (!Objects.equals(this.solutionType, other.getSolutionType())) {
      before += "solutionType:" + this.solutionType + "\n";
      after += "solutionType:" + other.getSolutionType() + "\n";
    }
    if (!Objects.equals(this.receiveUnitId, other.getReceiveUnitId())) {
      before += "receiveUnitId:" + this.receiveUnitId + "\n";
      after += "receiveUnitId:" + other.getReceiveUnitId() + "\n";
    }
    if (!Objects.equals(this.receiveUserId, other.getReceiveUserId())) {
      before += "receiveUserId:" + this.receiveUserId + "\n";
      after += "receiveUserId:" + other.getReceiveUserId() + "\n";
    }
    if (!Objects.equals(this.worklog, other.getWorklog())) {
      before += "worklog:" + this.worklog + "\n";
      after += "worklog:" + other.getWorklog() + "\n";
    }
    if (!Objects.equals(this.influenceScope, other.getInfluenceScope())) {
      before += "influenceScope:" + this.influenceScope + "\n";
      after += "influenceScope:" + other.getInfluenceScope() + "\n";
    }
    if (!Objects.equals(this.rcaType, other.getRcaType())) {
      before += "rcaType:" + this.rcaType + "\n";
      after += "rcaType:" + other.getRcaType() + "\n";
    }
    if (!Objects.equals(this.categorization, other.getCategorization())) {
      before += "categorization:" + this.categorization + "\n";
      after += "categorization:" + other.getCategorization() + "\n";
    }
    if (!Objects.equals(this.stateCode, other.getStateCode())) {
      before += "stateCode:" + this.stateCode + "\n";
      after += "stateCode:" + other.getStateCode() + "\n";
    }
    if (!Objects.equals(this.ptRelatedType, other.getPtRelatedType())) {
      before += "ptRelatedType:" + this.ptRelatedType + "\n";
      after += "ptRelatedType:" + other.getPtRelatedType() + "\n";
    }
    if (!Objects.equals(this.contactInfo, other.getContactInfo())) {
      before += "contactInfo:" + this.contactInfo + "\n";
      after += "contactInfo:" + other.getContactInfo() + "\n";
    }
    if (!Objects.equals(this.pmGroup, other.getPmGroup())) {
      before += "pmGroup:" + this.pmGroup + "\n";
      after += "pmGroup:" + other.getPmGroup() + "\n";
    }
    if (!Objects.equals(this.closeCode, other.getCloseCode())) {
      before += "closeCode:" + this.closeCode + "\n";
      after += "closeCode:" + other.getCloseCode() + "\n";
    }
    if (!Objects.equals(this.ptDuplicate, other.getCloseCode())) {
      before += "ptDuplicate:" + this.ptDuplicate + "\n";
      after += "ptDuplicate:" + other.getPtDuplicate() + "\n";
    }

    if (!Objects.equals(this.reasonOverdue, other.getReasonOverdue())) {
      before += "reasonOverdue:" + this.reasonOverdue + "\n";
      after += "reasonOverdue:" + other.getReasonOverdue() + "\n";
    }
    if (!Objects.equals(DateTimeUtils.date2ddMMyyyyHHMMss(this.durationSolutionTime),
        DateTimeUtils.date2ddMMyyyyHHMMss(other.getClosedTime()))) {
      before += "closedTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(this.durationSolutionTime) + "\n";
      after += "closedTime:" + DateTimeUtils.date2ddMMyyyyHHMMss(other.getDurationSolutionTime()) + "\n";
    }
    if (!Objects.equals(this.radicalSolutionType, other.getRadicalSolutionType())) {
      before += "radicalSolutionType:" + this.radicalSolutionType + "\n";
      after += "radicalSolutionType:" + other.getRadicalSolutionType() + "\n";
    }
    ret += before + "\n";
    ret += "=====================================\n";
    ret += after;
    return ret;
  }

  public ProblemsDTO toModelOutSide() {
    ProblemsDTO model = new ProblemsDTO(
        StringUtils.validString(problemId) ? String.valueOf(problemId) : null,
        problemCode,
        problemName,
        description,
        StringUtils.validString(typeId) ? String.valueOf(typeId) : null,
        StringUtils.validString(subCategoryId) ? String.valueOf(subCategoryId) : null,
        notes,
        StringUtils.validString(priorityId) ? String.valueOf(priorityId) : null,
        StringUtils.validString(impactId) ? String.valueOf(impactId) : null,
        StringUtils.validString(urgencyId) ? String.valueOf(urgencyId) : null,
        problemState,
        StringUtils.validString(accessId) ? String.valueOf(accessId) : null,
        affectedNode,
        vendor,
        affectedService,
        location,
        StringUtils.validString(locationId) ? String.valueOf(locationId) : null,
        StringUtils.validString(createdTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(createdTime)
            : null,
        StringUtils.validString(lastUpdateTime) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(lastUpdateTime) : null,
        StringUtils.validString(assignTimeTemp) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(assignTimeTemp) : null,
        StringUtils.validString(esRcaTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(esRcaTime) : null,
        StringUtils.validString(esWaTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(esWaTime) : null,
        StringUtils.validString(esSlTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(esSlTime) : null,
        StringUtils.validString(startedTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(startedTime)
            : null,
        StringUtils.validString(endedTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(endedTime) : null,
        StringUtils.validString(createUserId) ? String.valueOf(createUserId) : null,
        StringUtils.validString(createUnitId) ? String.valueOf(createUnitId) : null,
        createUserName,
        createUnitName,
        createUserPhone,
        StringUtils.validString(deferredTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(deferredTime)
            : null,
        insertSource,
        StringUtils.validString(isSendMessage) ? String.valueOf(isSendMessage) : null,
        relatedTt,
        relatedPt,
        relatedKedb,
        StringUtils.validString(ptType) ? String.valueOf(ptType) : null,
        StringUtils.validString(rcaFoundTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(rcaFoundTime)
            : null,
        StringUtils.validString(waFoundTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(waFoundTime)
            : null,
        StringUtils.validString(slFoundTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(slFoundTime)
            : null,
        StringUtils.validString(closedTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(closedTime)
            : null,
        rca,
        wa,
        solution,
        StringUtils.validString(solutionType) ? String.valueOf(solutionType) : null,
        StringUtils.validString(receiveUnitId) ? String.valueOf(receiveUnitId) : null,
        StringUtils.validString(receiveUserId) ? String.valueOf(receiveUserId) : null,
        StringUtils.validString(timeUsed) ? String.valueOf(timeUsed) : null,
        worklog,
        influenceScope,
        StringUtils.validString(delayTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(delayTime) : null,
        StringUtils.validString(rcaType) ? String.valueOf(rcaType) : null,
        StringUtils.validString(categorization) ? String.valueOf(categorization) : null,
        stateCode,
        StringUtils.validString(ptRelatedType) ? String.valueOf(ptRelatedType) : null,
        contactInfo,
        StringUtils.validString(pmGroup) ? String.valueOf(pmGroup) : null,
        StringUtils.validString(closeCode) ? String.valueOf(closeCode) : null,
        StringUtils.validString(assignedTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(assignedTime)
            : null,
        StringUtils.validString(pmId) ? String.valueOf(pmId) : null,
        pmUserName,
        softwareVersion,
        hardwareVersion,
        ptDuplicate,
        reasonOverdue,
        StringUtils.validString(isChat) ? String.valueOf(isChat) : null,
        itemTypeCode,
        stateName,
        content,
        userUpdateName,
        userUpdateId,
        unitUpdateId,
        unitUpdateName,
        worklogNew,
        nodeName,
        nodeIp,
        flag,
        radicalSolutionType,
        typeIdStr,
        subCategoryIdStr,
        impactIdStr,
        urgencyIdStr,
        accessIdStr,
        receiveUserIdStr,
        statusStr,
        priorityStr,
        rcaTypeStr,
        ptRelatedTypeStr,
        categorizationStr,
        isOutOfDate,
        receiveUnitIdStr,
        solutionTypeName,
        attachFileList,
        skipStatus,
        colorCheck,
        StringUtils.validString(durationSolutionTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(durationSolutionTime) : null,
        reasonReject
    );
    return model;
  }
}
