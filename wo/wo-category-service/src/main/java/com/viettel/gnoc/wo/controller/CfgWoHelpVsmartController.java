package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.wo.business.CfgWoHelpVsmartBusiness;
import com.viettel.gnoc.wo.dto.CfgWoHelpVsmartDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "CfgWoHelpVsmart")
@Slf4j
public class CfgWoHelpVsmartController {

  @Autowired
  CfgWoHelpVsmartBusiness cfgWoHelpVsmartBusiness;

  @RequestMapping(value = "/insertCfgWoHelpVsmart", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertCfgWoHelpVsmart(
      @RequestPart("files") List<MultipartFile> files,
      @Valid @RequestPart("formDataJson") CfgWoHelpVsmartDTO configCfgWoHelpVsmartDTO)
      throws Exception {
    ResultInSideDto result = cfgWoHelpVsmartBusiness
        .insertCfgWoHelpVsmart(files, configCfgWoHelpVsmartDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/updateCfgWoHelpVsmart", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> updateCfgWoHelpVsmart(
      @RequestPart("files") List<MultipartFile> files,
      @Valid @RequestPart("formDataJson") CfgWoHelpVsmartDTO configCfgWoHelpVsmartDTO)
      throws Exception {
    ResultInSideDto result = cfgWoHelpVsmartBusiness
        .updateCfgWoHelpVsmart(files, configCfgWoHelpVsmartDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/deleteCfgWoHelpVsmart")
  public ResponseEntity<ResultInSideDto> deleteCfgWoHelpVsmart(@RequestParam Long Id) {
    ResultInSideDto data = cfgWoHelpVsmartBusiness.deleteCfgWoHelpVsmart(Id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findCfgWoHelpVsmartsById")
  public ResponseEntity<CfgWoHelpVsmartDTO> findCfgWoHelpVsmartsById(@RequestParam Long Id) {
    CfgWoHelpVsmartDTO data = cfgWoHelpVsmartBusiness.findCfgWoHelpVsmartsById(Id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/deleteListCfgWoHelpVsmart")
  public ResponseEntity<ResultInSideDto> deleteListCfgWoHelpVsmart(
      @RequestBody List<CfgWoHelpVsmartDTO> cfgWoHelpVsmartDTOS) {
    ResultInSideDto data = cfgWoHelpVsmartBusiness.deleteListCfgWoHelpVsmart(cfgWoHelpVsmartDTOS);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCfgWoHelpVsmartDTOSearchWeb")
  public ResponseEntity<Datatable> getListCfgWoHelpVsmartDTOSearchWeb(
      @RequestBody CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO) {
    Datatable data = cfgWoHelpVsmartBusiness.getListCfgWoHelpVsmartDTOSearchWeb(cfgWoHelpVsmartDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCbbSystem")
  public ResponseEntity<List<CatItemDTO>> getListCbbSystem() {
    List<CatItemDTO> data = cfgWoHelpVsmartBusiness.getListCbbSystem();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDataHeader/systemId{systemId}/typeId{typeId}")
  public ResponseEntity<List<ObjKeyValueVsmartDTO>> getDataHeader(@PathVariable Long systemId,
      @PathVariable String typeId) {
    List<ObjKeyValueVsmartDTO> data = cfgWoHelpVsmartBusiness.getDataHeader(systemId, typeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
