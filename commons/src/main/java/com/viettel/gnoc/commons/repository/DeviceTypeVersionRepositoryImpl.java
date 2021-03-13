package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.DeviceTypeVersionDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.DeviceTypeVersionEntity;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class DeviceTypeVersionRepositoryImpl extends BaseRepository implements
    DeviceTypeVersionRepository {

  @Override
  public Datatable getListDeviceTypeVersionDTO(DeviceTypeVersionDTO deviceTypeVersionDTO) {
    BaseDto baseDto = sqlSearch(deviceTypeVersionDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        deviceTypeVersionDTO.getPage(),
        deviceTypeVersionDTO.getPageSize(), DeviceTypeVersionDTO.class,
        deviceTypeVersionDTO.getSortName(), deviceTypeVersionDTO.getSortType());
  }

  @Override
  public List<DeviceTypeVersionDTO> getListDeviceTypeVersion(
      DeviceTypeVersionDTO deviceTypeVersionDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-List-Device-Type-Version-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (deviceTypeVersionDTO.getVendorId() != null) {
      sql += " AND d.VENDOR_ID = :vendorId";
      parameters.put("vendorId", deviceTypeVersionDTO.getVendorId());
    }
    if (deviceTypeVersionDTO.getTypeId() != null) {
      sql += " AND d.TYPE_ID = :typeId";
      parameters.put("typeId", deviceTypeVersionDTO.getTypeId());
    }
    List<DeviceTypeVersionDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(DeviceTypeVersionDTO.class));
    return list;
  }

  @Override
  public ResultInSideDto insertDeviceTypeVersion(DeviceTypeVersionDTO deviceTypeVersionDTO) {
    return insertByModel(deviceTypeVersionDTO.toEntity(), "deviceTypeVersionId");
  }

  @Override
  public ResultInSideDto updateDeviceTypeVersion(DeviceTypeVersionDTO deviceTypeVersionDTO) {
    DeviceTypeVersionEntity entity = deviceTypeVersionDTO.toEntity();
    getEntityManager().merge(entity);
    return new ResultInSideDto(entity.getDeviceTypeVersionId(), RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public DeviceTypeVersionDTO findDeviceTypeVersionById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(DeviceTypeVersionEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public List<DeviceTypeVersionDTO> getDataExportDeviceTypeVersionDTO(
      DeviceTypeVersionDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(), BeanPropertyRowMapper
            .newInstance(DeviceTypeVersionDTO.class));
  }

  @Override
  public DeviceTypeVersionDTO checkDeviceTypeVersionExit(
      DeviceTypeVersionDTO deviceTypeVersionDTO) {
    List<DeviceTypeVersionEntity> dataEntity = (List<DeviceTypeVersionEntity>) findByMultilParam(
        DeviceTypeVersionEntity.class,
        "vendorId", deviceTypeVersionDTO.getVendorId(),
        "typeId", deviceTypeVersionDTO.getTypeId(),
        "softwareVersion", deviceTypeVersionDTO.getSoftwareVersion(),
        "hardwareVersion", deviceTypeVersionDTO.getHardwareVersion());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  private BaseDto sqlSearch(DeviceTypeVersionDTO deviceTypeVersionDTO) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "search-Device-Type-Version-DTO");
    Map<String, Object> parameters = new HashMap<>();
    String locale = I18n.getLocale();
    parameters.put("locale", locale);
    if (deviceTypeVersionDTO.getVendorId() != null) {
      sql += " AND d.VENDOR_ID = :vendorId";
      parameters.put("vendorId", deviceTypeVersionDTO.getVendorId());
    }
    if (deviceTypeVersionDTO.getTypeId() != null) {
      sql += " AND d.TYPE_ID = :typeId";
      parameters.put("typeId", deviceTypeVersionDTO.getTypeId());
    }

    if (StringUtils.isNotNullOrEmpty(deviceTypeVersionDTO.getSoftwareVersion())) {
      sql += " AND lower(d.SOFTWARE_VERSION) LIKE :sw_version ESCAPE '\\' ";
      parameters.put("sw_version",
          StringUtils.convertLowerParamContains(deviceTypeVersionDTO.getSoftwareVersion()));
    }

    if (StringUtils.isNotNullOrEmpty(deviceTypeVersionDTO.getHardwareVersion())) {
      sql += " AND lower(d.HARDWARE_VERSION) LIKE :hw_version ESCAPE '\\' ";
      parameters.put("hw_version",
          StringUtils.convertLowerParamContains(deviceTypeVersionDTO.getHardwareVersion()));
    }

    if (StringUtils.isNotNullOrEmpty(deviceTypeVersionDTO.getTemp())) {
      sql += " AND d.TEMP = :temp";
      parameters.put("temp", deviceTypeVersionDTO.getTemp());
    }

    if (deviceTypeVersionDTO.getSubTypeId() != null) {
      sql += " AND e.PARENT_ITEM_ID = :subType";
      parameters.put("subType", deviceTypeVersionDTO.getSubTypeId());
    }

    if (StringUtils.isNotNullOrEmpty(deviceTypeVersionDTO.getSearchAll())) {
      sql += " AND (lower(d.TEMP) LIKE :searchAll ESCAPE '\\'  ";
      sql += " OR lower(d.SOFTWARE_VERSION) LIKE :searchAll ESCAPE '\\'  ";
      sql += " OR lower(d.HARDWARE_VERSION) LIKE :searchAll ESCAPE '\\'  ";
      sql += "  ) ";
      parameters
          .put("searchAll",
              StringUtils.convertLowerParamContains(deviceTypeVersionDTO.getSearchAll()));
    }

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

}
