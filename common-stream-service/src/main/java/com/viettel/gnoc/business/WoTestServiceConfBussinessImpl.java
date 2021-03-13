package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WoTestServiceConfDTO;
import com.viettel.gnoc.repository.WoTestServiceConfRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoTestServiceConfBussinessImpl implements WoTestServiceConfBussiness {

  @Autowired
  private WoTestServiceConfRepository woTestServiceConfRepository;

  @Override
  public Datatable getListWoTestServiceConf(WoTestServiceConfDTO woTestServiceConfDTO) {
    log.debug("Request to getListWoTestServiceConf : {}", woTestServiceConfDTO);
    return woTestServiceConfRepository.getListWoTestServiceConf(woTestServiceConfDTO);
  }

  @Override
  public ResultInSideDto insert(WoTestServiceConfDTO woTestServiceConfDTO) {
    log.debug("Request to add: {}", woTestServiceConfDTO);
    return woTestServiceConfRepository.add(woTestServiceConfDTO);
  }

  @Override
  public ResultInSideDto update(WoTestServiceConfDTO woTestServiceConfDTO) {
    log.debug("Request to update: {}", woTestServiceConfDTO);
    return woTestServiceConfRepository.edit(woTestServiceConfDTO);
  }

  @Override
  public WoTestServiceConfDTO getDetail(Long id) {
    log.debug("Request to getDetail: {}", id);
    WoTestServiceConfDTO woTestServiceConfDTO = woTestServiceConfRepository.getDetail(id);
    return woTestServiceConfDTO;
  }
}
