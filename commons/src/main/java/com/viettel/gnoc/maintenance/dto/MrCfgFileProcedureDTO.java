package com.viettel.gnoc.maintenance.dto;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrCfgFileProcedureEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MrCfgFileProcedureDTO extends BaseDto {

  private Long fileId;
  private String fileName;
  private String filePath;
  private Long procedureId;
  private String defaultSortField;

  public MrCfgFileProcedureDTO(Long fileId,
      String fileName,
      String filePath,
      Long procedureId) {
    this.fileId = fileId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.procedureId = procedureId;
  }

  public MrCfgFileProcedureEntity toEntity() {
    MrCfgFileProcedureEntity model = new MrCfgFileProcedureEntity(
        fileId,
        fileName,
        filePath,
        procedureId);
    return model;
  }
}
