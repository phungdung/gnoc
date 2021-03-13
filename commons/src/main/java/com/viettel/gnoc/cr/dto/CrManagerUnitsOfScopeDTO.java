package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.CrManagerUnitsOfScopeEntity;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DungPV
 */

@Setter
@Getter
@NoArgsConstructor
//@MultiFieldUnique(message = "{validation.CrManagerUnitsOfScopeDTO.unique}", clazz = CrManagerUnitsOfScopeEntity.class, uniqueFields = "unitId,cmseId", idField = "cmnoseId")
public class CrManagerUnitsOfScopeDTO extends BaseDto {

  private Long cmnoseId;
  @NotNull(message = "{validation.CrManagerUnitsOfScopeDTO.cmseId.NotNull}")
  private Long cmseId;
  private String cmseCode;
  private String cmseName;
  @NotNull(message = "{validation.CrManagerUnitsOfScopeDTO.unitId.NotNull}")
  private Long unitId;
  private String unitCode;
  private String unitName;
  private Long crTypeId;
  private String crTypeName;
  private Long deviceType;
  private String deviceTypeName;
  private String changedTime;

  List<CrUnitsScopeDeviceTypeDTO> lstCrUnitsScopeDeviceTypeDTO;

  public CrManagerUnitsOfScopeDTO(Long cmnoseId, Long cmseId, Long unitId, Long crTypeId,
      Long deviceType) {
    this.cmnoseId = cmnoseId;
    this.cmseId = cmseId;
    this.unitId = unitId;
    this.crTypeId = crTypeId;
    this.deviceType = deviceType;
  }

  public CrManagerUnitsOfScopeEntity toModel() {
    return new CrManagerUnitsOfScopeEntity(cmnoseId, cmseId, unitId, crTypeId, deviceType);
  }
}
