package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
@Transactional
public class SRConfigRepositoryImpl extends BaseRepository implements SRConfigRepository {

  @Override
  public List<SRConfigDTO> getByConfigGroup(String configGroup) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locale", I18n.getLocale());
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getByConfigGroup");

    if (!StringUtils.isStringNullOrEmpty(configGroup)) {
      sql += " AND a.CONFIG_GROUP =:configGroup ";
      parameters.put("configGroup", configGroup);
    }

    sql += " ORDER BY a.CONFIG_NAME ";

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    return list;
  }

  @Override
  public List<SRConfigDTO> getConfigGroup(String parentCode) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locale", I18n.getLocale());
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getConfigGroup");

    if (!StringUtils.isStringNullOrEmpty(parentCode)) {
      sql += " AND a.PARENTCODE =:parentCode ";
      parameters.put("parentCode", parentCode);
    }

    sql += " ORDER BY a.CONFIGNAME ";

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    return list;
  }

  @Override
  public List<SRConfigDTO> getSmsContent(String nextStatus, String serviceArray,
      String serviceGroup, String serviceCode, String configGroup) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locale", I18n.getLocale());
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getSmsContent");
    parameters.put("configGroup", configGroup);
    parameters.put("nextStatus", nextStatus);
    parameters.put("serviceArray", serviceArray);
    parameters.put("serviceGroup", serviceGroup);
    parameters.put("serviceCode", serviceCode);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRConfigDTO.class));
  }

  @Override
  public List<SRConfigDTO> getDataByConfigCode(SRConfigDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getDataByConfigCode");
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {
      parameters.put("p_config_code", dto.getConfigCode());
      parameters.put("p_config_group", dto.getConfigGroup());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRConfigDTO.class));
  }

  @Override
  public List<SRConfigDTO> getSmsContentByConfig(SRConfigDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getSmsContentByConfig");
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {
      parameters.put("configCode", dto.getConfigCode());
      parameters.put("configGroup", dto.getConfigGroup());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRConfigDTO.class));
  }

  @Override
  public List<SRConfigDTO> getCrInforByParentGroup(String parentGroup) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getCrInforByParentGroup");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(parentGroup)) {
      sql += " AND A.PARENT_GROUP = :parentGroup ";
      parameters.put("parentGroup", parentGroup);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRConfigDTO.class));
  }
}
