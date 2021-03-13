package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.mr.business.MrDeviceBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrDevice")
public class MrDeviceController {

  @Autowired
  MrDeviceBusiness mrDeviceBusiness;

  @PostMapping("/getListMrDeviceSoftDTO")
  public ResponseEntity<Datatable> getListMrDeviceSoftDTO(
      @RequestBody MrDeviceDTO mrDeviceDTO) {
    Datatable data = mrDeviceBusiness.getListMrDeviceSoftDTO(mrDeviceDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateMrDeviceHard")
  public ResponseEntity<ResultInSideDto> updateMrDeviceHard(
      @RequestBody MrDeviceDTO mrDeviceDTO) {
    ResultInSideDto data = mrDeviceBusiness.updateMrDeviceHard(mrDeviceDTO, false, false);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/deleteMrDeviceHard")
  public ResponseEntity<ResultInSideDto> deleteMrDeviceHard(@RequestBody MrDeviceDTO mrDeviceDTO) {
    ResultInSideDto data = mrDeviceBusiness.deleteMrDeviceHard(mrDeviceDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<MrDeviceDTO> getDetail(Long deviceId) {
    MrDeviceDTO data = mrDeviceBusiness.getDetail(deviceId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/approveMrDeviceHard")
  public ResponseEntity<ResultInSideDto> approveMrDeviceHard(@RequestBody MrDeviceDTO mrDeviceDTO) {
    ResultInSideDto result = mrDeviceBusiness.approveMrDeviceHard(mrDeviceDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
