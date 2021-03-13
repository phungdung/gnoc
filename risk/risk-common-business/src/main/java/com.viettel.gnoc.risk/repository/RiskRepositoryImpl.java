package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.risk.dto.RiskDTO;
import com.viettel.gnoc.risk.dto.RiskDTOSearch;
import com.viettel.gnoc.risk.model.RiskEntity;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskRepositoryImpl extends BaseRepository implements RiskRepository {

  @Autowired
  CommonRepository commonRepository;

  @Autowired
  UserRepository userRepository;

  @Override
  public Datatable getListDataSearchWeb(RiskDTO riskDTO) {
    BaseDto baseDto = sqlGetListDataSearchWeb(riskDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        riskDTO.getPage(), riskDTO.getPageSize(), RiskDTO.class, riskDTO.getSortName(),
        riskDTO.getSortType());
    return datatable;
  }

  @Override
  public String getSeqTableWo(String seq) {
    return getSeqTableBase(seq);
  }

  @Override
  public ResultInSideDto insertRisk(RiskDTO riskDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    RiskEntity riskEntity = getEntityManager().merge(riskDTO.toEntity());
    resultInSideDto.setId(riskEntity.getRiskId());
    return resultInSideDto;
  }

  @Override
  public RiskDTO findRiskByIdFromWeb(Long riskId, Double offsetFromUser) {
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_RISK, "find-Risk-By-Id-From-Web");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemRisk", Constants.LANGUAGUE_EXCHANGE_SYSTEM.RISK);
    parameters.put("bussinessRisk", Constants.APPLIED_BUSSINESS.RISK_TYPE);
    parameters.put("leeLocale", leeLocale);
    parameters.put("offset", offsetFromUser);
    parameters.put("statusClose", Constants.OD_STATUS.CLOSE);

    sql += " AND r.risk_id = :riskId";
    parameters.put("riskId", riskId);
    List<RiskDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(RiskDTO.class));
    if (list != null && !list.isEmpty()) {
      RiskDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  private BaseDto sqlGetListDataSearchWeb(RiskDTO riskDTO) {
    BaseDto baseDto = new BaseDto();
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_RISK, "get-List-Data-Search-Web");
    Map<String, Object> parameters = new HashMap<>();
    List<Long> lstStatus = new ArrayList<>();
    parameters.put("systemRisk", Constants.LANGUAGUE_EXCHANGE_SYSTEM.RISK);
    parameters.put("bussinessRisk", Constants.APPLIED_BUSSINESS.RISK_TYPE);
    parameters.put("leeLocale", leeLocale);
    parameters.put("offset", riskDTO.getOffSetFromUser());
    parameters.put("statusClose", Constants.OD_STATUS.CLOSE);

    //Tim kiem nhanh
    if (StringUtils.isNotNullOrEmpty(riskDTO.getSearchAll())) {
      sql += " AND (LOWER(r.risk_code) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(r.risk_name) LIKE :searchAll ESCAPE '\\')";
      parameters.put("searchAll", StringUtils.convertLowerParamContains(riskDTO.getSearchAll()));
    }

    // Ma cong viec
    if (StringUtils.isNotNullOrEmpty(riskDTO.getRiskCode())) {
      sql += " AND LOWER(r.risk_code) LIKE :riskCode ESCAPE '\\'";
      parameters.put("riskCode", StringUtils.convertLowerParamContains(riskDTO.getRiskCode()));
    }

    //Noi dung cong viec
    if (StringUtils.isNotNullOrEmpty(riskDTO.getRiskName())) {
      sql += " AND LOWER(r.risk_name) LIKE :riskName ESCAPE '\\'";
      parameters.put("riskName", StringUtils.convertLowerParamContains(riskDTO.getRiskName()));
    }

    // Thoi gian khoi tao
    if (riskDTO.getCreateTimeFrom() != null) {
      sql += " AND r.create_time >= TO_TIMESTAMP(:createTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("createTimeFrom", DateUtil.date2ddMMyyyyHHMMss(riskDTO.getCreateTimeFrom()));
    }
    if (riskDTO.getCreateTimeTo() != null) {
      sql += " AND r.create_time <= TO_TIMESTAMP(:createTimeTo,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("createTimeTo", DateUtil.date2ddMMyyyyHHMMss(riskDTO.getCreateTimeTo()));
    }

    // Nhan vien thuc hien
    if (riskDTO.getReceiveUserId() != null) {
      sql += " AND r.receive_user_id = :receiveUserId";
      parameters.put("receiveUserId", riskDTO.getReceiveUserId());
    }

    // Nhan vien tao
    if (riskDTO.getCreateUserId() != null) {
      sql += " AND r.create_user_Id = :createUserId";
      parameters.put("createUserId", riskDTO.getCreateUserId());
    }

    // he thong
    if (StringUtils.isNotNullOrEmpty(riskDTO.getInsertSource())) {
      sql += " AND r.insert_source = :insertSource";
      parameters.put("insertSource", riskDTO.getInsertSource());
    }

    // nhom loai cong viec
    if (riskDTO.getRiskGroupTypeId() != null) {
      sql += " AND r.risk_type_id in (select risk_type_id from rish_type where risk_group_type_id = :riskGroupTypeId)";
      parameters.put("riskGroupTypeId", riskDTO.getRiskGroupTypeId());
    }

    // he thong danh gia rui ro
    if (riskDTO.getSystemId() != null) {
      sql += " AND r.system_id = :systemId";
      parameters.put("systemId", riskDTO.getSystemId());
    }

    //thi truong
    if (riskDTO.getCountrySystem() != null) {
      sql += " AND sys.country_id = :countrySystem";
      parameters.put("countrySystem", riskDTO.getCountrySystem());
    }

    // loai rui ro
    if (riskDTO.getRiskTypeId() != null) {
      sql += " AND r.risk_type_id = :riskTypeId";
      parameters.put("riskTypeId", riskDTO.getRiskTypeId());
    }

    // muc do uu tien
    if (riskDTO.getPriorityId() != null) {
      sql += " AND r.priority_id = :priorityId";
      parameters.put("priorityId", riskDTO.getPriorityId());
    }

    // Thoi gian bat dau
    if (riskDTO.getStartTimeFrom() != null) {
      sql += " AND r.start_time >= TO_TIMESTAMP(:startTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("startTimeFrom", DateUtil.date2ddMMyyyyHHMMss(riskDTO.getStartTimeFrom()));
    }
    if (riskDTO.getStartTimeTo() != null) {
      sql += " AND r.start_time <= TO_TIMESTAMP(:startTimeTo,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("startTimeTo", DateUtil.date2ddMMyyyyHHMMss(riskDTO.getStartTimeTo()));
    }

    // Thoi gian ket thuc
    if (riskDTO.getEndTimeFrom() != null) {
      sql += " AND r.end_time >= TO_TIMESTAMP(:endTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("endTimeFrom", DateUtil.date2ddMMyyyyHHMMss(riskDTO.getEndTimeFrom()));
    }
    if (riskDTO.getEndTimeTo() != null) {
      sql += " AND r.end_time <= TO_TIMESTAMP(:endTimeTo,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("endTimeTo", DateUtil.date2ddMMyyyyHHMMss(riskDTO.getEndTimeTo()));
    }

    // don vi tao
    if (riskDTO.getCreateUnitId() != null) {
      if ("1".equals(riskDTO.getChildCreateUnit())) {
        sql += " AND r.create_user_id in ("
            + " select user_id from common_gnoc.users where unit_id in"
            + " (select unit_id from common_gnoc.unit where level < 50"
            + " start with unit_id = :createUnitId"
            + " connect by prior unit_id = parent_unit_id))";
      } else {
//        sql += " AND r.create_unit_id in (select user_id from common_gnoc.users where unit_id = :createUnitId)";
        sql += " AND r.create_unit_id = :createUnitId";
      }
      parameters.put("createUnitId", riskDTO.getCreateUnitId());
    }

    // don vi xu ly
    if (riskDTO.getReceiveUnitId() != null) {
      if ("1".equals(riskDTO.getChildReceiveUnit())) {
        sql += " AND r.receive_unit_id in ("
            + " select unit_id from common_gnoc.unit where level < 50"
            + " start with unit_id = :receiveUnitId"
            + " connect by prior unit_id = parent_unit_id)";
      } else {
        sql += " AND r.receive_unit_id = :receiveUnitId";
      }
      parameters.put("receiveUnitId", riskDTO.getReceiveUnitId());
    }

    if (riskDTO.getUserId() != null) {
      sql += " and (";
      Boolean isHasCondition = false;
      if (riskDTO.getIsCreated() != null && riskDTO.getIsCreated()) {
        sql += isHasCondition ? " or r.create_user_id = :userId" : " r.create_user_id = :userId";
        isHasCondition = true;
      }
      if (riskDTO.getIsReceiveUser() != null && riskDTO.getIsReceiveUser()) {
        sql += isHasCondition ? " or r.receive_user_id = :userId" : " r.receive_user_id = :userId";
        isHasCondition = true;
      }
      if (riskDTO.getIsReceiveUnit() != null && riskDTO.getIsReceiveUnit()) {
        sql += isHasCondition ? " or (\n"
            + " case when r.STATUS = 1 then\n"
            + "  case when r.CREATE_UNIT_ID IN ("
            + " select unit_id from common_gnoc.unit where level < 50"
            + " start with unit_id = (select unit_id from common_gnoc.users where user_id = :userId)"
            + " connect by prior unit_id = parent_unit_id)"
            + " then 1 else 0 end\n"
            + " else\n"
            + "  case when r.RECEIVE_UNIT_ID IN ("
            + " select unit_id from common_gnoc.unit where level < 50"
            + " start with unit_id = (select unit_id from common_gnoc.users where user_id = :userId)"
            + " connect by prior unit_id = parent_unit_id)"
            + " then 1 else 0 end\n"
            + " end\n"
            + " ) = 1" : "(\n"
            + " case when r.STATUS = 1 then\n"
            + "  case when r.CREATE_UNIT_ID IN ("
            + " select unit_id from common_gnoc.unit where level < 50"
            + " start with unit_id = (select unit_id from common_gnoc.users where user_id = :userId)"
            + " connect by prior unit_id = parent_unit_id) then 1 else 0 end\n"
            + " else\n"
            + "  case when r.RECEIVE_UNIT_ID IN ("
            + " select unit_id from common_gnoc.unit where level < 50"
            + " start with unit_id = (select unit_id from common_gnoc.users where user_id = :userId)"
            + " connect by prior unit_id = parent_unit_id) then 1 else 0 end\n"
            + " end\n"
            + " ) = 1";
        isHasCondition = true;
      }
      if (!isHasCondition) {
        sql += " 1 = 1";
      }
      sql += " )";
      parameters.put("userId", riskDTO.getUserId());
    }

    String statusClose = getValueFromValueKey(Constants.AP_PARAM.RISK_STATUS_PROPERTY,
        Constants.RISK_STATUS.CLOSE);
    if (StringUtils.isNotNullOrEmpty(riskDTO.getStatusSearchWeb())) {
      if ("-1".equals(riskDTO.getStatusSearchWeb())) {
        sql += " AND r.status <> :statusClose";
        parameters.put("statusClose", statusClose);
      } else if (riskDTO.getStatusSearchWeb().contains(",")) {
        sql += " AND r.status in (:lstStatus)";
        List<String> myList = new ArrayList<>(
            Arrays.asList(riskDTO.getStatusSearchWeb().split(",")));
        for (String tmp : myList) {
          lstStatus.add(Long.valueOf(tmp.trim()));
        }
        if (lstStatus != null && !lstStatus.isEmpty()) {
          parameters.put("lstStatus", lstStatus);
        }
      } else {
        sql += " AND r.status = :statusSearchWeb";
        parameters.put("statusSearchWeb", riskDTO.getStatusSearchWeb());
      }
    }
    //Dunglv add search by OtherCode
    if(StringUtils.isNotNullOrEmpty(riskDTO.getOtherSystemCode())){
      sql += " AND r.other_system_code = :otherSystemCode";
      parameters.put("otherSystemCode", riskDTO.getOtherSystemCode());
    }

    sql += " ORDER BY r.create_time DESC";

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public String getValueFromValueKey(String configKey, String valueKey) {
    ConfigPropertyDTO cfg = commonRepository.getConfigPropertyObj(configKey);
    if (cfg != null && cfg.getValue() != null) {
      String[] value = cfg.getValue().split(",");
      String[] des = cfg.getDescription().split(",");
      for (int i = 0; i < value.length; i++) {
        // risk so sanh key = value va return des
        if (valueKey.equalsIgnoreCase(value[i])) {
          return des[i];
        }
      }
    }
    return null;
  }

  @Override
  public ResultInSideDto updateRisk(RiskDTO riskDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(riskDTO.toEntity());
    resultInSideDto.setId(riskDTO.getRiskId());
    return resultInSideDto;
  }

  @Override
  public RiskEntity findRiskByRiskId(Long riskId) {
    return getEntityManager().find(RiskEntity.class, riskId);
  }

  @Override
  public List<RiskDTO> getListRiskExport(RiskDTO riskDTO) {
    BaseDto baseDto = sqlGetListDataSearchWeb(riskDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(RiskDTO.class));
  }

  @Override
  public List<RiskDTOSearch> getListDataSearchForOther(RiskDTOSearch searchDtoInput, int start,
      int count) {
    BaseDto baseDto = buildQueryListDataSearchForOther(searchDtoInput, false);
    int page = 1;
    int pageSize = 0;
    if (count > 0) {
      page = (int) Math.ceil((start + 1) * 1.0 / count);
      pageSize = count;
    }

    Datatable data = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        page, pageSize, RiskDTOSearch.class, "", "");
    if (data != null) {
      return (List<RiskDTOSearch>) data.getData();
    }
    return new ArrayList<>();
  }

  @Override
  public CatItemDTO getDoNextAction(String itemId) {
    try {
      String sql = "SELECT ITEM_VALUE FROM COMMON_GNOC.CAT_ITEM WHERE ITEM_ID = :itemId";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("itemId", itemId);
      List<CatItemDTO> lstActionNext = getNamedParameterJdbcTemplate().query(sql, parameters,
          BeanPropertyRowMapper.newInstance(CatItemDTO.class));
      if (lstActionNext != null && !lstActionNext.isEmpty()) {
        return lstActionNext.get(0);
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }

  public BaseDto buildQueryListDataSearchForOther(RiskDTOSearch dto, Boolean isCount) {
    UsersEntity us = userRepository.getUserByUserName(dto.getUserName());
    if (us != null) {
      dto.setUserId(us.getUserId().toString());
    }

    Double offset = userRepository.getOffsetFromUser(Long.parseLong(dto.getUserId()));
    RiskDTOSearch searchDto = converTimeFromClientToServerWeb(dto, offset);
    StringBuilder sql = new StringBuilder();
    List<Long> lstStatus = new ArrayList<>();
    Map<String, Object> params = new HashMap<>();
    if (isCount) {
      sql.append(" select count(o.risk_id) count_risk_id ");
    } else {
      sql.append(" select o.risk_id riskId,o.risk_code riskCode,o.risk_name riskName");
      sql.append(",o.effect effect");
      sql.append(",o.effect_detail effectDetail");
      sql.append(",o.system_id systemId");
      sql.append(",o.is_external_vtnet isExternalVtnet");
      sql.append(",o.result_processing resultProcessing");
      sql.append(",o.frequency_detail frequencyDetail");
      sql.append(",o.frequency frequency");
      sql.append(",o.reason_accept reasonAccept");
      sql.append(",o.reason_reject reasonReject");
      sql.append(",o.reason_cancel reasonCancel");
      sql.append(",o.suggest_solution suggestSolution");
      sql.append(",o.closed_date closedDate");
      sql.append(",o.opened_date openedDate");
      sql.append(",o.accepted_date acceptedDate");
      sql.append(",o.canceled_date canceledDate");
      sql.append(",o.received_date receivedDate");
      sql.append(",o.rejected_date rejectedDate");
      sql.append(",o.log_time logTime");
      sql.append(",sys.system_name systemName");
      sql.append(",o.subject_id subjectId");
      sql.append(",o.solution solution");
      sql.append(",o.redundancy redundancy");
      sql.append(",o.evedence evedence");
      sql.append(",o.result result");

      sql.append(", unc.unit_name createUnitName");
      sql.append(", unc.unit_id createUnitId");
      sql.append(" ,o.create_time + :offset * interval '1' hour createTime,o.description "
          + " ,o.last_update_time + :offset * interval '1' hour lastUpdateTime");
      sql.append(" ,o.risk_type_id riskTypeId,t.risk_type_name riskTypeName");
      sql.append(" ,o.priority_id priorityId, o.start_time + :offset * interval '1' hour startTime"
          + " ,o.end_time + :offset * interval '1' hour endTime");
      sql.append(
          " ,o.receive_user_id receiveUserId, usr.username receiveUserName, o.receive_unit_id receiveUnitId");
      sql.append(
          " , unr.unit_name receiveUnitName, unr.unit_code receiveUnitCode,o.other_system_code otherSystemCode");
      sql.append(" ,o.create_user_Id createUserId,o.insert_source insertSource,o.status status,");
      sql.append(" (case when o.status = " + Constants.OD_STATUS.CLOSE);
      sql.append("        then round((cast(o.end_time as date)-cast(o.close_time as date))*24,2) ");
      sql.append("        else round((cast(o.end_time as date)-sysdate)*24,2) ");
      sql.append("    end) remainTime");
      sql.append(
          " , usc.username createUserName, o.close_time + :offset * interval '1' hour closeTime ");
      params.put("offset", offset == null ? 0 : offset);
    }
    sql.append(
        " from wfm.risk o, wfm.risk_type t,wfm.risk_system sys, common_gnoc.users usr, common_gnoc.unit unr"
            + " , common_gnoc.users usc, common_gnoc.unit unc");
    sql.append(" where 1 = 1 ");
    sql.append(" and o.risk_type_id = t.risk_type_id(+)");
    sql.append(" and o.system_id = sys.id(+)");
    sql.append(" and o.receive_user_id = usr.user_id(+)");
    sql.append(" and o.create_user_Id = usc.user_id(+)");
    sql.append(" and o.receive_unit_id = unr.unit_id(+) ");
    sql.append(" and o.create_unit_id = unc.unit_id(+) ");

    // <editor-fold defaultstate="collapsed" desc="Add dieu kien">
    // Ma cong viec
    if (!StringUtils.isStringNullOrEmpty(searchDto.getRiskCode())) {
      sql.append(" AND LOWER(o.risk_code) LIKE :riskCode escape '\\' ");
      params.put("riskCode", StringUtils.convertLowerParamContains(searchDto.getRiskCode()));
    }

    //Noi dung cong viec
    if (!StringUtils.isStringNullOrEmpty(searchDto.getRiskName())) {
      sql.append(" AND LOWER(o.risk_name) LIKE :riskName escape '\\' ");
      params.put("riskName", StringUtils.convertLowerParamContains(searchDto.getRiskName()));
    }

    // Thoi gian khoi tao
    if (!StringUtils.isStringNullOrEmpty(searchDto.getCreateTimeFrom())) {
      sql.append(" AND o.create_time >= TO_TIMESTAMP(:createTimeFrom,'dd/mm/yyyy hh24:mi:ss') ");
      params.put("createTimeFrom", searchDto.getCreateTimeFrom());
    }

    if (!StringUtils.isStringNullOrEmpty(searchDto.getCreateTimeTo())) {
      sql.append(" AND o.create_time <= TO_TIMESTAMP(:createTimeTo,'dd/mm/yyyy hh24:mi:ss') ");
      params.put("createTimeTo", searchDto.getCreateTimeTo());
    }

    // Nhan vien thuc hien
    if (!StringUtils.isStringNullOrEmpty(searchDto.getReceiveUserId())) {
      sql.append(" AND o.receive_user_id = :receiveUserId ");
      params.put("receiveUserId", searchDto.getReceiveUserId());
    }
    // Nhan vien tao
    if (!StringUtils.isStringNullOrEmpty(searchDto.getCreateUserId())) {
      sql.append(" AND o.create_user_Id =  :createUserId ");
      params.put("createUserId", searchDto.getCreateUserId());
    }

    // he thong
    if (!StringUtils.isStringNullOrEmpty(searchDto.getInsertSource())) {
      sql.append(" AND o.insert_source = :insertSource ");
      params.put("insertSource", searchDto.getInsertSource());
    }

    // nhom loai cong viec
    if (!StringUtils.isStringNullOrEmpty(searchDto.getRiskGroupTypeId())) {
      sql.append(
          " AND o.risk_type_id in (select risk_type_id from od_type where risk_group_type_id = :riskGroupTypeId) ");
      params.put("riskGroupTypeId", searchDto.getRiskGroupTypeId());
    }

    // loai rui ro
    if (!StringUtils.isStringNullOrEmpty(searchDto.getRiskTypeId())) {
      sql.append(" AND o.risk_type_id = :riskTypeId ");
      params.put("riskTypeId", searchDto.getRiskTypeId());
    }
    // muc do uu tien
    if (!StringUtils.isStringNullOrEmpty(searchDto.getPriorityId())) {
      sql.append(" AND o.priority_id = :priorityId ");
      params.put("priorityId", searchDto.getPriorityId());
    }
    // ma clear
    if (!StringUtils.isStringNullOrEmpty(searchDto.getClearCodeId())) {
      sql.append(" AND o.clear_code_id = :clearCodeId ");
      params.put("clearCodeId", searchDto.getClearCodeId());
    }
    // ma close
    if (!StringUtils.isStringNullOrEmpty(searchDto.getCloseCodeId())) {
      sql.append(" AND o.close_code_id = :closeCodeId ");
      params.put("closeCodeId", searchDto.getCloseCodeId());
    }
    // nhan vien thuc hien
    if (!StringUtils.isStringNullOrEmpty(searchDto.getReceiveUserId())) {
      sql.append(" AND o.receive_user_id = :receiveUserId");
      params.put("receiveUserId", searchDto.getReceiveUserId());
    }
    // Thoi gian bat dau
    if (!StringUtils.isStringNullOrEmpty(searchDto.getStartTimeFrom())) {
      sql.append(" AND o.start_time >= TO_TIMESTAMP(:startTimeFrom,'dd/mm/yyyy hh24:mi:ss') ");
      params.put("startTimeFrom", searchDto.getStartTimeFrom());
    }

    if (!StringUtils.isStringNullOrEmpty(searchDto.getStartTimeTo())) {
      sql.append(" AND o.start_time <= TO_TIMESTAMP(:startTimeTo,'dd/mm/yyyy hh24:mi:ss') ");
      params.put("startTimeTo", searchDto.getStartTimeTo());
    }
    // Thoi gian ket thuc
    if (!StringUtils.isStringNullOrEmpty(searchDto.getEndTimeFrom())) {
      sql.append(" AND o.end_time >= TO_TIMESTAMP(:endTimeFrom,'dd/mm/yyyy hh24:mi:ss') ");
      params.put("endTimeFrom", searchDto.getEndTimeFrom());
    }

    if (!StringUtils.isStringNullOrEmpty(searchDto.getEndTimeTo())) {
      sql.append(" AND o.end_time <= TO_TIMESTAMP(:endTimeTo ,'dd/mm/yyyy hh24:mi:ss') ");
      params.put("endTimeTo", searchDto.getEndTimeTo());
    }
    // don vi tao
    if (!StringUtils.isStringNullOrEmpty(searchDto.getCreateUnitId())) {
      if ("1".equals(searchDto.getChildCreateUnit())) {
        sql.append(" AND o.create_user_id in ( "
            + " select user_id from common_gnoc.users where unit_id in "
            + " (select unit_id from common_gnoc.unit where level < 50 "
            + " start with unit_id = :createUnitId "
            + " connect by prior unit_id = parent_unit_id)) "
        );
      } else {
        sql.append(
            " AND o.create_unit_id in (select user_id from common_gnoc.users where unit_id = :createUnitId)  ");
      }
      params.put("createUnitId", searchDto.getCreateUnitId());
    }
    // don vi xu ly
    if (!StringUtils.isStringNullOrEmpty(searchDto.getReceiveUnitId())) {
      if ("1".equals(searchDto.getChildReceiveUnit())) {
        sql.append(" AND o.receive_unit_id in ( "
            + " select unit_id from common_gnoc.unit where level < 50 "
            + " start with unit_id = :receiveUnitId "
            + " connect by prior unit_id = parent_unit_id) "
        );
      } else {
        sql.append(" AND o.receive_unit_id = :receiveUnitId  ");
      }
      params.put("receiveUnitId", searchDto.getReceiveUnitId());
    }

    if (!StringUtils.isStringNullOrEmpty(searchDto.getUserId())) {
      sql.append(" and ( ");
      Boolean isHasCondition = false;
      if (searchDto.getIsCreated() != null && searchDto.getIsCreated()) {
        sql.append(
            isHasCondition ? " or o.create_user_id = :userId " : " o.create_user_id = :userId ");
        isHasCondition = true;
        params.put("userId", searchDto.getUserId());
      }
      if (searchDto.getIsReceiveUser() != null && searchDto.getIsReceiveUser()) {
        sql.append(isHasCondition ? " or o.receive_user_id = :userId  "
            : "  o.receive_user_id = :userId  ");
        isHasCondition = true;
        params.put("userId", searchDto.getUserId());
      }
//            if (searchDto.getIsReceiveUnit() != null && searchDto.getIsReceiveUnit()) {
//                sql.append(isHasCondition ? " or o.receive_unit_id = (select unit_id from common_gnoc.users where user_id =?)  "
//                        : "  o.receive_unit_id = (select unit_id from common_gnoc.users where user_id =?)  ");
//                isHasCondition = true;
//                lstParams.add(Long.valueOf(searchDto.getUserId()));
//            }

      if (searchDto.getIsReceiveUnit() != null && searchDto.getIsReceiveUnit()) {
        sql.append(isHasCondition ? "or (\n"
            + "case when o.STATUS = 1 then \n"
            + "  case when o.CREATE_UNIT_ID IN (select user_id from common_gnoc.users where unit_id in"
            + "  start with unit_id =  (select unit_id from common_gnoc.users where user_id = :userId)  connect by prior unit_id = parent_unit_id) "
            + " then 1 else 0 end\n"
            + "else \n"
            + "  case when o.RECEIVE_UNIT_ID IN (select unit_id from common_gnoc.users where user_id = :userId) (select user_id from common_gnoc.users where unit_id in"
            + "  start with unit_id =  (select unit_id from common_gnoc.users where user_id = ?)  connect by prior unit_id = parent_unit_id) "
            + " then 1 else 0 end \n"
            + "end \n"
            + ") = 1" : "(\n"
            + "case when o.STATUS = 1 then \n"
            + "  case when o.CREATE_UNIT_ID = (select unit_id from common_gnoc.users where user_id = :userId) then 1 else 0 end\n"
            + "else \n"
            + "  case when o.RECEIVE_UNIT_ID = (select unit_id from common_gnoc.users where user_id = :userId)  then 1 else 0 end \n"
            + "end \n"
            + ") = 1");
        isHasCondition = true;
        params.put("userId", searchDto.getUserId());
      }
      if (!isHasCondition) {
        sql.append("1=1");
      }
      sql.append(" ) ");
    }

    if (!StringUtils.isStringNullOrEmpty(searchDto.getStatus())) {
      if ("-1".equals(searchDto.getStatus())) {//Chua hoan thanh - Cho CD phe duyet
//                sql.append(" AND w.status <> 7 and w.status <> 8 and ( w.status <> 6 or (w.status = 6 AND w.result is null))");
      }//ducdm1_them trang thai hoan thanh_start
      else if (searchDto.getStatus().contains(",")) {
        String status = null;
        if (searchDto.getStatus().contains("7")) {
          status = searchDto.getStatus().replaceAll("7,", "");
        } else {
          status = searchDto.getStatus();
        }
        sql.append(" AND o.status in (:lstStatus)");
        List<String> myList = new ArrayList<>(Arrays.asList(status.split(",")));
        for (String tmp : myList) {
          lstStatus.add(Long.parseLong(tmp.trim()));
        }
        if (lstStatus != null && !lstStatus.isEmpty()) {
          params.put("lstStatus", lstStatus);
        }
      } else {
        sql.append(" AND o.status = :statusSearch  ");
        params.put("statusSearch", searchDto.getStatus());
      }
    }

    // </editor-fold>
    if (!isCount) {
      sql.append(" ORDER BY o.create_time DESC ");
    }
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql.toString());
    baseDto.setParameters(params);
    return baseDto;
  }

  public RiskDTOSearch converTimeFromClientToServerWeb(RiskDTOSearch woDto, Double offset) {
    try {
      woDto.setStartTime(DateTimeUtils.converClientDateToServerDate(woDto.getStartTime(), offset));
      woDto.setStartTimeFrom(
          DateTimeUtils.converClientDateToServerDate(woDto.getStartTimeFrom(), offset));
      woDto.setStartTimeTo(
          DateTimeUtils.converClientDateToServerDate(woDto.getStartTimeTo(), offset));
      woDto.setEndTime(DateTimeUtils.converClientDateToServerDate(woDto.getEndTime(), offset));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return woDto;
  }

  @Override
  public String getValueFromDesKey(String configKey, String valueKey) {
    try {
      ConfigPropertyDTO cfg = commonRepository.getConfigPropertyObj(configKey);
      if (cfg != null && cfg.getValue() != null) {
        String[] value = cfg.getValue().split(",");
        String[] des = cfg.getDescription().split(",");
        for (int i = 0; i < value.length; i++) {
          if (valueKey.equalsIgnoreCase(des[i])) {
            return value[i];
          }
        }
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }

  @Override
  public File onDownloadMultipleFile(RiskDTO riskDTO) {
    try {
      Map<String, Object> parameters = new HashMap<>();
      List<Long> idDownloadList = riskDTO.getIdDownloadList();
      List<String> listFile = new ArrayList<>();
      for (Long fileId : idDownloadList) {
        String sql = "select f.path from common_gnoc.gnoc_file f where id = :fileId";
        parameters.put("fileId", fileId);
        List<GnocFileDto> list = getNamedParameterJdbcTemplate().query(sql, parameters,
            BeanPropertyRowMapper.newInstance(GnocFileDto.class));
        if (list != null && !list.isEmpty()) {
          String path = list.get(0).getPath();
          listFile.add(path);
        }
      }
      String zipFile = "";
      String sqlItem = "select * from common_gnoc.cat_item where ITEM_CODE = :itemCode";
      parameters.put("itemCode", riskDTO.getItemCode());
      List<CatItemDTO> catItem = getNamedParameterJdbcTemplate().query(sqlItem, parameters,
          BeanPropertyRowMapper.newInstance(CatItemDTO.class));
      LocalDate localDate = LocalDate.now();
      if (catItem != null && !catItem.isEmpty()) {
        zipFile = catItem.get(0).getItemValue().trim() + localDate.getYear() + "/" + localDate
            .getMonthValue() + "/" +
            localDate.getDayOfMonth() + "/" + riskDTO.getRiskCode() + ".zip";
      }
      listFile = filterList(listFile);
      if (listFile != null && listFile.size() > 0) {
        zipFile(zipFile, listFile);
      }
      File downloadFileFinal = new File(zipFile);
      return downloadFileFinal;
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }

  public List<RiskDTO> getListDataSearchByOther(RiskDTO riskDTO) {
    List<RiskDTO> listRiskFromTT = new ArrayList<>();
    try{
      Map<String, Object> params = new HashMap<>();
      String sql = " SELECT * FROM WFM.RISK WHERE 1 = 1";
      if(riskDTO != null){
        sql += " AND OTHER_SYSTEM_CODE = :otherSystemCode";
        params.put("otherSystemCode", riskDTO.getOtherSystemCode());
      }
      listRiskFromTT = getNamedParameterJdbcTemplate().query(sql, params,
          BeanPropertyRowMapper.newInstance(RiskDTO.class));
      return listRiskFromTT;
    }catch (Exception e){
      log.error(e.getMessage());
    }
    return listRiskFromTT;
  }

  static public void zipFile(String path, List<String> fileList) {
    FileOutputStream fos = null;
    ZipOutputStream zipOut = null;
    try {
      File file = new File(path);
      file.getParentFile().mkdirs();
      file.createNewFile();
      fos = new FileOutputStream(path);
      zipOut = new ZipOutputStream(fos);
      for (String filePath : fileList) {
        File fileToZip = new File(filePath);
        zipOut.putNextEntry(new ZipEntry(fileToZip.getName()));
        Files.copy(fileToZip.toPath(), zipOut);
      }
      zipOut.closeEntry();
      //remember close it
      zipOut.close();
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    } finally {
      if (zipOut != null) {
        try {
          zipOut.closeEntry();
          //remember close it
        } catch (IOException ex) {
          log.error(ex.getMessage(), ex);
        }
        try {
          zipOut.close();
        } catch (IOException ex) {
          log.error(ex.getMessage(), ex);
        }
      }
      if (fos != null){
        try {
          fos.close();
        }catch (IOException io){
          log.error(io.getMessage(), io);
        }
      }
    }
  }

  public List<String> filterList(List<String> stringList) {
    List<String> newList = new ArrayList<>();
    for (String item : stringList) {
      if (!newList.contains(item)) {
        newList.add(item);
      }
    }
    return newList;
  }

  @Override
  public ResultInSideDto updateRiskOtherSystem(RiskDTO riskDTO){
    RiskEntity riskEntity = getEntityManager().find(RiskEntity.class, riskDTO.getRiskId());
    riskEntity.setOtherSystemCode(riskDTO.getOtherSystemCode());
    riskEntity.setInsertSource(riskDTO.getInsertSource());
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(riskEntity);
    return resultInSideDTO;
  }
}
