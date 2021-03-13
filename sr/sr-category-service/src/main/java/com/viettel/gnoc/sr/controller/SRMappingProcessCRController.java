package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatServiceBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.business.SRMappingProcessCRBusiness;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.sr.dto.SRSearchDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "srMappingProcessCR")
@Slf4j
public class SRMappingProcessCRController {

  @Autowired
  protected CatItemBusiness catItemBusiness;

  @Autowired
  protected CatServiceBusiness catServiceBusiness;

  @Autowired
  protected SRMappingProcessCRBusiness srMappingProcessCRBusiness;

  @PostMapping("/getStartTimeEndTimeFromCrImpact")
  public ResponseEntity<SRMappingProcessCRDTO> getStartTimeEndTimeFromCrImpact(
      @RequestBody SRCreateAutoCRDTO dto) {
    return new ResponseEntity<>(srMappingProcessCRBusiness.getStartTimeEndTimeFromCrImpact(dto),
        HttpStatus.OK);
  }

  @PostMapping("/getListMappingProcessCR")
  public ResponseEntity<Datatable> getListMappingProcessCR(
      @RequestBody SRMappingProcessCRDTO srMappingProcessCRDTO) {
    Datatable data = srMappingProcessCRBusiness.getListMappingProcessCR(srMappingProcessCRDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdate")
  public ResponseEntity<ResultInSideDto> insertOrUpdate(
      @Valid @RequestBody SRMappingProcessCRDTO srMappingProcessCRDTO) {
    ResultInSideDto data = srMappingProcessCRBusiness.insertOrUpdate(srMappingProcessCRDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteSRMappingProcessCR")
  public ResponseEntity<ResultInSideDto> deleteSRMappingProcessCR(Long id) {
    ResultInSideDto data = srMappingProcessCRBusiness.deleteSRMappingProcessCR(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListMapSearch")
  public ResponseEntity<SRSearchDTO> getListMapSearch() {
    SRSearchDTO data = srMappingProcessCRBusiness.getListSearchDTO();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListWoById")
  public ResponseEntity<List<SRMappingProcessCRDTO>> getListWoById(Long id) {
    List<SRMappingProcessCRDTO> data = srMappingProcessCRBusiness.getListWoById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getSRMappingProcessCRDetail")
  public ResponseEntity<SRMappingProcessCRDTO> getSRMappingProcessCRDetail(Long id) {
    SRMappingProcessCRDTO data = srMappingProcessCRBusiness.getSRMappingProcessCRDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSRMappingProcessCRDTO")
  public ResponseEntity<List<SRMappingProcessCRDTO>> getListSRMappingProcessCRDTO(
      @RequestBody SRMappingProcessCRDTO srMappingProcessCRDTO) {
    return new ResponseEntity<>(
        srMappingProcessCRBusiness.getListSRMappingProcessCRDTO(srMappingProcessCRDTO),
        HttpStatus.OK);
  }

  @GetMapping("/getLstWoCdGroupCBB")
  public ResponseEntity<List<WoCdGroupInsideDTO>> getLstWoCdGroupCBB() {
    return new ResponseEntity<>(srMappingProcessCRBusiness.getLstWoCdGroupCBB(), HttpStatus.OK);
  }

  @GetMapping("/getDutyTypeByProcessId")
  public ResponseEntity<List<ItemDataCRInside>> getDutyTypeByProcessId(Long crProcessId) {
    return new ResponseEntity<>(srMappingProcessCRBusiness.getDutyTypeByProcessId(crProcessId),
        HttpStatus.OK);
  }

  @PostMapping("/getDutyTypeByProcessIdProxy")
  public ResponseEntity<List<ItemDataCRInside>> getDutyTypeByProcessIdProxy(@RequestBody
      SRMappingProcessCRDTO srMappingProcessCRDTO) {
    if (srMappingProcessCRDTO != null) {
      if (StringUtils.isNotNullOrEmpty(srMappingProcessCRDTO.getProxyLocale())){
        I18n.setLocale(srMappingProcessCRDTO.getProxyLocale());
      }
      return new ResponseEntity<>(srMappingProcessCRBusiness
          .getDutyTypeByProcessId(srMappingProcessCRDTO.getCrProcessParentId()),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
  }

  @GetMapping("/getLstAffectedServiceCBB")
  public ResponseEntity<List<ItemDataCRInside>> getLstAffectedServiceCBB() {
    return new ResponseEntity<>(srMappingProcessCRBusiness.getLstAffectedServiceCBB(),
        HttpStatus.OK);
  }

  @PostMapping("/getListWoTypeDTOCBB")
  public ResponseEntity<List<WoTypeInsideDTO>> getListWoTypeDTOCBB(
      @RequestBody WoTypeInsideDTO dto) {
    return new ResponseEntity<>(srMappingProcessCRBusiness.getListWoTypeDTOCBB(dto),
        HttpStatus.OK);
  }

  @GetMapping("/getLstPriorityCBB")
  public ResponseEntity<List<WoInsideDTO>> getLstPriority() {
    return new ResponseEntity<>(srMappingProcessCRBusiness.getLstPriority(),
        HttpStatus.OK);
  }

  @GetMapping("/getListWoTestTypeCBB")
  public ResponseEntity<List<WoTypeInsideDTO>> getListWoTestTypeCBB() {
    return new ResponseEntity<>(srMappingProcessCRBusiness.getListWoTestTypeCBB(),
        HttpStatus.OK);
  }

  @GetMapping("/getListStatusCrCBB")
  public ResponseEntity<Map<String, String>> getListStatusCrCBB(Long crProcessId) {
    return new ResponseEntity<>(srMappingProcessCRBusiness.getListStatusCrCBB(crProcessId),
        HttpStatus.OK);
  }
}
