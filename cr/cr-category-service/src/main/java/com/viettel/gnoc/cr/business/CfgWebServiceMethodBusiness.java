package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.WebServiceMethodDTO;
import java.util.List;

public interface CfgWebServiceMethodBusiness {

  WebServiceMethodDTO findWebServiceMethodById(Long id);

  ResultInSideDto insertWebServiceMethod(WebServiceMethodDTO webServiceMethodDTO);

  String deleteWebServiceMethodById(Long id);

  String updateWebServiceMethod(WebServiceMethodDTO dto);

  List<WebServiceMethodDTO> getListWebServiceMethod();

  String deleteListWebServiceMethodByWebServiceId(List<WebServiceMethodDTO> dto);
}
