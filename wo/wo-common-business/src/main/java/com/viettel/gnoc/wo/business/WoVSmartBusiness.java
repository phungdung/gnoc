package com.viettel.gnoc.wo.business;

import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.wo.dto.CdInfoForm;
import com.viettel.gnoc.wo.dto.CfgWoTickHelpDTO;
import com.viettel.gnoc.wo.dto.CountWoForVSmartForm;
import com.viettel.gnoc.wo.dto.KpiCompleteVsmartResult;
import com.viettel.gnoc.wo.dto.MaterialThresDTO;
import com.viettel.gnoc.wo.dto.ObjFile;
import com.viettel.gnoc.wo.dto.ObjKeyValue;
import com.viettel.gnoc.wo.dto.SupportCaseForm;
import com.viettel.gnoc.wo.dto.VsmartUpdateForm;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoChecklistDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoHisForAccountDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionInsideDTO;
import com.viettel.gnoc.wo.dto.WoSalaryResponse;
import com.viettel.gnoc.wo.dto.WoTypeServiceDTO;
import com.viettel.gnoc.wo.dto.WoWorklogDTO;
import com.viettel.gnoc.wo.dto.WoWorklogInsideDTO;
import com.viettel.nocproV4.RequestInputBO;
import java.util.List;

public interface WoVSmartBusiness {

  List<ObjKeyValue> getDataTestService(Long woId);

  ResultDTO checkInfraForComplete(WoDTO createWoDto);

  List<WoCdGroupInsideDTO> getListCdByLocation(String locationCode);

  List<WoWorklogInsideDTO> getListWorklogByWoIdPaging(WoWorklogInsideDTO woWorklogInsideDTO);

  List<WoMaterialDeducteInsideDTO> getListMaterial(
      WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO);

  List<WoPostInspectionInsideDTO> getListExistedWoPostInspection(
      WoPostInspectionInsideDTO woPostInspectionDTO);

  List<SupportCaseForm> getLstSupportCase(Long woId);

  List<WoPostInspectionInsideDTO> onSearch(WoPostInspectionDTO woPostInspectionDTO, int startRow,
      int pageLength);

  Integer onSearchCount(WoPostInspectionDTO woPostInspectionDTO);

  List<CountWoForVSmartForm> getCountWoForVSmart(CountWoForVSmartForm countWoForVSmartForm);

  VsmartUpdateForm getScriptId(Long woId);

  ResultDTO checkUpdateSupportWO(Long woId);

  ResultDTO deleteListWoPostInspection(List<WoPostInspectionDTO> woPostInspectionListDTO);

  WoSalaryResponse countWOByFT(WoSalaryResponse woSalaryResponse);

  List<CdInfoForm> getListCdInfo(CdInfoForm cdInfoForm);

  String getSequenseWo(String seq);

  List<String> getListSequenseWo(String seq, int size);

  ResultDTO updateWOPostInspectionFromVsmart(WoPostInspectionDTO woPostInspectionListDTO);

  ResultDTO updateWOPostInspection(WoPostInspectionDTO inspectionDTO);

  ResultDTO checkDeviceCodeOfWo(WoInsideDTO woInsideDTO);

  Integer getCountWOByUsers(WoDTOSearch woDTOSearch);

  ResultDTO updatePendingWo(WoInsideDTO woInsideDTO, Boolean callCC);

  ResultDTO aprovePXK(Long woId, Long status, String reason, Long isDestroy);

  ResultDTO confirmNotCreateAlarm(RequestInputBO inputBO, Long woId) throws Exception;

  List<ObjKeyValueVsmartDTO> getDataCfgWoHelp(Long systemId, String typeId);

  ResultDTO acceptWo(String username, Long woId, String comment, Boolean isFt);

  WoDTO findWoById(Long woId);

  String insertWOPostInspectionFromVsmart(List<WoPostInspectionDTO> lstInspectionDTO,
      List<ObjKeyValue> lstObjKeyValue);

  ResultDTO cancelReqBccs(String woCode, String content);

  ResultDTO updateMopInfo(String woCode, String result, String mopId, Long type);

  List<WoPostInspectionDTO> getListWOPostInspection(WoPostInspectionDTO inspectionDTO,
      int rowStart,
      int maxRow, String sortType, String sortFieldList);

  ResultDTO dispatchWo(String username, String ftName, String woId, String comment);

  List<CompCause> getListCompCause3Level(String ccResult);

  List<ResultDTO> createListWo(List<WoDTO> createWoDto);

  ResultDTO createWo(WoDTO createWoDto);

  ResultDTO approveWoVsmart(VsmartUpdateForm updateForm, String username, String woId,
      String comment, String action, String ftName) throws Exception;

  ResultDTO insertWoKTTS(WoDTO woDTO);

  List<Users> getListUserByUnitCode(String unitCode, String allOfChildUnit);

  UsersDTO getUserInfo(String userName, String staffCode);

  Users getUserModelInfo(String userName, String staffCode);

  List<WoMerchandiseDTO> getListWoMerchandise(String woId);

  List<WoWorklogDTO> getListWorklogByWoId(String woId);

  KpiCompleteVsmartResult getKpiComplete(String startTime, String endTime, List<String> lstUser);

  List<WoChecklistDTO> getListWoChecklistDetailByWoId(String woId);

  List<ObjFile> getFileFromWo(String woId, List<String> lstFileName);

  ResultDTO insertCfgWoTickHelp(CfgWoTickHelpDTO cfgWoTickHelpDTO);

  String insertWOPostInspection(List<WoPostInspectionDTO> lstInspectionDTO);

  List<ObjKeyValue> loadWoPostInspectionChecklist(String woId, String accountName);

  List<UsersDTO> getListFtByUser(String userId, String keyword, int rowStart,
      int maxRow);

  ResultDTO rejectWo(String username, String woId, String comment, Boolean isFt);

  String deleteWoPost(Long id);

  ResultDTO updateStatus(VsmartUpdateForm updateForm, String username, String woId, String status,
      String comment,
      String ccResult, String qrCode, List<WoMaterialDeducteDTO> listMaterial,
      Long reasonIdLv1, Long reasonIdLv2, Long actionKTTS,
      List<WoMerchandiseDTO> lstMerchandise, String reasonKtts, String handoverUser,
      String sessionId, String ipPortParentNode,
      List<String> listFileName, List<byte[]> fileArr);

  List<ResultDTO> getWOSummaryInfobyUser(String userId, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo);

  List<WoDTO> getListWOAndAccount(String username, String fromDate, String toDate, String woCode,
      String cdId, String accountIsdn);

  ResultDTO insertWoWorklog(WoWorklogDTO woWorklogDTO);

  ResultDTO checkRequiredStation(String woCode);

  ResultDTO pendingWoForVsmart(VsmartUpdateForm updateForm, String woCode, String endPendingTime,
      String user, String system, String reasonName, String reasonId, String customer,
      String phone) throws Exception;
//
//  List<MaterialThresDTO> getListMaterialDTOByAction(Long actionId, Long serviceId, Long infraType,
//      boolean isEnable, String nationCode);

  WoTypeServiceDTO getIsCheckQrCode(Long woId);

  Integer getCountListFtByUser(String userName, String keyword);

  List<CompCause> getListReasonOverdue(Long parentId, String nationCode);

  ResultDTO actionUpdateIsSupportWO(VsmartUpdateForm updateForm, String woIdStr, String needSupport,
      String userName, String content) throws Exception;

  List<WoDTOSearch> getListWOByUsers(String userName, String summaryStatus, Integer isDetail,
      WoDTOSearch woDTO, int start, int count, int typeSearch);

  ResultDTO updateCfgWoTickHelpVsmart(CfgWoTickHelpDTO cfgWoTickHelpDTO);

  ResultDTO updateTechnicalClues(String woCode, String technicalClues);

  List<WoCdGroupDTO> getListCdGroup(String userName);

  String insertListWoChecklistDetail(List<WoChecklistDTO> lstChecklist);

  List<WoWorklogInsideDTO> getListDataByWoIdPaging(String woId, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  List<WoCdGroupInsideDTO> getListCdGroupByDTO(WoCdGroupInsideDTO woCdGroupDTO);

  List<ObjKeyValue> getObjKeyValueFromFile(Long woId, Long type);

  public ResultDTO startWork(WoDTO woDto);

  public List<ResultDTO> getWOSummaryInfobyType(String username, int typeSearch, Long cdId,
      WoDTOSearch woDTOInput);

  public List<WoHisForAccountDTO> getListWoHisForAccount(WoDTOSearch woDTOSearch, String locale);

}
