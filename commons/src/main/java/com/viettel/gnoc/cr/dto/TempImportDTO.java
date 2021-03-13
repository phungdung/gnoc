package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.TempImportEntity;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Unique(message = "{validation.cr.tempImport.isDupplicate.CR}", clazz = TempImportEntity.class, uniqueField = "code", idField = "tempImportId")

public class TempImportDTO extends BaseDto {

  private Long tempImportId;
  @NotEmpty(message = "{validation.tempImport.null.code}")
  private String code;
  private String name;
  @NotNull(message = "{validation.tempImport.null.totalColumn}")
  private Long totalColumn;
  private String title;
  private Long createrId;
  private Date createrTime;
  private Long processTypeId;
  private Long isActive;
  private String isActiveStr;
  private Long webServiceMethodId;
  private Long isValidateInput;
  private Long isValidateOutput;
  private Long isRevert;
  private Long isMecFile;
  private String path;
  private Long appliedSystem;
  private Long isEditable;
  private String isEditableStr;

  private Long fileType;
  private Long cpteId;
  private List<TempImportColDTO> tempImportColDTOS;
  private List<LanguageExchangeDTO> listName;
  private List<Long> idDeleteList;

  public TempImportDTO(Long tempImportId,
      String code, String name,
      Long totalColumn,
      String title, Long createrId, Date createrTime, Long processTypeId, Long isActive,
      Long webServiceMethodId, Long isValidateInput, Long isValidateOutput, Long isRevert,
      Long isMecFile, String path, Long appliedSystem, Long isEditable) {
    this.tempImportId = tempImportId;
    this.code = code;
    this.name = name;
    this.totalColumn = totalColumn;
    this.title = title;
    this.createrId = createrId;
    this.createrTime = createrTime;
    this.processTypeId = processTypeId;
    this.isActive = isActive;
    this.webServiceMethodId = webServiceMethodId;
    this.isValidateInput = isValidateInput;
    this.isValidateOutput = isValidateOutput;
    this.isRevert = isRevert;
    this.isMecFile = isMecFile;
    this.path = path;
    this.appliedSystem = appliedSystem;
    this.isEditable = isEditable;
  }

  public TempImportEntity toEntity() {
    return new TempImportEntity(
        tempImportId,
        code,
        name,
        totalColumn,
        title,
        createrId,
        createrTime,
        processTypeId,
        isActive,
        webServiceMethodId,
        isValidateInput,
        isValidateOutput,
        isRevert,
        isMecFile,
        path,
        appliedSystem,
        isEditable
    );
  }
}
