package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.repository.CfgTimeTroubleProcessRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CfgTimeTroubleProcessBusinessImpl implements CfgTimeTroubleProcessBusiness {

  @Autowired
  CfgTimeTroubleProcessRepository cfgTimeTroubleProcessRepository;

  @Override
  public List<CfgTimeTroubleProcessDTO> getListCfgTimeTroubleProcessDTO(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    List<CfgTimeTroubleProcessDTO> lstTrouble;
    log.debug("Request to getListCfgTimeTroubleProcessDTO: {}", cfgTimeTroubleProcessDTO);
    Datatable datatable = cfgTimeTroubleProcessRepository
        .getListCfgTimeTroubleProcessDTO(cfgTimeTroubleProcessDTO);
    lstTrouble = (List<CfgTimeTroubleProcessDTO>) datatable.getData();
    return lstTrouble;
  }
}
