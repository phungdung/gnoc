package com.viettel.gnoc.wo.business;

import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
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
import org.springframework.web.multipart.MultipartFile;

public interface WoBusiness {

  Datatable getListDataSearchWeb(WoInsideDTO woInsideDTO);

  WoInsideDTO findWoByIdFromWeb(Long woId);

  ResultInSideDto insertWoFromWeb(List<MultipartFile> fileAttacks,
      List<MultipartFile> fileCfgAttacks, WoInsideDTO woInsideDTO) throws Exception;

  ResultInSideDto insertWoFromWebInMrMNGT(WoInsideDTO woInsideDTO) throws Exception;

  ResultInSideDto deleteListWo(List<WoInsideDTO> listWoInsideDTO) throws Exception;

  ResultInSideDto updateWoFromWeb(List<MultipartFile> fileAttacks,
      List<MultipartFile> fileCfgAttacks,
      WoInsideDTO woInsideDTO) throws Exception;

  List<CatItemDTO> getListWoSystemInsertWeb();

  List<GnocFileDto> getListFileFromWo(Long woId);

  List<CatStationBO> getStationListNation(String stationCode, String date);

  List<CatWarehouseBO> getListWarehouseNation(String warehouseCode, String warehouseName,
      String woType, String staffCode);

  List<CntContractBO> getListContractFromConstrNation(String constrtCode);

  List<ConstrConstructionsBO> getConstructionListNation(String stationCode);

  List<CatItemDTO> getListWoKttsAction(String key);

  File exportDataWo(WoInsideDTO woInsideDTO) throws Exception;

  File getTemplateImportWo() throws Exception;
  // nang cap wo tai san (start)
  File getTemplateImportAssetsForWo() throws Exception;

  File getTemplateImportWoTranSferOfProperty() throws Exception;

  File getTemplateImportWoDowngradeWithDrawal() throws Exception;

  File getTemplateImportWoUpgradeStation() throws Exception;
  // nang cap wo tai san (end)
  Datatable getListWoHistoryByWoId(WoHistoryInsideDTO woHistoryInsideDTO);

  Datatable getListWorklogByWoIdPaging(WoWorklogInsideDTO woWorklogInsideDTO);

  ResultInSideDto insertWoWorklog(WoInsideDTO woInsideDTO);

  Datatable loadTroubleCrDTO(WoInsideDTO woInsideDTO);

  ResultInSideDto updateFileAttack(List<MultipartFile> fileAttacks, WoInsideDTO woInsideDTO)
      throws IOException;

  File getTemplateExportWOTestServiceFromCR() throws Exception;

  File getTemplateExportWOTestServiceFromWO() throws Exception;

  ResultInSideDto acceptWoFromWeb(WoInsideDTO woInsideDTO) throws Exception;

  ResultInSideDto acceptWoCommon(String username, Long woId, String comment, String role)
      throws Exception;

  ResultInSideDto dispatchWoFromWeb(WoInsideDTO woInsideDTO) throws Exception;

  ResultInSideDto dispatchWo(String username, String ftName, Long woId, String comment)
      throws Exception;

  ResultInSideDto rejectWoFromWeb(WoInsideDTO woInsideDTO) throws Exception;

  ResultInSideDto rejectWo(String username, Long woId, String comment, String role)
      throws Exception;

  ResultInSideDto auditWoFromWeb(WoInsideDTO woInsideDTO) throws Exception;

  ResultInSideDto pendingWoFromWeb(WoInsideDTO woInsideDTO) throws Exception;

  ResultInSideDto updateStatusFromWeb(WoInsideDTO woInsideDTO);

  ResultInSideDto splitWoFromWeb(List<MultipartFile> fileAttacks, WoInsideDTO woInsideDTO)
      throws IOException;

  ResultInSideDto updatePendingWoFromWeb(WoInsideDTO woInsideDTO) throws Exception;

  ResultInSideDto approveWoFromWeb(WoInsideDTO woInsideDTO) throws Exception;

  ResultInSideDto callIPCC(WoInsideDTO woInsideDTO);

  Datatable getListLogCallIpccDTO(LogCallIpccDTO logCallIpccDTO);

  ResultInSideDto exportDataWoFromListCr(MultipartFile fileImport, Date startTimeFrom,
      Date startTimeTo);

  ResultInSideDto exportFileTestService(MultipartFile fileImport);

  List<WoPriorityDTO> getWoPriorityByWoTypeID(Long woTypeId);

  ResultInSideDto completeWoSPM(WoInsideDTO woInsideDTO);

  Datatable getListWoChild(WoInsideDTO woInsideDTO);

  List<WoInsideDTO> getListWoByWoTypeId(Long woTypeId);

  //outsite
  List<WoDTO> getListWoDTO(WoDTO woDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  List<WoInsideDTO> getListWoDTOByWoSystemId(String woSystemId);

  ResultDTO getResultCallIPCC(String userName, String passWord, String input);

  ResultInSideDto insertWoCommon(WoDTO woDTO) throws Exception;

  ResultInSideDto updatePendingWoCommon(String woCode, Date endPendingTime, String user,
      String comment, String system, boolean callCC) throws Exception;

  ResultInSideDto approveWoCommon(VsmartUpdateForm updateForm, String username, Long woId,
      String comment, String action, String ftName) throws Exception;

  //lay danh sach file tu wo
  List<ObjFile> getFileFromWo(Long woId, List<String> lstFileName);

  ResultInSideDto createWoCommon(WoDTO createWoDto) throws Exception;

  ResultDTO aprovePXK(Long woId, Long status, String reason, Long isDestroy);

  ResultDTO insertWo(WoDTO woDTO) throws Exception;

  ResultDTO updateWoInfo(WoDTO woDTO) throws Exception;

  List<SearchWoKpiCDBRForm> searchWoKpiCDBR(String startTime, String endTime);

  ResultDTO createWoTKTU(WoDTO createWoDto);

  List<String> getSequenseWo(String seqName, int... size);

  ResultDTO updatePendingWo(String woCode, String endPendingTime, String user,
      String comment, String system, boolean callCC) throws Exception;

  ResultDTO updateWoForSPM(WoDTO woDTO);

  ResultDTO changeStatusWo(WoUpdateStatusForm updateForm);

  ResultDTO createWoFollowNode(WoDTO createWoDto, List<String> listNode);

  String getNationFromUnitId(Long unitId);

  ObjResponse getWoDetail(String woId, String userId);

  List<CompCause> getListReasonOverdue(Long parentId, String nationCode);

  List<WoDTOSearch> getListWOByUsers(String userId, String summaryStatus, Integer isDetail,
      WoDTOSearch woDTO, int start, int count, int typeSearch);

  ResultDTO pendingWo(String woCode, String endPendingTime, String user, String system,
      String reasonName, String reasonId, String customer, String phone) throws Exception;

  ResultDTO createWoVsmart(WoDTO createWoDto);

  String checkProblemSignalForAccount(String subscriptionAccount, String workId, String sysId);

  ResultDTO cancelReqBccs(String woCode, String content);

  ResultDTO updateFileForWo(WoDTO woDTO);

  ResultDTO approveWo(VsmartUpdateForm updateForm, String username, String woId, String comment,
      String action, String ftName, String sessionId, String ipPortParentNode)
      throws Exception;

  ResultDTO insertWoWorklog(WoWorklogDTO woWorklogDTO);

  List<ResultDTO> getWOSummaryInfobyUserCommon(String userId, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo);

  ResultInSideDto updateStatusCommon(VsmartUpdateForm updateForm, String username,
      String woId, String status, String comment, String ccResult, String qrCode,
      List<WoMaterialDeducteInsideDTO> listMaterial, Long reasonIdLv1, Long reasonIdLv2,
      Long actionKTTS,
      List<WoMerchandiseInsideDTO> lstMerchandise, String reasonKtts, String handoverUser,
      String sessionId, String ipPortParentNode,
      List<String> listFileName, List<byte[]> fileArr)
      throws Exception;

  ResultDTO updateMopInfo(String woCode, String result, String mopId, Long type);

  ResultDTO acceptWo(String username, String woId, String comment) throws Exception;

  List<ResultDTO> createListWoVsmart(List<WoDTO> createWoDto);

  ResultDTO insertWoKTTS(WoDTO woDTO);

  KpiCompleteVsmartResult getKpiComplete(String startTime, String endTime, List<String> lstUser);

  List<WoChecklistDTO> getListWoChecklistDetailByWoId(String woId);

  ResultDTO createWo(WoDTO createWoDto) throws Exception;

  List<UsersDTO> getListCd(Long cdGroupId);

  ResultDTO updateStatus(String username, String woId, String status, String comment)
      throws Exception;

  WoInsideDTO findWoByIdNoOffset(Long woId);

  ResultInSideDto updateTableWo(WoInsideDTO woInsideDTO);

  List<ResultDTO> getWOSummaryInfobyUser(String userId);

  ResultDTO pendingWoForVsmart(VsmartUpdateForm updateForm, String woCode, String endPendingTime,
      String user, String system, String reasonName, String reasonId, String customer,
      String phone) throws Exception;

  WoTypeServiceInsideDTO getIsCheckQrCode(Long woId);

  Integer getCountListFtByUser(String userName, String keyword);

  ResultDTO actionUpdateIsSupportWO(VsmartUpdateForm updateForm, String woIdStr, String needSupport,
      String userName, String content) throws Exception;

  ResultDTO createWoForOtherSystem(WoDTO woDTO) throws Exception;

  ResultDTO callIPCCWithName(String woId, Long userId, String userCall, String fileAudioName);

  String getConfigProperty();

  ResultDTO insertWoForSPM(WoDTO woDTO) throws Exception;

  ResultDTO deleteWOForRollback(String woCode, String reason, String system);

  String updateWo(WoDTO woDTO) throws ParseException, IOException;

  WoDTO findWoById(Long id);

  ResultDTO closeWo(List<String> listCode, String system);

  List<WoDTOSearch> getListDataSearch(WoDTOSearch searchDto);

  List<ResultDTO> getWOStatistic(Long unitId, int isSend, int isSearchChild, String fromDate,
      String toDate);

  Integer getWOTotal(WoDTOSearch searchDto);

  ResultInSideDto deleteWo(Long woId);

  List<UsersDTO> getListFtByUser(String userName, String keyword, int rowStart, int maxRow);

  List<WoInsideDTO> getListDataForRisk(WoInsideDTO woInsideDTO);

  List<ResultDTO> getWOSummaryInfobyUser2(String username, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo);

  ResultInSideDto runMopOnSAP(WoInsideDTO woInsideDTO);

  ResultDTO updateResultTestFromSAP(WoDTO woDto) throws Exception;

  ResultInSideDto importData(MultipartFile upLoadFile, List<MultipartFile> lstAttachments)
      throws Exception;

  // nang cap wo import tai san
  ResultInSideDto importDataAssets(MultipartFile upLoadFile, List<MultipartFile> lstAttachments, String formDataJson)
      throws Exception;

  WoSearchWebDTO getWoSearchWebDTOByWoCode(String code);

  List<WoDTO> getListWoByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<WoMerchandiseInsideDTO> getListWoMerchandiseDTO(
      WoMerchandiseInsideDTO woMerchandiseInsideDTO);

  WoInsideDTO findWoByWoCodeNoOffset(String woCode);

  List<MaterialThresDTO> getListMaterialByWoId(Long woId);

  Datatable getListConfigAutoCreateWoOs(AutoCreateWoOsDTO autoCreateWoOsDTO);

  ResultInSideDto insertOrUpdateAutoCreateWoOs(AutoCreateWoOsDTO autoCreateWoOsDTO);

  AutoCreateWoOsDTO getConfigById(Long id);

  ResultInSideDto delete(Long id);

  ResultInSideDto syncFileFromWeb(Long woId);
}
