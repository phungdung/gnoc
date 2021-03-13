package com.viettel.gnoc.cr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class ItemDataCR {

  private String defaultSortField;
  private String valueStr;
  private String secondValue;
  private String displayStr;
  private String thirdValue;

  public ItemDataCR() {
    setDefaultSortField("name");
  }
}
