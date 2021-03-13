package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.mr.business.MrScheduleBtsHisBusiness;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrScheduleBtsHis")
public class MrScheduleBtsHisController {

  @Autowired
  MrScheduleBtsHisBusiness mrScheduleBtsHisBusiness;

  @PostMapping("/getListMrScheduleBtsHisDTO")
  public ResponseEntity<Datatable> getListMrScheduleBtsHisDTO(
      @RequestBody MrScheduleBtsHisDTO mrScheduleBtsHisDTO) {
    Datatable data = mrScheduleBtsHisBusiness.getListMrScheduleBtsHisDTO(mrScheduleBtsHisDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWoBts")
  public ResponseEntity<List<MrScheduleBtsHisDetailInsiteDTO>> getListWoBts(
      @RequestBody MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO) {
    List<MrScheduleBtsHisDetailInsiteDTO> data = mrScheduleBtsHisBusiness
        .getListWoBts(mrScheduleBtsHisDetailDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/reCreateWo")
  public ResponseEntity<ResultInSideDto> reCreateWo(
      @RequestBody MrScheduleBtsHisDTO mrScheduleBtsHisDTO) {
    ResultInSideDto data = mrScheduleBtsHisBusiness.reCreateWo(mrScheduleBtsHisDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/reCreateWoConfirm")
  public ResponseEntity<ResultInSideDto> reCreateWoConfirm(
      @RequestBody MrScheduleBtsHisDTO mrScheduleBtsHisDTO) {
    ResultInSideDto data = mrScheduleBtsHisBusiness.reCreateWoConfirm(mrScheduleBtsHisDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
