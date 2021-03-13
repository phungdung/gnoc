package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrITHisRepository {

  Datatable getListMrSoftITHis(MrScheduleITHisDTO mrScheduleITHisDTO);

  Datatable getListMrHardITHis(MrScheduleITHisDTO mrScheduleITHisDTO);

  List<MrScheduleITHisDTO> getDataExportMrHardITHis(MrScheduleITHisDTO dto);

  List<MrScheduleITHisDTO> getDataExport(MrScheduleITHisDTO dto);

  ResultInSideDto insertUpdateListScheduleHis(List<MrScheduleITHisDTO> mrScheduleITHisDTOS);

  ResultInSideDto insertList(List<MrScheduleITHisDTO> lst);
}
