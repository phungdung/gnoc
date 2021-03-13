package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.GnocFileBusiness;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "GnocFileService")
public class GnocFileController {

  @Autowired
  private GnocFileBusiness gnocFileBusiness;

  @PostMapping("/getListGnocFileByDto")
  public ResponseEntity<List<GnocFileDto>> getListGnocFileByDto(
      @RequestBody GnocFileDto gnocFileDto) {
    List<GnocFileDto> data = gnocFileBusiness.getListGnocFileByDto(gnocFileDto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findListGnocFile")
  public ResponseEntity<List<GnocFileDto>> findListGnocFile(@RequestBody GnocFileDto gnocFileDto){
    List<GnocFileDto> lstFile = gnocFileBusiness.findListGnocFile(gnocFileDto);
    return new ResponseEntity<>(lstFile, HttpStatus.OK);
  }

  @RequestMapping(value = "/uploadFileCommon", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> upLoadCommonFile(
      @RequestPart("fileAttacks") List<MultipartFile> fileAttacks) {
    ResultInSideDto result = gnocFileBusiness.uploadFileCommon(fileAttacks);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
