package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.maintenance.model.MrEntity;
import com.viettel.gnoc.maintenance.model.MrITSoftScheduleEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrITSoftScheduleRepositoryImpl extends BaseRepository implements
    MrITSoftScheduleRepository {

  @Override
  public Datatable getListMrSchedule(MrITSoftScheduleDTO mrITSoftScheduleDTO) {
    BaseDto baseDto = sqlSearch(mrITSoftScheduleDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mrITSoftScheduleDTO.getPage(), mrITSoftScheduleDTO.getPageSize(),
        MrITSoftScheduleDTO.class,
        mrITSoftScheduleDTO.getSortName(), mrITSoftScheduleDTO.getSortType());
  }

  @Override
  public MrITSoftScheduleDTO getDetail(Long scheduleId) {
    MrITSoftScheduleDTO mrITSoftScheduleDTO = new MrITSoftScheduleDTO();
    mrITSoftScheduleDTO.setScheduleId(String.valueOf(scheduleId));
    if (scheduleId != null) {
      BaseDto baseDto = sqlSearch(mrITSoftScheduleDTO);
      List<MrITSoftScheduleDTO> lst = getNamedParameterJdbcTemplate()
          .query(baseDto.getSqlQuery(), baseDto.getParameters(),
              BeanPropertyRowMapper.newInstance(MrITSoftScheduleDTO.class));
      return lst.isEmpty() ? null : lst.get(0);
    }
    return null;
  }

  @Override
  public List<MrITSoftScheduleDTO> findListMrScheduleByGroupCode(String groupCode) {
    if (StringUtils.isNotNullOrEmpty(groupCode)) {
      String sql = "";
      Map<String, Object> listParams = new HashMap<>();
      sql += (SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_SOFT,
          "get-list-mr-schedule-it-soft"));
      sql += (" AND MR.GROUP_CODE = :groupCode");
      sql += (" and mr.MR_ID is null and mr.CR_ID is null");
      listParams.put("groupCode", groupCode);
      listParams.put("lee_Locale", I18n.getLocale());
      return getNamedParameterJdbcTemplate().query(sql, listParams,
          BeanPropertyRowMapper.newInstance(MrITSoftScheduleDTO.class));
    }
    return null;
  }

  public ResultInSideDto updateMrEarAndLastestTime(MrInsideDTO mrDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrEntity mrEntity = getEntityManager().find(MrEntity.class, mrDTO.getMrId());
    if (mrEntity != null) {
      mrEntity.setEarliestTime(mrDTO.getEarliestTime());
      mrEntity.setLastestTime(mrDTO.getLastestTime());
      mrEntity = getEntityManager().merge(mrEntity);
      resultInSideDto.setId(mrEntity.getMrId());
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
    }
    return resultInSideDto;
  }

  @Override
  public List<MrITSoftScheduleDTO> getListMrSheduleITByIdForImport(List<String> idSearchs) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_SOFT,
            "get-list-scheduleit-for-import");
    Map<String, Object> params = new HashMap<>();
    params.put("scheduleIds", idSearchs);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrITSoftScheduleDTO.class));
  }


  @Override
  public List<MrScheduleITHisDTO> getListScheduleMoveToHis(MrSynItDevicesDTO mrSynItDevicesDTO) {

    List<MrScheduleITHisDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_SOFT,
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
  public List<MrITSoftScheduleDTO> getListScheduleMove(
      MrITSoftScheduleDTO mrITSoftScheduleDTO) {

    List<MrITSoftScheduleDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_SOFT,
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
          BeanPropertyRowMapper.newInstance(MrITSoftScheduleDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public BaseDto sqlSearch(MrITSoftScheduleDTO mrITSoftScheduleDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_SOFT, "get-list-mr-schedule-it-soft");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("lee_Locale", I18n.getLocale());
    if (mrITSoftScheduleDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getSearchAll())) {
        sqlQuery += " AND UPPER(mr.DEVICE_NAME) LIKE :searchAll ESCAPE '\\' OR UPPER(mr.DEVICE_CODE) LIKE :searchAll ESCAPE '\\' OR  UPPER(mr.IP_NODE) LIKE :searchAll ";
        parameters.put("searchAll",
            StringUtils.convertUpperParamContains(mrITSoftScheduleDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getMarketCode())) {
        sqlQuery += " AND MR.MARKET_CODE =:marketCode";
        parameters.put("marketCode", mrITSoftScheduleDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getRegion())) {
        sqlQuery += " AND mr.REGION =:region";
        parameters.put("region", mrITSoftScheduleDTO.getRegion());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getArrayCode())) {
        sqlQuery += " AND UPPER(mr.ARRAY_CODE) LIKE :arrayCode ESCAPE '\\'";
        parameters.put("arrayCode",
            StringUtils.convertUpperParamContains(mrITSoftScheduleDTO.getArrayCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getDeviceType())) {
        sqlQuery += " AND UPPER(mr.DEVICE_TYPE) LIKE :deviceType ESCAPE '\\'";
        parameters.put("deviceType",
            StringUtils.convertUpperParamContains(mrITSoftScheduleDTO.getDeviceType()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getScheduleId())) {
        sqlQuery += (" AND mr.SCHEDULE_ID =:scheduleId ");
        parameters.put("scheduleId", mrITSoftScheduleDTO.getScheduleId());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getDeviceName())) {
        sqlQuery += " AND UPPER(mr.DEVICE_NAME) LIKE :deviceName ESCAPE '\\' ";
        parameters.put("deviceName",
            StringUtils.convertUpperParamContains(mrITSoftScheduleDTO.getDeviceName()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getDateModifyFromSearch())) {
        sqlQuery += " AND MR.NEXT_DATE_MODIFY >= TO_DATE(:dateModifyFromSearch,'dd/MM/yyyy')";
        parameters.put("dateModifyFromSearch", mrITSoftScheduleDTO.getDateModifyFromSearch());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getDateModifyToSearch())) {
        sqlQuery += " AND MR.NEXT_DATE_MODIFY <= TO_DATE(:dateModifyToSearch,'dd/MM/yyyy')";
        parameters.put("dateModifyToSearch", mrITSoftScheduleDTO.getDateModifyToSearch());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getImplementUnitId())) {
        sqlQuery += " AND unit.IMPLEMENT_UNIT =:implementUnit ";
        parameters.put("implementUnit", mrITSoftScheduleDTO.getImplementUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getCheckingUnitId())) {
        sqlQuery += " AND unit.CHECKING_UNIT =:checkingUnit ";
        parameters.put("checkingUnit", mrITSoftScheduleDTO.getCheckingUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getDeviceCode())) {
        sqlQuery += " AND UPPER(mr.DEVICE_CODE) LIKE :deviceCode ESCAPE '\\'";
        parameters.put("deviceCode",
            StringUtils.convertUpperParamContains(mrITSoftScheduleDTO.getDeviceCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getIpNode())) {
        sqlQuery += " AND UPPER(mr.IP_NODE) LIKE :ipNode ";
        parameters
            .put("ipNode",
                StringUtils.convertUpperParamContains(mrITSoftScheduleDTO.getIpNode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getCycle())) {
        sqlQuery += " AND TO_CHAR(sof.CYCLE) =:cycle";
        parameters.put("cycle", mrITSoftScheduleDTO.getCycle());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getMrConfirm())) {
        sqlQuery += " AND dev.MR_CONFIRM_SOFT =:mrConfirm ";
        parameters.put("mrConfirm", mrITSoftScheduleDTO.getMrConfirm());
      }
      if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getCheckMr())) {
        if ("1".equals(mrITSoftScheduleDTO.getCheckMr())) {
          sqlQuery += " AND mr.MR_ID IS NULL";
        }
      }

    }
    sqlQuery += " ORDER BY mr.SCHEDULE_ID desc ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }


  public List<MrITSoftScheduleDTO> getListRegionByMrSynItDevices(String country) {
    List<MrITSoftScheduleDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_SOFT,
          "get-list-region"));
      listParams.put("p_country", country);
      String sqlStr = sql.toString();
      lst = getNamedParameterJdbcTemplate().query(sqlStr, listParams,
          BeanPropertyRowMapper.newInstance(MrITSoftScheduleDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<MrITSoftScheduleDTO> getDataExport(MrITSoftScheduleDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrITSoftScheduleDTO.class));
  }


  public MrITSoftScheduleDTO getUnit(MrITSoftScheduleDTO mrITSoftScheduleDTO) {
    List<MrITSoftScheduleDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_SOFT,
          "get-list-unit"));
      listParams.put("marketCode", mrITSoftScheduleDTO.getMarketName());
      listParams.put("deviceType", mrITSoftScheduleDTO.getDeviceType());
      listParams.put("region", mrITSoftScheduleDTO.getRegion());

      String sqlStr = sql.toString();
      lst = getNamedParameterJdbcTemplate().query(sqlStr, listParams,
          BeanPropertyRowMapper.newInstance(MrITSoftScheduleDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return !lst.isEmpty() ? lst.get(0) : null;
  }

  public MrITSoftScheduleDTO getNodeAffected(MrITSoftScheduleDTO mrITSoftScheduleDTO) {
    List<MrITSoftScheduleDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_SOFT,
          "get-list-node-affected"));
      listParams.put("marketCode", mrITSoftScheduleDTO.getMarketCode());
      listParams.put("deviceType", mrITSoftScheduleDTO.getDeviceType());
      listParams.put("objectId", mrITSoftScheduleDTO.getDeviceId());

      String sqlStr = sql.toString();
      lst = getNamedParameterJdbcTemplate().query(sqlStr, listParams,
          BeanPropertyRowMapper.newInstance(MrITSoftScheduleDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return !lst.isEmpty() ? lst.get(0) : null;
  }

  @Override
  public ResultInSideDto deleteListSchedule(List<MrITSoftScheduleDTO> lstScheduleDTO) {
    if (lstScheduleDTO != null && !lstScheduleDTO.isEmpty()) {
      for (MrITSoftScheduleDTO dto : lstScheduleDTO) {
        deleteMrScheduleIT(Long.valueOf(dto.getScheduleId()));
      }
    }
    return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public ResultInSideDto deleteMrScheduleIT(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrITSoftScheduleEntity entity = getEntityManager().find(MrITSoftScheduleEntity.class, id);
    if (entity != null && entity.getScheduleId() != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setId(id);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateMrSchedule(MrITSoftScheduleDTO mrITSoftScheduleDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrITSoftScheduleEntity mrITSoftScheduleEntity = getEntityManager()
        .merge(mrITSoftScheduleDTO.toEntity());
    resultInSideDto.setId(mrITSoftScheduleEntity.getScheduleId());
    return resultInSideDto;
  }

  @Override
  public MrITSoftScheduleDTO getByMrId(String mrId) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_IT_SOFT, "get-by-mr-id");
      Map<String, Object> params = new HashMap<>();
      params.put("mrId", mrId);
      List<MrITSoftScheduleDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrITSoftScheduleDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
