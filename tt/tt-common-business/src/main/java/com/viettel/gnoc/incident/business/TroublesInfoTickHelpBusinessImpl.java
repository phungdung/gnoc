package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.CfgSupportFormDTO;
import com.viettel.gnoc.incident.repository.TroublesTickHelpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class TroublesInfoTickHelpBusinessImpl implements TroublesInfoTickHelpBusiness {

  @Autowired
  TroublesTickHelpRepository troublesTickHelpRepository;

  @Override
  public Datatable getListInfoTickHelpByWoCode(CfgSupportFormDTO dto) {
    log.debug("Request to getListInfoTickHelpByWoCode : {}", dto);
    return troublesTickHelpRepository.getListInfoTickHelpByWoCode(dto);
  }
}
