package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatServiceBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.WoMaterialBusiness;
import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDTO;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "woMaterial")
@Slf4j
public class WoMaterialController {

  @Autowired
  protected WoMaterialBusiness woMaterialBusiness;

  @Autowired
  protected CatItemBusiness catItemBusiness;

  @Autowired
  protected CatServiceBusiness catServiceBusiness;

  @PostMapping("/listWoMaterial")
  public ResponseEntity<Datatable> getListWoMaterialPage(
      @RequestBody MaterialThresInsideDTO materialThresDTO) {
    Datatable data = woMaterialBusiness.getListWoMaterialPage(materialThresDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long materialThresId) {
    ResultInSideDto data = woMaterialBusiness.delete(materialThresId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> add(
      @Valid @RequestBody MaterialThresInsideDTO materialThresDTO) {
    ResultInSideDto data = woMaterialBusiness.insert(materialThresDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> edit(
      @Valid @RequestBody MaterialThresInsideDTO materialThresDTO) {
    ResultInSideDto data = woMaterialBusiness.update(materialThresDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //categoryCode WO_ACTION_GROUP
  @PostMapping("/getItemAction")
  public ResponseEntity<List<CatItemDTO>> getItemAction(String categoryCode, String itemCode) {
    List<CatItemDTO> data = catItemBusiness.getListItemByCategory(categoryCode, itemCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/GetDetailByMaterialThresId")
  public ResponseEntity<MaterialThresInsideDTO> GetDetailByMaterialThresId(Long materialThresId) {
    MaterialThresInsideDTO data = woMaterialBusiness.findByMaterialThresId(materialThresId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findAllMaterialByName")
  public ResponseEntity<List<WoMaterialDTO>> findAllMaterialByName(String materialName) {
    List<WoMaterialDTO> data = woMaterialBusiness.findAllMaterial(materialName);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findWoMaterialById/materialId{materialId}")
  public WoMaterialDTO findWoMaterialById(@PathVariable Long materialId) {
    WoMaterialDTO dto = woMaterialBusiness.findWoMaterialById(materialId);
    return dto;
  }

}
