package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrMaterialDTO;
import com.viettel.gnoc.maintenance.dto.MrMaterialDisplacementDTO;
import com.viettel.gnoc.mr.business.MrMaterialDisplacementBusiness;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrMaterialDisplacement")
public class MrMaterialDisplacementController {

  @Autowired
  MrMaterialDisplacementBusiness mrMaterialDisplacementBusiness;

  @PostMapping("/getListMrMaterialDisplacementDTO")
  public ResponseEntity<List<MrMaterialDisplacementDTO>> getListMrMaterialDisplacementDTO(
      @RequestBody MrMaterialDisplacementDTO mrMaterialDisplacementDTO) {
    List<MrMaterialDisplacementDTO> data = mrMaterialDisplacementBusiness
        .getListMrMaterialDisplacementDTO(mrMaterialDisplacementDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListMrMaterialDTO2")
  public ResponseEntity<Datatable> getListMrMaterialDTO2(@RequestBody MrMaterialDTO mrMaterialDTO) {
    Datatable datatable = mrMaterialDisplacementBusiness
        .getListMrMaterialDTO2(mrMaterialDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<MrMaterialDTO> getDetail(Long id) {
    MrMaterialDTO dataResult = mrMaterialDisplacementBusiness
        .getDetail(id);
    return new ResponseEntity<>(dataResult, HttpStatus.OK);
  }

  @PostMapping("/insertMrMaterial")
  public ResponseEntity<ResultInSideDto> insertMrMaterial(@Valid
  @RequestBody MrMaterialDTO mrMaterialDTO) {
    ResultInSideDto resultInSideDto = mrMaterialDisplacementBusiness
        .insertMrMaterial(mrMaterialDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PutMapping("/updateMrMaterial")
  public ResponseEntity<ResultInSideDto> updateMrMaterial(@Valid
  @RequestBody MrMaterialDTO mrMaterialDTO) {
    ResultInSideDto resultInSideDto = mrMaterialDisplacementBusiness
        .updateMrMaterial(mrMaterialDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @DeleteMapping("/deleteMrMaterial")
  public ResponseEntity<ResultInSideDto> deleteMrMaterial(Long id) {
    ResultInSideDto resultInSideDto = mrMaterialDisplacementBusiness
        .deleteMrMaterial(id);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/getDeviceTypeCBB")
  public ResponseEntity<Map<String, String>> getDeviceTypeCBB() {
    return new ResponseEntity<>(mrMaterialDisplacementBusiness
        .getDeviceTypeCBB(), HttpStatus.OK);
  }
}
