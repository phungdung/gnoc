package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.incident.dto.CfgSupportFormDTO;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroublesTickHelpRepositoryImpl extends BaseRepository implements
    TroublesTickHelpRepository {


  @Override
  public Datatable getListInfoTickHelpByWoCode(CfgSupportFormDTO dto) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_INFO_TICK_HELP,
        "get-list-info-tick-help-by-wo-code");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woCode", dto.getWoCode());
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(), dto.getPageSize(),
        CfgSupportFormDTO.class, dto.getSortName(), dto.getSortType());
  }
}
