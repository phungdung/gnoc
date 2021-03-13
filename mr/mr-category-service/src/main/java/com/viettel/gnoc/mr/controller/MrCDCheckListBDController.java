package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrCDCheckListBDDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.mr.business.MrCDCheckListBDBusiness;
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

@Slf4j
@RestController
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrCDCheckListBD")
public class MrCDCheckListBDController {

  @Autowired
  MrCDCheckListBDBusiness mrCDCheckListBDBusiness;

  @PostMapping("/listMrCDCheckListBDPage")
  public ResponseEntity<Datatable> getListMrCDCheckListBDPage(
      @RequestBody MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    Datatable data = mrCDCheckListBDBusiness.getListMrCDCheckListBDPage(mrCDCheckListBDDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertMrCDCheckListBD")
  public ResponseEntity<ResultInSideDto> add(
      @Valid @RequestBody MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    ResultInSideDto data = mrCDCheckListBDBusiness.insert(mrCDCheckListBDDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateMrCDCheckListBD")
  public ResponseEntity<ResultInSideDto> edit(
      @Valid @RequestBody MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    ResultInSideDto data = mrCDCheckListBDBusiness.update(mrCDCheckListBDDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteMrCDCheckListBD")
  public ResponseEntity<ResultInSideDto> deleteMrCDCheckListBD(Long wiId) {
    ResultInSideDto data = mrCDCheckListBDBusiness.delete(wiId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailByMrCDCheckListBDId")
  public ResponseEntity<MrCDCheckListBDDTO> getDetailByMrCDCheckListBDId(Long wiId) {
    MrCDCheckListBDDTO data = mrCDCheckListBDBusiness.getDetail(wiId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //Mang: getCombobox Array
  @GetMapping("/getComboboxArray")
  public ResponseEntity<List<CatItemDTO>> getComboboxArray() {
    List<CatItemDTO> data = mrCDCheckListBDBusiness.getComboboxArray();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //Loai thiet bi: getComboboxDeviceType
  @GetMapping("/getDeviceTypeCbb")
  public ResponseEntity<List<MrDeviceCDDTO>> getDeviceTypeCbb(String arrayCode) {
    List<MrDeviceCDDTO> data = mrCDCheckListBDBusiness.getDeviceTypeCbb(arrayCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //Dau viec: getComboboxActivities
  @GetMapping("/getComboboxActivities")
  public ResponseEntity<List<CatItemDTO>> getComboboxActivities(Long itemId) {
    List<CatItemDTO> data = mrCDCheckListBDBusiness.getComboboxActivities(itemId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
