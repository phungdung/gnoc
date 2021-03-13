/**
 * @(#)TransitionStateConfigForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.TransitionStateConfigEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.commons.validator.SizeByte;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.transitionStateConfigDTO.multiple.unique}", clazz = TransitionStateConfigEntity.class,
    uniqueFields = "process,beginStateId,endStateId", idField = "id")
public class TransitionStateConfigDTO extends BaseDto {

  private Long id;
  @NotNull(message = "validation.transitionStateConfigDTO.beginStateId.NotNull")
  private Long beginStateId;
  @NotNull(message = "validation.transitionStateConfigDTO.endStateId.NotNull")
  private Long endStateId;
  @SizeByte(max = 500, message = "validation.transitionStateConfigDTO.description.tooLong")
  private String description;
  @NotNull(message = "validation.transitionStateConfigDTO.process.NotNull")
  private Long process;
  // nang cap
  private String skipStatus;
  private Long roleCode;

  private String beginStateName;
  private String endStateName;
  private String processName;
  private String processCode;
  private String beginStateCode;
  private String endStateCode;

  public TransitionStateConfigDTO(Long id, Long beginStateId, Long endStateId,
      String description, Long process, String skipStatus, Long roleCode) {
    this.id = id;
    this.beginStateId = beginStateId;
    this.endStateId = endStateId;
    this.description = description;
    this.process = process;
    this.skipStatus = skipStatus;
    this.roleCode = roleCode;
  }

  public TransitionStateConfigDTO(Long id, Long beginStateId, Long endStateId,
      String description, Long process, String beginStateName, String endStateName, String skipStatus, Long roleCode) {
    this.id = id;
    this.beginStateId = beginStateId;
    this.endStateId = endStateId;
    this.description = description;
    this.process = process;
    this.skipStatus = skipStatus;
    this.roleCode = roleCode;
    this.beginStateName = beginStateName;
    this.endStateName = endStateName;
  }

  public TransitionStateConfigEntity toEntity() {
    return new TransitionStateConfigEntity(id, beginStateId, endStateId, description, process, skipStatus, roleCode);
  }

}
