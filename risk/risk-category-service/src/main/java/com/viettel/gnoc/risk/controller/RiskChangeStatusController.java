package com.viettel.gnoc.risk.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.risk.business.RiskChangeStatusBusiness;
import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusRoleDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import java.io.File;
import java.io.IOException;
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
@RequestMapping(Constants.RISK_API_PATH_PREFIX + "RiskChangeStatus")
public class RiskChangeStatusController {

  @Autowired
  RiskChangeStatusBusiness riskChangeStatusBusiness;

  @PostMapping("/getDataRiskChangeStatusSearchWeb")
  public ResponseEntity<Datatable> getDataRiskChangeStatusSearchWeb(
      @RequestBody RiskChangeStatusDTO riskChangeStatusDTO) {
    Datatable data = riskChangeStatusBusiness.getDataRiskChangeStatusSearchWeb(riskChangeStatusDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListRiskChangeStatusDTO")
  public List<RiskChangeStatusDTO> getListRiskChangeStatusDTO(
      @RequestBody RiskChangeStatusDTO riskChangeStatusDTO) {
    return riskChangeStatusBusiness.getListRiskChangeStatusDTO(riskChangeStatusDTO);
  }

  @PostMapping("/getListRiskCfgBusinessDTO")
  public List<RiskCfgBusinessDTO> getListRiskCfgBusinessDTO(
      @RequestBody RiskCfgBusinessDTO riskCfgBusinessDTO) {
    return riskChangeStatusBusiness.getListRiskCfgBusinessDTO(riskCfgBusinessDTO);
  }

  @PostMapping("/getListRiskChangeStatusRoleDTO")
  public List<RiskChangeStatusRoleDTO> getListRiskChangeStatusRoleDTO(
      @RequestBody RiskChangeStatusRoleDTO riskChangeStatusRoleDTO) {
    return riskChangeStatusBusiness.getListRiskChangeStatusRoleDTO(riskChangeStatusRoleDTO);
  }

  @RequestMapping(value = "/insertOrUpdateRiskChangeStatus", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertOrUpdateRiskChangeStatus(
      @Valid @RequestPart("formDataJson") RiskChangeStatusDTO riskChangeStatusDTO,
      @RequestPart("lstMultipartFile") List<MultipartFile> lstMultipartFile) throws IOException {
    ResultInSideDto resultInSideDto = riskChangeStatusBusiness
        .insertOrUpdateRiskChangeStatus(riskChangeStatusDTO, lstMultipartFile);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/findRiskChangeStatusByIdFromWeb")
  public ResponseEntity<RiskChangeStatusDTO> findRiskChangeStatusByIdFromWeb(Long id) {
    RiskChangeStatusDTO riskChangeStatusDTO = riskChangeStatusBusiness
        .findRiskChangeStatusByIdFromWeb(id);
    return new ResponseEntity<>(riskChangeStatusDTO, HttpStatus.OK);
  }

  @PostMapping("/exportDataRiskChangeStatus")
  public ResponseEntity<File> exportDataRiskChangeStatus(
      @RequestBody RiskChangeStatusDTO riskChangeStatusDTO) throws Exception {
    File data = riskChangeStatusBusiness.exportDataRiskChangeStatus(riskChangeStatusDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListRiskTypeDTOCombobox")
  public ResponseEntity<List<RiskTypeDTO>> getListRiskTypeDTOCombobox(
      @RequestBody RiskTypeDTO riskTypeDTO) {
    List<RiskTypeDTO> list = riskChangeStatusBusiness.getListRiskTypeDTOCombobox(riskTypeDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/deleteRiskChangeStatus")
  public ResponseEntity<ResultInSideDto> deleteRiskChangeStatus(Long id) {
    ResultInSideDto result = riskChangeStatusBusiness.deleteRiskChangeStatus(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
