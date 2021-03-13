package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.wo.model.CfgWoHelpVsmartEntity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
@MultiFieldUnique(message = "{validation.cfgWoHelpVsmart.isDupplicate.CR}", clazz = CfgWoHelpVsmartEntity.class, uniqueFields = "systemId,typeId", idField = "id")
public class CfgWoHelpVsmartDTO extends BaseDto {

  private Long id;
  @NotNull(message = "{validation.cfgWoHelpVsmartDTO.systemId.NotNull}")
  private Long systemId;
  private String typeId;
  @NotEmpty(message = "{validation.cfgWoHelpVsmartDTO.fileId.NotEmpty}")
  private String fileId;
  private String typeName;
  private String systemName;
  private String fileName;
  private Long syncStatus;

  public CfgWoHelpVsmartDTO(Long id, Long systemId, String typeId, String fileId, String typeName) {
    this.id = id;
    this.systemId = systemId;
    this.typeId = typeId;
    this.fileId = fileId;
    this.typeName = typeName;
  }

  public CfgWoHelpVsmartEntity toEntity() {
    return new CfgWoHelpVsmartEntity(id, systemId, typeId, fileId, typeName, null);

  }
}
