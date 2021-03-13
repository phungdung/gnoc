package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocTimezoneDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.business.CrCableBusiness;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.nims.infra.webservice.InfraSleevesBO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrCableService")
@Slf4j
public class CrCableController {

  @Autowired
  CommonBusiness commonBusiness;

  @Autowired
  CrCableBusiness crCableBusiness;

  @GetMapping("/getAllGnocTimezone")
  public ResponseEntity<List<GnocTimezoneDto>> getAllGnocTimezone() {
    List<GnocTimezoneDto> gnocTimezoneDtos = commonBusiness.getAllGnocTimezone();
    return new ResponseEntity<>(gnocTimezoneDtos, HttpStatus.OK);
  }

  //Tuyen cap bang ben trong
  @PostMapping("/GNOC_getInfraCableLane")
  public ResponseEntity<Datatable> GNOC_getInfraCableLane(@RequestBody CrCableDTO crCableDTO) {
    Datatable datatable = crCableBusiness
        .GNOC_getInfraCableLane(crCableDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  //Doan cap bang ben trong
  @PostMapping("/getAllCableInLane")
  public ResponseEntity<Datatable> getAllCableInLane(@RequestBody CrCableDTO crCableDTO) {
    Datatable datatable = crCableBusiness
        .getAllCableInLane(crCableDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @GetMapping("/getSleevesInCable")
  public ResponseEntity<List<InfraSleevesBO>> getSleevesInCable(String sleeveCode, Long purpose,
      String cableCode) {
    List<InfraSleevesBO> lstSleevesInCable = crCableBusiness
        .getSleevesInCable(sleeveCode, purpose, cableCode);
    return new ResponseEntity<>(lstSleevesInCable, HttpStatus.OK);
  }

  //Thong tin link khi bam nut search link
  @PostMapping("/getLinkInfo")
  public ResponseEntity<Datatable> getLinkInfo(@RequestBody CrCableDTO crCableDTO) {
    Datatable datatable = crCableBusiness.getLinkInfo(crCableDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  //Tuyen cap tac dong
  @PostMapping("/getListCrCableByCondition")
  public ResponseEntity<List<CrCableDTO>> getListCrCableByCondition(
      @RequestBody CrInsiteDTO crInsiteDTO) {
    List<CrCableDTO> lstCable = crCableBusiness
        .getListCrCableByCondition(crInsiteDTO);
    return new ResponseEntity<>(lstCable, HttpStatus.OK);
  }

}
