package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.risk.dto.RiskHistoryDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskHistoryRepositoryImpl extends BaseRepository implements RiskHistoryRepository {

  @Override
  public ResultInSideDto insertRiskHistory(RiskHistoryDTO riskHistoryDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(riskHistoryDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public Datatable getListRiskHistoryByRiskId(RiskHistoryDTO riskHistoryDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_HISTORY, "get-List-Risk-History-By-Risk-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", riskHistoryDTO.getOffset());
    parameters.put("categoryCode", Constants.CATEGORY.RISK_STATUS);
    parameters.put("riskId", riskHistoryDTO.getRiskId());
    return getListDataTableBySqlQuery(sql, parameters, riskHistoryDTO.getPage(),
        riskHistoryDTO.getPageSize(), RiskHistoryDTO.class, riskHistoryDTO.getSortName(),
        riskHistoryDTO.getSortType());
  }

  @Override
  public RiskHistoryDTO findRiskHistoryByIdFromWeb(RiskHistoryDTO riskHistoryDTO) {
    String sql;
    if(riskHistoryDTO.getLanguage().equals("en_US")){
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_HISTORY, "find-Risk-History-By-Id-From-Web-En");
    }
    else {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_HISTORY, "find-Risk-History-By-Id-From-Web-Vi");
    }
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("riskHisId", riskHistoryDTO.getRiskHisId());
    parameters.put("bussiness", riskHistoryDTO.getBussiness());
    parameters.put("system", riskHistoryDTO.getSystem());
    List<RiskHistoryDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(RiskHistoryDTO.class));
    if (list != null && !list.isEmpty()) {
      RiskHistoryDTO dto = list.get(0);
      return dto;
    }
    return null;
  }
}
