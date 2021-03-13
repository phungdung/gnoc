package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.business.CfgTempImportDataBusiness;
import com.viettel.gnoc.cr.dto.TempImportDataDTO;
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
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CfgTempImportDataController")
public class CfgTempImportDataController {

  @Autowired
  private CfgTempImportDataBusiness cfgTempImportDataBusiness;

  @PostMapping("/findCfgTempImportDataById")
  public ResponseEntity<TempImportDataDTO> findCfgTempImportDataById(Long id) {
    TempImportDataDTO tempImportDataDTO = cfgTempImportDataBusiness
        .findCfgTempImportDataById(id);
    return new ResponseEntity<>(tempImportDataDTO, HttpStatus.OK);
  }

  @PostMapping("/insertTempImportData")
  public ResponseEntity<ResultInSideDto> insertTempImportData(
      @RequestBody TempImportDataDTO tempImportDataDTO) {
    ResultInSideDto resultDto = cfgTempImportDataBusiness
        .insertTempImportData(tempImportDataDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/deleteTempImportDataById")
  public ResponseEntity<ResultInSideDto> deleteTempImportDataById(@RequestParam Long id) {
    String resultDto = cfgTempImportDataBusiness.deleteTempImportDataById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/updateTempImportData")
  public ResponseEntity<ResultInSideDto> updateTempImportData(
      @RequestBody TempImportDataDTO dto) {
    String resultDto = cfgTempImportDataBusiness.updateTempImportData(dto);
    return new ResponseEntity<>(new ResultInSideDto(dto.getTidaId(), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/getListTempImportData")
  public ResponseEntity<Datatable> getListTempImportData(
      @RequestBody TempImportDataDTO tempImportDataDTO) {
    Datatable data = cfgTempImportDataBusiness.getListTempImportData(tempImportDataDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
