package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.IpccServiceEntity;
import java.util.List;

public interface IpccServiceRepository {

  BaseDto sqlSearch(IpccServiceDTO ipccServiceDTO);

  List<IpccServiceDTO> getListIpccServiceAll();

  Datatable getListIpccServiceDTOPage(IpccServiceDTO ipccServiceDTO);

  IpccServiceDTO getDeatilIpccServiceById(Long id);

  ResultInSideDto addOrUpdateIpccService(IpccServiceDTO ipccServiceDTO);

  ResultInSideDto deleteIpccService(Long id);

  List<IpccServiceEntity> findByIsDefault(Long isDefault);
}
