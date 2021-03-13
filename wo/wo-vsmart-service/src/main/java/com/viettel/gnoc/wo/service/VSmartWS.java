package com.viettel.gnoc.wo.service;

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
import com.viettel.gnoc.wo.dto.WoChecklistDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoHisForAccountDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionDTO;
import com.viettel.gnoc.wo.dto.WoSalaryResponse;
import com.viettel.gnoc.wo.dto.WoTypeServiceDTO;
import com.viettel.gnoc.wo.dto.WoWorklogDTO;
import com.viettel.nocproV4.RequestInputBO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(serviceName = "VSmartWS")
public interface VSmartWS {

  @WebMethod(operationName = "findWoById")
  public WoDTO findWoById(@WebParam(name = "woDTOId") Long id);

  @WebMethod(operationName = "insertWOPostInspectionFromVsmart")
  public String insertWOPostInspectionFromVsmart(
      @WebParam(name = "lstInspectionDTO") List<WoPostInspectionDTO> lstInspectionDTO,
      @WebParam(name = "lstObjKeyValue") List<ObjKeyValue> lstObjKeyValue);

  @WebMethod(operationName = "cancelReqBccs")
  public ResultDTO cancelReqBccs(@WebParam(name = "woSystemId") String woSystemId,
      @WebParam(name = "content") String content);

  @WebMethod(operationName = "updateMopInfo")
  public ResultDTO updateMopInfo(@WebParam(name = "woCode") String woCode,
      @WebParam(name = "result") String result,
      @WebParam(name = "mopId") String mopId, @WebParam(name = "type") Long type);

  @WebMethod(operationName = "getListWOPostInspection")
  public List<WoPostInspectionDTO> getListWOPostInspection(
      @WebParam(name = "inspectionDTO") WoPostInspectionDTO inspectionDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList
  );

  @WebMethod(operationName = "dispatchWo")
  public ResultDTO dispatchWo(@WebParam(name = "username") String username,
      @WebParam(name = "ftname") String ftName, @WebParam(name = "woId") String woId,
      @WebParam(name = "comment") String comment);

  @WebMethod(operationName = "getListCompCause3Level")
  public List<CompCause> getListCompCause3Level(@WebParam(name = "ccResult") String ccResult);

  @WebMethod(operationName = "getListDataByWoId")
  public List<WoWorklogDTO> getListWorklogByWoId(@WebParam(name = "woId") String woId);

  @WebMethod(operationName = "createListWo")
  public List<ResultDTO> createListWo(@WebParam(name = "createWoDto") List<WoDTO> createWoDto);

  @WebMethod(operationName = "updateWOPostInspection")
  public ResultDTO updateWOPostInspection(
      @WebParam(name = "inspectionDTO") WoPostInspectionDTO inspectionDTO,
      @WebParam(name = "username") String username);

  @WebMethod(operationName = "createWo")
  public ResultDTO createWo(@WebParam(name = "createWoDto") WoDTO createWoDto);

  @WebMethod(operationName = "approveWoVsmart")
  public ResultDTO approveWoVsmart(@WebParam(name = "updateForm") VsmartUpdateForm updateForm,
      @WebParam(name = "username") String username, @WebParam(name = "woId") String woId,
      @WebParam(name = "comment") String comment, @WebParam(name = "action") String action,
      @WebParam(name = "ftName") String ftName);

  @WebMethod(operationName = "insertWoKTTS")
  public ResultDTO insertWoKTTS(@WebParam(name = "woDTO") WoDTO woDTO);

  @WebMethod(operationName = "getListUserByUnitCode")
  public List<Users> getListUserByUnitCode(@WebParam(name = "unitCode") String unitCode,
      @WebParam(name = "allOfChildUnit") String allOfChildUnit);

  @WebMethod(operationName = "getListWoMerchandise")
  public List<WoMerchandiseDTO> getListWoMerchandise(@WebParam(name = "woId") String woId);

  @WebMethod
  public KpiCompleteVsmartResult getKpiComplete(@WebParam(name = "startTime") String startTime,
      @WebParam(name = "endTime") String endTime,
      @WebParam(name = "lstUser") List<String> lstUser
  );

  @WebMethod(operationName = "getListWoChecklistDetailByWoId")
  public List<WoChecklistDTO> getListWoChecklistDetailByWoId(@WebParam(name = "woId") String woId);

  @WebMethod(operationName = "getUserInfo")
  public Users getUserInfo(@WebParam(name = "userName") String userName,
      @WebParam(name = "staffCode") String staffCode);

  @WebMethod(operationName = "getFileFromWo")
  public List<ObjFile> getFileFromWo(@WebParam(name = "woId") String woId,
      @WebParam(name = "lstFileName") List<String> lstFileName);

  @WebMethod(operationName = "insertCfgWoTickHelp")
  public ResultDTO insertCfgWoTickHelp(
      @WebParam(name = "cfgWoTickHelpDTO") CfgWoTickHelpDTO cfgWoTickHelpDTO);


  @WebMethod(operationName = "insertWOPostInspection")
  public String insertWOPostInspection(
      @WebParam(name = "lstInspectionDTO") List<WoPostInspectionDTO> lstInspectionDTO);

  @WebMethod(operationName = "loadWoPostInspectionChecklist")
  public List<ObjKeyValue> loadWoPostInspectionChecklist(@WebParam(name = "woId") String woId,
      @WebParam(name = "accountName") String accountName);

  @WebMethod
  public List<UsersDTO> getListFtByUser(@WebParam(name = "userId") String userId
      , @WebParam(name = "keyword") String keyword//
      , @WebParam(name = "rowStart") int rowStart//
      , @WebParam(name = "maxRow") int maxRow);

  @WebMethod(operationName = "rejectWo")
  public ResultDTO rejectWo(@WebParam(name = "username") String string,
      @WebParam(name = "woId") String string1,
      @WebParam(name = "comment") String string2,
      @WebParam(name = "isFt") Boolean isFt);

  @WebMethod(operationName = "deleteWOPostInspection")
  public String deleteWoPostInspection(@WebParam(name = "woPostInspectionDTOId") Long id);

  @WebMethod(operationName = "updateStatus")
  public ResultDTO updateStatus(
      @WebParam(name = "updateForm") VsmartUpdateForm updateForm,
      @WebParam(name = "username") String username,
      @WebParam(name = "woId") String woId,
      @WebParam(name = "status") String status,
      @WebParam(name = "comment") String comment,
      @WebParam(name = "ccResult") String ccResult,
      @WebParam(name = "qrCode") String qrCode,
      @WebParam(name = "listMaterial") List<WoMaterialDeducteDTO> listMaterial,
      @WebParam(name = "reasonIdLv1") Long reasonIdLv1,
      @WebParam(name = "reasonIdLv2") Long reasonIdLv2,
      @WebParam(name = "actionKTTS") Long actionKTTS,
      @WebParam(name = "lstSerialKTTS") List<WoMerchandiseDTO> lstMerchandise,
      @WebParam(name = "reasonKtts") String reasonKtts,
      @WebParam(name = "handoverUser") String handoverUser,
      @WebParam(name = "listFileName") List<String> listFileName,
      @WebParam(name = "fileArr") List<byte[]> fileArr//
  );

  @WebMethod(operationName = "getWOSummaryInfobyUser")
  public List<ResultDTO> getWOSummaryInfobyUser(
      @WebParam(name = "userId") String string,
      @WebParam(name = "typeSearch") int typeSearch
      , @WebParam(name = "cdId") Long cdId,
      @WebParam(name = "createTimeFrom") String createTimeFrom,
      @WebParam(name = "createTimeTo") String createTimeTo) throws Exception;

  @WebMethod(operationName = "checkDeviceCodeOfWo")
  public ResultDTO checkDeviceCodeOfWo(@WebParam(name = "woCode") String woCode,
      @WebParam(name = "deviceCode") String deviceCode);

  @WebMethod(operationName = "confirmNotCreateAlarm")
  public ResultDTO confirmNotCreateAlarm(@WebParam(name = "inputBO") RequestInputBO inputBO,
      @WebParam(name = "woId") Long woId);

  @WebMethod
  public ResultDTO updatePendingWo(@WebParam(name = "woCode") String woCode,
      @WebParam(name = "endPendingTime") String endPendingTime,
      @WebParam(name = "user") String user, @WebParam(name = "comment") String comment,
      @WebParam(name = "system") String system);

  @WebMethod(operationName = "updateWOPostInspectionFromVsmart")
  public ResultDTO updateWOPostInspectionFromVsmart(
      @WebParam(name = "inspectionDTO") WoPostInspectionDTO inspectionDTO,
      @WebParam(name = "username") String username,
      @WebParam(name = "lstObjKeyValue") List<ObjKeyValue> lstObjKeyValue);

  @WebMethod(operationName = "getSequenseWo")
  public List<String> getSequenseWo(@WebParam(name = "sequenseName") String string,
      @WebParam(name = "Size") int... size);

  @WebResult(name = "woDTOSearch", targetNamespace = "http://wfm.viettel.vn")
  public Integer getCountWOByUsers(@WebParam(name = "userId") String userId,
      @WebParam(name = "summaryStatus") String summaryStatus,
      @WebParam(name = "woDTO") WoDTOSearch wdtos, @WebParam(name = "isDetail") Integer isDetail,
      @WebParam(name = "typeSearch") int typeSearch);

  @WebMethod(operationName = "getListCdInfo")
  public List<CdInfoForm> getListCdInfo(@WebParam(name = "lstWoCode") List<String> lstWoCode,
      @WebParam(name = "lstWoId") List<Long> lstWoId);

  @WebMethod(operationName = "countWOByFT")
  public WoSalaryResponse countWOByFT(@WebParam(name = "username") List<String> username,
      @WebParam(name = "startTime") String startTime, @WebParam(name = "endTime") String endTime);

  @WebMethod(operationName = "deleteListWoPostInspection")
  public String deleteListWoPostInspection(
      @WebParam(name = "woPostInspectionListDTO") List<WoPostInspectionDTO> woPostInspectionListDTO);

  @WebMethod(operationName = "onSearchCount")
  public int onSearchCount(
      @WebParam(name = "woPostInspectionDTO") WoPostInspectionDTO inspectionDTO);

  @WebMethod(operationName = "checkUpdateSupportWO")
  public ResultDTO checkUpdateSupportWO(@WebParam(name = "woId") String woId);

  @WebMethod(operationName = "getDataCfgWoHelp")
  public List<ObjKeyValueVsmartDTO> getDataCfgWoHelp(@WebParam(name = "systemId") Long systemId,
      @WebParam(name = "typeId") String typeId);

  @WebMethod(operationName = "getScriptId")
  public VsmartUpdateForm getScriptId(@WebParam(name = "woId") String woId);

  @WebMethod(operationName = "checkInfraForComplete")
  public ResultDTO checkInfraForComplete(@WebParam(name = "woDTO") WoDTO woDTO);

  @WebMethod(operationName = "acceptWo")
  public ResultDTO acceptWo(@WebParam(name = "username") String username,
      @WebParam(name = "woId") String woId, @WebParam(name = "comment") String comment,
      @WebParam(name = "isFt") Boolean isFt);

  @WebMethod(operationName = "getCountWoForVSmart")
  public List<CountWoForVSmartForm> getCountWoForVSmart(
      @WebParam(name = "userName") String userName,
      @WebParam(name = "summaryStatus") String summaryStatus);

  @WebMethod(operationName = "onSearch")
  public List<WoPostInspectionDTO> onSearch(
      @WebParam(name = "woPostInspectionDTO") WoPostInspectionDTO inspectionDTO,
      @WebParam(name = "startRow") int startRow, @WebParam(name = "pageLength") int pageLength);

  @WebMethod(operationName = "insertListWoChecklistDetail")
  public String insertListWoChecklistDetail(
      @WebParam(name = "lstChecklist") List<WoChecklistDTO> lstChecklist);

  @WebMethod(operationName = "getLstSupportCase")
  public List<SupportCaseForm> getLstSupportCase(@WebParam(name = "woId") String woId);

  @WebMethod(operationName = "getListExistedWoPostInspection")
  public List<WoPostInspectionDTO> getListExistedWoPostInspection(
      @WebParam(name = "locationCode") String locationCode,
      @WebParam(name = "woType") String woType);

  @WebMethod(operationName = "getListMaterial")
  public List<WoMaterialDeducteDTO> getListMaterial(@WebParam(name = "woId") Long woId,
      @WebParam(name = "userName") String userName);

  @WebMethod(operationName = "getListDataByWoIdPaging")
  public List<WoWorklogDTO> getListWorklogByWoIdPaging(
      @WebParam(name = "woId") String woId,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  @WebMethod(operationName = "aprovePXK")
  public ResultDTO aprovePXK(@WebParam(name = "woId") Long woId,
      @WebParam(name = "status") Long status, @WebParam(name = "reason") String reason);

  @WebMethod(operationName = "getListCdByLocation")
  public List<WoCdGroupDTO> getListCdByLocation(
      @WebParam(name = "locationCode") String locationCode);

  @WebMethod(operationName = "getListWOAndAccount")
  public List<WoDTO> getListWOAndAccount(@WebParam(name = "username") String username,
      @WebParam(name = "fromDate") String fromDate, @WebParam(name = "toDate") String toDate,
      @WebParam(name = "woCode") String woCode, @WebParam(name = "cdId") String cdId,
      @WebParam(name = "accountIsdn") String accountIsdn);

  @WebMethod(operationName = "insertWoWorklog")
  public ResultDTO insertWoWorklog(@WebParam(name = "woHistoryDTO") WoWorklogDTO woWorklogDTO);

  @WebMethod(operationName = "checkRequiredStation")
  public ResultDTO checkRequiredStation(@WebParam(name = "woCode") String woCode);

  @WebMethod(operationName = "pendingWoForVsmart")
  public ResultDTO pendingWoForVsmart(@WebParam(name = "updateForm") VsmartUpdateForm updateForm,
      @WebParam(name = "woCode") String woCode,
      @WebParam(name = "endPendingTime") String endPendingTime,
      @WebParam(name = "user") String user,
      @WebParam(name = "system") String system,
      @WebParam(name = "reasonName") String reasonName,
      @WebParam(name = "reasonId") String reasonId,
      @WebParam(name = "customer") String customer,
      @WebParam(name = "phone") String phone) throws Exception;

  @WebMethod(operationName = "getListMaterialDTOByAction")
  public List<MaterialThresDTO> getListMaterialDTOByAction(
      @WebParam(name = "actionId") Long actionId//
      , @WebParam(name = "serviceId") Long serviceId//
      , @WebParam(name = "infraType") Long infraType//
      , @WebParam(name = "isEnable") boolean isEnable);

  @WebMethod(operationName = "getIsCheckQrCode")
  public WoTypeServiceDTO getIsCheckQrCode(@WebParam(name = "woId") Long woId);

  @WebMethod(operationName = "getCountListFtByUser")
  public Integer getCountListFtByUser(@WebParam(name = "userId") String userId
      , @WebParam(name = "keyword") String keyword);

  @WebMethod(operationName = "getListReasonOverdue")
  public List<CompCause> getListReasonOverdue(@WebParam(name = "parentId") Long parentId);

  @WebMethod(operationName = "actionUpdateIsSupportWO")
  public ResultDTO actionUpdateIsSupportWO(
      @WebParam(name = "updateForm") VsmartUpdateForm updateForm,
      @WebParam(name = "woIdStr") String woIdStr,
      @WebParam(name = "needSupport") String needSupport,
      @WebParam(name = "userName") String userName,
      @WebParam(name = "content") String content);

  @WebMethod(operationName = "getListWOByUsers")
  @WebResult(name = "woDTOSearch")
  public List<WoDTOSearch> getListWOByUsers(@WebParam(name = "userId") String userId,
      @WebParam(name = "summaryStatus") String summaryStatus,
      @WebParam(name = "isDetail") Integer isDetail,
      @WebParam(name = "woDTO") WoDTOSearch wdtos,
      @WebParam(name = "start") int start,
      @WebParam(name = "count") int count,
      @WebParam(name = "typeSearch") int typeSearch);

  @WebMethod(operationName = "updateCfgWoTickHelpVsmart")
  public ResultDTO updateCfgWoTickHelpVsmart(
      @WebParam(name = "cfgWoTickHelpDTO") CfgWoTickHelpDTO cfgWoTickHelpDTO);

  @WebMethod(operationName = "getDataTestService")
  public List<ObjKeyValue> getDataTestService(@WebParam(name = "woId") Long woId);

  @WebMethod(operationName = "updateTechnicalClues")
  public ResultDTO updateTechnicalClues(@WebParam(name = "woSystemId") String woSystemId,
      @WebParam(name = "technicalClues") String technicalClues);

  @WebMethod(operationName = "getListCdGroup")
  public List<WoCdGroupDTO> getListCdGroup(@WebParam(name = "userName") String userName);

  @WebMethod(operationName = "getObjKeyValueFromFile")
  public List<ObjKeyValue> getObjKeyValueFromFile(@WebParam(name = "woId") Long woId,
      @WebParam(name = "type") Long type);

  @WebMethod(operationName = "getListCdGroupByDTO")
  public List<WoCdGroupDTO> getListCdGroupByDTO(
      @WebParam(name = "woCdGroupDTO") WoCdGroupDTO woCdGroupDTO);

  @WebMethod(operationName = "getWOSummaryInfobyUser2")
  public List<ResultDTO> getWOSummaryInfobyUser2(
      @WebParam(name = "userId", targetNamespace = "") String string,
      @WebParam(name = "typeSearch", targetNamespace = "") int typeSearch,
      @WebParam(name = "cdId", targetNamespace = "") Long cdId,
      @WebParam(name = "createTimeFrom", targetNamespace = "") String createTimeFrom,
      @WebParam(name = "createTimeTo", targetNamespace = "") String createTimeTo) throws Exception;

  // ThanhPT18: Get list WO
  @WebMethod(operationName = "getListDataSearch")
  public List<WoDTOSearch> getListDataSearch(
      @WebParam(name = "woDTOSearch") WoDTOSearch woDTOSearch,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);


  @WebMethod(operationName = "getListGoodsDTOByAction")
  public List<MaterialThresDTO> getListGoodsDTOByAction(
      @WebParam(name = "actionId") Long actionId//
      , @WebParam(name = "serviceId") Long serviceId//
      , @WebParam(name = "infraType") Long infraType//
      , @WebParam(name = "isEnable") boolean isEnable);

  // thuc hien luu startWorkTime
  @WebMethod(operationName = "startWork")
  public ResultDTO startWork(@WebParam(name = "woDto") WoDTO woDto);

  @WebMethod(operationName = "getWOSummaryInfobyType")
  public List<ResultDTO> getWOSummaryInfobyType(
      @WebParam(name = "userId", targetNamespace = "") String string,
      @WebParam(name = "typeSearch", targetNamespace = "") int typeSearch
      , @WebParam(name = "cdId", targetNamespace = "") Long cdId,
      @WebParam(name = "woDTOInput", targetNamespace = "") WoDTOSearch woDTOInput);

  @WebMethod(operationName = "getListWoHisForAccount")
  public List<WoHisForAccountDTO> getListWoHisForAccount(
      @WebParam(name = "woDTOSearch") WoDTOSearch woDTOSearch);
}
