package com.viettel.gnoc.mr.controller;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrITSoftProcedureDTO;
import com.viettel.gnoc.mr.business.MrITSoftProcedureBusiness;
import java.util.List;
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
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "MrSoftITProcedure")
public class MrITSoftProcedureController {

  @Autowired
  private MrITSoftProcedureBusiness mrITSoftProcedureBusiness;

  @PostMapping("/getListMrSoftITProcedure")
  public ResponseEntity<Datatable> getListMrSoftITProcedure(
      @RequestBody MrITSoftProcedureDTO mrITSoftProcedureDTO) {
    return new ResponseEntity<>(mrITSoftProcedureBusiness.getListMrSoftITProcedure(
        mrITSoftProcedureDTO), HttpStatus.OK);
  }

  @GetMapping("/findById")
  public ResponseEntity<MrITSoftProcedureDTO> getDetail(Long id) {
    MrITSoftProcedureDTO data = mrITSoftProcedureBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/onInsert", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> onInsert(
      @RequestPart("fileAttachs") List<MultipartFile> fileAttachs,
      @Valid @RequestPart("formDataJson") MrITSoftProcedureDTO dto) throws Exception {
    ResultInSideDto result = mrITSoftProcedureBusiness.onInsert(fileAttachs, dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }


  @PostMapping("/onUpdate")
  public ResponseEntity<ResultInSideDto> onUpdate(
      @RequestPart("fileAttachs") List<MultipartFile> fileAttachs,
      @Valid @RequestPart("formDataJson") MrITSoftProcedureDTO dto) throws Exception {
    ResultInSideDto result = mrITSoftProcedureBusiness.onUpdate(fileAttachs, dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @DeleteMapping("/deleteById")
  public ResponseEntity<ResultInSideDto> deleteById(Long id) {
    ResultInSideDto data = mrITSoftProcedureBusiness.delete(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
