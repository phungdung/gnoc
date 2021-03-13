package com.viettel.gnoc.pt.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.MASTER_DATA;
import com.viettel.gnoc.commons.utils.Constants.PROBLEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.pt.business.CfgProblemTimeProcessBusiness;
import com.viettel.gnoc.pt.dto.CfgProblemTimeProcessDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.PT_API_PATH_PREFIX + "CfgProblemTimeProcess")
public class CfgProblemTimeProcessController {

  @Autowired
  private CfgProblemTimeProcessBusiness cfgProblemTimeProcessBusiness;
  @Autowired
  private CatItemBusiness catItemBusiness;


  @GetMapping("/getCfgProblemTimeProcess")
  public ResponseEntity<CfgProblemTimeProcessDTO> getCfgProblemTimeProcess(
      @RequestParam long id) {
    CfgProblemTimeProcessDTO cfgProblemTimeProcessDTOS = cfgProblemTimeProcessBusiness.findById(id);
    return new ResponseEntity<>(cfgProblemTimeProcessDTOS, HttpStatus.OK);
  }

  @PostMapping("/onInsert")
  public ResponseEntity<ResultInSideDto> onInsert(
      @Valid @RequestBody CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO) {
    ResultInSideDto resultInSideDto;
    try {
      resultInSideDto = cfgProblemTimeProcessBusiness.onInsert(cfgProblemTimeProcessDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto = new ResultInSideDto(null, RESULT.ERROR, e.getMessage());
    }
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/onDeleteList")
  public ResponseEntity<ResultInSideDto> onDeleteList(
      @RequestBody List<CfgProblemTimeProcessDTO> cfgProblemTimeProcessDTO) {
    ResultInSideDto resultInSideDto;
    try {
      resultInSideDto = cfgProblemTimeProcessBusiness.onDeleteList(cfgProblemTimeProcessDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto = new ResultInSideDto(null, RESULT.ERROR, e.getMessage());
    }
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/onUpdateList")
  public ResponseEntity<ResultInSideDto> onUpdateList(
      @Valid @RequestBody CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO) {
    ResultInSideDto resultInSideDto;
    try {
      resultInSideDto = cfgProblemTimeProcessBusiness
          .onUpdateList(cfgProblemTimeProcessDTO.getCfgProblemTimeProcessDTOS(),
              cfgProblemTimeProcessDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto = new ResultInSideDto(null, RESULT.ERROR, e.getMessage());
    }
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/getItemMaster")
  public ResponseEntity<Datatable> getItemMastergetItemMaster(@RequestParam String categoryCode) {
    String idColName = "";
    String type = "";
    switch (categoryCode) {
      case PROBLEM.PT_TYPE:
        idColName = Constants.ITEM_CODE;
        break;
      case PROBLEM.PRIORITY:
        idColName = Constants.ITEM_CODE;
        type = MASTER_DATA.OD_STATUS;
        break;
    }
    Datatable data = getDataItemMaster(categoryCode, "", type, idColName, Constants.ITEM_NAME);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getSequence")
  public ResponseEntity<String> getSequence() {
    String sequence = "";
    try {
      sequence = cfgProblemTimeProcessBusiness.getSequence();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<>(sequence, HttpStatus.OK);
  }

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(
      @RequestBody CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO) {
    Datatable datatable;
    try {
      datatable = cfgProblemTimeProcessBusiness.getListDataSearch(cfgProblemTimeProcessDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getCfgProblemTimeProcessByDTO")
  public ResponseEntity<CfgProblemTimeProcessDTO> getCfgProblemTimeProcessByDTO(
      @RequestBody CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO) {
    CfgProblemTimeProcessDTO cfg = cfgProblemTimeProcessBusiness
        .getCfgProblemTimeProcessByDTO(cfgProblemTimeProcessDTO);
    //add Offday cho combobox pt
    //this dateTime is field of PT
    return new ResponseEntity<>(cfg, HttpStatus.OK);
  }

  private Datatable getDataItemMaster(String categoryCode, String system,
      String type, String idColName, String nameCol) {
    return catItemBusiness.getItemMaster(categoryCode, system, type, idColName, nameCol);
  }

}
