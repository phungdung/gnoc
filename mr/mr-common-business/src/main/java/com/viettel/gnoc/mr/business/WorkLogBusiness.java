package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.maintenance.dto.WorkLogResultDTO;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import java.util.List;

public interface WorkLogBusiness {

  ResultDTO createObject(WorkLogDTO workLogDTO);

  List<WorkLogResultDTO> getListWorklogSearch(WorkLogDTO wlgDTO);
}
