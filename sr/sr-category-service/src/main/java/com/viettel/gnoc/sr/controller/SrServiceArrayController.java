package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatServiceBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.business.SRServiceManageBusiness;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SearchConfigServiceDTO;
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

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "srServiceArray")
@Slf4j
public class SrServiceArrayController {

  @Autowired
  protected CatItemBusiness catItemBusiness;

  @Autowired
  protected CatServiceBusiness catServiceBusiness;

  @Autowired
  protected SRServiceManageBusiness srServiceManageBusiness;

  @PostMapping("/getListSearchSRServiceArray")
  public ResponseEntity<Datatable> getListSearchSRServiceArray(
      @RequestBody SRConfigDTO srConfigDTO) {
    Datatable data = srServiceManageBusiness.getListSearchSRServiceArray(srConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateServiceArray")
  public ResponseEntity<ResultInSideDto> insertOrUpdateServiceArray(
      @Valid @RequestBody SRConfigDTO srConfigDTO) {
    ResultInSideDto data = srServiceManageBusiness.insertOrUpdateServiceArray(srConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteSRServiceArray")
  public ResponseEntity<ResultInSideDto> deleteSRService(Long srConfigId) {
    ResultInSideDto data = srServiceManageBusiness.deleteSRService(srConfigId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getSRServiceArrayDetail")
  public ResponseEntity<SRConfigDTO> getSRServiceArrayDetail(Long srConfigId) {
    SRConfigDTO data = srServiceManageBusiness.getSRServiceArrayDetail(srConfigId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getSRServiceGroupDetail")
  public ResponseEntity<SRConfigDTO> getSRServiceGroupDetail(Long srConfigId) {
    SRConfigDTO data = srServiceManageBusiness.getSRServiceGroupDetail(srConfigId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSearchSRServiceGroup")
  public ResponseEntity<Datatable> getListSearchSRServiceGroup(
      @RequestBody SRConfigDTO srConfigDTO) {
    Datatable data = srServiceManageBusiness.getListSearchSRServiceGroup(srConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateServiceGroup")
  public ResponseEntity<ResultInSideDto> insertOrUpdateServiceGroup(
      @Valid @RequestBody SRConfigDTO srConfigDTO) {
    ResultInSideDto data = srServiceManageBusiness.insertOrUpdateServiceGroup(srConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //get list SRConfigServiceView
  @PostMapping("/getListConfigServiceSystem")
  public ResponseEntity<Datatable> getListConfigServiceSystem(
      @RequestBody SRConfigDTO srConfigDTO) {
    Datatable data = srServiceManageBusiness.getListConfigServiceSystem(srConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // update SRConfigServiceView
  @PostMapping("/insertOrUpdateSRConfigService")
  public ResponseEntity<ResultInSideDto> insertOrUpdateSRConfigService(
      @Valid @RequestBody SRConfigDTO srConfigDTO) {
    ResultInSideDto data = srServiceManageBusiness.insertOrUpdateSRConfigService(srConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // delete SRConfigServiceView
  @GetMapping("/deleteSRConfig")
  public ResponseEntity<ResultInSideDto> deleteSRConfig(Long srConfigId) {
    ResultInSideDto data = srServiceManageBusiness.deleteSRService(srConfigId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get config for SRConfigServiceView
  @GetMapping("/getByConfigGroup")
  public ResponseEntity<List<SRConfigDTO>> getByConfigGroup(String configGroup) {
    List<SRConfigDTO> data = srServiceManageBusiness.getByConfigGroup(configGroup);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get search SRConfigServiceView
  @GetMapping("/getListSearch")
  public ResponseEntity<SearchConfigServiceDTO> getListSearch() {
    SearchConfigServiceDTO data = srServiceManageBusiness.getListSearch();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get config for SRConfigServiceView
  @GetMapping("/getByConfigGroupCountry")
  public ResponseEntity<List<SRConfigDTO>> getByConfigGroupCountry(String country,
      String configGroup) {
    List<SRConfigDTO> data = srServiceManageBusiness.getByConfigGroupCountry(country, configGroup);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
