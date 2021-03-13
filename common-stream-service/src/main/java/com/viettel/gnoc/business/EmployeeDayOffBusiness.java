package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.EmployeeDayOffDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeDayOffBusiness {

  Datatable getListEmployeeDayOff(EmployeeDayOffDTO employeeDayOffDTO);

  EmployeeDayOffDTO findEmployeeDayOffById(Long id);

  String deleteEmployeeDayOffById(Long id);

  ResultInSideDto insertEmployeeDayOff(List<EmployeeDayOffDTO> employeeDayOffDTOS);

  ResultInSideDto updateEmployeeDayOff(EmployeeDayOffDTO dto);

  ResultInSideDto importData(MultipartFile file);

  File getTemplate() throws Exception;

  File exportData(EmployeeDayOffDTO dto) throws Exception;
}
