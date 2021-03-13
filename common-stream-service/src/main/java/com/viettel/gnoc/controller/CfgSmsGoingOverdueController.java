package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.CfgSmsGoingOverdueBusiness;
import com.viettel.gnoc.commons.dto.CfgSmsGoingOverdueDTO;
import com.viettel.gnoc.commons.dto.CfgSmsUserGoingOverdueFullDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
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

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "TTCfgSmsGoingOverdueService")
public class CfgSmsGoingOverdueController {

  @Autowired
  private CfgSmsGoingOverdueBusiness cfgSmsGoingOverdueBusiness;

  @PostMapping("/getListCfgSmsGoingOverdueDTO")
  public ResponseEntity<Datatable> getListCfgSmsGoingOverdueDTO(
      @RequestBody CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO) {
    Datatable data = cfgSmsGoingOverdueBusiness.getListCfgSmsGoingOverdueDTO(cfgSmsGoingOverdueDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateCfgSmsGoingOverdue")
  public ResponseEntity<ResultInSideDto> updateCfgSmsGoingOverdue(@Valid
  @RequestBody CfgSmsGoingOverdueDTO dto) {
    String resultDto = cfgSmsGoingOverdueBusiness.updateCfgSmsGoingOverdue(dto);
    return new ResponseEntity<>(
        new ResultInSideDto(Long.parseLong(dto.getCfgId()), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/deleteCfgSmsGoingOverdue")
  public ResponseEntity<ResultInSideDto> deleteCfgSmsGoingOverdue(Long id) {
    String resultDto = cfgSmsGoingOverdueBusiness.deleteCfgSmsGoingOverdue(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/deleteListCfgSmsGoingOverdue")
  public ResponseEntity<String> deleteListCfgSmsGoingOverdue(
      @RequestBody List<CfgSmsGoingOverdueDTO> dto) {
    String resultDto = cfgSmsGoingOverdueBusiness.deleteListCfgSmsGoingOverdue(dto);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/findCfgSmsGoingOverdueById")
  public ResponseEntity<CfgSmsGoingOverdueDTO> findCfgSmsGoingOverdueById(Long id) {
    CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO = cfgSmsGoingOverdueBusiness
        .findCfgSmsGoingOverdueById(id);
    return new ResponseEntity<>(cfgSmsGoingOverdueDTO, HttpStatus.OK);
  }

  @PostMapping("/insertCfgSmsGoingOverdue")
  public ResponseEntity<ResultInSideDto> insertCfgSmsGoingOverdue(
      @RequestBody CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO) {
    ResultInSideDto resultInSideDto = cfgSmsGoingOverdueBusiness
        .insertCfgSmsGoingOverdue(cfgSmsGoingOverdueDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getSequenseCfgSmsGoingOverdue")
  public ResponseEntity<String> getSequenseCfgSmsGoingOverdue(String seqName) {
    return new ResponseEntity<>(
        cfgSmsGoingOverdueBusiness.getSequenseCfgSmsGoingOverdue(seqName, 1).get(0), HttpStatus.OK);
  }

  @PostMapping("/getListCfgSmsGoingOverdueByCondition")
  public ResponseEntity<List<CfgSmsGoingOverdueDTO>> getListCfgSmsGoingOverdueByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortName) {
    List<CfgSmsGoingOverdueDTO> data = cfgSmsGoingOverdueBusiness
        .getListCfgSmsGoingOverdueByCondition(lstCondition, rowStart, maxRow, sortType, sortName);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getUserInfo")
  public ResponseEntity<UsersInsideDto> getUserInfo(Long userId) {
    UsersInsideDto usersInsideDto = cfgSmsGoingOverdueBusiness.getUserInfo(userId);
    return new ResponseEntity<>(usersInsideDto, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateCfg")
  public ResponseEntity<String> insertOrUpdateCfg(
      @RequestBody List<CfgSmsGoingOverdueDTO> cfgSmsGoingOverdueDTOS) {
    String result = cfgSmsGoingOverdueBusiness.insertOrUpdateCfg(cfgSmsGoingOverdueDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/deleteCfgSmsGoingOverdueAndUserList")
  public ResponseEntity<ResultInSideDto> deleteCfgSmsGoingOverdueAndUserList(
      @RequestBody CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO) {
    ResultInSideDto resultInSideDto = cfgSmsGoingOverdueBusiness
        .deleteCfgSmsGoingOverdueAndUserList(cfgSmsGoingOverdueDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getListCfgSmsGoingOverdueDTO_allFields")
  public ResponseEntity<List<CfgSmsGoingOverdueDTO>> getListCfgSmsGoingOverdueDTO_allFields(
      String cfgName, String unitId, String userId, String levelId) {
    List<CfgSmsGoingOverdueDTO> data = cfgSmsGoingOverdueBusiness
        .getListCfgSmsGoingOverdueDTO_allFields(cfgName, unitId, userId, levelId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCfgSmsUser")
  public ResponseEntity<Datatable> getListCfgSmsUser(
      @RequestBody CfgSmsUserGoingOverdueFullDTO cfgSmsUserGoingOverdueFullDTO) {
    Datatable data = cfgSmsGoingOverdueBusiness.getListCfgSmsUser(cfgSmsUserGoingOverdueFullDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateCfgSmsGoingOverdue2")
  public ResponseEntity<ResultInSideDto> updateCfgSmsGoingOverdue2(
      @RequestBody CfgSmsGoingOverdueDTO dto) {
    ResultInSideDto resultInSideDto = cfgSmsGoingOverdueBusiness.updateCfgSmsGoingOverdue2(dto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getMaxLevelIDByUnitID")
  public ResponseEntity<Long> getMaxLevelIDByUnitID(@RequestBody CfgSmsGoingOverdueDTO dto) {
    Long maxLevelID = cfgSmsGoingOverdueBusiness.getMaxLevelIDByUnitID(dto);
    return new ResponseEntity<>(maxLevelID, HttpStatus.OK);
  }
}
