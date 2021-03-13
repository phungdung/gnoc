package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatServiceBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.business.SRCatalogBusiness;
import com.viettel.gnoc.sr.business.SRConfigBusiness;
import com.viettel.gnoc.sr.business.SRFlowExecuteBusiness;
import com.viettel.gnoc.sr.business.SRRoleBusiness;
import com.viettel.gnoc.sr.business.SRRoleUserBusiness;
import com.viettel.gnoc.sr.dto.SRCatalogChildDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "srCatalog")
@Slf4j
public class SRCatalogController {

  @Autowired
  protected CatItemBusiness catItemBusiness;

  @Autowired
  protected CatServiceBusiness catServiceBusiness;

  @Autowired
  protected SRCatalogBusiness srCatalogBusiness;

  @Autowired
  protected SRConfigBusiness srConfigBusiness;

  @Autowired
  protected SRFlowExecuteBusiness srFlowExecuteBusiness;

  @Autowired
  protected UnitBusiness unitBusiness;

  @Autowired
  protected SRRoleUserBusiness srRoleUserBusiness;

  @Autowired
  protected SRRoleBusiness srRoleBusiness;

  @PostMapping("/listSRCatalogDTO")
  public ResponseEntity<Datatable> getListSRCatalogPage(
      @RequestBody SRCatalogDTO srCatalogDTO) {
    Datatable data = srCatalogBusiness.getListSRCatalogPage(srCatalogDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long serviceId) {
    ResultInSideDto data = srCatalogBusiness.delete(serviceId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDetail")
  public ResponseEntity<SRCatalogDTO> getDetail(Long serviceId) {
    SRCatalogDTO data = srCatalogBusiness.getDetail(serviceId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/insert", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insert(
      @RequestPart("srFilesList") List<MultipartFile> srFilesList,
      @Valid @RequestPart("formDataJson") SRCatalogDTO srCatalogDTO) throws IOException {
    ResultInSideDto data = srCatalogBusiness.insert(srFilesList, srCatalogDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> update(
      @RequestPart("srFilesList") List<MultipartFile> srFilesList,
      @RequestPart("formDataJson") SRCatalogDTO srCatalogDTO) throws Exception {
    ResultInSideDto data = srCatalogBusiness.update(srFilesList, srCatalogDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/listSRConfigGroupCBB") //service group CBB
  public ResponseEntity<List<SRConfigDTO>> getListConfigGroup(String parentCode) {
    List<SRConfigDTO> data = srConfigBusiness.getListConfigGroup(parentCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


  //service array CBB :String configGroup = SERVICE_ARRAY
  //giao tiep he thong ngoai CBB configGroup = OTHER_SYS_SERVICE
  @PostMapping("/getByConfigGroupCBB")
  public ResponseEntity<List<SRConfigDTO>> getByConfigGroup(@RequestBody SRConfigDTO srConfigDTO) {
    List<SRConfigDTO> data = srConfigBusiness.getByConfigGroup(srConfigDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListSearchSRServiceArray")
  public ResponseEntity<List<SRCatalogDTO>> getListServiceNameToMapping() {
    List<SRCatalogDTO> data = srCatalogBusiness.getListServiceNameToMapping();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSRFlowExecuteCBB") //truyen country vao dto
  public ResponseEntity<List<SRFlowExecuteDTO>> getListSRFlowExecuteCBB(
      @RequestBody SRFlowExecuteDTO srFlowExecuteDTO) {
    List<SRFlowExecuteDTO> data = srFlowExecuteBusiness.getListSRFlowExecuteCBB(srFlowExecuteDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSRCatalogDTO")
  public ResponseEntity<List<SRCatalogDTO>> getListSRCatalogDTO(
      @RequestBody SRCatalogDTO catalogDTO) {
    List<SRCatalogDTO> data = srCatalogBusiness.getListSRCatalogDTO(catalogDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //  //lay tren common nhe
  @PostMapping("/getListUnit")
  public ResponseEntity<List<UnitDTO>> getListUnit(@RequestBody UnitDTO unitDTO) {
    List<UnitDTO> data = unitBusiness.getListUnit(unitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //getlistUnitRoleByUnitId -- Tryền vào UnitId và country
  @PostMapping("/getlistUnitRoleByUnitId")
  public ResponseEntity<List<SRRoleUserInSideDTO>> getlistUnitRoleByUnitId(
      @RequestBody SRRoleUserInSideDTO srRoleUserDTO) {
    List<SRRoleUserInSideDTO> data = srRoleUserBusiness.getListSRRoleUser(srRoleUserDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListSRRoleByLocationCBB")
  public ResponseEntity<List<SRRoleDTO>> getListSRRoleByLocationCBB(
      @RequestBody SRRoleDTO srRoleDTO) {
    List<SRRoleDTO> data = srRoleBusiness.getListSRRoleByLocationCBB(srRoleDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListServiceChild")
  public ResponseEntity<List<SRCatalogDTO>> getListServiceChild(
      @RequestBody SRCatalogDTO srCatalogDTO) {
    List<SRCatalogDTO> data = srCatalogBusiness.getListServiceChild(srCatalogDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCatalogChild")
  public ResponseEntity<List<SRCatalogChildDTO>> getListCatalogChild(
      @RequestBody SRCatalogChildDTO srCatalogChildDTO) {
    List<SRCatalogChildDTO> data = srCatalogBusiness.getListCatalogChild(srCatalogChildDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //getlistUnitRoleByUnitId -- Tryền vào UnitId và country (Load ơ them moi)
  @PostMapping("/getListSRRoleUserByUnitId")
  public ResponseEntity<ResultInSideDto> getListSRRoleUserByUnitId(
      @RequestBody SRRoleUserInSideDTO srRoleUserDTO) {
    ResultInSideDto data = srCatalogBusiness.getListSRRoleUserByUnitId(srRoleUserDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
