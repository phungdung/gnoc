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
@AllArgsConstructor
@NoArgsConstructor
public class MaterialThresDTO extends BaseDto {

  //Fields
  private String materialThresId;
  private String materialId;
  private String materialCode;
  private String materialName;
  private String unitId;
  private String unitName;
  private String actionId;
  private String serviceId;
  private String infraType;
  private String techThres;
  private String warningThres;
  private String freeThres;
  private String techDistanctThres;
  private String warningDistanctThres;
  private String freeDistanctThres;
  private String isEnable;
  private String actionCode;
  private String actionName;
  private String serviceCode;
  private String serviceName;
  private String technology;
  private String quantity;
  //ThanhLV12_them moi nhan vien _start
  private String userName;
  private String materialGroupName;
  private String materialGroupCode;
  //ThanhLV12_them moi nhan vien _end

  public MaterialThresInsideDTO toInSide() {
    try {
      MaterialThresInsideDTO dto = new MaterialThresInsideDTO(
          this.materialThresId == null ? null : Long.valueOf(this.materialThresId),
          this.materialId == null ? null : Long.valueOf(this.materialId),
          this.actionId == null ? null : Long.valueOf(this.actionId),
          this.serviceId == null ? null : Long.valueOf(this.serviceId),
          this.infraType == null ? null : Long.valueOf(this.infraType),
          this.techThres == null ? null : Double.valueOf(this.techThres),
          this.warningThres == null ? null : Double.valueOf(this.warningThres),
          this.freeThres == null ? null : Double.valueOf(this.freeThres),
          this.techDistanctThres == null ? null : Double.valueOf(this.techDistanctThres),
          this.warningDistanctThres == null ? null : Double.valueOf(this.warningDistanctThres),
          this.freeDistanctThres == null ? null : Double.valueOf(this.freeDistanctThres),
          this.isEnable == null ? null : Long.valueOf(this.isEnable));
      return dto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
