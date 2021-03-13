package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.SearchConfigUserBusiness;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SearchConfigUserDTO;
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
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "SearchConfigUserService")
public class SearchConfigUserController {

  @Autowired
  private SearchConfigUserBusiness searchConfigUserBusiness;

  @PostMapping("/getListSearchConfigUserDTO")
  public ResponseEntity<List> getListSearchConfigUserDTO(
      @RequestBody SearchConfigUserDTO searchConfigUserDTO) {
    List lst = searchConfigUserBusiness.getListSearchConfigUserDTO(searchConfigUserDTO,
        0, Integer.MAX_VALUE,
        searchConfigUserDTO.getSortType(), searchConfigUserDTO.getSortName());
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @GetMapping("/getSequenceSearchConfigUser")
  public ResponseEntity<String> getSequenceSearchConfigUser(String seqName) {
    return new ResponseEntity<>(
        searchConfigUserBusiness.getSequenseSearchConfigUser(seqName, 1).get(0), HttpStatus.OK);
  }

  @GetMapping("/findSearchConfigUserById")
  public ResponseEntity<SearchConfigUserDTO> findSearchConfigUserById(Long id) {
    SearchConfigUserDTO SearchConfigUserDTO = searchConfigUserBusiness.findSearchConfigUserById(id);
    return new ResponseEntity<>(SearchConfigUserDTO, HttpStatus.OK);
  }

  @PostMapping("/deleteSearchConfigUser")
  public ResponseEntity<ResultInSideDto> deleteSearchConfigUser(Long id) {
    String resultDto = searchConfigUserBusiness.deleteSearchConfigUser(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/insertSearchConfigUser")
  public ResponseEntity<ResultInSideDto> insertSearchConfigUser(
      @RequestBody SearchConfigUserDTO dto) {
    ResultInSideDto resultInSideDto = searchConfigUserBusiness.insertSearchConfigUser(dto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateSearchConfigUser")
  public ResponseEntity<ResultInSideDto> updateSearchConfigUser(
      @RequestBody SearchConfigUserDTO dto) {
    String resultDto = searchConfigUserBusiness.updateSearchConfigUser(dto);
    return new ResponseEntity<>(
        new ResultInSideDto(dto.getSearchConfigUserId(), resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/deleteListSearchConfigUser")
  public ResponseEntity<ResultInSideDto> deleteListSearchConfigUser(
      @RequestBody SearchConfigUserDTO dto) {
    ResultInSideDto resultInSideDto = searchConfigUserBusiness.deleteListSearchConfigUser(dto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getListSearchConfigUserByCondition")
  public ResponseEntity<List<SearchConfigUserDTO>> getListSearchConfigUserByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    List<SearchConfigUserDTO> data = searchConfigUserBusiness
        .getListSearchConfigUserByCondition(lstCondition, rowStart, maxRow, sortType,
            sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListSearchConfigUser")
  public ResponseEntity<ResultInSideDto> insertOrUpdateListSearchConfigUser(
      @RequestBody SearchConfigUserDTO dto) {
    ResultInSideDto resultInSideDto = searchConfigUserBusiness
        .insertOrUpdateListSearchConfigUser(dto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }
}
