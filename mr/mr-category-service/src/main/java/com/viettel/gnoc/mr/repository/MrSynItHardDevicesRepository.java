package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import java.util.List;

public interface MrSynItHardDevicesRepository {

  List<MrSynItDevicesDTO> getListMrSynITDeviceSoftDTO(MrSynItDevicesDTO dto, int rowStart,
      int maxRow);

  BaseDto sqlSearch(MrSynItDevicesDTO mrSynItDevicesDTO);

  Datatable getListMrSynITDeviceHardPage(MrSynItDevicesDTO dto);

  List<MrSynItDevicesDTO> getListMrSynItDeviceHardExport(MrSynItDevicesDTO dto);

  MrSynItDevicesDTO getMrSynItDevicesHardDetail(Long id);

  List<MrSynItDevicesDTO> getListDeviceTypeByArrayCode(String arrayCode);

  MrSynItDevicesDTO findMrDeviceByObjectId(MrSynItDevicesDTO dto);

  ResultInSideDto updateList(List<MrSynItDevicesDTO> lstMrDeviceDto);

  ResultInSideDto add(MrSynItDevicesDTO mrSynItDevicesDTO);

  ResultInSideDto edit(MrSynItDevicesDTO mrSynItDevicesDTO);

  ResultInSideDto delete(Long id);

  ResultInSideDto updateMrSynItDevice(MrSynItDevicesDTO mrSynItDevicesDTO);

  ResultInSideDto updateCreateUserByMarket(MrCfgMarketDTO mrCfgMarketDTO, String type);
}
