package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.WebServiceDTO;
import com.viettel.gnoc.cr.dto.WebServiceMethodDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgWebServiceMethodRepository {

  WebServiceMethodDTO findWebServiceMethodById(Long id);

  ResultInSideDto insertWebServiceMethod(WebServiceMethodDTO webServiceMethodDTO);

  String deleteWebServiceMethodById(Long id);

  String updateWebServiceMethod(WebServiceMethodDTO dto);

  List<WebServiceMethodDTO> getListWebServiceMethod();

  String deleteListWebServiceMethodByWebServiceId(List<WebServiceMethodDTO> dto);

  WebServiceDTO findWebServiceById(Long id);

  List<WebServiceMethodDTO> getWebServiceMethodDTOS(Long id);
}
