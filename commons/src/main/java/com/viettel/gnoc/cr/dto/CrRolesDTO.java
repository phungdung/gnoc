package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.CrRolesEntity;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DungPV
 */
@Setter
@Getter
@NoArgsConstructor
@Unique(message = "{validation.CrRolesDTO.cmreCode.unique}", clazz = CrRolesEntity.class, uniqueField = "cmreCode", idField = "cmreId")
public class CrRolesDTO extends BaseDto {

  private Long cmreId;
  @NotEmpty(message = "{validation.CrRolesDTO.cmreCode.NotEmpty}")
  @Size(max = 100, message = "{validation.CrRolesDTO.cmreCode.tooLong}")
  private String cmreCode;
  @NotEmpty(message = "{validation.CrRolesDTO.cmreName.NotEmpty}")
  @Size(max = 200, message = "{validation.CrRolesDTO.cmreName.tooLong}")
  private String cmreName;
  @Size(max = 1000, message = "{validation.CrRolesDTO.description.tooLong}")
  private String description;
  @NotNull(message = "{validation.CrRolesDTO.status.NotNull}")
  private Long status;
  private Long cmroutId;
  private Long unitId;
  private String unitName;
  private String parentName;
  @NotNull(message = "{validation.CrRolesDTO.isScheduleCrEmergency.NotNull}")
  private Long isScheduleCrEmergency;
  private List<LanguageExchangeDTO> listRoleName;

  public CrRolesDTO(Long cmreId, String cmreCode, String cmreName, String description, Long status,
      Long isScheduleCrEmergency) {
    this.cmreId = cmreId;
    this.cmreCode = cmreCode;
    this.cmreName = cmreName;
    this.description = description;
    this.status = status;
    this.isScheduleCrEmergency = isScheduleCrEmergency;
  }

  public CrRolesEntity toEntity() {
    return new CrRolesEntity(cmreId, cmreCode, cmreName, description, status,
        isScheduleCrEmergency);
  }
}
