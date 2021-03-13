package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SmsGatewayDTO;
import java.util.List;

public interface SmsGatewayBusiness {

  Datatable getListSmsGatewayDTO(SmsGatewayDTO smsGatewayDTO);

  List<SmsGatewayDTO> getListSmsGatewayAll(SmsGatewayDTO smsGatewayDTO);

  ResultInSideDto updateSmsGateway(SmsGatewayDTO smsGatewayDTO);

  ResultInSideDto deleteSmsGateway(Long smsGatewayId);

  SmsGatewayDTO findSmsGatewayById(Long smsGatewayId);

  ResultInSideDto insertSmsGateway(SmsGatewayDTO smsGatewayDTO);

  Datatable getListIpccServiceDatatable(IpccServiceDTO ipccServiceDTO);

  List<IpccServiceDTO> getListIpccServiceDTO(IpccServiceDTO ipccServiceDTO);

  ResultInSideDto insertIpccServiceDTO(IpccServiceDTO ipccServiceDTO);

  ResultInSideDto updateIpccServiceDTO(IpccServiceDTO ipccServiceDTO);

  ResultInSideDto deleteIpccServiceDTO(Long ipccServiceId);
}
