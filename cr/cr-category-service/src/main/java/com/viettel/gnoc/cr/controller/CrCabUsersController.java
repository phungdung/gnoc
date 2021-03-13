package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CrCabUsersBusiness;
import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.List;
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
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrCabUsersController")
public class CrCabUsersController {

  @Autowired
  CrCabUsersBusiness crCabUsersBusiness;

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_CAB_USERS)
  @PostMapping("/insertCrCabUsers")
  public ResponseEntity<ResultInSideDto> insertCrCabUsers(
      @RequestBody CrCabUsersDTO crCabUsersDTO) {
    ResultInSideDto data = crCabUsersBusiness.insertCrCabUsers(crCabUsersDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_CAB_USERS)
  @PostMapping("/updateCrCabUsers")
  public ResponseEntity<ResultInSideDto> updateCrCabUsers(
      @RequestBody CrCabUsersDTO crCabUsersDTO) {
    ResultInSideDto data = crCabUsersBusiness.updateCrCabUsers(crCabUsersDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_CAB_USERS)
  @PostMapping("/deleteListCrCabUsers")
  public ResponseEntity<ResultInSideDto> deleteListCrCabUsers(
      @RequestBody List<CrCabUsersDTO> crCabUsersDTO) {
    ResultInSideDto data = crCabUsersBusiness.deleteListCrCabUsers(crCabUsersDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getAllInfoCrCABUsers")
  public ResponseEntity<Datatable> getAllInfoCrCABUsers(@RequestBody CrCabUsersDTO crCabUsersDTO) {
    Datatable data = crCabUsersBusiness.getAllInfoCrCABUsers(crCabUsersDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_CAB_USERS)
  @GetMapping("/deleteCrCabUsers")
  public ResponseEntity<ResultInSideDto> deleteCrCabUsers(@RequestParam Long id) {
    ResultInSideDto data = crCabUsersBusiness.deleteCrCabUsers(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getAllUserInUnitCrCABUsers")
  public ResponseEntity<List<UnitDTO>> getAllUserInUnitCrCABUsers(@RequestParam Long id) {
    List<UnitDTO> data = crCabUsersBusiness
        .getAllUserInUnitCrCABUsers(id, null, "", "", "", "", "");
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListImpactSegmentCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListImpactSegmentCBB() {
    List<ItemDataCRInside> data = crCabUsersBusiness.getListImpactSegmentCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findById")
  public ResponseEntity<CrCabUsersDTO> findById(@RequestParam Long id) {
    CrCabUsersDTO data = crCabUsersBusiness.findById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
