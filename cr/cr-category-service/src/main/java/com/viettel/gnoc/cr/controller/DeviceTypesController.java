package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.DeviceTypesBusiness;
import com.viettel.gnoc.cr.dto.DeviceTypesDTO;
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

@Slf4j
@RestController
@RequestMapping(Constants.TT_API_PATH_PREFIX + "DeviceTypesController")
public class DeviceTypesController {

  @Autowired
  DeviceTypesBusiness deviceTypesBusiness;

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEVICE_TYPE_MANAGEMENT)
  @PostMapping("/insertDeviceTypes")
  public ResponseEntity<ResultInSideDto> insertDeviceTypes(
      @Valid @RequestBody DeviceTypesDTO deviceTypesDTO) {
    ResultInSideDto data = deviceTypesBusiness.insertDeviceTypes(deviceTypesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEVICE_TYPE_MANAGEMENT)
  @PostMapping("/updateDeviceTypes")
  public ResponseEntity<ResultInSideDto> updateDeviceTypes(
      @Valid @RequestBody DeviceTypesDTO deviceTypesDTO) {
    ResultInSideDto data = deviceTypesBusiness.updateDeviceTypes(deviceTypesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEVICE_TYPE_MANAGEMENT)
  @GetMapping("/deleteDeviceTypes")
  public ResponseEntity<ResultInSideDto> deleteDeviceTypes(@RequestParam Long Id) {
    ResultInSideDto data = deviceTypesBusiness.deleteDeviceTypes(Id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findDeviceTypesById")
  public ResponseEntity<DeviceTypesDTO> findDeviceTypesById(@RequestParam Long Id) {
    DeviceTypesDTO data = deviceTypesBusiness.findDeviceTypesById(Id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEVICE_TYPE_MANAGEMENT)
  @PostMapping("/deleteListDeviceTypes")
  public ResponseEntity<ResultInSideDto> deleteListDeviceTypes(
      @RequestBody List<DeviceTypesDTO> deviceTypesListDTO) {
    ResultInSideDto data = deviceTypesBusiness.deleteListDeviceTypes(deviceTypesListDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getLisDeviceTypesSearch")
  public ResponseEntity<Datatable> getLisDeviceTypesSearch(
      @RequestBody DeviceTypesDTO deviceTypesDTO) {
    Datatable data = deviceTypesBusiness.getLisDeviceTypesSearch(deviceTypesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


}
