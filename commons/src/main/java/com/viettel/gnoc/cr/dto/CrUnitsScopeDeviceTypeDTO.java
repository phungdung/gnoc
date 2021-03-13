package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.cr.model.CrUnitsScopeDeviceTypeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DungPV
 */
@Setter
@Getter
@NoArgsConstructor
public class CrUnitsScopeDeviceTypeDTO {

  private Long id;
  private Long deviceTypeId;
  private String deviceTypeName;
  private Long crUnitsScopeId;

  public CrUnitsScopeDeviceTypeDTO(Long id, Long deviceTypeId, Long crUnitsScopeId) {
    this.id = id;
    this.deviceTypeId = deviceTypeId;
    this.crUnitsScopeId = crUnitsScopeId;
  }

  public CrUnitsScopeDeviceTypeEntity toDTO() {
    return new CrUnitsScopeDeviceTypeEntity(id, deviceTypeId, crUnitsScopeId);
  }
}
