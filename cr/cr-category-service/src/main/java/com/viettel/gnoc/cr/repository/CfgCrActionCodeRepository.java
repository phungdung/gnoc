package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrActionCodeDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgCrActionCodeRepository {

  CrActionCodeDTO findCrActionCodeById(Long id);

  ResultInSideDto insertCfgCrActionCode(CrActionCodeDTO crActionCodeDTO);

  String deleteCfgCrActionCodeById(Long id);

  String updateCfgCrActionCode(CrActionCodeDTO dto);

  Datatable getListCfgCrActionCode(CrActionCodeDTO crActionCodeDTO);
}
