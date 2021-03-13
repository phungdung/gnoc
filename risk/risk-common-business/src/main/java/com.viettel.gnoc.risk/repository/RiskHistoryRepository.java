package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskHistoryDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskHistoryRepository {

  ResultInSideDto insertRiskHistory(RiskHistoryDTO riskHistoryDTO);

  Datatable getListRiskHistoryByRiskId(RiskHistoryDTO riskHistoryDTO);

  RiskHistoryDTO findRiskHistoryByIdFromWeb(RiskHistoryDTO riskHistoryDTO);
}
