package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureITHardDTO;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.maintenance.model.MrCfgProcedureITHardEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrITHardProcedureRepositoryImpl extends BaseRepository implements
    MrITHardProcedureRepository {

  @Override
  public BaseDto sqlSearch(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder.getSqlQueryById("mrITHardProcedure", "get-list-mr-it-hard-procedure");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("lee_Locale", I18n.getLocale());
    parameters.put("Hard", I18n.getLanguage("mrCfgProcedureITHardDTO.Hard"));
    parameters.put("Soft", I18n.getLanguage("mrCfgProcedureITHardDTO.Soft"));
    parameters.put("month", I18n.getLanguage("mrCfgProcedureITHardDTO.month"));
    parameters.put("day", I18n.getLanguage("mrCfgProcedureITHardDTO.day"));
    parameters.put("inActive", I18n.getLanguage("common.inActive"));
    parameters.put("active", I18n.getLanguage("common.active"));
    parameters.put("applied_system", Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    parameters.put("bussiness", Constants.COMMON_TRANSLATE_BUSINESS.CAT_ITEM);
    parameters.put("categoryCode", MR_ITEM_NAME.MR_WORKS);

    if (mrCfgProcedureITHardDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getSearchAll())) {
        sql += (" AND UPPER(CP.PROCEDURE_NAME)  LIKE :searchAll ESCAPE '\\' ");
        parameters.put("searchAll",
            StringUtils.convertUpperParamContains(mrCfgProcedureITHardDTO.getSearchAll()));
      }

      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getProcedureName())) {
        sql += (" AND UPPER(CP.PROCEDURE_NAME)  LIKE :procedureName ESCAPE '\\' ");
        parameters.put("procedureName",
            StringUtils.convertUpperParamContains(mrCfgProcedureITHardDTO.getProcedureName()));
      }

      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getCycleType())) {
        sql += (" AND CP.CYCLE_TYPE = :cycleType ");
        parameters.put("cycleType", mrCfgProcedureITHardDTO.getCycleType());
      }

      if (mrCfgProcedureITHardDTO.getCycle() != null) {
        sql += (" AND CP.CYCLE = :cycle ");
        parameters.put("cycle", mrCfgProcedureITHardDTO.getCycle());
      }

      if (mrCfgProcedureITHardDTO.getProcedureId() != null) {
        sql += (" AND CP.PROCEDURE_ID = :procedureId ");
        parameters.put("procedureId", mrCfgProcedureITHardDTO.getProcedureId());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getMarketCode())) {
        sql += (" AND CP.MARKET_CODE = :marketCode ");
        parameters.put("marketCode", mrCfgProcedureITHardDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getArrayCode())) {
        sql += (" AND CP.ARRAY_CODE = :arrayCode ");
        parameters.put("arrayCode", mrCfgProcedureITHardDTO.getArrayCode());
      }

      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getStatus())) {
        sql += (" AND CP.STATUS = :status ");
        parameters.put("status", mrCfgProcedureITHardDTO.getStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getDeviceType())) {
        sql += (" AND UPPER(CP.DEVICE_TYPE) = :deviceType ");
        parameters.put("deviceType", mrCfgProcedureITHardDTO.getDeviceType());
      }
    }
    sql += " ORDER BY  marketCode,arrayCode,mrMode ";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListMrHardITProcedure(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) {
    BaseDto baseDto = sqlSearch(mrCfgProcedureITHardDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrCfgProcedureITHardDTO.getPage(), mrCfgProcedureITHardDTO.getPageSize(),
        MrCfgProcedureITHardDTO.class, mrCfgProcedureITHardDTO.getSortName(),
        mrCfgProcedureITHardDTO.getSortType());
  }

  @Override
  public MrCfgProcedureITHardDTO getDetail(Long procedureId) {
    if (!StringUtils.isStringNullOrEmpty(procedureId)) {
      BaseDto baseDto = sqlSearch(new MrCfgProcedureITHardDTO(procedureId));
      List<MrCfgProcedureITHardDTO> lst = getNamedParameterJdbcTemplate()
          .query(baseDto.getSqlQuery(), baseDto.getParameters(),
              BeanPropertyRowMapper.newInstance(MrCfgProcedureITHardDTO.class));
      return lst.isEmpty() ? null : lst.get(0);
    }
    return null;
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) {
    MrCfgProcedureITHardEntity entity = getEntityManager()
        .merge(mrCfgProcedureITHardDTO.toEntity());
    return new ResultInSideDto(entity.getProcedureId(), RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public List<MrCfgProcedureITHardDTO> getListDataExport(
      MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) {
    BaseDto baseDto = sqlSearch(mrCfgProcedureITHardDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrCfgProcedureITHardDTO.class));
  }

  @Override
  public ResultInSideDto deleteMrCfgProcedureITHard(Long procedureId) {
    MrCfgProcedureITHardEntity entity = getEntityManager()
        .find(MrCfgProcedureITHardEntity.class, procedureId);
    getEntityManager().remove(entity);
    return new ResultInSideDto(procedureId, RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public boolean checkDuplicateCfgProcedureITHardByDTO(
      MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) {
    String sql = ("SELECT T.MARKET_CODE as marketCode,T.ARRAY_CODE as arrayCode, T.DEVICE_TYPE deviceType FROM MR_CFG_PROCEDURE_IT_HARD T WHERE 1 = 1");
    Map<String, Object> parameters = new HashMap<>();
    if (mrCfgProcedureITHardDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getMarketCode())) {
        sql += (" AND UPPER(T.MARKET_CODE) = :marketCode ");
        parameters.put("marketCode", mrCfgProcedureITHardDTO.getMarketCode().toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getArrayCode())) {
        sql += (" AND UPPER(T.ARRAY_CODE) =:arrayCode ");
        parameters.put("arrayCode", mrCfgProcedureITHardDTO.getArrayCode().toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getMrMode())) {
        sql += (" AND UPPER(T.MR_MODE) =:mrMode ");
        parameters.put("mrMode", mrCfgProcedureITHardDTO.getMrMode().toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getDeviceType())) {
        sql += (" AND UPPER(T.DEVICE_TYPE) =:deviceType ");
        parameters.put("deviceType", mrCfgProcedureITHardDTO.getDeviceType().toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getCycleType())) {
        sql += (" AND UPPER(T.CYCLE_TYPE) =:cycleType ");
        parameters.put("cycleType", mrCfgProcedureITHardDTO.getCycleType().toUpperCase());
      }
      if (mrCfgProcedureITHardDTO.getCycle() != null) {
        sql += (" AND T.CYCLE =:cycle ");
        parameters.put("cycle", mrCfgProcedureITHardDTO.getCycle());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCfgProcedureITHardDTO.getProcedureId())) {
        sql += (" AND T.PROCEDURE_ID <> :procedureId ");
        parameters.put("procedureId", mrCfgProcedureITHardDTO.getProcedureId());
      }
    }
    List<MrCfgProcedureITHardDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrCfgProcedureITHardDTO.class));
    if (lst != null && !lst.isEmpty()) {
      return true;
    }
    return false;
  }

  @Override
  public List<MrSynItDevicesDTO> getListDeviceType() {
    String sqlQuery =
        " SELECT DISTINCT ARRAY_CODE arrayCode,DEVICE_TYPE deviceType "
            + " FROM MR_SYN_IT_DEVICES WHERE DEVICE_TYPE IS NOT NULL ORDER BY ARRAY_CODE ";
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrSynItDevicesDTO.class));
  }

  @Override
  public List<MrCfgProcedureITHardDTO> checkMrCfgProcedureITHardExist(
      MrCfgProcedureITHardDTO cfgProcedureDTO) {
    Map<String, Object> params = new HashMap<>();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "SELECT T.PROCEDURE_ID as procedureId, T.MARKET_CODE as marketCode,T.ARRAY_CODE as arrayCode,  T.DEVICE_TYPE deviceType"
            + " , T.MR_MODE as mrMode, T.CYCLE_TYPE as cycleType, T.CYCLE as cycle FROM MR_CFG_PROCEDURE_IT_HARD T WHERE 1 = 1");
    if (cfgProcedureDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getMarketCode())) {
        sql.append(" AND T.MARKET_CODE =:marketCode ");
        params.put("marketCode", cfgProcedureDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getArrayCode())) {
        sql.append(" AND T.ARRAY_CODE =:arrayCode ");
        params.put("arrayCode", cfgProcedureDTO.getArrayCode());
      }

      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getDeviceType())) {
        sql.append(" AND UPPER(T.DEVICE_TYPE) =:deviceType");
        params.put("deviceType", cfgProcedureDTO.getDeviceType().toUpperCase());
      } else {
        sql.append(" AND T.DEVICE_TYPE is null ");
      }

      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getMrMode())) {
        sql.append(" AND T.MR_MODE =:mrMode ");
        params.put("mrMode", cfgProcedureDTO.getMrMode());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getCycleType())) {
        sql.append(" AND T.CYCLE_TYPE =:cycleType ");
        params.put("cycleType", cfgProcedureDTO.getCycleType());
      } else {
        sql.append(" AND T.CYCLE_TYPE is null ");
      }
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getCycle())) {
        sql.append(" AND T.CYCLE =:cycle ");
        params.put("cycle", cfgProcedureDTO.getCycle());
      } else {
        sql.append(" AND T.CYCLE is null ");
      }

      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getProcedureId())) {
        sql.append(" AND UPPER(T.PROCEDURE_ID) <> :procedureId ");
        params.put("procedureId", cfgProcedureDTO.getProcedureId());
      }
    }
    List<MrCfgProcedureITHardDTO> mrCfgProcedureITDTOS = getNamedParameterJdbcTemplate()
        .query(sql.toString(), params,
            BeanPropertyRowMapper.newInstance(MrCfgProcedureITHardDTO.class));
    if (mrCfgProcedureITDTOS != null && !mrCfgProcedureITDTOS.isEmpty()) {
      return mrCfgProcedureITDTOS;
    }
    return null;
  }

  public List<MrITHardScheduleDTO> getScheduleInProcedureITHard(
      MrITHardScheduleDTO mrITHardScheduleDTO) {
    List<MrITHardScheduleDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_HARD,
          "get-list-schedule-by-procedureId"));
      if (mrITHardScheduleDTO != null) {
        if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getProcedureId())) {
          sql.append(" AND T1.PROCEDURE_ID = :procedureId ");
          listParams.put("procedureId", mrITHardScheduleDTO.getProcedureId());
        }
      }
      String sqlStr = sql.toString();
      lst = getNamedParameterJdbcTemplate().query(sqlStr, listParams,
          BeanPropertyRowMapper.newInstance(MrITHardScheduleDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

}
