
package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "WFM", name = "MATERIAL_THRES")
public class MaterialThresEntity {

  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MATERIAL_THRES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MATERIAL_THRES_ID", nullable = false)
  private Long materialThresId;

  @Column(name = "MATERIAL_ID")
  private Long materialId;

  @Column(name = "ACTION_ID")
  private Long actionId;

  @Column(name = "SERVICE_ID")
  private Long serviceId;

  @Column(name = "INFRA_TYPE")
  private Long infraType;

  @Column(name = "TECH_THRES")
  private Double techThres;

  @Column(name = "WARNING_THRES")
  private Double warningThres;

  @Column(name = "FREE_THRES")
  private Double freeThres;

  @Column(name = "TECH_DISTANCT_THRES")
  private Double techDistanctThres;

  @Column(name = "WARNING_DISTANCT_THRES")
  private Double warningDistanctThres;

  @Column(name = "FREE_DISTANCT_THRES")
  private Double freeDistanctThres;

  @Column(name = "IS_ENABLE")
  private Long isEnable;

  //Constructor
  public MaterialThresEntity(Long materialId, Long actionId, Long serviceId, Long infraType,
      Double techThres, Double warningThres, Double freeThres, Double techDistanctThres,
      Double warningDistanctThres, Double freeDistanctThres, Long isEnable) {
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

  public MaterialThresInsideDTO toDTO() {
    MaterialThresInsideDTO dto = new MaterialThresInsideDTO(
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
    return dto;
  }
}

