package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskSystemRepository {

  Datatable getDataRiskSystemSearchWeb(RiskSystemDTO riskSystemDTO);

  List<RiskSystemDTO> getListRiskSystem(RiskSystemDTO riskSystemDTO);

  ResultInSideDto insertOrUpdateRiskSystem(RiskSystemDTO riskSystemDTO);

  ResultInSideDto deleteRiskSystem(Long id);

  RiskSystemDTO findRiskSystemByIdFromWeb(Long id, Double offsetFromUser);

  List<RiskSystemDTO> getListRiskSystemExport(RiskSystemDTO riskSystemDTO);

  RiskSystemDTO checkRiskSystemExit(RiskSystemDTO riskSystemDTO);

  RiskSystemDTO getRiskSystemOldById(Long id);
}
