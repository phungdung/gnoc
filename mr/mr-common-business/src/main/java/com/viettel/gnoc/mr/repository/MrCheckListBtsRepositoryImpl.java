package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCheckListBtsRepositoryImpl extends BaseRepository implements
    MrCheckListBtsRepository {


  @Override
  public List<MrChecklistsBtsDTO> getListScheduleBtsHisDTO(MrChecklistsBtsDTO dto, int rowStart,
      int maxRow) {
    String sqlQuery;
    Map<String, Object> parameters = new HashMap<>();
    sqlQuery = "SELECT t1.CHECKLIST_ID checkListId, "
        + "  t1.CONTENT content , "
        + "  t1.DEVICE_TYPE deviceType, "
        + "  t1.PHOTO_REQ photoReq, "
        + "  t1.MIN_PHOTO minPhoto, "
        + "  t1.MAX_PHOTO maxPhoto, "
        + "  t1.CYCLE cycle, "
        + "  t1.WO_CODE woCode, "
        + " t1.TASK_STATUS, "
        + "t1.SERIAL "
        + " FROM OPEN_PM.MR_SCHEDULE_BTS_HIS_DETAIL t1 "
        + " WHERE 1 =1 ";
    if (!StringUtils.isStringNullOrEmpty(dto.getWoCode())) {
      sqlQuery += " AND t1.WO_CODE =:woCode ";
      parameters.put("woCode", dto.getWoCode());
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getChecklistId())) {
      sqlQuery += " AND t1.CHECKLIST_ID =:checkListId ";
      parameters.put("checkListId", dto.getChecklistId());
    }
    sqlQuery += " ORDER BY t1.APPROVE_DATE DESC ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrChecklistsBtsDTO.class));
  }

  @Override
  public List<MrChecklistsBtsDTO> getListScheduleBtsHisDTOByWoCodes(List<String> woCodes) {
    String sqlQuery;
    sqlQuery = "SELECT t1.CHECKLIST_ID checkListId, "
        + "  t1.CONTENT content , "
        + "  t1.DEVICE_TYPE deviceType, "
        + "  t1.PHOTO_REQ photoReq, "
        + "  t1.MIN_PHOTO minPhoto, "
        + "  t1.MAX_PHOTO maxPhoto, "
        + "  t1.CYCLE cycle, "
        + "  t1.WO_CODE woCode, "
        + " t1.TASK_STATUS, "
        + "t1.SERIAL "
        + " FROM OPEN_PM.MR_SCHEDULE_BTS_HIS_DETAIL t1 "
        + " WHERE 1 =1 AND t1.WO_CODE in (:idx) ";
    return getListDataByParamList(MrChecklistsBtsDTO.class, sqlQuery, woCodes, null, 500);
  }
}
