package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CrManagerProcessBusiness;
import com.viettel.gnoc.cr.dto.CrOcsScheduleDTO;
import com.viettel.gnoc.cr.dto.CrProcessGroup;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KienPV
 */
@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "CrManagerProcess")
@Slf4j
public class CrManagerProcessController {

  @Autowired
  protected CrManagerProcessBusiness crManagerProcessBusiness;

  @Autowired
  protected CatItemBusiness catItemBusiness;

  @PostMapping("/getListSearchCrProcess")
  public ResponseEntity<Datatable> getListSearchCrProcess(
      @RequestBody CrProcessInsideDTO crProcessDTO) {
    Datatable datatable = crManagerProcessBusiness.getListSearchCrProcess(crProcessDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getListCrProcessCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListCrProcessCBB(
      @RequestBody CrProcessInsideDTO crProcessDTO) {
    List<ItemDataCRInside> result = crManagerProcessBusiness.getListCrProcessCBB(crProcessDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getRootCrProcess")
  public ResponseEntity<List<CrProcessInsideDTO>> getRootCrProces() {
    List<CrProcessInsideDTO> result = crManagerProcessBusiness.getRootCrProcess();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getLstFileFromProcessId")
  public ResponseEntity<List<CrProcessGroup>> getLstFileFromProcessId(
      @RequestBody CrProcessGroup crProcessGroup) {
    List<CrProcessGroup> result = crManagerProcessBusiness.getLstFileFromProcessId(crProcessGroup);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getLstUnitFromProcessId")
  public ResponseEntity<List<CrProcessGroup>> getLstUnitFromProcessId(Long crProcessId) {
    List<CrProcessGroup> result = crManagerProcessBusiness.getLstUnitFromProcessId(crProcessId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getLstAllChildrenByProcessId")
  public ResponseEntity<List<CrProcessInsideDTO>> getLstAllChildrenByProcessId(Long crProcessId) {
    List<CrProcessInsideDTO> result = crManagerProcessBusiness
        .getLstAllChildrenByProcessId(crProcessId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getParentByProcessId")
  public ResponseEntity<CrProcessInsideDTO> getParentByProcessId(Long crProcessId) {
    CrProcessInsideDTO result = crManagerProcessBusiness.getParentByProcessId(crProcessId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.PROCESS_MANAGEMENT)
  @DeleteMapping("/deleteGroupUnitOrFileByProcessId")
  public ResponseEntity<ResultInSideDto> deleteGroupUnitOrFileByProcessId(Long cmsorsId) {
    ResultInSideDto result = crManagerProcessBusiness.deleteGroupUnitOrFileByProcessId(cmsorsId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.PROCESS_MANAGEMENT)
  @DeleteMapping("/deleteAllChildByParent")
  public ResponseEntity<ResultInSideDto> deleteAllChildByParent(Long crProcessId) {
    ResultInSideDto result = crManagerProcessBusiness.deleteAllChildByParent(crProcessId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.PROCESS_MANAGEMENT)
  @DeleteMapping("/deleteFileAndDataWhenChangeProcess")
  public ResponseEntity<ResultInSideDto> deleteFileAndDataWhenChangeProcess(String crId) {
    ResultInSideDto result = crManagerProcessBusiness
        .deleteFileAndDataWhenChangeProcess(crId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.PROCESS_MANAGEMENT)
  @PutMapping("/saveCrProcessWo")
  public ResponseEntity<ResultInSideDto> saveCrProcessWo(
      @RequestBody @Valid CrProcessWoDTO crProcessWoDTO) {
    ResultInSideDto result = crManagerProcessBusiness.saveCrProcessWo(crProcessWoDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.PROCESS_MANAGEMENT)
  @DeleteMapping("/deleteListCrProcessWo")
  public ResponseEntity<ResultInSideDto> deleteListCrProcessWo(
      @RequestBody List<Long> lstCrProcessWoId) {
    ResultInSideDto result = crManagerProcessBusiness.deleteListCrProcessWo(lstCrProcessWoId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getLstWoFromProcessId")
  public ResponseEntity<List<CrProcessWoDTO>> getLstWoFromProcessId(Long crProcessId) {
    List<CrProcessWoDTO> result = crManagerProcessBusiness.getLstWoFromProcessId(crProcessId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListCrOcsScheduleDTO")
  public ResponseEntity<List<CrOcsScheduleDTO>> getListCrOcsScheduleDTO(
      @RequestBody CrOcsScheduleDTO crOcsScheduleDTO) {
    List<CrOcsScheduleDTO> result = crManagerProcessBusiness
        .getListCrOcsScheduleDTO(crOcsScheduleDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.PROCESS_MANAGEMENT)
  @PostMapping("/insertOrUpdateCrOcsScheduleDTO")
  public ResponseEntity<ResultInSideDto> insertOrUpdateCrOcsScheduleDTO(
      @RequestBody @Valid CrOcsScheduleDTO crOcsScheduleDTO) {
    ResultInSideDto result = crManagerProcessBusiness
        .insertOrUpdateCrOcsScheduleDTO(crOcsScheduleDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.PROCESS_MANAGEMENT)
  @DeleteMapping("/deleteListCrOcsScheduleDTO")
  public ResponseEntity<ResultInSideDto> deleteListCrOcsScheduleDTO(
      @RequestBody List<Long> crOcsScheduleDTOs) {
    ResultInSideDto result = crManagerProcessBusiness.deleteListCrOcsScheduleDTO(crOcsScheduleDTOs);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getCrProcessDetail")
  public ResponseEntity<CrProcessInsideDTO> getCrProcessDetail(Long crProcessId) {
    CrProcessInsideDTO result = crManagerProcessBusiness.getCrProcessDetail(crProcessId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getCrProcessById")
  public ResponseEntity<CrProcessInsideDTO> getCrProcessById(Long crProcessId) {
    CrProcessInsideDTO result = crManagerProcessBusiness.getCrProcessById(crProcessId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.PROCESS_MANAGEMENT)
  @PostMapping("/saveAllList")
  public ResponseEntity<ResultInSideDto> saveAllList(
      @RequestBody @Valid CrProcessInsideDTO crProcessDTO) {
    ResultInSideDto result = crManagerProcessBusiness.saveAllList(crProcessDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getcCrProcessWo")
  public ResponseEntity<CrProcessWoDTO> getcCrProcessWo(Long id) {
    CrProcessWoDTO result = crManagerProcessBusiness.getcCrProcessWo(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getListWoType")
  public ResponseEntity<List<WoTypeInsideDTO>> getListWoType() {
    List<WoTypeInsideDTO> result = crManagerProcessBusiness.getListWoType();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.PROCESS_MANAGEMENT)
  @PostMapping("/insertOrUpdateCrProcessDTO")
  public ResponseEntity<ResultInSideDto> insertOrUpdateCrProcessDTO(
      @RequestBody @Valid CrProcessInsideDTO crProcessDTO) {
    ResultInSideDto result = crManagerProcessBusiness.insertOrUpdateCrProcessDTO(crProcessDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/actionGetListProcessType")
  public ResponseEntity<List<CrProcessInsideDTO>> actionGetListProcessType(
      @RequestBody CrProcessInsideDTO crProcessDTO) {
    List<CrProcessInsideDTO> result = crManagerProcessBusiness
        .actionGetListProcessType(crProcessDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getLstFileTemplate")
  public ResponseEntity<Datatable> getLstFileTemplate(@RequestBody CrProcessGroup crProcessGroup) {
    Datatable result = crManagerProcessBusiness.getLstFileTemplate(crProcessGroup);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.PROCESS_MANAGEMENT)
  @DeleteMapping("/deleteListCrProcess")
  public ResponseEntity<ResultInSideDto> deleteListCrProcess(
      @RequestBody List<Long> lstCrProcessId) {
    ResultInSideDto result = crManagerProcessBusiness.deleteListCrProcess(lstCrProcessId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.PROCESS_MANAGEMENT)
  @DeleteMapping("/deleteCrProcess")
  public ResponseEntity<ResultInSideDto> deleteCrProcess(
      Long crProcessId) {
    ResultInSideDto result = crManagerProcessBusiness.deleteCrProcess(crProcessId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getCrProcessWoDTO")
  public ResponseEntity<CrProcessWoDTO> getCrProcessWoDTO(
      Long crProcessWoId) {
    CrProcessWoDTO result = crManagerProcessBusiness.getCrProcessWoDTO(crProcessWoId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/generateCrProcessCode")
  public ResponseEntity<CrProcessInsideDTO> generateCrProcessCode(
      @RequestBody CrProcessInsideDTO crProcessDTO) {
    return new ResponseEntity<>(crManagerProcessBusiness.generateCrProcessCode(crProcessDTO),
        HttpStatus.OK);
  }

  @PostMapping("/getListCrProcessDTO")
  public ResponseEntity<List<CrProcessInsideDTO>> getListCrProcessDTO(
      @RequestBody CrProcessInsideDTO crProcessDTO) {
    List<CrProcessInsideDTO> lstResult = crManagerProcessBusiness
        .getListCrProcessDTO(crProcessDTO, 0, 0, "", "");
    return new ResponseEntity<>(lstResult, HttpStatus.OK);
  }

  @GetMapping("/getAllCrProcess{parentId}")
  public ResponseEntity<List<CrProcessInsideDTO>> getAllCrProcess(
      @PathVariable("parentId") Long parentId) {
    List<CrProcessInsideDTO> lstResult = crManagerProcessBusiness.getAllCrProcess(parentId);
    return new ResponseEntity<>(lstResult, HttpStatus.OK);
  }
}
