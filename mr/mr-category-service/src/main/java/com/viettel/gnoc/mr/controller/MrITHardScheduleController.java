package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.mr.business.MrITHardScheduleBusiness;
import com.viettel.gnoc.mr.business.MrScheduleTelBusiness;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrScheduleITHardService")
public class MrITHardScheduleController {

  @Autowired
  MrITHardScheduleBusiness mrITHardScheduleBusiness;
  @Autowired
  MrScheduleTelBusiness mrScheduleTelBusiness;

  @PostMapping("/getListMrScheduleITHard")
  public ResponseEntity<Datatable> getListMrSchedule(
      @RequestBody MrITHardScheduleDTO mrITHardScheduleDTO) {
    Datatable datatable = mrITHardScheduleBusiness.getListMrScheduleITHard(mrITHardScheduleDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<MrITHardScheduleDTO> getDetail(Long id) {
    MrITHardScheduleDTO data = mrITHardScheduleBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


  @DeleteMapping("/deleteMrScheduleITHard")
  public ResponseEntity<ResultInSideDto> deleteMrScheduleIT(Long mrScheduleId) {
    ResultInSideDto data = mrITHardScheduleBusiness.deleteMrScheduleITHard(mrScheduleId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/onUpdate")
  public ResponseEntity<ResultInSideDto> onUpdate(
      @RequestBody MrITHardScheduleDTO mrITHardScheduleDTO) throws Exception {
    ResultInSideDto data = mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findWoById")
  public ResponseEntity<WoInsideDTO> findWoById(@RequestBody MrITHardScheduleDTO mrITHardScheduleDTO) {
    WoInsideDTO data = mrScheduleTelBusiness
        .findWoById(mrITHardScheduleDTO.getWoId(), mrITHardScheduleDTO.getMrId());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
