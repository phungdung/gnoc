package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.security.PassTranformer;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TungPV
 */
@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "textEditor")
public class TextEditorController {

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

  @Value("${application.ftp.folder}")
  private String ftpFolder;

  @Autowired
  CommonBusiness commonBusiness;

  @PostMapping("/onUpload")
  public ResponseEntity<ResultInSideDto> onUpload(MultipartFile file, String url)
      throws IOException {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    String filePath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
        PassTranformer.decrypt(ftpPass), ftpFolder, file.getOriginalFilename(), file.getBytes(),
        null);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto
        .setLink(URLDecoder.decode(url, "UTF-8") + "?path=" + URLEncoder.encode(filePath, "UTF-8"));
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/onDownloadByPath")
  public ResponseEntity<Resource> onDownloadByPath(String path)
      throws Exception {
    FTPClient ftpClient = FTPUtil.connectionFTPClient(ftpServer, ftpPort,
        PassTranformer.decrypt(ftpUser), PassTranformer.decrypt(ftpPass));
    ResponseEntity<Resource> resourceResponseEntity = FileUtils
        .responseSourceFromFile(ftpClient, URLDecoder.decode(path, "UTF-8"));
    FTPUtil.disconnectionFTPClient(ftpClient);
    return resourceResponseEntity;
  }

  @GetMapping("/onDeleteByPath")
  public ResponseEntity<ResultInSideDto> onDeleteByPath(String path) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    boolean check = false;
    try {
      check = FileUtils.deleteFtpFile(ftpServer, ftpPort,
          PassTranformer.decrypt(ftpUser), PassTranformer.decrypt(ftpPass),
          URLDecoder.decode(path, "UTF-8"));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    if (check) {
      resultInSideDto.setKey(RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
    }
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }
}
