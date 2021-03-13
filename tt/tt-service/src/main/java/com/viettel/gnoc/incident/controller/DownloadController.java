package com.viettel.gnoc.incident.controller;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.security.PassTranformer;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TungPV
 */
@RestController
@Slf4j
@RequestMapping(Constants.TT_API_PATH_PREFIX + "download")
public class DownloadController {

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

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
