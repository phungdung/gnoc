package com.viettel.gnoc.incident.service;

import com.viettel.bccs2.CauseDTO;
import com.viettel.bccs2.TroubleNetworkSolutionDTO;
import com.viettel.gnoc.commons.dto.ConcaveDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.CommonDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "TroublesServiceForCC")
public interface TroublesServiceForCC {

  @WebMethod(operationName = "onRollBackTroubleForCC")
  public List<ResultDTO> onRollBackTroubleForCC(
      @WebParam(name = "lstComplaint") List<CommonDTO> lstComplaint);

  @WebMethod(operationName = "onSearchCountForCC")
  public int onSearchCountForCC(@WebParam(name = "troublesDTO") TroublesDTO troublesDTO,
      @WebParam(name = "lstCreateUnitIdByCC") List<String> lstCreateUnitIdByCC,
      @WebParam(name = "lstComplaintTypeId") List<String> lstComplaintTypeId,
      @WebParam(name = "lstComplaintParentId") List<String> lstComplaintParentId,
      @WebParam(name = "lstTroubleCode") List<String> lstTroubleCode
  );

  @WebMethod(operationName = "onSearchForCC")
  public List<TroublesDTO> onSearchForCC(@WebParam(name = "troublesDTO") TroublesDTO troublesDTO,
      @WebParam(name = "lstCreateUnitIdByCC") List<String> lstCreateUnitIdByCC,
      @WebParam(name = "lstComplaintTypeId") List<String> lstComplaintTypeId,
      @WebParam(name = "lstComplaintParentId") List<String> lstComplaintParentId,
      @WebParam(name = "lstTroubleCode") List<String> lstTroubleCode,
      @WebParam(name = "startRow") Integer startRow,
      @WebParam(name = "pageLength") Integer pageLength
  );

  @WebMethod(operationName = "getTroubleInfoForCC")
  public ResultDTO getTroubleInfoForCC(@WebParam(name = "troublesDTO") TroublesDTO troublesDTO);

  @WebMethod(operationName = "onInsertTroubleForCC")
  public ResultDTO onInsertTroubleForCC(@WebParam(name = "troublesDTO") TroublesDTO troublesDTO,
      @WebParam(name = "arrFileName") String[] arrFileName,
      @WebParam(name = "arrFileData") byte[][] arrFileData);

  @WebMethod(operationName = "findTroublesById")
  public TroublesDTO findTroublesById(Long id);

  @WebMethod(operationName = "getSequenseTroubles")
  public List<String> getSequenseTroubles(String seqName, int... size);

  @WebMethod(operationName = "reassignTicketForCC")
  public ResultDTO reassignTicketForCC(@WebParam(name = "troublesDTO") TroublesDTO troublesDTO,
      @WebParam(name = "arrFileName") String[] arrFileName,
      @WebParam(name = "arrFileData") byte[][] arrFileData);

  @WebMethod(operationName = "getListTroubleActionLog")
  public List<TroubleActionLogsDTO> getListTroubleActionLog(
      @WebParam(name = "troubleCode") String troubleCode);

  @WebMethod(operationName = "getListWorkLog")
  public List<TroubleWorklogDTO> getListWorkLog(@WebParam(name = "troubleCode") String troubleCode);

  @WebMethod(operationName = "getConcaveByTicket")
  public String getConcaveByTicket(@WebParam(name = "troubleCode") String troubleCode);

  @WebMethod(operationName = "sendTicketToTKTU")
  public ResultDTO sendTicketToTKTU(@WebParam(name = "tForm") TroublesDTO tForm);

  @WebMethod(operationName = "getConcaveByCellAndLocation")
  public List<ConcaveDTO> getConcaveByCellAndLocation(
      @WebParam(name = "lstCell") List<String> lstCell,
      @WebParam(name = "lng") String lng,
      @WebParam(name = "lat") String lat);

  @WebMethod(operationName = "getListUnitByTrouble")
  public List<UnitDTO> getListUnitByTrouble(@WebParam(name = "troubleCode") String troubleCode);

  @WebMethod(operationName = "onUpdateTroubleFromTKTU")
  public ResultDTO onUpdateTroubleFromTKTU(@WebParam(name = "troublesDTO") TroublesDTO troublesDTO);

  @WebMethod(operationName = "onUpdateTroubleFromWo")
  public ResultDTO onUpdateTroubleFromWo(@WebParam(name = "troublesDTO") TroublesDTO troublesDTO);

  @WebMethod(operationName = "onUpdateTroubleCC")
  public ResultDTO onUpdateTroubleCC(@WebParam(name = "troublesDTO") TroublesDTO troublesDTO);

  @WebMethod(operationName = "getListWoLog")
  public List<WoHistoryDTO> getListWoLog(@WebParam(name = "troubleCode") String troubleCode);

  @WebMethod
  public List<TroubleNetworkSolutionDTO> getGroupSolutionForCC3(
      @WebParam(name = "nocDTO") CfgServerNocDTO nocDTO);

  public List<CauseDTO> getCompCauseDTOForCC3(@WebParam(name = "parentId") String parentId,
      @WebParam(name = "serviceTypeId") String serviceTypeId,
      @WebParam(name = "probGroupId") String probGroupId,
      @WebParam(name = "nocDTO") CfgServerNocDTO nocDTO);
}
