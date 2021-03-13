package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrCfgProcedureBtsDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author tiennv
 * @version 1.0
 * @since 04/04/2018 05:16:02
 */

@Entity
@Table(schema = "OPEN_PM", name = "MR_CFG_PROCEDURE_BTS")
@Getter
@Setter
@NoArgsConstructor
public class MrCfgProcedureBtsEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CFG_PROCEDURE_BTS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CFG_PROCEDURE_BTS_ID", nullable = false)
  private Long cfgProcedureBtsId;

  @Column(name = "MARKET_CODE")
  private Long marketCode;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "CYCLE")
  private Long cycle;

  @Column(name = "GEN_MR_BEFORE")
  private Long genMrBefore;

  @Column(name = "MR_TIME")
  private Long mrTime;

  @Column(name = "MATERIAL_TYPE")
  private String materialType;

  @Column(name = "MAINTENANCE_HOUR")
  private Long maintenanceHour;

  @Column(name = "SUPPLIER_CODE")
  private String supplierCode;


  public MrCfgProcedureBtsEntity(Long cfgProcedureBtsId, Long marketCode, String deviceType,
      Long cycle, Long genMrBefore, Long mrTime, String materialType, Long maintenanceHour,
      String supplierCode) {
    this.cfgProcedureBtsId = cfgProcedureBtsId;
    this.marketCode = marketCode;
    this.deviceType = deviceType;
    this.cycle = cycle;
    this.genMrBefore = genMrBefore;
    this.mrTime = mrTime;
    this.materialType = materialType;
    this.maintenanceHour = maintenanceHour;
    this.supplierCode = supplierCode;
  }

  public MrCfgProcedureBtsDTO toDTO() {
    MrCfgProcedureBtsDTO dto = new MrCfgProcedureBtsDTO(
        cfgProcedureBtsId,
        marketCode,
        deviceType,
        cycle,
        genMrBefore,
        mrTime,
        materialType,
        maintenanceHour,
        supplierCode
    );
    return dto;
  }
}
