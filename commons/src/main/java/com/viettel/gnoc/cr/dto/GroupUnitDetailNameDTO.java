package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
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
public class GroupUnitDetailNameDTO extends BaseDto {

  private Long groupUnitDetailId;
  private String changedTime;
  private long unitId;
  private String unitName;
  private String unitCode;
  private long groupUnitId;
  private String groupUnitCode;
  private String groupUnitName;
}
