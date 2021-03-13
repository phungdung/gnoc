package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.CFG_TIME_TROUBLE_PROCESS_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.model.CfgTimeTroubleProcessEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgTimeTroubleProcessRepositoryImpl extends BaseRepository implements
    CfgTimeTroubleProcessRepository {

  @Override
  public Datatable getListCfgTimeTroubleProcessDTO(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    BaseDto baseDto = sqlGetListCfgTimeTroubleProcessDTO(cfgTimeTroubleProcessDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        cfgTimeTroubleProcessDTO.getPage(), cfgTimeTroubleProcessDTO.getPageSize(),
        CfgTimeTroubleProcessDTO.class, cfgTimeTroubleProcessDTO.getSortName(),
        cfgTimeTroubleProcessDTO.getSortType());
    return datatable;
  }

  @Override
  public CfgTimeTroubleProcessDTO findCfgTimeTroubleProcessById(Long id) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_TIME_TROUBLE_PROCESS,
        "find-Cfg-Time-Trouble-Process-By-Id");
    Map<String, Object> parameters = new HashMap<>();
    sql += " AND cfg.ID = :id";
    parameters.put("id", id);
    List<CfgTimeTroubleProcessDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CfgTimeTroubleProcessDTO.class));
    if (list != null && !list.isEmpty()) {
      CfgTimeTroubleProcessDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  @Override
  public ResultInSideDto insertCfgTimeTroubleProcess(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    cfgTimeTroubleProcessDTO.setLastUpdateTime(new Date());
    CfgTimeTroubleProcessEntity cfgTimeTroubleProcessEntity = getEntityManager().merge(cfgTimeTroubleProcessDTO.toEntity());
    if (cfgTimeTroubleProcessEntity != null) {
      resultInSideDto.setId(cfgTimeTroubleProcessEntity.getId());
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCfgTimeTroubleProcess(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    cfgTimeTroubleProcessDTO.setLastUpdateTime(new Date());
    getEntityManager().merge(cfgTimeTroubleProcessDTO.toEntity());
    resultInSideDto.setId(cfgTimeTroubleProcessDTO.getId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertOrUpdateListCfgTimeTroubleProcess(
      List<CfgTimeTroubleProcessDTO> listCfgTimeTroubleProcessDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (CfgTimeTroubleProcessDTO dto : listCfgTimeTroubleProcessDTO) {
      if (dto.getId() != null) {
        resultInSideDto = updateCfgTimeTroubleProcess(dto);
      } else {
        resultInSideDto = insertCfgTimeTroubleProcess(dto);
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteCfgTimeTroubleProcess(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    CfgTimeTroubleProcessEntity entity = getEntityManager()
        .find(CfgTimeTroubleProcessEntity.class, id);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<String> getSequenseCfgTimeTroubleProcess(String seqName, int... size) {
    int number = (size[0] > 0 ? size[0] : 1);
    return getListSequense(seqName, number);
  }

  @Override
  public List<CfgTimeTroubleProcessDTO> getListCfgTimeTroubleProcessByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchByConditionBean(new CfgTimeTroubleProcessEntity(), lstCondition, rowStart,
        maxRow, sortType, sortFieldList);
  }

  @Override
  public List<CfgTimeTroubleProcessDTO> getListCfgTimeTroubleProcessExport(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    BaseDto baseDto = sqlGetListCfgTimeTroubleProcessDTO(cfgTimeTroubleProcessDTO);
    List<CfgTimeTroubleProcessDTO> list = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CfgTimeTroubleProcessDTO.class));
    return list;
  }

  @Override
  public List<CatItemDTO> getListSubCategory(Long typeId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_TIME_TROUBLE_PROCESS, "get-List-SubCategory");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("ptSubCategory", CFG_TIME_TROUBLE_PROCESS_MASTER_CODE.ALARM_GROUP);
    if (typeId != null) {
      sql += " AND PARENT_ITEM_ID = :typeId";
      parameters.put("typeId", typeId);
    }
    sql += " ORDER BY ITEM_NAME";
    List<CatItemDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      list = setLanguage(list, lstLanguage, Constants.ITEM_ID, Constants.ITEM_NAME);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public CfgTimeTroubleProcessDTO checkCfgTimeTroubleProcessExit(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    List<CfgTimeTroubleProcessEntity> dataEntity = (List<CfgTimeTroubleProcessEntity>) findByMultilParam(
        CfgTimeTroubleProcessEntity.class,
        "country", cfgTimeTroubleProcessDTO.getCountry(),
        "typeId", cfgTimeTroubleProcessDTO.getTypeId(),
        "subCategoryId", cfgTimeTroubleProcessDTO.getSubCategoryId(),
        "priorityId", cfgTimeTroubleProcessDTO.getPriorityId());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  public BaseDto sqlGetListCfgTimeTroubleProcessDTO(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    BaseDto baseDto = new BaseDto();
    String leeLocale = I18n.getLocale();
    String sql;
    Map<String, Object> parameters = new HashMap<>();
    if ("vi_VN".equals(leeLocale)) {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_TIME_TROUBLE_PROCESS,
          "get-List-Cfg-Time-Trouble-Process-DTO");
    } else {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_TIME_TROUBLE_PROCESS,
          "sql-Get-List-Cfg-Time-Trouble-Process-DTO-By-Language");
      parameters.put("appliedSystem", LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
      parameters.put("appliedBussiness", APPLIED_BUSSINESS.CAT_ITEM.toString());
      parameters.put("leeLocale", leeLocale);
    }
    if (StringUtils.isNotNullOrEmpty(cfgTimeTroubleProcessDTO.getSearchAll())) {
      if ("vi_VN".equals(leeLocale)) {
        sql += " AND (LOWER(ci4.ITEM_NAME) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(ci1.ITEM_NAME) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(ci2.ITEM_NAME) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(ci3.ITEM_NAME) LIKE :searchAll ESCAPE '\\')";
      } else {
        sql += " AND (LOWER(lle4.LEE_VALUE) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(lle1.LEE_VALUE) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(lle2.LEE_VALUE) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(lle3.LEE_VALUE) LIKE :searchAll ESCAPE '\\')";
      }
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(cfgTimeTroubleProcessDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(cfgTimeTroubleProcessDTO.getCountry())) {
      sql += " AND cfg.COUNTRY = :country";
      parameters.put("country", cfgTimeTroubleProcessDTO.getCountry());
    }
    if (cfgTimeTroubleProcessDTO.getTypeId() != null) {
      sql += " AND cfg.TYPE_ID = :typeId";
      parameters.put("typeId", cfgTimeTroubleProcessDTO.getTypeId());
    }
    if (cfgTimeTroubleProcessDTO.getSubCategoryId() != null) {
      sql += " AND cfg.SUB_CATEGORY_ID = :subCategoryId";
      parameters.put("subCategoryId", cfgTimeTroubleProcessDTO.getSubCategoryId());
    }
    if (cfgTimeTroubleProcessDTO.getPriorityId() != null) {
      sql += " AND cfg.PRIORITY_ID = :priorityId";
      parameters.put("priorityId", cfgTimeTroubleProcessDTO.getPriorityId());
    }
    if (cfgTimeTroubleProcessDTO.getIsCall() != null) {
      sql += " AND cfg.IS_CALL = :isCall";
      parameters.put("isCall", cfgTimeTroubleProcessDTO.getIsCall());
    }
    sql += " ORDER BY cfg.LAST_UPDATE_TIME DESC";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public CfgTimeTroubleProcessDTO getConfigTimeTroubleProcess(Long typeId, Long subCategoryId,
      Long priority, String country) {
    CfgTimeTroubleProcessDTO ret = null;
    try {
      String sql = "SELECT   a.id, "
          + "         a.type_id typeId, "
          + "         a.sub_category_id subCategoryId, "
          + "         a.priority_id priorityId, "
          + "         a.create_tt_time createTtTime, "
          + "         a.PROCESS_WO_TIME processWoTime, "
          + "         a.process_tt_time processTtTime,"
          + "         a.close_tt_time closeTtTime," //HaiNV20 add
          + "         a.is_call isCall,"
          + "         a.IS_STATION_VIP isStationVip, "
          + "         a.TIME_STATION_VIP timeStationVip,  "
          + "         a.TIME_WO_VIP timeWoVip,"
          + "         a.cd_Audio_Name cdAudioName,"
          + "         a.ft_Audio_Name ftAudioName "
          + "  FROM   one_tm.cfg_time_trouble_process a "
          + "  WHERE a.type_id = :typeId "
          + "  AND a.sub_category_id = :subCategoryId "
          + "  AND a.priority_id = :priority ";
      if (StringUtils.validString(country) == true && country.toUpperCase().contains("NOC")) {
        sql = sql + "  AND lower(a.country) = :country";
      } else {
        sql = sql + "  AND a.country = 'NOC' ";
      }
      Map<String, Object> parameters = new HashMap<>();

      parameters.put("typeId", typeId);
      parameters.put("subCategoryId", subCategoryId);
      parameters.put("priority", priority);
      parameters.put("typeId", typeId);
      if (StringUtils.validString(country) == true && country.toUpperCase().contains("NOC")) {
        parameters.put("country", country.toLowerCase());
      }

      List<CfgTimeTroubleProcessDTO> obj = getNamedParameterJdbcTemplate().
          query(sql, parameters, BeanPropertyRowMapper.newInstance(CfgTimeTroubleProcessDTO.class));
      if (obj != null && !obj.isEmpty()) {
        ret = obj.get(0);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return ret;
  }

  public List<CatItemDTO> getItemDTO(String typeId, String alarmId, String nationCode) {
    List<CatItemDTO> ret = new ArrayList<>();
    try {
      Map<String, Object> lstParam = new HashMap<>();

      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_TIME_TROUBLE_PROCESS,
          "get-item-dto");

      if (!StringUtils.isStringNullOrEmpty(typeId)) {
        sql += " AND b.TYPE_ID = :typeId ";
        lstParam.put("typeId", typeId);
      }
      if (!StringUtils.isStringNullOrEmpty(alarmId)) {
        sql += " AND b.SUB_CATEGORY_ID = :alarmId ";
        lstParam.put("alarmId", alarmId);
      }
      if (StringUtils.isStringNullOrEmpty(nationCode) || "NOC".equalsIgnoreCase(nationCode)
          || !nationCode.contains("NOC_")) {
        sql += " AND (b.COUNTRY = 'NOC' or b.COUNTRY is null) ";
      } else {
        sql += " AND b.COUNTRY = :nationCode ";
        lstParam.put("nationCode", nationCode);
      }

      sql += " GROUP BY a.item_id, a.item_code, a.item_name, a.item_value ORDER BY a.item_id ";
      ret = getNamedParameterJdbcTemplate()
          .query(sql, lstParam, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
      List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
          LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
          APPLIED_BUSSINESS.CAT_ITEM.toString());

      if (ret != null && lstLanguage != null) {
        ret = setLanguage(ret, lstLanguage, "itemId", "itemName");
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return ret;
  }

}
