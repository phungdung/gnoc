package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrActionCodeDTO;
import com.viettel.gnoc.cr.repository.CfgCrActionCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class CfgCrActionCodeBusinessImpl implements CfgCrActionCodeBusiness {

  @Autowired
  private CfgCrActionCodeRepository cfgCrActionCodeRepository;

  @Override
  public CrActionCodeDTO findCrActionCodeById(Long id) {
    log.debug("Request to findCrActionCodeById : {}", id);
    return cfgCrActionCodeRepository.findCrActionCodeById(id);
  }

  @Override
  public ResultInSideDto insertCfgCrActionCode(CrActionCodeDTO crActionCodeDTO) {
    log.debug("Request to insertCfgCrActionCode : {}", crActionCodeDTO);
    return cfgCrActionCodeRepository.insertCfgCrActionCode(crActionCodeDTO);
  }

  @Override
  public String deleteCfgCrActionCodeById(Long id) {
    log.debug("Request to deleteCfgCrActionCodeById : {}", id);
    return cfgCrActionCodeRepository.deleteCfgCrActionCodeById(id);
  }

  @Override
  public String updateCfgCrActionCode(CrActionCodeDTO dto) {
    log.debug("Request to updateCfgCrActionCode : {}", dto);
    return cfgCrActionCodeRepository.updateCfgCrActionCode(dto);
  }

  @Override
  public Datatable getListCfgCrActionCode(CrActionCodeDTO crActionCodeDTO) {
    log.debug("Request to getListCfgCrActionCode : {}", crActionCodeDTO);
    return cfgCrActionCodeRepository.getListCfgCrActionCode(crActionCodeDTO);
  }
}
