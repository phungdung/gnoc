package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.RequestInputBO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ActionLogRepositoryImpl extends BaseRepository implements
    ActionLogRepository {

  @Override
  public BaseDto getSQLByCode(RequestInputBO requestInputBO) {
    try {
      String sql = "Select SQL_TEXT sqlQuery, FORMAT_DATE timeOffset from common_gnoc.CFG_WEB_SERVICE_SQL where status = 1 and code = :code";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("code", requestInputBO.getCode());
      List<BaseDto> lstResult = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(BaseDto.class));
      if (lstResult != null && !lstResult.isEmpty()) {
        return lstResult.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<Map<String, Object>> getDataFromSQL(String sql, Map<String, Object> params) {
    try {
      List<Map<String, Object>> lst =  getNamedParameterJdbcTemplate().queryForList(sql, params);
      return lst;
    } catch (Exception e) {
      throw e;
    }
  }
}
