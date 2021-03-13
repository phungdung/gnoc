package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.business.CfgChildArrayBusiness;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DungPV
 */
@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CfgChildArray")
public class CfgChildArrayController {

  @Autowired
  protected CfgChildArrayBusiness cfgChildArrayBusiness;

  @PostMapping("/getListCfgChildArray")
  public ResponseEntity<Datatable> getListCfgChildArray(
      @RequestBody CfgChildArrayDTO cfgChildArrayDTO) {
    Datatable data = cfgChildArrayBusiness.getListCfgChildArray(cfgChildArrayDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListImpactSegmentCBB")
  public ResponseEntity<List<ImpactSegmentDTO>> getListImpactSegmentCBB() {
    List<ImpactSegmentDTO> data = cfgChildArrayBusiness.getListImpactSegmentCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_CHILD_ARRAY)
  @DeleteMapping("/delete")
  public ResponseEntity<ResultInSideDto> deleteCfgChildArray(Long childrenId) {
    ResultInSideDto data = cfgChildArrayBusiness.delete(childrenId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<CfgChildArrayDTO> getDetail(Long childrenId) {
    CfgChildArrayDTO data = cfgChildArrayBusiness.getDetail(childrenId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_CHILD_ARRAY)
  @PostMapping("/add")
  public ResponseEntity<ResultInSideDto> addCfgChildArray(
      @Valid @RequestBody CfgChildArrayDTO cfgChildArrayDTO) {
    ResultInSideDto data = cfgChildArrayBusiness.add(cfgChildArrayDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.CONFIG_CHILD_ARRAY)
  @PutMapping("/update")
  public ResponseEntity<ResultInSideDto> updateCfgChildArray(
      @Valid @RequestBody CfgChildArrayDTO cfgChildArrayDTO) {
    ResultInSideDto data = cfgChildArrayBusiness.update(cfgChildArrayDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getCbbChildArray")
  public ResponseEntity<List<CfgChildArrayDTO>> getCbbChildArray(
      @RequestBody CfgChildArrayDTO cfgChildArrayDTO) {
    if (StringUtils.isNotNullOrEmpty(cfgChildArrayDTO.getProxyLocale())) {
      I18n.setLocale(cfgChildArrayDTO.getProxyLocale());
    }
    List<CfgChildArrayDTO> data = cfgChildArrayBusiness.getCbbChildArray(cfgChildArrayDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
