package com.viettel.gnoc.pt.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.pt.business.ProblemActionLogsBusiness;
import com.viettel.gnoc.pt.dto.ProblemActionLogsDTO;
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
@RequestMapping(Constants.PT_API_PATH_PREFIX + "ProblemActionLogsService")
public class ProblemActionLogsController {

  @Autowired
  private ProblemActionLogsBusiness problemActionLogsBusiness;

  @PostMapping("/getListProblemActionLogsDTO")
  public ResponseEntity<List> getListProblemActionLogsDTO(
      @RequestBody ProblemActionLogsDTO ProblemActionLogsDTO, int start, int maxSize,
      String sortType, String sortName) {
    List lst;
    try {
      lst = problemActionLogsBusiness.getListProblemActionLogsDTO(ProblemActionLogsDTO,
          start, maxSize,
          sortType, sortName);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      lst = new ArrayList<>();
    }
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @GetMapping("/getSequenceProblemActionLog")
  public ResponseEntity<String> getSequenceProblemActionLog(String seqName) {
    return new ResponseEntity<>(
        problemActionLogsBusiness.getSequenseProblemActionLogs(seqName, 1).get(0), HttpStatus.OK);
  }

  @GetMapping("/findProblemActionLogsById")
  public ResponseEntity<ProblemActionLogsDTO> findProblemActionLogsById(Long id) {
    ProblemActionLogsDTO ProblemActionLogsDTO = problemActionLogsBusiness
        .findProblemActionLogsById(id);
    return new ResponseEntity<>(ProblemActionLogsDTO, HttpStatus.OK);
  }

  @PostMapping("/deleteProblemActionLogs")
  public ResponseEntity<ResultInSideDto> deleteProblemActionLogs(Long id) {
    String resultDTO = problemActionLogsBusiness.deleteProblemActionLogs(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDTO, resultDTO), HttpStatus.OK);
  }

  @PostMapping("/insertProblemActionLogs")
  public ResponseEntity<ResultInSideDto> insertProblemActionLogs(
      @RequestBody ProblemActionLogsDTO dto) {
    ResultInSideDto resultInSideDto = problemActionLogsBusiness.insertProblemActionLogs(dto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateProblemActionLogs")
  public ResponseEntity<ResultInSideDto> updateProblemActionLogs(
      @RequestBody ProblemActionLogsDTO dto) {
    String resultDto = problemActionLogsBusiness.updateProblemActionLogs(dto);
    return new ResponseEntity<>(
        new ResultInSideDto(dto.getProblemActionLogsId(), resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/deleteListProblemActionLogs")
  public ResponseEntity<String> deleteListProblemActionLogs(
      @RequestBody List<ProblemActionLogsDTO> dto) {
    String resultDto = problemActionLogsBusiness.deleteListProblemActionLogs(dto);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/getListProblemActionLogsByCondition")
  public ResponseEntity<List<ProblemActionLogsDTO>> getListProblemActionLogsByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    List<ProblemActionLogsDTO> data = problemActionLogsBusiness
        .getListProblemActionLogsByCondition(lstCondition, rowStart, maxRow, sortType,
            sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListProblemActionLogs")
  public ResponseEntity<String> insertOrUpdateListProblemActionLogs(
      @RequestBody List<ProblemActionLogsDTO> problemActionLogsDTOS) {
    String result = problemActionLogsBusiness
        .insertOrUpdateListProblemActionLogs(problemActionLogsDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/searchProblemActionLogs")
  public ResponseEntity<Datatable> searchProblemActionLogs(
      @RequestBody ProblemActionLogsDTO problemActionLogsDTO) {
    try {
      Datatable datatable = problemActionLogsBusiness
          .onSearchProblemActionLogsDTO(problemActionLogsDTO);
      return new ResponseEntity<>(datatable, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<>(new Datatable(), HttpStatus.OK);
  }
}
