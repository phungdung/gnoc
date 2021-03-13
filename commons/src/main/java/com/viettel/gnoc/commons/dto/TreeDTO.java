package com.viettel.gnoc.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TungPV
 */
@Getter
@Setter
@NoArgsConstructor
public class TreeDTO {

  private String key;
  private String value;
  private String title;
  private Boolean isLeaf;
  private Boolean disabled;
}
