/**
 * @(#)CrProcessTemplateBO.java 11/16/2015 5:23 PM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.CrProcessTemplateEntity;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kienpv
 * @version 1.0
 * @since 11/16/2015 5:23 PM
 */
@Setter
@Getter
@NoArgsConstructor
@Slf4j
public class CrProcessTemplateDTO extends BaseDto {

  private Long cpteId;

  @NotEmpty(message = "{validation.crProcessTemplate.null.crProcessId}")
  private Long crProcessId;

  @NotEmpty(message = "{validation.crProcessTemplate.null.tempImportId}")
  private Long tempImportId;

  private Long fileType;

  private String name;

  private String code;

  public CrProcessTemplateDTO(Long cpteId, Long crProcessId, Long tempImportId, Long fileType) {
    this.cpteId = cpteId;
    this.crProcessId = crProcessId;
    this.tempImportId = tempImportId;
    this.fileType = fileType;
  }

  public CrProcessTemplateEntity toEntity() {
    CrProcessTemplateEntity entity = new CrProcessTemplateEntity(cpteId, crProcessId, tempImportId,
        fileType);

    return entity;
  }
}

