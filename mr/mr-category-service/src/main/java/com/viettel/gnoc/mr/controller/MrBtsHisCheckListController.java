package com.viettel.gnoc.mr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.mr.business.MrBtsHisCheckListBusiness;
import com.viettel.gnoc.mr.business.MrDeviceBtsBusiness;
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
@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrBtsHisCheckList")
public class MrBtsHisCheckListController {

  @Autowired
  MrDeviceBtsBusiness mrDeviceBtsBusiness;

  @Autowired
  MrBtsHisCheckListBusiness mrBtsHisCheckListBusiness;

  @PostMapping("/checkIsApprovalReturnLstMrScheBtsHisDetail")
  public ResponseEntity<ResultInSideDto> checkIsApprovalReturnLstMrScheBtsHisDetail(String typeView,
      String formDataJson) {
    ResultInSideDto result = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    try {
      ObjectMapper mapper = new ObjectMapper();
      MrScheduleBtsHisDetailInsiteDTO mrDeviceBtsDTO = mapper
          .readValue(formDataJson, MrScheduleBtsHisDetailInsiteDTO.class);
      result = mrBtsHisCheckListBusiness
          .checkIsApprovalReturnLstMrScheBtsHisDetail(typeView, mrDeviceBtsDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/checkIsHaveRole")
  public ResponseEntity<ResultInSideDto> checkIsHaveRole(String typeView, String woCode) {
    ResultInSideDto result = mrBtsHisCheckListBusiness.checkIsHaveRole(typeView, woCode);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListWoCodeMrScheduleBtsHisDetail")
  public ResponseEntity<List<MrScheduleBtsHisDetailInsiteDTO>> getListWoCodeMrScheduleBtsHisDetail(
      @RequestBody MrScheduleBtsHisDetailInsiteDTO dto) {
    List<MrScheduleBtsHisDetailInsiteDTO> result = mrBtsHisCheckListBusiness
        .getListWoCodeMrScheduleBtsHisDetail(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getCheckListByWoId")
  public ResponseEntity<List<MrScheduleBtsHisDetailInsiteDTO>> getCheckListByWoId(String woCode) {
    List<MrScheduleBtsHisDetailInsiteDTO> result = mrBtsHisCheckListBusiness.getCheckListByWoId(woCode);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/checkShowRedAndGetFiles")
  public ResponseEntity<List<MrScheduleBtsHisDetailInsiteDTO>> checkShowRedAndGetFiles(String woCode) {
    List<MrScheduleBtsHisDetailInsiteDTO> result = mrBtsHisCheckListBusiness
        .checkShowRedAndGetFiles(woCode);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/actionSaveList")
  public ResponseEntity<ResultInSideDto> actionSaveList(
      @RequestBody MrScheduleBtsHisDetailInsiteDTO dto) {
    ResultInSideDto result = mrBtsHisCheckListBusiness.actionSaveList(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}


