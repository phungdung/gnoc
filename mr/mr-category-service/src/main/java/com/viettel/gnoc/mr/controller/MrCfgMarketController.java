package com.viettel.gnoc.mr.controller;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import com.viettel.gnoc.mr.business.MrCfgMarketBusiness;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "mrCfgMarket")
public class MrCfgMarketController {

  @Autowired
  MrCfgMarketBusiness mrCfgMarketBusiness;

  @PostMapping("/getListCfgMarket")
  public ResponseEntity<List<MrCfgMarketDTO>> getListCfgMarket(
      @RequestBody MrCfgMarketDTO mrCfgMarketDTO) {
    List<MrCfgMarketDTO> data = mrCfgMarketBusiness.getListCfgMarket(mrCfgMarketDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateListMarket")
  public ResponseEntity<ResultInSideDto> updateListMarket(
      @RequestBody MrCfgMarketDTO mrCfgMarketDTO) {
    ResultInSideDto data = mrCfgMarketBusiness.updateListMarket(mrCfgMarketDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateListMarketSynIt")
  public ResponseEntity<ResultInSideDto> updateListMarketSynItSoft(
      @RequestBody MrCfgMarketDTO mrCfgMarketDTO) {
    ResultInSideDto data = mrCfgMarketBusiness.updateListMarketSynItSoft(mrCfgMarketDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateListMarketSynItHard")
  public ResponseEntity<ResultInSideDto> updateListMarketSynItHard(
      @RequestBody MrCfgMarketDTO mrCfgMarketDTO) {
    ResultInSideDto data = mrCfgMarketBusiness.updateListMarketSynItHard(mrCfgMarketDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
