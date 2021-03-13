package com.viettel.gnoc.incident.controller;

import com.viettel.bccs.cc.service.ProblemGroupDTO;
import com.viettel.bccs.cc.service.ProblemTypeDTO;
import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TroubleStatisticForm;
import com.viettel.gnoc.commons.dto.TroubleStatisticFormDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.CrSearchDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.incident.business.CatReasonBusiness;
import com.viettel.gnoc.incident.business.ItAccountBusiness;
import com.viettel.gnoc.incident.business.TransmissionInfoBusiness;
import com.viettel.gnoc.incident.business.TroubleBRCDBusiness;
import com.viettel.gnoc.incident.business.TroubleCardBusiness;
import com.viettel.gnoc.incident.business.TroubleMopBusiness;
import com.viettel.gnoc.incident.business.TroubleMopDtBusiness;
import com.viettel.gnoc.incident.business.TroubleNodeBusiness;
import com.viettel.gnoc.incident.business.TroublesActionlogBusiness;
import com.viettel.gnoc.incident.business.TroublesBusiness;
import com.viettel.gnoc.incident.business.TroublesInfoTickHelpBusiness;
import com.viettel.gnoc.incident.business.TroublesProblemsBusiness;
import com.viettel.gnoc.incident.business.TroublesRelatedBusiness;
import com.viettel.gnoc.incident.business.TroublesWorklogBusiness;
import com.viettel.gnoc.incident.business.WOCreateBusiness;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTOSearch;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.CfgSupportFormDTO;
import com.viettel.gnoc.incident.dto.InfraCableLaneDTO;
import com.viettel.gnoc.incident.dto.LinkInfoDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleCardDTO;
import com.viettel.gnoc.incident.dto.TroubleCardInsertDTO;
import com.viettel.gnoc.incident.dto.TroubleMopInsiteDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroubleWorklogEntity;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.risk.dto.RiskDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TienNV
 */
@Slf4j
@RestController
@RequestMapping(Constants.TT_API_PATH_PREFIX + "Troubles")
public class TroublesController {

  @Autowired
  private TroublesBusiness troublesBusiness;

  @Autowired
  private TroublesWorklogBusiness troublesBusinessWorklog;

  @Autowired
  private TroublesProblemsBusiness troublesProblems;

  @Autowired
  private TroublesRelatedBusiness troublesRelatedBusiness;

  @Autowired
  private TroublesActionlogBusiness troublesActionlogBusiness;

  @Autowired
  private CatItemBusiness catItemBusiness;

  @Autowired
  TransmissionInfoBusiness transmissionInfoBusiness;

  @Autowired
  TroubleCardBusiness troubleCardBusiness;

  @Autowired
  TroubleMopBusiness troubleMopBusiness;

  @Autowired
  CatReasonBusiness catReasonBusiness;

  @Autowired
  TroublesInfoTickHelpBusiness troublesInfoTickHelpBusiness;

  @Autowired
  TroubleBRCDBusiness troubleBRCDBusiness;

  @Autowired
  TroubleNodeBusiness troubleNodeBusiness;

  @Autowired
  ItAccountBusiness itAccountBusiness;

  @Autowired
  TroubleMopDtBusiness troubleMopDtBusiness;

  @Autowired
  WOCreateBusiness woCreateBusiness;


  @PostMapping("/onSearch")
  public ResponseEntity<Datatable> onSearch(@RequestBody TroublesInSideDTO troublesDTO) {
    Datatable datatable;
    try {
      datatable = troublesBusiness.onSearch(troublesDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      datatable = new Datatable();
    }
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/onSearchTroubleRelated")
  public ResponseEntity<Datatable> onSearchTroubleRelated(
      @RequestBody TroublesInSideDTO troublesDTO) {
    Datatable data = troublesBusiness.onSearchTroubleRelated(troublesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertTrouble")
  public ResponseEntity<ResultInSideDto> insertTrouble(
      @RequestPart("files") List<MultipartFile> files,
      @RequestPart("formDataJson") TroublesInSideDTO troublesDTO) throws Exception {
    ResultInSideDto resultInSideDto = troublesBusiness
        .insertTroublesTT(troublesDTO.getAuthorityDTO(), files, troublesDTO,
            troublesDTO.getListAccount());
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/onUpdateTrouble")
  public ResponseEntity<ResultInSideDto> onUpdateTrouble(
      @RequestPart("files") List<MultipartFile> files,
      @RequestPart("formDataJson") TroublesInSideDTO troublesDTO) throws Exception {
    ResultInSideDto resultInSideDto = troublesBusiness.onUpdateTroubleTT(troublesDTO, files);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/onUpdateTroubleEntity")
  public ResponseEntity<ResultInSideDto> onUpdateTroubleEntity(
      @RequestBody TroublesInSideDTO troublesDTO) {
    ResultInSideDto resultInSideDto = troublesBusiness.updateTrouble(troublesDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/sendChatListUsers")
  public ResponseEntity<ResultInSideDto> sendChatListUsers(
      @RequestBody TroublesInSideDTO troublesDTO)
      throws Exception {
    return new ResponseEntity<>(troublesBusiness.sendChatListUsers(troublesDTO), HttpStatus.OK);
  }

  @PostMapping("/callIPCC")
  public ResponseEntity<ResultInSideDto> callIPCC(@RequestBody TroublesInSideDTO troublesDTO)
      throws Exception {
    return new ResponseEntity<>(troublesBusiness.callIPCC(troublesDTO), HttpStatus.OK);
  }

  @GetMapping("/findTroubleById")
  public ResponseEntity<TroublesInSideDTO> findTroubleById(Long id) throws Exception {
    TroublesInSideDTO data = troublesBusiness.findTroublesById(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // save processing troubleworklogs
  @PostMapping("/insertTroubleWorklog")
  public ResponseEntity<ResultInSideDto> insertTroubleWorklog(
      @RequestBody TroubleWorklogEntity entity) {
    ResultInSideDto resultInSideDto = troublesBusinessWorklog.insertTroubleWorklog(entity);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  // get list troubleWorklog by trouble id
  @PostMapping("/getListTroubleWorklogByTroubleId")
  public ResponseEntity<Datatable> getListTroubleWorklogByTroubleId(
      @RequestBody TroubleWorklogInsiteDTO troubleWorklogDTO) {
    Datatable data = troublesBusinessWorklog.getListTroubleWorklogByTroubleId(troubleWorklogDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //troubleFile
  @PostMapping("/getListFileAttachByTroubleId")
  public ResponseEntity<Datatable> getListFileAttachByTroubleId(
      @RequestBody GnocFileDto gnocFileDto) {
    Datatable datatable = troublesBusiness.getListFileAttachByTroubleId(gnocFileDto);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  //troubleFile
  @PostMapping("/insertTroubleFilesUpload")
  public ResponseEntity<ResultInSideDto> insertTroubleFilesUpload(
      @RequestPart("files") List<MultipartFile> files,
      @RequestPart("formDataJson") TroublesInSideDTO troublesDTO) throws IOException {
    ResultInSideDto resultInSideDto = troublesBusiness
        .insertTroubleFilesUpload(files, troublesDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  // problems
  @PostMapping("/getListProblems")
  public ResponseEntity<Datatable> getListProblems(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) {
    Datatable data = troublesProblems.getListProblems(problemsInsideDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListRelatedTT")
  public ResponseEntity<Datatable> getListRelatedTT(@RequestBody TroublesInSideDTO dto) {
    Datatable data = troublesRelatedBusiness.getListRelatedTT(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // getListRelatedTT by popup
  @PostMapping("/getListRelatedTTByPopup")
  public ResponseEntity<Datatable> getListRelatedTTByPopup(@RequestBody TroublesInSideDTO dto) {
    Datatable data = troublesRelatedBusiness.getListRelatedTTByPopup(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // getListRelatedTT by popup bo sung
  @PostMapping("/getListRelatedTTByPopupAdd")
  public ResponseEntity<Datatable> getListRelatedTTByPopupAdd(@RequestBody TroublesInSideDTO dto) {
    Datatable data = troublesRelatedBusiness.getListRelatedTTByPopupAdd(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteTrouble")
  public ResponseEntity<ResultInSideDto> deleteTrouble(Long id) {
    ResultInSideDto dto = troublesBusiness.deleteTrouble(id);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @PostMapping("/viewCall")
  public ResponseEntity<Datatable> viewCall(@RequestBody TroublesInSideDTO dtoTran) {
    return new ResponseEntity<>(troublesBusiness.viewCall(dtoTran), HttpStatus.OK);
  }

  @PostMapping("/getListChatUsers")
  public ResponseEntity<List<UsersInsideDto>> getListChatUsers(
      @RequestBody TroublesInSideDTO troublesDTO) {
    List<UsersInsideDto> result = troublesBusiness.getListChatUsers(troublesDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListTroubleActionLogsDTO")
  public ResponseEntity<Datatable> getListTroubleActionLogsDTO(
      @RequestBody TroublesInSideDTO troublesDTO) {
    Datatable datatable = troublesActionlogBusiness.getListTroubleActionLogsDTO(troublesDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/countByState")
  public ResponseEntity<List<CatItemDTO>> countByState(@RequestBody TroublesInSideDTO troublesDTO) {
    List<CatItemDTO> catItemDTOS = troublesBusiness.onSearchCountByState(troublesDTO);
    return new ResponseEntity<>(catItemDTOS, HttpStatus.OK);
  }

  @GetMapping("/getLstNetworkLevel")
  public ResponseEntity<List<CatItemDTO>> getLstNetworkLevel(@RequestParam String typeId) {
    List<CatItemDTO> data = troublesBusiness.getLstNetworkLevel(typeId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


  @PostMapping("/getListReasonBCCS")
  public ResponseEntity<ResultInSideDto> getListReasonBCCS(
      @RequestBody TroublesInSideDTO troublesDTO) {
    ResultInSideDto result = troublesBusiness
        .getListReasonBCCS(troublesDTO, troublesDTO.getParentId(), troublesDTO.getLevel());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/getListReasonOverdue")
  public ResponseEntity<List> getListReasonOverdue(@RequestBody TroublesInSideDTO troublesDTO) {
    List lst = troublesBusiness.getListReasonOverdue(troublesDTO, troublesDTO.getParentId());
    return new ResponseEntity<>(lst, HttpStatus.OK);
  }
  //cuong tm
  //tab	Thông tin truyền dẫn

  //Loai mang truyen dan
  @PostMapping("/getListItemByCategory")
  public ResponseEntity<List<CatItemDTO>> getListItemByCategory() {
    List<String> lstCategory = new ArrayList<>();
    lstCategory.add("TT_TRANS_NW_TYPE");
    List<CatItemDTO> data = catItemBusiness.getListCatItemDTOByListCategory(lstCategory);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListCatReason")
  public ResponseEntity<List<CatItemDTO>> getListCatReason(String itemId) {
    //truyen theo itemid
    List<CatItemDTO> data = transmissionInfoBusiness.getListCatReason(itemId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //loai cap
  @GetMapping("/getListCableType")
  public ResponseEntity<List<CatItemDTO>> getListCableType(String lineCutCode,
                                                           String codeSnippetOff) {
    List<CatItemDTO> data = transmissionInfoBusiness.getListCableType(lineCutCode, codeSnippetOff);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //search  dut tuyen
  @PostMapping("/onSearchInfraCableLaneDTO")
  public ResponseEntity<Datatable> onSearchInfraCableLaneDTO(
      @RequestBody InfraCableLaneDTO infraCableLaneDTO) {
    Datatable data = transmissionInfoBusiness.onSearchInfraCableLaneDTO(infraCableLaneDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //doan dut
  @GetMapping("/getListSnippetOff")
  public ResponseEntity<List<CatItemDTO>> getListSnippetOff(@RequestParam String lineCutCode) {
    List<CatItemDTO> data = transmissionInfoBusiness.getListSnippetOff(lineCutCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //View list
  @GetMapping("/getListLinkInfoDTO")
  public ResponseEntity<List<LinkInfoDTO>> getListLinkInfoDTO(@RequestParam String codeSnippetOff) {
    List<LinkInfoDTO> data = transmissionInfoBusiness.getListLinkInfoDTO(codeSnippetOff);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //search mang xong thay the
  @PostMapping("/onSearchInfraSleevesDTO")
  public ResponseEntity<Datatable> onSearchInfraSleevesDTO(
      @RequestBody TroublesInSideDTO troublesDTO) {
    Datatable data = transmissionInfoBusiness
        .onSearchInfraSleevesDTO(troublesDTO.getClosuresReplace(), troublesDTO.getCodeSnippetOff(),
            troublesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //tab mop
  @PostMapping("/getListTroubleMopDTO")
  public ResponseEntity<Datatable> getListTroubleMopDTO(
      @RequestBody TroubleMopInsiteDTO troubleMopDTO) {
    //  condition.setTroubleId(troubleId);
    Datatable data = troubleMopBusiness.getListTroubleMopDTO(troubleMopDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListTroubleMopDtDTO")
  public ResponseEntity<Datatable> getListTroubleMopDtDTO(
      @RequestBody TroubleMopInsiteDTO troubleMopDTO) {
    Datatable data = troubleMopBusiness.getListTroubleMopDtDTO(troubleMopDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateTroubleMop")
  public ResponseEntity<ResultInSideDto> updateTroubleMop(
      @RequestBody TroubleMopInsiteDTO troubleMopDTO) {
    ResultInSideDto data = troubleMopBusiness.updateTroubleMop(troubleMopDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //tab Thông tin hàng hóa lỗi
  @PostMapping("/getListTroubleCardDTO")
  public ResponseEntity<Datatable> getListTroubleCardDTO(
      @RequestBody TroubleCardDTO troubleCardDTO) {
    Datatable data = troubleCardBusiness.getListTroubleCardDTO(troubleCardDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/updateTroubleCardDTO")
  public ResponseEntity<ResultInSideDto> updateTroubleCardDTO(
      @RequestBody TroubleCardDTO troubleCardDTO) {
    ResultInSideDto data = troubleCardBusiness.updateTroubleCardDTO(troubleCardDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertTroubleCardDTO")
  public ResponseEntity<ResultInSideDto> insertTroubleCardDTO(
      @RequestBody TroubleCardDTO troubleCardDTO) {
    ResultInSideDto data = troubleCardBusiness.insertTroubleCardDTO(troubleCardDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertOrUpdateTroubleCard")
  public ResponseEntity<ResultInSideDto> insertOrUpdateTroubleCard(
      @RequestBody List<TroubleCardDTO> troubleCardDTOS) {
    ResultInSideDto data = troubleCardBusiness.insertOrUpdateTroubleCard(troubleCardDTOS);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/deleteTroubleCard")
  public ResponseEntity<ResultInSideDto> deleteTroubleCard(Long id) {
    ResultInSideDto data = troubleCardBusiness.deleteTroubleCard(id);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/deleteListTroubleCard")
  public ResponseEntity<ResultInSideDto> deleteListTroubleCard(
      @RequestBody List<TroubleCardDTO> troubleCardDTOS) {
    ResultInSideDto data = troubleCardBusiness.deleteListTroubleCard(troubleCardDTOS);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertlistTroubleCard")
  public ResponseEntity<ResultInSideDto> insertlistTroubleCard(
      @RequestBody TroubleCardInsertDTO troubleCardInsertDTOS) {
    ResultInSideDto data = troubleCardBusiness.insertListTroubleCard(troubleCardInsertDTOS);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //tab wo
  @PostMapping("/getListDataSearch")
  public ResponseEntity<Datatable> getListDataSearch(@RequestBody TroublesInSideDTO troublesDTO) {
    Datatable data = troublesBusiness.getDataOfTabWO(troublesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //---
  @GetMapping("/getListReasonSearch")
  public ResponseEntity<List<CatItemDTO>> getListReasonSearch(Long parentId, String excludeCode) {
    List<CatItemDTO> data = catReasonBusiness.getListCatReason(parentId, excludeCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListGroupSolution")
  public ResponseEntity<List> getListGroupSolution(@RequestBody TroublesInSideDTO troublesDTO)
      throws Exception {
    List data = troublesBusiness.getListGroupSolution(troublesDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // BRCD
  @PostMapping("/getInfoBRCDByTroubleId")
  public ResponseEntity<TroublesInSideDTO> getInfoBRCDByTroubleId(@RequestParam Long troubleId) {
    TroublesInSideDTO dto = troubleBRCDBusiness.getInfoBRCDByTroubleId(troubleId);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  // BRCD
  @PostMapping("/getInsertOrUpdateInfoBRCD")
  public ResponseEntity<ResultInSideDto> getInsertOrUpdateInfoBRCD(
      @RequestBody TroublesInSideDTO troublesDTO) {
    ResultInSideDto resultInSideDto = troubleBRCDBusiness.getInsertOrUpdateInfoBRCD(troublesDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  // Info tick help
  @PostMapping("/getListInfoTickHelpByWoCode")
  public ResponseEntity<Datatable> getListInfoTickHelpByWoCode(@RequestBody CfgSupportFormDTO dto) {
    Datatable data = troublesInfoTickHelpBusiness.getListInfoTickHelpByWoCode(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/searchCrRelated")
  public ResponseEntity<Datatable> searchCrRelated(@RequestBody CrSearchDTO dto) {
    Datatable data = troublesBusiness.searchCrRelated(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/loadDataOfTabCr")
  public ResponseEntity<Datatable> loadDataOfTabCr(@RequestBody TroublesInSideDTO dto) {
    Datatable data = troublesBusiness.loadTroubleCrDTO(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/loadDataOfTabNetworkNode")
  public ResponseEntity<Datatable> loadDataOfTabNetworkNode(@RequestBody TroublesInSideDTO dto) {
    Datatable data = troubleNodeBusiness.getListTroubleNodeDTO(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/loadDataOfTabItAccount")
  public ResponseEntity<Datatable> loadDataOfTabItAccount(@RequestBody TroublesInSideDTO dto) {
    Datatable data = itAccountBusiness.getListItAccountDTO(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/loadCrRelatedDetail")
  public ResponseEntity<List<CrSearchDTO>> loadCrRelatedDetail(@RequestParam String crRelatedCode) {
    List<CrSearchDTO> data = troublesBusiness.loadCrRelatedDetail(crRelatedCode);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/loadUserSupportGroup")
  public ResponseEntity<Datatable> loadUserSupportGroup(
      @RequestBody TroublesInSideDTO troublesDTO) {
    Datatable datatable = troublesBusiness.loadUserSupportGroup(troublesDTO);
    return new ResponseEntity<>(datatable, HttpStatus.OK);
  }

  @PostMapping("/sendTicketToTKTU")
  public ResponseEntity<ResultInSideDto> sendTicketToTKTU(
      @RequestBody TroublesInSideDTO troublesDTO) throws Exception {
    ResultInSideDto resultInSideDto = troublesBusiness.sendTicketToTKTU(troublesDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/insertCrCreatedFromOtherSystem")
  public ResponseEntity<ResultInSideDto> insertCrCreatedFromOtherSystem(
      @RequestBody TroublesInSideDTO troublesDTO) {
    troublesBusiness.insertCrCreatedFromOtherSystem(troublesDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getAlarmClearGNOC")
  public ResponseEntity<ResultInSideDto> getAlarmClearGNOC(
      @RequestBody TroublesInSideDTO troublesDTO) throws Exception {
    ResultInSideDto resultInSideDto = troublesBusiness.getAlarmClearGNOC(troublesDTO);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/checkWoRequiredClosed")
  public ResponseEntity<ResultInSideDto> checkWoRequiredClosed(
      @RequestBody TroublesInSideDTO troubleDTO) {
    String result = troublesBusiness.checkWoRequiredClosed(troubleDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, result);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getListDataSearchWo")
  public ResponseEntity<List<WoDTOSearch>> getListDataSearchWo(
      @RequestBody WoDTOSearch woDTOSearch) {
    List<WoDTOSearch> woDTOSearches = troublesBusiness.getListDataSearchWo(woDTOSearch);
    return new ResponseEntity<>(woDTOSearches, HttpStatus.OK);
  }

  @PostMapping("/changeStatusWo")
  public ResponseEntity<ResultDTO> changeStatusWo(
      @RequestBody WoUpdateStatusForm woUpdateStatusForm) {
    ResultDTO resultDTO = woCreateBusiness.changeStatusWo(woUpdateStatusForm);
    return new ResponseEntity<>(resultDTO, HttpStatus.OK);
  }

  @PostMapping("/searchParentTTForCR")
  public Datatable searchParentTTForCR(@RequestBody TroublesInSideDTO dto) {
    Datatable data = troublesBusiness.searchParentTTForCR(dto);
    return data;
  }

  @PostMapping("/countTicketByShift")
  public List<TroublesInSideDTO> countTicketByShift(@RequestBody TroublesInSideDTO troublesDTO) {
    return troublesBusiness.countTicketByShift(troublesDTO);
  }

  @PostMapping("/onUpdateTroubleMobile")
  public ResultDTO onUpdateTroubleMobile(@RequestBody TroublesDTO troublesDTO) throws Exception {
    ResultDTO resultDTO = troublesBusiness.onUpdateTroubleMobile(troublesDTO);
    return resultDTO;
  }

  @PostMapping("/onClosetroubleFromWo")
  public ResultDTO onClosetroubleFromWo(@RequestBody TroublesDTO troublesDTO) {
    ResultDTO resultDTO = troublesBusiness.onClosetroubleFromWo(troublesDTO);
    return resultDTO;
  }

  @GetMapping("/checkAlarmNOC/troubleCode/{troubleCode}/typeWo/{typeWo}")
  public ResultDTO checkAlarmNOC(@PathVariable String troubleCode, @PathVariable String typeWo) {
    ResultDTO resultDTO = troublesBusiness.checkAlarmNOC(troubleCode, typeWo);
    return resultDTO;
  }

  @GetMapping("/getTroubleByCode/troubleCode/{troubleCode}")
  public List<TroublesDTO> getTroubleByCode(@PathVariable String troubleCode) {
    return troublesBusiness.getTroubleByCode(troubleCode);
  }

  @PostMapping("/getStatisticTroubleTotal")
  public List<TroubleStatisticForm> getStatisticTroubleTotal(
      @RequestBody TroubleStatisticFormDTO troubleStatisticFormDTO) {
    return troublesBusiness.getStatisticTroubleTotal(troubleStatisticFormDTO.getUnitId(),
        troubleStatisticFormDTO.getIsCreateUnit(), troubleStatisticFormDTO.getSearchChild(),
        troubleStatisticFormDTO.getStartTime(), troubleStatisticFormDTO.getEndTime());
  }

  @PostMapping("/getInfoTicketForAMI")
  public List<TroublesDTO> getInfoTicketForAMI(@RequestBody TroublesDTO troublesDTO) {
    List<TroublesDTO> list = troublesBusiness.getInfoTicketForAMI(troublesDTO);
    return list;
  }

  @PostMapping("/getListProblemGroupParent")
  public List<ProblemGroupDTO> getListProblemGroupParent(
      @RequestBody CfgServerNocDTO cfgServerNocDTO)
      throws Exception {
    List<ProblemGroupDTO> list = troublesBusiness.getListProblemGroupParent(cfgServerNocDTO);
    return list;
  }

  @PostMapping("/getListProblemGroupByParrenId/probGroupId{probGroupId}")
  public List<ProblemGroupDTO> getListProblemGroupByParrenId(@PathVariable Long probGroupId,
                                                             @RequestBody CfgServerNocDTO cfgServerNocDTO)
      throws Exception {
    List<ProblemGroupDTO> list = troublesBusiness
        .getListProblemGroupByParrenId(probGroupId, cfgServerNocDTO);
    return list;
  }

  @PostMapping("/getListPobTypeByGroupId/probGroupId{probGroupId}")
  public List<ProblemTypeDTO> getListPobTypeByGroupId(@PathVariable Long probGroupId,
                                                      @RequestBody CfgServerNocDTO cfgServerNocDTO)
      throws Exception {
    List<ProblemTypeDTO> list = troublesBusiness
        .getListPobTypeByGroupId(probGroupId, cfgServerNocDTO);
    return list;
  }

  //Dunglv3 start
  @GetMapping("/getListLocationCombobox")
  public ResponseEntity<List<ItemDataCRInside>> getListLocationCombobox(Long parentId) {
    List<ItemDataCRInside> list = troublesBusiness.getListLocationCombobox(parentId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/getTroubleActionLogDTOByTroubleId")
  public ResponseEntity<TroubleActionLogsDTO> getTroubleActionLogDTOByTroubleId(Long id) {
    TroubleActionLogsDTO troubleActionLogsDTO = troublesBusiness.getTroubleActionLogDTOByTroubleId(id);
    return new ResponseEntity<>(troubleActionLogsDTO, HttpStatus.OK);
  }

  //tab OD
  @GetMapping("/findListOdByTt")
  public ResponseEntity<List<OdSearchInsideDTO>> findListOdByTt(Long troubleId) {
    List<OdSearchInsideDTO> data = troublesBusiness.findListOdByTt(troubleId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  //tab RISK
  @GetMapping("/findListRiskByTt")
  public ResponseEntity<List<RiskDTO>> findListRiskByTt(Long troubleId) {
    List<RiskDTO> data = troublesBusiness.findListRiskByTt(troubleId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // THANGDT CHECK TT CO STATIONCODE
  @GetMapping("/checkStationCodeTTForWo")
  public ResponseEntity<WoInsideDTO> checkStationCodeTTForWo(Long id) {
    WoInsideDTO woInsideDTO = troublesBusiness.checkStationCodeTTForWo(id);
    return new ResponseEntity<>(woInsideDTO, HttpStatus.OK);
  }

  // Thangdt get danh sach dia ban theo like
  @GetMapping("/getListDistrictByLocationName")
  public ResponseEntity<List<DataItemDTO>> getListDistrictByLocationName(String name) {
    List<DataItemDTO> list = troublesBusiness.getListDistrictByLocationName(name);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  // Thangdt searchOdRelated
  @PostMapping("/searchOdRelated")
  public ResponseEntity<Datatable> searchOdRelated(@RequestBody OdSearchInsideDTO dto) {
    Datatable data = troublesBusiness.searchOdRelated(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // Thangdt insertOdCreatedFromOtherSystem
  @PostMapping("/insertOdCreatedFromOtherSystem")
  public ResponseEntity<ResultInSideDto> insertOdCreatedFromOtherSystem(
      @RequestBody TroublesInSideDTO troublesDTO) {
    troublesBusiness.insertOdCreatedFromOtherSystem(troublesDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  // Thangdt searchRiskRelated
  @PostMapping("/searchRiskRelated")
  public ResponseEntity<Datatable> searchRiskRelated(@RequestBody RiskDTO dto) {
    Datatable data = troublesBusiness.searchRiskRelated(dto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  // Thangdt insertRiskCreatedFromOtherSystem
  @PostMapping("/insertRiskCreatedFromOtherSystem")
  public ResponseEntity<ResultInSideDto> insertRiskCreatedFromOtherSystem(
      @RequestBody TroublesInSideDTO troublesDTO) {
    troublesBusiness.insertRiskCreatedFromOtherSystem(troublesDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    return new ResponseEntity<>(resultInSideDto, HttpStatus.OK);
  }

  @PostMapping("/getListReason")
  public ResponseEntity<List<CatReasonInSideDTOSearch>> getListReason(@RequestBody CatReasonInSideDTO reasonDto) {
    List<CatReasonInSideDTOSearch> data = catReasonBusiness.getListReasonSearchForWo(reasonDto);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

	@GetMapping("/getListUnitApproval")
  public ResponseEntity<List<CatItemDTO>> getListUnitApproval() {
    List<CatItemDTO> data = troublesBusiness.getListUnitApproval();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

}
