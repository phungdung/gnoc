package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CfgReturnCodeCatalogBusiness;
import com.viettel.gnoc.cr.dto.ReturnCodeCatalogDTO;
import java.util.List;
import javax.validation.Valid;
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
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CfgReturnCodeCatalogController")
public class CfgReturnCodeCatalogController {

  @Autowired
  private CfgReturnCodeCatalogBusiness cfgReturnCodeCatalogBusiness;

  @PostMapping("/findCfgReturnCodeCatalogById")
  public ResponseEntity<ReturnCodeCatalogDTO> findCfgReturnCodeCatalogById(Long id) {
    ReturnCodeCatalogDTO returnCodeCatalogDTO = cfgReturnCodeCatalogBusiness
        .findCfgReturnCodeCatalogById(id);
    return new ResponseEntity<>(returnCodeCatalogDTO, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_RETURN_ACTION)
  @PostMapping("/insertCfgReturnCodeCatalog")
  public ResponseEntity<ResultInSideDto> insertCfgReturnCodeCatalog(
      @Valid @RequestBody ReturnCodeCatalogDTO returnCodeCatalogDTO) {
    ResultInSideDto resultDto = cfgReturnCodeCatalogBusiness
        .insertCfgReturnCodeCatalog(returnCodeCatalogDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_RETURN_ACTION)
  @PostMapping("/deleteCfgReturnCodeCatalogById")
  public ResponseEntity<ResultInSideDto> deleteCfgReturnCodeCatalogById(@RequestParam Long id) {
    String resultDto = cfgReturnCodeCatalogBusiness.deleteCfgReturnCodeCatalogById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_RETURN_ACTION)
  @PostMapping("/updateCfgReturnCodeCatalog")
  public ResponseEntity<ResultInSideDto> updateCfgReturnCodeCatalog(
      @Valid @RequestBody ReturnCodeCatalogDTO dto) {
    String resultDto = cfgReturnCodeCatalogBusiness.updateCfgReturnCodeCatalog(dto);
    return new ResponseEntity<>(
        new ResultInSideDto(dto.getReturnCodeCategoryId(), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/getListReturnCodeCatalog")
  public ResponseEntity<Datatable> getListReturnCodeCatalog(
      @RequestBody ReturnCodeCatalogDTO returnCodeCatalogDTO) {
    Datatable data = cfgReturnCodeCatalogBusiness.getListReturnCodeCatalog(returnCodeCatalogDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListReturnCategory")
  public ResponseEntity<List<ReturnCodeCatalogDTO>> getListReturnCategory() {
    List<ReturnCodeCatalogDTO> data = cfgReturnCodeCatalogBusiness.getListReturnCategory();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
