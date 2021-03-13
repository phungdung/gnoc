package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
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
public class ReceiveUnitRepositoryImpl extends BaseRepository implements ReceiveUnitRepository {

  @Override
  public Datatable getListReceiveUnitSearch(ReceiveUnitDTO receiveUnitDTO) {
    BaseDto baseDto = sqlSearch(receiveUnitDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        receiveUnitDTO.getPage(),
        receiveUnitDTO.getPageSize(), ReceiveUnitDTO.class, receiveUnitDTO.getSortName(),
        receiveUnitDTO.getSortType());
  }

  @Override
  public ReceiveUnitDTO getReceiveUnit(Long receiveUnit) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "search-Receive-Unit");
    sql += " and u.unit_id = :unitId ";
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("unitId", receiveUnit);
    List<ReceiveUnitDTO> list = getNamedParameterJdbcTemplate().query(sql, parameter,
        BeanPropertyRowMapper.newInstance(ReceiveUnitDTO.class));
    if (list != null && !list.isEmpty()) {
      ReceiveUnitDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  @Override
  public List<ReceiveUnitDTO> getListReceiveUnit() {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "search-Receive-Unit");
    return getNamedParameterJdbcTemplate().query(sql, (Map<String, ?>) null,
        BeanPropertyRowMapper.newInstance(ReceiveUnitDTO.class));
  }

  @Override
  public List<ReceiveUnitDTO> getListReceiveUnit(List<Long> listUnitId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "search-Receive-Unit");
    sql += " and u.unit_id in (:listUnitId)";
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("listUnitId", listUnitId);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameter, BeanPropertyRowMapper.newInstance(ReceiveUnitDTO.class));
  }

  public BaseDto sqlSearch(ReceiveUnitDTO receiveUnitDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "search-Receive-Unit");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(receiveUnitDTO.getUnitName())) {
      sqlQuery += " and lower(u.unit_name) LIKE :unitName ESCAPE '\\' ";
      parameters
          .put("unitName", StringUtils.convertLowerParamContains(receiveUnitDTO.getUnitName()));
    }
    if (StringUtils.isNotNullOrEmpty(receiveUnitDTO.getUnitCode())) {
      sqlQuery += " and lower(u.unit_code) LIKE :unitCode ESCAPE '\\' ";
      parameters
          .put("unitCode", StringUtils.convertLowerParamContains(receiveUnitDTO.getUnitCode()));
    }
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

}
