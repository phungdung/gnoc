package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.TroubleImportantEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author hungtv77
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class TroubleImportantDTO extends BaseDto {

  private Long troubleId;
  private String troubleName;
  private String countryId;
  private String priorityId;
  private String classNetwork;
  private String array;
  private String isAlarm;
  private Date startTime;
  private Date endTime;
  private Date processTime;
  private String numberPakh;
  private String numberPakhReal;
  private String groupReason;
  private String nature;
  private String detail;
  private String confirmTrouble;
  private String serviceOwner;
  private String unitId;
  private String ptCode;
  private String vender;
  private String isNotAlarm;
  private String triggerUnit;
  private String week;
  private Date detectTime;
  private Date detectTroubleTime;
  private String userCreate;
  private String userDetect;
  private String userProcess;
  private String coordination;
  private String arrayName;
  private String groupReasonName;

  private Date createTimeFrom;
  private Date createTimeTo;
  private String startTimeString;
  private String endTimeString;
  private String processTimeString;
  private String detectTimeString;
  private String detectTroubleTimeString;

  public TroubleImportantDTO(Long troubleId, String troubleName, String countryId, String priorityId, String classNetwork,
    String array, String isAlarm, Date startTime, Date endTime, Date processTime, String numberPakh,
     String numberPakhReal, String groupReason, String nature, String detail, String confirmTrouble, String serviceOwner,
      String unitId, String ptCode, String vender, String isNotAlarm, String triggerUnit, String week,
       Date detectTime, Date detectTroubleTime, String userCreate, String userDetect, String userProcess,
        String coordination, String arrayName, String groupReasonName) {
    this.troubleId = troubleId;
    this.troubleName = troubleName;
    this.countryId = countryId;
    this.priorityId = priorityId;
    this.classNetwork = classNetwork;
    this.array = array;
    this.isAlarm = isAlarm;
    this.startTime = startTime;
    this.endTime = endTime;
    this.processTime = processTime;
    this.numberPakh = numberPakh;
    this.numberPakhReal = numberPakhReal;
    this.groupReason = groupReason;
    this.nature = nature;
    this.detail = detail;
    this.confirmTrouble = confirmTrouble;
    this.serviceOwner = serviceOwner;
    this.unitId = unitId;
    this.ptCode = ptCode;
    this.vender = vender;
    this.isNotAlarm = isNotAlarm;
    this.triggerUnit = triggerUnit;
    this.week = week;
    this.detectTime = detectTime;
    this.detectTroubleTime = detectTroubleTime;
    this.userCreate = userCreate;
    this.userDetect = userDetect;
    this.userProcess = userProcess;
    this.coordination = coordination;
    this.arrayName = arrayName;
    this.groupReasonName = groupReasonName;
  }

  public TroubleImportantEntity toEntity() {
    return new TroubleImportantEntity(troubleId, troubleName, countryId, priorityId, classNetwork, array, isAlarm, startTime,
        endTime, processTime, numberPakh, numberPakhReal, groupReason, nature, detail, confirmTrouble, serviceOwner,
        unitId, ptCode, vender, isNotAlarm, triggerUnit, week, detectTime, detectTroubleTime, userCreate, userDetect,
        userProcess, coordination, arrayName, groupReasonName);
  }
}
