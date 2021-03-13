package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.model.TroubleWorklogEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroubleWorklogRepositoryImpl extends BaseRepository implements
    TroubleWorklogRepository {

  @Override
  public ResultInSideDto insertTroubleWorklog(TroubleWorklogEntity troubleWorklogEntity) {
    return insertByModel(troubleWorklogEntity, "id");
  }

  @Override
  public Datatable getListTroubleWorklogByTroubleId(TroubleWorklogInsiteDTO troubleWorklogDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_WORKLOG,
        "get_list_trouble_worklog_by_trouble_id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("troubleId", troubleWorklogDTO.getTroubleId());
    return getListDataTableBySqlQuery(sql, parameters, troubleWorklogDTO.getPage(),
        troubleWorklogDTO.getPageSize(), TroubleWorklogInsiteDTO.class,
        troubleWorklogDTO.getSortName(),
        troubleWorklogDTO.getSortType());
  }

  @Override
  public ResultInSideDto deleteTroubleWorklogByTroubleId(Long troubleId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<TroubleWorklogEntity> list = (List<TroubleWorklogEntity>) findByMultilParam(
        TroubleWorklogEntity.class, "troubleId", troubleId);
    if (list != null && list.size() > 0) {
      for (TroubleWorklogEntity entity : list) {
        getEntityManager().remove(entity);
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }
}
