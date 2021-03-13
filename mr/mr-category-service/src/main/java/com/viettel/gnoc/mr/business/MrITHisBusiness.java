package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import java.io.File;
import java.util.List;

public interface MrITHisBusiness {

  Datatable getListMrSoftITHis(MrScheduleITHisDTO mrScheduleITHisDTO);

  Datatable getListMrHardITHis(MrScheduleITHisDTO mrScheduleITHisDTO);

  File exportDataMrHardITHis(MrScheduleITHisDTO mrScheduleITHisDTO) throws Exception;

  File exportData(MrScheduleITHisDTO mrScheduleITHisDTO) throws Exception;

  ResultInSideDto insertList(List<MrScheduleITHisDTO> lst);
}
