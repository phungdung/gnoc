package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.mr.business.MrITHisBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "MrHardITHistory")
public class MrITHardHisController {

  @Autowired
  private MrITHisBusiness mrITHisBusiness;

  @PostMapping("/getListMrHardITHis")
  public ResponseEntity<Datatable> getListMrHardITHis(
      @RequestBody MrScheduleITHisDTO mrScheduleITHisDTO) {
    return new ResponseEntity<>(mrITHisBusiness.getListMrHardITHis(mrScheduleITHisDTO),
        HttpStatus.OK);
  }
}
