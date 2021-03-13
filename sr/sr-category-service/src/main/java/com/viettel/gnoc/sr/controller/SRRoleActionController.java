package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.business.SRFlowExecuteBusiness;
import com.viettel.gnoc.sr.business.SRRoleActionBusiness;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
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
@RequestMapping(Constants.OD_API_PATH_PREFIX + "srRoleActions")
@Slf4j
public class SRRoleActionController {

  @Autowired
  protected SRRoleActionBusiness srRoleActionBusiness;

  @Autowired
  protected SRFlowExecuteBusiness srFlowExecuteBusiness;


  @PostMapping("/listSRRoleActionPage")
  public ResponseEntity<Datatable> getListSRRoleActionPage(
      @RequestBody SRFlowExecuteDTO srFlowExecuteDTO) {
    Datatable data = srFlowExecuteBusiness.getListSRFlowExecute(srFlowExecuteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> add(
      @RequestBody SRFlowExecuteDTO srFlowExecuteDTO) {
    ResultInSideDto data = srRoleActionBusiness.insert(srFlowExecuteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateRoleActions")
  public ResponseEntity<ResultInSideDto> editRoleActions(
      @RequestBody SRFlowExecuteDTO srFlowExecuteDTO) {
    ResultInSideDto data = srRoleActionBusiness.update(srFlowExecuteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteByFlowExecuteId")
  public ResponseEntity<ResultInSideDto> delete(String listflowId) {
    ResultInSideDto data = srFlowExecuteBusiness.delete(listflowId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailByFlowExecuteId")
  public ResponseEntity<SRFlowExecuteDTO> getDetailByFlowExecuteId(String listflowId) {
    SRFlowExecuteDTO data = srFlowExecuteBusiness.getDetail(listflowId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailByRoleActionId")
  public ResponseEntity<SRRoleActionDTO> getDetailByRoleActionId(Long roleActionId) {
    SRRoleActionDTO data = srRoleActionBusiness.getDetail(roleActionId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getComboBoxStatus")
  public ResponseEntity<List<SRRoleActionDTO>> getComboBoxStatus() {
    List<SRRoleActionDTO> data = srRoleActionBusiness.getComboBoxStatus();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getComboBoxRoleType")
  public ResponseEntity<List<SRRoleActionDTO>> getComboBoxRoleType() {
    List<SRRoleActionDTO> data = srRoleActionBusiness.getComboBoxRoleType();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getComboBoxActions")
  public ResponseEntity<List<SRRoleActionDTO>> getComboBoxActions(String roleType) {
    List<SRRoleActionDTO> data = srRoleActionBusiness.getComboBoxActions(roleType);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getComboBoxGroupRole")
  public ResponseEntity<List<SRRoleActionDTO>> getComboBoxGroupRole() {
    List<SRRoleActionDTO> data = srRoleActionBusiness.getComboBoxGroupRole();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSRRoleActionDTO")
  public ResponseEntity<List<SRRoleActionDTO>> getListSRRoleActionDTO(
      @RequestBody SRRoleActionDTO srRoleActionDTO) {
    List<SRRoleActionDTO> data = srRoleActionBusiness.getListSRRoleActionDTO(srRoleActionDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
