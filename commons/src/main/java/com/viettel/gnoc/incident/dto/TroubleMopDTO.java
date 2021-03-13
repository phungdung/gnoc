package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
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
public class TroubleMopDTO extends BaseDto {

  //Fields
  private String troubleMopId;
  private String troubleId;
  private String rule;
  private String tblAlarmCurrent;
  private String runType;
  private String mopId;
  private String mopName;
  private String groupMopId;
  private String groupMopName;
  private String alarmId;
  private String numberRun;
  private String runCycle;
  private String system;
  private String createTime;
  private String workLog;
  private String maxNumberRun;
  private String stateMop;
  private String domain;
  private String lastUpdateTime;
  private String isRun;

  private String stateMopName;
  private String runTypeName;

  public TroubleMopDTO(String troubleMopId, String troubleId, String rule, String tblAlarmCurrent,
      String runType,
      String mopId, String mopName, String groupMopId, String groupMopName, String alarmId,
      String numberRun, String runCycle, String system,
      String createTime, String workLog, String maxNumberRun, String stateMop, String domain,
      String lastUpdateTime, String isRun) {
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

  public TroubleMopInsiteDTO toInsiteDTO() {
    TroubleMopInsiteDTO model = new TroubleMopInsiteDTO(
        !StringUtils.validString(troubleMopId) ? null : Long.valueOf(troubleMopId),
        !StringUtils.validString(troubleId) ? null : Long.valueOf(troubleId),
        !StringUtils.validString(rule) ? null : Long.valueOf(rule),
        tblAlarmCurrent,
        !StringUtils.validString(runType) ? null : Long.valueOf(runType),
        !StringUtils.validString(mopId) ? null : Long.valueOf(mopId),
        mopName,
        !StringUtils.validString(groupMopId) ? null : Long.valueOf(groupMopId),
        groupMopName,
        !StringUtils.validString(alarmId) ? null : Long.valueOf(alarmId),
        !StringUtils.validString(numberRun) ? null : Long.valueOf(numberRun),
        !StringUtils.validString(runCycle) ? null : Long.valueOf(runCycle),
        system,
        !StringUtils.validString(createTime) ? null : DateTimeUtils.convertStringToDate(createTime),
        workLog,
        !StringUtils.validString(maxNumberRun) ? null : Long.valueOf(maxNumberRun),
        !StringUtils.validString(stateMop) ? null : Long.valueOf(stateMop),
        domain,
        !StringUtils.validString(lastUpdateTime) ? null
            : DateTimeUtils.convertStringToDate(lastUpdateTime),
        !StringUtils.validString(isRun) ? null : Long.valueOf(isRun)
    );
    return model;
  }
}
