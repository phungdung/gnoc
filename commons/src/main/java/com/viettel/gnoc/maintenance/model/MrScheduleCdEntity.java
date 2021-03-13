/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrScheduleCdDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author tiennv
 */

@Entity
@Table(schema = "OPEN_PM", name = "MR_SCHEDULE_CD")
@Getter
@Setter
@NoArgsConstructor
public class MrScheduleCdEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_SCHEDULE_CD_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SCHEDULE_ID")
  private Long scheduleId;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "DEVICE_CD_ID")
  private Long deviceCdId;

  @Column(name = "DEVICE_NAME")
  private String deviceName;

  @Column(name = "PROCEDURE_ID")
  private Long procedureId;

  @Column(name = "LAST_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date lastDate;

  @Column(name = "NEXT_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date nextDate;

  @Column(name = "NEXT_DATE_MODIFY")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date nextDateModify;

  @Column(name = "STATION")
  private String station;

  @Column(name = "UPDATED_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date updatedDate;

  @Column(name = "MR_ID")
  private Long mrId;

  @Column(name = "MODIFY_USER")
  private String modifyUser;

  @Column(name = "CYCLE")
  private Long cycle;

  @Column(name = "MODIFY_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date modifyDate;

  public MrScheduleCdEntity(Long scheduleId) {
    this.scheduleId = scheduleId;
  }

  public MrScheduleCdEntity(
      Long scheduleId,
      String marketCode,
      String deviceType,
      Long deviceCdId,
      String deviceName,
      Long procedureId,
      Date lastDate,
      Date nextDate,
      Date nextDateModify,
      String station,
      Date updatedDate,
      Long mrId,
      String modifyUser,
      Date modifyDate,
      Long cycle
  ) {
    this.scheduleId = scheduleId;
    this.marketCode = marketCode;
    this.deviceType = deviceType;
    this.deviceCdId = deviceCdId;
    this.deviceName = deviceName;
    this.procedureId = procedureId;
    this.lastDate = lastDate;
    this.nextDate = nextDate;
    this.nextDateModify = nextDateModify;
    this.station = station;
    this.updatedDate = updatedDate;
    this.mrId = mrId;
    this.modifyUser = modifyUser;
    this.modifyDate = modifyDate;
    this.cycle = cycle;
  }


  public MrScheduleCdDTO toDTO() {
    MrScheduleCdDTO dto = new MrScheduleCdDTO(
        marketCode,
        scheduleId,
        deviceType,
        deviceCdId,
        deviceName,
        procedureId,
        lastDate,
        nextDate,
        nextDateModify,
        station,
        updatedDate,
        mrId,
        modifyUser,
        modifyDate,
        cycle == null ? null : cycle.toString()
    );
    return dto;
  }
}
