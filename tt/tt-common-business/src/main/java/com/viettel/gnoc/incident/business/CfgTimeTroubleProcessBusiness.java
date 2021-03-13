package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import java.util.List;

public interface CfgTimeTroubleProcessBusiness {


  List<CfgTimeTroubleProcessDTO> getListCfgTimeTroubleProcessDTO(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);
}
