package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.UnitCommonBusiness;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.List;
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

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "unitCommon")
public class UnitCommonController {

  @Autowired
  private UnitCommonBusiness unitCommonBusiness;

  @PostMapping("/getListUnitDTO")
  public ResponseEntity<Datatable> getListUnitDTO(@RequestBody UnitDTO unitDTO) {
    Datatable lst = unitCommonBusiness.getListUnitDTO(unitDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/updateUnit")
  public ResponseEntity<ResultInSideDto> updateUnit(@RequestBody @Valid UnitDTO unitDTO) {
    ResultInSideDto rs = unitCommonBusiness.updateUnit(unitDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @PostMapping("/updateUnitChildren")
  public ResponseEntity<ResultInSideDto> updateUnitChildren(@RequestBody @Valid UnitDTO unitDTO) {
    ResultInSideDto rs = unitCommonBusiness.updateUnit(unitDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @DeleteMapping("/deleteUnit")
  public ResponseEntity<ResultInSideDto> deleteUnit(Long id) {
    ResultInSideDto rs = unitCommonBusiness.deleteUnit(id);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @GetMapping("/findUnitById")
  public ResponseEntity<UnitDTO> findUnitById(Long id) {
    UnitDTO rs = unitCommonBusiness.findUnitById(id);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @PostMapping("/insertUnit")
  public ResponseEntity<ResultInSideDto> insertUnit(@RequestBody @Valid UnitDTO unitDTO) {
    ResultInSideDto rs = unitCommonBusiness.insertUnit(unitDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @GetMapping("/getListUnitByLevel")
  public ResponseEntity<List<UnitDTO>> getListUnitByLevel(String level) {
    List<UnitDTO> rs = unitCommonBusiness.getListUnitByLevel(level);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @GetMapping("/getUnitDTOForTree")
  public ResponseEntity<List<UnitDTO>> getUnitDTOForTree(Boolean isRoot, String status,
      String parentId) {
    List<UnitDTO> rs = unitCommonBusiness.getUnitDTOForTree(isRoot, status, parentId);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @GetMapping("/getUnitVSADTOForTree")
  public ResponseEntity<List<UnitDTO>> getUnitVSADTOForTree(Boolean isRoot, String status,
      String parentId) {
    List<UnitDTO> rs = unitCommonBusiness.getUnitVSADTOForTree(isRoot, status, parentId);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @PostMapping("/getListUnitVSA")
  public ResponseEntity<List<UnitDTO>> getListUnitVSA(@RequestBody UnitDTO unitDTO) {
    List<UnitDTO> rs = unitCommonBusiness.getListUnitVSA(unitDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  @PostMapping("/getListUnit")
  public ResponseEntity<List<UnitDTO>> getListUnit(@RequestBody UnitDTO unitDTO) {
    List<UnitDTO> rs = unitCommonBusiness.getListUnit(unitDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  // get list all data on search screen
  @PostMapping("/getListUnitDatatableAll")
  public ResponseEntity<Datatable> getListUnitDatatableAll(@RequestBody UnitDTO unitDTO) {
    Datatable rs = unitCommonBusiness.getListUnitDatatableAll(unitDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

  // get list all data on search screen
  // if search, might set location name = ?
  @PostMapping("/getLocationAll")
  public ResponseEntity<Datatable> getLocationAll(@RequestBody CatLocationDTO catLocationDTO) {
    Datatable rs = unitCommonBusiness.getListLocationAll(catLocationDTO);
    return new ResponseEntity<>(rs, HttpStatus.OK);
  }

}
