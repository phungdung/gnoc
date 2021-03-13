package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.cr.model.CrManagerScopesOfRolesEntity;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author DungPV
 */
@Getter
@Setter
@MultiFieldUnique(message = "{validation.CrManagerScopesOfRolesDTO.unique}", clazz = CrManagerScopesOfRolesEntity.class, uniqueFields = "cmseId,cmreId", idField = "cmsorsId")
public class CrManagerScopesOfRolesDTO extends BaseDto {

  private Long cmsorsId;
  private String cmsorsName;
  @NotNull(message = "{validation.CrManagerScopesOfRolesDTO.cmseId.NotNull}")
  private Long cmseId;
  private String cmseCode;
  private String cmseName;
  @NotNull(message = "{validation.CrManagerScopesOfRolesDTO.cmreId.NotNull}")
  private Long cmreId;
  private String cmreCode;
  private String cmreName;
  private String changedTime;

  public CrManagerScopesOfRolesDTO(Long cmsorsId) {
    this.cmsorsId = cmsorsId;
  }

  public CrManagerScopesOfRolesDTO() {
  }

  public CrManagerScopesOfRolesDTO(Long cmsorsId, Long cmseId, Long cmreId) {
    this.cmsorsId = cmsorsId;
    this.cmseId = cmseId;
    this.cmreId = cmreId;
  }

  public CrManagerScopesOfRolesEntity toModel() {
    return new CrManagerScopesOfRolesEntity(cmsorsId, cmseId, cmreId);
  }
}
