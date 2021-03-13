package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroublesTempClosedDTO;
import com.viettel.gnoc.incident.repository.TroublesTempClosedRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class TroublesTempClosedBusinessImpl implements TroublesTempClosedBusiness {

  @Autowired
  TroublesTempClosedRepository troublesTempClosedRepository;

  @Override
  public ResultInSideDto add(TroublesTempClosedDTO troublesTempClosedDTO) {
    log.debug("Request to add: {}", troublesTempClosedDTO);
    return troublesTempClosedRepository.add(troublesTempClosedDTO);
  }

  @Override
  public ResultInSideDto insertList(List<TroublesTempClosedDTO> troublesTempList) {
    log.debug("Request to insertList: {}", troublesTempList);
    return troublesTempClosedRepository.insertList(troublesTempList);
  }


}
