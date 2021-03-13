package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.LogChangeConfigBusiness;
import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.commons.utils.StringUtils;
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
import org.springframework.web.bind.annotation.RestController;
import viettel.passport.client.UserToken;

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CatItemService")
public class CatItemController {

  @Autowired
  private CatItemBusiness catItemBusiness;

  @Autowired
  private LogChangeConfigBusiness logChangeConfigBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @PostMapping("/getListCatItemSearch")
  public ResponseEntity<Datatable> getListCatItemSearch(@RequestBody CatItemDTO catItemDTO) {
    Datatable data = catItemBusiness.getListCatItemSearch(catItemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getCatItemDetail")
  public ResponseEntity<CatItemDTO> getCatItemDetail(Long itemId) {
    CatItemDTO data = catItemBusiness.getCatItemById(itemId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteCatItem")
  public ResponseEntity<ResultInSideDto> deleteCatItem(Long itemId) {
    ResultInSideDto data = catItemBusiness.deleteCatItem(itemId);
    UserToken userToken = ticketProvider.getUserToken();
    if (data.getKey().equals(RESULT.SUCCESS)) {
      logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete Item", "Delete Item, itemId: " + data.getId(),
          null, null));
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertCatItem")
  public ResponseEntity<ResultInSideDto> insertCatItem(@Valid @RequestBody CatItemDTO catItemDTO) {
    ResultInSideDto data = catItemBusiness.insertCatItem(catItemDTO);
    UserToken userToken = ticketProvider.getUserToken();
    if (data.getKey().equals(RESULT.SUCCESS)) {
      logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Insert Item", "Insert Item " + catItemDTO.getItemCode() + catItemDTO.getItemName(),
          null, null));
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CAT_ITEM)
  @PostMapping("/updateCatItem")
  public ResponseEntity<ResultInSideDto> updateCatItem(@Valid @RequestBody CatItemDTO catItemDTO) {
    ResultInSideDto data = catItemBusiness.updateCatItem(catItemDTO);
    UserToken userToken = ticketProvider.getUserToken();
    if (data.getKey().equals(RESULT.SUCCESS)) {
      logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update Item", "Update Item , itemId: " + data.getId(),
          null, null));
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCatItemByListParentCBB")
  public ResponseEntity<List<CatItemDTO>> getListCatItemByListParentCBB(
      @RequestBody CatItemDTO catItemDTO) {
    List<CatItemDTO> data = catItemBusiness.getListCatItemByListParentCBB(catItemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCatItemDTO")
  public ResponseEntity<List<CatItemDTO>> getListCatItemDTO(@RequestBody CatItemDTO catItemDTO) {
    List<CatItemDTO> data = catItemBusiness.getListCatItemDTO(catItemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCatItemTransDTO")
  public ResponseEntity<List<CatItemDTO>> getListCatItemTransDTO(
      @RequestBody CatItemDTO catItemDTO) {
    if (catItemDTO != null && StringUtils.isNotNullOrEmpty(catItemDTO.getProxyLocale())) {
      I18n.setLocale(catItemDTO.getProxyLocale());
    }
    List<CatItemDTO> data = catItemBusiness.getListCatItemTransDTO(catItemDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
