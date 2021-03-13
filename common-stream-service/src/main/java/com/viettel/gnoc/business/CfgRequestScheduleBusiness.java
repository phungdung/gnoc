package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.cr.dto.RequestScheduleDTO;
import com.viettel.gnoc.cr.dto.ScheduleCRDTO;
import com.viettel.gnoc.cr.dto.ScheduleEmployeeDTO;
import java.io.File;
import java.util.List;

public interface CfgRequestScheduleBusiness {

  ResultInSideDto insertRequestSchedule(RequestScheduleDTO requestScheduleDTO);

  String deleteRequestSchedule(Long id);

  Datatable getListYear(CatItemDTO catItemDTO);

  Datatable getListRequestSchedule(RequestScheduleDTO requestScheduleDTO);

  String updateRequestSchedule(RequestScheduleDTO dto);

  RequestScheduleDTO findRequestScheduleById(Long id);

  Datatable getListUnit(UnitDTO unitDTO);

  List<ScheduleEmployeeDTO> getListEmployee(ScheduleEmployeeDTO scheduleEmployeeDTO);

  List<ScheduleCRDTO> onSearchCR(ScheduleCRDTO scheduleCRDTO, Long type);

  File exportData(RequestScheduleDTO requestScheduleDTO) throws Exception;

  /*List<ScheduleCRDTO> getListScheduleCR2(RequestScheduleDTO requestScheduleDTO);
   */
//  List<ScheduleCRFormDTO> searchCR2(RequestScheduleDTO requestScheduleDTO);
//
//  List<ScheduleCRDTO> searchCRAfter2(RequestScheduleDTO requestScheduleDTO);

  ResultInSideDto onSave(RequestScheduleDTO requestScheduleDTO);

  RequestScheduleDTO getCrBefore(RequestScheduleDTO dtoCheck);

  File exportDataCRAfter(RequestScheduleDTO dto4) throws Exception;

  ResultInSideDto cancelStatus(RequestScheduleDTO dto);

  List<ScheduleCRDTO> searchCRAfterFail(RequestScheduleDTO requestScheduleDTO);
}
