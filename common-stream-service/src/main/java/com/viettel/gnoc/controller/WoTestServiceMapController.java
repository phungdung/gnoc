package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.WoTestServiceMapBussiness;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "WoTestServiceMap")
public class WoTestServiceMapController {

  @Autowired
  private WoTestServiceMapBussiness woTestServiceMapBussiness;

  @PostMapping("/getListWoTestServiceMapDTO/rowStart{rowStart}/maxRow{maxRow}/sortType{sortType}/sortFieldList{sortFieldList}")
  public List<WoTestServiceMapDTO> getListWoTestServiceMapDTO(@RequestBody WoTestServiceMapDTO woTestServiceMapDTO,
      @PathVariable(value="rowStart") int rowStart, @PathVariable(value="maxRow") int maxRow,
      @PathVariable(value="sortType") String sortType, @PathVariable(value="sortFieldList") String sortFieldList) {
    List<WoTestServiceMapDTO> data = woTestServiceMapBussiness.search(woTestServiceMapDTO, rowStart, maxRow, sortType, sortFieldList);
    return data;
  }

  @PostMapping("/insertWoTestServiceMap")
  public ResultDTO insertWoTestServiceMap(@RequestBody WoTestServiceMapDTO woTestServiceMapDTO) {
    try {
      return woTestServiceMapBussiness.insertWoTestServiceMap(woTestServiceMapDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO(null, RESULT.FAIL, RESULT.FAIL);
    }
  }

  @PostMapping("/insertOrUpdateListWoTestServiceMap")
  public String insertOrUpdateListWoTestServiceMap(@RequestBody List<WoTestServiceMapDTO> lsWoTestServiceMapDTOS) {
    try {
      return woTestServiceMapBussiness.insertOrUpdateListWoTestServiceMap(lsWoTestServiceMapDTOS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.FAIL;
    }
  }
}
