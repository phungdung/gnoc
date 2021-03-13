package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.LogChangeConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class LogChangeConfigBusinessImpl implements LogChangeConfigBusiness {

  @Autowired
  protected LogChangeConfigRepository logChangeConfigRepository;

  @Override
  public ResultInSideDto insertLog(LogChangeConfigDTO logChangeConfigDTO) {
    log.debug("Request to insertProblemActionLogs: {}", logChangeConfigDTO);
    return logChangeConfigRepository.insertLog(logChangeConfigDTO);
  }
}
