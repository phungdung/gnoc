package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.repository.WoPriorityRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoPriorityBusinessImpl implements WoPriorityBusiness {

  @Autowired
  protected WoPriorityRepository woPriorityRepository;

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Override
  public ResultInSideDto delete(Long priorityId) {
    log.debug("Request to delete : {}", priorityId);
    return woPriorityRepository.delete(priorityId);
  }

  @Override
  public List<WoPriorityDTO> findAllByWoTypeID(Long woTypeId) {
    log.debug("Request to findAllByWoTypeID : {}", woTypeId);
    return woPriorityRepository.findAllByWoTypeID(woTypeId);
  }

  @Override
  public ResultInSideDto insertListPriority(WoTypeInsideDTO woTypeInsideDTO) {
    log.debug("Request to insertList : {}", woTypeInsideDTO);
    return woPriorityRepository.insertList(woTypeInsideDTO);
  }

  @Override
  public List<WoPriorityDTO> getListWoPriorityDTO(WoPriorityDTO woPriorityDTO) {
    return woPriorityRepository
        .getListWoPriorityDTO(woPriorityDTO);
  }
}
