package com.viettel.gnoc.maintenance.dto;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.maintenance.model.MrITSoftCrImplUnitEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.mrCfgCrUnitITDTO.multiple.unique}", clazz = MrITSoftCrImplUnitEntity.class,
    uniqueFields = "marketCode,region,arrayCode,deviceTypeId", idField = "cfgId")
public class MrITSoftCrImplUnitDTO extends BaseDto {

  private Long cfgId;
  private String marketCode;
  private String marketName;
  private String arrayCode;
  private String arrayName;
  private String deviceTypeId;
  private String deviceTypeName;
  private String implementUnit;
  private String implementUnitName;
  private String checkingUnit;
  private String checkingUnitName;
  private String createUser;
  private String approveUserLv1;
  private String approveUserLv2;
  private Long isCreateWO;
  private String manageUnit;
  private String manageUnitName;
  private String region;
  private String resultImport;


  public MrITSoftCrImplUnitDTO(Long cfgId, String marketCode, String arrayCode, String deviceTypeId,
      String implementUnit, String checkingUnit, String createUser, String approveUserLv1,
      String approveUserLv2, Long isCreateWO, String manageUnit, String region) {
    this.cfgId = cfgId;
    this.marketCode = marketCode;
    this.arrayCode = arrayCode;
    this.deviceTypeId = deviceTypeId;
    this.implementUnit = implementUnit;
    this.checkingUnit = checkingUnit;
    this.createUser = createUser;
    this.approveUserLv1 = approveUserLv1;
    this.approveUserLv2 = approveUserLv2;
    this.isCreateWO = isCreateWO;
    this.manageUnit = manageUnit;
    this.region = region;
  }

  public MrITSoftCrImplUnitEntity toModel() {
    try {
      MrITSoftCrImplUnitEntity model = new MrITSoftCrImplUnitEntity(
          cfgId,
          marketCode,
          arrayCode,
          deviceTypeId,
          StringUtils.validString(implementUnit) ? Long.valueOf(implementUnit) : null,
          StringUtils.validString(checkingUnit) ? Long.valueOf(checkingUnit) : null,
          createUser,
          approveUserLv1,
          approveUserLv2,
          isCreateWO,
          StringUtils.validString(manageUnit) ? Long.valueOf(manageUnit) : null,
          region
      );
      return model;
    } catch (Exception e) {

    }
    return null;
  }
}
