package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.repository.ItAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class ItAccountBusinessImpl implements ItAccountBusiness {

  @Autowired
  ItAccountRepository itAccountRepository;

  @Override
  public Datatable getListItAccountDTO(TroublesInSideDTO dto) {
    return itAccountRepository.getListItAccountDTO(dto);
  }
}
