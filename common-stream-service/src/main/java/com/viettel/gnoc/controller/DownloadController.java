package com.viettel.gnoc.controller;

import com.viettel.gnoc.business.CfgBusinessCallSmsBusiness;
import com.viettel.gnoc.business.EmployeeDayOffBusiness;
import com.viettel.gnoc.business.LanguageExchangeBussiness;
import com.viettel.gnoc.business.MappingVsaUnitBusiness;
import com.viettel.gnoc.business.ShiftHandoverBusiness;
import com.viettel.gnoc.business.UnitCommonBusiness;
import com.viettel.gnoc.business.WoCdTempBusiness;
import com.viettel.gnoc.commons.business.CfgRoleDataBusiness;
import com.viettel.gnoc.commons.business.DeviceTypeVersionBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
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
 * @author TungPV
 */
@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "download")
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
  protected DeviceTypeVersionBusiness deviceTypeVersionBusiness;

  @Autowired
  private EmployeeDayOffBusiness employeeDayOffBusiness1;

  @Autowired
  private ShiftHandoverBusiness shiftHandoverBusiness;

  @Autowired
  WoCdTempBusiness woCdTempBusiness;

  @Autowired
  UnitCommonBusiness unitCommonBusiness;

  @Autowired
  MappingVsaUnitBusiness mappingVsaUnitBusiness;

  @Autowired
  LanguageExchangeBussiness languageExchangeBussiness;

  @Autowired
  CfgBusinessCallSmsBusiness cfgBusinessCallSmsBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  CfgRoleDataBusiness cfgRoleDataBusiness;

  @GetMapping("/onDownloadFileTemplate")
  public ResponseEntity<Resource> onDownloadFileTemplate(String moduleName)
      throws Exception {
    File file;
    switch (moduleName) {
      case "DEVICE_TYPE_VERSION":
        file = deviceTypeVersionBusiness.getTemplate();
        break;
      case "EMPLOYEE_DAY_OFF_IMPORT_FILE":
        file = employeeDayOffBusiness1.getTemplate();
        break;
      case "IMPORT_SHIFT_WORK":
        file = shiftHandoverBusiness.getTemplate();
        break;
      case "CR_IMPORT_FILE":
        file = shiftHandoverBusiness.getTemplateCR();
        break;
      case "WO_CD_TEMP":
        file = woCdTempBusiness.getTemplate();
        break;
      case "IMPORT_UNIT":
        file = unitCommonBusiness.getUnitTemplate();
        break;
      case "IMPORT_MAPPING_VSA_UNIT":
        file = mappingVsaUnitBusiness.getVsaTemplate();
        break;
      case "IMPORT_MAPPING_VSA_UNIT_REF":
        file = mappingVsaUnitBusiness.getVsaTemplateReference();
        break;
      case "LANGUAGE_EXCHANGE":
        file = languageExchangeBussiness.getTemplate();
        break;
      case "CFG_BUSINESS_CALL_SMS":
        file = cfgBusinessCallSmsBusiness.getTemplate();
        break;
      case "IMPORT_EMPLOYEEE":
        file = userBusiness.getTemplate();
        break;
      case "CFG_ROLE_DATA":
        file = cfgRoleDataBusiness.getTemplate();
        break;
      case "EMPLOYEE_APPROVE":
        file = userBusiness.getApproveTemplate();
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
}
