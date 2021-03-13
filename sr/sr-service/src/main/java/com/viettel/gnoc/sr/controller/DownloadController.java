package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.sr.business.SrBusiness;
import com.viettel.gnoc.sr.dto.SrWsToolCrDTO;
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

@RestController
@Slf4j
@RequestMapping(Constants.CR_API_PATH_PREFIX + "download")
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
  SrBusiness srBusiness;

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

  @PostMapping("/onDownloadFileTypeByTemplateId")
  public ResponseEntity<Resource> onDownloadFileTypeByTemplateId(@RequestBody SrWsToolCrDTO srWsToolCrDTO)
      throws Exception {
    String filePath = srBusiness.fileByPath(srWsToolCrDTO);
    if (srWsToolCrDTO.getFileType().equals(Constants.SR_ROLE_UPDATE.FILE_TYPE_OPEN_CONNECT) || srWsToolCrDTO.getFileType()
        .equals(Constants.SR_ROLE_UPDATE.NIMS) || srWsToolCrDTO.getFileType().equals(Constants.SR_ROLE_UPDATE.AOM)) {
      return FileUtils.responseSourceFromFileTemplate(filePath);
    }
    return FileUtils.responseSourceFromFile(new File(filePath));
  }

  @GetMapping("/onDownloadFileTemplate")
  public ResponseEntity<Resource> onDownloadFileTemplate(String moduleName)
      throws Exception {
    switch (moduleName) {
      case "SR_CREATE_AUTO_CR":
        String fileTemplateSRCreateAutoCR = "templates"
            + File.separator + I18n.getLanguage("sr.template.import");
        return FileUtils.responseSourceFromFileTemplate(fileTemplateSRCreateAutoCR);
      case "MAPPING_IP_FILE":
        String fileTemplateMappingIp = "templates"
            + File.separator + I18n.getLanguage("sr.template.fileMapping");
        return FileUtils.responseSourceFromFileTemplate(fileTemplateMappingIp);
      default:
        break;
    }
    return null;
  }
}
