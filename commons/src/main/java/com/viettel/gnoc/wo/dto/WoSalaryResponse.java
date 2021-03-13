package com.viettel.gnoc.wo.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WoSalaryResponse {

  private String key;
  private String message;
  private List<TotalWoForm> value;
  private List<String> userName;
  private String startPeriod;
  private String endPeriod;
}
