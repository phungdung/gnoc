package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CrManagerScopesOfRolesBusiness;
import com.viettel.gnoc.cr.business.CrManagerUnitsOfScopeBusiness;
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import com.viettel.gnoc.cr.dto.CrManagerUnitsOfScopeDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.io.File;
import java.util.List;
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
@RequestMapping(Constants.OD_API_PATH_PREFIX + "CrManagerUnitsOfScope")
public class CrManagerUnitsOfScopeController {

  @Autowired
  protected CrManagerUnitsOfScopeBusiness crManagerUnitsOfScopeBusiness;

  @Autowired
  protected CrManagerScopesOfRolesBusiness crManagerScopesOfRolesBusiness;

  @PostMapping("/getListCrManagerUnitsOfScope")
  public ResponseEntity<Datatable> getListCrManagerUnitsOfScope(@RequestBody
      CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    Datatable data = crManagerUnitsOfScopeBusiness.getListUnitOfScope
        (crManagerUnitsOfScopeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDeatil")
  public ResponseEntity<CrManagerUnitsOfScopeDTO> getDeatil(Long cmnoseId) {
    return new ResponseEntity<>(crManagerUnitsOfScopeBusiness.getDetail(cmnoseId), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEPARTMENTS_SCOPE_MANAGEMENT)
  @PostMapping("/addCrManagerUnitsOfScope")
  public ResponseEntity<ResultInSideDto> addCrManagerUnitsOfScope
      (@Valid @RequestBody CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    return new ResponseEntity<>(crManagerUnitsOfScopeBusiness.addCrManagerUnitsOfScope
        (crManagerUnitsOfScopeDTO), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEPARTMENTS_SCOPE_MANAGEMENT)
  @PutMapping("/updateCrManagerUnitsOfScope")
  public ResponseEntity<ResultInSideDto> updateCrManagerUnitsOfScope
      (@Valid @RequestBody CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    return new ResponseEntity<>(crManagerUnitsOfScopeBusiness.updateCrManagerUnitsOfScope
        (crManagerUnitsOfScopeDTO), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEPARTMENTS_SCOPE_MANAGEMENT)
  @DeleteMapping("deleteCrManagerUnitsOfScope")
  public ResponseEntity<ResultInSideDto> deleteCrManagerUnitsOfScope(Long cmnoseId) {
    return new ResponseEntity<>(crManagerUnitsOfScopeBusiness.deleteCrManagerUnitsOfScope
        (cmnoseId), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.DEPARTMENTS_SCOPE_MANAGEMENT)
  @DeleteMapping("deleteListCrManagerUnitsOfScope")
  public ResponseEntity<ResultInSideDto> deleteListCrManagerUnitsOfScope
      (@RequestBody CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    return new ResponseEntity<>(crManagerUnitsOfScopeBusiness.deleteListCrManagerUnitsOfScope
        (crManagerUnitsOfScopeDTO), HttpStatus.OK);
  }

  @PostMapping("/exportData")
  public ResponseEntity<File> exportData(
      @RequestBody CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) throws Exception {
    return new ResponseEntity<>(crManagerUnitsOfScopeBusiness.exportData(crManagerUnitsOfScopeDTO),
        HttpStatus.OK);
  }

  @GetMapping("/getListDeviceTypeByImpactSegmentCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListDeviceTypeByImpactSegmentCBB(
      Long impactSegmentId) {
    return new ResponseEntity<>(
        crManagerUnitsOfScopeBusiness.getListDeviceTypeByImpactSegmentCBB(impactSegmentId),
        HttpStatus.OK);
  }

  @PostMapping("/findUnitByCodeOrName")
  public ResponseEntity<List<UnitDTO>> findUnitByCodeOrName(@RequestBody UnitDTO unitDTO) {
    return new ResponseEntity<>(crManagerUnitsOfScopeBusiness.findListUnitByCodeOrName(unitDTO),
        HttpStatus.OK);
  }

  @GetMapping("/getListManagerScopeCBB")
  public ResponseEntity<List<CrManagerScopeDTO>> getListManagerScopeCBB() {
    List<CrManagerScopeDTO> data = crManagerScopesOfRolesBusiness.getListManagerScopeCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListImpactSegmentCBB")
  public ResponseEntity<List<ImpactSegmentDTO>> getListImpactSegmentCBB() {
    List<ImpactSegmentDTO> data = crManagerUnitsOfScopeBusiness.getListImpactSegmentCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
