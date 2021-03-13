package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.business.CfgTempImportColBusiness;
import com.viettel.gnoc.cr.dto.TempImportColDTO;
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
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CfgTempImportColController")
public class CfgTempImportColController {

  @Autowired
  private CfgTempImportColBusiness cfgTempImportColBusiness;

  @PostMapping("/findTempImportColById")
  public ResponseEntity<TempImportColDTO> findTempImportColById(Long id) {
    TempImportColDTO tempImportDTO = cfgTempImportColBusiness
        .findTempImportColById(id);
    return new ResponseEntity<>(tempImportDTO, HttpStatus.OK);
  }

  @PostMapping("/insertTempImportCol")
  public ResponseEntity<ResultInSideDto> insertTempImportCol(
      @RequestBody TempImportColDTO tempImportColDTO) {
    ResultInSideDto resultDto = cfgTempImportColBusiness
        .insertTempImportCol(tempImportColDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/deleteTempImportColById")
  public ResponseEntity<ResultInSideDto> deleteTempImportColById(@RequestParam Long id) {
    String resultDto = cfgTempImportColBusiness.deleteTempImportColById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/updateTempImportCol")
  public ResponseEntity<ResultInSideDto> updateTempImportCol(
      @RequestBody TempImportColDTO dto) {
    String resultDto = cfgTempImportColBusiness.updateTempImportCol(dto);
    return new ResponseEntity<>(new ResultInSideDto(dto.getTempImportId(), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/getListTempImportCol")
  public ResponseEntity<Datatable> getListTempImportCol(
      @RequestBody TempImportColDTO tempImportColDTO) {
    Datatable data = cfgTempImportColBusiness.getListTempImportCol(tempImportColDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
