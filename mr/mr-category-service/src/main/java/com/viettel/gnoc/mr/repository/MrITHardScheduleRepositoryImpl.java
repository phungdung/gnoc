package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.maintenance.model.MrITHardScheduleEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrITHardScheduleRepositoryImpl extends BaseRepository implements
    MrITHardScheduleRepository {

  @Override
  public Datatable getListMrScheduleITHard(MrITHardScheduleDTO mrITHardScheduleDTO) {
    BaseDto baseDto = sqlSearch(mrITHardScheduleDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mrITHardScheduleDTO.getPage(), mrITHardScheduleDTO.getPageSize(),
        MrITHardScheduleDTO.class,
        mrITHardScheduleDTO.getSortName(), mrITHardScheduleDTO.getSortType());
  }

  public BaseDto sqlSearch(MrITHardScheduleDTO mrITHardScheduleDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_HARD, "get-list-mr-schedule-it-hard");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("lee_Locale", I18n.getLocale());
    if (mrITHardScheduleDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrITHardScheduleDTO.getSearchAll())) {
        sqlQuery += (" AND (LOWER(mr.DEVICE_CODE) LIKE :searchAll ESCAPE '\\' OR LOWER(mr.DEVICE_NAME) LIKE :searchAll ESCAPE '\\' )");
        parameters.put("searchAll", StringUtils.convertLowerParamContains(
            mrITHardScheduleDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getMarketCode())) {
        sqlQuery += " AND MR.MARKET_CODE =:marketCode";
        parameters.put("marketCode", mrITHardScheduleDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getRegion())) {
        sqlQuery += " AND mr.REGION =:region";
        parameters.put("region", mrITHardScheduleDTO.getRegion());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getArrayCode())) {
        sqlQuery += " AND UPPER(mr.ARRAY_CODE) LIKE :arrayCode ESCAPE '\\'";
        parameters.put("arrayCode",
            StringUtils.convertUpperParamContains(mrITHardScheduleDTO.getArrayCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getNetworkType())) {
        sqlQuery += (" AND mr.NETWORK_TYPE =:netWorkType ");
        parameters.put("netWorkType", mrITHardScheduleDTO.getNetworkType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getDeviceType())) {
        sqlQuery += " AND UPPER(mr.DEVICE_TYPE) LIKE :deviceType ESCAPE '\\'";
        parameters.put("deviceType",
            StringUtils.convertUpperParamContains(mrITHardScheduleDTO.getDeviceType()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getScheduleId())) {
        sqlQuery += (" AND mr.SCHEDULE_ID =:scheduleId ");
        parameters.put("scheduleId", mrITHardScheduleDTO.getScheduleId());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getDeviceName())) {
        sqlQuery += " AND UPPER(mr.DEVICE_NAME) LIKE :deviceName ESCAPE '\\' ";
        parameters.put("deviceName",
            StringUtils.convertUpperParamContains(mrITHardScheduleDTO.getDeviceName()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getDeviceCode())) {
        sqlQuery += " AND UPPER(mr.DEVICE_CODE) LIKE :deviceCode ESCAPE '\\' ";
        parameters.put("deviceCode",
            StringUtils.convertUpperParamContains(mrITHardScheduleDTO.getDeviceCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getDateModifyFromSearch())) {
        sqlQuery += " AND MR.NEXT_DATE_MODIFY >= TO_DATE(:dateModifyFromSearch,'dd/MM/yyyy')";
        parameters.put("dateModifyFromSearch", mrITHardScheduleDTO.getDateModifyFromSearch());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getDateModifyToSearch())) {
        sqlQuery += " AND MR.NEXT_DATE_MODIFY <= TO_DATE(:dateModifyToSearch,'dd/MM/yyyy')";
        parameters.put("dateModifyToSearch", mrITHardScheduleDTO.getDateModifyToSearch());
      }

      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getIpNode())) {
        sqlQuery += " AND UPPER(mr.IP_NODE) LIKE :ipNode ESCAPE '\\' ";
        parameters
            .put("ipNode",
                StringUtils.convertUpperParamContains(mrITHardScheduleDTO.getIpNode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getCycle())) {
        sqlQuery += " AND TO_CHAR(har.CYCLE) =:cycle";
        parameters.put("cycle", mrITHardScheduleDTO.getCycle());
      }

      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getCheckMr())) {
        if ("1".equals(mrITHardScheduleDTO.getCheckMr())) {
          sqlQuery += " AND mr.MR_ID IS NULL";
        }
      }

    }
    sqlQuery += " ORDER BY mr.SCHEDULE_ID desc ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public MrITHardScheduleDTO getDetail(Long scheduleId) {
    if (scheduleId != null) {
      BaseDto baseDto = sqlSearch(new MrITHardScheduleDTO(scheduleId));
      List<MrITHardScheduleDTO> lst = getNamedParameterJdbcTemplate()
          .query(baseDto.getSqlQuery(), baseDto.getParameters(),
              BeanPropertyRowMapper.newInstance(MrITHardScheduleDTO.class));
      return lst.isEmpty() ? null : lst.get(0);
    }
    return null;
  }

  @Override
  public List<MrITHardScheduleDTO> getListDataExport(MrITHardScheduleDTO mrITHardScheduleDTO) {
    BaseDto baseDto = sqlSearch(mrITHardScheduleDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrITHardScheduleDTO.class));
  }

  @Override
  public ResultInSideDto updateMrSchedule(MrITHardScheduleDTO mrITHardScheduleDTO) {
    MrITHardScheduleEntity mrITHardScheduleEntity = getEntityManager()
        .merge(mrITHardScheduleDTO.toEntity());
    return new ResultInSideDto(mrITHardScheduleEntity.getScheduleId(), RESULT.SUCCESS,
        RESULT.SUCCESS);
  }

  @Override
  public ResultInSideDto deleteMrScheduleITHard(Long mrScheduleId) {
    if (mrScheduleId != null) {
      MrITHardScheduleEntity mrITHardScheduleEntity = getEntityManager()
          .find(MrITHardScheduleEntity.class, mrScheduleId);
      if (mrITHardScheduleEntity != null) {
        getEntityManager().remove(mrITHardScheduleEntity);
        return new ResultInSideDto(mrScheduleId, RESULT.SUCCESS,
            RESULT.SUCCESS);
      } else {
        return new ResultInSideDto(mrScheduleId, RESULT.ERROR,
            RESULT.ERROR);
      }
    }
    return new ResultInSideDto(mrScheduleId, RESULT.ERROR,
        RESULT.ERROR);
  }

  @Override
  public ResultInSideDto deleteListSchedule(List<MrITHardScheduleDTO> lstMrITSoftSchedule) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    if (lstMrITSoftSchedule != null && !lstMrITSoftSchedule.isEmpty()) {
      for (MrITHardScheduleDTO mrITHardScheduleDTO : lstMrITSoftSchedule) {
        resultInSideDto = deleteMrScheduleITHard(
            mrITHardScheduleDTO.getScheduleId());
      }
    }
    return resultInSideDto;
  }

  @Override
  public List<MrITHardScheduleDTO> findListMrScheduleByGroupCode(String groupCode) {
    if (StringUtils.isNotNullOrEmpty(groupCode)) {
      List<MrITHardScheduleDTO> lst = new ArrayList<>();
      StringBuilder sql = new StringBuilder();
      try {
        Map<String, Object> listParams = new HashMap<>();
        sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_HARD,
            "get-list-mr-schedule-it-hard"));
        sql.append(" AND MR.GROUP_CODE = :groupCode");
        listParams.put("groupCode", groupCode);
        String sqlStr = sql.toString();
        lst = getNamedParameterJdbcTemplate().query(sqlStr, listParams,
            BeanPropertyRowMapper.newInstance(MrITHardScheduleDTO.class));
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      return lst;
    }
    return null;
  }

  @Override
  public List<MrITHardScheduleDTO> getListRegionByMrSynItDevices(String country) {
    List<MrITHardScheduleDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_HARD,
          "get-list-region"));
      listParams.put("p_country", country);
      String sqlStr = sql.toString();
      lst = getNamedParameterJdbcTemplate().query(sqlStr, listParams,
          BeanPropertyRowMapper.newInstance(MrITHardScheduleDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<MrITHardScheduleDTO> getListMrSheduleITByIdForImport(List<String> idSearchs) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_HARD,
            "get-list-scheduleit-for-import");
    Map<String, Object> params = new HashMap<>();
    params.put("scheduleIds", idSearchs);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrITHardScheduleDTO.class));
  }

  @Override
  public List<MrScheduleITHisDTO> getListScheduleMoveToHis(MrSynItDevicesDTO mrSynItDevicesDTO) {

    List<MrScheduleITHisDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_HARD,
          "get-list-schedule-move-to-his"));
      if (mrSynItDevicesDTO != null) {
        if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getObjectId())) {
          sql.append(" AND a.OBJECT_ID = :objectId");
          listParams.put("objectId", mrSynItDevicesDTO.getObjectId());
        }
        if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getDeviceType())) {
          sql.append(" AND a.DEVICE_TYPE = :deviceType");
          listParams.put("deviceType", mrSynItDevicesDTO.getDeviceType());
        }
        if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMarketCode())) {
          sql.append(" AND a.MARKET_CODE = :marketCode");
          listParams.put("marketCode", mrSynItDevicesDTO.getMarketCode());
        }
      }

      String sqlStr = sql.toString();
      lst = getNamedParameterJdbcTemplate().query(sqlStr, listParams,
          BeanPropertyRowMapper.newInstance(MrScheduleITHisDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<MrITHardScheduleDTO> getListScheduleMove(
      MrITHardScheduleDTO mrITSoftScheduleDTO) {

    List<MrITHardScheduleDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_HARD,
          "get-list-schedule-move-to-his"));
      if (mrITSoftScheduleDTO != null) {
        if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getDeviceId())) {
          sql.append(" AND a.DEVICE_ID = :deviceId");
          listParams.put("deviceId", mrITSoftScheduleDTO.getDeviceId());
        }
        if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getDeviceCode())) {
          sql.append(" AND a.DEVICE_CODE = :deviceCode");
          listParams.put("deviceCode", mrITSoftScheduleDTO.getDeviceCode());
        }
        if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getMarketCode())) {
          sql.append(" AND a.MARKET_CODE = :marketCode");
          listParams.put("marketCode", mrITSoftScheduleDTO.getMarketCode());
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

  @Override
  public ResultInSideDto updateMrEarAndLastestTime(MrInsideDTO mrDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      Map<String, Object> listParams = new HashMap<>();
      StringBuffer sql = new StringBuffer();
      sql.append(
          "update OPEN_PM.MR set earliest_Time = :p_earliestTime , :p_lastestTime where mr_id = :mrId");
      listParams.put("p_earliestTime", mrDTO.getEarliestTime());
      listParams.put("p_lastestTime", mrDTO.getLastestTime());
      listParams.put("mrId", mrDTO.getMrId());
      String sqlStr = sql.toString();
      int row = getNamedParameterJdbcTemplate().update(sqlStr, listParams);
      resultInSideDto.setQuantitySucc(row);
      return resultInSideDto;
    } catch (Exception e) {
    }
    return null;
  }
}
