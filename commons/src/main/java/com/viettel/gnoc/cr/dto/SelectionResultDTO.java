package com.viettel.gnoc.cr.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SelectionResultDTO {

  private String key;
  private String message;
  private List<CrForNocProMonitorOnlineDTO> lstDTO;
}
