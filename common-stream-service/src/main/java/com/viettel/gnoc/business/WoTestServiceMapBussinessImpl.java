package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.repository.WoTestServiceMapRepository;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoTestServiceMapBussinessImpl implements WoTestServiceMapBussiness {

  @Autowired
  WoTestServiceMapRepository woTestServiceMapRepository;

  @Override
  public List<WoTestServiceMapDTO> search(WoTestServiceMapDTO tDTO, int start, int maxResult,
      String sortType, String sortField) {
    return woTestServiceMapRepository.getListWoTestServiceMapDTO(tDTO, start, maxResult, sortType, sortField);
  }

  @Override
  public ResultDTO insertWoTestServiceMap(WoTestServiceMapDTO woTestServiceMapDTO) {
    return woTestServiceMapRepository.createObject(woTestServiceMapDTO);
  }

  @Override
  public String insertOrUpdateListWoTestServiceMap(List<WoTestServiceMapDTO> lsWoTestServiceMapDTOS) {
    return woTestServiceMapRepository.insertList(lsWoTestServiceMapDTOS);
  }
}
