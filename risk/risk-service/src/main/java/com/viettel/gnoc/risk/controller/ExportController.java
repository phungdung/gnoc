package com.viettel.gnoc.risk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.risk.business.RiskBusiness;
import com.viettel.gnoc.risk.dto.RiskDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.RISK_API_PATH_PREFIX + "export")
public class ExportController {

  @Autowired
  RiskBusiness riskBusiness;

  @Autowired
  CommonBusiness commonBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file;
    switch (moduleName) {
      case "RISK":
        RiskDTO riskDTO = mapper.readValue(formDataJson, RiskDTO.class);
        try {
          file = riskBusiness.exportDataRisk(riskDTO);
          break;
        } catch (Exception e) {
          HttpHeaders headers = new HttpHeaders();
          List<String> customHeaders = new ArrayList<>();
          customHeaders.add("Error-Message");
          customHeaders.add("maxRow");
          headers.setAccessControlExposeHeaders(customHeaders);
          ConfigPropertyDTO cfg = commonBusiness.getConfigPropertyByKey();
          headers.set("Error-Message", "maxRowExport");
          headers.set("maxRow", cfg.getValue());
           return ResponseEntity.ok()
              .headers(headers)
              .body(null);
        }
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
