package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.business.CfgWebServiceMethodBusiness;
import com.viettel.gnoc.cr.dto.WebServiceMethodDTO;
import java.util.List;
import javax.validation.Valid;
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
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CfgWebServiceMethodController")
public class CfgWebServiceMethodController {

  @Autowired
  private CfgWebServiceMethodBusiness cfgWebServiceMethodBusiness;

  @PostMapping("/findWebServiceMethodById")
  public ResponseEntity<WebServiceMethodDTO> findWebServiceMethodById(Long id) {
    WebServiceMethodDTO webServiceMethodDTO = cfgWebServiceMethodBusiness
        .findWebServiceMethodById(id);
    return new ResponseEntity<>(webServiceMethodDTO, HttpStatus.OK);
  }

  @PostMapping("/insertWebServiceMethod")
  public ResponseEntity<ResultInSideDto> insertWebServiceMethod(
      @RequestBody @Valid WebServiceMethodDTO webServiceMethodDTO) {
    ResultInSideDto resultDto = cfgWebServiceMethodBusiness
        .insertWebServiceMethod(webServiceMethodDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/deleteWebServiceMethodById")
  public ResponseEntity<ResultInSideDto> deleteWebServiceMethodById(@RequestParam Long id) {
    String resultDto = cfgWebServiceMethodBusiness.deleteWebServiceMethodById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/deleteListWebServiceMethodByWebServiceId")
  public ResponseEntity<String> deleteListWebServiceMethodByWebServiceId(
      @RequestBody List<WebServiceMethodDTO> dto) {
    String resultDto = cfgWebServiceMethodBusiness.deleteListWebServiceMethodByWebServiceId(dto);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }


  @PostMapping("/updateWebServiceMethod")
  public ResponseEntity<ResultInSideDto> updateWebServiceMethod(
      @RequestBody @Valid WebServiceMethodDTO dto) {
    String resultDto = cfgWebServiceMethodBusiness.updateWebServiceMethod(dto);
    return new ResponseEntity<>(new ResultInSideDto(dto.getWebServiceId(), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/getListWebServiceMethod")
  public ResponseEntity<List<WebServiceMethodDTO>> getListWebServiceMethod() {
    List<WebServiceMethodDTO> data = cfgWebServiceMethodBusiness.getListWebServiceMethod();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
