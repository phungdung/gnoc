package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author TungPV
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class UnitRepositoryImpl extends BaseRepository implements UnitRepository {

  @Override
  public List<UnitDTO> getListUnitDTO(UnitDTO unitDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchEntity(UnitEntity.class, unitDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<UnitDTO> getListUnitByCondition(List<ConditionBean> conditionBeans, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new UnitEntity(), conditionBeans, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public UnitDTO findUnitById(Long id) {
    if (id != null && id > 0) {
      UnitEntity unitEntity = getEntityManager().find(UnitEntity.class, id);
      if (unitEntity != null) {
        return unitEntity.toDTO();
      }
    }
    return null;
  }

  @Override
  public List<UnitDTO> getUnitByUnitDTO(UnitDTO unitDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
        "get-Unit-By-Unit-DTO");
    Map<String, Object> params = new HashMap<>();
    if (unitDTO.getUnitId() != null) {
      sql += " AND UNIT_ID = :unitId ";
      params.put("unitId", unitDTO.getUnitId());
    }
    if (StringUtils.isNotNullOrEmpty(unitDTO.getUnitCode())) {
      sql += " AND lower(UNIT_CODE) = :unitCode ";
      params.put("unitCode", unitDTO.getUnitCode().trim().toLowerCase());
    }
    if (unitDTO.getStatus() != null) {
      sql += " AND STATUS = :status ";
      params.put("status", unitDTO.getStatus());
    }

    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(UnitDTO.class));
  }

  @Override
  public List<UnitDTO> getListUnitByCodeOrName(UnitDTO unitDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-Unit-By-Code-Or-Name-DTO");
    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(unitDTO.getSearchAll())) {
      StringBuilder sqlSearchAll = new StringBuilder(
          " AND (lower(ut.unit_code) LIKE :searchAll ESCAPE '\\' ");
      sqlSearchAll.append(" OR lower(ut.UNIT_NAME) LIKE :searchAll ESCAPE '\\' ) ");
      sqlQuery += sqlSearchAll.toString();
      params.put("searchAll", StringUtils.convertLowerParamContains(unitDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(unitDTO.getUnitCode())) {
      sqlQuery += " AND LOWER(ut.unit_code) LIKE :unitCode ESCAPE '\\' ";
      params.put("unitCode", StringUtils.convertLowerParamContains(
          unitDTO.getUnitCode()));
    }
    if (StringUtils.isNotNullOrEmpty(unitDTO.getUnitName())) {
      sqlQuery += " AND LOWER(ut.UNIT_NAME) LIKE :unitName ESCAPE '\\' ";
      params.put("unitName", StringUtils.convertLowerParamContains
          (unitDTO.getUnitName()));
    }
    List<UnitDTO> listUnit = getNamedParameterJdbcTemplate()
        .query(sqlQuery, params, BeanPropertyRowMapper.newInstance(UnitDTO.class));
    return listUnit.isEmpty() ? null : listUnit;
  }

  @Override
  public List<UnitDTO> getListUnit(UnitDTO unitDTO) {
    Map<String, Object> params = new HashMap<>();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select ut.unit_id unitId, ut.parent_unit_id, ut.unit_code unitCode,ut.LOCATION_ID locationId,ut.unit_name unitTrueName,"
            + " case when ut.unit_code is null then ''"
            + " else TO_CHAR(ut.unit_code || ' (' || ut.unit_name || ')') end as unitName, "
            + " case when parentUt.unit_code is null then ''"
            + " else TO_CHAR(parentUt.unit_code || ' (' || parentUt.unit_name || ')') end as parentUnitName "
            + " from common_gnoc.unit ut "
            + " left join common_gnoc.unit parentUt on ut.parent_unit_id = parentUt.unit_id"
            + " where ut.status = 1 ");
    if (unitDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitCode())) {
        sql.append(" and lower(ut.unit_code) like lower(:unitcode) escape '\\' ");
        params.put("unitcode", StringUtils.convertLowerParamContains(unitDTO.getUnitCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitName())) {
        sql.append(" and lower(ut.unit_name) like lower(:unitname) escape '\\'");
        params.put("unitname", StringUtils.convertLowerParamContains(unitDTO.getUnitName()));

      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getLocationId())) {
        sql.append(" and ut.LOCATION_ID =:locationId ");
        params.put("locationId", unitDTO.getLocationId());
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getRoleTypeName())) {
        sql.append(
            " and ut.ROLE_TYPE in (select ITEM_ID from COMMON_GNOC.CAT_ITEM where ITEM_CODE =:roletypename) ");
        params.put("roletypename", (unitDTO.getRoleTypeName()));
      }
      if (unitDTO.getLstUnitIds() != null && unitDTO.getLstUnitIds().size() > 0) {
        sql.append(" AND ut.UNIT_ID      IN "
            + "  (SELECT DISTINCT T1.UNIT_ID "
            + "  FROM COMMON_GNOC.UNIT T1 "
            + "    START WITH T1.UNIT_ID     IN (:lstUnitLogin) "
            + "    CONNECT BY prior T1.UNIT_ID=T1.PARENT_UNIT_ID "
            + "  ) ");
        params.put("lstUnitLogin", (unitDTO.getLstUnitIds()));
      }
    }
    sql.append(" order by ut.unit_code ");

    return getNamedParameterJdbcTemplate()
        .query(sql.toString(), params, BeanPropertyRowMapper
            .newInstance(UnitDTO.class));
  }

  @Override
  public UnitDTO getUnitByUnitCode(String unitCode) {
    if (StringUtils.isNotNullOrEmpty(unitCode)) {
      String sql = "SELECT u.* FROM COMMON_GNOC.UNIT u WHERE LOWER(u.UNIT_CODE) = :unitCode";
      Map<String, Object> params = new HashMap<>();
      params.put("unitCode", unitCode.toLowerCase());
      List<UnitDTO> list = getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper
          .newInstance(UnitDTO.class));
      if (list != null && list.size() > 0) {
        return list.get(0);
      }
    }
    return null;
  }

  @Override
  public List<UnitDTO> getListUnitByLevel(String level) {
    try {
      Map<String, Object> mapParam = new HashMap<>();
      StringBuilder sql = new StringBuilder();
      sql.append(
          " SELECT UNIT_ID unitId, UNIT_NAME unitName, UNIT_CODE unitCode, PARENT_UNIT_ID parentUnitId,");
      sql.append(
          " DESCRIPTION, STATUS, UNIT_TYPE unitType, UNIT_LEVEL unitLevel, LOCATION_ID locationId, IS_NOC isNoc,");
      sql.append(
          " TIME_ZONE timeZone, IS_COMMITTEE isCommittee, UPDATE_TIME updateTime, SMS_GATEWAY_ID smsGatewayId, IPCC_SERVICE_ID ipccServiceId,");
      sql.append(
          " NATION_CODE nationCode, NATION_ID nationId, MOBILE, ROLE_TYPE roleType, EMAIL email");
      sql.append(" FROM COMMON_GNOC.unit ");

      if (StringUtils.isNotNullOrEmpty(level)) {
        sql.append(" where level = :level"
            + " start with parent_unit_id is null connect by prior unit_id = parent_unit_id\n"
            + " order by level ");
        mapParam.put("level", level);
      } else {
        sql.append(" where level < 10"
            + " start with parent_unit_id is null connect by prior unit_id = parent_unit_id\n "
            + " order by level");
      }
      return getNamedParameterJdbcTemplate()
          .query(sql.toString(), mapParam, BeanPropertyRowMapper
              .newInstance(UnitDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<UnitDTO> getListUnitDTOByListUnitId(List<Long> listUnitId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
        "get-List-Unit-DTO-By-List-Unit-Id");
    Map<String, Object> params = new HashMap<>();
    if (listUnitId != null && !listUnitId.isEmpty()) {
      sql += " AND u.UNIT_ID IN (:listUnitId)";
      params.put("listUnitId", listUnitId);
    }
    return getNamedParameterJdbcTemplate().query(sql, params,
        BeanPropertyRowMapper.newInstance(UnitDTO.class));
  }

  @Override
  public UnitDTO getUnitDTOByUnitCode(String unitCode) {
    UnitDTO unitDTO = null;
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-UnitDTO-By-UnitCode");
    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(unitCode)) {
      sql += " AND UNIT_CODE = :unitCode";
      params.put("unitCode", unitCode);
    }
    List<UnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(UnitDTO.class));
    if (list != null && !list.isEmpty()) {
      unitDTO = list.get(0);
    }
    return unitDTO;
  }

  @Override
  public List<UnitDTO> getListUnit() {
    Map<String, Object> params = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-list-unit-dto"));
    sql.append(" order by ut.unit_code ");
    return getNamedParameterJdbcTemplate()
        .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(UnitDTO.class));
  }

  @Override
  public List<UnitDTO> getListUnitChildren(Long unitId) {
    if (unitId != null) {
      String sql = "SELECT u.unit_id unitId "
          + "FROM common_gnoc.unit u "
          + "WHERE LEVEL                  < 50 "
          + "  START WITH u.unit_id       = :unitId "
          + "  CONNECT BY PRIOR u.unit_id = u.parent_unit_id";
      Map<String, Object> params = new HashMap<>();
      params.put("unitId", unitId);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(UnitDTO.class));
    }
    return new ArrayList<>();
  }

  @Override
  public List<com.viettel.gnoc.ws.dto.UnitDTO> getUnitDTO(String fromDate, String toDate) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-list-unit-dto-by-fromDate-toDate");
    Map<String, Object> params = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(fromDate)) {
      sql += " AND u.UPDATE_TIME >= to_date(:fromDate,'dd/MM/yyyy HH24:MI:ss') ";
      params.put("fromDate", fromDate);
    }
    if (!StringUtils.isStringNullOrEmpty(toDate)) {
      sql += " AND u.UPDATE_TIME <= to_date(:toDate,'dd/MM/yyyy HH24:MI:ss') ";
      params.put("toDate", toDate);
    }
    return getNamedParameterJdbcTemplate().query(sql, params,
        BeanPropertyRowMapper.newInstance(com.viettel.gnoc.ws.dto.UnitDTO.class));
  }

  @Override
  public List<com.viettel.gnoc.ws.dto.UnitDTO> getUnit(com.viettel.gnoc.ws.dto.UnitDTO unitDTO,
      int rowStart, int maxRow) {
    List<com.viettel.gnoc.ws.dto.UnitDTO> list = new ArrayList<>();
    try {
      Map<String, Object> parameters = new HashMap<>();
      String sql = " SELECT   ut.unit_id unitId,"
          + "         ut.parent_unit_id parentUnitId,"
          + "         ut.unit_name unitName,"
          + "         ut.unit_code unitCode,"
          + "         parentut.unit_code parentUnitCode,"
          + "         parentut.unit_name parentUnitName"
          + "  FROM       common_gnoc.unit ut"
          + "         LEFT JOIN"
          + "             common_gnoc.unit parentut"
          + "         ON ut.parent_unit_id = parentut.unit_id"
          + " WHERE   1 = 1 ";
      if (unitDTO != null) {
        if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitCode())) {
          sql += " and lower(ut.unit_code) like :unitCode escape '\\'";
          parameters.put("unitCode", StringUtils.convertLowerParamContains(unitDTO.getUnitCode()));
        }
        if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitName())) {
          sql += " and lower(ut.unit_name) like :unitName escape '\\'";
          parameters.put("unitName", StringUtils.convertLowerParamContains(unitDTO.getUnitName()));
        }
        if (!StringUtils.isStringNullOrEmpty(unitDTO.getUserUnitId())) {
          sql += " AND ut.unit_id IN (    SELECT   a.unit_id"
              + "                                  FROM   common_gnoc.unit a"
              + "                                 WHERE   LEVEL < 50"
              + "                            START WITH   a.unit_id = :unitId"
              + "                            CONNECT BY   PRIOR a.unit_id = a.parent_unit_id) ";
          parameters.put("unitId", unitDTO.getUserUnitId());
        }

      }
      list = getNamedParameterJdbcTemplate().query(sql, parameters,
          BeanPropertyRowMapper.newInstance(com.viettel.gnoc.ws.dto.UnitDTO.class));
      if (list != null && list.size() >= rowStart) {
        List<com.viettel.gnoc.ws.dto.UnitDTO> lstResult = new ArrayList<>();
        if (list.size() < maxRow) {
          for (int i = rowStart; i < list.size(); i++) {
            lstResult.add(list.get(i));
          }
        } else {
          maxRow = maxRow + rowStart;
          for (int i = rowStart; i < maxRow; i++) {
            lstResult.add(list.get(i));
          }
        }
        return lstResult;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }
}
