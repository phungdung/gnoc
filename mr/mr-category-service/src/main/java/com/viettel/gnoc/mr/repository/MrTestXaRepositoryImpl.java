package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.model.MrCdBatteryEntity;
import com.viettel.gnoc.maintenance.model.MrNodesEntity;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;


@Repository
@Slf4j
public class MrTestXaRepositoryImpl extends BaseRepository implements MrTestXaRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UserRepository userRepository;

  @Override
  public Datatable getListDatatableMrCdBatterryDTO(MrCdBatteryDTO mrCdBatteryDTO) {
    BaseDto baseDto = sqlSearch(mrCdBatteryDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrCdBatteryDTO.getPage(),
        mrCdBatteryDTO.getPageSize(), MrCdBatteryDTO.class,
        mrCdBatteryDTO.getSortName(), mrCdBatteryDTO.getSortType());
  }

  @Override
  public MrCdBatteryDTO findMrCDBatteryById(Long dcPowerId) {
    MrCdBatteryEntity entity = getEntityManager()
        .find(MrCdBatteryEntity.class, dcPowerId);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public MrCdBatteryDTO findMrCDBatteryByProperty(MrCdBatteryDTO mrCdBatteryDTO) {
    List<MrCdBatteryEntity> lstEntity = findByMultilParam(MrCdBatteryEntity.class, "stationCode",
        mrCdBatteryDTO.getStationCode(), "dcPower", mrCdBatteryDTO.getDcPower(), "dischargeType",
        mrCdBatteryDTO.getDischargeType());
    if (lstEntity != null && !lstEntity.isEmpty()) {
      return lstEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public List<ItemDataCRInside> getListLocationCombobox(Long parentId) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CD_BATTERY, "get-list-location");
    if (parentId != null) {
      sql += "  START WITH PARENT_ID        =:parentId";
      parameters.put("parentId", parentId);
    }
    sql += " CONNECT BY prior PARENT_ID = parent_id";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public List<ItemDataCRInside> getListLocationComboboxByCode(String locationCode) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CD_BATTERY, "get-list-location");
    if (locationCode != null) {
      sql += "  START WITH LOCATION_CODE        =:locationCode";
      parameters.put("locationCode", locationCode);
    }
    sql += " CONNECT BY prior PARENT_ID = parent_id";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public List<ItemDataCRInside> getListCountry() {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CD_BATTERY, "get-list-country");
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public MrCdBatteryDTO getDetailById(Long dcPowerId) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CD_BATTERY, "get-detail-mr-cd-battery");
//    Double offset=0.0D;
//    if(userToken!=null&&StringUtils.isNotNullOrEmpty(userToken.getUserName())){
//      offset = userRepository.getOffsetFromUser(userToken.getUserName());
//    }
    parameters.put("offset", TimezoneContextHolder.getOffsetDouble());
    if (dcPowerId != null) {
      sql += "AND t1.DC_POWER_ID =:dcPowerId ";
      parameters.put("dcPowerId", dcPowerId);
    }
    List<MrCdBatteryDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrCdBatteryDTO.class));
    return list.get(0);
  }

  @Override
  public MrCdBatteryDTO findById(Long dcPowerId) {
    if (dcPowerId != null && dcPowerId > 0) {
      return getEntityManager().find(MrCdBatteryEntity.class, dcPowerId).toDTO();
    }
    return null;
  }

  @Override
  public List<MrCdBatteryDTO> getListMrCdBatteryByListId(List<Long> lstIds) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CD_BATTERY, "get-list-mr_cd_battery-by-ids");
    Map<String, Object> params = new HashMap<>();
    params.put("lstId", lstIds);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrCdBatteryDTO.class));
  }

  @Override
  public ResultInSideDto updateMrCdBatteryDTO(MrCdBatteryDTO mrCdBatteryDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    MrCdBatteryEntity entity = mrCdBatteryDTO.toEntity();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(" UPDATE OPEN_PM.MR_CD_BATTERY ");
    sql.append(" SET STATION_ID          = :stationId,");
    sql.append("  PROVINCE              = :province,");
    sql.append("  STAFF_NAME            = :staffName,");
    sql.append("  STAFF_EMAIL           = :staffEmail,");
    sql.append("  STAFF_PHONE           = :staffPhone,");
    sql.append("  TIME_DISCHARGE        = :timeDischarge,");
    sql.append("  RECENT_DISCHARGE_CD   = :recentDischargeCd,");
    sql.append("  MR_CODE               = :mrCode,");
    sql.append("  WO_CODE               = :woCode,");
    sql.append("  STATUS                = :status,");
    sql.append("  UPDATED_TIME          = :updateTime,");
    sql.append("  UPDATED_USER          = :updateUser,");
    sql.append("  RECENT_DISCHAGE_NOC   = :recentDischageNoc,");
    sql.append("  RECENT_DISCHAGE_GNOC  = :recentDischageGnoc,");
    sql.append("  PRODUCTION_TECHNOLOGY = :productionTechnology,");
    sql.append("  DISTRICT_CODE         = :districtCode,");
    sql.append("  DISCHARGE_CONFIRM     = :dischargeConfirm,");
    sql.append("  RESULT_DISCHARGE      = :resultDischarge,");
    sql.append("  DISCHARGE_NUMBER      = :dischargeNumber,");
    sql.append("  DISCHARGE_REASON_FAIL = :dischargeReasonFail,");
    sql.append("  RECENT_DISCHARGE_GNOC = :recentDischargeGnoc,");
    sql.append("  RECENT_DISCHARGE_NOC  = :recentDischargeNoc,");
    sql.append("  REASON_ACCEPT         = :reasonAccept,");
    sql.append("  STATUS_ACCEPT         = :statusAccept,");
    sql.append("  AREA_CODE             = :areaCode,");
    sql.append("  PROVINCE_CODE         = :provinceCode,");
    sql.append("  ISWOACCU              = :iswoaccu,");
    sql.append("  MARKET_CODE           = :marketCode,");
    sql.append("  DISTRICT_NAME         = :districtName");
    sql.append(" WHERE STATION_CODE      = :stationCode");
    sql.append(" AND DC_POWER            = :dcPower");
    sql.append(" AND DISCHARGE_TYPE      = :dischargeType");

    parameters.put("stationId", entity.getStationId());
    parameters.put("province", entity.getProvince());
    parameters.put("staffName", entity.getStaffName());
    parameters.put("staffEmail", entity.getStaffEmail());
    parameters.put("staffPhone", entity.getStaffPhone());
    parameters.put("timeDischarge", entity.getTimeDischarge());
    parameters.put("recentDischargeCd", entity.getRecentDischargeCd());
    parameters.put("mrCode", entity.getMrCode());
    parameters.put("woCode", entity.getWoCode());
    parameters.put("status", entity.getStatus());
    parameters.put("updateTime", entity.getUpdatedTime());
    parameters.put("updateUser", entity.getUpdatedUser());
    parameters.put("recentDischageNoc", entity.getRecentDischageNoc());
    parameters.put("recentDischageGnoc", entity.getRecentDischageGnoc());
    parameters.put("productionTechnology", entity.getProductionTechnology());
    parameters.put("districtCode", entity.getDistrictCode());
    parameters.put("dischargeConfirm", entity.getDischargeConfirm());
    parameters.put("resultDischarge", entity.getResultDischarge());
    parameters.put("dischargeNumber", entity.getDischargeNumber());
    parameters.put("dischargeReasonFail", entity.getDischargeReasonFail());
    parameters.put("recentDischargeGnoc", entity.getRecentDischargeGnoc());
    parameters.put("recentDischargeNoc", entity.getRecentDischargeNoc());
    parameters.put("reasonAccept", entity.getReasonAccept());
    parameters.put("statusAccept", entity.getStatusAccept());
    parameters.put("areaCode", entity.getAreaCode());
    parameters.put("provinceCode", entity.getProvinceCode());
    parameters.put("iswoaccu", entity.getIswoAccu());
    parameters.put("marketCode", entity.getMarketCode());
    parameters.put("districtName", entity.getDistrictName());
    parameters.put("stationCode", entity.getStationCode());
    parameters.put("dcPower", entity.getDcPower());
    parameters.put("dischargeType", entity.getDischargeType());
    getNamedParameterJdbcTemplate().update(sql.toString(), parameters);
    return resultInSideDto;
  }

  @Override
  public List<MrCdBatteryDTO> getDataExport(MrCdBatteryDTO mrCdBatteryDTO) {
    BaseDto baseDto = sqlSearch(mrCdBatteryDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrCdBatteryDTO.class));
  }

  @Override
  public MrNodesDTO findMrNodeByID(Long mrNodeID) {
    MrNodesEntity entity = getEntityManager()
        .find(MrNodesEntity.class, mrNodeID);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteMrNode(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrNodesEntity mrNodesEntity = getEntityManager()
        .find(MrNodesEntity.class, id);
    getEntityManager().remove(mrNodesEntity);
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateWo(WoDTO woDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    WoInsideDTO woInsideDTO = getEntityManager().merge(woDTO.toModelInSide());
    resultInSideDto.setId(woInsideDTO.getWoId());
    return resultInSideDto;
  }

  @Override
  public List<MrCdBatteryDTO> getByIdImport(List<Long> dcPowerIds, String userName) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CD_BATTERY, "getDataImportByID");
//    if (!dcPowerIds.isEmpty()) {
//      parameters.put("listId", dcPowerIds);
//
//    }
    if (!dcPowerIds.isEmpty()) {
      sql += " AND T1.DC_POWER_ID in (";
      String dcPowerId = "";
      for (Long PowerId : dcPowerIds) {
        dcPowerId += "'" + PowerId + "',";
      }
      if (dcPowerId.endsWith(",")) {
        dcPowerId = dcPowerId.substring(0, dcPowerId.length() - 1);
      }
      sql += dcPowerId;
      sql += " ) ";
    }
    if (!StringUtils.isStringNullOrEmpty(userName)) {

      parameters.put("userName", userName);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrCdBatteryDTO.class));
  }

  @Override
  public List<MrCdBatteryDTO> getListWoFromMrCdBattery(String woCode) {
    String sql = " SELECT DC_POWER_ID dcPowerId, DC_POWER dcPower, STATION_CODE stationCode, WO_CODE woCode, DISCHARGE_TYPE dischargeType, ISWOACCU iswoAccu FROM OPEN_PM.MR_CD_BATTERY WHERE WO_CODE =:woCode ";
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("woCode", woCode);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameter, BeanPropertyRowMapper.newInstance(MrCdBatteryDTO.class));
  }

  public BaseDto sqlSearch(MrCdBatteryDTO mrCdBatteryDTO) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    UserToken userToken = ticketProvider.getUserToken();
    Double offset = 0.0D;
    if (userToken != null && StringUtils.isNotNullOrEmpty(userToken.getUserName())) {
      offset = userRepository.getOffsetFromUser(userToken.getUserName());
    }
    parameters.put("offset", offset);
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CD_BATTERY, "get-list-Mr-CdBattery");
    parameters.put("Yes", I18n.getLanguage("mrTestXa.yes"));
    parameters.put("Isnot", I18n.getLanguage("mrTestXa.no"));
    parameters.put("unassigned", I18n.getLanguage("message.wo.status.UNASSIGNED"));
    parameters.put("assigned", I18n.getLanguage("message.wo.status.ASSIGNED"));
    parameters.put("ftReject", I18n.getLanguage("message.wo.status.REJECT"));
    parameters.put("dispatch", I18n.getLanguage("message.wo.status.DISPATCH"));
    parameters.put("accept", I18n.getLanguage("message.wo.status.ACCEPT"));
    parameters.put("inprocess", I18n.getLanguage("message.wo.status.INPROCESS"));
    parameters.put("closeFT", I18n.getLanguage("message.wo.status.CLOSED_FT"));
    parameters.put("draft", I18n.getLanguage("message.wo.status.DRAFT"));
    parameters.put("closeCD", I18n.getLanguage("message.wo.status.CLOSED_CD"));
    parameters.put("pending", I18n.getLanguage("message.wo.status.PENDING"));
    parameters.put("cdReject", I18n.getLanguage("message.wo.status.REJECT_CD"));
    parameters.put("finish", I18n.getLanguage("mrTestXa.batteryDisFinish"));
    parameters.put("processing", I18n.getLanguage("mrTestXa.batteryDisProcessing"));
    parameters.put("userName", mrCdBatteryDTO.getUserImplemen());
    parameters.put("activeStatus0", I18n.getLanguage("mrTestXa.activeStatus0"));
    parameters.put("activeStatus1", I18n.getLanguage("mrTestXa.activeStatus1"));
    if (mrCdBatteryDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrCdBatteryDTO.getSearchAll())) {
        sql += " AND (lower(t1.stationCode) LIKE :searchAll ESCAPE '\\' OR lower(t1.dcPower) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll", StringUtils.convertLowerParamContains(mrCdBatteryDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getMarketCode())) {
        sql += " AND t1.marketCode =:marketCode ";
        parameters.put("marketCode", mrCdBatteryDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getAreaCode())) {
        sql += " AND t1.areaCode =:areaCode ";
        parameters.put("areaCode", mrCdBatteryDTO.getAreaCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getProvinceCode())) {
        sql += " AND t1.provinceCode =:provinceCode ";
        parameters.put("provinceCode", mrCdBatteryDTO.getProvinceCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getStationCode())) {
        sql += " AND LOWER(t1.stationCode) LIKE :stationCode ESCAPE '\\' ";
        parameters.put("stationCode",
            StringUtils.convertLowerParamContains(mrCdBatteryDTO.getStationCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getDcPower())) {
        sql += " AND LOWER(t1.dcPower) LIKE :dcPower ESCAPE '\\' ";
        parameters
            .put("dcPower", StringUtils.convertLowerParamContains(mrCdBatteryDTO.getDcPower()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getDischargeType())) {
        sql += " AND t1.dischargeType =:dischargeType";
        parameters.put("dischargeType", mrCdBatteryDTO.getDischargeType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getTimeDischargeFrom())) {
        sql += " AND t1.recentDischargeCd >= TO_DATE(:timeDischargeFrom, 'dd/MM/yyyy HH24:mi:ss') ";
        parameters.put("timeDischargeFrom", mrCdBatteryDTO.getTimeDischargeFrom());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getTimeDischargeTo())) {
        sql += " AND t1.recentDischargeCd <= TO_DATE(:timeDischargeTo, 'dd/MM/yyyy HH24:mi:ss') ";
        parameters.put("timeDischargeTo", mrCdBatteryDTO.getTimeDischargeTo());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getTimeDischarge())) {
        sql += " AND LOWER(t1.timeDischarge) LIKE :timeDischarge ESCAPE '\\' ";
        parameters.put("timeDischarge",
            StringUtils.convertLowerParamContains(mrCdBatteryDTO.getTimeDischarge()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getIswoAccu())) {
        sql += " AND t1.iswoAccu =:iswoAccu";
        parameters.put("iswoAccu", mrCdBatteryDTO.getIswoAccu());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getMrCode())) {
        sql += " AND LOWER(t1.mrCode) LIKE :mrCode ESCAPE '\\' ";
        parameters.put("mrCode", StringUtils.convertLowerParamContains(mrCdBatteryDTO.getMrCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getWoCode())) {
        sql += " AND LOWER(t1.woCode) LIKE :woCode ESCAPE '\\' ";
        parameters.put("woCode", StringUtils.convertLowerParamContains(mrCdBatteryDTO.getWoCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getStatus())) {
        sql += " AND t1.statusWo =:status";
        parameters.put("status", mrCdBatteryDTO.getStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getDischargeConfirm())) {
        sql += " AND LOWER(t1.dischargeConfirm) LIKE :dischargeConfirm ESCAPE '\\' ";
        parameters.put("dischargeConfirm",
            StringUtils.convertLowerParamContains(mrCdBatteryDTO.getDischargeConfirm()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getDistrictName())) {
        sql += " AND LOWER(t1.districtName) LIKE :districtName ESCAPE '\\' ";
        parameters.put("districtName",
            StringUtils.convertLowerParamContains(mrCdBatteryDTO.getDistrictName()));
      }
      if (!StringUtils.isLongNullOrEmpty(mrCdBatteryDTO.getActiveStatus())) {
        sql += " AND NVL(t1.activeStatus,1) =:activeStatus  ";
        parameters.put("activeStatus", mrCdBatteryDTO.getActiveStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getStatusN())) {
        if ("1".equals(mrCdBatteryDTO.getStatusN())) {
          sql += " AND t1.statusN =:statusN";
          parameters.put("statusN", mrCdBatteryDTO.getStatusN());
        } else {
          sql += " AND t1.statusN <>:statusN";
          parameters.put("statusN", 1);
        }
      }
      if ("1".equals(mrCdBatteryDTO.getHaveMr())) {
        sql += " AND t1.mrCode IS NULL AND t1.mrCode IS NULL ";
      }
    }
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
