package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrCfgCrUnitTelDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.mr.business.MrCfgCrUnitTelBusiness;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrCfgCrUnitTel")
public class MrCfgCrUnitTelController {

  @Autowired
  MrCfgCrUnitTelBusiness mrCfgCrUnitTelBusiness;

  @PostMapping("/getListDataMrCfgCrUnitTelSearchWeb")
  public ResponseEntity<Datatable> getListDataMrCfgCrUnitTelSearchWeb(
      @RequestBody MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    Datatable data = mrCfgCrUnitTelBusiness.getListDataMrCfgCrUnitTelSearchWeb(mrCfgCrUnitTelDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertMrCfgCrUnitTel")
  public ResponseEntity<ResultInSideDto> insertMrCfgCrUnitTel(
      @Valid @RequestBody MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    ResultInSideDto data = mrCfgCrUnitTelBusiness.insertMrCfgCrUnitTel(mrCfgCrUnitTelDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateMrCfgCrUnitTel")
  public ResponseEntity<ResultInSideDto> updateMrCfgCrUnitTel(
      @Valid @RequestBody MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    ResultInSideDto data = mrCfgCrUnitTelBusiness.updateMrCfgCrUnitTel(mrCfgCrUnitTelDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findMrCfgCrUnitTelById")
  public ResponseEntity<MrCfgCrUnitTelDTO> findMrCfgCrUnitTelById(Long cfgId) {
    MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO = mrCfgCrUnitTelBusiness.findMrCfgCrUnitTelById(cfgId);
    return new ResponseEntity<>(mrCfgCrUnitTelDTO, HttpStatus.OK);
  }

  @GetMapping("/deleteMrCfgCrUnitTel")
  public ResponseEntity<ResultInSideDto> deleteMrCfgCrUnitTel(Long cfgId) {
    ResultInSideDto resultInSideDto = mrCfgCrUnitTelBusiness.deleteMrCfgCrUnitTel(cfgId);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/exportDataMrCfgCrUnitTel")
  public ResponseEntity<File> exportDataMrCfgCrUnitTel(
      @RequestBody MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) throws Exception {
    File data = mrCfgCrUnitTelBusiness.exportDataMrCfgCrUnitTel(mrCfgCrUnitTelDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListRegionCombobox")
  public ResponseEntity<List<String>> getListRegionCombobox(String marketCode) {
    List<String> list = mrCfgCrUnitTelBusiness.getListRegionCombobox(marketCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListNetworkTypeCombobox")
  public ResponseEntity<List<String>> getListNetworkTypeCombobox(String arrayCode) {
    List<String> list = mrCfgCrUnitTelBusiness.getListNetworkTypeCombobox(arrayCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListDeviceTypeCombobox")
  public ResponseEntity<List<String>> getListDeviceTypeCombobox(
      @RequestBody MrDeviceDTO mrDeviceDTO) {
    List<String> list = mrCfgCrUnitTelBusiness.getListDeviceTypeCombobox(mrDeviceDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListUnitCombobox")
  public ResponseEntity<List<UnitDTO>> getListUnitCombobox() {
    List<UnitDTO> list = mrCfgCrUnitTelBusiness.getListUnitCombobox();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getTemplateImport")
  public ResponseEntity<File> getTemplateImport() throws Exception {
    File data = mrCfgCrUnitTelBusiness.getTemplateImport();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/importDataMrCfgCrUnitTel", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> importDataMrCfgCrUnitTel(
      @RequestPart("fileImport") MultipartFile fileImport) {
    ResultInSideDto result = mrCfgCrUnitTelBusiness.importDataMrCfgCrUnitTel(fileImport);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
