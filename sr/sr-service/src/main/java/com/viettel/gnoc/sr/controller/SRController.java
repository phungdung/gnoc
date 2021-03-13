package com.viettel.gnoc.sr.controller;

import com.google.gson.Gson;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.sr.business.SRCreateAutoCrBusiness;
import com.viettel.gnoc.sr.business.SrBusiness;
import com.viettel.gnoc.sr.business.SrOutsideBusiness;
import com.viettel.gnoc.sr.dto.SRApproveDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfig2DTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SREvaluateDTO;
import com.viettel.gnoc.sr.dto.SRMopDTO;
import com.viettel.gnoc.sr.dto.SRParamDTO;
import com.viettel.gnoc.sr.dto.SRReasonRejectDTO;
import com.viettel.gnoc.sr.dto.SRRenewDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
import com.viettel.gnoc.sr.dto.SRWorklogTypeDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.dto.SrWsToolCrDTO;
import com.viettel.gnoc.sr.dto.UnitSRCatalogDTO;
import com.viettel.gnoc.sr.model.SRParamEntity;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping(Constants.SR_API_PATH_PREFIX + "SR")
@Slf4j
public class SRController {

  @Autowired
  SrBusiness srBusiness;

  @Autowired
  SrOutsideBusiness srOutsideBusiness;

  @Autowired
  SRCreateAutoCrBusiness srCreateAutoCrBusiness;

  //danh sách sr
  @PostMapping("/getListSR")
  public ResponseEntity<Datatable> getListSR(@RequestBody SrInsiteDTO srInsiteDTO) {
    Datatable datatable = srBusiness.getListSR(srInsiteDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  // insertSR
  @RequestMapping(value = "/insertSR", produces = "application/zip", method = RequestMethod.POST)
  public ResponseEntity<StreamingResponseBody> insertSR(@Valid @RequestBody SrInsiteDTO srInsiteDTO)
      throws Exception {
    ResultInSideDto data = srBusiness.insertSRDTO(srInsiteDTO);
    List<File> files = new ArrayList<>();
    Map<String, Object> objectMap = new HashMap<>();
    if (data != null && data.getLstResult() != null && !data.getLstResult().isEmpty()) {
      try {
        List<GnocFileDto> lstResult = (List<GnocFileDto>) data.getLstResult();
        for (GnocFileDto fileDto : lstResult) {
          if (fileDto.getPath() != null) {
            log.info("\n insertSR CÓ FILE ERROR" + fileDto.getPath());
            files.add(new File(fileDto.getPath()));
          }
        }
      } catch (Exception e) {
        log.info(e.getMessage(), e);
      }
    }
    if (data != null) {
      objectMap.put("key", data.getKey());
      objectMap.put("message", data.getMessage());
      if (StringUtils.isNotNullOrEmpty(data.getValidateKey())) {
        objectMap.put("validateKey", data.getValidateKey());
      }
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

  @PostMapping("/saveSrChild")
  public ResponseEntity<ResultInSideDto> saveSrChild(@RequestBody SrInsiteDTO srInsiteDTO) {
    return new ResponseEntity<>(srBusiness.saveSrChild(srInsiteDTO), HttpStatus.OK);
  }

  @RequestMapping(value = "/updateSRDTO", produces = "application/zip", method = RequestMethod.POST)
  public ResponseEntity<StreamingResponseBody> updateSRDTO(
      @Valid @RequestBody SrInsiteDTO srInsiteDTO) throws Exception {
    ResultInSideDto data = srBusiness.updateSRDTO(srInsiteDTO);
    List<File> files = new ArrayList<>();
    Map<String, Object> objectMap = new HashMap<>();
    String fileName = "";
    if (data != null && data.getLstResult() != null && !data.getLstResult().isEmpty()) {
      try {
        List<GnocFileDto> lstResult = (List<GnocFileDto>) data.getLstResult();
        for (GnocFileDto fileDto : lstResult) {
          if (fileDto.getPath() != null) {
            log.info("\n updateSRDTO CÓ FILE ERROR" + fileDto.getPath());
            files.add(new File(fileDto.getPath()));
          }
        }
      } catch (Exception e) {
        log.info(e.getMessage(), e);
      }
    }
    if (data != null) {
      objectMap.put("key", data.getKey());
      objectMap.put("message", data.getMessage());
      objectMap.put("fileName", fileName);
      if (StringUtils.isNotNullOrEmpty(data.getValidateKey())) {
        objectMap.put("validateKey", data.getValidateKey());
      }
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

  // lấy SRCode và SrId
  @GetMapping("/getSRCode")
  public ResponseEntity<ResultInSideDto> getSRCodeInsert(String country, Long srId) {
    return new ResponseEntity<>(srBusiness.getSRCode(country, srId), HttpStatus.OK);
  }

  //danh sách sr không có cấu hình
  @PostMapping("/getListSRByUserLogin")
  public ResponseEntity<Datatable> getListSRByUserLogin(@RequestBody SrInsiteDTO srInsiteDTO) {
    Datatable datatable = srBusiness.getListSRByUserLogin(srInsiteDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/getDataByConfigCode")
  public ResponseEntity<List<SRConfigDTO>> getDataByConfigCode(
      @RequestBody SRConfigDTO srConfigDTO) {
    return new ResponseEntity<>(srBusiness.getDataByConfigCode(srConfigDTO),
        HttpStatus.OK);
  }

  @GetMapping("/getByConfigGroup")
  public ResponseEntity<List<SRConfigDTO>> getByConfigGroup(
      String configGroup) {
    return new ResponseEntity<>(srBusiness.getByConfigGroup(configGroup),
        HttpStatus.OK);
  }

  @GetMapping("/getServiceArrayCBB")
  public ResponseEntity<List<SRConfigDTO>> getServiceArrayCBB(String srUser, String country) {
    return new ResponseEntity<>(srBusiness.getServiceArrayCBB(srUser, country),
        HttpStatus.OK);
  }

  @GetMapping("/getServiceGroupCBB")
  public ResponseEntity<List<SRConfigDTO>> getServiceGroupCBB(
      String configCodeServicesArray, String countryId) {
    return new ResponseEntity<>(srBusiness.getServiceGroupCBB(configCodeServicesArray, countryId),
        HttpStatus.OK);
  }

  @GetMapping("/getListServicesCBB")
  public ResponseEntity<List<SRCatalogDTO>> getListServicesCBB(String configCodeServicesGroup) {
    return new ResponseEntity<>(srBusiness.getListServicesCBB(configCodeServicesGroup),
        HttpStatus.OK);
  }

  @GetMapping("/getListUnitCBB")
  public ResponseEntity<List<UnitDTO>> getListUnitCBB() {
    return new ResponseEntity<>(srBusiness.getListUnitCBB(),
        HttpStatus.OK);
  }

  @GetMapping("/getListUnitBySeviceCBB")
  public ResponseEntity<List<UnitDTO>> getListUnitBySeviceCBB(String services, Boolean isMoreUnit,
      String srUser) {
    return new ResponseEntity<>(srBusiness.getListUnitBySeviceCBB(services, isMoreUnit, srUser),
        HttpStatus.OK);
  }

  @GetMapping("/getListSRRoleCBB")
  public ResponseEntity<List<SRRoleDTO>> getListSRRoleCBB(Long countryId) {
    return new ResponseEntity<>(srBusiness.getListSRRoleCBB(countryId), HttpStatus.OK);
  }

  // truongnt add start
  @GetMapping("/findNationByUnitId")
  public ResponseEntity<SrInsiteDTO> findNationByUnitId(Long unitId) {
    return new ResponseEntity<>(srBusiness.findNationByUnitId(unitId), HttpStatus.OK);
  }

  @GetMapping("/getListSRApprove")
  public ResponseEntity<List<SRApproveDTO>> getListSRApprove(Long srId) {
    List<SRApproveDTO> data = srBusiness.getListSRApprove(srId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSRRenewDTO")
  public ResponseEntity<List<SRRenewDTO>> getListSRRenewDTO(@RequestBody SrInsiteDTO srInsiteDTO) {
    List<SRRenewDTO> data = srBusiness.getListSRRenewDTO(srInsiteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // truongnt add end
  @GetMapping("/getChangeExecutionTimeAndFlowExecute")
  public ResponseEntity<SrInsiteDTO> getChangeExecutionTimeAndFlowExecute(String startTime,
      String services, String countryId) {
    return new ResponseEntity<>(
        srBusiness.getChangeExecutionTimeAndFlowExecute(startTime, services, countryId),
        HttpStatus.OK);
  }

  @PostMapping("/getListCBBSeviceInsert")
  public ResponseEntity<List<SRCatalogDTO>> getListCBBSevice(@RequestBody SrInsiteDTO srInsiteDTO) {
    return new ResponseEntity<>(
        srBusiness.getListCBBSeviceInsert(srInsiteDTO),
        HttpStatus.OK);
  }

  @GetMapping("/getCmbRoleCode")
  public ResponseEntity<List<SRRoleUserInSideDTO>> getCmbRoleCode(String unit, String service) {
    return new ResponseEntity<>(srBusiness.getCmbRoleCode(unit, service), HttpStatus.OK);
  }

  @PostMapping("/getSRDetail")
  public ResponseEntity<SrInsiteDTO> getSRDetail(@RequestBody SrInsiteDTO srInsiteDTO) {
    SrInsiteDTO data = srBusiness.getSRDetail(srInsiteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // start tab WorkLog
  //lấy combox WorkLogType khi thêm mới WorkLog
  @GetMapping("/getBySRStatus")
  public ResponseEntity<List<SRWorklogTypeDTO>> getBySRStatus(String srStatus) {
    return new ResponseEntity<>(srBusiness.getBySRStatus(srStatus), HttpStatus.OK);
  }

  //lấy thông tin WorkLog theo SrId truyền vào để lấy ra dữ liệu của tab WorkLog
  @GetMapping("/getListSRWorklogWithUnit")
  public ResponseEntity<List<SRWorkLogDTO>> getListSRWorklogWithUnit(Long srId) {
    return new ResponseEntity<>(srBusiness.getListSRWorklogWithUnit(srId), HttpStatus.OK);
  }

  // truyền SrId,setReasonRejectId,setWlText
  @PostMapping("/insertSRWorklog")
  public ResponseEntity<ResultInSideDto> insertSRWorklog(@RequestBody SRWorkLogDTO srWorklogDTO) {
    return new ResponseEntity<>(srBusiness.insertSRWorklog(srWorklogDTO), HttpStatus.OK);
  }

  // truyền wlTypeId hàm getBySRStatus lấy từ cbb của nếu trong thêm mới bảng SR có srStatus có trạng thái Rejected hoặc Cancelled
  @GetMapping("/getCmbReason")
  public ResponseEntity<List<SRReasonRejectDTO>> getCmbReason(Long wlTypeId) {
    return new ResponseEntity<>(srBusiness.getCmbReason(wlTypeId), HttpStatus.OK);
  }
  // end tab WorkLog

  //tab file đính kèm

  @PostMapping("/getListSRDialogFile")
  public ResponseEntity<List<GnocFileDto>> getListSRDialogFile(
      @RequestBody GnocFileDto gnocFileDto) {
    List<GnocFileDto> data = srBusiness.getListSRDialogFile(gnocFileDto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getCBBFileType")
  public ResponseEntity<List<SRConfig2DTO>> getCBBFileType(@RequestBody SrInsiteDTO srInsiteDTO) {
    return new ResponseEntity<>(srBusiness.getCBBFileType(srInsiteDTO), HttpStatus.OK);
  }

  @PostMapping("/deleteSRFile")
  public ResponseEntity<ResultInSideDto> deleteSRFile(@RequestBody GnocFileDto gnocFileDto) {
    ResultInSideDto result = srBusiness.deleteSRFile(gnocFileDto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/insertSRFile", produces = "application/zip", method = RequestMethod.POST)
  public ResponseEntity<StreamingResponseBody> insertSRFile(
      @RequestPart("srFileList") List<MultipartFile> srFileList,
      @RequestPart("formDataJson") SrInsiteDTO srInsiteDTO) throws Exception {
    ResultInSideDto data = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    List<File> files = new ArrayList<>();
    String fileName = "";
    if (data.getFile() != null) {
      files.add(data.getFile());
      fileName = data.getFile().getName();
      log.info("\n insertSRFile CÓ FILE MOP" + data.getFile().getName());
    }
    if (data != null && data.getFilePath() != null) {
      log.info("\n insertSRFile CÓ FILE ERROR" + data.getFilePath());
      files.add(new File(data.getFilePath()));
    }
    Gson gson = new Gson();
    Map<String, Object> objectMap = new HashMap<>();
    objectMap.put("key", data.getKey());
    objectMap.put("message", data.getMessage());
    objectMap.put("fileName", fileName);
    objectMap.put("object", data.getObject());
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
          for (File item : files) {
            zipOutputStream.putNextEntry(new ZipEntry(item.getName()));
            FileInputStream fileInputStream = new FileInputStream(item);
            IOUtils.copy(fileInputStream, zipOutputStream);
            fileInputStream.close();
          }
          if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
          }
        });
  }

  @RequestMapping(value = "/insertSRDialogFile", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertSRDialogFile(
      @RequestPart("srFileList") List<MultipartFile> srFileList,
      @RequestPart("formDataJson") SrInsiteDTO srInsiteDTO) {
    return new ResponseEntity<>(srBusiness.insertSRDialogFile(srFileList, srInsiteDTO),
        HttpStatus.OK);
  }

  @PostMapping("/getListSRFile")
  public ResponseEntity<List<GnocFileDto>> getListSRFile(@RequestBody GnocFileDto gnocFileDto) {
    List<GnocFileDto> data = srBusiness.getListSRFile(gnocFileDto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //  chọn combobox getCBBFileType trước và kiểm tra
// khi fileType = TOOL thì hiện combobox
  @PostMapping("/getCBBCmbFileName")
  public ResponseEntity<List<ResultInSideDto>> getCBBCmbFileName(
      @RequestBody SrWsToolCrDTO srWsToolCrDTO) throws Exception {
    return new ResponseEntity<>(srBusiness.getCBBCmbFileName(srWsToolCrDTO), HttpStatus.OK);
  }
  //end tab file đính kèm

  //tab phe duyet
  @GetMapping("/findSRApproveBySrId")
  public ResponseEntity<SRApproveDTO> findSRApproveBySrId(Long srId) {
    SRApproveDTO data = srBusiness.findSRApproveBySrId(srId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateSRApprove")
  public ResponseEntity<ResultInSideDto> insertOrUpdateSRApprove(
      @RequestBody SRApproveDTO srApproveDTO) {
    ResultInSideDto res = srBusiness.insertOrUpdateSRApprove(srApproveDTO);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  //tab gia han
  @GetMapping("/findSRRenewBySrId")
  public ResponseEntity<SRRenewDTO> findSRRenewBySrId(Long srId) {
    SRRenewDTO data = srBusiness.findSRRenewBySrId(srId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateSRRenew")
  public ResponseEntity<ResultInSideDto> insertOrUpdateSRRenew(@RequestBody SRRenewDTO srRenewDTO) {
    ResultInSideDto res = srBusiness.insertOrUpdateSRRenew(srRenewDTO);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  //tab key-value
  @GetMapping("/findListSRParamBySrId")
  public ResponseEntity<List<SRParamEntity>> findListSRParamBySrId(Long srId) {
    List<SRParamEntity> data = srBusiness.findListSRParamBySrId(srId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertListSRParam")
  public ResponseEntity<ResultInSideDto> insertListSRParam(
      @RequestBody List<SRParamDTO> lsSrParam) {
    ResultInSideDto res = srBusiness.insertListSRParam(lsSrParam);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  //tab Thong tin Mop
  @PostMapping("/getListSRMopDTO")
  public ResponseEntity<List<SRMopDTO>> getListSRMopDTO(@RequestBody SRMopDTO srMopDTO) {
    List<SRMopDTO> data = srBusiness.getListSRMopDTO(srMopDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSRMopNotSR")
  public ResponseEntity<List<SRMopDTO>> getListSRMopNotSR(@RequestBody SRMopDTO srMopDTO) {
    List<SRMopDTO> data = srBusiness.getListSRMopNotSR(srMopDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertListSRMop")
  public ResponseEntity<ResultInSideDto> insertListSRMop(@RequestBody List<SRMopDTO> lsSrMop) {
    ResultInSideDto data = srBusiness.insertListSRMop(lsSrMop);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateListSRMop")
  public ResponseEntity<ResultInSideDto> updateListSRMop(@RequestBody List<SRMopDTO> lsSrMop) {
    ResultInSideDto data = srBusiness.updateListSRMop(lsSrMop);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/deleteListSRMop")
  public ResponseEntity<ResultInSideDto> deleteListSRMop(@RequestBody List<SRMopDTO> lsSrMop) {
    ResultInSideDto data = srBusiness.deleteListSRMop(lsSrMop);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //tab thong tin danh gia
  @GetMapping("/findSREvaluateBySrId")
  public ResponseEntity<List<SREvaluateDTO>> findSREvaluateBySrId(Long srId) {
    List<SREvaluateDTO> data = srBusiness.findSREvaluateBySrId(srId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateSREvaluate")
  public ResponseEntity<ResultInSideDto> insertOrUpdateSREvaluate(
      @RequestBody SREvaluateDTO srEvaluateDTO) {
    ResultInSideDto res = srBusiness.insertOrUpdateSREvaluate(srEvaluateDTO);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  //tab OD
  @GetMapping("/findListOdBySr")
  public ResponseEntity<List<OdSearchInsideDTO>> findListOdBySr(Long srId) {
    List<OdSearchInsideDTO> data = srBusiness.findListOdBySr(srId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //tab WO
  @PostMapping("/findListWoBySr")
  public ResponseEntity<List<WoDTOSearch>> findListWoBySr(@RequestBody SrInsiteDTO srDto) {
    List<WoDTOSearch> data = srBusiness.findListWoBySr(srDto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //tab CR
  @GetMapping("/findListCrBySr")
  public ResponseEntity<List<CrInsiteDTO>> findListCrBySr(Long srId) {
    List<CrInsiteDTO> data = srBusiness.findListCrBySr(srId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //valueChange comboBox Status => get data comboBoxSrUser
  @PostMapping("/getCmbSrUser")
  public ResponseEntity<List<SRRoleUserInSideDTO>> getCmbSrUser(@RequestBody
      SRRoleUserInSideDTO srRoleUserDTO) {
    return new ResponseEntity<>(srBusiness.getCmbSrUser(srRoleUserDTO),
        HttpStatus.OK);
  }

  @PostMapping("/getListUnitSRCatalog")
  public ResponseEntity<List<UnitSRCatalogDTO>> getListUnitSRCatalog(@RequestBody
      SRCatalogDTO srCatalogDTO) {
    return new ResponseEntity<>(srBusiness.getListUnitSRCatalog(srCatalogDTO),
        HttpStatus.OK);
  }

  //Delete SR gird chính
  @PostMapping("/deleteSR")
  public ResponseEntity<ResultInSideDto> deleteSR(@RequestBody SrInsiteDTO srInsiteDTO) {
    ResultInSideDto data = srBusiness.deleteSR(srInsiteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/findSrCreateAutoBySrId")
  public ResponseEntity<SRCreateAutoCRDTO> findSrCreateAutoBySrId(
      @RequestBody SRCreateAutoCRDTO srCreateAutoCRDTO) {
    return new ResponseEntity<>(srCreateAutoCrBusiness.findSrCreateAutoBySrId(srCreateAutoCRDTO),
        HttpStatus.OK);
  }

  @RequestMapping(value = "/insertOrUpdateSRCreateAutoCr", produces = "application/zip", method = RequestMethod.POST)
  public ResponseEntity<StreamingResponseBody> insertOrUpdateSRCreateAutoCr(
      @RequestPart("srFileList") List<MultipartFile> srFileList,
      @RequestPart("filesProcess") List<MultipartFile> filesProcess,
      @RequestPart("formDataJson") SRCreateAutoCRDTO srCreateAutoCRDTO) throws Exception {
    ResultInSideDto data = srCreateAutoCrBusiness
        .insertOrUpdateSRCreateAutoCr(srFileList, filesProcess, srCreateAutoCRDTO);
    List<File> files = new ArrayList<>();
    String fileName = "";
    if (data.getFile() != null) {
      files.add(data.getFile());
      fileName = data.getFile().getName();
      log.info("\n SR insertOrUpdateSRCreateAutoCr " + fileName);
    }
    if (data != null && data.getFilePath() != null) {
      log.info("\n SR CÓ FILE ERROR" + data.getFilePath());
      files.add(new File(data.getFilePath()));
    }
    Gson gson = new Gson();
    Map<String, Object> objectMap = new HashMap<>();
    objectMap.put("key", data.getKey());
    objectMap.put("message", data.getMessage());
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
        .body(out -> {
          var zipOutputStream = new ZipOutputStream(out);
          for (File item : files) {
            zipOutputStream.putNextEntry(new ZipEntry(item.getName()));
            FileInputStream fileInputStream = new FileInputStream(item);
            IOUtils.copy(fileInputStream, zipOutputStream);
            fileInputStream.close();
          }
          if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
          }
        });
  }

  @PostMapping("/deleteMopFileWS")
  public ResponseEntity<ResultInSideDto> deleteMopFileWS(
      @RequestBody SrInsiteDTO srInsiteDTO) {
    return new ResponseEntity<>(srBusiness.deleteMopFileWS(srInsiteDTO),
        HttpStatus.OK);
  }

  @PostMapping("/deleteSRChild")
  public ResponseEntity<ResultInSideDto> deleteSRChild(
      @RequestBody SrInsiteDTO srChild) {
    return new ResponseEntity<>(srBusiness.deleteSRChild(srChild),
        HttpStatus.OK);
  }

  @GetMapping("/roleActionSRChild")
  public ResponseEntity<ResultInSideDto> roleActionSRChild(String createdUser) {
    return new ResponseEntity<>(srBusiness.roleActionSRChild(createdUser),
        HttpStatus.OK);
  }

  @PostMapping("/getListSRForWO")
  public List<SRDTO> getListSRForWO(@RequestBody SRDTO srdto) {
    return srBusiness.getListSRForWO(srdto);
  }

  @PostMapping("/getListSRForLinkCR/loginUser{loginUser}/srCode{srCode}")
  public List<SRDTO> getListSRForLinkCR(@PathVariable(value = "loginUser") String loginUser,
      @PathVariable(value = "srCode") String srCode) {
    return srOutsideBusiness.getListSRForLinkCR(loginUser, srCode);
  }

  @PostMapping("/getListSRCatalogByConfigGroup/configGroup{configGroup}")
  public List<SRCatalogDTO> getListSRCatalogByConfigGroup(
      @PathVariable(value = "configGroup") String configGroup) {
    return srOutsideBusiness.getListSRCatalogByConfigGroup(configGroup);
  }

  //thangdt nang cap pt
  @GetMapping("/getListDataSearchForPt")
  public List<SrInsiteDTO> getListDataSearchForPt(String problemId) {
    log.info("findListSrByPt");
    SrInsiteDTO srInsiteDTO = new SrInsiteDTO();
    srInsiteDTO.setOtherSystemCode(problemId);
    List<SrInsiteDTO> data = srBusiness.getListDataSearchForPt(srInsiteDTO);
    return data;
  }

  //tripm nang cap od
  @GetMapping("/findSrFromOdByProxyId{srId}")
  public SrInsiteDTO findSrFromOdByProxyId(@PathVariable(value = "srId") Long srId) {
    SrInsiteDTO srInsiteDTO = srBusiness.finSrFromOdByProxyId(srId);
    return srInsiteDTO;
  }

  //dungpv 18/08/2020 add SR != trang thai Concluded va userLogin = userCreate thi cho phep them moi SR
  @GetMapping("/checkSRConcluded")
  public boolean checkSRConcluded() {
    return srBusiness.checkSRConcluded();
  }

  @PostMapping("/closedSR")
  public ResponseEntity<ResultInSideDto> closedSR(@RequestBody SrInsiteDTO srInsiteDTO) {
    ResultInSideDto resultInSideDto = srBusiness.closedSR(srInsiteDTO.getSrId());
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getListSRFileCheckRole")
  public ResponseEntity<ResultInSideDto> getListSRFileCheckRole(
      @RequestBody GnocFileDto gnocFileDto) {
    ResultInSideDto resultInSideDto = srBusiness.getListSRFileCheckRole(gnocFileDto);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateCrNumberForSR")
  public ResponseEntity<ResultInSideDto> updateCrNumberForSR(@RequestBody SrInsiteDTO srInsiteDTO) {
    ResultInSideDto resultInSideDto = srBusiness
        .updateCrNumberForSR(srInsiteDTO.getCrNumber(), srInsiteDTO.getSrId());
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/updateCrNumberTabSRForCR")
  public ResponseEntity<ResultInSideDto> updateCrNumberTabSRForCR(@RequestBody CrInsiteDTO crInsiteDTO) {
    ResultInSideDto resultInSideDto = srBusiness
        .updateCrNumberTabSRForCR(crInsiteDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getListTabSrChild")
  public ResponseEntity<Datatable> getListTabSrChild(@RequestBody SrInsiteDTO srInsiteDTO) {
    return new ResponseEntity<>(srBusiness.getListTabSrChild(srInsiteDTO), HttpStatus.OK);
  }
}
