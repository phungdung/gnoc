package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.wo.model.MaterialThresEntity;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.materialThres.null.unique}", clazz = MaterialThresEntity.class,
    uniqueFields = "materialId,actionId,infraType,serviceId", idField = "materialThresId")
public class MaterialThresInsideDTO extends BaseDto {

  private Long materialThresId;
  private Long materialId;
  private Long actionId;
  private Long serviceId;
  private Long infraType;

  @NotNull(message = "{validation.materialThres.null.techThres}")
  @DecimalMax(value = "9999999", message = "{validation.materialThres.techThres.tooLong}")

  private Double techThres;

  @NotNull(message = "{validation.materialThres.null.warningThres}")
  @DecimalMax(value = "9999999", message = "{validation.materialThres.warningThres.tooLong}")

  private Double warningThres;

  @NotNull(message = "{validation.materialThres.null.freeThres}")
  @DecimalMax(value = "9999999", message = "{validation.materialThres.freeThres.tooLong}")

  private Double freeThres;

  @DecimalMax(value = "9999999", message = "{validation.materialThres.techDistanctThres.tooLong}")
  private Double techDistanctThres;

  @DecimalMax(value = "9999999", message = "{validation.materialThres.warningDistanctThres.tooLong}")
  private Double warningDistanctThres;

  @DecimalMax(value = "9999999", message = "{validation.materialThres.freeDistanctThres.tooLong}")
  private Double freeDistanctThres;
  private Long isEnable;

  //String
  private String techThresStr;
  private String warningThresStr;
  private String freeThresStr;
  private String techDistanctThresStr;
  private String warningDistanctThresStr;
  private String freeDistanctThresStr;
  //Fields WoMaterialDTO
  private String materialGroupCode;
  private String materialCode;
  private String materialName;
  private String materialGroupName;
  private Long unitId;
  private String unitName;

  private String actionCode;
  private String actionName;
  private String serviceCode;
  private String serviceName;
  private String technology;
  private String quantity;
  private String resultImport;
  private Long checkDouble;
  private String defaultSortField;
  private String userName;

  public MaterialThresInsideDTO() {
    setDefaultSortField("materialThresId");
  }

  //Constructor
  public MaterialThresInsideDTO(Long materialThresId, Long materialId, Long actionId, Long serviceId
      , Long infraType, Double techThres, Double warningThres, Double freeThres
      , Double techDistanctThres, Double warningDistanctThres, Double freeDistanctThres
      , Long isEnable) {
    this.materialThresId = materialThresId;
    this.materialId = materialId;
    this.actionId = actionId;
    this.serviceId = serviceId;
    this.infraType = infraType;
    this.techThres = techThres;
    this.warningThres = warningThres;
    this.freeThres = freeThres;
    this.techDistanctThres = techDistanctThres;
    this.warningDistanctThres = warningDistanctThres;
    this.freeDistanctThres = freeDistanctThres;
    this.isEnable = isEnable;
  }

  public MaterialThresDTO toOutSide() {
    MaterialThresDTO model = new MaterialThresDTO(
        StringUtils.validString(materialThresId) ? String.valueOf(materialThresId) : null,
        StringUtils.validString(materialThresId) ? String.valueOf(materialId) : null,
        materialCode,
        materialName,
        StringUtils.validString(unitId) ? String.valueOf(unitId) : null,
        unitName,
        StringUtils.validString(actionId) ? String.valueOf(actionId) : null,
        StringUtils.validString(serviceId) ? String.valueOf(serviceId) : null,
        StringUtils.validString(infraType) ? String.valueOf(infraType) : null,
        StringUtils.validString(techThres) ? String.valueOf(techThres.longValue()) : null,
        StringUtils.validString(warningThres) ? String.valueOf(warningThres.longValue())
            : null,
        StringUtils.validString(freeThres) ? String.valueOf(freeThres.longValue()) : null,
        StringUtils.validString(techDistanctThres) ? String.valueOf(techDistanctThres.longValue())
            : null,
        StringUtils.validString(warningDistanctThres) ? String
            .valueOf(warningDistanctThres.longValue()) : null,
        StringUtils.validString(freeDistanctThres) ? String.valueOf(freeDistanctThres.longValue())
            : null,
        StringUtils.validString(isEnable) ? String.valueOf(isEnable) : null,
        actionCode,
        actionName,
        serviceCode,
        serviceName,
        technology,
        quantity,
        userName,
        materialGroupName,
        materialGroupCode
    );
    return model;
  }

  public MaterialThresEntity toEntity() {
    MaterialThresEntity model = new MaterialThresEntity(
        materialThresId
        , materialId
        , actionId
        , serviceId
        , infraType
        , techThres
        , warningThres
        , freeThres
        , techDistanctThres
        , warningDistanctThres
        , freeDistanctThres
        , isEnable
    );
    return model;
  }
}
