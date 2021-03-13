package com.viettel.gnoc.ws.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrMaterialDisplacementEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MrMaterialDisplacementDTO extends BaseDto {

  private String materialId;
  private String deviceType;
  private String materialName;
  private String fuelType;
  private String power;
  private String serial;
  private String unit;
  private String unitPrice;
  private String quocta;
  private String quantity;
  private String totalCost;
  private String woId;
  private String deviceCode;
  private String stationCode;
  private String provinceCode;
  private String materialCode;
  private String genericCode;
  private String dateTime;

  public MrMaterialDisplacementEntity toEntity() {
    try {
      MrMaterialDisplacementEntity model = new MrMaterialDisplacementEntity(
          StringUtils.isNotNullOrEmpty(materialId) ? Long.valueOf(materialId) : null,
          deviceType,
          materialName,
          fuelType,
          power,
          serial,
          unit,
          unitPrice,
          quocta,
          StringUtils.isNotNullOrEmpty(quantity) ? Long.valueOf(quantity) : null,
          woId,
          deviceCode,
          stationCode,
          provinceCode,
          materialCode,
          genericCode,
          !StringUtils.isStringNullOrEmpty(dateTime) ? DataUtil.convertStringToDateTime(dateTime) : null
      );
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
