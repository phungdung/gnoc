package com.viettel.gnoc.risk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.risk.business.RiskChangeStatusBusiness;
import com.viettel.gnoc.risk.business.RiskSystemBusiness;
import com.viettel.gnoc.risk.business.RiskTypeBusiness;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.RISK_API_PATH_PREFIX + "export")
public class ExportController {

  @Autowired
  RiskSystemBusiness riskSystemBusiness;

  @Autowired
  RiskTypeBusiness riskTypeBusiness;

  @Autowired
  RiskChangeStatusBusiness riskChangeStatusBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file;
    switch (moduleName) {
      case "RISK_SYSTEM":
        RiskSystemDTO riskSystemDTO = mapper.readValue(formDataJson, RiskSystemDTO.class);
        file = riskSystemBusiness.exportDataRiskSystem(riskSystemDTO);
        break;
      case "RISK_TYPE":
        RiskTypeDTO riskTypeDTO = mapper.readValue(formDataJson, RiskTypeDTO.class);
        file = riskTypeBusiness.exportDataRiskType(riskTypeDTO);
        break;
      case "RISK_CHANGE_STATUS":
        RiskChangeStatusDTO riskChangeStatusDTO = mapper
            .readValue(formDataJson, RiskChangeStatusDTO.class);
        file = riskChangeStatusBusiness.exportDataRiskChangeStatus(riskChangeStatusDTO);
        break;
      default:
        file = null;
        break;
    }
    if (file != null) {
      return FileUtils.responseSourceFromFile(file);
    }
    return null;
  }

}
