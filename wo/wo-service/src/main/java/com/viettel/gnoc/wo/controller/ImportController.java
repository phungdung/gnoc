package com.viettel.gnoc.wo.controller;

import com.google.gson.Gson;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.business.WoBusiness;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping(Constants.WO_API_PATH_PREFIX + "import")
public class ImportController {

  @Autowired
  WoBusiness woBusiness;

  @PostMapping("/onImportFile")
  public ResponseEntity<Resource> onImportFile(MultipartFile file,
      List<MultipartFile> filesAttachment, String formDataJson, String moduleName)
      throws Exception {
    ResultInSideDto data = new ResultInSideDto();
    switch (moduleName) {
      case "WO":
        data = woBusiness.importData(file, filesAttachment);
        break;
      case "WO_ASSSETS":
        data = woBusiness.importDataAssets(file, filesAttachment, formDataJson);
        break;

      default:
        data.setKey(RESULT.ERROR);
        break;
    }
    InputStreamResource resource = null;
    String fileName = "";
    long contentLength = 0;
    HttpHeaders headers = new HttpHeaders();
    if(moduleName.equals("WO")){
      if (RESULT.SUCCESS.equals(data.getKey())
          || RESULT.FILE_IS_NULL.equals(data.getKey())
          || RESULT.NODATA.equals(data.getKey())
          || RESULT.FILE_INVALID_FORMAT.equals(data.getKey())
          || RESULT.DATA_OVER.equals(data.getKey()) || "ERROR_NO_DOWNLOAD".equals(data.getKey())) {
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
      headers.setContentType(MediaType
          .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
      List<String> customHeaders = new ArrayList<>();
      customHeaders.add("Content-Disposition");
      headers.setAccessControlExposeHeaders(customHeaders);
      headers.setContentDispositionFormData(data.getKey(), fileName);
    }else if(moduleName.equals("WO_ASSSETS")){
      if (StringUtils.isNotNullOrEmpty(data.getFilePath())) {
        File fileResponse;
        fileResponse = new File(data.getFilePath());
        resource = new InputStreamResource(new FileInputStream(fileResponse));
        fileName = fileResponse.getName();
        contentLength = fileResponse.length();
      }
      Gson gson = new Gson();
      Map<String, Object> objectMap = new HashMap<>();
      objectMap.put("key", data.getKey());
      objectMap.put("message", data.getMessage());
      objectMap.put("fileName", fileName);
      headers.setContentType(MediaType
          .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
      List<String> customHeaders = new ArrayList<>();
      customHeaders.add("Content-Disposition");
      headers.setAccessControlExposeHeaders(customHeaders);
        headers.setContentDispositionFormData("attachment", URLEncoder
            .encode(gson.toJson(objectMap), "UTF-8").replace("+", "%20"));
    }

    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(contentLength)
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(resource);
  }
}
