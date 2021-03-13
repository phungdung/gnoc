package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CrManagerScopesOfRolesBusiness;
import com.viettel.gnoc.cr.dto.CrManagerRolesDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopesOfRolesDTO;
import java.io.File;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(Constants.OD_API_PATH_PREFIX + "CrManagerScopesOfRoles")
@Slf4j
public class CrManagerScopesOfRolesController {

  @Autowired
  protected CrManagerScopesOfRolesBusiness crManagerScopesOfRolesBusiness;

  @PostMapping("/getListCrManagerScopesOfRoles")
  public ResponseEntity<Datatable> getListManagerScopesOfRoles(@RequestBody
      CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
    Datatable data = crManagerScopesOfRolesBusiness.
        getListManagerScopesOfRoles(crManagerScopesOfRolesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.ROLES_SCOPE_MANAGEMENT)
  @PostMapping("/addCrManagerScopesOfRoles")
  public ResponseEntity<ResultInSideDto> addCrManagerScopesOfRoles
      (@Valid @RequestBody CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
    ResultInSideDto resultInSideDto = crManagerScopesOfRolesBusiness
        .addManagerScopesOfRoles(crManagerScopesOfRolesDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.ROLES_SCOPE_MANAGEMENT)
  @PutMapping("/updateCrManagerScopesOfRoles")
  public ResponseEntity<ResultInSideDto> updateCrManagerScopesOfRoles
      (@Valid @RequestBody CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
//    ResultInSideDto resultInSideDto = new ResultInSideDto();
    ResultInSideDto resultInSideDto = crManagerScopesOfRolesBusiness
        .updateManagerScopesOfRoles(crManagerScopesOfRolesDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.ROLES_SCOPE_MANAGEMENT)
  @DeleteMapping("/deleteCrManagerScopesOfRoles")
  public ResponseEntity<ResultInSideDto> deleteCrManagerScopesOfRoles
      (Long cmsorsId) {
    return new ResponseEntity<>(crManagerScopesOfRolesBusiness
        .deleteManagerScopesOfRoles(cmsorsId), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.ROLES_SCOPE_MANAGEMENT)
  @DeleteMapping("/deleteListCrManagerScopesOfRoles")
  public ResponseEntity<ResultInSideDto> deleteListCrManagerScopesOfRoles
      (@RequestBody CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
    return new ResponseEntity<>(crManagerScopesOfRolesBusiness
        .deleteListManagerScopesOfRoles(crManagerScopesOfRolesDTO), HttpStatus.OK);
  }

  @PostMapping("/exportData")
  public ResponseEntity<File> exportData(
      @RequestBody CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) throws Exception {
    File data = crManagerScopesOfRolesBusiness.exportData(crManagerScopesOfRolesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<CrManagerScopesOfRolesDTO> getDetail(Long cmsorsId) {
    CrManagerScopesOfRolesDTO data = crManagerScopesOfRolesBusiness.getDetail(cmsorsId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListManagerScopeCBB")
  public ResponseEntity<List<CrManagerScopeDTO>> getListManagerScopeCBB() {
    List<CrManagerScopeDTO> data = crManagerScopesOfRolesBusiness.getListManagerScopeCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListCrManagerRolesCBB")
  public ResponseEntity<List<CrManagerRolesDTO>> getListCrManagerRolesCBB() {
    List<CrManagerRolesDTO> data = crManagerScopesOfRolesBusiness.getListCrManagerRolesCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
