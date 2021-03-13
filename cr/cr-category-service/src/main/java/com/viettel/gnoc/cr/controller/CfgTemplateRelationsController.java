package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.business.CfgTemplateRelationsBusiness;
import com.viettel.gnoc.cr.dto.TemplateRelationsDTO;
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
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CfgTemplateRelationsController")
public class CfgTemplateRelationsController {

  @Autowired
  private CfgTemplateRelationsBusiness cfgTemplateRelationsBusiness;

  @PostMapping("/updateTemplateRelations")
  public ResponseEntity<ResultInSideDto> updateTemplateRelations(
      @RequestBody TemplateRelationsDTO dto) {
    String resultDto = cfgTemplateRelationsBusiness.updateTemplateRelations(dto);
    return new ResponseEntity<>(new ResultInSideDto(dto.getTrsId(), resultDto, resultDto),
        HttpStatus.OK);
  }

  @PostMapping("/findTemplateRelationsById")
  public ResponseEntity<TemplateRelationsDTO> findTemplateRelationsById(Long id) {
    TemplateRelationsDTO templateRelationsDTO = cfgTemplateRelationsBusiness
        .findTemplateRelationsById(id);
    return new ResponseEntity<>(templateRelationsDTO, HttpStatus.OK);
  }

  @PostMapping("/insertTemplateRelations")
  public ResponseEntity<ResultInSideDto> insertTemplateRelations(
      @RequestBody TemplateRelationsDTO templateRelationsDTO) {
    ResultInSideDto resultDto = cfgTemplateRelationsBusiness
        .insertTemplateRelations(templateRelationsDTO);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @PostMapping("/deleteTemplateRelationsById")
  public ResponseEntity<ResultInSideDto> deleteTemplateRelationsById(@RequestParam Long id) {
    String resultDto = cfgTemplateRelationsBusiness.deleteTemplateRelationsById(id);
    return new ResponseEntity<>(new ResultInSideDto(id, resultDto, resultDto), HttpStatus.OK);
  }

  @PostMapping("/getListTemplateRelations")
  public ResponseEntity<Datatable> getListTemplateRelations(
      @RequestBody TemplateRelationsDTO templateRelationsDTO) {
    Datatable data = cfgTemplateRelationsBusiness.getListTemplateRelations(templateRelationsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
