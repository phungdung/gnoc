package com.viettel.gnoc.maintenance.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
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

/**
 * @author trungduong
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MR_DEVICE_BTS")
public class MrDeviceBtsEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_DEVICE_BTS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "DEVICE_ID", unique = true, nullable = false)
  private Long deviceId;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "PROVINCE_CODE")
  private String provinceCode;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "USER_MANAGER")
  private String userManager;

  @Column(name = "UPDATE_USER")
  private String updateUser;

  @Column(name = "SERIAL")
  private String serial;

  @Column(name = "FUEL_TYPE")
  private String fuelType;

  @Column(name = "POWER")
  private String power;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "MAINTENANCE_TIME")
  private Date maintenanceTime;

  @Column(name = "OPERATION_HOUR")
  private Double operationHour;

  @Column(name = "AREA_CODE")
  private String areaCode;

  @Column(name = "OPERATION_HOUR_LAST_UPDATE")
  private Double operationHourLastUpdate;

  @Column(name = "PRODUCER")
  private String producer;

  @Column(name = "PUT_STATUS")
  private String putStatus;

  @Column(name = "IN_KTTS")
  private String inKTTS;

  @Column(name = "COUNTRY_NAME")
  private String countryName;

  @Column(name = "PROVINCE_NAME")
  private String provinceName;


  public MrDeviceBtsDTO toDTO() {
    MrDeviceBtsDTO dto = new MrDeviceBtsDTO(
        deviceId
        , marketCode
        , provinceCode
        , stationCode
        , deviceType
        , userManager
        , updateUser
        , serial
        , fuelType
        , power
        , maintenanceTime == null ? null : maintenanceTime.toString()
        , operationHour == null ? null : operationHour.toString()
        , areaCode
        , operationHourLastUpdate == null ? null : operationHourLastUpdate.toString()
        , producer
        , putStatus
        , inKTTS
        , countryName
        , provinceName
    );
    return dto;
  }
}
