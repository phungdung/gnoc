package com.viettel.gnoc.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KedbDTO {

  //Fields
  private String kedbId;
  private String kedbCode;
  private String kedbName;
  private String description;
  private String influenceScope;
  private String createUserId;
  private String createUserName;
  private String createUnitName;
  private String receiveUserId;
  private String ptTtRelated;
  private String typeId;
  private String subCategoryId;
  private String vendor;
  private String softwareVersion;
  private String hardwareVersion;
  private String ttWa;
  private String rca;
  private String ptWa;
  private String solution;
  private String worklog;
  private String createdTime;
  private String kedbState;
  private String notes;
  private String receiveUserName;

  private String vendorStr;
  private String subCategoryIdStr;
  private String typeIdStr;
  private String kedbStateStr;
  private String softwareVersionStr;
  private String hardwareVersionStr;
  private String completer;
  private String completedTime;
  private String numLike;
  private String numView;
  private String comments;
  private String usersLike;
  private String unitCheckId;
  private String unitCheckName;
  private String userUpdateName;
  private String userUpdateId;
  private String unitUpdateId;
  private String unitUpdateName;
  private String parentTypeId;
  private String parentTypeName;

  public KedbDTO(String kedbId, String kedbCode, String kedbName, String description,
      String influenceScope, String createUserId,
      String createUserName, String receiveUserId, String ptTtRelated, String typeId,
      String subCategoryId, String vendor,
      String softwareVersion, String ttWa, String rca, String ptWa, String solution, String worklog,
      String createdTime,
      String kedbState, String notes, String receiveUserName, String hardwareVersion,
      String typeIdStr, String completer, String completedTime,
      String numLike, String numView, String comments, String usersLike, String unitCheckId,
      String unitCheckName, String parentTypeId) {
    this.kedbId = kedbId;
    this.kedbCode = kedbCode;
    this.kedbName = kedbName;
    this.description = description;
    this.influenceScope = influenceScope;
    this.createUserId = createUserId;
    this.createUserName = createUserName;
    this.receiveUserId = receiveUserId;
    this.ptTtRelated = ptTtRelated;
    this.typeId = typeId;
    this.subCategoryId = subCategoryId;
    this.vendor = vendor;
    this.softwareVersion = softwareVersion;
    this.ttWa = ttWa;
    this.rca = rca;
    this.ptWa = ptWa;
    this.solution = solution;
    this.worklog = worklog;
    this.createdTime = createdTime;
    this.kedbState = kedbState;
    this.notes = notes;
    this.receiveUserName = receiveUserName;
    this.hardwareVersion = hardwareVersion;
    this.typeIdStr = typeIdStr;
    this.completer = completer;
    this.completedTime = completedTime;
    this.numLike = numLike;
    this.numView = numView;
    this.comments = comments;
    this.usersLike = usersLike;
    this.unitCheckId = unitCheckId;
    this.unitCheckName = unitCheckName;
    this.parentTypeId = parentTypeId;
  }


}
