package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.business.CrBusiness;
import com.viettel.gnoc.cr.business.CrForOtherSystemBusiness;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrOutputForQLTNDTO;
import com.viettel.gnoc.sr.dto.InsertFileDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrOtherSystemService")
@Slf4j
public class CrOtherSystemController {

  @Autowired
  CrForOtherSystemBusiness crForOtherSystemBusiness;

  @Autowired
  CrBusiness crBusiness;

  @PostMapping("/getListDataByObjectId")
  public ResponseEntity<List<CrCreatedFromOtherSysDTO>> getListDataByObjectId(Long objectId) {
    List<CrCreatedFromOtherSysDTO> lst = crForOtherSystemBusiness
        .getListDataByObjectId(objectId);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/getCrForQLTN")
  public ResponseEntity<CrOutputForQLTNDTO> getCrForQLTN(String userService, String passService,
      String crNumber) {
    CrOutputForQLTNDTO dto = crForOtherSystemBusiness
        .getCrForQLTN(userService, passService, crNumber);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @PostMapping("/insertFile")
  public ResponseEntity<ResultDTO> insertFile(@RequestBody InsertFileDTO insertFileDTO) {
    ResultDTO resultDTO = crForOtherSystemBusiness
        .insertFile(insertFileDTO.getUserName(), insertFileDTO.getCrNumber(),
            insertFileDTO.getFileType(), insertFileDTO.getFileName(),
            insertFileDTO.getFileContent());
    return new ResponseEntity<>(resultDTO, HttpStatus.OK);
  }

  @PostMapping("/updateWorkOrderForWOPRoxy")
  public ResponseEntity<ResultInSideDto> updateWorkOrderForWOPRoxy(
      @RequestBody WoInsideDTO woInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    try {
      String msg = crBusiness
          .updateWorkOrder(woInsideDTO.getCrNumber(), woInsideDTO.getTypeOperation(),
              woInsideDTO.getMopGnoc());
      if (!RESULT.SUCCESS.equalsIgnoreCase(msg)) {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(msg);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  //truyen them crnumber vao
  @PostMapping("/deletWoMopTest")
  public ResponseEntity<ResultInSideDto> deletWoMopTest(@RequestBody WoInsideDTO woInsideDTO) {
    ResultInSideDto resultInSideDto = crBusiness.deletWoMopTest(woInsideDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

}


