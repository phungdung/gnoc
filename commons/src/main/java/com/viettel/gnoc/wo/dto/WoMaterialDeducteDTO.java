/**
 * @(#)WoMaterialDeducteForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class WoMaterialDeducteDTO {

  private String woMaterialDeducteId;
  private String woId;
  private String userId;
  private String userName;
  private String materialId;
  private String isDeducte;
  private String sendImResult;
  private String createDate;
  private String sendImTime;
  private String actionId;
  private String quantity;
  private String reasonId;
  private String firstMetersIndex;
  private String lastMetersIndex;
  private String cableLengthOnNims;
  private String materialGroupCode;
  private String parentActionId;
  private String materialName;
  private String materialGroupName;
  private String actionName;
  private String unitName;
  private String woCode;
  private String serial;
  private String type;
  private String nationCode;
  private String nationId;
  private String defaultSortField;

  public WoMaterialDeducteInsideDTO toModelInSide() {
    WoMaterialDeducteInsideDTO model = new WoMaterialDeducteInsideDTO(
        StringUtils.validString(woMaterialDeducteId) ? Long.valueOf(woMaterialDeducteId) : null,
        StringUtils.validString(woId) ? Long.valueOf(woId) : null,
        StringUtils.validString(userId) ? Long.valueOf(userId) : null,
        userName,
        StringUtils.validString(materialId) ? Long.valueOf(materialId) : null,
        StringUtils.validString(isDeducte) ? Long.valueOf(isDeducte) : null,
        sendImResult,
        StringUtils.validString(createDate) ? DateTimeUtils.convertStringToDate(createDate) : null,
        StringUtils.validString(sendImTime) ? DateTimeUtils.convertStringToDate(sendImTime) : null,
        StringUtils.validString(actionId) ? Long.valueOf(actionId) : null,
        StringUtils.validString(quantity) ? Double.valueOf(quantity) : null,
        StringUtils.validString(reasonId) ? Long.valueOf(reasonId) : null,
        StringUtils.validString(firstMetersIndex) ? Double.valueOf(firstMetersIndex) : null,
        StringUtils.validString(lastMetersIndex) ? Double.valueOf(lastMetersIndex) : null,
        StringUtils.validString(cableLengthOnNims) ? Double.valueOf(cableLengthOnNims) : null,
        materialGroupCode,
        StringUtils.validString(parentActionId) ? Long.valueOf(parentActionId) : null,
        materialName,
        materialGroupName,
        actionName,
        unitName,
        woCode,
        serial,
        StringUtils.validString(type) ? Long.valueOf(type) : null,
        nationCode,
        StringUtils.validString(nationId) ? Long.valueOf(nationId) : null,
        defaultSortField
    );
    return model;

  }


}
