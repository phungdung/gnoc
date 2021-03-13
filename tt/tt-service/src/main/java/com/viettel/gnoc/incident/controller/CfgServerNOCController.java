package com.viettel.gnoc.incident.controller;

import com.viettel.gnoc.commons.dto.WSNocprov4DTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.incident.business.CfgServerNOCBusiness;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.TT_API_PATH_PREFIX + "CfgServerNOC")
public class CfgServerNOCController {

  @Autowired
  private CfgServerNOCBusiness cfgServerNOCBusiness;

  @PostMapping("/getListCfgServerNocByCondition")
  public List<CfgServerNocDTO> getListCfgServerNocByCondition(@RequestBody
      WSNocprov4DTO wsNocprov4DTO) {
    return cfgServerNOCBusiness
        .getListCfgServerNocByCondition(wsNocprov4DTO.getLstCondition(),
            wsNocprov4DTO.getRowStart(), wsNocprov4DTO.getMaxRow(), wsNocprov4DTO.getSortType(),
            wsNocprov4DTO.getSortFieldList());
  }
}
