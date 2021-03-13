/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
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
 * @author truongtx
 */
@Entity
@Table(schema = "OPEN_PM", name = "MR_DEVICE_CD")
@Getter
@Setter
@NoArgsConstructor
public class MrDeviceCDEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_DEVICE_CD_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "DEVICE_CD_ID", unique = true, nullable = false)
  private Long deviceCdId;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "DEVICE_NAME")
  private String deviceName;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Column(name = "USER_MR_HARD")
  private String userMrHard;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "UPDATE_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date updateDate;

  @Column(name = "UPDATE_USER")
  private String updateUser;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "IS_COMPLETE_1M", length = 22)
  private Long isComplete1m;

  @Column(name = "IS_COMPLETE_3M", length = 22)
  private Long isComplete3m;

  @Column(name = "IS_COMPLETE_6M", length = 22)
  private Long isComplete6m;

  @Column(name = "IS_COMPLETE_12M", length = 22)
  private Long isComplete12m;

  @Column(name = "LAST_DATE_1M")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date lastDate1M;

  @Column(name = "LAST_DATE_3M")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date lastDate3M;

  @Column(name = "LAST_DATE_6M")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date lastDate6M;

  @Column(name = "LAST_DATE_12M")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date lastDate12M;

  public MrDeviceCDEntity(Long deviceCdId, String deviceType, String deviceName, String stationCode,
      String userMrHard, Long status, Date updateDate, String updateUser, String marketCode,
      Date lastDate1M, Date lastDate3M, Date lastDate6M, Date lastDate12M
  ) {
    this.deviceCdId = deviceCdId;
    this.deviceType = deviceType;
    this.deviceName = deviceName;
    this.stationCode = stationCode;
    this.userMrHard = userMrHard;
    this.status = status;
    this.updateDate = updateDate;
    this.updateUser = updateUser;
    this.marketCode = marketCode;
    this.lastDate1M = lastDate1M;
    this.lastDate3M = lastDate3M;
    this.lastDate6M = lastDate6M;
    this.lastDate12M = lastDate12M;
  }

  public MrDeviceCDDTO toDTO() {
    MrDeviceCDDTO deviceCDDTO = new MrDeviceCDDTO(
        deviceCdId == null ? null : deviceCdId.toString(),
        deviceType == null ? null : deviceType,
        deviceName == null ? null : deviceName,
        stationCode == null ? null : stationCode,
        userMrHard == null ? null : userMrHard,
        status == null ? null : status.toString(),
        updateDate == null ? null : updateDate.toString(),
        updateUser == null ? null : updateUser,
        marketCode,
        this.lastDate1M,
        this.lastDate3M,
        this.lastDate6M,
        this.lastDate12M

    );
    return deviceCDDTO;
  }
}
