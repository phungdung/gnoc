
package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CfgInfoTtSpmBusiness;
import com.viettel.gnoc.commons.dto.CfgInfoTtSpmDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CfgInfoTtSpm")
public class CfgInfoTtSpmController {

  @Autowired
  private CfgInfoTtSpmBusiness cfgInfoTtSpmBusiness;

  @PostMapping("/getListCfgInfoTtSpmDTO")
  public ResponseEntity<List<CfgInfoTtSpmDTO>> getListCfgInfoTtSpmDTO(
      @RequestBody CfgInfoTtSpmDTO cfgInfoTtSpmDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    List<CfgInfoTtSpmDTO> data = cfgInfoTtSpmBusiness
        .getListCfgInfoTtSpmDTO(cfgInfoTtSpmDTO, rowStart, maxRow, sortType, sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateCfgInfoTtSpm")
  public ResponseEntity<ResultInSideDto> updateCfgInfoTtSpm(@Valid
  @RequestBody CfgInfoTtSpmDTO dto) {
    String resultDto = cfgInfoTtSpmBusiness.updateCfgInfoTtSpm(dto);
    return new ResponseEntity<>(
        new ResultInSideDto(Long.parseLong(dto.getId()), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/deleteCfgInfoTtSpm")
  public ResponseEntity<ResultInSideDto> deleteCfgInfoTtSpm(Long id) {
    String resultDto = cfgInfoTtSpmBusiness.deleteCfgInfoTtSpm(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/deleteListCfgInfoTtSpm")
  public ResponseEntity<String> deleteListCfgInfoTtSpm(
      @RequestBody List<CfgInfoTtSpmDTO> dto) {
    String resultDto = cfgInfoTtSpmBusiness.deleteListCfgInfoTtSpm(dto);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/findCfgInfoTtSpmById")
  public ResponseEntity<CfgInfoTtSpmDTO> findCfgInfoTtSpmById(Long id) {
    CfgInfoTtSpmDTO cfgInfoTtSpmDTO = cfgInfoTtSpmBusiness
        .findCfgInfoTtSpmById(id);
    return new ResponseEntity<>(cfgInfoTtSpmDTO, HttpStatus.OK);
  }

  @PostMapping("/insertCfgInfoTtSpm")
  public ResponseEntity<ResultInSideDto> insertCfgInfoTtSpm(@Valid
  @RequestBody CfgInfoTtSpmDTO cfgInfoTtSpmDTO) {
    ResultInSideDto resultInSideDto = cfgInfoTtSpmBusiness
        .insertCfgInfoTtSpm(cfgInfoTtSpmDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListCfgInfoTtSpm")
  public ResponseEntity<String> insertOrUpdateListCfgInfoTtSpm(
      @RequestBody List<CfgInfoTtSpmDTO> cfgInfoTtSpmDTOS) {
    String result = cfgInfoTtSpmBusiness.insertOrUpdateListCfgInfoTtSpm(cfgInfoTtSpmDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getSequenseCfgInfoTtSpm")
  public ResponseEntity<String> getSequenseCfgInfoTtSpm(String seqName) {
    return new ResponseEntity<>(
        cfgInfoTtSpmBusiness.getSequenseCfgInfoTtSpm(seqName, 1).get(0), HttpStatus.OK);
  }

  @PostMapping("/getListCfgInfoTtSpmByCondition")
  public ResponseEntity<List<CfgInfoTtSpmDTO>> getListCfgInfoTtSpmByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortName) {
    List<CfgInfoTtSpmDTO> data = cfgInfoTtSpmBusiness
        .getListCfgInfoTtSpmByCondition(lstCondition, rowStart, maxRow, sortType, sortName);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCfgInfoTtSpmDTO2")
  public ResponseEntity<Datatable> getListCfgInfoTtSpmDTO2(
      @RequestBody CfgInfoTtSpmDTO cfgInfoTtSpmDTO) {
    Datatable data = cfgInfoTtSpmBusiness.getListCfgInfoTtSpmDTO2(cfgInfoTtSpmDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}

