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
public class ObjectSearchDto {

  private String moduleName;
  private String parent;
  private Long isHasChildren;
  private String param;
  private Long rownum;
  private String value;
  private Long processForCr;
  private Long locationId;

}
