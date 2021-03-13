package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SR_STATUS;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRActionCodeDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SREvaluateDTO;
import com.viettel.gnoc.sr.dto.SRFilesDTO;
import com.viettel.gnoc.sr.dto.SRRenewDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.model.SRActionCodeEntity;
import com.viettel.gnoc.sr.model.SREntity;
import com.viettel.gnoc.sr.model.SREvaluateEntity;
import com.viettel.gnoc.sr.model.SRFilesEntity;
import com.viettel.gnoc.sr.model.SRRenewEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
@Transactional
public class SrRepositoryImpl extends BaseRepository implements SrRepository {

  @Autowired
  UserRepository userRepository;

  @Override
  public BaseDto sqlSearch(SrInsiteDTO srdto) {
    BaseDto baseDto = new BaseDto();
    Double offset = 0.0;
    String locale = I18n.getLocale();
    if (!StringUtils.isStringNullOrEmpty(srdto.getUsername())) {
      offset = userRepository.getOffsetFromUser(srdto.getUsername());
    }
    srdto = converTimeFromClientToServer(srdto, offset);
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "get-list-SR");
    parameters.put("double_offset", Double.valueOf(offset));
    parameters.put("locale", locale);
    StringBuilder SQLQuery = new StringBuilder();
    SQLQuery.append(sql);
    if (srdto != null) {
      if (StringUtils.isNotNullOrEmpty(srdto.getSearchAll())) {
        StringBuilder sqlSearchAll = new StringBuilder(
            " AND (lower(T1.SRCODE) LIKE :searchAll ESCAPE '\\' ");
        sqlSearchAll.append(" OR lower(T1.TITLE) LIKE :searchAll ESCAPE '\\' ) ");
        SQLQuery.append(sqlSearchAll.toString());
        parameters.put("searchAll", StringUtils.convertLowerParamContains(
            srdto.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getSrCode())) {
        SQLQuery.append(" AND lower(T1.SRCODE) LIKE :SRCODE escape '\\'");
        parameters.put("SRCODE", StringUtils.convertLowerParamContains(srdto.getSrCode()));
      }

      if (!StringUtils.isStringNullOrEmpty(srdto.getCreateFromDate())) {
        SQLQuery.append(
            " AND to_date(T1.CREATEDTIME,'dd/MM/yyyy HH24:mi:ss') >= TO_DATE(:CreateFromDate, 'dd/MM/yyyy HH24:mi:ss')");
        parameters
            .put("CreateFromDate", DateTimeUtils
                .convertDateToString(srdto.getCreateFromDate(), "dd/MM/yyyy HH:mm:ss"));
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getCreateToDate())) {
        SQLQuery.append(
            " AND to_date(T1.CREATEDTIME,'dd/MM/yyyy HH24:mi:ss') <= TO_DATE(:CreateToDate,'dd/MM/yyyy HH24:mi:ss')");
        parameters.put("CreateToDate",
            DateTimeUtils.convertDateToString(srdto.getCreateToDate(), "dd/MM/yyyy HH:mm:ss"));
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getTitle())) {
        SQLQuery.append(" AND lower(T1.TITLE) LIKE :TITLE escape '\\'");
        parameters.put("TITLE", StringUtils.convertLowerParamContains(srdto.getTitle()));
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getStatus())) {
        SQLQuery.append(" AND T1.STATUS IN (:STATUS) ");
        parameters.put("STATUS", Arrays.asList(srdto.getStatus().split(",")));
      }
//      tìm kiếm nâng cao
//      if (srdto.getAdvanced()) {
      if (!StringUtils.isStringNullOrEmpty(srdto.getCountry())) {
        SQLQuery.append(" AND T1.COUNTRY =:COUNTRY ");
        parameters.put("COUNTRY", srdto.getCountry());
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getServiceArray())) {
        SQLQuery.append(" AND T1.SERVICEARRAY =:SERVICEARRAY ");
        parameters.put("SERVICEARRAY", srdto.getServiceArray());
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getSrUnit())) {
        SQLQuery.append(" AND (T1.SRUNIT =:SRUNIT ");
        parameters.put("SRUNIT", srdto.getSrUnit());
        if (!StringUtils.isStringNullOrEmpty(srdto.getSearchParentUnit()) && srdto
            .getSearchParentUnit()) {
          SQLQuery.append(
              " or T1.SRUNIT = (select PARENT_UNIT_ID from COMMON_GNOC.V_UNIT_AS_TREE where UNIT_ID = :SRUNIT) ");
          parameters.put("SRUNIT", srdto.getSrUnit());
        }
        SQLQuery.append(" ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getCreatedUser())) {
        SQLQuery.append(" AND lower(T1.CREATEDUSER) LIKE :CREATEDUSER escape '\\'");
        parameters
            .put("CREATEDUSER", StringUtils.convertLowerParamContains(srdto.getCreatedUser()));
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getServiceGroup())) {
        SQLQuery.append(" AND T1.SERVICEGROUP =:SERVICEGROUP ");
        parameters.put("SERVICEGROUP", srdto.getServiceGroup());
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getCheckingUnit())) {
        SQLQuery.append(
            " AND T1.CREATEDUSER IN (SELECT u.USERNAME FROM COMMON_GNOC.USERS u WHERE UNIT_ID =:CheckingUnit) ");
        parameters.put("CheckingUnit", Long.parseLong(srdto.getCheckingUnit().trim()));
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getServiceCode())) {
        SQLQuery.append(
            " AND T1.SERVICEID in (SELECT SERVICE_ID from OPEN_PM.SR_CATALOG where SERVICE_CODE =:SERVICE_CODE AND STATUS = 'A') ");
        parameters.put("SERVICE_CODE", srdto.getServiceCode());
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getSrUser())) {
        SQLQuery.append(" AND lower(T1.SRUSER) LIKE :SRUSER escape '\\'");
        parameters.put("SRUSER", StringUtils.convertLowerParamContains(srdto.getSrUser()));
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getParentCode())) {
        SQLQuery.append(" AND lower(T1.PARENTCODE) LIKE :PARENTCODE escape '\\'");
        parameters
            .put("PARENTCODE", StringUtils.convertLowerParamContains(srdto.getParentCode()));
      } else {
        SQLQuery.append(" AND T1.PARENTCODE is null  ");
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getStatusRenew())) {
        SQLQuery.append(" AND T1.STATUSRENEW =:STATUSRENEW");
        parameters.put("STATUSRENEW", srdto.getStatusRenew());
      }
      if (!StringUtils.isStringNullOrEmpty(srdto.getRoleCode())) {
        SQLQuery.append(
            " AND (T1.SRUNIT in (select distinct UNIT_ID from OPEN_PM.SR_ROLE_USER where ROLE_CODE in (:ROLECODE) ");
        SQLQuery.append(" ) ");
        SQLQuery.append(" OR T1.ROLECODE is null ) ");
        parameters.put("ROLECODE", Arrays.asList(srdto.getRoleCode().split(",")));
      }
//      }//kết thúc tìm kiếm nâng cao
      //them dieu kien tim kiem cho he thong khac
      if (!StringUtils.isStringNullOrEmpty(srdto.getInsertSource())) {
        SQLQuery.append(" AND T1.INSERTSOURCE =:INSERTSOURCE ");
        parameters.put("INSERTSOURCE", srdto.getInsertSource());
      }

      if (!StringUtils.isStringNullOrEmpty(srdto.getOtherSystemCode())) {
        SQLQuery.append(" AND T1.OTHER_SYSTEM_CODE =:otherSystemCode ");
        parameters.put("otherSystemCode", srdto.getOtherSystemCode());
      }
      // dungpv 14/08/2020 edit them hien thi sr con len gird
      if (!StringUtils.isStringNullOrEmpty(srdto.getSrChildCode())) {
        SQLQuery.append(
            " AND T1.SRCODE in (Select PARENT_CODE from OPEN_PM.SR where LOWER(SR_CODE) LIKE :srChildCode escape '\\' ) ");
        parameters
            .put("srChildCode", StringUtils.convertLowerParamContains(srdto.getSrChildCode()));
      }
      //end
    }

    SQLQuery.append(" ORDER BY STATUSORDER,nvl(UPDATEDTIME,CREATEDTIME) DESC");
    String sqlStr = SQLQuery.toString();
    if (srdto != null) {
      if (!StringUtils.isStringNullOrEmpty(srdto.getCreateFromDate())) {
        sqlStr = sqlStr.replace("limitTimeDayOffFrom",
            " AND t1.CREATED_TIME >= TO_DATE(:createFromTime, 'dd/MM/yyyy HH24:mi:ss') ");
        sqlStr = sqlStr.replace("limitTimeHisFrom",
            " AND n.CREATED_TIME >= TO_DATE(:createFromTime, 'dd/MM/yyyy HH24:mi:ss') "
                + "AND tn.CREATED_TIME >= TO_DATE(:createFromTime, 'dd/MM/yyyy HH24:mi:ss')");
        sqlStr = sqlStr.replace("limitTimeFrom",
            " AND his.CREATED_TIME >= TO_DATE(:createFromTime, 'dd/MM/yyyy HH24:mi:ss') "
                + " AND sr.CREATED_TIME >= TO_DATE(:createFromTime, 'dd/MM/yyyy HH24:mi:ss') ");
        parameters
            .put("createFromTime", DateTimeUtils
                .convertDateToString(srdto.getCreateFromDate(), "dd/MM/yyyy HH:mm:ss"));
      } else {
        sqlStr = sqlStr.replace("limitTimeDayOffFrom", "");
        sqlStr = sqlStr.replace("limitTimeHisFrom", "");
        sqlStr = sqlStr.replace("limitTimeFrom", "");
      }
      //20201221 dungpv edit tim kiem SR can CLOSED
      if (!StringUtils.isLongNullOrEmpty(srdto.getIsForceClosed()) && srdto.getIsForceClosed() == 1L
          && StringUtils.isNotNullOrEmpty(srdto.getUsername())) {
        String closedRequestTime = " WHERE 1=1  ";
        closedRequestTime += "  AND T1.parent_code                             IS NULL ";
        closedRequestTime += "  AND T1.CREATED_USER                             =:createUserClosed ";
        closedRequestTime += "  AND ((T1.STATUS                                 ='Concluded' AND (T1.updated_time + NVL(T2.TIME_CLOSED_SR,0) <= sysdate )) ";
        closedRequestTime += "  OR (T1.STATUS NOT                             IN ('New','Cancelled','Rejected','Concluded','Closed') ";
        closedRequestTime += "  AND T1.IS_FORCE_CLOSED                         = 1)) ";
        sqlStr = sqlStr.replace("$closedRequestTime$", closedRequestTime);
        parameters
            .put("createUserClosed", srdto.getUsername());
      } else {
        sqlStr = sqlStr.replace("$closedRequestTime$", "");
      }
    } else {
      sqlStr = sqlStr.replace("limitTimeDayOffFrom", "");
      sqlStr = sqlStr.replace("limitTimeHisFrom", "");
      sqlStr = sqlStr.replace("limitTimeFrom", "");
      sqlStr = sqlStr.replace("$closedRequestTime$", "");
    }
    baseDto.setSqlQuery(sqlStr);
    baseDto.setParameters(parameters);
    baseDto.setPage(srdto.getPage());
    baseDto.setPageSize(srdto.getPageSize());
    return baseDto;
  }

  @Override
  public Datatable getListSR(SrInsiteDTO srdto) {
    BaseDto baseDto = sqlSearch(srdto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        srdto.getPage(), srdto.getPageSize(),
        SrInsiteDTO.class, srdto.getSortName(), srdto.getSortType());
  }

  @Override
  public Datatable getListSRByUserLogin(SrInsiteDTO srInsiteDTO) {
    srInsiteDTO = converTimeFromClientToServer(srInsiteDTO,
        Double.valueOf(srInsiteDTO.getOffset()));
    String locale = I18n.getLocale();
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "get-list-SR");
    parameters.put("double_offset", Double.valueOf(srInsiteDTO.getOffset()));
    parameters.put("locale", locale);
    StringBuilder SQLQuery = new StringBuilder();
    SQLQuery.append(sql);
    SQLQuery.append(" AND T1.PARENTCODE is null  "
        + " AND ((T1.SRUSER is null and T1.SRUNIT in (select UNIT_ID from SR_ROLE_USER where ROLE_CODE like '%SRF%')) "
        + " OR T1.SRUSER = :srUser "
        + " OR T1.CREATEDUSER = :createdUser )");
    parameters.put("srUser", srInsiteDTO.getSrUser());
    parameters.put("createdUser", srInsiteDTO.getCreatedUser());
    if (!StringUtils.isStringNullOrEmpty(srInsiteDTO.getCountry())) {
      SQLQuery.append(" AND T1.COUNTRY = :country ");
      parameters.put("country", srInsiteDTO.getCountry());
    }
    if (!StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUnit())) {
      SQLQuery.append(" AND T1.SRUNIT = :srUnit ");
      parameters.put("srUnit", srInsiteDTO.getSrUnit());
    }
    if (!StringUtils.isStringNullOrEmpty(srInsiteDTO.getCreateFromDate())) {
      SQLQuery.append(
          " AND to_date(T1.CREATEDTIME,'dd/MM/yyyy HH24:mi:ss') >= TO_DATE(:createFromDate, 'dd/MM/yyyy HH24:mi:ss')");
      parameters.put("createFromDate",
          DateTimeUtils.convertDateToString(srInsiteDTO.getCreateFromDate()));
    }
    if (!StringUtils.isStringNullOrEmpty(srInsiteDTO.getCreateToDate())) {
      SQLQuery.append(
          " AND to_date(T1.CREATEDTIME,'dd/MM/yyyy HH24:mi:ss') <= TO_DATE(:createToDate,'dd/MM/yyyy HH24:mi:ss')");
      parameters
          .put("createToDate", DateTimeUtils.convertDateToString(srInsiteDTO.getCreateToDate()));
    }
    if (!StringUtils.isStringNullOrEmpty(srInsiteDTO.getStatus())) {
      SQLQuery.append(" AND T1.STATUS IN (:STATUS) ");
      parameters.put("STATUS", Arrays.asList(srInsiteDTO.getStatus().split(",")));
    }
    SQLQuery.append(" ORDER BY STATUSORDER,nvl(UPDATEDTIME,CREATEDTIME) DESC");
    String sqlStr = SQLQuery.toString();
    if (!StringUtils.isStringNullOrEmpty(srInsiteDTO.getCreateFromDate())) {
      sqlStr = sqlStr.replace("limitTimeDayOffFrom",
          " AND t1.CREATED_TIME >= TO_DATE(:createFromTime, 'dd/MM/yyyy HH24:mi:ss') ");
      sqlStr = sqlStr.replace("limitTimeHisFrom",
          " AND n.CREATED_TIME >= TO_DATE(:createFromTime, 'dd/MM/yyyy HH24:mi:ss') "
              + "AND tn.CREATED_TIME >= TO_DATE(:createFromTime, 'dd/MM/yyyy HH24:mi:ss')");
      sqlStr = sqlStr.replace("limitTimeFrom",
          " AND his.CREATED_TIME >= TO_DATE(:createFromTime, 'dd/MM/yyyy HH24:mi:ss') "
              + " AND sr.CREATED_TIME >= TO_DATE(:createFromTime, 'dd/MM/yyyy HH24:mi:ss') ");
      parameters
          .put("createFromTime", DateTimeUtils
              .convertDateToString(srInsiteDTO.getCreateFromDate(), "dd/MM/yyyy HH:mm:ss"));
    } else {
      sqlStr = sqlStr.replace("limitTimeDayOffFrom", "");
      sqlStr = sqlStr.replace("limitTimeHisFrom", "");
      sqlStr = sqlStr.replace("limitTimeFrom", "");
    }
    return getListDataTableBySqlQuery(sqlStr, parameters,
        srInsiteDTO.getPage(), srInsiteDTO.getPageSize(),
        SrInsiteDTO.class, srInsiteDTO.getSortName(), srInsiteDTO.getSortType());
  }

  @Override
  public ResultInSideDto insertSR(SrInsiteDTO srDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SREntity srEntity = getEntityManager().merge(srDTO.toEntity());
    resultInSideDto.setId(srEntity.getSrId());
    return resultInSideDto;
  }

  @Override
  public List<SRActionCodeDTO> searchSrActionCode(SRActionCodeDTO tDTO, int start, int maxResult,
      String sortType, String sortField) {
    return onSearchEntity(SRActionCodeEntity.class, tDTO, start, maxResult, sortType, sortField);
  }

  @Override
  public List<SrInsiteDTO> getListSRExport(SrInsiteDTO srdto) {
    BaseDto baseDto = sqlSearch(srdto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(SrInsiteDTO.class));
  }

  @Override
  public List<UnitDTO> getListSRUnitForDetail(SrInsiteDTO dto) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListSRUnitForDetail");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(dto.getSrUser())) {
      sql += " AND R.USER_NAME = :username ";
      parameters.put("username", dto.getSrUser());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getServiceId())) {
      sql += " AND S.SERVICE_ID = :serviceId ";
      parameters.put("serviceId", dto.getServiceId());
    }
    sql += " order by U.UNIT_CODE asc";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UnitDTO.class));
  }

  @Override
  public List<SRRoleDTO> getListSRRole(SRRoleDTO srRoleDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "sr-role-list");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(srRoleDTO.getCountry())) {
      sqlQuery += " AND T1.COUNTRY= :country ";
      parameters.put("country", srRoleDTO.getCountry());
    }
    if (!StringUtils.isStringNullOrEmpty(srRoleDTO.getParentCode())) {
      sqlQuery += " AND T1.PARENT_CODE= :parentCode ";
      parameters.put("parentCode", srRoleDTO.getParentCode());
    }
    if (!StringUtils.isStringNullOrEmpty(srRoleDTO.getGroupRole())) {
      sqlQuery += " AND T1.GROUP_ROLE= :groupRole ";
      parameters.put("groupRole", srRoleDTO.getGroupRole());
    }
    if (!StringUtils.isStringNullOrEmpty(srRoleDTO.getRoleCode())) {
      sqlQuery += " AND (lower(T1.ROLE_CODE) LIKE :roleCode ESCAPE '\\' )";
      parameters.put("roleCode", StringUtils.convertLowerParamContains(srRoleDTO.getRoleCode()));
    }
    sqlQuery += " order by T1.ROLE_CODE DESC";
    parameters.put("p_leeLocale", I18n.getLocale());
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRRoleDTO.class));
  }

  @Override
  public List<SrInsiteDTO> getCrNumberCreatedFromSR(SrInsiteDTO dto, int rowStart, int maxRow,
      boolean outSide) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getCrNumberCreatedFromSR");
    SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getSrId())) {
        sql += " where t1.sr_id = :srId ";
        parameters.put("srId", dto.getSrId());
      } else if (dto.getListId() != null && !dto.getListId().isEmpty()) {
        sql += " where t1.sr_id in (:lstSrId) ";
        parameters.put("lstSrId", dto.getListId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getCreateFromDate())) {
        sql = sql.replace("fromDate",
            " AND CREATED_TIME >= to_date(:CreateFromDate,'dd/MM/yyyy HH24:mi:ss') ");
        parameters.put("CreateFromDate", sp.format(dto.getCreateFromDate()));
      } else {
        sql = sql.replace("fromDate", " ");
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getCreateToDate())) {
        sql = sql.replace("toDate",
            " AND CREATED_TIME <= to_date(:CreateToDate,'dd/MM/yyyy HH24:mi:ss') ");
        parameters.put("CreateToDate", sp.format(dto.getCreateToDate()));
      } else {
        sql = sql.replace("toDate", " ");
      }
    } else {
      sql = sql.replace("fromDate", " ").replace("toDate", " ");
    }
    List<SrInsiteDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SrInsiteDTO.class));
    if (outSide) {
      if (lst != null && lst.size() >= rowStart) {
        List<SrInsiteDTO> lstResult = new ArrayList<>();
        if (lst.size() < maxRow) {
          for (int i = rowStart; i < lst.size(); i++) {
            lstResult.add(lst.get(i));
          }
        } else {
          maxRow = maxRow + rowStart;
          for (int i = rowStart; i < maxRow; i++) {
            lstResult.add(lst.get(i));
          }
        }
        return lstResult;
      }
    }
    return lst;
  }

  @Override
  public List<Date> getDayOffForExecutionTime(String locationId) {
    List<Date> lst = new ArrayList<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getDayOffForExecutionTime");
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("locationId", locationId);
    List<String> lstDateString = getNamedParameterJdbcTemplate()
        .queryForList(sql, parameter, String.class);
    if (!lstDateString.isEmpty() && lstDateString.size() > 0) {
      for (String dateString : lstDateString) {
        Date date = DateTimeUtils.convertStringToDate(dateString);
        lst.add(date);
      }
    }
    return lst;
  }

  @Override
  public List<SrInsiteDTO> getWorkLog(SrInsiteDTO dto, int rowStart, int maxRow, boolean outSide) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getWorkLog");
    Map<String, Object> parameters = new HashMap<>();
    SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getSrId())) {
        sql += " AND T1.SR_ID = :srId ";
        parameters.put("srId", dto.getSrId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getCreateFromDate())) {
        sql = sql.replace("fromDate",
            " AND A.CREATED_TIME >= to_date(:CreateFromDate,'dd/MM/yyyy HH24:mi:ss') ");
        parameters.put("CreateFromDate", sp.format(dto.getCreateFromDate()));
      } else {
        sql = sql.replace("fromDate", " ");
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getCreateToDate())) {
        sql = sql.replace("toDate",
            " AND A.CREATED_TIME <= to_date(:CreateToDate,'dd/MM/yyyy HH24:mi:ss') ");
        parameters.put("CreateToDate", sp.format(dto.getCreateToDate()));
      } else {
        sql = sql.replace("toDate", " ");
      }
    } else {
      sql = sql.replace("fromDate", " ").replace("toDate", " ");
    }
    sql += " order by T2.CREATED_TIME desc";
    List<SrInsiteDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SrInsiteDTO.class));
    if (outSide) {
      if (lst != null && lst.size() >= rowStart && maxRow != 0) {
        List<SrInsiteDTO> lstResult = new ArrayList<>();
        if (lst.size() < maxRow) {
          for (int i = rowStart; i < lst.size(); i++) {
            lstResult.add(lst.get(i));
          }
        } else {
          for (int i = rowStart; i < maxRow; i++) {
            lstResult.add(lst.get(i));
          }
        }
        return lstResult;
      }
    }
    return lst;
  }

  @Override
  public List<SREvaluateDTO> getListSREvaluate(SREvaluateDTO srEvaluateDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(SREvaluateEntity.class, srEvaluateDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public List<SREvaluateDTO> getListSREvaluateNew(SREvaluateDTO srEvaluateDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListSREvaluateForSR");
    Map<String, Object> parameters = new HashMap<>();
    SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    if (srEvaluateDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srEvaluateDTO.getSrId())) {
        sql += " AND se.SR_ID = :srId ";
        parameters.put("srId", srEvaluateDTO.getSrId());
      }
      if (!StringUtils.isStringNullOrEmpty(srEvaluateDTO.getCreateFromDate())) {
        sql += " AND sr.CREATED_TIME >= to_date(:createFromDate,'dd/MM/yyyy HH24:mi:ss') ";
        parameters.put("createFromDate", sp.format(srEvaluateDTO.getCreateFromDate()));
      }
      if (!StringUtils.isStringNullOrEmpty(srEvaluateDTO.getCreateToDate())) {
        sql += " AND sr.CREATED_TIME <= to_date(:createToDate,'dd/MM/yyyy HH24:mi:ss') ";
        parameters.put("createToDate", sp.format(srEvaluateDTO.getCreateToDate()));
      }
    }
    sql += " order by se.CREATE_DATE desc";
    List<SREvaluateDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SREvaluateDTO.class));
    return lst;
  }

  @Override
  public List<SRRoleUserInSideDTO> searchSRRoleUser(SRRoleUserInSideDTO srRoleUserDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "searchSRRoleUser");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getCountry())) {
      sql += " AND COUNTRY =:country ";
      parameters.put("country", srRoleUserDTO.getCountry());
    }
    if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getRoleCode())) {
      sql += " AND ROLE_CODE =:roleCode ";
      parameters.put("roleCode", srRoleUserDTO.getRoleCode());
    }
    if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getStatus())) {
      sql += " AND STATUS =:status ";
      parameters.put("status", srRoleUserDTO.getStatus());
    }
    if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUnitId())) {
      sql += " AND UNIT_ID =:unitId ";
      parameters.put("unitId", srRoleUserDTO.getUnitId());
    }
    if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getIsLeader())) {
      sql += " AND IS_LEADER =:isLeader ";
      parameters.put("isLeader", srRoleUserDTO.getIsLeader());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRRoleUserInSideDTO.class));
  }

  @Override
  public List<String> getLeaderApprove(String roleCode, String unitId) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getLeaderApprove");
    parameters.put("unit", unitId);
    parameters.put("roleCode", roleCode);
    Query query = getEntityManager().createNativeQuery(sql);
    for (Map.Entry<String, Object> param : parameters.entrySet()) {
      query.setParameter(param.getKey(), param.getValue());
    }
    return (List<String>) query.getResultList();
  }

  @Override
  public List<String> getListSequenseSR(String seq, int size) {
    return getListSequense(seq, size);
  }

  @Override
  public SrInsiteDTO getDetail(Long srId, String userToken) {
    SREntity srEntity = getEntityManager().find(SREntity.class, srId);
    if (srEntity != null) {
      SrInsiteDTO srInsiteDTO = srEntity.toDTO();
      if (srInsiteDTO != null) {
        Double offset = userRepository.getOffsetFromUser(userToken);
        srInsiteDTO = converTimeFromClientToServer(srInsiteDTO, -offset);
        return srInsiteDTO;
      }
    }
    return null;
  }

  @Override
  public SrInsiteDTO getDetailNoOffset(Long srId) {
    SREntity srEntity = getEntityManager().find(SREntity.class, srId);
    if (srEntity != null) {
      SrInsiteDTO srInsiteDTO = srEntity.toDTO();
      if (srInsiteDTO != null) {
        return srInsiteDTO;
      }
    }
    return null;
  }

  @Override
  public List<SrInsiteDTO> findByParenCode(String parenCode) {
    List<SrInsiteDTO> listTmp = new ArrayList<>();
    if (StringUtils.isNotNullOrEmpty(parenCode)) {
      List<SREntity> lst = findByMultilParam(SREntity.class, "parentCode", parenCode);
      if (lst != null && lst.size() > 0) {
        for (SREntity dto : lst) {
          listTmp.add(dto.toDTO());
        }
      }
    }
    return listTmp;
  }

  @Override
  public boolean checkUserLoginIsLeader(String roleCode, String createdUser, String loginUser,
      String unitId, String type) {
    String unit = getUnitParentForApprove(type, unitId);
    if (StringUtils.isStringNullOrEmpty(unit)) {
      return false;
    }
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "checkUserLoginIsLeader");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unit", unit);
    parameters.put("loginUser", loginUser);
    parameters.put("roleCode", roleCode);
    List<String> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(String.class));
    if (lst != null && !lst.isEmpty()) {
      return true;
    }
    return false;
  }

  @Override
  public String getUnitParentForApprove(String type, String unitId) {
    String sql = "";
    if ("1".equals(type)) {
      //neu type = 1 thi lay unit cua nguoi tao
      sql = " select unit_id as unitId, is_committee as isCommittee from common_gnoc.unit where unit_id = :unitId ";
    } else if ("2".equals(type)) {
      //neu type = 2 thi lay parent cua don vi duyet cap 1
      sql =
          " select  b1.parent_unit_id as unitId , b2.is_committee as isCommittee from common_gnoc.unit b1  "
              + " join common_gnoc.unit b2  "
              + " on b1.parent_unit_id = b2.unit_id  "
              + " where b1.unit_id = :unitId ";
    }
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unitId", unitId);
    List<UnitDTO> unitDTOList = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UnitDTO.class));
    String unit = "";
    if (unitDTOList != null && !unitDTOList.isEmpty()) {
      if (StringUtils.isStringNullOrEmpty(unitDTOList.get(0).getIsCommittee())) {
        return null;
      }
      unit =
          unitDTOList.get(0).getUnitId() != null ? unitDTOList.get(0).getUnitId().toString() : null;
//                if ("0".equals(objArr[0].toString())) {
      //namtn edit on Feb 25 2019
      if (1L == unitDTOList.get(0).getIsCommittee()) {
        //de quy thi lay parent
        return getUnitParentForApprove("2", unit);
      }
      return unit;
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteSR(Long srId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SREntity srEntity = getEntityManager().find(SREntity.class, srId);
    if (srEntity != null) {
      getEntityManager().remove(srEntity);
    }
    return resultInSideDto;
  }

  public SrInsiteDTO converTimeFromClientToServer(SrInsiteDTO srDTO, Double offset) {
    try {
      srDTO.setStartTime(converClientDateToServerDate(srDTO.getStartTime(), offset));
      srDTO.setEndTime(converClientDateToServerDate(srDTO.getEndTime(), offset));
      srDTO.setCreatedTime(converClientDateToServerDate(srDTO.getCreatedTime(), offset));
      srDTO.setCreateFromDate(converClientDateToServerDate(srDTO.getCreateFromDate(), offset));
      srDTO.setCreateToDate(converClientDateToServerDate(srDTO.getCreateToDate(), offset));
      srDTO.setSendDate(converClientDateToServerDate(srDTO.getSendDate(), offset));
      srDTO.setUpdatedTime(converClientDateToServerDate(srDTO.getUpdatedTime(), offset));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return srDTO;
  }

  private Date converClientDateToServerDate(Date clientTime, Double offset) {
    if (offset == null || offset.equals(0.0)) {
      return clientTime;
    }
    if (StringUtils.isStringNullOrEmpty(clientTime)) {
      return null;
    }
    try {
      Calendar cal = Calendar.getInstance();
      String result = DateTimeUtils.convertDateToString(clientTime, "dd/MM/yyyy HH:mm");
      Date date = DateTimeUtils.convertStringToTime(result, "dd/MM/yyyy HH:mm");
      cal.setTime(date);
      cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - offset.intValue());
      Date dateConver = cal.getTime();
      return dateConver;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public SrInsiteDTO findNationByLocationId(Long locationId) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "find-nation-by-location-id");
    if (locationId != null) {
      sql += " AND a.location_id = :locationId ";
      parameters.put("locationId", locationId);
    }
    sql += " START WITH a.parent_id IS NULL CONNECT BY PRIOR a.location_id = a.parent_id ";
    List<SrInsiteDTO> lst = null;
    if (locationId != null) {
      lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(SrInsiteDTO.class));
    } else {
      Datatable data = getListDataTableBySqlQuery(sql, parameters, 1, 10, SrInsiteDTO.class, null,
          null);
      if (data != null) {
        lst = (List<SrInsiteDTO>) data.getData();
      }
    }

    if (lst != null && lst.size() > 0) {
      return lst.get(0);
    }
    return null;
  }

  @Override
  public List<SrInsiteDTO> checkSRCreatedFromOtherSys(String srCode) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "checkSRCreatedFromOtherSys");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("srCode", srCode);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SrInsiteDTO.class));
  }

  @Override
  public List<SRDTO> getCrInfoCreatedFromSR(Long srId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getCrInfoCreatedFromSR");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("srId", srId);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRDTO.class));
  }

  @Override
  public ResultInSideDto updateStepIdCr(String crId, String stepId, Long srId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      String sql = "UPDATE OPEN_PM.CR_CREATED_FROM_OTHER_SYS SET STEP_ID = :stepId " +
          " WHERE SYSTEM_ID = 5 AND CR_ID = :crId AND OBJECT_ID = :srId ";
      Query query = getEntityManager().createNativeQuery(sql);
      query.setParameter("crId", crId);
      query.setParameter("stepId", stepId);
      query.setParameter("srId", srId);

      int execute = query.executeUpdate();
      if (execute > 0) {
        resultInSideDto.setKey(RESULT.SUCCESS);
      } else {
        resultInSideDto.setKey(RESULT.FAIL);
      }
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<SRRenewDTO> getListSRRenewDTO(SRRenewEntity srRenewEntity,
      List<ConditionBean> lstConditionBean, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(srRenewEntity, lstConditionBean, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public List<SRFilesEntity> getListSRFileByObejctId(Long obejctId) {
    return findByMultilParam(SRFilesEntity.class, "obejctId", obejctId);
  }

  @Override
  public List<SRFilesEntity> getListSRFileByObejctId(String fileGroup, String fileType,
      Long obejctId) {
    return findByMultilParam(SRFilesEntity.class, "fileGroup", fileGroup, "fileType", fileType,
        "obejctId", obejctId);
  }

  @Override
  public ResultInSideDto addSRFile(SRFilesDTO srFilesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRFilesEntity srFilesEntity = getEntityManager().merge(srFilesDTO.toEntity());
    resultInSideDto.setId(srFilesEntity.getFileId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteSRFile(Long fileId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRFilesEntity srFilesEntity = getEntityManager().find(SRFilesEntity.class, fileId);
    if (srFilesEntity != null) {
      getEntityManager().remove(srFilesEntity);
    }
    return resultInSideDto;
  }

  @Override
  public List<SrInsiteDTO> getTotalSRProcessTime(List<Long> lstSrId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getTotalSRProcessTime");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("status", SR_STATUS.CLOSED);
    if (lstSrId != null && !lstSrId.isEmpty()) {
      sql += " and a.SR_ID in (:lstSrId) ";
      parameters.put("lstSrId", lstSrId);
    }
    List<SrInsiteDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SrInsiteDTO.class));
    return lst.isEmpty() ? null : lst;
  }

  @Override
  public List<SrInsiteDTO> getTotalCreateCrSlow(List<Long> lstSrId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getTotalCreateCrSlow");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("status", SR_STATUS.PLANNED);
    parameters.put("onTime", I18n.getLanguage("SR.createCrSlow.onTime"));
    parameters.put("slow", I18n.getLanguage("SR.createCrSlow.slow"));
    if (lstSrId != null && !lstSrId.isEmpty()) {
      sql += " and data.srId in (:lstSrId) ";
      parameters.put("lstSrId", lstSrId);
    }
    List<SrInsiteDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SrInsiteDTO.class));
    return lst.isEmpty() ? null : lst;
  }

  @Override
  public List<SRDTO> getListSRForLinkCR(String loginUser, String srCode) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListSRForLinkCR");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("loginUser", loginUser);
    if (!StringUtils.isStringNullOrEmpty(srCode)) {
      sql += " AND UPPER(sr.SR_CODE) LIKE :srCode escape '\\' ";
      parameters.put("srCode", StringUtils.convertUpperParamContains(srCode));
    }
    sql += " order by sr.CREATED_TIME desc ";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRDTO.class));
  }

  @Override
  public List<SRDTO> getListSRForWO(SRDTO srdto) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "get-List-SR-For-WO");
    Map<String, Object> parameters = new HashMap<>();
    if (srdto != null) {
      if (StringUtils.isNotNullOrEmpty(srdto.getCountry())) {
        sql += " AND T1.COUNTRY = :country";
        parameters.put("country", srdto.getCountry());
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getServiceArray())) {
        sql += " AND T1.SERVICE_ARRAY = :serviceArray";
        parameters.put("serviceArray", srdto.getServiceArray());
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getCreatedUser())) {
        sql += " AND LOWER(T1.CREATED_USER) LIKE :createdUser escape '\\'";
        parameters
            .put("createdUser", StringUtils.convertLowerParamContains(srdto.getCreatedUser()));
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getSrUnit())) {
        sql += " AND T1.SR_UNIT = :srUnit";
        parameters.put("srUnit", srdto.getSrUnit());
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getStatus())) {
        sql += " AND T1.STATUS = :status";
        parameters.put("status", srdto.getStatus());
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getSrCode())) {
        sql += " AND LOWER(T1.SR_CODE) LIKE :srCode escape '\\'";
        parameters
            .put("srCode", StringUtils.convertLowerParamContains(srdto.getSrCode()));
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getServiceGroup())) {
        sql += " AND T1.SERVICE_GROUP = :serviceGroup";
        parameters.put("serviceGroup", srdto.getServiceGroup());
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getCheckingUnit())) {
        sql += " AND T1.CREATED_USER IN (SELECT u.USERNAME FROM COMMON_GNOC.USERS u WHERE UNIT_ID = :checkingUnit)";
        parameters.put("checkingUnit", srdto.getCheckingUnit());
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getCreateFromDate())) {
        sql += " AND T1.CREATED_TIME >= TO_DATE(:createFromDate, 'dd/MM/yyyy HH24:mi:ss')";
        parameters.put("createFromDate", srdto.getCreateFromDate());
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getTitle())) {
        sql += " AND LOWER(T1.TITLE) LIKE :title escape '\\'";
        parameters.put("title", StringUtils.convertLowerParamContains(srdto.getTitle()));
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getServiceId())) {
        sql += " AND T1.SERVICE_ID = :serviceId";
        parameters.put("serviceId", srdto.getServiceId());
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getSrUser())) {
        sql += " AND LOWER(T1.SR_USER) LIKE :srUser escape '\\'";
        parameters.put("srUser", StringUtils.convertLowerParamContains(srdto.getSrUser()));
      }
      if (StringUtils.isNotNullOrEmpty(srdto.getCreateToDate())) {
        sql += " AND T1.CREATED_TIME <= TO_DATE(:createToDate,'dd/MM/yyyy HH24:mi:ss')";
        parameters.put("createToDate", srdto.getCreateToDate());
      }
    }
    sql += " ORDER BY nvl(T1.UPDATED_TIME,T1.CREATED_TIME) DESC";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRDTO.class));
  }

  @Override
  public List<SRDTO> getListSRForOutside(SrInsiteDTO srdto) {
    List<SRDTO> lst = new ArrayList<>();
    try {
      BaseDto baseDto = sqlSearch(srdto);
      Query query = getEntityManager().createNativeQuery(baseDto.getSqlQuery());
      query.unwrap(SQLQuery.class)
          .addScalar("srId", new StringType())
          .addScalar("srCode", new StringType())
          .addScalar("country", new StringType())
          .addScalar("serviceArray", new StringType())
          .addScalar("serviceArrayName", new StringType())
          .addScalar("serviceGroup", new StringType())
          .addScalar("serviceGroupName", new StringType())
          .addScalar("serviceName", new StringType())
          .addScalar("title", new StringType())
          .addScalar("status", new StringType())
          .addScalar("statusName", new StringType())
          .addScalar("createdUser", new StringType())
          .addScalar("sendDate", new StringType())
          .addScalar("startTime", new StringType())
          .addScalar("endTime", new StringType())
          .addScalar("srUser", new StringType())
          .addScalar("srUnit", new StringType())
          .addScalar("updatedTime", new StringType())
          .addScalar("createdTime", new StringType())
          .addScalar("checkingUnit", new StringType())
          .addScalar("serviceId", new StringType())
          .addScalar("serviceCode", new StringType())
          .addScalar("replyTime", new StringType())
          .addScalar("executionTime", new StringType())
          .addScalar("remainExecutionTime", new StringType())
          .addScalar("evaluateReplyTime", new StringType())
          .addScalar("pathSrUnit", new StringType())
          .addScalar("actualExecutionTime", new StringType())
          .addScalar("parentCode", new StringType())
          .addScalar("description", new StringType())
          .addScalar("roleCode", new StringType())
          .addScalar("flowExecute", new StringType())
          .addScalar("flowExecuteName", new StringType())
          .addScalar("statusRenew", new StringType())
          .addScalar("dayRenew", new StringType())
          .addScalar("crWoCreatTime", new StringType())
          .addScalar("insertSource", new StringType())
          .setResultTransformer(Transformers.aliasToBean(SRDTO.class));

      for (Map.Entry<String, Object> param : baseDto.getParameters().entrySet()) {
        query.setParameter(param.getKey(), param.getValue());
      }
      if (baseDto.getPage() > 0) {
        query.setFirstResult(baseDto.getPage());
      }
      if (baseDto.getPageSize() > 0) {
        query.setMaxResults(baseDto.getPageSize());
      }
      lst = query.getResultList();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return lst;
  }

  //tripm nang cap od
  @Override
  public SrInsiteDTO finSrFromOdByProxyId(Long srId) {
    SREntity srEntity = getEntityManager().find(SREntity.class, srId);
    if (srEntity != null) {
      return srEntity.toDTO();
    }
    return null;
  }

  @Override
  public List<SREntity> getListSRChild(SrInsiteDTO srInsiteDTO) {
    try {
      List<SREntity> lst = findByMultilParam(SREntity.class, "parentCode",
          srInsiteDTO.getParentCode());
      return lst;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<SrInsiteDTO> getListSRChildCodeByParentCode(List<String> srParentCodes) {
    if (srParentCodes != null && !srParentCodes.isEmpty()) {
      String sql = "SELECT SR_CODE srCode,\n"
          + "  STATUS status,\n"
          + "  PARENT_CODE parentCode\n"
          + " FROM OPEN_PM.SR WHERE  1=1 AND PARENT_CODE IN (:lstSRCode) ";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("lstSRCode", srParentCodes);
      return getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(SrInsiteDTO.class));
    }
    return null;
  }

  @Override
  public int checkSRConcluded(String userLogin) {
    if (StringUtils.isNotNullOrEmpty(userLogin)) {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "checkSRConcluded");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("status", Constants.SR_STATUS.CONCLUDED);
      parameters.put("createUser", userLogin);
      List<BaseDto> lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(BaseDto.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0).getTotalRow();
      }
    }
    return 0;
  }

  @Override
  public ResultInSideDto updateSRProcessMess(String srCode) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    if (StringUtils.isNotNullOrEmpty(srCode)) {
      String sql = "select COUNT(*) totalRow from open_pm.sr_process_send_message where STATUS ='NOK_VIPA' and sr_code =:srCode and CR_NUMBER is null";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("srCode", srCode);
      List<BaseDto> lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(BaseDto.class));
      if (lst != null && !lst.isEmpty() && lst.get(0).getTotalRow() > 0) {
        String sqlUpdate = " UPDATE open_pm.sr_process_send_message SET STATUS ='NOK_VIPA_DEL' where STATUS ='NOK_VIPA' and  sr_code =:srCode and CR_NUMBER is null ";
        int row = getNamedParameterJdbcTemplate().update(sqlUpdate, parameters);
        if (row != 0) {
          log.info(RESULT.SUCCESS);
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCrNumberForSR(String crNumber, Long srId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      String sql = " UPDATE OPEN_PM.SR SET cr_number =:cr_number WHERE sr_id=:sr_id ";
      Map<String, Object> params = new HashMap<>();
      params.put("cr_number", crNumber);
      params.put("sr_id", srId);
      getNamedParameterJdbcTemplate().update(sql, params);
      resultInSideDto.setKey(RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.FAIL);
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListTabSrChild(SrInsiteDTO srInsiteDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR, "getListTabSrChild");
    Map<String, Object> params = new HashMap<>();
    params.put("locale", I18n.getLocale());
    if (StringUtils.isNotNullOrEmpty(srInsiteDTO.getParentCode())) {
      sql += " AND T1.PARENT_CODE =:parentCode";
      params.put("parentCode", srInsiteDTO.getParentCode());
    }
    return getListDataTableBySqlQuery(sql, params,
        srInsiteDTO.getPage(), srInsiteDTO.getPageSize(),
        SrInsiteDTO.class, srInsiteDTO.getSortName(), srInsiteDTO.getSortType());
  }
}
