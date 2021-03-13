package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureITHardDTO;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import java.util.List;

public interface MrITHardProcedureRepository {

  BaseDto sqlSearch(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO);

  Datatable getListMrHardITProcedure(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO);

  MrCfgProcedureITHardDTO getDetail(Long procedureId);

  ResultInSideDto insertOrUpdate(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO);

  List<MrCfgProcedureITHardDTO> getListDataExport(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO);

  ResultInSideDto deleteMrCfgProcedureITHard(Long procedureId);

  boolean checkDuplicateCfgProcedureITHardByDTO(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO);

  List<MrSynItDevicesDTO> getListDeviceType();

  List<MrCfgProcedureITHardDTO> checkMrCfgProcedureITHardExist(
      MrCfgProcedureITHardDTO cfgProcedureDTO);

  List<MrITHardScheduleDTO> getScheduleInProcedureITHard(MrITHardScheduleDTO mrITHardScheduleDTO);
}
