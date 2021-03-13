package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.business.CrApprovalDepartmentBusiness;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
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
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrApprovalDepartmentService")
@Slf4j
public class CrApprovalDepartmentController {

  @Autowired
  CrApprovalDepartmentBusiness crApprovalDepartmentBusiness;

  @PostMapping("/searchSql")
  public ResponseEntity<Datatable> searchSql(@RequestBody CrApprovalDepartmentInsiteDTO dto) {
    Datatable list = crApprovalDepartmentBusiness
        .searchSQL(dto);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getApprovalDepartmentByProcess")
  public ResponseEntity<List<CrApprovalDepartmentInsiteDTO>> getApprovalDepartmentByProcess(
      Long crProcessId) {
    List<CrApprovalDepartmentInsiteDTO> list = crApprovalDepartmentBusiness
        .getApprovalDepartmentByProcess(crProcessId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }
}
