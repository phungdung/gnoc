package com.viettel.gnoc.mr.controller;


import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrSchedulePeriodicDTO;
import com.viettel.gnoc.mr.business.MrSchedulePeriodicBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrSchedulePeriodic")
public class MrSchedulePeriodicController {
  @Autowired
  MrSchedulePeriodicBusiness mrSchedulePeriodicBusiness;


  @PostMapping("/insertMrSchedulePeriodic")
  public ResponseEntity<ResultInSideDto> insertMrApprovalDepartment(@RequestBody MrSchedulePeriodicDTO mrSchedulePeriodicDTO) {
    ResultInSideDto result = mrSchedulePeriodicBusiness
        .insertMrSchedulePeriodic(mrSchedulePeriodicDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
