package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.sr.business.SRCatalogBusiness;
import com.viettel.gnoc.sr.business.SRMappingProcessCRBusiness;
import com.viettel.gnoc.sr.business.SRRoleBusiness;
import com.viettel.gnoc.sr.business.SRRoleUserBusiness;
import com.viettel.gnoc.sr.business.SRServiceManageBusiness;
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
@RequestMapping(Constants.PT_API_PATH_PREFIX + "download")
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
  SRRoleUserBusiness srRoleUserBusiness;

  @Autowired
  SRRoleBusiness srRoleBusiness;

  @Autowired
  SRCatalogBusiness srCatalogBusiness;

  @Autowired
  SRServiceManageBusiness srServiceManageBusiness;

  @Autowired
  SRMappingProcessCRBusiness srMappingProcessCRBusiness;

  @GetMapping("/onDownloadFileTemplate")
  public ResponseEntity<Resource> onDownloadFileTemplate(String moduleName)
      throws Exception {
    File file;
    switch (moduleName) {
      case "SR_ROLE_USER":
        file = srRoleUserBusiness.getTemplate();
        break;
      case "SR_ROLE":
        file = srRoleBusiness.getTemplate();
        break;
      case "SR_CATALOG":
        file = srCatalogBusiness.getCatalogTemplate();
        break;
      case "SR_CATALOG_CHILD":
        file = srCatalogBusiness.getChildTemplate();
        break;
      case "SR_SERVICE_ARRAY":
        file = srServiceManageBusiness.getTemplateServiceArray();
        break;
      case "SR_SERVICE_GROUP":
        file = srServiceManageBusiness.getTemplateServiceGroup();
        break;
      case "SR_MAPPING_PROCESS_CR":
        file = srMappingProcessCRBusiness.getTemplate();
        break;
      case "SR_TMP_UNIT":
        file = srCatalogBusiness.getUnitTemplate();
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
