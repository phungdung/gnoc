package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.CfgFtOnTimeBusiness;
import com.viettel.gnoc.wo.dto.CfgFtOnTimeDTO;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.WO_API_PATH_PREFIX + "/cfgFtOnTime")
public class CfgFtOnTimeController {

  @Autowired
  CfgFtOnTimeBusiness cfgFtOnTimeBusiness;

  @Autowired
  CatItemBusiness catItemBusiness;

  @GetMapping("/getListUserByCdGroupCBB")
  public ResponseEntity<List<UsersInsideDto>> getListUserByCdGroupCBB(String woGroupId) {
    List<UsersInsideDto> list = cfgFtOnTimeBusiness.getListUserByCdGroupCBB(woGroupId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(@RequestBody CfgFtOnTimeDTO cfgFtOnTimeDTO) {
    Datatable datatable = cfgFtOnTimeBusiness.onSearch(cfgFtOnTimeDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @GetMapping("/getLstBusinessCBB")
  public ResponseEntity<List<CatItemDTO>> getLstBusinessCBB() {
    List<CatItemDTO> list = cfgFtOnTimeBusiness.getLstBusinessCBB();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdate")
  public ResponseEntity<ResultInSideDto> insertOrUpdate(
      @Valid @RequestBody CfgFtOnTimeDTO cfgFtOnTimeDTO) {
    ResultInSideDto resultInSideDto = cfgFtOnTimeBusiness.insertOrUpdate(cfgFtOnTimeDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<CfgFtOnTimeDTO> getDetail(Long id) {
    CfgFtOnTimeDTO item = cfgFtOnTimeBusiness.findById(id);
    return new ResponseEntity<>(item, HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long id) {
    ResultInSideDto resultInSideDto = cfgFtOnTimeBusiness.delete(id);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/getCurrentCDGroup")
  public ResponseEntity<ResultInSideDto> getCurrentCDGroup() {
    ResultInSideDto item = cfgFtOnTimeBusiness.getWoCdGroupByUnitCurrentSession();
    return new ResponseEntity<>(item, HttpStatus.OK);
  }
}
