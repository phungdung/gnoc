package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskRelationDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskRelationRepository {

  ResultInSideDto insertRiskRelation(RiskRelationDTO riskRelationDTO);

  Datatable getListRiskRelationByRiskId(RiskRelationDTO riskRelationDTO);

  List<RiskRelationDTO> getRiskRelationByRiskId(RiskRelationDTO riskRelationDTO);
}
