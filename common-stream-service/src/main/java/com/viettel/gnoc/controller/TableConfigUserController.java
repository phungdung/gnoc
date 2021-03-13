package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.TableConfigUserBusiness;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TableConfigUserDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "TableConfigUserService")
public class TableConfigUserController {

  @Autowired
  private TableConfigUserBusiness tableConfigUserBusiness;

  @PostMapping("/getListTableConfigUserDTO")
  public ResponseEntity<List> getListTableConfigUserDTO(
      @RequestBody TableConfigUserDTO tableConfigUserDTO) {
    List lst = tableConfigUserBusiness.getListTableConfigUserDTO(tableConfigUserDTO,
        0, Integer.MAX_VALUE,
        tableConfigUserDTO.getSortType(), tableConfigUserDTO.getSortName());
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @GetMapping("/getSequenceTableConfigUser")
  public ResponseEntity<String> getSequenceTableConfigUser(String seqName) {
    return new ResponseEntity<>(
        tableConfigUserBusiness.getSequenseTableConfigUser(seqName, 1).get(0), HttpStatus.OK);
  }

  @GetMapping("/findTableConfigUserById")
  public ResponseEntity<TableConfigUserDTO> findTableConfigUserById(Long id) {
    TableConfigUserDTO TableConfigUserDTO = tableConfigUserBusiness.findTableConfigUserById(id);
    return new ResponseEntity<>(TableConfigUserDTO, HttpStatus.OK);
  }

  @PostMapping("/deleteTableConfigUser")
  public ResponseEntity<ResultInSideDto> deleteTableConfigUser(Long id) {
    ResultInSideDto resultInSideDto = tableConfigUserBusiness.deleteTableConfigUser(id);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/insertTableConfigUser")
  public ResponseEntity<ResultInSideDto> insertTableConfigUser(
      @RequestBody TableConfigUserDTO dto) {
    ResultInSideDto resultInSideDto = tableConfigUserBusiness.insertTableConfigUser(dto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateTableConfigUser")
  public ResponseEntity<ResultInSideDto> updateTableConfigUser(
      @RequestBody TableConfigUserDTO dto) {
    String resultDto = tableConfigUserBusiness.updateTableConfigUser(dto);
    return new ResponseEntity<>(
        new ResultInSideDto(dto.getTableConfigUserId(), resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/deleteListTableConfigUser")
  public ResponseEntity<String> deleteListTableConfigUser(
      @RequestBody List<TableConfigUserDTO> dto) {
    String resultDto = tableConfigUserBusiness.deleteListTableConfigUser(dto);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/getListTableConfigUserByCondition")
  public ResponseEntity<List<TableConfigUserDTO>> getListTableConfigUserByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    List<TableConfigUserDTO> data = tableConfigUserBusiness
        .getListTableConfigUserByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListTableConfigUser")
  public ResponseEntity<String> insertOrUpdateListTableConfigUser(
      @RequestBody List<TableConfigUserDTO> problemCrDTOS) {
    String result = tableConfigUserBusiness.insertOrUpdateListTableConfigUser(problemCrDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }


}
