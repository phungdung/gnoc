package com.viettel.gnoc.controller;

import com.viettel.bccs.cc.service.ProblemGroupDTO;
import com.viettel.bccs.cc.service.ProblemTypeDTO;
import com.viettel.gnoc.business.MapProbToKedbBussiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MapProbToKedbDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.kedb.dto.KedbDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.PT_API_PATH_PREFIX + "MapProbToKedb")
@Slf4j
public class MapProbToKedbController {

  @Autowired
  MapProbToKedbBussiness mapProbToKedbBussiness;

  @PostMapping("/getListMapProbToKedbDTO")
  public ResponseEntity<Datatable> getListMapProbToKedbDTO(
      @RequestBody MapProbToKedbDTO mapProbToKedbDTO) {
    Datatable data = mapProbToKedbBussiness
        .getListMapProbToKedbDTO(mapProbToKedbDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListKedbDTO")
  public ResponseEntity<Datatable> getListKedbDTO(@RequestBody KedbDTO kedbDTO) {
    return new ResponseEntity<>(mapProbToKedbBussiness.getListKedbDTO(kedbDTO), HttpStatus.OK);
  }

  @PostMapping("/getListGroupCBB")
  public ResponseEntity<List<ProblemGroupDTO>> getListGroupCBB() {
    List<ProblemGroupDTO> data = mapProbToKedbBussiness.getListGroup();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListKindCBB")
  public ResponseEntity<List<ProblemGroupDTO>> getListKindCBB(Long probGroupId) {
    List<ProblemGroupDTO> data = mapProbToKedbBussiness.getListKind(probGroupId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListTypeCBB")
  public ResponseEntity<List<ProblemTypeDTO>> getListTypeCBB(Long probGroupId) {
    List<ProblemTypeDTO> data = mapProbToKedbBussiness.getListType(probGroupId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertMapProbToKedb")
  public ResponseEntity<ResultInSideDto> insertMapProbToKedb(
      @Valid @RequestBody MapProbToKedbDTO mapProbToKedbDTO) {
    ResultInSideDto data = mapProbToKedbBussiness.insertMapProbToKedb(mapProbToKedbDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateMapProbToKedb")
  public ResponseEntity<ResultInSideDto> updateMapProbToKedb(
      @Valid @RequestBody MapProbToKedbDTO mapProbToKedbDTO) {
    ResultInSideDto data = mapProbToKedbBussiness.updateMapProbToKedb(mapProbToKedbDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


  @PostMapping("/deleteMapProbToKedb")
  public ResponseEntity<ResultInSideDto> deleteMapProbToKedb(@RequestParam Long id) {
    ResultInSideDto data = mapProbToKedbBussiness.deleteMapProbToKedb(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get Detail
  @GetMapping("/getDetail")
  public ResponseEntity<MapProbToKedbDTO> getDetail(Long id) {
    MapProbToKedbDTO data = mapProbToKedbBussiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
