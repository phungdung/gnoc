package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrUngCuuTTDTO;
import com.viettel.gnoc.mr.business.MrUngCuuTTBusiness;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "MrUngCuuTT")
public class MrUngCuuTTController {

  @Autowired
  private MrUngCuuTTBusiness mrUngCuuTTBusiness;

  @PostMapping("/getListMrUctt")
  public ResponseEntity<Datatable> getListMrUctt(
      @RequestBody MrUngCuuTTDTO mrUngCuuTTDTO) {
    return new ResponseEntity<>(mrUngCuuTTBusiness
        .getListMrUctt(mrUngCuuTTDTO), HttpStatus.OK);
  }

  @RequestMapping(value = "/insertMrUctt", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertSRFile(
      @RequestPart("mrFileList") List<MultipartFile> mrFileList,
      @RequestPart("formDataJson") MrUngCuuTTDTO mrUngCuuTTDTO) throws IOException {
    return new ResponseEntity<>(mrUngCuuTTBusiness.insertMrUctt(mrFileList, mrUngCuuTTDTO),
        HttpStatus.OK);
  }
}
