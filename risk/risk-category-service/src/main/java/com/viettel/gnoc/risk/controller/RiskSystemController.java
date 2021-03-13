package com.viettel.gnoc.risk.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.risk.business.RiskSystemBusiness;
import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import com.viettel.gnoc.risk.dto.RiskSystemHistoryDTO;
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

@Slf4j
@RestController
@RequestMapping(Constants.RISK_API_PATH_PREFIX + "RiskSystem")
public class RiskSystemController {

  @Autowired
  RiskSystemBusiness riskSystemBusiness;

  @PostMapping("/getDataRiskSystemSearchWeb")
  public ResponseEntity<Datatable> getDataRiskSystemSearchWeb(
      @RequestBody RiskSystemDTO riskSystemDTO) {
    Datatable data = riskSystemBusiness.getDataRiskSystemSearchWeb(riskSystemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListRiskSystem")
  public List<RiskSystemDTO> getListRiskSystem(@RequestBody RiskSystemDTO riskSystemDTO) {
    return riskSystemBusiness.getListRiskSystem(riskSystemDTO);
  }

  @RequestMapping(value = "/insertRiskSystemWeb", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertRiskSystemWeb(
      @RequestPart("fileAttacks") List<MultipartFile> fileAttacks,
      @Valid @RequestPart("formDataJson") RiskSystemDTO riskSystemDTO) throws Exception {
    ResultInSideDto result = riskSystemBusiness.insertRiskSystemWeb(fileAttacks, riskSystemDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/updateRiskSystemWeb", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> updateRiskSystemWeb(
      @RequestPart("fileAttacks") List<MultipartFile> fileAttacks,
      @Valid @RequestPart("formDataJson") RiskSystemDTO riskSystemDTO) throws Exception {
    ResultInSideDto result = riskSystemBusiness.updateRiskSystemWeb(fileAttacks, riskSystemDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/deleteRiskSystem")
  public ResponseEntity<ResultInSideDto> deleteRiskSystem(Long id) {
    ResultInSideDto result = riskSystemBusiness.deleteRiskSystem(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/findRiskSystemByIdFromWeb")
  public ResponseEntity<RiskSystemDTO> findRiskSystemByIdFromWeb(Long id) {
    RiskSystemDTO riskSystemDTO = riskSystemBusiness.findRiskSystemByIdFromWeb(id);
    return new ResponseEntity<>(riskSystemDTO, HttpStatus.OK);
  }

  @GetMapping("/getListFileFromRiskSystem")
  public ResponseEntity<List<GnocFileDto>> getListFileFromRiskSystem(Long systemId) {
    List<GnocFileDto> list = riskSystemBusiness.getListFileFromRiskSystem(systemId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListRiskSystemHistoryBySystemId")
  public ResponseEntity<Datatable> getListRiskSystemHistoryBySystemId(
      @RequestBody RiskSystemHistoryDTO riskSystemHistoryDTO) {
    Datatable data = riskSystemBusiness.getListRiskSystemHistoryBySystemId(riskSystemHistoryDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportDataRiskSystem")
  public ResponseEntity<File> exportDataRiskSystem(@RequestBody RiskSystemDTO riskSystemDTO)
      throws Exception {
    File data = riskSystemBusiness.exportDataRiskSystem(riskSystemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getTemplateImport")
  public ResponseEntity<File> getTemplateImport() throws Exception {
    File data = riskSystemBusiness.getTemplateImport();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/importDataRiskSystem", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> importDataRiskSystem(
      @RequestPart("fileImport") MultipartFile fileImport) {
    ResultInSideDto result = riskSystemBusiness.importDataRiskSystem(fileImport);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
