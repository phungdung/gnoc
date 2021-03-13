package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.repository.GnocLanguageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class GnocLanguageBusinessImpl implements GnocLanguageBusiness {

  @Autowired
  GnocLanguageRepository gnocLanguageRepository;

  @Override
  public ResultInSideDto insertGnocLanguageDTO(GnocLanguageDto gnocLanguageDto) {
    log.debug("Request to insertGnocLanguageDTO : {}", gnocLanguageDto);
    return gnocLanguageRepository.insertGnocLanguageDTO(gnocLanguageDto);
  }

  @Override
  public String updateGnocLanguageDTO(GnocLanguageDto dto) {
    log.debug("Request to updateGnocLanguageDTO : {}", dto);
    return gnocLanguageRepository.updateGnocLanguageDTO(dto);
  }

  @Override
  public String deleteGnocLanguageById(Long id) {
    log.debug("Request to deleteGnocLanguageById : {}", id);
    return gnocLanguageRepository.deleteGnocLanguageById(id);
  }

  @Override
  public Datatable getListGnocLanguage(GnocLanguageDto dto) {
    log.debug("Request to getListGnocLanguage : {}", dto);
    return gnocLanguageRepository.getListGnocLanguage(dto);
  }

  @Override
  public GnocLanguageDto findGnocLanguageId(Long id) {
    log.debug("Request to findGnocLanguageId : {}", id);
    return gnocLanguageRepository.findGnocLanguageId(id);
  }
}
