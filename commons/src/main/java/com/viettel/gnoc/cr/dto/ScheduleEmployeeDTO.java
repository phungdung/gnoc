package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.ScheduleEmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ScheduleEmployeeDTO extends BaseDto {

  private Long idEmployee;
  private Long idSchedule;
  private String nameDisplay;
  private String userName;
  private String empArray;
  private String empChildren;
  private Long empLevel;
  private String workingDay;
  private Long unitId;

  private String type;
  private String year;
  private String month;
  private String week;
  private String dayOff;
  private String arrayChildName;
  private String parentName;
  private String startDate;
  private String endDate;

  //tiennv them nang cap CR
  private String empLevelName;

  public ScheduleEmployeeDTO(Long idEmployee, Long idSchedule, String nameDisplay, String userName,
      String empArray, String empChildren, Long empLevel, Long unitId,
      String dayOff) {
    this.idEmployee = idEmployee;
    this.idSchedule = idSchedule;
    this.nameDisplay = nameDisplay;
    this.userName = userName;
    this.empArray = empArray;
    this.empChildren = empChildren;
    this.empLevel = empLevel;
    this.unitId = unitId;
    this.dayOff = dayOff;
  }

  public ScheduleEmployeeEntity toEntity() {
    return new ScheduleEmployeeEntity(
        idEmployee,
        idSchedule,
        nameDisplay,
        userName,
        empArray,
        empChildren,
        empLevel,
        unitId,
        dayOff
    );
  }
}
