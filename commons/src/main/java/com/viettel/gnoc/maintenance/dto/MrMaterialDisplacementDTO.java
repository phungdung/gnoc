package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrMaterialDisplacementEntity;
import java.util.Date;
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
public class MrMaterialDisplacementDTO extends BaseDto {

  //Fields
  private Long materialId;
  private String deviceType;
  private String materialName;
  private String fuelType;
  private String power;
  private String serial;
  private String unit;
  private String unitPrice;
  private String quocta;
  private Long quantity;
  private String woId;
  private String deviceCode;
  private String stationCode;
  private String provinceCode;
  private String materialCode;
  private String genericCode;
  private Date dateTime;

  private String totalCost;

  //Constructor


  public MrMaterialDisplacementDTO(Long materialId, String deviceType, String materialName,
      String fuelType, String power, String serial, String unit, String unitPrice,
      String quocta, Long quantity, String woId, String deviceCode, String stationCode,
      String provinceCode, String materialCode, String genericCode, Date dateTime) {
    this.materialId = materialId;
    this.deviceType = deviceType;
    this.materialName = materialName;
    this.fuelType = fuelType;
    this.power = power;
    this.serial = serial;
    this.unit = unit;
    this.unitPrice = unitPrice;
    this.quocta = quocta;
    this.quantity = quantity;
    this.woId = woId;
    this.deviceCode = deviceCode;
    this.stationCode = stationCode;
    this.provinceCode = provinceCode;
    this.materialCode = materialCode;
    this.genericCode = genericCode;
    this.dateTime = dateTime;
  }

  public MrMaterialDisplacementEntity toEntity() {
    MrMaterialDisplacementEntity model = new MrMaterialDisplacementEntity(
        materialId,
        deviceType,
        materialName,
        fuelType,
        power,
        serial,
        unit,
        unitPrice,
        quocta,
        quantity,
        woId,
        deviceCode,
        stationCode,
        provinceCode,
        materialCode,
        genericCode,
        dateTime
    );
    return model;
  }
}
