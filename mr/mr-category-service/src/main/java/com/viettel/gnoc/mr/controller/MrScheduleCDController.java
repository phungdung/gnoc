package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdDTO;
import com.viettel.gnoc.mr.business.MrScheduleCDBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrScheduleCDService")
public class MrScheduleCDController {

  @Autowired
  private MrScheduleCDBusiness mrScheduleCDBusiness;

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(@RequestBody MrScheduleCdDTO mrScheduleCdDTO) {
    Datatable datatable = mrScheduleCDBusiness.onSearch(mrScheduleCdDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @DeleteMapping("/deleteById")
  public ResponseEntity<ResultInSideDto> deleteById(Long id) {
    ResultInSideDto data = mrScheduleCDBusiness.deleteById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/onUpdate")
  public ResponseEntity<ResultInSideDto> onUpdate(@RequestBody MrScheduleCdDTO dto) {
    ResultInSideDto result = mrScheduleCDBusiness.addOrUpdate(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/findById")
  public ResponseEntity<MrScheduleCdDTO> findById(Long id) {
    MrScheduleCdDTO data = mrScheduleCDBusiness.findById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
