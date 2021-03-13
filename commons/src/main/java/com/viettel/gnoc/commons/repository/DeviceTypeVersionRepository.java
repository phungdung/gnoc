package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.DeviceTypeVersionDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTypeVersionRepository {

  Datatable getListDeviceTypeVersionDTO(DeviceTypeVersionDTO deviceTypeVersionDTO);

  List<DeviceTypeVersionDTO> getListDeviceTypeVersion(DeviceTypeVersionDTO deviceTypeVersionDTO);

  ResultInSideDto insertDeviceTypeVersion(DeviceTypeVersionDTO deviceTypeVersionDTO);

  ResultInSideDto updateDeviceTypeVersion(DeviceTypeVersionDTO deviceTypeVersionDTO);

  DeviceTypeVersionDTO findDeviceTypeVersionById(Long id);

  List<DeviceTypeVersionDTO> getDataExportDeviceTypeVersionDTO(
      DeviceTypeVersionDTO dto);

  DeviceTypeVersionDTO checkDeviceTypeVersionExit(
      DeviceTypeVersionDTO deviceTypeVersionDTO);
}
