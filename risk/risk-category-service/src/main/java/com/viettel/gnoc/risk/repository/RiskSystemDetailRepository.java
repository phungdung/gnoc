package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskSystemDetailDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskSystemDetailRepository {

  ResultInSideDto deleteRiskSystemDetailBySystemId(Long systemId);

  ResultInSideDto insertRiskSystemDetail(RiskSystemDetailDTO detailDTO);

  List<RiskSystemDetailDTO> getListRiskSystemDetailBySystemId(Long systemId);

  List<RiskSystemDetailDTO> getRiskSystemDetailBySystemIdAndManageUserId(String systemId, String manageUserId);

  ResultInSideDto deleteRiskSystemDetailBySystemIdAndManageUserId(Long systemId, Long manageUserId);
}
