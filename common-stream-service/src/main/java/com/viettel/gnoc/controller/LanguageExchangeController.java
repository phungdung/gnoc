package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.LanguageExchangeBussiness;
import com.viettel.gnoc.commons.business.LogChangeConfigBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
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
import viettel.passport.client.UserToken;

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "LanguageExchange")
public class LanguageExchangeController {

  @Autowired
  private LanguageExchangeBussiness languageExchangeBussiness;

  @Autowired
  private LogChangeConfigBusiness logChangeConfigBusiness;

  @Autowired
  TicketProvider ticketProvider;

  //get list language exchange
  @PostMapping("/getListLanguageExchange")
  public ResponseEntity<Datatable> getListLanguageExchange(
      @RequestBody LanguageExchangeDTO dto) {
    Datatable data = languageExchangeBussiness.getListLanguageExchange(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListTableBySystem")
  public ResponseEntity<List<CatItemDTO>> getListTableBySystem(String systemName) {
    List<CatItemDTO> data = languageExchangeBussiness.getListTableBySystem(systemName);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get list hệ thống CBB getListItemByCategory("MASTER_DATA_SCHEMA","");
  //get list table CBB getListItemByCategory("MASTER_DATA_TABLE","");
  @PostMapping("/getListLanguage")
  public ResponseEntity<List<GnocLanguageDto>> getListLanguage() {
    List<GnocLanguageDto> data = languageExchangeBussiness.getListLanguage();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get Detail
  @GetMapping("/getDetail")
  public ResponseEntity<LanguageExchangeDTO> getDetailLanguageExchange(Long langExchangeId) {
    LanguageExchangeDTO data = languageExchangeBussiness.getDetailLanguageExchange(langExchangeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateLanguageExchange")
  public ResponseEntity<ResultInSideDto> updateLanguageExchange(
      @Valid @RequestBody LanguageExchangeDTO dto) {
    ResultInSideDto data = languageExchangeBussiness.updateLanguageExchange(dto);
    UserToken userToken = ticketProvider.getUserToken();
    if (data.getKey().equals(RESULT.SUCCESS)) {
      logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update Item LanguageExchange", "Update Item , itemId: " + data.getId(),
          null, null));
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertLanguageExchange")
  public ResponseEntity<ResultInSideDto> insertLanguageExchange(
      @Valid @RequestBody LanguageExchangeDTO dto) {
    ResultInSideDto data = languageExchangeBussiness.insertLanguageExchange(dto);
    UserToken userToken = ticketProvider.getUserToken();
    if (data.getKey().equals(RESULT.SUCCESS)) {
      logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Insert Item LanguageExchange",
          "Insert Item " + dto.getLeeLocaleName() + dto.getLeeValue(),
          null, null));
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/deleteLanguageExchangeById")
  public ResponseEntity<ResultInSideDto> deleteLanguageExchangeById(@RequestParam Long id) {
    ResultInSideDto data = languageExchangeBussiness.deleteLanguageExchangeById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
