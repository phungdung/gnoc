package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.wo.dto.WoCdTempDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoCdTempRepositoryImpl extends BaseRepository implements WoCdTempRepository {

  @Override
  public Long getFtByCdConfig(Long cdId) {
    String sql = "SELECT a.wo_cd_temp_id woCdTempId, a.user_id userId FROM wo_cd_temp a where a.start_time<= sysdate and a.end_time>=sysdate";
    Map<String, Object> parameters = new HashMap<>();
    if (cdId != null && !"".equals(cdId)) {
      sql += " and a.wo_group_id = :cdId";
      parameters.put("cdId", cdId);
      List<WoCdTempDTO> lst = getNamedParameterJdbcTemplate().query(sql, parameters,
          BeanPropertyRowMapper.newInstance(WoCdTempDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0).getUserId();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }


}
