package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDTO;
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
@Table(schema = "OPEN_PM", name = "MR_SCHEDULE_BTS_HIS")
public class MrScheduleBtsHisEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_SCHEDULE_HIS_BTS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MR_DEVICE_HIS_ID", unique = true, nullable = false)
  private Long mrDeviceHisId;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "AREA_CODE")
  private String areaCode;

  @Column(name = "PROVINCE_CODE")
  private String provinceCode;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "DEVICE_ID")
  private String deviceId;

  @Column(name = "DEVICE_CODE")
  private String deviceCode;

  @Column(name = "SERIAL")
  private String serial;

  @Column(name = "CYCLE")
  private String cycle;

  @Column(name = "COMPLETE_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date completeDate;

  @Column(name = "WO_CODE")
  private String woCode;

  @Column(name = "USER_MANAGER")
  private String userManager;

  @Column(name = "MR_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date mrDate;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Column(name = "IS_COMPLETE")
  private Long isComplete;

  @Column(name = "STATUS")
  private String status;

  public MrScheduleBtsHisDTO toDTO() {
    MrScheduleBtsHisDTO dto = new MrScheduleBtsHisDTO(
        mrDeviceHisId,
        marketCode,
        areaCode,
        provinceCode,
        deviceType,
        deviceId,
        deviceCode,
        serial,
        cycle,
        completeDate,
        woCode,
        userManager,
        mrDate,
        stationCode,
        isComplete,
        status
    );
    return dto;
  }
}
