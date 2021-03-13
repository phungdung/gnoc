package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.MaterialCodienDTO;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "MATERIAL_CODIEN")
public class MaterialCodienEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MATERIAL_CODIEN_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "WO_ID")
  private Long woId;

  @Column(name = "MATERIAL_ID")
  private Long materialId;

  @Column(name = "MATERIAL_NAME")
  private String materialName;

  @Column(name = "QUANTITY")
  private String quantity;

  @Column(name = "SERIAL")
  private String serial;

  @Column(name = "TOTAL_COST")
  private String totalCost;

  @Column(name = "UNIT")
  private String unit;

  @Column(name = "UNIT_PRICE")
  private String unitPrice;

  @Column(name = "GENERIC_CODE")
  private String genericCode;

  @Column(name = "DEVICE_CODE")
  private String deviceCode;

  @Column(name = "PROVINCE_CODE")
  private String provinceCode;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Column(name = "MATERIAL_CODE")
  private String materialCode;

  @Column(name = "DATE_TIME")
  private String dateTime;

  public MaterialCodienDTO toDTO() {
    return new MaterialCodienDTO(id, woId, materialId, materialName, quantity, serial, totalCost,
        unit, unitPrice, genericCode, deviceCode, provinceCode, stationCode, materialCode,
        dateTime);
  }

}
