package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.cr.model.CrRoleDeptEntity;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.crRoleDept.isDupplicate.CR}", clazz = CrRoleDeptEntity.class, uniqueFields = "cmreId,unitId", idField = "cmroutId")
public class CrRoleDeptDTO extends BaseDto {

  private Long cmroutId;
  @NotNull(message = "{validation.crRoleDeptDTO.cmreId.NotNull}")
  private Long cmreId;
  @NotNull(message = "{validation.crRoleDeptDTO.unitId.NotNull}")
  private Long unitId;

  public CrRoleDeptEntity toEntity() {
    CrRoleDeptEntity model = new CrRoleDeptEntity(cmroutId, cmreId, unitId);
    return model;
  }
}
