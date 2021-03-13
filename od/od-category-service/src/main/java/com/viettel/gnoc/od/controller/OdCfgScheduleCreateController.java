package com.viettel.gnoc.od.controller;

import com.viettel.gnoc.commons.business.ReceiveUnitBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.od.business.OdCfgScheduleCreateBusiness;
import com.viettel.gnoc.od.dto.OdCfgScheduleCreateDTO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "OdCfgScheduleCreate")
@Slf4j
public class OdCfgScheduleCreateController {

  @Autowired
  OdCfgScheduleCreateBusiness odCfgScheduleCreateBusiness;

  @Autowired
  ReceiveUnitBusiness receiveUnitBusiness;

  @PostMapping("/getListOdCfgScheduleCreateDTOSearchWeb")
  public ResponseEntity<Datatable> getListOdCfgScheduleCreateDTOSearchWeb(
      @RequestBody OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) {
    Datatable data = odCfgScheduleCreateBusiness
        .getListOdCfgScheduleCreateDTOSearchWeb(odCfgScheduleCreateDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findOdCfgScheduleCreateById")
  public ResponseEntity<OdCfgScheduleCreateDTO> findOdCfgScheduleCreateById(Long id) {
    OdCfgScheduleCreateDTO data = odCfgScheduleCreateBusiness.findOdCfgScheduleCreateById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


  @RequestMapping(value = "/insertOdCfgScheduleCreate", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertOdCfgScheduleCreate(
      @RequestPart("files") List<MultipartFile> files,
      @Valid @RequestPart("formDataJson") OdCfgScheduleCreateDTO odCfgScheduleCreateDTO)
      throws IOException {
    ResultInSideDto data = odCfgScheduleCreateBusiness
        .insertOdCfgScheduleCreate(files, odCfgScheduleCreateDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/updateOdCfgScheduleCreate", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> updateOdCfgScheduleCreate(
      @RequestPart("files") List<MultipartFile> files,
      @Valid @RequestPart("formDataJson") OdCfgScheduleCreateDTO odCfgScheduleCreateDTO)
      throws IOException {
    ResultInSideDto data = odCfgScheduleCreateBusiness
        .updateOdCfgScheduleCreate(files, odCfgScheduleCreateDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteOdCfgScheduleCreate")
  public ResponseEntity<ResultInSideDto> deleteOdCfgScheduleCreate(Long id) {
    ResultInSideDto result = odCfgScheduleCreateBusiness.deleteOdCfgScheduleCreate(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/deleteListOdCfgScheduleCreate")
  public ResponseEntity<ResultInSideDto> deleteListOdCfgScheduleCreate(
      @RequestBody List<OdCfgScheduleCreateDTO> odCfgScheduleCreateDTOList) {
    List<Long> listId = new ArrayList<>();
    for (OdCfgScheduleCreateDTO item : odCfgScheduleCreateDTOList) {
      listId.add(item.getId());
    }
    ResultInSideDto result = odCfgScheduleCreateBusiness.deleteListOdCfgScheduleCreate(listId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getSequenseOdCfgScheduleCreate")
  public ResponseEntity<String> getSequenseOdCfgScheduleCreate() {
    String data = odCfgScheduleCreateBusiness.getSequenseOdCfgScheduleCreate();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/exportDataOdCfgScheduleCreate")
  public ResponseEntity<File> exportDataOdCfgScheduleCreate(
      @RequestBody OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) throws Exception {
    File data = odCfgScheduleCreateBusiness.exportData(odCfgScheduleCreateDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/importDataOdCfgScheduleCreate", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> importDataOdCfgScheduleCreate(
      @RequestPart("fileImport") MultipartFile fileImport,
      @RequestPart("files") List<MultipartFile> files) throws IOException {
    ResultInSideDto result = odCfgScheduleCreateBusiness.importData(fileImport, files);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getTemplate")
  public ResponseEntity<File> getTemplate() throws Exception {
    File data = odCfgScheduleCreateBusiness.getTemplate();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
