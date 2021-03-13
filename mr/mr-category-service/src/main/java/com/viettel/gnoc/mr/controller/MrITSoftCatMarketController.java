package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrITSoftCatMarketDTO;
import com.viettel.gnoc.mr.business.MrITSoftCatMarketBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "MrSoftCNTTCatMarket")
public class MrITSoftCatMarketController {

  @Autowired
  private MrITSoftCatMarketBusiness mrITSoftCatMarketBusiness;

  @PostMapping("/getListMrSoftCNTTCatMarket")
  public ResponseEntity<Datatable> getListMrITSoftCatMarket(
      @RequestBody MrITSoftCatMarketDTO mrITSoftCatMarketDTO) {
    return new ResponseEntity<>(mrITSoftCatMarketBusiness
        .getListMrCatMarketSearch(mrITSoftCatMarketDTO), HttpStatus.OK);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResultInSideDto> insert(
      @RequestBody MrITSoftCatMarketDTO mrITSoftCatMarketDTO) {
    ResultInSideDto data = mrITSoftCatMarketBusiness.insert(mrITSoftCatMarketDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ResultInSideDto> update(
      @RequestBody MrITSoftCatMarketDTO mrITSoftCatMarketDTO) {
    ResultInSideDto data = mrITSoftCatMarketBusiness.update(mrITSoftCatMarketDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(String marketCode) {
    ResultInSideDto data = mrITSoftCatMarketBusiness.delete(marketCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
