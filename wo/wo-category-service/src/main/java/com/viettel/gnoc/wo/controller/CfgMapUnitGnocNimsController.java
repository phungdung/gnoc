package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.CfgMapUnitGnocNimsBusiness;
import com.viettel.gnoc.wo.dto.CfgMapUnitGnocNimsDTO;
import javax.validation.Valid;
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
@RequestMapping(Constants.OD_API_PATH_PREFIX + "cfgMapUnitGnocNims")
@Slf4j
public class CfgMapUnitGnocNimsController {

  @Autowired
  protected CfgMapUnitGnocNimsBusiness cfgMapUnitGnocNimsBusiness;

  @Autowired
  protected CatItemBusiness catItemBusiness;

  @PostMapping("/getListCfgMapUnitGnocNims")
  public ResponseEntity<Datatable> getListCfgMapUnitGnocNimsPage(
      @RequestBody CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    Datatable data = cfgMapUnitGnocNimsBusiness
        .getListCfgMapUnitGnocNimsPage(cfgMapUnitGnocNimsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long id) {
    ResultInSideDto data = cfgMapUnitGnocNimsBusiness.delete(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> insert(@Valid
  @RequestBody CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    ResultInSideDto data = cfgMapUnitGnocNimsBusiness.insert(cfgMapUnitGnocNimsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> update(@Valid
  @RequestBody CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    ResultInSideDto data = cfgMapUnitGnocNimsBusiness.update(cfgMapUnitGnocNimsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailById")
  public ResponseEntity<CfgMapUnitGnocNimsDTO> getDetailById(Long id) {
    CfgMapUnitGnocNimsDTO data = cfgMapUnitGnocNimsBusiness.findById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
