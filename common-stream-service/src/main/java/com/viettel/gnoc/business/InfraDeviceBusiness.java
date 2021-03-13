package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import java.util.List;

public interface InfraDeviceBusiness {

  List<InfraDeviceDTO> getListInfraDeviceIp(InfraDeviceDTO infraDeviceDTO);

  Datatable getDatatableInfraDeviceIp(InfraDeviceDTO infraDeviceDTO);

  List<InfraDeviceDTO> getListInfraDeviceIpV2(InfraDeviceDTO infraDeviceDTO);

  List<InfraDeviceDTO> getListInfraDeviceIpV2SrProxy(InfraDeviceDTO infraDeviceDTO);

  Datatable getDatatableInfraDeviceIpV2(InfraDeviceDTO infraDeviceDTO);

  Datatable getDatatableInfraDeviceIpForCr(InfraDeviceDTO infraDeviceDTO);

  List<InfraDeviceDTO> geInfraDeviceByIps(List<String> lstIp, String nationCode);

  List<InfraDeviceDTO> getListInfraDeviceNonIp(InfraDeviceDTO infraDeviceDTO);

  List<InfraDeviceDTO> getListInfraDeviceDTOV2(InfraDeviceDTO infraDeviceDTO);
}
