package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CfgTroubleCallIpccBusiness;
import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CfgTroubleCallIpccDTO;
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

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CfgTroubleCallIpcc")
public class CfgTroubleCallIpccController {

  @Autowired
  private CfgTroubleCallIpccBusiness cfgTroubleCallIpccBusiness;

  @Autowired
  private CatItemBusiness catItemBusiness;

  //  RECEIVE_CALL_IPCC
//  LEVEL_CALL_IPCC
  @GetMapping("/getListItemByCategory")
  public ResponseEntity<List<CatItemDTO>> getListItemByCategory(String categoryCode) {
    return new ResponseEntity<>(catItemBusiness.getListItemByCategory(categoryCode, null),
        HttpStatus.OK);
  }

  @PostMapping("/getListCfgTroubleCallIpccDTO")
  public ResponseEntity<Datatable> getListCfgTroubleCallIpccDTO(
      @RequestBody CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO) {
    return new ResponseEntity<>(
        cfgTroubleCallIpccBusiness.getListCfgTroubleCallIpccDTO(cfgTroubleCallIpccDTO),
        HttpStatus.OK);
  }

  @GetMapping("/getDetailById")
  public ResponseEntity<CfgTroubleCallIpccDTO> getDetailById(Long id) {
    return new ResponseEntity<>(cfgTroubleCallIpccBusiness.getDetailById(id), HttpStatus.OK);
  }

  //  @SecurityAnnotation(role = ROLE., permission = PERMISSION.EDIT)
  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CFG_TROUBLE_CALL_IPCC)
  @PostMapping("/insertOrUpdateCfgTroubleCallIpcc")
  public ResponseEntity<ResultInSideDto> insertOrUpdateCfgTroubleCallIpcc(@Valid
  @RequestBody CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO) {
    return new ResponseEntity<>(
        cfgTroubleCallIpccBusiness.insertOrUpdateCfgTroubleCallIpcc(cfgTroubleCallIpccDTO),
        HttpStatus.OK);
  }

  //  @SecurityAnnotation(role = ROLE., permission = PERMISSION.DELETE)
  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CFG_TROUBLE_CALL_IPCC)
  @DeleteMapping("/deleteCfgTroubleCallIpcc")
  public ResponseEntity<ResultInSideDto> deleteCfgTroubleCallIpcc(Long id) {
    return new ResponseEntity<>(
        cfgTroubleCallIpccBusiness.deleteCfgTroubleCallIpcc(id),
        HttpStatus.OK);
  }
}
