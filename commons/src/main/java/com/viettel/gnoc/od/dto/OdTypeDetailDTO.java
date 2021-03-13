/**
 * @(#)OdTypeForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.od.model.OdTypeDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OdTypeDetailDTO extends BaseDto {

  //Fields
  private Long id;
  private Long priorityId;
  private Double processTime;
  private Long odTypeId;
  private String priorityName;
  private String priorityCode;
  private String odTypeCode;

  public OdTypeDetailEntity toEntity() {
    return new OdTypeDetailEntity(id, priorityId, processTime, odTypeId);
  }

  public OdTypeDetailDTO(Long id, Long priorityId, Double processTime, Long odTypeId) {
    this.id = id;
    this.priorityId = priorityId;
    this.processTime = processTime;
    this.odTypeId = odTypeId;
  }
}
