package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CatCfgClosedTicketBusiness;
import com.viettel.gnoc.commons.dto.CatCfgClosedTicketDTO;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CatCfgClosedTicket")
public class CatCfgClosedTicketController {

  @Autowired
  private CatCfgClosedTicketBusiness catCfgClosedTicketBusiness;

  //get list WoFileTemp
  @PostMapping("/getListCatCfgClosedTicket")
  public ResponseEntity<Datatable> getListCatCfgClosedTicket(
      @RequestBody CatCfgClosedTicketDTO dto) {
    Datatable data = catCfgClosedTicketBusiness.getListCatCfgClosedTicket(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSubCategory")
  public ResponseEntity<List<CatItemDTO>> getListSubCategory(Long typeId) {
    List<CatItemDTO> data = catCfgClosedTicketBusiness.getListSubCategory(typeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get Detail
  @GetMapping("/getDetail")
  public ResponseEntity<CatCfgClosedTicketDTO> getDetail(Long id) {
    CatCfgClosedTicketDTO data = catCfgClosedTicketBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CFG_CLOSED_TICKET)
  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> add(
      @Valid @RequestBody CatCfgClosedTicketDTO catCfgClosedTicketDTO) {
    ResultInSideDto data = catCfgClosedTicketBusiness.insert(catCfgClosedTicketDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CFG_CLOSED_TICKET)
  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> edit(
      @Valid @RequestBody CatCfgClosedTicketDTO catCfgClosedTicketDTO) {
    ResultInSideDto data = catCfgClosedTicketBusiness.update(catCfgClosedTicketDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CFG_CLOSED_TICKET)
  @PostMapping("/deleteWoFileTempById")
  public ResponseEntity<ResultInSideDto> delete(@RequestParam Long id) {
    ResultInSideDto data = catCfgClosedTicketBusiness.delete(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
