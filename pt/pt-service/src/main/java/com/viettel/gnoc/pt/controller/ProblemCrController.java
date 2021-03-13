package com.viettel.gnoc.pt.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.pt.business.ProblemCrBusiness;
import com.viettel.gnoc.pt.dto.ProblemCrDTO;
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
@RequestMapping(Constants.PT_API_PATH_PREFIX + "ProblemCrService")
public class ProblemCrController {

  @Autowired
  private ProblemCrBusiness problemCrBusiness;

  @PostMapping("/getListProblemCrDTO")
  public ResponseEntity<List> getListProblemCrDTO(@RequestBody ProblemCrDTO ProblemCrDTO, int start,
      int maxSize, String sortType, String sortName) {
    List lst;
    try {
      lst = problemCrBusiness.getListProblemCrDTO(ProblemCrDTO,
          start, maxSize,
          sortType, sortName);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      lst = new ArrayList<>();
    }
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @GetMapping("/getSequenceProblemCr")
  public ResponseEntity<String> getSequenceProblemCr(String seqName) {
    return new ResponseEntity<>(problemCrBusiness.getSequenseProblemCr(seqName, 1).get(0),
        HttpStatus.OK);
  }

  @GetMapping("/findProblemCrById")
  public ResponseEntity<ProblemCrDTO> findProblemCrById(Long id) {
    ProblemCrDTO ProblemCrDTO = problemCrBusiness.findProblemCrById(id);
    return new ResponseEntity<>(ProblemCrDTO, HttpStatus.OK);
  }

  @PostMapping("/deleteProblemCr")
  public ResponseEntity<ResultInSideDto> deleteProblemCr(Long id) {
    String resultDTO = problemCrBusiness.deleteProblemCr(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDTO, resultDTO), HttpStatus.OK);
  }

  @PostMapping("/insertProblemCr")
  public ResponseEntity<ResultInSideDto> insertProblemCr(@RequestBody ProblemCrDTO dto) {
    ResultInSideDto resultInSideDto = problemCrBusiness.insertProblemCr(dto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateProblemCr")
  public ResponseEntity<ResultInSideDto> updateProblemCr(@RequestBody ProblemCrDTO dto) {
    String resultDto = problemCrBusiness.updateProblemCr(dto);
    return new ResponseEntity<>(
        new ResultInSideDto(Long.parseLong(dto.getProblemCrId().toString()), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/deleteListProblemCr")
  public ResponseEntity<String> deleteListProblemCr(@RequestBody List<ProblemCrDTO> dto) {
    String resultDto = problemCrBusiness.deleteListProblemCr(dto);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/getListProblemCrByCondition")
  public ResponseEntity<List<ProblemCrDTO>> getListProblemCrByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    List<ProblemCrDTO> data = problemCrBusiness
        .getListProblemCrByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListProblemCr")
  public ResponseEntity<String> insertOrUpdateListProblemCr(
      @RequestBody List<ProblemCrDTO> problemCrDTOS) {
    String result = problemCrBusiness.insertOrUpdateListProblemCr(problemCrDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/searchProblemCr")
  public ResponseEntity<Datatable> searchProblemCr(@RequestBody ProblemCrDTO problemCrDTO) {
    return new ResponseEntity<>(problemCrBusiness.searchProblemCr(problemCrDTO), HttpStatus.OK);
  }


}
