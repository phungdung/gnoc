package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CrAffectedLevelBusiness;
import com.viettel.gnoc.cr.dto.CrAffectedLevelDTO;
import java.io.File;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DungPV
 */
@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "CrAffectedLevel")
@Slf4j
public class CrAffectedLevelController {

  @Autowired
  protected CrAffectedLevelBusiness crAffectedLevelBusiness;

  @PostMapping("/getListCrAffectedLevel")
  public ResponseEntity<Datatable> getListCrAffectedLevel(@RequestBody
      CrAffectedLevelDTO crAffectedLevelDTO) {
    Datatable data = crAffectedLevelBusiness.getListCrAffectedLevel(crAffectedLevelDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<CrAffectedLevelDTO> getDetail(Long affectedLevelId) {
    return new ResponseEntity<>(
        crAffectedLevelBusiness.getDetail(affectedLevelId), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.AFFECTED_LEVEL)
  @PostMapping("/addCrAffectedLevel")
  public ResponseEntity<ResultInSideDto> addCrAffectedLevel(@Valid @RequestBody
      CrAffectedLevelDTO crAffectedLevelDTO) {
    return new ResponseEntity<>(
        crAffectedLevelBusiness.addCrAffectedLevel(crAffectedLevelDTO), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.AFFECTED_LEVEL)
  @PutMapping("/updateCrAffectedLevel")
  public ResponseEntity<ResultInSideDto> updateCrAffectedLevel(@Valid @RequestBody
      CrAffectedLevelDTO crAffectedLevelDTO) {
    return new ResponseEntity<>(
        crAffectedLevelBusiness.updateCrAffectedLevel(crAffectedLevelDTO), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.AFFECTED_LEVEL)
  @DeleteMapping("/deleteCrAffectedLevel")
  public ResponseEntity<ResultInSideDto> deleteCrAffectedLevel(Long affectedLevelId) {
    return new ResponseEntity<>(
        crAffectedLevelBusiness.deleteCrAffectedLevel(affectedLevelId), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.AFFECTED_LEVEL)
  @DeleteMapping("/deleteListCrAffectedLevel")
  public ResponseEntity<ResultInSideDto> deleteListCrAffectedLevel(@RequestBody
      CrAffectedLevelDTO crAffectedLevelDTO) {
    return new ResponseEntity<>(
        crAffectedLevelBusiness.deleteListCrAffectedLevel(crAffectedLevelDTO), HttpStatus.OK);
  }

  @PostMapping("/exportData")
  public ResponseEntity<File> exportData(@RequestBody CrAffectedLevelDTO crAffectedLevelDTO)
      throws Exception {
    return new ResponseEntity<>(
        crAffectedLevelBusiness.exportData(crAffectedLevelDTO), HttpStatus.OK);
  }
}
