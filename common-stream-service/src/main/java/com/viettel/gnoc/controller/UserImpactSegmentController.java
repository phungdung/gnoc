package com.viettel.gnoc.controller;


import com.viettel.gnoc.business.UserImpactSegmentBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UserImpactSegmentDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.util.List;
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
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "UserImpactSegment")
public class UserImpactSegmentController {

  @Autowired
  private UserImpactSegmentBusiness userImpactSegmentBusiness;

  @PostMapping("/getListUserImpactSegmentOfCr")
  public ResponseEntity<Datatable> getListUserImpactSegmentOfCr(
      @RequestBody UserImpactSegmentDTO dto) {
    Datatable data = userImpactSegmentBusiness.getListUserImpactSegmentOfCr(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListImpactSegmentCBB")
  public ResponseEntity<List<ImpactSegmentDTO>> getListImpactSegmentCBB() {
    List<ImpactSegmentDTO> data = userImpactSegmentBusiness.getListImpactSegmentCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListRolesCBB")
  public ResponseEntity<List<RolesDTO>> getListRolesCBB() {
    List<RolesDTO> data = userImpactSegmentBusiness.getListRolesCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
