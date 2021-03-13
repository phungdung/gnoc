package com.viettel.gnoc.mr.repository;


import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCdBatteryCreateWoRepositoryImpl extends BaseRepository implements
    MrCdBatteryCreateWoRepository {

  @Override
  public boolean isValidCreateWo(String stationCode, String dcPower) {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = "select count(CREATE_WO_TIME) as count from OPEN_PM.MR_CD_BATTERY_CREATE_WO_HIS where 1=1 ";
      if (StringUtils.isNotNullOrEmpty(stationCode)) {
        sql += " AND STATION_CODE = :stationCode ";
        params.put("stationCode", stationCode);
      }
      if (StringUtils.isNotNullOrEmpty(dcPower)) {
        sql += " AND DC_POWER = :dcPower ";
        params.put("dcPower", dcPower);
      }
      sql += "AND TRUNC(sysdate,'MONTH') = TRUNC(CREATE_WO_TIME,'MONTH') having count(CREATE_WO_TIME) < 3";

      List<MrCdBatteryDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrCdBatteryDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return true;
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return false;
  }
}
