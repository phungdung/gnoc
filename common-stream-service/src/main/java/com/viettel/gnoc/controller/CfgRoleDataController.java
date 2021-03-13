package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CfgRoleDataBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CfgRoleDataDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import viettel.passport.client.UserToken;

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CfgRoleData")
public class CfgRoleDataController {

  @Autowired
  private CfgRoleDataBusiness cfgRoleDataBusiness;

  @PostMapping("/onSearchCfgRoleData")
  public ResponseEntity<Datatable> onSearchCfgRoleData(@RequestBody CfgRoleDataDTO cfgRoleDataDTO) {
    return new ResponseEntity<>(cfgRoleDataBusiness.onSearchCfgRoleData(cfgRoleDataDTO), HttpStatus.OK);
  }

  @PostMapping("/insertCfgRoleData")
  public ResponseEntity<ResultInSideDto> insertCfgRoleData(
      @RequestBody CfgRoleDataDTO cfgRoleDataDTO) {
    ResultInSideDto resultInSideDto = cfgRoleDataBusiness.insertCfgRoleData(cfgRoleDataDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateCfgRoleData")
  public ResponseEntity<ResultInSideDto> updateCfgRoleData(
      @RequestBody CfgRoleDataDTO cfgRoleDataDTO) {
    ResultInSideDto resultInSideDto = cfgRoleDataBusiness.updateCfgRoleData(cfgRoleDataDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/findCfgRoleDataById")
  public ResponseEntity<CfgRoleDataDTO> findCfgRoleDataById(Long id) {
    CfgRoleDataDTO cfgRoleDataDTO = cfgRoleDataBusiness.findCfgRoleDataById(id);
    return new ResponseEntity<>(cfgRoleDataDTO, HttpStatus.OK);
  }

  @GetMapping("/deleteCfgRoleData")
  public ResponseEntity<ResultInSideDto> deleteCfgRoleData(Long id) {
    String resultDTO = cfgRoleDataBusiness.deleteCfgRoleData(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDTO, resultDTO), HttpStatus.OK);
  }

  @GetMapping("/getUserByUserName")
  public ResponseEntity<UsersEntity> getUserByUserName(String username) {
    UsersEntity usersEntity = cfgRoleDataBusiness.getUserByUserName(username);
    return new ResponseEntity<>(usersEntity, HttpStatus.OK);
  }


}
