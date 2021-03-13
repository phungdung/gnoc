package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdHisDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrScheduleCdHisRepositoryImpl extends BaseRepository implements
    MrScheduleCdHisRepository {

  @Override
  public Datatable getListMrScheduleCdHis(MrScheduleCdHisDTO mrScheduleCdHisDTO) {
    BaseDto baseDto = sqlSearchMrScheduleCdHis(mrScheduleCdHisDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrScheduleCdHisDTO.getPage(),
        mrScheduleCdHisDTO.getPageSize(), MrScheduleCdHisDTO.class,
        mrScheduleCdHisDTO.getSortName(), mrScheduleCdHisDTO.getSortType());
  }

  @Override
  public List<MrScheduleCdHisDTO> onSearchExport(MrScheduleCdHisDTO mrScheduleCdHisDTO) {
    BaseDto baseDto = sqlSearchMrScheduleCdHis(mrScheduleCdHisDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrScheduleCdHisDTO.class));
  }

  public BaseDto sqlSearchMrScheduleCdHis(MrScheduleCdHisDTO mrScheduleCdHisDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MR_SCHEDULE_CD_HIS, "get-mr-schedule-cr-his");
    Map<String, Object> parameters = new HashMap<>();
    if (mrScheduleCdHisDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrScheduleCdHisDTO.getDeviceType())) {
        sql += " AND T1.DEVICE_TYPE = :deviceType ";
        parameters.put("deviceType", mrScheduleCdHisDTO.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleCdHisDTO.getDeviceName())) {
        sql += " AND UPPER(T1.DEVICE_NAME) LIKE :deviceName ESCAPE '\\'";
        parameters.put("deviceName",
            StringUtils.convertLowerParamContains(mrScheduleCdHisDTO.getDeviceName())
                .toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleCdHisDTO.getMrDateFrom())) {
        sql += " AND TO_DATE(TO_CHAR(T1.MR_DATE, 'dd/MM/yyyy'), 'dd/MM/yyyy') >= TO_DATE(:mrDateFrom, 'dd/MM/yyyy') ";
        parameters.put("mrDateFrom", mrScheduleCdHisDTO.getMrDateFrom());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleCdHisDTO.getMrDateTo())) {
        sql += " AND TO_DATE(TO_CHAR(T1.MR_DATE, 'dd/MM/yyyy'), 'dd/MM/yyyy') <= TO_DATE(:mrDateTo, 'dd/MM/yyyy') ";
        parameters.put("mrDateTo", mrScheduleCdHisDTO.getMrDateTo());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleCdHisDTO.getMrCode())) {
        sql += " AND UPPER(T1.MR_CODE) LIKE :mrCode ESCAPE '\\'";
        parameters.put("mrCode",
            StringUtils.convertLowerParamContains(mrScheduleCdHisDTO.getMrCode()).toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleCdHisDTO.getUserMrHard())) {
        sql += " AND UPPER(T2.USER_MR_HARD) LIKE :userMrHard ESCAPE '\\'";
        parameters.put("userMrHard",
            StringUtils.convertLowerParamContains(mrScheduleCdHisDTO.getUserMrHard())
                .toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleCdHisDTO.getStationCode())) {
        sql += " AND T2.STATION_CODE = :stationCode ";
        parameters.put("stationCode", mrScheduleCdHisDTO.getStationCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleCdHisDTO.getMarketCode())) {
        sql += " AND UPPER(T1.MARKET_CODE) LIKE :marketCode ESCAPE '\\'";
        parameters.put("marketCode",
            StringUtils.convertLowerParamContains(mrScheduleCdHisDTO.getMarketCode())
                .toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleCdHisDTO.getSearchAll())) {
        sql += " AND LOWER(T1.DEVICE_NAME) LIKE '%' || :searchAll || '%' ESCAPE '\\'";
        sql += " OR LOWER(T1.MR_CODE) LIKE '%' || :searchAll || '%' ESCAPE '\\'";
        sql += " OR LOWER(T2.USER_MR_HARD) LIKE '%' || :searchAll || '%' ESCAPE '\\'";
        parameters.put("searchAll",
            StringUtils.convertLowerParamContains(mrScheduleCdHisDTO.getSearchAll()));
      }
    }
    sql += " ORDER BY T1.MR_DATE ";

    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrScheduleCdHisDTO dto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(dto.toEntity());
    return resultInSideDTO;
  }
}
