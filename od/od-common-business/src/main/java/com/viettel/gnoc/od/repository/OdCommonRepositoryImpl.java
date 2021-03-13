package com.viettel.gnoc.od.repository;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.od.model.OdTypeEntity;
import com.viettel.gnoc.wo.model.CfgMapUnitGnocNimsEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class OdCommonRepositoryImpl extends BaseRepository implements OdCommonRepository {


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
  public OdTypeDTO checkOdTypeExist(String odTypeCode) {
    List<OdTypeEntity> dataEntity = (List<OdTypeEntity>) findByMultilParam(OdTypeEntity.class,
        "odTypeCode",
        odTypeCode);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public UnitEntity getUnitByUnitCode(String unitCode) {
    List<UnitEntity> dataEntity = (List<UnitEntity>) findByMultilParam(
        UnitEntity.class,
        "unitCode",
        unitCode);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0);
    }
    return null;
  }

  @Override
  public UnitEntity getUnitCodeMapNims(String unitNimsCode) {
    List<CfgMapUnitGnocNimsEntity> dataEntity = (List<CfgMapUnitGnocNimsEntity>) findByMultilParam(
        CfgMapUnitGnocNimsEntity.class,
        "unitNimsCode",
        unitNimsCode);
    if (dataEntity != null && dataEntity.size() > 0) {
      CfgMapUnitGnocNimsEntity o = dataEntity.get(0);
      if (!StringUtils.isStringNullOrEmpty(o.getUnitGnocCode())) {
        return getUnitByUnitCode(o.getUnitGnocCode());
      }
    }
    return null;
  }

//  @Override
//  public Map<String, String> getConfigProperty() throws Exception {
//    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_OD, "config-property-sql");
//    Map<String, String> mapResult = new HashMap<String, String>();
//    List<ConfigPropertyDTO> result = getEntityManager().createQuery(sqlQuery).getResultList();
//    if (result != null && !result.isEmpty()) {
//      for (ConfigPropertyDTO cfg : result) {
//        mapResult.put(cfg.getKey(), cfg.getValue());
//      }
//    }
//    return mapResult;
//  }

  @Override
  public String getSeqTableOD(String sequense) {
    return getSeqTableBase(sequense);
  }

  public BaseDto sqlSearch(OdSearchInsideDTO odSearchInsideDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_OD, "od-sql-search");
    Map<String, Object> parameters = new HashMap<>();
    List<Long> lstStatus = new ArrayList<>();
    if (odSearchInsideDTO.getOffset() == null) {
      odSearchInsideDTO.setOffset(0L);
    }
    parameters.put("offset", odSearchInsideDTO.getOffset());
    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getOdCode())) {
      sqlQuery += " AND LOWER(o.OD_CODE) LIKE :odCode ESCAPE '\\'";
      parameters
          .put("odCode", StringUtils.convertLowerParamContains(odSearchInsideDTO.getOdCode()));
    }
    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getOdName())) {
      sqlQuery += " AND LOWER(o.OD_NAME) LIKE :odName ESCAPE '\\'";
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

    if (odSearchInsideDTO.getOdId() != null) {
      sqlQuery += " AND o.OD_ID = :odId ";
      parameters.put("odId", odSearchInsideDTO.getOdId());
    }
    if (odSearchInsideDTO.getWoId() != null) {
      sqlQuery += " AND o.WO_ID = :woId ";
      parameters.put("woId", odSearchInsideDTO.getWoId());
    }

    if (odSearchInsideDTO.getReceiveUserId() != null) {
      sqlQuery += " AND o.RECEIVE_USER_ID = :receiveUserId ";
      parameters.put("receiveUserId", odSearchInsideDTO.getReceiveUserId());
    }
    if (odSearchInsideDTO.getCreatePersonId() != null) {
      sqlQuery += " AND o.CREATE_PERSON_ID = :createPersonId ";
      parameters.put("createPersonId", odSearchInsideDTO.getCreatePersonId());
    }

    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getPlanCode())) {
      sqlQuery += " AND LOWER(o.PLAN_CODE) LIKE :planCode ";
      parameters
          .put("planCode", StringUtils.convertLowerParamContains(odSearchInsideDTO.getPlanCode()));
    }
    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getInsertSource())) {
      sqlQuery += " AND o.INSERT_SOURCE = :insertSource ";
      parameters.put("insertSource", odSearchInsideDTO.getInsertSource());
    }

    if (StringUtils.isNotNullOrEmpty(odSearchInsideDTO.getOtherSystemCode())) {
      sqlQuery += " AND LOWER(o.OTHER_SYSTEM_CODE) LIKE :otherSystemCode ";
      parameters.put("otherSystemCode",
          StringUtils.convertLowerParamContains(odSearchInsideDTO.getOtherSystemCode()));
    }

    if (odSearchInsideDTO.getOdGroupTypeId() != null) {
      sqlQuery += " AND o.od_type_id in (select od_type_id from od_type where od_group_type_id = :odGroupTypeId)";
      parameters.put("odGroupTypeId", odSearchInsideDTO.getOdGroupTypeId());
    }
    if (odSearchInsideDTO.getOdTypeId() != null) {
      sqlQuery += " AND o.OD_TYPE_ID = :odTypeId ";
      parameters.put("odTypeId", odSearchInsideDTO.getOdTypeId());
    }

    if (odSearchInsideDTO.getPriorityId() != null) {
      sqlQuery += " AND o.PRIORITY_ID = :priorityId ";
      parameters.put("priorityId", odSearchInsideDTO.getPriorityId());
    }

    if (odSearchInsideDTO.getClearCodeId() != null) {
      sqlQuery += " AND o.clear_code_id = :clearCodeId ";
      parameters.put("clearCodeId", odSearchInsideDTO.getClearCodeId());
    }
    if (odSearchInsideDTO.getCloseCodeId() != null) {
      sqlQuery += " AND o.close_code_id = :closeCodeId ";
      parameters.put("closeCodeId", odSearchInsideDTO.getCloseCodeId());
    }

    if (odSearchInsideDTO.getReceiveUserId() != null) {
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
    if (odSearchInsideDTO.getCreateUnitId() != null) {
      if ("1".equals(odSearchInsideDTO.getChildCreateUnit())) {
        sqlQuery += " AND o.create_person_id in (select user_id from common_gnoc.users "
            + "where unit_id in (select unit_id from common_gnoc.unit where level < 50 start"
            + " with unit_id =:createUnitId connect by prior unit_id = parent_unit_id)) ";
        parameters.put("createUnitId", odSearchInsideDTO.getCreateUnitId());
      } else {
        sqlQuery += " AND o.create_person_id in (select user_id from common_gnoc.users where unit_id =:createUnitId) ";
        parameters.put("createUnitId", odSearchInsideDTO.getCreateUnitId());
      }
    }

    if (odSearchInsideDTO.getReceiveUnitId() != null) {
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

    if (odSearchInsideDTO.getStatus() != null) {
      if ("-1".equals(odSearchInsideDTO.getStatus())) {//Chua hoan thanh - Cho CD phe duyet
//                sql.append(" AND w.status <> 7 and w.status <> 8 and ( w.status <> 6 or (w.status = 6 AND w.result is null))");
      }//ducdm1_them trang thai hoan thanh_start
      else if ((odSearchInsideDTO.getStatus()).contains(",")) {
        sqlQuery += " AND o.status in (:lstStatus)";
        List<String> myList = new ArrayList<>(
            Arrays.asList((odSearchInsideDTO.getStatus()).split(",")));
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

}
