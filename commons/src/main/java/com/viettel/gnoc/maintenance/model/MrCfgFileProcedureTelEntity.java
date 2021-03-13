/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrCfgFileProcedureTelDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author tiennv
 */
@Entity
@Table(schema = "OPEN_PM", name = "MR_CFG_PROCEDURE_TEL_FILES")
@Getter
@Setter
@NoArgsConstructor
public class MrCfgFileProcedureTelEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CFG_PROCEDURE_TEL_FILES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "FILE_ID", unique = true, nullable = false)
  private Long fileId;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "FILE_PATH")
  private String filePath;

  @Column(name = "PROCEDURE_ID")
  private Long procedureId;

  public MrCfgFileProcedureTelEntity(Long fileId,
      String fileName,
      String filePath,
      Long procedureId) {
    this.fileId = fileId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.procedureId = procedureId;
  }

  public MrCfgFileProcedureTelDTO toDTO() {
    MrCfgFileProcedureTelDTO dto = new MrCfgFileProcedureTelDTO(
        fileId,
        fileName,
        filePath,
        procedureId);
    return dto;
  }
}
