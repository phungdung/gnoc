package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrHardCDDTO;
import com.viettel.gnoc.mr.business.MrHardCdBusiness;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
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
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrHardCD")
public class MrHardCdController {

  @Autowired
  MrHardCdBusiness mrHardCdBusiness;

  @PostMapping("/listMrHardCDPage")
  public ResponseEntity<Datatable> listMrHardCDPage(@RequestBody MrHardCDDTO mrHardCDDTO) {
    Datatable data = mrHardCdBusiness.getListMrHardCDPage(mrHardCDDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertMrHardCD")
  public ResponseEntity<ResultInSideDto> add(@Valid @RequestBody MrHardCDDTO mrHardCDDTO) {
    ResultInSideDto data = mrHardCdBusiness.insert(mrHardCDDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateMrHardCD")
  public ResponseEntity<ResultInSideDto> edit(@Valid @RequestBody MrHardCDDTO mrHardCDDTO) {
    ResultInSideDto data = mrHardCdBusiness.update(mrHardCDDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteMrHardCD")
  public ResponseEntity<ResultInSideDto> delete(Long hardCDId) {
    ResultInSideDto data = mrHardCdBusiness.delete(hardCDId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailMrHardCD")
  public ResponseEntity<MrHardCDDTO> getDetailMrHardCDByID(Long hardCDId) {
    MrHardCDDTO mrHardCDDTO = mrHardCdBusiness.getDetail(hardCDId);
    return new ResponseEntity<>(mrHardCDDTO, HttpStatus.OK);
  }

  @GetMapping("/getWoCdGroupCBB")
  public ResponseEntity<List<WoCdGroupDTO>> getWoCdGroupCBB() {
    List<WoCdGroupDTO> list = mrHardCdBusiness.getWoCdGroupCBB();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

}
