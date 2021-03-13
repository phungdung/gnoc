package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrConfigRepositoryImpl extends BaseRepository implements MrConfigRepository {

  @Override
  public List<MrConfigDTO> getConfigByGroup(String configGroup) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CONFIG, "get-Config-By-Group");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("configGroup", configGroup);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrConfigDTO.class));
  }
}
