package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrConfigTestXaDTO;
import com.viettel.gnoc.mr.business.MrConfigTestXaBusiness;
import java.util.List;
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
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrConfigTestXa")
public class MrConfigTestXaController {

  @Autowired
  private MrConfigTestXaBusiness mrConfigTestXaBusiness;

  @PostMapping("/getListMrConfigTestXaPage")
  public ResponseEntity<Datatable> getListMrTestXa(
      @RequestBody MrConfigTestXaDTO mrConfigTestXaDTO) {
    return new ResponseEntity<>(mrConfigTestXaBusiness
        .getListMrConfigTestXa(mrConfigTestXaDTO), HttpStatus.OK);
  }

  @PostMapping("/insertMrConfigTestXa")
  public ResponseEntity<ResultInSideDto> insertMrConfigTestXa(
      @RequestBody MrConfigTestXaDTO mrConfigTestXaDTO) {
    return new ResponseEntity<>(mrConfigTestXaBusiness
        .insert(mrConfigTestXaDTO), HttpStatus.OK);
  }

  @PostMapping("/updateMrConfigTestXa")
  public ResponseEntity<ResultInSideDto> updateMrConfigTestXa(
      @RequestBody MrConfigTestXaDTO mrConfigTestXaDTO) {
    return new ResponseEntity<>(mrConfigTestXaBusiness
        .update(mrConfigTestXaDTO), HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> deleteById(@RequestParam(value = "mrConfigValueId") Long configId) {
    ResultInSideDto data = mrConfigTestXaBusiness.deleteMrConfigTestXa(configId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailMrConfigTestXa")
  public ResponseEntity<MrConfigTestXaDTO> getDetailMrConfigTestXa(@RequestParam(value = "mrConfigValueId") Long configId) {
    MrConfigTestXaDTO data = mrConfigTestXaBusiness.getDetail(configId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListStation")
  public ResponseEntity<List<MrConfigTestXaDTO>> getListStation(){
    List<MrConfigTestXaDTO> lstMrConfigTestXaDTO = mrConfigTestXaBusiness.getListStation();
    return new ResponseEntity<>(lstMrConfigTestXaDTO, HttpStatus.OK);
  }
}
