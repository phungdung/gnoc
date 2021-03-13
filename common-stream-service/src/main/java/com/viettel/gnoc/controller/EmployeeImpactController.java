package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.EmployeeImpactBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.EmployeeImpactDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "EmployeeImpactController")
public class EmployeeImpactController {

  @Autowired
  private EmployeeImpactBusiness employeeImpactBusiness;

  // insert employee
  @PostMapping("/insertEmpImpact")
  public ResponseEntity<ResultInSideDto> insertEmpImpact(
      @RequestBody EmployeeImpactDTO employeeImpactDTO) {
    ResultInSideDto resultDto = employeeImpactBusiness
        .insertEmpImpact(employeeImpactDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  // get combobox mảng cha
  @PostMapping("/getListParentArray")
  public ResponseEntity<List<ItemDataCRDTO>> getListParentArray() {
    List<ItemDataCRDTO> data = employeeImpactBusiness
        .getListParentArray();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get combobox mảng con
  @PostMapping("/getListChildArray")
  public ResponseEntity<List<CfgChildArrayDTO>> getListChildArray(
      @RequestBody CfgChildArrayDTO cfgChildArrayDTO) {
    List<CfgChildArrayDTO> data = employeeImpactBusiness
        .getListChildArray(cfgChildArrayDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get combobox level
  @PostMapping("/getListLevel")
  public ResponseEntity<Datatable> getListLevel(@RequestBody CatItemDTO catItemDTO) {
    Datatable data = employeeImpactBusiness
        .getListLevel(catItemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // getListEmployeeImpact
  @PostMapping("/getListEmployeeImpact")
  public ResponseEntity<Datatable> getListEmployeeImpact(
      @RequestBody EmployeeImpactDTO employeeImpactDTO) {
    Datatable data = employeeImpactBusiness.getListEmployeeImpact(employeeImpactDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // deleteEmpImpact
  @PostMapping("/deleteEmpImpact")
  public ResponseEntity<ResultInSideDto> deleteEmpImpact(Long id) {
    String resultDto = employeeImpactBusiness.deleteEmpImpact(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  // updateEmpImpact
  @PostMapping("/updateEmpImpact")
  public ResponseEntity<ResultInSideDto> updateEmpImpact(
      @RequestBody EmployeeImpactDTO dto) {
    ResultInSideDto resultDto = employeeImpactBusiness.updateEmpImpact(dto);
    return new ResponseEntity<>(resultDto,
        HttpStatus.OK);
  }

  //get detail
  @PostMapping("/findEmpImpactById")
  public ResponseEntity<EmployeeImpactDTO> findEmpImpactById(Long id) {
    EmployeeImpactDTO employeeImpactDTO = employeeImpactBusiness
        .findEmpImpactById(id);
    return new ResponseEntity<>(employeeImpactDTO, HttpStatus.OK);
  }
}
