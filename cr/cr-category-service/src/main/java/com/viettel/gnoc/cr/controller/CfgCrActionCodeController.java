package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CfgCrActionCodeBusiness;
import com.viettel.gnoc.cr.dto.CrActionCodeDTO;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CfgCrActionCodeController")
public class CfgCrActionCodeController {

  @Autowired
  private CfgCrActionCodeBusiness cfgCrActionCodeBusiness;

  @PostMapping("/findCfgCrActionCodeById")
  public ResponseEntity<CrActionCodeDTO> findCfgCrActionCodeById(Long id) {
    CrActionCodeDTO crActionCodeDTO = cfgCrActionCodeBusiness
        .findCrActionCodeById(id);
    return new ResponseEntity<>(crActionCodeDTO, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_ACTION_CODE)
  @PostMapping("/insertCfgCrActionCode")
  public ResponseEntity<ResultInSideDto> insertCfgCrActionCode(
      @Valid @RequestBody CrActionCodeDTO crActionCodeDTO) {
    ResultInSideDto resultDto = cfgCrActionCodeBusiness
        .insertCfgCrActionCode(crActionCodeDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_ACTION_CODE)
  @PostMapping("/deleteCfgCrActionCodeById")
  public ResponseEntity<ResultInSideDto> deleteCfgCrActionCodeById(@RequestParam Long id) {
    String resultDto = cfgCrActionCodeBusiness.deleteCfgCrActionCodeById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_ACTION_CODE)
  @PostMapping("/updateCfgCrActionCode")
  public ResponseEntity<ResultInSideDto> updateCfgCrActionCode(
      @Valid @RequestBody CrActionCodeDTO dto) {
    String resultDto = cfgCrActionCodeBusiness.updateCfgCrActionCode(dto);
    return new ResponseEntity<>(new ResultInSideDto(dto.getCrActionCodeId(), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/getListCfgCrActionCode")
  public ResponseEntity<Datatable> getListCfgCrActionCode(
      @RequestBody CrActionCodeDTO crActionCodeDTO) {
    Datatable data = cfgCrActionCodeBusiness.getListCfgCrActionCode(crActionCodeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
