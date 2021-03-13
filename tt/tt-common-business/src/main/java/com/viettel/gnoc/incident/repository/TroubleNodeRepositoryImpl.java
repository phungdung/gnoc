package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.incident.dto.TroubleNodeDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroubleNodeEntity;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroubleNodeRepositoryImpl extends BaseRepository implements
    TroubleNodeRepository {

  @Override
  public ResultInSideDto insertTroubleNode(TroubleNodeEntity entity) {
    return insertByModel(entity, "id");
  }

  @Override
  public Datatable getListTroubleNodeDTO(TroublesInSideDTO dto) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_NODE,
        "get_trouble_nodes_by_trouble_id");
    sql += " AND TROUBLE_ID =:troubleId";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("troubleId", dto.getTroubleId());
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), TroubleNodeDTO.class, dto.getSortName(),
        dto.getSortType());
  }
}
