package com.viettel.gnoc.sr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.sr.business.SRCatalogBusiness;
import com.viettel.gnoc.sr.business.SRMappingProcessCRBusiness;
import com.viettel.gnoc.sr.business.SRRoleBusiness;
import com.viettel.gnoc.sr.business.SRRoleUserBusiness;
import com.viettel.gnoc.sr.business.SRServiceManageBusiness;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
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
@RequestMapping(Constants.PT_API_PATH_PREFIX + "import")
public class ImportController {

  @Autowired
  protected SRRoleUserBusiness srRoleUserBusiness;

  @Autowired
  protected SRRoleBusiness srRoleBusiness;

  @Autowired
  protected SRCatalogBusiness srCatalogBusiness;

  @Autowired
  protected SRMappingProcessCRBusiness srMappingProcessCRBusiness;

  @Autowired
  protected SRServiceManageBusiness srServiceManageBusiness;

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT,
      permission = {SUB_ADMIN_EDIT_VIEW.SR_ROLE_USER},
      modulePermission = {"SR_ROLE_USER"}
  )
  @PostMapping("/onImportFile")
  public ResponseEntity<Resource> onImportFile(MultipartFile file,
      List<MultipartFile> filesAttachment, String formDataJson, String moduleName)
      throws Exception {
    ResultInSideDto data = new ResultInSideDto();
    switch (moduleName) {
      case "SR_ROLE_USER":
        data = srRoleUserBusiness.importData(file);
        break;
      case "SR_ROLE":
        data = srRoleBusiness.importData(file);
        break;
      case "SR_CATALOG_CHILD":
        data = srCatalogBusiness.importDataCatalogChild(file);
        break;
      case "SR_CATALOG":
        data = srCatalogBusiness.importDataCatalog(file);
        break;
      case "SR_SERVICE_ARRAY":
        data = srServiceManageBusiness.importDataServiceArray(file);
        break;
      case "SR_SERVICE_GROUP":
        data = srServiceManageBusiness.importDataServiceGroup(file);
        break;
      case "SR_MAPPING_PROCESS_CR":
        data = srMappingProcessCRBusiness.importData(file);
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
        || RESULT.DATA_OVER.equals(data.getKey())||RESULT.NODATA.equals(data.getKey())) {
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

  @PostMapping("/onImportFileLocal")
  public ResponseEntity<ResultInSideDto> onImportFileLocal(MultipartFile file,
      List<MultipartFile> filesAttachment, String formDataJson, String moduleName)
      throws Exception {
    ResultInSideDto data = new ResultInSideDto();
    ObjectMapper mapper = new ObjectMapper();
    switch (moduleName) {
      case "SR_TMP_UNIT":
        SRRoleUserInSideDTO srRoleUserDTO = mapper
            .readValue(formDataJson, SRRoleUserInSideDTO.class);
        data = srCatalogBusiness.importDataUnit(file, srRoleUserDTO);
        break;
      default:
        data.setKey(RESULT.ERROR);
        break;
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
