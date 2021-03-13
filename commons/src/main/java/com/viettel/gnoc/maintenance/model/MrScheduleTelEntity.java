package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MR_SCHEDULE_TEL")
public class MrScheduleTelEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_SCHEDULE_TEL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SCHEDULE_ID", unique = true, nullable = false)
  private Long scheduleId;
  @Column(name = "MARKET_CODE")
  private String marketCode;
  @Column(name = "ARRAY_CODE")
  private String arrayCode;
  @Column(name = "DEVICE_TYPE")
  private String deviceType;
  @Column(name = "DEVICE_ID")
  private Long deviceId;
  @Column(name = "DEVICE_CODE")
  private String deviceCode;
  @Column(name = "DEVICE_NAME")
  private String deviceName;
  @Column(name = "PROCEDURE_ID")
  private Long procedureId;
  @Column(name = "LAST_DATE")
  private Date lastDate;
  @Column(name = "NEXT_DATE")
  private Date nextDate;
  @Column(name = "NEXT_DATE_MODIFY")
  private Date nextDateModify;
  @Column(name = "MODIFY_USER")
  private String modifyUser;
  @Column(name = "MODIFY_DATE")
  private Date modifyDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "MR_ID")
  private Long mrId;
  @Column(name = "MR_SOFT")
  private String mrSoft;
  @Column(name = "MR_HARD")
  private String mrHard;
  @Column(name = "MR_12M")
  private String mr12m;
  @Column(name = "STATION")
  private String station;
  @Column(name = "REGION")
  private String region;
  @Column(name = "MR_HARD_CYCLE")
  private Long mrHardCycle;
  @Column(name = "MR_CONFIRM")
  private String mrConfirm;
  @Column(name = "MR_COMMENT")
  private String mrComment;
  @Column(name = "NETWORK_TYPE")
  private String networkType;
  @Column(name = "CR_ID")
  private Long crId;
  @Column(name = "WO_ID")
  private Long woId;
  @Column(name = "GROUP_CODE")
  private String groupCode;

  public MrScheduleTelDTO toDTO() {
    return new MrScheduleTelDTO(scheduleId, marketCode, arrayCode, deviceType, deviceId, deviceCode,
        deviceName, procedureId, lastDate, nextDate, nextDateModify, modifyUser, modifyDate,
        updatedDate, mrId, mrSoft, mrHard, mr12m, station, region, mrHardCycle, mrConfirm,
        mrComment, networkType, crId, woId, groupCode);
  }
}
