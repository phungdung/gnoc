package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CfgCrImpactFrameBusiness;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CfgCrImpactFrameController")
public class CfgCrImpactFrameController {

  @Autowired
  private CfgCrImpactFrameBusiness cfgCrImpactFrameBusiness;

  @PostMapping("/findCrImpactFrameById")
  public ResponseEntity<CrImpactFrameInsiteDTO> findCrImpactFrameById(Long id) {
    CrImpactFrameInsiteDTO crImpactFrameDTO = cfgCrImpactFrameBusiness
        .findCrImpactFrameById(id);
    return new ResponseEntity<>(crImpactFrameDTO, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_IMPACT_FRAME)
  @PostMapping("/insertCrImpactFrame")
  public ResponseEntity<ResultInSideDto> insertCrImpactFrame(
      @Valid @RequestBody CrImpactFrameInsiteDTO crImpactFrameDTO) {
    ResultInSideDto resultDto = cfgCrImpactFrameBusiness
        .insertCrImpactFrame(crImpactFrameDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_IMPACT_FRAME)
  @PostMapping("/deleteCrImpactFrameById")
  public ResponseEntity<ResultInSideDto> deleteCrImpactFrameById(@RequestParam Long id) {
    String resultDto = cfgCrImpactFrameBusiness.deleteCrImpactFrameById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_IMPACT_FRAME)
  @PostMapping("/updateCrImpactFrame")
  public ResponseEntity<ResultInSideDto> updateCrImpactFrame(
      @Valid @RequestBody CrImpactFrameInsiteDTO dto) {
    ResultInSideDto resultDto = cfgCrImpactFrameBusiness.updateCrImpactFrame(dto);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/getListCrImpactFrame")
  public ResponseEntity<Datatable> getListCrImpactFrame(
      @RequestBody CrImpactFrameInsiteDTO crImpactFrameDTO) {
    Datatable data = cfgCrImpactFrameBusiness.getListCrImpactFrame(crImpactFrameDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
