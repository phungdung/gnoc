package com.viettel.gnoc.pt.repository;


import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TransitionStateConfigDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.UserRepositoryImpl;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.pt.dto.ProblemFilesDTO;
import com.viettel.gnoc.pt.dto.ProblemMonitorDTO;
import com.viettel.gnoc.pt.dto.ProblemsChartDTO;
import com.viettel.gnoc.pt.dto.ProblemsDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemsMobileDTO;
import com.viettel.gnoc.pt.model.ProblemFilesEntity;
import com.viettel.gnoc.pt.model.ProblemsEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

/**
 * @author NamTN
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class PtProblemsRepositoryImpl extends BaseRepository implements PtProblemsRepository {

  @Autowired
  protected ProblemNodeRepository problemNodeRepository;
  @Autowired
  protected ProblemActionLogsRepository problemActionLogsRepository;
  @Autowired
  protected ProblemWorklogRepository problemWorklogRepository;
  @Autowired
  protected UserRepositoryImpl userRepository;
  @Autowired
  TicketProvider ticketProvider;

  @SuppressWarnings("unchecked")
  @Override
  public List<ProblemsInsideDTO> getListProblemDTO(ProblemsInsideDTO problemsInsideDTO) {
    BaseDto baseDto = sqlSearch(problemsInsideDTO);
    List<ProblemsInsideDTO> problemsInsideDTOList = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(ProblemsInsideDTO.class));
    return problemsInsideDTOList;
  }

  @Override
  public List<ProblemsInsideDTO> getListProblemDTOForTT(ProblemsInsideDTO problemsInsideDTO) {
    List<ProblemsInsideDTO> lstReturn = getListProblemDTO(problemsInsideDTO);
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        Constants.APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      lstReturn = setLanguage(lstReturn, lstLanguage, "stateName", "problemState");
      lstReturn = setLanguage(lstReturn, lstLanguage, "stateCode", "priorityId");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstReturn;
  }

  @Override
  public List<ProblemsInsideDTO> getListProblemsDTO(ProblemsInsideDTO problemsInsideDTO) {
    String sqlQuery = "Select p.* From ONE_TM.PROBLEMS p";
    Map<String, Object> parameters = new HashMap<>();
    Datatable datatable = getListDataTableBySqlQuery(sqlQuery,
        parameters, problemsInsideDTO.getPage(), problemsInsideDTO.getPageSize(),
        ProblemsInsideDTO.class, problemsInsideDTO.getSortName(), problemsInsideDTO.getSortType());
    List<ProblemsInsideDTO> lstProblems = (List<ProblemsInsideDTO>) datatable.getData();
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        Constants.APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      lstProblems = setLanguage(lstProblems, lstLanguage, "stateName", "problemState");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstProblems;
  }

  @Override
  public Datatable getListProblemsSearch(ProblemsInsideDTO problemsInsideDTO) {
    BaseDto baseDto = sqlGetListProblemSearch(problemsInsideDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), problemsInsideDTO.getPage(), problemsInsideDTO.getPageSize(),
        ProblemsInsideDTO.class, problemsInsideDTO.getSortName(), problemsInsideDTO.getSortType());
  }

  @Override
  public List<ProblemsInsideDTO> getListProblemsSearchExport(ProblemsInsideDTO problemsInsideDTO) {
    BaseDto baseDto = sqlGetListProblemSearch(problemsInsideDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(ProblemsInsideDTO.class));
  }


  @Override
  public List<ProblemsInsideDTO> getListProblemSearch(ProblemsInsideDTO problemsInsideDTO) {
    List<ProblemsInsideDTO> lstReturn = (List<ProblemsInsideDTO>) getListProblemsSearch(
        problemsInsideDTO)
        .getData();
    if (!lstReturn.isEmpty()) {
      lstReturn = getLanguageExchange(lstReturn);
    }
    return lstReturn;

  }

  @Override
  public int getListProblemSearchCount(ProblemsInsideDTO problemsInsideDTO) {
    Datatable datatable = getListProblemsSearch(problemsInsideDTO);
    return datatable.getTotal();
  }

  @Override
  public List<String> getSequenseProblems(String seqName, int... size) {
    int number = (size[0] > 0 ? size[0] : 1);

    return getListSequense(seqName, number);
  }


  @Override
  public ResultInSideDto add(ProblemsInsideDTO problemsInsideDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    ProblemsEntity problemsEntity = getEntityManager().merge(problemsInsideDTO.toEntity());
    resultInSideDTO.setId(problemsEntity.getProblemId());
    return resultInSideDTO;
  }

  @Override
  public String getSeqTableProblems(String seq) {
    return getSeqTableBase(seq);
  }

  @Override
  public ResultInSideDto edit(ProblemsInsideDTO problemsInsideDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    getEntityManager().merge(problemsInsideDTO.toEntity());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    ProblemsEntity problemsEntity = getEntityManager().find(ProblemsEntity.class, id);
    getEntityManager().remove(problemsEntity);
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteListProblems(List<ProblemsInsideDTO> problemsListDTO) {
    ResultInSideDto result = new ResultInSideDto();
    result.setKey(RESULT.ERROR);
    for (ProblemsInsideDTO problemsInsideDTO : problemsListDTO) {
      result = delete(problemsInsideDTO.getProblemId());
    }
    return result;
  }

  @Override
  public ProblemsInsideDTO findProblemsById(Long id) {
    if (id != null && id > 0) {
      ProblemsEntity problemsEntity = getEntityManager().find(ProblemsEntity.class, id);
      return problemsEntity.toDTO();
    }
    return null;
  }

  @Override
  public List<ProblemsChartDTO> getProblemsChartData(String receiveUnitId) {
    BaseDto baseDto = sqlGetProblemsChartData(receiveUnitId);
    List<ProblemsChartDTO> chartDTOList = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(),
            baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(ProblemsChartDTO.class));
    return chartDTOList;
  }

  @Override
  public List<ProblemMonitorDTO> getProblemsMonitor(String priorityId, String unitId,
      String fromDate, String toDate, String findInSubUnit, String unitType) {
    String subSql;
    if ("true".equals(findInSubUnit)) {
      if ("receive".equals(unitType)) {
        subSql = " and receive_unit_id IN (SELECT  a.unit_id FROM common_gnoc.unit a WHERE LEVEL < 50 START WITH   a.unit_id = :unitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)\n";
      } else {
        subSql = " and create_unit_id IN (SELECT  a.unit_id FROM common_gnoc.unit a WHERE LEVEL < 50 START WITH   a.unit_id = :unitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)\n";
      }
    } else {
      if ("receive".equals(unitType)) {
        subSql = " and receive_unit_id IN (:unitId)";
      } else {
        subSql = " and create_unit_id IN (:unitId)";
      }
    }
    String sqlSearch = " select\n"
        + " -- da qua han tim nguyen nhan goc\n"
        + " (select count(*) from\n"
        + " (select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and state_code in ('PT_QUEUED') and es_rca_time is not null and rca_found_time is null \n"
        + " and to_number(es_rca_time - sysdate) < 0"
        + subSql
        + " union\n"
        + " select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_DIAGNOSED') and es_rca_time is not null and rca_found_time is not null \n"
        + " and to_number(es_rca_time - rca_found_time) < 0"
        + subSql
        + " ))rcaOutOfDatePT,\n"
        + " -- tim nguyen nhan goc trong han\n"
        + " (select count(*) from\n"
        + " (select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_QUEUED') and es_rca_time is not null and rca_found_time is null \n"
        + " and to_number(es_rca_time - sysdate) >= 0"
        + subSql
        + " union\n"
        + " select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_DIAGNOSED') and es_rca_time is not null and rca_found_time is not null \n"
        + " and to_number(es_rca_time - rca_found_time) >= 0"
        + subSql
        + " ))rcaOnDatePT,\n"
        + " -- tim giai phap tam thoi qua han\n"
        + " (   select count(*) from\n"
        + " (select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_DIAGNOSED') and es_wa_time is not null and wa_found_time is null \n"
        + " and to_number(es_wa_time - sysdate) < 0"
        + subSql
        + " union\n"
        + " select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_WA_FOUND') and es_wa_time is not null and wa_found_time is not null \n"
        + " and to_number(es_wa_time - wa_found_time) < 0"
        + subSql
        + " ))waOutOfDatePT,\n"
        + "  -- tim giai phap tam thoi trong han\n"
        + " (select count(*) from\n"
        + " (select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_DIAGNOSED') and es_wa_time is not null and wa_found_time is null \n"
        + " and to_number(es_wa_time - sysdate) >= 0"
        + subSql
        + " union\n"
        + " select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_WA_FOUND') and es_wa_time is not null and wa_found_time is not null \n"
        + " and to_number(es_wa_time - wa_found_time) >= 0"
        + subSql
        + " ))waOnDatePT,\n"
        + "  -- tim giai phap triet de qua han\n"
        + " (select count(*) from\n"
        + " (select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_WA_IMPL') and es_sl_time is not null and sl_found_time is null \n"
        + " and to_number(es_sl_time - sysdate) < 0"
        + subSql
        + " union\n"
        + " select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_SL_FOUND') and es_sl_time is not null and sl_found_time is not null \n"
        + " and to_number(es_sl_time - sl_found_time) < 0"
        + subSql
        + " ))slOutOfDatePT,\n"
        + "  -- tim giai phap triet de trong han\n"
        + " (select count(*) from\n"
        + " (select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_WA_IMPL') and es_sl_time is not null and sl_found_time is null \n"
        + " and to_number(es_sl_time - sysdate) >= 0"
        + subSql
        + " union\n"
        + " select problem_id from problems\n"
        + " where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_SL_FOUND') and es_sl_time is not null and sl_found_time is not null \n"
        + " and to_number(es_sl_time - sl_found_time) >= 0"
        + subSql
        + " ))slOnDatePT,\n"
        + " --cho dong\n"
        + " (select count(*) from problems where priority_id=:priorityId and created_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') and created_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') and  state_code in ('PT_CLEAR')"
        + subSql + ") clearPT\n"
        + " from dual";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("priorityId", priorityId);
    parameters.put("fromDate", fromDate);
    parameters.put("toDate", toDate);
    parameters.put("unitId", unitId);
    return getNamedParameterJdbcTemplate()
        .query(sqlSearch,
            parameters,
            BeanPropertyRowMapper.newInstance(ProblemMonitorDTO.class));
  }

  @Override
  public List<ProblemsMobileDTO> getProblemsMobileUnitAll(String receiveUnitId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS, "pt-get-problems-mobile-unit-all");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("receiveUnitId", receiveUnitId);
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(ProblemsMobileDTO.class));
  }

  @Override
  public List<ProblemsInsideDTO> getListProblemsByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    List<ProblemsInsideDTO> lstProblems = onSearchByConditionBean(new ProblemsEntity(),
        lstCondition, rowStart, maxRow, sortType, sortFieldList);
    if (!lstProblems.isEmpty()) {
      lstProblems = getLanguageExchange(lstProblems);
    }
    return lstProblems;
  }

  @Override
  public List<ProblemsMobileDTO> getProblemsMobileUnit(String receiveUnitId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS, "pt-get-problems-mobile-unit");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("receiveUnitId", receiveUnitId);
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(ProblemsMobileDTO.class));
  }

  @Override
  public ResultInSideDto insertOrUpdateListProblems(List<ProblemsInsideDTO> problemsInsideDTOList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (problemsInsideDTOList != null && problemsInsideDTOList.size() > 0) {
      for (ProblemsInsideDTO problemsInsideDTO : problemsInsideDTOList) {
        if (problemsInsideDTO.getProblemId() != null) {
          resultInSideDto = add(problemsInsideDTO);
          return resultInSideDto;
        } else {
          resultInSideDto = edit(problemsInsideDTO);
          return resultInSideDto;
        }
      }
    }
    resultInSideDto.setKey(RESULT.NODATA);
    return resultInSideDto;
  }

  @Override
  public List<CatItemDTO> getTransitionStatus(ProblemsInsideDTO dto) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS, "pt-get-transition-state");
    Map<String, Object> params = new HashMap<>();
    sqlQuery += " AND tsc.BEGIN_STATE_ID = :beginId ";
    params.put("beginId", dto.getProblemState());
    sqlQuery += " AND (tsc.ROLE_CODE = :roleCode OR tsc.ROLE_CODE IS NULL OR tsc.ROLE_CODE = 0) ";
    params.put("roleCode", dto.getRoleCode());
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, params, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
  }

  @Override
  public List<InfraDeviceDTO> getInfraDeviceDTOSByListCode(List<String> nodeCodes) {
    String sql = " select DEVICE_CODE deviceCode, NATION_CODE nationCode from COMMON_GNOC.infra_device where DEVICE_CODE in (:deviceCode) ";
    Map<String, Object> params = new HashMap<>();
    params.put("deviceCode", nodeCodes);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));
  }

  @Override
  public Datatable getListProblemSearchDulidates(String fromDate, String toDate,
      UserTokenGNOCSimple userTokenGNOC,
      List<String> lstIn, List<ProblemsInsideDTO> lstNotIn, Integer startRow, Integer pageLength,
      String sortName, String sortType) {
    BaseDto baseDto = sqlGetListProblemSearchDulidate(fromDate, toDate, lstIn, lstNotIn,
        userTokenGNOC);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), startRow, pageLength,
        ProblemsInsideDTO.class, sortName, sortType);
    List<ProblemsInsideDTO> lstReturn = (List<ProblemsInsideDTO>) datatable.getData();
    if (!lstReturn.isEmpty()) {
      lstReturn = getLanguageExchange(lstReturn);
    }
    datatable.setData(lstReturn);
    return datatable;
  }

  private List<ProblemsInsideDTO> getLanguageExchange(
      List<ProblemsInsideDTO> listProblemsInsideDTOS) {
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        Constants.APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      listProblemsInsideDTOS = setLanguage(listProblemsInsideDTOS, lstLanguage, "problemState",
          "stateName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return listProblemsInsideDTOS;
  }


  public BaseDto sqlGetProblemsChartData(String receiveUnitId) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS, "pt-get-problems-chart-data");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("receiveUnitId", receiveUnitId);
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public BaseDto sqlSearch(ProblemsInsideDTO problemsInsideDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS, "pt-get-list-problem");
    Map<String, Object> parameters = new HashMap<>();
    if (problemsInsideDTO != null && !StringUtils
        .isStringNullOrEmpty(problemsInsideDTO.getRelatedTt())) {
      sqlQuery += " AND lower(a.related_tt) like :relatedTt ";
      parameters.put("relatedTt", problemsInsideDTO.getRelatedTt().toLowerCase());
    }
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public BaseDto sqlGetListProblemSearch(ProblemsInsideDTO problemsInsideDTO) {
    BaseDto baseDto = new BaseDto();
    UserToken userToken = ticketProvider.getUserToken();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS, "pt-get-list-problem-search");
    Map<String, Object> parameters = new HashMap<>();
    if (problemsInsideDTO.getKeyword() == null) {
      problemsInsideDTO.setKeyword("");
    }
    parameters
        .put("keyword", StringUtils.convertLowerParamContains(problemsInsideDTO.getKeyword()));
    parameters.put("offset", offset);
    if (problemsInsideDTO.getReceiveUnitId() == null) {
      parameters.put("receiveUnitId", userToken.getDeptId());
    } else {
      parameters.put("receiveUnitId", problemsInsideDTO.getReceiveUnitId());
    }
    parameters.put("createUnitId", problemsInsideDTO.getCreateUnitId());
    if (StringUtils.isNotNullOrEmpty(problemsInsideDTO.getProblemName())) {
      sqlQuery += " AND LOWER(pr.PROBLEM_NAME) LIKE :problemName ESCAPE '\\' ";
      parameters
          .put("problemName",
              StringUtils.convertLowerParamContains(problemsInsideDTO.getProblemName()));
    }
    if (StringUtils.isNotNullOrEmpty(problemsInsideDTO.getProblemCode())) {
      sqlQuery += " AND LOWER(pr.PROBLEM_CODE) LIKE :problemCode ESCAPE '\\' ";
      parameters
          .put("problemCode",
              StringUtils.convertLowerParamContains(problemsInsideDTO.getProblemCode()));
    }
    if ((problemsInsideDTO.getTypeId() != null)) {
      sqlQuery += " and pr.type_Id=:typeId";
      parameters.put("typeId", problemsInsideDTO.getTypeId());
    }
    if ((problemsInsideDTO.getSubCategoryId() != null)) {
      sqlQuery += " and pr.sub_Category_Id=:subCategoryId";
      parameters.put("subCategoryId", problemsInsideDTO.getSubCategoryId());
    }

    if ((problemsInsideDTO.getPriorityId() != null) && (!""
        .equals(problemsInsideDTO.getPriorityId()))) {
      sqlQuery += " and pr.priority_Id in (";
      String strParam = "";
      String[] stPriority = Long.toString(problemsInsideDTO.getPriorityId()).split(",");
      for (int i = 0; i < stPriority.length; i++) {
        strParam += ":priorityId" + i + ",";
        parameters.put("priorityId" + i, stPriority[i]);
      }
      sqlQuery += strParam.substring(0, strParam.length() - 1);
      sqlQuery += ")";
    }
    if ((problemsInsideDTO.getCategorization() != null)) {
      sqlQuery += " and pr.categorization=:categorization";
      parameters.put("categorization", problemsInsideDTO.getCategorization());
    }
    if ((problemsInsideDTO.getPtRelatedType() != null)) {
      sqlQuery += " and pr.pt_Related_Type=:ptRelatedType";
      parameters.put("ptRelatedType", problemsInsideDTO.getPtRelatedType());
    }
    if ((problemsInsideDTO.getLocationId() != null)) {
      sqlQuery += " AND pr.location_id in (select location_id from common_gnoc.cat_location start with location_id = :locationId  connect by  prior location_id = parent_id) ";
      parameters.put("locationId", problemsInsideDTO.getLocationId());
    }
    if ((problemsInsideDTO.getAffectedNode() != null) && (!""
        .equals(problemsInsideDTO.getAffectedNode()))) {
      sqlQuery += " and LOWER(pr.affected_Node) like :affectedNode escape '\\' ";
      parameters.put("affectedNode",
          StringUtils.convertLowerParamContains(problemsInsideDTO.getAffectedNode()));
    }
    if ((problemsInsideDTO.getCreateUnitId() != null) && !problemsInsideDTO.getIsCreateUnitId()) {
      sqlQuery += " and pr.create_Unit_Id in (:createUnitId)";
    }
    if ((problemsInsideDTO.getCreateUnitId() != null) && problemsInsideDTO.getIsCreateUnitId()) {
      sqlQuery += " and pr.create_Unit_Id in (select * from list_create_unit_id)";
    }

    if ((problemsInsideDTO.getPmGroup() != null)) {
      sqlQuery += " and pr.pm_Group = :pm_group_zero ";
      parameters.put("pm_group_zero", problemsInsideDTO.getPmGroup());
    }

    if ((problemsInsideDTO.getReceiveUnitId() != null && !problemsInsideDTO.getIsReceiveUnitId())) {
      sqlQuery += " and pr.receive_Unit_Id = :receiveUnitId_Web ";
      parameters.put("receiveUnitId_Web", problemsInsideDTO.getReceiveUnitId());
    }

    if (problemsInsideDTO.getReceiveUserId() != null) {
      sqlQuery += " and pr.receive_user_id = :receiveUserId ";
      parameters.put("receiveUserId", problemsInsideDTO.getReceiveUserId());
    }

    if ((problemsInsideDTO.getProblemState() != null) && (!""
        .equals(problemsInsideDTO.getProblemState()))) {
      sqlQuery += " and pr.problem_State in (";
      String strParam = "";
      String[] stState = problemsInsideDTO.getProblemState().split(",");
      for (int i = 0; i < stState.length; i++) {
        strParam += ":problemState" + i + ",";
        parameters.put("problemState" + i, stState[i]);
      }
      sqlQuery += strParam.substring(0, strParam.length() - 1);
      sqlQuery += ")";
    }
    if ((problemsInsideDTO.getFromDate() != null) && (!""
        .equals(problemsInsideDTO.getFromDate()))) {
      sqlQuery += " and pr.created_Time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') - :offset/24";
      parameters.put("fromDate", problemsInsideDTO.getFromDate());
    }
    if ((problemsInsideDTO.getToDate() != null) && (!"".equals(problemsInsideDTO.getToDate()))) {
      sqlQuery += " and pr.created_Time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') - :offset/24";
      parameters.put("toDate", problemsInsideDTO.getToDate());
    }

    if ((problemsInsideDTO.getPmUserName() != null) && (!""
        .equals(problemsInsideDTO.getPmUserName()))) {
      sqlQuery += " and LOWER(pr.PM_USER_NAME) like :pmUserName escape '\\'";
      parameters
          .put("pmUserName",
              StringUtils.convertLowerParamContains(problemsInsideDTO.getPmUserName()));
    }
    sqlQuery += ")";

    if (problemsInsideDTO.getLstPmGroup() != null && problemsInsideDTO.getLstPmGroup().size() > 0) {
      String sqlQuery2 = "AND ( (1 = 1 ";
      if (problemsInsideDTO.getReceiveUnitId() != null) {
        if (!problemsInsideDTO.getIsReceiveUnitId()) {
          sqlQuery2 += " and pr.receive_Unit_Id in (:receiveUnitId)";
        }
        if (problemsInsideDTO.getIsReceiveUnitId()) {
          sqlQuery2 += " and (pr.receive_Unit_Id in (select * from list_receive_unit_id) or pr.receive_Unit_Id in (:receiveUnitId))";
        }
      }
      sqlQuery2 += ")";
      List<String> stPmGroup = problemsInsideDTO.getLstPmGroup();
      if (problemsInsideDTO.getReceiveUnitId() == null) {
        sqlQuery2 += " OR pr.pm_Group in (";
        String strParamTemp = "";
        for (int i = 0; i < stPmGroup.size(); i++) {
          strParamTemp += ":pmGroup" + i + ",";
          parameters.put("pmGroup" + i, stPmGroup.get(i));
        }
        sqlQuery2 += strParamTemp.substring(0, strParamTemp.length() - 1);
        sqlQuery2 += ")";
      }
      sqlQuery2 += ")";
      sqlQuery += sqlQuery2;
    } else {
      String sqlQuery2 = "AND ( (1 = 1 ";
      if (problemsInsideDTO.getReceiveUnitId() != null && problemsInsideDTO.getIsReceiveUnitId()) {
        sqlQuery2 += " and (pr.receive_Unit_Id in (select * from list_receive_unit_id)) ";
      }
      sqlQuery2 += ")";
      sqlQuery2 += ")";
      sqlQuery += sqlQuery2;
    }
    sqlQuery += "order by pr.created_Time desc";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  private BaseDto sqlGetListProblemSearchDulidate(String fromDate, String toDate,
      List<String> lstIn, List<ProblemsInsideDTO> lstNotIn, UserTokenGNOCSimple userTokenGNOC) {
    BaseDto baseDto = new BaseDto();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    String sqlViewTmp = "";
    String sqlView = "";
    if (lstIn != null && !lstIn.isEmpty()) {
      sqlViewTmp += "with t as (select p.*, (";
      for (int i = 0; i < lstIn.size(); i++) {
        sqlViewTmp += "(case when REGEXP_COUNT (problem_name, '";
        sqlViewTmp += lstIn.get(i).trim().replace("\\", "\\\\").replaceAll("%", "\\\\%")
            .replaceAll("_", "\\\\_");
        sqlViewTmp += "', 1, 'i') > 0 then 1 else 0 end) + ";
      }
      sqlView = sqlViewTmp.substring(0, sqlViewTmp.length() - 2);
      sqlView += " ) as total"
          + " from one_tm.problems p order by p.created_time desc"
          + ") ";
    }
    String sql = sqlView;
    sql += SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS, "pt-get-list-problem-search-dulidate");
    Map<String, Object> parameters = new HashMap<>();
    if (lstNotIn != null && !lstNotIn.isEmpty()) {
      sql += " and a.problem_code not in( ";
      String strParam = "";
      for (int i = 0; i < lstNotIn.size(); i++) {
        strParam += ":problemCode" + i + ",";
        parameters.put("problemCode" + i, lstNotIn.get(i).getProblemCode());
      }
      sql += strParam.substring(0, strParam.length() - 1);
      sql += ")";
    }

    if (StringUtils.isNotNullOrEmpty(userTokenGNOC.getFullName())) {
      sql += " and  a.problem_State in (";
      String strParam = "";
      String[] stState = userTokenGNOC.getFullName().split(",");
      for (int i = 0; i < stState.length; i++) {
        strParam += ":problemState" + i + ",";
        parameters.put("problemState" + i, stState[i]);
      }
      sql += strParam.substring(0, strParam.length() - 1);
      sql += ")";
    }

    if ((fromDate != null) && (!"".equals(fromDate))) {
      sql += " and a.created_Time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') - :offset/24";
      parameters.put("fromDate", fromDate);
    }
    if ((toDate != null) && (!"".equals(toDate))) {
      sql += " and a.created_Time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') - :offset/24";
      parameters.put("toDate", toDate);
    }
    if (userTokenGNOC != null && StringUtils.isNotNullOrEmpty(userTokenGNOC.getLocale())) {//type
      sql += " and a.TYPE_ID =:typeId ";
      parameters.put("typeId", userTokenGNOC.getLocale());
    }
    if (userTokenGNOC != null && StringUtils
        .isNotNullOrEmpty(userTokenGNOC.getMobile())) {//sub category
      sql += " and a.SUB_CATEGORY_ID =:subCategory ";
      parameters.put("subCategory", userTokenGNOC.getMobile());
    }
    sql += " order by total desc";
    parameters.put("offset", offset);
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<UsersInsideDto> getListChatUsers(ProblemsInsideDTO problemsInsideDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    List<Long> userIds = new ArrayList<>();
    userIds.add(userToken.getUserID());
    userIds.add(problemsInsideDTO.getCreateUserId());
    userIds.add(problemsInsideDTO.getReceiveUserId());
    return userRepository.getListUsersByListUserId(userIds);
  }

  @Override
  public Long getMaxRowDuplicate(String tableCode) {
    return getMaxRowTable(tableCode);
  }

  @Override
  public Datatable searchParentPTForCR(ProblemsInsideDTO dto) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> params = new HashMap<>();
    sql.append(
        "select t.PROBLEM_ID itemValue, t.PROBLEM_CODE itemName from PROBLEMS t where 1 = 1 ");
    if (StringUtils.isNotNullOrEmpty(dto.getStateCode())) {
      List<String> lstState = Arrays.asList(dto.getStateCode().split(","));
      sql.append(" and t.PROBLEM_STATE in (:lstStatus) ");
      params.put("lstStatus", lstState);
    }
    if (dto.getReceiveUserId() != null) {
      sql.append(" and t.RECEIVE_USER_ID = :receiveUserId ");
      params.put("receiveUserId", dto.getReceiveUserId());
    }
    if (StringUtils.isNotNullOrEmpty(dto.getProblemCode())) {
      sql.append(" AND LOWER(t.PROBLEM_CODE) like :problemCode escape '\\' ");
      params.put("problemCode", StringUtils.convertLowerParamContains(dto.getProblemCode()));
    }

    return getListDataTableBySqlQuery(sql.toString(),
        params, dto.getPage(), dto.getPageSize(),
        CatItemDTO.class, dto.getSortName(), dto.getSortType());
  }

  @Override
  public Datatable getListProblemFiles(GnocFileDto dto) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS, "pt-get-list-file-problem");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_business_code", GNOC_FILE_BUSSINESS.PROBLEMS);
    parameters.put("p_business_id", dto.getBusinessId());
    return getListDataTableBySqlQuery(sqlQuery,
        parameters, dto.getPage(), dto.getPageSize(),
        GnocFileDto.class, dto.getSortName(), dto.getSortType());
  }

  @Override
  public ResultInSideDto insertProblemFiles(ProblemFilesDTO problemFilesDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ProblemFilesEntity problemFilesEntity = getEntityManager().merge(problemFilesDTO.toEntity());
    resultInSideDTO.setId(problemFilesEntity.getProblemFileId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteProblemFiles(Long problemFileId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    ProblemFilesEntity problemFilesEntity = getEntityManager()
        .find(ProblemFilesEntity.class, problemFileId);
    getEntityManager().remove(problemFilesEntity);
    return resultInSideDto;
  }

  @Override
  public Datatable getDatatableProblemsChartUpgrade(ProblemsInsideDTO problemsInsideDTO) {
    BaseDto baseDto = sqlGetListProblemSearchChart(problemsInsideDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), problemsInsideDTO.getPage(), problemsInsideDTO.getPageSize(),
        ProblemsInsideDTO.class, problemsInsideDTO.getSortName(), problemsInsideDTO.getSortType());
  }

  @Override
  public List<ProblemsInsideDTO> getListProblemsChartUpgrade(ProblemsInsideDTO problemsInsideDTO) {
    BaseDto baseDto = sqlGetListProblemSearchChart(problemsInsideDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(),
        baseDto.getParameters(), BeanPropertyRowMapper.newInstance(ProblemsInsideDTO.class));
  }

  @Override
  public List<TransitionStateConfigDTO> getSkipStatusPT(TransitionStateConfigDTO dto) {
    List<TransitionStateConfigDTO> ret = new ArrayList<>();
    Map<String, Object> paramMap = new HashMap<>();
    try {
      String sql = "SELECT   t.begin_state_id beginStateId,"
          + "         t.end_state_id endStateId,"
          + "         t.id,"
          + "         t.description,"
          + "         t.process,"
          + "         b.item_name beginStateName,"
          + "         b.item_code beginStateCode,"
          + "         e.item_name endStateName,"
          + "         e.item_code endStateCode,"
          + "         c.category_name processName,"
          + "         c.category_code processCode,"
          + "         t.skip_status skipStatus"
          + "  FROM   common_gnoc.transition_state_config t, "
          + " common_gnoc.cat_item b, "
          + " common_gnoc.cat_item e,"
          + " common_gnoc.category c"
          + " WHERE   t.begin_state_id = b.item_id AND t.end_state_id = e.item_id AND t.process = c.category_id AND t.process = 3 ";
      if (dto != null) {
        if (!StringUtils.isStringNullOrEmpty(dto.getBeginStateCode())) {
          sql += " AND b.item_code = :begin_state_code ";
          paramMap.put("begin_state_code", dto.getBeginStateCode());
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getEndStateCode())) {
          sql += " AND e.item_code = :end_state_code ";
          paramMap.put("end_state_code", dto.getEndStateCode());
        }
      }
      ret = getNamedParameterJdbcTemplate()
          .query(sql, paramMap, BeanPropertyRowMapper.newInstance(TransitionStateConfigDTO.class));
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    return ret;
  }

  @Override
  public List<UsersInsideDto> lstUserLDP(Long unitId) {
    List<UsersInsideDto> usersInsideDtoList = new ArrayList<>();
    Map<String, Object> paramMap = new HashMap<>();
    try {
      String sql = "select us.USER_ID, us.USERNAME\n"
          + "from common_gnoc.v_user_role us\n"
          + "where is_manager = 1";
      if (unitId != null) {
        sql += " AND us.unit_id = :unit_id ";
        paramMap.put("unit_id", unitId);
      }
      usersInsideDtoList = getNamedParameterJdbcTemplate()
          .query(sql, paramMap, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    return usersInsideDtoList;
  }

  @Override
  public List<UsersInsideDto> lstUserLDP1(Long id) {
    List<UsersInsideDto> usersInsideDtoList = new ArrayList<>();
    Map<String, Object> paramMap = new HashMap<>();
    try {
      String sql = "select us.USER_ID, us.USERNAME\n"
          + "from common_gnoc.v_user_role us\n"
          + "where is_manager = 1";
      if (id != null) {
        sql += " AND us.username = :unit_id ";
        paramMap.put("unit_id", id);
      }
      usersInsideDtoList = getNamedParameterJdbcTemplate()
          .query(sql, paramMap, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    return usersInsideDtoList;
  }

  @Override
  public List<ProblemsDTO> onSynchProblem(String fromDate, String toDate, String insertSource,
      List<String> lstState) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS, "createSynchProblemSql");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(insertSource)) {
      sql += " and insert_source=:insertSource ";
      parameters.put("insertSource", insertSource);
    }

    if (StringUtils.isNotNullOrEmpty(fromDate)) {
      sql += " and last_update_time>=to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') ";
      parameters.put("fromDate", fromDate);
    }
    if (StringUtils.isNotNullOrEmpty(toDate)) {
      sql += " and last_update_time<=to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') ";
      parameters.put("toDate", toDate);
    }

    if (lstState != null && !lstState.isEmpty()) {
      sql += " AND problem_state in (:lstState) ";
      parameters.put("lstState", lstState);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ProblemsDTO.class));
  }

  public BaseDto sqlGetListProblemSearchChart(ProblemsInsideDTO problemsInsideDTO) {
    BaseDto baseDto = new BaseDto();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS, "pt-get-problems-chart-data-upgrate");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", offset);

    if (StringUtils.isNotNullOrEmpty(problemsInsideDTO.getProblemState())) {
      sqlQuery += " and pr.problem_State in (-1,";
      String strParam = "";
      String[] stState = problemsInsideDTO.getProblemState().split(",");
      for (int i = 0; i < stState.length; i++) {
        strParam += ":problemState" + i + ",";
        parameters.put("problemState" + i, stState[i]);
      }
      sqlQuery += strParam.substring(0, strParam.length() - 1);
      sqlQuery += ")";
    }

    //PM + lanh dao don vi
    if (problemsInsideDTO.getLstPmGroup() != null && problemsInsideDTO.getLstPmGroup().size() > 0) {
      sqlQuery += " and pr.pm_Group IN (:pm_group) ";
      parameters.put("pm_group", problemsInsideDTO.getLstPmGroup());
    }

    // PM
    if (problemsInsideDTO.getPmId() != null) {
      sqlQuery += "  AND pr.PM_ID = :pmId ";
      parameters.put("pmId", problemsInsideDTO.getPmId());
    }

    //Lanh dao + BO
    if (problemsInsideDTO.getReceiveUnitId() != null) {
      sqlQuery += " and pr.receive_Unit_Id = :receiveUnitId_Web ";
      parameters.put("receiveUnitId_Web", problemsInsideDTO.getReceiveUnitId());
    }

    //BO
    if (problemsInsideDTO.getReceiveUserId() != null) {
      sqlQuery += "  AND pr.RECEIVE_USER_ID = :receiveUserId  ";
      parameters.put("receiveUserId", problemsInsideDTO.getReceiveUserId());
    }

    if (StringUtils.isNotNullOrEmpty(problemsInsideDTO.getColorCheck())) {
      sqlQuery += "  AND pr.COLORCHECK = :colorCheck  ";
      parameters.put("colorCheck", problemsInsideDTO.getColorCheck());
    }

    sqlQuery += " ORDER BY pr.PROBLEM_STATE ASC  ";

    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

}
