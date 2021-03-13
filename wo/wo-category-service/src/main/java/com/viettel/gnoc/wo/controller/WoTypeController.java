package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.WoPriorityBusiness;
import com.viettel.gnoc.wo.business.WoTypeBusiness;
import com.viettel.gnoc.wo.business.WoTypeCfgRequiredBusiness;
import com.viettel.gnoc.wo.business.WoTypeCheckListBusiness;
import com.viettel.gnoc.wo.dto.WoChecklistDetailDTO;
import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
import com.viettel.gnoc.wo.dto.WoTypeCheckListDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeTimeDTO;
import java.io.IOException;
import java.util.ArrayList;
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

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "woType")
@Slf4j
public class WoTypeController {

  @Autowired
  protected WoTypeBusiness woTypeBusiness;

  @Autowired
  protected CatItemBusiness catItemBusiness;

  @Autowired
  protected WoPriorityBusiness woPriorityBusiness;

  @Autowired
  protected WoTypeCheckListBusiness woTypeCheckListBusiness;

  @Autowired
  protected WoTypeCfgRequiredBusiness woTypeCfgRequiredBusiness;

  @PostMapping("/getListWoTypeByLocalePage")
  public ResponseEntity<Datatable> getListWoTypeByLocalePage(
      @RequestBody WoTypeInsideDTO woTypeInsideDTO) {
    Datatable data = woTypeBusiness.getListWoTypeByLocalePage(woTypeInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long woTypeId) {
    ResultInSideDto data = woTypeBusiness.delete(woTypeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/insert", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insert(
      @RequestPart("filesGuideline") List<MultipartFile> filesGuideline,
      @RequestPart("filesAttached") List<MultipartFile> filesAttached,
      @Valid @RequestPart("formDataJson") WoTypeInsideDTO woTypeInsideDTO) throws IOException {
    ResultInSideDto data = woTypeBusiness.insert(filesGuideline, filesAttached, woTypeInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> update(
      @RequestPart("filesGuideline") List<MultipartFile> filesGuideline,
      @RequestPart("filesAttached") List<MultipartFile> filesAttached,
      @Valid @RequestPart("formDataJson") WoTypeInsideDTO woTypeInsideDTO) throws IOException {
    ResultInSideDto data = woTypeBusiness.update(filesGuideline, filesAttached, woTypeInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //WO_GROUP_TYPE
  @PostMapping("/getItemWoTypeGroup")
  public ResponseEntity<List<CatItemDTO>> getItemWoTypeGroup(String categoryCode, String itemCode) {
    List<CatItemDTO> data = catItemBusiness.getListItemByCategory(categoryCode, itemCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findByWoTypeId")
  public ResponseEntity<WoTypeInsideDTO> findByWoTypeId(Long woTypeId) {
    WoTypeInsideDTO data = woTypeBusiness.findByWoTypeId(woTypeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //categoryCode CFG_REQUIRED_WO_TYPE
  @PostMapping("/getListRequiredInfo")
  public ResponseEntity<List<CatItemDTO>> getListRequiredInfo(String categoryCode,
      String itemCode) {
    List<CatItemDTO> data = catItemBusiness.getListItemByCategory(categoryCode, itemCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //categoryCode CFG_REQUIRED_WO_TYPE
  //categoryCode WO_PRIORITY_CODE
  @PostMapping("/getItemMaster")
  public ResponseEntity<Datatable> getItemMaster(String categoryCode, String system,
      String type,
      String idColName, String nameCol) {
    Datatable data = catItemBusiness.getItemMaster(categoryCode, system, type, idColName, nameCol);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deletePriority")
  public ResponseEntity<ResultInSideDto> deletePriority(Long priorityId) {
    ResultInSideDto data = woPriorityBusiness.delete(priorityId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertListPriority")
  public ResponseEntity<ResultInSideDto> insertPriority(
      @RequestBody WoTypeInsideDTO woTypeInsideDTO) {
    ResultInSideDto data = woPriorityBusiness.insertListPriority(woTypeInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteWoTypeCheckList")
  public ResponseEntity<ResultInSideDto> deleteWoTypeCheckList(Long woTypeChecklistId) {
    ResultInSideDto data = woTypeCheckListBusiness.delete(woTypeChecklistId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertWoTypeCheckList")
  public ResponseEntity<ResultInSideDto> insertWoTypeCheckList(
      @RequestBody WoTypeCheckListDTO woTypeCheckListDTO) {
    ResultInSideDto data = woTypeCheckListBusiness.add(woTypeCheckListDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertCfgRequired")
  public ResponseEntity<ResultInSideDto> insertCfgRequired(
      @RequestBody WoTypeCfgRequiredDTO woTypeCfgRequiredDTO) {
    ResultInSideDto data = woTypeCfgRequiredBusiness.add(woTypeCfgRequiredDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWoTypeChecklistDTO")
  public ResponseEntity<Datatable> getListWoTypeChecklistDTO(
      @RequestBody WoTypeCheckListDTO woTypeCheckListDTO) {
    Datatable data = woTypeCheckListBusiness.getListWoTypeChecklistDTO(woTypeCheckListDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWoChecklistDetailDTO")
  public ResponseEntity<List<WoChecklistDetailDTO>> getListWoChecklistDetailDTO(
      @RequestBody WoChecklistDetailDTO woChecklistDetailDTO) {
    List<WoChecklistDetailDTO> list = woTypeCheckListBusiness
        .getListWoChecklistDetailDTO(woChecklistDetailDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/updateWoChecklistDetail")
  public ResponseEntity<ResultInSideDto> updateWoChecklistDetail(
      @RequestBody List<WoChecklistDetailDTO> listWoChecklistDetailDTO) {
    ResultInSideDto data = woTypeCheckListBusiness
        .updateWoChecklistDetail(listWoChecklistDetailDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findWoTypeById/woTypeId{woTypeId}")
  public WoTypeInsideDTO findWoTypeById(@PathVariable Long woTypeId) {
    WoTypeInsideDTO woTypeInsideDTO = woTypeBusiness.findWoTypeById(woTypeId);
    return woTypeInsideDTO;
  }

  @PostMapping("/getListWoTypeTimeDTO")
  public ResponseEntity<List<WoTypeTimeDTO>> getListWoTypeTimeDTO(
      @RequestBody WoTypeTimeDTO woTypeTimeDTO) {
    List<WoTypeTimeDTO> list = woTypeBusiness.getListWoTypeTimeDTO(woTypeTimeDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListWoTypeDTO")
  public List<WoTypeInsideDTO> getListWoTypeDTO(
      @RequestBody WoTypeInsideDTO woTypeInsideDTO) {
    List<WoTypeInsideDTO> lst = woTypeBusiness.getListWoTypeIsEnable(woTypeInsideDTO);
    return lst;
  }

  @PostMapping("/getListWoTypeByLocaleNotLike")
  public ResponseEntity<List<WoTypeInsideDTO>> getListWoTypeByLocaleNotLike(
      @RequestBody WoTypeInsideDTO woTypeInsideDTO) {
    List<WoTypeInsideDTO> lst = woTypeBusiness.getListWoTypeByLocaleNotLike(woTypeInsideDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/getListWoTypeCfgRequiredByWoTypeId")
  public ResponseEntity<List<WoTypeCfgRequiredDTO>> getListWoTypeCfgRequiredByWoTypeId(
      @RequestBody WoTypeCfgRequiredDTO woTypeCfgRequiredDTO) {
    List<WoTypeCfgRequiredDTO> lst = new ArrayList<>();
    if (woTypeCfgRequiredDTO != null) {
      lst = woTypeCfgRequiredBusiness
          .getListWoTypeCfgRequiredByWoTypeId(woTypeCfgRequiredDTO.getWoTypeId(),
              woTypeCfgRequiredDTO.getCfgCode());
    }
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @GetMapping("/getWoTypeByCode/woTypeCode{woTypeCode}")
  public ResponseEntity<WoTypeInsideDTO> getWoTypeByCode(
      @PathVariable("woTypeCode") String woTypeCode) {
    return new ResponseEntity<>(woTypeBusiness.getWoTypeByCode(woTypeCode), HttpStatus.OK);
  }

}
