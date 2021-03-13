/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.maintenance.model.MrCfgFileProcedureTelEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author tiennv
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MrCfgFileProcedureTelDTO {

  private Long fileId;
  private String fileName;
  private String filePath;
  private Long procedureId;
  private String defaultSortField;
  private String filePathGnoc;

  public MrCfgFileProcedureTelDTO(Long fileId,
      String fileName,
      String filePath,
      Long procedureId) {
    this.fileId = fileId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.procedureId = procedureId;
  }

  public MrCfgFileProcedureTelEntity toEntity() {
    MrCfgFileProcedureTelEntity model = new MrCfgFileProcedureTelEntity(
        fileId,
        fileName,
        filePath,
        procedureId);
    return model;
  }
}
