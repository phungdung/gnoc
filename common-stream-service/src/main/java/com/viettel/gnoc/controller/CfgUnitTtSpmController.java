package com.viettel.gnoc.controller;


import com.viettel.gnoc.business.CfgUnitTtSpmBusiness;
import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmDTO;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmSearchDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CfgUnitTtSpm")
@Slf4j
public class CfgUnitTtSpmController {

  @Autowired
  CfgUnitTtSpmBusiness cfgUnitTtSpmBusiness;
  @Autowired
  UnitBusiness unitBusiness;
  @Autowired
  CatItemBusiness catItemBusiness;
  @Autowired
  CatLocationBusiness catLocationBusiness;

  @PostMapping("/getListCatItem")
  public ResponseEntity<List<CatItemDTO>> getCatItem() {
    List<String> lstCategory = new ArrayList<>();
    lstCategory.add(Constants.PROBLEM.PT_TYPE);
    List<CatItemDTO> data = catItemBusiness.getListCatItemDTOByListCategory(lstCategory);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getCatLocation")
  public ResponseEntity<List<CatLocationDTO>> getCatLocation() {
    CatLocationDTO catLocationDTO = new CatLocationDTO();
    List<CatLocationDTO> data = catLocationBusiness
        .getListCatLocationDTO(catLocationDTO, 0, Integer.MAX_VALUE, "", "");
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getUnit")
  public ResponseEntity<List<UnitDTO>> getUnit() {
    UnitDTO unitDTO = new UnitDTO();
    List<UnitDTO> data = unitBusiness.getListUnitDTO(unitDTO, 0, Integer.MAX_VALUE, "", "");
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertCfgUnitTtSpm")
  public ResponseEntity<ResultInSideDto> insertCfgUnitTtSpm(
      @Valid @RequestBody CfgUnitTtSpmDTO cfgUnitTtSpmDTO) {
    ResultInSideDto data = cfgUnitTtSpmBusiness.insertCfgUnitTtSpm(cfgUnitTtSpmDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteCfgUnitTtSpm")
  public ResponseEntity<String> deleteCfgUnitTtSpm(Long cfgUnitTtSpmId) {
    String result = cfgUnitTtSpmBusiness.deleteCfgUnitTtSpm(cfgUnitTtSpmId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/updateCfgUnitTtSpm")
  public ResponseEntity<String> updateCfgUnitTtSpm(
      @Valid @RequestBody CfgUnitTtSpmDTO cfgUnitTtSpmDTO) {
    String data = cfgUnitTtSpmBusiness.updateCfgUnitTtSpm(cfgUnitTtSpmDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/deleteListCfgUnitTtSpm")
  public ResponseEntity<String> deleteListCfgUnitTtSpm(
      @RequestBody List<CfgUnitTtSpmDTO> cfgUnitTtSpmListDTO) {
    String result = cfgUnitTtSpmBusiness.deleteListCfgUnitTtSpm(cfgUnitTtSpmListDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListCfgUnitTtSpm")
  public ResponseEntity<String> insertOrUpdateListCfgUnitTtSpm(@Valid
  @RequestBody List<CfgUnitTtSpmDTO> cfgUnitTtSpmDTO) {
    String result = cfgUnitTtSpmBusiness.insertOrUpdateListCfgUnitTtSpm(cfgUnitTtSpmDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getSequenseCfgUnitTtSpm")
  public ResponseEntity<String> getSequenseCfgUnitTtSpm(String seqName) {
    return new ResponseEntity<>(cfgUnitTtSpmBusiness.getSequenseCfgUnitTtSpm(seqName, 1).get(0),
        HttpStatus.OK);
  }

  @GetMapping("/findCfgUnitTtSpmById")
  public ResponseEntity<CfgUnitTtSpmDTO> findCfgUnitTtSpmById(Long cfgUnitTtSpmId) {
    CfgUnitTtSpmDTO data = cfgUnitTtSpmBusiness.findCfgUnitTtSpmById(cfgUnitTtSpmId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCfgUnitTtSpmSearchDTO")
  public ResponseEntity<Datatable> getListCfgUnitTtSpmSearchDTO(
      @RequestBody CfgUnitTtSpmSearchDTO cfgUnitTtSpmSearchDTO) {
    Datatable data = cfgUnitTtSpmBusiness.getListUnitTtSpmSearch(cfgUnitTtSpmSearchDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCfgUnitTtSpmDTO")
  public ResponseEntity<List<CfgUnitTtSpmDTO>> getListCfgUnitTtSpmDTO(
      @RequestBody CfgUnitTtSpmDTO cfgUnitTtSpmDTO) {
    List<CfgUnitTtSpmDTO> data = cfgUnitTtSpmBusiness
        .getListCfgUnitTtSpmDTO(cfgUnitTtSpmDTO, 0, Integer.MAX_VALUE, "", "");
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListUnit")
  public ResponseEntity<List<UnitDTO>> getListUnit(@RequestBody UnitDTO unitDTO) {
    List<UnitDTO> data = unitBusiness.getListUnit(unitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}

