/**
 * @(#)CatReasonForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.incident.model.CatReasonEntity;
import java.util.Date;
import java.util.List;
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
public class CatReasonInSideDTO extends BaseDto {

  private Long id;
  private String reasonName;
  private String reasonCode;
  private Long parentId;
  private String description;
  private Long typeId;
  private Long reasonType;//
  private Date updateTime;
  private Long isCheckScript;
  private Long isUpdateClosure;
  private List<Long> lstTypeId;
  private Long isParentNull;

  public CatReasonInSideDTO(Long id, String reasonName, String reasonCode, Long parentId,
      String description, Long typeId, Long reasonType, Date updateTime, Long isCheckScript,
      Long isUpdateClosure) {
    this.id = id;
    this.reasonName = reasonName;
    this.reasonCode = reasonCode;
    this.parentId = parentId;
    this.description = description;
    this.typeId = typeId;
    this.reasonType = reasonType;
    this.updateTime = updateTime;
    this.isCheckScript = isCheckScript;
    this.isUpdateClosure = isUpdateClosure;
  }

  public CatReasonEntity toEntity() {
    return new CatReasonEntity(id, reasonName, reasonCode, parentId, description, typeId,
        reasonType, updateTime, isCheckScript, isUpdateClosure);
  }

//  public CatReasonDTO toModelOutSide() {
//    CatReasonDTO model = new CatReasonDTO(
//        StringUtils.validString(id) ? String.valueOf(id) : null,
//        reasonName,
//        StringUtils.validString(parentId) ? String.valueOf(parentId) : null,
//        description,
//        StringUtils.validString(typeId) ? String.valueOf(typeId) : null,
//        StringUtils.validString(updateTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(updateTime) : null,
//        reasonCode,
//        StringUtils.validString(reasonType) ? String.valueOf(reasonType) : null,
//        StringUtils.validString(isCheckScript) ? String.valueOf(isCheckScript) : null,
//        StringUtils.validString(isUpdateClosure) ? String.valueOf(isUpdateClosure) : null
//    );
//    return model;
//  }
}
