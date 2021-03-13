package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CfgWebServiceSQLDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CfgWebServiceSQLEntity;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgWebServiceSQLRepositoryImpl extends BaseRepository implements
    CfgWebServiceSQLRepository {

  @Override
  public ResultInSideDto insertOrUpdate(CfgWebServiceSQLDTO cfgWebServiceSQLDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (cfgWebServiceSQLDTO.getId() == null) {
      cfgWebServiceSQLDTO.setCreateTime(new Date());
    } else {
      cfgWebServiceSQLDTO.setUpdateTime(new Date());
    }
    CfgWebServiceSQLEntity cfgWebServiceSQLEntity = getEntityManager()
        .merge(cfgWebServiceSQLDTO.toEntity());
    resultInSideDto.setId(cfgWebServiceSQLEntity.getId());
    return resultInSideDto;
  }

  @Override
  public CfgWebServiceSQLDTO getDetail(Long id) {
    CfgWebServiceSQLEntity cfgWebServiceSQLEntity = getEntityManager()
        .find(CfgWebServiceSQLEntity.class, id);
    if (cfgWebServiceSQLEntity != null) {
      return cfgWebServiceSQLEntity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteById(Long id) {
    CfgWebServiceSQLEntity cfgWebServiceSQLEntity = getEntityManager()
        .find(CfgWebServiceSQLEntity.class, id);
    if (cfgWebServiceSQLEntity != null) {
      String result = deleteById(CfgWebServiceSQLEntity.class, id, "id");
      return new ResultInSideDto(null, result, result);
    }
    return new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
  }

  @Override
  public Datatable onSearch(CfgWebServiceSQLDTO cfgWebServiceSQLDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
        "cfg-web-service-sql-search");
    Map<String, Object> parameters = new HashMap<>();
    if (cfgWebServiceSQLDTO.getId() != null) {
      sql += " AND t1.ID = :id ";
      parameters.put("id", cfgWebServiceSQLDTO.getId());
    }
    if (cfgWebServiceSQLDTO.getStatus() != null) {
      sql += " AND t1.STATUS = :status ";
      parameters.put("status", cfgWebServiceSQLDTO.getStatus());
    }
    if (StringUtils.isNotNullOrEmpty(cfgWebServiceSQLDTO.getCode())) {
      sql += " AND LOWER(t1.code) LIKE :code ESCAPE '\\'";
      parameters
          .put("code", StringUtils.convertLowerParamContains(cfgWebServiceSQLDTO.getCode()));
    }
    if (StringUtils.isNotNullOrEmpty(cfgWebServiceSQLDTO.getSqlText())) {
      sql += " AND LOWER(t1.SQL_TEXT) LIKE :sqlText ESCAPE '\\'";
      parameters
          .put("sqlText", StringUtils.convertLowerParamContains(cfgWebServiceSQLDTO.getSqlText()));
    }
    if (StringUtils.isNotNullOrEmpty(cfgWebServiceSQLDTO.getSystem())) {
      sql += " AND LOWER(t1.system) LIKE :system ESCAPE '\\'";
      parameters
          .put("system", StringUtils.convertLowerParamContains(cfgWebServiceSQLDTO.getSystem()));
    }
    if (StringUtils.isNotNullOrEmpty(cfgWebServiceSQLDTO.getColumnKey())) {
      sql += " AND LOWER(t1.COLUMN_KEY) LIKE :columnKey ESCAPE '\\'";
      parameters.put("columnKey",
          StringUtils.convertLowerParamContains(cfgWebServiceSQLDTO.getColumnKey()));
    }

    if (cfgWebServiceSQLDTO.getStatus() != null) {
      sql += " AND LOWER(t1.COLUMN_KEY) LIKE :columnKey ESCAPE '\\'";
      parameters.put("columnKey",
          StringUtils.convertLowerParamContains(cfgWebServiceSQLDTO.getColumnKey()));
    }
    return getListDataTableBySqlQuery(sql, parameters, cfgWebServiceSQLDTO.getPage(),
        cfgWebServiceSQLDTO.getPageSize(), CfgWebServiceSQLDTO.class,
        cfgWebServiceSQLDTO.getSortName(),
        cfgWebServiceSQLDTO.getSortType());
  }

}
