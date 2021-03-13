package com.viettel.gnoc.mr.controller;

import com.google.gson.Gson;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.mr.business.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TienNV
 */
@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "import")
public class ImportController {

  @Autowired
  MrDeviceCDBusiness mrDeviceCDBusiness;

  @Autowired
  MrCDWorkItemBusiness mrCDWorkItemBusiness;

  @Autowired
  MrScheduleCDBusiness mrScheduleCDBusiness;

  @Autowired
  MrScheduleBtsBusiness mrScheduleBtsBusiness;

  @Autowired
  MrDeviceBtsBusiness mrDeviceBtsBusiness;

  @Autowired
  MrChecklistBtsBusiness mrChecklistBtsBusiness;

  @Autowired
  MrCheckListBusiness mrCheckListBusiness;

  @Autowired
  MrHardDevicesCheckListBusiness mrHardDevicesCheckListBusiness;

  @Autowired
  MrCfgProcedureBtsBusiness mrCfgProcedureBtsBusiness;

  @Autowired
  MrMaterialDisplacementBusiness mrMaterialDisplacementBusiness;

  @Autowired
  MrHardGroupConfigBusiness mrHardGroupConfigBusiness;

  @Autowired
  MrHardUnitConfigBusiness mrHardUnitConfigBusiness;

  @Autowired
  MrScheduleTelBusiness mrScheduleTelBusiness;

  @Autowired
  MrCfgCrUnitTelBusiness mrCfgCrUnitTelBusiness;

  @Autowired
  MrDeviceSoftBusiness mrDeviceSoftBusiness;

  @Autowired
  MrCDCheckListBDBusiness mrCDCheckListBDBusiness;

  @Autowired
  MrCfgProcedureTelHardBusiness mrCfgProcedureTelHardBusiness;

  @Autowired
  MrDeviceBusiness mrDeviceBusiness;

  @Autowired
  MrITSoftCrImplUnitBusiness mrITSoftCrImplUnitBusiness;

  @Autowired
  MrITSoftScheduleBusiness mrITSoftScheduleBusiness;

  @Autowired
  MrSynItSoftDevicesBusiness mrSynItSoftDevicesBusiness;

  @Autowired
  MrITHardScheduleBusiness mrITHardScheduleBusiness;

  @Autowired
  MrITHardProcedureBusiness mrITHardProcedureBusiness;

  @Autowired
  MrSynItHardDevicesBusiness mrSynItHardDevicesBusiness;

  @Autowired
  MrTestXaBusiness mrTestXaBusiness;

  @Autowired
  MrUserCfgApprovedSmsBtsBusiness mrUserCfgApprovedSmsBtsBusiness;

  @Autowired
  MrConfigTestXaBusiness mrConfigTestXaBusiness;

  @Autowired
  MrCauseWoWasCompletedBusiness mrCauseWoWasCompletedBusiness;

  @PostMapping("/onImportFile")
  public ResponseEntity<Resource> onImportFile(MultipartFile file,
      List<MultipartFile> filesAttachment, String formDataJson, String moduleName)
      throws Exception {
    ResultInSideDto data = new ResultInSideDto();
    switch (moduleName) {
      case "MR_DEVICE_CD":
        data = mrDeviceCDBusiness.importMrDeviceCD(file);
        break;
      case "MR_CD_WORK_ITEM":
        data = mrCDWorkItemBusiness.importMrCDWorkItem(file);
        break;
      case "MR_MATERIAL":
        data = mrMaterialDisplacementBusiness.importData(file);
        break;
      case "MR_CFG_CR_UNIT_TEL":
        data = mrCfgCrUnitTelBusiness.importDataMrCfgCrUnitTel(file);
        break;
      case "MR_DEVICE_SOFT":
        data = mrDeviceSoftBusiness.importDataMrDeviceSoft(file);
        break;
      case "MR_CD_CHECK_LIST_BD":
        data = mrCDCheckListBDBusiness.importMrCDCheckListBD(file);
        break;
      case "MR_SCHEDULE_CD":
        data = mrScheduleCDBusiness.importMrScheduleCD(file);
        break;
      case "MR_CHECKLIST":
        data = mrCheckListBusiness.importMrCheckList(file);
        break;
      case "MR_HARD_DEVICES_CHECKLIST":
        data = mrHardDevicesCheckListBusiness.importMrCheckList(file);
        break;
      case "MR_SCHEDULE_BTS":
        data = mrScheduleBtsBusiness.importMrScheduleBTS(file);
        break;
      case "MR_DEVICE_BTS":
        data = mrDeviceBtsBusiness.importMrDeviceBts(file);
        break;
      case "MR_CFG_PROCEDURE_BTS":
        data = mrCfgProcedureBtsBusiness.importMrCfgProcedureBTS(file);
        break;
      case "MR_SCHEDULE_TEL":
        data = mrScheduleTelBusiness.importData(file);
        break;
      case "MR_CFG_PROCEDURE_TEL_HARD":
        data = mrCfgProcedureTelHardBusiness.importMrCfgProcedureTelHard(file);
        break;
      case "MR_HARD_GROUP_CONFIG":
        data = mrHardGroupConfigBusiness.importData(file);
        break;
      case "MR_HARD_UNIT_CONFIG":
        data = mrHardUnitConfigBusiness.importData(file);
        break;
      case "MR_SCHEDULE_TEL_SOFT":
        data = mrScheduleTelBusiness.importSoftData(file);
        break;
      case "MR_DEVICE_HARD":
        data = mrDeviceBusiness.importData(file);
        break;
      case "MR_CFG_CR_UNIT_IT":
        data = mrITSoftCrImplUnitBusiness.importDataMrCfgCrUnitIT(file);
        break;
      case "MR_SCHEDULE_IT_SOFT":
        data = mrITSoftScheduleBusiness.importData(file);
        break;
      case "MR_SCHEDULE_IT_HARD":
        data = mrITHardScheduleBusiness.importData(file);
        break;
      case "MR_SYN_IT_SOFT_DEVICE":
        data = mrSynItSoftDevicesBusiness.importData(file);
        break;
      case "MR_SYN_IT_HARD_DEVICE":
        data = mrSynItHardDevicesBusiness.importData(file);
        break;
      case "MR_CFG_PROCEDURE_IT_HARD":
        data = mrITHardProcedureBusiness.importData(file);
        break;
      case "MR_CHECKLIST_BTS": //dunglv commit
        data = mrChecklistBtsBusiness.importData(file);
        break;
      case "MR_TEST_XA":
        data = mrTestXaBusiness.importData(file);
        break;
      case "MR_USER_CFG_APPROVED_SMS_BTS":
        data = mrUserCfgApprovedSmsBtsBusiness.importData(file);
        break;
      case "MR_CONFIGVALUE":
        data = mrConfigTestXaBusiness.importData(file);
        break;
      case "MR_CAUSE_WO_WAS_NOT_COMPLETED":
        data = mrCauseWoWasCompletedBusiness.importData(file);
        break;
      default:
        data.setKey(RESULT.ERROR);
        break;
    }
    InputStreamResource resource = null;
    String message = null;
    long contentLength = 0;

    File fileResponse;
    String fileName = "";
    if (StringUtils.isNotNullOrEmpty(data.getFilePath())) {
      fileResponse = new File(data.getFilePath());
      resource = new InputStreamResource(new FileInputStream(fileResponse));
      fileName = fileResponse.getName();
      contentLength = fileResponse.length();
    }
    if (RESULT.SUCCESS.equals(data.getKey())) {
    } else if (RESULT.FILE_IS_NULL.equals(data.getKey())
        || RESULT.FILE_INVALID_FORMAT.equals(data.getKey())
        || RESULT.DATA_OVER.equals(data.getKey())
        || RESULT.ERROR.equals(data.getKey())
        || RESULT.NODATA.equals(data.getKey())
        || "ERROR_NO_DOWNLOAD".equals(data.getKey())) {
      message = data.getMessage();
    } else if (RESULT.DUPLICATE.equals(data.getKey())) {
      data.setKey(RESULT.ERROR);
      message = data.getMessage();
    }

    Gson gson = new Gson();
    Map<String, Object> objectMap = new HashMap<>();
    objectMap.put("key", data.getKey());
    objectMap.put("message", message);
    objectMap.put("fileName", fileName);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType
        .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    List<String> customHeaders = new ArrayList<>();
    customHeaders.add("Content-Disposition");
    headers.setAccessControlExposeHeaders(customHeaders);
    headers.setContentDispositionFormData("attachment", URLEncoder
        .encode(gson.toJson(objectMap), "UTF-8").replace("+", "%20"));
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(contentLength)
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(resource);
  }
}
