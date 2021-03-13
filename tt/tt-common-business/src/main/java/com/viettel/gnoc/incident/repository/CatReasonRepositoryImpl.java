package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTOSearch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CatReasonRepositoryImpl extends BaseRepository implements
    CatReasonRepository {

  @Override
  public List<CatReasonInSideDTOSearch> getListReasonSearch(CatReasonInSideDTO reasonDto) {
    List<CatReasonInSideDTOSearch> ret = new ArrayList<>();
    try {
      Map<String, Object> lstParam = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CAT_REASON_PROCESS, "tt-reason-search-dto");
      if (!StringUtils.isStringNullOrEmpty(reasonDto.getReasonCode())) {
        sql += "   AND LOWER(a.reason_code) LIKE :reasonCode ";
        lstParam.put("reasonCode", "%" + reasonDto.getReasonCode().toLowerCase() + "%");
      }
      if (!StringUtils.isStringNullOrEmpty(reasonDto.getReasonName())) {
        sql += "   AND LOWER(a.reason_name) LIKE :reasonName ";
        lstParam.put("reasonName", "%" + reasonDto.getReasonName().toLowerCase() + "%");
      }
      //ThanhLV12_fix_23/11/2015_start
      if (reasonDto.getTypeId() != null && !"-1".equals(reasonDto.getTypeId())) {
        sql += " and a.type_id = :typeId ";
        lstParam.put("typeId", reasonDto.getTypeId());
      }
      if (reasonDto.getParentId() != null && !"".equals(reasonDto.getParentId())) {
        sql += " and a.id in ( "
            + "    select id from cat_reason "
            + "    start with parent_id = :parentId "
            + "    connect by PRIOR  id = parent_id "
            + " ) ";
        lstParam.put("parentId", reasonDto.getParentId());
      }

      ret = getNamedParameterJdbcTemplate().query(sql, lstParam,
          BeanPropertyRowMapper.newInstance(CatReasonInSideDTOSearch.class));

      List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
          LANGUAGUE_EXCHANGE_SYSTEM.PT_TT,
          APPLIED_BUSSINESS.CAT_REASON);
      if (ret != null && lstLanguage != null) {
        ret = setLanguage(ret, lstLanguage, "parentId", "parentName");
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    return ret;
  }

  @Override
  public List<CatReasonInSideDTOSearch> getListReasonSearchForWo(CatReasonInSideDTO reasonDto) {
    List<CatReasonInSideDTOSearch> ret = new ArrayList<>();
    try {
      Map<String, Object> lstParam = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CAT_REASON_PROCESS, "tt-reason-search-dto");
      if (!StringUtils.isStringNullOrEmpty(reasonDto.getReasonCode())) {
        sql += "   AND LOWER(a.reason_code) LIKE :reasonCode ";
        lstParam.put("reasonCode", "%" + reasonDto.getReasonCode().toLowerCase() + "%");
      }
      if (!StringUtils.isStringNullOrEmpty(reasonDto.getReasonName())) {
        sql += "   AND LOWER(a.reason_name) LIKE :reasonName ";
        lstParam.put("reasonName", "%" + reasonDto.getReasonName().toLowerCase() + "%");
      }
      if (reasonDto.getTypeId() != null && !"-1".equals(reasonDto.getTypeId())) {
        sql += " and a.type_id = :typeId ";
        lstParam.put("typeId", reasonDto.getTypeId());
      } else if (reasonDto.getLstTypeId() != null && !reasonDto.getLstTypeId().isEmpty()) {
        sql += " and a.type_id in (:lstTypeId) ";
        lstParam.put("lstTypeId", reasonDto.getLstTypeId());
      }
      if (reasonDto.getParentId() != null && !"".equals(reasonDto.getParentId())) {
        sql += " AND A.PARENT_ID = :parentId ";
        lstParam.put("parentId", reasonDto.getParentId());
      }
      if (reasonDto.getIsParentNull() != null && reasonDto.getIsParentNull() == 1L) {
        sql += " AND A.PARENT_ID IS NULL ";
      }

      ret = getNamedParameterJdbcTemplate().query(sql, lstParam,
          BeanPropertyRowMapper.newInstance(CatReasonInSideDTOSearch.class));

      List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
          LANGUAGUE_EXCHANGE_SYSTEM.PT_TT,
          APPLIED_BUSSINESS.CAT_REASON);
      if (ret != null && lstLanguage != null) {
        ret = setLanguage(ret, lstLanguage, "parentId", "parentName");
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    return ret;
  }

  @Override
  public List<CatReasonDTO> getReasonDTOForTree(Boolean isRoot, String typeId, String parentId) {
    BaseDto baseDto = sqlGetReasonDTOForTree(isRoot, typeId, parentId);
    return changeListreasonDTO(baseDto);
  }

  @Override
  public Map<String, CatReasonInSideDTO> getCatReasonData() {
    Map<String, CatReasonInSideDTO> mapReturn = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CAT_REASON_PROCESS,
        "get-Cat-Reason-Data");

    List<CatReasonInSideDTO> lstCatReason = getNamedParameterJdbcTemplate()
        .query(sql, new HashMap<>(), BeanPropertyRowMapper.newInstance(CatReasonInSideDTO.class));
    if (lstCatReason != null && !lstCatReason.isEmpty()) {
      for (CatReasonInSideDTO reason : lstCatReason) {
        mapReturn.put(String.valueOf(reason.getId()), reason);
      }
    }
    return mapReturn;
  }

  @Override
  public List<CatReasonDTO> getReasonDTOForVsmart(String typeId, String parentId, int level) {
    BaseDto baseDto = sqlSearchSqlForVsmart(typeId, parentId, level);
    return changeListreasonDTO(baseDto);
  }


  public List<CatReasonDTO> changeListreasonDTO(BaseDto baseDto) {
    List<CatReasonDTO> reasonDTOList = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CatReasonDTO.class));
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        LANGUAGUE_EXCHANGE_SYSTEM.PT_TT,
        APPLIED_BUSSINESS.CAT_REASON);
    try {
      reasonDTOList = setLanguage(reasonDTOList, lstLanguage, "id", "reasonName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return reasonDTOList;
  }

  public BaseDto sqlGetReasonDTOForTree(Boolean isRoot, String typeId, String parentId) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CAT_REASON_PROCESS,
        "get-Reason-For-Tree");
    Map<String, Object> parameters = new HashMap<>();
    if (isRoot != null && isRoot) {
      sql += " AND a.parent_id is null ";
    }
    if (!DataUtil.isNullOrEmpty(typeId)) {
      sql += "   AND a.type_id = :typeId ";
      parameters.put("typeId", typeId);
    }
    if (!DataUtil.isNullOrEmpty(parentId)) {
      sql += "   AND a.parent_id = :parentId ";
      parameters.put("parentId", parentId);
    }
    sql += " order by NLSSORT(reasonName,'NLS_SORT=vietnamese') ";

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public BaseDto sqlSearchSqlForVsmart(String typeId, String parentId, int level) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CAT_REASON_PROCESS,
        "get-Reason-For-Vsmart");
    Map<String, Object> parameters = new HashMap<>();
    if (level == 3L && StringUtils.isNotNullOrEmpty(parentId)) {
      sql += " START WITH a.parent_id =:parentId ";
      parameters.put("parentId", parentId);
      sql += " CONNECT BY PRIOR a.id = a.parent_id ) cr where 1 = 1";
      sql += " AND cr.id not in (SELECT PARENT_ID FROM one_tm.cat_reason where PARENT_ID is not null) ";
    } else {
      sql += " START WITH a.parent_id IS NULL "
          + " CONNECT BY PRIOR a.id = a.parent_id ) cr where 1 = 1 ";
    }

    if (StringUtils.isNotNullOrEmpty(typeId)) {
      sql += " AND cr.TYPE_ID =:typeId ";
      parameters.put("typeId", typeId);
    }

    if (level != 3L && StringUtils.isNotNullOrEmpty(parentId)) {
      sql += " AND cr.parent_id =:parentId ";
      parameters.put("parentId", parentId);
    }

    if (!StringUtils.isNotNullOrEmpty(parentId)) {
      sql += " AND cr.parent_id IS NULL ";
    }
    sql += " order by cr.REASON_NAME desc ";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<CatReasonDTO> getReasonDTO(String fromDate, String toDate) {
    Map<String, Object> parameters = new HashMap<>();
    try {
      String sql = " SELECT a.id, "
          + " a.reason_name reasonName, "
          + " a.parent_id parentId, "
          + " a.description, "
          + " a.type_id typeId, "
          + " a.reason_type reasonType, "
          + " a.reason_code reasonCode, "
          + " TO_CHAR(a.update_time,'dd/MM/yyyy HH24:MI:ss') updateTime, "
          + " a.IS_CHECK_SCRIPT isCheckScript, "
          + " a.is_update_closure isUpdateClosure "
          + " FROM one_tm.CAT_REASON a "
          + " WHERE 1 = 1 ";
      if (!StringUtils.isStringNullOrEmpty(fromDate)) {
        sql += " AND a.update_time >= to_date(:fromDate,'dd/MM/yyyy HH24:MI:ss') ";
        parameters.put("fromDate", fromDate);
      }
      if (!StringUtils.isStringNullOrEmpty(toDate)) {
        sql += " AND a.update_time <= to_date(:toDate,'dd/MM/yyyy HH24:MI:ss') ";
        parameters.put("toDate", toDate);
      }
      return getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(CatReasonDTO.class));
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
    return null;
  }
}
