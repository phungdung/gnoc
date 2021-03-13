/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.cr.dto;


import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.model.CrAffectedServiceDetailsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class CrAffectedServiceDetailsDTO {

  private String defaultSortField;
  //Fields
  private String casdsId;
  private String crId;
  private String affectedServiceId;
  private String insertTime;

  public CrAffectedServiceDetailsDTO(String casdsId, String crId, String affectedServiceId,
      String insertTime) {
    this.casdsId = casdsId;
    this.crId = crId;
    this.affectedServiceId = affectedServiceId;
    this.insertTime = insertTime;
  }

  public CrAffectedServiceDetailsEntity toEntity() {
    CrAffectedServiceDetailsEntity model = new CrAffectedServiceDetailsEntity(
        StringUtils.validString(casdsId) ?
            Long.valueOf(casdsId) : null,
        StringUtils.validString(crId) ?
            Long.valueOf(crId) : null,
        StringUtils.validString(affectedServiceId) ?
            Long.valueOf(affectedServiceId) : null,
        StringUtils.validString(insertTime) ?
            DateTimeUtils.convertStringToDate(insertTime)
            : null);
    return model;
  }

}

