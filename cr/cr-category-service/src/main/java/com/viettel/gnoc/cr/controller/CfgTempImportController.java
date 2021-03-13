package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CfgTempImportBusiness;
import com.viettel.gnoc.cr.business.CfgWebServiceMethodBusiness;
import com.viettel.gnoc.cr.dto.MethodParameterDTO;
import com.viettel.gnoc.cr.dto.TempImportDTO;
import com.viettel.gnoc.cr.dto.WebServiceMethodDTO;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CfgTempImportController")
public class CfgTempImportController {

  @Autowired
  private CfgTempImportBusiness cfgTempImportBusiness;

  @Autowired
  private CfgWebServiceMethodBusiness cfgWebServiceMethodBusiness;

  @PostMapping("/getListTempImport")
  public ResponseEntity<Datatable> getListTempImport(
      @RequestBody TempImportDTO tempImportDTO) {
    Datatable data = cfgTempImportBusiness.getListTempImport(tempImportDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findTempImportById")
  public ResponseEntity<TempImportDTO> findTempImportById(Long id) {
    TempImportDTO tempImportDTO = cfgTempImportBusiness
        .findTempImportById(id);
    return new ResponseEntity<>(tempImportDTO, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_TEMP_IMPORT)
  @PostMapping("/insertTempImport")
  public ResponseEntity<ResultInSideDto> insertTempImport(
      @RequestPart("files") List<MultipartFile> files,
      @Valid @RequestPart("formDataJson") TempImportDTO tempImportDTO) throws IOException {
    ResultInSideDto resultDto = cfgTempImportBusiness
        .insertTempImport(files, tempImportDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_TEMP_IMPORT)
  @PostMapping("/updateTempImport")
  public ResponseEntity<ResultInSideDto> updateTempImport(
      @RequestPart("files") List<MultipartFile> files,
      @Valid @RequestPart("formDataJson") TempImportDTO tempImportDTO) throws IOException {
    ResultInSideDto resultDto = cfgTempImportBusiness
        .updateTempImport(files, tempImportDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_TEMP_IMPORT)
  @PostMapping("/deleteTempImportById")
  public ResponseEntity<ResultInSideDto> deleteTempImportById(@RequestParam Long id) {
    String resultDto = cfgTempImportBusiness.deleteTempImportById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/getFunctionWebServiceMethod")
  public ResponseEntity<List<WebServiceMethodDTO>> getFunctionWebServiceMethod() {
    List<WebServiceMethodDTO> data = cfgWebServiceMethodBusiness.getListWebServiceMethod();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getMethodPrameter")
  public ResponseEntity<List<MethodParameterDTO>> getMethodPrameter() {
    List<MethodParameterDTO> data = cfgTempImportBusiness.getMethodPrameter();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
