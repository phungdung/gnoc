package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CfgWhiteListIpBusiness;
import com.viettel.gnoc.commons.dto.CfgWhiteListIpDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CfgWhiteListIpService")
public class CfgWhiteListIpController {

  @Autowired
  private CfgWhiteListIpBusiness cfgWhiteListIpBusiness;

  @PostMapping("/getListDataSearchWeb")
  public ResponseEntity<Datatable> getListDataSearchWeb(@RequestBody CfgWhiteListIpDTO dto) {
    Datatable data = cfgWhiteListIpBusiness.getListDataSearchWeb(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/onInsert")
  public ResponseEntity<ResultInSideDto> onInsert(@RequestBody CfgWhiteListIpDTO dto) {
    ResultInSideDto result = cfgWhiteListIpBusiness.add(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getDetailById")
  public ResponseEntity<CfgWhiteListIpDTO> getDetailById(Long id) {
    CfgWhiteListIpDTO cfgWhiteListIpDTO = cfgWhiteListIpBusiness.getDetailById(id);
    return new ResponseEntity<>(cfgWhiteListIpDTO, HttpStatus.OK);
  }

  @PostMapping("/onUpdate")
  public ResponseEntity<ResultInSideDto> onUpdate(@RequestBody CfgWhiteListIpDTO dto) {
    ResultInSideDto result = cfgWhiteListIpBusiness.edit(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(@RequestParam Long id) {
    ResultInSideDto result = cfgWhiteListIpBusiness.delete(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/checkIpSystemName")
  public ResponseEntity<CfgWhiteListIpDTO> checkIpSystemName(@RequestParam CfgWhiteListIpDTO dto) {
    CfgWhiteListIpDTO result = cfgWhiteListIpBusiness
        .checkIpSystemName(dto.getUserName());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
