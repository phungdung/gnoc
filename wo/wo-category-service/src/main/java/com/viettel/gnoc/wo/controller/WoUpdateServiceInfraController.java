package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.WoUpdateServiceInfraBusiness;
import com.viettel.gnoc.wo.dto.WoUpdateServiceInfraDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "woUpdateServiceInfra")
@Slf4j
public class WoUpdateServiceInfraController {

  @Autowired
  protected WoUpdateServiceInfraBusiness woUpdateServiceInfraBusiness;

  @Autowired
  protected CatItemBusiness catItemBusiness;

  @PostMapping("/getListWoUpdateServiceInfra")
  public ResponseEntity<Datatable> getListWoMaterialPage(
      @RequestBody WoUpdateServiceInfraDTO woUpdateServiceInfraDTO) {
    Datatable data = woUpdateServiceInfraBusiness
        .getListWoUpdateServiceInfraPage(woUpdateServiceInfraDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> update(
      @RequestBody WoUpdateServiceInfraDTO woUpdateServiceInfraDTO) {
    ResultInSideDto data = woUpdateServiceInfraBusiness.update(woUpdateServiceInfraDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
