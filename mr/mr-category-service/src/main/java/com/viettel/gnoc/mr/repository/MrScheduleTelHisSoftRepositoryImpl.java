package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrScheduleTelHisSoftRepositoryImpl extends BaseRepository implements
    MrScheduleTelHisSoftRepository {

  @Override
  public Datatable getListDataSoftSearchWeb(MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    BaseDto baseDto = sqlGetListDataSoftSearchWeb(mrScheduleTelHisDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrScheduleTelHisDTO.getPage(), mrScheduleTelHisDTO.getPageSize(),
        MrScheduleTelHisDTO.class, mrScheduleTelHisDTO.getSortName(),
        mrScheduleTelHisDTO.getSortType());
    return datatable;
  }

  @Override
  public List<MrScheduleTelHisDTO> getListMrScheduleTelHisSoftExport(
      MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    BaseDto baseDto = sqlGetListDataSoftSearchWeb(mrScheduleTelHisDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrScheduleTelHisDTO.class));
  }

  private BaseDto sqlGetListDataSoftSearchWeb(MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_TEL_HIS_SOFT,
            "sql-Get-List-Data-Soft-Search-Web");
    Map<String, Object> parameters = new HashMap<>();
    if (mrScheduleTelHisDTO != null) {
      //Tim kiem nhanh
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getSearchAll())) {
        sql += " AND (UPPER(h.DEVICE_CODE) LIKE :searchAll ESCAPE '\\'";
        sql += " OR UPPER(h.DEVICE_NAME) LIKE :searchAll ESCAPE '\\'";
        sql += " OR UPPER(h.DEVICE_TYPE) LIKE :searchAll ESCAPE '\\')";
        parameters.put("searchAll",
            StringUtils.convertUpperParamContains(mrScheduleTelHisDTO.getSearchAll()));
      }

      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getMarketCode())) {
        sql += " AND h.MARKET_CODE = :marketCode";
        parameters.put("marketCode", mrScheduleTelHisDTO.getMarketCode());
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getArrayCode())) {
        sql += " AND h.ARRAY_CODE = :arrayCode";
        parameters.put("arrayCode", mrScheduleTelHisDTO.getArrayCode());
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getDeviceType())) {
        sql += " AND UPPER(h.DEVICE_TYPE) LIKE :deviceType ESCAPE '\\'";
        parameters.put("deviceType",
            StringUtils.convertUpperParamContains(mrScheduleTelHisDTO.getDeviceType()));
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getDeviceCode())) {
        sql += " AND UPPER(h.DEVICE_CODE) LIKE :deviceCode ESCAPE '\\'";
        parameters.put("deviceCode",
            StringUtils.convertUpperParamContains(mrScheduleTelHisDTO.getDeviceCode()));
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getDeviceName())) {
        sql += " AND UPPER(h.DEVICE_NAME) LIKE :deviceName ESCAPE '\\'";
        parameters.put("deviceName",
            StringUtils.convertUpperParamContains(mrScheduleTelHisDTO.getDeviceName()));
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getMrDateFrom())) {
        sql += " AND h.MR_DATE >= TO_DATE(:mrDateFrom, 'dd/MM/yyyy HH24:mi:ss')";
        parameters.put("mrDateFrom", mrScheduleTelHisDTO.getMrDateFrom());
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getMrDateTo())) {
        sql += " AND h.MR_DATE <= TO_DATE(:mrDateTo, 'dd/MM/yyyy HH24:mi:ss')";
        parameters.put("mrDateTo", mrScheduleTelHisDTO.getMrDateTo());
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getMrCode())) {
        sql += " AND UPPER(h.MR_CODE) LIKE :mrCode ESCAPE '\\'";
        parameters.put("mrCode",
            StringUtils.convertUpperParamContains(mrScheduleTelHisDTO.getMrCode()));
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getCrNumber())) {
        sql += " AND UPPER(cr.CR_NUMBER) LIKE :crNumber ESCAPE '\\'";
        parameters.put("crNumber",
            StringUtils.convertUpperParamContains(mrScheduleTelHisDTO.getCrNumber()));
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getCrId())) {
        sql += " h.CR_ID LIKE :crId ESCAPE '\\' ";
        parameters.put("crId",
            StringUtils.convertUpperParamContains(mrScheduleTelHisDTO.getCrId()));
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelHisDTO.getProcedureName())) {
        sql += " AND UPPER(h.PROCEDURE_NAME) LIKE :procedureName ESCAPE '\\'";
        parameters.put("procedureName",
            StringUtils.convertUpperParamContains(mrScheduleTelHisDTO.getProcedureName()));
      }
      if (mrScheduleTelHisDTO.getCheckMrCr() != null && mrScheduleTelHisDTO.getCheckMrCr()) {
        sql += " AND (h.MR_ID is not null or h.CR_ID is not null)";
      }
    }
    sql += " ORDER BY h.MR_DEVICE_HIS_ID ASC";

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
