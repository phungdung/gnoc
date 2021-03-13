package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrHardDevicesCheckListDTO;
import com.viettel.gnoc.mr.business.MrHardDevicesCheckListBusiness;
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
 * @author TrungDuong
 */
@Slf4j
@RestController
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrHardDevicesCheckList")
public class MrHardDevicesCheckListController {

  @Autowired
  MrHardDevicesCheckListBusiness mrHardDevicesCheckListBusiness;

  @PostMapping("/getListMrCheckListPage")
  public ResponseEntity<Datatable> getListMrCheckListPage(
      @RequestBody MrHardDevicesCheckListDTO mrCheckListDTO) {
    Datatable datatable = mrHardDevicesCheckListBusiness.onSearch(mrCheckListDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/insertMrCheckList")
  public ResponseEntity<ResultInSideDto> insertMrCheckList(
      @RequestBody MrHardDevicesCheckListDTO mrCheckListDTO) {
    ResultInSideDto resultInSideDto = mrHardDevicesCheckListBusiness.insert(mrCheckListDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateMrCheckList")
  public ResponseEntity<ResultInSideDto> updateMrCheckList(
      @RequestBody MrHardDevicesCheckListDTO mrCheckListDTO) {
    ResultInSideDto resultInSideDto = mrHardDevicesCheckListBusiness.update(mrCheckListDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> deleteById(Long checkListId) {
    ResultInSideDto data = mrHardDevicesCheckListBusiness.deleteMrCheckList(checkListId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetailMrCheckList")
  public ResponseEntity<MrHardDevicesCheckListDTO> getDetailMrCheckList(Long checkListId) {
    MrHardDevicesCheckListDTO data = mrHardDevicesCheckListBusiness.getDetail(checkListId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListArrayDeviceTypeNetworkType")
  public ResponseEntity<List<MrHardDevicesCheckListDTO>> getListArrayDeviceTypeNetworkType() {
    List<MrHardDevicesCheckListDTO> data = mrHardDevicesCheckListBusiness
        .getListArrayDeviceTypeNetworkType();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
