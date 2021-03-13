package com.viettel.gnoc.wo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatLocationDTO {

  private Long locationId;
  private String locationCode;
  private String locationName;
  private Long parentId;
  private String nationCode;
  private Long status;

}
