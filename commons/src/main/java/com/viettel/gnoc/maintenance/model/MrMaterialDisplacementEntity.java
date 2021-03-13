package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrMaterialDisplacementDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_MATERIAL_DISPLACEMENT")
public class MrMaterialDisplacementEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_MATERIAL_DISPLACEMENT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MATERIAL_ID", unique = true, nullable = false)
  private Long materialId;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "MATERIAL_NAME")
  private String materialName;

  @Column(name = "FUEL_TYPE")
  private String fuelType;

  @Column(name = "POWER")
  private String power;

  @Column(name = "SERIAL")
  private String serial;

  @Column(name = "UNIT")
  private String unit;

  @Column(name = "UNIT_PRICE")
  private String unitPrice;

  @Column(name = "QUOCTA")
  private String quocta;

  @Column(name = "QUANTITY")
  private Long quantity;

  @Column(name = "WO_ID")
  private String woId;

  @Column(name = "DEVICE_CODE")
  private String deviceCode;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Column(name = "PROVINCE_CODE")
  private String provinceCode;

  @Column(name = "MATERIAL_CODE")
  private String materialCode;

  @Column(name = "GENERIC_CODE")
  private String genericCode;

  @Column(name = "DATE_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date dateTime;


  public MrMaterialDisplacementDTO toDTO() {
    MrMaterialDisplacementDTO dto = new MrMaterialDisplacementDTO(
        materialId,
        deviceType,
        materialName,
        fuelType,
        power,
        serial,
        unit,
        unitPrice,
        quocta,
        quantity,
        woId,
        deviceCode,
        stationCode,
        provinceCode,
        materialCode,
        genericCode,
        dateTime
    );
    return dto;
  }
}
