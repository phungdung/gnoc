package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;

public interface CfgCrImpactFrameBusiness {

  CrImpactFrameInsiteDTO findCrImpactFrameById(Long id);

  ResultInSideDto insertCrImpactFrame(CrImpactFrameInsiteDTO crImpactFrameDTO);

  String deleteCrImpactFrameById(Long id);

  ResultInSideDto updateCrImpactFrame(CrImpactFrameInsiteDTO dto);

  Datatable getListCrImpactFrame(CrImpactFrameInsiteDTO crImpactFrameDTO);
}
