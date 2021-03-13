package com.viettel.gnoc.controller;


import com.viettel.gnoc.business.CfgRequireHaveWoBussiness;
import com.viettel.gnoc.commons.dto.CfgRequireHaveWoDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
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

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CfgRequireHaveWo")
public class CfgRequireHaveWoController {

  @Autowired
  private CfgRequireHaveWoBussiness cfgRequireHaveWoBussiness;

  //get list WoFileTemp
  @PostMapping("/getListCfgRequireHaveWo")
  public ResponseEntity<Datatable> getListCfgRequireHaveWo(
      @RequestBody CfgRequireHaveWoDTO cfgRequireHaveWoDTO) {
    Datatable data = cfgRequireHaveWoBussiness.getListCfgRequireHaveWo(cfgRequireHaveWoDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // get Detail
  @GetMapping("/getDetail")
  public ResponseEntity<CfgRequireHaveWoDTO> getDetail(Long id) {
    CfgRequireHaveWoDTO data = cfgRequireHaveWoBussiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //insert WoFileTemp
//  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.REQUIRE_HAVE_WO)
  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> insert(
      @Valid @RequestBody CfgRequireHaveWoDTO cfgRequireHaveWoDTO) {
    ResultInSideDto resultInSideDto = cfgRequireHaveWoBussiness.insert(cfgRequireHaveWoDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  //update WoFileTemp
//  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.REQUIRE_HAVE_WO)
  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> update(
      @Valid @RequestBody CfgRequireHaveWoDTO dto) {
    ResultInSideDto data = cfgRequireHaveWoBussiness.update(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //delete WoFileTemp by id
//  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.REQUIRE_HAVE_WO)
  @PostMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(@RequestParam Long id) {
    ResultInSideDto data = cfgRequireHaveWoBussiness.delete(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getReasonDTOForTree")
  public ResponseEntity<List<CatReasonDTO>> getReasonDTOForTree(
      @RequestBody CatReasonDTO catReasonDTO) {
    List<CatReasonDTO> data = cfgRequireHaveWoBussiness.getReasonDTOForTree(catReasonDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
