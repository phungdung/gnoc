package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.business.MrScheduleTelHisSoftBusiness;
import java.io.File;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrScheduleTelHisSoft")
public class MrScheduleTelHisSoftController {

  @Autowired
  MrScheduleTelHisSoftBusiness mrScheduleTelHisSoftBusiness;

  @PostMapping("/getListDataSoftSearchWeb")
  public ResponseEntity<Datatable> getListDataSoftSearchWeb(
      @RequestBody MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    Datatable data = mrScheduleTelHisSoftBusiness.getListDataSoftSearchWeb(mrScheduleTelHisDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportDataMrScheduleTelHisSoft")
  public ResponseEntity<File> exportDataMrScheduleTelHisSoft(
      @RequestBody MrScheduleTelHisDTO mrScheduleTelHisDTO) throws Exception {
    File data = mrScheduleTelHisSoftBusiness.exportDataMrScheduleTelHisSoft(mrScheduleTelHisDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
