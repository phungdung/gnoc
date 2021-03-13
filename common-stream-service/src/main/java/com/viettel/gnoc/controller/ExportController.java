package com.viettel.gnoc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.business.CfgBusinessCallSmsBusiness;
import com.viettel.gnoc.business.CfgRequestScheduleBusiness;
import com.viettel.gnoc.business.EmployeeDayOffBusiness;
import com.viettel.gnoc.business.LanguageExchangeBussiness;
import com.viettel.gnoc.business.MapProbToKedbBussiness;
import com.viettel.gnoc.business.ShiftHandoverBusiness;
import com.viettel.gnoc.business.UnitCommonBusiness;
import com.viettel.gnoc.business.WoCdTempBusiness;
import com.viettel.gnoc.commons.business.*;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.EmployeeDayOffDTO;
import com.viettel.gnoc.cr.dto.RequestScheduleDTO;
import com.viettel.gnoc.cr.dto.ShiftCrDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkDTO;
import com.viettel.gnoc.wo.dto.WoCdTempInsideDTO;
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
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "export")
public class ExportController {

  @Autowired
  DeviceTypeVersionBusiness deviceTypeVersionBusiness;

  @Autowired
  TransitionStateConfigBusiness transitionStateConfigBusiness;

  @Autowired
  private EmployeeDayOffBusiness employeeDayOffBusiness1;

  @Autowired
  CfgRequestScheduleBusiness cfgRequestScheduleBusiness1;

  @Autowired
  private ShiftHandoverBusiness shiftHandoverBusiness1;

  @Autowired
  private CatCfgClosedTicketBusiness catCfgClosedTicketBusiness;
  @Autowired
  WoCdTempBusiness woCdTempBusiness;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  UnitCommonBusiness unitCommonBusiness;

  @Autowired
  LanguageExchangeBussiness languageExchangeBussiness;

  @Autowired
  CfgBusinessCallSmsBusiness cfgBusinessCallSmsBusiness;

  @Autowired
  MapProbToKedbBussiness mapProbToKedbBussiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  CfgRoleDataBusiness cfgRoleDataBusiness;

  @Autowired
  CommonBusiness commonBusiness;

  @Autowired
  TroubleImportantBusiness troubleImportantBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file;
    switch (moduleName) {
      case "DEVICE_TYPE_VERSION":
        DeviceTypeVersionDTO deviceTypeVersionDTO = mapper
            .readValue(formDataJson, DeviceTypeVersionDTO.class);
        file = deviceTypeVersionBusiness.exportData(deviceTypeVersionDTO);
        break;
      case "TRANSITION_STATE_CONFIG":
        TransitionStateConfigDTO transitionStateConfigDTO = mapper
            .readValue(formDataJson, TransitionStateConfigDTO.class);
        file = transitionStateConfigBusiness.exportData(transitionStateConfigDTO);
        break;
      case "EXPORT_EMPLOYEE_DAY_OFF":
        EmployeeDayOffDTO dto = mapper.readValue(formDataJson, EmployeeDayOffDTO.class);
        file = employeeDayOffBusiness1.exportData(dto);
        break;
      case "EXPORT_SCHEDULE_CR_BEFORE":
        RequestScheduleDTO requestScheduleDTO = mapper
            .readValue(formDataJson, RequestScheduleDTO.class);
        file = cfgRequestScheduleBusiness1.exportData(requestScheduleDTO);
        break;
      case "EXPORT_SCHEDULE_CR_AFTER":
        RequestScheduleDTO dto4 = mapper.readValue(formDataJson, RequestScheduleDTO.class);
        file = cfgRequestScheduleBusiness1.exportDataCRAfter(dto4);
        break;
      case "EXPORT_SHIFT_HANDOVER_DATA":
        ShiftHandoverDTO dto3 = mapper.readValue(formDataJson, ShiftHandoverDTO.class);
        file = shiftHandoverBusiness1.exportData(dto3);
        break;
      case "EXPORT_SHIFT_HANDOVER_WORK_LIST":
        ShiftWorkDTO shiftWorkDTO = mapper.readValue(formDataJson, ShiftWorkDTO.class);
        file = shiftHandoverBusiness1.exportShiftWorkData(shiftWorkDTO);
        break;
      case "EXPORT_SHIFT_HANDOVER_CR_LIST":
        ShiftCrDTO shiftCrDTO = mapper.readValue(formDataJson, ShiftCrDTO.class);
        file = shiftHandoverBusiness1.exportShiftCrData(shiftCrDTO);
        break;
      case "EXPORT_SHIFT_HANDOVER_ROW":
        ShiftHandoverDTO shiftHandoverDTO = mapper.readValue(formDataJson, ShiftHandoverDTO.class);
        file = shiftHandoverBusiness1.exportShiftRow(shiftHandoverDTO);
        break;
      case "EXPORT_CAT_CFG_CLOSED_TICKET":
        CatCfgClosedTicketDTO catCfgClosedTicketDTO = mapper
            .readValue(formDataJson, CatCfgClosedTicketDTO.class);
        file = catCfgClosedTicketBusiness.exportCatCfgClosedTicket(catCfgClosedTicketDTO);
        break;
      case "EXPORT_LANGUAGE_EXCHANGE":
        LanguageExchangeDTO languageExchangeDTO = mapper
            .readValue(formDataJson, LanguageExchangeDTO.class);
        file = languageExchangeBussiness.exportLanguageExchange(languageExchangeDTO);
        break;
      case "EXPORT_LANGUAGE_EXCHANGE_NOT_CONFIG":
        languageExchangeDTO = mapper
            .readValue(formDataJson, LanguageExchangeDTO.class);
        file = languageExchangeBussiness.exportLanguageExchangeNotConfig(languageExchangeDTO);
        break;
      case "WO_CD_TEMP":
        WoCdTempInsideDTO woCdTempInsideDTO = mapper
            .readValue(formDataJson, WoCdTempInsideDTO.class);
        file = woCdTempBusiness.exportData(woCdTempInsideDTO);
        break;
      case "EXPORT_CATITEM":
        CatItemDTO catItemDTO = mapper.readValue(formDataJson, CatItemDTO.class);
        file = catItemBusiness.exportData(catItemDTO);
        break;
      case "EXPORT_UNIT_COMMON":
        UnitDTO unitDTO = mapper.readValue(formDataJson, UnitDTO.class);
        file = unitCommonBusiness.exportData(unitDTO);
        break;
      case "CFG_BUSINESS_CALL_SMS":
        CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO = mapper
            .readValue(formDataJson, CfgBusinessCallSmsDTO.class);
        file = cfgBusinessCallSmsBusiness.exportData(cfgBusinessCallSmsDTO);
        break;
      case "MAP_PROB_TO_KEDB":
        MapProbToKedbDTO mapProbToKedbDTO = mapper
            .readValue(formDataJson, MapProbToKedbDTO.class);
        file = mapProbToKedbBussiness.exportData(mapProbToKedbDTO);
        break;
      case "EXPORT_EMPLOYEEE":
        UsersInsideDto usersInsideDto = mapper.readValue(formDataJson, UsersInsideDto.class);
        file = userBusiness.exportData(usersInsideDto);
        break;
      case "EMPLOYEE_APPROVE":
        UsersInsideDto usersInside = mapper.readValue(formDataJson, UsersInsideDto.class);
        file = userBusiness.exportApproveUsersData(usersInside);
        break;
      case "CFG_ROLE_DATA":
        CfgRoleDataDTO cfgRoleDataDTO = mapper.readValue(formDataJson, CfgRoleDataDTO.class);
        file = cfgRoleDataBusiness.exportData(cfgRoleDataDTO);
        break;
      case "EXPORT_HIS_CHANGE":
        HisUserImpactDTO hisUserImpactDTO = mapper.readValue(formDataJson, HisUserImpactDTO.class);
        file = commonBusiness.exportData(hisUserImpactDTO);
        break;
      case "EXPORT_TROUBLE_IMPORTANT":
        TroubleImportantDTO troubleImportantDTO = mapper.readValue(formDataJson, TroubleImportantDTO.class);
        file = troubleImportantBusiness.exportData(troubleImportantDTO);
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
