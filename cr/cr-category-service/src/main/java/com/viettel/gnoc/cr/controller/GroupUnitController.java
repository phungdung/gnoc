package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.cr.business.GroupUnitBusiness;
import com.viettel.gnoc.cr.dto.GroupUnitDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.TT_API_PATH_PREFIX + "GroupUnitController")
public class GroupUnitController {

  @Autowired
  GroupUnitBusiness groupUnitBusiness;

  @PostMapping("/getListGroupUnitDTO")
  public ResponseEntity<Datatable> getListGroupUnitDTO(@RequestBody GroupUnitDTO groupUnitDTO) {
    Datatable data = groupUnitBusiness.getListGroupUnitDTO(groupUnitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.GROUP_UNIT)
  @PostMapping("/insertGroupUnit")
  public ResponseEntity<ResultInSideDto> insertGroupUnit(
      @Valid @RequestBody GroupUnitDTO groupUnitDTO) {
    ResultInSideDto data = groupUnitBusiness.insertGroupUnit(groupUnitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.GROUP_UNIT)
  @PostMapping("/updateGroupUnit")
  public ResponseEntity<ResultInSideDto> updateGroupUnit(
      @Valid @RequestBody GroupUnitDTO groupUnitDTO) {
    ResultInSideDto data = groupUnitBusiness.updateGroupUnit(groupUnitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.GROUP_UNIT)
  @GetMapping("/deleteGroupUnit")
  public ResponseEntity<ResultInSideDto> deleteGroupUnit(@RequestParam Long id) {
    ResultInSideDto data = groupUnitBusiness.deleteGroupUnit(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.GROUP_UNIT)
  @PostMapping("/deleteListGroupUnit")
  public ResponseEntity<ResultInSideDto> deleteListGroupUnit(
      @RequestBody List<GroupUnitDTO> groupUnitDTOS) {
    ResultInSideDto data = groupUnitBusiness.deleteListGroupUnit(groupUnitDTOS);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findGroupUnitById")
  public ResponseEntity<GroupUnitDTO> findGroupUnitById(@RequestParam Long id) {
    GroupUnitDTO data = groupUnitBusiness.findGroupUnitById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.GROUP_UNIT)
  @PostMapping("/updateListGroupUnit")
  public ResponseEntity<ResultInSideDto> updateListGroupUnit(
      @RequestBody List<GroupUnitDTO> groupUnitDTOS) {
    ResultInSideDto data = groupUnitBusiness.updateListGroupUnit(groupUnitDTOS);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


}
