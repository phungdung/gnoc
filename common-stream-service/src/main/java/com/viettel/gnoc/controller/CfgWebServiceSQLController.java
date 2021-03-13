package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CfgWebServiceSQLBusiness;
import com.viettel.gnoc.commons.dto.CfgWebServiceSQLDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CfgWebServiceSQL")
public class CfgWebServiceSQLController {

  @Autowired
  private CfgWebServiceSQLBusiness cfgWebServiceSQLBusiness;

  //get list WoFileTemp
  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(
      @RequestBody CfgWebServiceSQLDTO dto) {
    Datatable data = cfgWebServiceSQLBusiness.onSearch(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get Detail
  @GetMapping("/getDetaiById")
  public ResponseEntity<CfgWebServiceSQLDTO> getDetaiById(Long id) {
    CfgWebServiceSQLDTO data = cfgWebServiceSQLBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/onInsert")
  public ResponseEntity<ResultInSideDto> onInsert(
      @Valid @RequestBody CfgWebServiceSQLDTO cfgWebServiceSQLDTO) {
    ResultInSideDto data = cfgWebServiceSQLBusiness.insertOrUpdate(cfgWebServiceSQLDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/onUpdate")
  public ResponseEntity<ResultInSideDto> onUpdate(
      @Valid @RequestBody CfgWebServiceSQLDTO cfgWebServiceSQLDTO) {
    ResultInSideDto data = cfgWebServiceSQLBusiness.insertOrUpdate(cfgWebServiceSQLDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CFG_CLOSED_TICKET)
  @DeleteMapping("/onDelete")
  public ResponseEntity<ResultInSideDto> onDelete(@RequestParam Long id) {
    ResultInSideDto data = cfgWebServiceSQLBusiness.deleteById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
