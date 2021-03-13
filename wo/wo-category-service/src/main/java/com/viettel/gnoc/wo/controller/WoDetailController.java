package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.WoDetailBusiness;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "WoDetail")
@Slf4j
public class WoDetailController {


  @Autowired
  protected WoDetailBusiness woDetailBusiness;

  @PostMapping("/getListWoDetailDTO")
  public List<WoDetailDTO> getListWoDetailDTO(
      @RequestBody WoDetailDTO woDetailDTO) {
    return woDetailBusiness.getListWoDetailDTO(woDetailDTO);
  }
}
