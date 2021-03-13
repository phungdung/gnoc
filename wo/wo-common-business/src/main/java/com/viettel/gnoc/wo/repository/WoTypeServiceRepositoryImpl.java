package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.wo.dto.WoTypeServiceInsideDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoTypeServiceRepositoryImpl extends BaseRepository implements WoTypeServiceRepository {

  @Override
  public WoTypeServiceInsideDTO getTypeService(Long woTypeId, Long serviceId) {
    if (woTypeId != null && serviceId != null) {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE_SERVICE, "get-Type-Service");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("woTypeId", woTypeId);
      parameters.put("serviceId", serviceId);
      List<WoTypeServiceInsideDTO> list = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoTypeServiceInsideDTO.class));
      return list.isEmpty() ? null : list.get(0);
    }
    return null;
  }
}
