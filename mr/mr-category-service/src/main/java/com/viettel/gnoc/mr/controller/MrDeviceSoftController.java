package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.mr.business.MrDeviceSoftBusiness;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import java.io.File;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrDeviceSoft")
public class MrDeviceSoftController {

  @Autowired
  MrDeviceSoftBusiness mrDeviceSoftBusiness;

  @PostMapping("/getListDataMrDeviceSoftSearchWeb")
  public ResponseEntity<Datatable> getListDataMrDeviceSoftSearchWeb(
      @RequestBody MrDeviceDTO mrDeviceDTO) {
    Datatable data = mrDeviceSoftBusiness.getListDataMrDeviceSoftSearchWeb(mrDeviceDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findMrDeviceSoftWeb")
  public ResponseEntity<MrDeviceDTO> findMrDeviceSoftWeb(Long deviceId) {
    MrDeviceDTO mrDeviceDTO = mrDeviceSoftBusiness.findMrDeviceSoftWeb(deviceId);
    return new ResponseEntity<>(mrDeviceDTO, HttpStatus.OK);
  }

  @PostMapping("/updateMrDeviceSoft")
  public ResponseEntity<ResultInSideDto> updateMrDeviceSoft(
      @Valid @RequestBody MrDeviceDTO mrDeviceDTO) {
    ResultInSideDto data = mrDeviceSoftBusiness.updateMrDeviceSoft(mrDeviceDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/deleteMrDeviceSoft")
  public ResponseEntity<ResultInSideDto> deleteMrDeviceSoft(@RequestBody MrDeviceDTO mrDeviceDTO) {
    ResultInSideDto result = mrDeviceSoftBusiness.deleteMrDeviceSoft(mrDeviceDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/approveMrDeviceSoft")
  public ResponseEntity<ResultInSideDto> approveMrDeviceSoft(@RequestBody MrDeviceDTO mrDeviceDTO) {
    ResultInSideDto result = mrDeviceSoftBusiness.approveMrDeviceSoft(mrDeviceDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getListMaintainStatusCombobox")
  public ResponseEntity<List<MrConfigDTO>> getListMaintainStatusCombobox() {
    List<MrConfigDTO> list = mrDeviceSoftBusiness.getListMaintainStatusCombobox();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListConfirmSoftCombobox")
  public ResponseEntity<List<MrConfigDTO>> getListConfirmSoftCombobox() {
    List<MrConfigDTO> list = mrDeviceSoftBusiness.getListConfirmSoftCombobox();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListWoCdGroupCombobox")
  public ResponseEntity<List<WoCdGroupInsideDTO>> getListWoCdGroupCombobox() {
    List<WoCdGroupInsideDTO> list = mrDeviceSoftBusiness.getListWoCdGroupCombobox();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/exportDataMrDeviceSoft")
  public ResponseEntity<File> exportDataMrDeviceSoft(
      @RequestBody MrDeviceDTO mrDeviceDTO) throws Exception {
    File data = mrDeviceSoftBusiness.exportDataMrDeviceSoft(mrDeviceDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListMrCfgMarket")
  public ResponseEntity<List<MrCfgMarketDTO>> getListMrCfgMarket() {
    List<MrCfgMarketDTO> list = mrDeviceSoftBusiness.getListMrCfgMarket();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/updateListMrCfgMarket")
  public ResponseEntity<ResultInSideDto> updateListMrCfgMarket(
      @RequestBody MrCfgMarketDTO mrCfgMarketDTO) {
    ResultInSideDto data = mrDeviceSoftBusiness.updateListMrCfgMarket(mrCfgMarketDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getTemplateImport")
  public ResponseEntity<File> getTemplateImport() throws Exception {
    File data = mrDeviceSoftBusiness.getTemplateImport();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/importDataMrDeviceSoft", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> importDataMrDeviceSoft(
      @RequestPart("fileImport") MultipartFile fileImport) {
    ResultInSideDto result = mrDeviceSoftBusiness.importDataMrDeviceSoft(fileImport);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getListRegionCombobox")
  public ResponseEntity<List<String>> getListRegionCombobox(String marketCode) {
    List<String> list = mrDeviceSoftBusiness.getListRegionCombobox(marketCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListNetworkTypeCombobox")
  public ResponseEntity<List<String>> getListNetworkTypeCombobox(String arrayCode) {
    List<String> list = mrDeviceSoftBusiness.getListNetworkTypeCombobox(arrayCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListDeviceTypeCombobox")
  public ResponseEntity<List<String>> getListDeviceTypeCombobox(
      @RequestBody MrDeviceDTO mrDeviceDTO) {
    List<String> list = mrDeviceSoftBusiness.getListDeviceTypeCombobox(mrDeviceDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListUnitCombobox")
  public ResponseEntity<List<UnitDTO>> getListUnitCombobox() {
    List<UnitDTO> list = mrDeviceSoftBusiness.getListUnitCombobox();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

}
