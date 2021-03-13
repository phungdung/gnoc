package com.viettel.gnoc.pt.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.pt.business.ProblemWoBusiness;
import com.viettel.gnoc.pt.dto.ProblemWoDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
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

@RestController
@RequestMapping(Constants.PT_API_PATH_PREFIX + "ProblemWo")
@Slf4j
public class ProblemWoController {

  @Autowired
  ProblemWoBusiness problemWoBusiness;


  @PostMapping("/insertProblemWo")
  public ResponseEntity<ResultInSideDto> insertProblemWo(
      @Valid @RequestBody ProblemWoDTO problemWoDTO) {
    ResultInSideDto data = problemWoBusiness.insertProblemWo(problemWoDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findProblemWoById")
  public ResponseEntity<ProblemWoDTO> findProblemWoById(Long problemWoId) {
    ProblemWoDTO data = problemWoBusiness.findProblemWoById(problemWoId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteProblemWo")
  public ResponseEntity<String> deleteProblemWo(Long problemWoId) {
    String result = problemWoBusiness.deleteProblemWo(problemWoId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/updateProblemWo")
  public ResponseEntity<String> updateProblemWo(@Valid @RequestBody ProblemWoDTO problemWoDTO) {
    String data = problemWoBusiness.updateProblemWo(problemWoDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //  @PostMapping("/getListProblemWoDTO")
//  public ResponseEntity<List<ProblemWoDTO>> getListProblemWoDTO(@RequestBody ProblemWoDTO problemWoDTO,int rowStart, int maxRow, String sortType, String sortFieldList) {
//    List<ProblemWoDTO> data = problemWoBusiness.getListProblemWoDTO(problemWoDTO,rowStart,maxRow,sortType,sortFieldList);
//    return new ResponseEntity<>(data, HttpStatus.OK);
//  }
  @PostMapping("/deleteListProblemWo")
  public ResponseEntity<String> deleteListProblemWo(
      @RequestBody List<ProblemWoDTO> problemWoDTOList) {
    String result = problemWoBusiness.deleteListProblemWo(problemWoDTOList);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListProblemWo")
  public ResponseEntity<String> insertOrUpdateListProblemWo(@Valid
  @RequestBody List<ProblemWoDTO> problemWoDTOList) {
    String result = problemWoBusiness.insertOrUpdateListProblemWo(problemWoDTOList);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  //  @PostMapping("/getSequenseProblemWo")
//  public ResponseEntity<String> getSequenseProblemWo() {
//    String result = problemWoBusiness.getSequenseProblemWo();
//    return new ResponseEntity<>(result, HttpStatus.OK);
//  }
  @PostMapping("/getListProblemWoByCondition")
  public ResponseEntity<List<ProblemWoDTO>> getListProblemWoByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    List<ProblemWoDTO> data = problemWoBusiness
        .getListProblemWoByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListProblemWoDTO")
  public ResponseEntity<Datatable> getListProblemWoDTO(@RequestBody ProblemWoDTO problemWoDTO) {
    Datatable data = problemWoBusiness.getListProblemWo(problemWoDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getSequenseProblemWo")
  public ResponseEntity<String> getSeqProblemWo(String seqName) {
    return new ResponseEntity<>(problemWoBusiness.getSeqProblemWo(seqName, 1).get(0),
        HttpStatus.OK);
  }

//  @PostMapping("/getListWoDTO")
//  public ResponseEntity<Datatable> getListWoDTO(@RequestBody WoDTO woDTO) {
//    Datatable data = problemWoBusiness.getListDataWo(woDTO);
//    return new ResponseEntity<>(data, HttpStatus.OK);
//
//  }

  @PostMapping("/getListDataSearchWeb")
  public ResponseEntity<Datatable> getListDataSearchWeb(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    Datatable data = problemWoBusiness.getListDataSearchWeb(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
//  @PostMapping("/insertWoDTO")
//  public ResponseEntity<ResultInSideDto> insertWoDTO(@RequestBody WoDetail woDTO) {
//    ResultInSideDto data = WoServicesImpl.getInstance().insertWo(woDTO);
//    return new ResponseEntity<>(data, HttpStatus.OK);
//  }

}
