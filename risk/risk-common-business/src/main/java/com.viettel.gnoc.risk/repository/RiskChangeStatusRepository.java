package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskChangeStatusRepository {

  List<RiskChangeStatusDTO> onSearch(RiskChangeStatusDTO riskChangeStatusDTO, int start,
      int maxResult, String sortType, String sortField);
}
