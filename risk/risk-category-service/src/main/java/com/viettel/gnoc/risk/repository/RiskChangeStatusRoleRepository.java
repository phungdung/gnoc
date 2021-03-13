package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskChangeStatusRoleDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskChangeStatusRoleRepository {

  List<RiskChangeStatusRoleDTO> onSearch(RiskChangeStatusRoleDTO riskChangeStatusRoleDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  ResultInSideDto deleteListRiskChangeStatusRole(Long id);

  ResultInSideDto insertRiskChangeStatusRole(RiskChangeStatusRoleDTO riskChangeStatusRoleDTO);

  List<RiskChangeStatusRoleDTO> getListRoleByRiskChangeStatusId(Long riskChangeStatusId);
}
