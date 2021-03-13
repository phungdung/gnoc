/**
 * @(#)TroubleActionLogsForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.incident.model.TroubleActionLogsEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class TroubleActionLogsDTO {

  //Fields
  private Long id;
  private String content;
  private Date createTime;
  private Long createUnitId;
  private Long createUserId;
  private String type;
  private Long troubleId;
  private String createrUnitName;
  private String createrUserName;
  private Long stateId;
  private String troubleCode;
  private String stateName;
  private String insertSource;
  private String actionInfo;

  private String rootCause;
  private String workArround;
  private String inforMation;

  public TroubleActionLogsDTO(Long id, String content, Date createTime, Long createUnitId,
      Long createUserId, String type, Long troubleId, String createrUnitName,
      String createrUserName, Long stateId,
      String troubleCode, String stateName, String insertSource, String actionInfo) {
    this.id = id;
    this.content = content;
    this.createTime = createTime;
    this.createUnitId = createUnitId;
    this.createUserId = createUserId;
    this.type = type;
    this.troubleId = troubleId;
    this.createrUnitName = createrUnitName;
    this.createrUserName = createrUserName;
    this.stateId = stateId;
    this.troubleCode = troubleCode;
    this.stateName = stateName;
  }

  public TroubleActionLogsDTO(Long id, String content, Date createTime, Long createUnitId,
                              Long createUserId, String type, Long troubleId, String createrUnitName,
                              String createrUserName, Long stateId,
                              String troubleCode, String stateName, String insertSource, String actionInfo, String inforMation) {
    this.id = id;
    this.content = content;
    this.createTime = createTime;
    this.createUnitId = createUnitId;
    this.createUserId = createUserId;
    this.type = type;
    this.troubleId = troubleId;
    this.createrUnitName = createrUnitName;
    this.createrUserName = createrUserName;
    this.stateId = stateId;
    this.troubleCode = troubleCode;
    this.stateName = stateName;
    this.inforMation = inforMation;
  }

  public TroubleActionLogsEntity toEntity() {
    TroubleActionLogsEntity model = new TroubleActionLogsEntity(
        id,
        content,
        createTime,
        createUnitId,
        createUserId,
        type,
        troubleId,
        createrUnitName,
        createrUserName,
        stateId,
        troubleCode,
        stateName,
        actionInfo,
        insertSource,
        inforMation
    );
    model.setRootCause(rootCause);
    model.setWorkArround(workArround);
    return model;
  }

}
