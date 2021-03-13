package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrProcessWoRepositoryImpl extends BaseRepository implements
    CrProcessWoRepository {

  @Override
  public List<CrProcessWoDTO> getLstWoFromProcessId(String crProcessId) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_WO, "get-lst-wo-from-process-id");
      Map<String, Object> parameters = new HashMap<>();
      if (!StringUtils.isStringNullOrEmpty(crProcessId)) {
        sql += " AND a.cr_process_id in (:cr_process_id) ";
        List<String> lstProcess = Arrays.asList(crProcessId.split(","));
        parameters.put("cr_process_id", lstProcess);
      } else {
        sql += " AND a.cr_process_id = :cr_process_id ";
        parameters.put("cr_process_id", "0");
      }
      List<CrProcessWoDTO> lst = getNamedParameterJdbcTemplate().query(sql, parameters,
          BeanPropertyRowMapper.newInstance(CrProcessWoDTO.class));
      Map<String, String> mapConditionRemove = new HashMap<>();
      if (lst != null && lst.size() > 0) {
        for (int i = lst.size() - 1; i == 0; i--) {
          String key =
              String.valueOf(lst.get(i).getWoTypeId()) + String.valueOf(lst.get(i).getWoName());
          if (!mapConditionRemove.containsKey(key)) {
            mapConditionRemove.put(key, key);
          } else {
            lst.remove(i);
          }
        }
      }
      return lst;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
