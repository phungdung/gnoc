package com.viettel.gnoc.pt.controller;

import com.google.gson.Gson;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.pt.business.ProblemConfigTimeBusiness;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping(Constants.PT_API_PATH_PREFIX+ "import")
public class ImportController {

  @Autowired
  ProblemConfigTimeBusiness problemConfigTimeBusiness;

  @PostMapping("/onImportFile")
  public ResponseEntity<Resource> onImportFile(MultipartFile file,
      List<MultipartFile> filesAttachment, String formDataJson, String moduleName)
      throws Exception {
    ResultInSideDto data = new ResultInSideDto();
    switch (moduleName) {
      case "PT_PROBLEMS_CONFIG_TIME":
        data = problemConfigTimeBusiness.importProblemConfigTime(file);
        break;
      default:
        data.setKey(RESULT.ERROR);
        break;
    }
    InputStreamResource resource = null;
    String message = null;
    long contentLength = 0;

    File fileResponse;
    String fileName = "";
    if (StringUtils.isNotNullOrEmpty(data.getFilePath())) {
      fileResponse = new File(data.getFilePath());
      resource = new InputStreamResource(new FileInputStream(fileResponse));
      fileName = fileResponse.getName();
      contentLength = fileResponse.length();
    }
    if (RESULT.SUCCESS.equals(data.getKey())) {
    } else if (RESULT.FILE_IS_NULL.equals(data.getKey())
        || RESULT.FILE_INVALID_FORMAT.equals(data.getKey())
        || RESULT.DATA_OVER.equals(data.getKey())
        || RESULT.ERROR.equals(data.getKey())
        || RESULT.NODATA.equals(data.getKey())) {
      message = data.getMessage();
    } else if (RESULT.DUPLICATE.equals(data.getKey())) {
      data.setKey(RESULT.ERROR);
      message = data.getMessage();
    }

    Gson gson = new Gson();
    Map<String, Object> objectMap = new HashMap<>();
    objectMap.put("key", data.getKey());
    objectMap.put("message", message);
    objectMap.put("fileName", fileName);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType
        .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    List<String> customHeaders = new ArrayList<>();
    customHeaders.add("Content-Disposition");
    headers.setAccessControlExposeHeaders(customHeaders);
    headers.setContentDispositionFormData("attachment", URLEncoder
        .encode(gson.toJson(objectMap), "UTF-8").replace("+", "%20"));
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(contentLength)
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(resource);
  }
}
