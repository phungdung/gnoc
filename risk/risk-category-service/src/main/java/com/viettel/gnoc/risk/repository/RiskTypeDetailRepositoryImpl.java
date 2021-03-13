package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
import com.viettel.gnoc.risk.model.RiskTypeDetailEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskTypeDetailRepositoryImpl extends BaseRepository implements
    RiskTypeDetailRepository {

  @Override
  public List<RiskTypeDetailDTO> getListRiskTypeDetail(RiskTypeDetailDTO riskTypeDetailDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_TYPE_DETAIL, "get-List-Risk-Type-Detail");
    Map<String, Object> parameters = new HashMap<>();
    if (riskTypeDetailDTO != null) {
      if (riskTypeDetailDTO.getRiskTypeId() != null) {
        sql += " and d.RISK_TYPE_ID = :riskTypeId";
        parameters.put("riskTypeId", riskTypeDetailDTO.getRiskTypeId());
      }
      if (riskTypeDetailDTO.getPriorityId() != null) {
        sql += " and d.PRIORITY_ID = :priorityId";
        parameters.put("priorityId", riskTypeDetailDTO.getPriorityId());
      }
    }
    sql += " order by d.PRIORITY_ID";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(RiskTypeDetailDTO.class));
  }

  @Override
  public ResultInSideDto add(RiskTypeDetailDTO riskTypeDetailDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(riskTypeDetailDTO.toEntity());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteListRiskTypeDetail(Long riskTypeId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<RiskTypeDetailEntity> listDetailEntity = findByMultilParam(RiskTypeDetailEntity.class,
        "riskTypeId", riskTypeId);
    if (listDetailEntity != null && listDetailEntity.size() > 0) {
      for (RiskTypeDetailEntity detailEntity : listDetailEntity) {
        getEntityManager().remove(detailEntity);
      }
    }
    return resultInSideDto;
  }
}
