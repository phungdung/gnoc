package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.*;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author hungtv77
 */
@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "ManagerTroubleImportant")
public class ManagermentTroubleImportantController {

  @Autowired
  TroubleImportantBusiness troubleImportantBusiness;

  @PostMapping("/searchListTroubleImportant")
  public ResponseEntity<Datatable> searchListTroubleImportant(@RequestBody TroubleImportantDTO dto) {
    Datatable data = troubleImportantBusiness.searchListTroubleImportant(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> insert(@RequestBody TroubleImportantDTO dto) {
    ResultInSideDto data = troubleImportantBusiness.insert(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> update(@RequestBody TroubleImportantDTO dto) {
    ResultInSideDto data = troubleImportantBusiness.update(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailTrouble")
  public ResponseEntity<TroubleImportantDTO> getDetailTrouble(Long id) {
    TroubleImportantDTO data = troubleImportantBusiness.getDetailTrouble(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long id) {
    ResultInSideDto data = troubleImportantBusiness.delete(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}


