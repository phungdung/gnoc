package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.WSNocprov4DTO;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.wo.business.WoVSmartBusiness;
import com.viettel.gnoc.wo.dto.CfgWoTickHelpDTO;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping(Constants.WO_API_PATH_PREFIX + "WoVsmart")
public class WoVsmartController {

  @Autowired
  WoVSmartBusiness woVSmartBusiness;

  @Autowired
  TtServiceProxy ttServiceProxy;

  @PostMapping("/updateCfgWoTickHelpVsmart")
  public ResponseEntity<ResultDTO> updateCfgWoTickHelpVsmart(
      @RequestBody CfgWoTickHelpDTO cfgWoTickHelpDTO) {
    ResultDTO result = woVSmartBusiness.updateCfgWoTickHelpVsmart(cfgWoTickHelpDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/testImDirection")
  public ResponseEntity<List> testImDirection(
      String nationCode) throws Exception {
    List<ConditionBean> lstCondition = new ArrayList<ConditionBean>();
    lstCondition.add(new ConditionBean("insertSource", "NOC_" + nationCode, Constants.NAME_EQUAL,
        Constants.STRING));
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    List<CfgServerNocDTO> result = ttServiceProxy
        .getListCfgServerNocByCondition(new WSNocprov4DTO(lstCondition, 0, 100, "", ""));
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
