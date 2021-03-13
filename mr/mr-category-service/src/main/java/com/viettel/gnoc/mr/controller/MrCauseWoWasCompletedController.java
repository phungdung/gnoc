package com.viettel.gnoc.mr.controller;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import com.viettel.gnoc.mr.business.MrCauseWoWasCompletedBusiness;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrCauseWoWasCompleted")
public class MrCauseWoWasCompletedController {

  @Autowired
  MrCauseWoWasCompletedBusiness mrCauseWoWasCompletedBusiness;

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(
      @RequestBody MrCauseWoWasCompletedDTO mrCauseWoWasCompletedDTO) {
    return new ResponseEntity<>(mrCauseWoWasCompletedBusiness.onSearch(mrCauseWoWasCompletedDTO), HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdate")
  public ResponseEntity<ResultInSideDto> insertOrUpdate(
      @RequestBody MrCauseWoWasCompletedDTO mrCauseWoWasCompletedDTO) {
    ResultInSideDto data = mrCauseWoWasCompletedBusiness.insertOrUpdate(mrCauseWoWasCompletedDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findById")
  public ResponseEntity<MrCauseWoWasCompletedDTO> findById(Long id) {
    return new ResponseEntity<>(mrCauseWoWasCompletedBusiness.findById(id), HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long id) {
    return new ResponseEntity<>(mrCauseWoWasCompletedBusiness.delete(id), HttpStatus.OK);
  }

}
