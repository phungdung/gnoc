package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class ResultTroubleDTO {

  private String id;
  private String key;
  private String message;
  private String requestTime;
  private String finishTime;
  private TTChangeStatusDTO ttChangeStatusDTO;
}
