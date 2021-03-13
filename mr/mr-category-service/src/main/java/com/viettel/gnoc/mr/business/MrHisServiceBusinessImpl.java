package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.mr.repository.MrHisServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MrHisServiceBusinessImpl implements MrHisServiceBusiness {

  @Autowired
  MrHisServiceRepository mrHisServiceRepository;

  @Override
  public ResultInSideDto insertMrHis(MrHisDTO hisDto) {
    return mrHisServiceRepository.insertMrHis(hisDto);
  }
}
