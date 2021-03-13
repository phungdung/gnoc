package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.mr.business.MrITSoftScheduleBusiness;
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

@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrScheduleITService")
public class MrITSoftScheduleController {

  @Autowired
  MrITSoftScheduleBusiness mrITSoftScheduleBusiness;

  @PostMapping("/getListMrSchedule")
  public ResponseEntity<Datatable> getListMrSchedule(
      @RequestBody MrITSoftScheduleDTO mrITSoftScheduleDTO) {
    Datatable datatable = mrITSoftScheduleBusiness.getListMrSchedule(mrITSoftScheduleDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @GetMapping("/findById")
  public ResponseEntity<MrITSoftScheduleDTO> getDetail(Long id) {
    MrITSoftScheduleDTO data = mrITSoftScheduleBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


  @DeleteMapping("/deleteMrScheduleIT")
  public ResponseEntity<ResultInSideDto> deleteMrScheduleIT(Long mrScheduleId) {
    ResultInSideDto data = mrITSoftScheduleBusiness.deleteMrScheduleITSoft(mrScheduleId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/onUpdate")
  public ResponseEntity<ResultInSideDto> onUpdate(
      @RequestBody MrITSoftScheduleDTO mrITSoftScheduleDTO) throws Exception {
    ResultInSideDto data = mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
