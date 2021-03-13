package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.wo.business.CfgFtOnTimeBusiness;
import com.viettel.gnoc.wo.business.CfgMapUnitGnocNimsBusiness;
import com.viettel.gnoc.wo.business.CfgWoHelpVsmartBusiness;
import com.viettel.gnoc.wo.business.CfgWoHighTempBusiness;
import com.viettel.gnoc.wo.business.MapProvinceCdBusiness;
import com.viettel.gnoc.wo.business.WoCdGroupBusiness;
import com.viettel.gnoc.wo.business.WoMaterialBusiness;
import com.viettel.gnoc.wo.business.WoTypeBusiness;
import com.viettel.gnoc.wo.dto.CfgWoHelpVsmartDTO;
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
  WoMaterialBusiness woMaterialBusiness;

  @Autowired
  WoTypeBusiness woTypeBusiness;

  @Autowired
  CfgMapUnitGnocNimsBusiness cfgMapUnitGnocNimsBusiness;

  @Autowired
  CfgWoHelpVsmartBusiness cfgWoHelpVsmartBusiness;

  @Autowired
  MapProvinceCdBusiness mapProvinceCdBusiness;

  @Autowired
  WoCdGroupBusiness woCdGroupBusiness;

  @Autowired
  CfgFtOnTimeBusiness cfgFtOnTimeBusiness;

  @Autowired
  CfgWoHighTempBusiness cfgWoHighTempBusiness;

  @GetMapping("/onDownloadFileTemplate")
  public ResponseEntity<Resource> onDownloadFileTemplate(String moduleName)
      throws Exception {
    File file;
    switch (moduleName) {
      case "WO_MATERIAL_THRES":
        file = woMaterialBusiness.getMaterialTemplate();
        break;
      case "WO_TYPE":
        file = woTypeBusiness.getWoTypeTemplate();
        break;
      case "WO_CFG_MAP_UNIT_GNOC_NIMS":
        file = cfgMapUnitGnocNimsBusiness.getTemplate();
        break;
      case "MAP_PROVINCE_CD":
        file = mapProvinceCdBusiness.getTemplate();
        break;
      case "WO_CD_GROUP":
        file = woCdGroupBusiness.getTemplateImport();
        break;
      case "WO_CD":
        file = woCdGroupBusiness.getTemplateAssignUser();
        break;
      case "WO_TYPE_GROUP":
        file = woCdGroupBusiness.getTemplateAssignTypeGroup();
        break;
      case "CFG_FT_ONTIME":
        file = cfgFtOnTimeBusiness.getTemplate();
        break;
      case "CFG_WO_HIGH_TEMP":
        file = cfgWoHighTempBusiness.getTemplate();
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

  @PostMapping("/onDownloadFileTemplateCfgWoHelpVsmart")
  public ResponseEntity<Resource> onDownloadFileTemplateCfgWoHelpVsmart(
      @RequestBody CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO) throws Exception {
    File file;
    if (cfgWoHelpVsmartDTO.getSystemId() != null) {
      file = cfgWoHelpVsmartBusiness.getTemplate(cfgWoHelpVsmartDTO.getSystemId().toString());
    } else {
      file = null;
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
