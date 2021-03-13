package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CrAlarmFaultGroupDTO;
import com.viettel.gnoc.commons.dto.CrModuleDraftDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.business.CrAlarmBusiness;
import com.viettel.gnoc.cr.business.CrFileAttachBusiness;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrModuleDetailDTO;
import com.viettel.gnoc.cr.dto.CrVendorDetailDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrAlarmService")
@Slf4j
public class CrAlarmController {

  @Autowired
  CrAlarmBusiness crAlarmBusiness;

  @Autowired
  CrFileAttachBusiness crFileAttachBusiness;

  @Autowired
  protected CatItemBusiness catItemBusiness;

  @PostMapping("/getListAlarmByCr")
  public ResponseEntity<List<CrAlarmInsiteDTO>> getListAlarmByCr(
      @RequestBody CrInsiteDTO crInsiteDTO) {
    List<CrAlarmInsiteDTO> lst = crAlarmBusiness.getListAlarmByCr(crInsiteDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @GetMapping("/getListFaultSrc")
  public ResponseEntity<HashSet<String>> getListFaultSrc(String nationCode) throws Exception {
    return new ResponseEntity<>(crAlarmBusiness.getListFaultSrc(nationCode),
        HttpStatus.OK);
  }

  @GetMapping("/getListGroupFaultSrc")
  public ResponseEntity<List<CrAlarmFaultGroupDTO>> getListGroupFaultSrc(String faultSrc,
      String nationCode) throws Exception {
    return new ResponseEntity<>(crAlarmBusiness.getListGroupFaultSrc(faultSrc, nationCode),
        HttpStatus.OK);
  }

  //Fill alarm setting sau khi bam nut search khi them moi alarm
  @PostMapping("/getAlarmList")
  public ResponseEntity<Datatable> getAlarmList(@RequestBody CrAlarmInsiteDTO crAlarmDTO)
      throws Exception {
    return new ResponseEntity<>(crAlarmBusiness.getAlarmList(crAlarmDTO), HttpStatus.OK);
  }


  //Fill data module trong tab vendor
  @PostMapping("/getVendorList")
  public ResponseEntity<Datatable> getVendorList(@RequestBody CrAlarmSettingDTO crAlarmSettingDTO) {
    return new ResponseEntity<>(crAlarmBusiness.getVendorList(crAlarmSettingDTO),
        HttpStatus.OK);
  }

  @GetMapping("/nationMap")
  public ResponseEntity<Map<String, String>> nationMap() {
    return new ResponseEntity<>(crAlarmBusiness.nationMap(),
        HttpStatus.OK);
  }

  //Fill data module trong tabModule
  @PostMapping("/setupModuleData")
  public ResponseEntity<Datatable> setupModuleData(@RequestBody CrModuleDraftDTO crModuleDraftDTO) {
    return new ResponseEntity<>(crAlarmBusiness.setupModuleData(crModuleDraftDTO), HttpStatus.OK);
  }

  //Fill data vao Tab Module
  @PostMapping("/getListModuleByCr")
  public ResponseEntity<List<CrModuleDetailDTO>> getListModuleByCr(
      @RequestBody CrInsiteDTO crInsiteDTO) {
    return new ResponseEntity<>(crAlarmBusiness.getListModuleByCr(crInsiteDTO), HttpStatus.OK);
  }

  //Fill data vao Tab Vendor
  @PostMapping("/getListVendorByCr")
  public ResponseEntity<List<CrVendorDetailDTO>> getListVendorByCr(
      @RequestBody CrInsiteDTO crInsiteDTO) {
    return new ResponseEntity<>(crAlarmBusiness.getListVendorByCr(crInsiteDTO), HttpStatus.OK);
  }

  @PostMapping("/getAlarmSetting")
  public ResponseEntity<Datatable> getAlarmSetting(
      @RequestBody CrAlarmSettingDTO crAlarmSettingDTO) {
    return new ResponseEntity<>(crAlarmBusiness.getAlarmSetting(crAlarmSettingDTO), HttpStatus.OK);
  }

  //LOAD ALARM
  @PostMapping("/getAlarmSettingByVendor")
  public ResponseEntity<List<CrAlarmSettingDTO>> getAlarmSettingByVendor(
      @RequestBody CrAlarmSettingDTO crAlarmSettingDTO) {
    return new ResponseEntity<>(crAlarmBusiness.getAlarmSettingByVendor(crAlarmSettingDTO),
        HttpStatus.OK);
  }

  //LOAD ALARM
  @PostMapping("/getAlarmSettingByModule")
  public ResponseEntity<List<CrAlarmSettingDTO>> getAlarmSettingByModule(
      @RequestBody CrAlarmSettingDTO crAlarmSettingDTO) {
    return new ResponseEntity<>(crAlarmBusiness.getAlarmSettingByModule(crAlarmSettingDTO),
        HttpStatus.OK);
  }

  @PostMapping("/getAlarmSettingByLstNationModule")
  public ResponseEntity<List<CrAlarmSettingDTO>> getAlarmSettingByLstNationModule(
      @RequestBody List<CrAlarmSettingDTO> crAlarmSettingDTOS) {
    return new ResponseEntity<>(crAlarmBusiness.getListAlarmSettingByModule(crAlarmSettingDTOS),
        HttpStatus.OK);
  }

  @PostMapping("/getListItemByCategory")
  public ResponseEntity<List<CatItemDTO>> getListItemByCategory(String categoryCode,
      String itemCode) {
    return new ResponseEntity<>(catItemBusiness.getListItemByCategory(categoryCode, itemCode),
        HttpStatus.OK);
  }
}
