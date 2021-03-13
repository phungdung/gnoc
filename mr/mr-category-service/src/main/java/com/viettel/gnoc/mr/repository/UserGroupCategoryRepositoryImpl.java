package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserGroupCategoryRepositoryImpl extends BaseRepository implements
    UserGroupCategoryRepository {

  @Override
  public List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO dto) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MR_MAINTENANCE_MNGT, "get-List-User-Group-By-System");
    params.put("leeLocale", StringUtils.isStringNullOrEmpty(dto.getProxyLocale()) ? I18n.getLocale()
        : dto.getProxyLocale());
    if (!StringUtils.isStringNullOrEmpty(dto.getUgcySystem())) {
      sql += " AND leugc.ugcy_system = :ugcySystem ";
      params.put("ugcySystem", dto.getUgcySystem());
    }
    sql += " ORDER BY leugc.ugcy_code";
    List<UserGroupCategoryDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
        BeanPropertyRowMapper.newInstance(UserGroupCategoryDTO.class));
    return list;
  }
}
