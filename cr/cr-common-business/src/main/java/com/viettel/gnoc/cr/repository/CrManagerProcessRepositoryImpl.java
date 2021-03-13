package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrProcessDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrManagerProcessRepositoryImpl extends BaseRepository implements
    CrManagerProcessRepository {

  @Override
  public List<CrProcessDTO> getAllCrProcess(Long parentId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getAllCrProcess");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (!StringUtils.isStringNullOrEmpty(parentId)) {
      sql += " and lcp.parent_id=:prarentId ";
      parameters.put("prarentId", parentId);
    } else {
      sql += " and lcp.parent_id is null ";
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrProcessDTO.class));
  }
}
