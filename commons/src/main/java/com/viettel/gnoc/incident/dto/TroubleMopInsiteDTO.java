package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.incident.model.TroubleMopEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class TroubleMopInsiteDTO extends BaseDto {

  //Fields
  private Long troubleMopId;
  private Long troubleId;
  private Long rule;
  private String tblAlarmCurrent;
  private Long runType;
  private Long mopId;
  private String mopName;
  private Long groupMopId;
  private String groupMopName;
  private Long alarmId;
  private Long numberRun;
  private Long runCycle;
  private String system;
  private Date createTime;
  private String workLog;
  private Long maxNumberRun;
  private Long stateMop;
  private String domain;
  private Date lastUpdateTime;
  private Long isRun;

  private String stateMopName;
  private String runTypeName;

  public TroubleMopInsiteDTO(Long troubleMopId, Long troubleId, Long rule, String tblAlarmCurrent,
      Long runType,
      Long mopId, String mopName, Long groupMopId, String groupMopName, Long alarmId,
      Long numberRun, Long runCycle, String system,
      Date createTime, String workLog, Long maxNumberRun, Long stateMop, String domain,
      Date lastUpdateTime, Long isRun) {
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

  public TroubleMopEntity toEntity() {
    TroubleMopEntity model = new TroubleMopEntity(
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
    return model;
  }


}
