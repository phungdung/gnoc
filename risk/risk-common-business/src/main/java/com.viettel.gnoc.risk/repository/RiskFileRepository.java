package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskFileDTO;
import com.viettel.gnoc.risk.model.RiskFileEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskFileRepository {

  ResultInSideDto insertRiskFile(RiskFileDTO riskFileDTO);

  List<RiskFileEntity> getListRiskFileByRiskId(Long riskId);

  ResultInSideDto deleteRiskFile(Long riskFileId);
}
