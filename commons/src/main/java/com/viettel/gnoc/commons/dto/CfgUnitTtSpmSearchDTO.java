package com.viettel.gnoc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CfgUnitTtSpmSearchDTO extends BaseDto {

  private String id;
  private String typeId;
  private String locationId;
  private String unitId;

  private String typeName;
  private String locationName;
  private String unitName;
  private String typeUnit;
  private String typeUnitName;
  private String defaultSortField;
}
