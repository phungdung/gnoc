package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CfgCreateWoBusiness;
import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CfgCreateWoInsiteDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CfgCreateWo")
public class CfgCreateWoController {

  @Autowired
  private CfgCreateWoBusiness cfgCreateWoBusiness;

  @PostMapping("/getListCfgCreateWoDTOPage")
  public ResponseEntity<Datatable> getListCfgCreateWoDTOPage(
      @RequestBody CfgCreateWoInsiteDTO cfgCreateWoDTO) {
    return new ResponseEntity<>(cfgCreateWoBusiness
        .getListCfgCreateWoDTOPage(cfgCreateWoDTO), HttpStatus.OK);
  }


  @GetMapping("/getDetailById")
  public ResponseEntity<CfgCreateWoInsiteDTO> getDetailById(Long id) {
    return new ResponseEntity<>(cfgCreateWoBusiness
        .getDetailById(id), HttpStatus.OK);
  }

  @GetMapping("/getCmbArrayIncident")
  public ResponseEntity<List<CatItemDTO>> getCmbArrayIncident() {
    return new ResponseEntity<>(cfgCreateWoBusiness
        .getCmbArrayIncident(), HttpStatus.OK);
  }

  @GetMapping("/getCmbAlarmGroup")
  public ResponseEntity<List<CatItemDTO>> getCmbAlarmGroup(Long itemId) {
    return new ResponseEntity<>(cfgCreateWoBusiness
        .getCmbAlarmGroup(itemId), HttpStatus.OK);
  }

  //  @SecurityAnnotation(role = ROLE., permission = PERMISSION.EDIT)
  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CFG_CREATE_WO)
  @PostMapping("/insertOrUpdateCfgCreateWo")
  public ResponseEntity<ResultInSideDto> insertOrUpdateCfgCreateWo(
      @Valid @RequestBody CfgCreateWoInsiteDTO cfgCreateWoDTO) {
    return new ResponseEntity<>(cfgCreateWoBusiness
        .insertOrUpdateCfgCreateWo(cfgCreateWoDTO), HttpStatus.OK);
  }

  //  @SecurityAnnotation(role = ROLE., permission = PERMISSION.DELETE)
  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CFG_CREATE_WO)
  @DeleteMapping("/deleteCfgCreateWo")
  public ResponseEntity<ResultInSideDto> deleteCfgCreateWo(Long id) {
    return new ResponseEntity<>(cfgCreateWoBusiness.deleteCfgCreateWo(id), HttpStatus.OK);
  }
}
