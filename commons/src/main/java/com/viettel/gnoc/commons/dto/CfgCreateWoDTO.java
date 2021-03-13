/**
 * @(#)CfgTimeTroubleProcessForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CfgCreateWoEntity;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class CfgCreateWoDTO {

  //Fields
  private String id;
  private String typeId;
  private String typeName;
  private String alarmGroupId;
  private String alarmGroupName;
  private String lastUpdateTime;
  private String defaultSortField;

  public CfgCreateWoDTO(String id, String typeId, String alarmGroupId, String lastUpdateTime) {
    this.id = id;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.lastUpdateTime = lastUpdateTime;

  }

  public CfgCreateWoEntity toEntity() {
    CfgCreateWoEntity model = new CfgCreateWoEntity(
        StringUtils.validString(id) ? Long.valueOf(id) : null,
        StringUtils.validString(typeId) ? Long.valueOf(typeId) : null,
        StringUtils.validString(alarmGroupId) ? Long.valueOf(alarmGroupId) : null,
        StringUtils.validString(lastUpdateTime) ? DateTimeUtils.convertStringToDate(lastUpdateTime)
            : null
    );
    return model;
  }
}
