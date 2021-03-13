package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CrManagerScopeBusiness;
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrManagerScopeController")
public class CrManagerScopeController {

  @Autowired
  CrManagerScopeBusiness crManagerScopeBusiness;

  @PostMapping("/getListCrManagerScopeSearch")
  public ResponseEntity<Datatable> getListCrManagerScopeSearch(
      @RequestBody CrManagerScopeDTO crManagerScopeDTO) {
    Datatable data = crManagerScopeBusiness.getListCrManagerScopeSearch(crManagerScopeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.SCOPES_MANAGEMENT)
  @PostMapping("/insertCrManagerScope")
  public ResponseEntity<ResultInSideDto> insertCrManagerScope(@Valid
  @RequestBody CrManagerScopeDTO crManagerScopeDTO) {
    ResultInSideDto data = crManagerScopeBusiness.insertCrManagerScope(crManagerScopeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.SCOPES_MANAGEMENT)
  @PostMapping("/updateCrManagerScope")
  public ResponseEntity<ResultInSideDto> updateCrManagerScope(@Valid
  @RequestBody CrManagerScopeDTO crManagerScopeDTO) {
    ResultInSideDto data = crManagerScopeBusiness.updateCrManagerScope(crManagerScopeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.SCOPES_MANAGEMENT)
  @PostMapping("/deleteListCrManagerScope")
  public ResponseEntity<ResultInSideDto> deleteListCrManagerScope(
      @RequestBody List<CrManagerScopeDTO> crManagerScopeDTO) {
    ResultInSideDto data = crManagerScopeBusiness.deleteListCrManagerScope(crManagerScopeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.SCOPES_MANAGEMENT)
  @PostMapping("/updateLisCrManagerScope")
  public ResponseEntity<ResultInSideDto> updateLisCrManagerScope(
      @RequestBody List<CrManagerScopeDTO> crManagerScopeDTO) {
    ResultInSideDto data = crManagerScopeBusiness.updateLisCrManagerScope(crManagerScopeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findCrManagerScopeDTOById")
  public ResponseEntity<CrManagerScopeDTO> findCrManagerScopeDTOById(@RequestParam Long id) {
    CrManagerScopeDTO data = crManagerScopeBusiness.findCrManagerScopeDTOById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.SCOPES_MANAGEMENT)
  @GetMapping("/deleteCrManagerScope")
  public ResponseEntity<ResultInSideDto> deleteCrManagerScope(@RequestParam Long id) {
    ResultInSideDto data = crManagerScopeBusiness.deleteCrManagerScope(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
