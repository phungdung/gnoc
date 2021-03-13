package com.viettel.gnoc.cr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.business.CrAlarmBusiness;
import com.viettel.gnoc.cr.business.CrBusiness;
import com.viettel.gnoc.cr.business.CrImpactedNodesBusiness;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.CR_API_PATH_PREFIX + "export")
public class ExportController {

  @Autowired
  CrBusiness crBusiness;

  @Autowired
  CrAlarmBusiness crAlarmBusiness;

  @Autowired
  CrImpactedNodesBusiness crImpactedNodesBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file;
    switch (moduleName) {
      case "CR":
        CrInsiteDTO crInsiteDTO = mapper
            .readValue(formDataJson, CrInsiteDTO.class);
        file = crBusiness.exportSearchData(crInsiteDTO);
        break;
      case "CR_ALARM":
        List<CrAlarmSettingDTO> lstCasId = Arrays
            .asList(mapper.readValue(formDataJson, CrAlarmSettingDTO[].class));
        file = crAlarmBusiness.exportDataNew(lstCasId);
        break;
      case "IMPACT_NODES":
        List<CrImpactedNodesDTO> lstImpact = Arrays
            .asList(mapper.readValue(formDataJson, CrImpactedNodesDTO[].class));
        file = crImpactedNodesBusiness.exportData(lstImpact);
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
