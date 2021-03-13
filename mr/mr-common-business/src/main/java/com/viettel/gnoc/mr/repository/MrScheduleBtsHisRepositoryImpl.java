package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrScheduleBtsHisRepositoryImpl extends BaseRepository implements
    MrScheduleBtsHisRepository {


  public List<MrScheduleBtsHisDetailDTO> getListWoBts(String woCode) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_BTS_HIS, "getListWoBts-used-MrService");
    Map<String, Object> param = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(woCode)) {
      sql += " AND t1.WO_CODE =:woCode ";
      param.put("woCode", woCode);
    }
    sql += " ORDER BY t1.CHECKLIST_ID ";
    return getNamedParameterJdbcTemplate()
        .query(sql, param, BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailDTO.class));
  }


}
