package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.business.SRStatusBusiness;
import com.viettel.gnoc.sr.dto.SRStatusDTO;
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
@RequestMapping(Constants.OD_API_PATH_PREFIX + "srStatus")
@Slf4j
public class SRStatusController {

  @Autowired
  protected SRStatusBusiness srStatusBusiness;

  @PostMapping("/listSRStatusDTO")
  public ResponseEntity<Datatable> getListSRStatusPage(
      @RequestBody SRStatusDTO srStatusDTO) {
    Datatable data = srStatusBusiness.getListSRStatusPage(srStatusDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> add(@Valid @RequestBody SRStatusDTO srStatusDTO) {
    ResultInSideDto data = srStatusBusiness.insert(srStatusDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> edit(@Valid @RequestBody SRStatusDTO srStatusDTO) {
    ResultInSideDto data = srStatusBusiness.update(srStatusDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailByConfigId")
  public ResponseEntity<SRStatusDTO> getDetailByConfigId(Long configId) {
    SRStatusDTO data = srStatusBusiness.getDetail(configId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
