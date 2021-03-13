package com.viettel.gnoc.sr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnitSRCatalogDTO {

  private String unitId;
  private String unitName;
  private String serviceId;
  private String error;
  private String country;

  private String autoCreateSR;
  private String roleCode;
  private String defaultSortField;

  public UnitSRCatalogDTO(String error) {
    this.error = error;
  }
}
