package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrMaterialDTO;
import java.util.Date;
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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_MATERIAL")
public class MrMaterialEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_MATERIAL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MATERIAL_ID", nullable = false)
  private Long materialId;
  @Column(name = "MATERIAL_NAME")
  private String materialName;
  @Column(name = "SERIAL")
  private String serial;
  @Column(name = "UNIT_PRICE")
  private Long unitPrice;
  @Column(name = "DATE_TIME")
  private Date dateTime;
  @Column(name = "DEVICE_TYPE")
  private String deviceType;
  @Column(name = "MARKET_CODE")
  private Long marketCode;

  public MrMaterialDTO toDTO() {
    return new MrMaterialDTO(materialId, materialName, serial, unitPrice, dateTime,
        deviceType, marketCode);
  }
}
