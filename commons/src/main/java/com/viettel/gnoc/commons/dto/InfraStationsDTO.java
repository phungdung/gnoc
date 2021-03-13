package com.viettel.gnoc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfraStationsDTO extends BaseDto {

  private Long stationId;
  private String stationCode;
}
