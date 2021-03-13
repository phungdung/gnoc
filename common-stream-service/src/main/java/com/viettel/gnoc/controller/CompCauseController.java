package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CompCauseBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CompCauseDTO;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.List;
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
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CompCauseService")
public class CompCauseController {

  @Autowired
  private CompCauseBusiness compCauseBusiness;

  @PostMapping("/getCompCauseListByMap")
  public ResponseEntity<List<CompCauseDTO>> getCompCauseListByMap(
      @RequestBody CompCauseDTO compCauseDTO, Boolean isEnable) {
    List<CompCauseDTO> lstTmp = null;
    try {
      lstTmp = compCauseBusiness.getComCauseList(
          compCauseDTO.getServiceTypeId() == null ? null
              : Long.valueOf(compCauseDTO.getServiceTypeId())
          , compCauseDTO.getCcGroupId()
          , compCauseDTO.getParentId() == null ? null : Long.valueOf(compCauseDTO.getParentId())
          , compCauseDTO.getLevelId() == null ? null : Integer.valueOf(compCauseDTO.getLevelId())
          , compCauseDTO.getLineType()
          , compCauseDTO.getCfgType() == null ? null : Long.valueOf(compCauseDTO.getCfgType())
          , null
          , isEnable);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    List<CompCauseDTO> lstReturn = compCauseBusiness.translateList(lstTmp, I18n.getLocale());
    return new ResponseEntity<>(lstReturn, HttpStatus.OK);
  }

  @PostMapping("/findCompCauseById")
  public ResponseEntity<CompCauseDTO> findCompCauseById(Long id) {
    CompCauseDTO compCauseDTO = compCauseBusiness.findCompCauseById(id);
    return new ResponseEntity<>(compCauseDTO, HttpStatus.OK);
  }
}
