package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrITSoftScheduleRepository {

  Datatable getListMrSchedule(MrITSoftScheduleDTO mrITSoftScheduleDTO);

  List<MrScheduleITHisDTO> getListScheduleMoveToHis(MrSynItDevicesDTO mrSynItDevicesDTO);

  List<MrITSoftScheduleDTO> getListScheduleMove(MrITSoftScheduleDTO mrITSoftScheduleDTO);

  MrITSoftScheduleDTO getDetail(Long scheduleId);

  List<MrITSoftScheduleDTO> getListRegionByMrSynItDevices(String country);

  List<MrITSoftScheduleDTO> getDataExport(MrITSoftScheduleDTO dto);

  ResultInSideDto deleteListSchedule(List<MrITSoftScheduleDTO> lstScheduleDTO);

  ResultInSideDto deleteMrScheduleIT(Long id);

  ResultInSideDto updateMrSchedule(MrITSoftScheduleDTO mrITSoftScheduleDTO);

  List<MrITSoftScheduleDTO> findListMrScheduleByGroupCode(String groupCode);

  ResultInSideDto updateMrEarAndLastestTime(MrInsideDTO mrDTO);

  List<MrITSoftScheduleDTO> getListMrSheduleITByIdForImport(List<String> idSearchs);

  MrITSoftScheduleDTO getByMrId(String mrId);
}
