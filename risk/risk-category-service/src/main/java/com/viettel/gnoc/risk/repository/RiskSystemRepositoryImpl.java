package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import com.viettel.gnoc.risk.model.RiskSystemEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskSystemRepositoryImpl extends BaseRepository implements RiskSystemRepository {

  @Override
  public Datatable getDataRiskSystemSearchWeb(RiskSystemDTO riskSystemDTO) {
    BaseDto baseDto = sqlDataRiskSystemSearchWeb(riskSystemDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        riskSystemDTO.getPage(), riskSystemDTO.getPageSize(), RiskSystemDTO.class,
        riskSystemDTO.getSortName(), riskSystemDTO.getSortType());
    return datatable;
  }

  @Override
  public List<RiskSystemDTO> getListRiskSystem(RiskSystemDTO riskSystemDTO) {
    BaseDto baseDto = sqlDataRiskSystemSearchWeb(riskSystemDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(RiskSystemDTO.class));
  }

  @Override
  public ResultInSideDto insertOrUpdateRiskSystem(RiskSystemDTO riskSystemDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    RiskSystemEntity riskSystemEntity = getEntityManager().merge(riskSystemDTO.toEntity());
    resultInSideDto.setId(riskSystemEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteRiskSystem(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    RiskSystemEntity entity = getEntityManager().find(RiskSystemEntity.class, id);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }

  @Override
  public RiskSystemDTO findRiskSystemByIdFromWeb(Long id, Double offsetFromUser) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_SYSTEM, "find-Risk-System-By-Id-From-Web");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", offsetFromUser);
    parameters.put("categoryCode", Constants.CATEGORY.RISK_SYSTEM_PRIORITY);
    parameters.put("id", id);
    List<RiskSystemDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(RiskSystemDTO.class));
    if (list != null && !list.isEmpty()) {
      RiskSystemDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  @Override
  public List<RiskSystemDTO> getListRiskSystemExport(RiskSystemDTO riskSystemDTO) {
    BaseDto baseDto = sqlDataRiskSystemSearchWeb(riskSystemDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(RiskSystemDTO.class));
  }

  @Override
  public RiskSystemDTO checkRiskSystemExit(RiskSystemDTO riskSystemDTO) {
    List<RiskSystemEntity> dataEntity = (List<RiskSystemEntity>) findByMultilParam(
        RiskSystemEntity.class, "systemCode", riskSystemDTO.getSystemCode().toUpperCase());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public RiskSystemDTO getRiskSystemOldById(Long id) {
    RiskSystemEntity entity = getEntityManager().find(RiskSystemEntity.class, id);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  private BaseDto sqlDataRiskSystemSearchWeb(RiskSystemDTO riskSystemDTO) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_SYSTEM, "get-List-Risk-System");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("categoryCode", Constants.CATEGORY.RISK_SYSTEM_PRIORITY);
    if (riskSystemDTO != null) {
      //Tim kiem nhanh
      if (StringUtils.isNotNullOrEmpty(riskSystemDTO.getSearchAll())) {
        sql += " AND (LOWER(s.SYSTEM_CODE) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(s.SYSTEM_NAME) LIKE :searchAll ESCAPE '\\')";
        parameters
            .put("searchAll", StringUtils.convertLowerParamContains(riskSystemDTO.getSearchAll()));
      }
      if (StringUtils.isNotNullOrEmpty(riskSystemDTO.getSystemCode())) {
        sql += " AND UPPER(s.SYSTEM_CODE) LIKE :systemCode ESCAPE '\\'";
        parameters.put("systemCode",
            StringUtils.convertUpperParamContains(riskSystemDTO.getSystemCode()));
      }
      if (riskSystemDTO.getManageUnitId() != null) {
        sql += " AND sd.MANAGE_UNIT_ID = :manageUnitId";
        parameters.put("manageUnitId", riskSystemDTO.getManageUnitId());
      }
      if (StringUtils.isNotNullOrEmpty(riskSystemDTO.getSystemName())) {
        sql += " AND UPPER(s.SYSTEM_NAME) LIKE :systemName ESCAPE '\\'";
        parameters.put("systemName",
            StringUtils.convertUpperParamContains(riskSystemDTO.getSystemName()));
      }
      if (riskSystemDTO.getManageUserId() != null) {
        sql += " AND sd.MANAGE_USER_ID = :manageUserId";
        parameters.put("manageUserId", riskSystemDTO.getManageUserId());
      }
      if (riskSystemDTO.getSystemPriority() != null) {
        sql += " AND s.SYSTEM_PRIORITY = :systemPriority";
        parameters.put("systemPriority", riskSystemDTO.getSystemPriority());
      }
      if (riskSystemDTO.getCountryId() != null) {
        sql += " AND s.COUNTRY_ID = :countryId";
        parameters.put("countryId", riskSystemDTO.getCountryId());
      }
    }
    sql += " ORDER BY s.SYSTEM_NAME ASC";

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
