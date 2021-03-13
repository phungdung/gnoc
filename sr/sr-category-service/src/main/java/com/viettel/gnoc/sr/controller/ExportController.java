package com.viettel.gnoc.sr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.sr.business.SRCatalogBusiness;
import com.viettel.gnoc.sr.business.SRFlowExecuteBusiness;
import com.viettel.gnoc.sr.business.SRMappingProcessCRBusiness;
import com.viettel.gnoc.sr.business.SRRoleBusiness;
import com.viettel.gnoc.sr.business.SRRoleUserBusiness;
import com.viettel.gnoc.sr.business.SRServiceManageBusiness;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
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
  protected SRRoleUserBusiness srRoleUserBusiness;
  @Autowired
  protected SRRoleBusiness srRoleBusiness;
  @Autowired
  protected SRCatalogBusiness srCatalogBusiness;
  @Autowired
  protected SRServiceManageBusiness srServiceManageBusiness;

  @Autowired
  protected SRMappingProcessCRBusiness srMappingProcessCRBusiness;

  @Autowired
  protected SRFlowExecuteBusiness srFlowExecuteBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file = null;
    switch (moduleName) {
      case "SR_ROLE":
        SRRoleDTO srRoleDTO = mapper.readValue(formDataJson, SRRoleDTO.class);
        file = srRoleBusiness.exportData(srRoleDTO);
        break;
      case "SR_ROLE_USER":
        SRRoleUserInSideDTO srRoleUserDTO = mapper
            .readValue(formDataJson, SRRoleUserInSideDTO.class);
        file = srRoleUserBusiness.exportData(srRoleUserDTO);
        break;
      case "SR_CATALOG":
        SRCatalogDTO srCatalogDTO = mapper.readValue(formDataJson, SRCatalogDTO.class);
        file = srCatalogBusiness.exportData(srCatalogDTO);
        break;
      case "SR_FLOW_EXECUTE":
        SRFlowExecuteDTO srFlowExecuteDTO = mapper.readValue(formDataJson, SRFlowExecuteDTO.class);
        file = srFlowExecuteBusiness.exportSearchData(srFlowExecuteDTO);
        break;
      case "SR_SERVICE_ARRAY":
        SRConfigDTO srServiceArray = mapper.readValue(formDataJson, SRConfigDTO.class);
        file = srServiceManageBusiness.exportDataServiceArray(srServiceArray);
        break;

      case "SR_SERVICE_GROUP":
        SRConfigDTO srServiceGroup = mapper.readValue(formDataJson, SRConfigDTO.class);
        file = srServiceManageBusiness.exportDataServiceGroup(srServiceGroup);
        break;

      case "SR_MAPPING_PROCESS_CR":
        SRMappingProcessCRDTO srMappingProcessCRDTO = mapper
            .readValue(formDataJson, SRMappingProcessCRDTO.class);
        file = srMappingProcessCRBusiness.exportSRMappingProcessCR(srMappingProcessCRDTO);
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
