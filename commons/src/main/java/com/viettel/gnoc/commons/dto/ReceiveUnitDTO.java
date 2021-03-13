package com.viettel.gnoc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveUnitDTO extends BaseDto implements Cloneable {

  private Long unitId;
  private String unitName;
  private String unitCode;
  private String parentUnitId;
  private String parentUnitName;

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
