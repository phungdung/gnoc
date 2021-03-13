package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.model.SRConfigEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRServiceManageRepositoryImpl extends BaseRepository implements
    SRServiceManageRepository {

  public BaseDto getSQL(SRConfigDTO configDTO) {
    Map<String, Object> parameters = new HashMap<>();
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getListServiceArrays");

    if (!StringUtils.isStringNullOrEmpty(configDTO.getConfigCode())) {
      sql += " AND LOWER(t1.CONFIG_CODE) LIKE :configCode  ESCAPE '\\' ";
      parameters
          .put("configCode", StringUtils.convertLowerParamContains(configDTO.getConfigCode()));
    }
    if (!StringUtils.isStringNullOrEmpty(configDTO.getConfigName())) {
      sql += " AND LOWER(T1.CONFIG_NAME) LIKE :configName ESCAPE '\\' ";
      parameters
          .put("configName", StringUtils.convertLowerParamContains(configDTO.getConfigName()));
    }
    if (!StringUtils.isStringNullOrEmpty(configDTO.getCountry())) {
      sql += " AND T1.COUNTRY LIKE :country ";
      parameters.put("country", configDTO.getCountry());
    }
    if (!StringUtils.isStringNullOrEmpty(configDTO.getCountry())) {
      sql += " AND T1.COUNTRY LIKE :country ";
      parameters.put("country", configDTO.getCountry());
    }
    if (!StringUtils.isStringNullOrEmpty(configDTO.getAutomation())) {
      sql += " AND T1.AUTO_MATION LIKE :automation ";
      parameters.put("automation", configDTO.getAutomation());
    }
    if (!StringUtils.isStringNullOrEmpty(configDTO.getUpdatedUser())) {
      sql += " AND T1.UPDATED_USER LIKE :updateUser ";
      parameters.put("updateUser", configDTO.getUpdatedUser());
    }
    if (!StringUtils.isStringNullOrEmpty(configDTO.getUpdatedTime())) {
      sql += " AND updatedTime LIKE :time ";
      parameters.put("time", configDTO.getUpdatedTime());
    }

    if (StringUtils.isNotNullOrEmpty(configDTO.getSearchAll())) {
      sql += " AND ( "
          + " lower(t1.CONFIG_CODE) LIKE :searchAll ESCAPE '\\' "
          + " OR lower(T1.CONFIG_NAME) LIKE :searchAll ESCAPE '\\' )";
      parameters
          .put("searchAll", StringUtils.convertLowerParamContains(configDTO.getSearchAll().trim()));
    }

    sql += " ORDER BY t1.UPDATED_TIME DESC";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListSearchSRServiceArray(SRConfigDTO configDTO) {
    BaseDto baseDto = getSQL(configDTO);

    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        configDTO.getPage(), configDTO.getPageSize(), SRConfigDTO.class,
        configDTO.getSortName(), configDTO.getSortType());
  }

  @Override
  public ResultInSideDto insertOrUpdateService(SRConfigDTO srConfigDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    SRConfigEntity entity = getEntityManager().merge(srConfigDTO.toEntity());
    resultDto.setId(entity.getConfigId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  @Override
  public SRConfigDTO getSRServiceArrayDetail(Long id) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getListServiceArrays");

    if (!StringUtils.isStringNullOrEmpty(id)) {
      sql += " AND t1.CONFIG_ID = :configId ";
      parameters.put("configId", id);
    }

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    if (list != null && list.size() > 0) {
      return list.get(0);
    }

    return null;
  }

  @Override
  public SRConfigDTO getSRServiceGroupDetail(Long id) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getListServiceGroup");

    if (!StringUtils.isStringNullOrEmpty(id)) {
      sql += " AND t3.CONFIG_ID = :configId ";
      parameters.put("configId", id);
    }

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    if (list != null && list.size() > 0) {
      return list.get(0);
    }

    return null;
  }

  @Override
  public ResultInSideDto deleteSRService(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    SRConfigEntity entity = getEntityManager().find(SRConfigEntity.class, id);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public List<SRConfigDTO> getByConfigGroup(String configGroup) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locale", I18n.getLocale());
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getByConfigGroup");

    if (!StringUtils.isStringNullOrEmpty(configGroup)) {
      sql += " AND a.CONFIG_GROUP LIKE :configGroup ";
      parameters.put("configGroup", configGroup);
    }

    sql += " ORDER BY a.CONFIG_NAME ";

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    return list;
  }

  public BaseDto getSQLServiceGroup(SRConfigDTO srConfigDTO) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getListServiceGroup");

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigCode())) {
      sql += " AND LOWER(T3.CONFIG_CODE) LIKE :configCode  ESCAPE '\\' ";
      parameters
          .put("configCode", StringUtils.convertLowerParamContains(srConfigDTO.getConfigCode()));
    }
    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigName())) {
      sql += " AND LOWER(T3.CONFIG_NAME) LIKE :configName  ESCAPE '\\' ";
      parameters
          .put("configName", StringUtils.convertLowerParamContains(srConfigDTO.getConfigName()));
    }
    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getCountry())) {
      sql += " AND t3.COUNTRY LIKE :country ";
      parameters.put("country", srConfigDTO.getCountry());
    }
    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getParentCode())) {
      sql += " AND LOWER(T3.PARENT_CODE) LIKE :parentCode  ESCAPE '\\' ";
      parameters
          .put("parentCode", StringUtils.convertLowerParamContains(srConfigDTO.getParentCode()));
    }
    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getUpdatedUser())) {
      sql += " AND T3.UPDATED_USER LIKE :updateUser ";
      parameters.put("updateUser", srConfigDTO.getUpdatedUser());
    }
    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getUpdatedTime())) {
      sql += " AND updatedTime LIKE :time ";
      parameters.put("time", srConfigDTO.getUpdatedTime());
    }

    if (StringUtils.isNotNullOrEmpty(srConfigDTO.getSearchAll())) {
      sql += " AND ( "
          + " lower(T3.CONFIG_CODE) LIKE :searchAll ESCAPE '\\' "
          + " OR lower(T3.CONFIG_NAME) LIKE :searchAll ESCAPE '\\' "
          + "  or lower(T3.PARENT_CODE) LIKE :searchAll ESCAPE '\\' )";
      parameters
          .put("searchAll",
              StringUtils.convertLowerParamContains(srConfigDTO.getSearchAll().trim()));
    }

    sql += " ORDER BY t3.UPDATED_TIME DESC";

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListSearchSRServiceGroup(SRConfigDTO configDTO) {
    BaseDto baseDto = getSQLServiceGroup(configDTO);

    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        configDTO.getPage(), configDTO.getPageSize(), SRConfigDTO.class,
        configDTO.getSortName(), configDTO.getSortType());
  }

  @Override
  public SRConfigDTO getSRServiceArrayByDTO(SRConfigDTO srConfigDTO) {
    List<SRConfigEntity> list = findByMultilParam(SRConfigEntity.class, "configCode",
        srConfigDTO.getConfigCode(),
        "configName", srConfigDTO.getConfigName());

    if (list != null && list.size() > 0) {
      SRConfigEntity entity = list.get(0);
      SRConfigDTO dto = entity.toDTO();
      return dto;
    }

    return null;
  }

  @Override
  public List<SRConfigDTO> getListConfigDTO(SRConfigDTO srConfigDTO) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locale", I18n.getLocale());
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getListServiceArrays");

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigCode())) {
      sql += " AND (LOWER(t1.CONFIG_CODE) LIKE :configCode  ESCAPE '\\') ";
      parameters.put("configCode",
          StringUtils.convertLowerParamContains(srConfigDTO.getConfigCode().trim()));
    }
    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigName())) {
      sql += " AND (LOWER(T1.CONFIG_NAME) LIKE :configName ESCAPE '\\') ";
      parameters.put("configName",
          StringUtils.convertLowerParamContains(srConfigDTO.getConfigName().trim()));
    }

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    return list;
  }

  @Override
  public List<SRConfigDTO> getListSrConfigGroup(SRConfigDTO srConfigDTO) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locale", I18n.getLocale());
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getListServiceGroup");

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigCode())) {
      sql += " AND (LOWER(T3.PARENT_CODE) LIKE :configCode  ESCAPE '\\') ";
      parameters.put("configCode",
          StringUtils.convertLowerParamContains(srConfigDTO.getConfigCode().trim()));
    }
    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigName())) {
      sql += " AND (LOWER(T3.CONFIG_NAME) LIKE :configName ESCAPE '\\') ";
      parameters.put("configName",
          StringUtils.convertLowerParamContains(srConfigDTO.getConfigName().trim()));
    }

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    return list;
  }

  @Override
  public List<SRConfigDTO> getListSRConfigImport(SRConfigDTO srConfigDTO) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locale", I18n.getLocale());
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getListArrayImport");

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigCode())) {
      sql += " AND (LOWER(t1.CONFIG_CODE) LIKE :configCode  ESCAPE '\\' OR LOWER(T1.CONFIG_NAME) LIKE :configName ESCAPE '\\') ";
      parameters.put("configCode",
          StringUtils.convertLowerParamContains(srConfigDTO.getConfigCode().trim()));
    }
    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigName())) {
      parameters.put("configName",
          StringUtils.convertLowerParamContains(srConfigDTO.getConfigName().trim()));
    } else {
      parameters.put("configName", null);
    }

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    return list;
  }

  @Override
  public List<SRConfigDTO> getListConfigDTOGroup(SRConfigDTO srConfigDTO) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locale", I18n.getLocale());
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getListGroupImport");

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigCode())) {
      sql += " AND (LOWER(t3.CONFIG_CODE) LIKE :configCode  ESCAPE '\\' OR LOWER(T3.CONFIG_NAME) LIKE :configName ESCAPE '\\') ";
      parameters.put("configCode",
          StringUtils.convertLowerParamContains(srConfigDTO.getConfigCode().trim()));
    }
    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigName())) {
      parameters.put("configName",
          StringUtils.convertLowerParamContains(srConfigDTO.getConfigName().trim()));
    } else {
      parameters.put("configName", null);
    }

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    return list;
  }


  @Override
  public SRConfigDTO getSRServiceGroupByDTO(SRConfigDTO srConfigDTO) {
    List<SRConfigEntity> list = findByMultilParam(SRConfigEntity.class, "configCode",
        srConfigDTO.getConfigCode(),
        "configGroup", srConfigDTO.getConfigGroup());

    if (list != null && list.size() > 0) {
      SRConfigEntity entity = list.get(0);
      SRConfigDTO dto = entity.toDTO();
      return dto;
    }

    return null;
  }

  @Override
  public List<SRConfigDTO> checkEnableGroup(SRConfigDTO configDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "checkEnableGroup");

    if (!StringUtils.isStringNullOrEmpty(configDTO.getConfigCode())) {
      sql += " AND T3.PARENT_CODE like :parentCode ESCAPE '\\' ";
      parameters
          .put("parentCode", StringUtils.convertLowerParamContains(configDTO.getConfigCode()));
    }
    if (!StringUtils.isStringNullOrEmpty(configDTO.getConfigName())) {
      sql += " OR LOWER(T3.CONFIG_NAME) LIKE :configName ESCAPE '\\'";
      parameters
          .put("configName", StringUtils.convertLowerParamContains(configDTO.getConfigName()));
    }

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    return list;
  }

  @Override
  public List<SRConfigDTO> checkEnableGroupNotConvert(SRConfigDTO configDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "checkEnableGroup");

    if (!StringUtils.isStringNullOrEmpty(configDTO.getConfigCode())) {
      sql += " AND T3.PARENT_CODE = :parentCode  ";
      parameters
          .put("parentCode", configDTO.getConfigCode());
    }
    if (!StringUtils.isStringNullOrEmpty(configDTO.getConfigName())) {
      sql += " OR LOWER(T3.CONFIG_NAME) LIKE :configName ESCAPE '\\'";
      parameters
          .put("configName", StringUtils.convertLowerParamContains(configDTO.getConfigName()));
    }

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    return list;
  }

  @Override
  public Datatable getListConfigServiceSystem(SRConfigDTO srConfigDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getListConfigServiceSystem");

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigCode())) {
      sql += " AND LOWER(c.CONFIG_CODE) LIKE :configCode ESCAPE '\\' ";
      parameters
          .put("configCode", StringUtils.convertLowerParamContains(srConfigDTO.getConfigCode()));
    }
    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getServiceCode())) {
      sql += " AND LOWER(a.SERVICE_CODE) LIKE :serviceCode  ESCAPE '\\'";
      parameters
          .put("serviceCode", StringUtils.convertLowerParamContains(srConfigDTO.getServiceCode()));
    }
    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigId())) {
      sql += " AND b.CONFIG_ID != :configId  ";
      parameters.put("configId", srConfigDTO.getConfigId());
    }

    sql += " order by c.CONFIG_NAME,a.SERVICE_NAME";

    return getListDataTableBySqlQuery(sql, parameters,
        srConfigDTO.getPage(), srConfigDTO.getPageSize(), SRConfigDTO.class,
        srConfigDTO.getSortName(), srConfigDTO.getSortType());
  }

  @Override
  public ResultInSideDto insertOrUpdateSRConfigService(SRConfigDTO srConfigDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(Constants.RESULT.SUCCESS);
    SRConfigEntity entity = getEntityManager().merge(srConfigDTO.toEntity());
    resultDto.setId(entity.getConfigId());

    return resultDto;
  }

  @Override
  public boolean checkSrConfigExisted(SRConfigDTO srConfigDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "checkSrConfigExisted");

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigGroup())) {
      sql += " AND LOWER(CONFIG_GROUP) LIKE :configGroup ";
      parameters.put("configGroup", srConfigDTO.getConfigGroup().toLowerCase());
    }

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigCode()) ||
        !StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigName())) {
      sql += " AND ( LOWER(CONFIG_CODE) LIKE :configCode OR LOWER(CONFIG_NAME) LIKE :configName) ";
      parameters.put("configCode", srConfigDTO.getConfigCode().toLowerCase());
      parameters.put("configName", srConfigDTO.getConfigName().toLowerCase());
    }

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigId())) {
      sql += " AND CONFIG_ID <> :id ";
      parameters.put("id", srConfigDTO.getConfigId());
    }

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    if (list != null && list.size() > 0) {
      return true;
    }

    return false;
  }

  @Override
  public List<SRConfigDTO> getListSrConfig(SRConfigDTO srConfigDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "SELECT * FROM OPEN_PM.SR_CONFIG WHERE 1=1 AND STATUS ='A'";

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigGroup())) {
      sql += " AND LOWER(CONFIG_GROUP) LIKE :configGroup ";
      parameters.put("configGroup", srConfigDTO.getConfigGroup().toLowerCase());
    }

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigCode())) {
      sql += " AND LOWER(CONFIG_CODE) LIKE :configCode  ";
      parameters.put("configCode", srConfigDTO.getConfigCode().toLowerCase());
    }

    if (!StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigName())) {
      sql += " AND LOWER(CONFIG_NAME) LIKE :configName ";
      parameters.put("configName", srConfigDTO.getConfigName().toLowerCase());
    }

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    if (list != null && list.size() > 0) {
      return list;
    }

    return null;
  }

  @Override
  public List<SRConfigDTO> getSrConfigExistedByName(SRConfigDTO srConfigDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql =
        " Select * from SR_CONFIG t WHERE 1=1 AND t.CONFIG_NAME = :p_config_name "
            + " AND t.CONFIG_GROUP = :p_config_group "
            + " and t.STATUS='A' ";
    parameters.put("p_config_name", srConfigDTO.getConfigName());
    parameters.put("p_config_group", srConfigDTO.getConfigGroup());
    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  @Override
  public boolean checkSrConfigExistedByCode(SRConfigDTO srConfigDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql =
        " Select * from SR_CONFIG t WHERE 1=1  AND t.CONFIG_CODE = :p_config_code "
            + " AND t.CONFIG_GROUP = :p_config_group "
            + " and t.STATUS ='A' ";
    parameters.put("p_config_code", srConfigDTO.getConfigCode());
    parameters.put("p_config_group", srConfigDTO.getConfigGroup());
    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

  @Override
  public boolean checkSrConfigExistedByName(SRConfigDTO srConfigDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql =
        "Select * from SR_CONFIG t WHERE 1=1  AND t.CONFIG_NAME = :p_config_name "
            + " AND t.CONFIG_GROUP = :p_config_group "
            + " and t.STATUS='A' ";
    parameters.put("p_config_name", srConfigDTO.getConfigName());
    parameters.put("p_config_group", srConfigDTO.getConfigGroup());
    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

  @Override
  public List<SRConfigDTO> getListDataExportServiceArray(SRConfigDTO srConfigDTO) {
    BaseDto baseDto = getSQL(srConfigDTO);

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    if (list != null && list.size() > 0) {
      return list;
    }

    return null;
  }

  @Override
  public List<SRConfigDTO> getListDataExportServiceGroup(SRConfigDTO srConfigDTO) {
    BaseDto baseDto = getSQLServiceGroup(srConfigDTO);

    List<SRConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(SRConfigDTO.class));

    if (list != null && list.size() > 0) {
      return list;
    }

    return null;
  }
}
