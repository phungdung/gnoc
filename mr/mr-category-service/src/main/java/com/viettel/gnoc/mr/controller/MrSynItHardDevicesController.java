package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.business.MrSynItHardDevicesBusiness;
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

@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrSynItHardDevices")
public class MrSynItHardDevicesController {

  @Autowired
  MrSynItHardDevicesBusiness mrSynItHardDevicesBusiness;

  @PostMapping("/getListMrDTOSearch")
  public ResponseEntity<Datatable> getListMrSynITDeviceSoftDTO(@RequestBody MrSynItDevicesDTO dto,
      int rowStart, int maxRow) {
    Datatable data = new Datatable();
    List<MrSynItDevicesDTO> lst = mrSynItHardDevicesBusiness
        .getListMrSynITDeviceHardDTO(dto, rowStart, maxRow);
    if (lst != null && lst.size() > 0) {
      data.setTotal(lst.size());
      int pages = (int) Math.ceil(lst.size() * 1.0 / dto.getPageSize());
      data.setPages(pages);
      data.setData(
          DataUtil.subPageList(lst, dto.getPage(), dto.getPageSize()));
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListMrSynItDevicesHardDTO")
  public ResponseEntity<Datatable> getListMrSynItDevicesHardDTO(
      @RequestBody MrSynItDevicesDTO mrSynItDevicesDTO) {
    Datatable data = mrSynItHardDevicesBusiness.getListMrSynITDeviceHardPage(mrSynItDevicesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getMrSynItDevicesDetail")
  public ResponseEntity<MrSynItDevicesDTO> getMrSynItDevicesDetail(Long id) {
    MrSynItDevicesDTO data = mrSynItHardDevicesBusiness.getMrSynItDevicesHardDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/deleteMrSynItHardDevice")
  public ResponseEntity<ResultInSideDto> deleteMrSynItHardDevice(
      @RequestBody MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto data = mrSynItHardDevicesBusiness.deleteMrSynItHardDevice(mrSynItDevicesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/approveMrSynItHardDevice")
  public ResponseEntity<ResultInSideDto> approveItHardDevice(
      @RequestBody MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto data = mrSynItHardDevicesBusiness.approveItHardDevice(mrSynItDevicesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateMrSynItHardDevice")
  public ResponseEntity<ResultInSideDto> updateMrSynItHardDevice(
      @RequestBody MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto data = mrSynItHardDevicesBusiness.updateMrSynItHardDevice(mrSynItDevicesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListRegionByMrSynItHardDevices")
  public ResponseEntity<List<MrITHardScheduleDTO>> getListRegionByMrSynItHardDevices(
      String country) {
    List<MrITHardScheduleDTO> data = mrSynItHardDevicesBusiness
        .getListRegionByMrSynItHardDevices(country);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
