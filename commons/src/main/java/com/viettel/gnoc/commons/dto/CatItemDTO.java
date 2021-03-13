/**
 * @(#)CatItemForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CatItemEntity;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author itsol
 * @version 2.0
 * @since 12/03/2018
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.catItem.null.unique}", clazz = CatItemEntity.class, uniqueFields = "itemCode", idField = "itemId")
public class CatItemDTO extends BaseDto {

  //Fields
  private Long itemId;
  private Date updateTime;
  @NotEmpty(message = "{validation.catItem.null.itemCode}")
  private String itemCode;
  @NotEmpty(message = "{validation.catItem.null.itemName}")
  private String itemName;
  @NotEmpty(message = "{validation.catItem.null.itemValue}")
  private String itemValue;
  @NotNull(message = "{validation.catItem.null.categoryId}")
  private Long categoryId;
  private Long position;
  private String description;
  @NotNull(message = "{validation.catItem.null.editable}")
  private Long editable;
  private Long parentItemId;
  @NotNull(message = "{validation.catItem.null.status}")
  private Long status;
  private String categoryIdName;
  private String parentItemName;
  private String categoryCode;
  private String categoryName;

  private String editableStr;
  private String statusStr;

  public CatItemEntity toEntity() {
    return new CatItemEntity(itemId, updateTime, itemCode, itemName, itemValue, categoryId,
        position, description, editable, parentItemId, status);
  }

  public CatItemDTO(Long itemId, Date updateTime, String itemCode, String itemName,
      String itemValue, Long categoryId, Long position, String description, Long editable,
      Long parentItemId, Long status) {
    this.itemId = itemId;
    this.updateTime = updateTime;
    this.itemCode = itemCode;
    this.itemName = itemName;
    this.itemValue = itemValue;
    this.categoryId = categoryId;
    this.position = position;
    this.description = description;
    this.editable = editable;
    this.parentItemId = parentItemId;
    this.status = status;
  }

  //cuong tm
  public CatItemDTO(String itemId, String itemName) {
    this.itemId = Long.parseLong(itemId);
    this.itemName = itemName;
  }

  public com.viettel.gnoc.ws.dto.CatItemDTO toOutsideDTO() {
    return new com.viettel.gnoc.ws.dto.CatItemDTO(
        StringUtils.isLongNullOrEmpty(itemId) ? null : itemId.toString(),
        itemCode,
        itemName,
        itemValue,
        StringUtils.isLongNullOrEmpty(categoryId) ? null : categoryId.toString(),
        categoryIdName,
        StringUtils.isLongNullOrEmpty(position) ? null : position.toString(),
        description,
        StringUtils.isLongNullOrEmpty(editable) ? null : editable.toString(),
        StringUtils.isLongNullOrEmpty(parentItemId) ? null : parentItemId.toString(),
        StringUtils.isLongNullOrEmpty(status) ? null : status.toString(),
        parentItemName,
        StringUtils.isStringNullOrEmpty(updateTime) ? null
            : DateUtil.date2ddMMyyyyHHMMss(updateTime),
        categoryCode,
        categoryName,
        null,
        null);
  }
}
