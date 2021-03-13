package com.viettel.gnoc.controller;


import com.viettel.gnoc.business.ShiftHandoverBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.ShiftHandoverDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.ShiftCrDTO;
import com.viettel.gnoc.cr.dto.ShiftItDTO;
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

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "ShiftHandoverController")
public class ShiftHandoverController {

  @Autowired
  private ShiftHandoverBusiness shiftHandoverBusiness;

  //get detail
  @PostMapping("/findShiftHandoverById")
  public ResponseEntity<ShiftHandoverDTO> findShiftHandoverById(Long id) {
    ShiftHandoverDTO shiftHandoverDTO = shiftHandoverBusiness
        .findShiftHandoverById(id);
    return new ResponseEntity<>(shiftHandoverDTO, HttpStatus.OK);
  }

  @PostMapping("/getListShiftHandover")
  public ResponseEntity<Datatable> getListShiftHandover(
      @RequestBody ShiftHandoverDTO shiftHandoverDTO) {
    Datatable data = shiftHandoverBusiness.getListShiftHandover(shiftHandoverDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


  //getListShiftID
  @PostMapping("/getListShiftID")
  public ResponseEntity<List<ShiftHandoverDTO>> getListShiftID() {
    List<ShiftHandoverDTO> data = shiftHandoverBusiness.getListShiftID();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //updateCfgShiftHandover
  @RequestMapping(value = "/updateCfgShiftHandover", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> updateCfgShiftHandover(
      @RequestPart("files") List<MultipartFile> files,
      @RequestPart("formDataJson") ShiftHandoverDTO dto) throws IOException {
    ResultInSideDto resultDto = shiftHandoverBusiness.updateCfgShiftHandover(files, dto);
    return new ResponseEntity<>(resultDto,
        HttpStatus.OK);
  }


  @RequestMapping(value = "/insertCfgShiftHandover", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertCfgShiftHandover(
      @RequestPart("files") List<MultipartFile> files,
      @RequestPart("formDataJson") ShiftHandoverDTO shiftHandoverDTO) throws IOException {
    ResultInSideDto resultInSideDto = shiftHandoverBusiness
        .insertCfgShiftHandover(files, shiftHandoverDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  //get detail ListShiftHandOver
  @PostMapping("/findListShiftHandOverById")
  public ResponseEntity<ShiftHandoverDTO> findListShiftHandOverById(Long id) {
    ShiftHandoverDTO shiftHandoverDTO = shiftHandoverBusiness
        .findListShiftHandOverById(id);
    return new ResponseEntity<>(shiftHandoverDTO, HttpStatus.OK);
  }

  @PostMapping("/countTicketByShift")
  public ResponseEntity<List<ShiftItDTO>> countTicketByShift(
      @RequestBody ShiftHandoverDTO shiftHandoverDTO) {
    List<ShiftItDTO> data = shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findCrByCrNumber")
  public ResponseEntity<ResultInSideDto> findCrByCrNumber(@RequestBody ShiftCrDTO shiftCrDTO) {
    ResultInSideDto data = shiftHandoverBusiness.findCrByCrNumber(shiftCrDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get sequence shift handover
  @PostMapping("/getSequenseProblemWorklog")
  public ResponseEntity<Long> getSequenseShiftHandover(String seqName) {
    int size = 1;
    return new ResponseEntity<>(shiftHandoverBusiness.getSequenseShiftHandover(seqName, size),
        HttpStatus.OK);
  }

}
