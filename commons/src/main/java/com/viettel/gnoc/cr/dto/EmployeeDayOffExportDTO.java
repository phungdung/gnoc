package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
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
public class EmployeeDayOffExportDTO extends BaseDto {

  private String idDayOff;
  private String empId;
  private String empUsername;
  private String dayOff;
  private String vacation;
  private String resultImport;
  private String empUnit;
}
