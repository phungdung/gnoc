package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.maintenance.dto.MrUserCfgApprovedSmsBtsDTO;
import com.viettel.gnoc.maintenance.model.MrScheduleBtsHisDetailEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

@Repository
@Slf4j
public class MrScheduleBtsHisRepositoryImpl extends BaseRepository implements
    MrScheduleBtsHisRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  MrUserCfgApprovedSmsBtsRepository mrUserCfgApprovedSmsBtsRepository;


  @Override
  public Datatable getListMrScheduleBtsHisDTO(MrScheduleBtsHisDTO mrScheduleBtsHisDTO) {
    BaseDto baseDto = sqlSearch(mrScheduleBtsHisDTO, false);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mrScheduleBtsHisDTO.getPage(), mrScheduleBtsHisDTO.getPageSize(),
        MrScheduleBtsHisDTO.class,
        mrScheduleBtsHisDTO.getSortName(), mrScheduleBtsHisDTO.getSortType());
  }

  @Override
  public List<MrScheduleBtsHisDTO> getDataExport(MrScheduleBtsHisDTO mrScheduleBtsHisDTO) {
    BaseDto baseDto = sqlSearch(mrScheduleBtsHisDTO, false);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDTO.class));
  }

  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoBts(
      MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_BTS_HIS, "getListWoBts");
    Map<String, Object> parameters = new HashMap<>();
    if (mrScheduleBtsHisDetailDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDetailDTO.getWoCode())) {
        sql += " AND t1.WO_CODE =:woCode ";
        parameters.put("woCode", mrScheduleBtsHisDetailDTO.getWoCode());
      }
    }
    sql += " ORDER BY t1.CHECKLIST_ID ASC";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailInsiteDTO.class));
  }

  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_BTS_HIS,
            "getListWoCodeMrScheduleBtsHisDetail");
    Map<String, Object> parameters = new HashMap<>();
    if (mrScheduleBtsHisDetailDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDetailDTO.getSerial())) {
        sql += " AND m.SERIAL =:serial ";
        parameters.put("serial", mrScheduleBtsHisDetailDTO.getSerial());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDetailDTO.getDeviceType())) {
        sql += " AND m.DEVICE_TYPE =:deviceType ";
        parameters.put("deviceType", mrScheduleBtsHisDetailDTO.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDetailDTO.getCycle())) {
        sql += " AND m.CYCLE =:cycle ";
        parameters.put("cycle", mrScheduleBtsHisDetailDTO.getCycle());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDetailDTO.getWoCode())) {
        sql += " AND (m.WO_CODE_ORIGINAL =:woCode OR m.WO_CODE =:woCode) ";
        parameters.put("woCode", mrScheduleBtsHisDetailDTO.getWoCode());
      }
    }
    sql += " ORDER BY m.wo_code DESC";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailInsiteDTO.class));
  }

  @Override
  public ResultInSideDto editHisDetail(MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrScheduleBtsHisDetailEntity entity = getEntityManager()
        .find(MrScheduleBtsHisDetailEntity.class,
            mrScheduleBtsHisDetailDTO.getScheduleBtsHisDetailId());
    entity.setTaskApprove(mrScheduleBtsHisDetailDTO.getTaskApprove());
    entity.setApproveUser(mrScheduleBtsHisDetailDTO.getApproveUser());
    entity.setApproveDate(mrScheduleBtsHisDetailDTO.getApproveDate());
    entity.setReason(mrScheduleBtsHisDetailDTO.getReason());
    //trungduong them noi dung phe duyet cua khu vuc
    entity.setTaskApproveArea(mrScheduleBtsHisDetailDTO.getTaskApproveArea());
    entity.setApproveUserArea(mrScheduleBtsHisDetailDTO.getApproveUserArea());
    entity.setApproveDateArea(mrScheduleBtsHisDetailDTO.getApproveDateArea());
    entity.setReasonArea(mrScheduleBtsHisDetailDTO.getReasonArea());
    getEntityManager().merge(entity);
    return resultInSideDto;
  }

  @Override
  public String updateMrScheduleBts(String serial, String deviceType, String cycle) {
    try {
      String sql = " UPDATE OPEN_PM.MR_SCHEDULE_BTS SET IS_WO_ORIGINAL = 1 "
          + " WHERE 1=1 AND SERIAL = :p_serial AND DEVICE_TYPE = :p_deviceType AND CYCLE = :p_cycle";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("p_serial", serial);
      parameters.put("p_deviceType", deviceType);
      parameters.put("p_cycle", cycle);
      getNamedParameterJdbcTemplate().update(sql, parameters);
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return e.getMessage();
    }
  }

  @Override
  public String updateMrScheduleBtsHis(String serial, String deviceType, String cycle,
      String woCode, String isComplete, String finishTime) {
    try {
      String sql = " UPDATE OPEN_PM.MR_SCHEDULE_BTS_HIS SET IS_COMPLETE = :p_isComplete "
          + " WHERE 1=1 AND SERIAL = :p_serial AND DEVICE_TYPE = :p_deviceType AND CYCLE = :p_cycle and WO_CODE=:p_woCode";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("p_isComplete", isComplete);
      parameters.put("p_serial", serial);
      parameters.put("p_deviceType", deviceType);
      parameters.put("p_cycle", cycle);
      parameters.put("p_woCode", woCode);
      if (StringUtils.isNotNullOrEmpty(finishTime)) {
        sql =
            " UPDATE OPEN_PM.MR_SCHEDULE_BTS_HIS SET IS_COMPLETE = :p_isComplete, COMPLETE_DATE = TO_DATE(:complete_date, 'dd/mm/yyyy hh24:mi:ss') "
                + " WHERE 1=1 AND SERIAL = :p_serial AND DEVICE_TYPE = :p_deviceType AND CYCLE = :p_cycle and WO_CODE=:p_woCode ";
        parameters.put("complete_date", finishTime);
      }

      getNamedParameterJdbcTemplate().update(sql, parameters);
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return e.getMessage();
    }
  }

  @Override
  public ResultInSideDto insertHisDetail(
      MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrScheduleBtsHisDetailEntity mrScheduleBtsHisDetailEntity = getEntityManager()
        .merge(mrScheduleBtsHisDetailDTO.toEntity());
    resultInSideDto.setId(mrScheduleBtsHisDetailEntity.getScheduleBtsHisDetailId());
    return resultInSideDto;
  }

  public ResultInSideDto deleteMrScheduleBtsHisByWoCode(
      MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String sql = "";
    Map<String, Object> parameters = new HashMap<>();
    if (mrScheduleBtsHisDetailDTO != null) {
      sql = "DELETE FROM OPEN_PM.MR_SCHEDULE_BTS_HIS_DETAIL WHERE WO_CODE = :woCode";
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDetailDTO.getWoCode())) {
        parameters.put("woCode", mrScheduleBtsHisDetailDTO.getWoCode());
      }
    }
    int row = getNamedParameterJdbcTemplate().update(sql, parameters);
    resultInSideDto.setQuantitySucc(row);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertReassignWO(String woCodeOld, String woCodeNew) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String sql = "";
    Map<String, Object> parameters = new HashMap<>();
    sql = "INSERT INTO open_pm.mr_schedule_bts "
        + "SELECT mr_schedule_bts_seq.nextval, "
        + "  MARKET_CODE, "
        + "  AREA_CODE, "
        + "  STATION_CODE, "
        + "  DEVICE_TYPE, "
        + "  SERIAL, "
        + "  CYCLE, "
        + "  MR_CODE, "
        + "  '" + woCodeNew + "', "
        + "  MODIFY_DATE, "
        + "  MR_DATE, " //insert vao cot NEXT_DATE_MODIFY
        + "  USER_MANAGER, "
        + "  PROVINCE_CODE, "
        + "  0, "
        + "  0, "
        + "  0, "
        + "  '" + woCodeOld + "' "
        + "FROM mr_schedule_bts_his "
        + "WHERE wo_code = :woCode";
    parameters.put("woCode", woCodeOld);
    int row = getNamedParameterJdbcTemplate().update(sql, parameters);
    resultInSideDto.setQuantitySucc(row);
    return resultInSideDto;
  }

  public BaseDto sqlSearch(MrScheduleBtsHisDTO mrScheduleBtsHisDTO, boolean isExportDetail) {
    UserToken userToken = ticketProvider.getUserToken();
    Map<String, Object> parameters = new HashMap<>();
    BaseDto baseDto = new BaseDto();
    StringBuffer sqlQuery = new StringBuffer();
    //Lay userLogin ben bang cau hinh UserApprove SMS tu UserLogin
    MrUserCfgApprovedSmsBtsDTO userCfgApprovedSmsBtsDTO = mrUserCfgApprovedSmsBtsRepository
        .getApproveLevelByUserLogin(userToken.getUserName());
    String querySearch_1 = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_BTS_HIS,
            "Search-Query01");
        /*Neu user login ton tai trong bang cau hinh va co Khu vuc = Khu vuc cua thiet bi
      va tinh trong cau hinh == null thi hien thi cac records tuong ung.*/
    String querySearch_2 = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_BTS_HIS,
            "search-Query02");
    if (isExportDetail) {
      //SQL OLD
      querySearch_1 = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_BTS_HIS,
              "search-Query-Detail01");

        /*Neu user login ton tai trong bang cau hinh va co Khu vuc = Khu vuc cua thiet bi
        va tinh trong cau hinh == null thi hien thi cac records tuong ung.*/
      querySearch_2 = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_BTS_HIS,
              "search-Query-Detail02");
    }

    if (!StringUtils.isStringNullOrEmpty(userToken.getUserName())
        && userCfgApprovedSmsBtsDTO != null) {
      sqlQuery.append(querySearch_2);
      parameters.put("userName", userToken.getUserName());
    } else if (userCfgApprovedSmsBtsDTO == null) {
      sqlQuery.append(querySearch_1);
      sqlQuery.append(" AND T1.USER_MANAGER =:userManager ");
      parameters.put("userManager", userToken.getUserName());
    }

    if (mrScheduleBtsHisDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrScheduleBtsHisDTO.getSearchAll())) {
        sqlQuery.append(
            " AND (lower(T1.SERIAL) LIKE :searchAll ESCAPE '\\' OR lower(T1.STATION_CODE) LIKE :searchAll ESCAPE '\\' OR lower(T1.WO_CODE) LIKE :searchAll ESCAPE '\\' )");
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(mrScheduleBtsHisDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getMarketCode())) {
        sqlQuery.append(" AND T1.MARKET_CODE = :marketCode ");
        parameters.put("marketCode", mrScheduleBtsHisDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getAreaCode())) {
        sqlQuery.append(" AND T1.AREA_CODE = :areaCode ");
        parameters.put("areaCode", mrScheduleBtsHisDTO.getAreaCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getProvinceCode())) {
        sqlQuery.append(" AND T1.PROVINCE_CODE = :provinceCode ");
        parameters.put("provinceCode", mrScheduleBtsHisDTO.getProvinceCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getDeviceType())) {
        sqlQuery.append(" AND T1.DEVICE_TYPE = :deviceType ");
        parameters.put("deviceType", mrScheduleBtsHisDTO.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getStationCode())) {
        sqlQuery.append(" AND LOWER(T1.STATION_CODE) LIKE :stationCode ESCAPE '\\' ");
        parameters.put("stationCode",
            StringUtils.convertLowerParamContains(mrScheduleBtsHisDTO.getStationCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getWoCode())) {
        sqlQuery.append(" AND LOWER(T1.WO_CODE) LIKE :woCode ESCAPE '\\' ");
        parameters
            .put("woCode", StringUtils.convertLowerParamContains(mrScheduleBtsHisDTO.getWoCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getCycle())) {
        sqlQuery.append(" AND T1.CYCLE = :cycle ");
        parameters.put("cycle", mrScheduleBtsHisDTO.getCycle());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getUserManager())) {
        sqlQuery.append(" AND LOWER(T1.USER_MANAGER) LIKE :userManager ESCAPE '\\' ");
        parameters.put("userManager",
            StringUtils.convertLowerParamContains(mrScheduleBtsHisDTO.getUserManager()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getSerial())) {
        sqlQuery.append(" AND (lower(T1.SERIAL) LIKE :serial ESCAPE '\\' )");
        parameters
            .put("serial", StringUtils.convertLowerParamContains(mrScheduleBtsHisDTO.getSerial()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getCompleteDateFrom())
          && !StringUtils
          .isStringNullOrEmpty(mrScheduleBtsHisDTO.getCompleteDateTo())) {
        sqlQuery.append(
            " AND (trunc(T1.COMPLETE_DATE) BETWEEN  TO_DATE('" + mrScheduleBtsHisDTO
                .getCompleteDateFrom()
                + "','dd/MM/yyyy') AND TO_DATE('" + mrScheduleBtsHisDTO.getCompleteDateTo()
                + "','dd/MM/yyyy'))");
      }

      if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getIsComplete())) {
        String isComplete = String.valueOf(mrScheduleBtsHisDTO.getIsComplete());
        if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisDTO.getIsCompleteArea())) {
          String isCompleteArea = String.valueOf(mrScheduleBtsHisDTO.getIsCompleteArea());
          isComplete = isCompleteArea;
          if ("6".equals(isComplete)) {
            //Chua phe duyet cap 2
            sqlQuery.append(" AND ( T1.IS_COMPLETE = 1 ) ");
//            sqlQuery.append(" AND T9.TASK_APPROVE = 1 AND T9.TASK_APPROVE_AREA is null ");
          } else if ("4".equals(isComplete)) {
            //Phe duyet cap 2 OK
            sqlQuery.append(" AND (T1.IS_COMPLETE =:isCompelte ) ");
//            sqlQuery.append(" AND t9.tonTaiTuChoiCap1 = 1 AND t9.tonTaiTuChoiCap2 = 1 ");
//            " AND t9.tonTaiTuChoiCap1 = 1 AND (t9.tonTaiTuChoiCap2 = 1 OR (SYSDATE - t9.dieukiendate >= 30)) ");
            parameters.put("isCompelte", isComplete);
          }
        } else {
          if (!StringUtils.isStringNullOrEmpty(isComplete) && !"3"
              .equals(isComplete) && !"2".equals(isComplete)) {
            sqlQuery.append(" AND (T1.IS_COMPLETE = :isCompelte OR T1.IS_COMPLETE > 2");
            if ("0".equals(isComplete)) {
              sqlQuery.append(" OR T1.IS_COMPLETE is null ");
            }
            sqlQuery.append(") ");
            parameters.put("isCompelte", isComplete);
          }
        }
        if ("2".equals(isComplete)) {
          sqlQuery.append("AND "
              + "      (( T1.IS_COMPLETE = 2 )"
              + "  OR "
              + "        (( T1.IS_COMPLETE  <> 2 "
              + "      OR T1.IS_COMPLETE IS NULL )"
              + "    AND "
              + "          (( wo_goc.create_date         < to_date('01/01/2020','dd/MM/yyyy') "
              + "        AND wo_giao_lai.create_date IS NULL )"
              + "      OR "
              + "        ( "
              + "          ( wo_giao_lai.create_date < to_date('01/01/2020','dd/MM/yyyy')))))) ");

          replaceAll(sqlQuery, "{createDateWo}",
              "AND create_date <= to_date('01/01/2020','dd/MM/yyyy') OR create_date >= to_date('01/01/2020','dd/MM/yyyy')");
        } else {
          replaceAll(sqlQuery, "{createDateWo}",
              "AND create_date >= to_date('01/01/2020','dd/MM/yyyy')");
        }

        if ("3".equals(isComplete)) {
          //OLD Tu choi phe duyet, dang thuc hien lai (Cap 1)
          sqlQuery.append(" AND (wo_giao_lai.STATUS != 8 AND wo_giao_lai.WO_CODE is not null) ");
        } else if ("5".equals(isComplete)) {
          //Tu choi phe duyet dang thuc hien lai (Cap 2)
          sqlQuery.append(" AND T1.IS_COMPLETE = 5 ");
//          sqlQuery.append("AND( t9.tonTaiTuChoiCap2 <> null AND t9.tonTaiTuChoiCap2 <1)");
        }
        if ("0".equals(isComplete) || "6".equals(isComplete)) {
          sqlQuery.append(
              " AND((wo_goc.STATUS  = 8 and wo_giao_lai.STATUS is null) or wo_giao_lai.STATUS = 8) ");
        }
      } else {
        replaceAll(sqlQuery, "{createDateWo}",
            "AND create_date >= to_date('01/01/2020','dd/MM/yyyy') or create_date <= to_date('01/01/2020','dd/MM/yyyy')");
      }

    }
    if (!isExportDetail) {
      sqlQuery.append(" ORDER BY T1.MR_DEVICE_HIS_ID DESC ");
    } else {
      sqlQuery.append(" ORDER BY DT.SERIAL, DT.WO_CODE, DT.CHECKLIST_ID ");
    }
    baseDto.setSqlQuery(sqlQuery.toString());
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public static void replaceAll(StringBuffer sql, String from, String to) {
    int index = sql.indexOf(from);
    while (index != -1) {
      sql.replace(index, index + from.length(), to);
      index += to.length(); // Move to the end of the replacement
      index = sql.indexOf(from, index);
    }
  }

  public String deleteMrScheduleBtsByWoCode(String woCode) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String sql = "";
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(woCode)) {
      sql = "DELETE FROM OPEN_PM.MR_SCHEDULE_BTS WHERE WO_CODE = :woCode";
      if (!StringUtils.isStringNullOrEmpty(woCode)) {
        parameters.put("woCode", woCode);
      }
    }
    int row = getNamedParameterJdbcTemplate().update(sql, parameters);
    resultInSideDto.setQuantitySucc(row);
    return RESULT.SUCCESS;
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoBtsBySerial(String serial) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_BTS, "get-list-WO-bts-by-serial");
      Map<String, Object> params = new HashMap<>();
      if (!StringUtils.isStringNullOrEmpty(serial)) {
        sql += " AND t1.SERIAL = :serial ";
        params.put("serial", serial);
      }
      sql += " ORDER BY t1.CHECKLIST_ID ";

      return getNamedParameterJdbcTemplate()
          .query(sql, params,
              BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailInsiteDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getDataExportDetail(
      MrScheduleBtsHisDTO mrScheduleBtsHisDTO) {
    BaseDto baseDto = sqlSearch(mrScheduleBtsHisDTO, true);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailInsiteDTO.class));
  }
}
