package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.cr.dto.EmployeeDayOffDTO;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDayOffRepository {

  Datatable getListEmployeeDayOff(EmployeeDayOffDTO employeeDayOffDTO);

  List<EmployeeDayOffDTO> getListEmployeeDayOffExport(EmployeeDayOffDTO dto);

  EmployeeDayOffDTO findEmployeeDayOffById(Long id);

  String deleteEmployeeDayOffById(Long id);

  ResultInSideDto insertEmployeeDayOff(List<EmployeeDayOffDTO> employeeDayOffDTOS);

  ResultInSideDto updateEmployeeDayOff(EmployeeDayOffDTO dto);

  List<EmployeeDayOffDTO> getListEmployeeDayOffExport2();

  List<UsersInsideDto> getListUserInUnit(UnitDTO dto);

  List<UsersInsideDto> checkUserIsEnable(String userId);

  EmployeeDayOffDTO checkEmployeeDayOffExist(Long empId, Date dayOff, String vacation);

  //List<EmployeeDayOffDTO> checkDuplicateEmployee(EmployeeDayOffDTO dto);
}
