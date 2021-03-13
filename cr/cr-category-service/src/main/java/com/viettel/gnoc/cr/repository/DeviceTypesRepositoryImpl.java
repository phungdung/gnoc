package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.DeviceTypesDTO;
import com.viettel.gnoc.cr.model.DeviceTypesEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class DeviceTypesRepositoryImpl extends BaseRepository implements DeviceTypesRepository {

  @Override
  public ResultInSideDto insertDeviceTypes(DeviceTypesDTO deviceTypesDTO) {

    return insertByModel(deviceTypesDTO.toEntity(), colId);
  }

  @Override
  public ResultInSideDto updateDeviceTypes(DeviceTypesDTO deviceTypesDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(RESULT.SUCCESS);
    resultDto.setId(deviceTypesDTO.getDeviceTypeId());
    getEntityManager().merge(deviceTypesDTO.toEntity());
    return resultDto;
  }

  @Override
  public DeviceTypesDTO findDeviceTypesById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(DeviceTypesEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteDeviceTypes(Long id) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(deleteById(DeviceTypesEntity.class, id, colId));
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteListDeviceTypes(List<DeviceTypesDTO> deviceTypesListDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    String result = deleteByListDTO(deviceTypesListDTO, DeviceTypesEntity.class, colId);
    resultDto.setKey(result);
    return resultDto;
  }

  @Override
  public Datatable getLisDeviceTypesSearch(DeviceTypesDTO deviceTypesDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DEVICE_TYPES, "getLisDeviceTypesSearch");
    parameters.put("p_system", "OPEN_PM");
    parameters.put("p_bussiness", "OPEN_PM.DEVICE_TYPES");
    parameters.put("p_leeLocale", I18n.getLocale());
    if (!StringUtils.isStringNullOrEmpty(deviceTypesDTO.getDeviceTypeCode())) {
      sql += " and LOWER(DEVICE_TYPE_CODE) like LOWER(:deviceTypeCode) escape '\\'";
      parameters.put("deviceTypeCode",
          StringUtils.convertLowerParamContains(deviceTypesDTO.getDeviceTypeCode()));
    }
    if (!StringUtils.isStringNullOrEmpty(deviceTypesDTO.getDeviceTypeName())) {
      sql += " and LOWER(DEVICE_TYPE_NAME) like LOWER(:deviceTypeName) escape '\\'";
      parameters.put("deviceTypeName",
          StringUtils.convertLowerParamContains(deviceTypesDTO.getDeviceTypeName()));
    }
    if (!StringUtils.isStringNullOrEmpty(deviceTypesDTO.getSearchAll())) {
      sql += " and (LOWER(DEVICE_TYPE_CODE) like LOWER(:searchAll) escape '\\'";
      sql += " or LOWER(DEVICE_TYPE_NAME) like LOWER(:searchAll) escape '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(deviceTypesDTO.getSearchAll()));
    }
    sql += " ORDER by DEVICE_TYPE_ID DESC";
    return getListDataTableBySqlQuery(sql, parameters, deviceTypesDTO.getPage(),
        deviceTypesDTO.getPageSize(),
        DeviceTypesDTO.class, deviceTypesDTO.getSortName(), deviceTypesDTO.getSortType());
  }

  @Override
  public DeviceTypesDTO findDeviceTypesBy(DeviceTypesDTO dto) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append("select * from DEVICE_TYPES a where 1=1 ");

    if (!StringUtils.isStringNullOrEmpty(dto.getDeviceTypeCode())) {
      sql.append(" and a.DEVICE_TYPE_CODE like  :deviceTypeCode");
      parameters.put("deviceTypeCode", dto.getDeviceTypeCode());
    }

    List<DeviceTypesDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(DeviceTypesDTO.class));

    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }

    return null;
  }

  @Override
  public List<DeviceTypesDTO> getDeviceTypesCBB() {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DEVICE_TYPES, "getLisDeviceTypesSearch");
    parameters.put("p_system", "OPEN_PM");
    parameters.put("p_bussiness", "OPEN_PM.DEVICE_TYPES");
    parameters.put("p_leeLocale", I18n.getLocale());
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(DeviceTypesDTO.class));
  }

  private static final String colId = "deviceTypeId";
}
