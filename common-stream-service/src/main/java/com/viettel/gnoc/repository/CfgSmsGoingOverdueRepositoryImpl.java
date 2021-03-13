package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CfgSmsGoingOverdueDTO;
import com.viettel.gnoc.commons.dto.CfgSmsUserGoingOverdueDTO;
import com.viettel.gnoc.commons.dto.CfgSmsUserGoingOverdueFullDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.CfgSmsGoingOverdueEntity;
import com.viettel.gnoc.commons.model.CfgSmsUserGoingOverdueEntity;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Repository
public class CfgSmsGoingOverdueRepositoryImpl extends BaseRepository implements
    CfgSmsGoingOverdueRepository {

  @Override
  public Datatable getListCfgSmsGoingOverdueDTO(
      CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TT_CFG_SMS_OVERDUE, "cfgSmsGoingOverdue-get-list");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(cfgSmsGoingOverdueDTO.getUserId())) {
      sql += ", COMMON_GNOC.CFG_SMS_USER_GOING_OVERDUE B ";
    }
    sql += " WHERE Q.UNIT_ID = E.UNIT_ID AND to_char(Q.LEVEL_ID) = w.ITEM_VALUE(+) ";
    if (StringUtils.isNotNullOrEmpty(cfgSmsGoingOverdueDTO.getCfgId())) {
      sql += " AND Q.CFG_ID = :cfgId";
      parameters.put("cfgId", cfgSmsGoingOverdueDTO.getCfgId());
    }
    if (StringUtils.isNotNullOrEmpty(cfgSmsGoingOverdueDTO.getUserId())) {
      sql += " AND Q.CFG_ID = B.CFG_ID AND B.USER_ID = :userId ";
      parameters.put("userId", cfgSmsGoingOverdueDTO.getUserId());
    }
    if (StringUtils.isNotNullOrEmpty(cfgSmsGoingOverdueDTO.getCfgName())) {
      sql += " AND (LOWER(Q.CFG_NAME) LIKE :cfgName ESCAPE '\\')";
      parameters.put("cfgName",
          StringUtils.convertLowerParamContains(cfgSmsGoingOverdueDTO.getCfgName()));
    }
    if (StringUtils.isNotNullOrEmpty(cfgSmsGoingOverdueDTO.getUnitId())) {
      sql += " AND Q.UNIT_ID = :unitId";
      parameters.put("unitId", cfgSmsGoingOverdueDTO.getUnitId());
    }
    if (StringUtils.isNotNullOrEmpty(cfgSmsGoingOverdueDTO.getLocationId())) {
      sql += " AND Q.LOCATION_ID = :locationId";
      parameters.put("locationId", cfgSmsGoingOverdueDTO.getLocationId());
    }
    if (StringUtils.isNotNullOrEmpty(cfgSmsGoingOverdueDTO.getPriorityId())) {
      sql += " AND Q.PRIORITY_ID = :priorityId";
      parameters.put("priorityId", cfgSmsGoingOverdueDTO.getPriorityId());
    }

    if (StringUtils.isNotNullOrEmpty(cfgSmsGoingOverdueDTO.getLevelId())) {
      sql += " AND Q.LEVEL_ID = :levelId";
      parameters.put("levelId", cfgSmsGoingOverdueDTO.getLevelId());
    }
    if (StringUtils.isNotNullOrEmpty(cfgSmsGoingOverdueDTO.getSearchAll())) {
      sql += " AND (LOWER(Q.CFG_NAME) LIKE :searchAll ESCAPE '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(cfgSmsGoingOverdueDTO.getSearchAll()));
      sql += " ORDER BY Q.CFG_NAME ";
    }

    return getListDataTableBySqlQuery(sql, parameters, cfgSmsGoingOverdueDTO.getPage(),
        cfgSmsGoingOverdueDTO.getPageSize(), CfgSmsGoingOverdueDTO.class,
        cfgSmsGoingOverdueDTO.getSortName(),
        cfgSmsGoingOverdueDTO.getSortType());
  }

  @Override
  public String updateCfgSmsGoingOverdue(CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO) {
    CfgSmsGoingOverdueEntity cfgSmsGoingOverdueEntity = cfgSmsGoingOverdueDTO.toEntity();
    getEntityManager().merge(cfgSmsGoingOverdueEntity);
    return RESULT.SUCCESS;
  }

  @Override
  public String deleteCfgSmsGoingOverdue(Long id) {
    deleteById(CfgSmsGoingOverdueEntity.class, id, colId);
    deleteById(CfgSmsUserGoingOverdueEntity.class, id, colId);
    return RESULT.SUCCESS;
  }

  @Override
  public CfgSmsGoingOverdueDTO findCfgSmsGoingOverdueById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CfgSmsGoingOverdueEntity.class, id).toDTO();
    }
    return null;
  }

  private ResultInSideDto insertCfgSmsGoingOverdueCommon(List<CfgSmsUserGoingOverdueDTO> list,
      CfgSmsGoingOverdueEntity entitySms, CfgSmsUserGoingOverdueEntity entityUser) {
    ResultInSideDto resultInSideDto = insertByModel(entitySms, colId);
    String userId;
    String cfgType;
    if (!list.isEmpty()) {
      List<CfgSmsUserGoingOverdueDTO> a = new ArrayList<>();
      for (CfgSmsUserGoingOverdueDTO dto1 : list) {
        CfgSmsUserGoingOverdueDTO dto2 = new CfgSmsUserGoingOverdueDTO();
        userId = dto1.getUserId();
        cfgType = dto1.getCfgType();
        dto2.setCfgType(cfgType);
        dto2.setUserId(userId);
        dto2.setCfgId(resultInSideDto.getId().toString());
        a.add(dto2);
      }
      for (CfgSmsUserGoingOverdueDTO x : a) {
        entityUser = x.toEntity();
        insertByModel(entityUser, colId);
      }
      return resultInSideDto;
    } else {
      return resultInSideDto;
    }
  }

  private List<CfgSmsGoingOverdueDTO> checkUniqueCfgSmsGoingOverdue(String levelId, String unitId,
      String priorityId, String locationId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TT_CFG_SMS_OVERDUE, "get-list-check-unique");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(levelId)) {
      sql += " AND a.LEVEL_ID = :levelId";
      parameters.put("levelId", levelId);
    } else {
      sql += " AND a.LEVEL_ID IS NULL";
    }
    if (!StringUtils.isStringNullOrEmpty(unitId)) {
      sql += " AND a.UNIT_ID = :unitId ";
      parameters.put("unitId", unitId);
    } else {
      sql += " AND a.UNIT_ID IS NULL";
    }
    if (!StringUtils.isStringNullOrEmpty(priorityId)) {
      sql += " AND a.PRIORITY_ID = :priorityId";
      parameters.put("priorityId", priorityId);
    } else {
      sql += " AND a.PRIORITY_ID IS NULL";
    }
    if (!StringUtils.isStringNullOrEmpty(locationId)) {
      sql += " AND a.LOCATION_ID = :locationId";
      parameters.put("locationId", locationId);
    } else {
      sql += " AND a.LOCATION_ID IS NULL";
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CfgSmsGoingOverdueDTO.class));
  }

  @Override
  public ResultInSideDto insertCfgSmsGoingOverdue(CfgSmsGoingOverdueDTO dto) {
    CfgSmsGoingOverdueEntity entity = dto.toEntity();
    List<CfgSmsUserGoingOverdueDTO> list = dto.getList();
    CfgSmsUserGoingOverdueEntity entity2 = new CfgSmsUserGoingOverdueEntity();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.DUPLICATE);
    List<CfgSmsGoingOverdueDTO> listSmsGoing1 = checkUniqueCfgSmsGoingOverdue(dto.getLevelId(),
        dto.getUnitId(), dto.getPriorityId(), dto.getLocationId());
    if (listSmsGoing1 == null || listSmsGoing1.isEmpty()) {
      return insertCfgSmsGoingOverdueCommon(list, entity, entity2);
    }
//    if (StringUtils.isNotNullOrEmpty(dto.getLevelId()) && StringUtils
//        .isNotNullOrEmpty(dto.getUnitId()) && StringUtils
//        .isNotNullOrEmpty(dto.getPriorityId()) && StringUtils
//        .isNotNullOrEmpty(dto.getLocationId())) {
//      List<CfgSmsGoingOverdueDTO> listSmsGoing1 = checkUniqueCfgSmsGoingOverdue(dto.getLevelId(),
//          dto.getUnitId(), dto.getPriorityId(), dto.getLocationId());
//    } else if (StringUtils.isNotNullOrEmpty(dto.getLevelId()) && StringUtils
//        .isNotNullOrEmpty(dto.getUnitId()) && StringUtils.isNotNullOrEmpty(dto.getPriorityId())) {
//      List<CfgSmsGoingOverdueDTO> listSmsGoing2 = checkUniqueCfgSmsGoingOverdue(dto.getLevelId(),
//          dto.getUnitId(), dto.getPriorityId(), null);
//      List<CfgSmsGoingOverdueDTO> dtos1 = new ArrayList<>();
//      if (listSmsGoing2 != null && listSmsGoing2.size() > 0) {
//        for (CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO : listSmsGoing2) {
//          if (!StringUtils.isStringNullOrEmpty(cfgSmsGoingOverdueDTO.getLocationId())) {
//            dtos1.add(cfgSmsGoingOverdueDTO);
//            break;
//          }
//        }
//      }
//      if (dtos1.isEmpty() || dtos1 == null) {
//        return insertCfgSmsGoingOverdueCommon(list, entity, entity2);
//      }
//    } else if (StringUtils.isNotNullOrEmpty(dto.getLevelId()) && StringUtils
//        .isNotNullOrEmpty(dto.getUnitId()) && StringUtils.isNotNullOrEmpty(dto.getLocationId())) {
//      List<CfgSmsGoingOverdueDTO> listSmsGoing3 = checkUniqueCfgSmsGoingOverdue(dto.getLevelId(),
//          dto.getUnitId(), null, dto.getLocationId());
//      List<CfgSmsGoingOverdueDTO> dtos1 = new ArrayList<>();
//      if (listSmsGoing3 != null && listSmsGoing3.size() > 0) {
//        for (CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO : listSmsGoing3) {
//          if (!StringUtils.isStringNullOrEmpty(cfgSmsGoingOverdueDTO.getPriorityId())) {
//            dtos1.add(cfgSmsGoingOverdueDTO);
//            break;
//          }
//        }
//      }
//      if (dtos1.isEmpty() || dtos1 == null) {
//        return insertCfgSmsGoingOverdueCommon(list, entity, entity2);
//      }
//    } else if (StringUtils.isNotNullOrEmpty(dto.getLevelId()) && StringUtils
//        .isNotNullOrEmpty(dto.getUnitId())) {
//      List<CfgSmsGoingOverdueDTO> listSmsGoing4 = checkUniqueCfgSmsGoingOverdue(dto.getLevelId(),
//          dto.getUnitId(), null, null);
//      ArrayList<CfgSmsGoingOverdueDTO> dtos1 = new ArrayList<>();
//      if (listSmsGoing4 != null && listSmsGoing4.size() > 0) {
//        for (CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO : listSmsGoing4) {
//          if (!StringUtils.isStringNullOrEmpty(cfgSmsGoingOverdueDTO.getPriorityId())
//              || !StringUtils
//              .isStringNullOrEmpty(cfgSmsGoingOverdueDTO.getLocationId())) {
//            dtos1.add(cfgSmsGoingOverdueDTO);
//            break;
//          }
//        }
//      }
//      if (dtos1.isEmpty() || dtos1 == null) {
//        return insertCfgSmsGoingOverdueCommon(list, entity, entity2);
//      }
//    }
    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    return resultInSideDto;
  }


  @Override
  public List<String> getSequenseCfgSmsGoingOverdue(String seqName, int[] size) {
    return getListSequense(seqName, size);
  }

  @Override
  public List<CfgSmsGoingOverdueDTO> getListCfgSmsGoingOverdueByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFiedlList) {
    return onSearchByConditionBean(new CfgSmsGoingOverdueEntity(), lstCondition, rowStart, maxRow,
        sortType, sortFiedlList);
  }

  @Override
  public UsersInsideDto getUserInfo(Long userId) {
    if (userId != null && userId > 0) {
      return getEntityManager().find(UsersEntity.class, userId).toDTO();
    }
    return null;
  }

  @Override
  public String insertOrUpdateCfg(List<CfgSmsGoingOverdueDTO> cfgSmsGoingOverdueDTOS) {
    for (CfgSmsGoingOverdueDTO dto : cfgSmsGoingOverdueDTOS) {
      CfgSmsGoingOverdueEntity entity = dto.toEntity();
      if (entity.getCfgId() != null && entity.getCfgId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public ResultInSideDto deleteCfgSmsGoingOverdueAndUserList(
      CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      deleteById(CfgSmsUserGoingOverdueEntity.class,
          Long.parseLong(cfgSmsGoingOverdueDTO.getCfgId()), colId);
      getEntityManager().remove(cfgSmsGoingOverdueDTO.toEntity());
      resultInSideDto.setKey(RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
    }
    return resultInSideDto;
  }

  @Override
  public List<CfgSmsGoingOverdueDTO> getListCfgSmsGoingOverdueDTO_allFields(
      String cfgName, String unitId, String userId, String levelId) {
    BaseDto baseDto = sqlSearch(cfgName, unitId, userId, levelId);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(CfgSmsGoingOverdueDTO.class));
  }

  @Override
  public Datatable getListCfgSmsUser(
      CfgSmsUserGoingOverdueFullDTO cfgSmsUserGoingOverdueFullDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TT_CFG_SMS_OVERDUE, "getListCfgSmsUser");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("cfgId", cfgSmsUserGoingOverdueFullDTO.getCfgId());
    return getListDataTableBySqlQuery(sqlQuery, parameters, cfgSmsUserGoingOverdueFullDTO.getPage(),
        cfgSmsUserGoingOverdueFullDTO.getPageSize(), CfgSmsUserGoingOverdueFullDTO.class,
        cfgSmsUserGoingOverdueFullDTO.getSortName(), cfgSmsUserGoingOverdueFullDTO.getSortType());
  }

  @Override
  public String deleteListCfgSmsGoingOverdue(
      List<CfgSmsGoingOverdueDTO> cfgSmsGoingOverdueListDTO) {
    return deleteByListDTO(cfgSmsGoingOverdueListDTO, CfgSmsGoingOverdueEntity.class, colId);
  }

  @Override
  public ResultInSideDto updateCfgSmsGoingOverdue2(CfgSmsGoingOverdueDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    Long maxLevelID = getMaxLevelIDByUnitID(dto);
    Long minLevelID = getMinLevelIDByUnitID(dto);
    Long countElementWithMinLevelID = getCountElementWithMinLevelID(dto);
    Long countElementWithMaxLevelID = getCountElementWithMaxLevelID(dto);
    try {
      ArrayList<CfgSmsUserGoingOverdueDTO> list = dto.getList();
      deleteById(CfgSmsUserGoingOverdueEntity.class, Long.parseLong(dto.getCfgId()), colId);
      deleteById(CfgSmsGoingOverdueEntity.class, Long.parseLong(dto.getCfgId()), colId);
      ResultInSideDto resultInSideDto2 = new ResultInSideDto();
      resultInSideDto2.setKey(RESULT.ERROR);
      if (!list.isEmpty() && list != null) {
        dto.setCfgId("");
        if (maxLevelID == minLevelID && countElementWithMinLevelID == 1) {
          if (Long.valueOf(dto.getLevelId()) <= minLevelID) {
            return insertCfgSmsGoingOverdue(dto);
          } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInSideDto2.setMessage(I18n.getLanguage("cfgSmsGoingOverdue.err.level"));
            return resultInSideDto2;
          }
        } else if (maxLevelID == minLevelID && countElementWithMinLevelID > 1) {
          if (Long.valueOf(dto.getLevelId()) == minLevelID || Long.valueOf(dto.getLevelId()) == (
              minLevelID + 1)) {
            return insertCfgSmsGoingOverdue(dto);
          } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInSideDto2.setMessage(I18n.getLanguage("cfgSmsGoingOverdue.err.level"));
            return resultInSideDto2;
          }
        } else if (maxLevelID > minLevelID && countElementWithMaxLevelID == 1) {
          if (Long.valueOf(dto.getLevelId()) == maxLevelID || Long.valueOf(dto.getLevelId()) == (
              maxLevelID - 1)) {
            return insertCfgSmsGoingOverdue(dto);
          } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInSideDto2.setMessage(I18n.getLanguage("cfgSmsGoingOverdue.err.level"));
            return resultInSideDto2;
          }
        } else if (maxLevelID > minLevelID && countElementWithMaxLevelID > 1) {
          if (Long.valueOf(dto.getLevelId()) == maxLevelID || Long.valueOf(dto.getLevelId()) == (
              maxLevelID - 1) || Long.valueOf(dto.getLevelId()) == (maxLevelID + 1)) {
            return insertCfgSmsGoingOverdue(dto);
          } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInSideDto2.setMessage(I18n.getLanguage("cfgSmsGoingOverdue.err.level"));
            return resultInSideDto2;
          }
        }
      } else if (list.isEmpty() || list == null) {
        dto.setCfgId("");
        if (maxLevelID == minLevelID && countElementWithMinLevelID == 1) {
          if (Long.valueOf(dto.getLevelId()) <= minLevelID) {
            return insertCfgSmsGoingOverdue(dto);
          } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInSideDto2.setMessage(I18n.getLanguage("cfgSmsGoingOverdue.err.level"));
            return resultInSideDto2;
          }
        } else if (maxLevelID == minLevelID && countElementWithMinLevelID > 1) {
          if (Long.valueOf(dto.getLevelId()) == minLevelID || Long.valueOf(dto.getLevelId()) == (
              minLevelID + 1)) {
            return insertCfgSmsGoingOverdue(dto);
          } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInSideDto2.setMessage(I18n.getLanguage("cfgSmsGoingOverdue.err.level"));
            return resultInSideDto2;
          }
        } else if (maxLevelID > minLevelID && countElementWithMaxLevelID == 1) {
          if (Long.valueOf(dto.getLevelId()) == maxLevelID || Long.valueOf(dto.getLevelId()) == (
              maxLevelID - 1)) {
            return insertCfgSmsGoingOverdue(dto);
          } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInSideDto2.setMessage(I18n.getLanguage("cfgSmsGoingOverdue.err.level"));
            return resultInSideDto2;
          }
        } else if (maxLevelID > minLevelID && countElementWithMaxLevelID > 1) {
          if (Long.valueOf(dto.getLevelId()) == maxLevelID || Long.valueOf(dto.getLevelId()) == (
              maxLevelID - 1) || Long.valueOf(dto.getLevelId()) == (maxLevelID + 1)) {
            return insertCfgSmsGoingOverdue(dto);
          } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInSideDto2.setMessage(I18n.getLanguage("cfgSmsGoingOverdue.err.level"));
            return resultInSideDto2;
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public Long getMaxLevelIDByUnitID(CfgSmsGoingOverdueDTO dto) {
    String sql =
        "SELECT max(to_number(a.LEVEL_ID)) levelMaxId FROM COMMON_GNOC.CFG_SMS_GOING_OVERDUE a WHERE a.UNIT_ID = :p_unit_id ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_unit_id", Long.parseLong(dto.getUnitId()));
    List<CfgSmsGoingOverdueDTO> cfgSmsGoingOverdueDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(CfgSmsGoingOverdueDTO.class));
    Long maxLevelID = 0L;
    if (cfgSmsGoingOverdueDTOS != null && cfgSmsGoingOverdueDTOS.size() > 0
        && cfgSmsGoingOverdueDTOS.get(0).getLevelMaxId() != null) {
      maxLevelID = cfgSmsGoingOverdueDTOS.get(0).getLevelMaxId();
    }
    return maxLevelID;
  }

  public Long getMinLevelIDByUnitID(CfgSmsGoingOverdueDTO dto) {
    String sql =
        "SELECT min(to_number(a.LEVEL_ID)) levelMinId FROM COMMON_GNOC.CFG_SMS_GOING_OVERDUE a WHERE a.UNIT_ID = :p_unit_id ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_unit_id", Long.parseLong(dto.getUnitId()));
    List<CfgSmsGoingOverdueDTO> cfgSmsGoingOverdueDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(CfgSmsGoingOverdueDTO.class));
    Long levelMinId = 0L;
    if (cfgSmsGoingOverdueDTOS != null && cfgSmsGoingOverdueDTOS.size() > 0
        && cfgSmsGoingOverdueDTOS.get(0).getLevelMinId() != null) {
      levelMinId = cfgSmsGoingOverdueDTOS.get(0).getLevelMinId();
    }
    return levelMinId;
  }

  public Long getCountElementWithMaxLevelID(CfgSmsGoingOverdueDTO dto) {
    String sql =
        "SELECT COUNT(*) countElementWithMaxLevelID FROM COMMON_GNOC.CFG_SMS_GOING_OVERDUE a WHERE a.UNIT_ID = :p_unit_id "
            + " AND a.LEVEL_ID = :p_level_id ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_unit_id", Long.parseLong(dto.getUnitId()));
    parameters.put("p_level_id", getMaxLevelIDByUnitID(dto));
    List<CfgSmsGoingOverdueDTO> cfgSmsGoingOverdueDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(CfgSmsGoingOverdueDTO.class));
    Long countElementWithMaxLevelID = 0L;
    if (cfgSmsGoingOverdueDTOS != null && cfgSmsGoingOverdueDTOS.size() > 0
        && cfgSmsGoingOverdueDTOS.get(0).getCountElementWithMaxLevelID() != null) {
      countElementWithMaxLevelID = cfgSmsGoingOverdueDTOS.get(0).getCountElementWithMaxLevelID();
    }
    return countElementWithMaxLevelID;
  }

  public Long getCountElementWithMinLevelID(CfgSmsGoingOverdueDTO dto) {
    String sql =
        "SELECT COUNT(*) countElementWithMinLevelID FROM COMMON_GNOC.CFG_SMS_GOING_OVERDUE a WHERE a.UNIT_ID = :p_unit_id "
            + " AND a.LEVEL_ID = :p_level_id ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_unit_id", Long.parseLong(dto.getUnitId()));
    parameters.put("p_level_id", getMinLevelIDByUnitID(dto));
    List<CfgSmsGoingOverdueDTO> cfgSmsGoingOverdueDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(CfgSmsGoingOverdueDTO.class));
    Long countElementWithMinLevelID = 0L;
    if (cfgSmsGoingOverdueDTOS != null && cfgSmsGoingOverdueDTOS.size() > 0
        && cfgSmsGoingOverdueDTOS.get(0).getCountElementWithMinLevelID() != null) {
      countElementWithMinLevelID = cfgSmsGoingOverdueDTOS.get(0).getCountElementWithMinLevelID();
    }
    return countElementWithMinLevelID;
  }

  private BaseDto sqlSearch(String cfgName, String unitId, String userId, String levelId) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TT_CFG_SMS_OVERDUE,
        "getListCfgSmsGoingOverdueDTO_allFields");
    Map<String, Object> parameters = new HashMap<>();
    if (userId != null && !"".equals(userId)) {
      sqlQuery += ", COMMON_GNOC.CFG_SMS_USER_GOING_OVERDUE B ";
    }
    sqlQuery += " WHERE A.UNIT_ID = C.UNIT_ID AND A.LEVEL_ID = d.ITEM_VALUE   ";
    if (cfgName != null && !"".equals(cfgName)) {
      sqlQuery += " AND lower(A.CFG_NAME) like :cfgName ";
      parameters.put("cfgName", cfgName);
    }
    if (userId != null && !"".equals(userId)) {
      sqlQuery += " AND A.CFG_ID = B.CFG_ID AND B.USER_ID = :userId ";
      parameters.put("userId", userId);
    }
    if (unitId != null && !"".equals(unitId)) {
      sqlQuery += " AND C.UNIT_ID = :unitId ";
      parameters.put("unitId", unitId);
    }
    if (levelId != null && !"".equals(levelId)) {
      sqlQuery += " AND A.LEVEL_ID = :levelId ";
      parameters.put("levelId", levelId);
    }
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  private static final String colId = "cfgId";

}
