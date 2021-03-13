package com.viettel.gnoc.wo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.wo.business.WoBusiness;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
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
@RequestMapping(Constants.WO_API_PATH_PREFIX + "export")
public class ExportController {

  @Autowired
  WoBusiness woBusiness;

  @PostMapping("/onExportFile")
  public ResponseEntity<Resource> onExportFile(String formDataJson, String moduleName)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    File file;
    switch (moduleName) {
      case "WO":
        WoInsideDTO woInsideDTO = mapper.readValue(formDataJson, WoInsideDTO.class);
        file = woBusiness.exportDataWo(woInsideDTO);
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

  @PostMapping("/onExportFileWoTestService")
  public ResponseEntity<Resource> onExportFileWoTestService(MultipartFile filesAttach,
      String startTimeFrom,
      String startTimeTo, String moduleName)
      throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    switch (moduleName) {
      case "EXPORT_WO_FROM_CR":
        Date stf = DateUtil.string2DateTime(startTimeFrom);
        Date stt = DateUtil.string2DateTime(startTimeTo);
        resultInSideDto = woBusiness.exportDataWoFromListCr(filesAttach, stf, stt);
        break;
      case "EXPORT_FILE_TEST_SERVICE":
        resultInSideDto = woBusiness.exportFileTestService(filesAttach);
        break;
      default:
        resultInSideDto.setKey(RESULT.ERROR);
        break;
    }
    InputStreamResource resource = null;
    String fileName = null;
    long contentLength = 0;
    String message = null;
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      resource = new InputStreamResource(new FileInputStream(resultInSideDto.getFile()));
      fileName = resultInSideDto.getFile().getName();
      contentLength = resultInSideDto.getFile().length();
    } else if (RESULT.FILE_IS_NULL.equals(resultInSideDto.getKey())
        || RESULT.FILE_INVALID_FORMAT.equals(resultInSideDto.getKey())
        || RESULT.DATA_OVER.equals(resultInSideDto.getKey())
        || RESULT.ERROR.equals(resultInSideDto.getKey())
        || RESULT.NODATA.equals(resultInSideDto.getKey())) {
      message = resultInSideDto.getMessage();
    }
    Gson gson = new Gson();
    Map<String, Object> objectMap = new HashMap<>();
    objectMap.put("key", resultInSideDto.getKey());
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
