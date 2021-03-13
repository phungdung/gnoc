package com.viettel.gnoc.incident.controller;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.incident.business.CfgTimeTroubleProcessBusiness;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
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

@RestController
@RequestMapping(Constants.PT_API_PATH_PREFIX + "CfgTimeTroubleProcess")
@Slf4j
public class CfgTimeTroubleProcessController {

  @Autowired
  CfgTimeTroubleProcessBusiness cfgTimeTroubleProcessBusiness;

  @PostMapping("/getListCfgTimeTroubleProcessDTO")
  public ResponseEntity<Datatable> getListCfgTimeTroubleProcessDTO(
      @RequestBody CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    Datatable data = cfgTimeTroubleProcessBusiness
        .getListCfgTimeTroubleProcessDTO(cfgTimeTroubleProcessDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getConfigTimeTroubleProcess")
  public CfgTimeTroubleProcessDTO getConfigTimeTroubleProcess(
      @RequestBody CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    return cfgTimeTroubleProcessBusiness.getConfigTimeTroubleProcess(cfgTimeTroubleProcessDTO);
  }

  @PostMapping("/findCfgTimeTroubleProcessById")
  public ResponseEntity<CfgTimeTroubleProcessDTO> findCfgTimeTroubleProcessById(Long id) {
    CfgTimeTroubleProcessDTO data = cfgTimeTroubleProcessBusiness
        .findCfgTimeTroubleProcessById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertCfgTimeTroubleProcess")
  public ResponseEntity<ResultInSideDto> insertCfgTimeTroubleProcess(
      @Valid @RequestBody CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    ResultInSideDto data = cfgTimeTroubleProcessBusiness
        .insertCfgTimeTroubleProcess(cfgTimeTroubleProcessDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateCfgTimeTroubleProcess")
  public ResponseEntity<ResultInSideDto> updateCfgTimeTroubleProcess(
      @Valid @RequestBody CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    ResultInSideDto data = cfgTimeTroubleProcessBusiness
        .updateCfgTimeTroubleProcess(cfgTimeTroubleProcessDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListCfgTimeTroubleProcess")
  public ResponseEntity<ResultInSideDto> insertOrUpdateListCfgTimeTroubleProcess(
      @RequestBody List<CfgTimeTroubleProcessDTO> listCfgTimeTroubleProcessDTO) {
    ResultInSideDto result = cfgTimeTroubleProcessBusiness
        .insertOrUpdateListCfgTimeTroubleProcess(listCfgTimeTroubleProcessDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/deleteCfgTimeTroubleProcess")
  public ResponseEntity<ResultInSideDto> deleteCfgTimeTroubleProcess(Long id) {
    ResultInSideDto result = cfgTimeTroubleProcessBusiness.deleteCfgTimeTroubleProcess(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/deleteListCfgTimeTroubleProcess")
  public ResponseEntity<ResultInSideDto> deleteListCfgTimeTroubleProcess(
      @RequestBody List<CfgTimeTroubleProcessDTO> listCfgTimeTroubleProcessDTO) {
    ResultInSideDto result = cfgTimeTroubleProcessBusiness
        .deleteListCfgTimeTroubleProcess(listCfgTimeTroubleProcessDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getSequenseCfgTimeTroubleProcess")
  public ResponseEntity<List<String>> getSequenseCfgTimeTroubleProcess(int... size) {
    List<String> data = cfgTimeTroubleProcessBusiness.getSequenseCfgTimeTroubleProcess(size);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCfgTimeTroubleProcessByCondition")
  public ResponseEntity<List<CfgTimeTroubleProcessDTO>> getListCfgTimeTroubleProcessByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    List<CfgTimeTroubleProcessDTO> data = cfgTimeTroubleProcessBusiness
        .getListCfgTimeTroubleProcessByCondition(lstCondition, rowStart, maxRow, sortType,
            sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportDataCfgTimeTroubleProcess")
  public ResponseEntity<File> exportDataCfgTimeTroubleProcess(
      @RequestBody CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) throws Exception {
    File data = cfgTimeTroubleProcessBusiness.exportData(cfgTimeTroubleProcessDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getTemplate")
  public ResponseEntity<File> getTemplate() throws Exception {
    File data = cfgTimeTroubleProcessBusiness.getTemplate();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSubCategory")
  public ResponseEntity<List<CatItemDTO>> getListSubCategory(Long typeId) {
    List<CatItemDTO> data = cfgTimeTroubleProcessBusiness.getListSubCategory(typeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/importDataCfgTimeTroubleProcess", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> importDataCfgTimeTroubleProcess(
      @RequestPart("fileImport") MultipartFile fileImport) {
    ResultInSideDto result = cfgTimeTroubleProcessBusiness.importData(fileImport);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getItemDTO")
  public ResponseEntity<List<CatItemDTO>> getItemDTO(String typeId, String alarmId,
      String nationCode) {
    List<CatItemDTO> data = cfgTimeTroubleProcessBusiness.getItemDTO(typeId, alarmId, nationCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
