package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.model.SRConfigEntity;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRConfigRepositoryImpl extends BaseRepository implements SRConfigRepository {

  @Override
  public Datatable getListSRConfigPage(SRConfigDTO srConfigDTO) {
    BaseDto baseDto = sqlSearch(srConfigDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        srConfigDTO.getPage(), srConfigDTO.getPageSize(),
        SRConfigDTO.class,
        srConfigDTO.getSortName(), srConfigDTO.getSortType());
  }

  @Override
  public List<SRConfigDTO> getListConfigGroup(String parentCode) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CONFIG, "sr-config-group-list");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(parentCode)) {
      sqlQuery += " AND a.PARENTCODE= :parentCode ";
      parameters.put("parentCode", parentCode);
    }
    parameters.put("p_leeLocale", I18n.getLocale());
    sqlQuery += " ORDER BY a.CONFIGNAME ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));
  }

  @Override
  public List<SRConfigDTO> getByConfigGroup(SRConfigDTO srConfigDTO) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locale", I18n.getLocale());
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getByConfigGroup");
    if (srConfigDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigGroup())) {
        sql += " AND a.CONFIG_GROUP =:configGroup ";
        parameters.put("configGroup", srConfigDTO.getConfigGroup());
      }
      if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getParentGroup())) {
        sql += " AND a.PARENT_GROUP =:parentGroup ";
        parameters.put("parentGroup", srConfigDTO.getParentGroup());
      }
      if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getCountry())) {
        sql += " AND a.COUNTRY =:country ";
        parameters.put("country", srConfigDTO.getCountry());
      }
    }
    sql += " ORDER BY a.CONFIG_NAME ";

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    return list;
  }

  @Override
  public ResultInSideDto add(SRConfigDTO srConfigDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRConfigEntity srConfigEntity = getEntityManager().merge(srConfigDTO.toEntity());
    resultInSideDto.setId(srConfigEntity.getConfigId());
    return resultInSideDto;
  }

  @Override
  public SRConfigDTO getDetail(Long roleId) {
    SRConfigEntity srConfigEntity = getEntityManager().find(SRConfigEntity.class, roleId);
    SRConfigDTO srConfigDTO = srConfigEntity.toDTO();
    return srConfigDTO;
  }

  @Override
  public ResultInSideDto edit(SRConfigDTO srConfigDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(srConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public SRConfigDTO checkConfigCatalog(String configCode, String configGroup) {
    List<SRConfigEntity> dataEntity = (List<SRConfigEntity>) findByMultilParam(
        SRConfigEntity.class,
        "configCode", configCode,
        "configGroup", configGroup);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public SRConfigDTO getConfigGroupByConfigCode(String configCode, String status,
      String parentGroup, Long serviceId) {
    List<SRConfigEntity> dataEntity = (List<SRConfigEntity>) findByMultilParam(
        SRConfigEntity.class,
        "configCode", configCode,
        "status", status,
        "parentGroup", parentGroup);
    if (dataEntity != null && dataEntity.size() > 0) {
      SRConfigDTO srConfigDTO = dataEntity.get(0).toDTO();
      if (srConfigDTO != null) {
        String srConfigSerivceIds = srConfigDTO.getSrCfgServiceIds();
        if (!StringUtils.isStringNullOrEmpty(srConfigSerivceIds)) {
          List<String> arrServiceIds = Arrays.asList(srConfigSerivceIds.split(","));
          List<String> arrConfigStr = Arrays.asList(srConfigDTO.getConfigGroup().split(","));
          String strConfigReturn = "";
          if (arrServiceIds.size() == arrConfigStr.size()) {
            for (int i = 0; i < arrConfigStr.size(); i++) {
              if (("-" + arrServiceIds.get(i) + "-").contains("-" + serviceId + "-")) {
                strConfigReturn += arrConfigStr.get(i) + ",";
              }
            }
            if (strConfigReturn.endsWith(",")) {
              strConfigReturn = strConfigReturn.substring(0, strConfigReturn.length() - 1);
            }
            srConfigDTO.setConfigGroup(strConfigReturn);
          }
        }
      }
      return srConfigDTO;
    }
    return null;
  }

  @Override
  public ResultInSideDto delete(Long configId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRConfigEntity srConfigEntity = getEntityManager().find(SRConfigEntity.class, configId);
    if (srConfigEntity != null) {
      getEntityManager().remove(srConfigEntity);
    }
    return resultInSideDto;
  }

  public BaseDto sqlSearch(SRConfigDTO srConfigDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CONFIG, "sr-list-config-page");
    Map<String, Object> parameters = new HashMap<>();
    if (srConfigDTO != null) {
      if (StringUtils.isNotNullOrEmpty(srConfigDTO.getSearchAll())) {
        sqlQuery += " AND (lower(a.CONFIG_GROUP) LIKE :searchAll ESCAPE '\\' OR lower(a.CONFIG_NAME) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(srConfigDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigGroup())) {
        sqlQuery += " AND (lower(a.CONFIG_GROUP) LIKE :configGroup ESCAPE '\\')";
        parameters.put("configGroup",
            StringUtils.convertLowerParamContains(srConfigDTO.getConfigGroup()));
      }
      if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigCode())) {
        sqlQuery += " AND (lower(a.CONFIG_CODE) LIKE :configCode ESCAPE '\\')";
        parameters
            .put("configCode", StringUtils.convertLowerParamContains(srConfigDTO.getConfigCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigName())) {
        sqlQuery += " AND (lower(a.CONFIG_NAME) LIKE :configName ESCAPE '\\')";
        parameters
            .put("configName", StringUtils.convertLowerParamContains(srConfigDTO.getConfigName()));
      }
      if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getStatus())) {
        sqlQuery += " AND a.STATUS= :status ";
        parameters.put("status", srConfigDTO.getStatus());
      }
      parameters.put("locale", I18n.getLocale());
    }
    sqlQuery += " ORDER BY a.UPDATED_TIME DESC";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
