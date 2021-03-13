package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.business.CrBusiness;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
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
  protected CrBusiness crBusiness;

  @GetMapping("/onDownloadFileTemplate")
  public ResponseEntity<Resource> onDownloadFileTemplate(String moduleName)
      throws Exception {
    switch (moduleName) {
      case "CR_MAIN":
        String fileTemplateCrMain =
            "templates" + File.separator + "CR_NUMBER_" + I18n.getLocale() + ".xls";
        return FileUtils.responseSourceFromFileTemplate(fileTemplateCrMain);
      case "CR_IMPACT_NODES":
        String fileTemplateCrImpactNodes =
            "templates" + File.separator + "CR_IMPORT_NODE_" + I18n.getLocale() + ".xls";
        return FileUtils.responseSourceFromFileTemplate(fileTemplateCrImpactNodes);
      default:
        break;
    }
    return null;
  }

  @PostMapping("/checkDuplicateCr")
  public ResponseEntity<Resource> checkDuplicateCr(@RequestBody CrInsiteDTO crInsiteDTO)
      throws Exception {
    ResultInSideDto resultInSideDto = crBusiness.checkDuplicateCr(crInsiteDTO);
    return FileUtils.responseSourceObjectFromFile(resultInSideDto);
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
