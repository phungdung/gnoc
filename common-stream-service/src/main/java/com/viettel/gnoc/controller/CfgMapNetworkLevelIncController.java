package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.CfgMapNetLevelIncTypeBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import java.util.List;
import java.util.Map;
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
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "cfgMapNetworkLevelInc")
public class CfgMapNetworkLevelIncController {

  @Autowired
  CfgMapNetLevelIncTypeBusiness cfgMapNetLevelIncTypeBusiness;

  @PostMapping("/getListCfgMapNetLevelIncTypeDTO")
  public ResponseEntity<List<CfgMapNetLevelIncTypeDTO>> getListCfgMapNetLevelIncTypeDTO(
      @RequestBody CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    List<CfgMapNetLevelIncTypeDTO> list = cfgMapNetLevelIncTypeBusiness
        .getListCfgMapNetLevelIncTypeDTO(cfgMapNetLevelIncTypeDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListCfgMapNetLevelIncTypeDatatable")
  public ResponseEntity<Datatable> getListCfgMapNetLevelIncTypeDatatable(
      @RequestBody CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    Datatable datatable = cfgMapNetLevelIncTypeBusiness
        .getListCfgMapNetLevelIncTypeDatatable(cfgMapNetLevelIncTypeDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PutMapping("/updateCfgMapNetLevelIncType")
  public ResponseEntity<ResultInSideDto> updateCfgMapNetLevelIncType(
      @RequestBody @Valid CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    ResultInSideDto rs = cfgMapNetLevelIncTypeBusiness
        .updateCfgMapNetLevelIncType(cfgMapNetLevelIncTypeDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @DeleteMapping("/deleteCfgMapNetLevelIncType")
  public ResponseEntity<ResultInSideDto> deleteCfgMapNetLevelIncType(Long id) {
    ResultInSideDto rs = cfgMapNetLevelIncTypeBusiness
        .deleteCfgMapNetLevelIncType(id);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @GetMapping("/findCfgMapNetLevelIncTypeById")
  public ResponseEntity<CfgMapNetLevelIncTypeDTO> findCfgMapNetLevelIncTypeById(Long id) {
    CfgMapNetLevelIncTypeDTO rs = cfgMapNetLevelIncTypeBusiness
        .findCfgMapNetLevelIncTypeById(id);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @PostMapping("/insertCfgMapNetLevelIncType")
  public ResponseEntity<ResultInSideDto> insertCfgMapNetLevelIncType(
      @RequestBody @Valid CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    ResultInSideDto rs = cfgMapNetLevelIncTypeBusiness
        .insertCfgMapNetLevelIncType(cfgMapNetLevelIncTypeDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @GetMapping("/netWorkRiskList")
  public ResponseEntity<Map<?, ?>> netWorkRiskList() {
    Map<?, ?> rs = cfgMapNetLevelIncTypeBusiness
        .netWorkRiskList();
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @GetMapping("/netWorkLevelList")
  public ResponseEntity<Map<?, ?>> netWorkLevelList() {
    Map<?, ?> rs = cfgMapNetLevelIncTypeBusiness
        .netWorkLevelList();
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }
}
