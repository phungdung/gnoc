package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.sr.business.SRRoleBusiness;
import com.viettel.gnoc.sr.business.SRRoleUserBusiness;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "srRoleUser")
@Slf4j
public class SRRoleUserController {

  @Autowired
  protected SRRoleUserBusiness srRoleUserBusiness;

  @Autowired
  protected SRRoleBusiness srRoleBusiness;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Autowired
  protected UnitBusiness unitBusiness;

  @PostMapping("/listSRRoleUserDTO")
  public ResponseEntity<Datatable> getListSRRoleUserPage(
      @RequestBody SRRoleUserInSideDTO srRoleUserDTO) {
    Datatable data = srRoleUserBusiness.getListSRRoleUserPage(srRoleUserDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getlistSRRoleUserDTO")
  public ResponseEntity<List<SRRoleUserInSideDTO>> getlistSRRoleUserDTO(
      @RequestBody SRRoleUserInSideDTO srRoleUserDTO) {
    List<SRRoleUserInSideDTO> data = srRoleUserBusiness.getListSRRoleUser(srRoleUserDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.SR_ROLE_USER)
  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> add(
      @Valid @RequestBody SRRoleUserInSideDTO srRoleUserDTO) {
    ResultInSideDto data = srRoleUserBusiness.insert(srRoleUserDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.SR_ROLE_USER)
  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> edit(
      @Valid @RequestBody SRRoleUserInSideDTO srRoleUserDTO) {
    ResultInSideDto data = srRoleUserBusiness.update(srRoleUserDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.SR_ROLE_USER)
  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long roleUserId) {
    ResultInSideDto data = srRoleUserBusiness.delete(roleUserId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailByRoleUserId")
  public ResponseEntity<SRRoleUserInSideDTO> getDetailByRoleUserId(Long roleUserId) {
    SRRoleUserInSideDTO data = srRoleUserBusiness.getDetail(roleUserId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //  //lever 1 (tren common)
//  @GetMapping("/getListLocationByLevelCBB")
//  public ResponseEntity<List<ItemDataCRInside>> getListLocationByLevelCBB(Long level, Long parentId) {
//    List<ItemDataCRInside> catLocationDTOS = catLocationBusiness
//        .getListLocationByLevelCBB(null, level, parentId);
//    return new ResponseEntity<>(catLocationDTOS, HttpStatus.OK);
//  }
//
//  //lay tren common nhe
//  @PostMapping("/getListUnit")
//  public ResponseEntity<List<UnitDTO>> getListUnit(@RequestBody UnitDTO unitDTO) {
//    List<UnitDTO> data = unitBusiness.getListUnit(unitDTO);
//    return new ResponseEntity<>(data, HttpStatus.OK);
//  }

  @PostMapping("/getListSRRoleByLocationCBB")
  public ResponseEntity<List<SRRoleDTO>> getListSRRoleByLocationCBB(
      @RequestBody SRRoleDTO srRoleDTO) {
    List<SRRoleDTO> data = srRoleBusiness.getListSRRoleByLocationCBB(srRoleDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListUser/unitId{unitId}/country{country}/username{username}")
  public ResponseEntity<List<SRRoleUserInSideDTO>> getListUser(
      @PathVariable("unitId") String unitId,
      @PathVariable("country") String country,
      @PathVariable("username") String username) {
    return new ResponseEntity<>(srRoleUserBusiness.getListUser(unitId, country, username),
        HttpStatus.OK);
  }
}
