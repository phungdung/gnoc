package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.maintenance.model.MrCfgCrUnitTelEntity;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.mrCfgCrUnitTelDTO.multiple.unique}", clazz = MrCfgCrUnitTelEntity.class,
    uniqueFields = "marketCode,region,arrayCode,networkType,deviceType", idField = "cfgId")
public class MrCfgCrUnitTelDTO extends BaseDto {

  private Long cfgId;
  @NotNull(message = "validation.mrCfgCrUnitTelDTO.marketCode.notNull")
  private String marketCode;
  @NotNull(message = "validation.mrCfgCrUnitTelDTO.arrayCode.notNull")
  private String arrayCode;
  @NotNull(message = "validation.mrCfgCrUnitTelDTO.deviceType.notNull")
  private String deviceType;
  @NotNull(message = "validation.mrCfgCrUnitTelDTO.implementUnit.notNull")
  private Long implementUnit;
  @NotNull(message = "validation.mrCfgCrUnitTelDTO.checkingUnit.notNull")
  private Long checkingUnit;
  private String createdUser;
  private Date createdTime;
  private String updatedUser;
  private Date updatedTime;
  @NotNull(message = "validation.mrCfgCrUnitTelDTO.region.notNull")
  private String region;
  private Date createdDate;
  @NotNull(message = "validation.mrCfgCrUnitTelDTO.networkType.notNull")
  private String networkType;

  private String marketName;
  private String implementUnitName;
  private String implementUnitId;
  private String checkingUnitName;
  private String checkingUnitId;
  private String arrayName;
  private String resultImport;

  public MrCfgCrUnitTelDTO(Long cfgId, String marketCode, String arrayCode,
      String deviceType, Long implementUnit, Long checkingUnit, String createdUser,
      Date createdTime, String updatedUser, Date updatedTime, String region,
      Date createdDate, String networkType) {
    this.cfgId = cfgId;
    this.marketCode = marketCode;
    this.arrayCode = arrayCode;
    this.deviceType = deviceType;
    this.implementUnit = implementUnit;
    this.checkingUnit = checkingUnit;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.region = region;
    this.createdDate = createdDate;
    this.networkType = networkType;
  }

  public MrCfgCrUnitTelEntity toEntity() {
    return new MrCfgCrUnitTelEntity(cfgId, marketCode, arrayCode, deviceType, implementUnit,
        checkingUnit, createdUser, createdTime, updatedUser, updatedTime, region, createdDate,
        networkType);
  }

}
