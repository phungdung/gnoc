package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskChangeStatusRepository {

  Datatable getDataRiskChangeStatusSearchWeb(RiskChangeStatusDTO riskChangeStatusDTO);

  List<RiskChangeStatusDTO> onSearch(RiskChangeStatusDTO riskChangeStatusDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  ResultInSideDto insertOrUpdateRiskChangeStatus(RiskChangeStatusDTO riskChangeStatusDTO);

  RiskChangeStatusDTO findRiskChangeStatusById(Long id);

  List<RiskChangeStatusDTO> getListRiskChangeStatusExport(RiskChangeStatusDTO riskChangeStatusDTO);

  ResultInSideDto deleteRiskChangeStatus(Long id);
}
