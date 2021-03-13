package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrITSoftCrImplUnitDTO;
import com.viettel.gnoc.mr.business.MrITSoftCrImplUnitBusiness;
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

@Slf4j
@RestController
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrITSoftCrImplUnit")
public class MrITSoftCrImplUnitController {

  @Autowired
  MrITSoftCrImplUnitBusiness mrITSoftCrImplUnitBusiness;

  @PostMapping("/getListDataMrITSoftCfgCrUnit")
  public ResponseEntity<Datatable> getListDataMrCfgCrUnitITSoft(
      @RequestBody MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    Datatable data = mrITSoftCrImplUnitBusiness.getListDataMrCfgCrUnitITSoft(mrITSoftCrImplUnitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertMrCfgCrUnitIT")
  public ResponseEntity<ResultInSideDto> insertMrCfgCrUnitIT(
      @Valid @RequestBody MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    ResultInSideDto data = mrITSoftCrImplUnitBusiness.insertMrCfgCrUnitIT(mrITSoftCrImplUnitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateMrCfgCrUnitIT")
  public ResponseEntity<ResultInSideDto> updateMrCfgCrUnitIT(
      @Valid @RequestBody MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    ResultInSideDto data = mrITSoftCrImplUnitBusiness.updateMrCfgCrUnitIT(mrITSoftCrImplUnitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findMrCfgCrUnitITById")
  public ResponseEntity<MrITSoftCrImplUnitDTO> findMrCfgCrUnitITById(Long cfgId) {
    MrITSoftCrImplUnitDTO mrCfgCrUnitTelDTO = mrITSoftCrImplUnitBusiness
        .findMrCfgCrUnitITById(cfgId);
    return new ResponseEntity<>(mrCfgCrUnitTelDTO, HttpStatus.OK);
  }

  @GetMapping("/deleteMrCfgCrUnitIT")
  public ResponseEntity<ResultInSideDto> deleteMrCfgCrUnitIT(Long cfgId) {
    ResultInSideDto resultInSideDto = mrITSoftCrImplUnitBusiness.deleteMrCfgCrUnitIT(cfgId);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

}
