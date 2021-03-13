package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import com.viettel.gnoc.sr.model.SRRoleActionsEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRRoleActionRepositoryImpl extends BaseRepository implements SRRoleActionRepository {

  @Override
  public Datatable getListSRRoleActionPage(SRRoleActionDTO srRoleActionDTO) {
    BaseDto baseDto = sqlSearch(srRoleActionDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        srRoleActionDTO.getPage(), srRoleActionDTO.getPageSize(),
        SRRoleActionDTO.class,
        srRoleActionDTO.getSortName(), srRoleActionDTO.getSortType());
  }

  @Override
  public List<SRRoleActionDTO> getListSRRoleActionDTO(SRRoleActionDTO srRoleActionDTO) {
    String sqlQuery =
        SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_ROLE_ACTIONS, "getListSRRoleActionDTO");
    Map<String, Object> parameters = new HashMap<>();
    if (srRoleActionDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getCountry())) {
        sqlQuery += " AND B.COUNTRY= :country ";
        parameters.put("country", srRoleActionDTO.getCountry());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getCurrentStatus())) {
        sqlQuery += " AND A.CURRENT_STATUS= :currentStatus ";
        parameters.put("currentStatus", srRoleActionDTO.getCurrentStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getNextStatus())) {
        sqlQuery += " AND instr(',' || NEXT_STATUS || ',',',' || :nextStatus || ',') > 0 ";
        parameters.put("nextStatus", srRoleActionDTO.getNextStatus());
      }

      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getRoleType())) {
        sqlQuery += " AND A.ROLE_TYPE= :roleType ";
        parameters.put("roleType", srRoleActionDTO.getRoleType());
      }

      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getFlowId())) {
        sqlQuery += " AND A.FLOW_ID= :flowId ";
        parameters.put("flowId", srRoleActionDTO.getFlowId());
      }

      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getGroupRole())) {
        String[] arr = srRoleActionDTO.getGroupRole().split(",");
        for (String str : arr) {
          sqlQuery += " AND INSTR(',' || A.GROUP_ROLE || ',' , ',' || :str || ',') > 0 ";
          parameters.put("str", str);
        }
      }
    }
    sqlQuery += " ORDER BY B.COUNTRY, A.CURRENT_STATUS ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRRoleActionDTO.class));
  }

  @Override
  public ResultInSideDto add(SRRoleActionDTO srRoleActionDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRRoleActionsEntity srRoleActionsEntity = getEntityManager().merge(srRoleActionDTO.toEntity());
    resultInSideDto.setId(srRoleActionsEntity.getRoleActionId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(SRRoleActionDTO srRoleActionDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRRoleActionsEntity srRoleActionsEntity = srRoleActionDTO.toEntity();
    if (srRoleActionsEntity.getRoleActionId() != null) {
      getEntityManager().merge(srRoleActionsEntity);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteByRoleActionsId(Long roleActionId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRRoleActionsEntity srRoleActionsEntity = getEntityManager()
        .find(SRRoleActionsEntity.class, roleActionId);
    getEntityManager().remove(srRoleActionsEntity);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteRoleAction(SRRoleActionDTO srRoleActionDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRRoleActionsEntity srRoleActionsEntity = getEntityManager()
        .find(SRRoleActionsEntity.class, srRoleActionDTO.getRoleActionId());
    getEntityManager().remove(srRoleActionsEntity);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteRoleActionByFlowID(Long flowID) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    deleteByMultilParam(SRRoleActionsEntity.class, "flowId", flowID);
    return resultInSideDto;
  }

  @Override
  public SRRoleActionDTO getDetail(Long roleActionId) {
    SRRoleActionsEntity srRoleActionsEntity = getEntityManager()
        .find(SRRoleActionsEntity.class, roleActionId);
    SRRoleActionDTO srRoleActionDTO = srRoleActionsEntity.toDTO();
    return srRoleActionDTO;
  }

  @Override
  public List<SRRoleActionDTO> getListRoleActionsByFlowExecuteId(Long flowId) {
    String sqlQuery =
        SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_ROLE_ACTIONS, "role-actions-by-flowId-list");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(flowId)) {
      sqlQuery += " AND A.FLOW_ID= :flowId ";
      parameters.put("flowId", flowId);
    }
    sqlQuery += " ORDER BY B.COUNTRY, A.CURRENT_STATUS ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRRoleActionDTO.class));
  }

  @Override
  public ResultInSideDto insertOrUpdate(SRRoleActionDTO srRoleActionDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    SRRoleActionsEntity srRoleActionsEntity = getEntityManager().merge(srRoleActionDTO.toEntity());
    resultInSideDTO.setId(srRoleActionsEntity.getRoleActionId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdateList(List<SRRoleActionDTO> srRoleActionsDTOList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (SRRoleActionDTO dto : srRoleActionsDTOList) {
      resultInSideDto = insertOrUpdate(dto);
    }
    return resultInSideDto;
  }

  @Override
  public List<SRRoleActionDTO> getComboBoxStatus() {
    String sqlQuery = " select CONFIG_CODE currentStatus , CONFIG_NAME currentStatusName from OPEN_PM.SR_CONFIG T2 where CONFIG_GROUP='STATUS' and STATUS = 'A' ORDER BY CONFIG_CODE ASC ";
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRRoleActionDTO.class));
  }

  @Override
  public List<SRRoleActionDTO> getComboBoxRoleType() {
    String sqlQuery = " select CONFIG_CODE roleType, CONFIG_NAME roleTypeName from OPEN_PM.SR_CONFIG T1 where CONFIG_GROUP = 'ROLE_ACTION_TYPE' and STATUS = 'A' ORDER BY CONFIG_CODE ASC ";
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRRoleActionDTO.class));

  }

  @Override
  public List<SRRoleActionDTO> getComboBoxActions(String roleType) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_ROLE_ACTIONS, "sr-roleActions-action-list");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(roleType)) {
      sqlQuery += " AND PARENT_CODE= :roleType ";
      parameters.put("roleType", roleType);
    }
    sqlQuery += " ORDER BY CONFIG_NAME ASC ";
    parameters.put("p_leeLocale", I18n.getLocale());
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRRoleActionDTO.class));
  }

  @Override
  public List<SRRoleActionDTO> getComboBoxGroupRole() {
    String sqlQuery = " select CONFIG_NAME groupRoleName, CONFIG_CODE groupRole from OPEN_PM.SR_CONFIG where CONFIG_GROUP = 'GROUP_ROLE' and STATUS = 'A' ORDER BY CONFIG_NAME ASC ";
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRRoleActionDTO.class));
  }

  public BaseDto sqlSearch(SRRoleActionDTO srRoleActionDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = null;
    Map<String, Object> parameters = new HashMap<>();
    if (srRoleActionDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getCountry())) {
        sqlQuery += " AND T1.COUNTRY= :country ";
        parameters.put("country", srRoleActionDTO.getCountry());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getCurrentStatus())) {
        sqlQuery += " AND T1.CURRENT_STATUS= :currentStatus ";
        parameters.put("currentStatus", srRoleActionDTO.getCurrentStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getNextStatus())) {
        sqlQuery += " AND T1.NEXT_STATUS= :nextStatus ";
        parameters.put("nextStatus", srRoleActionDTO.getNextStatus());
      }

      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getRoleType())) {
        sqlQuery += " AND T1.ROLE_TYPE= :roleType ";
        parameters.put("roleType", srRoleActionDTO.getRoleType());
      }

      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getFlowId())) {
        sqlQuery += " AND T1.FLOW_ID= :flowId ";
        parameters.put("flowId", srRoleActionDTO.getFlowId());
      }

      if (!StringUtils.isStringNullOrEmpty(srRoleActionDTO.getGroupRole())) {
        sqlQuery += " AND T1.GROUP_ROLE= :groupRole ";
        parameters.put("groupRole", srRoleActionDTO.getGroupRole());
      }
    }
    sqlQuery += " ORDER BY B.COUNTRY, A.CURRENT_STATUS ";
    parameters.put("p_leeLocale", I18n.getLocale());
    sqlQuery += " GROUP BY T1.FLOW_ID, T1.FLOW_NAME, T1.FLOW_DESCRIPTION, T1.COUNTRY ";
    sqlQuery += " ORDER BY T1.FLOW_NAME ASC, T1.COUNTRY DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
