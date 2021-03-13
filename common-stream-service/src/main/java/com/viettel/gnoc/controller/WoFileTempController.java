package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.WoFileTempBussiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WoFileTempDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
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

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "WoFileTemp")
public class WoFileTempController {

  @Autowired
  private WoFileTempBussiness woFileTempBussiness;

  //get list WoFileTemp
  @PostMapping("/getListWoFileTemp")
  public ResponseEntity<Datatable> getListWoFileTemp(
      @RequestBody WoFileTempDto dto) {
    Datatable data = woFileTempBussiness.getListWoFileTemp(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get Detail
  @GetMapping("/getDetail")
  public ResponseEntity<WoFileTempDto> getDetail(Long woFileTempId) {
    WoFileTempDto data = woFileTempBussiness.getDetail(woFileTempId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get list wo type CBB
  @GetMapping("/getListWoTypeCBB")
  public ResponseEntity<List<WoTypeInsideDTO>> getListWoTypeCBB() {
    List<WoTypeInsideDTO> data = woFileTempBussiness.getListWoTypeCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //insert WoFileTemp
//  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.WO_FILE_TEMP)
  @PostMapping("/insertWoFileTemp")
  public ResponseEntity<ResultInSideDto> insertWoFileTemp(
      @Valid @RequestBody WoFileTempDto woFileTempDto) {
    ResultInSideDto resultInSideDto = woFileTempBussiness.insertWoFileTemp(woFileTempDto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  //update WoFileTemp
//  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.WO_FILE_TEMP)
  @PostMapping("/updateWoFileTemp")
  public ResponseEntity<ResultInSideDto> updateWoFileTemp(
      @RequestBody WoFileTempDto dto) {
    ResultInSideDto data = woFileTempBussiness.updateWoFileTemp(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //delete WoFileTemp by id
//  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.WO_FILE_TEMP)
  @PostMapping("/deleteWoFileTempById")
  public ResponseEntity<ResultInSideDto> deleteWoFileTempById(@RequestParam Long id) {
    ResultInSideDto data = woFileTempBussiness.deleteWoFileTempById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


}
