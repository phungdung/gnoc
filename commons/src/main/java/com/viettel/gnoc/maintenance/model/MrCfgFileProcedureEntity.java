package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrCfgFileProcedureDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "OPEN_PM", name = "MR_CFG_PROCEDURE_FILES")
@Getter
@Setter
@NoArgsConstructor
public class MrCfgFileProcedureEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CFG_PROCEDURE_FILES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "FILE_ID", unique = true, nullable = false)
  private Long fileId;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "FILE_PATH")
  private String filePath;

  @Column(name = "PROCEDURE_ID")
  private Long procedureId;

  public MrCfgFileProcedureEntity(Long fileId,
      String fileName,
      String filePath,
      Long procedureId) {
    this.fileId = fileId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.procedureId = procedureId;
  }

  public MrCfgFileProcedureDTO toDTO() {
    MrCfgFileProcedureDTO dto = new MrCfgFileProcedureDTO(
        fileId,
        fileName,
        filePath,
        procedureId);
    return dto;
  }
}
