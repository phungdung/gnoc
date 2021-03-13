package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrScheduleCdHisDTO;
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

@Entity
@Table(schema = "OPEN_PM", name = "MR_SCHEDULE_CD_HIS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MrScheduleCdHisEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_SCHEDULE_CD_HIS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SCHEDULE_CD_HIS_ID", unique = true, nullable = false)
  private Long scheduleCdHisId;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "DEVICE_CD_ID")
  private Long deviceCdId;

  @Column(name = "DEVICE_NAME")
  private String deviceName;

  @Column(name = "MR_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date mrDate;

  @Column(name = "MR_CONTENT")
  private String mrContent;

  @Column(name = "MR_MODE")
  private String mrMode;

  @Column(name = "MR_TYPE")
  private String mrType;

  @Column(name = "MR_ID")
  private Long mrId;

  @Column(name = "MR_CODE")
  private String mrCode;

  @Column(name = "IMPORTANT_LEVEL")
  private String importantLevel;

  @Column(name = "PROCEDURE_ID")
  private String procedureId;

  @Column(name = "PROCEDURE_NAME")
  private String procedureName;

  @Column(name = "CYCLE")
  private Long cycle;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  public MrScheduleCdHisEntity(
      Long scheduleCdHisId,
      String deviceType,
      Long deviceCdId,
      String deviceName,
      Date mrDate,
      String mrContent,
      String mrMode,
      String mrType,
      Long mrId,
      String mrCode,
      String importantLevel,
      String procedureId,
      String procedureName,
      String marketCode,
      Long cycle
  ) {
    this.scheduleCdHisId = scheduleCdHisId;
    this.deviceType = deviceType;
    this.deviceCdId = deviceCdId;
    this.deviceName = deviceName;
    this.mrDate = mrDate;
    this.mrContent = mrContent;
    this.mrMode = mrMode;
    this.mrType = mrType;
    this.mrId = mrId;
    this.mrCode = mrCode;
    this.importantLevel = importantLevel;
    this.procedureId = procedureId;
    this.procedureName = procedureName;
    this.marketCode = marketCode;
    this.cycle = cycle;
  }

  public MrScheduleCdHisDTO toDTO() {
    MrScheduleCdHisDTO dto = new MrScheduleCdHisDTO(
        scheduleCdHisId == null ? null : scheduleCdHisId.toString(),
        deviceType,
        deviceCdId == null ? null : deviceCdId.toString(),
        deviceName,
        mrDate == null ? null : mrDate.toString(),
        mrContent,
        mrMode,
        mrType,
        mrId == null ? null : mrId.toString(),
        mrCode,
        importantLevel,
        procedureId,
        procedureName,
        marketCode,
        cycle == null ? null : cycle.toString()
    );
    return dto;
  }

}
