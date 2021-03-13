package com.viettel.gnoc.pt.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.pt.business.ProblemNodeBusiness;
import com.viettel.gnoc.pt.dto.ProblemNodeDTO;
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
@RequestMapping(Constants.PT_API_PATH_PREFIX + "ProblemNodeService")
public class ProblemNodeController {

  @Autowired
  private ProblemNodeBusiness problemNodeBusiness;

  @PostMapping("/getListProblemNodeDTO")
  public ResponseEntity<List> getListProblemNodeDTO(@RequestBody ProblemNodeDTO ProblemNodeDTO,
      int start, int maxSize, String sortType, String sortName) {
    List lst;
    try {
      lst = problemNodeBusiness.getListProblemNodeDTO(ProblemNodeDTO,
          start, maxSize,
          sortType, sortName);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      lst = new ArrayList<>();
    }
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @GetMapping("/getSequenceProblemNode")
  public ResponseEntity<String> getSequenceProblemNode(String seqName) {
    return new ResponseEntity<>(problemNodeBusiness.getSequenseProblemNode(seqName, 1).get(0),
        HttpStatus.OK);
  }

  @GetMapping("/findProblemNodeById")
  public ResponseEntity<ProblemNodeDTO> findProblemNodeById(Long id) {
    ProblemNodeDTO ProblemNodeDTO = problemNodeBusiness.findProblemNodeById(id);
    return new ResponseEntity<>(ProblemNodeDTO, HttpStatus.OK);
  }

  @PostMapping("/deleteProblemNode")
  public ResponseEntity<ResultInSideDto> deleteProblemNode(Long id) {
    String resultDTO = problemNodeBusiness.deleteProblemNode(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDTO, resultDTO), HttpStatus.OK);
  }

  @PostMapping("/insertProblemNode")
  public ResponseEntity<ResultInSideDto> insertProblemNode(@RequestBody ProblemNodeDTO dto) {
    ResultInSideDto resultInSideDto = problemNodeBusiness.insertProblemNode(dto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateProblemNode")
  public ResponseEntity<ResultInSideDto> updateProblemNode(@RequestBody ProblemNodeDTO dto) {
    String resultDto = problemNodeBusiness.updateProblemNode(dto);
    return new ResponseEntity<>(
        new ResultInSideDto((dto.getProblemNodeId()), resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/deleteListProblemNode")
  public ResponseEntity<String> deleteListProblemNode(@RequestBody List<ProblemNodeDTO> dto) {
    String resultDto = problemNodeBusiness.deleteListProblemNode(dto);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/getListProblemNodeByCondition")
  public ResponseEntity<List<ProblemNodeDTO>> getListProblemNodeByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    List<ProblemNodeDTO> data = problemNodeBusiness
        .getListProblemNodeByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListProblemNode")
  public ResponseEntity<String> insertOrUpdateListProblemNode(
      @RequestBody List<ProblemNodeDTO> ProblemNodeDTOS) {
    String result = problemNodeBusiness.insertOrUpdateListProblemNode(ProblemNodeDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/searchProblemNode")
  public ResponseEntity<Datatable> searchProblemNode(@RequestBody ProblemNodeDTO dto) {
    try {
      Datatable datatable = problemNodeBusiness.onSearchProblemNodeDTO(dto);
      return new ResponseEntity<>(datatable, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<>(new Datatable(), HttpStatus.OK);
  }

}
