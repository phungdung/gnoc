package com.viettel.gnoc.pt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.pt.business.CfgProblemTimeProcessBusiness;
import com.viettel.gnoc.pt.dto.CfgProblemTimeProcessDTO;
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
@RequestMapping(Constants.PT_API_PATH_PREFIX + "export")
public class ExportController {

  @Autowired
  CfgProblemTimeProcessBusiness cfgProblemTimeProcessBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file;
    switch (moduleName) {
      case "PT_CFG_TIME_PROCESS":
        CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO = mapper
            .readValue(formDataJson, CfgProblemTimeProcessDTO.class);
        file = cfgProblemTimeProcessBusiness.exportData(cfgProblemTimeProcessDTO);
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
