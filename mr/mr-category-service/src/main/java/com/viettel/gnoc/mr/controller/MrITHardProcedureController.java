package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureITHardDTO;
import com.viettel.gnoc.mr.business.MrITHardProcedureBusiness;
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

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "MrHardITProcedure")
public class MrITHardProcedureController {

  @Autowired
  private MrITHardProcedureBusiness mrITHardProcedureBusiness;

  @PostMapping("/getListMrHardITProcedure")
  public ResponseEntity<Datatable> getListMrSoftITProcedure(
      @RequestBody MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) {
    return new ResponseEntity<>(mrITHardProcedureBusiness.getListMrHardITProcedure(
        mrCfgProcedureITHardDTO), HttpStatus.OK);
  }

  @GetMapping("/getDetailMrHardITProcedure")
  public ResponseEntity<MrCfgProcedureITHardDTO> getDetailMrHardITProcedure(Long procedureId) {
    return new ResponseEntity<>(mrITHardProcedureBusiness.getDetail(procedureId), HttpStatus.OK);
  }

  @PostMapping("/insertMrCfgProcedureITHard")
  public ResponseEntity<ResultInSideDto> insertMrCfgProcedureITHard(
      @RequestBody MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) {
    return new ResponseEntity<>(
        mrITHardProcedureBusiness.insertMrCfgProcedureITHard(mrCfgProcedureITHardDTO),
        HttpStatus.OK);
  }

  @PutMapping("/updateMrCfgProcedureITHard")
  public ResponseEntity<ResultInSideDto> updateMrCfgProcedureITHard(
      @RequestBody MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) {
    return new ResponseEntity<>(
        mrITHardProcedureBusiness.updateMrCfgProcedureITHard(mrCfgProcedureITHardDTO),
        HttpStatus.OK);
  }

  @DeleteMapping("/deleteMrCfgProcedureITHard")
  public ResponseEntity<ResultInSideDto> deleteMrCfgProcedureITHard(Long procedureId) {
    return new ResponseEntity<>(
        mrITHardProcedureBusiness.deleteMrCfgProcedureITHard(procedureId),
        HttpStatus.OK);
  }
}
