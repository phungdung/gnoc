package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrSchedulePeriodicDTO;
import java.util.List;

public interface MrSchedulePeriodicBusiness {

  List<MrSchedulePeriodicDTO> getListMrSchedulePeriodicDTO(
      MrSchedulePeriodicDTO mrSchedulePeriodicDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  ResultInSideDto insertMrSchedulePeriodic(MrSchedulePeriodicDTO ms);
}
