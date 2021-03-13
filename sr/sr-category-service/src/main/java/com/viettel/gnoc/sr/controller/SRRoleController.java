package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.business.SRRoleBusiness;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "srRole")
@Slf4j
public class SRRoleController {

  @Autowired
  protected SRRoleBusiness srRoleBusiness;

  @PostMapping("/listSRRoleDTO")
  public ResponseEntity<Datatable> getListSRRolePage(
      @RequestBody SRRoleDTO srRoleDTO) {
    Datatable data = srRoleBusiness.getListSRRolePage(srRoleDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> add(@Valid @RequestBody SRRoleDTO srRoleDTO) {
    ResultInSideDto data = srRoleBusiness.insert(srRoleDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> edit(@Valid @RequestBody SRRoleDTO srRoleDTO) {
    ResultInSideDto data = srRoleBusiness.update(srRoleDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long roleId) {
    ResultInSideDto data = srRoleBusiness.delete(roleId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailByRoleId")
  public ResponseEntity<SRRoleDTO> getDetailByRoleId(Long roleId) {
    SRRoleDTO data = srRoleBusiness.getDetail(roleId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

//  @PostMapping("/getListSRRoleByLocationCBB")
//  public ResponseEntity<List<SRRoleDTO>> getListSRRoleByLocationCBB(
//      @RequestBody SRRoleDTO srRoleDTO) {
//    List<SRRoleDTO> data = srRoleBusiness.getListSRRoleByLocationCBB(srRoleDTO);
//    return new ResponseEntity<>(data, HttpStatus.OK);
//  }

  @PostMapping("/getListSRRoleDTO")
  public ResponseEntity<List<SRRoleDTO>> getListSRRoleDTO(
      @RequestBody SRRoleDTO srRoleDTO) {
    List<SRRoleDTO> data = srRoleBusiness.getListSRRoleDTO(srRoleDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
