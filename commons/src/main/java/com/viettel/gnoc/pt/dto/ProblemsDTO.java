/**
 * @(#)ProblemsForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.pt.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
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
public class ProblemsDTO {

  //Fields
  private String problemId;
  private String problemCode;
  private String problemName;
  private String description;
  private String typeId;
  private String subCategoryId;
  private String notes;
  private String priorityId;
  private String impactId;
  private String urgencyId;
  private String problemState;
  private String accessId;
  private String affectedNode;
  private String vendor;
  private String affectedService;
  private String location;
  private String locationId;
  private String createdTime;
  private String lastUpdateTime;
  private String assignTimeTemp;
  private String esRcaTime;
  private String esWaTime;
  private String esSlTime;
  private String startedTime;
  private String endedTime;
  private String createUserId;
  private String createUnitId;
  private String createUserName;
  private String createUnitName;
  private String createUserPhone;
  private String deferredTime;
  private String insertSource;
  private String isSendMessage;
  private String relatedTt;
  private String relatedPt;
  private String relatedKedb;
  private String ptType;
  private String rcaFoundTime;
  private String waFoundTime;
  private String slFoundTime;
  private String closedTime;
  private String rca;
  private String wa;
  private String solution;
  private String solutionType;
  private String receiveUnitId;
  private String receiveUserId;
  private String timeUsed;
  private String worklog;
  private String influenceScope;
  private String delayTime;
  private String rcaType;
  private String categorization;
  private String stateCode;
  private String ptRelatedType;
  private String contactInfo;
  private String pmGroup;
  private String closeCode;
  private String assignedTime;
  private String pmId;
  private String pmUserName;
  private String softwareVersion;
  private String hardwareVersion;
  private String ptDuplicate;
  private String reasonOverdue;
  private String isChat;
  private String itemTypeCode;
  private String stateName;
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
  private String durationSolutionTime;
  private String reasonReject;
  public ProblemsInsideDTO toModelInSide() {
    ProblemsInsideDTO model = new ProblemsInsideDTO(
        StringUtils.validString(problemId) ? Long.valueOf(problemId) : null,
        problemCode,
        problemName,
        description,
        StringUtils.validString(typeId) ? Long.valueOf(typeId) : null,
        StringUtils.validString(subCategoryId) ? Long.valueOf(subCategoryId) : null,
        notes,
        StringUtils.validString(priorityId) ? Long.valueOf(priorityId) : null,
        StringUtils.validString(impactId) ? Long.valueOf(impactId) : null,
        StringUtils.validString(urgencyId) ? Long.valueOf(urgencyId) : null,
        problemState,
        StringUtils.validString(accessId) ? Long.valueOf(accessId) : null,
        affectedNode,
        vendor,
        affectedService,
        location,
        StringUtils.validString(locationId) ? Long.valueOf(locationId) : null,
        StringUtils.validString(createdTime) ? DateTimeUtils.convertStringToDate(createdTime)
            : null,
        StringUtils.validString(lastUpdateTime) ? DateTimeUtils
            .convertStringToDate(lastUpdateTime) : null,
        StringUtils.validString(assignTimeTemp) ? DateTimeUtils
            .convertStringToDate(assignTimeTemp) : null,
        StringUtils.validString(esRcaTime) ? DateTimeUtils.convertStringToDate(esRcaTime) : null,
        StringUtils.validString(esWaTime) ? DateTimeUtils.convertStringToDate(esWaTime) : null,
        StringUtils.validString(esSlTime) ? DateTimeUtils.convertStringToDate(esSlTime) : null,
        StringUtils.validString(startedTime) ? DateTimeUtils.convertStringToDate(startedTime)
            : null,
        StringUtils.validString(endedTime) ? DateTimeUtils.convertStringToDate(endedTime) : null,
        StringUtils.validString(createUserId) ? Long.valueOf(createUserId) : null,
        StringUtils.validString(createUnitId) ? Long.valueOf(createUnitId) : null,
        createUserName,
        createUnitName,
        createUserPhone,
        StringUtils.validString(deferredTime) ? DateTimeUtils.convertStringToDate(deferredTime)
            : null,
        insertSource,
        StringUtils.validString(isSendMessage) ? Long.valueOf(isSendMessage) : null,
        relatedTt,
        relatedPt,
        relatedKedb,
        StringUtils.validString(ptType) ? Long.valueOf(ptType) : null,
        StringUtils.validString(rcaFoundTime) ? DateTimeUtils.convertStringToDate(rcaFoundTime)
            : null,
        StringUtils.validString(waFoundTime) ? DateTimeUtils.convertStringToDate(waFoundTime)
            : null,
        StringUtils.validString(slFoundTime) ? DateTimeUtils.convertStringToDate(slFoundTime)
            : null,
        StringUtils.validString(closedTime) ? DateTimeUtils.convertStringToDate(closedTime)
            : null,
        rca,
        wa,
        solution,
        StringUtils.validString(solutionType) ? Long.valueOf(solutionType) : null,
        StringUtils.validString(receiveUnitId) ? Long.valueOf(receiveUnitId) : null,
        StringUtils.validString(receiveUserId) ? Long.valueOf(receiveUserId) : null,
        StringUtils.validString(timeUsed) ? Double.valueOf(timeUsed) : null,
        worklog,
        influenceScope,
        StringUtils.validString(delayTime) ? DateTimeUtils.convertStringToDate(delayTime) : null,
        StringUtils.validString(rcaType) ? Long.valueOf(rcaType) : null,
        StringUtils.validString(categorization) ? Long.valueOf(categorization) : null,
        stateCode,
        StringUtils.validString(ptRelatedType) ? Long.valueOf(ptRelatedType) : null,
        contactInfo,
        StringUtils.validString(pmGroup) ? Long.valueOf(pmGroup) : null,
        StringUtils.validString(closeCode) ? Long.valueOf(closeCode) : null,
        StringUtils.validString(assignedTime) ? DateTimeUtils.convertStringToDate(assignedTime)
            : null,
        StringUtils.validString(pmId) ? Long.valueOf(pmId) : null,
        pmUserName,
        softwareVersion,
        hardwareVersion,
        ptDuplicate,
        reasonOverdue,
        StringUtils.validString(isChat) ? Long.valueOf(isChat) : null,
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
        null,
        null,
        StringUtils.validString(durationSolutionTime) ? DateTimeUtils.convertStringToDate(durationSolutionTime)
            : null,
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
        null
    );
    return model;
  }

}
