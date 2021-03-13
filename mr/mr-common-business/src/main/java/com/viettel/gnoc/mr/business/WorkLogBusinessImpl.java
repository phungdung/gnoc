package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.maintenance.dto.WorkLogResultDTO;
import com.viettel.gnoc.mr.repository.WorkLogRepository;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WorkLogBusinessImpl implements WorkLogBusiness {

  @Autowired
  WorkLogRepository workLogRepository;

  @Override
  public ResultDTO createObject(WorkLogDTO workLogDTO) {
    return workLogRepository.createObject(workLogDTO);
  }

  @Override
  public List<WorkLogResultDTO> getListWorklogSearch(WorkLogDTO wlgDTO) {
    return workLogRepository.getListWorklogSearch(wlgDTO);
  }
}
