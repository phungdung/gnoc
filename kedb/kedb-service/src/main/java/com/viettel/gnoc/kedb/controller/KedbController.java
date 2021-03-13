package com.viettel.gnoc.kedb.controller;

import com.viettel.gnoc.commons.business.DeviceTypeVersionBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.kedb.business.KedbBusiness;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import java.io.File;
import java.util.ArrayList;
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
@RequestMapping(Constants.PT_API_PATH_PREFIX + "KedbService")
@Slf4j
public class KedbController {

  @Autowired
  KedbBusiness kedbBusiness;

  @Autowired
  DeviceTypeVersionBusiness deviceTypeVersionBusiness;

  @PostMapping("/getListKedbDTO")
  public ResponseEntity<Datatable> getListKedbDTO(@RequestBody KedbDTO kedbDTO) {
    Datatable data = kedbBusiness.getListKedbDTO(kedbDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findKedbById")
  public ResponseEntity<KedbDTO> findKedbById(Long kedbId) {
    KedbDTO data = kedbBusiness.findKedbById(kedbId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/insertKedb", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertKedb(@RequestPart("files") List<MultipartFile> files,
      @Valid @RequestPart("formDataJson") KedbDTO kedbDTO) throws Exception {
    ResultInSideDto result = kedbBusiness.insertKedb(files, kedbDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/updateKedb", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> updateKedb(@RequestPart("files") List<MultipartFile> files,
      @Valid @RequestPart("formDataJson") KedbDTO kedbDTO) throws Exception {
    ResultInSideDto result = kedbBusiness.updateKedb(files, kedbDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/deleteKedb")
  public ResponseEntity<ResultInSideDto> deleteKedb(Long kedbId) {
    ResultInSideDto result = kedbBusiness.deleteKedb(kedbId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/deleteListKedb")
  public ResponseEntity<ResultInSideDto> deleteListKedb(@RequestBody List<KedbDTO> listKedbDTO) {
    List<Long> listId = new ArrayList<>();
    for (KedbDTO dto : listKedbDTO) {
      listId.add(dto.getKedbId());
    }
    ResultInSideDto result = kedbBusiness.deleteListKedb(listId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getSequenseKedb")
  public ResponseEntity<List<String>> getSequenseKedb() {
    List<String> data = kedbBusiness.getSequenseKedb();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListKedb")
  public ResponseEntity<ResultInSideDto> insertOrUpdateListKedb(
      @RequestBody List<KedbDTO> listKedbDTO) {
    ResultInSideDto result = kedbBusiness.insertOrUpdateListKedb(listKedbDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getOffset")
  public ResponseEntity<String> getOffset(@RequestBody UserTokenGNOCSimple userTokenGNOC) {
    String result = kedbBusiness.getOffset(userTokenGNOC);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/synchKedbByCreateTime")
  public ResponseEntity<List<KedbDTO>> synchKedbByCreateTime(String fromDate, String toDate) {
    List<KedbDTO> list = kedbBusiness.synchKedbByCreateTime(fromDate, toDate);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListKedbByCondition")
  public ResponseEntity<List<KedbDTO>> getListKedbByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    List<KedbDTO> data = kedbBusiness.getListKedbByCondition(lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportDataKedb")
  public ResponseEntity<File> exportDataKedb(@RequestBody KedbDTO kedbDTO) throws Exception {
    File data = kedbBusiness.exportData(kedbDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSubCategory")
  public ResponseEntity<List<CatItemDTO>> getListSubCategory(Long typeId) {
    List<CatItemDTO> data = kedbBusiness.getListSubCategory(typeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getTemplate")
  public ResponseEntity<File> getTemplate() throws Exception {
    File data = kedbBusiness.getTemplate();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/importDataKedb", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> importDataKedb(
      @RequestPart("fileImport") MultipartFile fileImport,
      @RequestPart("files") List<MultipartFile> files) {
    ResultInSideDto result = kedbBusiness.importData(fileImport, files);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListUnitCheckKedb")
  public ResponseEntity<List<UnitDTO>> getListUnitCheckKedb(@RequestBody KedbDTO kedbDTO) {
    List<UnitDTO> data = kedbBusiness.getListUnitCheckKedb(kedbDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
