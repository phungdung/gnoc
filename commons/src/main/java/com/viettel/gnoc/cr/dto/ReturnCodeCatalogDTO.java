package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.ReturnCodeCatalogEntity;
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
@Unique(message = "{validation.cr.returnCodeCatalog.isDupplicate.CR}", clazz = ReturnCodeCatalogEntity.class, uniqueField = "returnCode", idField = "returnCodeCategoryId")

public class ReturnCodeCatalogDTO extends BaseDto {

  private Long returnCodeCategoryId;
  @NotEmpty(message = "{validation.returnCodeCatalog.null.returnCode}")
  private String returnCode;
  @NotEmpty(message = "{validation.returnCodeCatalog.null.returnTitle}")
  private String returnTitle;
  @NotNull(message = "{validation.returnCodeCatalog.null.returnCategory}")
  private Long returnCategory;
  private String returnCategoryName;
  private String description;
  private Long isActive;
  private Long isEditable;

  public ReturnCodeCatalogDTO(Long returnCodeCategoryId, String returnCode, String returnTitle,
      Long returnCategory, String description, Long isActive, Long isEditable) {
    this.returnCodeCategoryId = returnCodeCategoryId;
    this.returnCode = returnCode;
    this.returnTitle = returnTitle;
    this.returnCategory = returnCategory;
    this.description = description;
    this.isActive = isActive;
    this.isEditable = isEditable;
  }

  public ReturnCodeCatalogEntity toEntity() {
    return new ReturnCodeCatalogEntity(
        returnCodeCategoryId,
        returnCode,
        returnTitle,
        returnCategory,
        description,
        isActive,
        isEditable
    );
  }
}
