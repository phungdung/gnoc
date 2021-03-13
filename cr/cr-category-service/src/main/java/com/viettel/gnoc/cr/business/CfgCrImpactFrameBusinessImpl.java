package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.repository.CfgCrImpactFrameRepository;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Slf4j
@Transactional
@Service
public class CfgCrImpactFrameBusinessImpl implements CfgCrImpactFrameBusiness {

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  private CfgCrImpactFrameRepository cfgCrImpactFrameRepository;

  @Override
  public CrImpactFrameInsiteDTO findCrImpactFrameById(Long id) {
    log.debug("Request to findCrImpactFrameById : {}", id);
    CrImpactFrameInsiteDTO crImpactFrameDTO = cfgCrImpactFrameRepository.findCrImpactFrameById(id);
    crImpactFrameDTO.setListCrImpactFrame(languageExchangeRepository
        .getListLanguageExchangeById("OPEN_PM", "OPEN_PM.CR_IMPACT_FRAME", id, null));
    return crImpactFrameDTO;
  }

  @Override
  public ResultInSideDto insertCrImpactFrame(CrImpactFrameInsiteDTO crImpactFrameDTO) {
    log.debug("Request to insertCrImpactFrame : {}", crImpactFrameDTO);
    UserToken userToken = TicketProvider.getUserToken();
    crImpactFrameDTO.setCreatedUser(userToken.getUserName());
    crImpactFrameDTO.setUpdatedUser(userToken.getUserName());
    crImpactFrameDTO.setCreatedTime(new Date());
    crImpactFrameDTO.setUpdatedTime(new Date());
    ResultInSideDto resultInSideDto = cfgCrImpactFrameRepository
        .insertCrImpactFrame(crImpactFrameDTO);
    languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.CR_IMPACT_FRAME", resultInSideDto.getId(),
            crImpactFrameDTO.getListCrImpactFrame());
    return resultInSideDto;
  }

  @Override
  public String deleteCrImpactFrameById(Long id) {
    log.debug("Request to deleteCrImpactFrameById : {}", id);
    languageExchangeRepository.deleteListLanguageExchange("OPEN_PM", "OPEN_PM.CR_IMPACT_FRAME", id);
    return cfgCrImpactFrameRepository.deleteCrImpactFrameById(id);
  }

  @Override
  public ResultInSideDto updateCrImpactFrame(CrImpactFrameInsiteDTO dto) {
    log.debug("Request to updateCrImpactFrame : {}", dto);
    UserToken userToken = TicketProvider.getUserToken();
    dto.setUpdatedUser(userToken.getUserName());
    dto.setUpdatedTime(new Date());
    ResultInSideDto result = cfgCrImpactFrameRepository.updateCrImpactFrame(dto);
    languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.CR_IMPACT_FRAME", dto.getImpactFrameId(),
            dto.getListCrImpactFrame());
    return result;
  }

  @Override
  public Datatable getListCrImpactFrame(CrImpactFrameInsiteDTO dto) {
    log.debug("Request to getListCrImpactFrame : {}", dto);

    return cfgCrImpactFrameRepository.getListCrImpactFrame(dto);
  }
}
