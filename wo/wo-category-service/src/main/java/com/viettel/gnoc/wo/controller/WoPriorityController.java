package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.WoPriorityBusiness;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
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
@RequestMapping(Constants.WO_API_PATH_PREFIX + "woPriority")
@Slf4j
public class WoPriorityController {

  @Autowired
  protected WoPriorityBusiness woPriorityBusiness;

  @GetMapping("/findByWoTypeId")
  public ResponseEntity<List<WoPriorityDTO>> findAllWoPriorityByWoTypeID(Long woTypeId) {
    List<WoPriorityDTO> data = woPriorityBusiness.findAllByWoTypeID(woTypeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWoPriorityDTO")
  public ResponseEntity<List<WoPriorityDTO>> getListWoPriorityDTO(
      @RequestBody WoPriorityDTO woPriorityDTO) {
    List<WoPriorityDTO> lst = woPriorityBusiness
        .getListWoPriorityDTO(woPriorityDTO);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }
}
