package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.SR_CONFIG;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SROutsideRepositoryImpl extends BaseRepository implements SROutsideRepository {

  @Override
  public List<SRDTO> getListSRForGatePro(String fromDate, String toDate, SRDTO srDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_AOM, "getListSRForGatePro");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(fromDate)) {
      sqlQuery += " AND srh.created_time >= to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') ";
      parameters.put("fromDate", fromDate);
    }
    if (!StringUtils.isStringNullOrEmpty(toDate)) {
      sqlQuery += " AND srh.created_time <= to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') ";
      parameters.put("toDate", toDate);
    }
    if (srDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srDTO.getSrId())) {
        sqlQuery += " AND t1.sr_id = :srId ";
        parameters.put("srId", srDTO.getSrId());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRDTO.class));
  }

  @Override
  public List<SRDTO> getListSRForWOTHVSmart(SRDTO srDTO, String woId) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListSRForWOTHVSmart");
    if (!StringUtils.isStringNullOrEmpty(woId)) {
      sql += " AND b3.WO_ID = :woId ";
      parameters.put("woId", woId);
    }
    sql += " ORDER BY T1.CREATED_TIME DESC";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRDTO.class));
  }

  @Override
  public List<SRDTO> getListSRByConfigGroup(SRDTO dto, String configGroup) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListSRByConfigGroup");
    parameters.put("configGroup", configGroup);
    if (!StringUtils.isStringNullOrEmpty(dto.getCreatedUser())) {
      sql += " AND T1.CREATED_USER = :createdUser ";
      parameters.put("createdUser", dto.getCreatedUser());
    }
    sql += " ORDER BY T1.CREATED_TIME DESC";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRDTO.class));
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogByConfigGroup(String configGroup) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListSRCatalogByConfigGroup");
    parameters.put("configGroup", configGroup);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
  }

  @Override
  public SRDTO getDetailSRForVSmart(String srId, String loginUser) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getDetailSRForVSmart");
    parameters.put("srId", srId);
    List<SRDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRDTO.class));
    return (lst != null && !lst.isEmpty()) ? lst.get(0) : null;
  }

  @Override
  public List<SRDTO> getListSRForVSmart(SRDTO dto) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListSRForVSmart");
    parameters.put("loginUser", dto.getLoginUser());
    if (!StringUtils.isStringNullOrEmpty(dto.getTitle())) {
      sql += " AND (UPPER(T1.TITLE) LIKE :title escape '\\' OR UPPER(T1.SR_CODE) LIKE :title escape '\\' ) ";
      parameters.put("title", StringUtils.convertUpperParamContains(dto.getTitle()));
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getStatus())) {
      String sqlStatus = " AND T1.STATUS IN (";
      String[] arrStatus = dto.getStatus().split(",");
      for (String s : arrStatus) {
        s = s.trim();
        sqlStatus += "'" + s + "'" + ",";
      }
      if (sqlStatus.endsWith(",")) {
        sqlStatus = sqlStatus.substring(0, sqlStatus.length() - 1);
        sqlStatus += ") ";
        sql += sqlStatus;
      }
    }
    sql += " AND T1.STATUS != 'Closed' ORDER BY T3.PARENT_CODE,nvl(T1.UPDATED_TIME,T1.CREATED_TIME) DESC";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRDTO.class));
  }

  @Override
  public List<SRRoleUserDTO> getListSRUserForVSmart(String serviceCode, Long unitId,
      String roleCode, String country) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListSRUserForVSmart");
    if (StringUtils.isNotNullOrEmpty(country)) {
      sql += " AND b2.COUNTRY =:country ";
      parameters.put("country", country);
    }
    if (StringUtils.isNotNullOrEmpty(serviceCode)) {
      sql += " AND b1.service_code =:serviceCode ";
      parameters.put("serviceCode", serviceCode);
    }
    if (!StringUtils.isLongNullOrEmpty(unitId)) {
      sql += " AND b2.unit_id =:unitId ";
      parameters.put("unitId", unitId);
    }
    if (StringUtils.isNotNullOrEmpty(roleCode)) {
      sql += " AND b2.ROLE_CODE =:roleCode ";
      parameters.put("roleCode", roleCode);
    }
    sql += " ORDER BY b2.user_name ASC ";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRRoleUserDTO.class));
  }

  @Override
  public List<SRRoleUserDTO> getListRoleUserForVsmart(SRRoleUserDTO srRoleUserDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "SELECT DISTINCT "
        + " T1.ROLE_CODE roleCode, "
        + " T2.ROLE_NAME roleName "
        + " FROM OPEN_PM.SR_ROLE_USER T1 "
        + " INNER JOIN OPEN_PM.SR_ROLE T2 ON T1.ROLE_CODE = T2.ROLE_CODE "
        + " WHERE 1 = 1 ";

    if (srRoleUserDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUnitId())) {
        sql += " AND T1.UNIT_ID = :unitId ";
        parameters.put("unitId", srRoleUserDTO.getUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getCountry())) {
        sql += " AND T1.COUNTRY = :country ";
        parameters.put("country", srRoleUserDTO.getCountry());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRRoleUserDTO.class));
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogByConfigGroupIBPMS(String configGroup) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListSRCatalogByConfigGroupIBPMS");
    parameters.put("configGroup", configGroup);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogByConfigGroupVSMAT(String configGroup,
      String serviceGroup) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListSRCatalogByConfigGroupVSMAT");
    if (StringUtils.isNotNullOrEmpty(serviceGroup)) {
      sql += " AND b3.config_code =:serviceGroup ";
      parameters.put("serviceGroup", serviceGroup);
    }
    parameters.put("configGroup", configGroup);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
  }

  @Override
  public List<SRConfigDTO> getListServiceGrouprForVsmart() {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListServiceGrouprForVsmart");
    parameters.put("configGroup", SR_CONFIG.DICH_VU_VSMART);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRConfigDTO.class));
  }
}
