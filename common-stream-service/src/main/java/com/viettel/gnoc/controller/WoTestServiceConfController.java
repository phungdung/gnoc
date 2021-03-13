package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.WoTestServiceConfBussiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WoTestServiceConfDTO;
import com.viettel.gnoc.commons.utils.Constants;
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
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "WoTestServiceConf")
public class WoTestServiceConfController {

  @Autowired
  private WoTestServiceConfBussiness woTestServiceConfBussiness;

  @PostMapping("/getListWoTestServiceConf")
  public ResponseEntity<Datatable> getListWoTestServiceConf(
      @RequestBody WoTestServiceConfDTO woTestServiceConfDTO) {
    Datatable data = woTestServiceConfBussiness.getListWoTestServiceConf(woTestServiceConfDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> insert(
      @Valid @RequestBody WoTestServiceConfDTO woTestServiceConfDTO) {
    ResultInSideDto data = woTestServiceConfBussiness.insert(woTestServiceConfDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> update(
      @Valid @RequestBody WoTestServiceConfDTO woTestServiceConfDTO) {
    ResultInSideDto data = woTestServiceConfBussiness.update(woTestServiceConfDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<WoTestServiceConfDTO> getDetail(Long id) {
    WoTestServiceConfDTO data = woTestServiceConfBussiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
