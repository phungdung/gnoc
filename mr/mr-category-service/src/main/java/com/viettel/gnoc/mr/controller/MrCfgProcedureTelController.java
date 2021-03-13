package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import com.viettel.gnoc.mr.business.MrCfgProcedureTelBusiness;
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

@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrCfgProcedureTelService")
public class MrCfgProcedureTelController {

  @Autowired
  MrCfgProcedureTelBusiness mrCfgProcedureTelBusiness;

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(
      @RequestBody MrCfgProcedureTelDTO mrCfgProcedureTelDTO) {
    Datatable datatable = mrCfgProcedureTelBusiness.onSearch(mrCfgProcedureTelDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @RequestMapping(value = "/onInsert", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> onInsert(
      @RequestPart("fileAttachs") List<MultipartFile> fileAttachs,
      @Valid @RequestPart("formDataJson") MrCfgProcedureTelDTO dto) throws Exception {
    ResultInSideDto result = mrCfgProcedureTelBusiness.onInsert(fileAttachs, dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/onUpdate")
  public ResponseEntity<ResultInSideDto> onUpdate(
      @RequestPart("fileAttachs") List<MultipartFile> fileAttachs,
      @Valid @RequestPart("formDataJson") MrCfgProcedureTelDTO dto) throws Exception {
    ResultInSideDto result = mrCfgProcedureTelBusiness.onUpdate(fileAttachs, dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/findById")
  public ResponseEntity<MrCfgProcedureTelDTO> findById(Long id) {
    MrCfgProcedureTelDTO data = mrCfgProcedureTelBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @DeleteMapping("/deleteById")
  public ResponseEntity<ResultInSideDto> deleteById(Long id) {
    ResultInSideDto data = mrCfgProcedureTelBusiness.delete(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
