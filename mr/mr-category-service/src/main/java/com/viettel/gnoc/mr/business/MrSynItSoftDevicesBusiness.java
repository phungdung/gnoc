package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


public interface MrSynItSoftDevicesBusiness {

  List<MrSynItDevicesDTO> getListMrSynITDeviceSoftDTO(MrSynItDevicesDTO dto, int rowStart,
      int maxRow);

  List<MrSynItDevicesDTO> getListDeviceTypeByArrayCode(String arrayCode);

  MrSynItDevicesDTO findMrDeviceByObjectId(MrSynItDevicesDTO dto);

  ResultInSideDto insertOrUpdateListDevice(List<MrSynItDevicesDTO> lstMrDeviceDto);

  List<MrSynItDevicesDTO> onSearchEntity(MrSynItDevicesDTO mrDeviceDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  MrSynItDevicesDTO getMrSynItDevicesDetail(Long id);

  File ExportData(MrSynItDevicesDTO mrSynItDevicesDTO) throws Exception;

  Datatable getListMrSynItDeviceSoftDTO(MrSynItDevicesDTO mrSynItDevicesDTO);

  ResultInSideDto deleteMrSynItSoftDevice(MrSynItDevicesDTO mrSynItDevicesDTO);

  File getTemplate() throws Exception;

  ResultInSideDto approveItSoftDevice(MrSynItDevicesDTO mrSynItDevicesDTO);

  ResultInSideDto updateMrItSoftDevice(MrSynItDevicesDTO mrSynItDevicesDTO);

  ResultInSideDto importData(MultipartFile multipartFile);

  List<MrSynItDevicesDTO> getDeviceITStationCodeCbb();
}
