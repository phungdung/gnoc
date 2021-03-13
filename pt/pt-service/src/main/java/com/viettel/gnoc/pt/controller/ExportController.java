package com.viettel.gnoc.pt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.pt.business.ProblemConfigTimeBusiness;
import com.viettel.gnoc.pt.business.ProblemsBusiness;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
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
  ProblemsBusiness problemsBusiness;

  @Autowired
  ProblemConfigTimeBusiness problemConfigTimeBusiness;
  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file;
    switch (moduleName) {
      case "PT_PROBLEMS_PROCESS":
        ProblemsInsideDTO problemsInsideDTO = mapper
            .readValue(formDataJson, ProblemsInsideDTO.class);
        file = problemsBusiness.getListProblemsSearchExport(problemsInsideDTO);
        break;
      case "PT_PROBLEMS_CONFIG_TIME":
        ProblemConfigTimeDTO problemConfigTimeDTO = mapper
            .readValue(formDataJson, ProblemConfigTimeDTO.class);
        file = problemConfigTimeBusiness.getListProblemConfigTimeSearchExport(problemConfigTimeDTO);
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
