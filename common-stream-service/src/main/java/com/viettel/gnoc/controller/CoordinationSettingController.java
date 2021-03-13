package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.CoordinationSettingBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.CoordinationSettingDTO;
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
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CoordinationSetting")
public class CoordinationSettingController {

  @Autowired
  CoordinationSettingBusiness coordinationSettingBusiness;

  @PostMapping("/listCoordinationSettingPage")
  public ResponseEntity<Datatable> getListCoordinationSettingPage(
      @RequestBody CoordinationSettingDTO coordinationSettingDTO) {
    Datatable data = coordinationSettingBusiness
        .getListCoordinationSettingPage(coordinationSettingDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //  @SecurityAnnotation(role = ROLE.UTILITY_COORDINATION_SETTING, permission = PERMISSION.ADD)
  @PostMapping("/insertCoordinationSetting")
  public ResponseEntity<ResultInSideDto> add(
      @Valid @RequestBody CoordinationSettingDTO coordinationSettingDTO) {
    ResultInSideDto data = coordinationSettingBusiness.insert(coordinationSettingDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //  @SecurityAnnotation(role = ROLE.UTILITY_COORDINATION_SETTING, permission = PERMISSION.EDIT)
  @PostMapping("/updateCoordinationSetting")
  public ResponseEntity<ResultInSideDto> edit(
      @RequestBody CoordinationSettingDTO coordinationSettingDTO) {
    ResultInSideDto data = coordinationSettingBusiness.update(coordinationSettingDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //  @SecurityAnnotation(role = ROLE.UTILITY_COORDINATION_SETTING, permission = PERMISSION.DELETE)
  @GetMapping("/deleteCoordinationSetting")
  public ResponseEntity<ResultInSideDto> deleteCoordinationSetting(
      Long id) {
    ResultInSideDto data = coordinationSettingBusiness
        .deleteCoordinationSetting(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailByCoordinationSettingId")
  public ResponseEntity<CoordinationSettingDTO> getDetailByCoordinationSettingId(Long id) {
    CoordinationSettingDTO data = coordinationSettingBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
