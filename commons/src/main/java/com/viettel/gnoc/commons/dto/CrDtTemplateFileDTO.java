package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CrDtTemplateFileEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.CrDtTemplateFileDTO.unique}", clazz = CrDtTemplateFileEntity.class, uniqueFields = "crProcessId,templateType", idField = "crDtTemplateFileId")
public class CrDtTemplateFileDTO extends BaseDto {

  private Long crDtTemplateFileId;
  @NotNull(message = "{validation.CrDtTemplateFileDTO.null.crProcessId}")
  private Long crProcessId;
  @NotNull(message = "{validation.CrDtTemplateFileDTO.null.fileName}")
  private String fileName;
  @NotNull(message = "{validation.CrDtTemplateFileDTO.null.templateType}")
  private String templateType;
  private Date modifiedDate;
  private String crProcessName;
  @NotNull(message = "{validation.CrDtTemplateFileDTO.null.crProcessParentId}")
  private Long crProcessParentId;
  private String crProcessParentName;
  private String pathFile;

  public CrDtTemplateFileDTO(Long crDtTemplateFileId, Long crProcessId, String fileName,
      String templateType, Date modifiedDate) {
    this.crDtTemplateFileId = crDtTemplateFileId;
    this.crProcessId = crProcessId;
    this.fileName = fileName;
    this.templateType = templateType;
    this.modifiedDate = modifiedDate;
  }

  public CrDtTemplateFileEntity toEntity() {
    return new CrDtTemplateFileEntity(crDtTemplateFileId, crProcessId, fileName, templateType,
        modifiedDate);
  }
}
