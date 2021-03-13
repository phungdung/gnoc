package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureBtsDTO;
import com.viettel.gnoc.mr.business.MrCfgProcedureBtsBusiness;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrCfgProcedureBtsService")
public class MrCfgProcedureBtsController {

  @Autowired
  MrCfgProcedureBtsBusiness mrCfgProcedureBtsBusiness;

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(
      @RequestBody MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO) {
    Datatable datatable = mrCfgProcedureBtsBusiness.onSearch(mrCfgProcedureBtsDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<MrCfgProcedureBtsDTO> getDetail(Long id) {
    MrCfgProcedureBtsDTO data = mrCfgProcedureBtsBusiness.findById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @DeleteMapping("/deleteById")
  public ResponseEntity<ResultInSideDto> deleteById(Long id) {
    ResultInSideDto data = mrCfgProcedureBtsBusiness.deleteById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/onUpdate")
  public ResponseEntity<ResultInSideDto> onUpdate(@Valid @RequestBody MrCfgProcedureBtsDTO dto) {
    ResultInSideDto result = mrCfgProcedureBtsBusiness.insertOrUpdateMrCfgProcedureBts(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/onInsert")
  public ResponseEntity<ResultInSideDto> onInsert(@Valid @RequestBody MrCfgProcedureBtsDTO dto) {
    ResultInSideDto result = mrCfgProcedureBtsBusiness.insertOrUpdateMrCfgProcedureBts(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
