package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CfgUnitTtSpmEntity;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.cfgUnitTtSpm.isDupplicate.TT}", clazz = CfgUnitTtSpmEntity.class,
    uniqueFields = "typeId,locationId,unitId,typeUnit", idField = "id")
public class CfgUnitTtSpmDTO {
  //Fields

  private String id;

  private String typeId;

  private String locationId;

  private String unitId;

  private String typeName;

  private String locationName;

  private String unitName;

  private String typeUnit;

  private String typeUnitName;

  private String defaultSortField;

  public CfgUnitTtSpmDTO(String id, String typeId, String locationId, String unitId,
      String typeUnit) {
    this.id = id;
    this.typeId = typeId;
    this.locationId = locationId;
    this.unitId = unitId;
    this.typeUnit = typeUnit;
  }

  public CfgUnitTtSpmEntity toEntity() {
    CfgUnitTtSpmEntity model = new CfgUnitTtSpmEntity(
        StringUtils.validString(id) ? Long.valueOf(id)
            : null,
        StringUtils.validString(typeId) ? Long.valueOf(typeId)
            : null,
        StringUtils.validString(locationId) ? Long.valueOf(locationId)
            : null,
        StringUtils.validString(unitId) ? Long.valueOf(unitId)
            : null,
        StringUtils.validString(typeUnit) ? Long.valueOf(typeUnit)
            : null
    );
    return model;
  }
}
