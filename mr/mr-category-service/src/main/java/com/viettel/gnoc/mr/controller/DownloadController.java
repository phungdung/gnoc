package com.viettel.gnoc.mr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsDTO;
import com.viettel.gnoc.mr.business.MrCDCheckListBDBusiness;
import com.viettel.gnoc.mr.business.MrCDWorkItemBusiness;
import com.viettel.gnoc.mr.business.MrCauseWoWasCompletedBusiness;
import com.viettel.gnoc.mr.business.MrCfgCrUnitTelBusiness;
import com.viettel.gnoc.mr.business.MrCfgProcedureBtsBusiness;
import com.viettel.gnoc.mr.business.MrCfgProcedureTelHardBusiness;
import com.viettel.gnoc.mr.business.MrCheckListBusiness;
import com.viettel.gnoc.mr.business.MrChecklistBtsBusiness;
import com.viettel.gnoc.mr.business.MrConfigTestXaBusiness;
import com.viettel.gnoc.mr.business.MrDeviceBtsBusiness;
import com.viettel.gnoc.mr.business.MrDeviceBusiness;
import com.viettel.gnoc.mr.business.MrDeviceCDBusiness;
import com.viettel.gnoc.mr.business.MrDeviceSoftBusiness;
import com.viettel.gnoc.mr.business.MrHardDevicesCheckListBusiness;
import com.viettel.gnoc.mr.business.MrHardGroupConfigBusiness;
import com.viettel.gnoc.mr.business.MrHardUnitConfigBusiness;
import com.viettel.gnoc.mr.business.MrITHardProcedureBusiness;
import com.viettel.gnoc.mr.business.MrITHardScheduleBusiness;
import com.viettel.gnoc.mr.business.MrITSoftCrImplUnitBusiness;
import com.viettel.gnoc.mr.business.MrITSoftScheduleBusiness;
import com.viettel.gnoc.mr.business.MrMaterialDisplacementBusiness;
import com.viettel.gnoc.mr.business.MrScheduleBtsBusiness;
import com.viettel.gnoc.mr.business.MrScheduleCDBusiness;
import com.viettel.gnoc.mr.business.MrScheduleTelBusiness;
import com.viettel.gnoc.mr.business.MrSynItHardDevicesBusiness;
import com.viettel.gnoc.mr.business.MrSynItSoftDevicesBusiness;
import com.viettel.gnoc.mr.business.MrTestXaBusiness;
import com.viettel.gnoc.mr.business.MrUserCfgApprovedSmsBtsBusiness;
import com.viettel.security.PassTranformer;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TienNV
 */
@RestController
@Slf4j
@RequestMapping(Constants.MR_API_PATH_PREFIX + "download")
public class DownloadController {

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

  @Autowired
  MrDeviceCDBusiness mrDeviceCDBusiness;

  @Autowired
  MrDeviceBtsBusiness mrDeviceBtsBusiness;

  @Autowired
  MrCDWorkItemBusiness mrCDWorkItemBusiness;

  @Autowired
  MrScheduleBtsBusiness mrScheduleBtsBusiness;

  @Autowired
  MrChecklistBtsBusiness mrChecklistBtsBusiness;

  @Autowired
  MrCheckListBusiness mrCheckListBusiness;

  @Autowired
  MrHardDevicesCheckListBusiness mrHardDevicesCheckListBusiness;

  @Autowired
  MrCfgProcedureBtsBusiness mrCfgProcedureBtsBusiness;

  @Autowired
  MrCfgProcedureTelHardBusiness mrCfgProcedureTelHardBusiness;

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
  MrDeviceBusiness mrDeviceBusiness;

  @Autowired
  MrScheduleCDBusiness mrScheduleCDBusiness;

  @Autowired
  MrCDCheckListBDBusiness mrCDCheckListBDBusiness;

  @Autowired
  MrITSoftCrImplUnitBusiness mrITSoftCrImplUnitBusiness;

  @Autowired
  MrSynItSoftDevicesBusiness mrSynItSoftDevicesBusiness;

  @Autowired
  MrITSoftScheduleBusiness mrITSoftScheduleBusiness;

  @Autowired
  MrITHardProcedureBusiness mrITHardProcedureBusiness;

  @Autowired
  MrITHardScheduleBusiness mrITHardScheduleBusiness;

  @Autowired
  MrSynItHardDevicesBusiness mrSynItHardDevicesBusiness;

  @Autowired
  MrTestXaBusiness mrTestXaBusiness;

  @Autowired
  MrConfigTestXaBusiness mrConfigTestXaBusiness;

  @Autowired
  MrUserCfgApprovedSmsBtsBusiness mrUserCfgApprovedSmsBtsBusiness;

  @Autowired
  MrCauseWoWasCompletedBusiness mrCauseWoWasCompletedBusiness;

  @GetMapping("/onDownloadFileTemplate")
  public ResponseEntity<Resource> onDownloadFileTemplate(String moduleName, String formDataJson)
      throws Exception {
    File file = null;
    ObjectMapper mapper = new ObjectMapper();
    switch (moduleName) {
      case "MR_DEVICE_CD":
        file = mrDeviceCDBusiness.getFileTemplate();
        break;
      case "MR_CD_WORK_ITEM":
        file = mrCDWorkItemBusiness.getTemplate();
        break;
      case "MR_CD_CHECK_LIST_BD":
        file = mrCDCheckListBDBusiness.getTemplate();
        break;
      case "MR_SCHEDULE_CD":
        file = mrScheduleCDBusiness.getFileTemplate();
        break;
      case "MR_DEVICE_BTS":
        MrDeviceBtsDTO mrDeviceBtsDTO = mapper
            .readValue(formDataJson, MrDeviceBtsDTO.class);
        file = mrDeviceBtsBusiness.getFileTemplate(mrDeviceBtsDTO);
        break;
      case "MR_SCHEDULE_BTS":
        MrScheduleBtsDTO mrScheduleBtsDTO = new MrScheduleBtsDTO();
        if (StringUtils.isNotNullOrEmpty(formDataJson)) {
          mrScheduleBtsDTO = mapper
              .readValue(formDataJson, MrScheduleBtsDTO.class);
        }
        file = mrScheduleBtsBusiness.getFileTemplate(mrScheduleBtsDTO);
        break;
      case "MR_CHECKLIST":
        file = mrCheckListBusiness.getFileTemplate();
        break;
      case "MR_HARD_DEVICES_CHECKLIST":
        file = mrHardDevicesCheckListBusiness.getFileTemplate();
        break;
      case "MR_CFG_PROCEDURE_BTS":
        file = mrCfgProcedureBtsBusiness.getFileTemplate();
        break;
      case "MR_CHECKLIST_BTS":  //dunglv commit
        file = mrChecklistBtsBusiness.getFileTemplate();
        break;
      case "MR_MATERIAL":
        file = mrMaterialDisplacementBusiness.getTemplate();
        break;
      case "MR_CFG_PROCEDURE_TEL_HARD":
        file = mrCfgProcedureTelHardBusiness.getFileTemplate();
        break;
      case "MR_SCHEDULE_TEL":
        file = mrScheduleTelBusiness.getTemplate("H");
        break;
      case "MR_HARD_GROUP_CONFIG":
        file = mrHardGroupConfigBusiness.getTemplate();
        break;
      case "MR_HARD_UNIT_CONFIG":
        file = mrHardUnitConfigBusiness.getTemplate();
        break;
      case "MR_CFG_CR_UNIT_TEL":
        file = mrCfgCrUnitTelBusiness.getTemplateImport();
        break;
      case "MR_DEVICE_SOFT":
        file = mrDeviceSoftBusiness.getTemplateImport();
        break;
      case "MR_DEVICE_HARD":
        file = mrDeviceBusiness.getTemplate();
        break;
      case "MR_SCHEDULE_TEL_SOFT":
        file = mrScheduleTelBusiness.getTemplate("S");
        break;
      case "MR_CFG_CR_UNIT_IT":
        file = mrITSoftCrImplUnitBusiness.getTemplateImport();
        break;
      case "MR_SYN_IT_SOFT_DEVICE":
        file = mrSynItSoftDevicesBusiness.getTemplate();
        break;
      case "MR_SYN_IT_HARD_DEVICE":
        file = mrSynItHardDevicesBusiness.getTemplate();
        break;
      case "MR_SCHEDULE_IT_SOFT":
        file = mrITSoftScheduleBusiness.getTemplate("S");
        break;
      case "MR_SCHEDULE_IT_HARD":
        file = mrITHardScheduleBusiness.getTemplate("H");
        break;
      case "MR_CFG_PROCEDURE_IT_HARD":
        file = mrITHardProcedureBusiness.getTemplate();
        break;
      case "MR_TEST_XA":
        file = mrTestXaBusiness.getTemplate();
        break;
      case "MR_CONFIGVALUE":
        file = mrConfigTestXaBusiness.getTemplate();
        break;
      case "MR_USER_CFG_APPROVED_SMS_BTS":
        file = mrUserCfgApprovedSmsBtsBusiness.getFileTemplate();
        break;
      case "MR_CAUSE_WO_WAS_NOT_COMPLETED":
        file = mrCauseWoWasCompletedBusiness.getTemplate();
        break;
      default:
        break;
    }
    if (file != null) {
      return FileUtils.responseSourceFromFile(file);
    }
    return null;
  }

  @PostMapping("/onDownloadFileByPath")
  public ResponseEntity<Resource> onDownloadFileByPath(@RequestBody ResultInSideDto resultInSideDto)
      throws Exception {
    FTPClient ftpClient = FTPUtil.connectionFTPClient(ftpServer, ftpPort,
        PassTranformer.decrypt(ftpUser), PassTranformer.decrypt(ftpPass));
    ResponseEntity<Resource> resourceResponseEntity = FileUtils
        .responseSourceFromFile(ftpClient, resultInSideDto.getFilePath());
    FTPUtil.disconnectionFTPClient(ftpClient);
    return resourceResponseEntity;
  }

  @PostMapping("/onDownloadFileTempByPath")
  public ResponseEntity<Resource> onDownloadFileTempByPath(
      @RequestBody ResultInSideDto resultInSideDto)
      throws Exception {
    File file = new File(resultInSideDto.getFilePath());
    return FileUtils.responseSourceFromFile(file);
  }

  @GetMapping("/onViewImageByPath")
  public ResponseEntity<Resource> onViewImageByPath(String filePath)
      throws Exception {
    FTPClient ftpClient = FTPUtil.connectionFTPClient(ftpServer, ftpPort,
        PassTranformer.decrypt(ftpUser), PassTranformer.decrypt(ftpPass));
    ResponseEntity<Resource> resourceResponseEntity = FileUtils
        .responseSourceFromFile(ftpClient, filePath);
    FTPUtil.disconnectionFTPClient(ftpClient);
    return resourceResponseEntity;
  }
}
