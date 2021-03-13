package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrCDWorkItemDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.mr.business.MrCDWorkItemBusiness;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrCDWorkItem")
public class MrCDWorkItemController {

  @Autowired
  MrCDWorkItemBusiness mrCDWorkItemBusiness;

  @PostMapping("/listMrCDWorkItemPage")
  public ResponseEntity<Datatable> getListMrCDWorkItemPage(
      @RequestBody MrCDWorkItemDTO mrCDWorkItemDTO) {
    Datatable data = mrCDWorkItemBusiness.getListMrCDWorkItemPage(mrCDWorkItemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertMrCDWorkItem")
  public ResponseEntity<ResultInSideDto> add(
      @Valid @RequestBody MrCDWorkItemDTO mrCDWorkItemDTO) {
    ResultInSideDto data = mrCDWorkItemBusiness.insert(mrCDWorkItemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateMrCDWorkItem")
  public ResponseEntity<ResultInSideDto> edit(
      @Valid @RequestBody MrCDWorkItemDTO mrCDWorkItemDTO) {
    ResultInSideDto data = mrCDWorkItemBusiness.update(mrCDWorkItemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteMrCDWorkItem")
  public ResponseEntity<ResultInSideDto> deleteMrCDWorkItem(Long wiId) {
    ResultInSideDto data = mrCDWorkItemBusiness.delete(wiId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailByMrCDWorkItemId")
  public ResponseEntity<MrCDWorkItemDTO> getDetailByMrCDWorkItemId(Long wiId) {
    MrCDWorkItemDTO data = mrCDWorkItemBusiness.getDetail(wiId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //Mang: getCombobox Array
  @GetMapping("/getComboboxArray")
  public ResponseEntity<List<CatItemDTO>> getComboboxArray() {
    List<CatItemDTO> data = mrCDWorkItemBusiness.getComboboxArray();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //Loai thiet bi: getComboboxDeviceType
  @GetMapping("/getDeviceTypeCbb")
  public ResponseEntity<List<MrDeviceCDDTO>> getDeviceTypeCbb(String arrayCode) {
    List<MrDeviceCDDTO> data = mrCDWorkItemBusiness.getDeviceTypeCbb(arrayCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //Dau viec: getComboboxActivities
  @GetMapping("/getComboboxActivities")
  public ResponseEntity<List<CatItemDTO>> getComboboxActivities(Long itemId) {
    List<CatItemDTO> data = mrCDWorkItemBusiness.getComboboxActivities(itemId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
