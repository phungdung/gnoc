package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.incident.dto.ActionInfoDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.model.TroubleActionLogsEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroubleActionLogsRepositoryImpl extends BaseRepository implements
    TroubleActionLogsRepository {

  @Override
  public ResultInSideDto insertTroubleActionLogs(TroubleActionLogsEntity entity) {
    return insertByModel(entity, "id");
  }

  @Override
  public List<TroubleActionLogsDTO> getListTroubleActionLogsDTO(TroubleActionLogsDTO dto, int start,
      int maxResult, String sortType, String sortField) {
    return onSearchEntity(TroubleActionLogsEntity.class, dto, start, maxResult, sortType,
        sortField);
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    TroubleActionLogsEntity troublesEntity = getEntityManager()
        .find(TroubleActionLogsEntity.class, id);
    getEntityManager().remove(troublesEntity);
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteTroubleActionLogsByTroubleId(Long troubleId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<TroubleActionLogsEntity> list = (List<TroubleActionLogsEntity>) findByMultilParam(
        TroubleActionLogsEntity.class, "troubleId", troubleId);
    if (list != null && list.size() > 0) {
      for (TroubleActionLogsEntity entity : list) {
        getEntityManager().remove(entity);
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<String> onSearchActionInfo(ActionInfoDTO dto) {
    try {
      Map<String, Object> parameters = new HashMap<>();
      List<String> listInsertSource = new ArrayList<>();
      if (!DataUtil.isNullOrEmpty(dto.getInsertSource())) {
        listInsertSource = Arrays.asList(dto.getInsertSource().split(";"));
      }
      String sql = "SELECT a.action_info"
          + "   FROM   one_tm.TROUBLE_ACTION_LOGS a"
          + "   WHERE  1 = 1  ";
      if (dto != null) {
        if (dto.getTimeFrom() != null && !"".equals(dto.getTimeFrom())) {
          sql += " AND a.create_time >= to_date(:createdTimeFrom,'dd/MM/yyyy HH24:MI:ss') ";
          parameters.put("createdTimeFrom", dto.getTimeFrom());
        }
        if (dto.getTimeTo() != null && !"".equals(dto.getTimeTo())) {
          sql += " AND a.create_time <= to_date(:createdTimeTo,'dd/MM/yyyy HH24:MI:ss') ";
          parameters.put("createdTimeTo", dto.getTimeTo());
        }
        if (!DataUtil.isNullOrEmpty(dto.getInsertSource())) {
          sql += " and a.INSERT_SOURCE in (:insertSource)";
          parameters.put("insertSource", listInsertSource);
        }
      }

      return getNamedParameterJdbcTemplate().queryForList(sql, parameters, String.class);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
