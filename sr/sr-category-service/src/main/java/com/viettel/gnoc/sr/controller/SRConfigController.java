package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.business.SRConfigBusiness;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
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
@RequestMapping(Constants.OD_API_PATH_PREFIX + "srConfig")
@Slf4j
public class SRConfigController {

  @Autowired
  protected SRConfigBusiness srConfigBusiness;

  @PostMapping("/listSRConfigDTO")
  public ResponseEntity<Datatable> getListSRConfigPage(
      @RequestBody SRConfigDTO srConfigDTO) {
    Datatable data = srConfigBusiness.getListSRConfigPage(srConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> add(@Valid @RequestBody SRConfigDTO srConfigDTO) {
    ResultInSideDto data = srConfigBusiness.insert(srConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<SRConfigDTO> getDetailByRoleId(Long configId) {
    SRConfigDTO data = srConfigBusiness.getDetail(configId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> edit(@Valid @RequestBody SRConfigDTO srConfigDTO) {
    ResultInSideDto data = srConfigBusiness.update(srConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long configId) {
    ResultInSideDto data = srConfigBusiness.delete(configId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
