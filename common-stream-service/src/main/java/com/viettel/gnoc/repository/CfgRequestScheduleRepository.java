package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.RequestScheduleDTO;
import com.viettel.gnoc.cr.dto.ScheduleCRDTO;
import com.viettel.gnoc.cr.dto.ScheduleEmployeeDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgRequestScheduleRepository {

  ResultInSideDto insertRequestSchedule(RequestScheduleDTO requestScheduleDTO);

  String deleteRequestSchedule(Long id);

  Datatable getListYear(CatItemDTO catItemDTO);

  Datatable getListRequestSchedule(RequestScheduleDTO requestScheduleDTO);

  String updateRequestSchedule(RequestScheduleDTO dto);

  RequestScheduleDTO findRequestScheduleById(Long id);

  Datatable getListUnit(UnitDTO unitDTO);

  List<ScheduleEmployeeDTO> getListEmployee(ScheduleEmployeeDTO scheduleEmployeeDTO);

  List<ScheduleCRDTO> getListScheduleCR(ScheduleCRDTO scheduleCRDTO);

//  List<ScheduleCRDTO> getListScheduleCR2(ScheduleCRDTO dto);

  String insertListDayOffDTO(List<ScheduleEmployeeDTO> lst);

  String insertListCRDTO(List<ScheduleCRDTO> lstScheduleTmp);

  boolean checkExisted(RequestScheduleDTO dto);

  List<ScheduleEmployeeDTO> getListEmployee2(ScheduleEmployeeDTO dto1);

  List<CrInsiteDTO> getListCrAutoSchedule(CrInsiteDTO crInsiteDTO);

  List<CfgChildArrayDTO> getListCfgChildArray(CfgChildArrayDTO dto);
}
