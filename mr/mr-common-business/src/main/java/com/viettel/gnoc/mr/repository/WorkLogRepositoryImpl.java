package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.WorkLogResultDTO;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import com.viettel.gnoc.wo.model.WorkLogEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WorkLogRepositoryImpl extends BaseRepository implements WorkLogRepository {

  @Override
  public ResultDTO createObject(WorkLogDTO workLogDTO) {
    ResultDTO resultDTO = new ResultDTO();
    WorkLogEntity workLogEntity = getEntityManager().merge(workLogDTO.toEntity());
    resultDTO.setId(String.valueOf(workLogEntity.getWorkLogId()));
    resultDTO.setKey(Constants.RESULT.SUCCESS);
    return resultDTO;
  }

  @Override
  public List<WorkLogResultDTO> getListWorklogSearch(WorkLogDTO wlgDTO) {
    List<WorkLogResultDTO> list = new ArrayList<>();
    try {
      StringBuffer sql = new StringBuffer();
      Map<String, Object> params = new HashMap<>();
      if (I18n.getLocale().toLowerCase().contains("en")) {
        sql.append(" SELECT   a.work_log_id worklogid,\n"
            + "         a.wlg_object_type wlgobjecttype,\n"
            + "         a.wlg_object_id wlgobjectid,\n"
            + "         a.user_id userid,\n"
            + "         a.user_group_action usergroupaction,\n"
            + "         ug.ugcy_name usergroupactionname,\n"
            + "         a.wlg_text wlgtext,\n"
            + "         a.wlg_effort_hours wlgefforthours,\n"
            + "         a.wlg_effort_minutes wlgeffortminutes,\n"
            + "         a.wlg_access_type wlgaccesstype,\n"
            + "         TO_CHAR (a.created_date, 'dd/MM/yyyy HH24:mi:ss') createddate,\n"
            + "         a.wlay_id wlayid,\n"
            + "         u.username username,\n"
            + "         ug.ugcy_name usergroupname\n"
            + "  FROM           open_pm.work_log a\n"
            + "             LEFT JOIN\n"
            + "                 common_gnoc.users u\n"
            + "             ON a.user_id = u.user_id\n"
            + "         LEFT JOIN\n"
            + "             (SELECT   NVL (le.lee_value, ugc.ugcy_name) ugcy_name, ugcy_id\n"
            + "                FROM       open_pm.user_group_category ugc\n"
            + "                       LEFT JOIN\n"
            + "                           (SELECT   *\n"
            + "                              FROM   COMMON_GNOC.language_exchange l\n"
            + "                             WHERE   l.applied_system = 2\n"
            + "                                     AND l.applied_bussiness = 16) le\n"
            + "                       ON ugc.ugcy_id = le.bussiness_id) ug\n"
            + "         ON ug.ugcy_id = a.user_group_action\n"
            + " WHERE   1 = 1");
      } else {
        sql.append(" SELECT a.work_log_id workLogId, a.wlg_object_type wlgObjectType, \n"
            + "    a.wlg_object_id wlgObjectId, a.user_id userId,\n"
            + "    a.user_group_action userGroupAction, "
            + "    ug.ugcy_name userGroupActionName, "
            + "    a.wlg_text wlgText, \n"
            + "    a.wlg_effort_hours wlgEffortHours, a.wlg_effort_minutes wlgEffortMinutes,\n"
            + "    a.wlg_access_type wlgAccessType, "
            + "    TO_CHAR(a.created_date, 'dd/MM/yyyy HH24:mi:ss') createdDate,\n"
            + "    a.wlay_id wlayId, u.username userName,\n"
            + "    ug.ugcy_name userGroupName\n"
            + " FROM work_log a \n"
            + " LEFT JOIN common_gnoc.users u on a.user_id = u.user_id\n"
            + " LEFT JOIN user_group_category ug on ug.ugcy_id = a.user_group_action\n"
            + " where 1 = 1 ");
      }
      //        Query q = session.createSQLQuery(sql.toString());
      //tim kiem theo ma don vi
      if (wlgDTO != null) {
        if (!StringUtils.isStringNullOrEmpty(wlgDTO.getWlgObjectId())) {
          sql.append(" and a.wlg_object_id = :wlObjectId");
          params.put("wlObjectId", wlgDTO.getWlgObjectId());
        }
        if (!StringUtils.isStringNullOrEmpty(wlgDTO.getWlgObjectType())) {
          sql.append(" and a.wlg_object_type = :wlObjectType");
          params.put("wlObjectType", wlgDTO.getWlgObjectType());
        }
      }
      sql.append(" order by a.created_date desc ");

      list = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(WorkLogResultDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }
}
