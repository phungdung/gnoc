package com.viettel.gnoc.pt.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.pt.business.ProblemWorklogBusiness;
import com.viettel.gnoc.pt.dto.ProblemWorklogDTO;
import java.util.Date;
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


@Slf4j
@RestController
@RequestMapping(Constants.PT_API_PATH_PREFIX + "ProblemWorklog")
public class ProblemWorklogController {

  @Autowired
  private ProblemWorklogBusiness problemWorklogBusiness;

  @PostMapping("/insertProblemWorklog")
  public ResponseEntity<ResultInSideDto> insertProblemWorklog(@Valid
  @RequestBody ProblemWorklogDTO problemWorklogDTO) throws Exception {
    ResultInSideDto resultInSideDto;
    //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();
    problemWorklogDTO.setCreatedTime(date);
    resultInSideDto = problemWorklogBusiness.onInsert(problemWorklogDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateProblemWorklog")
  public ResponseEntity<String> updateProblemWorklog(@Valid
  @RequestBody ProblemWorklogDTO problemWorklogDTO) throws Exception {
    String result = problemWorklogBusiness.updateProblemWorklog(problemWorklogDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/deleteProblemWorklog")
  public ResponseEntity<String> deleteProblemWorklog(Long problemWorklogId) {
    return new ResponseEntity<>(problemWorklogBusiness.deleteProblemWorklog(problemWorklogId),
        HttpStatus.OK);
  }

  @PostMapping("/deleteLisProblemWorklog")
  public ResponseEntity<String> deleteListProblemWorklog(
      @RequestBody List<ProblemWorklogDTO> problemWorklogDTOS) {
    String result = problemWorklogBusiness.deleteListProblemWorklog(problemWorklogDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListProblemWorklogDTO")
  public ResponseEntity<Datatable> getListProblemWorklogDTO(
      @RequestBody ProblemWorklogDTO problemWorklogDTO) {
    Datatable data = problemWorklogBusiness.getListProblemWorklogDTO(problemWorklogDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findProblemWorklogById")
  public ResponseEntity<ProblemWorklogDTO> findProblemWorklogById(Long problemWorklogId)
      throws Exception {
    ProblemWorklogDTO problemWorklogDTO = problemWorklogBusiness
        .findProblemWorklogById(problemWorklogId);
    return new ResponseEntity<>(problemWorklogDTO, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListProblemWorklog")
  public ResponseEntity<String> insertOrUpdateListProblemWorklog(
      @RequestBody List<ProblemWorklogDTO> problemWorklogDTOS) throws Exception {
    String result = problemWorklogBusiness.insertOrUpdateListProblemWorklog(problemWorklogDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getSequenseProblemWorklog")
  public ResponseEntity<String> getSequenseProblemWorklog(String seqName) {
    return new ResponseEntity<>(problemWorklogBusiness.getSequenseProblemWorklog(seqName, 1).get(0),
        HttpStatus.OK);
  }

  @PostMapping("/getListProblemWorklogByCondition")
  public ResponseEntity<List> getListProblemWorklogByCondition(List<ConditionBean> lstConditionBean,
      int start, int maxResult, String sortType, String sortField) {
    List data = problemWorklogBusiness
        .getListProblemWorklogByCondition(lstConditionBean, start, maxResult, sortType, sortField);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}


