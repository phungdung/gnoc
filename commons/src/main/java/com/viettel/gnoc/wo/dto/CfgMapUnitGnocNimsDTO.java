/**
 * @(#)WoActionForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.wo.model.CfgMapUnitGnocNimsEntity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.cfgMapUnitGnocNims.null.unique}", clazz = CfgMapUnitGnocNimsEntity.class, uniqueFields = "unitNimsCode,unitGnocCode,businessCode", idField = "id")
public class CfgMapUnitGnocNimsDTO extends BaseDto {

  private Long id;

  @NotEmpty(message = "{validation.cfgMapUnitGnocNims.null.unitNimsCode}")
  @Size(max = 500, message = "{validation.cfgMapUnitGnocNims.unitNimsCode.tooLong}")
  private String unitNimsCode;

  @NotEmpty(message = "{validation.cfgMapUnitGnocNims.null.unitGnocCode}")
  @Size(max = 500, message = "{validation.cfgMapUnitGnocNims.unitGnocCode.tooLong}")
  private String unitGnocCode;

  private Long businessCode;
  private String businessName;
  private String resultImport;
  private String validate;

  public CfgMapUnitGnocNimsDTO(Long id, String unitNimsCode, String unitGnocCode,
      Long businessCode, String businessName) {
    this.id = id;
    this.unitNimsCode = unitNimsCode;
    this.unitGnocCode = unitGnocCode;
    this.businessCode = businessCode;
    this.businessName = businessName;
  }

  public CfgMapUnitGnocNimsEntity toEntity() {
    return new CfgMapUnitGnocNimsEntity(id, unitNimsCode, unitGnocCode, businessCode, businessName);
  }

}
