package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.CfgRequestScheduleBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.RequestScheduleDTO;
import com.viettel.gnoc.cr.dto.ScheduleCRDTO;
import com.viettel.gnoc.cr.dto.ScheduleEmployeeDTO;
import java.util.List;
import javax.validation.Valid;
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
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CfgRequestScheduleController")
public class CfgRequestScheduleController {

  @Autowired
  CfgRequestScheduleBusiness cfgRequestScheduleBusiness;

  // insertRequestSchedule
  @PostMapping("/insertRequestSchedule")
  public ResponseEntity<ResultInSideDto> insertRequestSchedule(
      @RequestBody @Valid RequestScheduleDTO requestScheduleDTO) {
    ResultInSideDto resultDto = cfgRequestScheduleBusiness
        .insertRequestSchedule(requestScheduleDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  // deleteRequestSchedule
  @PostMapping("/deleteRequestSchedule")
  public ResponseEntity<ResultInSideDto> deleteRequestSchedule(Long id) {
    String resultDto = cfgRequestScheduleBusiness.deleteRequestSchedule(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  // get list year
  @PostMapping("/getListYear")
  public ResponseEntity<Datatable> getListYear(@RequestBody CatItemDTO catItemDTO) {
    Datatable data = cfgRequestScheduleBusiness
        .getListYear(catItemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // getListRequestSchedule
  @PostMapping("/getListRequestSchedule")
  public ResponseEntity<Datatable> getListRequestSchedule(
      @RequestBody RequestScheduleDTO requestScheduleDTO) {
    Datatable data = cfgRequestScheduleBusiness.getListRequestSchedule(requestScheduleDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // updateRequestSchedule
  @PostMapping("/updateRequestSchedule")
  public ResponseEntity<ResultInSideDto> updateRequestSchedule(
      @RequestBody @Valid RequestScheduleDTO dto) {
    String resultDto = cfgRequestScheduleBusiness.updateRequestSchedule(dto);
    return new ResponseEntity<>(new ResultInSideDto(dto.getIdSchedule(), resultDto, resultDto),
        HttpStatus.OK);
  }

  //findRequestScheduleById
  @PostMapping("/findRequestScheduleById")
  public ResponseEntity<RequestScheduleDTO> findRequestScheduleById(Long id) {
    RequestScheduleDTO requestScheduleDTO = cfgRequestScheduleBusiness
        .findRequestScheduleById(id);
    return new ResponseEntity<>(requestScheduleDTO, HttpStatus.OK);
  }

  //getListUnit
  @PostMapping("/getListUnit")
  public ResponseEntity<Datatable> getListUnit(@RequestBody UnitDTO unitDTO) {
    Datatable data = cfgRequestScheduleBusiness
        .getListUnit(unitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //chi tiết sắp lịch
  @PostMapping("/getListEmployee")
  public ResponseEntity<List<ScheduleEmployeeDTO>> getListEmployee(
      @RequestBody ScheduleEmployeeDTO scheduleEmployeeDTO) {
    List<ScheduleEmployeeDTO> data = cfgRequestScheduleBusiness
        .getListEmployee(scheduleEmployeeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // CR before
  @PostMapping("/searchCR")
  public ResponseEntity<List<ScheduleCRDTO>> searchCR(@RequestBody ScheduleCRDTO scheduleCRDTO) {
    List<ScheduleCRDTO> data = cfgRequestScheduleBusiness.onSearchCR(scheduleCRDTO, 0L);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // CR After
  @PostMapping("/searchCRAfter")
  public ResponseEntity<List<ScheduleCRDTO>> searchCRAfter(
      @RequestBody ScheduleCRDTO scheduleCRDTO) {
    List<ScheduleCRDTO> data = cfgRequestScheduleBusiness.onSearchCR(scheduleCRDTO, 1l);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


  //Danh sách CR không sắp được lịch
  @PostMapping("/searchCRAfterFail")
  public ResponseEntity<List<ScheduleCRDTO>> searchCRAfter2Fail(
      @RequestBody RequestScheduleDTO requestScheduleDTO) {
    List<ScheduleCRDTO> lstDataCR = cfgRequestScheduleBusiness
        .searchCRAfterFail(requestScheduleDTO);
    return new ResponseEntity<>(lstDataCR, HttpStatus.OK);
  }

  // Ghi lại
  // OnSave
  @PostMapping("/onSave")
  public ResponseEntity<ResultInSideDto> onSave(
      @RequestBody RequestScheduleDTO requestScheduleDTO) {
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  // Lấy danh sách CR trước khi sắp lịch và danh sách nhân viên
  @PostMapping("/getCrBefore")
  public ResponseEntity<RequestScheduleDTO> getCrBefore(@RequestBody RequestScheduleDTO dtoCheck) {
    RequestScheduleDTO result = cfgRequestScheduleBusiness.getCrBefore(dtoCheck);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  // Button cancel
  @PostMapping("/cancelStatus")
  public ResponseEntity<ResultInSideDto> cancelStatus(@RequestBody RequestScheduleDTO dto) {
    ResultInSideDto result = cfgRequestScheduleBusiness.cancelStatus(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
