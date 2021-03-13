package com.viettel.gnoc.kedb.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.kedb.business.KedbActionLogsBusiness;
import com.viettel.gnoc.kedb.dto.KedbActionLogsDTO;
import java.util.ArrayList;
import java.util.List;
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
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.PT_API_PATH_PREFIX + "KedbActionLogsService")
public class KedbActionLogsController {

  @Autowired
  private KedbActionLogsBusiness kedbActionLogsBusiness;

  @PostMapping("/getListKedbActionLogsDTO")
  public ResponseEntity<List> getListKedbActionLogsDTO(
      @RequestBody KedbActionLogsDTO kedbActionLogsDTO) {
    List lst;
    try {
      lst = kedbActionLogsBusiness.getListKedbActionLogsDTO(kedbActionLogsDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      lst = new ArrayList<>();
    }
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @GetMapping("/getSequenceKdbActionLog")
  public ResponseEntity<String> getSequenceKdbActionLog(String seqName) {
    return new ResponseEntity<>(kedbActionLogsBusiness.getSequenseKedbActionLogs(seqName, 1).get(0),
        HttpStatus.OK);
  }

  @GetMapping("/findKedbActionLogsById")
  public ResponseEntity<KedbActionLogsDTO> findKedbActionLogsById(Long id) {
    KedbActionLogsDTO kedbActionLogsDTO = kedbActionLogsBusiness.findKedbActionLogsById(id);
    return new ResponseEntity<>(kedbActionLogsDTO, HttpStatus.OK);
  }

  @PostMapping("/deleteKedbActionLogs")
  public ResponseEntity<ResultInSideDto> deleteKedbActionLogs(Long id) {
    String resultDTO = kedbActionLogsBusiness.deleteKedbActionLogs(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDTO, resultDTO), HttpStatus.OK);
  }

  @PostMapping("/insertKedbActionLogs")
  public ResponseEntity<ResultInSideDto> insertKedbActionLogs(@RequestBody KedbActionLogsDTO dto) {
    ResultInSideDto resultInSideDto = kedbActionLogsBusiness.insertKedbActionLogs(dto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateKedbActionLogs")
  public ResponseEntity<ResultInSideDto> updateKedbActionLogs(@RequestBody KedbActionLogsDTO dto) {
    String resultDto = kedbActionLogsBusiness.updateKedbActionLogs(dto);
    return new ResponseEntity<>(new ResultInSideDto(dto.getId(), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/deleteListKedbActionLogs")
  public ResponseEntity<String> deleteListKedbActionLogs(@RequestBody List<KedbActionLogsDTO> dto) {
    String resultDto = kedbActionLogsBusiness.deleteListKedbActionLogs(dto);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/getListKedbActionLogsByCondition")
  public ResponseEntity<List<KedbActionLogsDTO>> getListKedbActionLogsByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    List<KedbActionLogsDTO> data = kedbActionLogsBusiness
        .getListKedbActionLogsByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListKedbActionLogs")
  public ResponseEntity<String> insertOrUpdateListKedbActionLogs(
      @RequestBody List<KedbActionLogsDTO> problemCrDTOS) {
    String result = kedbActionLogsBusiness.insertOrUpdateListKedbActionLogs(problemCrDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/searchKedbActionLogs")
  public ResponseEntity<Datatable> searchProblemActionLogs(
      @RequestBody KedbActionLogsDTO kedbActionLogsDTO) {
    Datatable datatable = kedbActionLogsBusiness.onSearchKedbActionLogs(kedbActionLogsDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

}
