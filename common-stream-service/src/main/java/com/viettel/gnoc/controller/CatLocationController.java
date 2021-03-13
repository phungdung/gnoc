package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "CatLocationService")
public class CatLocationController {

  @Autowired
  private CatLocationBusiness catLocationBusiness;

  @PostMapping("/getCatLocationByLevel")
  public ResponseEntity<List<CatLocationDTO>> getCatLocationByLevel(String level) {
    List<CatLocationDTO> data = catLocationBusiness.getCatLocationByLevel(level);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getCatLocationByParentId")
  public ResponseEntity<List<CatLocationDTO>> getCatLocationByParentId(String parentId) {
    List<CatLocationDTO> data = catLocationBusiness.getCatLocationByParentId(parentId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListLocationProvince")
  public ResponseEntity<List<CatLocationDTO>> getListLocationProvince() {
    List<CatLocationDTO> catLocationDTOS = catLocationBusiness.getListLocationProvince();
    return new ResponseEntity<>(catLocationDTOS, HttpStatus.OK);
  }

  @GetMapping("/getListLocationByLevelCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListLocationByLevelCBB(Long level,
      Long parentId) {
    List<ItemDataCRInside> catLocationDTOS = catLocationBusiness
        .getListLocationByLevelCBB(null, level, parentId);
    return new ResponseEntity<>(catLocationDTOS, HttpStatus.OK);
  }

  @GetMapping("/getListLocationByLevelCBBProxy/level{level}/parentId{parentId}")
  public ResponseEntity<List<ItemDataCRInside>> getListLocationByLevelCBBProxy(
      @PathVariable(value = "level") Long level, @PathVariable(value = "parentId") Long parentId) {
    List<ItemDataCRInside> catLocationDTOS = catLocationBusiness
        .getListLocationByLevelCBB(null, level, parentId);
    return new ResponseEntity<>(catLocationDTOS, HttpStatus.OK);
  }
}
