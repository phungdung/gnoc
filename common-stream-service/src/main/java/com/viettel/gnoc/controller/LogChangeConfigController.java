package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.LogChangeConfigBusiness;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "LogChangeConfig")
public class LogChangeConfigController {

  @Autowired
  private LogChangeConfigBusiness logChangeConfigBusiness;

  @PostMapping("/insertLog")
  public ResponseEntity<ResultInSideDto> insertLog(
      @RequestBody LogChangeConfigDTO logChangeConfigDTO) {
    ResultInSideDto result = logChangeConfigBusiness.insertLog(logChangeConfigDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
