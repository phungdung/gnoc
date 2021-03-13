package com.viettel.gnoc.od.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.od.business.OdBusiness;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
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
@RequestMapping(Constants.OD_API_PATH_PREFIX + "export")
public class ExportController {

  @Autowired
  CommonBusiness commonBusiness;

  @Autowired
  OdBusiness odBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file;
    switch (moduleName) {
      case "OD_WORKFLOW":
        OdSearchInsideDTO odSearchInsideDTO = mapper
            .readValue(formDataJson, OdSearchInsideDTO.class);
        file = odBusiness.exportData(odSearchInsideDTO);
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
