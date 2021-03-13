package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.maintenance.model.MrDeviceCDEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrDeviceCDRepositoryImpl extends BaseRepository implements
    MrDeviceCDRepository {

  @Override
  public List<MrDeviceCDDTO> getComboboxDeviceType() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_CD, "get-cbb-device-type");
    List<MrDeviceCDDTO> data = getNamedParameterJdbcTemplate()
        .query(sql, new HashMap<>(), BeanPropertyRowMapper.newInstance(MrDeviceCDDTO.class));
    if (data != null && data.size() > 0) {
      return data;
    }
    return new ArrayList<>();
  }

  @Override
  public List<MrDeviceCDDTO> getComboboxStationCode() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_CD, "get-cbb-station-code");
    List<MrDeviceCDDTO> data = getNamedParameterJdbcTemplate()
        .query(sql, new HashMap<>(), BeanPropertyRowMapper.newInstance(MrDeviceCDDTO.class));
    if (data != null && data.size() > 0) {
      return data;
    }
    return new ArrayList<>();
  }

  @Override
  public Datatable onSearch(MrDeviceCDDTO mrDeviceCDDTO) {
    BaseDto baseDto = sqlSearchDeviceCD(mrDeviceCDDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrDeviceCDDTO.getPage(), mrDeviceCDDTO.getPageSize(), MrDeviceCDDTO.class,
        mrDeviceCDDTO.getSortName(), mrDeviceCDDTO.getSortType());
  }

  @Override
  public List<MrDeviceCDDTO> exportSearchData(MrDeviceCDDTO mrDeviceCDDTO) {
    BaseDto baseDto = sqlSearchDeviceCD(mrDeviceCDDTO);
    List<MrDeviceCDDTO> data = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrDeviceCDDTO.class));
    if (data == null) {
      return new ArrayList<>();
    }
    return data;
  }


  @Override
  public List<MrDeviceCDDTO> getListMrDeviceCDDTO(MrDeviceCDDTO mrDeviceCDDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(MrDeviceCDEntity.class, mrDeviceCDDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public String insertOrUpdateListMrDeviceCD(List<MrDeviceCDDTO> lstData) {
    if (lstData != null && lstData.size() > 0) {
      for (MrDeviceCDDTO dto : lstData) {
        addOrUpdate(dto);
      }
      return RESULT.SUCCESS;
    }
    return RESULT.ERROR;
  }

  public ResultInSideDto addOrUpdate(MrDeviceCDDTO dto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    MrDeviceCDEntity entity = getEntityManager().merge(dto.toEntity());
    resultInSideDTO.setId(entity.getDeviceCdId());
    return resultInSideDTO;
  }

  public BaseDto sqlSearchDeviceCD(MrDeviceCDDTO mrDeviceCDDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_CD, "on-search");
    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(mrDeviceCDDTO.getStatus())) {
      sql += " AND cd.STATUS = :status ";
      params.put("status", mrDeviceCDDTO.getStatus());
    }

    if (StringUtils.isNotNullOrEmpty(mrDeviceCDDTO.getDeviceType())) {
      sql += " AND cd.DEVICE_TYPE = :deviceType ";
      params.put("deviceType", mrDeviceCDDTO.getDeviceType());
    }

    if (StringUtils.isNotNullOrEmpty(mrDeviceCDDTO.getMarketCode())) {
      sql += " AND cd.MARKET_CODE = :marketCode ";
      params.put("marketCode", mrDeviceCDDTO.getMarketCode());
    }

    if (StringUtils.isNotNullOrEmpty(mrDeviceCDDTO.getStationCode())) {
      sql += " AND cd.STATION_CODE = :stationCode ";
      params.put("stationCode", mrDeviceCDDTO.getStationCode());
    }

    if (StringUtils.isNotNullOrEmpty(mrDeviceCDDTO.getUserMrHard())) {
      sql += " AND LOWER(cd.USER_MR_HARD) like :userMrHard ESCAPE '\\' ";
      params.put("userMrHard",
          StringUtils.convertLowerParamContains(mrDeviceCDDTO.getUserMrHard()));
    }

    if (StringUtils.isNotNullOrEmpty(mrDeviceCDDTO.getMarketName())) {
      sql += " AND LOWER(cl.LOCATION_NAME) like :locationName ESCAPE '\\' ";
      params.put("locationName",
          StringUtils.convertLowerParamContains(mrDeviceCDDTO.getMarketName()));
    }

    if (StringUtils.isNotNullOrEmpty(mrDeviceCDDTO.getDeviceName())) {
      sql += " AND LOWER(cd.DEVICE_NAME) like :deviceName ESCAPE '\\' ";
      params.put("deviceName",
          StringUtils.convertLowerParamContains(mrDeviceCDDTO.getDeviceName()));
    }

    if (StringUtils.isNotNullOrEmpty(mrDeviceCDDTO.getSearchAll())) {
      String sqlAdd = "";
      if (I18n.getLanguage("mrDeviceCD.statusValue1").toLowerCase()
          .contains(mrDeviceCDDTO.getSearchAll().toLowerCase().trim())) {
        sqlAdd = " OR cd.STATUS = 1 ";
      } else if (I18n.getLanguage("mrDeviceCD.statusValue2").toLowerCase()
          .contains(mrDeviceCDDTO.getSearchAll().toLowerCase().trim())) {
        sqlAdd = " OR cd.status is null or cd.status <> 1 ";
      }

      sql += " AND ( "
          + " LOWER(cd.DEVICE_NAME) LIKE :searchAll ESCAPE '\\' "
          + " OR LOWER(cd.USER_MR_HARD) LIKE :searchAll ESCAPE '\\' "
          + sqlAdd
          + " )";
      params.put("searchAll",
          StringUtils.convertLowerParamContains(mrDeviceCDDTO.getSearchAll().trim()));
    }

    sql += " order by NLSSORT(cd.DEVICE_TYPE, 'NLS_SORT=vietnamese') ASC, ";
    sql += " NLSSORT(cd.STATION_CODE, 'NLS_SORT=vietnamese') ASC, ";
    sql += " NLSSORT(cd.USER_MR_HARD, 'NLS_SORT=vietnamese') ASC, ";
    sql += " NLSSORT(cd.MARKET_CODE, 'NLS_SORT=vietnamese') ASC, ";
    sql += " NLSSORT(cd.DEVICE_NAME, 'NLS_SORT=vietnamese') ASC";
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }

  @Override
  public MrDeviceCDEntity findMrDeviceById(Long mrId) {
    if (mrId != null) {
      MrDeviceCDEntity mrEntity = getEntityManager().find(MrDeviceCDEntity.class, mrId);
      return mrEntity;
    }
    return null;
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrDeviceCDEntity mrDeviceCDEntity) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (mrDeviceCDEntity != null) {
      resultInSideDto.setKey(RESULT.SUCCESS);
      MrDeviceCDEntity entity = getEntityManager().merge(mrDeviceCDEntity);
      resultInSideDto.setId(entity.getDeviceCdId());
      return resultInSideDto;
    }
    return null;
  }
}
