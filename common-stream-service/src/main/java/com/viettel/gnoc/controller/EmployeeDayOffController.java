package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.EmployeeDayOffBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.EmployeeDayOffDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "EmployeeDayOffController")
public class EmployeeDayOffController {

  @Autowired
  private EmployeeDayOffBusiness employeeDayOffBusiness;

  // getListEmployeeDayOff
  @PostMapping("/getListEmployeeDayOff")
  public ResponseEntity<Datatable> getListEmployeeDayOff(
      @RequestBody EmployeeDayOffDTO employeeDayOffDTO) {
    Datatable data = employeeDayOffBusiness.getListEmployeeDayOff(employeeDayOffDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //get detail
  @PostMapping("/findEmployeeDayOffById")
  public ResponseEntity<EmployeeDayOffDTO> findEmployeeDayOffById(Long id) {
    EmployeeDayOffDTO employeeDayOffDTO = employeeDayOffBusiness
        .findEmployeeDayOffById(id);
    return new ResponseEntity<>(employeeDayOffDTO, HttpStatus.OK);
  }

  //delete EmployeeDayOffById
  @PostMapping("/deleteEmployeeDayOffById")
  public ResponseEntity<ResultInSideDto> deleteEmployeeDayOffById(@RequestParam Long id) {
    String resultDto = employeeDayOffBusiness.deleteEmployeeDayOffById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  // insertEmployeeDayOff
  @PostMapping("/insertEmployeeDayOff")
  public ResponseEntity<ResultInSideDto> insertEmployeeDayOff(
      @RequestBody List<EmployeeDayOffDTO> employeeDayOffDTOS) {
    ResultInSideDto result = employeeDayOffBusiness
        .insertEmployeeDayOff(employeeDayOffDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  //update EmployeeDayOff
  @PostMapping("/updateEmployeeDayOff")
  public ResponseEntity<ResultInSideDto> updateEmployeeDayOff(
      @RequestBody EmployeeDayOffDTO dto) {
    ResultInSideDto result = employeeDayOffBusiness.updateEmployeeDayOff(dto);
    return new ResponseEntity<>(result,
        HttpStatus.OK);
  }


}
