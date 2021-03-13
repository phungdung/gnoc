package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrUserCfgApprovedSmsBtsDTO;
import com.viettel.gnoc.mr.business.MrUserCfgApprovedSmsBtsBusiness;
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
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "mrHardElectronicBTSCycle")
public class MrUserCfgApprovedSmsBtsController {

  @Autowired
  MrUserCfgApprovedSmsBtsBusiness mrUserCfgApprovedSmsBtsBusiness;

  @Autowired
  UserBusiness userBusiness;

  @PostMapping("/getListMrUserCfgApprovedSmsBts")
  public ResponseEntity<Datatable> getListMrUserCfgApprovedSmsBts(
      @RequestBody MrUserCfgApprovedSmsBtsDTO smsBtsDTO) {
    Datatable data = mrUserCfgApprovedSmsBtsBusiness.getListMrUserCfgApprovedSmsBts(smsBtsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<MrUserCfgApprovedSmsBtsDTO> getDetail(Long id) {
    MrUserCfgApprovedSmsBtsDTO data = mrUserCfgApprovedSmsBtsBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdate")
  public ResponseEntity<ResultInSideDto> insertOrUpdate(
      @RequestBody MrUserCfgApprovedSmsBtsDTO smsBtsDTO) {
    ResultInSideDto data = mrUserCfgApprovedSmsBtsBusiness.insertOrUpdate(smsBtsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteMrUserCfgApprovedSmsBts")
  public ResponseEntity<ResultInSideDto> deleteMrUserCfgApprovedSmsBts(Long userCfgApprovedSmsId) {
    ResultInSideDto data = mrUserCfgApprovedSmsBtsBusiness
        .deleteMrUserCfgApprovedSmsBts(userCfgApprovedSmsId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //getUserByUserID
  @GetMapping("/getUserByUserId")
  public ResponseEntity<UsersInsideDto> getUserByUserId(Long userId) {
    UsersInsideDto data = mrUserCfgApprovedSmsBtsBusiness.getUserByUserId(userId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //getApproveLevelByUserLogin
  @GetMapping("/getApproveLevelByUserLogin")
  public ResponseEntity<MrUserCfgApprovedSmsBtsDTO> getApproveLevelByUserLogin() {
    MrUserCfgApprovedSmsBtsDTO data = mrUserCfgApprovedSmsBtsBusiness.getApproveLevelByUserLogin();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
