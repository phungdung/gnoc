package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.risk.dto.RiskSystemDetailDTO;
import com.viettel.gnoc.risk.model.RiskSystemDetailEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskSystemDetailRepositoryImpl extends BaseRepository implements
    RiskSystemDetailRepository {

  @Override
  public ResultInSideDto deleteRiskSystemDetailBySystemId(Long systemId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<RiskSystemDetailEntity> listEntity = findByMultilParam(RiskSystemDetailEntity.class,
        "systemId", systemId);
    if (listEntity != null && listEntity.size() > 0) {
      for (RiskSystemDetailEntity entity : listEntity) {
        getEntityManager().remove(entity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertRiskSystemDetail(RiskSystemDetailDTO detailDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(detailDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public List<RiskSystemDetailDTO> getListRiskSystemDetailBySystemId(Long systemId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_SYSTEM_DETAIL,
            "get-List-Risk-System-Detail-By-System-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemId", systemId);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(RiskSystemDetailDTO.class));
  }

  @Override
  public List<RiskSystemDetailDTO> getRiskSystemDetailBySystemIdAndManageUserId(String systemId,
      String manageUserId) {
    List<RiskSystemDetailDTO> list;
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_SYSTEM_DETAIL,
            "get-List-Risk-System_Detail-By_System_Id-And-Manage-User-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemId", systemId);
    parameters.put("manageUserId", manageUserId);
    try {
      list = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(RiskSystemDetailDTO.class));
    } catch (Exception e) {
      list = new ArrayList<>();
      return list;
    }
    return list;
  }

  @Override
  public ResultInSideDto deleteRiskSystemDetailBySystemIdAndManageUserId(Long systemId,
      Long manageUserId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<RiskSystemDetailEntity> listEntity = findByMultilParam(RiskSystemDetailEntity.class,
        "systemId", systemId, "manageUserId", manageUserId);
    if (listEntity != null && listEntity.size() > 0) {
      for (RiskSystemDetailEntity entity : listEntity) {
        getEntityManager().remove(entity);
      }
    }
    return resultInSideDto;
  }
}
