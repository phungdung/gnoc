package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.maintenance.model.MrCfgProcedureBtsEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class MrCfgProcedureBtsDTO extends BaseDto {

  private Long cfgProcedureBtsId;
  private Long marketCode;
  @SizeByte(max = 255, message = "validation.mrCfgProcedureBtsDTO.deviceType.tooLong")
  private String deviceType;
  private Long cycle;
  private Long genMrBefore;
  private Long mrTime;

  @SizeByte(max = 100, message = "validation.mrCfgProcedureBtsDTO.materialType.tooLong")
  private String materialType;
  private Long maintenanceHour;

  @SizeByte(max = 20, message = "validation.mrCfgProcedureBtsDTO.supplierCode.tooLong")
  private String supplierCode;
  private String materialTypeName;
  private String marketName;
  private String deviceTypeName;

  public MrCfgProcedureBtsDTO(Long cfgProcedureBtsId, Long marketCode, String deviceType,
      Long cycle, Long genMrBefore, Long mrTime, String materialType, Long maintenanceHour,
      String supplierCode) {
    this.cfgProcedureBtsId = cfgProcedureBtsId;
    this.marketCode = marketCode;
    this.deviceType = deviceType;
    this.cycle = cycle;
    this.genMrBefore = genMrBefore;
    this.mrTime = mrTime;
    this.materialType = materialType;
    this.maintenanceHour = maintenanceHour;
    this.supplierCode = supplierCode;
  }

  public MrCfgProcedureBtsEntity toEntity() {
    try {
      MrCfgProcedureBtsEntity model = new MrCfgProcedureBtsEntity(
          this.cfgProcedureBtsId,
          this.marketCode,
          this.deviceType,
          this.cycle,
          this.genMrBefore,
          this.mrTime,
          this.materialType,
          this.maintenanceHour,
          this.supplierCode
      );
      return model;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }

  public MrCfgProcedureBtsDTO toCheckDupDTO() {
    MrCfgProcedureBtsDTO dto = new MrCfgProcedureBtsDTO();
    dto.setCfgProcedureBtsId(this.cfgProcedureBtsId);
    dto.setCycle(this.cycle);
    dto.setDeviceType(this.deviceType);
    dto.setMarketCode(this.marketCode);
    dto.setMaterialType(this.materialType);
    if (Constants.MARKET_CODE.HAITI.equals(String.valueOf(dto.getMarketCode()))) {
      dto.setSupplierCode(this.supplierCode);
    }
    return dto;
  }
}
