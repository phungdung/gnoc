package com.viettel.gnoc.mr.controller;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import com.viettel.gnoc.mr.business.MrTestXaBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrTestXaBTD")
public class MrTestXaController {

  @Autowired
  MrTestXaBusiness mrTestXaBusiness;

  @PostMapping("/getListDatatableMrCdBatterryDTO")
  public ResponseEntity<Datatable> getListDatatableMrCdBatterryDTO(
      @RequestBody MrCdBatteryDTO mrCdBatteryDTO) {
    Datatable data = mrTestXaBusiness.getListDatatableMrCdBatterryDTO(mrCdBatteryDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findMrCDBatteryByProperty")
  public ResponseEntity<MrCdBatteryDTO> findMrCDBatteryByProperty(@RequestBody MrCdBatteryDTO dto){
    MrCdBatteryDTO mrCdBatteryDTO = mrTestXaBusiness.findMrCDBatteryByProperty(dto);
    return new ResponseEntity<>(mrCdBatteryDTO, HttpStatus.OK);
  }

  @GetMapping("/findMrCDBatteryById")
  public ResponseEntity<MrCdBatteryDTO> findMrCDBatteryById(Long dcPowerId){
    MrCdBatteryDTO mrCdBatteryDTO = mrTestXaBusiness.findMrCDBatteryById(dcPowerId);
    return new ResponseEntity<>(mrCdBatteryDTO, HttpStatus.OK);
  }

  @GetMapping("/getListLocationCombobox")
  public ResponseEntity<List<ItemDataCRInside>> getListLocationCombobox(Long parentId) {
    List<ItemDataCRInside> list = mrTestXaBusiness.getListLocationCombobox(parentId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListLocationComboboxByCode")
  public ResponseEntity<List<ItemDataCRInside>> getListLocationComboboxByCode(String locationCode) {
    List<ItemDataCRInside> list = mrTestXaBusiness.getListLocationComboboxByCode(locationCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListCountry")
  public ResponseEntity<List<ItemDataCRInside>> getListCountry() {
    List<ItemDataCRInside> list = mrTestXaBusiness.getListCountry();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getDetailMrCdBatteryById")
  public ResponseEntity<MrCdBatteryDTO> getDetailById(Long dcPowerId){
    MrCdBatteryDTO mrCdBatteryDTO = mrTestXaBusiness.getDetailById(dcPowerId);
    return new ResponseEntity<>(mrCdBatteryDTO, HttpStatus.OK);
  }

  @PostMapping("/updateMrCdBatteryDTO")
  public ResponseEntity<ResultInSideDto> updateMrCdBatteryDTO(@RequestBody MrCdBatteryDTO mrCdBatteryDTO){
    ResultInSideDto resultInSideDto = mrTestXaBusiness.updateMrCdBatteryDTO(mrCdBatteryDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }
}
