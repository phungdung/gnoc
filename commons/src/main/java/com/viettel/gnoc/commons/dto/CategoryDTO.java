
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CategoryEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kienpv
 * @version 1.0
 * @since 8/25/2015 4:16 PM
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.CfgCategory.null.unique}", clazz = CategoryEntity.class, uniqueFields = "categoryCode", idField = "categoryId")
public class CategoryDTO extends BaseDto {

  private Long categoryId;

  @NotNull(message = "validation.CategoryDTO.categoryName.NotNull")
  @Size(max = 100, message = "validation.CategoryDTO.categoryName.tooLong")
  private String categoryName;

  @NotNull(message = "validation.CategoryDTO.categoryCode.NotNull")
  @Size(max = 50, message = "validation.CategoryDTO.categoryCode.tooLong")
  private String categoryCode;
  private String description;
  private Long editable;
  private String editableDisplay;
  private Long parentCategoryId;
  private String parentName;

  private String defaultSortField;

  List<ConditionBean> lstCondition;
  List<CategoryDTO> categoryDTO;

  public CategoryDTO(Long categoryId, String categoryName, String categoryCode,
      String description, Long editable, Long parentCategoryId) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.categoryCode = categoryCode;
    this.description = description;
    this.editable = editable;
    this.parentCategoryId = parentCategoryId;
  }

  public CategoryEntity toEntity() {
    CategoryEntity entity = new CategoryEntity(categoryId, categoryName, categoryCode,
        description, editable, parentCategoryId);
    return entity;
  }
}
