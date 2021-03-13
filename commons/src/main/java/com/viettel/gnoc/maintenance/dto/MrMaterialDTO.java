package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrMaterialEntity;
import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@NoArgsConstructor
@Slf4j
public class MrMaterialDTO extends BaseDto implements Cloneable {

  private Long materialId;
  @NotEmpty(message = "{validation.MrMaterialDTO.null.materialName}")
  private String materialName;
  private String serial;
  @Max(value = 9999999999L, message = "{validation.MrMaterialDTO.unitPrice.tooLong}")
  private Long unitPrice;
  private Date dateTime;
  @NotEmpty(message = "{validation.MrMaterialDTO.null.deviceType}")
  private String deviceType;
  @NotNull(message = "{validation.MrMaterialDTO.null.marketCode}")
  private Long marketCode;

  private String leeValue;
  private String materialNameEN;
  private String userManager;
  private String deviceTypeName;
  private String resultImport;
  private String validate;
  private String dateTimeStr;
  private String unitPriceStr;
  private Date dateTimeToSearch;
  private Date dateTimeFromSearch;

  public MrMaterialDTO(Long materialId, String materialName, String serial, Long unitPrice,
      Date dateTime, String deviceType, Long marketCode) {
    this.materialId = materialId;
    this.materialName = materialName;
    this.serial = serial;
    this.unitPrice = unitPrice;
    this.dateTime = dateTime;
    this.deviceType = deviceType;
    this.marketCode = marketCode;
  }

  public MrMaterialDTO(Long materialId) {
    this.materialId = materialId;
  }

  public MrMaterialEntity toEntity() {
    return new MrMaterialEntity(materialId, materialName, serial, unitPrice, dateTime,
        deviceType, marketCode);
  }

  @Override
  public MrMaterialDTO clone() {
    try {
      return (MrMaterialDTO) super.clone();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }
}
