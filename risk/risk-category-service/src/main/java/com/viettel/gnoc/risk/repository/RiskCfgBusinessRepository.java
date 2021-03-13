package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskCfgBusinessRepository {

  List<RiskCfgBusinessDTO> onSearch(RiskCfgBusinessDTO riskCfgBusinessDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  ResultInSideDto deleteListRiskCfgBusiness(Long id);

  ResultInSideDto insertRiskCfgBusiness(RiskCfgBusinessDTO riskCfgBusinessDTO);

  List<RiskCfgBusinessDTO> getListCfgByRiskChangeStatusId(Long riskChangeStatusId);
}
