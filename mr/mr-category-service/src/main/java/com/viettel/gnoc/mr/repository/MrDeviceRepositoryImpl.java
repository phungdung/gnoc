package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.model.MrDeviceEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrDeviceRepositoryImpl extends BaseRepository implements MrDeviceRepository {

  @Override
  public MrDeviceDTO findMrDeviceById(Long mrDeviceId) {
    if (!StringUtils.isStringNullOrEmpty(mrDeviceId)) {
      List<MrDeviceEntity> lst = findByMultilParam(MrDeviceEntity.class, "deviceId", mrDeviceId);
      return lst.isEmpty() ? null : lst.get(0).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto updateMrDeviceServices(MrDeviceDTO mrDeviceDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrDeviceEntity entity = getEntityManager().merge(mrDeviceDTO.toEntity());
    resultInSideDto.setId(entity.getDeviceId());
    return resultInSideDto;
  }

  @Override
  public List<MrDeviceDTO> getListDeviceTypeByNetworkType(String arrayCode, String networkType) {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE, "get-list-device-type-by-network-type");
      params.put("arrayCode", arrayCode);
      params.put("networkType", networkType);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }


  @Override
  public List<MrDeviceDTO> getListNetworkTypeByArrayCode(String arrayCode) {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE, "get-list-network-type-by-arraycode");
      params.put("arrayCode", arrayCode);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public Datatable getListMrDeviceSoftDTO(MrDeviceDTO mrDeviceDTO) {
    BaseDto baseDto = sqlSearch(mrDeviceDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrDeviceDTO.getPage(), mrDeviceDTO
            .getPageSize(), MrDeviceDTO.class, mrDeviceDTO.getSortName(),
        mrDeviceDTO.getSortType());
  }

  @Override
  public ResultInSideDto edit(MrDeviceDTO mrDeviceDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(mrDeviceDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public MrDeviceDTO getDetail(Long deviceId) {
    MrDeviceEntity mrDeviceEntity = getEntityManager()
        .find(MrDeviceEntity.class, deviceId);
    MrDeviceDTO mrHardGroupConfigDTO = mrDeviceEntity.toDTO();
    return mrHardGroupConfigDTO;
  }

  @Override
  public ResultInSideDto delete(Long deviceId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrDeviceEntity mrDeviceEntity = getEntityManager()
        .find(MrDeviceEntity.class, deviceId);
    if (mrDeviceEntity != null) {
      getEntityManager().remove(mrDeviceEntity);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCreateUserByMarket(MrCfgMarketDTO mrCfgMarketDTO, String type) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String sql = "";
    Map<String, Object> parameters = new HashMap<>();
    if (mrCfgMarketDTO != null && type != null) {
      if ("BDM".equals(type)) {
        sql = "UPDATE MR_DEVICE SET CREATE_USER_SOFT = :createdUser where MARKET_CODE = :marketCode ";
        parameters.put("createdUser", mrCfgMarketDTO.getCreatedUserSoftName());
      } else {
        sql = "UPDATE MR_DEVICE SET CREATE_USER_HARD = :createdUser where MARKET_CODE = :marketCode ";
        parameters.put("createdUser", mrCfgMarketDTO.getCreatedUserHardName());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCfgMarketDTO.getMarketCode())) {
        parameters.put("marketCode", mrCfgMarketDTO.getMarketCode());
      }
    }
    int row = getNamedParameterJdbcTemplate().update(sql, parameters);
    resultInSideDto.setQuantitySucc(row);
    return resultInSideDto;
  }

  @Override
  public List<MrDeviceDTO> getListDataExport(
      MrDeviceDTO mrDeviceDTO) {
    BaseDto baseDto = sqlSearch(mrDeviceDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
  }

  @Override
  public List<String> getListRegionSoftByMarketCode(String marketCode) {
    if (StringUtils.isNotNullOrEmpty(marketCode)) {
      List<String> list = new ArrayList<>();
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE,
          "get-List-Region-Soft-By-Market-Code");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("marketCode", marketCode);
      List<MrDeviceDTO> listMrDevice = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
      if (listMrDevice != null && listMrDevice.size() > 0) {
        for (MrDeviceDTO dto : listMrDevice) {
          list.add(dto.getRegionSoft());
        }
      }
      return list;
    }
    return null;
  }

  @Override
  public List<String> getNetworkTypeByArrayCode(String arrayCode) {
    if (StringUtils.isNotNullOrEmpty(arrayCode)) {
      List<String> list = new ArrayList<>();
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE,
          "get-Network-Type-By-Array-Code");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("arrayCode", arrayCode);
      List<MrDeviceDTO> listMrDevice = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
      if (listMrDevice != null && listMrDevice.size() > 0) {
        for (MrDeviceDTO dto : listMrDevice) {
          list.add(dto.getNetworkType());
        }
      }
      return list;
    }
    return null;
  }

  @Override
  public List<String> getDeviceTypeByNetworkType(String arrayCode, String networkType) {
    List<String> list = new ArrayList<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE,
        "get-Device-Type-By-Network-Type");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("networkType", networkType);
    if (StringUtils.isNotNullOrEmpty(arrayCode)) {
      sql += " AND ARRAY_CODE = :arrayCode";
      parameters.put("arrayCode", arrayCode);
    }
    sql += " ORDER BY DEVICE_TYPE";
    List<MrDeviceDTO> listMrDevice = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    if (listMrDevice != null && listMrDevice.size() > 0) {
      for (MrDeviceDTO dto : listMrDevice) {
        list.add(dto.getDeviceType());
      }
    }
    return list;
  }

  public BaseDto sqlSearch(MrDeviceDTO mrDeviceDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = null;
    if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getIsCompleteHard())) {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE,
              "getListMrDeviceSoftDTO");
    } else {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE,
              "getListMrDeviceHardDTO");
    }
    Map<String, Object> parameters = new HashMap<>();
    if (mrDeviceDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getSearchAll())) {
        sqlQuery += " AND (lower(T1.DEVICE_NAME) LIKE :searchAll ESCAPE '\\' OR lower(T1.NODE_CODE) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(mrDeviceDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMarketCode())) {
        sqlQuery += " AND T1.MARKET_CODE = :marketCode ";
        parameters.put("marketCode", mrDeviceDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getRegionHard())) {
        sqlQuery += " AND T1.REGION_HARD = :regionHard ";
        parameters.put("regionHard", mrDeviceDTO.getRegionHard());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getArrayCode())) {
        sqlQuery += " AND T1.ARRAY_CODE = :arrayCode ";
        parameters.put("arrayCode", mrDeviceDTO.getArrayCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getNetworkType())) {
        sqlQuery += " AND T1.NETWORK_TYPE = :networkType ";
        parameters.put("networkType", mrDeviceDTO.getNetworkType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getDeviceType())) {
        sqlQuery += " AND T1.DEVICE_TYPE = :deviceType ";
        parameters.put("deviceType", mrDeviceDTO.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getNodeIp())) {
        sqlQuery += " AND (lower(T1.NODE_IP) LIKE :nodeIp ESCAPE '\\' )";
        parameters.put("nodeIp", StringUtils.convertLowerParamContains(mrDeviceDTO.getNodeIp()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getNodeCode())) {
        sqlQuery += " AND lower(T1.NODE_CODE) LIKE :nodeCode ESCAPE '\\' ";
        parameters
            .put("nodeCode", StringUtils.convertLowerParamContains(mrDeviceDTO.getNodeCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getDeviceName())) {
        sqlQuery += " AND (lower(T1.DEVICE_NAME) LIKE :deviceName ESCAPE '\\' )";
        parameters
            .put("deviceName", StringUtils.convertLowerParamContains(mrDeviceDTO.getDeviceName()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getVendor())) {
        sqlQuery += " AND (lower(T1.VENDOR) LIKE :vendor ESCAPE '\\' )";
        parameters
            .put("vendor", StringUtils.convertLowerParamContains(mrDeviceDTO.getVendor()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getStationCode())) {
        sqlQuery += " AND (lower(T1.STATION_CODE) LIKE :stationCode ESCAPE '\\' )";
        parameters.put("stationCode",
            StringUtils.convertLowerParamContains(mrDeviceDTO.getStationCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrHard())) {
        sqlQuery += " AND T1.MR_HARD = :mrHard ";
        parameters.put("mrHard", mrDeviceDTO.getMrHard());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getIsCompleteHard())) {
        sqlQuery += " AND T2.DEVICE_ID is null ";
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getBoUnitHard())) {
        sqlQuery += " AND T1.BO_UNIT_HARD = :boUnitHard ";
        parameters.put("boUnitHard", mrDeviceDTO.getBoUnitHard());
      }
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getApproveStatusHard())) {
        if (mrDeviceDTO.getApproveStatusHard() == -1L) {
          sqlQuery += " AND T1.APPROVE_STATUS_HARD IS NULL ";
        } else {
          sqlQuery += " AND T1.APPROVE_STATUS_HARD = :approveStatusHard ";
          parameters.put("approveStatusHard", mrDeviceDTO.getApproveStatusHard());
        }
      }
    }
    sqlQuery += " ORDER BY T1.DEVICE_ID DESC ";
    parameters.put("p_leeLocale", I18n.getLocale());
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<MrDeviceDTO> getListRegionByMarketCode(String marketCode) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE,
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
  public List<MrDeviceDTO> onSearchEntity(MrDeviceDTO mrDeviceDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return onSearchEntity(MrDeviceEntity.class, mrDeviceDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public ResultInSideDto updateList(List<MrDeviceDTO> lstMrDeviceDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (lstMrDeviceDto != null && !lstMrDeviceDto.isEmpty()) {
      for (MrDeviceDTO dto : lstMrDeviceDto) {
        getEntityManager().merge(dto.toEntity());
      }
    }
    return resultInSideDto;
  }

  @Override
  public List<MrDeviceDTO> getDeviceStationCodeCbb() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE, "getDeviceStationCode");
    List<MrDeviceDTO> data = getNamedParameterJdbcTemplate()
        .query(sql, new HashMap<>(), BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    if (data != null && data.size() > 0) {
      return data;
    }
    return new ArrayList<>();
  }

  @Override
  public MrDeviceDTO ckeckMrDeviceHardExist(MrDeviceDTO mrDeviceDTO) {
    List<MrDeviceEntity> lstMrDeviceHard = findByMultilParam(
        MrDeviceEntity.class, "marketCode", mrDeviceDTO.getMarketCode()
        , "nodeCode", mrDeviceDTO.getNodeCode());
    if (lstMrDeviceHard != null && !lstMrDeviceHard.isEmpty()) {
      return lstMrDeviceHard.get(0).toDTO();
    }
    return null;
  }

  @Override
  public MrDeviceDTO checkNetworkTypeByArrayCode(String arrayCode, String networkType) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE, "checkNetworkTypeByArrayCode");
    params.put("arrayCode", arrayCode);
    params.put("networkType", networkType);
    List<MrDeviceDTO> mrDeviceDTOList = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    return mrDeviceDTOList.isEmpty() ? null : mrDeviceDTOList.get(0);
  }

  @Override
  public MrDeviceDTO checkDeviceTypeByArrayNet(String arrayCode, String networkType,
      String deviceType) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE, "checkDeviceTypeByArrayNet");
    params.put("arrayCode", arrayCode);
    params.put("networkType", networkType);
    params.put("deviceType", deviceType);
    List<MrDeviceDTO> mrDeviceDTOList = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    return mrDeviceDTOList.isEmpty() ? null : mrDeviceDTOList.get(0);
  }


  @Override
  public List<MrDeviceDTO> getMrDeviceByA_N_T() {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE, "get-all-device-type");
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  public List getListDataByIdx(Class<?> mapperClass, String sql, List<String> lstStrIdx,
      List<Long> lstLongIdx, int sizing) {
    return getListDataByParamList(mapperClass, sql, lstStrIdx, lstLongIdx, sizing);
  }

  @Override
  public Map<String, MrDeviceDTO> getMapMrDevice(List<String> deviceIds) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE, "get-device-type-as-entity");
    sql += " AND DEVICE_ID IN (:idx) ";
    List<MrDeviceDTO> lst = getListDataByIdx(MrDeviceDTO.class, sql, deviceIds, null, 500);
    if (lst != null && lst.size() > 0) {
      Map<String, MrDeviceDTO> map = new HashMap<>();
      lst.forEach(item -> {
        map.put(item.getDeviceIdStr(), item);
      });
      return map;
    } else {
      return new HashMap<>();
    }
  }

  @Override
  public void getMapArrayCode(List<String> arrayCodes, Map<String, String> mapA_N,
      Map<String, String> mapA_N_D) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE, "get-device-type-as-entity");
    sql += " AND ARRAY_CODE IN (:idx) ";
    List<MrDeviceDTO> lst = getListDataByIdx(MrDeviceDTO.class, sql, arrayCodes, null, 200);
    if (lst != null && lst.size() > 0) {
      lst.forEach(item -> {
        mapA_N
            .put(String.valueOf(item.getArrayCode()) + ";" + String.valueOf(item.getNetworkType()),
                item.getDeviceIdStr());
        mapA_N_D.put(
            String.valueOf(item.getArrayCode()) + ";" + String.valueOf(item.getNetworkType()) + ";"
                + String.valueOf(item.getDeviceType()), item.getDeviceIdStr());
      });
    }
  }

  @Override
  public void getMapNetWorkType(List<String> networkTypes, Map<String, String> mapA_N,
      Map<String, String> mapA_N_D) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE, "get-device-type-as-entity");
    sql += " AND NETWORK_TYPE IN (:idx) ";
    List<MrDeviceDTO> lst = getListDataByIdx(MrDeviceDTO.class, sql, networkTypes, null, 200);
    if (lst != null && lst.size() > 0) {
      lst.forEach(item -> {
        mapA_N
            .put(String.valueOf(item.getArrayCode()) + ";" + String.valueOf(item.getNetworkType()),
                item.getDeviceIdStr());
        mapA_N_D.put(
            String.valueOf(item.getArrayCode()) + ";" + String.valueOf(item.getNetworkType()) + ";"
                + String.valueOf(item.getDeviceType()), item.getDeviceIdStr());
      });

    }
  }

  @Override
  public void getMapDeviceType(List<String> deviceTypes, Map<String, String> mapA_N,
      Map<String, String> mapA_N_D) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE, "get-device-type-as-entity");
    sql += " AND DEVICE_TYPE IN (:idx) ";
    List<MrDeviceDTO> lst = getListDataByIdx(MrDeviceDTO.class, sql, deviceTypes, null, 200);
    if (lst != null && lst.size() > 0) {
      lst.forEach(item -> {
        mapA_N
            .put(String.valueOf(item.getArrayCode()) + ";" + String.valueOf(item.getNetworkType()),
                item.getDeviceIdStr());
        mapA_N_D.put(
            String.valueOf(item.getArrayCode()) + ";" + String.valueOf(item.getNetworkType()) + ";"
                + String.valueOf(item.getDeviceType()), item.getDeviceIdStr());
      });
    }
  }

  @Override
  public List<MrDeviceDTO> getListDevice() {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_DEVICE, "get-list-device");
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }
}
