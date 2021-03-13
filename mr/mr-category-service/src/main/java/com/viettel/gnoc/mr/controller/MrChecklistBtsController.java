package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.mr.business.MrChecklistBtsBusiness;
import java.io.File;
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

@Slf4j
@RestController
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrChecklistBts")
public class MrChecklistBtsController {

  @Autowired
  MrChecklistBtsBusiness mrChecklistBtsBusiness;

  @PostMapping("/getListDataSearchWeb")
  public ResponseEntity<Datatable> getListDataSearchWeb(
      @RequestBody MrChecklistsBtsDTO mrChecklistsBtsDTO) {
    Datatable data = mrChecklistBtsBusiness.getListDataSearchWeb(mrChecklistsBtsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertMrChecklistBts")
  public ResponseEntity<ResultInSideDto> insertMrChecklistBts(
      @Valid @RequestBody MrChecklistsBtsDTO mrChecklistsBtsDTO) {
    ResultInSideDto data = mrChecklistBtsBusiness.insertMrChecklistBts(mrChecklistsBtsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateMrChecklistBts")
  public ResponseEntity<ResultInSideDto> updateMrChecklistBts(
      @Valid @RequestBody MrChecklistsBtsDTO mrChecklistsBtsDTO) {
    ResultInSideDto data = mrChecklistBtsBusiness.updateMrChecklistBts(mrChecklistsBtsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findMrChecklistBtsByIdFromWeb")
  public ResponseEntity<MrChecklistsBtsDTO> findMrChecklistBtsByIdFromWeb(
      @RequestBody MrChecklistsBtsDTO mrChecklistsBtsDTO) {
    MrChecklistsBtsDTO result = mrChecklistBtsBusiness
        .findMrChecklistBtsByIdFromWeb(mrChecklistsBtsDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/deleteMrChecklistBts")
  public ResponseEntity<ResultInSideDto> deleteMrChecklistBts(Long checklistId) {
    ResultInSideDto resultInSideDto = mrChecklistBtsBusiness.deleteMrChecklistBts(checklistId);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/exportDataMrChecklistBts")
  public ResponseEntity<File> exportDataMrChecklistBts(
      @RequestBody MrChecklistsBtsDTO mrChecklistsBtsDTO) throws Exception {
    File data = mrChecklistBtsBusiness.exportDataMrChecklistBts(mrChecklistsBtsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSupplierBtsCombobox")
  public ResponseEntity<List<MrDeviceBtsDTO>> getListSupplierBtsCombobox(
      @RequestBody MrDeviceBtsDTO mrDeviceBtsDTO) {
    List<MrDeviceBtsDTO> list = mrChecklistBtsBusiness.getListSupplierBtsCombobox(mrDeviceBtsDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

}
