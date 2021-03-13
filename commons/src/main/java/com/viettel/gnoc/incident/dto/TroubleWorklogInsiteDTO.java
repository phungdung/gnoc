/**
 * @(#)TroubleWorklogForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.model.TroubleWorklogEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class TroubleWorklogInsiteDTO extends BaseDto {

  //Fields
  private Long id;
  private Long createUserId;
  private String createUserName;
  private Long createUnitId;
  private String createUnitName;
  private String worklog;
  private String description;
  private Date createdTime;
  private Long troubleId;

  public TroubleWorklogInsiteDTO(
      Long id, Long createUserId, String createUserName, Long createUnitId, String createUnitName,
      String worklog, String description, Date createdTime, Long troubleId) {
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
        createdTime,
        troubleId);
    return model;
  }

  public TroubleWorklogDTO toOutSite() {
    TroubleWorklogDTO dto = new TroubleWorklogDTO(
        id,
        createUserId,
        createUserName,
        createUnitId,
        createUnitName,
        worklog,
        description,
        StringUtils.isStringNullOrEmpty(createdTime) ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(createdTime),
        troubleId
    );
    return dto;
  }

}
