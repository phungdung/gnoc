package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskTypeDetailRepository {

  List<RiskTypeDetailDTO> getListRiskTypeDetail(RiskTypeDetailDTO riskTypeDetailDTO);

  ResultInSideDto add(RiskTypeDetailDTO riskTypeDetailDTO);

  ResultInSideDto deleteListRiskTypeDetail(Long riskTypeId);
}
