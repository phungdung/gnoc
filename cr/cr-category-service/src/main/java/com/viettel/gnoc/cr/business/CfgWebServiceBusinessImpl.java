package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.WebServiceDTO;
import com.viettel.gnoc.cr.dto.WebServiceMethodDTO;
import com.viettel.gnoc.cr.repository.CfgWebServiceMethodRepository;
import com.viettel.gnoc.cr.repository.CfgWebServiceRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class CfgWebServiceBusinessImpl implements CfgWebServiceBusiness {

  @Autowired
  private CfgWebServiceRepository cfgWebServiceRepository;

  @Autowired
  private CfgWebServiceMethodRepository cfgWebServiceMethodRepository;

  @Override
  public WebServiceDTO findWebServiceById(Long id) {
    log.debug("Request to findWebServiceById : {}", id);
    WebServiceDTO webServiceDTO = cfgWebServiceMethodRepository.findWebServiceById(id);
    return webServiceDTO;
  }

  @Override
  public ResultInSideDto insertWebService(WebServiceDTO webServiceDTO) {
    log.debug("Request to insertWebService : {}", webServiceDTO);
    try {
      ResultInSideDto resultInSideDto = cfgWebServiceRepository.insertWebService(webServiceDTO);
      if (!webServiceDTO.getWebServiceMethodDTOS().isEmpty()) {
        List<WebServiceMethodDTO> webServiceMethodDTOS = webServiceDTO.getWebServiceMethodDTOS();
        for (WebServiceMethodDTO dto : webServiceMethodDTOS) {
          dto.setWebServiceId(resultInSideDto.getId());
          cfgWebServiceMethodRepository.insertWebServiceMethod(dto);
        }
      }
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public String deleteWebServiceById(Long id) {
    log.debug("Request to insertWebService : {}", id);
    List<WebServiceMethodDTO> webServiceMethodDTOS;
    if (id != null && id > 0) {
      webServiceMethodDTOS = cfgWebServiceMethodRepository.getWebServiceMethodDTOS(id);
      for (WebServiceMethodDTO dto : webServiceMethodDTOS) {
        cfgWebServiceMethodRepository.deleteWebServiceMethodById(dto.getWebServiceMethodId());
      }
    }
    return cfgWebServiceRepository.deleteWebServiceById(id);
  }

  @Override
  public String updateWebService(WebServiceDTO webServiceDTO) {
    log.debug("Request to insertWebService : {}", webServiceDTO);
    try {
      if (!webServiceDTO.getWebServiceMethodDTOS().isEmpty()) {
        List<WebServiceMethodDTO> webServiceMethodDTOS = webServiceDTO.getWebServiceMethodDTOS();
        cfgWebServiceMethodRepository
            .deleteListWebServiceMethodByWebServiceId(webServiceMethodDTOS);
        for (WebServiceMethodDTO dto : webServiceMethodDTOS) {
          dto.setWebServiceMethodId(null);
          dto.setWebServiceId(webServiceDTO.getWebServiceId());
          cfgWebServiceMethodRepository.insertWebServiceMethod(dto);
        }
      }
      return cfgWebServiceRepository.updateWebService(webServiceDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public Datatable getListWebService(WebServiceDTO dto) {
    log.debug("Request to getListWebService : {}", dto);
    return cfgWebServiceRepository.getListWebService(dto);
  }
}
