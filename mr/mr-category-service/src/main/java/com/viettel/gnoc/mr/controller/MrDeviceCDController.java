package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.mr.business.MrDeviceCDBusiness;
import java.util.List;
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
@RequestMapping(Constants.MR_API_PATH_PREFIX + "MrDeviceCDService")
public class MrDeviceCDController {

  @Autowired
  private MrDeviceCDBusiness mrDeviceCDBusiness;

  @GetMapping("/getComboboxDeviceType")
  public ResponseEntity<List<MrDeviceCDDTO>> getComboboxDeviceType() {
    List<MrDeviceCDDTO> data = mrDeviceCDBusiness.getComboboxDeviceType();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getComboboxStationCode")
  public ResponseEntity<List<MrDeviceCDDTO>> getComboboxStationCode() {
    List<MrDeviceCDDTO> data = mrDeviceCDBusiness.getComboboxStationCode();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(@RequestBody MrDeviceCDDTO mrDeviceCDDTO) {
    Datatable datatable = mrDeviceCDBusiness.onSearch(mrDeviceCDDTO);
    if (datatable == null) {
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

}
