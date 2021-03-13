package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrITHardScheduleBusiness {

  Datatable getListMrScheduleITHard(MrITHardScheduleDTO mrITHardScheduleDTO);

  MrITHardScheduleDTO getDetail(Long scheduleId);

  ResultInSideDto deleteMrScheduleITHard(Long mrScheduleId);

  File exportData(MrITHardScheduleDTO mrITHardScheduleDTO) throws Exception;

  ResultInSideDto onUpdateHard(MrITHardScheduleDTO mrITHardScheduleDTO) throws Exception;

  ResultInSideDto importData(MultipartFile fileImport);

  ResultInSideDto deleteMrScheduleITSoft(Long mrScheduleId);

  File getTemplate(String type) throws Exception;

  List<MrITHardScheduleDTO> getListRegionByMrSynItDevices(String country);

}
