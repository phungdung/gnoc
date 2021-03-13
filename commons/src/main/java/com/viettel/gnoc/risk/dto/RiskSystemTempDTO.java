package com.viettel.gnoc.risk.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskSystemTempDTO {

  private String id;
  private String systemCode;
  private String systemName;
  private String schedule;
  private String systemPriority;
  private String lastUpdateTime;
  private String countryId;
  private List<String> lstFileName;

}
