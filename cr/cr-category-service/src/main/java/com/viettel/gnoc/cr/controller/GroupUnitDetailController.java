package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.GroupUnitDetailBusiness;
import com.viettel.gnoc.cr.dto.GroupUnitDetailDTO;
import com.viettel.gnoc.cr.dto.GroupUnitDetailNameDTO;
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

@Slf4j
@RestController
@RequestMapping(Constants.TT_API_PATH_PREFIX + "GroupUnitDetailController")
public class GroupUnitDetailController {

  @Autowired
  GroupUnitDetailBusiness groupUnitDetailBusiness;

  @Autowired
  UnitBusiness unitBusiness;

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.GROUP_DEPARTMENT_CONFIG)
  @PostMapping("/insertGroupUnitDetail")
  public ResponseEntity<ResultInSideDto> insertGroupUnitDetail(
      @Valid @RequestBody GroupUnitDetailDTO groupUnitDetailDTO) {
    ResultInSideDto data = groupUnitDetailBusiness.insertGroupUnitDetail(groupUnitDetailDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.GROUP_DEPARTMENT_CONFIG)
  @PostMapping("/updateGroupUnitDetail")
  public ResponseEntity<ResultInSideDto> updateGroupUnitDetail(
      @Valid @RequestBody GroupUnitDetailDTO groupUnitDetailDTO) {
    ResultInSideDto data = groupUnitDetailBusiness.updateGroupUnitDetail(groupUnitDetailDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.GROUP_DEPARTMENT_CONFIG)
  @GetMapping("/deleteGroupUnitDetail")
  public ResponseEntity<ResultInSideDto> deleteGroupUnitDetail(Long id) {
    ResultInSideDto data = groupUnitDetailBusiness.deleteGroupUnitDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.GROUP_DEPARTMENT_CONFIG)
  @PostMapping("/deleteListGroupUnitDetail")
  public ResponseEntity<ResultInSideDto> deleteListGroupUnitDetail(
      @RequestBody List<GroupUnitDetailDTO> groupUnitDetailListDTO) {
    ResultInSideDto data = groupUnitDetailBusiness
        .deleteListGroupUnitDetail(groupUnitDetailListDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findeGroupUnitDetailById")
  public ResponseEntity<GroupUnitDetailDTO> findeGroupUnitDetailById(Long Id) {
    GroupUnitDetailDTO data = groupUnitDetailBusiness.findById(Id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //search
  @PostMapping("/getListUnitOfGroup")
  public ResponseEntity<Datatable> getListUnitOfGroup(
      @RequestBody GroupUnitDetailNameDTO groupUnitDetailNameDTO) {
    Datatable data = groupUnitDetailBusiness.getListUnitOfGroup(groupUnitDetailNameDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //search List ID
  @PostMapping("/getIDGroup")
  public ResponseEntity<Datatable> getIDGroup(@RequestBody GroupUnitDetailDTO groupUnitDetailDTO) {
    Datatable data = groupUnitDetailBusiness.getIDGroup(groupUnitDetailDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //Đơn vị
  @PostMapping("/getListUnit")
  public ResponseEntity<List<UnitDTO>> getListUnit(@RequestBody UnitDTO unitDTO) {
    List<UnitDTO> data = unitBusiness.getListUnit(unitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
