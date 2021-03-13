package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.SearchDeviceNIMSDTO;
import com.viettel.gnoc.mr.business.SearchDeviceNIMSBusiness;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.MR_API_PATH_PREFIX + "SearchDeviceNIMSService")
public class SearchDeviceNIMSController {

  @Autowired
  SearchDeviceNIMSBusiness searchDeviceNIMSBusiness;

  @PostMapping("/getListSearchDeviceNIMS")
  public ResponseEntity<Datatable> getListSearchDeviceNIMS(
      @RequestBody SearchDeviceNIMSDTO searchDeviceNIMSDTO) {
    Datatable datatable = searchDeviceNIMSBusiness.getListSearchDeviceNIMS(searchDeviceNIMSDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getComboboxNetworkClass")
  public ResponseEntity<List<SearchDeviceNIMSDTO>> getComboboxNetworkClass() {
    List<SearchDeviceNIMSDTO> searchDeviceNIMSDTOS = searchDeviceNIMSBusiness
        .getComboboxNetworkClass();
    return new ResponseEntity<>(searchDeviceNIMSDTOS, HttpStatus.OK);
  }

  @PostMapping("/getComboboxNetworkType")
  public ResponseEntity<List<SearchDeviceNIMSDTO>> getComboboxNetworkType() {
    List<SearchDeviceNIMSDTO> searchDeviceNIMSDTOS = searchDeviceNIMSBusiness
        .getComboboxNetworkType();
    return new ResponseEntity<>(searchDeviceNIMSDTOS, HttpStatus.OK);
  }
}
