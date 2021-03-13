package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CfgWebServiceBusiness;
import com.viettel.gnoc.cr.dto.WebServiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CfgWebServiceController")
public class CfgWebServiceController {

  @Autowired
  private CfgWebServiceBusiness cfgWebServiceBusiness;

  @PostMapping("/findWebServiceById")
  public ResponseEntity<WebServiceDTO> findWebServiceById(Long id) {
    WebServiceDTO webServiceDTO = cfgWebServiceBusiness
        .findWebServiceById(id);
    return new ResponseEntity<>(webServiceDTO, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_WEBSERVICE_IMPORT)
  @PostMapping("/insertWebService")
  public ResponseEntity<ResultInSideDto> insertWebService(
      @RequestBody WebServiceDTO webServiceDTO) {
    ResultInSideDto resultDto = cfgWebServiceBusiness
        .insertWebService(webServiceDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_WEBSERVICE_IMPORT)
  @PostMapping("/deleteWebServiceById")
  public ResponseEntity<ResultInSideDto> deleteWebServiceById(@RequestParam Long id) {
    String resultDto = cfgWebServiceBusiness.deleteWebServiceById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_WEBSERVICE_IMPORT)
  @PostMapping("/updateWebService")
  public ResponseEntity<ResultInSideDto> updateWebService(
      @RequestBody WebServiceDTO dto) {
    String resultDto = cfgWebServiceBusiness.updateWebService(dto);
    return new ResponseEntity<>(new ResultInSideDto(dto.getWebServiceId(), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/getListWebService")
  public ResponseEntity<Datatable> getListWebService(
      @RequestBody WebServiceDTO webServiceDTO) {
    Datatable data = cfgWebServiceBusiness.getListWebService(webServiceDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
