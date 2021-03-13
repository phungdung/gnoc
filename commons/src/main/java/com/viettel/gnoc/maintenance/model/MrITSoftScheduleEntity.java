package com.viettel.gnoc.maintenance.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
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

/**
 * @author kienpv
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_SCHEDULE_IT")
public class MrITSoftScheduleEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_SCHEDULE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SCHEDULE_ID", unique = true, nullable = false)
  private Long scheduleId;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "ARRAY_CODE")
  private String arrayCode;

  @Column(name = "REGION")
  private String region;

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
  @Column(name = "UPTIME_DATE")
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
  @Column(name = "UPDATED_DATE")
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

  @Column(name = "NETWORK_TYPE")
  private String networkType;

  @Column(name = "IP_NODE")
  private String ipNode;

  @Column(name = "NOTE")
  private String note;

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

  @Column(name = "LOG_MOP")
  private String logMop;

  public MrITSoftScheduleDTO toDTO() {
    MrITSoftScheduleDTO dto = new MrITSoftScheduleDTO(
        scheduleId == null ? null : scheduleId.toString(),
        marketCode,
        arrayCode,
        deviceType,
        deviceId,
        deviceCode,
        deviceName,
        procedureId == null ? null : procedureId.toString(),
        lastDate == null ? null : DateTimeUtils.convertDateToString(lastDate),
        uptimeDate == null ? null : DateTimeUtils.convertDateToString(uptimeDate),
        nextDate == null ? null : DateTimeUtils.convertDateToString(nextDate),
        nextDateModify == null ? null : DateTimeUtils.convertDateToString(nextDateModify),
        modifyUser,
        modifyDate == null ? null : DateTimeUtils.convertDateToString(modifyDate),
        updatedDate == null ? null : DateTimeUtils.convertDateToString(updatedDate),
        levelImportant,
        mrId == null ? null : mrId.toString(),
        providerId == null ? null : providerId.toString(),
        groupCode,
        ipServer,
        vendor, region,
        networkType, ipNode,
        note,
        cdId == null ? null : cdId.toString(),
        woId == null ? null : woId.toString(),
        userbd,
        reasonBd,
        crId == null ? null : crId.toString(),
        logMop
    );
    return dto;
  }

}
