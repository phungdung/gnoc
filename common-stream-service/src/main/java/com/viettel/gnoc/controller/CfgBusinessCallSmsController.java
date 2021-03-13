package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.CfgBusinessCallSmsBusiness;
import com.viettel.gnoc.commons.business.LogChangeConfigBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CfgBusinessCallSmsDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
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
import viettel.passport.client.UserToken;

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CfgBusinessCallSmsService")
public class CfgBusinessCallSmsController {

  @Autowired
  private CfgBusinessCallSmsBusiness cfgBusinessCallSmsBusiness;

  @Autowired
  private LogChangeConfigBusiness logChangeConfigBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @PostMapping("/getListCfgBusinessCallSmsBusiness")
  public ResponseEntity<Datatable> getListCfgBusinessCallSmsBusiness(
      @RequestBody CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) {
    Datatable data = cfgBusinessCallSmsBusiness.getListCfgBusinessCallSmsDTO(cfgBusinessCallSmsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListWoCdGroupCBB")
  public ResponseEntity<List<WoCdGroupInsideDTO>> getListWoCdGroupCBB() {
    List<WoCdGroupInsideDTO> data = cfgBusinessCallSmsBusiness
        .getListWoCdGroupCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> insert(@Valid
  @RequestBody CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) {
    ResultInSideDto data = cfgBusinessCallSmsBusiness.insert(cfgBusinessCallSmsDTO);
    UserToken userToken = ticketProvider.getUserToken();
    if (data.getKey().equals(RESULT.SUCCESS)) {
      logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Insert CfgBusinessCallSms", "Insert , id: " + data.getId(),
          null, null));
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> update(@Valid
  @RequestBody CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) {
    ResultInSideDto data = cfgBusinessCallSmsBusiness.update(cfgBusinessCallSmsDTO);
    UserToken userToken = ticketProvider.getUserToken();
    if (data.getKey().equals(RESULT.SUCCESS)) {
      logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update CfgBusinessCallSms", "Update , id: " + cfgBusinessCallSmsDTO.getId(),
          null, null));
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<CfgBusinessCallSmsDTO> getDetail(Long id) {
    CfgBusinessCallSmsDTO data = cfgBusinessCallSmsBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long id) {
    ResultInSideDto data = cfgBusinessCallSmsBusiness.delete(id);
    UserToken userToken = ticketProvider.getUserToken();
    if (data.getKey().equals(RESULT.SUCCESS)) {
      logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete CfgBusinessCallSms", "Delete , id: " + data.getId(),
          null, null));
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
