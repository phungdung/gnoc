package com.viettel.gnoc.wo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldForm {

  private String fieldKey;
  private String fieldName;
  private String fieldValue;
  private Long visible;

  public FieldForm(String fieldKey, String fieldName, String fieldValue) {
    this.fieldKey = fieldKey;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue == null ? "" : fieldValue;
    this.visible = 1L;//default show
  }
}
