package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgCrImpactFrameRepository {

  CrImpactFrameInsiteDTO findCrImpactFrameById(Long id);

  ResultInSideDto insertCrImpactFrame(CrImpactFrameInsiteDTO crImpactFrameDTO);

  String deleteCrImpactFrameById(Long id);

  ResultInSideDto updateCrImpactFrame(CrImpactFrameInsiteDTO dto);

  Datatable getListCrImpactFrame(CrImpactFrameInsiteDTO dto);
}
