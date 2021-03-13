package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.maintenance.model.MrScheduleBtsEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrScheduleBtsRepositoryImpl extends BaseRepository implements
    MrScheduleBtsRepository {

  @Override
  public Datatable onSearch(MrScheduleBtsDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(), dto.getPage(),
        dto.getPageSize(), MrScheduleBtsDTO.class, dto.getSortName(), dto.getSortType());
  }

  public BaseDto sqlSearch(MrScheduleBtsDTO dto) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_BTS, "on-search");
    Map<String, Object> params = new HashMap<>();

    if (!StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
      sql += " AND T1.MARKET_CODE = :marketCode ";
      params.put("marketCode", dto.getMarketCode());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getProvinceCode())) {
      String[] provincodeList = dto.getProvinceCode().split(",");
      if(provincodeList.length == 1){
        sql += " AND T1.PROVINCE_CODE LIKE :provinceCode ";
        params.put("provinceCode", provincodeList[0]);
      }else if(provincodeList.length == 2){
        sql += " AND T1.PROVINCE_CODE LIKE :provinceCode1 OR T1.PROVINCE_CODE LIKE :provinceCode2";
        params.put("provinceCode1", provincodeList[0]);
        params.put("provinceCode2", provincodeList[1]);
      }else {
        sql += " AND T1.PROVINCE_CODE LIKE :provinceCode ";
        params.put("provinceCode", dto.getProvinceCode());
      }
    }

    if (StringUtils.isNotNullOrEmpty(dto.getDeviceType())) {
      sql += " AND UPPER(T1.DEVICE_TYPE) = :deviceType ";
      params.put("deviceType", dto.getDeviceType().toUpperCase());
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getDateModifyFromSearch())) {
      sql += " AND T1.MODIFY_DATE >= TO_DATE(:dateModifyFromSearch,'dd/MM/yyyy') ";
      params.put("dateModifyFromSearch", dto.getDateModifyFromSearch());
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getDateModifyToSearch())) {
      sql += " AND T1.MODIFY_DATE <= TO_DATE(:dateModifyToSearch,'dd/MM/yyyy') ";
      params.put("dateModifyToSearch", dto.getDateModifyToSearch());
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getSerial())) {
      sql += " AND LOWER(T1.SERIAL) LIKE :serial  ESCAPE '\\' ";
      params.put("serial", StringUtils.convertLowerParamContains(dto.getSerial()));
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getCycle())) {
      sql += " AND T1.CYCLE = :cycle ";
      params.put("cycle", dto.getCycle());
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getMrCode())) {
      sql += " AND LOWER(T1.MR_CODE) LIKE :mrCode ESCAPE '\\' ";
      params.put("mrCode", StringUtils.convertLowerParamContains(dto.getMrCode()));
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getStationCode())) {
      sql += " AND LOWER(T1.STATION_CODE) LIKE :stationCode ESCAPE '\\' ";
      params.put("stationCode", StringUtils.convertLowerParamContains(dto.getStationCode()));
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getWoStatus())) {
      sql += " AND T2.STATUS = :woStatus ";
      params.put("woStatus", dto.getWoStatus());
    }


    if (!StringUtils.isStringNullOrEmpty(dto.getWoCode())) {
      sql += " AND LOWER(T1.WO_CODE) LIKE :woCode ESCAPE '\\' ";
      params.put("woCode", StringUtils.convertLowerParamContains(dto.getWoCode()));
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getIsWoOriginal())) {
      sql += " AND (T1.IS_WO_ORIGINAL = :isWoOriginal ";
      if ("1".equals(dto.getIsWoOriginal())) {
        sql += " OR T1.IS_WO_ORIGINAL IS NULL ";
      }
      sql += ") ";
      params.put("isWoOriginal", dto.getIsWoOriginal());
    }

    if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
      sql += (" AND (LOWER(T1.SERIAL) LIKE :searchAll ESCAPE '\\' )");
      params.put("searchAll", StringUtils.convertLowerParamContains(
          dto.getSearchAll()));
    }

    sql += " ORDER BY T1.MARKET_CODE, T1.PROVINCE_CODE, T1.STATION_CODE, T1.DEVICE_TYPE, T1.MODIFY_DATE DESC ";

    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }

  @Override
  public List<MrScheduleBtsDTO> onSearchExport(MrScheduleBtsDTO mrScheduleCdDTO) {
    BaseDto baseDto = sqlSearch(mrScheduleCdDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrScheduleBtsDTO.class));
  }

  @Override
  public String updateMrScheduleBts(List<MrScheduleBtsDTO> lstMrSchedule) {
    String msg = RESULT.SUCCESS;
    for (MrScheduleBtsDTO dto : lstMrSchedule) {
      msg = updateNextDateModify(dto);
    }
    return msg;
  }


  public String updateNextDateModify(MrScheduleBtsDTO dto) {
    MrScheduleBtsEntity entity = getEntityManager()
        .find(MrScheduleBtsEntity.class, Long.valueOf(dto.getScheduleId()));
    if (entity != null) {
      entity.setNextDateModify(DateTimeUtils.convertStringToDate(dto.getNextDateModify()));
      getEntityManager().merge(entity);
      return RESULT.SUCCESS;
    }
    return RESULT.FAIL;
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO dto) {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_BTS,
          "get-lst-WO-code-schedule-bts-his-detail");
      params.put("serial", dto.getSerial());
      params.put("deviceType", dto.getDeviceType());
      params.put("cycle", dto.getCycle());
      if (!StringUtils.isStringNullOrEmpty(dto.getWoCode())) {
        sql += " and (wo_code_original = :woCode or wo_code = :woCode) ";
        params.put("woCode", dto.getWoCode());
      }
      sql += " order by wo_code desc";
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailInsiteDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetailNew(
      MrScheduleBtsHisDetailInsiteDTO dto) {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_BTS,
          "get-lst-WO-code-schedule-bts-his-detail-new");
      params.put("serial", dto.getSerial());
      params.put("deviceType", dto.getDeviceType());
      params.put("cycle", dto.getCycle());
      if (!StringUtils.isStringNullOrEmpty(dto.getWoCode())) {
        sql += " and (wo_code_original = :woCode or wo_code = :woCode) ";
        params.put("woCode", dto.getWoCode());
      }
      sql += " group by wo_code ";
      sql += " order by wo_code desc";
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailInsiteDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetailNOK(
      MrScheduleBtsHisDetailInsiteDTO dto) {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_BTS,
          "get-ListWoCode-MrSchedule-Bts-His-Detail-NOK");
      params.put("serial", dto.getSerial());
      params.put("deviceType", dto.getDeviceType());
      params.put("cycle", dto.getCycle());
      if (!StringUtils.isStringNullOrEmpty(dto.getWoCode())) {
        sql += " and (wo_code_original = :woCode or wo_code = :woCode) ";
        params.put("woCode", dto.getWoCode());
      }
      sql += " order by wo_code desc";
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailInsiteDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

}
