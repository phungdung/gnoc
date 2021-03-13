package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.business.MrSynItSoftDevicesBusiness;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrSynItSoftDevices")
public class MrSynItSoftDevicesController {

  @Autowired
  MrSynItSoftDevicesBusiness mrSynItSoftDevicesBusiness;


  @PostMapping("/getListMrDTOSearch")
  public ResponseEntity<Datatable> getListMrSynITDeviceSoftDTO(@RequestBody MrSynItDevicesDTO dto,
      int rowStart, int maxRow) {
    Datatable data = new Datatable();
    List<MrSynItDevicesDTO> lst = mrSynItSoftDevicesBusiness
        .getListMrSynITDeviceSoftDTO(dto, rowStart, maxRow);
    if (lst != null && lst.size() > 0) {
      data.setTotal(lst.size());
      int pages = (int) Math.ceil(lst.size() * 1.0 / dto.getPageSize());
      data.setPages(pages);
      data.setData(
          DataUtil.subPageList(lst, dto.getPage(), dto.getPageSize()));
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListMrSynItDeviceSoftDTO")
  public ResponseEntity<Datatable> getListMrSynItDeviceSoftDTO(
      @RequestBody MrSynItDevicesDTO mrSynItDevicesDTO) {
    Datatable data = mrSynItSoftDevicesBusiness.getListMrSynItDeviceSoftDTO(mrSynItDevicesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getMrSynItDevicesDetail")
  public ResponseEntity<MrSynItDevicesDTO> getMrSynItDevicesDetail(Long id) {
    MrSynItDevicesDTO data = mrSynItSoftDevicesBusiness.getMrSynItDevicesDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/deleteMrSynItSoftDevice")
  public ResponseEntity<ResultInSideDto> deleteMrSynItSoftDevice(
      @RequestBody MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto data = mrSynItSoftDevicesBusiness.deleteMrSynItSoftDevice(mrSynItDevicesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/approveMrSynItSoftDevice")
  public ResponseEntity<ResultInSideDto> approveItSoftDevice(
      @RequestBody MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto data = mrSynItSoftDevicesBusiness.approveItSoftDevice(mrSynItDevicesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateMrSynItSoftDevice")
  public ResponseEntity<ResultInSideDto> updateMrItSoftDevice(
      @RequestBody MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto data = mrSynItSoftDevicesBusiness.updateMrItSoftDevice(mrSynItDevicesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
