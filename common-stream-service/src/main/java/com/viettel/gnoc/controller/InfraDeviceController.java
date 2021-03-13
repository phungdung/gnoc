package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.InfraDeviceBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "InfraDeviceService")
public class InfraDeviceController {

  @Autowired
  private InfraDeviceBusiness infraDeviceBusiness;

  @PostMapping("/getInfraDeviceDTO")
  public ResponseEntity<Datatable> getInfraDeviceDTO(@RequestBody InfraDeviceDTO infraDeviceDTO) {
    Datatable datatable = infraDeviceBusiness.getDatatableInfraDeviceIp(infraDeviceDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getInfraDeviceDTOV2")
  public ResponseEntity<Datatable> getInfraDeviceDTOV2(@RequestBody InfraDeviceDTO infraDeviceDTO) {
    Datatable datatable = infraDeviceBusiness.getDatatableInfraDeviceIpV2(infraDeviceDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getInfraDeviceDTOForCr")
  public ResponseEntity<Datatable> getInfraDeviceDTOForCr(
      @RequestBody InfraDeviceDTO infraDeviceDTO) {
    Datatable datatable = infraDeviceBusiness.getDatatableInfraDeviceIpForCr(infraDeviceDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/geInfraDeviceByIps")
  public ResponseEntity<List<InfraDeviceDTO>> geInfraDeviceByIps(
      @RequestBody InfraDeviceDTO infraDeviceDTO) {
    List<InfraDeviceDTO> data = infraDeviceBusiness
        .geInfraDeviceByIps(infraDeviceDTO.getLstIps(), infraDeviceDTO.getNationCode());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListInfraDeviceIpV2")
  public ResponseEntity<List<InfraDeviceDTO>> getListInfraDeviceIpV2(
      @RequestBody InfraDeviceDTO infraDeviceDTO) {
    List<InfraDeviceDTO> data = infraDeviceBusiness.getListInfraDeviceIpV2(infraDeviceDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListInfraDeviceIpV2SrProxy")
  public ResponseEntity<List<InfraDeviceDTO>> getListInfraDeviceIpV2SrProxy(
      @RequestBody InfraDeviceDTO infraDeviceDTO) {
    List<InfraDeviceDTO> data = infraDeviceBusiness.getListInfraDeviceIpV2SrProxy(infraDeviceDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListInfraDeviceNonIp")
  public ResponseEntity<List<InfraDeviceDTO>> getListInfraDeviceNonIp(
      @RequestBody InfraDeviceDTO infraDeviceDTO) {
    List<InfraDeviceDTO> data = infraDeviceBusiness.getListInfraDeviceNonIp(infraDeviceDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListInfraDeviceDTOV2")
  public ResponseEntity<List<InfraDeviceDTO>> getListInfraDeviceDTOV2(
      @RequestBody InfraDeviceDTO infraDeviceDTO) {
    List<InfraDeviceDTO> data = infraDeviceBusiness.getListInfraDeviceDTOV2(infraDeviceDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
