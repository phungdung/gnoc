package com.viettel.gnoc.pt.controller;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.pt.business.ProblemConfigTimeBusiness;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
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

@Slf4j
@RestController
@RequestMapping(Constants.PT_API_PATH_PREFIX + "ProblemConfigTimeService")
public class ProblemConfigTimeController {
  @Autowired
  private ProblemConfigTimeBusiness problemConfigTimeBusiness;

  @GetMapping("/getCombobox")
  public ResponseEntity<List<CatItemDTO>> getListComboboxGroupReasonOrSolution(String codeCatagory) {
    List<CatItemDTO> catItemDTOList = problemConfigTimeBusiness.getListComboboxGroupReasonOrSolution(codeCatagory);
    return new ResponseEntity<>(catItemDTOList, HttpStatus.OK);
  }

  @PostMapping("/searchProblemConfigTime")
  public ResponseEntity<Datatable> onSearchProbleConfigTime(@RequestBody ProblemConfigTimeDTO problemConfigTimeDTO) {
    return new ResponseEntity<>(problemConfigTimeBusiness.onSearchProbleConfigTime(problemConfigTimeDTO), HttpStatus.OK);
  }

  @PostMapping("/insertProblemConfigTime")
  public ResponseEntity<ResultInSideDto> insertProblemConfigTime(
      @RequestBody ProblemConfigTimeDTO problemConfigTimeDTO) {
    ResultInSideDto resultInSideDto = problemConfigTimeBusiness.insertProblemConfigTime(problemConfigTimeDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateProblemConfigTime")
  public ResponseEntity<ResultInSideDto> updateProblemConfigTime(
      @RequestBody ProblemConfigTimeDTO problemConfigTimeDTO) {
    ResultInSideDto resultInSideDto = problemConfigTimeBusiness.updateProblemConfigTime(problemConfigTimeDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/findProblemConfigTimeById")
  public ResponseEntity<ProblemConfigTimeDTO> findProblemConfigTimeById(Long id) {
    ProblemConfigTimeDTO problemConfigTimeDTO = problemConfigTimeBusiness.findProblemConfigTimeById(id);
    return new ResponseEntity<>(problemConfigTimeDTO, HttpStatus.OK);
  }

  @PostMapping("/deleteProblemConfigTime")
  public ResponseEntity<ResultInSideDto> deleteProblemNode(Long id) {
    String resultDTO = problemConfigTimeBusiness.deleteProblemConfigTime(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDTO, resultDTO), HttpStatus.OK);
  }
}
