package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.business.CrAutoServiceForSRBusiness;
import com.viettel.gnoc.cr.dto.InsertAutoCrForSrDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrAutoServiceForSRControllers")
@Slf4j
public class CrAutoServiceForSRControllers {

  @Autowired
  CrAutoServiceForSRBusiness crAutoServiceForSRBusiness;

  @GetMapping("/getCrNumber/crProcessId{crProcessId}")
  public ResponseEntity<ResultInSideDto> getCrNumber(
      @PathVariable("crProcessId") String crProcessId) {
    String result = "";
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      result = crAutoServiceForSRBusiness
          .getCrNumber(crProcessId);
    } catch (Exception e) {
      resultInSideDto.setMessage(e.getCause().getMessage());
    }
    if (!StringUtils.isStringNullOrEmpty(result) && result.contains("CR")) {
      resultInSideDto.setKey(RESULT.SUCCESS);
      resultInSideDto.setIdValue(result);
    } else {
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setIdValue(result);
    }
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/insertAutoCrForSR")
  public ResponseEntity<ResultDTO> insertAutoCrForSR(
      @RequestBody InsertAutoCrForSrDTO insertAutoCrForSrDTO) {
    ResultDTO result = new ResultDTO();
    if (insertAutoCrForSrDTO != null) {
      I18n.setLocale(insertAutoCrForSrDTO.getLocale());
      result = crAutoServiceForSRBusiness
          .insertAutoCrForSR(insertAutoCrForSrDTO.getCrDTO(), insertAutoCrForSrDTO.getLstFile(),
              insertAutoCrForSrDTO.getSystem(), insertAutoCrForSrDTO.getNationCode(),
              insertAutoCrForSrDTO.getLstWo(), insertAutoCrForSrDTO.getLstMop(),
              insertAutoCrForSrDTO.getLstNodeIp());
    }
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getFilePathSrCr")
  public ResponseEntity<GnocFileDto> getFilePathSrCr(@RequestBody GnocFileDto gnocFileDto) {
    GnocFileDto result = crAutoServiceForSRBusiness.getFilePathSrCr(gnocFileDto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
