package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrITSoftScheduleBusiness {

  Datatable getListMrSchedule(MrITSoftScheduleDTO mrITSoftScheduleDTO);

  MrITSoftScheduleDTO getDetail(Long scheduleId);

  List<MrITSoftScheduleDTO> getListRegionByMrSynItDevices(String country);

  File exportData(MrITSoftScheduleDTO mrITSoftScheduleDTO) throws Exception;

  ResultInSideDto deleteMrScheduleITSoft(Long mrScheduleId);

  ResultInSideDto onUpdateSoft(MrITSoftScheduleDTO mrITSoftScheduleDTO) throws Exception;

  ResultInSideDto importData(MultipartFile fileImport);

  File getTemplate(String type) throws Exception;

}
