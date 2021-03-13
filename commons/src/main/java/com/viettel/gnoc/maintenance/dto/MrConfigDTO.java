package com.viettel.gnoc.maintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MrConfigDTO {

  private String configGroup;
  private String configCode;
  private String configName;
  private String country;
  private String configValue;
}
