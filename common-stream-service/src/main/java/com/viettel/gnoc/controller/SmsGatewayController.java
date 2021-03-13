package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.SmsGatewayBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SmsGatewayDTO;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "smsGateway")
public class SmsGatewayController {

  @Autowired
  private SmsGatewayBusiness smsGatewayBusiness;

  @PostMapping("/getListSmsGatewayDTO")
  public ResponseEntity<Datatable> getListSmsGatewayDTO(@RequestBody SmsGatewayDTO smsGatewayDTO) {
    Datatable lst = smsGatewayBusiness.getListSmsGatewayDTO(smsGatewayDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @GetMapping("/getListSmsGatewayAll")
  public ResponseEntity<List<SmsGatewayDTO>> getListSmsGatewayAll() {
    List<SmsGatewayDTO> lst = smsGatewayBusiness.getListSmsGatewayAll(new SmsGatewayDTO());
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PutMapping("/updateSmsGateway")
  public ResponseEntity<ResultInSideDto> updateSmsGateway(
      @RequestBody @Valid SmsGatewayDTO smsGatewayDTO) {
    ResultInSideDto rs = smsGatewayBusiness.updateSmsGateway(smsGatewayDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @PutMapping("/deleteSmsGateway")
  public ResponseEntity<ResultInSideDto> deleteSmsGateway(Long smsGatewayId) {
    ResultInSideDto rs = smsGatewayBusiness.deleteSmsGateway(smsGatewayId);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @GetMapping("/findSmsGatewayById")
  public ResponseEntity<SmsGatewayDTO> findSmsGatewayById(Long smsGatewayId) {
    SmsGatewayDTO rs = smsGatewayBusiness.findSmsGatewayById(smsGatewayId);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @PostMapping("/insertSmsGateway")
  public ResponseEntity<ResultInSideDto> insertSmsGateway(
      @RequestBody @Valid SmsGatewayDTO smsGatewayDTO) {
    ResultInSideDto rs = smsGatewayBusiness.insertSmsGateway(smsGatewayDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @PostMapping("/getListIpccServiceDatatable")
  public ResponseEntity<Datatable> getListIpccServiceDatatable(
      @RequestBody IpccServiceDTO ipccServiceDTO) {
    Datatable rs = smsGatewayBusiness.getListIpccServiceDatatable(ipccServiceDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @GetMapping("/getListIpccServiceAll")
  public ResponseEntity<List<IpccServiceDTO>> getListIpccServiceAll() {
    List<IpccServiceDTO> rs = smsGatewayBusiness.getListIpccServiceDTO(new IpccServiceDTO());
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @PostMapping("/insertIpccServiceDTO")
  public ResponseEntity<ResultInSideDto> insertIpccServiceDTO(
      @RequestBody @Valid IpccServiceDTO ipccServiceDTO) {
    ResultInSideDto rs = smsGatewayBusiness.insertIpccServiceDTO(ipccServiceDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @PutMapping("/updateIpccServiceDTO")
  public ResponseEntity<ResultInSideDto> updateIpccServiceDTO(
      @RequestBody @Valid IpccServiceDTO ipccServiceDTO) {
    ResultInSideDto rs = smsGatewayBusiness.updateIpccServiceDTO(ipccServiceDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @DeleteMapping("/deleteIpccServiceDTO")
  public ResponseEntity<ResultInSideDto> deleteIpccServiceDTO(Long ipccServiceId) {
    ResultInSideDto rs = smsGatewayBusiness.deleteIpccServiceDTO(ipccServiceId);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }
}
