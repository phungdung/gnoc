package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatServiceBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.business.SRReasonRejectBusiness;
import com.viettel.gnoc.sr.business.SRServiceManageBusiness;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRReasonRejectDTO;
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
@RequestMapping(Constants.OD_API_PATH_PREFIX + "srReasonReject")
@Slf4j
public class SRReasonRejectController {

  @Autowired
  protected CatItemBusiness catItemBusiness;

  @Autowired
  protected CatServiceBusiness catServiceBusiness;

  @Autowired
  protected SRReasonRejectBusiness srReasonRejectBusiness;

  @Autowired
  protected SRServiceManageBusiness srServiceManageBusiness;

  @PostMapping("/getListSRReasonRejectDTO")
  public ResponseEntity<Datatable> getListSRReasonRejectDTO(
      @RequestBody SRReasonRejectDTO srReasonRejectDTO) {
    Datatable data = srReasonRejectBusiness.getListSRReasonRejectDTO(srReasonRejectDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteSRReasonReject")
  public ResponseEntity<ResultInSideDto> deleteSRReasonReject(Long id) {
    ResultInSideDto data = srReasonRejectBusiness.deleteSRReasonReject(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateSRReason")
  public ResponseEntity<ResultInSideDto> insertOrUpdateSRReason(
      @RequestBody SRReasonRejectDTO srReasonRejectDTO) {
    ResultInSideDto data = srReasonRejectBusiness.insertOrUpdateSRReason(srReasonRejectDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //get combox status search
  @GetMapping("/getListStatus")
  public ResponseEntity<List<SRConfigDTO>> getListStatus() {
    List<SRConfigDTO> data = srServiceManageBusiness.getByConfigGroup(Constants.SR_STATUS.STATUS);
    if (data != null && !data.isEmpty()) {
      int size = data.size();
      for (int i = size - 1; i > -1; i--) {
        if (!Constants.SR_STATUS.CANCELLED.equals(data.get(i).getConfigCode())
            && !Constants.SR_STATUS.REJECTED.equals(data.get(i).getConfigCode())) {
          data.remove(i);
        }
      }
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getSRReasonRejectById")
  public ResponseEntity<SRReasonRejectDTO> getSRReasonRejectById(Long Id) {
    SRReasonRejectDTO data = srReasonRejectBusiness.getSRReasonRejectById(Id);

    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
