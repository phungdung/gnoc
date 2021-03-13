package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.MaterialCodienEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialCodienDTO extends BaseDto {

  private Long id;
  private Long woId;
  private Long materialId;
  private String materialName;
  private String quantity;
  private String serial;
  private String totalCost;
  private String unit;
  private String unitPrice;
  private String genericCode;
  private String deviceCode;
  private String provinceCode;
  private String stationCode;
  private String materialCode;
  private String dateTime;

  public MaterialCodienEntity toEntity() {
    return new MaterialCodienEntity(id, woId, materialId, materialName, quantity, serial, totalCost,
        unit, unitPrice, genericCode, deviceCode, provinceCode, stationCode, materialCode,
        dateTime);
  }

}
