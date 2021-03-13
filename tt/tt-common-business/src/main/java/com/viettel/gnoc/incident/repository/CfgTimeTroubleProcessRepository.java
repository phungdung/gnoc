package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgTimeTroubleProcessRepository {

  Datatable getListCfgTimeTroubleProcessDTO(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

}
