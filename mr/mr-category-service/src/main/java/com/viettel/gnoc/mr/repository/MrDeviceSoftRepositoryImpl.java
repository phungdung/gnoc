package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.model.MrDeviceEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrDeviceSoftRepositoryImpl extends BaseRepository implements MrDeviceSoftRepository {

  @Override
  public Datatable getListDataMrDeviceSoftSearchWeb(MrDeviceDTO mrDeviceDTO) {
    BaseDto baseDto = sqlGetListMrDeviceSoftSearchWeb(mrDeviceDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrDeviceDTO.getPage(), mrDeviceDTO.getPageSize(), MrDeviceDTO.class,
        mrDeviceDTO.getSortName(), mrDeviceDTO.getSortType());
    return datatable;
  }

  @Override
  public MrDeviceDTO findMrDeviceSoftWeb(Long deviceId) {
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_SOFT, "find-Mr-Device-Soft-By-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemArray", LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    parameters.put("bussinessArray", APPLIED_BUSSINESS.CAT_ITEM.toString());
    parameters.put("leeLocale", leeLocale);
    parameters.put("categoryCode", MR_ITEM_NAME.MR_SUBCATEGORY);
    parameters.put("deviceId", deviceId);
    List<MrDeviceDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public MrDeviceDTO findMrDeviceById(Long deviceId) {
    MrDeviceEntity entity = getEntityManager().find(MrDeviceEntity.class, deviceId);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertMrDevice(MrDeviceDTO mrDeviceDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(mrDeviceDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateMrDevice(MrDeviceDTO mrDeviceDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(mrDeviceDTO.toEntity());
    resultInSideDto.setId(mrDeviceDTO.getDeviceId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrDevice(Long deviceId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrDeviceEntity entity = getEntityManager().find(MrDeviceEntity.class, deviceId);
    getEntityManager().remove(entity);
    return resultInSideDTO;
  }

  @Override
  public List<MrDeviceDTO> getListMrDeviceSoftExport(MrDeviceDTO mrDeviceDTO) {
    BaseDto baseDto = sqlGetListMrDeviceSoftSearchWeb(mrDeviceDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
  }

  @Override
  public ResultInSideDto updateCreateUserSoftByMarket(String createUserSoft, String marketCode) {
    ResultInSideDto resultDto = new ResultInSideDto();
    String sql = "update MR_DEVICE set CREATE_USER_SOFT = :createUserSoft where MARKET_CODE = :marketCode";
    Query query = getEntityManager().createNativeQuery(sql);
    query.setParameter("createUserSoft", createUserSoft);
    query.setParameter("marketCode", marketCode);
    int result = query.executeUpdate();
    if (result > 0) {
      resultDto.setKey(RESULT.SUCCESS);
    } else {
      resultDto.setKey(RESULT.ERROR);
    }
    return resultDto;
  }

  @Override
  public MrDeviceDTO checkMrDeviceExit(MrDeviceDTO mrDeviceDTO) {
    List<MrDeviceEntity> dataEntity = (List<MrDeviceEntity>) findByMultilParam(
        MrDeviceEntity.class, "marketCode", mrDeviceDTO.getMarketCode(),
        "nodeCode", mrDeviceDTO.getNodeCode());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  private BaseDto sqlGetListMrDeviceSoftSearchWeb(MrDeviceDTO mrDeviceDTO) {
    BaseDto baseDto = new BaseDto();
    String leeLocale = I18n.getLocale();
    String sql;
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getMrSoft()) && "1"
        .equals(mrDeviceDTO.getMrSoft()) && mrDeviceDTO.getIsCompleteSoft() != null && mrDeviceDTO
        .getIsCompleteSoft().equals(1L)) {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_SOFT,
              "sql-Get-List-Mr-Device-Soft-Search-Web-1");
    } else {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE_SOFT,
              "sql-Get-List-Mr-Device-Soft-Search-Web-2");
    }
    parameters.put("systemArray", LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    parameters.put("bussinessArray", APPLIED_BUSSINESS.CAT_ITEM.toString());
    parameters.put("leeLocale", leeLocale);
    parameters.put("categoryCode", MR_ITEM_NAME.MR_SUBCATEGORY);
    //Tim kiem nhanh
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getSearchAll())) {
      sql += " AND (LOWER(d.NODE_CODE) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(d.DEVICE_NAME) LIKE :searchAll ESCAPE '\\')";
      parameters
          .put("searchAll", StringUtils.convertLowerParamContains(mrDeviceDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getMarketCode())) {
      sql += " AND d.MARKET_CODE = :marketCode";
      parameters.put("marketCode", mrDeviceDTO.getMarketCode());
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getArrayCode())) {
      sql += " AND d.ARRAY_CODE = :arrayCode";
      parameters.put("arrayCode", mrDeviceDTO.getArrayCode());
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getNetworkType())) {
      sql += " AND d.NETWORK_TYPE = :networkType";
      parameters.put("networkType", mrDeviceDTO.getNetworkType());
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getDeviceType())) {
      sql += " AND d.DEVICE_TYPE = :deviceType";
      parameters.put("deviceType", mrDeviceDTO.getDeviceType());
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getNodeCode())) {
      sql += " AND LOWER(d.NODE_CODE) LIKE :nodeCode ESCAPE '\\'";
      parameters
          .put("nodeCode", StringUtils.convertLowerParamContains(mrDeviceDTO.getNodeCode()));
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getDeviceName())) {
      sql += " AND LOWER(d.DEVICE_NAME) LIKE :deviceName ESCAPE '\\'";
      parameters
          .put("deviceName", StringUtils.convertLowerParamContains(mrDeviceDTO.getDeviceName()));
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getRegionSoft())) {
      sql += " AND d.REGION_SOFT = :regionSoft";
      parameters.put("regionSoft", mrDeviceDTO.getRegionSoft());
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getRegionHard())) {
      sql += " AND d.REGION_HARD = :regionHard";
      parameters.put("regionHard", mrDeviceDTO.getRegionHard());
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getImplementUnit())) {
      sql += " AND cfg.IMPLEMENT_UNIT = :implementUnit";
      parameters.put("implementUnit", Long.valueOf(mrDeviceDTO.getImplementUnit()));
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getCheckingUnit())) {
      sql += " AND cfg.CHECKING_UNIT = :checkingUnit";
      parameters.put("checkingUnit", Long.valueOf(mrDeviceDTO.getCheckingUnit()));
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getNodeIp())) {
      sql += " AND d.NODE_IP LIKE :nodeIp ESCAPE '\\' ";
      parameters.put("nodeIp", StringUtils.convertLowerParamContains(mrDeviceDTO.getNodeIp()));
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getGroupCode())) {
      sql += " AND d.GROUP_CODE = :groupCode";
      parameters.put("groupCode", mrDeviceDTO.getGroupCode());
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getStatus())) {
      sql += " AND d.STATUS = :status";
      parameters.put("status", mrDeviceDTO.getStatus());
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getVendor())) {
      sql += " AND d.VENDOR = :vendor";
      parameters.put("vendor", mrDeviceDTO.getVendor());
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getMrSoft())) {
      sql += " AND d.MR_SOFT = :mrSoft";
      parameters.put("mrSoft", mrDeviceDTO.getMrSoft());
    }
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getMrHard())) {
      sql += " AND d.MR_HARD = :mrHard";
      parameters.put("mrHFard", mrDeviceDTO.getMrHard());
    }
    if (mrDeviceDTO.getBoUnitSoft() != null) {
      sql += " AND d.BO_UNIT_SOFT = :boUnit";
      parameters.put("boUnit", mrDeviceDTO.getBoUnitSoft());
    }
    if (mrDeviceDTO.getApproveStatusSoft() != null) {
      if (mrDeviceDTO.getApproveStatusSoft() == -1L) {
        sql += " AND d.APPROVE_STATUS_SOFT IS NULL ";
      } else {
        sql += " AND d.APPROVE_STATUS_SOFT = :approveStatus ";
        parameters.put("approveStatus", mrDeviceDTO.getApproveStatusSoft());
      }
    }
    sql += " ORDER BY NLSSORT(d.NODE_CODE, 'NLS_SORT=vietnamese') ASC";

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
