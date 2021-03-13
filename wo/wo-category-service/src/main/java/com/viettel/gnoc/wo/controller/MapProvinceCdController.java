package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.MapProvinceCdBusiness;
import com.viettel.gnoc.wo.dto.MapProvinceCdDTO;
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
@RequestMapping(Constants.OD_API_PATH_PREFIX + "mapProvinceCd")
@Slf4j
public class MapProvinceCdController {

  @Autowired
  protected MapProvinceCdBusiness mapProvinceCdBusiness;

  @PostMapping("/getListDTOSearchWeb")
  public ResponseEntity<Datatable> getListDTOSearchWeb(
      @RequestBody MapProvinceCdDTO mapProvinceCdDTO) {
    Datatable data = mapProvinceCdBusiness.getListDTOSearchWeb(mapProvinceCdDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteMapProvinceCd")
  public ResponseEntity<ResultInSideDto> deleteMapProvinceCd(Long id) {
    ResultInSideDto data = mapProvinceCdBusiness.deleteMapProvinceCd(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<ResultInSideDto> add(
      @Valid @RequestBody MapProvinceCdDTO mapProvinceCdDTO) {
    ResultInSideDto data = mapProvinceCdBusiness.add(mapProvinceCdDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/edit")
  public ResponseEntity<ResultInSideDto> edit(
      @Valid @RequestBody MapProvinceCdDTO mapProvinceCdDTO) {
    ResultInSideDto data = mapProvinceCdBusiness.edit(mapProvinceCdDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<MapProvinceCdDTO> getDetail(Long id) {
    MapProvinceCdDTO data = mapProvinceCdBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
