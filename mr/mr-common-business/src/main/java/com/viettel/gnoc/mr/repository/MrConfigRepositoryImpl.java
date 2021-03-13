package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j

/**
 *
 * @author Dunglv3
 */

public class MrConfigRepositoryImpl extends BaseRepository implements MrConfigRepository {
  @Override
  public List<MrConfigDTO> getConfigByGroup(String configGroup) {
    String sql;
    Map<String, Object> params = new HashMap<>();
    sql = " SELECT CONFIG_GROUP configGroup, "
        + "  CONFIG_CODE configCode, "
        + "  CONFIG_NAME configName, "
        + "  COUNTRY country, "
        + "  CONFIG_VALUE configValue "
        + "FROM MR_CONFIG "
        + "WHERE CONFIG_GROUP = :configGroup "
        + "ORDER BY CONFIG_CODE";
    params.put("configGroup", configGroup);
    return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(MrConfigDTO.class));
  }
}
