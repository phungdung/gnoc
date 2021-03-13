package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.risk.dto.RiskRelationDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskRelationRepositoryImpl extends BaseRepository implements RiskRelationRepository {

  @Override
  public ResultInSideDto insertRiskRelation(RiskRelationDTO riskRelationDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(riskRelationDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public Datatable getListRiskRelationByRiskId(RiskRelationDTO riskRelationDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_RELATION, "get-List-Risk-Relation-By-Risk-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", riskRelationDTO.getOffset());
    parameters.put("riskId", riskRelationDTO.getRiskId());
    return getListDataTableBySqlQuery(sql, parameters, riskRelationDTO.getPage(),
        riskRelationDTO.getPageSize(), RiskRelationDTO.class, riskRelationDTO.getSortName(),
        riskRelationDTO.getSortType());
  }

  @Override
  public List<RiskRelationDTO> getRiskRelationByRiskId(RiskRelationDTO riskRelationDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_RELATION, "get-List-Risk-Relation-By-Risk-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", riskRelationDTO.getOffset());
    parameters.put("riskId", riskRelationDTO.getRiskId());
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(RiskRelationDTO.class));
  }
}
