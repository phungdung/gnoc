package com.viettel.gnoc.security.repository;

import com.viettel.gnoc.security.dto.ConfigPropertyDTO;
import com.viettel.gnoc.security.dto.RolesDTO;
import com.viettel.gnoc.security.dto.UsersDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author TungPV
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class UserRepositoryImpl extends BaseRepository implements UserRepository {

  @Override
  public UsersDto getUserDTOByUserName(String userName) {
    StringBuffer sql = new StringBuffer();
    Map<String, Object> parameters = new HashMap<>();
    sql.append("select us.username username, us.fullname fullname, us.user_id userId, "
        + "   us.unit_id unitId, ut.unit_name unitName, "
        + "   us.mobile, us.email email, gl.language_key languageKey "
        + "   from common_gnoc.users us "
        + "   left join common_gnoc.unit ut on us.unit_id = ut.unit_id "
        + "   left join common_gnoc.gnoc_language gl on us.user_language = to_char(gl.gnoc_language_id) "
        + "   where us.is_enable=1 ");
    if (userName != null && !"".equals(userName)) {
      sql.append(" and us.userName = :userName ");
      parameters.put("userName", userName);
    }
    List<UsersDto> datas = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters, BeanPropertyRowMapper
            .newInstance(UsersDto.class));
    if (datas != null && datas.size() > 0) {
      return datas.get(0);
    }
    return null;
  }

  @Override
  public List<RolesDTO> getRolesByUser(Long userId) {
    StringBuffer sql = new StringBuffer();
    Map<String, Object> parameters = new HashMap<>();
    sql.append("select B.*\n"
        + "from COMMON_GNOC.ROLE_USER A\n"
        + "join COMMON_GNOC.ROLES B\n"
        + "on A.ROLE_ID = B.ROLE_ID\n"
        + "where A.USER_ID = :userId and A.IS_ACTIVE = 1 and B.STATUS = 1");
    if (userId != null) {
      parameters.put("userId", userId);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters, BeanPropertyRowMapper
            .newInstance(RolesDTO.class));
  }

  @Override
  public Map<String, String> getConfigProperty() {
    List<ConfigPropertyDTO> result;
    Map<String, String> mapResult = new HashMap<String, String>();
    try {
      String sql = " select a.key, a.value from common_gnoc.config_property a";
      Map<String, String> params = new HashMap<>();
      result = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
      if (result != null && result.size() > 0) {
        result.forEach( c -> {
          mapResult.put(c.getKey(), c.getValue());
        });
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
    return mapResult;
  }
}
