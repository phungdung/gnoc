package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.CfgSupportCaseBusiness;
import com.viettel.gnoc.wo.dto.CfgSupportCaseDTO;
import com.viettel.gnoc.wo.dto.CfgSupportCaseTestDTO;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "cfgSupportCase")
@Slf4j
public class CfgSupportCaseController {

  @Autowired
  protected CfgSupportCaseBusiness cfgSupportCaseBusiness;

  @PostMapping("/getListCfgSupportCaseDTONew")
  public ResponseEntity<Datatable> getListCfgSupportCaseDTONew(
      @RequestBody CfgSupportCaseDTO cfgSupportCaseDTO) {
    Datatable data = cfgSupportCaseBusiness.getListCfgSupportCaseDTONew(cfgSupportCaseDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteCaseAndCaseTest")
  public ResponseEntity<ResultInSideDto> deleteCaseAndCaseTest(Long id) {
    ResultInSideDto data = cfgSupportCaseBusiness.deleteCaseAndCaseTest(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<ResultInSideDto> add(
      @Valid @RequestBody CfgSupportCaseDTO cfgSupportCaseDTO) {
    ResultInSideDto data = cfgSupportCaseBusiness.add(cfgSupportCaseDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/edit")
  public ResponseEntity<ResultInSideDto> edit(
      @Valid @RequestBody CfgSupportCaseDTO cfgSupportCaseDTO) {
    ResultInSideDto data = cfgSupportCaseBusiness.edit(cfgSupportCaseDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<CfgSupportCaseDTO> getDetail(Long id) {
    CfgSupportCaseDTO data = cfgSupportCaseBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListCfgSupportCaseTestId/cfgSuppportCaseId{cfgSuppportCaseId}")
  public ResponseEntity<List<CfgSupportCaseTestDTO>> getListCfgSupportCaseTestId(
      @PathVariable Long cfgSuppportCaseId) {
    return new ResponseEntity<>(
        cfgSupportCaseBusiness.getListCfgSupportCaseTestId(cfgSuppportCaseId), HttpStatus.OK);
  }
}
