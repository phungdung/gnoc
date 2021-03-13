package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.cr.dto.SessionResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrGeneralForMobileRepositoryImpl extends BaseRepository implements
    CrGeneralForMobileRepository {

  //sonvt19 Fix ATTT Gnoc Mobile start
  @Override
  public SessionResponse getSessionInfo(String sessionId) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL_FOR_MOBILE, "get-session-info");
      Map<String, Object> params = new HashMap<>();
      params.put("sessionId", sessionId);
      List<SessionResponse> lst = getNamedParameterJdbcTemplate().query(sql, params,
          BeanPropertyRowMapper.newInstance(SessionResponse.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public String checkSession(String sessionId) {
    String result = "";
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL_FOR_MOBILE, "check-session");
      Map<String, Object> params = new HashMap<>();
      params.put("session_id", sessionId);
      List<SessionResponse> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(SessionResponse.class));
      if (lst != null && !lst.isEmpty()) {
        result = lst.get(0).getUserId();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public String getLocationByUnitId(String unitId) {
    String result = "";
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL_FOR_MOBILE, "get-location-by-unit-id");
      Map<String, Object> params = new HashMap<>();
      params.put("unit_id", unitId);

      List<UnitDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(UnitDTO.class));
      if (lst != null && !lst.isEmpty()) {
        result = lst.get(0).getLocationCode();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }
}
