package com.viettel.gnoc.incident.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.incident.business.TroublesIbmBusiness;
import com.viettel.gnoc.incident.dto.ProductIbmDTO;
import com.viettel.gnoc.incident.dto.TroublesIbmDTO;
import com.viettel.gnoc.incident.dto.UnitIbmDTO;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constants.TT_API_PATH_PREFIX + "TroublesIbm")
@Slf4j
public class TroublesIbmController {

  @Autowired
  TroublesIbmBusiness troublesIbmBusiness;

  @PostMapping("/getListTroublesIbmDTO")
  public ResponseEntity<Datatable> getListTroublesIbmDTO(
      @RequestBody TroublesIbmDTO troublesIbmDTO) {
    Datatable data = troublesIbmBusiness.getListTroublesIbmDTO(troublesIbmDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListUnitIbmDTOCombobox")
  public ResponseEntity<List<UnitIbmDTO>> getListUnitIbmDTOCombobox() {
    List<UnitIbmDTO> list = troublesIbmBusiness.getListUnitIbmDTOCombobox();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListProductIbmDTOCombobox")
  public ResponseEntity<List<ProductIbmDTO>> getListProductIbmDTOCombobox() {
    List<ProductIbmDTO> list = troublesIbmBusiness.getListProductIbmDTOCombobox();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @RequestMapping(value = "/insertTroublesIbm", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertTroublesIbm(
      @RequestPart("files") List<MultipartFile> files,
      @Valid @RequestPart("formDataJson") TroublesIbmDTO troublesIbmDTO) throws IOException {
    ResultInSideDto result = troublesIbmBusiness.insertTroublesIbm(files, troublesIbmDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/findById")
  public ResponseEntity<TroublesIbmDTO> findById(Long id) {
    TroublesIbmDTO item = troublesIbmBusiness.findById(id);
    return new ResponseEntity<>(item, HttpStatus.OK);
  }

}
