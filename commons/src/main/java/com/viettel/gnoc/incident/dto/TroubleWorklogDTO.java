/**
 * @(#)TroubleWorklogForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.model.TroubleWorklogEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class TroubleWorklogDTO {

  //Fields
  private Long id;
  private Long createUserId;
  private String createUserName;
  private Long createUnitId;
  private String createUnitName;
  private String worklog;
  private String description;
  private String createdTime;
  private Long troubleId;
  private String defaultSortField;

  //Constructor
  public TroubleWorklogDTO() {
    this.defaultSortField = "createUserId";
    // This constructor is intentionally empty. Nothing special is needed here.
  }

  public TroubleWorklogDTO(
      Long id, Long createUserId, String createUserName, Long createUnitId, String createUnitName,
      String worklog, String description, String createdTime, Long troubleId) {
    this.defaultSortField = "name";
    this.id = id;
    this.createUserId = createUserId;
    this.createUserName = createUserName;
    this.createUnitId = createUnitId;
    this.createUnitName = createUnitName;
    this.worklog = worklog;
    this.description = description;
    this.createdTime = createdTime;
    this.troubleId = troubleId;
  }

  public TroubleWorklogEntity toEntity() {
    TroubleWorklogEntity model = new TroubleWorklogEntity(
        id,
        createUserId,
        createUserName,
        createUnitId,
        createUnitName,
        worklog,
        description,
        StringUtils.isStringNullOrEmpty(createdTime) ? null
            : DateTimeUtils.convertStringToDate(createdTime),
        troubleId);
    return model;
  }

  public TroubleWorklogInsiteDTO toModelInsite(TroubleWorklogInsiteDTO troubleWorklogInsiteDTO) {
    TroubleWorklogInsiteDTO dto = new TroubleWorklogInsiteDTO(
        id,
        createUserId,
        createUserName,
        createUnitId,
        createUnitName,
        worklog,
        description,
        StringUtils.isStringNullOrEmpty(createdTime) ? null
            : DateTimeUtils.convertStringToDate(createdTime),
        troubleId
    );
    return dto;
  }
}
