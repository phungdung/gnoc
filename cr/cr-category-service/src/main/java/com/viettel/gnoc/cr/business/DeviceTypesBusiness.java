package com.viettel.gnoc.cr.business;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.DeviceTypesDTO;
import java.io.File;
import java.util.List;

public interface DeviceTypesBusiness {

  ResultInSideDto insertDeviceTypes(DeviceTypesDTO deviceTypesDTO);

  ResultInSideDto updateDeviceTypes(DeviceTypesDTO deviceTypesDTO);

  DeviceTypesDTO findDeviceTypesById(Long id);

  ResultInSideDto deleteDeviceTypes(Long id);

  ResultInSideDto deleteListDeviceTypes(List<DeviceTypesDTO> deviceTypesListDTO);

  Datatable getLisDeviceTypesSearch(DeviceTypesDTO deviceTypesDTO);

  File exportData(DeviceTypesDTO deviceTypesDTO) throws Exception;
}
