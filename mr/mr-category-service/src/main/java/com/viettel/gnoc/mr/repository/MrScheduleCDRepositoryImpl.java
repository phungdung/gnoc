package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdExportDTO;
import com.viettel.gnoc.maintenance.model.MrScheduleCdEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrScheduleCDRepositoryImpl extends BaseRepository implements
    MrScheduleCDRepository {

  @Override
  public Datatable onSearch(MrScheduleCdDTO dto) {
    BaseDto baseDto = sqlSearchScheduleCD(dto, false);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(), dto.getPage(),
        dto.getPageSize(), MrScheduleCdDTO.class, dto.getSortName(), dto.getSortType());
  }

  @Override
  public List<MrScheduleCdExportDTO> onSearchExport(MrScheduleCdDTO dto) {
    BaseDto baseDto = sqlSearchScheduleCD(dto, true);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrScheduleCdExportDTO.class));
  }

  @Override
  public MrScheduleCdDTO findById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(MrScheduleCdEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto addOrUpdate(MrScheduleCdDTO dto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    MrScheduleCdEntity entity = getEntityManager().merge(dto.toEntity());
    resultInSideDTO.setId(entity.getScheduleId());
    return resultInSideDTO;
  }

  @Override
  public String insertOrUpdateListCd(List<MrScheduleCdDTO> lstData) {
    if (lstData != null && lstData.size() > 0) {
      for (MrScheduleCdDTO dto : lstData) {
        addOrUpdate(dto);
      }
      return RESULT.SUCCESS;
    }
    return RESULT.ERROR;
  }

  @Override
  public ResultInSideDto deleteById(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrScheduleCdEntity itemEntity = getEntityManager().find(MrScheduleCdEntity.class, id);
    if (itemEntity != null) {
      getEntityManager().remove(itemEntity);
    }
    return resultInSideDto;
  }

  public BaseDto sqlSearchScheduleCD(MrScheduleCdDTO dto, boolean isExport) {
    String sql;

    if (!isExport) {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_CD, "on-search");
    } else {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_CD, "on-search-export");
    }
    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(dto.getMarketCode())) {
      sql += " AND T1.MARKET_CODE = :marketCode ";
      params.put("marketCode", dto.getMarketCode());
    }
    if (dto.getCycle() != null) {
      sql += " AND LOWER(T1.CYCLE) LIKE :pCycle ESCAPE '\\'  ";
      params.put("pCycle", StringUtils.convertLowerParamContains(String.valueOf(dto.getCycle())));
    }

    if (StringUtils.isNotNullOrEmpty(dto.getStationCode())) {
      sql += " AND UPPER(T1.STATION) = :stationCode ";
      params.put("stationCode", dto.getStationCode().toUpperCase());
    }

    if (StringUtils.isNotNullOrEmpty(dto.getUserMrHard())) {
      sql += " AND LOWER(T4.USER_MR_HARD) like :userMrHard ESCAPE '\\' ";
      params.put("userMrHard",
          StringUtils.convertLowerParamContains(dto.getUserMrHard()));
    }

    if (StringUtils.isNotNullOrEmpty(dto.getDeviceType())) {
      sql += " AND LOWER(T1.DEVICE_TYPE) like :deviceType ESCAPE '\\'  ";
      params.put("deviceType", StringUtils.convertLowerParamContains(dto.getDeviceType()));
    }

    if (StringUtils.isNotNullOrEmpty(dto.getDeviceName())) {
      sql += " AND LOWER(T1.DEVICE_NAME) like :deviceName ESCAPE '\\' ";
      params.put("deviceName",
          StringUtils.convertLowerParamContains(dto.getDeviceName()));
    }

    if (dto.getDateModifyFromSearch() != null) {
      sql += " AND TO_DATE(TO_CHAR(T1.NEXT_DATE_MODIFY, 'dd/MM/yyyy'), 'dd/MM/yyyy') >= :dateMdfFrom ";
      params.put("dateMdfFrom", dto.getDateModifyFromSearch());
    }
    if (dto.getDateModifyToSearch() != null) {
      sql += " AND TO_DATE(TO_CHAR(T1.NEXT_DATE_MODIFY, 'dd/MM/yyyy'), 'dd/MM/yyyy') <= :dateMdfTo ";
      params.put("dateMdfTo", dto.getDateModifyToSearch());
    }

    if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
      sql += " AND ( "
          + " LOWER(T1.DEVICE_NAME) LIKE :searchAll ESCAPE '\\' "
          + " OR LOWER(T1.CYCLE) LIKE :searchAll ESCAPE '\\' "
          + " OR LOWER(T4.USER_MR_HARD) LIKE :searchAll ESCAPE '\\' "
          + " )";
      params.put("searchAll", StringUtils.convertLowerParamContains(dto.getSearchAll().trim()));
    }

    sql += "ORDER BY T1.NEXT_DATE_MODIFY ";
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }
}
