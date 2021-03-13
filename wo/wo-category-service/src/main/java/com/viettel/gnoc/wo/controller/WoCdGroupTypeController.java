package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.WoCdGroupTypeBusiness;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.WO_API_PATH_PREFIX + "WoCdGroupType")
public class WoCdGroupTypeController {

  @Autowired
  WoCdGroupTypeBusiness woCdGroupTypeBusiness;

  @PostMapping("/getListWoCdGroupTypeDTO")
  public List<WoCdGroupTypeDTO> getListWoCdGroupTypeDTO(
      @RequestBody WoCdGroupTypeDTO woCdGroupTypeDTO) {
    return woCdGroupTypeBusiness
        .getListWoCdGroupTypeDTO(woCdGroupTypeDTO);
  }

}
