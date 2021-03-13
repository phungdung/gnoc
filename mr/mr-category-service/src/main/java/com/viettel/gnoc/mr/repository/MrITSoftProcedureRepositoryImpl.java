package com.viettel.gnoc.mr.repository;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrITSoftProcedureDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.model.MrITSoftProcedureEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrITSoftProcedureRepositoryImpl extends BaseRepository implements
    MrITSoftProcedureRepository {

  @Override
  public Datatable getListMrSoftITProcedure(MrITSoftProcedureDTO mrITSoftProcedureDTO) {
    BaseDto baseDto = sqlSearch(mrITSoftProcedureDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrITSoftProcedureDTO.getPage(),
        mrITSoftProcedureDTO.getPageSize(), MrITSoftProcedureDTO.class,
        mrITSoftProcedureDTO.getSortName(), mrITSoftProcedureDTO.getSortType());
  }

  public BaseDto sqlSearch(MrITSoftProcedureDTO mrITSoftProcedureDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_PROCEDURE_IT_SOFT,
            "get-list-mr-it-soft-procedure");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("leeLocale", I18n.getLocale());
    if (mrITSoftProcedureDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrITSoftProcedureDTO.getSearchAll())) {
        sql += " AND ( lower(CP.PROCEDURE_NAME) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(mrITSoftProcedureDTO.getSearchAll()));
      }

      if (!StringUtils.isStringNullOrEmpty(mrITSoftProcedureDTO.getMarketCode())) {
        sql += (" AND  CP.MARKET_CODE =:marketCode ");
        parameters.put("marketCode", mrITSoftProcedureDTO.getMarketCode());
      }

      if (StringUtils.isNotNullOrEmpty(mrITSoftProcedureDTO.getArrayCode())) {
        sql += " AND ( lower( CP.ARRAY_CODE) LIKE :arrayCode ESCAPE '\\' )";
        parameters
            .put("arrayCode",
                StringUtils.convertLowerParamContains(mrITSoftProcedureDTO.getArrayCode()));
      }

      if (StringUtils.isNotNullOrEmpty(mrITSoftProcedureDTO.getProcedureName())) {
        sql += " AND ( lower(CP.PROCEDURE_NAME) LIKE :procedureName ESCAPE '\\' )";
        parameters
            .put("procedureName",
                StringUtils.convertLowerParamContains(mrITSoftProcedureDTO.getProcedureName()));
      }

      if (!StringUtils.isStringNullOrEmpty(mrITSoftProcedureDTO.getStatus())) {
        sql += (" AND   CP.STATUS =:status ");
        parameters.put("status", mrITSoftProcedureDTO.getStatus());
      }
    }
    sql += " ORDER BY CP.MARKET_CODE,CP.DEVICE_TYPE ";
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public MrITSoftProcedureDTO getDetail(Long procedureId) {
    MrITSoftProcedureEntity mrITSoftProcedureEntity = getEntityManager()
        .find(MrITSoftProcedureEntity.class, procedureId);
    MrITSoftProcedureDTO mrITSoftProcedureDTO = mrITSoftProcedureEntity.toDTO();
    return mrITSoftProcedureDTO;
  }


  @Override
  public ResultInSideDto insertOrUpdate(MrITSoftProcedureDTO mrITSoftProcedureDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrITSoftProcedureEntity mrITSoftProcedureEntity = getEntityManager()
        .merge(mrITSoftProcedureDTO.toModel());
    resultInSideDto.setId(mrITSoftProcedureEntity.getProcedureId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long procedureId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrITSoftProcedureEntity mrITSoftProcedureEntity = getEntityManager()
        .find(MrITSoftProcedureEntity.class, procedureId);
    getEntityManager().remove(mrITSoftProcedureEntity);
    return resultInSideDto;
  }

  public List<MrITSoftScheduleDTO> getScheduleInProcedureITSoft(
      MrITSoftScheduleDTO mrITSoftScheduleDTO) {
    List<MrITSoftScheduleDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_SOFT,
          "get-list-schedule-by-procedureId"));
      if (mrITSoftScheduleDTO != null) {
        if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getProcedureId())) {
          sql.append(" AND T1.PROCEDURE_ID = :procedureId ");
          listParams.put("procedureId", mrITSoftScheduleDTO.getProcedureId());

        }
      }
      ;
      String sqlStr = sql.toString();
      lst = getNamedParameterJdbcTemplate().query(sqlStr, listParams,
          BeanPropertyRowMapper.newInstance(MrITSoftScheduleDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<MrITSoftProcedureDTO> getDataExport(MrITSoftProcedureDTO dto) {
    BaseDto baseDto = sqlSearchDataExxport(dto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrITSoftProcedureDTO.class));
  }

  public BaseDto sqlSearchDataExxport(MrITSoftProcedureDTO mrITSoftProcedureDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_PROCEDURE_IT_SOFT,
            "get-list-mr-it-soft-procedure-export");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("leeLocale", I18n.getLocale());
    parameters.put("Hard", I18n.getLanguage("mrCfgProcedureITHardDTO.Hard"));
    parameters.put("Soft", I18n.getLanguage("mrCfgProcedureITHardDTO.Soft"));
    parameters.put("month", I18n.getLanguage("mrCfgProcedureITHardDTO.month"));
    parameters.put("day", I18n.getLanguage("mrCfgProcedureITHardDTO.day"));
    parameters.put("inActive", I18n.getLanguage("common.inActive"));
    parameters.put("active", I18n.getLanguage("common.active"));
    parameters.put("yesGenCr", I18n.getLanguage("mrCfgProcedureITSoft.yesGenCr"));
    parameters.put("noGenCr", I18n.getLanguage("mrCfgProcedureITSoft.noGenCr"));
    parameters.put("yesGenWo", I18n.getLanguage("mrCfgProcedureITSoft.yesGenWo"));
    parameters.put("noGenWo", I18n.getLanguage("mrCfgProcedureITSoft.noGenWo"));
    parameters.put("standard", I18n.getLanguage("mrCfgProcedureITSoft.standard"));
    parameters.put("normal", I18n.getLanguage("mrCfgProcedureITSoft.normal"));
    parameters.put("veryImportant", I18n.getLanguage("mrCfgProcedureITSoft.veryImportant"));
    parameters.put("emergency", I18n.getLanguage("mrCfgProcedureITSoft.emergency"));
    parameters.put("immediately", I18n.getLanguage("mrCfgProcedureITSoft.immediately"));
    parameters.put("medium", I18n.getLanguage("mrCfgProcedureITSoft.medium"));
    parameters.put("important", I18n.getLanguage("mrCfgProcedureITSoft.important"));
    parameters.put("high", I18n.getLanguage("mrCfgProcedureITSoft.high"));
    parameters.put("short", I18n.getLanguage("mrCfgProcedureITSoft.short"));
    if (StringUtils.isNotNullOrEmpty(mrITSoftProcedureDTO.getSearchAll())) {
      sql += " AND ( lower(CP.PROCEDURE_NAME) LIKE :searchAll ESCAPE '\\' )";
      parameters
          .put("searchAll",
              StringUtils.convertLowerParamContains(mrITSoftProcedureDTO.getSearchAll()));
    }

    if (!StringUtils.isStringNullOrEmpty(mrITSoftProcedureDTO.getMarketCode())) {
      sql += (" AND  CP.MARKET_CODE =:marketCode ");
      parameters.put("marketCode", mrITSoftProcedureDTO.getMarketCode());
    }

    if (StringUtils.isNotNullOrEmpty(mrITSoftProcedureDTO.getArrayCode())) {
      sql += " AND ( lower( CP.ARRAY_CODE) LIKE :arrayCode ESCAPE '\\' )";
      parameters
          .put("arrayCode",
              StringUtils.convertLowerParamContains(mrITSoftProcedureDTO.getArrayCode()));
    }

    if (StringUtils.isNotNullOrEmpty(mrITSoftProcedureDTO.getProcedureName())) {
      sql += " AND ( lower(CP.PROCEDURE_NAME) LIKE :procedureName ESCAPE '\\' )";
      parameters
          .put("procedureName",
              StringUtils.convertLowerParamContains(mrITSoftProcedureDTO.getProcedureName()));
    }

    if (!StringUtils.isStringNullOrEmpty(mrITSoftProcedureDTO.getStatus())) {
      sql += (" AND   CP.STATUS =:status ");
      parameters.put("status", mrITSoftProcedureDTO.getStatus());
    }
    sql += " ORDER BY CP.MARKET_CODE,CP.DEVICE_TYPE ";
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }


}
