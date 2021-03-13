package com.viettel.gnoc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatServiceBusiness;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.business.ReceiveUnitBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TungPV
 */
@RestController
@Slf4j
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "commonStreamAPI")
public class CommonController {

  @Autowired
  CommonBusiness commonBusiness;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  ReceiveUnitBusiness receiveUnitBusiness;

  @Autowired
  CatServiceBusiness catServiceBusiness;

  @GetMapping("/getAllGnocTimezone")
  public ResponseEntity<List<GnocTimezoneDto>> getAllGnocTimezone() {
    List<GnocTimezoneDto> gnocTimezoneDtos = commonBusiness.getAllGnocTimezone();
    return new ResponseEntity<>(gnocTimezoneDtos, HttpStatus.OK);
  }

  @GetMapping("/getAllGnocLanguage")
  public ResponseEntity<List<GnocLanguageDto>> getAllGnocLanguage() {
    List<GnocLanguageDto> gnocLanguageDtos = commonBusiness.getAllGnocLanguage();
    return new ResponseEntity<>(gnocLanguageDtos, HttpStatus.OK);
  }

  @PostMapping("/getListCombobox")
  public ResponseEntity<List<DataItemDTO>> getListCombobox(
      @RequestBody ObjectSearchDto objectSearchDto) {
    List<DataItemDTO> dataItemDTOS = commonBusiness.getListCombobox(objectSearchDto);
    return new ResponseEntity<>(dataItemDTOS, HttpStatus.OK);
  }

  @PostMapping("/getTreeData")
  public ResponseEntity<List<TreeDTO>> getTreeData(@RequestBody ObjectSearchDto objectSearchDto) {
    List<TreeDTO> treeDTOS = commonBusiness.getTreeData(objectSearchDto);
    return new ResponseEntity<>(treeDTOS, HttpStatus.OK);
  }

  @GetMapping("/getListDataItem")
  public ResponseEntity<List<DataItemDTO>> getListDataItem(String dataCode) {
    List<DataItemDTO> dataItemDTOS = commonBusiness.getListDataItem(dataCode);
    return new ResponseEntity<>(dataItemDTOS, HttpStatus.OK);
  }

  @PostMapping("/getItemMaster")
  public ResponseEntity<Datatable> getItemMaster(String categoryCode, String system,
      String type, String idColName, String nameCol) {
    Datatable data = catItemBusiness.getItemMaster(categoryCode, system, type, idColName, nameCol);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getItemServiceMaster")
  public ResponseEntity<Datatable> getItemServiceMaster(String system,
      String type, String idColName, String nameCol) {
    Datatable data = catServiceBusiness.getItemServiceMaster(system, type, idColName, nameCol);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListCatItemDTOByListCategoryLE")
  public ResponseEntity<List<CatItemDTO>> getListCatItemDTOByListCategoryLE(
      @RequestParam String categories) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    List<String> lstCategory = mapper.readValue(categories, ArrayList.class);
    List<CatItemDTO> data = catItemBusiness
        .getListCatItemDTOByListCategoryLE(I18n.getLocale(), LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            "1", lstCategory);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListItemByCategoryAndParent")
  public ResponseEntity<List<CatItemDTO>> getListItemByCategoryAndParent(String categoryCode,
      String parentItemId) {
    List<CatItemDTO> data = catItemBusiness
        .getListItemByCategoryAndParent(categoryCode, parentItemId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListCatItemByListParent")
  public ResponseEntity<List<CatItemDTO>> getListCatItemByListParent(String lstParentItemId,
      String categoryId) {
    List<CatItemDTO> data = catItemBusiness.getListCatItemByListParent(lstParentItemId, categoryId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListCellService")
  public ResponseEntity<Datatable> getListCellService(@RequestBody InfraCellServiceDetailDTO dto) {
    Datatable data = catItemBusiness.getListCellService(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("getListReceiveUnitSearch")
  public ResponseEntity<Datatable> getListReceiveUnitSearch(
      @RequestBody ReceiveUnitDTO receiveUnitDTO) {
    Datatable data = receiveUnitBusiness.getListReceiveUnitSearch(receiveUnitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getConfigProperty")
  public ResponseEntity<Map<String, String>> getConfigProperty(String key) throws Exception {
    Map<String, String> configProperty = commonBusiness.getConfigProperty();
    Map<String, String> item = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(key)) {
      if (configProperty.containsKey(key)) {
        item.put(key, configProperty.get(key));
      }
      return new ResponseEntity<>(item, HttpStatus.OK);
    }
    return new ResponseEntity<>(configProperty, HttpStatus.OK);
  }

  @GetMapping("/getSubAdminViews")
  public ResponseEntity<Map<String, String>> getSubAdminViews() {
    return new ResponseEntity<>(SUB_ADMIN_EDIT_VIEW.getGetView(), HttpStatus.OK);
  }

  @GetMapping("/checkRoleSubAdmin")
  public ResponseEntity<ResultInSideDto> checkRoleSubAdmin(String view) {
    ResultInSideDto data = commonBusiness.checkRoleSubAdmin(view);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/findCatItemById")
  public ResponseEntity<CatItemDTO> findCatItemById(Long id) {
    CatItemDTO data = catItemBusiness.getCatItemById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getItemMasterHasParent")
  public ResponseEntity<Datatable> getItemMasterHasParent(String categoryCode, String system,
      String type, String idColName, String nameCol) {
    Datatable data = catItemBusiness
        .getItemMasterHasParent(categoryCode, system, type, idColName, nameCol);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getItemMasterOrderValueNumber")
  public ResponseEntity<Datatable> getItemMasterOrderValueNumber(String categoryCode, String system,
      String type, String idColName, String nameCol) {
    Datatable data = catItemBusiness
        .getItemMasterOrderValueNumber(categoryCode, system, type, idColName, nameCol);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/searchDataDashboard")
  public ResponseEntity<List<DashboardDTO>> searchDataDashboard(@RequestBody DashboardDTO dashboardDTO){
    List<DashboardDTO> data = commonBusiness.searchDataDashboard(dashboardDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getDataTableDashboard")
  public ResponseEntity<List<DashboardDTO>> getDataTableDashboard(){
    List<DashboardDTO> data = commonBusiness.getDataTableDashboard();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListRole")
  public ResponseEntity<List<RolesDTO>> getListRole(){
    List<RolesDTO> data = commonBusiness.getListRole();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListComment")
  public  ResponseEntity<Datatable> getListComment(@RequestBody UserCommentDTO userCommentDTO){
    Datatable data = commonBusiness.getListComment(userCommentDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/addComment")
  public ResponseEntity<ResultInSideDto> addComment(@RequestBody UserCommentDTO userCommentDTO){
    ResultInSideDto resultInSideDto = commonBusiness.addComment(userCommentDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @GetMapping("/getListContact")
  public  ResponseEntity<List<ContactDTO>> getListContact(){
    List<ContactDTO> data = commonBusiness.getListContact();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //Thanhlv12 Add 29-09-2020
  @PostMapping("/insertHisUserImpact")
  public ResponseEntity<ResultInSideDto> insertHisUserImpact(@RequestBody DataHistoryChange dataHistoryChange) {
    ResultInSideDto resultInSideDto = commonBusiness.insertHisUserImpact(dataHistoryChange);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getListHistory")
  public ResponseEntity<Datatable> getListHistory(@RequestBody HisUserImpactDTO hisUserImpactDTO) {
    Datatable data = commonBusiness.getListHistory(hisUserImpactDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListCommonLink")
  public ResponseEntity<List<CatItemDTO>> getListCommonLink(String locale) {
    List<CatItemDTO> data = commonBusiness.getListCommonLink(locale);
    return  new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getConfigIconDislay")
  public ResponseEntity<CatItemDTO> getConfigIconDislay(String keyCode) {
    CatItemDTO data = commonBusiness.getConfigIconDislay(keyCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
  //End

}


