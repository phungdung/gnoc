package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrITHardScheduleRepository {

  Datatable getListMrScheduleITHard(MrITHardScheduleDTO mrITHardScheduleDTO);

  MrITHardScheduleDTO getDetail(Long scheduleId);

  List<MrITHardScheduleDTO> getListDataExport(MrITHardScheduleDTO mrITHardScheduleDTO);

  ResultInSideDto updateMrSchedule(MrITHardScheduleDTO mrITHardScheduleDTO);

  ResultInSideDto deleteMrScheduleITHard(Long mrScheduleId);

  ResultInSideDto deleteListSchedule(List<MrITHardScheduleDTO> lstMrITSoftSchedule);

  List<MrITHardScheduleDTO> findListMrScheduleByGroupCode(String groupCode);

  List<MrITHardScheduleDTO> getListRegionByMrSynItDevices(String country);

  List<MrITHardScheduleDTO> getListMrSheduleITByIdForImport(List<String> idSearchs);

  List<MrScheduleITHisDTO> getListScheduleMoveToHis(MrSynItDevicesDTO mrSynItDevicesDTO);

  ResultInSideDto updateMrEarAndLastestTime(MrInsideDTO mrDTO);

  List<MrITHardScheduleDTO> getListScheduleMove(MrITHardScheduleDTO mrITHardScheduleDTO);
}
