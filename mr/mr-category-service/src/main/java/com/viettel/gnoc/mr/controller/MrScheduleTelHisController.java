package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.business.MrScheduleTelHisBusiness;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrScheduleTelHis")
public class MrScheduleTelHisController {

  @Autowired
  MrScheduleTelHisBusiness mrScheduleTelHisBusiness;

  @PostMapping("/listMrScheduleTelHisPage")
  public ResponseEntity<Datatable> listMrScheduleTelHisPage(
      @RequestBody MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    Datatable data = mrScheduleTelHisBusiness.getListMrScheduleTelHisPage(mrScheduleTelHisDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


}
