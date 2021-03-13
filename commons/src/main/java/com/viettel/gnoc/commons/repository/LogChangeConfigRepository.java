package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;

public interface LogChangeConfigRepository {

  ResultInSideDto insertLog(LogChangeConfigDTO logChangeConfigDTO);
}
