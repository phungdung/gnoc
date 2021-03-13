package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.WoCdGroupBusiness;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import com.viettel.gnoc.wo.dto.WoTypeGroupDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.io.File;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(Constants.WO_API_PATH_PREFIX + "WoCdGroup")
public class WoCdGroupController {

  @Autowired
  WoCdGroupBusiness woCdGroupBusiness;

  @PostMapping("/getListWoCdGroupDTO")
  public ResponseEntity<Datatable> getListWoCdGroupDTO(
      @RequestBody WoCdGroupInsideDTO woCdGroupInsideDTO) {
    Datatable data = woCdGroupBusiness.getListWoCdGroupDTO(woCdGroupInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findWoCdGroupById/woGroupId{woGroupId}")
  public WoCdGroupInsideDTO findWoCdGroupById(@PathVariable Long woGroupId) {
    WoCdGroupInsideDTO woCdGroupInsideDTO = woCdGroupBusiness.findWoCdGroupById(woGroupId);
    return woCdGroupInsideDTO;
  }

  @PostMapping("/findWoCdGroupById")
  public ResponseEntity<WoCdGroupInsideDTO> findWoCdGroupByWoGroupId(Long woGroupId) {
    WoCdGroupInsideDTO woCdGroupInsideDTO = woCdGroupBusiness.findWoCdGroupById(woGroupId);
    return new ResponseEntity<>(woCdGroupInsideDTO, HttpStatus.OK);
  }

  @PostMapping("/insertWoCdGroup")
  public ResponseEntity<ResultInSideDto> insertWoCdGroup(
      @Valid @RequestBody WoCdGroupInsideDTO woCdGroupInsideDTO) {
    ResultInSideDto data = woCdGroupBusiness.insertWoCdGroup(woCdGroupInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateWoCdGroup")
  public ResponseEntity<ResultInSideDto> updateWoCdGroup(
      @Valid @RequestBody WoCdGroupInsideDTO woCdGroupInsideDTO) {
    ResultInSideDto data = woCdGroupBusiness.updateWoCdGroup(woCdGroupInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteWoCdGroup")
  public ResponseEntity<ResultInSideDto> deleteWoCdGroup(Long woGroupId) {
    ResultInSideDto result = woCdGroupBusiness.deleteWoCdGroup(woGroupId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListCdGroupByUser")
  public ResponseEntity<List<WoCdGroupInsideDTO>> getListCdGroupByUser(
      @RequestBody WoCdGroupTypeUserDTO woCdGroupTypeUserDTO) {
    List<WoCdGroupInsideDTO> list = woCdGroupBusiness.getListCdGroupByUser(woCdGroupTypeUserDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListWoCdGroupUnitDTO")
  public ResponseEntity<List<ReceiveUnitDTO>> getListWoCdGroupUnitDTO(
      @RequestBody WoCdGroupUnitDTO woCdGroupUnitDTO) {
    List<ReceiveUnitDTO> list = woCdGroupBusiness.getListWoCdGroupUnitDTO(woCdGroupUnitDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/updateWoCdGroupUnit")
  public ResponseEntity<ResultInSideDto> updateWoCdGroupUnit(
      @RequestBody WoCdGroupUnitDTO woCdGroupUnitDTO) {
    ResultInSideDto data = woCdGroupBusiness.updateWoCdGroupUnit(woCdGroupUnitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWoCdDTO")
  public ResponseEntity<List<UsersInsideDto>> getListWoCdDTO(@RequestBody WoCdDTO woCdDTO) {
    List<UsersInsideDto> list = woCdGroupBusiness.getListWoCdDTO(woCdDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/updateWoCd")
  public ResponseEntity<ResultInSideDto> updateWoCd(@RequestBody WoCdDTO woCdDTO) {
    ResultInSideDto data = woCdGroupBusiness.updateWoCd(woCdDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWoTypeAll")
  public ResponseEntity<List<WoTypeInsideDTO>> getListWoTypeAll() {
    List<WoTypeInsideDTO> list = woCdGroupBusiness.getListWoTypeAll();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListWoTypeGroupDTO")
  public ResponseEntity<List<WoTypeInsideDTO>> getListWoTypeGroupDTO(
      @RequestBody WoTypeGroupDTO woTypeGroupDTO) {
    List<WoTypeInsideDTO> list = woCdGroupBusiness.getListWoTypeGroupDTO(woTypeGroupDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/updateWoTypeGroup")
  public ResponseEntity<ResultInSideDto> updateWoTypeGroup(
      @RequestBody WoTypeGroupDTO woTypeGroupDTO) {
    ResultInSideDto data = woCdGroupBusiness.updateWoTypeGroup(woTypeGroupDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportDataWoCdGroup")
  public ResponseEntity<File> exportDataWoCdGroup(
      @RequestBody WoCdGroupInsideDTO woCdGroupInsideDTO)
      throws Exception {
    File data = woCdGroupBusiness.exportData(woCdGroupInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportDataWoCd")
  public ResponseEntity<File> exportDataWoCd(@RequestBody WoCdDTO woCdDTO)
      throws Exception {
    File data = woCdGroupBusiness.exportDataWoCd(woCdDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getTemplateImport")
  public ResponseEntity<File> getTemplateImport() throws Exception {
    File data = woCdGroupBusiness.getTemplateImport();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getTemplateAssignUser")
  public ResponseEntity<File> getTemplateAssignUser() throws Exception {
    File data = woCdGroupBusiness.getTemplateAssignUser();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getTemplateAssignTypeGroup")
  public ResponseEntity<File> getTemplateAssignTypeGroup() throws Exception {
    File data = woCdGroupBusiness.getTemplateAssignTypeGroup();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/importDataWoCdGroup", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> importDataWoCdGroup(
      @RequestPart("fileImport") MultipartFile fileImport) {
    ResultInSideDto result = woCdGroupBusiness.importDataWoCdGroup(fileImport);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/importDataAssignUser", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> importDataAssignUser(
      @RequestPart("fileImport") MultipartFile fileImport) {
    ResultInSideDto result = woCdGroupBusiness.importDataAssignUser(fileImport);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/importDataAssignTypeGroup", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> importDataAssignTypeGroup(
      @RequestPart("fileImport") MultipartFile fileImport) {
    ResultInSideDto result = woCdGroupBusiness.importDataAssignTypeGroup(fileImport);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/updateStatusCdGroup")
  public ResponseEntity<ResultInSideDto> updateStatusCdGroup(
      @RequestBody WoCdGroupInsideDTO woCdGroupInsideDTO) {
    ResultInSideDto data = woCdGroupBusiness.updateStatusCdGroup(woCdGroupInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCdGroup")
  public List<WoCdGroupInsideDTO> getListCdGroup(String userName) {
    return woCdGroupBusiness.getListCdGroup(userName);
  }

  @PostMapping("/getListCdGroupByUserProxy")
  public List<WoCdGroupInsideDTO> getListCdGroupByUserProxy(
      @RequestBody WoCdGroupTypeUserDTO woCdGroupTypeUserDTO) {
    return woCdGroupBusiness.getListCdGroupByUser(woCdGroupTypeUserDTO);
  }

}
