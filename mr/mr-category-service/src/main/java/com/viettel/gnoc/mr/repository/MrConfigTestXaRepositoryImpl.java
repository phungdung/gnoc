package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrConfigTestXaDTO;
import com.viettel.gnoc.maintenance.model.MrConfigTestXaEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrConfigTestXaRepositoryImpl extends BaseRepository implements MrConfigTestXaRepository {

  @Override
  public Datatable getListMrConfigTestXa(MrConfigTestXaDTO mrConfigTestXaDTO) {
    BaseDto baseDto = sqlSearch(mrConfigTestXaDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrConfigTestXaDTO.getPage(),
        mrConfigTestXaDTO.getPageSize(), MrConfigTestXaDTO.class,
        mrConfigTestXaDTO.getSortName(), mrConfigTestXaDTO.getSortType());
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrConfigTestXaDTO mrConfigTestXaDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrConfigTestXaEntity mrConfigTestXaEntity = getEntityManager().merge(mrConfigTestXaDTO.toEntity());
    resultInSideDto.setId(mrConfigTestXaEntity.getConfigId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrConfigTestXa(Long configId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrConfigTestXaEntity mrConfigTestXaEntity = getEntityManager().find(MrConfigTestXaEntity.class, configId);
    getEntityManager().remove(mrConfigTestXaEntity);
    return resultInSideDto;
  }

  @Override
  public MrConfigTestXaDTO getDetail(Long configId) {
    MrConfigTestXaEntity mrConfigTestXaEntity = getEntityManager().find(MrConfigTestXaEntity.class, configId);
    MrConfigTestXaDTO mrConfigTestXaDTO = mrConfigTestXaEntity.toDTO();
    return mrConfigTestXaDTO;
  }

  @Override
  public List<MrConfigTestXaDTO> checkListDTOExisted(MrConfigTestXaDTO mrConfigTestXaDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MR_CONFIG_TESTXA, "get-List-Mr-Config-TestXa");
    Map<String, Object> parameters = new HashMap<>();

    if (mrConfigTestXaDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getCountry())) {
        sql += "\nAND lower(COUNTRY) = :country";
        parameters.put("country", mrConfigTestXaDTO.getCountry());
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getProvince())) {
        sql += "\nAND lower(PROVINCE) = :province";
        parameters.put("province", mrConfigTestXaDTO.getProvince());
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getTimeTestXa())) {
        sql += "\nAND TIME_TESTXA = :timeTestXa";
        parameters.put("timeTestXa", mrConfigTestXaDTO.getTimeTestXa());
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getStationAtATime())) {
        sql += "\nAND STATION_AT_A_TIME = :stationAtATime";
        parameters.put("stationAtATime", mrConfigTestXaDTO.getStationAtATime());
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getExcepDistrict())) {
        sql += "\nAND EXCEP_DISTRICT = :excepdistrict";
        parameters.put("excepdistrict", mrConfigTestXaDTO.getExcepDistrict());
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getExcepStation())) {
        sql += "\nAND EXCEP_STATION = :excepStation";
        parameters.put("excepStation", mrConfigTestXaDTO.getExcepStation());
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getStatus())) {
        sql += "\nAND STATUS = :status";
        parameters.put("status", mrConfigTestXaDTO.getStatus());
      }
    }
    List<MrConfigTestXaDTO> listDTOS;
    listDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrConfigTestXaDTO.class));
    return listDTOS;
  }

  @Override
  public List<MrConfigTestXaDTO> getListStation() {
    String sql = "SELECT DISTINCT STATION_CODE station FROM OPEN_PM.MR_CD_BATTERY";
    List<MrConfigTestXaDTO> lstMrConfigTestXaDTO;
    lstMrConfigTestXaDTO = getNamedParameterJdbcTemplate()
        .query(sql, BeanPropertyRowMapper.newInstance(MrConfigTestXaDTO.class));
    return lstMrConfigTestXaDTO;
  }

  @Override
  public List<MrConfigTestXaDTO> getDataExport(MrConfigTestXaDTO mrConfigTestXaDTO) {
    BaseDto baseDto = sqlSearch(mrConfigTestXaDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrConfigTestXaDTO.class));
  }

  private BaseDto sqlSearch(MrConfigTestXaDTO mrConfigTestXaDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MR_CONFIG_TESTXA, "get-List-Mr-Config-TestXa");
    Map<String, Object> parameters = new HashMap<>();

    if (mrConfigTestXaDTO != null) {
      if(StringUtils.isNotNullOrEmpty((mrConfigTestXaDTO.getSearchAll()))) {
        if (!"".equalsIgnoreCase(mrConfigTestXaDTO.getSearchAll())) {
          sql += "AND LOWER(tmp_province.LOCATION_NAME) LIKE :searchAll";
          parameters.put("searchAll", StringUtils.convertLowerParamContains(
              mrConfigTestXaDTO.getSearchAll()));
        }
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getCountry())) {
        sql += "\nAND lower(COUNTRY) = :country";
        parameters.put("country", mrConfigTestXaDTO.getCountry().toLowerCase());
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getProvince())) {
        sql += "\nAND lower(PROVINCE) = :province";
        parameters.put("province", mrConfigTestXaDTO.getProvince().toLowerCase());
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getTimeTestXa())) {
        if (!"".equalsIgnoreCase(mrConfigTestXaDTO.getTimeTestXa())) {
          sql += "\nAND TIME_TESTXA = :timeTestXa";
          parameters.put("timeTestXa", mrConfigTestXaDTO.getTimeTestXa());
        }
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getStationAtATime())) {
        if (!"".equalsIgnoreCase(mrConfigTestXaDTO.getStationAtATime())) {
          sql += "\nAND STATION_AT_A_TIME = :stationAtATime";
          parameters.put("stationAtATime", mrConfigTestXaDTO.getStationAtATime());
        }
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getExcepDistrict())) {
        if (!"".equalsIgnoreCase(mrConfigTestXaDTO.getExcepDistrict())) {
          sql += "\nAND lower(EXCEP_DISTRICT) LIKE :excepdistrict";
          parameters.put(
              "excepdistrict",
              StringUtils.convertLowerParamContains(mrConfigTestXaDTO.getExcepDistrict())
          );
        }
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getExcepStation())) {
        if (!"".equalsIgnoreCase(mrConfigTestXaDTO.getExcepStation())) {
          sql += "\nAND lower(EXCEP_STATION) LIKE :excepStation";
          parameters.put(
              "excepStation",
              StringUtils.convertLowerParamContains(mrConfigTestXaDTO.getExcepStation())
          );
        }
      }

      if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getStatus())) {
        sql += "\nAND STATUS = :status";
        parameters.put("status", mrConfigTestXaDTO.getStatus());
      }
    }
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
