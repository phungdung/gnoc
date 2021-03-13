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
import com.viettel.gnoc.maintenance.dto.MrHardDevicesCheckListDTO;
import com.viettel.gnoc.maintenance.model.MrHardDevicesCheckListEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrHardDeviceCheckListRepositoryImpl extends BaseRepository implements
    MrHardDevicesCheckListRepository {

  @Override
  public Datatable onSearch(MrHardDevicesCheckListDTO mrCheckListDTO) {
    BaseDto baseDto = sqlSearch(mrCheckListDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrCheckListDTO.getPage(),
        mrCheckListDTO.getPageSize(), MrHardDevicesCheckListDTO.class,
        mrCheckListDTO.getSortName(), mrCheckListDTO.getSortType());
  }

  @Override
  public List<MrHardDevicesCheckListDTO> onSearchExport(MrHardDevicesCheckListDTO mrCheckListDTO) {
    BaseDto baseDto = sqlSearch(mrCheckListDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrHardDevicesCheckListDTO.class));
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrHardDevicesCheckListDTO mrCheckListDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrHardDevicesCheckListEntity mrCheckListEntity = getEntityManager()
        .merge(mrCheckListDTO.toEntity());
    resultInSideDto.setId(mrCheckListEntity.getMrHardDevicesCheckListId());
    return resultInSideDto;
  }

  @Override
  public String insertOrUpdateList(List<MrHardDevicesCheckListDTO> listDTO) {
    if (listDTO != null && listDTO.size() > 0) {
      for (MrHardDevicesCheckListDTO checkListDTO : listDTO) {
        insertOrUpdate(checkListDTO);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public ResultInSideDto deleteMrCheckList(Long checkListId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrHardDevicesCheckListEntity entity = getEntityManager()
        .find(MrHardDevicesCheckListEntity.class, checkListId);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }

  @Override
  public MrHardDevicesCheckListDTO getDetail(Long checkListId) {
    MrHardDevicesCheckListEntity mrCheckListEntity = getEntityManager()
        .find(MrHardDevicesCheckListEntity.class, checkListId);
    MrHardDevicesCheckListDTO mrCheckListDTO = mrCheckListEntity.toDTO();
    return mrCheckListDTO;
  }

  @Override
  public List<MrHardDevicesCheckListDTO> getListArrayDeviceTypeNetworkType() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_DEVICES_CHECKLIST,
            "getListArray-DeviceType-NetworkType");
    Map<String, Object> parameters = new HashMap<>();
    sqlQuery += " order by NLSSORT(arrayCode,'NLS_SORT=vietnamese'),networkType,deviceType ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(MrHardDevicesCheckListDTO.class));
  }

  //TrungDuong thêm
  //Hàm check trùng trong trường hợp có trường bị null.
  @Override
  public List<MrHardDevicesCheckListDTO> checkListDTOExisted(
      MrHardDevicesCheckListDTO mrCheckListDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_DEVICES_CHECKLIST,
            "get-mr-hard-devices-checkList-existed");
    Map<String, Object> parameters = new HashMap<>();
    if (mrCheckListDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrCheckListDTO.getMarketCode())) {
        sqlQuery += " AND T1.MARKET_CODE =:marketCode ";
        parameters.put("marketCode", mrCheckListDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCheckListDTO.getArrayCode())) {
        sqlQuery += " AND T1.ARRAY_CODE =:arrayCode ";
        parameters.put("arrayCode", mrCheckListDTO.getArrayCode());
      }
//      if (!StringUtils.isStringNullOrEmpty(mrCheckListDTO.getNetworkType())) {
//        sqlQuery += " AND T1.NETWORK_TYPE =:networkType ";
//        parameters.put("networkType", mrCheckListDTO.getNetworkType());
//      } else {
//        sqlQuery += " AND T1.NETWORK_TYPE is null ";
//      }
      if (!StringUtils.isStringNullOrEmpty(mrCheckListDTO.getDeviceType())) {
        sqlQuery += " AND T1.DEVICE_TYPE =:deviceType ";
        parameters.put("deviceType", mrCheckListDTO.getDeviceType());
      } else {
        sqlQuery += " AND T1.DEVICE_TYPE is null ";
      }
      if (!StringUtils.isStringNullOrEmpty(mrCheckListDTO.getCycle())) {
        sqlQuery += " AND T1.CYCLE =:cycle ";
        parameters.put("cycle", mrCheckListDTO.getCycle());
      }
    }
    List<MrHardDevicesCheckListDTO> listDTOS = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(MrHardDevicesCheckListDTO.class));
    if (listDTOS != null && listDTOS.size() > 0) {
      return listDTOS;
    }
    return null;
  }

  @Override
  public List<MrDeviceDTO> getListNetworkType() {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_DEVICES_CHECKLIST,
              "get-list-network-type");
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MrDeviceDTO> getListDeviceType() {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_DEVICES_CHECKLIST, "get-list-device-type");
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  public BaseDto sqlSearch(MrHardDevicesCheckListDTO dto) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_DEVICES_CHECKLIST,
            "get-List-Mr-hard-devices-CheckList-Page");
    Map<String, Object> params = new HashMap<>();
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getMarketName())) {
        sql += " AND LOWER(cl.location_name) LIKE :marketName ESCAPE '\\'";
        params.put("marketName", StringUtils.convertLowerParamContains(dto.getMarketName()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
        sql += " AND T1.MARKET_CODE =:marketCode ";
        params.put("marketCode", dto.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getArrayCode())) {
        sql += " AND T1.ARRAY_CODE =:arrayCode ";
        params.put("arrayCode", dto.getArrayCode());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
        sql += " AND T1.DEVICE_TYPE =:deviceType ";
        params.put("deviceType", dto.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getNetworkType())) {
        sql += " AND T1.NETWORK_TYPE =:networkType ";
        params.put("networkType", dto.getNetworkType());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getCycle())) {
        sql += " AND T1.CYCLE =:cycle ";
        params.put("cycle", dto.getCycle());
      }
    }
    params.put("p_leeLocale", I18n.getLocale());
    sql += " ORDER BY T1.UPDATED_TIME DESC nulls last";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }
}
