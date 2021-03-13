package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.risk.dto.RiskChangeStatusRoleDTO;
import com.viettel.gnoc.risk.model.RiskChangeStatusRoleEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskChangeStatusRoleRepositoryImpl extends BaseRepository implements
    RiskChangeStatusRoleRepository {

  @Override
  public List<RiskChangeStatusRoleDTO> onSearch(RiskChangeStatusRoleDTO riskChangeStatusRoleDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(RiskChangeStatusRoleEntity.class, riskChangeStatusRoleDTO, rowStart,
        maxRow, sortType, sortFieldList);
  }

  @Override
  public ResultInSideDto deleteListRiskChangeStatusRole(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<RiskChangeStatusRoleEntity> listEntity = findByMultilParam(
        RiskChangeStatusRoleEntity.class, "riskChangeStatusId", id);
    if (listEntity != null && listEntity.size() > 0) {
      for (RiskChangeStatusRoleEntity entity : listEntity) {
        getEntityManager().remove(entity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertRiskChangeStatusRole(
      RiskChangeStatusRoleDTO riskChangeStatusRoleDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(riskChangeStatusRoleDTO.toEntity());
    return resultInSideDTO;
  }

  @Override
  public List<RiskChangeStatusRoleDTO> getListRoleByRiskChangeStatusId(Long riskChangeStatusId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_CHANGE_STATUS_ROLE,
            "get-List-Role-By-Risk-Change-Status-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("categoryCode", Constants.CATEGORY.RISK_CHANGE_STATUS_ROLE);
    parameters.put("riskChangeStatusId", riskChangeStatusId);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(RiskChangeStatusRoleDTO.class));
  }
}
