package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.DeviceTypesDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTypesRepository {

  ResultInSideDto insertDeviceTypes(DeviceTypesDTO deviceTypesDTO);

  ResultInSideDto updateDeviceTypes(DeviceTypesDTO deviceTypesDTO);

  DeviceTypesDTO findDeviceTypesById(Long id);

  ResultInSideDto deleteDeviceTypes(Long id);

  ResultInSideDto deleteListDeviceTypes(List<DeviceTypesDTO> deviceTypesListDTO);

  Datatable getLisDeviceTypesSearch(DeviceTypesDTO deviceTypesDTO);

  DeviceTypesDTO findDeviceTypesBy(DeviceTypesDTO deviceTypesDTO);

  List<DeviceTypesDTO> getDeviceTypesCBB();
}
