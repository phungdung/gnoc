package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.CfgFollowWorkTimeBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.CfgFollowWorkTimeDTO;
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
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CfgFollowWorkTime")
public class CfgFollowWorkTimeController {

  @Autowired
  CfgFollowWorkTimeBusiness cfgFollowWorkTimeBusiness;

  @PostMapping("/listCfgFollowWorkTimePage")
  public ResponseEntity<Datatable> getListCfgFollowWorkTimePage(
      @RequestBody CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO) {
    Datatable data = cfgFollowWorkTimeBusiness.getListCfgFollowWorkTimePage(cfgFollowWorkTimeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertCfgFollowWorkTime")
  public ResponseEntity<ResultInSideDto> add(
      @Valid @RequestBody CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO) {
    ResultInSideDto data = cfgFollowWorkTimeBusiness.insert(cfgFollowWorkTimeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateCfgFollowWorkTime")
  public ResponseEntity<ResultInSideDto> edit(
      @Valid @RequestBody CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO) {
    ResultInSideDto data = cfgFollowWorkTimeBusiness.update(cfgFollowWorkTimeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteCfgFollowWorkTime")
  public ResponseEntity<ResultInSideDto> deleteCfgFollowWorkTime(
      Long configFollowWorkTimeId) {
    ResultInSideDto data = cfgFollowWorkTimeBusiness
        .delete(configFollowWorkTimeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailByCfgFollowWorkTimeId")
  public ResponseEntity<CfgFollowWorkTimeDTO> getDetailByCfgFollowWorkTimeId(
      Long configFollowWorkTimeId) {
    CfgFollowWorkTimeDTO data = cfgFollowWorkTimeBusiness.getDetail(configFollowWorkTimeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getFieldCombobox")
  public ResponseEntity<List<CatItemDTO>> getFieldCombobox(String systemCode) {
    List<CatItemDTO> data = cfgFollowWorkTimeBusiness.getListItemByCategory(systemCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getStepCombobox")
  public ResponseEntity<List<CatItemDTO>> getStepCombobox(String systemCode) {
    List<CatItemDTO> data = cfgFollowWorkTimeBusiness.getListItemByCat(systemCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
