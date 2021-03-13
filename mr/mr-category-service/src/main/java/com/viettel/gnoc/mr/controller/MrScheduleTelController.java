package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.mr.business.MrScheduleTelBusiness;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrScheduleTelService")
public class MrScheduleTelController {

  @Autowired
  private MrScheduleTelBusiness mrScheduleTelBusiness;

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(@RequestBody MrScheduleTelDTO mrScheduleTelDTO) {
    Datatable datatable = mrScheduleTelBusiness.onSearchScheduleTel(mrScheduleTelDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @GetMapping("/findById")
  public ResponseEntity<MrScheduleTelDTO> findById(Long id) {
    MrScheduleTelDTO data = mrScheduleTelBusiness
        .getDetail(id, "S");
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
  //CBB ly do khong bao duong, lay code + name

  @PostMapping("/onUpdate")
  public ResponseEntity<ResultInSideDto> onUpdate(
      @RequestBody MrScheduleTelDTO mrScheduleTelDTO) {
    ResultInSideDto data = mrScheduleTelBusiness.onUpdateSoft(mrScheduleTelDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @DeleteMapping("/deleteMrScheduleTel")
  public ResponseEntity<ResultInSideDto> deleteMrScheduleTel(
      @RequestBody MrScheduleTelDTO mrScheduleTelDTO) {
    List<MrScheduleTelDTO> lstDTOS = new ArrayList<>();
    lstDTOS.add(mrScheduleTelDTO);
    ResultInSideDto data = mrScheduleTelBusiness.deleteMrScheduleTelSoft(lstDTOS);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
