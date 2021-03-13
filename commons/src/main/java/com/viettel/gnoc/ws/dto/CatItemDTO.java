package com.viettel.gnoc.ws.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class CatItemDTO {

  //Fields
  private String itemId;
  private String itemCode;
  private String itemName;
  private String itemValue;
  private String categoryId;
  private String categoryIdName;
  private String position;
  private String description;
  private String editable;
  private String parentItemId;
  private String status;
  //    private static long changedTime = 0;
  private String parentItemName;
  private String updateTime;
  private String categoryCode;
  private String categoryName;
  private String checkbox;
  private String defaultSortField;

  public CatItemDTO() {
    setDefaultSortField("itemName");
  }

  public CatItemDTO(String itemId, String itemCode, String itemName, String itemValue,
      String categoryId, String position, String description, String editable, String parentItemId,
      String status) {
    this.itemId = itemId;
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

  public com.viettel.gnoc.commons.dto.CatItemDTO toInsideDTO() {
    try {
      return new com.viettel.gnoc.commons.dto.CatItemDTO(
          StringUtils.isNotNullOrEmpty(itemId) ? Long.valueOf(itemId) : null,
          StringUtils.isNotNullOrEmpty(updateTime) ? DateTimeUtils.convertStringToDate(updateTime)
              : null,
          itemCode,
          itemName,
          itemValue,
          StringUtils.isStringNullOrEmpty(categoryId) ? null
              : Long.valueOf(categoryId),
          StringUtils.isStringNullOrEmpty(position) ? null
              : Long.valueOf(position),
          description,
          StringUtils.isStringNullOrEmpty(editable) ? null
              : Long.valueOf(editable),
          StringUtils.isStringNullOrEmpty(parentItemId) ? null
              : Long.valueOf(parentItemId),
          StringUtils.isStringNullOrEmpty(status) ? null
              : Long.valueOf(status)
      );
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

}
