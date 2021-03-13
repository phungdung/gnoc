package com.viettel.gnoc.cr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.business.CfgTempImportBusiness;
import com.viettel.gnoc.cr.business.CrAffectedLevelBusiness;
import com.viettel.gnoc.cr.business.CrAlarmSettingBusiness;
import com.viettel.gnoc.cr.business.CrCabUsersBusiness;
import com.viettel.gnoc.cr.business.CrImpactSegmentBusiness;
import com.viettel.gnoc.cr.business.CrManagerProcessBusiness;
import com.viettel.gnoc.cr.business.CrManagerScopeBusiness;
import com.viettel.gnoc.cr.business.CrManagerScopesOfRolesBusiness;
import com.viettel.gnoc.cr.business.CrManagerUnitsOfScopeBusiness;
import com.viettel.gnoc.cr.business.DeviceTypesBusiness;
import com.viettel.gnoc.cr.business.GroupUnitBusiness;
import com.viettel.gnoc.cr.business.GroupUnitDetailBusiness;
import com.viettel.gnoc.cr.dto.CrAffectedLevelDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopesOfRolesDTO;
import com.viettel.gnoc.cr.dto.CrManagerUnitsOfScopeDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.DeviceTypesDTO;
import com.viettel.gnoc.cr.dto.GroupUnitDTO;
import com.viettel.gnoc.cr.dto.GroupUnitDetailNameDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.TempImportDTO;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.TT_API_PATH_PREFIX + "export")
public class ExportController {

  @Autowired
  protected CrManagerScopesOfRolesBusiness crManagerScopesOfRolesBusiness;

  @Autowired
  protected CrManagerUnitsOfScopeBusiness crManagerUnitsOfScopeBusiness;

  @Autowired
  protected CrAffectedLevelBusiness crAffectedLevelBusiness;

  @Autowired
  protected CrImpactSegmentBusiness crImpactSegmentBusiness;

  @Autowired
  protected CrAlarmSettingBusiness crAlarmSettingBusiness;

  @Autowired
  DeviceTypesBusiness deviceTypesBusiness;

  @Autowired
  GroupUnitDetailBusiness groupUnitDetailBusiness;

  @Autowired
  GroupUnitBusiness groupUnitBusiness;

  @Autowired
  CrManagerScopeBusiness crManagerScopeBusiness;

  @Autowired
  CrCabUsersBusiness crCabUsersBusiness;

  @Autowired
  CrManagerProcessBusiness crManagerProcessBusiness;

  @Autowired
  CfgTempImportBusiness cfgTempImportBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file = null;
    switch (moduleName) {
      case "CR_MANAGER_SCOPES_OF_ROLES":
        CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO = mapper
            .readValue(formDataJson, CrManagerScopesOfRolesDTO.class);
        file = crManagerScopesOfRolesBusiness.exportData(crManagerScopesOfRolesDTO);
        break;
      case "CR_MANAGER_UNITS_OF_SCOPE":
        CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO = mapper
            .readValue(formDataJson, CrManagerUnitsOfScopeDTO.class);
        file = crManagerUnitsOfScopeBusiness.exportData(crManagerUnitsOfScopeDTO);
        break;
      case "AFFECTED_LEVEL":
        CrAffectedLevelDTO crAffectedLevelDTO = mapper
            .readValue(formDataJson, CrAffectedLevelDTO.class);
        file = crAffectedLevelBusiness.exportData(crAffectedLevelDTO);
        break;
      case "IMPACT_SEGMENT":
        ImpactSegmentDTO impactSegmentDTO = mapper.readValue(formDataJson, ImpactSegmentDTO.class);
        file = crImpactSegmentBusiness.exportData(impactSegmentDTO);
        break;
      case "CR_ALARM_SETTTING":
        CrAlarmSettingDTO crAlarmSettingDTO = mapper
            .readValue(formDataJson, CrAlarmSettingDTO.class);
        file = crAlarmSettingBusiness.exportData(crAlarmSettingDTO);
        break;
      case "GROUP_UNIT_DETAIL":
        GroupUnitDetailNameDTO groupUnitDetailNameDTO = mapper
            .readValue(formDataJson, GroupUnitDetailNameDTO.class);
        file = groupUnitDetailBusiness.exportData(groupUnitDetailNameDTO);
        break;
      case "DEVICE_TYPES":
        DeviceTypesDTO deviceTypesDTO = mapper.readValue(formDataJson, DeviceTypesDTO.class);
        file = deviceTypesBusiness.exportData(deviceTypesDTO);
        break;
      case "GROUP_UNIT":
        GroupUnitDTO groupUnitDTO = mapper.readValue(formDataJson, GroupUnitDTO.class);
        file = groupUnitBusiness.exportDataListGroupUnit(groupUnitDTO);
        break;
      case "CR_MANAGERSCOPE":
        CrManagerScopeDTO crManagerScopeDTO = mapper
            .readValue(formDataJson, CrManagerScopeDTO.class);
        file = crManagerScopeBusiness.exportData(crManagerScopeDTO);
        break;
      case "CR_CABUSERS":
        CrCabUsersDTO crCabUsersDTO = mapper.readValue(formDataJson, CrCabUsersDTO.class);
        file = crCabUsersBusiness.exportData(crCabUsersDTO);
        break;
      case "CR_PROCESS_MANAGER":
        CrProcessInsideDTO crProcessDTO = mapper.readValue(formDataJson, CrProcessInsideDTO.class);
        file = crManagerProcessBusiness.exportData(crProcessDTO);
        break;
      case "CR_TEMP_IMPORT":
        TempImportDTO tempImportDTO = mapper.readValue(formDataJson, TempImportDTO.class);
        file = cfgTempImportBusiness.exportData(tempImportDTO);
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
