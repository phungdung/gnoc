package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import com.viettel.gnoc.maintenance.model.MrDeviceBtsEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrDeviceBtsRepositoryImpl extends BaseRepository implements MrDeviceBtsRepository {

  @Override
  public Datatable getListMrDeviceBtsPage(MrDeviceBtsDTO mrDeviceBtsDTO) {
    BaseDto baseDto = sqlSearch(mrDeviceBtsDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mrDeviceBtsDTO.getPage(), mrDeviceBtsDTO.getPageSize(),
        MrDeviceBtsDTO.class,
        mrDeviceBtsDTO.getSortName(), mrDeviceBtsDTO.getSortType());
  }

  @Override
  public List<MrDeviceBtsDTO> getListMrDeviceBtsDTO(MrDeviceBtsDTO mrDeviceBtsDTO) {
    BaseDto baseDto = sqlSearch(mrDeviceBtsDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrDeviceBtsDTO mrDeviceBtsDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrDeviceBtsEntity mrDeviceBtsEntity = getEntityManager().merge(mrDeviceBtsDTO.toEntity());
    resultInSideDto.setId(mrDeviceBtsEntity.getDeviceId());
    return resultInSideDto;
  }

  @Override
  public String UpdateListDeviceBts(List<MrDeviceBtsDTO> lstMrDeviceBts) {
    if (lstMrDeviceBts != null && lstMrDeviceBts.size() > 0) {
      for (MrDeviceBtsDTO dto : lstMrDeviceBts) {
        insertOrUpdate(dto);
      }
      return RESULT.SUCCESS;
    }
    return RESULT.ERROR;
  }

  @Override
  public ResultInSideDto delete(Long deviceId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrDeviceBtsEntity mrDeviceBtsEntity = getEntityManager()
        .find(MrDeviceBtsEntity.class, deviceId);
    getEntityManager().remove(mrDeviceBtsEntity);
    return resultInSideDto;
  }

  @Override
  public MrDeviceBtsDTO getDetail(Long deviceId) {
    MrDeviceBtsDTO mrDeviceBtsDTO = new MrDeviceBtsDTO();
    mrDeviceBtsDTO.setDeviceId(deviceId);
    BaseDto baseDto = sqlSearch(mrDeviceBtsDTO);
    List<MrDeviceBtsDTO> dtoList = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));
    if (dtoList != null && dtoList.size() > 0) {
      return dtoList.get(0);
    }
    return null;
  }

  @Override
  public List<MrDeviceBtsDTO> getListDeviceType() {
    String sqlQuery = " SELECT DISTINCT DEVICE_TYPE deviceType FROM MR_DEVICE_BTS ";
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));
  }

  @Override
  public List<MrDeviceBtsDTO> getListfuelTypeByDeviceType(String deviceType, String marketCode) {
    String sqlQuery = " SELECT DISTINCT FUEL_TYPE fuelType FROM MR_DEVICE_BTS WHERE 1=1 AND FUEL_TYPE IS NOT NULL ";
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(deviceType)) {
      sqlQuery += " AND DEVICE_TYPE =:deviceType ";
      parameters.put("deviceType", deviceType);
    }

    if (!StringUtils.isStringNullOrEmpty(marketCode)) {
      sqlQuery += " AND MARKET_CODE =:marketCode ";
      parameters.put("marketCode", marketCode);
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));
  }

  @Override
  public List<MrDeviceBtsDTO> getListProducerByDeviceType(String deviceType, String marketCode) {
    String sqlQuery = " SELECT DISTINCT T1.PRODUCER producer FROM MR_DEVICE_BTS T1 WHERE 1 = 1 AND T1.PRODUCER IS NOT NULL ";
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(deviceType)) {
      sqlQuery += " AND DEVICE_TYPE =:deviceType ";
      parameters.put("deviceType", deviceType);
    }

    if (!StringUtils.isStringNullOrEmpty(marketCode)) {
      sqlQuery += " AND MARKET_CODE =:marketCode ";
      parameters.put("marketCode", marketCode);
    }
    sqlQuery += " ORDER BY producer ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));
  }

  @Override
  public List<MrDeviceBtsDTO> getListProvince(String marketCode) {
    String sqlQuery = " SELECT DISTINCT T1.PROVINCE_CODE provinceCode,T1.PROVINCE_NAME provinceName FROM MR_DEVICE_BTS T1 WHERE 1=1 ";
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(marketCode)) {
      sqlQuery += " AND MARKET_CODE =:marketCode ";
      parameters.put("marketCode", marketCode);
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));
  }

  @Override
  public List<MrDeviceBtsDTO> getListMrDeviceBtsForCD(MrDeviceBtsDTO mrDeviceBtsDTO)
      throws Exception {
    String sqlQuery =
        SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_BTS, "get-list-MrDeviceBts-for-CD");
    Map<String, Object> parameters = new HashMap<>();
    if (mrDeviceBtsDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getMarketCode())) {
        sqlQuery += " AND T1.MARKET_CODE =:marketCode ";
        parameters
            .put("marketCode", mrDeviceBtsDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getProvinceCode())) {
        sqlQuery += " AND T1.PROVINCE_CODE =:provinceCode ";
        parameters
            .put("provinceCode", mrDeviceBtsDTO.getProvinceCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getDeviceType())) {
        sqlQuery += " AND T1.DEVICE_TYPE =:deviceType ";
        parameters
            .put("deviceType", mrDeviceBtsDTO.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getFuelType())) {
        sqlQuery += " and T1.FUEL_TYPE =:fuelType ";
        parameters
            .put("fuelType", mrDeviceBtsDTO.getFuelType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getAreaCode())) {
        sqlQuery += " and T1.AREA_CODE) =:areaCode ";
        parameters
            .put("areaCode", mrDeviceBtsDTO.getAreaCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getSerial())) {
        sqlQuery += " and LOWER(T1.SERIAL) like :serial escape '\\' ";
        parameters
            .put("serial",
                StringUtils.convertLowerParamContains(mrDeviceBtsDTO.getSerial()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getStationCode())) {
        sqlQuery += " and LOWER(T1.STATION_CODE) like :stationCode escape '\\' ";
        parameters
            .put("stationCode",
                StringUtils.convertLowerParamContains(mrDeviceBtsDTO.getStationCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getProducer())) {
        sqlQuery += " and LOWER(T1.PRODUCER) like :producer escape '\\' ";
        parameters
            .put("producer",
                StringUtils.convertLowerParamContains(mrDeviceBtsDTO.getProducer()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getPower())) {
        sqlQuery += " and T1.POWER =:power ";
        parameters
            .put("power", mrDeviceBtsDTO.getPower());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getMaintenanceTimeFrom())) {
        sqlQuery += " AND T1.MAINTENANCE_TIME >= TO_DATE(':maintenanceTimeFrom','dd/MM/yyyy HH24:mi:ss') ";
        parameters
            .put("maintenanceTimeFrom", mrDeviceBtsDTO.getMaintenanceTimeFrom());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getMaintenanceTimeTo())) {
        sqlQuery += " AND T1.MAINTENANCE_TIME <= TO_DATE(':maintenanceTimeTo','dd/MM/yyyy HH24:mi:ss') ";
        parameters
            .put("maintenanceTimeTo", mrDeviceBtsDTO.getMaintenanceTimeTo());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getOperationHour())) {
        sqlQuery += " AND T1.OPERATION_HOUR =:operationHour ";
        parameters
            .put("operationHour", mrDeviceBtsDTO.getOperationHour());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getPutStatus())) {
        sqlQuery += " AND T1.PUT_STATUS =:putStatus ";
        parameters
            .put("putStatus", mrDeviceBtsDTO.getPutStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getInKTTS())) {
        sqlQuery += " AND T1.IN_KTTS =:inKTTS ";
        parameters
            .put("inKTTS", mrDeviceBtsDTO.getInKTTS());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO dto) {
    String sqlQuery = " select distinct wo_code woCode from open_pm.MR_SCHEDULE_BTS_HIS_DETAIL "
        + " where serial = :serial and device_type = :deviceType and cycle =:cycle ";
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getWoCode())) {
        sqlQuery += " and (wo_code_original = :woCode or wo_code = :woCode) ";
        parameters
            .put("woCode", dto.getWoCode());
      }
    }
    sqlQuery += " order by wo_code desc ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailInsiteDTO.class));
  }

  @Override
  public List<MrDeviceBtsDTO> onSearchExport(MrDeviceBtsDTO mrDeviceBtsDTO) {
    BaseDto baseDto = sqlSearch(mrDeviceBtsDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));
  }

  @Override
  public String updateMaintenanceTimeMrDeviceBts(List<MrDeviceBtsDTO> lstDevices) {
    if (lstDevices != null && lstDevices.size() > 0) {
      for (MrDeviceBtsDTO dto : lstDevices) {
        MrDeviceBtsEntity mrDeviceBtsEntity = getEntityManager()
            .find(MrDeviceBtsEntity.class, dto.getDeviceId());
        mrDeviceBtsEntity
            .setMaintenanceTime(StringUtils.isStringNullOrEmpty(dto.getMaintenanceTime())? null : DateTimeUtils.convertStringToDate(dto.getMaintenanceTime()));
        mrDeviceBtsEntity.setMarketCode(dto.getMarketCode());
        mrDeviceBtsEntity.setDeviceType(dto.getDeviceType());
        mrDeviceBtsEntity.setSerial(dto.getSerial());
        getEntityManager().merge(mrDeviceBtsEntity);
      }
      return RESULT.SUCCESS;
    } else {
      return RESULT.ERROR;
    }

  }

  public BaseDto sqlSearch(MrDeviceBtsDTO mrDeviceBtsDTO) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_BTS, "get-list-MrDeviceBTS-page");
    parameters.put("deviceTypeDH", I18n.getLanguage("mrDeviceBts.list.deviceType.DH"));
    parameters.put("deviceTypeMPD", I18n.getLanguage("mrDeviceBts.list.deviceType.MPD"));

    parameters.put("putStatus0", I18n.getLanguage("mrDeviceBts.list.putStatus.0"));
    parameters.put("putStatus1", I18n.getLanguage("mrDeviceBts.list.putStatus.1"));
    parameters.put("putStatus2", I18n.getLanguage("mrDeviceBts.list.putStatus.2"));

    parameters.put("inKTTS0", I18n.getLanguage("mrDeviceBts.list.inKTTS.0"));
    parameters.put("inKTTS1", I18n.getLanguage("mrDeviceBts.list.inKTTS.1"));

    //voi deviceType = "DH"
    parameters.put("fuelTypeX", I18n.getLanguage("mrDeviceBts.list.fuelType.X"));
    parameters.put("fuelTypeR410a", I18n.getLanguage("mrDeviceBts.list.fuelType.R410a"));
    parameters.put("fuelTypeR410A", I18n.getLanguage("mrDeviceBts.list.fuelType.R410A"));
    parameters.put("fuelTypeR32", I18n.getLanguage("mrDeviceBts.list.fuelType.R32"));
    parameters.put("fuelType0", I18n.getLanguage("mrDeviceBts.list.fuelType.0"));

    //voi deviceType = "MPD"
    parameters.put("fuelTypeXX", I18n.getLanguage("mrDeviceBts.list.fuelType.XX"));
    parameters.put("fuelTypeD", I18n.getLanguage("mrDeviceBts.list.fuelType.D"));

    if (mrDeviceBtsDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrDeviceBtsDTO.getSearchAll())) {
        sqlQuery += " AND ( LOWER(T2.WO_CODE) like:searchAll ESCAPE '\\' OR LOWER(T1.STATION_CODE) like:searchAll ESCAPE '\\' OR LOWER(T1.SERIAL) like:searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(mrDeviceBtsDTO.getSearchAll()));
      }

      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getMarketCode())) {
        sqlQuery += " AND T1.MARKET_CODE =:marketCode ";
        parameters
            .put("marketCode", mrDeviceBtsDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getProvinceCode())) {
        sqlQuery += " AND T1.PROVINCE_CODE =:provinceCode ";
        parameters
            .put("provinceCode", mrDeviceBtsDTO.getProvinceCode());
      }

      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getAreaCode())) {
        sqlQuery += " AND T1.AREA_CODE =:areaCode ";
        parameters
            .put("areaCode", mrDeviceBtsDTO.getAreaCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getStationCode())) {
        sqlQuery += " AND LOWER(T1.STATION_CODE) like :stationCode escape '\\' ";
        parameters
            .put("stationCode",
                StringUtils.convertLowerParamContains(mrDeviceBtsDTO.getStationCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getWoCode())) {
        sqlQuery += " AND LOWER(T2.WO_CODE) like :woCode escape '\\' ";
        parameters
            .put("woCode",
                StringUtils.convertLowerParamContains(mrDeviceBtsDTO.getWoCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getUserManager())) {
        sqlQuery += " AND LOWER(T1.USER_MANAGER) like :userManager escape '\\' ";
        parameters
            .put("userManager",
                StringUtils.convertLowerParamContains(mrDeviceBtsDTO.getUserManager()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getDeviceType())) {
        sqlQuery += " AND T1.DEVICE_TYPE =:deviceType ";
        parameters
            .put("deviceType", mrDeviceBtsDTO.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getSerial())) {
        sqlQuery += " AND LOWER(T1.SERIAL) like :serial escape '\\' ";
        parameters
            .put("serial",
                StringUtils.convertLowerParamContains(mrDeviceBtsDTO.getSerial()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceBtsDTO.getDeviceId())) {
        sqlQuery += " AND T1.DEVICE_ID =:deviceId";
        parameters
            .put("deviceId", mrDeviceBtsDTO.getDeviceId());
      }

    }
    sqlQuery += " ORDER BY T1.MARKET_CODE,T1.PROVINCE_CODE, T1.STATION_CODE, T1.DEVICE_TYPE, T1.MAINTENANCE_TIME DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoBts(String woCode) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_BTS, "get-list-WO-bts");
      Map<String, Object> params = new HashMap<>();
      if (!StringUtils.isStringNullOrEmpty(woCode)) {
        sql += " AND t1.WO_CODE = :woCode ";
        params.put("woCode", woCode);
      }

      sql += " ORDER BY t1.CHECKLIST_ID ";

      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailInsiteDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MrScheduleBtsHisFileDTO> getListFileByCheckListWo(String chedklistId, String woId) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_BTS, "get-List-File-By-CheckListWo");
      Map<String, Object> params = new HashMap<>();
      if (!StringUtils.isStringNullOrEmpty(chedklistId)) {
        sql += " AND T1.CHECK_LIST_ID = :chedklistId ";
        params.put("chedklistId", chedklistId);
      }
      if (!StringUtils.isStringNullOrEmpty(woId)) {
        sql += " AND T1.WO_ID = :woId ";
        params.put("woId", woId);
      }

      sql += " ORDER BY t1.CHECK_LIST_ID ";

      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrScheduleBtsHisFileDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public String updateStatusTask(List<MrScheduleBtsHisDetailInsiteDTO> mrScheduleBtsHisDetailDTO) {
    for (MrScheduleBtsHisDetailInsiteDTO dto : mrScheduleBtsHisDetailDTO) {
      if (StringUtils.isStringNullOrEmpty(dto.getTaskStatus())) {
        return "Not allow parameters empty";
      }
      try {
        Map<String, Object> parameters = new HashMap<>();
        String sqlQuery = " UPDATE MR_SCHEDULE_BTS_HIS_DETAIL "
            + " SET TASK_STATUS =:taskStatus "
            + " WHERE WO_CODE =:woCode AND CHECKLIST_ID =:checkListId AND SERIAL =:serial ";
        if (!StringUtils.isStringNullOrEmpty(dto.getTaskStatus())) {
          parameters.put("taskStatus", dto.getTaskStatus());
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getWoCode())) {
          parameters.put("woCode", dto.getWoCode());
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getCheckListId())) {
          parameters.put("checkListId", dto.getCheckListId());
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getSerial())) {
          parameters.put("serial", dto.getSerial());
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public List<MrDeviceBtsDTO> getListSupplierBtsByDeviceType(String deviceType, String marketCode) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_BTS,
            "get-List-Supplier-Bts-By-Device-Type");
    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(deviceType)) {
      sql += " AND d.DEVICE_TYPE = :deviceType";
      params.put("deviceType", deviceType);
    }
    if (StringUtils.isNotNullOrEmpty(marketCode)) {
      sql += " AND d.MARKET_CODE = :marketCode";
      params.put("marketCode", marketCode);
    }
    sql += " ORDER BY d.PRODUCER";
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));
  }

  @Override
  public List<MrDeviceBtsDTO> getListMrDeviceBtsByListId(List<Long> lstIds) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_BTS, "get-list-device-by-ids");
    Map<String, Object> params = new HashMap<>();
    params.put("lstId", lstIds);
    return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));
  }

}
