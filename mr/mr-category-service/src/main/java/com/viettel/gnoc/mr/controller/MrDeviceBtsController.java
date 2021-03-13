package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.mr.business.MrDeviceBtsBusiness;
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
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrDeviceBTS")
public class MrDeviceBtsController {

  @Autowired
  MrDeviceBtsBusiness mrDeviceBtsBusiness;

  @PostMapping("/listMrDeviceBtsPage")
  public ResponseEntity<Datatable> listMrDeviceBtsPage(@RequestBody MrDeviceBtsDTO mrDeviceBtsDTO) {
    Datatable data = mrDeviceBtsBusiness.getListMrDeviceBtsPage(mrDeviceBtsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListMrDeviceBtsDTO")
  public ResponseEntity<List<MrDeviceBtsDTO>> getListMrDeviceBtsDTO(
      @RequestBody MrDeviceBtsDTO mrDeviceBtsDTO) {
    List<MrDeviceBtsDTO> data = mrDeviceBtsBusiness.getListMrDeviceBtsDTO(mrDeviceBtsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateMrDeviceBts")
  public ResponseEntity<ResultInSideDto> edit(@Valid @RequestBody MrDeviceBtsDTO mrDeviceBtsDTO) {
    ResultInSideDto data = mrDeviceBtsBusiness.update(mrDeviceBtsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteMrDeviceBts")
  public ResponseEntity<ResultInSideDto> delete(Long deviceId) {
    ResultInSideDto data = mrDeviceBtsBusiness.delete(deviceId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailMrDeviceBts")
  public ResponseEntity<MrDeviceBtsDTO> getDetailMrDeviceBtsByID(Long deviceId) {
    MrDeviceBtsDTO mrDeviceBtsDTO = mrDeviceBtsBusiness.getDetail(deviceId);
    return new ResponseEntity<>(mrDeviceBtsDTO, HttpStatus.OK);
  }

}
