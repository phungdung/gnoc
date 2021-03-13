package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.DeviceTypeVersionBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.DeviceTypeVersionDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "DeviceTypeVersionService")
public class DeviceTypeVersionController {

  @Autowired
  private DeviceTypeVersionBusiness deviceTypeVersionBusiness;

  @PostMapping("/getListDeviceTypeVersionDTO")
  public ResponseEntity<Datatable> getListDeviceTypeVersionDTO(
      @RequestBody DeviceTypeVersionDTO deviceTypeVersionDTO) {
    Datatable datatable = deviceTypeVersionBusiness
        .getListDeviceTypeVersionDTO(deviceTypeVersionDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getListDeviceTypeVersion")
  public ResponseEntity<List<DeviceTypeVersionDTO>> getListDeviceTypeVersion(
      @RequestBody DeviceTypeVersionDTO deviceTypeVersionDTO) {
    List<DeviceTypeVersionDTO> data = deviceTypeVersionBusiness
        .getListDeviceTypeVersion(deviceTypeVersionDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/onInsert")
  public ResponseEntity<ResultInSideDto> onInsert(@Valid @RequestBody DeviceTypeVersionDTO dto) {
    ResultInSideDto result = deviceTypeVersionBusiness.insertDeviceTypeVersion(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/onUpdate")
  public ResponseEntity<ResultInSideDto> onUpdate(@Valid @RequestBody DeviceTypeVersionDTO dto) {
    ResultInSideDto result = deviceTypeVersionBusiness.updateDeviceTypeVersion(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getDetailById")
  public ResponseEntity<DeviceTypeVersionDTO> onUpdate(@RequestParam Long id) {
    DeviceTypeVersionDTO result = deviceTypeVersionBusiness.findDeviceTypeVersionById(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
