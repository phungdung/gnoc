package com.viettel.gnoc.kedb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KedbExportDTO {

  private Long kedbId;
  private String kedbCode;
  private String kedbName;
  private String description;
  private String influenceScope;
  private Long createUserId;
  private String createUserName;
  private Long receiveUserId;
  private String ptTtRelated;
  private Long typeId;
  private Long subCategoryId;
  private String vendor;
  private String softwareVersion;
  private String ttWa;
  private String rca;
  private String ptWa;
  private String solution;
  private String worklog;
  private String createdTime;
  private Long kedbState;
  private String notes;
  private String receiveUserName;
  private String hardwareVersion;
  private String typeIdStr;
  private String completer;
  private String completedTime;
  private Long numLike;
  private Long numView;
  private String comments;
  private String usersLike;
  private Long unitCheckId;
  private String unitCheckName;
  private Long parentTypeId;

  private String typeName;
  private String subCategoryName;
  private String vendorName;
  private String parentTypeName;
  private String kedbStateName;
  private String createUnitName;
  private String unitUpdateName;
  private String userUpdateName;
  private String fromDate;
  private String toDate;

  private String resultImport;
  private String action;
}
