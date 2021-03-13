package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrITHisRepositoryImpl extends BaseRepository implements MrITHisRepository {

  private static final String MR_TYPE_HARD = "H";
  private static final String MR_TYPE_SOFT = "S";

  @Override
  public Datatable getListMrSoftITHis(MrScheduleITHisDTO mrScheduleITHisDTO) {
    BaseDto baseDto = sqlSearch(mrScheduleITHisDTO, MR_TYPE_SOFT);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrScheduleITHisDTO.getPage(),
        mrScheduleITHisDTO.getPageSize(), MrScheduleITHisDTO.class,
        mrScheduleITHisDTO.getSortName(), mrScheduleITHisDTO.getSortType()
    );
  }

  @Override
  public Datatable getListMrHardITHis(MrScheduleITHisDTO mrScheduleITHisDTO) {
    BaseDto baseDto = sqlSearch(mrScheduleITHisDTO, MR_TYPE_HARD);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrScheduleITHisDTO.getPage(),
        mrScheduleITHisDTO.getPageSize(), MrScheduleITHisDTO.class,
        mrScheduleITHisDTO.getSortName(), mrScheduleITHisDTO.getSortType()
    );
  }

  @Override
  public List<MrScheduleITHisDTO> getDataExportMrHardITHis(MrScheduleITHisDTO dto) {
    BaseDto baseDto = sqlSearch(dto, MR_TYPE_HARD);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrScheduleITHisDTO.class));
  }

  @Override
  public List<MrScheduleITHisDTO> getDataExport(MrScheduleITHisDTO dto) {
    BaseDto baseDto = sqlSearch(dto, MR_TYPE_SOFT);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrScheduleITHisDTO.class));
  }

  public BaseDto sqlSearch(MrScheduleITHisDTO mrScheduleITHisDTO, String type) {
    String sql = " ";
    Map<String, Object> parameters = new HashMap<>();
    if (type.equals(MR_TYPE_SOFT)) {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_IT_SOFT_HIS, "get-list-mr-it-soft-his");
      parameters.put("mrTypeName", I18n.getLanguage("mrScheduleITHisSoft.mrSoft"));
    } else {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_IT_SOFT_HIS, "get-list-mr-it-hard-his");
      parameters.put("mrTypeName", I18n.getLanguage("mrScheduleITHisHard.mrHard"));
    }
    parameters.put("lee_Locale", I18n.getLocale());
    parameters.put("state1", I18n.getLanguage("mrMngt.state.open"));
    parameters.put("state2", I18n.getLanguage("mrMngt.state.inactive_waitting"));
    parameters.put("state3", I18n.getLanguage("mrMngt.state.queue"));
    parameters.put("state4", I18n.getLanguage("mrMngt.state.active"));
    parameters.put("state5", I18n.getLanguage("mrMngt.state.inactive"));
    parameters.put("state6", I18n.getLanguage("mrMngt.state.close"));
    parameters.put("state7", I18n.getLanguage("mrMngt.state.close7"));
    parameters.put("state8", I18n.getLanguage("mrMngt.state.close8"));
    parameters.put("state9", I18n.getLanguage("mrMngt.state.close9"));
    parameters.put("state10", I18n.getLanguage("mrMngt.state.close10"));
    parameters.put("state11", I18n.getLanguage("mrMngt.state.close11"));
    parameters.put("state12", I18n.getLanguage("mrMngt.state.close12"));
    parameters.put("state13", I18n.getLanguage("mrMngt.state.close13"));
    parameters.put("state14", I18n.getLanguage("mrMngt.state.close14"));
    parameters.put("state15", I18n.getLanguage("mrMngt.state.close15"));
    parameters.put("nodeStatus0", I18n.getLanguage("mrScheduleITHisSoft.nodeStatus.notDone"));
    parameters.put("nodeStatus1", I18n.getLanguage("mrScheduleITHisSoft.nodeStatus.done"));
    if (mrScheduleITHisDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrScheduleITHisDTO.getSearchAll())) {
        sql += " AND ( lower(deviceType) LIKE :searchAll ESCAPE '\\'  or lower(deviceCode) LIKE :searchAll ESCAPE '\\'  or lower(deviceName) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(mrScheduleITHisDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getMrArrayType())) {
        sql += (" AND mrArrayType =:mrArrayType ");
        parameters.put("mrArrayType", mrScheduleITHisDTO.getMrArrayType());
      }

      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getMarketCode())) {
        sql += (" AND  marketCode =:marketCode ");
        parameters.put("marketCode", mrScheduleITHisDTO.getMarketCode());
      }

      if (StringUtils.isNotNullOrEmpty(mrScheduleITHisDTO.getArrayCode())) {
        sql += " AND ( lower( arrayCode) LIKE :arrayCode ESCAPE '\\' )";
        parameters
            .put("arrayCode",
                StringUtils.convertLowerParamContains(mrScheduleITHisDTO.getArrayCode()));
      }

      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getDeviceType())) {
        sql += (" AND lower(deviceType) LIKE :deviceType ESCAPE '\\' ");
        parameters
            .put("deviceType",
                StringUtils.convertLowerParamContains(mrScheduleITHisDTO.getDeviceType()));
      }

      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getDeviceCode())) {
        sql += (" AND (lower(deviceCode) LIKE :deviceCode ESCAPE '\\' ) ");
        parameters
            .put("deviceCode",
                StringUtils.convertLowerParamContains(mrScheduleITHisDTO.getDeviceCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getDeviceName())) {
        sql += (" AND (lower(deviceName) LIKE :deviceName ESCAPE '\\' ) ");
        parameters
            .put("deviceName",
                StringUtils.convertLowerParamContains(mrScheduleITHisDTO.getDeviceName()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getMrDateFrom())) {
        sql += ("  AND mrDate >= TO_DATE(:mrDateFrom, 'dd/MM/yyyy') ");
        parameters.put("mrDateFrom", mrScheduleITHisDTO.getMrDateFrom());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getMrDateTo())) {
        sql += (" AND mrDate <= TO_DATE(:mrDateTo, 'dd/MM/yyyy') ");
        parameters.put("mrDateTo", mrScheduleITHisDTO.getMrDateTo());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getMrCode())) {
        sql += (" AND (lower(mrCode) LIKE :mrCode ESCAPE '\\' ) ");
        parameters
            .put("mrCode",
                StringUtils.convertLowerParamContains(mrScheduleITHisDTO.getMrCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getCrId())) {
        sql += (" AND crId LIKE :crId ESCAPE '\\' ");
        parameters
            .put("crId",
                StringUtils.convertLowerParamContains(mrScheduleITHisDTO.getCrId()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getCrNumber())) {
        sql += (" AND (lower(crNumber) LIKE :crNumber ESCAPE '\\' ) ");
        parameters
            .put("crNumber",
                StringUtils.convertLowerParamContains(mrScheduleITHisDTO.getCrNumber()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getWoId())) {
        sql += (" AND woId LIKE :woId ESCAPE '\\' ");
        parameters
            .put("woId",
                StringUtils.convertLowerParamContains(mrScheduleITHisDTO.getWoId()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleITHisDTO.getProcedureName())) {
        sql += (" AND (lower(procedureName) LIKE :procedureName ESCAPE '\\' ) ");
        parameters
            .put("procedureName",
                StringUtils.convertLowerParamContains(mrScheduleITHisDTO.getProcedureName()));
      }
      if (mrScheduleITHisDTO.getCheckMrCr() != null && mrScheduleITHisDTO.getCheckMrCr()) {
        sql += " AND (mrId is not null or crId is not null) ";
      }
    }

    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public ResultInSideDto insertUpdateListScheduleHis(List<MrScheduleITHisDTO> mrScheduleITHisDTOs) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (mrScheduleITHisDTOs != null) {
      for (MrScheduleITHisDTO dto : mrScheduleITHisDTOs) {
        getEntityManager().merge(dto.toEntity());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertList(List<MrScheduleITHisDTO> lst) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    for (MrScheduleITHisDTO mrScheduleITHisDTO : lst) {
      getEntityManager().merge(mrScheduleITHisDTO.toEntity());
    }

    return resultInSideDto;
  }
}
