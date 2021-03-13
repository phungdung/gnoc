package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureCDDTO;
import com.viettel.gnoc.mr.business.MrCfgProcedureCDBusiness;
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


/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrCfgProcedureCDService")
public class MrCfgProcedureCDController {

  @Autowired
  private MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;


  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(@RequestBody MrCfgProcedureCDDTO mrCfgProcedureCDDTO) {
    Datatable datatable = mrCfgProcedureCDBusiness.onSearch(mrCfgProcedureCDDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @GetMapping("/getMrSubCategory")
  public ResponseEntity<List<CatItemDTO>> getMrSubCategory() {
    List<CatItemDTO> data = mrCfgProcedureCDBusiness.getMrSubCategory();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getMrPriority")
  public ResponseEntity<List<CatItemDTO>> getMrPriority() {
    List<CatItemDTO> data = mrCfgProcedureCDBusiness.getMrPriority();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getMrImpact")
  public ResponseEntity<List<CatItemDTO>> getMrImpact() {
    List<CatItemDTO> data = mrCfgProcedureCDBusiness.getMrImpact();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/onInsert")
  public ResponseEntity<ResultInSideDto> onInsert(@Valid @RequestBody MrCfgProcedureCDDTO dto) {
    ResultInSideDto result = mrCfgProcedureCDBusiness.onInsertOrUpdate(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/onUpdate")
  public ResponseEntity<ResultInSideDto> onUpdate(@Valid @RequestBody MrCfgProcedureCDDTO dto) {
    ResultInSideDto result = mrCfgProcedureCDBusiness.onInsertOrUpdate(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getOgListWorks")
  public ResponseEntity<List<CatItemDTO>> getOgListWorks(Long itemId) {
    List<CatItemDTO> result = mrCfgProcedureCDBusiness.getOgListWorks(itemId);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/findById")
  public ResponseEntity<MrCfgProcedureCDDTO> findById(Long id) {
    MrCfgProcedureCDDTO data = mrCfgProcedureCDBusiness.findById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @DeleteMapping("/deleteById")
  public ResponseEntity<ResultInSideDto> deleteById(Long id) {
    ResultInSideDto data = mrCfgProcedureCDBusiness.deleteById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
