package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import java.util.List;
import java.util.Map;

public interface MrDeviceRepository {

  MrDeviceDTO findMrDeviceById(Long mrDeviceId);

  ResultInSideDto updateMrDeviceServices(MrDeviceDTO mrDeviceDTO);

  List<MrDeviceDTO> getListDeviceTypeByNetworkType(String arrayCode, String networkType);

  List<MrDeviceDTO> getListNetworkTypeByArrayCode(String arrayCode);

  Datatable getListMrDeviceSoftDTO(MrDeviceDTO mrDeviceDTO);

  List<MrDeviceDTO> getListRegionByMarketCode(String marketCode);

  ResultInSideDto edit(MrDeviceDTO mrDeviceDTO);

  MrDeviceDTO getDetail(Long deviceId);

  ResultInSideDto delete(Long deviceId);

  ResultInSideDto updateCreateUserByMarket(MrCfgMarketDTO mrCfgMarketDTO, String type);

  List<MrDeviceDTO> getListDataExport(MrDeviceDTO mrDeviceDTO);

  List<String> getListRegionSoftByMarketCode(String marketCode);

  List<String> getNetworkTypeByArrayCode(String arrayCode);

  List<String> getDeviceTypeByNetworkType(String arrayCode, String networkType);

  List<MrDeviceDTO> onSearchEntity(MrDeviceDTO mrDeviceDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  ResultInSideDto updateList(List<MrDeviceDTO> lstMrDeviceDto);

  List<MrDeviceDTO> getDeviceStationCodeCbb();

  MrDeviceDTO ckeckMrDeviceHardExist(MrDeviceDTO mrDeviceDTO);

  MrDeviceDTO checkNetworkTypeByArrayCode(String arrayCode, String networkType);

  MrDeviceDTO checkDeviceTypeByArrayNet(String arrayCode, String networkType, String deviceType);

  List<MrDeviceDTO> getMrDeviceByA_N_T();

  Map<String, MrDeviceDTO> getMapMrDevice(List<String> deviceIds);

  void getMapArrayCode(List<String> arrayCodes, Map<String, String> mapA_N,
      Map<String, String> mapA_N_D);

  void getMapNetWorkType(List<String> networkTypes, Map<String, String> mapA_N,
      Map<String, String> mapA_N_D);

  void getMapDeviceType(List<String> deviceTypes, Map<String, String> mapA_N,
      Map<String, String> mapA_N_D);

  List<MrDeviceDTO> getListDevice();
}
