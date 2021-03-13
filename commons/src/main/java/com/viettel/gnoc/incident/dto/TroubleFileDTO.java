/**
 * @(#)TroubleFileForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.incident.model.TroubleFileEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TroubleFileDTO extends BaseDto {

  //Fields
  private Long id;
  private Long fileId;
  private String fileIdName;
  private Long troubleId;
  private String troubleIdName;
  private Long createUnitId;
  private String createUnitName;
  //tamdx_start
  private String fileName;
  private String createUserId;
  private String createUserName;
  private Date createTime;
  private String path;

  public TroubleFileDTO(Long id, Long fileId, Long troubleId) {
    this.id = id;
    this.fileId = fileId;
    this.troubleId = troubleId;
  }

  public TroubleFileEntity toEntity() {
    TroubleFileEntity model = new TroubleFileEntity(
        id,
        fileId,
        troubleId);
    return model;
  }
}
