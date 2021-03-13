package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.WoConfigPropertyBusiness;
import com.viettel.gnoc.wo.dto.WoConfigPropertyDTO;
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

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "WoConfigProperty")
@Slf4j
public class WoConfigPropertyController {

  @Autowired
  protected WoConfigPropertyBusiness woConfigPropertyBusiness;

  @PostMapping("/getListWoConfigProperty")
  public ResponseEntity<Datatable> getListWoMaterialPage(
      @RequestBody WoConfigPropertyDTO configPropertyDTO) {
    Datatable data = woConfigPropertyBusiness.getListConfigPropertyDTO(configPropertyDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(String Key) {
    ResultInSideDto data = woConfigPropertyBusiness.delete(Key);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/addConfigProperty")
  public ResponseEntity<ResultInSideDto> addConfigProperty(
      @Valid @RequestBody WoConfigPropertyDTO configPropertyDTO) {
    ResultInSideDto data = woConfigPropertyBusiness.addConfigProperty(configPropertyDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PutMapping("/updateConfigProperty")
  public ResponseEntity<ResultInSideDto> updateConfigProperty(
      @Valid @RequestBody WoConfigPropertyDTO configPropertyDTO) {
    ResultInSideDto data = woConfigPropertyBusiness.updateConfigProperty(configPropertyDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<WoConfigPropertyDTO> getDetail(String Key) {
    WoConfigPropertyDTO data = woConfigPropertyBusiness.getDetail(Key);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
