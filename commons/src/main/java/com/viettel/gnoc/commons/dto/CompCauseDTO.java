/**
 * @(#)CompCauseForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.model.CompCauseEntity;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompCauseDTO {

  //Fields
  private String compCauseId;
  private String name;
  private String code;
  private String description;
  private String isactive;
  private String serviceType;
  private String parentId;
  private String parentName;
  private String levelId;
  private String cfgType;
  private String cfgTypeName;
  private String errorType;
  private String errorTypeName;
  private String lineType;
  private String isSignal;
  private String defaultSortField;
  private String action;
  private String error;
  private List<Long> ccGroupId;
  private String serviceTypeId;

  public CompCauseDTO(
      String compCauseId, String name, String code, String description, String isactive,
      String serviceType, String parentId, String levelId, String cfgType, String errorType,
      String lineType, String isSignal) {
    this.compCauseId = compCauseId;
    this.name = name;
    this.code = code;
    this.description = description;
    this.isactive = isactive;
    this.serviceType = serviceType;
    this.parentId = parentId;
    this.levelId = levelId;
    this.cfgType = cfgType;
    this.errorType = errorType;
    this.lineType = lineType;
    this.isSignal = isSignal;
  }

  public void setCfgType(String cfgType) {
    if (cfgType == null) {
      this.cfgType = null;
    } else {
      this.cfgType = cfgType;
      switch (this.cfgType) {
        case "1":
          setCfgTypeName(I18n.getLanguage("common.comcause.cfgType.1"));
          break;
        case "2":
          setCfgTypeName(I18n.getLanguage("common.comcause.cfgType.2"));
          break;
        case "3":
          setCfgTypeName(I18n.getLanguage("common.comcause.cfgType.3"));
          break;
        default:
          setCfgTypeName(null);
          break;
      }
    }
  }


  public CompCauseEntity toEntity() {
    CompCauseEntity model = new CompCauseEntity(
        !StringUtils.validString(compCauseId) ? null
            : Long.valueOf(compCauseId),
        name,
        code,
        description,
        !StringUtils.validString(isactive) ? null
            : Long.valueOf(isactive),
        serviceType,
        !StringUtils.validString(parentId) ? null
            : Long.valueOf(parentId),
        !StringUtils.validString(levelId) ? null
            : Long.valueOf(levelId),
        !StringUtils.validString(cfgType) ? null
            : Long.valueOf(cfgType),
        !StringUtils.validString(errorType) ? null
            : Long.valueOf(errorType),
        lineType,
        !StringUtils.validString(isSignal) ? null
            : Double.parseDouble(isSignal));
    return model;
  }
}
