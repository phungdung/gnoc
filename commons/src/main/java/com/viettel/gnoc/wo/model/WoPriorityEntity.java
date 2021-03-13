
package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoPriorityDTO;
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

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "WFM", name = "WO_PRIORITY")
public class WoPriorityEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_PRIORITY_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "PRIORITY_ID", nullable = false)
  private Long priorityId;

  @Column(name = "WO_TYPE_ID", nullable = false, columnDefinition = "WoType")
  private Long woTypeId;

  @Column(name = "CD_RECEIVED_ALARM")
  private String cdReceivedAlarm;

  @Column(name = "CD_RECEIVED_ALARM_TYPE")
  private Long cdReceivedAlarmType;

  @Column(name = "CD_RECEIVED_THRESHOLD")
  private String cdReceivedThreshold;

  @Column(name = "CD_RECEIVED_THRESHOLD_TYPE")
  private Long cdReceivedThresholdType;

  @Column(name = "FT_RECEIVED_ALARM")
  private String ftReceivedAlarm;

  @Column(name = "FT_RECEIVED_ALARM_TYPE")
  private Long ftReceivedAlarmType;

  @Column(name = "FT_RECEIVED_THRESHOLD")
  private String ftReceivedThreshold;

  @Column(name = "FT_RECEIVED_THRESHOLD_TYPE")
  private Long ftReceivedThresholdType;

  @Column(name = "FT_PROCESS_ALARM")
  private String ftProcessAlarm;

  @Column(name = "FT_PROCESS_ALARM_TYPE")
  private Long ftProcessAlarmType;

  @Column(name = "FT_PROCESS_THRESHOLD")
  private String ftProcessThreshold;

  @Column(name = "FT_PROCESS_THRESHOLD_TYPE")
  private Long ftProcessThresholdType;

  @Column(name = "PRIORITY_NAME")
  private String priorityName;

  @Column(name = "PRIORITY_CODE")
  private String priorityCode;

  @Column(name = "IS_ENABLE")
  private Long isEnable;

  @Column(name = "CD_RECEIVE")
  private Long cdReceive;

  @Column(name = "CD_FORWAD_TO_FT")
  private Long cdForwardtToFt;

  @Column(name = "FT_RECEIVE")
  private Long ftReceive;

  @Column(name = "FT_EXECUTE")
  private Long ftExecute;

  @Column(name = "FT_FINISH")
  private Long ftFinish;

  @Column(name = "CD_CLOSE")
  private Long cdClose;


  public WoPriorityDTO toDTO() {
    WoPriorityDTO dto = new WoPriorityDTO(
        priorityId
        , woTypeId
        , cdReceivedAlarm
        , cdReceivedAlarmType
        , cdReceivedThreshold
        , cdReceivedThresholdType
        , ftReceivedAlarm
        , ftReceivedAlarmType
        , ftReceivedThreshold
        , ftReceivedThresholdType
        , ftProcessAlarm
        , ftProcessAlarmType
        , ftProcessThreshold
        , ftProcessThresholdType
        , priorityName
        , priorityCode
        , isEnable
        , cdReceive
        , cdForwardtToFt
        , ftReceive
        , ftExecute
        , ftFinish
        , cdClose
    );
    return dto;
  }
}

