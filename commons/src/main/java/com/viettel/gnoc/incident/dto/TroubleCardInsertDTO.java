package com.viettel.gnoc.incident.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class TroubleCardInsertDTO {

  private String troubleId;
  private List<TroubleCardDTO> lst;
}
