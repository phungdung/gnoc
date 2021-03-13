package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrApprovalDepartmentDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveRolesDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import com.viettel.gnoc.mr.business.MrApprovalDepartmentBusiness;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrApprovalDepartment")
public class MrApprovalDepartmentController {

  @Autowired
  MrApprovalDepartmentBusiness mrApprovalDepartmentBusiness;

  @PostMapping("/insertMrApprovalDepartment")
  public ResponseEntity<ResultInSideDto> insertMrApprovalDepartment(@RequestBody
                                                                        MrApprovalDepartmentDTO mrApprovalDepartmentDTO) {
    ResultInSideDto result = mrApprovalDepartmentBusiness
        .insertMrApprovalDepartment(mrApprovalDepartmentDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getLstMrApproveUserByRole")
  public ResponseEntity<List<MrApproveRolesDTO>> getLstMrApproveUserByRole(
      @RequestBody MrApproveRolesDTO mrRole) {
    List<MrApproveRolesDTO> result = mrApprovalDepartmentBusiness
        .getLstMrApproveUserByRole(mrRole);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getLstMrApproveSearch")
  public ResponseEntity<List<MrApproveSearchDTO>> getLstMrApproveSearch(
      @RequestBody MrApproveSearchDTO mrApproveSearchDTO) {
    List<MrApproveSearchDTO> result = mrApprovalDepartmentBusiness
        .getLstMrApproveSearch(mrApproveSearchDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  //Dung lv commit call proxy
  @GetMapping("/getLstMrApproveDeptByUser/userId{userId}")
  public List<MrApproveSearchDTO> getLstMrApproveDeptByUser(
      @PathVariable("userId") String userId) {
    return mrApprovalDepartmentBusiness
        .getLstMrApproveDeptByUser(userId);
  }

}
