package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsGatewayRepository {

  List<IpccServiceDTO> getListIpccServiceDTO(IpccServiceDTO ipccServiceDTO);
}
