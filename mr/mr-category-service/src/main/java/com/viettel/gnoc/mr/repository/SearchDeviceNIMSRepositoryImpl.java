package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.SearchDeviceNIMSDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SearchDeviceNIMSRepositoryImpl extends BaseRepository implements
    SearchDeviceNIMSRepository {

  @Override
  public Datatable getListSearchDeviceNIMS(SearchDeviceNIMSDTO searchDeviceNIMSDTO) {
    BaseDto baseDto = sqlGetListSearchDeviceNIMS(searchDeviceNIMSDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        searchDeviceNIMSDTO.getPage(),
        searchDeviceNIMSDTO.getPageSize(), SearchDeviceNIMSDTO.class,
        searchDeviceNIMSDTO.getSortName(), searchDeviceNIMSDTO.getSortType());
  }

  @Override
  public List<SearchDeviceNIMSDTO> getComboboxNetworkClass() {
    BaseDto baseDto = sqlGetComboboxNetworkClass();
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(SearchDeviceNIMSDTO.class));
  }

  @Override
  public List<SearchDeviceNIMSDTO> getComboboxNetworkType() {
    BaseDto baseDto = sqlGetComboboxNetworkType();
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(SearchDeviceNIMSDTO.class));
  }

  @Override
  public List<SearchDeviceNIMSDTO> onSearchExport(SearchDeviceNIMSDTO searchDeviceNIMSDTO) {
    BaseDto baseDto = sqlGetListSearchDeviceNIMS(searchDeviceNIMSDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(SearchDeviceNIMSDTO.class));
  }

  public BaseDto sqlGetComboboxNetworkType() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_SEARCH_DEVICE_ON_NIMS, "get-combo-box-network-type");
    Map<String, Object> parameters = new HashMap<>();
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public BaseDto sqlGetComboboxNetworkClass() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_SEARCH_DEVICE_ON_NIMS, "get-combo-box-network-class");
    Map<String, Object> parameters = new HashMap<>();
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }


  public BaseDto sqlGetListSearchDeviceNIMS(SearchDeviceNIMSDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_SEARCH_DEVICE_ON_NIMS, "get-list-search-device-NIMS");
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {
      //Tim kiem nhanh
      if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
        sql += " AND (LOWER(d.DEVICE_CODE) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(d.DEVICE_NAME) LIKE :searchAll ESCAPE '\\')";
        parameters
            .put("searchAll", StringUtils.convertLowerParamContains(dto.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getNetworkType())) {
        sql += " AND LOWER(d.NETWORK_TYPE) LIKE :networkType ESCAPE '\\' ";
        parameters.put("networkType",
            StringUtils.convertLowerParamContains(dto.getNetworkType()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getNetworkClass())) {
        sql += " AND LOWER(d.NETWORK_CLASS) LIKE :networkClass ESCAPE '\\' ";
        parameters.put("networkClass",
            StringUtils.convertLowerParamContains(dto.getNetworkClass()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getIpNode())) {
        sql += " AND LOWER(ip.IP) LIKE :ipNode ESCAPE '\\' ";
        parameters
            .put("ipNode", StringUtils.convertLowerParamContains(dto.getIpNode()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getDeviceCode())) {
        sql += " AND LOWER(d.DEVICE_CODE) LIKE :deviceCode ESCAPE '\\' ";
        parameters.put("deviceCode",
            StringUtils.convertLowerParamContains(dto.getDeviceCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getDeviceName())) {
        sql += " AND LOWER(d.DEVICE_NAME) LIKE :deviceName ESCAPE '\\' ";
        parameters.put("deviceName",
            StringUtils.convertLowerParamContains(dto.getDeviceName()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getDateFrom())) {
        sql += " AND d.CREATE_DATE >= TO_DATE(:dateFrom,'dd/MM/yyyy HH24:mi:ss') ";
        parameters.put("dateFrom", dto.getDateFrom());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getDateTo())) {
        sql += " AND d.CREATE_DATE <= TO_DATE(:dateTo,'dd/MM/yyyy HH24:mi:ss') ";
        parameters.put("dateTo", dto.getDateTo());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getStationName())) {
        sql += " AND LOWER(s.STATION_NAME) LIKE :stationName ESCAPE '\\' ";
        parameters.put("stationName",
            StringUtils.convertLowerParamContains(dto.getStationName()));
      }
    }
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
