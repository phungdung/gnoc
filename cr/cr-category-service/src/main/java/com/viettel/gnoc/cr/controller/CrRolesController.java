package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CrRolesBusiness;
import com.viettel.gnoc.cr.dto.CrRolesDTO;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DungPV
 */
@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrRoles")
public class CrRolesController {

  @Autowired
  protected CrRolesBusiness crRolesBusiness;

  @PostMapping("/getListCrRoles")
  public ResponseEntity<Datatable> getListCrRoles(@RequestBody CrRolesDTO crRolesDTO) {
    return new ResponseEntity<>(crRolesBusiness.getListCrRoles(crRolesDTO), HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<CrRolesDTO> getDetail(Long cmreId) {
    return new ResponseEntity<>(crRolesBusiness.getDetail(cmreId), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.ROLES_MANAGEMENT)
  @PostMapping("/addCrRoles")
  public ResponseEntity<ResultInSideDto> addCrRoles(@Valid @RequestBody CrRolesDTO crRolesDTO) {
    return new ResponseEntity<>(crRolesBusiness.addCrRoles(crRolesDTO), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.ROLES_MANAGEMENT)
  @PutMapping("/updateCrRoles")
  public ResponseEntity<ResultInSideDto> updateCrRoles(@Valid @RequestBody CrRolesDTO crRolesDTO) {
    return new ResponseEntity<>(crRolesBusiness.updateCrRoles(crRolesDTO), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.ROLES_MANAGEMENT)
  @DeleteMapping("/deleteCrRoles")
  public ResponseEntity<ResultInSideDto> deleteCrRoles(Long cmreId) {
    return new ResponseEntity<>(crRolesBusiness.deleteCrRoles(cmreId), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.ROLES_MANAGEMENT)
  @DeleteMapping("/deleteListCrRoles")
  public ResponseEntity<ResultInSideDto> deleteListCrRoles(@RequestBody CrRolesDTO crRolesDTO) {
    return new ResponseEntity<>(crRolesBusiness.deleteListCrRoles(crRolesDTO), HttpStatus.OK);
  }
}
