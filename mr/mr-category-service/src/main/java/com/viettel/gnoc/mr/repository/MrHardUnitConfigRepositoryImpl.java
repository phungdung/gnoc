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
import com.viettel.gnoc.maintenance.dto.MrHardUnitConfigDTO;
import com.viettel.gnoc.maintenance.model.MrHardUnitConfigEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrHardUnitConfigRepositoryImpl extends BaseRepository implements
    MrHardUnitConfigRepository {


  @Override
  public Datatable getListMrHardGroupConfigDTO(MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    BaseDto baseDto = sqlSearch(mrHardUnitConfigDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mrHardUnitConfigDTO.getPage(), mrHardUnitConfigDTO.getPageSize(),
        MrHardUnitConfigDTO.class,
        mrHardUnitConfigDTO.getSortName(), mrHardUnitConfigDTO.getSortType());
  }

  @Override
  public ResultInSideDto add(MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrHardUnitConfigEntity mrHardUnitConfigEntity = getEntityManager()
        .merge(mrHardUnitConfigDTO.toEntity());
    resultInSideDto.setId(mrHardUnitConfigEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto edit(MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(mrHardUnitConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public MrHardUnitConfigDTO getDetail(Long id) {
    MrHardUnitConfigEntity mrHardUnitConfigEntity = getEntityManager()
        .find(MrHardUnitConfigEntity.class, id);
    MrHardUnitConfigDTO mrHardUnitConfigDTO = mrHardUnitConfigEntity.toDTO();
    return mrHardUnitConfigDTO;
  }

  @Override
  public MrHardUnitConfigDTO findMrHardGroupConfigById(Long id) {
    MrHardUnitConfigEntity entity = getEntityManager().find(MrHardUnitConfigEntity.class, id);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrHardUnitConfigEntity mrHardUnitConfigEntity = getEntityManager()
        .find(MrHardUnitConfigEntity.class, id);
    if (mrHardUnitConfigEntity != null) {
      getEntityManager().remove(mrHardUnitConfigEntity);
    }
    return resultInSideDto;
  }

  @Override
  public List<MrHardUnitConfigDTO> getListDataExport(
      MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    BaseDto baseDto = sqlSearch(mrHardUnitConfigDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrHardUnitConfigDTO.class));
  }

  @Override
  public List<MrDeviceDTO> getListRegionByMarketCode(String marketCode) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_UNIT_CONFIG,
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
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_UNIT_CONFIG,
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
  public MrHardUnitConfigDTO ckeckMrHardGroupConfigExist(
      MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    List<MrHardUnitConfigEntity> lstMrHardUnitConfig = findByMultilParam(
        MrHardUnitConfigEntity.class, "marketCode", mrHardUnitConfigDTO.getMarketCode()
        , "region", mrHardUnitConfigDTO.getRegion()
        , "arrayCode", mrHardUnitConfigDTO.getArrayCode()
        , "deviceType", mrHardUnitConfigDTO.getDeviceType()
        , "stationCode", mrHardUnitConfigDTO.getStationCode());
    if (lstMrHardUnitConfig != null && !lstMrHardUnitConfig.isEmpty()) {
      return lstMrHardUnitConfig.get(0).toDTO();
    }
    return null;
  }

  public BaseDto sqlSearch(MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery;
    sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_UNIT_CONFIG,
            "getListMrHardGroupConfigDTO");
    Map<String, Object> parameters = new HashMap<>();
    if (mrHardUnitConfigDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getMarketCode())) {
        sqlQuery += " AND m.MARKET_CODE = :marketCode ";
        parameters.put("marketCode", mrHardUnitConfigDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getRegion())) {
        sqlQuery += " AND m.REGION = :region ";
        parameters.put("region", mrHardUnitConfigDTO.getRegion());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getArrayCode())) {
        sqlQuery += " AND m.ARRAY_CODE = :arrayCode ";
        parameters.put("arrayCode", mrHardUnitConfigDTO.getArrayCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getNetworkType())) {
        sqlQuery += " AND m.NETWORK_TYPE = :networkType ";
        parameters.put("networkType", mrHardUnitConfigDTO.getNetworkType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getDeviceType())) {
        sqlQuery += " AND m.DEVICE_TYPE = :deviceType ";
        parameters.put("deviceType", mrHardUnitConfigDTO.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getCdIdHard())) {
        sqlQuery += " AND m.CD_ID_HARD = :cdIdHard ";
        parameters.put("cdIdHard", mrHardUnitConfigDTO.getCdIdHard());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getStationCode())) {
        sqlQuery += " AND m.STATION_CODE = :stationCode ";
        parameters.put("stationCode", mrHardUnitConfigDTO.getStationCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getUpdateUser())) {
        sqlQuery += " and LOWER(m.UPDATE_USER) like :updateUser escape '\\' ";
        parameters
            .put("updateUser",
                StringUtils.convertLowerParamContains(mrHardUnitConfigDTO.getUpdateUser()));
      }
    }
    sqlQuery += " ORDER BY m.UPDATE_DATE DESC ";
    parameters.put("p_leeLocale", I18n.getLocale());
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
