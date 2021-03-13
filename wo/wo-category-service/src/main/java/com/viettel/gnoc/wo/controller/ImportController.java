package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.business.CfgFtOnTimeBusiness;
import com.viettel.gnoc.wo.business.CfgMapUnitGnocNimsBusiness;
import com.viettel.gnoc.wo.business.CfgWoHighTempBusiness;
import com.viettel.gnoc.wo.business.MapProvinceCdBusiness;
import com.viettel.gnoc.wo.business.WoCdGroupBusiness;
import com.viettel.gnoc.wo.business.WoMaterialBusiness;
import com.viettel.gnoc.wo.business.WoTypeBusiness;
import java.io.File;
import java.io.FileInputStream;
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
  protected WoMaterialBusiness woMaterialBusiness;

  @Autowired
  protected WoTypeBusiness woTypeBusiness;

  @Autowired
  protected CfgMapUnitGnocNimsBusiness cfgMapUnitGnocNimsBusiness;

  @Autowired
  protected MapProvinceCdBusiness mapProvinceCdBusiness;

  @Autowired
  protected WoCdGroupBusiness woCdGroupBusiness;

  @Autowired
  CfgFtOnTimeBusiness cfgFtOnTimeBusiness;

  @Autowired
  CfgWoHighTempBusiness cfgWoHighTempBusiness;

  @PostMapping("/onImportFile")
  public ResponseEntity<Resource> onImportFile(MultipartFile file,
      List<MultipartFile> filesAttachment, String formDataJson, String moduleName)
      throws Exception {
    ResultInSideDto data = new ResultInSideDto();
    switch (moduleName) {
      case "WO_MATERIAL_THRES":
        data = woMaterialBusiness.importData(file);
        break;

      case "WO_TYPE":
        data = woTypeBusiness.importData(file);
        break;

      case "WO_CFG_MAP_UNIT_GNOC_NIMS":
        data = cfgMapUnitGnocNimsBusiness.importData(file);
        break;
      case "MAP_PROVINCE_CD":
        data = mapProvinceCdBusiness.importData(file);
        break;
      case "WO_CD_GROUP":
        data = woCdGroupBusiness.importDataWoCdGroup(file);
        break;
      case "WO_CD":
        data = woCdGroupBusiness.importDataAssignUser(file);
        break;
      case "WO_TYPE_GROUP":
        data = woCdGroupBusiness.importDataAssignTypeGroup(file);
        break;
      case "CFG_FT_ONTIME":
        data = cfgFtOnTimeBusiness.importData(file);
        break;
      case "CFG_WO_HIGH_TEMP":
        data = cfgWoHighTempBusiness.importData(file);
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
