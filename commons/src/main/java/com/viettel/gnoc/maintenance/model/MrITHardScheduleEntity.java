package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_SCHEDULE_IT_HARD")
public class MrITHardScheduleEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_SCHEDULE_IT_HARD", allocationSize = 1)
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
  private String deviceId;

  @Column(name = "DEVICE_CODE")
  private String deviceCode;

  @Column(name = "DEVICE_NAME")
  private String deviceName;

  @Column(name = "PROCEDURE_ID")
  private Long procedureId;

  @Temporal(TemporalType.DATE)
  @Column(name = "LAST_DATE")
  private Date lastDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "UPDATE_TIME")
  private Date uptimeDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "NEXT_DATE")
  private Date nextDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "NEXT_DATE_MODIFY")
  private Date nextDateModify;

  @Column(name = "MODIFY_USER")
  private String modifyUser;

  @Temporal(TemporalType.DATE)
  @Column(name = "MODIFY_DATE")
  private Date modifyDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "UPDATE_DATE")
  private Date updatedDate;

  @Column(name = "LEVEL_IMPORTANT")
  private String levelImportant;

  @Column(name = "MR_ID")
  private Long mrId;

  @Column(name = "PROVIDER_ID")
  private Long providerId;

  @Column(name = "GROUP_CODE")
  private String groupCode;

  @Column(name = "IP_SERVER")
  private String ipServer;

  @Column(name = "VENDOR")
  private String vendor;

  @Column(name = "REGION")
  private String region;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Column(name = "BD_TYPE")
  private String bdType;

  @Column(name = "NETWORK_TYPE")
  private String networkType;

  @Column(name = "NOTE")
  private String note;

  @Column(name = "IP_NODE")
  private String ipNode;

  @Column(name = "CD_ID")
  private Long cdId;

  @Column(name = "WO_ID")
  private Long woId;

  @Column(name = "USER_BD")
  private String userbd;

  @Column(name = "REASON_BD")
  private String reasonBd;

  @Column(name = "CR_ID")
  private Long crId;

  public MrITHardScheduleDTO toDTO() {
    return new MrITHardScheduleDTO(scheduleId, marketCode, arrayCode, deviceType, deviceId,
        deviceCode, deviceName, procedureId, lastDate, uptimeDate, nextDate, nextDateModify,
        modifyUser, modifyDate, updatedDate, levelImportant, mrId, providerId, groupCode, ipServer,
        vendor, region, stationCode, bdType, networkType, note, ipNode, cdId, woId, userbd,
        reasonBd, crId);
  }

}
