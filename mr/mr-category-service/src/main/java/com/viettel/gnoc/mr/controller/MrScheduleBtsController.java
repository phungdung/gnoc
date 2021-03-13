package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsDTO;
import com.viettel.gnoc.mr.business.MrScheduleBtsBusiness;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrScheduleBtsService")
public class MrScheduleBtsController {

  @Autowired
  MrScheduleBtsBusiness mrScheduleBtsBusiness;

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(@RequestBody MrScheduleBtsDTO mrScheduleBtsDTO) {
    Datatable datatable = mrScheduleBtsBusiness.onSearch(mrScheduleBtsDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }
}
