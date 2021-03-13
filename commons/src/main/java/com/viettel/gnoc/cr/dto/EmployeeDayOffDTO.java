package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.cr.model.EmployeeDayOffEntity;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
@MultiFieldUnique(message = "{validation.EmployeeDayOffDTO.multiple.unique}", clazz = EmployeeDayOffEntity.class,
    uniqueFields = "empId,dayOff", idField = "idDayOff")
public class EmployeeDayOffDTO extends BaseDto {

  private Long idDayOff;
  private Long empId;
  private Long empUnit;
  @NotEmpty(message = "{validation.employeeDayOff.null.empUsername}")
  private String empUsername;
  @NotNull(message = "{validation.employeeDayOff.null.dayOff}")
  private Date dayOff;
  @NotEmpty(message = "{validation.employeeDayOff.null.vacation}")
  private String vacation;

  private Date dayOffFrom;
  private Date dayOffTo;

  private String unitName;
  private String unitId;
  private String unitCode;
  private String fromDate;
  private String toDate;
  private String resultImport;
  private String email;
  private String empIdError;
  private String dayOffError;
  private Long statusUpdate;
  private Long idDayOffSave;
  private String vacationName;

  public EmployeeDayOffDTO(Long idDayOff, Long empId, Long empUnit, String empUsername, Date dayOff,
      String vacation) {
    this.idDayOff = idDayOff;
    this.empId = empId;
    this.empUnit = empUnit;
    this.empUsername = empUsername;
    this.dayOff = dayOff;
    this.vacation = vacation;
  }

  public EmployeeDayOffEntity toEntity() {
    return new EmployeeDayOffEntity(
        idDayOff,
        empId,
        empUnit,
        empUsername,
        dayOff,
        vacation
    );
  }
}
