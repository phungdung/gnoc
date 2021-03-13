package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.WebServiceDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgWebServiceRepository {

  WebServiceDTO findWebServiceById(Long id);

  ResultInSideDto insertWebService(WebServiceDTO webServiceDTO);

  String deleteWebServiceById(Long id);

  String updateWebService(WebServiceDTO dto);

  Datatable getListWebService(WebServiceDTO dto);
}
