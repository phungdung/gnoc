package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import com.viettel.gnoc.mr.business.MrCfgProcedureTelHardBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TrungDuong
 */
@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrCfgProcedureTelHardService")
public class MrCfgProcedureTelHardController {

  @Autowired
  MrCfgProcedureTelHardBusiness mrCfgProcedureTelHardBusiness;

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(
      @RequestBody MrCfgProcedureTelDTO mrCfgProcedureTelDTO) {
    Datatable datatable = mrCfgProcedureTelHardBusiness.onSearch(mrCfgProcedureTelDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<MrCfgProcedureTelDTO> getDetail(Long procedureId) {
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = mrCfgProcedureTelHardBusiness
        .getDetail(procedureId);
    return new ResponseEntity<>(mrCfgProcedureTelDTO, HttpStatus.OK);
  }

  @PostMapping("/insertMrCfgProcedureTelHardDTO")
  public ResponseEntity<ResultInSideDto> insert(
      @RequestBody MrCfgProcedureTelDTO mrCfgProcedureTelDTO) {
    ResultInSideDto resultInSideDto = mrCfgProcedureTelHardBusiness.insert(mrCfgProcedureTelDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateMrCfgProcedureTelHardDTO")
  public ResponseEntity<ResultInSideDto> update(
      @RequestBody MrCfgProcedureTelDTO mrCfgProcedureTelDTO) {
    ResultInSideDto resultInSideDto = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/deleteMrCfgProcedureTelHardDTO")
  public ResponseEntity<ResultInSideDto> delete(Long procedureId) {
    ResultInSideDto data = mrCfgProcedureTelHardBusiness.delete(procedureId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


}
