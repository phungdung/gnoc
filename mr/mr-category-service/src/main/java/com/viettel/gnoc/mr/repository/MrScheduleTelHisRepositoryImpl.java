package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.COMMON_TRANSLATE_BUSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.maintenance.model.MrScheduleTelHisEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrScheduleTelHisRepositoryImpl extends BaseRepository implements
    MrScheduleTelHisRepository {

  @Override
  public Datatable getListMrScheduleHardHisPage(MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    BaseDto baseDto = sqlSearch(mrScheduleTelHisDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mrScheduleTelHisDTO.getPage(), mrScheduleTelHisDTO.getPageSize(),
        MrScheduleTelHisDTO.class,
        mrScheduleTelHisDTO.getSortName(), mrScheduleTelHisDTO.getSortType());
  }

  @Override
  public List<MrScheduleTelHisDTO> onSearchExport(MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    BaseDto baseDto = sqlSearch(mrScheduleTelHisDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrScheduleTelHisDTO.class));
  }

  @Override
  public ResultInSideDto insertScheduleHis(MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrScheduleTelHisEntity entity = getEntityManager().merge(mrScheduleTelHisDTO.toEntity());
    resultInSideDto.setId(entity.getMrDeviceHisId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertUpdateListScheduleHis(
      List<MrScheduleTelHisDTO> mrScheduleTelHisDTOs) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (mrScheduleTelHisDTOs != null) {
      for (MrScheduleTelHisDTO dto : mrScheduleTelHisDTOs) {
        getEntityManager().merge(dto.toEntity());
      }
    }
    return resultInSideDto;
  }

  public BaseDto sqlSearch(MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_TEL_HIS,
            "get-list-MrScheduleHardHis-page");
    parameters
        .put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    parameters
        .put("bussiness", COMMON_TRANSLATE_BUSINESS.CAT_ITEM);
    parameters
        .put("p_leeLocale", I18n.getLocale());
    parameters
        .put("categoryCode", MR_ITEM_NAME.MR_SUBCATEGORY);
    if (mrScheduleTelHisDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getSearchAll())) {
        sqlQuery +=
            " AND (LOWER(arrayCode) LIKE :searchAll ESCAPE '\\' "
                + " OR LOWER(deviceName) LIKE :searchAll ESCAPE '\\' "
                + " OR LOWER(deviceCode) LIKE :searchAll ESCAPE '\\' "
                + " OR LOWER(mrCode) LIKE :searchAll ESCAPE '\\' "
                + " OR LOWER(woId) LIKE :searchAll ESCAPE '\\' "
                + " OR LOWER(deviceType) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(mrScheduleTelHisDTO.getSearchAll()));
      }

      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getMarketCode())) {
        sqlQuery += " AND marketCode =:marketCode ";
        parameters
            .put("marketCode", mrScheduleTelHisDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getRegion())) {
        sqlQuery += " AND region =:region ";
        parameters
            .put("region", mrScheduleTelHisDTO.getRegion());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getArrayCode())) {
        sqlQuery += " AND LOWER(arrayCode) LIKE :arrayCode ESCAPE '\\' ";
        parameters
            .put("arrayCode",
                StringUtils.convertLowerParamContains(mrScheduleTelHisDTO.getArrayCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getDeviceType())) {
        sqlQuery += " AND UPPER(deviceType) LIKE :deviceType ESCAPE '\\' ";
        parameters
            .put("deviceType",
                StringUtils.convertUpperParamContains(mrScheduleTelHisDTO.getDeviceType()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getDeviceCode())) {
        sqlQuery += " AND LOWER(deviceCode) LIKE :deviceCode ESCAPE '\\' ";
        parameters
            .put("deviceCode",
                StringUtils.convertLowerParamContains(mrScheduleTelHisDTO.getDeviceCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getDeviceName())) {
        sqlQuery += " AND LOWER(deviceName) LIKE :deviceName ESCAPE '\\' ";
        parameters
            .put("deviceName",
                StringUtils.convertLowerParamContains(mrScheduleTelHisDTO.getDeviceName()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getMrCode())) {
        sqlQuery += " AND UPPER(mrCode) LIKE :mrCode ESCAPE '\\' ";
        parameters
            .put("mrCode",
                StringUtils.convertUpperParamContains(mrScheduleTelHisDTO.getMrCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getMrDateFrom())) {
        sqlQuery += " AND TO_DATE(mrDate,'dd/MM/yyyy') >= TO_DATE(:mrDateFrom, 'dd/MM/yyyy HH24:mi:ss') ";
        parameters
            .put("mrDateFrom", mrScheduleTelHisDTO.getMrDateFrom());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getMrDateTo())) {
        sqlQuery += " AND TO_DATE(mrDate,'dd/MM/yyyy') <= TO_DATE(:mrDateTo, 'dd/MM/yyyy HH24:mi:ss') ";
        parameters
            .put("mrDateTo", mrScheduleTelHisDTO.getMrDateTo());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getMrId())) {
        sqlQuery += " AND mrId =:mrId ";
        parameters
            .put("mrId", mrScheduleTelHisDTO.getMrId());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getWoId())) {
        sqlQuery += " AND woId LIKE :woId ESCAPE '\\' ";
        parameters
            .put("woId", StringUtils.convertLowerParamContains(mrScheduleTelHisDTO.getWoId()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelHisDTO.getMrWO())) {
        if ("1".equals(mrScheduleTelHisDTO.getMrWO())) {
          sqlQuery += " AND (mrId is not null and mrId <>0 or woId is not null )";
        }
      }
    }
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
