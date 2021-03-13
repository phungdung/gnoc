package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import com.viettel.gnoc.sr.model.SRFlowExecuteEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRFlowExecuteRepositoryImpl extends BaseRepository implements SRFlowExecuteRepository {

  @Override
  public Datatable getListSRFlowExecute(SRFlowExecuteDTO srFlowExecuteDTO) {
    BaseDto baseDto = sqlSearch(srFlowExecuteDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        srFlowExecuteDTO.getPage(), srFlowExecuteDTO.getPageSize(),
        SRFlowExecuteDTO.class,
        srFlowExecuteDTO.getSortName(), srFlowExecuteDTO.getSortType());
  }

  @Override
  public List<SRFlowExecuteDTO> onSearchExport(SRFlowExecuteDTO srFlowExecuteDTO) {
    BaseDto baseDto = sqlSearch(srFlowExecuteDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(SRFlowExecuteDTO.class));
  }

  @Override
  public List<SRFlowExecuteDTO> getListSRFlowExecuteCBB(SRFlowExecuteDTO srFlowExecuteDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_FLOW_EXECUTE, "getListSRFlowExecuteCBB");
    Map<String, Object> parameters = new HashMap<>();
    if (srFlowExecuteDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srFlowExecuteDTO.getCountry())) {
        sqlQuery += " AND T1.COUNTRY= :country ";
        parameters.put("country", srFlowExecuteDTO.getCountry());
      }
    }
    parameters.put("p_leeLocale", I18n.getLocale());
    sqlQuery += " GROUP BY T1.FLOW_ID, T1.FLOW_NAME, T1.FLOW_DESCRIPTION, T1.COUNTRY, L.LOCATION_NAME ";
    sqlQuery += " having COUNT(T2.CURRENT_STATUS) > 0 ";
    sqlQuery += " ORDER BY T1.FLOW_NAME ASC, T1.COUNTRY DESC ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(SRFlowExecuteDTO.class));
  }

  @Override
  public ResultInSideDto insertOrUpdate(SRFlowExecuteDTO srFlowExecuteDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRFlowExecuteEntity srFlowExecuteEntity = getEntityManager().merge(srFlowExecuteDTO.toEntity());
    resultInSideDto.setId(srFlowExecuteEntity.getFlowId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long flowId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRFlowExecuteEntity srFlowExecuteEntity = getEntityManager()
        .find(SRFlowExecuteEntity.class, flowId);
    getEntityManager().remove(srFlowExecuteEntity);
    return resultInSideDto;
  }

  @Override
  public SRFlowExecuteDTO getDetail(Long flowId) {
    SRFlowExecuteEntity srFlowExecuteEntity = getEntityManager()
        .find(SRFlowExecuteEntity.class, flowId);
    SRFlowExecuteDTO srFlowExecuteDTO = srFlowExecuteEntity.toDTO();
    return srFlowExecuteDTO;
  }

  @Override
  public List<SRFlowExecuteDTO> isFlowUsingByCatalog(Long flowId) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = " SELECT SERVICE_CODE from OPEN_PM.SR_CATALOG where FLOW_EXECUTE = :flowId ";
    if (!StringUtils.isStringNullOrEmpty(flowId)) {
      sqlQuery += " AND FLOW_EXECUTE = :flowId ";
      parameters.put("flowId", flowId);
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRFlowExecuteDTO.class));
  }

  @Override
  public List<SRFlowExecuteDTO> isFlowUsingByCatalogTable(String listflowId) {
    String sqlQuery = "SELECT SERVICE_CODE from OPEN_PM.SR_CATALOG where FLOW_EXECUTE in (:listflowId)";
    Map<String, Object> params = new HashMap<>();
    params.put("listflowId", ConditionBeanUtil.parseInputListString(listflowId));
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, params, BeanPropertyRowMapper.newInstance(SRFlowExecuteDTO.class));
  }

  @Override
  public Boolean isDuplicateFlowName(String flowName) {
    List<SRFlowExecuteEntity> executeEntities = findByMultilParam(SRFlowExecuteEntity.class,
        "flowName", flowName);
    if (executeEntities.isEmpty()) {
      return false;
    }
    return true;
  }

  @Override
  public List<SRFlowExecuteDTO> getListSRFlowAlreadyExist(SRFlowExecuteDTO srFlowExecuteDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_FLOW_EXECUTE, "sr-flow-execute-already-exist");
    Map<String, Object> params = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(srFlowExecuteDTO.getFlowName())) {
      sqlQuery += " AND Srf.Flow_Name =:flowName escape '\\' ";
      params
          .put("flowName", srFlowExecuteDTO.getFlowName());
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, params, BeanPropertyRowMapper.newInstance(SRFlowExecuteDTO.class));
  }

  public BaseDto sqlSearch(SRFlowExecuteDTO srFlowExecuteDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_FLOW_EXECUTE, "sr-flow-execute-list");
    Map<String, Object> parameters = new HashMap<>();
    if (srFlowExecuteDTO != null) {
      if (StringUtils.isNotNullOrEmpty(srFlowExecuteDTO.getSearchAll())) {
        sqlQuery += " AND (lower(FLOW_NAME) LIKE :searchAll ESCAPE '\\')";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(srFlowExecuteDTO.getSearchAll()));
      }

      if (!StringUtils.isStringNullOrEmpty(srFlowExecuteDTO.getFlowName())) {
        sqlQuery += " AND LOWER(FLOW_NAME) LIKE :flowName escape '\\' ";
        parameters
            .put("flowName", StringUtils.convertLowerParamContains(srFlowExecuteDTO.getFlowName()));
      }

    }
    sqlQuery += "Order By TO_NUMBER(FLEX.flowIdOrder) Desc";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
