package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MrLocationDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.CatItemEntity;
import com.viettel.gnoc.commons.model.CatLocationEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class CatLocationRepositoryImpl extends BaseRepository implements CatLocationRepository {

  @Override
  public List<CatLocationDTO> getCatLocationByParentId(String parentId) {
    Map<String, String> mapParam = new HashMap<>();
    Map<String, String> mapType = new HashMap<>();
    StringBuilder sqlBuilder = new StringBuilder();
    if (StringUtils.isNotNullOrEmpty(parentId)) {
      sqlBuilder.append(" from CatLocationEntity where status = 1 and parentId = :parentId ");
      mapParam.put("parentId", parentId);
      mapType.put("parentId", "Long");
    } else {
      sqlBuilder.append(" from CatLocationEntity where status = 1 and parentId is null ");
    }
    List<CatLocationDTO> lst = searchBySql(sqlBuilder.toString(), mapParam, mapType);
    return lst;
  }

  @Override
  public List<CatLocationDTO> getListCatLocationDTO(CatLocationDTO catLocationDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(CatItemEntity.class, catLocationDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public List<CatLocationDTO> getCatLocationByLevel(String level) {
    Map<String, Object> mapParam = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(
        " SELECT LOCATION_ID locationId, LOCATION_CODE locationCode, LOCATION_NAME locationName, DESCRIPTION description, PARENT_ID parentId, ");
    sql.append(" PARENT_CODE parentCode, LOCATION_ADMIN_LEVEL locationAdminLevel, ");
    sql.append(
        " TERRAIN terrain, PRE_CODE_STATION preCodeStation, STATUS, PLACE, FEATURE_LOCATION featureLocation, LAST_UPDATE_TIME lastUpdateTime, ");
    sql.append(" NATION_ID nationId ");
    sql.append(" FROM COMMON_GNOC.cat_location ");
    if (StringUtils.isNotNullOrEmpty(level)) {
      sql.append(" where status = 1 and level = :level"
          + " start with parent_id is null connect by prior location_id = parent_id"
          + " order by level ");
      mapParam.put("level", level);
    } else {
      sql.append(" where status = 1 and level < 10"
          + " start with parent_id is null connect by prior location_id = parent_id "
          + " order by level");
    }
    return getNamedParameterJdbcTemplate()
        .query(sql.toString(), mapParam, BeanPropertyRowMapper
            .newInstance(CatLocationDTO.class));
  }

  @Override
  public CatLocationDTO getLocationByCode(String locationCode, String locationId,
      String nationCode) {
    Map<String, Object> params = new HashMap<>();
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT a.LOCATION_ID as locationId, a.location_code as locationCode, "
        + " SYS_CONNECT_BY_PATH (a.LOCATION_ID, '/')|| '/' as locationIdFull, "
        + " SYS_CONNECT_BY_PATH (a.location_name, ' / ')|| '/' as locationNameFull,"
        + " SYS_CONNECT_BY_PATH (a.location_code, ' / ')|| '/' as locationCodeFull, "
        + " LEVEL AS locationLevel FROM common_gnoc.cat_location a  WHERE LEVEL < 6 ");

    if (!StringUtils.isStringNullOrEmpty(locationCode)) {
      sql.append(" and a.location_code = :location_code ");
      params.put("location_code", locationCode.trim());
    }
    if (!StringUtils.isStringNullOrEmpty(locationId)) {
      sql.append(" and a.location_id = :location_id ");
      params.put("location_id", locationId.trim());
    }
    if (StringUtils.isStringNullOrEmpty(nationCode) || "VNM".equalsIgnoreCase(nationCode)) {
      sql.append(" and (a.NATION_CODE = 'VNM' or a.NATION_CODE is null)  ");
    } else if (!StringUtils.isStringNullOrEmpty(nationCode)) {
      sql.append(" and a.NATION_CODE = :nationCode ");
      params.put("nationCode", nationCode);
    }
    sql.append(" START WITH a.parent_id IS NULL CONNECT BY PRIOR a.location_id = a.parent_id ");

    List<CatLocationDTO> obj = getNamedParameterJdbcTemplate()
        .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(CatLocationDTO.class));

    if (obj != null && !obj.isEmpty()) {
      return obj.get(0);
    }
    return null;
  }

  public CatLocationDTO getNationByLocationId(Long locationId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
        "get-nation-by-location-id");
    Map<String, Object> param = new HashMap<>();
    param.put("locationId", locationId);

    List<CatLocationDTO> obj = getNamedParameterJdbcTemplate()
        .query(sql, param, BeanPropertyRowMapper.newInstance(CatLocationDTO.class));
    if (obj != null && obj.size() > 0) {
      return obj.get(0);
    }
    return null;
  }

  @Override
  public List<CatLocationDTO> getListLocationProvince() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-all-location-province-vn");
    Map<String, Object> param = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, param, BeanPropertyRowMapper.newInstance(CatLocationDTO.class));
  }

  @Override
  public List<ItemDataCRInside> getListLocationByLevelCBB(Object form, Long level, Long parentId) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-list-cat-location-by-level-cbb");
    if (level != null && level != 0L && level != 3) {
      sql += " and level = :level ";
      params.put("level", level);
    }
    if (level != null && level == 3) { //them dieu kien get tinh thanh
      sql += " and parent_id in (select LOCATION_ID from common_gnoc.cat_location where parent_id = :parentId) ";
      params.put("parentId", parentId);
    } else {
      if (parentId != null && parentId != 0L) {
        sql += " and parent_id = :parent_id ";
        params.put("parent_id", parentId);
      }
    }
    sql += " START WITH parent_id IS NULL CONNECT BY PRIOR location_id = parent_id  order by location_name ";

    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public List<CatLocationDTO> searchByConditionBean(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    log.debug("Request to searchByConditionBean: {}", lstCondition);
    return onSearchByConditionBean(new CatLocationEntity(), lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }

  public String getListLocation(String locationName, String locationCode, String keyword,
      Long level,
      Map<String, Object> parameters) {
    log.debug("Request to getSQLLocation: {}:" + locationName + locationCode, keyword, level);
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "getListLocation");

    if (!StringUtils.isStringNullOrEmpty(locationName)) {
      sql += " and lower(a.location_name) like :location_name escape '\\' ";
      parameters.put("location_name",
          StringUtils.convertLowerParamContains(locationName.trim()));
    }
    if (!StringUtils.isStringNullOrEmpty(locationCode)) {
      sql += " and lower(a.location_code) like :location_code '\\' ";
      parameters.put("location_code",
          StringUtils.convertLowerParamContains(locationCode.trim()));
    }
    if (!StringUtils.isStringNullOrEmpty(keyword)) {
      sql += " and ( (lower(a.location_code) like :codeLocation escape '\\' ) or (lower(a.location_name) like :nameLocation escape '\\' )) ";
      parameters.put("codeLocation",
          StringUtils.convertLowerParamContains(keyword.trim()));
      parameters.put("nameLocation",
          StringUtils.convertLowerParamContains(keyword.trim()));
    }
    if (level != null && !level.equals(0L)) {
      sql += " AND level = :level ";
      parameters.put("level", level);
    }

    sql += " start with a.parent_id is null"
        + " connect by  prior a.location_id = a.parent_id ";
    sql += " order by a.location_name ";

    return sql;
  }

  @Override
  public Datatable getLocationDatapicker(CatLocationDTO catLocationDTO) {
    Map<String, Object> parameters = new HashMap<>();

    long temp = 0;
    if (!StringUtils.isStringNullOrEmpty(catLocationDTO.getLocationLevel())) {
      temp = Long.parseLong(catLocationDTO.getLocationLevel());
    }
    String sqlQuery = getListLocation(catLocationDTO.getLocationName(),
        catLocationDTO.getLocationCode(), catLocationDTO.getKeyword(),
        temp, parameters);
    return getListDataTableBySqlQuery(sqlQuery, parameters,
        catLocationDTO.getPage(), catLocationDTO.getPageSize(), UnitDTO.class,
        catLocationDTO.getSortName(), catLocationDTO.getSortType());
  }

  /**
   * @author tripm
   */
  public List<MrLocationDTO> getListLocationByStationCode(String stationCode) {
    List<MrLocationDTO> lst = new ArrayList<>();
    try {

      Map<String, Object> parameters = new HashMap<>();
      parameters.put("station_code", stationCode);

      String sql = "SELECT location_id locationId,\n"
          + "  location_name locationName,\n"
          + "  location_code locationCode,\n"
          + "  LEVEL AS locationLevel\n"
          + "FROM common_gnoc.CAT_LOCATION a\n"
          + "WHERE LEVEL               = 4\n"
          + "OR LEVEL                  = 3\n"
          + "  START WITH location_id IN\n"
          + "  (SELECT location_id locationId\n"
          + "  FROM common_gnoc.infra_stations\n"
          + "  WHERE STATION_CODE = :station_code\n"
          + "  )\n"
          + "  CONNECT BY PRIOR a.parent_id = a.location_id\n"
          + "ORDER BY locationLevel ASC";

      lst = getNamedParameterJdbcTemplateNormal()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(
              MrLocationDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return lst;
  }
}
