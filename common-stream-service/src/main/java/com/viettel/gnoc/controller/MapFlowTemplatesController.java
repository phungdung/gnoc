package com.viettel.gnoc.controller;


import com.viettel.gnoc.business.MapFlowTemplatesBusiness;
import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MapFlowTemplatesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.PROBLEM;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "MapFlowTemplates")
@Slf4j
public class MapFlowTemplatesController {

  @Autowired
  MapFlowTemplatesBusiness mapFlowTemplatesBusiness;
  @Autowired
  CatItemBusiness catItemBusiness;

  @GetMapping("/getListItemByCategoryAndParent")
  public ResponseEntity<List<CatItemDTO>> getListItemByCategoryAndParent(String parentItemId) {
    List<CatItemDTO> data = new ArrayList<>();
    if (StringUtils.isNotNullOrEmpty(parentItemId)) {
      data = catItemBusiness.getListItemByCategoryAndParent(PROBLEM.ALARM_GROUP, parentItemId);
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertMapFlowTemplates")
  public ResponseEntity<ResultInSideDto> insertMapFlowTemplates(
      @Valid @RequestBody MapFlowTemplatesDTO mapFlowTemplatesDTO) {
    ResultInSideDto data = mapFlowTemplatesBusiness.insertMapFlowTemplates(mapFlowTemplatesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteMapFlowTemplates")
  public ResponseEntity<ResultInSideDto> deleteMapFlowTemplates(Long mapFlowTemplatesId) {
    ResultInSideDto result = mapFlowTemplatesBusiness.deleteMapFlowTemplates(mapFlowTemplatesId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/updateMapFlowTemplates")
  public ResponseEntity<ResultInSideDto> updateMapFlowTemplates(
      @Valid @RequestBody MapFlowTemplatesDTO mapFlowTemplatesDTO) {
    ResultInSideDto data = mapFlowTemplatesBusiness.updateMapFlowTemplates(mapFlowTemplatesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/deleteListMapFlowTemplates")
  public ResponseEntity<ResultInSideDto> deleteListMapFlowTemplates(
      @RequestBody List<MapFlowTemplatesDTO> mapFlowTemplatesDTOS) {
    ResultInSideDto result = mapFlowTemplatesBusiness
        .deleteListMapFlowTemplates(mapFlowTemplatesDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateListMapFlowTemplates")
  public ResponseEntity<ResultInSideDto> insertOrUpdateListMapFlowTemplates(@Valid
  @RequestBody List<MapFlowTemplatesDTO> mapFlowTemplatesDTOS) {
    ResultInSideDto result = mapFlowTemplatesBusiness
        .insertOrUpdateListMapFlowTemplates(mapFlowTemplatesDTOS);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getSequenseMapFlowTemplates")
  public ResponseEntity<String> getSequenseMapFlowTemplates(String seqName) {
    return new ResponseEntity<>(
        mapFlowTemplatesBusiness.getSequenseMapFlowTemplates(seqName, 1).get(0),
        HttpStatus.OK);
  }

  @GetMapping("/findMapFlowTemplatesById")
  public ResponseEntity<MapFlowTemplatesDTO> findCfgUnitTtSpmById(Long mapFlowTemplatesId) {
    MapFlowTemplatesDTO data = mapFlowTemplatesBusiness
        .findMapFlowTemplatesById(mapFlowTemplatesId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListMapFlowTemplatesDTO")
  public ResponseEntity<Datatable> getListMapFlowTemplatesDTO(
      @RequestBody MapFlowTemplatesDTO mapFlowTemplatesDTO) {
    Datatable data = mapFlowTemplatesBusiness.getListMapFlowTemplatesDTO(mapFlowTemplatesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListMapFlowTemplates")
  public ResponseEntity<List<MapFlowTemplatesDTO>> getListMapFlowTemplates(
      @RequestBody MapFlowTemplatesDTO mapFlowTemplatesDTO) {
    List<MapFlowTemplatesDTO> data = mapFlowTemplatesBusiness
        .getListMapFlowTemplates(mapFlowTemplatesDTO, 0, Integer.MAX_VALUE, "", "");
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}

