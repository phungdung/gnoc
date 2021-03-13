package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoPriorityEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WoPriorityDTO extends BaseDto {

  //Fields
  private Long priorityId;
  private Long woTypeId;
  private String cdReceivedAlarm;
  private Long cdReceivedAlarmType;
  private String cdReceivedThreshold;
  private Long cdReceivedThresholdType;
  private String ftReceivedAlarm;
  private Long ftReceivedAlarmType;
  private String ftReceivedThreshold;
  private Long ftReceivedThresholdType;
  private String ftProcessAlarm;
  private Long ftProcessAlarmType;
  private String ftProcessThreshold;
  private Long ftProcessThresholdType;
  private String priorityName;
  private String priorityCode;
  private Long isEnable;
  private Long cdReceive;
  private Long cdForwardToFt;
  private Long ftReceive;
  private Long ftExecute;
  private Long ftFinish;
  private Long cdClose;


  public WoPriorityEntity toEntity() {
    WoPriorityEntity model = new WoPriorityEntity(
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
        , cdForwardToFt
        , ftReceive
        , ftExecute
        , ftFinish
        , cdClose
    );
    return model;
  }
}
