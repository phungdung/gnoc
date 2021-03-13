package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.wo.dto.WorkTimeDTO;

public interface WoTimeBusiness {

  ResultDTO insertOrUpdateWorkTime(WorkTimeDTO workTimeDTO);
}
