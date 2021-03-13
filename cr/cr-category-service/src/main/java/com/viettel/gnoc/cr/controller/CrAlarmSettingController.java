package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.config.SecurityAnnotation;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CrAlarmFaultGroupDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.PERMISSION;
import com.viettel.gnoc.commons.utils.Constants.ROLE;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CrAlarmSettingBusiness;
import com.viettel.gnoc.cr.business.CrManagerUnitsOfScopeBusiness;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "CrAlarmSetting")
@Slf4j
public class CrAlarmSettingController {

  @Autowired
  protected CrAlarmSettingBusiness crAlarmSettingBusiness;
  @Autowired
  protected CrManagerUnitsOfScopeBusiness crManagerUnitsOfScopeBusiness;
  @Autowired
  protected CatItemBusiness catItemBusiness;


  @PostMapping("/getCrAlarmSetting")
  public ResponseEntity<Datatable> getCrAlarmSetting(@RequestBody
      CrAlarmSettingDTO crAlarmSettingDTO) {
    Datatable data = crAlarmSettingBusiness.getCrAlarmSetting(crAlarmSettingDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @SecurityAnnotation(role = ROLE.CR_ALARM_SETTING, permission = PERMISSION.EDIT)
  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_ALARM_SETTING)
  @PostMapping("/updateVendorOrModuleAlarmSetting")
  public ResponseEntity<ResultInSideDto> updateVendorOrModuleAlarmSetting(@RequestBody
      CrAlarmSettingDTO crAlarmSettingDTO) {
    return new ResponseEntity<>(
        crAlarmSettingBusiness.updateVendorOrModuleAlarmSetting(crAlarmSettingDTO), HttpStatus.OK);
  }

  @SecurityAnnotation(role = ROLE.CR_ALARM_SETTING, permission = PERMISSION.ADD)
  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_ALARM_SETTING)
  @PostMapping("/saveOrUpdateAlarmSetting")
  public ResponseEntity<ResultInSideDto> saveOrUpdateAlarmSetting(@RequestBody
      CrAlarmSettingDTO crAlarmSettingDTO) {
    return new ResponseEntity<>(crAlarmSettingBusiness.saveOrUpdateAlarmSetting(crAlarmSettingDTO),
        HttpStatus.OK);
  }

  @SecurityAnnotation(role = ROLE.CR_ALARM_SETTING, permission = PERMISSION.EDIT)
  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_ALARM_SETTING)
  @PutMapping("/updateAlarmSetting")
  public ResponseEntity<ResultInSideDto> updateAlarmSetting(@RequestBody
      CrAlarmSettingDTO crAlarmSettingDTO) {
    return new ResponseEntity<>(crAlarmSettingBusiness.updateAlarmSetting(crAlarmSettingDTO),
        HttpStatus.OK);
  }

  @PostMapping("/findAlarmSettingById")
  public ResponseEntity<CrAlarmSettingDTO> findAlarmSettingById(
      @RequestBody CrAlarmInsiteDTO crAlarmDTO) {
    return new ResponseEntity<>(crAlarmSettingBusiness.findCrAlarmSettingById(crAlarmDTO),
        HttpStatus.OK);
  }

  @PostMapping("/getAlarmList")
  public ResponseEntity<Datatable> getAlarmList(@RequestBody CrAlarmInsiteDTO crAlarmDTO)
      throws Exception {
    return new ResponseEntity<>(crAlarmSettingBusiness.getAlarmList(crAlarmDTO), HttpStatus.OK);
  }

  @GetMapping("/getListImpactSegmentCBB")
  public ResponseEntity<List<ImpactSegmentDTO>> getListImpactSegmentCBB() {
    List<ImpactSegmentDTO> data = crAlarmSettingBusiness.getListImpactSegmentCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListDeviceTypeByImpactSegmentCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListDeviceTypeByImpactSegmentCBB(
      Long impactSegmentId) {
    return new ResponseEntity<>(
        crManagerUnitsOfScopeBusiness.getListDeviceTypeByImpactSegmentCBB(impactSegmentId),
        HttpStatus.OK);
  }

  @PostMapping("/getListCrProcessCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListCrProcessCBB(
      @RequestBody CrProcessInsideDTO crProcessDTO) {
    return new ResponseEntity<>(crAlarmSettingBusiness.getListCrProcessCBB(crProcessDTO),
        HttpStatus.OK);
  }

  @PostMapping("/getListItemByCategory")
  public ResponseEntity<List<CatItemDTO>> getListItemByCategory(String categoryCode,
      String itemCode) {
    return new ResponseEntity<>(catItemBusiness.getListItemByCategory(categoryCode, itemCode),
        HttpStatus.OK);
  }

  @GetMapping("/getListGroupFaultSrc")
  public ResponseEntity<List<CrAlarmFaultGroupDTO>> getListGroupFaultSrc(String faultSrc,
      String nationCode) throws Exception {
    return new ResponseEntity<>(crAlarmSettingBusiness.getListGroupFaultSrc(faultSrc, nationCode),
        HttpStatus.OK);
  }

  @GetMapping("/getListFaultSrc")
  public ResponseEntity<HashSet<String>> getListFaultSrc(String nationCode) throws Exception {
    return new ResponseEntity<>(crAlarmSettingBusiness.getListFaultSrc(nationCode),
        HttpStatus.OK);
  }

  @PostMapping("/getVendorList")
  public ResponseEntity<Datatable> getVendorList(@RequestBody CrAlarmSettingDTO crAlarmSettingDTO)
      throws Exception {
    return new ResponseEntity<>(
        crAlarmSettingBusiness
            .getVendorList(crAlarmSettingDTO.getVendorCode(), crAlarmSettingDTO.getVendorName(),
                crAlarmSettingDTO.getPage(), crAlarmSettingDTO.getPageSize()),
        HttpStatus.OK);
  }

  @PostMapping("/getModuleList")
  public ResponseEntity<Datatable> getModuleList(@RequestBody CrAlarmSettingDTO crAlarmSettingDTO)
      throws Exception {
    return new ResponseEntity<>(
        crAlarmSettingBusiness
            .getModuleList(crAlarmSettingDTO.getServiceCode(), crAlarmSettingDTO.getModuleCode(),
                crAlarmSettingDTO.getNationCode(), crAlarmSettingDTO.getNationCode(),
                crAlarmSettingDTO.getPage(), crAlarmSettingDTO.getPageSize()),
        HttpStatus.OK);
  }

  @PostMapping("/findAlarmSettingByProcessId")
  public ResponseEntity<CrAlarmSettingDTO> findAlarmSettingByProcessId(
      @RequestBody CrAlarmSettingDTO crAlarmSettingDTO) {
    return new ResponseEntity<>(
        crAlarmSettingBusiness.findAlarmSettingByProcessId(crAlarmSettingDTO),
        HttpStatus.OK);
  }

  @SecurityAnnotation(role = ROLE.CR_ALARM_SETTING, permission = PERMISSION.EDIT)
  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_ALARM_SETTING)
  @PostMapping("/validateAlarmSetting")
  public ResponseEntity<ResultInSideDto> validateAlarmSetting(Long casId) {
    return new ResponseEntity<>(
        crAlarmSettingBusiness.validateAlarmSetting(casId),
        HttpStatus.OK);
  }

  @SecurityAnnotation(role = ROLE.CR_ALARM_SETTING, permission = PERMISSION.DELETE)
  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CR_ALARM_SETTING)
  @DeleteMapping("/deleteAlarmSetting")
  public ResponseEntity<ResultInSideDto> deleteAlarmSetting(Long casId) {
    return new ResponseEntity<>(crAlarmSettingBusiness.delete(casId), HttpStatus.OK);
  }

  @GetMapping("/getNationMap")
  public ResponseEntity<Map<String, String>> getNationMap() {
    return new ResponseEntity<>(crAlarmSettingBusiness.nationMap(), HttpStatus.OK);
  }

}
