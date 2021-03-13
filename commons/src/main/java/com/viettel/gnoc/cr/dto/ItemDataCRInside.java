package com.viettel.gnoc.cr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DungPV
 */
@Setter
@Getter
@NoArgsConstructor
public class ItemDataCRInside {

  private String defaultSortField;
  private Long valueStr;
  private String displayStr;
  private String secondValue;
  private String thirdValue;
  private int clevel;
}
