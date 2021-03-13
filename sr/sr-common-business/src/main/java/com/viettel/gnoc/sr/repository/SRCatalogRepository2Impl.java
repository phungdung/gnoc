package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.UnitSRCatalogDTO;
import com.viettel.gnoc.sr.model.SRCatalogEntity;
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
public class SRCatalogRepository2Impl extends BaseRepository implements SRCatalogRepository2 {

  @Override
  public SRCatalogDTO findById(Long serviceId) {
    List<SRCatalogEntity> lst = findByMultilParam(SRCatalogEntity.class, "serviceId", serviceId);
    return lst.isEmpty() ? null : lst.get(0).toDTO();
  }

  @Override
  public List<UnitSRCatalogDTO> getListUnitSRCatalog(SRCatalogDTO dto) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById("SRCatalog", "getListUnitSRCatalog");
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getServiceCode())) {
        sql += (" AND t1.service_code = :serviceCode ");
        parameters.put("serviceCode", dto.getServiceCode());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getStatus())) {
        if ("A".equals(dto.getStatus())) {
          sql += (" AND t1.status = 'A' ");
        }
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getServiceId())) {
        sql += (" AND t1.service_id = :serviceId ");
        parameters.put("serviceId", dto.getServiceId());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UnitSRCatalogDTO.class));
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogDTO(SRCatalogDTO srCatalogDTO) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locale", I18n.getLocale());
    String sql = SQLBuilder
        .getSqlQueryById("SRCatalog", "getListSearchCatalog");
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
      sql += " AND (LOWER(T1.SERVICE_CODE) LIKE :serviceCode ESCAPE '\\' OR LOWER(T1.SERVICE_NAME) LIKE :serviceCode ESCAPE '\\') ";
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
  public List<SRCatalogDTO> getListCatalogWithRoleAndUnit(String type) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "";
    if ("ROLE".equals(type)) {
      sql =
          "select SERVICE_CODE serviceCode,STATUS status, listagg(ROLE_CODE,',') within group (order by null) roleCode, country "
              + " from SR_CATALOG "
              + " group by SERVICE_CODE, COUNTRY,STATUS";
    } else if ("UNIT".equals(type)) {
      sql =
          "select SERVICE_CODE serviceCode,STATUS status, listagg(EXECUTION_UNIT,',') within group (order by null) executionUnit, country "
              + " from SR_CATALOG "
              + " group by SERVICE_CODE, COUNTRY, STATUS";
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
  }

  @Override
  public List<SRCatalogDTO> getListCatalog(SRCatalogDTO dto) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById("SRCatalog", "getListCatalog");
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getServiceCode())) {
        sql += (" AND t1.service_code = :serviceCode ");
        parameters.put("serviceCode", dto.getServiceCode());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getExecutionUnit())) {
        sql += (" AND T1.EXECUTION_UNIT = :executionUnit ");
        parameters.put("executionUnit", dto.getExecutionUnit());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getCountry())) {
        sql += (" AND T1.COUNTRY = :country ");
        parameters.put("country", dto.getCountry());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRCatalogDTO.class));
  }
}
