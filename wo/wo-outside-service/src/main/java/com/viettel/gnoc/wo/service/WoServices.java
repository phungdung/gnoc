package com.viettel.gnoc.wo.service;

import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.wo.dto.AmiOneForm;
import com.viettel.gnoc.wo.dto.CfgSupportForm;
import com.viettel.gnoc.wo.dto.ObjFile;
import com.viettel.gnoc.wo.dto.ObjResponse;
import com.viettel.gnoc.wo.dto.SearchWoKpiCDBRForm;
import com.viettel.gnoc.wo.dto.Wo;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import com.viettel.gnoc.wo.dto.WoWorklogDTO;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "WoServices")
public interface WoServices {

  @WebMethod(operationName = "getListWoDTO")
  public List<WoDTO> getListWoDTO(@WebParam(name = "woDTO") WoDTO woDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  @WebMethod(operationName = "getResultCallIPCC")
  public ResultDTO getResultCallIPCC(@WebParam(name = "userName") String userName,
      @WebParam(name = "passWord") String passWord, @WebParam(name = "input") String input);

  @WebMethod(operationName = "checkInfraForComplete")
  public ResultDTO checkInfraForComplete(@WebParam(name = "woDTO") WoDTO woDTO);

  @WebMethod(operationName = "insertWo")
  public ResultDTO insertWo(@WebParam(name = "woDTO") WoDTO woDTO);

  @WebMethod(operationName = "updateHelpFromSPM")
  public ResultDTO updateHelpFromSPM(@WebParam(name = "woCode") String woCode,
      @WebParam(name = "description") String description, @WebParam(name = "result") Long result);

  @WebMethod(operationName = "updateWoInfo")
  public ResultDTO updateWoInfo(@WebParam(name = "WoDTO") WoDTO woDTO);

  @WebMethod(operationName = "searchWoKpiCDBR")
  public List<SearchWoKpiCDBRForm> searchWoKpiCDBR(@WebParam(name = "startTime") String startTime,
      @WebParam(name = "endTime") String endTime);

  @WebMethod(operationName = "createWoTKTU")
  public ResultDTO createWoTKTU(@WebParam(name = "createWoDto") WoDTO createWoDto);

  @WebMethod(operationName = "getSequenseWo")
  public List<String> getSequenseWo(@WebParam(name = "sequenseName") String seqName,
      @WebParam(name = "Size") int... size);

  @WebMethod(operationName = "updatePendingWo")
  public ResultDTO updatePendingWo(@WebParam(name = "woCode") String woCode,
      @WebParam(name = "endPendingTime") String endPendingTime,
      @WebParam(name = "user") String user,
      @WebParam(name = "comment") String comment,
      @WebParam(name = "system") String system);

  @WebMethod(operationName = "updateWoForSPM")
  public ResultDTO updateWoForSPM(@WebParam(name = "woDTO") WoDTO woDTO);

  @WebMethod(operationName = "getListWoForAccount")
  public List<WoDTO> getListWoForAccount(@WebParam(name = "lstAccount") List<String> lstAccount);

  @WebMethod(operationName = "getListWoSupportInfo")
  public List<CfgSupportForm> getListWoSupportInfo(@WebParam(name = "woCode") String woCode);

  @WebMethod(operationName = "changeStatusWo")
  public ResultDTO changeStatusWo(@WebParam(name = "updateForm") WoUpdateStatusForm updateForm);

  @WebMethod(operationName = "createWoFollowNode")
  public ResultDTO createWoFollowNode(@WebParam(name = "createWoDto") WoDTO createWoDto,
      @WebParam(name = "listNode") List<String> listNode);

  @WebMethod(operationName = "getNationFromUnitId")
  public String getNationFromUnitId(@WebParam(name = "unitId") Long unitId);

  @WebMethod(operationName = "callIPCC")
  public ResultDTO callIPCC(@WebParam(name = "woId") String woId,
      @WebParam(name = "userCall") String userCall);

  @WebMethod(operationName = "getWoDetail")
  public ObjResponse getWoDetail(@WebParam(name = "woId") String woId,
      @WebParam(name = "userId") String userId);

  @WebMethod(operationName = "getListReasonOverdue")
  public List<CompCause> getListReasonOverdue(@WebParam(name = "parentId") Long parentId,
      @WebParam(name = "nationCode") String nationCode);

  @WebMethod(operationName = "getListWoByWoType")
  public List<WoDTOSearch> getListWoByWoType(@WebParam(name = "woTypeCode") String woTypeCode,
      @WebParam(name = "createTimeFrom") String createTimeFrom,
      @WebParam(name = "createTimeTo") String createTimeTo);

  @WebMethod(operationName = "getListWOByUsers")
  public List<WoDTOSearch> getListWOByUsers(@WebParam(name = "userId") String userId,
      @WebParam(name = "summaryStatus") String summaryStatus,
      @WebParam(name = "isDetail") Integer isDetail,
      @WebParam(name = "woDTO") WoDTOSearch woDTO,
      @WebParam(name = "start") int start,
      @WebParam(name = "count") int count);

  @WebMethod(operationName = "pendingWo")
  public ResultDTO pendingWo(@WebParam(name = "woCode") String woCode,
      @WebParam(name = "endPendingTime") String endPendingTime,
      @WebParam(name = "user") String user,
      @WebParam(name = "system") String system,
      @WebParam(name = "reasonName") String reasonName,
      @WebParam(name = "reasonId") String reasonId,
      @WebParam(name = "customer") String customer,
      @WebParam(name = "phone") String phone);

  @WebMethod(operationName = "checkProblemSignalForAccount")
  public String checkProblemSignalForAccount(
      @WebParam(name = "subscriptionAccount") String subscriptionAccount,
      @WebParam(name = "workId") String workId, @WebParam(name = "sysId") String sysId);

  @WebMethod(operationName = "updateFileForWo")
  public ResultDTO updateFileForWo(@WebParam(name = "woDTO") WoDTO woDTO);

  @WebMethod(operationName = "approveWo")
  public ResultDTO approveWo(@WebParam(name = "username") String username,
      @WebParam(name = "woId") String woId, @WebParam(name = "comment") String comment,
      @WebParam(name = "action") String action, @WebParam(name = "ftName") String ftName);

  @WebMethod(operationName = "insertWoWorklog")
  public ResultDTO insertWoWorklog(@WebParam(name = "woHistoryDTO") WoWorklogDTO woWorklogDTO);

  @WebMethod(operationName = "acceptWo")
  public ResultDTO acceptWo(@WebParam(name = "username") String username,
      @WebParam(name = "woId") String woId, @WebParam(name = "comment") String comment);

  @WebMethod(operationName = "createWo")
  public ResultDTO createWo(@WebParam(name = "createWoDto") WoDTO createWoDto);

  @WebMethod(operationName = "getListCd")
  public List<UsersDTO> getListCd(@WebParam(name = "cdGroupId") Long cdGroupId);

  @WebMethod(operationName = "updateStatus")
  public ResultDTO updateStatus(@WebParam(name = "username") String username,
      @WebParam(name = "woId") String woId, @WebParam(name = "status") String status,
      @WebParam(name = "comment") String comment);

  @WebMethod(operationName = "updateDescriptionWoSPM")
  public ResultDTO updateDescriptionWoSPM(@WebParam(name = "woDTO") WoDTO woDTO);

  @WebMethod(operationName = "completeWorkHelp")
  public ResultDTO completeWorkHelp(@WebParam(name = "woCode") String woCode,
      @WebParam(name = "userName") String userName,
      @WebParam(name = "worklog") String worklog, @WebParam(name = "reasonCcId") String reasonCcId);

  @WebMethod(operationName = "getFileFromWo")
  public List<ObjFile> getFileFromWo(@WebParam(name = "woId") String woId,
      @WebParam(name = "lstFileName") List<String> lstFileName);

  @WebMethod(operationName = "getWOSummaryInfobyUser")
  public List<ResultDTO> getWOSummaryInfobyUser(@WebParam(name = "userId") String userId);

  @WebMethod(operationName = "rejectWo")
  public ResultDTO rejectWo(@WebParam(name = "username") String username,
      @WebParam(name = "woId") String woId, @WebParam(name = "comment") String comment);

  @WebMethod(operationName = "updateWo")
  public String updateWo(@WebParam(name = "woDTO") WoDTO woDTO) throws IOException, ParseException;

  @WebMethod(operationName = "closeWoForSPM")
  public ResultDTO closeWoForSPM(@WebParam(name = "lstWo") List<Wo> lstWo,
      @WebParam(name = "system") String system, @WebParam(name = "user") String user,
      @WebParam(name = "reasonLevel3Id") Long reasonLevel3Id);

  @WebMethod(operationName = "findWoById")
  public WoDTO findWoById(@WebParam(name = "woDTOId") Long id);

  @WebMethod(operationName = "closeWo")
  public ResultDTO closeWo(@WebParam(name = "listCode") List<String> listCode,
      @WebParam(name = "system") String system);

  @WebMethod(operationName = "deleteWOForRollback")
  public ResultDTO deleteWOForRollback(@WebParam(name = "woCode") String woCode,
      @WebParam(name = "reason") String reason, @WebParam(name = "system") String system);

  @WebMethod(operationName = "insertWoForSPM")
  public ResultDTO insertWoForSPM(@WebParam(name = "woDTO") WoDTO woDTO);

  @WebMethod(operationName = "getConfigProperty")
  public String getConfigProperty();

  @WebMethod
  public ResultDTO callIPCCWithName(@WebParam(name = "woId") String woId,
      @WebParam(name = "userId") Long userId, @WebParam(name = "userCall") String userCall,
      @WebParam(name = "fileAudioName") String fileAudioName);

  @WebMethod(operationName = "updateMopInfo")
  public ResultDTO updateMopInfo(@WebParam(name = "woCode") String woCode,
      @WebParam(name = "result") String result, @WebParam(name = "mopId") String mopId,
      @WebParam(name = "type") Long type);

  @WebMethod(operationName = "createWoForOtherSystem")
  public ResultDTO createWoForOtherSystem(@WebParam(name = "createWoDto") WoDTO createWoDto);

  @WebMethod(operationName = "getInfoAmiOne")
  public List<AmiOneForm> getInfoAmiOne(@WebParam(name = "amiOneId") List<String> lstAmiOneId);

  @WebMethod(operationName = "dispatchWo")
  public ResultDTO dispatchWo(@WebParam(name = "username") String username,
      @WebParam(name = "ftname") String ftName, @WebParam(name = "woId") String woId,
      @WebParam(name = "comment") String comment);

  @WebMethod(operationName = "updateResultTestFromSAP")
  public ResultDTO updateResultTestFromSAP(@WebParam(name = "woDTO") WoDTO woDTO);
}
