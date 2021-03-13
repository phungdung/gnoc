/**
 * @(#)CatReasonForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author itsol
 */

@Getter
@Setter
@NoArgsConstructor
public class CatReasonDTO extends BaseDto {

  //Fields
  private String id;
  private String reasonName;
  private String parentId;
  private String description;
  private String typeId;
  private String updateTime;
  private String reasonCode;
  private String reasonType;
  private String isCheckScript;
  private String isUpdateClosure;

  private Boolean isRoot;
  private String status;
  private String position;
  private String level;

  public CatReasonDTO(String id, String reasonName, String parentId, String description,
      String typeId, String updateTime, String reasonCode, String reasonType,
      String isCheckScript, String isUpdateClosure) {
    this.id = id;
    this.reasonName = reasonName;
    this.parentId = parentId;
    this.description = description;
    this.typeId = typeId;
    this.updateTime = updateTime;
    this.reasonCode = reasonCode;
    this.reasonType = reasonType;
    this.isCheckScript = isCheckScript;
    this.isUpdateClosure = isUpdateClosure;
  }
}
