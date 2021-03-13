package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskSystemHistoryDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskSystemHistoryRepository {

  ResultInSideDto insertRiskSystemHistory(RiskSystemHistoryDTO riskSystemHistoryDTO);

  Datatable getListRiskSystemHistoryBySystemId(RiskSystemHistoryDTO riskSystemHistoryDTO);

  List<RiskSystemHistoryDTO> getListHistoryBySystemId(Long systemId);
}
