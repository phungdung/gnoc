package com.viettel.gnoc.kedb.controller;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.kedb.business.KedbBusiness;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
 * @author TungPV
 */
@RestController
@Slf4j
@RequestMapping(Constants.PT_API_PATH_PREFIX + "import")
public class ImportController {

  @Autowired
  KedbBusiness kedbBusiness;

  @PostMapping("/onImportFile")
  public ResponseEntity<Resource> onImportFile(MultipartFile file,
      List<MultipartFile> filesAttachment, String formDataJson, String moduleName)
      throws IOException {
    ResultInSideDto data = new ResultInSideDto();
    switch (moduleName) {
      case "KEDB_MANAGEMENT":
        data = kedbBusiness.importData(file, filesAttachment);
        break;
      default:
        data.setKey(RESULT.ERROR);
        break;
    }
    InputStreamResource resource = null;
    String fileName = "";
    long contentLength = 0;
    if (RESULT.SUCCESS.equals(data.getKey())
        || RESULT.FILE_IS_NULL.equals(data.getKey())
        || RESULT.FILE_INVALID_FORMAT.equals(data.getKey())
        || RESULT.DATA_OVER.equals(data.getKey())) {
      log.info("Start get template file!!!");
      String pathTemplate = "templates" + File.separator
          + "TEMPLATE_EXPORT.xlsx";
      Resource classPathResource = new ClassPathResource(pathTemplate);
      InputStream inputStream = classPathResource.getInputStream();
      resource = new InputStreamResource(inputStream);
      fileName = "TEMPLATE_EXPORT.xlsx";
      contentLength = inputStream.available();
      log.info("End get template file!!!");
    } else {
      resource = new InputStreamResource(new FileInputStream(data.getFile()));
      fileName = data.getFile().getName();
      contentLength = data.getFile().length();

    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType
        .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    List<String> customHeaders = new ArrayList<>();
    customHeaders.add("Content-Disposition");
    headers.setAccessControlExposeHeaders(customHeaders);
    headers.setContentDispositionFormData(data.getKey(), fileName);
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(contentLength)
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(resource);
  }
}
