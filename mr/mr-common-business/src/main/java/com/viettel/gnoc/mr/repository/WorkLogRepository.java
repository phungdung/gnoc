package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.maintenance.dto.WorkLogResultDTO;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import java.util.List;

public interface WorkLogRepository {

  ResultDTO createObject(WorkLogDTO workLogDTO);

  List<WorkLogResultDTO> getListWorklogSearch(WorkLogDTO wlgDTO);
}
