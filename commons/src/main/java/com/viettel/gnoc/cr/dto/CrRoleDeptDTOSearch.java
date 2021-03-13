package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CrRoleDeptDTOSearch extends BaseDto {

  private Long cmreId;
  private String cmreCode;
  private String cmreName;
  private Long cmroutId;
  private Long unitId;
  private String unitName;
  private String parentName;
  private String parentUnitId;
}
