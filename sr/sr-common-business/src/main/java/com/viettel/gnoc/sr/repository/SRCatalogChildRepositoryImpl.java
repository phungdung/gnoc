package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRCatalogChildDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRCatalogChildRepositoryImpl extends BaseRepository implements
    SRCatalogChildRepository {


  @Override
  public List<SRCatalogChildDTO> getListCatalogChild(SRCatalogChildDTO srCatalogChildDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CATALOG_CHILD, "sr-catalog-child-list");
    Map<String, Object> parameters = new HashMap<>();
    if (srCatalogChildDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getExecutionUnitChild())) {
        sqlQuery += (" AND b2.EXECUTION_UNIT in ( ");
        String[] arrUnit = srCatalogChildDTO.getExecutionUnitChild().split(",");
        String unitStr = "";
        for (String unitId : arrUnit) {
          unitStr += "'" + unitId.trim() + "',";
        }
        if (unitStr.endsWith(",")) {
          unitStr = unitStr.substring(0, unitStr.length() - 1);
        }
        sqlQuery += " ";
        sqlQuery += (unitStr);
        sqlQuery += (" ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getServiceCode())) {
        sqlQuery += " AND b1.SERVICE_CODE = :serviceCode ";
        parameters.put("serviceCode", srCatalogChildDTO.getServiceCode());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getServiceId())) {
        sqlQuery += " AND b1.SERVICE_ID = :serviceId ";
        parameters.put("serviceId", srCatalogChildDTO.getServiceId());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getServiceIdChild())) {
        sqlQuery += " AND b1.SERVICE_ID_CHILD = :serviceIdChild ";
        parameters.put("serviceIdChild", srCatalogChildDTO.getServiceIdChild());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getStatus())) {
        sqlQuery += " AND b2.STATUS = :status ";
        parameters.put("status", srCatalogChildDTO.getStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getGenerateNo())) {
        sqlQuery += " AND NVL(b1.GENERATE_NO,0) = :generateNo ";
        parameters.put("generateNo", srCatalogChildDTO.getGenerateNo());
      }
    }
    sqlQuery += " order by b2.SERVICE_NAME desc ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRCatalogChildDTO.class));
  }

  @Override
  public String checkGenerateNo(SRCatalogChildDTO srCatalogChildDTO) {
    String temp = null;
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CATALOG_CHILD, "checkGenerateNo");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("serviceCode", srCatalogChildDTO.getServiceCode());
    parameters.put("serviceId", srCatalogChildDTO.getServiceId());
    parameters.put("status", srCatalogChildDTO.getStatus());
    List<BaseDto> lst = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BaseDto.class));
    if (lst != null && !lst.isEmpty()) {
      temp = lst.get(0).getTimeOffset();
    }
    return temp;
  }
}
