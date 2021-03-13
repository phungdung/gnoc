package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;

public interface LogChangeConfigBusiness {

  ResultInSideDto insertLog(LogChangeConfigDTO logChangeConfigDTO);
}
