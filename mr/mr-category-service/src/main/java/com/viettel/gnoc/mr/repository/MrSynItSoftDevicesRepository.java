package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import java.util.List;

public interface MrSynItSoftDevicesRepository {

  List<MrSynItDevicesDTO> getListMrSynITDeviceSoftDTO(MrSynItDevicesDTO dto, int rowStart,
      int maxRow);

  List<MrSynItDevicesDTO> getListDeviceTypeByArrayCode(String arrayCode);

  List<MrSynItDevicesDTO> getListDT_AC();

  MrSynItDevicesDTO findMrDeviceByObjectId(MrSynItDevicesDTO dto);

  ResultInSideDto deleteMrSynItSoftDevice(Long id);

  ResultInSideDto updateList(List<MrSynItDevicesDTO> lstMrDeviceDto);

  List<MrSynItDevicesDTO> onSearchEntity(MrSynItDevicesDTO mrDeviceDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  MrSynItDevicesDTO findMrDeviceById(Long mrDeviceId);

  MrSynItDevicesDTO getMrSynItDevicesDetail(Long id);

  ResultInSideDto updateUserSoftByMarket(MrSynItDevicesDTO mrSynItDevicesDTO);

  Datatable getListMrSynItDeviceSoftDTO(MrSynItDevicesDTO mrSynItDevicesDTO);

  List<MrSynItDevicesDTO> getListMrSynItDeviceSoftExport(MrSynItDevicesDTO mrSynItDevicesDTO);

  MrSynItDevicesDTO checkDeviceTypeByArrayCode(String arrayCode, String deviceType);

  MrSynItDevicesDTO ckeckMrSynItDeviceSoftExist(MrSynItDevicesDTO mrSynItDevicesDTO);

  ResultInSideDto updateMrSynItDevice(MrSynItDevicesDTO mrSynItDevicesDTO);

  List<MrSynItDevicesDTO> getDeviceITStationCodeCbb();
}
