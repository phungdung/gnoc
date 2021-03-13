package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.wo.dto.WorkTimeDTO;
import com.viettel.gnoc.wo.repository.WoTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoTimeBusinessImpl implements WoTimeBusiness {

  @Autowired
  WoTimeRepository woTimeRepository;

  @Override
  public ResultDTO insertOrUpdateWorkTime(WorkTimeDTO workTimeDTO) {
    return woTimeRepository.insertOrUpdateWorkTime(workTimeDTO);
  }
}
