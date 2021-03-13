package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.business.CfgWorkLogCategoryBusiness;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CfgWorkLogCategoryController")
public class CfgWorkLogCategoryController {

  @Autowired
  private CfgWorkLogCategoryBusiness cfgWorkLogCategoryBusiness;

  @PostMapping("/findWorkLogCategoryById")
  public ResponseEntity<WorkLogCategoryInsideDTO> findWorkLogCategoryById(Long id) {
    WorkLogCategoryInsideDTO workLogCategoryDTO = cfgWorkLogCategoryBusiness
        .findWorkLogCategoryById(id);
    return new ResponseEntity<>(workLogCategoryDTO, HttpStatus.OK);
  }

  @PostMapping("/insertWorkLogCategory")
  public ResponseEntity<ResultInSideDto> insertWorkLogCategory(
      @Valid @RequestBody WorkLogCategoryInsideDTO workLogCategoryDTO) {
    ResultInSideDto resultDto = cfgWorkLogCategoryBusiness
        .insertWorkLogCategory(workLogCategoryDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/deleteWorkLogCategoryById")
  public ResponseEntity<ResultInSideDto> deleteWorkLogCategoryById(@RequestParam Long id) {
    String resultDto = cfgWorkLogCategoryBusiness.deleteWorkLogCategoryById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/updateWorkLogCategory")
  public ResponseEntity<ResultInSideDto> updateWorkLogCategory(
      @Valid @RequestBody WorkLogCategoryInsideDTO dto) {
    String resultDto = cfgWorkLogCategoryBusiness.updateWorkLogCategory(dto);
    return new ResponseEntity<>(new ResultInSideDto(dto.getWlayId(), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/getListWorkLogCategory")
  public ResponseEntity<Datatable> getListWorkLogCategory(
      @RequestBody WorkLogCategoryInsideDTO dto) {
    Datatable data = cfgWorkLogCategoryBusiness.getListWorkLogCategory(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWorklogType")
  public ResponseEntity<List<WorkLogCategoryInsideDTO>> getListWorkLogType() {
    List<WorkLogCategoryInsideDTO> data = cfgWorkLogCategoryBusiness.getListWorkLogType();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
