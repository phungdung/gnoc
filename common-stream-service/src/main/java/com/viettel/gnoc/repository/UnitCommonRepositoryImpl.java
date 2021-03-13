package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UnitCommonRepositoryImpl extends BaseRepository implements UnitCommonRepository {

  public String getSQL(UnitDTO unitDTO, Map<String, Object> parameters) {
//    String sqlQuery = SQLBuilder
//        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UNIT, "getListUnit");

    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UNIT, "getListUnitSearch");
    parameters.put("p_leeLocale", I18n.getLocale());

    if (!StringUtils.isStringNullOrEmpty(unitDTO.getSearchAll())) {
      sqlQuery += " AND ( "
          + " lower(ut.UNIT_CODE) LIKE :searchAll ESCAPE '\\' "
          + " OR lower(ut.UNIT_NAME) LIKE :searchAll ESCAPE '\\' )";
      parameters
          .put("searchAll", StringUtils.convertLowerParamContains(unitDTO.getSearchAll()));
    }

    if (unitDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitCode())) {
        sqlQuery += " and lower(ut.unit_code) like :unit_code ESCAPE '\\' ";
        parameters.put("unit_code",
            StringUtils.convertLowerParamContains(unitDTO.getUnitCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitName())) {
        sqlQuery += " and lower(ut.unit_name) like :unit_name ESCAPE '\\' ";
        parameters.put("unit_name",
            StringUtils.convertLowerParamContains(unitDTO.getUnitName()));
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitLevel())) {
        sqlQuery += " and ut.UNIT_LEVEL = :unit_level ";
        parameters.put("unit_level",
            unitDTO.getUnitLevel());
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitType())) {
        sqlQuery += " and ut.UNIT_TYPE = :unit_type ";
        parameters.put("unit_type",
            unitDTO.getUnitType());
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getLocationId())) {
        sqlQuery += " and ut.LOCATION_ID = :location_id ";
        parameters.put("location_id",
            unitDTO.getLocationId());
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getSmsGatewayId())) {
        sqlQuery += " and ut.SMS_GATEWAY_ID = :smsGatewayId ";
        parameters.put("smsGatewayId",
            unitDTO.getSmsGatewayId());
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getIpccServiceName())) {
        sqlQuery += " and lower(lipcc.ipcc_service_code) like :ipccServiceName ESCAPE '\\' ";
        parameters.put("ipccServiceName",
            StringUtils.convertLowerParamContains(unitDTO.getIpccServiceName()));
      }
//      if (!StringUtils.isStringNullOrEmpty(unitDTO.getParentUnitName())) {
//        sqlQuery += " and lower(parentUt.unit_name) like :parentUnitName ESCAPE '\\' ";
//        parameters.put("parentUnitName",
//            StringUtils.convertLowerParamContains(unitDTO.getParentUnitName()));
//      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getParentUnitId())) {
        sqlQuery += " and ut.parent_unit_id = :parentId ";
        parameters.put("parentId",
            unitDTO.getParentUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getStatus())) {
        sqlQuery += " and ut.STATUS = :status ";
        parameters.put("status",
            unitDTO.getStatus());
      }
//      else {
//        sqlQuery += " and ut.STATUS = :status ";
//        parameters.put("status",
//            1);
//      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitId())) {
        sqlQuery += " and ut.unit_id = :unitId ";
        parameters.put("unitId",
            unitDTO.getUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getRoleTypeName())) {
        sqlQuery += " and lower(lrole.item_name) like :roleName ESCAPE '\\' ";
        parameters.put("roleName",
            StringUtils.convertLowerParamContains(unitDTO.getRoleTypeName()));
      }
    }

    sqlQuery += " order by ut.unit_code ";
    return sqlQuery;
  }

  public String getSQLALL(UnitDTO unitDTO, Map<String, Object> parameters) {
    String sqlQuery = "SELECT * FROM COMMON_GNOC.UNIT un WHERE 1=1 and un.STATUS = 1 ";

    if (unitDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitCode())) {
        sqlQuery += " and lower(un.unit_code) like :unit_code ESCAPE '\\' ";
        parameters.put("unit_code",
            StringUtils.convertLowerParamContains(unitDTO.getUnitCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitName())) {
        sqlQuery += " and lower(un.unit_name) like :unit_name ESCAPE '\\' ";
        parameters.put("unit_name",
            StringUtils.convertLowerParamContains(unitDTO.getUnitName()));
      }
    }

    sqlQuery += " order by un.unit_code ";
    return sqlQuery;
  }

  public String getSQLNotLike(UnitDTO unitDTO, Map<String, Object> parameters) {
    String sqlQuery = "SELECT * FROM COMMON_GNOC.UNIT un WHERE 1=1 ";

    if (unitDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitCode())) {
        sqlQuery += " and lower(un.unit_code) = :unit_code  ";
        parameters.put("unit_code",
            unitDTO.getUnitCode().toLowerCase());
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitName())) {
        sqlQuery += " and lower(un.unit_name) = :unit_name  ";
        parameters.put("unit_name",
            unitDTO.getUnitName().toLowerCase());
      }
    }

    sqlQuery += " order by un.unit_code ";
    return sqlQuery;
  }

  @Override
  public List<UnitDTO> getListUnit(UnitDTO unitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQLALL(unitDTO, parameters);
    List<UnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(UnitDTO.class));

    return list;
  }

  @Override
  public Datatable getListUnitDTO(UnitDTO unitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQL(unitDTO, parameters);
    if ("parentUnitName".equalsIgnoreCase(unitDTO.getSortName())) {
      unitDTO.setSortName("unitCode");
    }
    return getListDataTableBySqlQuery(sqlQuery, parameters,
        unitDTO.getPage(), unitDTO.getPageSize(), UnitDTO.class,
        unitDTO.getSortName(), unitDTO.getSortType());
  }

  @Override
  public List<UnitDTO> getListUnitDTOExport(UnitDTO unitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQL(unitDTO, parameters);
    List<UnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(UnitDTO.class));
    return list;
  }

  @Override
  public ResultInSideDto updateUnit(UnitDTO unitDTO) {
    return insertOrUpdate(unitDTO);
  }

  public ResultInSideDto insertOrUpdate(UnitDTO unitDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    UnitEntity entity = getEntityManager().merge(unitDTO.toEntity());
    resultDto.setId(entity.getUnitId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  @Override
  public ResultInSideDto updateUnitChildren(UnitDTO unitDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append("update unit a set");

    if (unitDTO.getSmsGatewayId() != null && !"".equals(unitDTO.getSmsGatewayId())) {
      sql.append(" a.sms_gateway_id=:smsGateWayId");
      parameters.put("smsGateWayId", unitDTO.getSmsGatewayId());
    }
    if (unitDTO.getIpccServiceId() != null && !"".equals(unitDTO.getIpccServiceId())) {
      if (!parameters.isEmpty()) {
        sql.append(",");
      }
      sql.append(" a.ipcc_service_id= :ipcc_id");
      parameters.put("ipcc_id", unitDTO.getIpccServiceId());
    }

    sql.append(" where a.unit_id in  (select b.unit_id from unit b");
    sql.append(
        " start with b.parent_unit_id = :pr_unit_id connect by prior b.unit_id = b.parent_unit_id) ");
    parameters.put("pr_unit_id", unitDTO.getUnitId());

    int row = getNamedParameterJdbcTemplate().update(sql.toString(), parameters);
    if (row != 0) {
      resultDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultDto.setKey(RESULT.ERROR);
    }

    return resultDto;
  }

  @Override
  public ResultInSideDto deleteUnit(Long id) {
    return delete(id);
  }

  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    UnitEntity entity = getEntityManager().find(UnitEntity.class, id);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListUnit(List<UnitDTO> unitListDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (UnitDTO item : unitListDTO) {
      resultInSideDto = delete(item.getUnitId());
    }
    return resultInSideDto;
  }

  @Override
  public UnitDTO findUnitById(Long id) {
//    UnitDTO dto = new UnitDTO();
//    dto.setUnitId(id);
//    Map<String, Object> parameters = new HashMap<>();
//    String sqlQuery = getSQL(dto, parameters);
//    List<UnitDTO> list = getNamedParameterJdbcTemplate()
//        .query(sqlQuery, parameters, BeanPropertyRowMapper
//            .newInstance(UnitDTO.class));
//
//    if (list != null && list.size() > 0) {
//      return list.get(0);
//    }
    UnitEntity entity = getEntityManager().find(UnitEntity.class, id);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertUnit(UnitDTO unitDTO) {
    return insertOrUpdate(unitDTO);
  }

  @Override
  public ResultInSideDto insertOrUpdateListUnit(List<UnitDTO> unitDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (UnitDTO item : unitDTO) {
      resultInSideDto = insertOrUpdate(item);
    }

    return resultInSideDto;
  }

  @Override
  public List<UnitDTO> getListUnitByLevel(String level) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "";
    if (level != null && !"".equals(level)) {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UNIT, "getListUnitByLevel");
      parameters.put("byLevel", level);
    } else {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UNIT, "getListUnitByLevelNull");
    }

    List<UnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(UnitDTO.class));

    return list;
  }

  @Override
  public List<UnitDTO> getUnitDTOForTree(Boolean isRoot, String status, String parentId) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UNIT, "getListUnitForTree");

    if (isRoot != null && isRoot) {
      sqlQuery += " AND a.parent_unit_id is null ";
    }
    if (!DataUtil.isNullOrEmpty(status)) {
      sqlQuery += "   AND a.status = :status ";
      parameters.put("status", status);
    }
    if (!DataUtil.isNullOrEmpty(parentId)) {
      sqlQuery += "   AND a.parent_unit_id = :parentId ";
      parameters.put("parentId", parentId);
    }

    List<UnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(UnitDTO.class));

    return list;
  }

  @Override
  public List<UnitDTO> getUnitVSADTOForTree(Boolean isRoot, String status, String parentId) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UNIT, "getListVSAForTree");

    if (isRoot != null && isRoot) {
      sqlQuery += " AND a.parent_unit_id is null ";
    }
    if (!DataUtil.isNullOrEmpty(status)) {
      sqlQuery += "   AND a.status = :status ";
      parameters.put("status", status);
    }
    if (!DataUtil.isNullOrEmpty(parentId)) {
      sqlQuery += "   AND a.parent_unit_id = :parentId ";
      parameters.put("parentId", parentId);
    }

    List<UnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(UnitDTO.class));

    return list;
  }

  @Override
  public List<UnitDTO> getListUnitVSA(UnitDTO unitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UNIT, "getListUnitVSA");

    if (unitDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitCode())) {
        sqlQuery += " and lower(ut.dept_code) like :dept_code ESCAPE '\\' ";
        parameters.put("dept_code", StringUtils.convertLowerParamContains(unitDTO.getUnitCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitName())) {
        sqlQuery += " and lower(ut.dept_name) like :dept_name ESCAPE '\\' ";
        parameters.put("dept_name", StringUtils.convertLowerParamContains(unitDTO.getUnitCode()));
      }
    }

    sqlQuery += " order by ut.dept_code ";
    List<UnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(UnitDTO.class));

    return list;
  }

  @Override
  public List<UnitDTO> getListUnitVSANotName(UnitDTO unitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UNIT, "getListUnitVSANotName");

    sqlQuery += " order by ut.dept_code ";
    List<UnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(UnitDTO.class));

    return list;
  }

  @Override
  public Datatable getListUnitDatatableAll(UnitDTO unitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQLALL(unitDTO, parameters);
    return getListDataTableBySqlQuery(sqlQuery, parameters,
        unitDTO.getPage(), unitDTO.getPageSize(), UnitDTO.class,
        unitDTO.getSortName(), unitDTO.getSortType());
  }

  @Override
  public List<UnitDTO> getListUnitNotLike(UnitDTO unitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQLNotLike(unitDTO, parameters);
    List<UnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(UnitDTO.class));

    return list;
  }
  //thangdt
  @Override
  public List<UnitDTO> getListUnitVSANotNameAndActive(UnitDTO unitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UNIT, "getListUnitVSANotNameAndActive");

    sqlQuery += " order by unitCode ";
    List<UnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(UnitDTO.class));

    return list;
  }

}
