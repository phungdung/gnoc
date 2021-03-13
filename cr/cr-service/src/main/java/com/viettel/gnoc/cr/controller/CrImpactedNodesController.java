package com.viettel.gnoc.cr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.business.CrImpactedNodesBusiness;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrImpactedNodesService")
@Slf4j
public class CrImpactedNodesController {

  @Autowired
  CrImpactedNodesBusiness crImpactedNodesBusiness;

  @RequestMapping(value = "/actionImportAndGetNetworkNodeV2", produces = "application/zip", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<StreamingResponseBody> actionImportAndGetNetworkNodeV2(
      @RequestPart("fileImport") MultipartFile fileImport,
      @RequestPart("lstImpact") String lstImpact, @RequestPart("lstAffect") String lstAffect,
      String nationCode, int type) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    List<CrImpactedNodesDTO> lstImpactNodes = new ArrayList<>();
    List<LinkedHashMap> lstImpactMap = mapper
        .readValue(lstImpact, ArrayList.class);
    if (lstImpactMap != null) {
      lstImpactMap.forEach(item -> {
        Object ipId = item.get("ipId");
        Object deviceCode = item.get("deviceCode");
        Object deviceName = item.get("deviceName");
        Object deviceCodeOld = item.get("deviceCodeOld");
        Object dtCode = item.get("dtCode");
        Object ip = item.get("ip");
        CrImpactedNodesDTO dto = new CrImpactedNodesDTO();
        dto.setIpId(ipId == null ? null : ipId.toString());
        dto.setDeviceCode(deviceCode == null ? null : deviceCode.toString());
        dto.setDeviceName(deviceName == null ? null : deviceName.toString());
        dto.setDeviceCodeOld(deviceCodeOld == null ? null : deviceCodeOld.toString());
        dto.setDtCode(dtCode == null ? null : dtCode.toString());
        dto.setIp(ip == null ? null : ip.toString());
        lstImpactNodes.add(dto);
      });
    }

    List<CrAffectedNodesDTO> lstAffectedNodes = new ArrayList<>();
    List<LinkedHashMap> lstAffected = mapper
        .readValue(lstAffect, ArrayList.class);

    if (lstAffected != null) {
      lstAffected.forEach(item -> {
        Object ipId = item.get("ipId");
        Object deviceCode = item.get("deviceCode");
        Object deviceName = item.get("deviceName");
        Object dtCode = item.get("dtCode");
        Object ip = item.get("ip");
        CrAffectedNodesDTO dto = new CrAffectedNodesDTO();
        dto.setIpId(ipId == null ? null : ipId.toString());
        dto.setDeviceCode(deviceCode == null ? null : deviceCode.toString());
        dto.setDeviceName(deviceName == null ? null : deviceName.toString());
        dto.setDtCode(dtCode == null ? null : dtCode.toString());
        dto.setIp(ip == null ? null : ip.toString());
        lstAffectedNodes.add(dto);
      });
    }

    ResultInSideDto result = crImpactedNodesBusiness
        .actionImportAndGetNetworkNodeV2(fileImport, nationCode, type, lstImpactNodes,
            lstAffectedNodes);
    Map<String, Object> objectMap = new HashMap<>();
    List<File> files = new ArrayList<>();
    String fileName = "";
    if (result != null) {
      if (result.getFile() != null) {
        fileName = FileUtils.getFileName(result.getFile().getPath());
        files.add(result.getFile());
      }
      if (result.getFilePath() != null) {
        files.add(new File(result.getFilePath()));
      }

      objectMap.put("key", result.getKey());
      objectMap.put("message", result.getMessage());
      objectMap.put("fileName", fileName);
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
        .headers(headers)
        .body(out -> {
          var zipOutputStream = new ZipOutputStream(out);
          if (files != null && !files.isEmpty()) {
            for (File item : files) {
              zipOutputStream.putNextEntry(new ZipEntry(item.getName()));
              FileInputStream fileInputStream = new FileInputStream(item);
              IOUtils.copy(fileInputStream, zipOutputStream);
              fileInputStream.close();
            }
          }
          if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
          }
        });
  }

  @PostMapping("/getListInfraDeviceIpByListIP")
  public ResponseEntity<List<InfraDeviceDTO>> getListInfraDeviceIpByListIP(
      @RequestBody List<InfraDeviceDTO> listIp) {
    List<InfraDeviceDTO> list = crImpactedNodesBusiness.getListInfraDeviceIpByListIP(listIp);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getLisNodeOfCR")
  public ResponseEntity<List<CrImpactedNodesDTO>> getLisNodeOfCR(
      @RequestBody CrImpactedNodesDTO dto) {
    List<CrImpactedNodesDTO> list = crImpactedNodesBusiness
        .getLisNodeOfCR(dto);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListCrImpactedNodesDTO")
  public List<CrImpactedNodesDTO> getListCrImpactedNodesDTO(
      @RequestBody CrImpactedNodesDTO crImpactedNodesDTO) {
    return crImpactedNodesBusiness.getListCrImpactedNodesDTO(crImpactedNodesDTO);
  }

  @PostMapping("/getLisNodeOfCRForProxy")
  public ResponseEntity<List<CrImpactedNodesDTO>> getLisNodeOfCRForProxy(
      @RequestBody CrImpactedNodesDTO dto) {
    List<CrImpactedNodesDTO> list = crImpactedNodesBusiness
        .getLisNodeOfCRForProxy(dto);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }
}
