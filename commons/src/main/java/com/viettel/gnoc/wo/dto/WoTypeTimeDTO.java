package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoTypeTimeDTO extends BaseDto {

  private Long woTypeTimeId;
  private Double duration;
  private Long isImmediate;
  private Long userApprovePending;
  private Long waitForApprovePending;
  private Long woTypeId;

}
