package com.viettel.gnoc.incident.service;

import com.viettel.gnoc.incident.business.CfgTimeTroubleProcessBusiness;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CfgTimeTroubleProcessServiceImpl implements CfgTimeTroubleProcessService {

  @Autowired
  CfgTimeTroubleProcessBusiness cfgTimeTroubleProcessBusiness;

  @Override
  public List<CfgTimeTroubleProcessDTO> getListCfgTimeTroubleProcessDTO(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    return cfgTimeTroubleProcessBusiness.getListCfgTimeTroubleProcessDTO(cfgTimeTroubleProcessDTO);
  }
}
