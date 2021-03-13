package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialThresExportDTO extends BaseDto {

  //Fields
  private Long materialThresId;
  private Long materialId;
  private Long actionId;
  private Long serviceId;
  private Long infraType;
  private Double techThres;
  private Double warningThres;
  private Double freeThres;
  private Double techDistanctThres;
  private Double warningDistanctThres;
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

  //Constructor
  public MaterialThresExportDTO(Long materialThresId, Long materialId, Long actionId, Long serviceId
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

  public MaterialThresExportDTO(Long materialThresId, Long materialId, String materialCode
      , String materialName, Long actionId, Long serviceId, Long infraType, Double techThres
      , Double warningThres, Double freeThres, Double techDistanctThres, Double warningDistanctThres
      , Double freeDistanctThres, Long isEnable, String actionCode, String actionName
      , String serviceCode, String serviceName) {
    this.materialThresId = materialThresId;
    this.materialId = materialId;
    this.materialCode = materialCode;
    this.materialName = materialName;
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
    this.actionCode = actionCode;
    this.actionName = actionName;
    this.serviceCode = serviceCode;
    this.serviceName = serviceName;
  }

}
