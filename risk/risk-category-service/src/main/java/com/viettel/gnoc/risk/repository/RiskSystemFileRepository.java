package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskSystemFileDTO;
import com.viettel.gnoc.risk.model.RiskSystemFileEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskSystemFileRepository {

  ResultInSideDto insertRiskSystemFile(RiskSystemFileDTO riskSystemFileDTO);

  List<RiskSystemFileEntity> getListRiskSystemFileBySystemId(Long systemId);

  ResultInSideDto deleteRiskSystemFile(Long riskSystemFileId);
}
