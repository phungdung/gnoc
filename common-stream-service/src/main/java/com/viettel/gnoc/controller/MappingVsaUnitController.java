package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.MappingVsaUnitBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MappingVsaUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KienPV
 */
@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "mappingVsaUnit")
public class MappingVsaUnitController {

  @Autowired
  public MappingVsaUnitBusiness mappingVsaUnitBusiness;

  @PostMapping("/getListMappingVsaUnitDTO")
  public ResponseEntity<Datatable> getListMappingVsaUnitDTO(
      @RequestBody MappingVsaUnitDTO mappingVsaUnitDTO) {
    Datatable datatable = mappingVsaUnitBusiness.getListMappingVsaUnitDTO(mappingVsaUnitDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/updateMappingVsaUnit")
  public ResponseEntity<ResultInSideDto> updateMappingVsaUnit(
      @RequestBody @Valid MappingVsaUnitDTO mappingVsaUnitDTO) {
    ResultInSideDto rs = mappingVsaUnitBusiness.updateMappingVsaUnit(mappingVsaUnitDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @DeleteMapping("/deleteMappingVsaUnit")
  public ResponseEntity<ResultInSideDto> deleteMappingVsaUnit(Long id) {
    ResultInSideDto rs = mappingVsaUnitBusiness.deleteMappingVsaUnit(id);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @GetMapping("/findMappingVsaUnitById")
  public ResponseEntity<MappingVsaUnitDTO> findMappingVsaUnitById(Long id) {
    MappingVsaUnitDTO rs = mappingVsaUnitBusiness.findMappingVsaUnitById(id);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @PostMapping("/insertMappingVsaUnit")
  public ResponseEntity<ResultInSideDto> insertMappingVsaUnit(
      @RequestBody @Valid MappingVsaUnitDTO mappingVsaUnitDTO) {
    ResultInSideDto rs = mappingVsaUnitBusiness.insertMappingVsaUnit(mappingVsaUnitDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

}


