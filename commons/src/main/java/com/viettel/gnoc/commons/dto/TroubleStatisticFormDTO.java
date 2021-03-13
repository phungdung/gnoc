package com.viettel.gnoc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TroubleStatisticFormDTO {

  private String unitId;
  private Boolean isCreateUnit;
  private Boolean searchChild;
  private String startTime;
  private String endTime;
}
