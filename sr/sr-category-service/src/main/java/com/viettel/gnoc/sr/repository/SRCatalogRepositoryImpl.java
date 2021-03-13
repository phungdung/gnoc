package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SR_CONFIG;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRFilesDTO;
import com.viettel.gnoc.sr.model.SRCatalogEntity;
import com.viettel.gnoc.sr.model.SRFilesEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRCatalogRepositoryImpl extends BaseRepository implements SRCatalogRepository {

  @Override
  public Datatable getListSRCatalogPage(SRCatalogDTO srCatalogDTO) {
    BaseDto baseDto = sqlSearch(srCatalogDTO, 0L);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        srCatalogDTO.getPage(), srCatalogDTO.getPageSize(),
        SRCatalogDTO.class,
        srCatalogDTO.getSortName(), srCatalogDTO.getSortType());
  }

  @Override
  public List<SRCatalogDTO> getListServiceChild(SRCatalogDTO srCatalogDTO) {
    BaseDto baseDto = sqlSearch(srCatalogDTO, 0L);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
  }

  @Override
  public ResultInSideDto delete(Long serviceId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRCatalogEntity srCatalogEntity = getEntityManager()
        .find(SRCatalogEntity.class, serviceId);
    if (srCatalogEntity != null) {
      getEntityManager().remove(srCatalogEntity);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto add(SRCatalogDTO srCatalogDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRCatalogEntity srCatalogEntity = getEntityManager().merge(srCatalogDTO.toEntity());
    resultInSideDto.setId(srCatalogEntity.getServiceId());
    return resultInSideDto;
  }

  @Override
  public SRCatalogDTO getDetail(Long serviceId) {
    SRCatalogEntity srCatalogEntity = getEntityManager().find(SRCatalogEntity.class, serviceId);
    SRCatalogDTO srCatalogDTO = srCatalogEntity.toDTO();
    return srCatalogDTO;
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogNotUsing(SRCatalogDTO srCatalogDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CATALOG, "sr-catalog-not-using-list");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("lstStatus",srCatalogDTO.getLstStatusConfig());
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
  }

  @Override
  public List<SRCatalogDTO> getListRoleCodeByServiceCode(String serviceCode) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CATALOG, "getListRoleCodeByServiceCode");
    Map<String, Object> parameters = new HashMap<>();
    if (serviceCode != null) {
      sqlQuery += " AND SERVICE_CODE = :serviceCode ";
      parameters.put("serviceCode", serviceCode);
    }
    sqlQuery += " GROUP BY SERVICE_CODE ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
  }

  @Override
  public List<SRCatalogDTO> getListServiceNameToMapping() {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "getListServiceNameToMapping");
    List<SRCatalogDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  @Override
  public List<SRCatalogDTO> getListCountryService() {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "getListCountryService");
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogDTO(SRCatalogDTO srCatalogDTO) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locale", I18n.getLocale());
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_SERVICE, "getListSearchCatalog");
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getCountry())) {
      sql += " AND T1.COUNTRY = :country ";
      parameters.put("country", srCatalogDTO.getCountry());
    }
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceArray())) {
      sql += " AND T1.SERVICE_ARRAY = :serviceArray ";
      parameters.put("serviceArray", srCatalogDTO.getServiceArray());
    }
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceGroup())) {
      sql += " AND T1.SERVICE_GROUP = :serviceGroup ";
      parameters.put("serviceGroup", srCatalogDTO.getServiceGroup());
    }
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceCode())) {
      sql += " AND (LOWER(T1.SERVICE_CODE) LIKE :serviceCode  ESCAPE '\\' OR LOWER(T1.SERVICE_NAME) LIKE :serviceCode ESCAPE '\\') ";
      parameters
          .put("serviceCode", StringUtils.convertLowerParamContains(srCatalogDTO.getServiceCode()));
    }
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getExecutionUnit())) {
      sql += " AND T1.EXECUTION_UNIT in ( ";
      String[] arrUnit = srCatalogDTO.getExecutionUnit().split(",");
      String unitStr = "";
      for (String unitId : arrUnit) {
        unitStr += "'" + unitId.trim() + "',";
      }
      if (unitStr.endsWith(",")) {
        unitStr = unitStr.substring(0, unitStr.length() - 1);
      }

      sql += unitStr;
      sql += " ) ";
    }
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getStatus())) {
      sql += "  AND T1.STATUS = :status ";
      parameters.put("status", srCatalogDTO.getStatus());
    }
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceId())) {
      sql += "  AND T1.SERVICE_ID = :serviceId ";
      parameters.put("serviceId", srCatalogDTO.getServiceId());
    }
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getRoleCode())) {
      sql += " AND T1.ROLE_CODE is not null ";
      String[] arr = srCatalogDTO.getRoleCode().split(",");
      String sqlTmp = " AND (";
      for (String str : arr) {
        sqlTmp += " instr(',' || T1.ROLE_CODE || ',',',' || " + str + " || ',') > 0 ";
        sqlTmp += " OR";
      }
      if (sqlTmp.endsWith("OR")) {
        sqlTmp = sqlTmp.substring(0, sqlTmp.length() - 2);
      }
      sqlTmp += " )";
      sql += sqlTmp;
    }
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getFlowExecute())) {
      sql += "  AND T1.FLOW_EXECUTE = :flowExecute ";
      parameters.put("flowExecute", srCatalogDTO.getFlowExecute());
    }

    sql += " ORDER BY COUNTRY,SERVICE_ARRAY,SERVICE_GROUP,SERVICE_CODE ";

    List<SRCatalogDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));

    return list;
  }

  @Override
  public List<SRCatalogDTO> getListDataExport(SRCatalogDTO srCatalogDTO) {
    BaseDto baseDto = sqlSearch(srCatalogDTO, 1L);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalog(SRCatalogDTO srCatalogDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CATALOG, "sr-catalog-list-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (srCatalogDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceCode())) {
        sqlQuery += " AND T1.SERVICE_CODE = :serviceCode ";
        parameters.put("serviceCode", srCatalogDTO.getServiceCode());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
  }

  @Override
  public SRCatalogDTO checkSRCatalogExist(String serviceCode, String executionUnit) {
    List<SRCatalogEntity> dataEntity = (List<SRCatalogEntity>) findByMultilParam(
        SRCatalogEntity.class,
        "serviceCode", serviceCode, "executionUnit", executionUnit);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  public BaseDto sqlSearch(SRCatalogDTO srCatalogDTO, Long key) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery;
    if (key == 1L) {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CATALOG, "sr-catalog-list-export");
    } else {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CATALOG, "sr-catalog-list");
    }
    Map<String, Object> parameters = new HashMap<>();
    if (srCatalogDTO != null) {
      if (StringUtils.isNotNullOrEmpty(srCatalogDTO.getSearchAll())) {
        sqlQuery += " AND (lower(T1.SERVICE_CODE) LIKE :searchAll ESCAPE '\\' OR lower(T1.SERVICE_NAME) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(srCatalogDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceId())) {
        sqlQuery += " AND T1.SERVICE_ID = :serviceId ";
        parameters.put("serviceId", srCatalogDTO.getServiceId());
      }
      if (StringUtils.isNotNullOrEmpty(srCatalogDTO.getServiceCode())) {
        sqlQuery += " AND (lower(T1.SERVICE_CODE) LIKE :serviceCode ESCAPE '\\' )";
        parameters
            .put("serviceCode",
                StringUtils.convertLowerParamContains(srCatalogDTO.getServiceCode()));
      }

      if (StringUtils.isNotNullOrEmpty(srCatalogDTO.getServiceName())) {
        sqlQuery += " AND (lower(T1.SERVICE_NAME) LIKE :serviceName ESCAPE '\\' )";
        parameters
            .put("serviceName",
                StringUtils.convertLowerParamContains(srCatalogDTO.getServiceName()));
      }

      if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getCountry())) {
        sqlQuery += " AND T1.COUNTRY= :country ";
        parameters.put("country", srCatalogDTO.getCountry());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceArray())) {
        sqlQuery += " AND T1.SERVICE_ARRAY= :serviceArray ";
        parameters.put("serviceArray", srCatalogDTO.getServiceArray());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getExecutionUnit())) {
        sqlQuery += " AND T1.EXECUTION_UNIT= :executionUnit ";
        parameters.put("executionUnit", srCatalogDTO.getExecutionUnit());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceGroup())) {
        sqlQuery += " AND T1.SERVICE_GROUP= :serviceGroup ";
        parameters.put("serviceGroup", srCatalogDTO.getServiceGroup());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getRoleCode())) {
        String sqlR = " AND T1.ROLE_CODE IN (";
        String[] arrStatus = srCatalogDTO.getRoleCode().split(",");
        for (String s : arrStatus) {
          s = s.trim();
          sqlR += "'" + s + "'" + ",";
        }
        if (sqlR.endsWith(",")) {
          sqlR = sqlR.substring(0, sqlR.length() - 1);
        }
        sqlR += ") ";
        sqlQuery += sqlR;
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getStatus())) {
        sqlQuery += " AND T1.STATUS= :status ";
        parameters.put("status", srCatalogDTO.getStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getFlowExecute())) {
        sqlQuery += " AND T1.FLOW_EXECUTE= :flowExecute ";
        parameters.put("flowExecute", srCatalogDTO.getFlowExecute());
      }
    }
    parameters.put("p_leeLocale", I18n.getLocale());
    if (key == 1L) {
      parameters.put("varDate", I18n.getLanguage("srCatalog.varDate"));
    }
    sqlQuery += " ORDER BY T1.SERVICE_ID DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<SRFilesEntity> getListSRFileByObejctId(Long obejctId) {
    return findByMultilParam(SRFilesEntity.class, "fileGroup", SR_CONFIG.FILE_GROUP_SC, "obejctId",
        obejctId);
  }

  @Override
  public ResultInSideDto addSRFile(SRFilesDTO srFilesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRFilesEntity srFilesEntity = getEntityManager().merge(srFilesDTO.toEntity());
    resultInSideDto.setId(srFilesEntity.getFileId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteSRFile(Long fileId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRFilesEntity srFilesEntity = getEntityManager().find(SRFilesEntity.class, fileId);
    if (srFilesEntity != null) {
      getEntityManager().remove(srFilesEntity);
    }
    return resultInSideDto;
  }

  @Override
  public List<SRFilesDTO> findSrFilesByServiceIds(List<Long> serviceId) {
    String sql = "select FILE_ID, FILE_NAME, FILE_PATH, OBEJCT_ID from SR_FILES where OBEJCT_ID in (:idx) ";
    Map<String, Object> params = new HashMap<>();
    params.put("idx", serviceId);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(SRFilesDTO.class));
  }

  @Override
  public List<SRCatalogEntity> findSRCatalogByServiceCode(String serviceCode) {
    return findByMultilParam(SRCatalogEntity.class, "serviceCode", serviceCode);
  }

  @Override
  public SRFilesDTO findSRFile(Long id) {
    SRFilesEntity srFilesEntity = getEntityManager().find(SRFilesEntity.class, id);
    if (srFilesEntity != null) {
      return srFilesEntity.toDTO();
    }
    return null;
  }
}
