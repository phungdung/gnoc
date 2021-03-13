package com.viettel.gnoc.incident.controller;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.business.TTChangeStatusBusiness;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.PT_API_PATH_PREFIX + "TTChangeStatus")
@Slf4j
public class TTChangeStatusController {

  @Autowired
  TTChangeStatusBusiness ttChangeStatusBusiness;

  @PostMapping("/getListTTChangeStatus")
  public ResponseEntity<Datatable> getListTTChangeStatus(
      @RequestBody TTChangeStatusDTO ttChangeStatusDTO) {
    return new ResponseEntity<>(ttChangeStatusBusiness.getListTTChangeStatus(ttChangeStatusDTO),
        HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateTTChangeStatus")
  public ResponseEntity<ResultInSideDto> insertOrUpdateTTChangeStatus(@Valid
      @RequestBody TTChangeStatusDTO ttChangeStatusDTO) {
    ResultInSideDto resultInSideDto = ttChangeStatusBusiness
        .insertOrUpdateTTChangeStatus(ttChangeStatusDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getDetailCfg")
  public ResponseEntity<TTChangeStatusDTO> getDetailCfg(
      @RequestBody TTChangeStatusDTO ttChangeStatusDTO) {
    TTChangeStatusDTO ttChangeStatusDTOResult = new TTChangeStatusDTO();
    if (ttChangeStatusDTO != null) {
      if (StringUtils.isNotNullOrEmpty(ttChangeStatusDTO.getProxyLocale())) {
        I18n.setLocale(ttChangeStatusDTO.getProxyLocale());
      }
      ttChangeStatusDTOResult = ttChangeStatusBusiness
          .getDetailCfg(ttChangeStatusDTO);
    }
    return new ResponseEntity<>(ttChangeStatusDTOResult, HttpStatus.OK);
  }

  @DeleteMapping("/deleteTTChangeStatus")
  public ResponseEntity<ResultInSideDto> deleteTTChangeStatus(Long id) {
    ResultInSideDto result = ttChangeStatusBusiness.deleteTTChangeStatus(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/findTTChangeStatusById")
  public ResponseEntity<TTChangeStatusDTO> findTTChangeStatusById(Long id) {
    TTChangeStatusDTO riskChangeStatusDTO = ttChangeStatusBusiness
        .findTTChangeStatusById(id);
    return new ResponseEntity<>(riskChangeStatusDTO, HttpStatus.OK);
  }

}
