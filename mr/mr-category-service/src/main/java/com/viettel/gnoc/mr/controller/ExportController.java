package com.viettel.gnoc.mr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.*;
import com.viettel.gnoc.mr.business.*;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "export")
public class ExportController {

  @Autowired
  MrDeviceCDBusiness mrDeviceCDBusiness;

  @Autowired
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Autowired
  MrHardCdBusiness mrHardCdBusiness;

  @Autowired
  MrDeviceBtsBusiness mrDeviceBtsBusiness;

  @Autowired
  MrScheduleCdHisBusiness mrScheduleCdHisBusiness;

  @Autowired
  MrScheduleCDBusiness mrScheduleCDBusiness;

  @Autowired
  MrScheduleBtsBusiness mrScheduleBtsBusiness;

  @Autowired
  SearchDeviceNIMSBusiness searchDeviceNIMSBusiness;

  @Autowired
  MrChecklistBtsBusiness mrChecklistBtsBusiness;

  @Autowired
  MrCheckListBusiness mrCheckListBusiness;

  @Autowired
  MrHardDevicesCheckListBusiness mrHardDevicesCheckListBusiness;

  @Autowired
  MrMaterialDisplacementBusiness mrMaterialDisplacementBusiness;

  @Autowired
  MrScheduleTelBusiness mrScheduleTelBusiness;

  @Autowired
  MrCfgProcedureBtsBusiness mrCfgProcedureBtsBusiness;

  @Autowired
  MrScheduleTelHisBusiness mrScheduleTelHisBusiness;

  @Autowired
  MaintenanceMngtBusiness maintenanceMngtBusiness;

  @Autowired
  MrScheduleTelHisSoftBusiness mrScheduleTelHisSoftBusiness;

  @Autowired
  MrUngCuuTTBusiness mrUngCuuTTBusiness;

  @Autowired
  MrHardGroupConfigBusiness mrHardGroupConfigBusiness;

  @Autowired
  MrHardUnitConfigBusiness mrHardUnitConfigBusiness;

  @Autowired
  MrCfgProcedureTelBusiness mrCfgProcedureTelBusiness;

  @Autowired
  MrCfgProcedureTelHardBusiness mrCfgProcedureTelHardBusiness;

  @Autowired
  MrCfgCrUnitTelBusiness mrCfgCrUnitTelBusiness;

  @Autowired
  MrScheduleBtsHisBusiness mrScheduleBtsHisBusiness;

  @Autowired
  MrDeviceBusiness mrDeviceBusiness;

  @Autowired
  MrDeviceSoftBusiness mrDeviceSoftBusiness;

  @Autowired
  MrCDCheckListBDBusiness mrCDCheckListBDBusiness;

  @Autowired
  MrITSoftScheduleBusiness mrITSoftScheduleBusiness;

  @Autowired
  MrITHisBusiness mrITHisBusiness;

  @Autowired
  MrITSoftCrImplUnitBusiness mrITSoftCrImplUnitBusiness;

  @Autowired
  MrSynItSoftDevicesBusiness mrSynItSoftDevicesBusiness;

  @Autowired
  MrSynItHardDevicesBusiness mrSynItHardDevicesBusiness;

  @Autowired
  MrITHardProcedureBusiness mrITHardProcedureBusiness;

  @Autowired
  MrITSoftProcedureBusiness mrITSoftProcedureBusiness;

  @Autowired
  MrITHardScheduleBusiness mrITHardScheduleBusiness;

  @Autowired
  MrTestXaBusiness mrTestXaBusiness;

  @Autowired
  MrConfigTestXaBusiness mrConfigTestXaBusiness;

  @Autowired
  MrUserCfgApprovedSmsBtsBusiness mrUserCfgApprovedSmsBtsBusiness;

  @Autowired
  MrCauseWoWasCompletedBusiness mrCauseWoWasCompletedBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file;
    switch (moduleName) {
      case "MR_DEVICE_CD":
        MrDeviceCDDTO mrDeviceCDDTO = mapper
            .readValue(formDataJson, MrDeviceCDDTO.class);
        file = mrDeviceCDBusiness.exportSearchData(mrDeviceCDDTO);
        break;

      case "MR_CFG_PROCEDURE_CD":
        MrCfgProcedureCDDTO mrCfgProcedureCDDTO = mapper
            .readValue(formDataJson, MrCfgProcedureCDDTO.class);
        file = mrCfgProcedureCDBusiness.exportSearchData(mrCfgProcedureCDDTO);
        break;

      case "MR_HARD_CD":
        MrHardCDDTO mrHardCDDTO = mapper
            .readValue(formDataJson, MrHardCDDTO.class);
        file = mrHardCdBusiness.exportSearchData(mrHardCDDTO);
        break;

      case "MR_DEVICE_BTS":
        MrDeviceBtsDTO mrDeviceBtsDTO = mapper
            .readValue(formDataJson, MrDeviceBtsDTO.class);
        file = mrDeviceBtsBusiness.exportSearchData(mrDeviceBtsDTO);
        break;

      case "MR_SCHEDULE_CD_HIS":
        MrScheduleCdHisDTO mrScheduleCdHisDTO = mapper
            .readValue(formDataJson, MrScheduleCdHisDTO.class);
        file = mrScheduleCdHisBusiness.exportSearchData(mrScheduleCdHisDTO);
        break;

      case "SEARCH_DEVICE_ON_NIMS":
        SearchDeviceNIMSDTO searchDeviceNIMSDTO = mapper
            .readValue(formDataJson, SearchDeviceNIMSDTO.class);
        file = searchDeviceNIMSBusiness.exportSearchData(searchDeviceNIMSDTO);
        break;

      case "MR_SCHEDULE_CD":
        MrScheduleCdDTO dto = mapper
            .readValue(formDataJson, MrScheduleCdDTO.class);
        file = mrScheduleCDBusiness.exportSearchData(dto);
        break;

      case "MR_SCHEDULE_BTS":
        MrScheduleBtsDTO mrScheduleBtsDTO = mapper
            .readValue(formDataJson, MrScheduleBtsDTO.class);
        file = mrScheduleBtsBusiness.exportSearchData(mrScheduleBtsDTO);
        break;

      case "MR_CHECKLIST":
        MrCheckListDTO mrCheckListDTO = mapper
            .readValue(formDataJson, MrCheckListDTO.class);
        file = mrCheckListBusiness.exportSearchData(mrCheckListDTO);
        break;
      case "MR_HARD_DEVICES_CHECKLIST":
        MrHardDevicesCheckListDTO mrHardDevicesCheckListDTO = mapper
            .readValue(formDataJson, MrHardDevicesCheckListDTO.class);
        file = mrHardDevicesCheckListBusiness.exportSearchData(mrHardDevicesCheckListDTO);
        break;
      case "MR_CHECKLIST_BTS":
        MrChecklistsBtsDTO mrChecklistsBtsDTO = mapper
            .readValue(formDataJson, MrChecklistsBtsDTO.class);
        file = mrChecklistBtsBusiness.exportDataMrChecklistBts(mrChecklistsBtsDTO);
        break;
      case "MR_MATERIAL":
        MrMaterialDTO mrMaterialDTO = mapper
            .readValue(formDataJson, MrMaterialDTO.class);
        file = mrMaterialDisplacementBusiness.exportData(mrMaterialDTO);
        break;
      case "MR_SCHEDULE_TEL":
        MrScheduleTelDTO mrScheduleTelDTO = mapper
            .readValue(formDataJson, MrScheduleTelDTO.class);
        file = mrScheduleTelBusiness.exportData(mrScheduleTelDTO);
        break;

      case "MR_SCHEDULE_TEL_HIS":
        MrScheduleTelHisDTO mrScheduleTelHisDTO = mapper
            .readValue(formDataJson, MrScheduleTelHisDTO.class);
        file = mrScheduleTelHisBusiness.exportSearchData(mrScheduleTelHisDTO);
        break;

      case "MR_CFG_PROCEDURE_BTS":
        MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO = mapper
            .readValue(formDataJson, MrCfgProcedureBtsDTO.class);
        file = mrCfgProcedureBtsBusiness.exportSearchData(mrCfgProcedureBtsDTO);
        break;

      case "MR_MAINTENANCE_MNGT":
        MrSearchDTO mrSearchDTO = mapper.readValue(formDataJson, MrSearchDTO.class);
        file = maintenanceMngtBusiness.exportSearchData(mrSearchDTO);
        break;

      case "MR_SCHEDULE_TEL_HIS_SOFT":
        MrScheduleTelHisDTO telHisDTO = mapper.readValue(formDataJson, MrScheduleTelHisDTO.class);
        file = mrScheduleTelHisSoftBusiness.exportDataMrScheduleTelHisSoft(telHisDTO);
        break;

      case "MR_UCTT":
        MrUngCuuTTDTO mrUngCuuTTDTO = mapper.readValue(formDataJson, MrUngCuuTTDTO.class);
        file = mrUngCuuTTBusiness.exportSearchData(mrUngCuuTTDTO);
        break;

      case "MR_HARD_GROUP_CONFIG":
        MrHardGroupConfigDTO mrHardGroupConfigDTO = mapper
            .readValue(formDataJson, MrHardGroupConfigDTO.class);
        file = mrHardGroupConfigBusiness.exportData(mrHardGroupConfigDTO);
        break;

      case "MR_HARD_UNIT_CONFIG":
        MrHardUnitConfigDTO mrHardUnitConfigDTO = mapper
            .readValue(formDataJson, MrHardUnitConfigDTO.class);
        file = mrHardUnitConfigBusiness.exportData(mrHardUnitConfigDTO);
        break;

      case "MR_CFG_PROCEDURE_TEL":
        MrCfgProcedureTelDTO mrCfgProcedureTelDTO = mapper
            .readValue(formDataJson, MrCfgProcedureTelDTO.class);
        file = mrCfgProcedureTelBusiness.exportSearchData(mrCfgProcedureTelDTO);
        break;
      case "MR_CFG_PROCEDURE_TEL_HARD":
        MrCfgProcedureTelDTO mrCfgProcedureTelHardDTO = mapper
            .readValue(formDataJson, MrCfgProcedureTelDTO.class);
        file = mrCfgProcedureTelHardBusiness.exportSearchData(mrCfgProcedureTelHardDTO);
        break;

      case "MR_CFG_CR_UNIT_TEL":
        MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO = mapper
            .readValue(formDataJson, MrCfgCrUnitTelDTO.class);
        file = mrCfgCrUnitTelBusiness.exportDataMrCfgCrUnitTel(mrCfgCrUnitTelDTO);
        break;

      case "MR_SCHEDULE_BTS_HIS":
        MrScheduleBtsHisDTO mrScheduleBtsHisDTO = mapper
            .readValue(formDataJson, MrScheduleBtsHisDTO.class);
        file = mrScheduleBtsHisBusiness.exportData(mrScheduleBtsHisDTO);
        break;

      case "MR_DEVICE_HARD":
        MrDeviceDTO mrDeviceDTO = mapper
            .readValue(formDataJson, MrDeviceDTO.class);
        file = mrDeviceBusiness.exportData(mrDeviceDTO);
        break;

      case "MR_SCHEDULE_TEL_SOFT":
        MrScheduleTelDTO mrScheduleTelSoftDTO = mapper
            .readValue(formDataJson, MrScheduleTelDTO.class);
        file = mrScheduleTelBusiness.exportSoftData(mrScheduleTelSoftDTO);
        break;

      case "MR_DEVICE_SOFT":
        MrDeviceDTO mrDeviceSoftDTO = mapper
            .readValue(formDataJson, MrDeviceDTO.class);
        file = mrDeviceSoftBusiness.exportDataMrDeviceSoft(mrDeviceSoftDTO);
        break;

      case "MR_CD_CHECK_LIST_BD":
        MrCDCheckListBDDTO mrCDCheckListBDDTO = mapper
            .readValue(formDataJson, MrCDCheckListBDDTO.class);
        file = mrCDCheckListBDBusiness.exportData(mrCDCheckListBDDTO);
        break;
      case "MR_IT_SOFT_SCHEDULE":
        MrITSoftScheduleDTO mrITSoftScheduleDTO = mapper
            .readValue(formDataJson, MrITSoftScheduleDTO.class);
        file = mrITSoftScheduleBusiness.exportData(mrITSoftScheduleDTO);
        break;
      case "MR_IT_HARD_SCHEDULE":
        MrITHardScheduleDTO mrITHardScheduleDTO = mapper
            .readValue(formDataJson, MrITHardScheduleDTO.class);
        file = mrITHardScheduleBusiness.exportData(mrITHardScheduleDTO);
        break;
      case "MR_IT_SOFT_HISTORY":
        MrScheduleITHisDTO mrScheduleITHisDTO = mapper
            .readValue(formDataJson, MrScheduleITHisDTO.class);
        file = mrITHisBusiness.exportData(mrScheduleITHisDTO);
        break;
      case "MR_IT_HARD_HISTORY":
        MrScheduleITHisDTO mrItHardHistory = mapper
            .readValue(formDataJson, MrScheduleITHisDTO.class);
        file = mrITHisBusiness.exportDataMrHardITHis(mrItHardHistory);
        break;
      case "MR_CFG_CR_UNIT_IT":
        MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO = mapper
            .readValue(formDataJson, MrITSoftCrImplUnitDTO.class);
        file = mrITSoftCrImplUnitBusiness.exportData(mrITSoftCrImplUnitDTO);
        break;
      case "MR_SYN_IT_SOFT_DEVICE":
        MrSynItDevicesDTO mrSynItSoftDevicesDTO = mapper
            .readValue(formDataJson, MrSynItDevicesDTO.class);
        file = mrSynItSoftDevicesBusiness.ExportData(mrSynItSoftDevicesDTO);
        break;
      case "MR_SYN_IT_HARD_DEVICE":
        MrSynItDevicesDTO mrSynItHardDevicesDTO = mapper
            .readValue(formDataJson, MrSynItDevicesDTO.class);
        file = mrSynItHardDevicesBusiness.ExportData(mrSynItHardDevicesDTO);
        break;
      case "MR_CFG_PROCEDURE_IT_HARD":
        MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO = mapper
            .readValue(formDataJson, MrCfgProcedureITHardDTO.class);
        file = mrITHardProcedureBusiness.exportData(mrCfgProcedureITHardDTO);
        break;
      case "MR_CFG_PROCEDURE_IT_SOFT":
        MrITSoftProcedureDTO mrITSoftProcedureDTO = mapper
            .readValue(formDataJson, MrITSoftProcedureDTO.class);
        file = mrITSoftProcedureBusiness.exportData(mrITSoftProcedureDTO);
        break;
      case "MR_TEST_XA":
        MrCdBatteryDTO mrCdBatteryDTO = mapper
            .readValue(formDataJson, MrCdBatteryDTO.class);
        file = mrTestXaBusiness.exportData(mrCdBatteryDTO);
        break;
      case "MR_CONFIGVALUE":
        MrConfigTestXaDTO mrConfigTestXaDTO = mapper
            .readValue(formDataJson, MrConfigTestXaDTO.class);
        file = mrConfigTestXaBusiness.exportData(mrConfigTestXaDTO);
        break;
      case "MR_USER_CFG_APPROVED_SMS_BTS":
        MrUserCfgApprovedSmsBtsDTO mrUserCfgApprovedSmsBtsDTO = mapper
            .readValue(formDataJson, MrUserCfgApprovedSmsBtsDTO.class);
        file = mrUserCfgApprovedSmsBtsBusiness.exportData(mrUserCfgApprovedSmsBtsDTO);
        break;
      case "MR_CAUSE_WO_WAS_NOT_COMPLETED":
        MrCauseWoWasCompletedDTO mrCauseWoWasCompletedDTO = mapper
            .readValue(formDataJson, MrCauseWoWasCompletedDTO.class);
        file = mrCauseWoWasCompletedBusiness.exportData(mrCauseWoWasCompletedDTO);
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
