package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.WebServiceDTO;

public interface CfgWebServiceBusiness {

  WebServiceDTO findWebServiceById(Long id);

  ResultInSideDto insertWebService(WebServiceDTO webServiceDTO);

  String deleteWebServiceById(Long id);

  String updateWebService(WebServiceDTO dto);

  Datatable getListWebService(WebServiceDTO webServiceDTO);
}
