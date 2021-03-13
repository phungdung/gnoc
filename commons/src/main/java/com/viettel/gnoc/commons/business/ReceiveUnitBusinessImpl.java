package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
import com.viettel.gnoc.commons.repository.ReceiveUnitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ReceiveUnitBusinessImpl implements ReceiveUnitBusiness {

  @Autowired
  protected ReceiveUnitRepository receiveUnitRepository;

  @Override
  public Datatable getListReceiveUnitSearch(ReceiveUnitDTO receiveUnitDTO) {
    log.debug("Request to getListReceiveUnitSearch: {}", receiveUnitDTO);
    return receiveUnitRepository.getListReceiveUnitSearch(receiveUnitDTO);
  }

}
