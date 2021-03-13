package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.incident.model.CfgMapNetLevelIncTypeEntity;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.CfgMapNetLevelIncTypeDTO.null.unique}", clazz = CfgMapNetLevelIncTypeEntity.class, uniqueFields = "troubleTypeId", idField = "id")
public class CfgMapNetLevelIncTypeDTO extends BaseDto {

  private Long id;

  @NotNull(message = "validation.CfgMapNetLevelIncTypeDTO.troubleTypeId.NotNull")
  private Long troubleTypeId;
  @NotNull(message = "validation.CfgMapNetLevelIncTypeDTO.networkLevelId.NotNull")
  private String networkLevelId;
  private String networkLevelName;
  private String troubleTypeName;
  private String defaultSortField;

  public CfgMapNetLevelIncTypeDTO(Long id, Long troubleTypeId, String networkLevelId,
      String networkLevelName, String troubleTypeName) {
    this.id = id;
    this.troubleTypeId = troubleTypeId;
    this.networkLevelId = networkLevelId;
    this.networkLevelName = networkLevelName;
    this.troubleTypeName = troubleTypeName;
  }

  public CfgMapNetLevelIncTypeDTO(Long id, Long troubleTypeId, String networkLevelId) {
    this.id = id;
    this.troubleTypeId = troubleTypeId;
    this.networkLevelId = networkLevelId;
  }


  public CfgMapNetLevelIncTypeEntity toEntity() {
    CfgMapNetLevelIncTypeEntity model = new CfgMapNetLevelIncTypeEntity(
        id,
        troubleTypeId,
        networkLevelId, troubleTypeName, networkLevelName);
    return model;
  }
}
