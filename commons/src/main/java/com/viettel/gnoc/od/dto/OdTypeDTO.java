package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.od.model.OdTypeEntity;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Unique(message = "{validation.odTypeDTO.odTypeCode.unique}", clazz = OdTypeEntity.class, uniqueField = "odTypeCode", idField = "odTypeId")
public class OdTypeDTO extends BaseDto {

  private Long odTypeId;
  private String processType;
  @NotEmpty(message = "{validation.odTypeDTO.odTypeCode.NotEmpty}")
  private String odTypeCode;
  @NotEmpty(message = "{validation.odTypeDTO.odTypeName.NotEmpty}")
  private String odTypeName;
  @NotNull(message = "{validation.odTypeDTO.status.NotNull}")
  private Long status;
  private Long odGroupTypeId;

  private String statusName;
  private String odGroupTypeName;
  private Long priorityId;
  private Double processTime;
  private String checkValidate;
  private List<OdTypeDetailDTO> odTypeDetailDTOS;
  private List<OdTypeMapLocationDTO> odTypeMapLocationDTOS;

  private String key;
  private String message;

  public OdTypeDTO(String key, String message) {
    this.key = key;
    this.message = message;
  }

  public OdTypeEntity toEntity() {
    return new OdTypeEntity(odTypeId, odTypeCode, odTypeName, status, odGroupTypeId);
  }

  public OdTypeDTO(Long odTypeId, String odTypeCode, String odTypeName, Long status,
      Long odGroupTypeId) {
    this.odTypeId = odTypeId;
    this.odTypeCode = odTypeCode;
    this.odTypeName = odTypeName;
    this.status = status;
    this.odGroupTypeId = odGroupTypeId;
  }
}
