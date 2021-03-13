package com.viettel.gnoc.cr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.viettel.aam.MopFileResult;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.business.CrFileAttachBusiness;
import com.viettel.gnoc.cr.dto.AttachDtDTO;
import com.viettel.gnoc.cr.dto.CrFileObjectInsite;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.TemplateImportDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.vipa.MopDetailOutputDTO;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrFilesAttachService")
@Slf4j
public class CrFilesAttachController {

  @Autowired
  CrFileAttachBusiness crFileAttachBusiness;

  @RequestMapping(value = "/insertListFileAttach", produces = "application/zip", method = RequestMethod.POST)
  public ResponseEntity<Resource> insertListFileAttach(
      @RequestPart("formDataJson") CrFilesAttachInsiteDTO crFilesAttachDTO,
      @RequestPart List<MultipartFile> lstMutilKPI,
      @RequestPart List<MultipartFile> lstMutilDT,
      @RequestPart List<MultipartFile> lstMutilTest,
      @RequestPart List<MultipartFile> lstMutilRoll,
      @RequestPart List<MultipartFile> lstMutilPlant,
      @RequestPart List<MultipartFile> lstMutilImpactScenario,
      @RequestPart List<MultipartFile> lstMutilForm,
      @RequestPart List<MultipartFile> lstMutilFile,
      @RequestPart List<MultipartFile> lstMutilTxt,
      @RequestPart List<MultipartFile> lstMutilFileOther,
      @RequestPart List<MultipartFile> lstMutilProcess,
      @RequestPart List<MultipartFile> lstMultilLogImpact
  ) throws Exception {
    ResultInSideDto data = crFileAttachBusiness
        .insertList(crFilesAttachDTO, lstMutilKPI, lstMutilDT, lstMutilTest, lstMutilRoll,
            lstMutilPlant, lstMutilImpactScenario, lstMutilForm, lstMutilFile, lstMutilTxt,
            lstMutilFileOther, lstMutilProcess, lstMultilLogImpact);
    InputStreamResource resource = null;
    Map<String, Object> objectMap = new HashMap<>();
    String fileName = "";
    long contentLength = 0;
    if (data != null) {
      if (StringUtils.isNotNullOrEmpty(data.getFilePath())) {
        fileName = FileUtils.getFileName(data.getFilePath());
        File file = new File(data.getFilePath());
        contentLength = file.length();
        resource = new InputStreamResource(
            new FileInputStream(file));
      }
      objectMap.put("key", data.getKey());
      objectMap.put("message", data.getMessage());
      objectMap.put("fileName", fileName);
      objectMap.put("validateKey", data.getValidateKey());
      objectMap.put("processId", data.getProcessId());
    }
    Gson gson = new Gson();
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
        .headers(headers).contentLength(contentLength)
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(resource);
  }

  @PostMapping("/loadDtFromTDTT")
  public ResponseEntity<List<AttachDtDTO>> loadDtFromTDTT(String userName, String crProcess,
      String lstWork) {
    List<AttachDtDTO> list = crFileAttachBusiness.loadDtFromTDTT(userName, crProcess, lstWork);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/loadDtFromVIPA")
  public ResponseEntity<List<AttachDtDTO>> loadDtFromVIPA(String userName, String system,
      String crProcess, String lstWork) {
    List<AttachDtDTO> list = crFileAttachBusiness
        .loadDtFromVIPA(userName, system, crProcess, lstWork);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getMopInfo")
  public ResponseEntity<MopDetailOutputDTO> getMopInfo(String dtCode) {
    return new ResponseEntity<>(crFileAttachBusiness.getMopInfo(dtCode), HttpStatus.OK);
  }

  @PostMapping("/getMopFile")
  public ResponseEntity<MopFileResult> getMopFile(String dtCode, String crNumber) {
    return new ResponseEntity<>(crFileAttachBusiness.getMopFile(dtCode, crNumber), HttpStatus.OK);
  }

  @PostMapping("/getNetworkNodeTDTTV2")
  public ResponseEntity<List<InfraDeviceDTO>> getNetworkNodeTDTTV2(List<String> lstIp,
      String nationCode) {
    return new ResponseEntity<>(crFileAttachBusiness.getNetworkNodeTDTTV2(lstIp, nationCode),
        HttpStatus.OK);
  }

  @PostMapping("/getListFilesSearchDataTable")
  public ResponseEntity<Datatable> getListFilesSearchDataTable(
      @RequestBody CrFilesAttachInsiteDTO dto) {
    Datatable list = crFileAttachBusiness.getListFilesSearchDataTable(dto);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListCrFilesSearch")
  public ResponseEntity<List<CrFilesAttachInsiteDTO>> getListCrFilesSearch(
      @RequestBody CrFilesAttachInsiteDTO dto) {
    List<CrFilesAttachInsiteDTO> list = crFileAttachBusiness.getListCrFilesSearch(dto);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/findFileAttachById")
  public ResponseEntity<CrFilesAttachInsiteDTO> findFileAttachById(Long id) {
    CrFilesAttachInsiteDTO dto = crFileAttachBusiness
        .findFileAttachById(id);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @PostMapping("/getListCrFilesAttachDTO")
  public ResponseEntity<List<CrFilesAttachInsiteDTO>> getListCrFilesAttachDTO(
      @RequestBody CrFilesAttachInsiteDTO dto) {
    List<CrFilesAttachInsiteDTO> list = crFileAttachBusiness
        .getListCrFilesAttachDTO(dto, 0, 100, "", "");
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListTemplateFileByProcess")
  public ResponseEntity<List<CrFileObjectInsite>> getListTemplateFileByProcess(
      String crProcessId, String actionRight) {
    List<CrFileObjectInsite> list = crFileAttachBusiness
        .getListTemplateFileByProcess(crProcessId, actionRight);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/selectDTFile")
  public ResponseEntity<ResultInSideDto> selectDTFile(@RequestBody CrInsiteDTO crInsiteDTO) {
    ResultInSideDto data = crFileAttachBusiness
        .selectDTFile(crInsiteDTO.getAttachDtDTO(),
            crInsiteDTO.getSystemId() == null ? null : String.valueOf(crInsiteDTO.getSystemId()),
            crInsiteDTO.getCrNumber(), crInsiteDTO.getCrId());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertListNoIDForImportForSR")
  public ResponseEntity<List<TemplateImportDTO>> insertListNoIDForImportForSR(
      @RequestBody String dataFileProcess) {
    ObjectMapper mapper = new ObjectMapper();
    List<TemplateImportDTO> data = new ArrayList<>();
    try {
      SRDTO srdto = mapper
          .readValue(dataFileProcess, SRDTO.class);
      log.info("Step 1: Data before: " + String.valueOf(dataFileProcess));
      log.info("Step 2: insertListNoId");
      UserToken userToken = new UserToken();
      userToken.setUserName(srdto.getUsername());
      String locale = null;
      if (srdto != null) {
        locale = srdto.getLocale();
        LocaleContextHolder.setLocale(getLocale(locale));
      }
      data = crFileAttachBusiness
          .insertListNoIDForImportForSR(srdto.getLstUpload(), userToken,
              srdto.getUnitToken(), true);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
    }
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  private Locale getLocale(String locale) {
    Locale localeCus = null;
    if (locale != null && "en".equalsIgnoreCase(locale) || "en_us"
        .equalsIgnoreCase(locale)) {
      localeCus = new Locale("en", "US");
    } else if (locale != null && "vi".equalsIgnoreCase(locale) || "vi_vn"
        .equalsIgnoreCase(locale)) {
      localeCus = new Locale("vi", "VN");
    } else if (locale != null && "lo".equalsIgnoreCase(locale) || "lo_la"
        .equalsIgnoreCase(locale)) {
      localeCus = new Locale("lo", "LA");
    }
    return localeCus;
  }

  @PostMapping("/loadDtTestFromVIPA")
  public ResponseEntity<List<AttachDtDTO>> loadDtTestFromVIPA(String userName) {
    List<AttachDtDTO> list = crFileAttachBusiness
        .loadDtTestFromVIPA(userName);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }


  @PostMapping("/selectDTTestFile")
  public ResponseEntity<ResultInSideDto> selectDTTestFile(@RequestBody CrInsiteDTO crInsiteDTO) {
    ResultInSideDto data = crFileAttachBusiness.selectDTTestFile(crInsiteDTO.getAttachDtDTO(),
        crInsiteDTO.getSystemId() == null ? null : String.valueOf(crInsiteDTO.getSystemId()),
        crInsiteDTO.getCrNumber(), crInsiteDTO.getCrId());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @DeleteMapping("/deleteFileDT")
  public ResponseEntity<ResultInSideDto> deleteFileDT(String crId) {
    ResultInSideDto data = crFileAttachBusiness.deleteFileDT(crId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCrFilesSearchCheckRole")
  public ResponseEntity<ResultInSideDto> getListCrFilesSearchCheckRole(
      @RequestBody CrFilesAttachInsiteDTO dto) {
    ResultInSideDto resultInSideDto = crFileAttachBusiness.getListCrFilesSearchCheckRole(dto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }
}
