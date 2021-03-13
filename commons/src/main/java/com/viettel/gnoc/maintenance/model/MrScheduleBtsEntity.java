package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrScheduleBtsDTO;
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
 * @version 2.0
 * @since 07/01/2020 04:59:00
 */
@Entity
@Table(schema = "OPEN_PM", name = "MR_SCHEDULE_BTS")
@Getter
@Setter
@NoArgsConstructor
public class MrScheduleBtsEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_SCHEDULE_BTS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SCHEDULE_ID", unique = true, nullable = false)
  private Long scheduleId;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "AREA_CODE")
  private String areaCode;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "SERIAL")
  private String serial;

  @Column(name = "CYCLE")
  private String cycle;

  @Column(name = "MR_CODE")
  private String mrCode;

  @Column(name = "WO_CODE")
  private String woCode;

  @Column(name = "MODIFY_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date modifyDate;

  @Column(name = "NEXT_DATE_MODIFY")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date nextDateModify;

  @Column(name = "USER_MANAGER")
  private String userManager;

  @Column(name = "PROVINCE_CODE")
  private String provinceCode;

  public MrScheduleBtsEntity(Long scheduleId) {
    this.scheduleId = scheduleId;
  }

  public MrScheduleBtsEntity(Long scheduleId, String marketCode, String areaCode,
      String stationCode, String deviceType, String serial, String cycle, String mrCode,
      String woCode, Date modifyDate, Date nextDateModify, String userManager,
      String provinceCode) {
    this.scheduleId = scheduleId;
    this.marketCode = marketCode;
    this.areaCode = areaCode;
    this.stationCode = stationCode;
    this.deviceType = deviceType;
    this.serial = serial;
    this.cycle = cycle;
    this.mrCode = mrCode;
    this.woCode = woCode;
    this.modifyDate = modifyDate;
    this.nextDateModify = nextDateModify;
    this.userManager = userManager;
    this.provinceCode = provinceCode;
  }


  public MrScheduleBtsDTO toDTO() {
    MrScheduleBtsDTO dto = new MrScheduleBtsDTO(
        scheduleId == null ? null : scheduleId.toString(),
        marketCode,
        areaCode,
        stationCode,
        deviceType,
        serial,
        cycle,
        mrCode,
        woCode,
        modifyDate == null ? null : modifyDate.toString(),
        nextDateModify == null ? null : nextDateModify.toString(),
        userManager,
        provinceCode
    );
    return dto;
  }
}
