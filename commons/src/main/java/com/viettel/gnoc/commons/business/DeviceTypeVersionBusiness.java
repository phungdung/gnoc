package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.DeviceTypeVersionDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface DeviceTypeVersionBusiness {

  Datatable getListDeviceTypeVersionDTO(DeviceTypeVersionDTO deviceTypeVersionDTO);

  List<DeviceTypeVersionDTO> getListDeviceTypeVersion(DeviceTypeVersionDTO deviceTypeVersionDTO);

  ResultInSideDto insertDeviceTypeVersion(DeviceTypeVersionDTO deviceTypeVersionDTO);

  ResultInSideDto updateDeviceTypeVersion(DeviceTypeVersionDTO deviceTypeVersionDTO);

  DeviceTypeVersionDTO findDeviceTypeVersionById(Long id);

  File exportData(DeviceTypeVersionDTO deviceTypeVersionDTO) throws Exception;

  File getTemplate() throws Exception;

  ResultInSideDto importData(MultipartFile fileImport);
}
