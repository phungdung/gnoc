package com.viettel.gnoc.cr.dto;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.CrManagerScopeEntity;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Unique(message = "{validation.crManagerScopeDTO.cmseCode.unique}", clazz = CrManagerScopeEntity.class, uniqueField = "cmseCode", idField = "cmseId")
public class CrManagerScopeDTO extends BaseDto {

  private Long cmseId;
  @NotEmpty(message = "{validation.crManagerScopeDTO.cmseCode.NotEmpty}")
  @Size(max = 100, message = "{validation.cfgChildArray.cmseCode.maxLength}")
  private String cmseCode;

  @NotEmpty(message = "{validation.crManagerScopeDTO.cmseName.NotEmpty}")
  @Size(max = 200, message = "{validation.cfgChildArray.cmseName.maxLength}")
  private String cmseName;

  private Long isActive;
  @Size(max = 1000, message = "{validation.cfgChildArray.description.maxLength}")
  private String description;
  private List<LanguageExchangeDTO> listCmseName;

  public CrManagerScopeDTO(Long cmseId, String cmseCode, String cmseName, Long isActive,
      String description) {
    this.cmseId = cmseId;
    this.cmseCode = cmseCode;
    this.cmseName = cmseName;
    this.isActive = isActive;
    this.description = description;
  }

  public CrManagerScopeEntity toEntity() {
    return new CrManagerScopeEntity(cmseId, cmseCode, cmseName, isActive,
        description);
  }
}
