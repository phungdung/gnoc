package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.TransitionStateConfigBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TransitionStateConfigDTO;
import com.viettel.gnoc.commons.utils.Constants;
import java.io.File;
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


/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "TransitionStateConfig")
public class TransitionStateConfigController {

  @Autowired
  private TransitionStateConfigBusiness transitionStateConfigBusiness;

  @PostMapping("/onSearch")
  public ResponseEntity<List<TransitionStateConfigDTO>> onSearch(
      @RequestBody TransitionStateConfigDTO dto) {
    List<TransitionStateConfigDTO> lst = transitionStateConfigBusiness.onSearch(dto);
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }

  @PostMapping("/getListTransitionStateConfigDTO")
  public ResponseEntity<Datatable> getListTransitionStateConfigDTO(
      @RequestBody TransitionStateConfigDTO transitionStateConfigDTO) {
    Datatable data = transitionStateConfigBusiness
        .getListTransitionStateConfigDTO(transitionStateConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findTransitionStateConfigById")
  public ResponseEntity<TransitionStateConfigDTO> findTransitionStateConfigById(Long id) {
    TransitionStateConfigDTO data = transitionStateConfigBusiness
        .findTransitionStateConfigById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertTransitionStateConfig")
  public ResponseEntity<ResultInSideDto> insertTransitionStateConfig(
      @Valid @RequestBody TransitionStateConfigDTO transitionStateConfigDTO) {
    ResultInSideDto data = transitionStateConfigBusiness
        .insertTransitionStateConfig(transitionStateConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateTransitionStateConfig")
  public ResponseEntity<ResultInSideDto> updateTransitionStateConfig(
      @Valid @RequestBody TransitionStateConfigDTO transitionStateConfigDTO) {
    ResultInSideDto data = transitionStateConfigBusiness
        .updateTransitionStateConfig(transitionStateConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteTransitionStateConfig")
  public ResponseEntity<ResultInSideDto> deleteTransitionStateConfig(Long id) {
    ResultInSideDto result = transitionStateConfigBusiness.deleteTransitionStateConfig(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/exportTransitionStateConfig")
  public ResponseEntity<File> exportTransitionStateConfig(
      @RequestBody TransitionStateConfigDTO transitionStateConfigDTO) throws Exception {
    File data = transitionStateConfigBusiness.exportData(transitionStateConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListProcess")
  public ResponseEntity<List<CatItemDTO>> getListProcess() {
    List<CatItemDTO> data = transitionStateConfigBusiness.getListProcess();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListState")
  public ResponseEntity<List<CatItemDTO>> getListState(Long process) {
    List<CatItemDTO> data = transitionStateConfigBusiness.getListState(process);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
