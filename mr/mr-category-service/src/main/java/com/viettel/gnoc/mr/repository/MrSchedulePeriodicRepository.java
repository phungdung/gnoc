package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrSchedulePeriodicDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrSchedulePeriodicRepository {

  List<MrSchedulePeriodicDTO> search(MrSchedulePeriodicDTO mrSchedulePeriodicDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  ResultInSideDto insertMrSchedulePeriodic(MrSchedulePeriodicDTO mrSchedulePeriodicDTO);
}
