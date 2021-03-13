package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.risk.dto.RiskSystemHistoryDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskSystemHistoryRepositoryImpl extends BaseRepository implements
    RiskSystemHistoryRepository {

  @Override
  public ResultInSideDto insertRiskSystemHistory(RiskSystemHistoryDTO riskSystemHistoryDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(riskSystemHistoryDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public Datatable getListRiskSystemHistoryBySystemId(RiskSystemHistoryDTO riskSystemHistoryDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_SYSTEM_HISTORY,
        "get-List-Risk-System-History-By-System-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", riskSystemHistoryDTO.getOffset());
    parameters.put("systemId", riskSystemHistoryDTO.getSystemId());
    return getListDataTableBySqlQuery(sql, parameters, riskSystemHistoryDTO.getPage(),
        riskSystemHistoryDTO.getPageSize(), RiskSystemHistoryDTO.class,
        riskSystemHistoryDTO.getSortName(), riskSystemHistoryDTO.getSortType());
  }

  @Override
  public List<RiskSystemHistoryDTO> getListHistoryBySystemId(Long systemId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_SYSTEM_HISTORY,
        "get-List-History-By-System-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemId", systemId);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(RiskSystemHistoryDTO.class));
  }
}
