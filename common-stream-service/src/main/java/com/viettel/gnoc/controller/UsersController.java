package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserUpdateHisDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
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
import viettel.passport.client.UserToken;


/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "UsersService")
public class UsersController {

  @Autowired
  private UserBusiness userBusiness;
  @Autowired
  TicketProvider ticketProvider;

  @PostMapping("/getListUserDTO")
  public ResponseEntity<Datatable> getListUserDTO(@RequestBody UsersInsideDto usersInsideDto) {
    Datatable datatable = userBusiness.getListUsersDTO(usersInsideDto);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getListUserApproveDTO")
  public ResponseEntity<Datatable> getListUserApproveDTO(
      @RequestBody UsersInsideDto usersInsideDto) {
    Datatable datatable = userBusiness.getListUsersApproveDTO(usersInsideDto);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getListUserDTOByProxy")
  public List<UsersInsideDto> getListUserDTOByProxy(@RequestBody UsersInsideDto usersInsideDto) {
    List<UsersInsideDto> listUsersDTO = userBusiness
        .getListUsersDTOS(usersInsideDto);
    return listUsersDTO;
  }

  @GetMapping("/getUserDetailById")
  public ResponseEntity<UsersInsideDto> getUserDetailById(Long userId) {
    return new ResponseEntity<>(userBusiness.getUserDetailById(userId), HttpStatus.OK);
  }

//  @PostMapping("/updateUserApprove")
//  public ResponseEntity<ResultInSideDto> updateUserApprove(
//      @RequestBody ResultInSideDto resultInSideDto) {
//    ResultInSideDto result = userBusiness
//        .updateUserApprove(resultInSideDto.getCheck(), resultInSideDto.getId(),
//            resultInSideDto.getDescription());
//    return new ResponseEntity<>(result, HttpStatus.OK);
//  }

  @PostMapping("/approveUser")
  public ResponseEntity<ResultInSideDto> approveUser(@RequestBody UsersInsideDto usersInsideDto) {
    return new ResponseEntity<>(userBusiness.approveUser(usersInsideDto), HttpStatus.OK);
  }

  @PostMapping("/refuseUser")
  public ResponseEntity<ResultInSideDto> refuseUser(@RequestBody UsersInsideDto usersInsideDto) {
    return new ResponseEntity<>(userBusiness.refuseUser(usersInsideDto), HttpStatus.OK);
  }

  @PostMapping("/getListUserDTOS")
  public ResponseEntity<List<UsersInsideDto>> getListUserDTOS(
      @RequestBody UsersInsideDto usersInsideDto) {
    List<UsersInsideDto> datatable = userBusiness.getListUsersDTOS(usersInsideDto);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getListUsersByList")
  public ResponseEntity<Datatable> getListUsersByList(@RequestBody UsersInsideDto usersInsideDto) {
    //List user get catItem: CHAT_CODE
    Datatable datatable = userBusiness.getListUsersByList(usersInsideDto);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/updateUserTimeZone")
  public ResponseEntity<ResultInSideDto> updateUserTimeZone(@RequestParam String timeZoneId) {
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto result = userBusiness
        .updateUserTimeZone(String.valueOf(userToken.getUserID()), timeZoneId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/updateUserLanguage")
  public ResponseEntity<ResultInSideDto> updateUserLanguage(@RequestParam String languageId) {
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto result = userBusiness
        .updateUserLanguage(String.valueOf(userToken.getUserID()), languageId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getUserInfor")
  public ResponseEntity<UsersInsideDto> getUserInfor() {
    UsersInsideDto data = userBusiness.getUserInfor();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateUserInfor")
  public ResponseEntity<ResultInSideDto> updateUserInfor(
      @RequestBody UsersInsideDto usersInsideDto) {
    ResultInSideDto data = userBusiness.updateUserInfor(usersInsideDto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getImpactSegment")
  public ResponseEntity<List<ImpactSegmentDTO>> getImpactSegment(String system, String active) {
    List<ImpactSegmentDTO> data = userBusiness.getImpactSegment(system, active);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteUser")
  public ResponseEntity<ResultInSideDto> deleteUser(Long userId) {
    ResultInSideDto result = userBusiness.deleteUser(userId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/addUser")
  public ResponseEntity<ResultInSideDto> addUser(@RequestBody UsersInsideDto usersInsideDto) {
    ResultInSideDto data = userBusiness.addUser(usersInsideDto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getUserDetaiById")
  public ResponseEntity<UsersInsideDto> getUserDetaiById(Long id) {
    UsersInsideDto data = userBusiness.getUserDetaiById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateUser")
  public ResponseEntity<ResultInSideDto> updateUser(@RequestBody UsersInsideDto usersInsideDto) {
    ResultInSideDto data = userBusiness.updateUser(usersInsideDto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListUserHistory")
  public ResponseEntity<Datatable> getListUserHistory(
      @RequestBody UserUpdateHisDTO userUpdateHisDTO) {
    Datatable data = userBusiness.getListUserHistory(userUpdateHisDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findUserHistoryById")
  public ResponseEntity<UserUpdateHisDTO> findUserHistoryById(Long hisId) {
    UserUpdateHisDTO data = userBusiness.findUserHistoryById(hisId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/checkingRole")
  public ResponseEntity<UsersInsideDto> checkingRole() {
    UsersInsideDto data = userBusiness.checkRole();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
