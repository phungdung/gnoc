package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.WebServiceMethodDTO;
import com.viettel.gnoc.cr.repository.CfgWebServiceMethodRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class CfgWebServiceMethodBusinessImpl implements CfgWebServiceMethodBusiness {

  @Autowired
  private CfgWebServiceMethodRepository cfgWebServiceMethodRepository;

  @Override
  public WebServiceMethodDTO findWebServiceMethodById(Long id) {
    log.debug("Request to findWebServiceMethodById : {}", id);
    return cfgWebServiceMethodRepository.findWebServiceMethodById(id);
  }

  @Override
  public ResultInSideDto insertWebServiceMethod(WebServiceMethodDTO webServiceMethodDTO) {
    log.debug("Request to insertWebServiceMethod : {}", webServiceMethodDTO);
    return cfgWebServiceMethodRepository.insertWebServiceMethod(webServiceMethodDTO);
  }

  @Override
  public String deleteWebServiceMethodById(Long id) {
    log.debug("Request to deleteWebServiceMethodById : {}", id);
    return cfgWebServiceMethodRepository.deleteWebServiceMethodById(id);
  }

  @Override
  public String updateWebServiceMethod(WebServiceMethodDTO dto) {
    log.debug("Request to updateWebServiceMethod : {}", dto);
    return cfgWebServiceMethodRepository.updateWebServiceMethod(dto);
  }

  @Override
  public List<WebServiceMethodDTO> getListWebServiceMethod() {
    log.debug("Request to getListWebServiceMethod : {}");
    return cfgWebServiceMethodRepository.getListWebServiceMethod();
  }

  @Override
  public String deleteListWebServiceMethodByWebServiceId(List<WebServiceMethodDTO> dto) {
    log.debug("Request to deleteListWebServiceMethodByWebServiceId : {}", dto);
    return cfgWebServiceMethodRepository.deleteListWebServiceMethodByWebServiceId(dto);
  }
}
