package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHardGroupConfigDTO;
import com.viettel.gnoc.maintenance.model.MrHardGroupConfigEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrHardGroupConfigRepositoryImpl extends BaseRepository implements
    MrHardGroupConfigRepository {


  @Override
  public Datatable getListMrHardGroupConfigDTO(MrHardGroupConfigDTO mrHardGroupConfigDTO) {
    BaseDto baseDto = sqlSearch(mrHardGroupConfigDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mrHardGroupConfigDTO.getPage(), mrHardGroupConfigDTO.getPageSize(),
        MrHardGroupConfigDTO.class,
        mrHardGroupConfigDTO.getSortName(), mrHardGroupConfigDTO.getSortType());
  }

  @Override
  public ResultInSideDto add(MrHardGroupConfigDTO mrHardGroupConfigDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrHardGroupConfigEntity mrHardGroupConfigEntity = getEntityManager()
        .merge(mrHardGroupConfigDTO.toEntity());
    resultInSideDto.setId(mrHardGroupConfigEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto edit(MrHardGroupConfigDTO mrHardGroupConfigDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(mrHardGroupConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public MrHardGroupConfigDTO getDetail(Long id) {
    MrHardGroupConfigEntity mrHardGroupConfigEntity = getEntityManager()
        .find(MrHardGroupConfigEntity.class, id);
    MrHardGroupConfigDTO mrHardGroupConfigDTO = mrHardGroupConfigEntity.toDTO();
    return mrHardGroupConfigDTO;
  }

  @Override
  public MrHardGroupConfigDTO findMrHardGroupConfigById(Long id) {
    MrHardGroupConfigEntity entity = getEntityManager().find(MrHardGroupConfigEntity.class, id);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrHardGroupConfigEntity mrHardGroupConfigEntity = getEntityManager()
        .find(MrHardGroupConfigEntity.class, id);
    if (mrHardGroupConfigEntity != null) {
      getEntityManager().remove(mrHardGroupConfigEntity);
    }
    return resultInSideDto;
  }

  @Override
  public List<MrHardGroupConfigDTO> getListDataExport(
      MrHardGroupConfigDTO mrHardGroupConfigDTO) {
    BaseDto baseDto = sqlSearch(mrHardGroupConfigDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrHardGroupConfigDTO.class));
  }

  @Override
  public List<MrDeviceDTO> getListRegionByMarketCode(String marketCode) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_GROUP_CONFIG,
        "getListRegionByMarketCode");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(marketCode)) {
      sql += " AND MARKET_CODE = :marketCode ";
      parameters.put("marketCode", marketCode);
    }
    sql += " ORDER BY REGION_HARD ";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
  }

  @Override
  public List<MrDeviceDTO> getListNetworkTypeByArrayCode(String arrayCode) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_GROUP_CONFIG,
        "getListNetworkTypeByArrayCode");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(arrayCode)) {
      sql += " AND ARRAY_CODE = :arrayCode ";
      parameters.put("arrayCode", arrayCode);
    }
    sql += " ORDER BY NETWORK_TYPE ";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
  }


  @Override
  public MrHardGroupConfigDTO ckeckMrHardGroupConfigExist(
      MrHardGroupConfigDTO mrHardGroupConfigDTO) {
    List<MrHardGroupConfigEntity> lstMrHardGroupConfig = findByMultilParam(
        MrHardGroupConfigEntity.class, "marketCode", mrHardGroupConfigDTO.getMarketCode()
        , "region", mrHardGroupConfigDTO.getRegion()
        , "arrayCode", mrHardGroupConfigDTO.getArrayCode()
        , "networkType", mrHardGroupConfigDTO.getNetworkType()
        , "deviceType", mrHardGroupConfigDTO.getDeviceType()
//        , "cdIdHard", mrHardGroupConfigDTO.getCdIdHard()
        , "stationCode", mrHardGroupConfigDTO.getStationCode());
    if (lstMrHardGroupConfig != null && !lstMrHardGroupConfig.isEmpty()) {
      return lstMrHardGroupConfig.get(0).toDTO();
    }
    return null;
  }

  public BaseDto sqlSearch(MrHardGroupConfigDTO mrHardGroupConfigDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery;
    sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_GROUP_CONFIG,
            "getListMrHardGroupConfigDTO");
    Map<String, Object> parameters = new HashMap<>();
    if (mrHardGroupConfigDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrHardGroupConfigDTO.getMarketCode())) {
        sqlQuery += " AND m.MARKET_CODE = :marketCode ";
        parameters.put("marketCode", mrHardGroupConfigDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardGroupConfigDTO.getRegion())) {
        sqlQuery += " AND m.REGION = :region ";
        parameters.put("region", mrHardGroupConfigDTO.getRegion());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardGroupConfigDTO.getArrayCode())) {
        sqlQuery += " AND m.ARRAY_CODE = :arrayCode ";
        parameters.put("arrayCode", mrHardGroupConfigDTO.getArrayCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardGroupConfigDTO.getNetworkType())) {
        sqlQuery += " AND m.NETWORK_TYPE = :networkType ";
        parameters.put("networkType", mrHardGroupConfigDTO.getNetworkType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardGroupConfigDTO.getDeviceType())) {
        sqlQuery += " AND m.DEVICE_TYPE = :deviceType ";
        parameters.put("deviceType", mrHardGroupConfigDTO.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardGroupConfigDTO.getCdIdHard())) {
        sqlQuery += " AND m.CD_ID_HARD = :cdIdHard ";
        parameters.put("cdIdHard", mrHardGroupConfigDTO.getCdIdHard());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardGroupConfigDTO.getStationCode())) {
        sqlQuery += " AND m.STATION_CODE = :stationCode ";
        parameters.put("stationCode", mrHardGroupConfigDTO.getStationCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardGroupConfigDTO.getUpdateUser())) {
        sqlQuery += " and LOWER(m.UPDATE_USER) like :updateUser escape '\\' ";
        parameters
            .put("updateUser",
                StringUtils.convertLowerParamContains(mrHardGroupConfigDTO.getUpdateUser()));
      }
    }
    sqlQuery += " ORDER BY m.UPDATE_DATE DESC ";
    parameters.put("p_leeLocale", I18n.getLocale());
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<MrDeviceDTO> getListRegion() {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_GROUP_CONFIG,
        "getListRegion");
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
  }
}
