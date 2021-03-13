package com.viettel.gnoc.od.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.od.business.OdTypeBusiness;
import com.viettel.gnoc.od.business.OdTypeDetailBusiness;
import com.viettel.gnoc.od.business.OdTypeMapLocationBusiness;
import com.viettel.gnoc.od.dto.OdDTOSearch;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.od.dto.OdTypeDetailDTO;
import com.viettel.gnoc.od.dto.OdTypeMapLocationDTO;
import java.io.File;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author NamTN
 */
@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "odType")
@Slf4j
public class OdTypeController {

  @Autowired
  OdTypeBusiness odTypeBusiness;
  @Autowired
  CatItemBusiness catItemBusiness;
  @Autowired
  OdTypeDetailBusiness odTypeDetailBusiness;

  @Autowired
  OdTypeMapLocationBusiness odTypeMapLocationBusiness;

  @PostMapping("/search")
  public ResponseEntity<Datatable> search(@RequestBody OdTypeDTO odTypeDTO) {
    Datatable data = odTypeBusiness.search(odTypeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListOdType")
  public ResponseEntity<Datatable> getListOdType(@RequestBody OdTypeDTO odTypeDTO) {
    Datatable data = odTypeBusiness.getListOdType(odTypeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportData")
  public ResponseEntity<File> exportData(@RequestBody OdTypeDTO odTypeDTO) throws Exception {
    File data = odTypeBusiness.exportData(odTypeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getTemplate")
  public ResponseEntity<File> getTemplate() throws Exception {
    File data = odTypeBusiness.getTemplate();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/importData")
  public ResponseEntity<ResultInSideDto> importData(@RequestParam("file") MultipartFile upLoadFile)
      throws Exception {
    ResultInSideDto resultInSideDto = odTypeBusiness.importData(upLoadFile);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long odTypeId) {
    ResultInSideDto result = odTypeBusiness.delete(odTypeId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/deleteList")
  public ResponseEntity<ResultInSideDto> deleteList(@RequestBody OdTypeDTO odTypeDTO) {
    ResultInSideDto result = odTypeBusiness.deleteList(odTypeDTO.getListId());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<OdTypeDTO> getDetail(Long odTypeId) {
    OdTypeDTO data = odTypeBusiness.getDetail(odTypeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getInforByODType/odTypeCode{odTypeCode}")
  public ResponseEntity<OdTypeDTO> getInforByODType(@PathVariable String odTypeCode) {
    OdTypeDTO data = odTypeBusiness.getInforByODType(odTypeCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<ResultInSideDto> add(@Valid @RequestBody OdTypeDTO odTypeDTO) {
    ResultInSideDto data = odTypeBusiness.add(odTypeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/edit")
  public ResponseEntity<ResultInSideDto> edit(@Valid @RequestBody OdTypeDTO odTypeDTO) {
    ResultInSideDto data = odTypeBusiness.edit(odTypeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListOdTypeMapByOdTypeIdAndLocation")
  public OdTypeMapLocationDTO getListOdTypeMapByOdTypeIdAndLocation(
      @RequestBody OdDTOSearch odDTOSearch) {
    return odTypeMapLocationBusiness.getListOdTypeMapByOdTypeIdAndLocation(
        Long.valueOf(odDTOSearch.getOdTypeId()), odDTOSearch.getLocationCode());
  }

  @PostMapping("/getItemMaster")
  public ResponseEntity<Datatable> getItemMaster(String categoryCode, String system,
      String type,
      String idColName, String nameCol) {
    Datatable data = catItemBusiness.getItemMaster(categoryCode, system, type, idColName, nameCol);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getSeqOdType")
  public ResponseEntity<String> getSeqOdType() {
    log.info("Request to getSeqOdType controller : {}");
    String data = odTypeBusiness.getSeqOdType();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListOdTypeDetail")
  public List<OdTypeDetailDTO> getListOdTypeDetail(@RequestBody OdTypeDetailDTO odTypeDetailDTO) {
    return (List<OdTypeDetailDTO>) odTypeDetailBusiness.getListOdTypeDetail(odTypeDetailDTO)
        .getData();
  }
}
