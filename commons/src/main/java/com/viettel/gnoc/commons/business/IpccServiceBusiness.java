package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.util.List;

public interface IpccServiceBusiness {

  List<IpccServiceDTO> getListIpccServiceAll();

  Datatable getListIpccServiceDTOPage(IpccServiceDTO ipccServiceDTO);

  IpccServiceDTO getDeatilIpccServiceById(Long id);

  ResultInSideDto addOrUpdateIpccService(IpccServiceDTO ipccServiceDTO);

  ResultInSideDto deleteIpccService(Long id);
}
