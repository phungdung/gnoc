package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.ConfigPropertyDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroubleWorklogEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroublesServiceForCCRepositoryImpl extends BaseRepository implements
    TroublesServiceForCCRepository {

  @Override
  public List<TroublesInSideDTO> getTroubleDTOForRollback(List<String> lstTroubleCode,
      String complaintId,
      String fromDate, String toDate) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_SERVICE_FOR_CC,
        "get-Trouble-DTO-For-Rollback");
    Map<String, Object> parameters = new HashMap<>();
    if (lstTroubleCode != null && !lstTroubleCode.isEmpty()) {
      sql += " and a.trouble_code not in (:lstTroubleCode)";
      parameters.put("lstTroubleCode", lstTroubleCode);
    }
    if (StringUtils.isNotNullOrEmpty(complaintId)) {
      sql += " and a.complaint_id = :complaintId";
      parameters.put("complaintId", complaintId);
    }
    if (StringUtils.isNotNullOrEmpty(fromDate)) {
      sql += " and a.created_time >= to_date(:fromDate, 'dd/MM/yyyy HH24:mi:ss')";
      parameters.put("fromDate", fromDate);
    }
    if (StringUtils.isNotNullOrEmpty(toDate)) {
      sql += " and a.created_time <= to_date(:toDate, 'dd/MM/yyyy HH24:mi:ss')";
      parameters.put("toDate", toDate);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(TroublesInSideDTO.class));
  }

  @Override
  public int onSearchCountForCC(TroublesDTO troublesDTO) {
    int result;
    Map<String, Object> parameters = new HashMap<>();
    String sql = createSqlForCC(troublesDTO, parameters);
    sql = "select count(*) totalRow from (" + sql + ")";
    List<BaseDto> baseDto = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(
            BaseDto.class));
//    query.setParameter(parameters);
    if (baseDto != null && baseDto.size() > 0) {
      result = baseDto.get(0).getTotalRow();
    } else {
      result = 0;
    }
    return result;
  }

  @Override
  public List<TroublesDTO> onSearchForCC(TroublesDTO troublesDTO, Integer startRow,
      Integer pageLength) {
    try {
      if (startRow != null && pageLength != null && startRow >= 0 && pageLength > 0) {
        Map<String, Object> parameters = new HashMap<>();
        String sql = createSqlForCC(troublesDTO, parameters);
        Query query = getEntityManager().createNativeQuery(sql);
        query.unwrap(SQLQuery.class).
            addScalar("troubleId", new StringType()).
            addScalar("troubleName", new StringType()).
            addScalar("description", new StringType()).
            addScalar("troubleCode", new StringType()).
            addScalar("typeId", new StringType()).
            addScalar("subCategoryId", new StringType()).
            addScalar("priorityId", new StringType()).
            addScalar("impactId", new StringType()).
            addScalar("state", new StringType()).
            addScalar("affectedNode", new StringType()).
            addScalar("affectedService", new StringType()).
            addScalar("locationId", new StringType()).
            addScalar("location", new StringType()).
            addScalar("createdTime", new StringType()).
            addScalar("lastUpdateTime", new StringType()).
            addScalar("assignTime", new StringType()).
            addScalar("assignTimeTemp", new StringType()).
            addScalar("beginTroubleTime", new StringType()).
            addScalar("endTroubleTime", new StringType()).
            addScalar("deferredTime", new StringType()).
            addScalar("createUserId", new StringType()).
            addScalar("createUserName", new StringType()).
            addScalar("createUnitName", new StringType()).
            addScalar("createUnitId", new StringType()).
            addScalar("insertSource", new StringType()).
            addScalar("vendorId", new StringType()).
            addScalar("relatedPt", new StringType()).
            addScalar("relatedKedb", new StringType()).
            addScalar("receiveUnitId", new StringType()).
            addScalar("receiveUserId", new StringType()).
            addScalar("receiveUnitName", new StringType()).
            addScalar("receiveUserName", new StringType()).
            addScalar("rootCause", new StringType()).
            addScalar("solutionType", new StringType()).
            addScalar("workLog", new StringType()).
            addScalar("workArround", new StringType()).
            addScalar("rejectedCode", new StringType()).
            addScalar("risk", new StringType()).
            addScalar("rejectReason", new StringType()).
            addScalar("supportUnitId", new StringType()).
            addScalar("supportUnitName", new StringType()).
            addScalar("queueTime", new StringType()).
            addScalar("clearTime", new StringType()).
            addScalar("closedTime", new StringType()).
            addScalar("reasonOverdue", new StringType()).
            addScalar("timeZoneCreate", new StringType()).
            addScalar("priorityName", new StringType()).
            addScalar("stateName", new StringType()).
            addScalar("typeName", new StringType()).
            addScalar("timeUsed", new StringType()).
            addScalar("remainTime", new StringType()).
            addScalar("closeCode", new StringType()).
            addScalar("deferredReason", new StringType()).
            addScalar("reasonId", new StringType()).
            addScalar("reasonName", new StringType()).
            addScalar("alarmId", new StringType()).
            addScalar("timeCreateCfg", new StringType()).
            addScalar("timeProcess", new StringType()).
            addScalar("tblCurr", new StringType()).
            addScalar("tblHis", new StringType()).
            addScalar("tblClear", new StringType()).
            addScalar("networkLevel", new StringType()).
            addScalar("autoCreateWO", new LongType()).
            addScalar("lineCutCode", new StringType()).
            addScalar("codeSnippetOff", new StringType()).
            addScalar("cableType", new LongType()).
            addScalar("closuresReplace", new StringType()).
            addScalar("whereWrong", new StringType()).
            addScalar("asessmentData", new LongType()).
            addScalar("alarmGroupId", new StringType()).
            addScalar("complaintGroupId", new StringType()).
            addScalar("serviceType", new LongType()).
            addScalar("reasonLv1Id", new StringType()).
            addScalar("reasonLv1Name", new StringType()).
            addScalar("reasonLv2Id", new StringType()).
            addScalar("reasonLv2Name", new StringType()).
            addScalar("reasonLv3Id", new StringType()).
            addScalar("reasonLv3Name", new StringType()).
            addScalar("reasonOverdueId", new StringType()).
            addScalar("reasonOverdueName", new StringType()).
            addScalar("reasonOverdueId2", new StringType()).
            addScalar("reasonOverdueName2", new StringType()).
            addScalar("spmCode", new StringType()).
            addScalar("closeTtTime", new StringType()).
            addScalar("transNetworkTypeId", new LongType()).
            addScalar("transReasonEffectiveId", new LongType()).
            addScalar("transReasonEffectiveContent", new StringType()).
            addScalar("autoClose", new LongType()).
            addScalar("woCode", new StringType()).
            addScalar("clearUserId", new LongType()).
            addScalar("clearUserName", new StringType()).
            addScalar("alarmGroupName", new StringType()).
            addScalar("relatedTroubleCodes", new StringType()).
            addScalar("reOccur", new StringType()).
            addScalar("isMove", new StringType()).
            addScalar("dateMove", new StringType()).
            addScalar("unitMove", new StringType()).
            addScalar("unitMoveName", new StringType()).
            addScalar("isUpdateAfterClosed", new StringType()).
            addScalar("isStationVip", new StringType()).
            addScalar("isTickHelp", new StringType()).
            addScalar("numHelp", new StringType()).
            addScalar("amiId", new StringType()).
            addScalar("numPending", new StringType()).
            addScalar("numAon", new StringType()).
            addScalar("numGpon", new StringType()).
            addScalar("numNexttv", new StringType()).
            addScalar("numThc", new StringType()).
            addScalar("technology", new StringType()).
            addScalar("infoTicket", new StringType()).
            addScalar("catchingTime", new StringType()).
            addScalar("relatedCr", new StringType()).
            addScalar("nationCode", new StringType()).
            addScalar("isChat", new StringType()).
            addScalar("complaintId", new StringType()).
            addScalar("numReassign", new StringType()).
            addScalar("deferType", new StringType()).
            addScalar("estimateTime", new StringType()).
            addScalar("longitude", new StringType()).
            addScalar("latitude", new StringType()).
            addScalar("groupSolution", new StringType()).
            addScalar("cellService", new StringType()).
            addScalar("concave", new StringType()).
            addScalar("errorCode", new StringType()).
            addScalar("isSendTktu", new StringType()).
            addScalar("troubleAssignId", new StringType()).
            addScalar("createUnitIdByCC", new StringType()).
            addScalar("complaintTypeId", new StringType()).
            addScalar("complaintParentId", new StringType()).
            addScalar("isdn", new StringType()).
            addScalar("createUserByCC", new StringType()).
            addScalar("lstComplaint", new StringType()).
            addScalar("isOverdue", new StringType())
            .setResultTransformer(Transformers.aliasToBean(TroublesDTO.class));
        if (!parameters.isEmpty()) {
          for (Map.Entry<String, Object> map : parameters.entrySet()) {
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
  public List<TroublesDTO> getTroubleInfo(TroublesDTO dto) {
    List<TroublesDTO> ret = new ArrayList<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_SERVICE_FOR_CC,
        "get-Trouble-Info-For-CC");
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {
      if (dto.getTroubleId() != null && !"".equals(dto.getTroubleId())) {
        sql += " and lower(a.trouble_id) = :troubleId";
        parameters.put("troubleId", dto.getTroubleId());
      }
      if (dto.getTroubleCode() != null && !"".equals(dto.getTroubleCode())) {
        sql += " and lower(a.trouble_code) = :troubleCode";
        parameters.put("troubleCode", dto.getTroubleCode().toLowerCase());
      }
      if (dto.getComplaintId() != null && !"".equals(dto.getComplaintId())) {
        sql += " and lower(a.complaint_id) = :complaintId";
        parameters.put("complaintId", dto.getComplaintId());
      }
      if (dto.getCreatedTimeFrom() != null && !"".equals(dto.getCreatedTimeFrom())) {
        sql += " AND a.created_time >= to_date(:createdTimeFrom,'dd/MM/yyyy HH24:MI:ss')";
        parameters.put("createdTimeFrom", dto.getCreatedTimeFrom());
      }
      if (dto.getCreatedTimeTo() != null && !"".equals(dto.getCreatedTimeTo())) {
        sql += " AND a.created_time <= to_date(:createdTimeTo,'dd/MM/yyyy HH24:MI:ss')";
        parameters.put("createdTimeTo", dto.getCreatedTimeTo());
      }
      ret = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(TroublesDTO.class));
    }
    return ret;
  }

  @Override
  public Map<String, String> getConfigProperty() {
    Map<String, String> mapResult = new HashMap<>();
    String sql = "select a.key, a.value from common_gnoc.config_property a";
    List<ConfigPropertyDTO> result = getNamedParameterJdbcTemplate()
        .query(sql, (Map<String, ?>) null,
            BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
    if (result != null && !result.isEmpty()) {
      for (ConfigPropertyDTO cfg : result) {
        mapResult.put(cfg.getKey(), cfg.getValue());
      }
    }
    return mapResult;
  }

  @Override
  public List<TroubleActionLogsDTO> getListTroubleActionLog(String troubleCode) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_SERVICE_FOR_CC,
        "get-List-Trouble-Action-Log");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("troubleCode", troubleCode.trim().toLowerCase());
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(TroubleActionLogsDTO.class));
  }

  @Override
  public List<TroubleWorklogInsiteDTO> searchByConditionBean(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new TroubleWorklogEntity(), lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public List<UnitDTO> getListUnitByTrouble(String troubleCode) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_SERVICE_FOR_CC,
        "get-List-Unit-By-Trouble");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("troubleCode", troubleCode.toLowerCase());
    parameters.put("troubleCode1", troubleCode.toLowerCase());
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UnitDTO.class));
  }

  public String createSqlForCC(TroublesDTO dto, Map<String, Object> parameters) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_SERVICE_FOR_CC,
        "on-Search-For-CC");
    if (dto != null) {
      if (dto.getLstState() != null && !dto.getLstState().isEmpty()) {
        sql += " AND s.item_code in (:lstState)";
        parameters.put("lstState", dto.getLstState());
      }
      if (StringUtils.isNotNullOrEmpty(dto.getComplaintId())) {
        sql += " and a.COMPLAINT_ID = :complaintId ";
        parameters.put("complaintId", dto.getComplaintId());
      }
      if (dto.getUserUnitId() != null) {
        sql += " and a.create_unit_id = :userUnitId ";
        parameters.put("userUnitId", dto.getUserUnitId());
      }
      if (dto.getLocationId() != null && !"-1".equals(dto.getLocationId())) {
        sql += " AND a.location_id in (select location_id from common_gnoc.cat_location start with location_id = :locationId  connect by  prior location_id = parent_id) ";
        CatLocationDTO catLocationDTO = getLocationByCode(dto.getLocationId(), null,
            dto.getNationCode());
        if (catLocationDTO != null) {
          parameters.put("locationId", catLocationDTO.getLocationId());
        } else {
          parameters.put("locationId", null);
        }
      }
      if (dto.getTroubleCode() != null && !"".equals(dto.getTroubleCode())) {
        sql += " and lower(a.trouble_code) like :troubleCode escape '\\'";
        parameters.put("troubleCode", StringUtils.convertLowerParamContains(dto.getTroubleCode()));
      }
      if (dto.getTroubleName() != null && !"".equals(dto.getTroubleName())) {
        sql += " and lower(a.trouble_name) like :troubleName escape '\\'";
        parameters.put("troubleName", StringUtils.convertLowerParamContains(dto.getTroubleName()));
      }
      if (dto.getCreateUnitId() != null && !"".equals(dto.getCreateUnitId())) {
        if (dto.getSearchSubUnitCreate() != null && "1".equals(dto.getSearchSubUnitCreate())) {
          sql += " AND a.create_unit_id IN (SELECT u.unit_id FROM common_gnoc.unit u WHERE LEVEL < 50 START WITH u.unit_id = :createUnitId CONNECT BY PRIOR u.unit_id = u.parent_unit_id)";
        } else {
          sql += " and a.create_unit_id = :createUnitId";
        }
        parameters.put("createUnitId", dto.getCreateUnitId());
      }
      if (dto.getCreatedTimeFrom() != null && !"".equals(dto.getCreatedTimeFrom())) {
        sql += " AND a.created_time >= to_date(:createdTimeFrom,'dd/MM/yyyy HH24:MI:ss')";
        parameters.put("createdTimeFrom", dto.getCreatedTimeFrom());
      }
      if (dto.getCreatedTimeTo() != null && !"".equals(dto.getCreatedTimeTo())) {
        sql += " AND a.created_time <= to_date(:createdTimeTo,'dd/MM/yyyy HH24:MI:ss')";
        parameters.put("createdTimeTo", dto.getCreatedTimeTo());
      }
      if (dto.getReceiveUserId() != null && !"-1".equals(dto.getReceiveUserId())) {
        sql += " and a.receive_user_id = :receiveUserId";
        parameters.put("receiveUserId", dto.getReceiveUserId());
      }
      if (dto.getCreateUserId() != null && !"-1".equals(dto.getCreateUserId())) {
        sql += " and a.create_user_id = :createUserId";
        parameters.put("createUserId", dto.getCreateUserId());
      }
      if (StringUtils.isNotNullOrEmpty(dto.getTroubleId())) {
        sql += " and a.trouble_id = :troubleId";
        parameters.put("troubleId", dto.getTroubleId());
      }
      if (StringUtils.isNotNullOrEmpty(dto.getErrorCode())) {
        sql += " and a.error_code = :errorCode";
        parameters.put("errorCode", dto.getErrorCode());
      }
      if (StringUtils.isNotNullOrEmpty(dto.getIsOverdue()) && "1"
          .equals(dto.getIsOverdue())) {
        sql +=
            " and (s.item_code in('WAITING RECEIVE','QUEUE','SOLUTION FOUND','CLEAR','CLOSED','CLOSED NOT KEDB','WAIT FOR DEFERRED') "
                + " and nvl(a.time_process,0) < (nvl(a.time_used,0) + round((nvl(a.clear_time,sysdate)-nvl(a.assign_time_temp,a.assign_time))*24,2)) "
                + " or (s.item_code in ('DEFERRED') and nvl(a.time_process,0) < nvl(a.time_used,0)) "
                + " ) ";
      } else if (StringUtils.isNotNullOrEmpty(dto.getIsOverdue()) && "0"
          .equals(dto.getIsOverdue())) {
        sql +=
            " and (s.item_code in('WAITING RECEIVE','QUEUE','SOLUTION FOUND','CLEAR','CLOSED','CLOSED NOT KEDB','WAIT FOR DEFERRED') "
                + " and nvl(a.time_process,0) >= (nvl(a.time_used,0) + round((nvl(a.clear_time,sysdate)-nvl(a.assign_time_temp,a.assign_time))*24,2)) "
                + " or (s.item_code in ('DEFERRED') and nvl(a.time_process,0) >= nvl(a.time_used,0)) "
                + ") ";
      }
      sql += " AND a.INSERT_SOURCE like 'BCCS%' ";
      if (dto.getLstCreateUnitIdByCC() != null && !dto.getLstCreateUnitIdByCC().isEmpty()) {
        sql += " AND a.CREATE_UNIT_ID_BY_CC in (:lstCreateUnitIdByCC)";
        parameters.put("lstCreateUnitIdByCC", dto.getLstCreateUnitIdByCC());
      }
      if (dto.getLstComplaintTypeId() != null && !dto.getLstComplaintTypeId().isEmpty()) {
        sql += " AND a.COMPLAINT_TYPE_ID in (:lstComplaintTypeId)";
        parameters.put("lstComplaintTypeId", dto.getLstComplaintTypeId());
      }
      if (dto.getLstComplaintParentId() != null && !dto.getLstComplaintParentId().isEmpty()) {
        sql += " AND a.COMPLAINT_PARENT_ID in (:lstComplaintParentId)";
        parameters.put("lstComplaintParentId", dto.getLstComplaintParentId());
      }
      if (dto.getLstTroubleCode() != null && !dto.getLstTroubleCode().isEmpty()) {
        sql += " AND a.trouble_code in (:lstTroubleCode)";
        parameters.put("lstTroubleCode", dto.getLstTroubleCode());
      }
      sql += " order by a.created_time desc";
    }
    return sql;
  }

  public CatLocationDTO getLocationByCode(String locationCode, String locationId,
      String nationCode) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_SERVICE_FOR_CC,
        "get-Location-By-Code");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(locationCode)) {
      sql += " and a.location_code = :locationCode";
      parameters.put("locationCode", locationCode.trim());
    }
    if (StringUtils.isNotNullOrEmpty(locationId)) {
      sql += " and a.location_id = :locationId";
      parameters.put("locationId", locationId.trim());
    }
    if (StringUtils.isNotNullOrEmpty(nationCode)) {
      sql += " and a.NATION_CODE = :nationCode";
      parameters.put("nationCode", nationCode);
    } else {
      sql += " and (a.NATION_CODE = 'VNM' or a.NATION_CODE is null)";
    }
    sql += " START WITH a.parent_id IS NULL CONNECT BY PRIOR a.location_id = a.parent_id";
    List<CatLocationDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CatLocationDTO.class));
    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }
    return null;
  }

}
