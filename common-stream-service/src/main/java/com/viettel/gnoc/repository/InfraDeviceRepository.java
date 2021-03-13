package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import java.util.List;

public interface InfraDeviceRepository {

  List<InfraDeviceDTO> getListInfraDeviceIp(InfraDeviceDTO infraDeviceDTO);

  List<InfraDeviceDTO> getListInfraDeviceIpV2(InfraDeviceDTO infraDeviceDTO);

  List<InfraDeviceDTO> getListInfraDeviceIpV2SrProxy(InfraDeviceDTO infraDeviceDTO);

  List<InfraDeviceDTO> geInfraDeviceByIps(List<String> lstIp, String nationCode);

  List<InfraDeviceDTO> getListInfraDeviceNonIp(InfraDeviceDTO infraDeviceDTO);

  List<InfraDeviceDTO> getListInfraDeviceDTOV2(InfraDeviceDTO infraDeviceDTO);
}
