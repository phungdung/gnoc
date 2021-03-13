package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskTypeRepository {

  Datatable getDataRiskTypeSearchWeb(RiskTypeDTO riskTypeDTO);

  List<RiskTypeDTO> getListRiskTypeDTO(RiskTypeDTO riskTypeDTO);

  RiskTypeDTO findRiskTypeByIdFromWeb(Long riskTypeId);

  ResultInSideDto insertOrUpdateRiskType(RiskTypeDTO riskTypeDTO);

  ResultInSideDto delete(Long riskTypeId);

  List<RiskTypeDTO> getListRiskTypeExport(RiskTypeDTO riskTypeDTO);

  RiskTypeDTO checkRiskTypeExit(RiskTypeDTO riskTypeDTO);
}
