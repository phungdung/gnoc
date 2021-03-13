package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.maintenance.model.MrEntity;
import com.viettel.gnoc.maintenance.model.MrScheduleTelEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrScheduleTelRepositoryImpl extends BaseRepository implements MrScheduleTelRepository {

  public BaseDto sqlSearch(MrScheduleTelDTO mrScheduleTelDTO, String type) {
    String sql;
    if ("H".equals(type)) {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_TEL, "getListMrScheduleTel");
    } else {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_TEL, "on-search-schedule-soft");
    }
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("cycleTypeM", I18n.getLanguage("MrScheduleTel.cycleTypeM"));
    parameters.put("cycleTypeD", I18n.getLanguage("MrScheduleTel.cycleTypeD"));
    parameters.put("lee_Locale", I18n.getLocale());
    if (mrScheduleTelDTO != null) {
      if ("H".equals(type)) {
        if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getSearchAll())) {
          sql += (" AND (LOWER(T1.DEVICE_CODE) LIKE :searchAll ESCAPE '\\' OR LOWER(T1.DEVICE_TYPE) LIKE :searchAll ESCAPE '\\' OR LOWER(T1.NETWORK_TYPE) LIKE :searchAll ESCAPE '\\')");
          parameters.put("searchAll", StringUtils.convertLowerParamContains(
              mrScheduleTelDTO.getSearchAll()));
        }
        if (mrScheduleTelDTO.getWoId() != null && mrScheduleTelDTO.getWoId() > 0) {
          sql += (" AND T1.WO_ID =:woId ");
          parameters.put("woId", mrScheduleTelDTO.getWoId());
        }
      } else {
        if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getSearchAll())) {
          sql += (" AND (LOWER(T1.DEVICE_CODE) LIKE :searchAll ESCAPE '\\' OR LOWER(T1.DEVICE_NAME) LIKE :searchAll ESCAPE '\\' OR LOWER(T5.NODE_IP) LIKE :searchAll ESCAPE '\\'  OR (TO_CHAR(T4.CYCLE) LIKE :searchAll ESCAPE '\\' AND T1.MR_SOFT = 1)  )");
          parameters.put("searchAll", StringUtils.convertLowerParamContains(
              mrScheduleTelDTO.getSearchAll()));
        }
        if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getCrNumber())) {
          sql += (" AND LOWER(T12.CR_NUMBER) LIKE :crNumber ESCAPE '\\' ");
          parameters
              .put("crNumber", StringUtils.convertLowerParamContains(mrScheduleTelDTO.getCrNumber()));
        }
        if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getRegion())) {
          sql += (" AND LOWER(T1.REGION) LIKE :region ESCAPE '\\' ");
          parameters
              .put("region", StringUtils.convertLowerParamContains(mrScheduleTelDTO.getRegion()));
        }
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getScheduleId())) {
        sql += (" AND T1.SCHEDULE_ID =:scheduleId ");
        parameters.put("scheduleId", mrScheduleTelDTO.getScheduleId());
      }
      if (mrScheduleTelDTO.getScheduleIdList() != null && !mrScheduleTelDTO.getScheduleIdList()
          .isEmpty()) {
        sql += (" AND T1.SCHEDULE_ID IN (:scheduleIds) ");
        parameters.put("scheduleIds", mrScheduleTelDTO.getScheduleIdList());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMarketCode())) {
        sql += (" AND LOWER(T1.MARKET_CODE) LIKE :marketCode ESCAPE '\\' ");
        parameters.put("marketCode",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getMarketCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getRegion())) {
        sql += (" AND LOWER(T1.REGION) LIKE :region ESCAPE '\\' ");
        parameters.put("region",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getRegion()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getArrayCode())) {
        sql += (" AND LOWER(T1.ARRAY_CODE) LIKE :arrayCode ESCAPE '\\' ");
        parameters.put("arrayCode",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getArrayCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getDeviceCode())) {
        sql += (" AND LOWER(T1.DEVICE_CODE) LIKE :deviceCode ESCAPE '\\' ");
        parameters.put("deviceCode",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getDeviceCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getProcedureName())) {
        sql += (" AND LOWER(T4.PROCEDURE_NAME) LIKE :proceDureName ESCAPE '\\' ");
        parameters.put("proceDureName",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getProcedureName()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getNetworkType())) {
        sql += (" AND T1.NETWORK_TYPE =:netWorkType ");
        parameters.put("netWorkType", mrScheduleTelDTO.getNetworkType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getDeviceName())) {
        sql += (" AND LOWER(T1.DEVICE_NAME) LIKE :deviceName ESCAPE '\\' ");
        parameters.put("deviceName",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getDeviceName()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getDeviceType())) {
        sql += (" AND T1.DEVICE_TYPE =:deviceType ");
        parameters.put("deviceType", mrScheduleTelDTO.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getNodeIp())) {
        sql += (" AND LOWER(T5.NODE_IP) LIKE :nodeIp ESCAPE '\\' ");
        parameters.put("nodeIp",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getNodeIp()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getCycle())) {
        sql += (" AND ((TO_CHAR(T4.CYCLE) = :cycle AND T1.MR_SOFT = 1) OR (T1.MR_HARD = 1 AND T1.MR_HARD_CYCLE = :cycle)) ");
        parameters.put("cycle", mrScheduleTelDTO.getCycle());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getImplementUnitId())) {
        sql += (" AND T9.IMPLEMENT_UNIT = :implementUnit ");
        parameters.put("implementUnit", mrScheduleTelDTO.getImplementUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getCheckingUnitId())) {
        sql += (" AND T9.CHECKING_UNIT = :checkingUnit ");
        parameters.put("checkingUnit", mrScheduleTelDTO.getCheckingUnitId());
      }

      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getDateModifyFromSearch())) {
        sql += " AND TO_DATE(TO_CHAR(T1.NEXT_DATE_MODIFY, 'dd/MM/yyyy'), 'dd/MM/yyyy') >= TO_DATE(:nextDateModifyFrom,'dd/MM/yyyy') ";
        parameters.put("nextDateModifyFrom", DateUtil
            .dateToStringWithPattern(mrScheduleTelDTO.getDateModifyFromSearch(), "dd/MM/yyyy"));
      }

      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getDateModifyToSearch())) {
        sql += " AND TO_DATE(TO_CHAR(T1.NEXT_DATE_MODIFY, 'dd/MM/yyyy'), 'dd/MM/yyyy') <= TO_DATE(:nextDateModifyTo,'dd/MM/yyyy') ";
        parameters.put("nextDateModifyTo", DateUtil
            .dateToStringWithPattern(mrScheduleTelDTO.getDateModifyToSearch(), "dd/MM/yyyy"));
      }

      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getGroupCode())) {
        sql += (" AND T1.GROUP_CODE = :groupCode ");
        parameters.put("groupCode", mrScheduleTelDTO.getGroupCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrSoft())) {
        sql += (" AND T1.MR_SOFT = :mrSoft ");
        parameters.put("mrSoft", mrScheduleTelDTO.getMrSoft());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrHard())) {
        sql += (" AND T1.MR_HARD = :mrHard ");
        parameters.put("mrHard", mrScheduleTelDTO.getMrHard());
      }
      //start rikkei
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrConfirm())) {
        sql += (" AND T1.MR_CONFIRM = :mrConFirm ");
        parameters.put("mrConFirm", mrScheduleTelDTO.getMrConfirm());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getDeviceId())) {
        sql += (" AND T1.DEVICE_ID = :deviceId ");
        parameters.put("deviceId", mrScheduleTelDTO.getDeviceId());
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getUserMrHard())) {
        sql += (" AND LOWER(T5.USER_MR_HARD) LIKE :userMrHard ESCAPE '\\' ");
        parameters.put("userMrHard",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getUserMrHard()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrId())) {
        if (mrScheduleTelDTO.getMrId() == 1L) {
          sql += (" AND T1.MR_ID is null ");
        }
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrCode())) {
        sql += (" AND LOWER(T13.MR_CODE) LIKE :mrCode ESCAPE '\\' ");
        parameters.put("mrCode", StringUtils.convertLowerParamContains(mrScheduleTelDTO.getMrCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getVendor())) {
        sql += (" AND LOWER(T5.VENDOR) LIKE :vendor ESCAPE '\\' ");
        parameters
            .put("vendor", StringUtils.convertLowerParamContains(mrScheduleTelDTO.getVendor()));
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getStation())) {
        sql += (" AND LOWER(T1.STATION) LIKE :station ESCAPE '\\' ");
        parameters
            .put("station", StringUtils.convertLowerParamContains(mrScheduleTelDTO.getStation()));
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getImpactNode())) {
        sql += (" AND LOWER(T5.IMPACT_NODE) LIKE :impactNode ESCAPE '\\' ");
        parameters.put("impactNode",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getImpactNode()));
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getGroupCode())) {
        sql += (" AND LOWER(T1.GROUP_CODE) LIKE :groupCode ESCAPE '\\' ");
        parameters.put("groupCode",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getGroupCode()));
      }
      sql += " order by T1.UPDATED_DATE DESC";
      //end rikkei
    }
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListMrScheduleTel(MrScheduleTelDTO mrScheduleTelDTO, String type) {
    BaseDto baseDto = sqlSearch(mrScheduleTelDTO, type);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrScheduleTelDTO.getPage(), mrScheduleTelDTO.getPageSize(), MrScheduleTelDTO.class,
        mrScheduleTelDTO.getSortName(), mrScheduleTelDTO.getSortType());
  }

  @Override
  public MrScheduleTelDTO getDetail(Long id, String type) {
    if (!StringUtils.isStringNullOrEmpty(id)) {
      BaseDto baseDto = sqlSearch(new MrScheduleTelDTO(id), type);
      List<MrScheduleTelDTO> lst = getNamedParameterJdbcTemplate()
          .query(baseDto.getSqlQuery(), baseDto.getParameters(),
              BeanPropertyRowMapper.newInstance(MrScheduleTelDTO.class));
      return lst.isEmpty() ? null : lst.get(0);
    }
    return null;
  }

  @Override
  public List<MrScheduleTelDTO> getDataExport(MrScheduleTelDTO mrScheduleTelDTO, String type) {
    BaseDto baseDto = sqlSearch(mrScheduleTelDTO, type);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrScheduleTelDTO.class));
  }

  @Override
  public ResultInSideDto updateMrScheduleTel(MrScheduleTelDTO mrScheduleTelDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrScheduleTelEntity entity = getEntityManager().merge(mrScheduleTelDTO.toEntity());
    resultInSideDto.setId(entity.getScheduleId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrScheduleTel(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrScheduleTelEntity entity = getEntityManager().find(MrScheduleTelEntity.class, id);
    getEntityManager().remove(entity);
    resultInSideDto.setId(id);
    return resultInSideDto;
  }

  @Override
  public MrInsideDTO findMrById(Long mrId) {
    if (!StringUtils.isStringNullOrEmpty(mrId)) {
      List<MrEntity> lst = findByMultilParam(MrEntity.class, "mrId", mrId);
      return lst.isEmpty() ? null : lst.get(0).toDTO();
    }
    return new MrInsideDTO();
  }

  @Override
  public List<MrConfigDTO> getConfigByGroup(String configGroup) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_TEL, "getConfigByGroup");
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("configGroup", configGroup);
    return getNamedParameterJdbcTemplate().query(sql, parameter,
        BeanPropertyRowMapper.newInstance(MrConfigDTO.class));
  }

  @Override
  public ResultInSideDto deleteListSchedule(List<MrScheduleTelDTO> lstScheduleDTO) {
    if (lstScheduleDTO != null && !lstScheduleDTO.isEmpty()) {
      for (MrScheduleTelDTO dto : lstScheduleDTO) {
        deleteMrScheduleTel(dto.getScheduleId());
      }
    }
    return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public List<MrScheduleTelDTO> getListMrScheduleTelDTO(MrScheduleTelDTO mrScheduleTelDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_TEL, "get-List-Mr-Schedule-Tel-DTO");
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("cycleTypeM", I18n.getLanguage("MrScheduleTel.cycleTypeM"));
    parameter.put("cycleTypeD", I18n.getLanguage("MrScheduleTel.cycleTypeD"));
    if (mrScheduleTelDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getMarketCode())) {
        sql += " AND LOWER(T1.MARKET_CODE) LIKE :marketCode ESCAPE '\\'";
        parameter.put("marketCode",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getMarketCode()));
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getDeviceCode())) {
        sql += " AND LOWER(T1.DEVICE_CODE) LIKE :deviceCode ESCAPE '\\'";
        parameter.put("deviceCode",
            StringUtils.convertLowerParamContains(mrScheduleTelDTO.getDeviceCode()));
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getMrSoft())) {
        sql += " AND T1.MR_SOFT = :mrSoft";
        parameter.put("mrSoft", mrScheduleTelDTO.getMrSoft());
      }
      if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getMrHard())) {
        sql += " AND T1.MR_HARD = :mrHard";
        parameter.put("mrHard", mrScheduleTelDTO.getMrHard());
      }
      if (mrScheduleTelDTO.getDeviceId() != null) {
        sql += " AND T1.DEVICE_ID = :deviceId";
        parameter.put("deviceId", mrScheduleTelDTO.getDeviceId());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameter, BeanPropertyRowMapper.newInstance(MrScheduleTelDTO.class));
  }


  @Override
  public List<MrScheduleTelDTO> getListMrSheduleTelByIdForImport(List<String> idSearchs) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_TEL, "get-list-scheduletel-for-import");
    Map<String, Object> params = new HashMap<>();
    params.put("scheduleIds", idSearchs);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrScheduleTelDTO.class));
  }

  @Override
  public MrScheduleTelDTO findById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(MrScheduleTelEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public List<MrScheduleTelHisDTO> getListScheduleMoveToHis(MrDeviceDTO mrDeviceDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_TEL, "get-List-Schedule-Move-To-His");
    Map<String, Object> parameter = new HashMap<>();
    if (mrDeviceDTO != null) {
      if (mrDeviceDTO.getDeviceId() != null) {
        sql += " AND t.DEVICE_ID = :deviceId";
        parameter.put("deviceId", mrDeviceDTO.getDeviceId());
      }
      if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getNodeCode())) {
        sql += " AND LOWER(t.DEVICE_CODE) LIKE :deviceCode ESCAPE '\\'";
        parameter.put("deviceCode",
            StringUtils.convertLowerParamContains(mrDeviceDTO.getNodeCode()));
      }
      if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getMrSoft())) {
        sql += " AND t.MR_SOFT = :mrSoft";
        parameter.put("mrSoft", mrDeviceDTO.getMrSoft());
      }
      if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getMarketCode())) {
        sql += " AND t.MARKET_CODE = :marketCode";
        parameter.put("marketCode", mrDeviceDTO.getMarketCode());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameter, BeanPropertyRowMapper.newInstance(MrScheduleTelHisDTO.class));
  }

  @Override
  public List<CrHisDTO> checkExistCrId(String crId, String crIdOld) {
    if (StringUtils.isStringNullOrEmpty(crId)) {
      return new ArrayList<>();
    }
    try {
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      params.put("status", 9);
      StringBuilder sqlBuilder = new StringBuilder(
          SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_TEL, "check-exist-cr-id"));
      if (!StringUtils.isStringNullOrEmpty(crIdOld)) {
        sqlBuilder.append(
            "  AND  (select count(*) from cr JOIN OPEN_PM.CR_IMPACTED_NODES im on cr.CR_ID = im.CR_ID where  cr.CR_ID = :crId GROUP BY cr.CR_ID) "
                + "        = (select count(*) from cr JOIN OPEN_PM.CR_IMPACTED_NODES im on cr.CR_ID = im.CR_ID where  cr.CR_ID = :crOldId GROUP BY cr.CR_ID)");
        params.put("crOldId", crIdOld);
      }
      return getNamedParameterJdbcTemplate()
          .query(sqlBuilder.toString(), params, BeanPropertyRowMapper.newInstance(CrHisDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MrScheduleTelDTO> getByMrId(String mrId) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SCHEDULE_TEL, "get-mr-by-id");
      Map<String, Object> params = new HashMap<>();
      params.put("mrId", mrId);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrScheduleTelDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }
}
