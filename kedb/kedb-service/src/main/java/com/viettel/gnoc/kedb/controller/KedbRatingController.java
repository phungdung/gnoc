package com.viettel.gnoc.kedb.controller;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.kedb.business.KedbRatingBusiness;
import com.viettel.gnoc.kedb.dto.KedbRatingInsideDTO;
import java.util.ArrayList;
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

@RestController
@RequestMapping(Constants.PT_API_PATH_PREFIX + "KedbRatingService")
@Slf4j
public class KedbRatingController {

  @Autowired
  KedbRatingBusiness kedbRatingBusiness;

  @PostMapping("/getListKedbRatingDTO")
  public ResponseEntity<List<KedbRatingInsideDTO>> getListKedbRatingDTO(
      KedbRatingInsideDTO kedbRatingInsideDTO) {
    List<KedbRatingInsideDTO> list = kedbRatingBusiness.getListKedbRatingDTO(kedbRatingInsideDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/findKedbRatingById")
  public ResponseEntity<KedbRatingInsideDTO> findKedbRatingById(Long id) {
    KedbRatingInsideDTO dto = kedbRatingBusiness.findKedbRatingById(id);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @PostMapping("/getKedbRating")
  public ResponseEntity<KedbRatingInsideDTO> getKedbRating(
      KedbRatingInsideDTO kedbRatingInsideDTO) {
    KedbRatingInsideDTO dto = kedbRatingBusiness.getKedbRating(kedbRatingInsideDTO);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @PostMapping("/insertKedbRating")
  public ResponseEntity<ResultInSideDto> insertKedbRating(
      @RequestBody KedbRatingInsideDTO kedbRatingInsideDTO) {
    ResultInSideDto resultInSideDto = kedbRatingBusiness.insertKedbRating(kedbRatingInsideDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateKedbRating")
  public ResponseEntity<ResultInSideDto> updateKedbRating(
      @RequestBody KedbRatingInsideDTO kedbRatingInsideDTO) {
    ResultInSideDto result = kedbRatingBusiness.updateKedbRating(kedbRatingInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

//  @PostMapping("/insertOrUpdateKedbRating")
//  public ResponseEntity<KedbRatingInsideDTO> insertOrUpdateKedbRating(
//      @RequestBody KedbRatingInsideDTO kedbRatingInsideDTO) {
//    KedbRatingInsideDTO dto = kedbRatingBusiness.insertOrUpdateKedbRating(kedbRatingInsideDTO);
//    return new ResponseEntity<>(dto, HttpStatus.OK);
//  }

  @PostMapping("/insertOrUpdateListKedbRating")
  public ResponseEntity<ResultInSideDto> insertOrUpdateListKedbRating(
      @RequestBody List<KedbRatingInsideDTO> listKedbRatingInsideDTO) {
    ResultInSideDto result = kedbRatingBusiness.insertOrUpdateListKedbRating(
        listKedbRatingInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/deleteKedbRating")
  public ResponseEntity<ResultInSideDto> deleteKedbRating(Long id) {
    ResultInSideDto result = kedbRatingBusiness.deleteKedbRating(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/deleteListKedbRating")
  public ResponseEntity<ResultInSideDto> deleteListKedbRating(
      @RequestBody List<KedbRatingInsideDTO> listKedbRatingInsideDTO) {
    List<Long> listId = new ArrayList<>();
    for (KedbRatingInsideDTO dto : listKedbRatingInsideDTO) {
      listId.add(dto.getId());
    }
    ResultInSideDto result = kedbRatingBusiness.deleteListKedbRating(listId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getSequenseKedbRating")
  public ResponseEntity<List<String>> getSequenseKedbRating(int... size) {
    List<String> data = kedbRatingBusiness.getSequenseKedbRating(size);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListKedbRatingByCondition")
  public ResponseEntity<List<KedbRatingInsideDTO>> getListKedbRatingByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    List<KedbRatingInsideDTO> data = kedbRatingBusiness
        .getListKedbRatingByCondition(lstCondition, rowStart, maxRow,
            sortType, sortFieldList);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
