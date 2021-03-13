package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.GnocLanguageBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "GnocLanguageService")
public class GnocLanguageController {

  @Autowired
  private GnocLanguageBusiness gnocLanguageBusiness;

  //insert gnoclanguage
  @PostMapping("/insertGnocLanguageDTO")
  public ResponseEntity<ResultInSideDto> insertGnocLanguageDTO(
      @RequestBody GnocLanguageDto gnocLanguageDto) {
    ResultInSideDto resultInSideDto = gnocLanguageBusiness
        .insertGnocLanguageDTO(gnocLanguageDto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  //update gnoclanguage
  @PostMapping("/updateGnocLanguageDTO")
  public ResponseEntity<ResultInSideDto> updateGnocLanguageDTO(
      @RequestBody GnocLanguageDto dto) {
    String resultDto = gnocLanguageBusiness.updateGnocLanguageDTO(dto);
    return new ResponseEntity<>(new ResultInSideDto(dto.getGnocLanguageId(), resultDto, resultDto),
        HttpStatus.OK);
  }

  //delete gnoclanguage
  @PostMapping("/deleteGnocLanguageById")
  public ResponseEntity<ResultInSideDto> deleteGnocLanguageById(@RequestParam Long id) {
    String resultDto = gnocLanguageBusiness.deleteGnocLanguageById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  //get list gnocLanguage
  @PostMapping("/getListGnocLanguage")
  public ResponseEntity<Datatable> getListGnocLanguage(
      @RequestBody GnocLanguageDto dto) {
    Datatable data = gnocLanguageBusiness.getListGnocLanguage(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //get detail
  @PostMapping("/findGnocLanguageId")
  public ResponseEntity<GnocLanguageDto> findGnocLanguageId(Long id) {
    GnocLanguageDto cfgInfoTtSpmDTO = gnocLanguageBusiness
        .findGnocLanguageId(id);
    return new ResponseEntity<>(cfgInfoTtSpmDTO, HttpStatus.OK);
  }
}
