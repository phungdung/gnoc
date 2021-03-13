package com.viettel.gnoc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatVendorBODTO extends BaseDto {

  private String catCode;
  private String description;
  private String vendorCode;
  private Long vendorId;
  private String vendorName;
}
