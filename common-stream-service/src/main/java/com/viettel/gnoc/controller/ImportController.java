package com.viettel.gnoc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.viettel.gnoc.business.CfgBusinessCallSmsBusiness;
import com.viettel.gnoc.business.EmployeeDayOffBusiness;
import com.viettel.gnoc.business.LanguageExchangeBussiness;
import com.viettel.gnoc.business.MappingVsaUnitBusiness;
import com.viettel.gnoc.business.ShiftHandoverBusiness;
import com.viettel.gnoc.business.UnitCommonBusiness;
import com.viettel.gnoc.business.WoCdTempBusiness;
import com.viettel.gnoc.commons.business.CfgRoleDataBusiness;
import com.viettel.gnoc.commons.business.DeviceTypeVersionBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.SecurityAnnotation;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.PERMISSION;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.ROLE;
import com.viettel.gnoc.cr.dto.ShiftCrDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkDTO;
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
import org.springframework.http.HttpStatus;
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
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "import")
public class ImportController {

  @Autowired
  DeviceTypeVersionBusiness deviceTypeVersionBusiness;

  @Autowired
  private EmployeeDayOffBusiness employeeDayOffBusiness;

  @Autowired
  private ShiftHandoverBusiness shiftHandoverBusiness;

  @Autowired
  private WoCdTempBusiness woCdTempBusiness;

  @Autowired
  private LanguageExchangeBussiness languageExchangeBussiness;

  @Autowired
  private UnitCommonBusiness unitCommonBusiness;

  @Autowired
  private MappingVsaUnitBusiness mappingVsaUnitBusiness;

  @Autowired
  CfgBusinessCallSmsBusiness cfgBusinessCallSmsBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  CfgRoleDataBusiness cfgRoleDataBusiness;

  @SecurityAnnotation(
      role = {ROLE.UTILITY_VERSION_CATALOG, ROLE.UTILITY_CONFIG_EMPLOYEE_DAY_OFF,
          ROLE.UTILITY_WO_CD_TEMP, ROLE.UTILITY_LANGUAGE_EXCHANGE},
      permission = PERMISSION.IMPORT,
      moduleRole = {"DEVICE_TYPE_VERSION", "EMPLOYEE_DAY_OFF_IMPORT_FILE", "WO_CD_TEMP",
          "LANGUAGE_EXCHANGE"}
  )
  @PostMapping("/onImportFile")
  public ResponseEntity<Resource> onImportFile(MultipartFile file,
      List<MultipartFile> filesAttachment, String formDataJson, String moduleName)
      throws Exception {
    ResultInSideDto data = new ResultInSideDto();
    switch (moduleName) {
      case "DEVICE_TYPE_VERSION":
        data = deviceTypeVersionBusiness.importData(file);
        break;
      case "EMPLOYEE_DAY_OFF_IMPORT_FILE":
        data = employeeDayOffBusiness.importData(file);
        break;
      case "WO_CD_TEMP":
        data = woCdTempBusiness.importData(file);
        break;
      case "LANGUAGE_EXCHANGE":
        data = languageExchangeBussiness.importData(file);
        break;
      case "IMPORT_UNIT":
        data = unitCommonBusiness.importData(file);
        break;
      case "IMPORT_MAPPING_VSA_UNIT":
        data = mappingVsaUnitBusiness.importData(file);
        break;
      case "CFG_BUSINESS_CALL_SMS":
        data = cfgBusinessCallSmsBusiness.importData(file);
        break;
      case "IMPORT_EMPLOYEEE":
        data = userBusiness.importData(file);
        break;
      case "CFG_ROLE_DATA":
        data = cfgRoleDataBusiness.importData(file);
        break;
      case "EMPLOYEE_APPROVE":
        data = userBusiness.importApproveUsers(file);
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
        || RESULT.DATA_OVER.equals(data.getKey())
        || RESULT.NODATA.equals(data.getKey())) {
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
    List<String> customHeaders = new ArrayList<>();
    Gson gson = new Gson();
    Map<String, Object> objectMap = new HashMap<>();
    objectMap.put("key", data.getKey());
    objectMap.put("message", data.getMessage());
    objectMap.put("fileName", fileName);
    if ("EMPLOYEE_APPROVE".equals(moduleName)) {
      headers.setContentType(MediaType
          .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
      customHeaders.add("Content-Disposition");
      headers.setAccessControlExposeHeaders(customHeaders);
      headers.setContentDispositionFormData("attachment", URLEncoder
          .encode(gson.toJson(objectMap), "UTF-8").replace("+", "%20"));
      headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    } else {
      headers.setContentType(MediaType
          .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
      customHeaders.add("Content-Disposition");
      headers.setAccessControlExposeHeaders(customHeaders);
      headers.setContentDispositionFormData(data.getKey(), fileName);
      headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    }
    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(contentLength)
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(resource);
  }

  @PostMapping("/onImportFileLocal")
  public ResponseEntity<ResultInSideDto> onImportFileLocal(MultipartFile file,
      List<MultipartFile> filesAttachment, String formDataJson, String moduleName)
      throws Exception {
    ResultInSideDto data = new ResultInSideDto();
    ObjectMapper mapper = new ObjectMapper();
    switch (moduleName) {
      case "IMPORT_SHIFT_WORK":
        ShiftWorkDTO shiftWorkDTO = mapper.readValue(formDataJson, ShiftWorkDTO.class);
        data = shiftHandoverBusiness.importShiftWorkData(file, shiftWorkDTO);
        break;
      case "CR_IMPORT_FILE":
        ShiftCrDTO shiftCrDTO = mapper.readValue(formDataJson, ShiftCrDTO.class);
        data = shiftHandoverBusiness.importShiftCRData(file, shiftCrDTO);
        break;
      default:
        data.setKey(RESULT.ERROR);
        break;
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
