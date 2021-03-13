package com.viettel.gnoc.cr.controller;


import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CrRolesDeptBusiness;
import com.viettel.gnoc.cr.dto.CrRoleDeptDTO;
import com.viettel.gnoc.cr.dto.CrRoleDeptDTOSearch;
import com.viettel.gnoc.cr.dto.CrRolesDTO;
import com.viettel.gnoc.cr.model.CrRoleDeptEntity;
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
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrRolesDeptController")
public class CrRolesDeptController {

  @Autowired
  CrRolesDeptBusiness crRolesBusiness;

  @PostMapping("/getListCrRolesDTO")
  public ResponseEntity<List<CrRolesDTO>> getListCrRolesDTO(@RequestBody CrRolesDTO crRolesDTO) {
    List<CrRolesDTO> data = crRolesBusiness.getListCrRolesDTO(crRolesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEPARTMENTS_ROLES_MANAGEMENT)
  @PostMapping("/insertCrRoles")
  public ResponseEntity<ResultInSideDto> insertCrRoles(@Valid @RequestBody CrRolesDTO crRolesDTO) {
    ResultInSideDto data = crRolesBusiness.insertCrRoles(crRolesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEPARTMENTS_ROLES_MANAGEMENT)
  @PostMapping("/updateCrRoles")
  public ResponseEntity<ResultInSideDto> updateCrRoles(@Valid @RequestBody CrRolesDTO crRolesDTO) {
    ResultInSideDto data = crRolesBusiness.updateCrRoles(crRolesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findCrRolesDTOById")
  public ResponseEntity<CrRolesDTO> findCrRolesDTOById(@RequestParam Long id) {
    CrRolesDTO data = crRolesBusiness.findCrRolesDTOById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEPARTMENTS_ROLES_MANAGEMENT)
  @PostMapping("/insertRoleDept")
  public ResponseEntity<ResultInSideDto> insertRoleDept(
      @Valid @RequestBody CrRoleDeptDTO crRoleDeptDTO) {
    ResultInSideDto data = crRolesBusiness.insertRoleDept(crRoleDeptDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEPARTMENTS_ROLES_MANAGEMENT)
  @PostMapping("/updateRoleDept")
  public ResponseEntity<ResultInSideDto> updateRoleDept(
      @Valid @RequestBody CrRoleDeptDTO crRoleDeptDTO) {
    ResultInSideDto data = crRolesBusiness.updateRoleDept(crRoleDeptDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEPARTMENTS_ROLES_MANAGEMENT)
  @GetMapping("/deleteRoleDept")
  public ResponseEntity<ResultInSideDto> deleteRoleDept(@RequestParam Long id) {
    ResultInSideDto data = crRolesBusiness.deleteRoleDept(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findCrRoleDeptEntityById")
  public ResponseEntity<CrRoleDeptEntity> findCrRoleDeptEntityById(@RequestParam Long id) {
    CrRoleDeptEntity data = crRolesBusiness.findCrRoleDeptEntityById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListRoleDept")
  public ResponseEntity<Datatable> getListRoleDept(
      @RequestBody CrRoleDeptDTOSearch crRoleDeptDTOSearch) {
    Datatable data = crRolesBusiness.getListRoleDept(crRoleDeptDTOSearch);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
