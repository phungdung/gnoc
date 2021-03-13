package com.viettel.gnoc.mr.controller;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHardUnitConfigDTO;
import com.viettel.gnoc.mr.business.MrHardUnitConfigBusiness;
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

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "MrHardUnitConfig")
public class MrHardUnitConfigController {

  @Autowired
  private MrHardUnitConfigBusiness mrHardUnitConfigBusiness;

  @PostMapping("/getListMrHardUnitConfigDTO")
  public ResponseEntity<Datatable> getListMrHardGroupConfigDTO(
      @RequestBody MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    Datatable data = mrHardUnitConfigBusiness.getListMrHardUnitConfigDTO(mrHardUnitConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> insert(@Valid
  @RequestBody MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    ResultInSideDto data = mrHardUnitConfigBusiness.insert(mrHardUnitConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> update(@Valid
  @RequestBody MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    ResultInSideDto data = mrHardUnitConfigBusiness.update(mrHardUnitConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<MrHardUnitConfigDTO> getDetail(Long id) {
    MrHardUnitConfigDTO data = mrHardUnitConfigBusiness.getDetail(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long id) {
    ResultInSideDto data = mrHardUnitConfigBusiness.delete(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB khu vực theo quốc gia
  @GetMapping("/getListRegionByMarketCode")
  public ResponseEntity<List<MrDeviceDTO>> getListRegionByMarketCode(String marketCode) {
    List<MrDeviceDTO> data = mrHardUnitConfigBusiness.getListRegionByMarketCode(marketCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //CBB Loại mạng
  @GetMapping("/getListNetworkTypeByArrayCode")
  public ResponseEntity<List<MrDeviceDTO>> getListNetworkTypeByArrayCode(String arrayCode) {
    List<MrDeviceDTO> data = mrHardUnitConfigBusiness.getListNetworkTypeByArrayCode(arrayCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
