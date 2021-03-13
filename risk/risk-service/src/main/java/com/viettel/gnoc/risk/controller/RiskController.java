package com.viettel.gnoc.risk.controller;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.risk.business.RiskBusiness;
import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.dto.RiskDTO;
import com.viettel.gnoc.risk.dto.RiskDTOSearch;
import com.viettel.gnoc.risk.dto.RiskHistoryDTO;
import com.viettel.gnoc.risk.dto.RiskRelationDTO;
import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
@RequestMapping(Constants.RISK_API_PATH_PREFIX + "Risk")
public class RiskController {

  @Autowired
  RiskBusiness riskBusiness;

  @PostMapping("/getListDataSearchWeb")
  public ResponseEntity<Datatable> getListDataSearchWeb(@RequestBody RiskDTO riskDTO) {
    Datatable data = riskBusiness.getListDataSearchWeb(riskDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/insertRiskFromWeb", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertRiskFromWeb(
      @RequestPart("fileAttacks") List<MultipartFile> fileAttacks,
      @Valid @RequestPart("formDataJson") RiskDTO riskDTO) throws Exception {
    ResultInSideDto result = riskBusiness.insertRiskFromWeb(fileAttacks, riskDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/findRiskByIdFromWeb")
  public ResponseEntity<RiskDTO> findRiskByIdFromWeb(Long riskId) {
    RiskDTO riskDTO = riskBusiness.findRiskByIdFromWeb(riskId);
    return new ResponseEntity<>(riskDTO, HttpStatus.OK);
  }

  @RequestMapping(value = "/updateRiskFromWeb", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> updateRiskFromWeb(
      @RequestPart("fileAttacks") List<MultipartFile> fileAttacks,
      @RequestPart("fileCfgAttacks") List<MultipartFile> fileCfgAttacks,
      @Valid @RequestPart("formDataJson") RiskDTO riskDTO) throws Exception {
    ResultInSideDto result = riskBusiness.updateRiskFromWeb(fileAttacks, fileCfgAttacks, riskDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getListFileFromRisk")
  public ResponseEntity<List<GnocFileDto>> getListFileFromRisk(Long riskId) {
    List<GnocFileDto> list = riskBusiness.getListFileFromRisk(riskId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListRiskHistoryByRiskId")
  public ResponseEntity<Datatable> getListRiskHistoryByRiskId(
      @RequestBody RiskHistoryDTO riskHistoryDTO) {
    Datatable data = riskBusiness.getListRiskHistoryByRiskId(riskHistoryDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListRiskRelationByRiskId")
  public ResponseEntity<Datatable> getListRiskRelationByRiskId(
      @RequestBody RiskRelationDTO riskRelationDTO) {
    Datatable data = riskBusiness.getListRiskRelationByRiskId(riskRelationDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListRiskSystemCombobox")
  public ResponseEntity<List<RiskSystemDTO>> getListRiskSystemCombobox(
      @RequestBody RiskSystemDTO riskSystemDTO) {
    List<RiskSystemDTO> list = riskBusiness.getListRiskSystemCombobox(riskSystemDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListRiskTypeCombobox")
  public ResponseEntity<List<RiskTypeDTO>> getListRiskTypeCombobox(
      @RequestBody RiskTypeDTO riskTypeDTO) {
    List<RiskTypeDTO> list = riskBusiness.getListRiskTypeCombobox(riskTypeDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getValueFromValueKey")
  public ResponseEntity<ResultInSideDto> getValueFromValueKey(String configKey, String valueKey) {
    return new ResponseEntity<>(riskBusiness.getValueFromValueKey(configKey, valueKey),
        HttpStatus.OK);
  }

  @GetMapping("/findUnitById")
  public ResponseEntity<UnitDTO> findUnitById(Long unitId) {
    UnitDTO result = riskBusiness.findUnitById(unitId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListRiskTypeDetail")
  public ResponseEntity<List<RiskTypeDetailDTO>> getListRiskTypeDetail(
      @RequestBody RiskTypeDetailDTO riskTypeDetailDTO) {
    List<RiskTypeDetailDTO> list = riskBusiness.getListRiskTypeDetail(riskTypeDetailDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListRiskChangeStatus")
  public ResponseEntity<List<RiskChangeStatusDTO>> getListRiskChangeStatus(
      @RequestBody RiskChangeStatusDTO riskChangeStatusDTO) {
    List<RiskChangeStatusDTO> list = riskBusiness.getListRiskChangeStatus(riskChangeStatusDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListStatusNext")
  public ResponseEntity<List<Long>> getListStatusNext(@RequestBody RiskDTO riskDTO) {
    List<Long> list = riskBusiness.getListStatusNext(riskDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListRiskCfgBusiness")
  public ResponseEntity<List<RiskCfgBusinessDTO>> getListRiskCfgBusiness(
      @RequestBody RiskCfgBusinessDTO riskCfgBusinessDTO) {
    List<RiskCfgBusinessDTO> list = riskBusiness.getListRiskCfgBusiness(riskCfgBusinessDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListRiskRelationBySystem")
  public ResponseEntity<Datatable> getListRiskRelationBySystem(
      @RequestBody RiskRelationDTO riskRelationDTO) {
    Datatable data = riskBusiness.getListRiskRelationBySystem(riskRelationDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportDataRisk")
  public ResponseEntity<File> exportDataRisk(@RequestBody RiskDTO riskDTO) throws Exception {
    File data = riskBusiness.exportDataRisk(riskDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getFileByBusinessId")
  public ResponseEntity<List<GnocFileDto>> getFileByBusinessId(Long businessId) {
    List<GnocFileDto> data = riskBusiness.getFileByBusinessId(businessId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findRiskHistoryByIdFromWeb")
  public ResponseEntity<RiskHistoryDTO> findRiskHistoryByIdFromWeb(@RequestBody RiskHistoryDTO riskHistoryDTO) {
    RiskHistoryDTO data = riskBusiness.findRiskHistoryByIdFromWeb(riskHistoryDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListDataSearchForOther/rowStart{rowStart}/maxRow{maxRow}/sortType{sortType}/sortFieldList{sortFieldList}")
  public List<RiskDTOSearch> getListDataSearchForOther(@RequestBody RiskDTOSearch riskDTOSearch,
      @PathVariable(value="rowStart") int rowStart, @PathVariable(value="maxRow") int maxRow,
      @PathVariable(value="sortType") String sortType, @PathVariable(value="sortFieldList") String sortFieldList) {
    try {
      I18n.setLocale(riskDTOSearch.getProxyLocale());
      return riskBusiness
          .getListDataSearchForOther(riskDTOSearch, rowStart, maxRow, sortType, sortFieldList);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @PostMapping("/onDownloadMultipleFile")
  public  ResponseEntity<Resource> onDownloadMultipleFile(@RequestBody RiskDTO riskDTO){
    try {
    File data = riskBusiness.onDownloadMultipleFile(riskDTO);
    return FileUtils.responseSourceFromFile(data);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @PostMapping("/getListDataSearchByOther")
  public ResponseEntity<List<RiskDTO>> getListRiskByTt(
      @RequestBody RiskDTO riskDTO) {
    List<RiskDTO> data = riskBusiness.getListDataSearchByOther(riskDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // thangdt updateRiskOtherSystem
  @PostMapping("/updateRiskOtherSystem")
  public ResponseEntity<ResultInSideDto> updateRiskOtherSystem(
      @RequestBody RiskDTO riskDTO) {
    ResultInSideDto resultDTO = riskBusiness.updateRiskOtherSystem(riskDTO);
    return new ResponseEntity<>(resultDTO, HttpStatus.OK);
  }
}
