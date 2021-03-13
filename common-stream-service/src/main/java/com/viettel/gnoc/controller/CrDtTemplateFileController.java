package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CrDtTemplateFileBusiness;
import com.viettel.gnoc.commons.dto.CrDtTemplateFileDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CrDtTemplateFile")
public class CrDtTemplateFileController {

  @Autowired
  private CrDtTemplateFileBusiness crDtTemplateFileBusiness;

  @PostMapping("/getListCrDtTemplateFile")
  public ResponseEntity<Datatable> getListCrDtTemplateFile(
      @RequestBody CrDtTemplateFileDTO crDtTemplateFileDTO) {
    return new ResponseEntity<>(crDtTemplateFileBusiness
        .getListCrDtTemplateFile(crDtTemplateFileDTO), HttpStatus.OK);
  }

  @GetMapping("/getObjById")
  public ResponseEntity<CrDtTemplateFileDTO> getObjById(Long id) {
    return new ResponseEntity<>(crDtTemplateFileBusiness
        .getObjById(id), HttpStatus.OK);
  }

  @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> saveOrUpdate(
      @RequestPart("crFileList") List<MultipartFile> crFileList,
      @Valid @RequestPart("formDataJson") CrDtTemplateFileDTO crDtTemplateFileDTO) {
    return new ResponseEntity<>(crDtTemplateFileBusiness
        .saveOrUpdate(crFileList, crDtTemplateFileDTO), HttpStatus.OK);
  }

  @DeleteMapping("/deleteCrDtTemplateFile")
  public ResponseEntity<ResultInSideDto> deleteCrDtTemplateFile(Long id) {
    return new ResponseEntity<>(crDtTemplateFileBusiness
        .delete(id), HttpStatus.OK);
  }

  @GetMapping("/getChilProcessCBB")
  public ResponseEntity<List<CrProcessInsideDTO>> getChilProcessCBB(Long parentId) {
    return new ResponseEntity<>(crDtTemplateFileBusiness
        .getChilProcessCBB(parentId), HttpStatus.OK);
  }

  @GetMapping("/getParentProcessCBB")
  public ResponseEntity<List<CrProcessInsideDTO>> getParentProcessCBB() {
    return new ResponseEntity<>(crDtTemplateFileBusiness
        .getParentProcessCBB(), HttpStatus.OK);
  }

  @GetMapping("/getLstFileTypeCBB")
  public ResponseEntity<Map<String, String>> getLstFileTypeCBB() {
    return new ResponseEntity<>(crDtTemplateFileBusiness
        .getLstFileTypeCBB(), HttpStatus.OK);
  }
}
