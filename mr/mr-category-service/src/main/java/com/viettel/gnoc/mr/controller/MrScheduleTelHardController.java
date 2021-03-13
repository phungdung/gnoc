package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.mr.business.MrScheduleTelBusiness;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrScheduleTelHard")
public class MrScheduleTelHardController {

  @Autowired
  MrScheduleTelBusiness mrScheduleTelBusiness;

  @PostMapping("/getListMrScheduleTel")
  public ResponseEntity<Datatable> getListMrScheduleTel(
      @RequestBody MrScheduleTelDTO mrScheduleTelDTO) {
    Datatable data = mrScheduleTelBusiness
        .getListMrScheduleTel(mrScheduleTelDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findWoById")
  public ResponseEntity<WoInsideDTO> findWoById(@RequestBody MrScheduleTelDTO mrScheduleTelDTO) {
    WoInsideDTO data = mrScheduleTelBusiness
        .findWoById(mrScheduleTelDTO.getWoId(), mrScheduleTelDTO.getMrId());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<MrScheduleTelDTO> getDetail(Long id) {
    MrScheduleTelDTO data = mrScheduleTelBusiness
        .getDetail(id, "H");
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getConfigByGroup")
  public ResponseEntity<List<MrConfigDTO>> getConfigByGroup(String configGroup) {
    List<MrConfigDTO> data = mrScheduleTelBusiness
        .getConfigByGroup(configGroup);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PutMapping("/updateMrScheduleTelDTO")
  public ResponseEntity<ResultInSideDto> updateMrScheduleTelDTO(
      @RequestBody MrScheduleTelDTO mrScheduleTelDTO) {
    ResultInSideDto data = mrScheduleTelBusiness
        .updateMrScheduleTelDTO(mrScheduleTelDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @DeleteMapping("/deleteMrScheduleTel")
  public ResponseEntity<ResultInSideDto> deleteMrScheduleTel(Long scheduleId) {
    ResultInSideDto data = mrScheduleTelBusiness
        .deleteMrScheduleTel(scheduleId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


}
