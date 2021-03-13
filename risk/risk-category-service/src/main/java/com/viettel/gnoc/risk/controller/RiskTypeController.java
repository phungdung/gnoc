package com.viettel.gnoc.risk.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.risk.business.RiskTypeBusiness;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
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
@RequestMapping(Constants.RISK_API_PATH_PREFIX + "RiskType")
public class RiskTypeController {

  @Autowired
  RiskTypeBusiness riskTypeBusiness;

  @PostMapping("/getDataRiskTypeSearchWeb")
  public ResponseEntity<Datatable> getDataRiskTypeSearchWeb(@RequestBody RiskTypeDTO riskTypeDTO) {
    Datatable data = riskTypeBusiness.getDataRiskTypeSearchWeb(riskTypeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListRiskTypeDTO")
  public List<RiskTypeDTO> getListRiskTypeDTO(@RequestBody RiskTypeDTO riskTypeDTO) {
    return riskTypeBusiness.getListRiskTypeDTO(riskTypeDTO);
  }

  @PostMapping("/getListRiskTypeDetail")
  public List<RiskTypeDetailDTO> getListRiskTypeDetail(
      @RequestBody RiskTypeDetailDTO riskTypeDetailDTO) {
    return riskTypeBusiness.getListRiskTypeDetail(riskTypeDetailDTO);
  }

  @GetMapping("/findRiskTypeByIdFromWeb")
  public ResponseEntity<RiskTypeDTO> findRiskTypeByIdFromWeb(Long riskTypeId) {
    RiskTypeDTO riskTypeDTO = riskTypeBusiness.findRiskTypeByIdFromWeb(riskTypeId);
    return new ResponseEntity<>(riskTypeDTO, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateRiskType")
  public ResponseEntity<ResultInSideDto> insertOrUpdateRiskType(
      @RequestBody @Valid RiskTypeDTO riskTypeDTO) {
    ResultInSideDto resultInSideDto = riskTypeBusiness.insertOrUpdateRiskType(riskTypeDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/deleteRiskType")
  public ResponseEntity<ResultInSideDto> deleteRiskType(Long riskTypeId) {
    ResultInSideDto resultInSideDto = riskTypeBusiness.delete(riskTypeId);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/exportDataRiskType")
  public ResponseEntity<File> exportDataRiskType(@RequestBody RiskTypeDTO riskTypeDTO)
      throws Exception {
    File data = riskTypeBusiness.exportDataRiskType(riskTypeDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getTemplateImport")
  public ResponseEntity<File> getTemplateImport() throws Exception {
    File data = riskTypeBusiness.getTemplateImport();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/importDataRiskType", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> importDataRiskType(
      @RequestPart("fileImport") MultipartFile fileImport) {
    ResultInSideDto result = riskTypeBusiness.importDataRiskType(fileImport);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
