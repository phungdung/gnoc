package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.business.CrHisBusiness;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrHisService")
@Slf4j
public class CrHisController {

  @Autowired
  CrHisBusiness crHisBusiness;

  @PostMapping("/searchSql")
  public ResponseEntity<Datatable> searchSql(@RequestBody CrHisDTO dto) {
    Datatable list = crHisBusiness.searchSql(dto);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }
}
