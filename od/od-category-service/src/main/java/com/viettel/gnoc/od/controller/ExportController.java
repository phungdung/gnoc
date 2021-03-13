package com.viettel.gnoc.od.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.od.business.OdCfgScheduleCreateBusiness;
import com.viettel.gnoc.od.business.OdChangeStatusBusiness;
import com.viettel.gnoc.od.business.OdTypeBusiness;
import com.viettel.gnoc.od.dto.OdCfgScheduleCreateDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
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
  OdTypeBusiness odTypeBusiness;

  @Autowired
  OdChangeStatusBusiness odChangeStatusBusiness;

  @Autowired
  OdCfgScheduleCreateBusiness odCfgScheduleCreateBusiness;


  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file;
    switch (moduleName) {
      case "OD_TYPE":
        OdTypeDTO odTypeDTO = mapper.readValue(formDataJson, OdTypeDTO.class);
        file = odTypeBusiness.exportData(odTypeDTO);
        break;
      case "OD_CONFIG_BUSINESS":
        OdChangeStatusDTO odChangeStatusDTO = mapper
            .readValue(formDataJson, OdChangeStatusDTO.class);
        file = odChangeStatusBusiness.exportData(odChangeStatusDTO);
        break;
      case "OD_CONFIG_SCHEDULE_CREATE":
        OdCfgScheduleCreateDTO odCfgScheduleCreateDTO = mapper
            .readValue(formDataJson, OdCfgScheduleCreateDTO.class);
        file = odCfgScheduleCreateBusiness.exportData(odCfgScheduleCreateDTO);
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
