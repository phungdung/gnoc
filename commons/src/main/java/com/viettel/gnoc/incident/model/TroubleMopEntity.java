package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TroubleMopInsiteDTO;
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


@Entity
@Table(schema = "ONE_TM", name = "TROUBLE_MOP")
@NoArgsConstructor
@Getter
@Setter
public class TroubleMopEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TROUBLE_MOP_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "TROUBLE_MOP_ID", unique = true, nullable = false)
  private Long troubleMopId;

  @Column(name = "TROUBLE_ID")
  private Long troubleId;

  @Column(name = "RULE")
  private Long rule;

  @Column(name = "TBL_ALARM_CURRENT")
  private String tblAlarmCurrent;

  @Column(name = "RUN_TYPE")
  private Long runType;

  @Column(name = "MOP_ID")
  private Long mopId;

  @Column(name = "MOP_NAME")
  private String mopName;

  @Column(name = "GROUP_MOP_ID")
  private Long groupMopId;

  @Column(name = "GROUP_MOP_NAME")
  private String groupMopName;

  @Column(name = "ALARM_ID")
  private Long alarmId;

  @Column(name = "NUMBER_RUN")
  private Long numberRun;

  @Column(name = "RUN_CYCLE")
  private Long runCycle;

  @Column(name = "SYSTEM")
  private String system;

  @Column(name = "CREATE_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date createTime;

  @Column(name = "WORK_LOG")
  private String workLog;

  @Column(name = "MAX_NUMBER_RUN")
  private Long maxNumberRun;

  @Column(name = "STATE_MOP")
  private Long stateMop;

  @Column(name = "DOMAIN")
  private String domain;

  @Column(name = "last_update_time")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date lastUpdateTime;

  @Column(name = "is_run")
  private Long isRun;

  public TroubleMopEntity(Long troubleMopId, Long troubleId, Long rule, String tblAlarmCurrent,
      Long runType, Long mopId, String mopName, Long groupMopId,
      String groupMopName, Long alarmId, Long numberRun, Long runCycle, String system,
      Date createTime, String workLog, Long maxNumberRun,
      Long stateMop, String domain, Date lastUpdateTime, Long isRun) {
    this.troubleMopId = troubleMopId;
    this.troubleId = troubleId;
    this.rule = rule;
    this.tblAlarmCurrent = tblAlarmCurrent;
    this.runType = runType;
    this.mopId = mopId;
    this.mopName = mopName;
    this.groupMopId = groupMopId;
    this.groupMopName = groupMopName;
    this.alarmId = alarmId;
    this.numberRun = numberRun;
    this.runCycle = runCycle;
    this.system = system;
    this.createTime = createTime;
    this.workLog = workLog;
    this.maxNumberRun = maxNumberRun;
    this.stateMop = stateMop;
    this.domain = domain;
    this.lastUpdateTime = lastUpdateTime;
    this.isRun = isRun;
  }

  public TroubleMopInsiteDTO toDTO() {
    TroubleMopInsiteDTO dto = new TroubleMopInsiteDTO(
        troubleMopId,
        troubleId,
        rule,
        tblAlarmCurrent,
        runType,
        mopId,
        mopName,
        groupMopId,
        groupMopName,
        alarmId,
        numberRun,
        runCycle,
        system,
        createTime,
        workLog,
        maxNumberRun,
        stateMop,
        domain,
        lastUpdateTime,
        isRun
    );
    return dto;
  }
}
