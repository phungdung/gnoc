package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.WoCdTempBusiness;
import com.viettel.gnoc.commons.config.SecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.PERMISSION;
import com.viettel.gnoc.commons.utils.Constants.ROLE;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoCdTempInsideDTO;
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

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "WoCdTempService")
public class WoCdTempController {

  @Autowired
  private WoCdTempBusiness woCdTempBusiness;

  @PostMapping("/getListWoCdTempDTO")
  public ResponseEntity<Datatable> getListWoCdTempDTO(
      @RequestBody WoCdTempInsideDTO woCdTempInsideDTO) {
    Datatable data = woCdTempBusiness.getListWoCdTempDTO(woCdTempInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCdGroupByUserCBB")
  public ResponseEntity<List<WoCdGroupInsideDTO>> getListCdGroupByUserCBB(
      @RequestBody WoCdGroupTypeUserDTO woCdGroupTypeUserDTO) {
    List<WoCdGroupInsideDTO> data = woCdTempBusiness.getListCdGroupByUser(woCdGroupTypeUserDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListUserByCdGroupCBB")
  public ResponseEntity<List<UsersInsideDto>> getListUserByCdGroupCBB(Long woGroupId) {
    List<UsersInsideDto> data = woCdTempBusiness.getListUserByCdGroupCBB(woGroupId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @SecurityAnnotation(role = ROLE.UTILITY_WO_CD_TEMP, permission = PERMISSION.ADD)
  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> insert(
      @RequestBody WoCdTempInsideDTO woCdTempInsideDTO) {
    ResultInSideDto data = woCdTempBusiness.insert(woCdTempInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @SecurityAnnotation(role = ROLE.UTILITY_WO_CD_TEMP, permission = PERMISSION.EDIT)
  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> update(
      @RequestBody WoCdTempInsideDTO woCdTempInsideDTO) {
    ResultInSideDto data = woCdTempBusiness.update(woCdTempInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<WoCdTempInsideDTO> getDetail(Long woCdTempId) {
    WoCdTempInsideDTO data = woCdTempBusiness.getDetail(woCdTempId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @SecurityAnnotation(role = ROLE.UTILITY_WO_CD_TEMP, permission = PERMISSION.DELETE)
  @PostMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(@RequestBody WoCdTempInsideDTO woCdTempInsideDTO) {
    ResultInSideDto data = woCdTempBusiness.delete(woCdTempInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
