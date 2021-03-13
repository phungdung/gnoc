package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrDeviceSoftRepository {

  Datatable getListDataMrDeviceSoftSearchWeb(MrDeviceDTO mrDeviceDTO);

  MrDeviceDTO findMrDeviceSoftWeb(Long deviceId);

  MrDeviceDTO findMrDeviceById(Long deviceId);

  ResultInSideDto insertMrDevice(MrDeviceDTO mrDeviceDTO);

  ResultInSideDto updateMrDevice(MrDeviceDTO mrDeviceDTO);

  ResultInSideDto deleteMrDevice(Long deviceId);

  List<MrDeviceDTO> getListMrDeviceSoftExport(MrDeviceDTO mrDeviceDTO);

  ResultInSideDto updateCreateUserSoftByMarket(String createUserSoft, String marketCode);

  MrDeviceDTO checkMrDeviceExit(MrDeviceDTO importDTO);
}
