package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdHisDTO;
import com.viettel.gnoc.mr.business.MrScheduleCdHisBusiness;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrScheduleCdHisService")
public class MrScheduleCdHisController {

  @Autowired
  MrScheduleCdHisBusiness mrScheduleCdHisBusiness;

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> searchMrScheduleCdHisDTOs(
      @RequestBody MrScheduleCdHisDTO mrScheduleCdHisDTO) {
    Datatable datatable = mrScheduleCdHisBusiness.searchMrScheduleCdHisDTOs(mrScheduleCdHisDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }
}
