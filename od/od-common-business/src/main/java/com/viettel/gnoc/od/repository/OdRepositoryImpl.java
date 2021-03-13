package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.security.UserRole;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.MASTER_DATA;
import com.viettel.gnoc.commons.utils.Constants.OD_CHANGE_STATUS_ROLE;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.*;
import com.viettel.gnoc.od.model.OdEntity;
import com.viettel.gnoc.od.model.OdFileEntity;
import com.viettel.gnoc.od.model.OdPendingEntity;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import com.viettel.gnoc.wfm.dto.WoTypeTimeDTO;
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
public class OdRepositoryImpl extends BaseRepository implements OdRepository {

  @Autowired
  protected CatItemRepository catItemRepository;

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @SuppressWarnings("unchecked")
  @Override
  public Datatable getListDataSearch(OdSearchInsideDTO odSearchInsideDTO) {
    BaseDto baseDto = sqlSearch(odSearchInsideDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), odSearchInsideDTO.getPage(), odSearchInsideDTO.getPageSize(),
        OdSearchInsideDTO.class, odSearchInsideDTO.getSortName(), odSearchInsideDTO.getSortType());
  }

  @Override
  public List<OdSearchInsideDTO> getListDataExport(OdSearchInsideDTO odSearchInsideDTO) {
    BaseDto baseDto = sqlSearch(odSearchInsideDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(OdSearchInsideDTO.class));
  }


  @Override
  public OdDTO findOdById(Long odId) {
    OdEntity odEntity = getEntityManager().find(OdEntity.class, odId);
    if (odEntity != null) {
      OdDTO odDTO = odEntity.toDTO();
      return odDTO;
    }
    return null;
  }

  @Override
  public ResultInSideDto delete(Long odId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    OdEntity odEntity = getEntityManager().find(OdEntity.class, odId);
    getEntityManager().remove(odEntity);
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteListOd(List<OdDTO> odDTOList) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (OdDTO odDTO : odDTOList) {
      resultInSideDTO = delete(odDTO.getOdId());
    }
    return resultInSideDTO;
  }

  @Override
  public List<String> getSeqOd(String sequense, int size) {
    String sqlQuery = "select OD_SEQ.nextval odId from dual";
    Map<String, String> params = new HashMap<>();
    List<String> lstSequense = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      List<OdDTO> list = getNamedParameterJdbcTemplate()
          .query(sqlQuery, params, BeanPropertyRowMapper.newInstance(OdDTO.class));

      if (list != null && list.size() > 0) {
        lstSequense.add(list.get(0).getOdId().toString());
      }
    }
    return lstSequense;
  }

  @Override
  public ResultInSideDto add(OdDTO odDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    OdEntity odEntity = getEntityManager().merge(odDTO.toEntity());
    resultInSideDTO.setId(odEntity.getOdId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto edit(OdDTO odDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(odDTO.toEntity());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdate(OdDTO odDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    resultInSideDTO.setMessage(Constants.RESULT.SUCCESS);
    OdEntity oEntity = odDTO.toEntity();
    if (oEntity.getOdId() != null) {
      getEntityManager().merge(oEntity);
    } else {
      getEntityManager().persist(oEntity);
    }
    resultInSideDTO.setId(oEntity.getOdId());
    return resultInSideDTO;
  }

  @Override
  public UsersEntity getUserByUserId(Long userId) {
    return getEntityManager().find(UsersEntity.class, userId);
  }

  @Override
  public UsersEntity getUserByUserName(String userName) {
    List<UsersEntity> datas = findByMultilParam(UsersEntity.class, "username", userName);
    if (datas != null && datas.size() > 0) {
      return datas.get(0);
    }
    return null;
  }

  @Override
  public Map<String, String> getConfigProperty() throws Exception {
    List<ConfigPropertyDTO> result;
    Map<String, String> mapResult = new HashMap<String, String>();
    try {
      String sql = " select a.key, a.value from common_gnoc.config_property a";
      Map<String, String> params = new HashMap<>();
      result = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
      if (result != null && result.size() > 0) {
        result.forEach(c -> {
          mapResult.put(c.getKey(), c.getValue());
        });
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
    return mapResult;
  }


  @Override
  public ConfigPropertyDTO getConfigPropertyOd(String key) {
    List<ConfigPropertyDTO> result;
    try {
      String sql = "select a.key, a.value, a.description from common_gnoc.config_property a where a.KEY = :key ";
      Map<String, String> params = new HashMap<>();
      params.put("key", key);
      result = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
      if (result != null && result.size() > 0) {
        return result.get(0);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  @Override
  public String getConfigPropertyValue(String key) {
    try {
      String sql = " select a.value from common_gnoc.config_property a"
          + " where a.key = :key";
      Map<String, String> params = new HashMap<>();
      params.put("key", key);
      List<ConfigPropertyDTO> result = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
      if (result != null && !result.isEmpty()) {
        return result.get(0).getValue();
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
    return null;
  }

  @Override
  public List<UsersEntity> getListUserOfUnit(Long unitId) {
    return findByMultilParam(UsersEntity.class,
        "unitId", unitId, "isEnable", 1L);
  }

  @Override
  public OdDTO getDetailOdDTOById(Long odId) {
    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_OD, "od-sql-detail");
    Map<String, String> params = new HashMap<>();
    params.put("odId", odId.toString());
    List<OdDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, params, BeanPropertyRowMapper.newInstance(OdDTO.class));
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public Datatable getListStatusNext(Long odId, String userId, String unitId) {
    Map<String, Object> parameters = getParamSQLStatusNext(odId, userId, unitId);
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_OD, "od-sql-get-status-by-role");
    sqlQuery += " AND od.OD_TYPE_ID = oc.OD_TYPE_ID ";
    List<CatItemDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    if (list == null || list.size() == 0) {
      sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_OD, "od-sql-get-status-by-role");
      sqlQuery = appendSQLStatusNext(sqlQuery);
      sqlQuery += " AND oc.OD_TYPE_ID IS NULL ";
      sqlQuery += " AND oc.IS_DEFAULT = 1 ";
      list = getNamedParameterJdbcTemplate()
          .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    } else {
      sqlQuery = appendSQLStatusNext(sqlQuery);
      list = getNamedParameterJdbcTemplate()
          .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    }
    Datatable datatable = new Datatable();
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.WO,
        Constants.APPLIED_BUSSINESS.WO_TYPE);
    try {
      list = setLanguage(list, lstLanguage, Constants.ITEM_VALUE, MASTER_DATA.OD_STATUS);
      datatable.setData(list);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return datatable;
  }

  @Override
  public List<WoTypeTimeDTO> getListWoTypeTimeDtosByWoTypeId(Long odTypeId) {
    try {
      String sqlQuery =
          "SELECT WO_TYPE_TIME_ID woTypeTimeId, duration, IS_IMMEDIATE isImmediate, USER_APPOVE_PENDING userApprovePending, "
              + " WAIT_FOR_APPOVE_PENDING waitForApprovePending "
              + " FROM WO_TYPE_TIME WHERE WO_TYPE_ID = :wotypeId ";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("wotypeId", odTypeId.toString());
      List<WoTypeTimeDTO> list = getNamedParameterJdbcTemplate()
          .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(WoTypeTimeDTO.class));
      return list;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public BaseDto sqlSearch(OdSearchInsideDTO odSearchInsideDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_OD, "od-sql-search");
    Map<String, Object> parameters = new HashMap<>();
    List<Long> lstStatus = new ArrayList<>();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    parameters.put("offset", (offset == null) ? 0l : offset.longValue());

    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getSearchAll())) {
      sqlQuery +=
          "   AND (lower(o.OD_NAME) LIKE :searchAll ESCAPE '\\' OR lower(o.OD_CODE) LIKE :searchAll ESCAPE '\\' "
              + "      OR lower(o.other_system_code ) LIKE :searchAll ESCAPE '\\' "
              + "  ) ";
      parameters
          .put("searchAll",
              StringUtils.convertLowerParamContains(odSearchInsideDTO.getSearchAll()));
    }

    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getOdCode())) {
      sqlQuery += " AND LOWER(o.OD_CODE) LIKE :odCode ESCAPE '\\' ";
      parameters
          .put("odCode", StringUtils.convertLowerParamContains(odSearchInsideDTO.getOdCode()));
    }
    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getOdName())) {
      sqlQuery += " AND LOWER(o.OD_NAME) LIKE :odName ESCAPE '\\' ";
      parameters
          .put("odName", StringUtils.convertLowerParamContains(odSearchInsideDTO.getOdName()));
    }
    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getCreateTimeFrom())) {
      sqlQuery += " AND o.CREATE_TIME >= TO_TIMESTAMP(:createTimeFrom,'dd/mm/yyyy hh24:mi:ss') ";
      parameters.put("createTimeFrom", odSearchInsideDTO.getCreateTimeFrom());
    }
    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getCreateTimeTo())) {
      sqlQuery += " AND o.CREATE_TIME <= TO_TIMESTAMP(:createTimeTo,'dd/mm/yyyy hh24:mi:ss') ";
      parameters.put("createTimeTo", odSearchInsideDTO.getCreateTimeTo());
    }

    if ((odSearchInsideDTO.getOdId() != null && odSearchInsideDTO.getOdId() > 0)) {
      sqlQuery += " AND o.OD_ID = :odId ";
      parameters.put("odId", odSearchInsideDTO.getOdId());
    }
    if (odSearchInsideDTO.getWoId() != null && odSearchInsideDTO.getWoId() > 0) {
      sqlQuery += " AND o.WO_ID = :woId ";
      parameters.put("woId", odSearchInsideDTO.getWoId());
    }

    if ((odSearchInsideDTO.getReceiveUserId() != null
        && odSearchInsideDTO.getReceiveUserId() > 0)) {
      sqlQuery += " AND o.RECEIVE_USER_ID = :receiveUserId ";
      parameters.put("receiveUserId", odSearchInsideDTO.getReceiveUserId());
    }
    if (odSearchInsideDTO.getCreatePersonId() != null
        && odSearchInsideDTO.getCreatePersonId() > 0) {
      sqlQuery += " AND o.CREATE_PERSON_ID = :createPersonId ";
      parameters.put("createPersonId", odSearchInsideDTO.getCreatePersonId());
    }

    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getPlanCode())) {
      sqlQuery += " AND LOWER(o.PLAN_CODE) LIKE :planCode ESCAPE '\\' ";
      parameters
          .put("planCode", StringUtils.convertLowerParamContains(odSearchInsideDTO.getPlanCode()));
    }
    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getInsertSource())) {
      sqlQuery += " AND o.INSERT_SOURCE = :insertSource ";
      parameters.put("insertSource", odSearchInsideDTO.getInsertSource());
    }

    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getOtherSystemCode())) {
      sqlQuery += " AND LOWER(o.OTHER_SYSTEM_CODE) LIKE :otherSystemCode ESCAPE '\\' ";
      parameters.put("otherSystemCode",
          StringUtils.convertLowerParamContains(odSearchInsideDTO.getOtherSystemCode()));
    }

    if (odSearchInsideDTO.getOdGroupTypeId() != null && odSearchInsideDTO.getOdGroupTypeId() > 0) {
      sqlQuery += " AND o.od_type_id in (select od_type_id from od_type where od_group_type_id = :odGroupTypeId)";
      parameters.put("odGroupTypeId", odSearchInsideDTO.getOdGroupTypeId());
    }
    if ((odSearchInsideDTO.getOdTypeId() != null && odSearchInsideDTO.getOdTypeId() > 0)) {
      sqlQuery += " AND o.OD_TYPE_ID = :odTypeId ";
      parameters.put("odTypeId", odSearchInsideDTO.getOdTypeId());
    }

    if (odSearchInsideDTO.getPriorityId() != null && odSearchInsideDTO.getPriorityId() > 0) {
      sqlQuery += " AND o.PRIORITY_ID = :priorityId ";
      parameters.put("priorityId", odSearchInsideDTO.getPriorityId());
    }

    if (odSearchInsideDTO.getClearCodeId() != null && odSearchInsideDTO.getClearCodeId() > 0) {
      sqlQuery += " AND o.clear_code_id = :clearCodeId ";
      parameters.put("clearCodeId", odSearchInsideDTO.getClearCodeId());
    }
    if (odSearchInsideDTO.getCloseCodeId() != null && odSearchInsideDTO.getCloseCodeId() > 0) {
      sqlQuery += " AND o.close_code_id = :closeCodeId ";
      parameters.put("closeCodeId", odSearchInsideDTO.getCloseCodeId());
    }

    if (odSearchInsideDTO.getReceiveUserId() != null && odSearchInsideDTO.getReceiveUserId() > 0) {
      sqlQuery += " AND o.receive_user_id = :receiveUserId ";
      parameters.put("receiveUserId", odSearchInsideDTO.getReceiveUserId());
    }

    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getStartTimeFrom())) {
      sqlQuery += " AND o.start_time >= TO_TIMESTAMP(:startTimeFrom,'dd/mm/yyyy hh24:mi:ss') ";
      parameters.put("startTimeFrom", odSearchInsideDTO.getStartTimeFrom());
    }

    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getStartTimeTo())) {
      sqlQuery += " AND o.start_time <= TO_TIMESTAMP(:startTimeTo,'dd/mm/yyyy hh24:mi:ss') ";
      parameters.put("startTimeTo", odSearchInsideDTO.getStartTimeTo());
    }
    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getEndTimeFrom())) {
      sqlQuery += " AND o.end_time >= TO_TIMESTAMP(:endTimeFrom,'dd/mm/yyyy hh24:mi:ss') ";
      parameters.put("endTimeFrom", odSearchInsideDTO.getEndTimeFrom());
    }
    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getEndTimeTo())) {
      sqlQuery += " AND o.end_time <= TO_TIMESTAMP(:endTimeTo,'dd/mm/yyyy hh24:mi:ss') ";
      parameters.put("endTimeTo", odSearchInsideDTO.getEndTimeTo());
    }
    if (odSearchInsideDTO.getCreateUnitId() != null && odSearchInsideDTO.getCreateUnitId() > 0) {
      if ("1".equals(odSearchInsideDTO.getChildCreateUnit())) {
        sqlQuery +=
            " AND o.CREATE_UNIT_ID in (select unit_id from common_gnoc.unit where level < 50 start"
                + " with unit_id =:createUnitId connect by prior unit_id = parent_unit_id) ";
        parameters.put("createUnitId", odSearchInsideDTO.getCreateUnitId());
      } else {
        sqlQuery += " AND o.CREATE_UNIT_ID =:createUnitId ";
        parameters.put("createUnitId", odSearchInsideDTO.getCreateUnitId());
      }
    }

    if (odSearchInsideDTO.getReceiveUnitId() != null && odSearchInsideDTO.getReceiveUnitId() > 0) {
      if ("1".equals(odSearchInsideDTO.getChildReceiveUnit())) {
        sqlQuery += " AND o.receive_unit_id in ( "
            + " select unit_id from common_gnoc.unit where level < 50 "
            + " start with unit_id = :receiveUnitId "
            + " connect by prior unit_id = parent_unit_id) ";
        parameters.put("receiveUnitId", odSearchInsideDTO.getReceiveUnitId());
      } else {
        sqlQuery += " AND o.receive_unit_id =:receiveUnitId  ";
        parameters.put("receiveUnitId", odSearchInsideDTO.getReceiveUnitId());
      }
    }

    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getUserId())) {
      sqlQuery += " and ( ";
      Boolean isHasCondition = false;
      if (odSearchInsideDTO.getIsCreated() != null && odSearchInsideDTO.getIsCreated()) {
        sqlQuery +=
            isHasCondition ? " or o.create_person_id =:userId " : " o.create_person_id =:userId";
        isHasCondition = true;
        parameters.put("userId", odSearchInsideDTO.getUserId());
      }
      if (odSearchInsideDTO.getIsReceiveUser() != null && odSearchInsideDTO.getIsReceiveUser()) {
        sqlQuery +=
            isHasCondition ? " or o.receive_user_id =:userId  " : "  o.receive_user_id =:userId  ";
        isHasCondition = true;
        parameters.put("userId", odSearchInsideDTO.getUserId());
      }
      if (odSearchInsideDTO.getIsReceiveUnit() != null && odSearchInsideDTO.getIsReceiveUnit()) {
        sqlQuery += isHasCondition
            ? " or o.receive_unit_id = (select unit_id from common_gnoc.users where user_id =:userId )  "
            : "  o.receive_unit_id = (select unit_id from common_gnoc.users where user_id =:userId )  ";
        isHasCondition = true;
        parameters.put("userId", odSearchInsideDTO.getUserId());
      }
      if (!isHasCondition) {
        sqlQuery += "1=1";
      }
      sqlQuery += " ) ";
    }

    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getStatus())) {
      if ("-1".equals(odSearchInsideDTO.getStatus())) {//Chua hoan thanh - Cho CD phe duyet
//                sql.append(" AND w.status <> 7 and w.status <> 8 and ( w.status <> 6 or (w.status = 6 AND w.result is null))");
      }//ducdm1_them trang thai hoan thanh_start
      else if ((odSearchInsideDTO.getStatus()).contains(",")) {
        sqlQuery += " AND o.status in (:lstStatus)";
        List<String> myList = new ArrayList<>(
            Arrays.asList(odSearchInsideDTO.getStatus().split(",")));
        for (String tmp : myList) {
          lstStatus.add(Long.parseLong(tmp.trim()));
        }
        if (lstStatus != null && !lstStatus.isEmpty()) {
          parameters.put("lstStatus", lstStatus);
        }
      } else {
        sqlQuery += " AND o.status = :status  ";
        parameters.put("status", odSearchInsideDTO.getStatus());
      }
    }
    sqlQuery += " ORDER BY o.create_time DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  private String appendSQLStatusNext(String sqlQuery) {
    sqlQuery += " AND ( ";
    sqlQuery +=
        " (ocr.ROLE_ID = " + OD_CHANGE_STATUS_ROLE.NVT + " AND od.CREATE_PERSON_ID = :userId) OR ";
    sqlQuery += " (ocr.ROLE_ID = " + OD_CHANGE_STATUS_ROLE.DVT
        + " AND (select count(*) FROM COMMON_GNOC.USERS where unit_id = :unit_id) > 0) OR ";
    sqlQuery +=
        " (ocr.ROLE_ID = " + OD_CHANGE_STATUS_ROLE.NVXL + " AND od.RECEIVE_USER_ID = :userId) OR ";
    sqlQuery +=
        " (ocr.ROLE_ID = " + OD_CHANGE_STATUS_ROLE.NVPD + " AND od.APPROVER_ID = :userId) OR ";
    sqlQuery +=
        " (ocr.ROLE_ID = " + OD_CHANGE_STATUS_ROLE.DVXL + " AND od.RECEIVE_UNIT_ID = :unit_id) OR ";
    sqlQuery += " (ocr.ROLE_ID = " + OD_CHANGE_STATUS_ROLE.LD_DVXL
        + " AND (select count(*) from COMMON_GNOC.V_USER_ROLE where user_id = :userId and unit_id = :unit_id and ROLE_CODE = 'TP') > 0) ";
    sqlQuery += " ) ";
    return sqlQuery;
  }

  private Map<String, Object> getParamSQLStatusNext(Long odId, String userId, String unitId) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("odStatusCode", CATEGORY.OD_STATUS);
    parameters.put("odId", odId.toString());
    parameters.put("userId", userId);
    parameters.put("unit_id", unitId);
    return parameters;
  }

  @Override
  public List<UsersEntity> getManagerReceiverUnit(Long unitId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_OD, "od-get-manager-receiver-unit");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unit_id", unitId);
    List<UsersEntity> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(UsersEntity.class));
    return list;
  }

  @Override
  public List<OdFileEntity> getListOdFileByOdId(Long odId) {
    return findByMultilParam(OdFileEntity.class, "odId", odId);
  }

  @Override
  public ResultInSideDto insertOdFile(OdFileDTO odFileDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    OdFileEntity odFileEntity = getEntityManager().merge(odFileDTO.toEntity());
    resultInSideDto.setId(odFileEntity.getOdFileId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteOdFile(Long odFileId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    OdFileEntity odFileEntity = getEntityManager().find(OdFileEntity.class, odFileId);
    getEntityManager().remove(odFileEntity);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

//  @Override
//  public OdDTO findOdByOdCode(String odCode) {
//    String sql = "SELECT * FROM OD WHERE OD.OD_CODE = :odCode";
//    Map<String, Object> parameters = new HashMap<>();
//    parameters.put("odCode", odCode);
//    List<OdDTO> list = getNamedParameterJdbcTemplate()
//        .query(sql, parameters, BeanPropertyRowMapper.newInstance(OdDTO.class));
//    if (list != null && !list.isEmpty()) {
//      return list.get(0);
//    }
//    return null;
//  }

  @Override
  public ResultInSideDto updateOdOtherSystem(OdDTO odDTO) {
    OdEntity odEntity = getEntityManager().find(OdEntity.class, odDTO.getOdId());
    odEntity.setOtherSystemCode(odDTO.getOtherSystemCode());
    odEntity.setInsertSource(odDTO.getInsertSource());
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(odEntity);
    return resultInSideDTO;
  }

  @Override
  public OdHistoryDTO getListOdHistory(Long id) {
    try {
      String sql = "select his.OLD_STATUS from WFM.OD_HISTORY his where OD_ID = :odId and NEW_STATUS = 11 order by UPDATE_TIME DESC";
      Map<String, String> paramters = new HashMap<>();
      paramters.put("odId", id.toString());
      List<OdHistoryDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, paramters, BeanPropertyRowMapper.newInstance(OdHistoryDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0);
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }

  @Override
  public ResultInSideDto insertApprovalPause(OdPendingDTO odPendingDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      OdPendingEntity odPendingEntity = getEntityManager().merge(odPendingDTO.toEntity());
      resultInSideDto.setId(odPendingEntity.getOdPendId());
      return resultInSideDto;
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return resultInSideDto;
  }
}
