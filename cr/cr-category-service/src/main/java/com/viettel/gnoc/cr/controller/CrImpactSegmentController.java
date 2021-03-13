package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.CrImpactSegmentBusiness;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.io.File;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DungPV
 */
@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "CrImpactSegment")
@Slf4j
public class CrImpactSegmentController {

  @Autowired
  protected CrImpactSegmentBusiness crImpactSegmentBusiness;

  @PostMapping("/getListImpactSegment")
  public ResponseEntity<Datatable> getListCrImpactSegment(
      @RequestBody ImpactSegmentDTO impactSegmentDTO) {
    return new ResponseEntity<>(crImpactSegmentBusiness.getListCrImpactSegment(
        impactSegmentDTO), HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<ImpactSegmentDTO> getDetail(Long impactSegmentId) {
    return new ResponseEntity<>(crImpactSegmentBusiness.getDetail(impactSegmentId), HttpStatus.OK);
  }

  @GetMapping("/findById/id{id}")
  public ResponseEntity<ImpactSegmentDTO> findById(
      @PathVariable(value = "id") Long impactSegmentId) {
    return new ResponseEntity<>(crImpactSegmentBusiness.getDetail(impactSegmentId), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.IMPACT_SEGMENT)
  @PostMapping("/addImpactSegment")
  public ResponseEntity<ResultInSideDto> addCrImpactSegment(
      @Valid @RequestBody ImpactSegmentDTO impactSegmentDTO) {
    return new ResponseEntity<>(crImpactSegmentBusiness.addCrImpactSegment(
        impactSegmentDTO), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.IMPACT_SEGMENT)
  @PutMapping("/updateImpactSegment")
  public ResponseEntity<ResultInSideDto> updateCrImpactSegment(
      @Valid @RequestBody ImpactSegmentDTO impactSegmentDTO) {
    return new ResponseEntity<>(crImpactSegmentBusiness.updateCrImpactSegment(
        impactSegmentDTO), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.IMPACT_SEGMENT)
  @DeleteMapping("/deleteImpactSegment")
  public ResponseEntity<ResultInSideDto> deleteCrImpactSegment(Long impactSegmentId) {
    return new ResponseEntity<>(crImpactSegmentBusiness.deleteCrImpactSegment(impactSegmentId),
        HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.IMPACT_SEGMENT)
  @DeleteMapping("/deleteListImpactSegment")
  public ResponseEntity<ResultInSideDto> deleteListCrImpactSegment(
      @RequestBody ImpactSegmentDTO impactSegmentDTO) {
    return new ResponseEntity<>(crImpactSegmentBusiness.deleteListCrImpactSegment(
        impactSegmentDTO), HttpStatus.OK);
  }

  @PostMapping("/exportData")
  public ResponseEntity<File> exportData(@RequestBody ImpactSegmentDTO impactSegmentDTO)
      throws Exception {
    File data = crImpactSegmentBusiness.exportData(impactSegmentDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
