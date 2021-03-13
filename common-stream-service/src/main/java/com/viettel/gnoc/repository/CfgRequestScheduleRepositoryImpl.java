package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.RequestScheduleDTO;
import com.viettel.gnoc.cr.dto.ScheduleCRDTO;
import com.viettel.gnoc.cr.dto.ScheduleEmployeeDTO;
import com.viettel.gnoc.cr.model.RequestScheduleEntity;
import com.viettel.gnoc.cr.model.ScheduleCREntity;
import com.viettel.gnoc.cr.model.ScheduleEmployeeEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;


@Repository
@Slf4j
public class CfgRequestScheduleRepositoryImpl extends BaseRepository implements
    CfgRequestScheduleRepository {

  @Override
  public ResultInSideDto insertRequestSchedule(RequestScheduleDTO requestScheduleDTO) {
    RequestScheduleEntity requestScheduleEntity = requestScheduleDTO.toEntity();
    return insertByModel(requestScheduleEntity, colId);
  }

  @Override
  public String deleteRequestSchedule(Long id) {
    return deleteById(RequestScheduleEntity.class, id, colId);
  }

  @Override
  public Datatable getListYear(CatItemDTO catItemDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_REQUEST_SCHEDULE, "getListYear");
    Map<String, Object> parameters = new HashMap<>();
    return getListDataTableBySqlQuery(sql, parameters, catItemDTO.getPage(),
        catItemDTO.getPageSize(), CatItemDTO.class,
        catItemDTO.getSortName(),
        catItemDTO.getSortType());
  }

  @Override
  public Datatable getListRequestSchedule(RequestScheduleDTO requestScheduleDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_REQUEST_SCHEDULE, "getListRequestSchedule");
    Map<String, Object> parameters = new HashMap<>();
    if (requestScheduleDTO.getWeek() != null) {
      sql += " AND TO_CHAR(t1.END_DATE, 'WW') = :week";
      parameters.put("week", requestScheduleDTO.getWeek());
    }
    if (requestScheduleDTO.getMonth() != null) {
      sql += " AND EXTRACT(month from t1.START_DATE) = :month";
      parameters.put("month", requestScheduleDTO.getMonth());
    }
    if (requestScheduleDTO.getYear() != null) {
      sql += " AND EXTRACT(year from t1.START_DATE) = :year";
      parameters.put("year", requestScheduleDTO.getYear());
    }
    if (requestScheduleDTO.getStatus() != null) {
      sql += " AND t1.STATUS = :status";
      parameters.put("status", requestScheduleDTO.getStatus());
    }
    if (requestScheduleDTO.getUnitId() != null) {
      sql += " AND t1.UNIT_ID = :unitId";
      parameters.put("unitId", requestScheduleDTO.getUnitId());
    }
    if (StringUtils.isNotNullOrEmpty(requestScheduleDTO.getSearchAll())) {
      sql += " AND (LOWER(t2.UNIT_NAME) LIKE :searchAll ESCAPE '\\')";
      parameters
          .put("searchAll",
              StringUtils.convertLowerParamContains(requestScheduleDTO.getSearchAll()));
    }

    //tiennv bo sung cap nhat tim theo createUser

    if (StringUtils.isNotNullOrEmpty(requestScheduleDTO.getCreatedUser())) {
      sql += " AND (LOWER(t1.CREATED_USER) LIKE :createUser ESCAPE '\\')";
      parameters
          .put("createUser",
              StringUtils.convertLowerParamContains(requestScheduleDTO.getCreatedUser()));
    }
    if (!StringUtils.isStringNullOrEmpty(requestScheduleDTO.getCreatedDateFrom()) && !StringUtils
        .isStringNullOrEmpty(requestScheduleDTO.getCreatedDateTo())) {
      sql += " AND (t1.CREATED_DATE BETWEEN  TO_DATE(:createdDateFrom  , 'DD-MM-YYYY HH24:MI:SS') AND TO_DATE(:createdDateTo, 'DD-MM-YYYY HH24:MI:SS'))";
      parameters.put("createdDateFrom",
          DateUtil.date2ddMMyyyyString(requestScheduleDTO.getCreatedDateFrom()));
      parameters.put("createdDateTo",
          DateUtil.date2ddMMyyyyString(requestScheduleDTO.getCreatedDateTo()));

    }

    if (requestScheduleDTO.getType() != null) {

      sql += " AND t1.TYPE = :type";
      parameters.put("type", requestScheduleDTO.getType());

      if ("0".equals(requestScheduleDTO.getType()) || "1".equals(requestScheduleDTO.getType())) {

        if (!StringUtils.isStringNullOrEmpty(requestScheduleDTO.getDayFrom()) && !StringUtils
            .isStringNullOrEmpty(requestScheduleDTO.getDayTo())) {
          sql += " AND (t1.START_DATE BETWEEN  TO_DATE(:dayFrom , 'dd/mm/yyyy') AND TO_DATE(:dayTo , 'dd/mm/yyyy'))";
          parameters.put("dayFrom", DateUtil.date2ddMMyyyyString(requestScheduleDTO.getDayFrom()));
          parameters.put("dayTo", DateUtil.date2ddMMyyyyString(requestScheduleDTO.getDayTo()));
        }

        if (!StringUtils.isStringNullOrEmpty(requestScheduleDTO.getYear()) && !"-1"
            .equals(requestScheduleDTO.getYear())) {
          sql += " AND EXTRACT(year from t1.START_DATE) = :getYear";
          parameters.put("getYear", requestScheduleDTO.getYear());
        }
        if (!StringUtils.isStringNullOrEmpty(
            "0".equals(requestScheduleDTO.getType()) ? requestScheduleDTO.getMonth()
                : requestScheduleDTO.getWeek())) {
          sql += " AND t1.DETAIL_SCHEDULE = :detailSchedule";
          String inputParameter = "";
          if (StringUtils.isNotNullOrEmpty(requestScheduleDTO.getMonth())) {
            int monthInt = Integer.valueOf(requestScheduleDTO.getMonth());
            inputParameter = monthInt < 10 ? "0" + monthInt : String.valueOf(monthInt);
          }
          if (StringUtils.isNotNullOrEmpty(requestScheduleDTO.getWeek())) {
            int weekInt = Integer.valueOf(requestScheduleDTO.getWeek());
            inputParameter = weekInt < 10 ? "0" + weekInt : String.valueOf(weekInt);
          }
          parameters.put("detailSchedule", inputParameter);
        }
      } else if ("2".equals(requestScheduleDTO.getType())) {
        if (!StringUtils.isStringNullOrEmpty(requestScheduleDTO.getDays())) {
          sql += " AND (T1.START_DATE = TO_DATE(:days1, 'dd/MM/yyyy') OR T1.END_DATE = TO_DATE(:days2, 'dd/MM/yyyy') )";
          parameters.put("days1", requestScheduleDTO.getDays());
          parameters.put("days2", requestScheduleDTO.getDays());
        }
      }
    } else {
      if (!StringUtils.isStringNullOrEmpty(requestScheduleDTO.getDayFrom()) && !StringUtils
          .isStringNullOrEmpty(requestScheduleDTO.getDayTo())) {
        sql += " AND (t1.START_DATE BETWEEN  TO_DATE(:dayFrom , 'dd/mm/yyyy') AND TO_DATE(:dayTo , 'dd/mm/yyyy'))";
        parameters.put("dayFrom", DateUtil.date2ddMMyyyyString(requestScheduleDTO.getDayFrom()));
        parameters.put("dayTo", DateUtil.date2ddMMyyyyString(requestScheduleDTO.getDayTo()));
      }

    }
    return getListDataTableBySqlQuery(sql, parameters, requestScheduleDTO.getPage(),
        requestScheduleDTO.getPageSize(), RequestScheduleDTO.class,
        requestScheduleDTO.getSortName(),
        requestScheduleDTO.getSortType());
  }

  @Override
  public String updateRequestSchedule(RequestScheduleDTO dto) {
    RequestScheduleEntity requestScheduleEntity = dto.toEntity();
    getEntityManager().merge(requestScheduleEntity);
    return RESULT.SUCCESS;
  }

  @Override
  public RequestScheduleDTO findRequestScheduleById(Long id) {
    if (id != null && id > 0) {
      RequestScheduleDTO requestScheduleDTO = getEntityManager()
          .find(RequestScheduleEntity.class, id).toDTO();
      Date startDate = requestScheduleDTO.getStartDate();
      Calendar cal = Calendar.getInstance();
      cal.setTime(startDate);
      int week = cal.get(Calendar.WEEK_OF_YEAR);
      SimpleDateFormat formatNowYear = new SimpleDateFormat("YYYY");
      SimpleDateFormat formatNowMonth = new SimpleDateFormat("MM");
      String currentYear = formatNowYear.format(startDate);
      String currentMonth = formatNowMonth.format(startDate);
      requestScheduleDTO.setYear(currentYear);
      requestScheduleDTO.setMonth(currentMonth);
      requestScheduleDTO.setWeek(week + "");
      return requestScheduleDTO;
    }
    return null;
  }

  @Override
  public Datatable getListUnit(UnitDTO unitDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_REQUEST_SCHEDULE, "getListUnit");
    Map<String, Object> parameters = new HashMap<>();
    return getListDataTableBySqlQuery(sql, parameters, unitDTO.getPage(),
        unitDTO.getPageSize(), UnitDTO.class,
        unitDTO.getSortName(),
        unitDTO.getSortType());
  }

  @Override
  public List<ScheduleEmployeeDTO> getListEmployee(ScheduleEmployeeDTO scheduleEmployeeDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_REQUEST_SCHEDULE, "getListEmployee");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unitId", scheduleEmployeeDTO.getUnitId());
    parameters.put("idSchedule", scheduleEmployeeDTO.getIdSchedule());
    List<ScheduleEmployeeDTO> datatable = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper
            .newInstance(ScheduleEmployeeDTO.class));
    return datatable;
  }

  @Override
  public List<ScheduleEmployeeDTO> getListEmployee2(ScheduleEmployeeDTO scheduleEmployeeDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_REQUEST_SCHEDULE, "getListScheduleEmployee");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unitId", scheduleEmployeeDTO.getUnitId());
    if (!StringUtils.isStringNullOrEmpty(scheduleEmployeeDTO.getYear())) {
      sql += " AND EXTRACT(year from t2.DAYOFF) = :year ";
      parameters.put("year", scheduleEmployeeDTO.getYear());
    }
    if ("0".equals(scheduleEmployeeDTO.getType())) {
      if (!StringUtils.isStringNullOrEmpty(scheduleEmployeeDTO.getMonth())) {
        sql += " AND EXTRACT(month from t2.DAYOFF) = :detailMonth";
        parameters.put("detailMonth", scheduleEmployeeDTO.getMonth());
      }
    } else if ("1".equals(scheduleEmployeeDTO.getType())) {
      if (!StringUtils.isStringNullOrEmpty(scheduleEmployeeDTO.getWeek())) {
        sql += " AND to_char(t2.DAYOFF,'iw') = :detailWeek";
        parameters.put("detailWeek", scheduleEmployeeDTO.getWeek());
      }
    } else {
      if (!StringUtils.isStringNullOrEmpty(scheduleEmployeeDTO.getDayOff())) {
        sql += "AND to_char(t2.DAYOFF,'dd/MM/yyyy') = :dayOff";
        parameters.put("dayOff", scheduleEmployeeDTO.getDayOff());
      }
    }
    sql += " join tmp3 t3 on t1.userID = t3.empId"
        + " left join COMMON_GNOC.CAT_ITEM t4 ON t3.empLevel = t4.ITEM_VALUE "
        + " and t4.CATEGORY_ID in (select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE = 'GNOC_IMPACT')"
        + " where 1=1 ";

    sql += " group by t1.unitId, t1.userId, t1.username, t1.fullName , t1.username, t3.empArray,t3.empLevel, t3.empChildren,t3.arrayChildName, t4.item_name";
    List<ScheduleEmployeeDTO> datatable = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper
            .newInstance(ScheduleEmployeeDTO.class));
    return datatable;
  }

  @Override
  public List<ScheduleCRDTO> getListScheduleCR(ScheduleCRDTO scheduleCRDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_REQUEST_SCHEDULE, "getListScheduleCR");
    Map<String, Object> parameters = new HashMap<>();
    if (scheduleCRDTO.getType() != null) {
      sql += " AND t1.TYPE = :type ";
      parameters.put("type", scheduleCRDTO.getType());
    }
    if (scheduleCRDTO.getIdSchedule() != null) {
      sql += " AND t1.ID_SCHEDULE = :idSchedule ";
      parameters.put("idSchedule", scheduleCRDTO.getIdSchedule());
    }
    sql += " order by t1.REGISTRATION_DATE";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ScheduleCRDTO.class));
  }

  @Override
  public String insertListDayOffDTO(List<ScheduleEmployeeDTO> lst) {
    for (ScheduleEmployeeDTO dto : lst) {
      ScheduleEmployeeEntity entity = dto.toEntity();
      if (entity.getIdEmployee() != null && entity.getIdEmployee() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public String insertListCRDTO(List<ScheduleCRDTO> lstScheduleTmp) {
    for (ScheduleCRDTO dto : lstScheduleTmp) {
      ScheduleCREntity entity = dto.toEntity();
      if (entity.getId() != null && entity.getId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public boolean checkExisted(RequestScheduleDTO dto) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_REQUEST_SCHEDULE, "checkExisted");
      Map<String, Object> parameters = new HashMap<>();
      if (!StringUtils.isStringNullOrEmpty(dto.getStatus())) {
        sql += " AND t1.STATUS = :status";
        parameters.put("status", dto.getStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getType())) {
        sql += "  AND t1.TYPE = :type ";
        parameters.put("type", dto.getType());
        if ("0".equals(dto.getType()) || "1".equals(dto.getType())) {

          if (!StringUtils.isStringNullOrEmpty(dto.getYear()) && !"-1".equals(dto.getYear())) {
            sql += " AND EXTRACT(year from T1.START_DATE) = :year ";
            parameters.put("year", dto.getYear());
          }
          if (!StringUtils
              .isStringNullOrEmpty("0".equals(dto.getType()) ? dto.getMonth() : dto.getWeek())) {
            sql += " AND t1.DETAIL_SCHEDULE = :var";
            parameters.put("var", ("0".equals(dto.getType()) ? dto.getMonth() : dto.getWeek()));
          }
        } else if ("2".equals(dto.getType())) {
          if (!StringUtils.isStringNullOrEmpty(dto.getDays())) {
            sql += "AND (t1.START_DATE = TO_DATE(:startDate, 'dd/MM/yyyy') OR t1.END_DATE = TO_DATE(:endDate, 'dd/MM/yyyy') ) ";
            parameters.put("startDate", dto.getDays());
            parameters.put("endDate", dto.getDays());
          }
        }
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getIdSchedule())) {
        sql += " AND ID_SCHEDULE <> :id ";
        parameters.put("id", dto.getIdSchedule());
      }
      List<RequestScheduleDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters,
              BeanPropertyRowMapper.newInstance(RequestScheduleDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return true;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.getMessage();
      return true;
    }
    return false;
  }

  @Override
  public List<CrInsiteDTO> getListCrAutoSchedule(CrInsiteDTO crInsiteDTO) {
    try {
      StringBuilder sqlCr = new StringBuilder();
      sqlCr.append(
          "select cr.CR_ID crId from OPEN_PM.CR cr  WHERE cr.EARLIEST_START_TIME >= to_date(:earliestStartTime, 'dd/MM/yyyy HH24:MI:ss')"
              + " AND cr.EARLIEST_START_TIME   <= to_date(:earliestStartTimeTo, 'dd/MM/yyyy HH24:MI:ss')"
              + " AND cr.CHANGE_RESPONSIBLE_UNIT =  :changeResponsibleUnit ");
      if (StringUtils.isNotNullOrEmpty(crInsiteDTO.getState())) {
        String[] arrState = crInsiteDTO.getState().split(",");
        if (arrState != null && arrState.length > 0) {
          sqlCr.append(" and cr.state in ( ");
          for (int i = 0; i < arrState.length; i++) {
            sqlCr.append(arrState[i] + ", ");
          }
          sqlCr.append("-1) ");
        }
      }

      if (StringUtils.isNotNullOrEmpty(crInsiteDTO.getDutyType())) {
        String[] arrDuty = crInsiteDTO.getDutyType().split(",");
        if (arrDuty != null && arrDuty.length > 0) {
          sqlCr.append(" and cr.DUTY_TYPE in ( ");
          for (int i = 0; i < arrDuty.length; i++) {
            sqlCr.append(arrDuty[i] + ", ");
          }
          sqlCr.append("-1) ");
        }
      }

      Map<String, Object> parameters = new HashMap<>();
      parameters.put("earliestStartTime",
          DateUtil.date2ddMMyyyyHHMMss(crInsiteDTO.getEarliestStartTime()));
      parameters.put("earliestStartTimeTo", crInsiteDTO.getEarliestStartTimeTo());
      parameters.put("changeResponsibleUnit", crInsiteDTO.getChangeResponsibleUnit());
      parameters.put("dutyType", crInsiteDTO.getDutyType());

      if (StringUtils.isNotNullOrEmpty(crInsiteDTO.getCountry())) {
        sqlCr.append("AND cr.COUNTRY = :country ");
        parameters.put("country", crInsiteDTO.getCountry());
      }

      List<CrDTO> crDTOS = getNamedParameterJdbcTemplate()
          .query(sqlCr.toString(), parameters, BeanPropertyRowMapper.newInstance(CrDTO.class));
      List<CrInsiteDTO> lst = new ArrayList<>();
      List<String> lstCrId = new ArrayList<>();
      if (crDTOS == null || crDTOS.isEmpty()) {
        return null;
      } else {
        for (CrDTO cdto : crDTOS) {
          lstCrId.add(cdto.getCrId());
        }
      }
      int n = lstCrId.size() / 500;
      if (lstCrId.size() % 500 != 0) {
        n++;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_REQUEST_SCHEDULE, "getListCrAutoSchedule");
      for (int j = 1; j <= n; j++) {
        List<String> lstCrTempId;
        if (j < n) {
          lstCrTempId = lstCrId.subList((j - 1) * 500, j * 500);
        } else {
          lstCrTempId = lstCrId.subList((j - 1) * 500, lstCrId.size());
        }
        Map<String, Object> queryUser = new HashMap<>();
        queryUser.put("lstCr1", lstCrTempId);
        queryUser.put("lstCr2", lstCrTempId);
        queryUser.put("lstCr3", lstCrTempId);
        queryUser.put("lstCr4", lstCrTempId);
        List<CrInsiteDTO> lstCr = getNamedParameterJdbcTemplate()
            .query(sql, queryUser, BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
        if (lstCr == null || lstCr.isEmpty()) {
        } else {
          lst.addAll(lstCr);
        }
      }
      return lst;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<CfgChildArrayDTO> getListCfgChildArray(CfgChildArrayDTO dto) {
    List<CfgChildArrayDTO> lst = new ArrayList<>();
    try {
      StringBuffer sql = new StringBuffer();
      sql.append(SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_REQUEST_SCHEDULE, "get-list-cfgChildArray"));
      Map<String, Object> parameter = new HashMap<>();
      if (!StringUtils.isStringNullOrEmpty(dto.getParentId())) {
        sql.append(" AND t1.PARENT_ID = :parentId");
        parameter.put("parentId", dto.getParentId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getChildrenCode())) {
        sql.append(" AND lower(t1.CHILDREN_CODE) LIKE :childrenCode escape '\\' ");
        parameter.put("childrenCode", StringUtils.convertLowerParamContains(dto.getChildrenCode()));

      }
      if (!StringUtils.isStringNullOrEmpty(dto.getChildrenName())) {
        sql.append(" AND lower(t1.CHILDREN_NAME) LIKE :childrenName escape '\\' ");
        parameter.put("childrenName", StringUtils.convertLowerParamContains(dto.getChildrenName()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getStatus())) {
        sql.append(" AND t1.STATUS = :status ");
        parameter.put("status", dto.getStatus());
      }
      sql.append(" order by t1.UPDATED_TIME DESC");

      lst = getNamedParameterJdbcTemplate().query(sql.toString(), parameter,
          BeanPropertyRowMapper.newInstance(CfgChildArrayDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  private static final String colId = "idSchedule";
}
