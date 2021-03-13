package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.business.CrGeneralBusiness;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "CrGeneral")
@Slf4j
public class CrGeneralController {

  @Autowired
  protected CrGeneralBusiness crGeneralBusiness;

  @RequestMapping(value = "/insertCrCreatedFromOtherSystem", method = RequestMethod.POST)
  public ResponseEntity<ResultDTO> insertCrCreatedFromOtherSystem(
      @RequestBody CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      ResultInSideDto resultInSideDto = crGeneralBusiness
          .insertCrCreatedFromOtherSystem(crCreatedFromOtherSysDTO);
      if (resultInSideDto != null) {
        resultDTO.setKey(resultInSideDto.getKey());
        resultDTO.setMessage(resultInSideDto.getMessage());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<>(
        resultDTO,
        HttpStatus.OK);
  }

}
