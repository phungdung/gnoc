package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTOSearch;
import com.viettel.gnoc.wo.business.CfgFtOnTimeBusiness;
import com.viettel.gnoc.wo.business.CfgWoHighTempBusiness;
import com.viettel.gnoc.wo.dto.CfgFtOnTimeDTO;
import com.viettel.gnoc.wo.dto.CfgWoHighTempDTO;
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

@Slf4j
@RestController
@RequestMapping(Constants.WO_API_PATH_PREFIX + "/cfgWoHighTemp")
public class CfgWoHighTempController {

  @Autowired
  CfgWoHighTempBusiness cfgWoHighTempBusiness;

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(
      @RequestBody CfgWoHighTempDTO cfgWoHighTempDTO) {
    return new ResponseEntity<>(cfgWoHighTempBusiness.onSearch(cfgWoHighTempDTO), HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdate")
  public ResponseEntity<ResultInSideDto> insertOrUpdate(
      @RequestBody @Valid CfgWoHighTempDTO cfgWoHighTempDTO) {
    ResultInSideDto data = cfgWoHighTempBusiness.insertOrUpdate(cfgWoHighTempDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findById")
  public ResponseEntity<CfgWoHighTempDTO> findById(Long id) {
    return new ResponseEntity<>(cfgWoHighTempBusiness.findById(id), HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long id) {
    return new ResponseEntity<>(cfgWoHighTempBusiness.delete(id), HttpStatus.OK);
  }

  @GetMapping("/getPriorityWo")
  public ResponseEntity<List<DataItemDTO>> getPriorityWo() {
    return new ResponseEntity<>(cfgWoHighTempBusiness.getListPriority(), HttpStatus.OK);
  }

  @GetMapping("/getListReason")
  public ResponseEntity<List<CatReasonInSideDTOSearch>> getListReason(Long parentId) {
    return new ResponseEntity<>(cfgWoHighTempBusiness.getListReason(parentId), HttpStatus.OK);
  }

}
