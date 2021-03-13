package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.repository.RelatedTTRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class TroublesRelatedBusinessImpl implements TroublesRelatedBusiness {

  @Autowired
  RelatedTTRepository relatedTTRepository;

  @Override
  public Datatable getListRelatedTT(TroublesInSideDTO dto) {
    log.debug("Request to getListRelatedTT : {}", dto);
    return relatedTTRepository.getListRelatedTT(dto);
  }

  @Override
  public Datatable getListRelatedTTByPopup(TroublesInSideDTO dto) {
    log.debug("Request to getListRelatedTTByPopup : {}", dto);
    Datatable datatable = relatedTTRepository.getListRelatedTTByPopup(dto);
    return datatable;
  }

  @Override
  public Datatable getListRelatedTTByPopupAdd(TroublesInSideDTO dto) {
    log.debug("Request to getListRelatedTTByPopupAdd : {}", dto);
    return relatedTTRepository.getListRelatedTTByPopupAdd(dto);
  }
}
