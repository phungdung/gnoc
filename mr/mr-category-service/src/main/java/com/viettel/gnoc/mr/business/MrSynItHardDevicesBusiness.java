package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


public interface MrSynItHardDevicesBusiness {

  List<MrSynItDevicesDTO> getListMrSynITDeviceHardDTO(MrSynItDevicesDTO dto, int rowStart,
      int maxRow);

  Datatable getListMrSynITDeviceHardPage(MrSynItDevicesDTO dto);

  MrSynItDevicesDTO getMrSynItDevicesHardDetail(Long id);

  List<MrSynItDevicesDTO> getListDeviceTypeByArrayCode(String arrayCode);

  MrSynItDevicesDTO findMrDeviceByObjectId(MrSynItDevicesDTO dto);

  ResultInSideDto insertOrUpdateListDevice(List<MrSynItDevicesDTO> lstMrDeviceDto);

  File ExportData(MrSynItDevicesDTO mrSynItDevicesDTO) throws Exception;

  ResultInSideDto deleteMrSynItHardDevice(MrSynItDevicesDTO mrSynItDevicesDTO);

  File getTemplate() throws Exception;

  ResultInSideDto approveItHardDevice(MrSynItDevicesDTO mrSynItDevicesDTO);

  ResultInSideDto updateMrSynItHardDevice(MrSynItDevicesDTO mrSynItDevicesDTO);

  List<MrITHardScheduleDTO> getListRegionByMrSynItHardDevices(String country);

  ResultInSideDto importData(MultipartFile multipartFile);
}
