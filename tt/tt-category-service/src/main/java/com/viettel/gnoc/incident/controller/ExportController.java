package com.viettel.gnoc.incident.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.incident.business.CfgTimeTroubleProcessBusiness;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TungPV
 */
@RestController
@Slf4j
@RequestMapping(Constants.TT_API_PATH_PREFIX + "export")
public class ExportController {

  @Autowired
  CfgTimeTroubleProcessBusiness cfgTimeTroubleProcessBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file;
    switch (moduleName) {
      case "CFG_TIME_TROUBLE_PROCESS_MANAGEMENT":
        CfgTimeTroubleProcessDTO dto = mapper
            .readValue(formDataJson, CfgTimeTroubleProcessDTO.class);
        file = cfgTimeTroubleProcessBusiness.exportData(dto);
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
