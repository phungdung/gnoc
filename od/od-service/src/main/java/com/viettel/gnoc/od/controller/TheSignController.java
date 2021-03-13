package com.viettel.gnoc.od.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SignVofficeDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.od.business.OdBusiness;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TungPV
 */
@RestController
@Slf4j
@RequestMapping(Constants.OD_API_PATH_PREFIX + "theSign")
public class TheSignController {

  @Autowired
  OdBusiness odBusiness;

  @PostMapping("/onTheSign")
  public ResponseEntity<ResultInSideDto> onTheSign(
      @RequestPart("files") List<MultipartFile> multipartFiles,
      @RequestPart("formDataJson") String formDataJson,
      @RequestPart("moduleName") String moduleName)
      throws IOException {
    ResultInSideDto data = new ResultInSideDto();
    ObjectMapper mapper = new ObjectMapper();
    switch (moduleName) {
      case "OD_WORKFLOW":
        SignVofficeDTO signVofficeDTO = mapper.readValue(formDataJson, SignVofficeDTO.class);
        try {
          data = odBusiness.signToVoffice(signVofficeDTO, multipartFiles);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          data.setKey(RESULT.ERROR);
          data.setMessage(e.getMessage());
        }
        break;
      default:
        data.setKey(RESULT.ERROR);
        break;
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
