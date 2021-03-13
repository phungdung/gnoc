package com.viettel.gnoc.incident.repository;

import com.viettel.bccs.cc.service.ProblemGroupDTO;
import com.viettel.bccs.cc.service.ProblemTypeDTO;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmDTO;
import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.MapFlowTemplatesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TroubleStatisticForm;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.TT_MASTER_CODE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.dto.CommonFileDTO;
import com.viettel.gnoc.incident.dto.ItemDataTT;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.CommonFileEntity;
import com.viettel.gnoc.incident.model.TroubleActionLogsEntity;
import com.viettel.gnoc.incident.model.TroubleFileEntity;
import com.viettel.gnoc.incident.model.TroubleWorklogEntity;
import com.viettel.gnoc.incident.model.TroublesEntity;
import com.viettel.gnoc.incident.utils.WSBCCS2Port;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import com.viettel.gnoc.wo.dto.CfgMapUnitGnocNimsDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroublesRepositoryImpl extends BaseRepository implements TroublesRepository {

  @Autowired
  UserRepository userRepository;

  @Autowired
  WSBCCS2Port wsbccs2Port;

  @Override
  public ResultInSideDto insertTroubles(TroublesEntity troublesEntity) {
    log.debug("Request to insertTroubles: {}", troublesEntity);
    return insertByModel(troublesEntity, "troubleId");
  }

  @Override
  public ResultInSideDto onInsertWorkLog(TroublesInSideDTO tForm) throws Exception {
    TroubleWorklogEntity troubleLog = new TroubleWorklogEntity();
    try {
      if (!StringUtils.isStringNullOrEmpty(tForm.getWorkLog())) {
        troubleLog.setTroubleId(Long.valueOf(tForm.getTroubleId()));
        troubleLog.setCreateUnitId(Long.valueOf(tForm.getCreateUnitId()));
        troubleLog.setCreateUnitName(tForm.getCreateUnitName());
        troubleLog.setCreateUserId(
            tForm.getCreateUserId() == null ? null : Long.valueOf(tForm.getCreateUserId()));
        troubleLog
            .setCreateUserName(tForm.getCreateUserName() == null ? "" : tForm.getCreateUserName());
        troubleLog.setWorklog(tForm.getWorkLog());
        troubleLog.setCreatedTime(tForm.getLastUpdateTime());
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      ex.getMessage();
    }
    log.debug("Request to insertTroubles: {}", troubleLog);
    return insertByModel(troubleLog, "troubleId");
  }

  @Override
  public ResultInSideDto onUpdateTrouble(TroublesInSideDTO tForm) {
    TroublesEntity troublesEntity = tForm.toEntity();
    if (StringUtils.isStringNullOrEmpty(tForm.getTroubleId())) {
      getEntityManager().persist(troublesEntity);
    } else {
      getEntityManager().merge(troublesEntity);
    }
    return new ResultInSideDto(troublesEntity.getTroubleId(), RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public ResultInSideDto onInsertTroubleActionLogs(TroubleActionLogsDTO tForm) {
    log.debug("Request to insertTroubleActionLogs: {}", tForm);
    TroubleActionLogsEntity troubleActionLogsEntity = tForm.toEntity();
    return insertByModel(troubleActionLogsEntity, "troubleId");
  }


  @Override
  public String updateTroubles(TroublesEntity troublesEntity) {
    log.debug("Request to updateTroubleAssign: {}", troublesEntity);
    getEntityManager().merge(troublesEntity);
    return RESULT.SUCCESS;
  }

  @Override
  public Datatable onSearch(TroublesInSideDTO dto) {
    Map<String, Object> params = new HashMap<>();
    String sql = createSql(dto, params, false);
    return getListDataTableBySqlQuery(sql,
        params, dto.getPage(), dto.getPageSize(),
        TroublesInSideDTO.class, dto.getSortName(), dto.getSortType());

  }

  @Override
  public List<TroublesInSideDTO> getTroublesSearchExport(TroublesInSideDTO dto) {
    Map<String, Object> params = new HashMap<>();
    String sql = createSql(dto, params, false);
    return getNamedParameterJdbcTemplate()
        .query(sql, params,
            BeanPropertyRowMapper.newInstance(TroublesInSideDTO.class));
  }


  @Override
  public List<TroublesInSideDTO> searchByConditionBean(List<ConditionBean> lstCondition,
      int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new TroublesEntity(), lstCondition, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public List<String> getSequenseTroubles(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  //duongnt start
  @Override
  public TroublesDTO checkAccountSPM(List<String> lstAccount, String insertSource) {
    TroublesDTO troublesDTO = null;
    try {
      Map<String, Object> parram = new HashMap<>();
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES, "tt-get-account-spm");
      if (!StringUtils.isStringNullOrEmpty(insertSource)) {
        sql = sql + " and b.INSERT_SOURCE =:insertSource";
        parram.put("insertSource", insertSource);
      } else {
        sql = sql + " and b.INSERT_SOURCE in ('SPM', 'SPM_VTNET') ";
      }
      if (!StringUtils.isStringNullOrEmpty(lstAccount)) {
        parram.put("lstAccount", lstAccount);
      }
      List<TroublesDTO> obj = getNamedParameterJdbcTemplate()
          .query(sql, parram, BeanPropertyRowMapper
              .newInstance(TroublesDTO.class));
      if (obj != null && !obj.isEmpty()) {
        troublesDTO = obj.get(0);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return troublesDTO;
  }

  @Override
  public ResultInSideDto updateTroubleSpmVTNET(TroublesDTO troublesDTO) {
    ResultInSideDto result = new ResultInSideDto();
    try {
      Map<String, Object> parram = new HashMap<>();
      StringBuilder sql = new StringBuilder();
      sql.append("update one_tm.troubles set TROUBLE_ID = :troubleId");

      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getTimeProcess())) {
        sql.append(" , ASSIGN_TIME_TEMP = sysdate,  TIME_PROCESS = :timeProcess ");
        parram.put("timeProcess", troublesDTO.getTimeProcess());
      }
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getInsertSource())) {
        sql.append(" , INSERT_SOURCE = :insertSource ");
        parram.put("insertSource", troublesDTO.getInsertSource());
      }

      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getSpmCode())) {
        sql.append(" , SPM_CODE = :spmCode, SPM_ID = :spmId ");
        parram.put("spmCode", troublesDTO.getSpmCode());
        parram.put("spmId", troublesDTO.getSpmId());
      }
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getComplaintId())) {
        sql.append(
            " , COMPLAINT_ID = :complaintId, COMPLAINT_PARENT_ID = :complaintParentId, COMPLAINT_TYPE_ID = :complaintTypeId, COMPLAINT_GROUP_ID = :complaintGroupId ");
        parram.put("complaintId", troublesDTO.getComplaintId());
        parram.put("complaintParentId", troublesDTO.getComplaintParentId());
        parram.put("complaintTypeId", troublesDTO.getComplaintTypeId());
        parram.put("complaintGroupId", troublesDTO.getComplaintGroupId());
      }
      sql.append(" where TROUBLE_ID = :troubleId");
      parram.put("troubleId", troublesDTO.getTroubleId());
      getNamedParameterJdbcTemplate().update(sql.toString(), parram);

      result.setMessage(RESULT.SUCCESS);
      result.setKey(RESULT.SUCCESS);
    } catch (Exception e) {
      result.setId(null);
      result.setKey(RESULT.ERROR);
      result.setMessage(e.getMessage());
      log.error(e.getMessage(), e);
      throw e;
    }
    return result;
  }
  //duongnt start

  @Override
  public CfgTimeTroubleProcessDTO getConfigTimeTroubleProcess(Long typeId, Long subCategoryId,
      Long priority, String country) {
    CfgTimeTroubleProcessDTO ret = null;
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
          "tt-get-cfg-time-trouble-process");
      if (StringUtils.validString(country) == true && country.toUpperCase().contains("NOC")) {
        sql = sql + "  AND lower(a.country) = :country";
        params.put("country", country.toLowerCase());
      } else {
        sql = sql + "  AND a.country = 'NOC' ";
      }

      params.put("typeId", typeId);
      params.put("subCategoryId", subCategoryId);
      params.put("priority", priority);

      List<CfgTimeTroubleProcessDTO> obj = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper
              .newInstance(CfgTimeTroubleProcessDTO.class));
      if (obj != null && !obj.isEmpty()) {
        ret = obj.get(0);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return ret;

  }

  @Override
  public List<MapFlowTemplatesDTO> getMapFlowTemplate(String typeId, String alarmGroupId) {
    List<MapFlowTemplatesDTO> ret = new ArrayList<>();
    try {
      Map<String, Object> lstParam = new HashMap<>();
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
          "tt-get-map-flow-template");

      if (!StringUtils.isStringNullOrEmpty(typeId)) {
        sql += " AND a.TYPE_ID =:typeId ";
        lstParam.put("typeId", typeId);
      }
      if (!StringUtils.isStringNullOrEmpty(alarmGroupId)) {
        sql += " AND a.ALARM_GROUP_ID =:alarmGroupId ";
        lstParam.put("alarmGroupId", alarmGroupId);
      }
      sql += " order by t.ITEM_NAME";

      ret = getNamedParameterJdbcTemplate()
          .query(sql, lstParam, BeanPropertyRowMapper
              .newInstance(MapFlowTemplatesDTO.class));

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    return ret;
  }

  @Override
  public CfgUnitTtSpmDTO getUnitByLocation(String locationId, String typeId, String typeUnit) {
    CfgUnitTtSpmDTO ttSpmDTO = new CfgUnitTtSpmDTO();
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> params = new HashMap<>();
      String sqlFromSource = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
          "tt-get-unit-by-location");
      sql.append(sqlFromSource);
      if (typeUnit != null) {
        sql.append(" and b.type_unit =:typeUnit ");
        params.put("typeUnit", typeUnit);
      } else {
        sql.append(" and (b.type_unit = -1 or b.type_unit is null) ");
      }
      params.put("typeId", typeId);
      params.put("locationId", locationId);

      List<CfgUnitTtSpmDTO> obj = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper
              .newInstance(CfgUnitTtSpmDTO.class));
      if (obj != null && !obj.isEmpty()) {
        ttSpmDTO = obj.get(0);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return ttSpmDTO;
  }

  @Override
  public String getLocationNameFull(String locationId) {
    String ret = null;
    try {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
          "tt-get-location-name-full");
      Map<String, Object> params = new HashMap<>();
      params.put("locationId", locationId);
      List<CatLocationDTO> obj = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper
              .newInstance(CatLocationDTO.class));
      if (obj != null && !obj.isEmpty()) {
        ret = obj.get(0).getLocationNameFull();
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return ret;
  }

  @Override
  public Datatable onSearchTroubleRelated(TroublesInSideDTO troublesDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES, "on-Search-Trouble-Related");
    Map<String, Object> params = new HashMap<>();
    Datatable datatable;
    if (troublesDTO != null) {
      if (troublesDTO.getLstState() != null && !troublesDTO.getLstState().isEmpty()) {
        sql += " AND a.STATE in (:lstState)";
        params.put("lstState", troublesDTO.getLstState());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getTroubleCode())) {
        sql += " AND LOWER(a.trouble_code) like :troubleCode escape '\\' ";
        params.put("troubleCode",
            StringUtils.convertLowerParamContains(troublesDTO.getTroubleCode()));
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getTroubleName())) {
        sql += " AND LOWER(a.trouble_name) like :troubleName escape '\\' ";
        params.put("troubleName",
            StringUtils.convertLowerParamContains(troublesDTO.getTroubleName()));
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getCreatedTimeFrom())) {
        sql += " AND a.created_time >= to_date(:createdTimeFrom,'dd/MM/yyyy HH24:MI:ss') ";
        params.put("createdTimeFrom", troublesDTO.getCreatedTimeFrom());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getCreatedTimeTo())) {
        sql += " AND a.created_time <= to_date(:createdTimeTo,'dd/MM/yyyy HH24:MI:ss') ";
        params.put("createdTimeTo", troublesDTO.getCreatedTimeTo());
      }
      sql += " order by a.created_time desc";
      datatable = getListDataTableBySqlQuery(sql, params, troublesDTO.getPage(),
          troublesDTO.getPageSize(),
          TroublesInSideDTO.class, troublesDTO.getSortName(), troublesDTO.getSortType());
      return datatable;
    }
    return null;
  }


  public Map<String, String> getConfigProperty() {
    Map<String, String> mapResult = new HashMap<>();
    try {
      String sql = " select a.key, a.value from common_gnoc.config_property a ";
      List<ConfigPropertyDTO> result = getNamedParameterJdbcTemplate()
          .query(sql, mapResult, BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
      if (result != null && !result.isEmpty()) {
        for (ConfigPropertyDTO cfg : result) {
          mapResult.put(cfg.getKey(), cfg.getValue());
        }
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
    return mapResult;
  }

  String createSql(TroublesInSideDTO dto, Map<String, Object> lstParam, boolean isCountState) {
    String sql = "";
    if (isCountState) {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
          "tt-count-state-search-troubles");
    } else {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
          "tt-search-troubles");
    }
    Double offset = TimezoneContextHolder.getOffsetDouble();
    lstParam.put("offset", offset);
    if (dto != null) {
      //Neu tim theo trang thai open -> chi tim duoc cac trouble duoc tao ra boi don vi cua nguoi dung
      if (dto.getHaveOpenCondition() != null && "1".equals(dto.getHaveOpenCondition().trim())) {
        if (dto.getLstState() == null || dto.getLstState().isEmpty()) {
          sql += " AND (a.state = 1 and a.create_unit_id = :userUnitId ) ";//du thao
        } else {
          sql += " AND (a.state in (:lstState) OR (a.state = 1 and a.create_unit_id = :userUnitId ))";
        }
        lstParam.put("userUnitId",
            dto.getCreateUnitId() == null ? dto.getUserUnitId() : dto.getCreateUnitId());

      } else if (dto.getLstState() != null && !dto.getLstState().isEmpty()) {
        sql += " AND a.state in (:lstState) ";
        lstParam.put("lstState", dto.getLstState());
      } else if (dto.getResponeseUnitId() == null && dto.getUserUnitId() != null) {
        sql += " and a.create_unit_id = :userUnitId ";
        lstParam.put("userUnitId", dto.getUserUnitId());
      }
      if (dto.getSearchAll() != null && !"".equals(dto.getSearchAll())) {
        sql += " AND (lower(a.trouble_code) like :searchAll escape '\\' "
            + " or lower(a.trouble_name) like :searchAll escape '\\' "
            + " )";

        lstParam.put("searchAll", "%" + dto.getSearchAll().trim()
            .toLowerCase().replace("\\", "\\\\")
            .replaceAll("%", "\\\\%")
            .replaceAll("_", "\\\\_")
            + "%");

      }

      //tiennv them condition tim kiem
      if (!"1".equals(String.valueOf(dto.getHaveOpenCondition()).trim())) {
        if (dto.getLstState() == null || dto.getLstState().isEmpty()) {
          sql += " AND a.state in (1,2,3,4,5,6,7,8,9,10,11) ";
        }
      }

      if (dto.getLocationId() != null && dto.getLocationId() != -1l) {
        if (dto.getLocationId() == 4001000000l || dto.getLocationId() == 5000289722l
            || dto.getLocationId() == 1000014581l
            || dto.getLocationId() == 3500289726l || dto.getLocationId() == 4500000001l
            || dto.getLocationId() == 6000289723l
            || dto.getLocationId() == 3000289724l || dto.getLocationId() == 2000289729l
            || dto.getLocationId() == 1500289728l) {
          sql += " AND a.location_id in (select location_id from common_gnoc.cat_location start with location_id in (:locationId, :locationId2 ) connect by  prior location_id = parent_id) "; //HaiNV20 fixbug
          lstParam.put("locationId", dto.getLocationId());
          if (dto.getLocationId() == 1500289728) {//timor
            lstParam.put("locationId2", "289728");
          } else if (dto.getLocationId() == 2000289729l) {//timor
            lstParam.put("locationId2", "289729");
          } else if (dto.getLocationId() == 3000289724l) {//mozam
            lstParam.put("locationId2", "289724");
          } else if (dto.getLocationId() == 6000289723l) {//timor
            lstParam.put("locationId2", "289723");
          } else if (dto.getLocationId() == 4500000001l) {//myanmar
            lstParam.put("locationId2", "300656");
          } else if (dto.getLocationId() == 4001000000l) {//tanz
            lstParam.put("locationId2", "289727");
          } else if (dto.getLocationId() == 5000289722l) {//lao
            lstParam.put("locationId2", "289722");
          } else if (dto.getLocationId() == 1000014581l) {//cam
            lstParam.put("locationId2", "289721");
          } else if (dto.getLocationId() == 3500289726l) {//burundi
            lstParam.put("locationId2", "289726");
          }

        } else {
          sql += " AND a.location_id in (select location_id from common_gnoc.cat_location start with location_id = :locationId  connect by  prior location_id = parent_id) "; //HaiNV20 fixbug
          lstParam.put("locationId", dto.getLocationId());
        }
      }

      if (dto.getTroubleCode() != null && !"".equals(dto.getTroubleCode())) {
        sql += " and UPPER(a.trouble_code) like :troubleCode escape '\\' ";
        lstParam.put("troubleCode", "%" + dto.getTroubleCode().trim()
            .toUpperCase().replace("\\", "\\\\")
            .replaceAll("%", "\\\\%")
            .replaceAll("_", "\\\\_")
            + "%");
      }
      if (dto.getTroubleName() != null && !"".equals(dto.getTroubleName())) {
        sql += " and lower(a.trouble_name) like :troubleName escape '\\' ";
        lstParam.put("troubleName", "%" + dto.getTroubleName().trim()
            .toLowerCase().replace("\\", "\\\\")
            .replaceAll("%", "\\\\%")
            .replaceAll("_", "\\\\_")
            + "%");
      }
      if (dto.getImpactId() != null && !"-1".equals(String.valueOf(dto.getImpactId()))) {
        sql += " and a.impact_id = :impactId ";
        lstParam.put("impactId", dto.getImpactId());
      }

      if (dto.getPriorityId() != null && !"-1".equals(String.valueOf(dto.getPriorityId()))) {
        sql += " and a.priority_id = :priorityId ";
        lstParam.put("priorityId", dto.getPriorityId());
      }

      if (dto.getWarnLevel() != null && !"-1".equals(String.valueOf(dto.getWarnLevel()))) {
        sql += " and a.warn_Level = :warnLevel ";
        lstParam.put("warnLevel", dto.getWarnLevel());
      }
      if (dto.getCreateUnitId() != null) {
        if (dto.getSearchSubUnitCreate() != null && "1".equals(dto.getSearchSubUnitCreate())) {
          //Tim kiem ca don vi con
          sql += " AND a.create_unit_id IN (    SELECT   u.unit_id "
              + "                                  FROM   common_gnoc.unit u "
              + "                                 WHERE   LEVEL < 50 "
              + "                            START WITH   u.unit_id = :createUnitId "
              + "                            CONNECT BY   PRIOR u.unit_id = u.parent_unit_id) ";
        } else {
          //chi tim kiem don vi tao
          sql += " and a.create_unit_id = :createUnitId ";
        }
        lstParam.put("createUnitId", dto.getCreateUnitId());

      }
      if (dto.getReceiveUnitId() != null) {
        if (dto.getSearchSubUnitReceive() != null && "1".equals(dto.getSearchSubUnitReceive())) {
          //Tim kiem ca don vi con
          sql += " AND ( a.receive_unit_id IN ( SELECT   u.unit_id "
              + "                                  FROM   common_gnoc.unit u "
              + "                                 WHERE   LEVEL < 50 "
              + "                            START WITH   u.unit_id = :receiveUnitId "
              + "                            CONNECT BY   PRIOR u.unit_id = u.parent_unit_id ) "
//              + "or "
//              + " a.UNIT_MOVE IN ( SELECT   u.unit_id "
//              + "                                  FROM   common_gnoc.unit u "
//              + "                                 WHERE   LEVEL < 50 "
//              + "                            START WITH   u.unit_id = :unitMove "
//              + "                            CONNECT BY   PRIOR u.unit_id = u.parent_unit_id )"
              + ") ";
        } else {
          //chi tim kiem don xu ly
          sql += " AND (a.receive_unit_id = :receiveUnitId"
//              + " or a.UNIT_MOVE = :unitMove"
              + ")";
        }
        lstParam.put("receiveUnitId", dto.getReceiveUnitId());
//        lstParam.put("unitMove", dto.getReceiveUnitId());

      }

      //Dunglv3 add
      if (dto.getUnitMove() != null) {
        if (dto.getSearchUnitMove() != null && "1".equals(dto.getSearchUnitMove())) {
          //Tim kiem ca don vi con
          sql += " AND ( a.UNIT_MOVE IN ( SELECT   u.unit_id "
              + "                                  FROM   common_gnoc.unit u "
              + "                                 WHERE   LEVEL < 50 "
              + "                            START WITH   u.unit_id = :unitMove "
              + "                            CONNECT BY   PRIOR u.unit_id = u.parent_unit_id ) "
              + ") ";
        } else {
          sql +=
              " AND (a.UNIT_MOVE is not null AND a.RECEIVE_UNIT_ID <> :unitMove AND a.UNIT_MOVE = :unitMove"
                  + ")";
        }
        lstParam.put("unitMove", dto.getUnitMove());
      }
      //end

      if (dto.getResponeseUnitId() != null && !"".equals(dto.getResponeseUnitId())) {

        sql += " AND ( (a.create_unit_id =:createUnitId and a.state in (1, 9, 4, 6)) "
            + " or (a.receive_unit_id = :receiveUnitId and a.state in (3, 5, 8, 6, 7)) "
//            + " or a.UNIT_MOVE = :unitMove"
            + ")";

        lstParam.put("createUnitId", dto.getResponeseUnitId());
        lstParam.put("receiveUnitId", dto.getResponeseUnitId());
//        lstParam.put("unitMove", dto.getResponeseUnitId());
      }

      if (dto.getCreatedTimeFrom() != null && !"".equals(dto.getCreatedTimeFrom())) {
        sql += " AND a.created_time >= to_date(:createdTimeFrom,'dd/MM/yyyy HH24:MI:ss') - :offset/24 ";
        lstParam.put("createdTimeFrom", dto.getCreatedTimeFrom());
      }
      if (dto.getCreatedTimeTo() != null && !"".equals(dto.getCreatedTimeTo())) {
        sql += " AND a.created_time <= to_date(:createdTimeTo,'dd/MM/yyyy HH24:MI:ss')- :offset/24 ";
        lstParam.put("createdTimeTo", dto.getCreatedTimeTo());
      }

      if (dto.getEstimatedTimeToEndFrom() != null && !"".equals(dto.getEstimatedTimeToEndFrom())) {
        sql += " AND (NVL(a.BEGIN_TROUBLE_TIME, NULL) + a.TIME_PROCESS/24) >= to_date(:estimatedTimeToEndFrom,'dd/MM/yyyy HH24:MI:ss') - :offset/24 ";
        lstParam.put("estimatedTimeToEndFrom", dto.getEstimatedTimeToEndFrom());
      }
      if (dto.getEstimatedTimeToEndTo() != null && !"".equals(dto.getEstimatedTimeToEndTo())) {
        sql += " AND (NVL(a.BEGIN_TROUBLE_TIME, NULL) + a.TIME_PROCESS/24) <= to_date(:estimatedTimeToEndTo,'dd/MM/yyyy HH24:MI:ss')- :offset/24 ";
        lstParam.put("estimatedTimeToEndTo", dto.getEstimatedTimeToEndTo());
      }
      if (dto.getTypeId() != null && !"-1".equals(String.valueOf(dto.getTypeId()))) {
        sql += " and a.type_id = :typeId ";
        lstParam.put("typeId", dto.getTypeId());
      }
      if (dto.getAlarmGroupId() != null && !"-1".equals(dto.getAlarmGroupId())) {
        sql += " and a.alarm_group = :alarmGroupId ";
        lstParam.put("alarmGroupId", dto.getAlarmGroupId());
      }

      if (dto.getLstType() != null && !dto.getLstType().isEmpty()) {
        sql += " AND a.type_id in (:lstType) ";
        lstParam.put("lstType", dto.getLstType());
      }

      if (dto.getSubCategoryId() != null && !"-1".equals(String.valueOf(dto.getSubCategoryId()))) {
        sql += " and a.sub_category_id = :subCate ";
        lstParam.put("subCate", dto.getSubCategoryId());
      }
      if (dto.getVendorId() != null && !"-1".equals(String.valueOf(dto.getVendorId()))) {
        sql += " and a.vendor_id = :vendorId ";
        lstParam.put("vendorId", dto.getVendorId());
      }
      if (dto.getReceiveUserId() != null && !"-1".equals(String.valueOf(dto.getReceiveUserId()))) {
        sql += " and a.receive_user_id = :receiveUserId ";
        lstParam.put("receiveUserId", dto.getReceiveUserId());
      }
      if (dto.getCreateUserId() != null && !"-1".equals(String.valueOf(dto.getCreateUserId()))) {
        sql += " and a.create_user_id = :createUserId ";
        lstParam.put("createUserId", dto.getCreateUserId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getTroubleId())) {
        sql += " and a.trouble_id = :troubleId ";
        lstParam.put("troubleId", dto.getTroubleId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getRelatedTt()) && "1"
          .equals(dto.getRelatedTt())) {//la con
        sql += " and a.RELATED_TT is not null ";
      } else if (!StringUtils.isStringNullOrEmpty(dto.getRelatedTt()) && "2"
          .equals(dto.getRelatedTt())) {
        sql += " and a.trouble_code in (select RELATED_TT from one_tm.troubles where RELATED_TT is not null) ";
      } else if (!StringUtils.isStringNullOrEmpty(dto.getRelatedTt()) && "3"
          .equals(dto.getRelatedTt())) {
        sql += " and (a.trouble_code not in (select RELATED_TT from one_tm.troubles where RELATED_TT is not null) and a.RELATED_TT is null) ";
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getIsTickHelp()) && "1"
          .equals(String.valueOf(dto.getIsTickHelp()))) { //all
      } else if (!StringUtils.isStringNullOrEmpty(String.valueOf(dto.getIsTickHelp())) && "2"
          .equals(String.valueOf(dto.getIsTickHelp()))) { //not
        sql += " and (a.IS_TICK_HELP = 0 or a.IS_TICK_HELP  is null ) and (a.is_hidden is null ) ";
      } else if (!StringUtils.isStringNullOrEmpty(dto.getIsTickHelp()) && "3"
          .equals(String.valueOf(dto.getIsTickHelp()))) { //help
        sql += " and a.IS_TICK_HELP = 1 ";
      } else { //chi tim kiem tich help cho spm
        sql += " and ("
            + " ((a.INSERT_SOURCE = 'SPM' or a.INSERT_SOURCE = 'SPM_VTNET') AND (a.IS_TICK_HELP = 1 OR a.WO_CODE IS NULL)) "
            + " OR (a.INSERT_SOURCE  = 'BCCS' AND (a.IS_TICK_HELP = 1 OR a.AUTO_CLOSE = 0 OR a.AUTO_CLOSE is null OR a.WO_CODE IS NULL )) "
            + " OR (a.INSERT_SOURCE  not in ('SPM', 'BCCS', 'SPM_VTNET')) "
            + "  "
            + ") and (a.is_hidden is null )";
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getInsertSource())) {
        sql += " and a.INSERT_SOURCE IN (:insertSource) ";
        lstParam.put("insertSource", Arrays.asList(dto.getInsertSource().split(",")));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getRemainTime())) {
        sql +=
            " and :remainTime >= case when s.item_code in('WAITING RECEIVE','QUEUE','SOLUTION FOUND','CLEAR','CLOSED','CLOSED NOT KEDB','WAIT FOR DEFERRED')"
                + " then nvl(a.time_process,0)- (nvl(a.time_used,0) + round( (nvl(a.clear_time,sysdate)-nvl(a.assign_time_temp,a.assign_time))*24,2))"
                + "when s.item_code in ('DEFERRED')"
                + "then nvl(a.time_process,0)- nvl(a.time_used,0) "
                + "end ";

        lstParam.put("remainTime", dto.getRemainTime());
      }
      //Dunglv3 add nang cap
      if ("1".equals(dto.getCheckClearTime())) {
        sql += " and a.END_TROUBLE_TIME is not null";
      } else if ("0".equals(dto.getCheckClearTime())) {
        sql += " and a.END_TROUBLE_TIME is null";
      }
//      sql += " and a.TIME_PROCESS >= TO_DATE and a.TIME_PROCESS <= TO_DATE";
      if (!isCountState) {//neu khong phai count thi them dieu kiem order
        sql += " order by a.created_time desc";
      }

    }
    return sql;
  }

  @Override
  public TroublesInSideDTO getTroubleDTO(String troubleId, String troubleCode, String spmCode,
      String amiId,
      String complaintId, String fromDate, String toDate) {
    List<TroublesInSideDTO> ret = new ArrayList<>();
    TroublesInSideDTO troubleDTO = null;
    try {
      Map<String, String> lstParam = new HashMap<>();
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
          "tt-get-trouble-dto");

      if (!StringUtils.isStringNullOrEmpty(troubleId)) {
        sql += " and a.trouble_id = :troubleId ";
        lstParam.put("troubleId", troubleId);
      }

      if (!StringUtils.isStringNullOrEmpty(troubleCode)) {
        sql += " and a.trouble_code = :troubleCode ";
        lstParam.put("troubleCode", troubleCode);
      }

      if (!StringUtils.isStringNullOrEmpty(spmCode)) {
        sql += " and a.spm_code = :spmCode ";
        lstParam.put("spmCode", spmCode);
      }

      if (!StringUtils.isStringNullOrEmpty(amiId)) {
        sql += " and a.ami_id = :amiId ";
        lstParam.put("amiId", amiId);

        sql += " and a.created_time >= sysdate - 30 ";
      }

      if (!StringUtils.isStringNullOrEmpty(complaintId)) {
        sql += " and a.complaint_id = :complaintId ";
        lstParam.put("complaintId", complaintId);

      }
      if (!StringUtils.isStringNullOrEmpty(fromDate)) {
        sql += " and a.created_time >= to_date(:fromDate, 'dd/MM/yyyy HH24:mi:ss') ";
        lstParam.put("fromDate", fromDate);
      }

      if (!StringUtils.isStringNullOrEmpty(toDate)) {
        sql += " and a.created_time <= to_date(:toDate, 'dd/MM/yyyy HH24:mi:ss') ";
        lstParam.put("toDate", toDate);
      }

      ret = getNamedParameterJdbcTemplate()
          .query(sql, lstParam, BeanPropertyRowMapper.newInstance(TroublesInSideDTO.class));
      if (ret != null && !ret.isEmpty()) {
        troubleDTO = ret.get(0);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    return troubleDTO;
  }

  @Override
  public TroublesEntity findTroublesById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(TroublesEntity.class, id);
    }
    return null;
  }

  @Override
  public CfgServerNocDTO getServerDTO(CfgServerNocDTO cfgServerNocDTO) {
    try {
      Map<String, String> lstParam = new HashMap<String, String>();
      String sql = " SELECT CFG_SERVER_NOC_ID cfgServerNocId, "
          + " LINK link, LINK_NAME linkName, SERVER_NAME serverName, USER_NAME userName, "
          + " PASS pass, SALT salt, INSERT_SOURCE insertSource FROM ONE_TM.CFG_SERVER_NOC"
          + " where 1 = 1  ";

      if (!StringUtils.isStringNullOrEmpty(cfgServerNocDTO.getInsertSource())) {
        sql += " AND INSERT_SOURCE =:insertSource  ";
        lstParam.put("insertSource", cfgServerNocDTO.getInsertSource());
      }

      List<CfgServerNocDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, lstParam, BeanPropertyRowMapper.newInstance(CfgServerNocDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0);
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  public ResultInSideDto onUpdatebatchTrouble(TroublesInSideDTO tForm, String state)
      throws Exception {
    ResultInSideDto result = new ResultInSideDto();
    try {
      Map<String, Object> params = new HashMap<>();
      String sqlStr = "";
      if (Constants.TT_STATE.REJECT.equals(state)) {
        sqlStr = " UPDATE  ONE_TM.troubles set STATE = 4, REJECT_REASON =:rejectReason where INSERT_SOURCE = 'SPM' and ERROR_CODE = 1 and CONCAVE =:concave and CREATED_TIME > sysdate - 7 ";

      } else if (Constants.TT_STATE.CLEAR.equals(state)) {
        sqlStr =
            " UPDATE ONE_TM.troubles SET STATE = 9, END_TROUBLE_TIME = sysdate, CLEAR_TIME = sysdate, CLOSED_TIME = sysdate, "
                + "  REASON_ID =:reasonId , "
                + "  REASON_NAME =:reasonName , "
                + "  REASON_LV1_ID =:reasonLv1Id, "
                + "  REASON_LV1_NAME =:reasonLv1Name, "
                + "  REASON_LV2_ID =:reasonLv2Id, "
                + "  REASON_LV2_NAME =:reasonLv2Name, "
                + "  REASON_LV3_ID =:reasonLv3Id, "
                + "  REASON_LV3_NAME =:reasonLv3Name,"
                + "  WORK_ARROUND =:workArround, "
                + "  ROOT_CAUSE =:rootCause  ";

        if (!StringUtils.isStringNullOrEmpty(tForm.getReasonOverdue())) {
          sqlStr = sqlStr + " , REASON_OVERDUE =:reasonOverdue"
              + ", REASON_OVERDUE_ID =:reasonOverdueId"
              + ", REASON_OVERDUE_NAME =:reasonOverdueName"
              + ", REASON_OVERDUE_ID_2 =:reasonOverdueId2"
              + ", REASON_OVERDUE_NAME_2 =:reasonOverdueName2   ";
        }

        sqlStr = sqlStr + " WHERE INSERT_SOURCE = 'SPM' "
            + " AND ERROR_CODE      = 1 "
            + " AND CONCAVE         = :concave "
            + " AND CREATED_TIME    > sysdate - 7 ";
      } else {
        return result;
      }

      params.put("concave", tForm.getConcave());
      if (Constants.TT_STATE.REJECT.equals(state)) {
        params.put("rejectReason", tForm.getRejectReason());
      } else if (Constants.TT_STATE.CLEAR.equals(state)) {
        params.put("reasonId", tForm.getReasonId());
        params.put("reasonName", tForm.getReasonName());
        params.put("reasonLv1Id", tForm.getReasonLv1Id());
        params.put("reasonLv1Name", tForm.getReasonLv1Name());
        params.put("reasonLv2Id", tForm.getReasonLv2Id());
        params.put("reasonLv2Name", tForm.getReasonLv2Name());
        params.put("reasonLv3Id", tForm.getReasonLv3Id());
        params.put("reasonLv3Name", tForm.getReasonLv3Name());
        params.put("workArround", tForm.getWorkArround());
        params.put("rootCause", tForm.getRootCause());
        if (!StringUtils.isStringNullOrEmpty(tForm.getReasonOverdue())) {
          params.put("reasonOverdue", tForm.getReasonOverdue());
          params.put("reasonOverdueId", tForm.getReasonOverdueId());
          params.put("reasonOverdueName", tForm.getReasonOverdueName());
          params.put("reasonOverdueId2", tForm.getReasonOverdueId2());
          params.put("reasonOverdueName2", tForm.getReasonOverdueName2());
        }
      }
      getNamedParameterJdbcTemplate().update(sqlStr, params);

      result.setMessage(RESULT.SUCCESS);
      result.setKey(RESULT.SUCCESS);

    } catch (Exception e) {
      result.setId(null);
      result.setKey(RESULT.ERROR);
      result.setMessage(e.getMessage());
      log.error(e.getMessage(), e);
      throw e;
    }
    return result;
  }

  @Override
  public List<CatItemDTO> onSearchCountByState(TroublesInSideDTO dto) {
    List<CatItemDTO> lstCatItemDTOResult = new ArrayList<>();
    try {
      Map<String, Object> lstParam = new HashMap<>();
      String sql = createSql(dto, lstParam, true);
      sql = "select state as itemId, count(*) itemValue from (" + sql + ") group by state";

      lstCatItemDTOResult = getNamedParameterJdbcTemplate().query(sql, lstParam,
          BeanPropertyRowMapper.newInstance(CatItemDTO.class));

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    return lstCatItemDTOResult;
  }


  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    TroublesEntity troublesEntity = getEntityManager().find(TroublesEntity.class, id);
    getEntityManager().remove(troublesEntity);
    return resultInSideDTO;
  }

  @Override
  public List<TroublesInSideDTO> getTroubleInfo(TroublesInSideDTO dto) {
    BaseDto baseDto = sqlGetTroublesInfor(dto);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(), BeanPropertyRowMapper
            .newInstance(TroublesInSideDTO.class));
  }

  public BaseDto sqlGetTroublesInfor(TroublesInSideDTO dto) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
        "tt-get-troubles-info");
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {

      if (dto.getTroubleCode() != null && !"".equals(dto.getTroubleCode())) {
        sql += " and lower(a.trouble_code) = :troubleCode  ";
        parameters.put("troubleCode", dto.getTroubleCode().trim().toLowerCase());
      }
      if (dto.getCreatedTimeFrom() != null && !"".equals(dto.getCreatedTimeFrom())) {
        sql += " AND a.created_time >= to_date(:createdTimeFrom,'dd/MM/yyyy HH24:MI:ss') ";
        parameters.put("createdTimeFrom", dto.getCreatedTimeFrom());
      }
      if (dto.getCreatedTimeTo() != null && !"".equals(dto.getCreatedTimeTo())) {
        sql += " AND a.created_time <= to_date(:createdTimeTo,'dd/MM/yyyy HH24:MI:ss') ";
        parameters.put("createdTimeTo", dto.getCreatedTimeTo());
      }
    }
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<TroublesDTO> onSynchTrouble(String fromDate, String toDate, String insertSource,
      String subCategoryCode, String tableCurrent) {
    BaseDto baseDto = buildSqlOnSynchTrouble(fromDate, toDate, insertSource, subCategoryCode,
        tableCurrent);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(), BeanPropertyRowMapper
            .newInstance(TroublesDTO.class));
  }

  public BaseDto buildSqlOnSynchTrouble(String fromDate, String toDate, String insertSource,
      String subCategoryCode, String tableCurrent) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
        "on-synch-trouble");
    if (!DataUtil.isNullOrEmpty(fromDate)) {
      sql += " AND a.last_update_time > to_date(:fromDate,'dd/MM/yyyy HH24:MI:ss') ";
      parameters.put("fromDate", fromDate);
    }
    if (!DataUtil.isNullOrEmpty(toDate)) {
      sql += " AND a.last_update_time <= to_date(:toDate,'dd/MM/yyyy HH24:MI:ss') ";
      parameters.put("toDate", toDate);
    }
    if (!DataUtil.isNullOrEmpty(insertSource)) {
      sql += " AND a.INSERT_SOURCE =:insertSource ";
      parameters.put("insertSource", insertSource);
    } else {
      sql += " AND a.INSERT_SOURCE = 'NOC' ";
    }
    if (!DataUtil.isNullOrEmpty(subCategoryCode)) {
      sql += " AND mang.item_code =:subCategoryCode ";
      parameters.put("subCategoryCode", subCategoryCode);
    }
    if (!DataUtil.isNullOrEmpty(tableCurrent)) {
      sql += " AND a.TBL_CURR =:tableCurrent ";
      parameters.put("tableCurrent", tableCurrent);
    }
    baseDto.setParameters(parameters);
    baseDto.setSqlQuery(sql);
    return baseDto;
  }


  @Override
  public List<TroublesDTO> onSearchForSPM(TroublesDTO dto, String account, String spmCode,
      Long typeSearch) {
    List<TroublesDTO> troublesDTOList = new ArrayList<>();
    try {
      BaseDto baseDto = buildSqlOnSearchForSPM(dto, account, spmCode, typeSearch);
      troublesDTOList = getNamedParameterJdbcTemplate()
          .query(baseDto.getSqlQuery(), baseDto.getParameters(), BeanPropertyRowMapper
              .newInstance(TroublesDTO.class));
      List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
          Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
          Constants.APPLIED_BUSSINESS.CAT_ITEM.toString());
      try {
        troublesDTOList = setLanguage(troublesDTOList, lstLanguage, "priorityId", "priorityName");
        troublesDTOList = setLanguage(troublesDTOList, lstLanguage, "state", "stateName");
        troublesDTOList = setLanguage(troublesDTOList, lstLanguage, "typeId", "typeName");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return troublesDTOList;
  }

  public BaseDto buildSqlOnSearchForSPM(TroublesDTO dto, String account, String spmCode,
      Long typeSearch) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
        "sql-On-Search-For-SPM");
    if (typeSearch.equals(1L)) {
      sql += " and a.trouble_id in (select incident_id from it_spm_info where spm_code = :spmCode )";
      parameters.put("spmCode", spmCode);
    } else if (typeSearch.equals(0L)) {
      sql += " and s.item_code not in ('CLOSED','CANCELED','CLOSED NOT KEDB') "
          + " and a.trouble_id not in (select incident_id from it_spm_info where spm_code = :spmCode )";
      parameters.put("spmCode", spmCode);
      if (account != null) {
        sql += " and a.trouble_id in (select incident_id from it_account where lower(account) = lower(:account)) ";
        parameters.put("account", account);
      }
      if (dto != null) {
        if (dto.getUserUnitId() != null) {
          sql += " and a.create_unit_id = :userUnitId ";
          parameters.put("userUnitId", dto.getUserUnitId());
        }
        if (dto.getKeyword() != null && !"".equals(dto.getKeyword())) {
          sql += " AND (lower(a.trouble_code) like :keyword escape '\\' "
              + " or lower(a.trouble_name) like :keyword escape '\\' "
              + " or lower(a.description) like :keyword escape '\\' )";
          parameters
              .put("keyword", StringUtils.convertLowerParamContains(dto.getKeyword()));
        }
        if (dto.getLocationId() != null && !"-1".equals(dto.getLocationId())) {
          sql += " AND a.location_id in (select location_id from common_gnoc.cat_location start with location_id = :locationId  connect by  prior location_id = parent_id) "; //HaiNV20 fixbug
          parameters.put("locationId", dto.getLocationId());
        }
        if (dto.getTroubleCode() != null && !"".equals(dto.getTroubleCode())) {
          sql += " and lower(a.trouble_code) like :troubleCode escape '\\' ";
          parameters
              .put("troubleCode", StringUtils.convertLowerParamContains(dto.getTroubleCode()));
        }
        if (dto.getTroubleName() != null && !"".equals(dto.getTroubleName())) {
          sql += " and lower(a.trouble_name) like :troubleName escape '\\' ";
          parameters
              .put("troubleName", StringUtils.convertLowerParamContains(dto.getTroubleName()));
        }
        if (dto.getImpactId() != null && !"-1".equals(dto.getImpactId())) {
          sql += " and a.impact_id = :impactId ";
          parameters.put("impactId", dto.getImpactId());
        }
        if (dto.getPriorityId() != null && !"-1".equals(dto.getPriorityId())) {
          //lay danh sach priority
          List<String> lst = Arrays.asList((dto.getPriorityId()).split(","));
          if (lst != null && !lst.isEmpty()) {
            sql +=
                " and a.priority_id in (select ci.ITEM_ID from common_gnoc.cat_item ci where  ci.ITEM_CODE in( "
                    + "select ITEM_VALUE from common_gnoc.cat_item where  ITEM_CODE in (:priorityId))) ";
            parameters.put("priorityId", lst);
          }
        }
        if (dto.getCreateUnitId() != null) {
          if (dto.getSearchSubUnitCreate() != null && "1".equals(dto.getSearchSubUnitCreate())) {
            //Tim kiem ca don vi con
            sql += " AND a.create_unit_id IN (    SELECT   u.unit_id "
                + "                                  FROM   common_gnoc.unit u "
                + "                                 WHERE   LEVEL < 50 "
                + "                            START WITH   u.unit_id = :createUnitId "
                + "                            CONNECT BY   PRIOR u.unit_id = u.parent_unit_id) ";
          } else {
            //chi tim kiem don vi tao
            sql += " and a.create_unit_id = :createUnitId ";
          }
          parameters.put("createUnitId", dto.getCreateUnitId());

        }
        if (dto.getReceiveUnitId() != null) {
          if (dto.getSearchSubUnitReceive() != null && "1".equals(dto.getSearchSubUnitReceive())) {
            //Tim kiem ca don vi con
            sql += " AND a.receive_unit_id IN (    SELECT   u.unit_id "
                + "                                  FROM   common_gnoc.unit u "
                + "                                 WHERE   LEVEL < 50 "
                + "                            START WITH   u.unit_id = :receiveUnitId "
                + "                            CONNECT BY   PRIOR u.unit_id = u.parent_unit_id) ";
          } else {
            //chi tim kiem don vi tao
            sql += "AND a.receive_unit_id = :receiveUnitId ";
          }
          parameters.put("receiveUnitId", dto.getReceiveUnitId());
        }
        if (dto.getCreatedTimeFrom() != null && !"".equals(dto.getCreatedTimeFrom())) {
          sql += " AND a.created_time >= to_date(:createdTimeFrom,'dd/MM/yyyy HH24:MI:ss') ";
          parameters.put("createdTimeFrom", dto.getCreatedTimeFrom());
        }
        if (dto.getCreatedTimeTo() != null && !"".equals(dto.getCreatedTimeTo())) {
          sql += " AND a.created_time <= to_date(:createdTimeTo,'dd/MM/yyyy HH24:MI:ss') ";
          parameters.put("createdTimeTo", dto.getCreatedTimeTo());
        }
        if (dto.getTypeId() != null && !"-1".equals(dto.getTypeId())) {
          sql += " and a.type_id = :typeId ";
          parameters.put("typeId", dto.getTypeId());
        }
        if (dto.getSubCategoryId() != null && !"-1"
            .equals(dto.getSubCategoryId())) {
          sql += " and a.sub_category_id = :subCate ";
          parameters.put("subCate", dto.getSubCategoryId());
        }
        if (dto.getVendorId() != null && !"-1".equals(dto.getVendorId())) {
          sql += " and a.vendor_id = :vendorId ";
          parameters.put("vendorId", dto.getVendorId());
        }
        if (dto.getReceiveUserId() != null && !"-1"
            .equals(dto.getReceiveUserId())) {
          sql += " and a.receive_user_id = :receiveUserId ";
          parameters.put("receiveUserId", dto.getReceiveUserId());
        }
        if (dto.getCreateUserId() != null && !"-1".equals(dto.getCreateUserId())) {
          sql += " and a.create_user_id = :createUserId ";
          parameters.put("createUserId", dto.getCreateUserId());
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getTroubleId())) {
          sql += " and a.trouble_id = :troubleId ";
          parameters.put("troubleId", dto.getTroubleId());
        }
      }
      sql += " order by a.created_time desc";
    }
    baseDto.setParameters(parameters);
    baseDto.setSqlQuery(sql);
    return baseDto;
  }

  @Override
  public List<TroublesDTO> onSearchForVsmart(TroublesDTO dto, Integer startRow,
      Integer pageLength) {
    try {
      BaseDto baseDto = buildSqlOnSearchForVsmart(dto);
      if (startRow != null && pageLength != null && startRow >= 0 && pageLength > 0) {
        Map<String, Object> params = baseDto.getParameters();
        String sql = baseDto.getSqlQuery();
        Query query = getEntityManager().createNativeQuery(sql);
        query.unwrap(SQLQuery.class).
            addScalar("troubleId", new StringType()).
            addScalar("troubleName", new StringType()).
            addScalar("description", new StringType()).
            addScalar("troubleCode", new StringType()).
            addScalar("state", new StringType()).
            addScalar("stateName", new StringType()).
            addScalar("timeUsed", new StringType()).
            addScalar("remainTime", new StringType()).
            addScalar("beginTroubleTime", new StringType()).
            addScalar("endTroubleTime", new StringType()).
            addScalar("receiveUnitId", new StringType()).
            addScalar("receiveUserId", new StringType()).
            addScalar("receiveUnitName", new StringType()).
            addScalar("receiveUserName", new StringType()).
            addScalar("infoTicket", new StringType()).
            addScalar("rootCause", new StringType()).
            addScalar("solutionType", new StringType()).
            addScalar("workLog", new StringType()).
            addScalar("workArround", new StringType()).
            addScalar("createUnitName", new StringType()).
            addScalar("createUserName", new StringType()).
            setResultTransformer(Transformers.aliasToBean(TroublesDTO.class));
        if (!params.isEmpty()) {
          for (Map.Entry<String, Object> map : params.entrySet()) {
            query.setParameter(map.getKey(), map.getValue());
          }
        }

        query.setFirstResult(startRow);
        query.setMaxResults(pageLength);
        return query.getResultList();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public int onSearchCountForVsmart(TroublesDTO dto) {
    BaseDto baseDto = buildSqlOnSearchForVsmart(dto);
    Map<String, Object> params = baseDto.getParameters();
    String sql = "select count(*) totalRow from (" + baseDto.getSqlQuery() + ")";
    List<BaseDto> totalDTO = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(
            BaseDto.class));
//    query.setParameter(parameters);
    int result;
    if (totalDTO != null && totalDTO.size() > 0) {
      result = totalDTO.get(0).getTotalRow();
    } else {
      result = 0;
    }
    return result;
  }

  public BaseDto buildSqlOnSearchForVsmart(TroublesDTO dto) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
        "sql-On-Search-For-Vsmart");
    if (dto != null) {
      if (dto.getLstState() != null && !dto.getLstState().isEmpty()) {
        sql += " AND s.item_code in (:lstState) ";
        parameters.put("lstState", dto.getLstState());
      }
      if (dto.getTroubleCode() != null && !"".equals(dto.getTroubleCode())) {
        sql += " and lower(a.trouble_code) like :troubleCode escape '\\' ";
        parameters.put("troubleCode", StringUtils.convertLowerParamContains(dto.getTroubleCode()));
      }
      if (dto.getTroubleName() != null && !"".equals(dto.getTroubleName())) {
        sql += " and lower(a.trouble_name) like :troubleName escape '\\' ";
        parameters.put("troubleName", StringUtils.convertLowerParamContains(dto.getTroubleName()));
      }
      if (dto.getCreateUnitId() != null && !"".equals(dto.getCreateUnitId())) {
        sql += " and a.create_unit_id in (select unit_id from common_gnoc.users where USERNAME =:createUserName )";
        parameters.put("createUserName", dto.getCreateUnitId());
      }
      if (dto.getCreateUserId() != null && !"".equals(dto.getCreateUserId())) {
        sql += " and a.create_user_id = :createUserId ";
        parameters.put("createUserId", dto.getCreateUserId());
      }
      if (dto.getCreateUserName() != null && !"".equals(dto.getCreateUserName())) {
        sql += " and a.create_user_id in (select USER_ID from common_gnoc.users where USERNAME =:createUserName ) ";
        parameters.put("createUserName", dto.getCreateUserName());
      }
      if (dto.getCreatedTimeFrom() != null && !"".equals(dto.getCreatedTimeFrom())) {
        sql += " AND a.created_time >= to_date(:createdTimeFrom,'dd/MM/yyyy HH24:MI:ss') ";
        parameters.put("createdTimeFrom", dto.getCreatedTimeFrom());
      }
      if (dto.getCreatedTimeTo() != null && !"".equals(dto.getCreatedTimeTo())) {
        sql += " AND a.created_time <= to_date(:createdTimeTo,'dd/MM/yyyy HH24:MI:ss') ";
        parameters.put("createdTimeTo", dto.getCreatedTimeTo());
      }
      if (!DataUtil.isNullOrEmpty(dto.getInsertSource())) {
        sql += " and a.INSERT_SOURCE = :insertSource ";
        parameters.put("insertSource", dto.getInsertSource());
      }
      sql += " order by a.created_time desc";
    }
    baseDto.setParameters(parameters);
    baseDto.setSqlQuery(sql);
    return baseDto;
  }

  @Override
  public List<UnitEntity> getUnitByUnitDTO(UnitDTO unitId) {
    List<UnitEntity> lst = null;
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES, "tt-get_unit-by_unitDTO");
      if (!StringUtils.isStringNullOrEmpty(unitId.getUnitId())) {
        sql += " AND UNIT_ID =:unitId  ";
        params.put("unitId", unitId.getUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(unitId.getUnitCode())) {
        sql += " AND lower(UNIT_CODE) =:unitCode  ";
        params.put("unitCode", unitId.getUnitCode().trim().toLowerCase());
      }

      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(UnitEntity.class));

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return lst;
  }

  @Override
  public List<TroublesDTO> countTroubleByStation(String stationCode, String startTime,
      String endTime, String priority, int type) {
    List<TroublesDTO> ret = new ArrayList<>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES, "count-trouble-by-station");
      Map<String, Object> params = new HashMap<>();
      params.put("startTime", startTime);
      params.put("endTime", endTime);
      params.put("stationCode", stationCode);
      if (type == 1) {
        sql = sql + " SELECT NVL( COUNT(*),0) troubleId "
            + "  ,a.priority_id priorityId "
            + " FROM tb a "
            + " GROUP BY a.priority_id "
            + " ORDER BY a.priority_id DESC ";
      } else if (type == 2) { //tung ngay + muc do
        sql = sql + " SELECT NVL( COUNT(*),0) troubleId "
            + "  ,a.priority_id priorityId "
            + "  ,to_char(TRUNC(a.CREATED_TIME), 'dd/MM/yyyy') createdTime "
            + " FROM tb a "
            + " GROUP BY TRUNC(a.CREATED_TIME), a.priority_id   "
            + " ORDER BY TRUNC(a.CREATED_TIME), priorityId DESC ";
      } else if (type == 3) {// nguyen nhan
        sql = sql + " SELECT NVL(COUNT(*),0) troubleId "
            + "  ,a.reason_id reasonId "
            + "  ,a.reason_name reasonName "
            + " FROM tb a where a.reason_id is not null "
            + " GROUP BY a.reason_id ,a.reason_name "
            + " ORDER BY troubleId DESC ";
      } else {//chi tiet theo muc do
        sql = sql
            + " SELECT TROUBLE_NAME troubleName, beginTroubleTime, endTroubleTime, item_name state ,"
            + " RECEIVE_UNIT_NAME receiveUnitName, reason_name reasonName "
            + " FROM tb a where priority_id =:priorityId ";
        params.put("priorityId", priority);
      }
      ret = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(TroublesDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return ret;
  }

  @Override
  public Datatable searchParentTTForCR(TroublesInSideDTO troublesInSideDTO) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> params = new HashMap<>();
    sql.append(
        "select t.TROUBLE_ID itemValue, t.TROUBLE_CODE itemName from one_tm.troubles t where 1 = 1 ");
    if (troublesInSideDTO.getLstState() != null && troublesInSideDTO.getLstState().size() > 0) {
      sql.append(" and t.STATE in (:lstStatus) ");
      params.put("lstStatus", troublesInSideDTO.getLstState());
    }
    if (troublesInSideDTO.getReceiveUserId() != null) {
      sql.append(" and t.RECEIVE_USER_ID = :receiveUserId ");
      params.put("receiveUserId", troublesInSideDTO.getReceiveUserId());
    }
    if (StringUtils.isNotNullOrEmpty(troublesInSideDTO.getTroubleCode())) {
      sql.append(" AND LOWER(t.trouble_code) like :troubleCode escape '\\' ");
      params.put("troubleCode",
          StringUtils.convertLowerParamContains(troublesInSideDTO.getTroubleCode()));
    }

    return getListDataTableBySqlQuery(sql.toString(),
        params, troublesInSideDTO.getPage(), troublesInSideDTO.getPageSize(),
        CatItemDTO.class, troublesInSideDTO.getSortName(), troublesInSideDTO.getSortType());
  }

  @Override
  public List<TroublesInSideDTO> countTicketByShift(TroublesInSideDTO troublesDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES, "countTicketByShift");
    Map<String, Object> params = new HashMap<>();
    params.put("startTime", troublesDTO.getStartTime());
    params.put("endTime", troublesDTO.getEndTime());
    params.put("receiveUnitId", troublesDTO.getUnitId());

    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(TroublesInSideDTO.class));
  }

  @Override
  public List<TroublesDTO> getTroubleByCode(String troubleCode) {
    if (StringUtils.isNotNullOrEmpty(troubleCode)) {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES, "get-Trouble-By-Code");
      Map<String, Object> params = new HashMap<>();
      params.put("troubleCode", troubleCode);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(TroublesDTO.class));
    }
    return null;
  }

  @Override
  public List<TroubleStatisticForm> getStatisticTroubleDetail(String unitId, Boolean isCreateUnit,
      Boolean searchChild, String startTime, String endTime) {
    List<TroubleStatisticForm> lstResult = new ArrayList<>();
    try {
      SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES, "getStatisticTroubleDetail");
      Map<String, Object> params = new HashMap<>();
      if (!isCreateUnit) {
        if (searchChild) {
          sql = sql + " and a.receive_unit_id in ("
              + " select unit_id from common_gnoc.unit "
              + " start with unit_id = :unitId"
              + " connect by PRIOR unit_id=parent_unit_id"
              + " )";
        } else {
          sql = sql + " and a.receive_unit_id = :unitId";
        }
      } else {
        if (searchChild) {
          sql = sql + " and a.create_unit_id in ("
              + " select unit_id from common_gnoc.unit "
              + " start with unit_id = :unitId"
              + " connect by PRIOR unit_id=parent_unit_id"
              + " )";
        } else {
          sql = sql + " and a.create_unit_id = :unitId";
        }
      }
      sql = sql + " and a.created_time BETWEEN :startTime and :endTime "
          + " and s.item_code in ('WAIT FOR DEFERRED','SOLUTION FOUND','WAITING RECEIVE','QUEUE','DEFERRED')"
          + " ";
      params.put("unitId", Long.parseLong(unitId));
      params.put("startTime", dfm.parse(startTime));
      params.put("endTime", dfm.parse(endTime));

      lstResult = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(TroubleStatisticForm.class));
    } catch (Exception e) {
      log.info(e.getMessage(), e);
    }
    return lstResult;
  }

  @Override
  public List<GnocFileDto> getFileByTrouble(TroublesDTO trouble) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES, "get-File-By-Trouble");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("troubleId", trouble.getTroubleId());
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(GnocFileDto.class));
  }

  @Override
  public ResultInSideDto insertCommonFile(CommonFileDTO commonFileDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CommonFileEntity commonFileEntity = getEntityManager()
        .merge(commonFileDTO.toEntity());
    resultInSideDTO.setId(commonFileEntity.getFileId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertTroubleFile(TroubleFileEntity entity) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    TroubleFileEntity troubleFileEntity = getEntityManager().merge(entity);
    resultInSideDTO.setId(troubleFileEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteTroubleFileByTroubleId(Long troubleId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<TroubleFileEntity> list = (List<TroubleFileEntity>) findByMultilParam(
        TroubleFileEntity.class, "troubleId", troubleId);
    if (list != null && list.size() > 0) {
      for (TroubleFileEntity entity : list) {
        getEntityManager().remove(entity);
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<GnocFileDto> getTroubleFileDTO(GnocFileDto gnocFileDto) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
        "get-Trouble-File-DTO");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("troubleId", gnocFileDto.getBusinessId());
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(GnocFileDto.class));
  }

  @Override
  public Datatable getListFileAttachByTroubleId(GnocFileDto gnocFileDto) {
    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
        "get-list-file-trouble");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_business_code", GNOC_FILE_BUSSINESS.TROUBLES);
    parameters.put("p_business_id", gnocFileDto.getBusinessId());
    return getListDataTableBySqlQuery(sqlQuery,
        parameters, gnocFileDto.getPage(), gnocFileDto.getPageSize(),
        GnocFileDto.class, gnocFileDto.getSortName(), gnocFileDto.getSortType());
  }

  @Override
  public List<ProblemGroupDTO> getListProblemGroupParent(CfgServerNocDTO cfgServerNocDTO)
      throws Exception {
    return wsbccs2Port.getListProblemGroupParent(cfgServerNocDTO);
  }

  @Override
  public List<ProblemGroupDTO> getListProblemGroupByParrenId(Long probGroupId,
      CfgServerNocDTO cfgServerNocDTO)
      throws Exception {
    return wsbccs2Port.getListProblemGroupByParrenId(probGroupId, cfgServerNocDTO);
  }

  @Override
  public List<ProblemTypeDTO> getListPobTypeByGroupId(Long probGroupId,
      CfgServerNocDTO cfgServerNocDTO)
      throws Exception {
    return wsbccs2Port.getListPobTypeByGroupId(probGroupId, cfgServerNocDTO);
  }

  @Override
  public String getKedbByComplaint(String complaintId) {
    String kedbCode = "";
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> parram = new HashMap<>();
      sql.append(
          " SELECT KEDB_CODE FROM COMMON_GNOC.MAP_PROB_TO_KEDB where PROB_TYPE_ID_LV3 =:complaintId ");
      parram.put("complaintId", complaintId);
      List<KedbDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql.toString(), parram, BeanPropertyRowMapper.newInstance(KedbDTO.class));
      if (lst != null) {
        kedbCode = lst.get(0).getKedbCode();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return kedbCode;
  }

  @Override
  public UsersDTO getUserByUserLogin(String userName) {
    UsersDTO usersDTO = new UsersDTO();
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = " SELECT * FROM COMMON_GNOC.USERS WHERE 1 = 1";

      if (!StringUtils.isStringNullOrEmpty(userName)) {
        sql += " AND USERNAME = :userName";
        params.put("userName", userName);
      }
      List<UsersDTO> listUser = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(UsersDTO.class));
      if (!listUser.isEmpty()) {
        return listUser.get(0);
      }
    } catch (HibernateException e) {
      log.error(e.getMessage());
    }
    return usersDTO;
  }

  @Override
  public List<TroublesDTO> getTroubleInfoForVsSmart(TroublesDTO troublesDTO) {
    List<TroublesDTO> list = new ArrayList<>();
    try {
      Map<String, Object> parameters = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES, "tt-get-trouble-info-for-vsmart");
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getCreatedTimeFrom())) {
        sql += " AND t1.CREATED_TIME   >= TO_DATE(:createdTimeFrom, 'dd/MM/yyyy HH24:mi:ss')";
        parameters.put("createdTimeFrom", troublesDTO.getCreatedTimeFrom());
      }
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getCreatedTimeTo())) {
        sql += " AND t1.CREATED_TIME   <= TO_DATE(:createdTimeTo, 'dd/MM/yyyy HH24:mi:ss')";
        parameters.put("createdTimeTo", troublesDTO.getCreatedTimeTo());
      }
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getReceiveUserId())) {
        sql += " AND t1.RECEIVE_USER_ID = :userID";
        parameters.put("userID", troublesDTO.getReceiveUserId());
      }
      if (!troublesDTO.getListState().isEmpty()) {
        sql += " AND t1.STATE IN (:listStatus)";
        parameters.put("listStatus", troublesDTO.getListState());
      }
      sql += " ORDER BY t1.CREATED_TIME DESC ";

      list = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(TroublesDTO.class));
      return list;

    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return list;
  }

  @Override
  public TroublesDTO findTroublesDtoById(Long id) {
    List<TroublesDTO> list = new ArrayList<>();
    TroublesDTO troublesDTO = new TroublesDTO();
    try {
      Map<String, Object> parameters = new HashMap<>();
      String sql = " SELECT  T.*, T.TROUBLE_ID troubleId, T.TROUBLE_CODE troubleCode,TO_CHAR(T.CREATED_TIME,'dd/MM/yyyy HH24:mi:ss') createdTime,TO_CHAR(T.LAST_UPDATE_TIME,'dd/MM/yyyy HH24:mi:ss') lastUpdateTime FROM ONE_TM.TROUBLES T WHERE 1 = 1";
      sql += " AND T.TROUBLE_ID = :troubleId";
      parameters.put("troubleId", id);
      list = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(TroublesDTO.class));
      if (!list.isEmpty()) {
        return list.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return troublesDTO;
  }

  @Override
  public ResultDTO updateTroubleFromVSMART(TroublesDTO troublesDTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = " UPDATE ONE_TM.TROUBLES SET ";
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getState())) {
        sql += " STATE = :state , ";
        params.put("state", troublesDTO.getState());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getInfoTicket())) {
        sql += " INFO_TICKET = :infoTicket , ";
        params.put("infoTicket", troublesDTO.getInfoTicket());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getReasonId())) {
        sql += " REASON_ID = :reasonId , ";
        params.put("reasonId", troublesDTO.getReasonId());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getRootCause())) {
        sql += " ROOT_CAUSE = :rootCause , ";
        params.put("rootCause", troublesDTO.getRootCause());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getSolutionType())) {
        sql += " SOLUTION_TYPE = :solutionType , ";
        params.put("solutionType", troublesDTO.getSolutionType());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getWorkArround())) {
        sql += " WORK_ARROUND = :workArround , ";
        params.put("workArround", troublesDTO.getWorkArround());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getRejectedCode())) {
        sql += " REJECTED_CODE = :rejectedCode , ";
        params.put("rejectedCode", troublesDTO.getRejectedCode());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getRejectReason())) {
        sql += " REJECT_REASON = :rejectReason , ";
        params.put("rejectReason", troublesDTO.getRejectReason());
      }
      sql += " REJECT_TIME = sysdate WHERE TROUBLE_ID = :troubleId ";
      params.put("troubleId", troublesDTO.getTroubleId());
      getNamedParameterJdbcTemplateNormal().update(sql, params);
      resultDTO.setId(troublesDTO.getTroubleId());
    } catch (Exception e) {
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(RESULT.FAIL);
      log.error(e.getMessage());
    }
    return resultDTO;
  }

  @Override
  public UsersDTO getUnitIdByUserId(Long Id) {
    List<UsersDTO> list = new ArrayList<>();
    UsersDTO usersDTO = new UsersDTO();
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = " SELECT * FROM COMMON_GNOC.USERS WHERE 1 = 1 ";
      if (Id != null) {
        sql += " AND USER_ID = :userId";
        params.put("userId", Id);
      }
      list = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(UsersDTO.class));
      if (!list.isEmpty()) {
        usersDTO = list.get(0);
      }

    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return usersDTO;
  }

  @Override
  public List<ItemDataCRInside> getListLocationCombobox(Long parentId) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES, "tt-get-location");
    if (parentId != null) {
      sql += "  START WITH PARENT_ID        =:parentId";
      parameters.put("parentId", parentId);
    }
    sql += " CONNECT BY prior PARENT_ID = parent_id";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public ResultInSideDto insertTroublesHistory(TroubleActionLogsDTO troubleActionLogsDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(troubleActionLogsDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public TroubleActionLogsDTO getTroubleActionLogDTOByTroubleId(Long id) {
    List<TroubleActionLogsDTO> lst = new ArrayList<>();
    TroubleActionLogsDTO troubleActionLogsDTO = new TroubleActionLogsDTO();
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = " SELECT * FROM TROUBLE_ACTION_LOGS WHERE 1 = 1";
      if (id != null) {
        sql += " AND ID = :id";
        params.put("id", id);
      }
      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(TroubleActionLogsDTO.class));
      if (!lst.isEmpty()) {
        troubleActionLogsDTO = lst.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return troubleActionLogsDTO;
  }

  // thangdt
  @Override
  public WoInsideDTO checkStationCodeTTForWo(Long id) {
    List<WoInsideDTO> lst = new ArrayList<>();
    WoInsideDTO woInsideDTO = new WoInsideDTO();
    Map<String, Object> params = new HashMap<>();
    try {
      String sql =
          " select station_code stationCode, stn.station_id, stn.nation_code, dev.nation_code, trb.nation_code from common_gnoc.infra_stations stn\n"
              + "join common_gnoc.infra_device dev\n"
              + "on stn.station_id = dev.station_id\n"
              + "and nvl(stn.nation_code, 'VNM') = nvl(dev.nation_code, 'VNM')\n"
              + "join one_tm.troubles trb\n"
              + "on trb.affected_node = dev.device_code\n"
              + "and nvl(dev.nation_code, 'VNM') = nvl(trb.nation_code, 'VNM')\n"
              + "where trb.trouble_id = :troubleId\n";
      params.put("troubleId", id);
      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
      if (!lst.isEmpty()) {
        return woInsideDTO = lst.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return woInsideDTO;
  }

  // thangdt
  @Override
  public CatItemDTO convertIdToName(Long itemId, Long categoryId) {
    List<CatItemDTO> lst = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    Map<String, Object> params = new HashMap<>();
    String sql;
    try {
      if (categoryId == null) {
        sql = " select CAT.ITEM_ID, CAT.ITEM_NAME from COMMON_GNOC.CAT_ITEM CAT where item_id = :itemId";
      } else {
        sql = " select CAT.ITEM_ID, CAT.ITEM_NAME from COMMON_GNOC.CAT_ITEM CAT where category_id = :categoryId and item_id = :itemId ";
        params.put("categoryId", categoryId);
      }
      params.put("itemId", itemId);
      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
      if (!lst.isEmpty()) {
        return catItemDTO = lst.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return catItemDTO;
  }


  // Thangdt get danh sach dia ban theo like
  @Override
  public List<DataItemDTO> getListDistrictByLocationName(String name) {
    List<DataItemDTO> lst = new ArrayList<>();
    Map<String, Object> params = new HashMap<>();

    try {
      String sql = " select * from (SELECT\n"
          + "location_id itemId,\n"
          + "location_code itemCode,\n"
          + "SYS_CONNECT_BY_PATH(location_name, ' / ') itemName,\n"
          + "(location_name || ' (' || location_code || ')') description,\n"
          + "parent_id parenItemId,\n"
          + "LEVEL itemValue\n"
          + "FROM common_gnoc.cat_location\n"
          + "WHERE STATUS = 1 START WITH parent_id IS NULL CONNECT BY PRIOR location_id = parent_id) ";
      if (!StringUtils.isStringNullOrEmpty(name)) {
        sql += "  where lower(itemName) like :itemName escape '\\' ";
        params.put("itemName", StringUtils.convertLowerParamContains(name));
      }
      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(DataItemDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return lst;
  }

  @Override
  public List<ItemDataTT> getTroubleReasonTreeById(String typeId, String id) {
    List<ItemDataTT> lst = new ArrayList<>();
    Map<String, Object> params = new HashMap<>();
    try {
      String sql =
          " select level, a.ID itemId,a.reason_code itemCode,a.reason_name itemName,a.parent_id parentItemId from ONE_TM.cat_reason a "
              + " where level >0 and type_id =:typeId start with a.id =:id "
              + " connect by prior a.parent_id  = a.id "
              + " order by level DESC ";
      params.put("typeId", typeId);
      params.put("id", id);
      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataTT.class));
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return lst;
  }

  @Override
  public List<ItemDataTT> getReasonByParentId(String parentId,
      String typeId) {
    List<ItemDataTT> lst = new ArrayList<>();
    Map<String, Object> params = new HashMap<>();
    try {
      String sql =
          " select a.ID itemId,a.reason_code itemCode,a.reason_name itemName,a.parent_id parentItemId from ONE_TM.cat_reason a "
              + " where  a.parent_id =:parentId and a.type_id =:typeId ";
      params.put("typeId", typeId);
      params.put("parentId", parentId);
      lst = getNamedParameterJdbcTemplate()
          .query(sql, params,
              BeanPropertyRowMapper.newInstance(ItemDataTT.class));
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return lst;
  }

  @Override
  public List<ItemDataTT> getStatusConfig(TTChangeStatusDTO ttChangeStatusDTO) {
    List<ItemDataTT> lst = new ArrayList<>();
    Map<String, Object> params = new HashMap<>();
    try {
      String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
          "getStatusConfig");
      params.put("p_leeLocale", I18n.getLocale());
      params.put("p_system", "COMMON_GNOC");
      params.put("p_bussiness", "COMMON_GNOC.CAT_ITEM");
      params.put("categoryCode", TT_MASTER_CODE.TT_STATUS);
      if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getTtTypeId())) {
        sqlQuery += " and ttcs.TT_TYPE_ID = :typeId ";
        params.put("typeId", ttChangeStatusDTO.getTtTypeId());
      }
      if (StringUtils.isNotNullOrEmpty(ttChangeStatusDTO.getAlarmGroup())) {
        sqlQuery += " and ttcs.ALARM_GROUP = :alarmGroup ";
        params.put("alarmGroup", ttChangeStatusDTO.getAlarmGroup());
      }
      if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getOldStatus())) {
        sqlQuery += " and ttcs.OLD_STATUS = :oldStatus ";
        params.put("oldStatus", ttChangeStatusDTO.getOldStatus());
      }
      if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getIsDefault())
          && ttChangeStatusDTO.getIsDefault() == 1L) {
        sqlQuery += " and ttcs.IS_DEFAULT = 1 ";
      }
      lst = getNamedParameterJdbcTemplate()
          .query(sqlQuery, params,
              BeanPropertyRowMapper.newInstance(ItemDataTT.class));
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return lst;
  }

  @Override
  public List<TroublesDTO> countTroubleByCable(String lineCutCode, String startTime, String endTime,
      int type) {
    List<TroublesDTO> ret = new ArrayList<>();
    if (StringUtils.isStringNullOrEmpty(lineCutCode) || StringUtils.isStringNullOrEmpty(startTime)
        || StringUtils.isStringNullOrEmpty(endTime)) {
      return ret;
    }
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES,
        "countTroubleByCable");
    Map<String, String> parameters = new HashMap<>();
    sql += " AND lower(LINE_CUT_CODE) like :lineCutCode  escape '\\' ) ";
    parameters.put("startTime", startTime);
    parameters.put("endTime", endTime);
    parameters.put("lineCutCode", StringUtils.convertLowerParamContains(lineCutCode));
    if (type == 1) { //muc do
      sql += " SELECT NVL( COUNT(*),0) troubleId "
          + "  ,a.priority_id priorityId "
          + " FROM tb a "
          + " GROUP BY a.priority_id "
          + " ORDER BY a.priority_id DESC ";
      ret = getNamedParameterJdbcTemplate()
          .query(sql, parameters,
              BeanPropertyRowMapper.newInstance(TroublesDTO.class));
    } else if (type == 2) { //tung ngay + muc do
      sql += " SELECT NVL( COUNT(*),0) troubleId "
          + "  ,a.priority_id priorityId "
          + "  ,to_char(TRUNC(a.CREATED_TIME), 'dd/MM/yyyy') createdTime "
          + " FROM tb a "
          + " GROUP BY TRUNC(a.CREATED_TIME), a.priority_id   "
          + " ORDER BY TRUNC(a.CREATED_TIME), priorityId DESC ";
      ret = getNamedParameterJdbcTemplate()
          .query(sql, parameters,
              BeanPropertyRowMapper.newInstance(TroublesDTO.class));
    } else if (type == 3) {// nguyen nhan
      sql += " SELECT NVL(COUNT(*),0) troubleId "
          + "  ,a.reason_id reasonId "
          + "  ,a.reason_name reasonName "
          + " FROM tb a where a.reason_id is not null "
          + " GROUP BY a.reason_id ,a.reason_name "
          + " ORDER BY troubleId DESC ";
      ret = getNamedParameterJdbcTemplate()
          .query(sql, parameters,
              BeanPropertyRowMapper.newInstance(TroublesDTO.class));
    } else {// tim ALL SU CO
      sql +=
          " SELECT TROUBLE_CODE troubleCode, TROUBLE_NAME troubleName, beginTroubleTime, endTroubleTime, item_name state ,"
              + " RECEIVE_UNIT_NAME receiveUnitName, reason_name reasonName, rootCause, workArround "
              + " FROM tb a ";
      ret = getNamedParameterJdbcTemplate()
          .query(sql, parameters,
              BeanPropertyRowMapper.newInstance(TroublesDTO.class));
    }
    return ret;
  }

  @Override
  public CatItemDTO getItemByCode(String category, String itemCode, String parentId) {
    try {
      Map<String, Object> paramters = new HashMap<>();
      String sql = "select * from COMMON_GNOC.CAT_ITEM where STATUS = 1"
          + " and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE= :category)"
          + " and ITEM_CODE = :itemCode";
      paramters.put("category", category);
      paramters.put("itemCode", itemCode);
      if (parentId == null) {
        sql += " and parent_item_id is null";
      }
      else {
        sql += " and parent_item_id = :parentId";
        paramters.put("parentId", parentId);
      }
      List<CatItemDTO> lstItem = getNamedParameterJdbcTemplate()
          .query(sql, paramters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
      if (lstItem != null && !lstItem.isEmpty()) {
        return lstItem.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }

  @Override
  public CatLocationDTO getLocationByCode(String code) {
    try {
      Map<String, Object> paramters = new HashMap<>();
      String sql = "select * from common_gnoc.cat_location lo where LOCATION_CODE = :locationCode";
      paramters.put("locationCode", code);
      List<CatLocationDTO> lstLocation = getNamedParameterJdbcTemplate()
          .query(sql, paramters, BeanPropertyRowMapper.newInstance(CatLocationDTO.class));
      if (lstLocation != null && !lstLocation.isEmpty()) {
        return lstLocation.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }

  @Override
  public UnitDTO getUnitByCode(String code, Boolean isUnitGnoc) {
    UnitDTO unitDTO = new UnitDTO();
    try {
      Map<String, Object> paramters = new HashMap<>();
      String sql = "select * from COMMON_GNOC.UNIT u where UNIT_CODE = :unitCode";
      paramters.put("unitCode", code);
      if (!isUnitGnoc) {
        String sqlNims = " SELECT UNIT_GNOC_CODE FROM wfm.CFG_MAP_UNIT_GNOC_NIMS WHERE BUSINESS_NAME IS NULL AND UNIT_NIMS_CODE = :unitNims";
        paramters.put("unitNims", code);
        List<CfgMapUnitGnocNimsDTO> lstNims = getNamedParameterJdbcTemplate()
            .query(sqlNims, paramters, BeanPropertyRowMapper.newInstance(CfgMapUnitGnocNimsDTO.class));
        if (lstNims == null || lstNims.isEmpty()) {
          unitDTO.setDescription(I18n.getLanguage("trouble.validate.unitNims.exist"));
          return unitDTO;
        }
        else {
          paramters.put("unitCode", lstNims.get(0).getUnitGnocCode());
        }
      }
      List<UnitDTO> lstUnit = getNamedParameterJdbcTemplate()
          .query(sql, paramters, BeanPropertyRowMapper.newInstance(UnitDTO.class));
      if (lstUnit != null && !lstUnit.isEmpty()) {
        return lstUnit.get(0);
      }
      else {
        unitDTO.setDescription(I18n.getLanguage("trouble.validate.unit.exist"));
        return unitDTO;
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return unitDTO;
  }

  @Override
  public CatItemDTO getPriorityTrouble(TroublesInSideDTO dto) {
    CatItemDTO catItemDTO = new CatItemDTO();
    try {
      Map<String, Object> paramters = new HashMap<>();
      String sql = "SELECT it.ITEM_ID, it.ITEM_CODE, it.ITEM_NAME FROM COMMON_GNOC.CAT_ITEM it WHERE it.CATEGORY_ID = " +
          " (select ca.CATEGORY_ID from  COMMON_GNOC.CATEGORY ca where ca.CATEGORY_CODE = 'TT_PRIORITY') " +
          " and ITEM_CODE = :itemCode";
      paramters.put("itemCode", dto.getPriorityCode());
      List<CatItemDTO> lstCat = getNamedParameterJdbcTemplate()
          .query(sql, paramters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
      if (lstCat == null || lstCat.isEmpty()) {
        catItemDTO.setDescription(I18n.getLanguage("trouble.validate.priorityCode.exist"));
      }
      else {
        catItemDTO = lstCat.get(0);
        String processSql = "SELECT PROCESS_TT_TIME FROM ONE_TM.CFG_TIME_TROUBLE_PROCESS" +
            " WHERE TYPE_ID = :typeId" +
            " AND SUB_CATEGORY_ID = :subId" +
            " AND PRIORITY_ID  = :priId" +
            " AND COUNTRY IN (SELECT item_code FROM COMMON_GNOC.CAT_ITEM WHERE CATEGORY_ID = " +
            " (select ca.CATEGORY_ID from  COMMON_GNOC.CATEGORY ca where ca.CATEGORY_CODE = 'GNOC_COUNTRY') and description = :nationCode)";
        paramters.put("typeId", dto.getTypeId());
        paramters.put("subId", dto.getSubCategoryId());
        paramters.put("priId", catItemDTO.getItemId());
        paramters.put("nationCode", dto.getNationCode());
        List<CfgTimeTroubleProcessDTO> lstTime = getNamedParameterJdbcTemplate()
            .query(processSql, paramters, BeanPropertyRowMapper.newInstance(CfgTimeTroubleProcessDTO.class));
        if (lstTime == null || lstTime.isEmpty()) {
          catItemDTO.setDescription(I18n.getLanguage("trouble.validate.timeProcess.exist"));
        }
      }
      return catItemDTO;
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return catItemDTO;
  }

  @Override
  public List<CatItemDTO> getListUnitApproval() {
    List<CatItemDTO> lst = new ArrayList<>();
    try {
      Map<String, String> params = new HashMap<>();
      String sql = "select u.UNIT_ID, u.UNIT_NAME, u.UNIT_CODE from COMMON_GNOC.UNIT u where u.STATUS = 1 AND u.UNIT_ID" +
          " in (SELECT MANAGE_UNIT FROM OPEN_PM.v_manage_cr_config WHERE IS_SCHEDULE_CR_EMERGENCY = 1)";
      List<UnitDTO> lstUnit = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(UnitDTO.class));
      if (lstUnit != null && !lstUnit.isEmpty()) {
        CatItemDTO catItemDTO = new CatItemDTO();
        for (UnitDTO unit : lstUnit) {
          catItemDTO.setItemId(unit.getUnitId());
          catItemDTO.setItemCode(unit.getUnitCode());
          catItemDTO.setItemName(unit.getUnitName());
          lst.add(catItemDTO);
        }
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return lst;
  }
}
