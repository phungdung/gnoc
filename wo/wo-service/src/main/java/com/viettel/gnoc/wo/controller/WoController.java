package com.viettel.gnoc.wo.controller;

import com.thoughtworks.xstream.XStream;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import com.viettel.gnoc.wo.business.WoBusiness;
import com.viettel.gnoc.wo.business.WoHistoryBusiness;
import com.viettel.gnoc.wo.business.WoSPMBusiness;
import com.viettel.gnoc.wo.business.WoTicketBusiness;
import com.viettel.gnoc.wo.dto.*;
import com.viettel.qldtktts.service2.CatStationBO;
import com.viettel.qldtktts.service2.CatWarehouseBO;
import com.viettel.qldtktts.service2.CntContractBO;
import com.viettel.qldtktts.service2.ConstrConstructionsBO;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(Constants.WO_API_PATH_PREFIX + "Wo")
public class WoController {

  @Autowired
  WoBusiness woBusiness;

  @Autowired
  WoHistoryBusiness woHistoryBusiness;

  @Autowired
  WoSPMBusiness woSPMBusiness;

  @Autowired
  WoTicketBusiness woTicketBusiness;

  @PostMapping("/getListDataSearchWebProxy")
  public List<WoInsideDTO> getListDataSearchWebProxy(@RequestBody WoInsideDTO woInsideDTO) {
    Datatable data = woBusiness.getListDataSearchWeb(woInsideDTO);
    List<WoInsideDTO> list = (List<WoInsideDTO>) data.getData();
    if (list != null && list.size() > 0) {
      list.get(0).setTotalRow(data.getTotal());
    }
    return list;
  }

  @PostMapping("/getListDataSearchWeb")
  public Datatable getListDataSearchWeb(@RequestBody WoInsideDTO woInsideDTO) {
    Datatable data = woBusiness.getListDataSearchWeb(woInsideDTO);
    return data;
  }

  @PostMapping("/findWoById")
  public ResponseEntity<WoInsideDTO> findWoById(Long woId) {
    WoInsideDTO woInsideDTO = woBusiness.findWoByIdFromWeb(woId);
    return new ResponseEntity<>(woInsideDTO, HttpStatus.OK);
  }

  @RequestMapping(value = "/insertWoFromWeb", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertWoFromWeb(
      @RequestPart("fileAttacks") List<MultipartFile> fileAttacks,
      @RequestPart("fileCfgAttacks") List<MultipartFile> fileCfgAttacks,
      @Valid @RequestPart("formDataJson") WoInsideDTO woInsideDTO) throws Exception {
    ResultInSideDto result = woBusiness.insertWoFromWeb(fileAttacks, fileCfgAttacks, woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/insertWoFromWebInMrMNGT", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> insertWoFromWebInMrMNGT(
      @RequestBody WoInsideDTO woInsideDTO) throws Exception {
    ResultInSideDto result = woBusiness.insertWoFromWebInMrMNGT(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/updateWo", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> updateWo(
      @RequestPart("fileAttacks") List<MultipartFile> fileAttacks,
      @RequestPart("fileCfgAttacks") List<MultipartFile> fileCfgAttacks,
      @Valid @RequestPart("formDataJson") WoInsideDTO woInsideDTO) throws Exception {
    ResultInSideDto result = woBusiness.updateWoFromWeb(fileAttacks, fileCfgAttacks, woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/deleteListWo")
  public ResponseEntity<ResultInSideDto> deleteListWo(
      @RequestBody List<WoInsideDTO> listWoInsideDTO)
      throws Exception {
    ResultInSideDto result = woBusiness.deleteListWo(listWoInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getListWoSystemInsertWeb")
  public ResponseEntity<List<CatItemDTO>> getListWoSystemInsertWeb() {
    List<CatItemDTO> list = woBusiness.getListWoSystemInsertWeb();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListFileFromWo")
  public ResponseEntity<List<GnocFileDto>> getListFileFromWo(Long woId) {
    List<GnocFileDto> list = woBusiness.getListFileFromWo(woId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getStationListNation")
  public ResponseEntity<List<CatStationBO>> getStationListNation(String stationCode, String date) {
    List<CatStationBO> list = woBusiness.getStationListNation(stationCode, date);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListWarehouseNation")
  public ResponseEntity<List<CatWarehouseBO>> getListWarehouseNation(String warehouseCode,
      String warehouseName, String woType, String staffCode) {
    List<CatWarehouseBO> list = woBusiness
        .getListWarehouseNation(warehouseCode, warehouseName, woType, staffCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListContractFromConstrNation")
  public ResponseEntity<List<CntContractBO>> getListContractFromConstrNation(String constrtCode) {
    List<CntContractBO> list = woBusiness.getListContractFromConstrNation(constrtCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getConstructionListNation")
  public ResponseEntity<List<ConstrConstructionsBO>> getConstructionListNation(String stationCode) {
    List<ConstrConstructionsBO> list = woBusiness.getConstructionListNation(stationCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getListWoKttsAction")
  public ResponseEntity<List<CatItemDTO>> getListWoKttsAction(String key) {
    List<CatItemDTO> list = woBusiness.getListWoKttsAction(key);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/exportDataWo")
  public ResponseEntity<File> exportDataWo(@RequestBody WoInsideDTO woInsideDTO) throws Exception {
    File data = woBusiness.exportDataWo(woInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getTemplateImportWo")
  public ResponseEntity<File> getTemplateImportWo() throws Exception {
    File data = woBusiness.getTemplateImportWo();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWoHistoryByWoId")
  public ResponseEntity<Datatable> getListWoHistoryByWoId(
      @RequestBody WoHistoryInsideDTO woHistoryInsideDTO) {
    Datatable data = woBusiness.getListWoHistoryByWoId(woHistoryInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListWorklogByWoIdPaging")
  public ResponseEntity<Datatable> getListWorklogByWoIdPaging(
      @RequestBody WoWorklogInsideDTO woWorklogInsideDTO) {
    Datatable data = woBusiness.getListWorklogByWoIdPaging(woWorklogInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertWoWorklog")
  public ResponseEntity<ResultInSideDto> insertWoWorklog(@RequestBody WoInsideDTO woInsideDTO) {
    ResultInSideDto result = woBusiness.insertWoWorklog(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/loadDataOfTabCr")
  public ResponseEntity<Datatable> loadDataOfTabCr(@RequestBody WoInsideDTO woInsideDTO) {
    Datatable data = woBusiness.loadTroubleCrDTO(woInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/updateFileAttack", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> updateFileAttack(
      @RequestPart("fileAttacks") List<MultipartFile> fileAttacks,
      @RequestPart("formDataJson") WoInsideDTO woInsideDTO) throws IOException {
    ResultInSideDto result = woBusiness.updateFileAttack(fileAttacks, woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/getTemplateExportWOTestServiceFromCR")
  public ResponseEntity<File> getTemplateExportWOTestServiceFromCR() throws Exception {
    File data = woBusiness.getTemplateExportWOTestServiceFromCR();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getTemplateExportWOTestServiceFromWO")
  public ResponseEntity<File> getTemplateExportWOTestServiceFromWO() throws Exception {
    File data = woBusiness.getTemplateExportWOTestServiceFromWO();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/acceptWoFromWeb")
  public ResponseEntity<ResultInSideDto> acceptWoFromWeb(@RequestBody WoInsideDTO woInsideDTO)
      throws Exception {
    ResultInSideDto result = woBusiness.acceptWoFromWeb(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/dispatchWoFromWeb")
  public ResponseEntity<ResultInSideDto> dispatchWoFromWeb(@RequestBody WoInsideDTO woInsideDTO)
      throws Exception {
    ResultInSideDto result = woBusiness.dispatchWoFromWeb(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/rejectWoFromWeb")
  public ResponseEntity<ResultInSideDto> rejectWoFromWeb(@RequestBody WoInsideDTO woInsideDTO)
      throws Exception {
    ResultInSideDto result = woBusiness.rejectWoFromWeb(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/auditWoFromWeb")
  public ResponseEntity<ResultInSideDto> auditWoFromWeb(@RequestBody WoInsideDTO woInsideDTO)
      throws Exception {
    ResultInSideDto result = woBusiness.auditWoFromWeb(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/pendingWoFromWeb")
  public ResponseEntity<ResultInSideDto> pendingWoFromWeb(@RequestBody WoInsideDTO woInsideDTO)
      throws Exception {
    ResultInSideDto result = woBusiness.pendingWoFromWeb(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/updateStatusFromWeb")
  public ResponseEntity<ResultInSideDto> updateStatusFromWeb(@RequestBody WoInsideDTO woInsideDTO) {
    ResultInSideDto result = woBusiness.updateStatusFromWeb(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/exportDataWoFromListCr", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> exportDataWoFromListCr(
      @RequestPart("fileImport") MultipartFile fileImport, Date startTimeFrom, Date startTimeTo) {
    ResultInSideDto result = woBusiness
        .exportDataWoFromListCr(fileImport, startTimeFrom, startTimeTo);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/exportFileTestService", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> exportFileTestService(
      @RequestPart("fileImport") MultipartFile fileImport) {
    ResultInSideDto result = woBusiness.exportFileTestService(fileImport);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/updatePendingWoFromWeb")
  public ResponseEntity<ResultInSideDto> updatePendingWoFromWeb(
      @RequestBody WoInsideDTO woInsideDTO)
      throws Exception {
    ResultInSideDto result = woBusiness.updatePendingWoFromWeb(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/splitWoFromWeb", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResultInSideDto> splitWoFromWeb(
      @RequestPart("fileAttacks") List<MultipartFile> fileAttacks,
      @RequestPart("formDataJson") WoInsideDTO woInsideDTO) throws IOException {
    ResultInSideDto result = woBusiness.splitWoFromWeb(fileAttacks, woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/approveWoFromWeb")
  public ResponseEntity<ResultInSideDto> approveWoFromWeb(@RequestBody WoInsideDTO woInsideDTO)
      throws Exception {
    ResultInSideDto result = woBusiness.approveWoFromWeb(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/callIPCC")
  public ResponseEntity<ResultInSideDto> callIPCC(@RequestBody WoInsideDTO woInsideDTO) {
    ResultInSideDto result = woBusiness.callIPCC(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListLogCallIpccDTO")
  public ResponseEntity<Datatable> getListLogCallIpccDTO(
      @RequestBody LogCallIpccDTO logCallIpccDTO) {
    Datatable data = woBusiness.getListLogCallIpccDTO(logCallIpccDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getWoPriorityByWoTypeID")
  public ResponseEntity<List<WoPriorityDTO>> getWoPriorityByWoTypeID(Long woTypeId) {
    List<WoPriorityDTO> data = woBusiness.getWoPriorityByWoTypeID(woTypeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/completeWoSPM")
  public ResponseEntity<ResultInSideDto> completeWoSPM(@RequestBody WoInsideDTO woInsideDTO) {
    ResultInSideDto result = woBusiness.completeWoSPM(woInsideDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListWoChild")
  public ResponseEntity<Datatable> getListWoChild(@RequestBody WoInsideDTO woInsideDTO) {
    Datatable data = woBusiness.getListWoChild(woInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListWoByWoTypeId/woTypeId{woTypeId}")
  public List<WoInsideDTO> getListWoByWoTypeId(@PathVariable Long woTypeId) {
    List<WoInsideDTO> list = woBusiness.getListWoByWoTypeId(woTypeId);
    return list;
  }

  @PostMapping("/insertWoWorklogProxy")
  public ResponseEntity<ResultDTO> insertWoWorklogProxy(@RequestBody WoWorklogDTO woWorklogDTO) {
    ResultDTO result = woBusiness.insertWoWorklog(woWorklogDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListDataSearchProxy")
  public ResponseEntity<List<WoDTOSearch>> getListDataSearchProxy(
      @RequestBody WoDTOSearch woDTOSearch) {
    if (StringUtils.isNotNullOrEmpty(woDTOSearch.getProxyLocale())) {
      I18n.setLocale(woDTOSearch.getProxyLocale());
    }
    List<WoDTOSearch> result = woBusiness.getListDataSearch(woDTOSearch);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getWOStatisticProxy")
  public ResponseEntity<List<ResultDTO>> getWOStatisticProxy(
      @RequestBody WoStatisticDTO woStatisticDTO) {
    List<ResultDTO> lstData = woBusiness
        .getWOStatistic(woStatisticDTO.getUnitId(), woStatisticDTO.getIsSend(),
            woStatisticDTO.getIsSearchChild(), woStatisticDTO.getFromDate(),
            woStatisticDTO.getToDate());
    return new ResponseEntity<>(lstData, HttpStatus.OK);
  }

  @PostMapping("/getWOTotalProxy")
  public ResponseEntity<Integer> getWOTotalProxy(@RequestBody WoDTOSearch woDtoSearch) {
    Integer result = woBusiness.getWOTotal(woDtoSearch);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/findWoByIdProxy{woId}")
  public ResponseEntity<WoInsideDTO> findWoByIdProxy(@PathVariable(value = "woId") Long woId) {
    WoInsideDTO woDTO = woBusiness.findWoByIdNoOffset(woId);
    return new ResponseEntity<>(woDTO, HttpStatus.OK);
  }


  @PostMapping("/insertWoForSPMProxy")
  public ResponseEntity<ResultDTO> insertWoForSPMProxy(@RequestBody WoDTO woDTO) {
    try {
      XStream xstream = new XStream();
      xstream.alias("woDTO", WoDTO.class);
//    xstream.addImplicitCollection(KpignocForm.class, "list");
      String xml = xstream.toXML(woDTO);
      log.info("insertWoForSPMProxy", woDTO);
      log.info(xml);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    try {
      ResultDTO resultDTO = woBusiness.insertWoForSPM(woDTO);
      return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage()),
          HttpStatus.OK);
    }
  }

  @PostMapping("/changeStatusWoProxy")
  public ResponseEntity<ResultDTO> changeStatusWoProxy(
      @RequestBody WoUpdateStatusForm woUpdateStatusForm) {
    try {
      XStream xstream = new XStream();
      xstream.alias("woUpdateStatusForm", WoUpdateStatusForm.class);
//    xstream.addImplicitCollection(KpignocForm.class, "list");
      String xml = xstream.toXML(woUpdateStatusForm);
      log.info("changeStatusWoProxy", woUpdateStatusForm);
      log.info(xml);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    try {
      ResultDTO resultDTO = woBusiness.changeStatusWo(woUpdateStatusForm);
      return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage()),
          HttpStatus.OK);
    }
  }

  @GetMapping("/findWoByIdWSProxy/woId{woId}")
  public WoDTO findWoByIdWSProxy(@PathVariable(value = "woId") Long woId) {
    return woBusiness.findWoById(woId);
  }

  @GetMapping("/deleteWo/woId{woId}")
  public ResponseEntity<ResultInSideDto> deleteWo(@PathVariable("woId") Long woId) {
    return new ResponseEntity<>(woBusiness.deleteWo(woId), HttpStatus.OK);
  }

  @PostMapping("/createWoProxy")
  public ResponseEntity<ResultDTO> createWoProxy(@RequestBody WoDTO woDTO) {
    try {
      XStream xstream = new XStream();
      xstream.alias("woDTO", WoDTO.class);
//    xstream.addImplicitCollection(KpignocForm.class, "list");
      String xml = xstream.toXML(woDTO);
      log.info("createWoProxy", woDTO);
      log.info(xml);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    try {
      if(StringUtils.isNotNullOrEmpty(woDTO.getProxyLocale())){
        I18n.setLocale(woDTO.getProxyLocale());
      }
      ResultDTO resultDTO = woBusiness.createWo(woDTO);
      return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage()),
          HttpStatus.OK);
    }
  }

  @GetMapping("/deleteWOForRollbackProxy/woCode{woCode}/reason{reason}/system{system}")
  public ResultDTO deleteWOForRollbackProxy(@PathVariable(value = "woCode") String woCode,
      @PathVariable(value = "reason") String reason,
      @PathVariable(value = "system") String system) {
    return woBusiness.deleteWOForRollback(woCode, reason, system);
  }

  @PostMapping("/updateWoProxy")
  public ResponseEntity<String> updateWoProxy(@RequestBody WoDTO woDTO) {
    try {
      XStream xstream = new XStream();
      xstream.alias("woDTO", WoDTO.class);
//    xstream.addImplicitCollection(KpignocForm.class, "list");
      String xml = xstream.toXML(woDTO);
      log.info("updateWoProxy", woDTO);
      log.info(xml);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    try {
      String result = woBusiness.updateWo(woDTO);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(RESULT.FAIL, HttpStatus.OK);
    }
  }

  @PostMapping("/getListDataForRisk")
  public List<WoInsideDTO> getListDataForRisk(@RequestBody WoInsideDTO woInsideDTO) {
    return woBusiness.getListDataForRisk(woInsideDTO);
  }

  //them xu ly run mop
  @PostMapping("/runMopOnSAP")
  public ResultInSideDto runMopOnSAP(@RequestBody WoInsideDTO woInsideDTO) {
    log.debug("Request to runMopOnSAP: {}", woInsideDTO);
    return woBusiness.runMopOnSAP(woInsideDTO);
  }

  @GetMapping("/getWoSearchWebDTOByWoCode/code{code}")
  public WoSearchWebDTO getWoSearchWebDTOByWoCode(
      @PathVariable("code") String code) {
    return woBusiness.getWoSearchWebDTOByWoCode(code);
  }

  @PostMapping("/closeWoProxy")
  public ResultDTO closeWoProxy(@RequestBody RequestApiWODTO requestApiWODTO) {
    return woBusiness.closeWo(requestApiWODTO.getListCode(), requestApiWODTO.getSystem());
  }

  @PostMapping("/updateWoForSPM")
  public ResultDTO updateWoForSPMProxy(@RequestBody WoDTO woDTO) {
    return woBusiness.updateWoForSPM(woDTO);
  }

  @PostMapping("/updateWoInfo")
  public ResultDTO updateWoInfoProxy(@RequestBody WoDTO woDTO) throws Exception {
    return woBusiness.updateWoInfo(woDTO);
  }

  @PostMapping("/updatePendingWo")
  public ResultDTO updatePendingWo(@RequestBody RequestApiWODTO requestApiWODTO) throws Exception {
    return woBusiness
        .updatePendingWo(requestApiWODTO.getWoCode(), requestApiWODTO.getEndPendingTime(),
            requestApiWODTO.getUser(), requestApiWODTO.getComment(), requestApiWODTO.getSystem(),
            requestApiWODTO.getCallCC());
  }

  @PostMapping("/pendingWo")
  public ResultDTO pendingWo(@RequestBody RequestApiWODTO requestApiWODTO) throws Exception {
    return woBusiness
        .pendingWo(requestApiWODTO.getWoCode(), requestApiWODTO.getEndPendingTime(),
            requestApiWODTO.getUser(), requestApiWODTO.getSystem(), requestApiWODTO.getReasonName(),
            requestApiWODTO.getReasonId(), requestApiWODTO.getCustomer(),
            requestApiWODTO.getPhone());
  }

  @PostMapping("/closeWoForSPM")
  public ResultDTO closeWoForSPM(@RequestBody RequestApiWODTO requestApiWODTO) {
    return woSPMBusiness
        .closeWoForSPM(requestApiWODTO.getLstWo(), requestApiWODTO.getSystem(),
            requestApiWODTO.getUser(), requestApiWODTO.getReasonLevel3Id());
  }

  @GetMapping("/getConfigProperty")
  public String getConfigProperty() {
    return woBusiness.getConfigProperty();
  }

  @PostMapping("/createWoFollowNode")
  public ResultDTO createWoFollowNode(@RequestBody RequestApiWODTO requestApiWODTO) {
    return woBusiness
        .createWoFollowNode(requestApiWODTO.getCreateWoDto(), requestApiWODTO.getListNode());
  }

  @PostMapping("/completeWorkHelp")
  public ResultDTO completeWorkHelp(@RequestBody RequestApiWODTO requestApiWODTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      resultDTO = woTicketBusiness
          .completeWorkHelp(requestApiWODTO.getWoCode(), requestApiWODTO.getUserName(),
              requestApiWODTO.getWorkLog(), requestApiWODTO.getReasonCcId());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultDTO;
  }

  @PostMapping("/getListWoHistoryBySystem")
  public List<WoHistoryDTO> getListWoHistoryBySystem(@RequestBody RequestApiWODTO requestApiWODTO)
      throws ParseException {
    return woHistoryBusiness
        .getListWoHistoryBySystem(requestApiWODTO.getUserName(), requestApiWODTO.getWoId(),
            requestApiWODTO.getSystem(), requestApiWODTO.getSystemId(),
            requestApiWODTO.getStartDate(), requestApiWODTO.getEndDate());
  }

  @PostMapping("/insertWoHistory")
  public ResultInSideDto insertWoHistory(@RequestBody WoHistoryInsideDTO woHistoryInsideDTO) {
    return woHistoryBusiness.insertWoHistory(woHistoryInsideDTO);
  }

  @PostMapping("/getListWoByCondition")
  public List<WoDTO> getListWoByCondition(@RequestBody BaseDto baseDto) {
    return woBusiness
        .getListWoByCondition(baseDto.getLstCondition(), baseDto.getPage(), baseDto.getPageSize(),
            baseDto.getSortType(), baseDto.getSortName());
  }


  @GetMapping("/getSequenseWoProxy/sequenseWo{sequenseWo}/size{size}")
  public List<String> getSequenseWoProxy(@PathVariable("sequenseWo") String sequenseWo,
      @PathVariable("size") int size) {
    return woBusiness.getSequenseWo(sequenseWo, size);
  }

  @PostMapping("/insertWoProxy")
  public ResultDTO insertWoProxy(@RequestBody WoDTO woDTO) {
    StringUtils.printLogData("insertWoProxy", woDTO, WoDTO.class);
    try {
      return woBusiness.insertWo(woDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @PostMapping("/getListWoDTO/rowStart{rowStart}/maxRow{maxRow}/sortType{sortType}/sortFieldList{sortFieldList}")
  public List<WoDTO> getListWoDTO(@RequestBody WoDTO woDTO,
      @PathVariable(value="rowStart") int rowStart, @PathVariable(value="maxRow") int maxRow,
      @PathVariable(value="sortType") String sortType, @PathVariable(value="sortFieldList") String sortFieldList) {
    return woBusiness.getListWoDTO(woDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @PostMapping("/getListWoDTOByWoSystemId/woSystemId{woSystemId}")
  public List<WoInsideDTO> getListWoDTOByWoSystemId(@PathVariable(value="woSystemId") String woSystemId) {
    return woBusiness.getListWoDTOByWoSystemId(woSystemId);
  }

  @PostMapping("/getListWoMerchandiseDTO")
  public ResponseEntity<List<WoMerchandiseInsideDTO>> getListWoMerchandiseDTO(@RequestBody WoMerchandiseInsideDTO woMerchandiseInsideDTO) {
    List<WoMerchandiseInsideDTO> list = woBusiness.getListWoMerchandiseDTO(woMerchandiseInsideDTO);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/findWoByWoCodeNoOffset/woSystemId{woSystemId}")
  public ResponseEntity<WoInsideDTO> findWoByWoCodeNoOffset(@PathVariable(value="woSystemId") String woSystemId) {
    WoInsideDTO woInsideDTO = woBusiness.findWoByWoCodeNoOffset(woSystemId);
    return new ResponseEntity<>(woInsideDTO, HttpStatus.OK);
  }

  @GetMapping("/getListMaterialByWoId")
  public ResponseEntity<List<MaterialThresDTO>> getListMaterialByWoId(Long woId) {
    List<MaterialThresDTO> list = woBusiness.getListMaterialByWoId(woId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  //hungtv add

  @PostMapping("/getListConfigAutoCreateWoOs")
  public ResponseEntity<Datatable> getListConfigAutoCreateWoOs(@RequestBody AutoCreateWoOsDTO autoCreateWoOsDTO) {
    Datatable data = woBusiness.getListConfigAutoCreateWoOs(autoCreateWoOsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateAutoCreateWoOs")
  public ResponseEntity<ResultInSideDto> insertOrUpdateAutoCreateWoOs(@RequestBody AutoCreateWoOsDTO autoCreateWoOsDTO) {
    ResultInSideDto data = woBusiness.insertOrUpdateAutoCreateWoOs(autoCreateWoOsDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getConfigById")
  public ResponseEntity<AutoCreateWoOsDTO> getConfigById(Long id) {
    AutoCreateWoOsDTO data = woBusiness.getConfigById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/delete")
  public ResponseEntity<ResultInSideDto> delete(Long id) {
    ResultInSideDto data = woBusiness.delete(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/syncFileFromWeb")
  public ResponseEntity<ResultInSideDto> syncFileFromWeb(Long woId) {
    ResultInSideDto data = woBusiness.syncFileFromWeb(woId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
